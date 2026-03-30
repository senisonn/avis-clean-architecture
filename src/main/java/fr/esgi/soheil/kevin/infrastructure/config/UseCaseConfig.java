package fr.esgi.soheil.kevin.infrastructure.config;

import fr.esgi.soheil.kevin.application.port.out.*;
import fr.esgi.soheil.kevin.application.usecase.game.*;
import fr.esgi.soheil.kevin.application.usecase.player.*;
import fr.esgi.soheil.kevin.application.usecase.review.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The only place in the codebase where ports are wired to handlers.
 * If you swap an implementation, only this file changes.
 */
@Configuration
public class UseCaseConfig {

    // ── Game ──────────────────────────────────────────────────

    @Bean
    public GameFinderHandler gameFinder(GameRepository gameRepository) {
        return new GameFinderHandler(gameRepository);
    }

    @Bean
    public GameCreatorHandler gameCreator(GameRepository gameRepository) {
        return new GameCreatorHandler(gameRepository);
    }

    @Bean
    public GameUpdaterHandler gameUpdater(GameRepository gameRepository) {
        return new GameUpdaterHandler(gameRepository);
    }

    @Bean
    public GameDeleterHandler gameDeleter(GameRepository gameRepository) {
        return new GameDeleterHandler(gameRepository);
    }

    // ── Review ────────────────────────────────────────────────

    @Bean
    public ReviewFinderHandler reviewFinder(ReviewRepository reviewRepository) {
        return new ReviewFinderHandler(reviewRepository);
    }

    @Bean
    public ReviewSubmitterHandler reviewSubmitter(
            ReviewRepository reviewRepository,
            GameRepository   gameRepository,
            PlayerRepository playerRepository) {
        return new ReviewSubmitterHandler(reviewRepository, gameRepository, playerRepository);
    }

    @Bean
    public ReviewDeleterHandler reviewDeleter(ReviewRepository reviewRepository) {
        return new ReviewDeleterHandler(reviewRepository);
    }

    @Bean
    public ReviewApproverHandler reviewApprover(
            ReviewRepository    reviewRepository,
            ModeratorRepository moderatorRepository) {
        return new ReviewApproverHandler(reviewRepository, moderatorRepository);
    }

    @Bean
    public ReviewRejectorHandler reviewRejector(
            ReviewRepository    reviewRepository,
            ModeratorRepository moderatorRepository) {
        return new ReviewRejectorHandler(reviewRepository, moderatorRepository);
    }

    @Bean
    public PendingReviewFinderHandler pendingReviewFinder(
            ReviewRepository reviewRepository) {
        return new PendingReviewFinderHandler(reviewRepository);
    }

    // ── Player ────────────────────────────────────────────────

    @Bean
    public PlayerRegistrarHandler playerRegistrar(
            PlayerRepository playerRepository,
            PasswordEncoder  passwordEncoder,
            TokenManager     tokenManager) {
        return new PlayerRegistrarHandler(playerRepository, passwordEncoder, tokenManager);
    }

    @Bean
    public PlayerAuthenticatorHandler playerAuthenticator(
            PlayerRepository playerRepository,
            PasswordEncoder  passwordEncoder,
            TokenManager     tokenManager) {
        return new PlayerAuthenticatorHandler(playerRepository, passwordEncoder, tokenManager);
    }
}