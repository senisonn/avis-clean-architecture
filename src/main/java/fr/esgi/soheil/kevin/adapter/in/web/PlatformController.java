package fr.esgi.soheil.kevin.adapter.in.web;

import fr.esgi.soheil.kevin.application.port.in.PlatformManager;
import fr.esgi.soheil.kevin.domain.model.Platform;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/platforms")
@RequiredArgsConstructor
public class PlatformController {

    private final PlatformManager platformManager;

    @GetMapping
    public ResponseEntity<List<Platform>> getAll() {
        return ResponseEntity.ok(platformManager.getAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Platform> create(
            @RequestParam @NotBlank String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate releaseDate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(platformManager.add(name, releaseDate));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        platformManager.delete(id);
        return ResponseEntity.noContent().build();
    }
}