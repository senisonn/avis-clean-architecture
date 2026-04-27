package fr.esgi.soheil.kevin.application.port.out;

import fr.esgi.soheil.kevin.domain.model.Publisher;
import java.util.List;
import java.util.Optional;

public interface PublisherRepository {
    List<Publisher>     findAll();
    Optional<Publisher> findById(Long id);
    Publisher           save(Publisher publisher);
    void                deleteById(Long id);
    boolean             existsById(Long id);
}