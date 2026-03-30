package fr.esgi.soheil.kevin.domain.model;

public class AgeRating {

    private Long   id;
    private String label;
    private String colorRgb; 

    public AgeRating() {}

    public Long   getId()               { return id; }
    public void   setId(Long id)        { this.id = id; }
    public String getLabel()            { return label; }
    public void   setLabel(String l)    { this.label = l; }
    public String getColorRgb()         { return colorRgb; }
    public void   setColorRgb(String c) { this.colorRgb = c; }
}