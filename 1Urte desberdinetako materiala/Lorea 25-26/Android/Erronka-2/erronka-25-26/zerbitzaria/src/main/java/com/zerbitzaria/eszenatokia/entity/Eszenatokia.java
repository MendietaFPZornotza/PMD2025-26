package com.zerbitzaria.eszenatokia.entity;

import com.zerbitzaria.ekitaldia.entity.Ekitaldia;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eszenatokia")
public class Eszenatokia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String izena;

    private String lekua;

    @Column(nullable = false)
    private Integer aforoa;

    private String emaila;

    private String telefonoa;

    @OneToMany(mappedBy = "eszenatokia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ekitaldia> ekitaldiak = new ArrayList<>();

    public Eszenatokia() {}

    public Eszenatokia(String izena, String lekua, Integer aforoa, String emaila, String telefonoa) {
        this.izena = izena;
        this.lekua = lekua;
        this.aforoa = aforoa;
        this.emaila = emaila;
        this.telefonoa = telefonoa;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public String getLekua() { return lekua; }
    public void setLekua(String lekua) { this.lekua = lekua; }

    public Integer getAforoa() { return aforoa; }
    public void setAforoa(Integer aforoa) { this.aforoa = aforoa; }

    public String getEmaila() { return emaila; }
    public void setEmaila(String emaila) { this.emaila = emaila; }

    public String getTelefonoa() { return telefonoa; }
    public void setTelefonoa(String telefonoa) { this.telefonoa = telefonoa; }

    public List<Ekitaldia> getEkitaldiak() { return ekitaldiak; }
    public void setEkitaldiak(List<Ekitaldia> ekitaldiak) { this.ekitaldiak = ekitaldiak; }
}