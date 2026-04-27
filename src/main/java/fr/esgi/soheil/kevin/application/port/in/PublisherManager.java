package fr.esgi.soheil.kevin.application.port.in;

import fr.esgi.soheil.kevin.domain.model.Publisher;
import java.util.List;

public interface PublisherManager {
    List<Publisher> getAll();
    Publisher       add(String name);
    void            delete(Long id);
}