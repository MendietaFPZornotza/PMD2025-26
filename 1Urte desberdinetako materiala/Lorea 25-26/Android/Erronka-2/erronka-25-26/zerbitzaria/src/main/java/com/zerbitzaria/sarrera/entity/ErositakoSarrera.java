package com.zerbitzaria.sarrera.entity;

import com.zerbitzaria.ekitaldia.entity.Ekitaldia;
import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.zerbitzaria.common.enums.SarreraEgoera;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "erositako_sarrera",
        indexes = {
                @Index(name="idx_sarrera_ekitaldia_egoera", columnList="ekitaldia_id,egoera"),
                @Index(name="idx_sarrera_user", columnList="erabiltzailea_id"),
                @Index(name="idx_sarrera_deskarga", columnList="deskarga_kodea")
        },
        uniqueConstraints = {
                @UniqueConstraint(name="uk_sarrera_kodea", columnNames="kodea"),
                @UniqueConstraint(name="uk_ekitaldia_fila_eser", columnNames={"ekitaldia_id","fila","eserlekua"})
        }
)
public class ErositakoSarrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "deskarga_kodea", nullable = false)
    private String deskargaKodea;

    @Column(nullable = false)
    private String kodea;

    @Column(nullable = false, length = 2)
    private Integer fila;

    @Column(nullable = false, length = 2)
    private Integer eserlekua;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "erabiltzailea_id", nullable = false)
    private Erabiltzailea erabiltzailea;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ekitaldia_id", nullable = false)
    private Ekitaldia ekitaldia;

    private LocalDateTime erosketarenData;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SarreraEgoera egoera = SarreraEgoera.BALIOZKOA;

    private LocalDateTime erabilitaNoiz;

    private String qrPath;

    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        if (erosketarenData == null) erosketarenData = LocalDateTime.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }


    //Getter eta Setterrak

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKodea() {
        return kodea;
    }

    public void setKodea(String kodea) {
        this.kodea = kodea;
    }

    public String getDeskargaKodea() {
        return deskargaKodea;
    }

    public void setDeskarga_kodea(String deskarga_kodea) {
        this.deskargaKodea = deskarga_kodea;
    }

    public Integer getFila() {
        return fila;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }

    public Integer getEserlekua() {
        return eserlekua;
    }

    public void setEserlekua(Integer eserlekua) {
        this.eserlekua = eserlekua;
    }

    public Erabiltzailea getErabiltzailea() {
        return erabiltzailea;
    }

    public void setErabiltzailea(Erabiltzailea erabiltzailea) {
        this.erabiltzailea = erabiltzailea;
    }

    public Ekitaldia getEkitaldia() {
        return ekitaldia;
    }

    public void setEkitaldia(Ekitaldia ekitaldia) {
        this.ekitaldia = ekitaldia;
    }

    public LocalDateTime getErosketarenData() {
        return erosketarenData;
    }

    public void setErosketarenData(LocalDateTime erosketarenData) {
        this.erosketarenData = erosketarenData;
    }

    public SarreraEgoera getEgoera() {
        return egoera;
    }

    public void setEgoera(SarreraEgoera egoera) {
        this.egoera = egoera;
    }

    public LocalDateTime getErabilitaNoiz() {
        return erabilitaNoiz;
    }

    public void setErabilitaNoiz(LocalDateTime erabilitaNoiz) {
        this.erabilitaNoiz = erabilitaNoiz;
    }

    public String getQrPath() {
        return qrPath;
    }

    public void setQrPath(String qrPath) {
        this.qrPath = qrPath;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

