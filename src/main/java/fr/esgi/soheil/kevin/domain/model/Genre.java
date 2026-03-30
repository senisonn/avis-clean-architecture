package fr.esgi.soheil.kevin.domain.model;

public class Genre {

    private Long   id;
    private String name;

    public Genre() {}

    public Long   getId()              { return id; }
    public void   setId(Long id)       { this.id = id; }
    public String getName()            { return name; }
    public void   setName(String n)    { this.name = n; }
}