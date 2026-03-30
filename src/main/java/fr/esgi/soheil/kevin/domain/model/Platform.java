package fr.esgi.soheil.kevin.domain.model;

import java.time.LocalDate;

public class Platform {

    private Long      id;
    private String    name;
    private LocalDate releaseDate;

    public Platform() {}

    public Long      getId()                  { return id; }
    public void      setId(Long id)           { this.id = id; }
    public String    getName()                { return name; }
    public void      setName(String n)        { this.name = n; }
    public LocalDate getReleaseDate()         { return releaseDate; }
    public void      setReleaseDate(LocalDate d) { this.releaseDate = d; }
}