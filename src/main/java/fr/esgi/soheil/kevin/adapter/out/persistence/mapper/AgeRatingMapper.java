package fr.esgi.soheil.kevin.adapter.out.persistence.mapper;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.AgeRatingEntity;
import fr.esgi.soheil.kevin.domain.model.AgeRating;
import org.springframework.stereotype.Component;

@Component
public class AgeRatingMapper {

    public AgeRating toDomain(AgeRatingEntity e) {
        if (e == null) return null;
        AgeRating a = new AgeRating();
        a.setId(e.getId());
        a.setLabel(e.getLabel());
        a.setColorRgb(e.getColorRgb());
        return a;
    }

    public AgeRatingEntity toEntity(AgeRating a) {
        if (a == null) return null;
        return AgeRatingEntity.builder()
                .id(a.getId())
                .label(a.getLabel())
                .colorRgb(a.getColorRgb())
                .build();
    }
}

