package fr.esgi.soheil.kevin.domain.model;

import java.time.LocalDate;

public class Player {

    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDate birthDate;
    private Avatar avatar;

    public Player() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String u) {
        this.username = u;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String p) {
        this.password = p;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate b) {
        this.birthDate = b;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar a) {
        this.avatar = a;
    }
}

