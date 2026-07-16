package com.zerbitzaria.erabiltzailea.entity;

import jakarta.persistence.*;
import java.time.Instant;

import com.zerbitzaria.common.enums.ErabiltzaileMota;

@Entity
@Table(
  name = "erabiltzailea",
  uniqueConstraints = @UniqueConstraint(name="uk_erabiltzailea_emaila", columnNames="emaila")
)
public class Erabiltzailea {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 80)
  private String izena;

  @Column(nullable = false, length = 120)
  private String emaila;

  @Column(nullable = false, name="pasahitza_hash", length = 100)
  private String pasahitzaHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ErabiltzaileMota mota = ErabiltzaileMota.BEZEROA;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

    @Column(nullable = false)
    private boolean aktibo = true;

  @PrePersist
  public void prePersist() { this.createdAt = Instant.now(); }

  //Getter eta Setterrak

  public Integer getId() {
	return id;
  }

  public void setId(Integer id) {
	this.id = id;
  }

  public String getIzena() {
	return izena;
  }

  public void setIzena(String izena) {
	this.izena = izena;
  }

  public String getEmaila() {
	return emaila;
  }

  public void setEmaila(String emaila) {
	this.emaila = emaila;
  }

  public String getPasahitzaHash() {
	return pasahitzaHash;
  }

  public void setPasahitzaHash(String pasahitzaHash) {
	this.pasahitzaHash = pasahitzaHash;
  }

  public void setMota(ErabiltzaileMota mota) {
	this.mota = mota;
  }

  public ErabiltzaileMota getMota() { return mota; }

  public Instant getCreatedAt() {
	return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
	this.createdAt = createdAt;
  }

    public boolean isAktibo() { return aktibo; }
    public void setAktibo(boolean aktibo) { this.aktibo = aktibo; }
  
  
}
