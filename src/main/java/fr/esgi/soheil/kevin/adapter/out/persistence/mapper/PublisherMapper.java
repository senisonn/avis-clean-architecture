package fr.esgi.soheil.kevin.adapter.out.persistence.mapper;

import fr.esgi.soheil.kevin.adapter.out.persistence.entity.PublisherEntity;
import fr.esgi.soheil.kevin.domain.model.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {

    public Publisher toDomain(PublisherEntity e) {
        if (e == null) return null;
        Publisher p = new Publisher();
        p.setId(e.getId());
        p.setName(e.getName());
        return p;
    }

    public PublisherEntity toEntity(Publisher p) {
        if (p == null) return null;
        return PublisherEntity.builder()
                .id(p.getId())
                .name(p.getName())
                .build();
    }
}

