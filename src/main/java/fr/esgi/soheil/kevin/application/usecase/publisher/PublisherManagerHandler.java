package fr.esgi.soheil.kevin.application.usecase.publisher;

import fr.esgi.soheil.kevin.application.port.in.PublisherManager;
import fr.esgi.soheil.kevin.application.port.out.PublisherRepository;
import fr.esgi.soheil.kevin.domain.model.Publisher;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class PublisherManagerHandler implements PublisherManager {

    private final PublisherRepository publisherRepository;

    @Override
    public List<Publisher> getAll() {
        return publisherRepository.findAll();
    }

    @Override
    public Publisher add(String name) {
        Publisher publisher = new Publisher();
        publisher.setName(name);
        return publisherRepository.save(publisher);
    }

    @Override
    public void delete(Long id) {
        if (!publisherRepository.existsById(id))
            throw new IllegalArgumentException("Publisher not found: " + id);
        publisherRepository.deleteById(id);
    }
}