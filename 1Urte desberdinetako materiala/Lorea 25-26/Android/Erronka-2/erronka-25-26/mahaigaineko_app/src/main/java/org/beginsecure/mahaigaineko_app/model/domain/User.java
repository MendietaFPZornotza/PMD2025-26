package org.beginsecure.mahaigaineko_app.model.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private Integer id;
    private LocalDateTime createdAt;
    private String emaila;
    private String izena;
    private String mota;

    public User() {}

    public User(Integer id, LocalDateTime createdAt, String emaila, String izena, String mota) {
        this.id = id;
        this.createdAt = createdAt;
        this.emaila = emaila;
        this.izena = izena;
        this.mota = mota;
    }

    public User(LocalDateTime createdAt, String emaila, String izena, String mota) {
        this.createdAt = createdAt;
        this.emaila = emaila;
        this.izena = izena;
        this.mota = mota;
    }

    // Getter-ak (TableView-ko PropertyValueFactory-rentzat)
    public Integer getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getEmaila() { return emaila; }
    public String getIzena() { return izena; }
    public String getMota() { return mota; }

    // Setter-ak
    public void setId(Integer id) { this.id = id; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setEmaila(String emaila) { this.emaila = emaila; }
    public void setIzena(String izena) { this.izena = izena; }
    public void setMota(String mota) { this.mota = mota; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(id, that.id) && Objects.equals(emaila, that.emaila);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, emaila);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", emaila='" + emaila + '\'' +
                ", izena='" + izena + '\'' +
                ", mota='" + mota + '\'' +
                '}';
    }
}