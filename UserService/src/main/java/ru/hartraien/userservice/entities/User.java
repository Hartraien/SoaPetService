package ru.hartraien.userservice.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_user_entities")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userentity_seq")
    private Long id;


    private String username;

    private String password;


    private int failedLoginAttempts;

    private boolean locked;

    private LocalDateTime unlockTime;

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


    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(LocalDateTime lockedTime) {
        this.unlockTime = lockedTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", failedLoginAttempts=" + failedLoginAttempts +
                ", locked=" + locked +
                ", unlockTime=" + unlockTime +
                '}';
    }
}
