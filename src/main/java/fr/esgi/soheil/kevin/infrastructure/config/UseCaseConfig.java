package fr.esgi.soheil.kevin.infrastructure.config;

import fr.esgi.soheil.kevin.application.port.in.*;
import fr.esgi.soheil.kevin.application.port.out.*;
import fr.esgi.soheil.kevin.application.usecase.game.*;
import fr.esgi.soheil.kevin.application.usecase.genre.GenreManagerHandler;
import fr.esgi.soheil.kevin.application.usecase.moderator.*;
import fr.esgi.soheil.kevin.application.usecase.platform.PlatformManagerHandler;
import fr.esgi.soheil.kevin.application.usecase.player.*;
import fr.esgi.soheil.kevin.application.usecase.publisher.PublisherManagerHandler;
import fr.esgi.soheil.kevin.application.usecase.review.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The only place in the codebase where ports are wired to handlers.
 * If you swap an implementation, only this file changes.
 */
@Configuration
public class UseCaseConfig {

    // ── Game ──────────────────────────────────────────────────

    @Bean
    public GameFinder gameFinder(GameRepository gameRepository) {
        return new GameFinderHandler(gameRepository);
    }

    @Bean
    public GameCreator gameCreator(GameRepository gameRepository) {
        return new GameCreatorHandler(gameRepository);
    }

    @Bean
    public GameUpdater gameUpdater(GameRepository gameRepository,
                                   PublisherRepository publisherRepository,
                                   GenreRepository genreRepository) {
        return new GameUpdaterHandler(gameRepository, publisherRepository, genreRepository);
    }

    @Bean
    public GameDeleter gameDeleter(GameRepository gameRepository) {
        return new GameDeleterHandler(gameRepository);
    }

    // ── Review ────────────────────────────────────────────────

    @Bean
    public ReviewFinder reviewFinder(ReviewRepository reviewRepository) {
        return new ReviewFinderHandler(reviewRepository);
    }

    @Bean
    public ReviewSubmitter reviewSubmitter(
            ReviewRepository reviewRepository,
            GameRepository   gameRepository,
            PlayerRepository playerRepository) {
        return new ReviewSubmitterHandler(reviewRepository, gameRepository, playerRepository);
    }

    @Bean
    public ReviewDeleter reviewDeleter(ReviewRepository reviewRepository) {
        return new ReviewDeleterHandler(reviewRepository);
    }

    @Bean
    public ReviewApprover reviewApprover(
            ReviewRepository    reviewRepository,
            ModeratorRepository moderatorRepository) {
        return new ReviewApproverHandler(reviewRepository, moderatorRepository);
    }

    @Bean
    public ReviewRejector reviewRejector(
            ReviewRepository    reviewRepository,
            ModeratorRepository moderatorRepository) {
        return new ReviewRejectorHandler(reviewRepository, moderatorRepository);
    }

    @Bean
    public PendingReviewFinder pendingReviewFinder(
            ReviewRepository reviewRepository) {
        return new PendingReviewFinderHandler(reviewRepository);
    }

    // ── Genre / Platform / Publisher ──────────────────────────

    @Bean
    public GenreManager genreManager(GenreRepository genreRepository) {
        return new GenreManagerHandler(genreRepository);
    }

    @Bean
    public PlatformManager platformManager(PlatformRepository platformRepository) {
        return new PlatformManagerHandler(platformRepository);
    }

    @Bean
    public PublisherManager publisherManager(PublisherRepository publisherRepository) {
        return new PublisherManagerHandler(publisherRepository);
    }

    // ── Moderator ─────────────────────────────────────────────

    @Bean
    public ModeratorAuthenticator moderatorAuthenticator(
            ModeratorRepository moderatorRepository,
            PasswordHasher       passwordHasher,
            TokenManager         tokenManager) {
        return new ModeratorAuthenticatorHandler(moderatorRepository, passwordHasher, tokenManager);
    }

    // ── Player ────────────────────────────────────────────────

    @Bean
    public PlayerRegistrar playerRegistrar(
            PlayerRepository playerRepository,
            PasswordHasher   passwordHasher,
            TokenManager     tokenManager) {
        return new PlayerRegistrarHandler(playerRepository, passwordHasher, tokenManager);
    }

    @Bean
    public PlayerAuthenticator playerAuthenticator(
            PlayerRepository playerRepository,
            PasswordHasher   passwordHasher,
            TokenManager     tokenManager) {
        return new PlayerAuthenticatorHandler(playerRepository, passwordHasher, tokenManager);
    }
}