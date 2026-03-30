package fr.esgi.soheil.kevin.domain.model;

import java.time.LocalDate;
import java.util.List;

// Pure Java class — no Spring, no JPA, no Lombok
// This class would work without any framework at all
public class Game {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Float price;
    private LocalDate releaseDate;
    private Genre genre;
    private Publisher publisher;
    private AgeRating ageRating;
    private List<Platform> platforms;

    public Game() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        this.description = d;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String i) {
        this.imageUrl = i;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float p) {
        this.price = p;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate d) {
        this.releaseDate = d;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre g) {
        this.genre = g;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher p) {
        this.publisher = p;
    }

    public AgeRating getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(AgeRating a) {
        this.ageRating = a;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> p) {
        this.platforms = p;
    }
}

