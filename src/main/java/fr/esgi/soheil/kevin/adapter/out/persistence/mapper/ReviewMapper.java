package fr.esgi.soheil.kevin.adapter.out.persistence.mapper;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.ReviewEntity;
import fr.esgi.soheil.kevin.adapter.out.persistence.entity.ReviewStatus;
import fr.esgi.soheil.kevin.domain.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final GameMapper      gameMapper;
    private final PlayerMapper    playerMapper;
    private final ModeratorMapper moderatorMapper;

    
    public Review toDomain(ReviewEntity e) {
        if (e == null) return null;
        Review r = new Review();
        r.setId(e.getId());
        r.setContent(e.getContent());
        r.setRating(e.getRating());
        r.setSubmittedAt(e.getSubmittedAt());
        
        r.setStatus(fr.esgi.soheil.kevin.domain.model.ReviewStatus
                .valueOf(e.getStatus().name()));
        r.setGame(gameMapper.toDomain(e.getGame()));
        r.setPlayer(playerMapper.toDomain(e.getPlayer()));
        r.setModerator(moderatorMapper.toDomain(e.getModerator()));
        return r;
    }

    
    public ReviewEntity toEntity(Review r) {
        if (r == null) return null;
        return ReviewEntity.builder()
                .id(r.getId())
                .content(r.getContent())
                .rating(r.getRating())
                .submittedAt(r.getSubmittedAt())
                
                .status(ReviewStatus.valueOf(r.getStatus().name()))
                .game(gameMapper.toEntity(r.getGame()))
                .player(playerMapper.toEntity(r.getPlayer()))
                .moderator(moderatorMapper.toEntity(r.getModerator()))
                .build();
    }
}

