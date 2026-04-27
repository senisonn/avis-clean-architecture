package fr.esgi.soheil.kevin.adapter.in.web;

import fr.esgi.soheil.kevin.application.port.in.GenreManager;
import fr.esgi.soheil.kevin.domain.model.Genre;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreManager genreManager;

    @GetMapping
    public ResponseEntity<List<Genre>> getAll() {
        return ResponseEntity.ok(genreManager.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Genre> create(@RequestParam @NotBlank String name) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreManager.add(name));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genreManager.delete(id);
        return ResponseEntity.noContent().build();
    }
}