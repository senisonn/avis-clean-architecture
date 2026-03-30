package fr.esgi.soheil.kevin.domain.model;

public class Moderator {

    private Long   id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;

    public Moderator() {}

    public Long   getId()                       { return id; }
    public void   setId(Long id)                { this.id = id; }
    public String getUsername()                  { return username; }
    public void   setUsername(String u)          { this.username = u; }
    public String getEmail()                     { return email; }
    public void   setEmail(String e)             { this.email = e; }
    public String getPassword()                  { return password; }
    public void   setPassword(String p)          { this.password = p; }
    public String getPhoneNumber()               { return phoneNumber; }
    public void   setPhoneNumber(String n)       { this.phoneNumber = n; }
}

