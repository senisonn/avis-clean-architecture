package fr.esgi.soheil.kevin.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.esgi.soheil.kevin.adapter.out.persistence.entity.*;
import fr.esgi.soheil.kevin.adapter.out.persistence.repository.*;
import fr.esgi.soheil.kevin.application.port.out.PasswordHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads {@code seed.json} on startup so the app comes up with realistic demo
 * data. Idempotent — skips if any games already exist.
 *
 * Disable with {@code app.seed.enabled=false} in {@code application.properties}.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final GenreJpaRepository     genreRepo;
    private final PlatformJpaRepository  platformRepo;
    private final PublisherJpaRepository publisherRepo;
    private final GameJpaRepository      gameRepo;
    private final PlayerJpaRepository    playerRepo;
    private final ModeratorJpaRepository moderatorRepo;
    private final ReviewJpaRepository    reviewRepo;
    private final AgeRatingJpaRepository ageRatingRepo;
    private final PasswordHasher         passwordHasher;

    @Value("classpath:seed.json")
    private Resource seedResource;

    @Value("${app.seed.enabled:true}")
    private boolean enabled;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (!enabled) {
            log.info("[seed] disabled via app.seed.enabled=false");
            return;
        }
        if (gameRepo.count() > 0) {
            log.info("[seed] data already present — skipping");
            return;
        }

        SeedData seed = readSeed();

        Map<String, AgeRatingEntity> ageRatings = new HashMap<>();
        for (var ar : seed.ageRatings) {
            ageRatings.put(ar.label, ageRatingRepo.save(
                    AgeRatingEntity.builder().label(ar.label).colorRgb(ar.colorRgb).build()));
        }

        Map<String, GenreEntity> genres = new HashMap<>();
        for (var g : seed.genres) {
            genres.put(g.name, genreRepo.save(GenreEntity.builder().name(g.name).build()));
        }

        Map<String, PlatformEntity> platforms = new HashMap<>();
        for (var p : seed.platforms) {
            platforms.put(p.name, platformRepo.save(
                    PlatformEntity.builder()
                            .name(p.name)
                            .releaseDate(parseDate(p.releaseDate))
                            .build()));
        }

        Map<String, PublisherEntity> publishers = new HashMap<>();
        for (var p : seed.publishers) {
            publishers.put(p.name, publisherRepo.save(PublisherEntity.builder().name(p.name).build()));
        }

        Map<String, ModeratorEntity> moderators = new HashMap<>();
        for (var m : seed.moderators) {
            moderators.put(m.username, moderatorRepo.save(
                    ModeratorEntity.builder()
                            .username(m.username)
                            .email(m.email)
                            .password(passwordHasher.hash(m.password))
                            .phoneNumber(m.phoneNumber)
                            .build()));
        }

        Map<String, PlayerEntity> players = new HashMap<>();
        for (var p : seed.players) {
            players.put(p.username, playerRepo.save(
                    PlayerEntity.builder()
                            .username(p.username)
                            .email(p.email)
                            .password(passwordHasher.hash(p.password))
                            .birthDate(parseDate(p.birthDate))
                            .build()));
        }

        Map<String, GameEntity> games = new HashMap<>();
        for (var g : seed.games) {
            List<PlatformEntity> linked = new ArrayList<>();
            if (g.platforms != null) {
                for (String name : g.platforms) {
                    PlatformEntity pe = platforms.get(name);
                    if (pe != null) linked.add(pe);
                }
            }
            GameEntity saved = gameRepo.save(GameEntity.builder()
                    .name(g.name)
                    .description(g.description)
                    .imageUrl(g.imageUrl)
                    .price(g.price)
                    .releaseDate(parseDate(g.releaseDate))
                    .genre(genres.get(g.genre))
                    .publisher(publishers.get(g.publisher))
                    .ageRatingEntity(ageRatings.get(g.ageRating))
                    .platforms(linked)
                    .build());
            games.put(g.name, saved);
        }

        for (var r : seed.reviews) {
            PlayerEntity player = players.get(r.playerUsername);
            GameEntity   game   = games.get(r.gameName);
            if (player == null || game == null) {
                log.warn("[seed] skipping review (player={} or game={} missing)", r.playerUsername, r.gameName);
                continue;
            }
            ModeratorEntity mod = r.moderatorUsername != null ? moderators.get(r.moderatorUsername) : null;
            reviewRepo.save(ReviewEntity.builder()
                    .content(r.content)
                    .rating(r.rating)
                    .submittedAt(parseDateTime(r.submittedAt))
                    .status(r.status != null ? ReviewStatus.valueOf(r.status) : ReviewStatus.PUBLISHED)
                    .player(player)
                    .gameEntity(game)
                    .moderator(mod)
                    .build());
        }

        log.info("[seed] inserted {} games, {} players, {} reviews",
                games.size(), players.size(), seed.reviews.size());
    }

    private SeedData readSeed() throws IOException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try (InputStream in = seedResource.getInputStream()) {
            return mapper.readValue(in, SeedData.class);
        }
    }

    private static LocalDate parseDate(String raw) {
        return raw == null || raw.isBlank() ? null : LocalDate.parse(raw);
    }

    private static LocalDateTime parseDateTime(String raw) {
        return raw == null || raw.isBlank() ? null : LocalDateTime.parse(raw);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class SeedData {
        public List<SeedAgeRating> ageRatings = List.of();
        public List<SeedNamed>     genres     = List.of();
        public List<SeedPlatform>  platforms  = List.of();
        public List<SeedNamed>     publishers = List.of();
        public List<SeedModerator> moderators = List.of();
        public List<SeedPlayer>    players    = List.of();
        public List<SeedGame>      games      = List.of();
        public List<SeedReview>    reviews    = List.of();
    }

    static class SeedAgeRating { public String label; public String colorRgb; }
    static class SeedNamed     { public String name; }
    static class SeedPlatform  { public String name; public String releaseDate; }
    static class SeedModerator {
        public String username; public String email; public String password; public String phoneNumber;
    }
    static class SeedPlayer {
        public String username; public String email; public String password; public String birthDate;
    }
    static class SeedGame {
        public String name; public String description; public String imageUrl;
        public Float price; public String releaseDate;
        public String genre; public String publisher; public String ageRating;
        public List<String> platforms;
    }
    static class SeedReview {
        public String playerUsername; public String gameName;
        public Float  rating; public String content;
        public String submittedAt; public String status;
        public String moderatorUsername;
    }
}
