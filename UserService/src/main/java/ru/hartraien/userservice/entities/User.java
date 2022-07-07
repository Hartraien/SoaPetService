package ru.hartraien.userservice.entities;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "t_user_entities")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userentity_seq")
    private Long id;


    private String username;

    private String password;

    public User() {
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}