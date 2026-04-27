package fr.esgi.soheil.kevin.adapter.in.web;

import fr.esgi.soheil.kevin.application.port.in.PublisherManager;
import fr.esgi.soheil.kevin.domain.model.Publisher;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherManager publisherManager;

    @GetMapping
    public ResponseEntity<List<Publisher>> getAll() {
        return ResponseEntity.ok(publisherManager.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Publisher> create(@RequestParam @NotBlank String name) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherManager.add(name));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        publisherManager.delete(id);
        return ResponseEntity.noContent().build();
    }
}