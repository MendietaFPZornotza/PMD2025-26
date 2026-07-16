package com.zerbitzaria.ekitaldia.entity;

import com.zerbitzaria.eszenatokia.entity.Eszenatokia;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.zerbitzaria.common.enums.EkitaldiEgoera;
import com.zerbitzaria.common.enums.EkitaldiMota;

import java.time.Instant;

@Entity
@Table(
  name = "ekitaldia",
  indexes = { @Index(name="idx_ekitaldia_eszen", columnList="eszenatokia_id") },
  uniqueConstraints = { @UniqueConstraint(name="uk_ekitaldia_odoo", columnNames="odoo_id") }
)
public class Ekitaldia {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String izenburua;

  @Lob
  private String sinopsia;

  private String generoa;

  @Column(nullable = false, precision = 8, scale = 2)
  private BigDecimal prezioa = BigDecimal.ZERO;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EkitaldiMota mota;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "eszenatokia_id", nullable = false)
  private Eszenatokia eszenatokia;

  @Column(nullable = false)
  private LocalDateTime hasiera;

  @Column(nullable = false)
  private LocalDateTime amaiera;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private boolean aktibo = true;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EkitaldiEgoera egoera = EkitaldiEgoera.ZIRRIBORROA;

  private String argazkia;

  @Column(name="odoo_id", unique = true)
  private Integer odooId;

  @PrePersist
  public void prePersist() {
    this.createdAt = Instant.now();
    validateDates();
  }

  @PreUpdate
  public void preUpdate() {
    validateDates();
  }

  private void validateDates() {
    if (amaiera != null && hasiera != null && !amaiera.isAfter(hasiera)) {
      throw new IllegalArgumentException("amaiera ezin da hasiera baino lehenago edo berdina izan");
    }
  }
  
  public Ekitaldia() {
  }
  
  public Ekitaldia( String izenburua, EkitaldiMota mota, Eszenatokia eszenatokia, LocalDateTime hasiera, LocalDateTime amaiera, BigDecimal prezioa) {
		  this.izenburua = izenburua;
		  this.mota = mota;
		  this.eszenatokia = eszenatokia;
		  this.hasiera = hasiera;
		  this.amaiera = amaiera;
		  this.prezioa = prezioa;
		  this.aktibo = true;
		  this.egoera = EkitaldiEgoera.ZIRRIBORROA;
		}
  
  // Getters and Setters

  public Integer getId() {
	return id;
  }

  public void setId(Integer id) {
	this.id = id;
  }

  public String getIzenburua() {
	return izenburua;
  }

  public void setIzenburua(String izenburua) {
	this.izenburua = izenburua;
  }

  public String getSinopsia() {
	return sinopsia;
  }

  public void setSinopsia(String sinopsia) {
	this.sinopsia = sinopsia;
  }

  public String getGeneroa() {
	return generoa;
  }

  public void setGeneroa(String generoa) {
	this.generoa = generoa;
  }

  public BigDecimal getPrezioa() {
	return prezioa;
  }

  public void setPrezioa(BigDecimal prezioa) {
	this.prezioa = prezioa;
  }

  public EkitaldiMota getMota() {
	return mota;
  }

  public void setMota(EkitaldiMota mota) {
	this.mota = mota;
  }

  public Eszenatokia getEszenatokia() {
	return eszenatokia;
  }

  public void setEszenatokia(Eszenatokia eszenatokia) {
	this.eszenatokia = eszenatokia;
  }

  public LocalDateTime getHasiera() {
	return hasiera;
  }

  public void setHasiera(LocalDateTime hasiera) {
	this.hasiera = hasiera;
  }

  public LocalDateTime getAmaiera() {
	return amaiera;
  }

  public void setAmaiera(LocalDateTime amaiera) {
	this.amaiera = amaiera;
  }

  public Instant getCreatedAt() {
	return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
	this.createdAt = createdAt;
  }

  public boolean isAktibo() {
	return aktibo;
  }

  public void setAktibo(boolean aktibo) {
	this.aktibo = aktibo;
  }

  public EkitaldiEgoera getEgoera() {
	return egoera;
  }

  public void setEgoera(EkitaldiEgoera egoera) {
	this.egoera = egoera;
  }

  public String getArgazkia() {
	return argazkia;
  }

  public void setArgazkia(String argazkia) {
	this.argazkia = argazkia;
  }

  public Integer getOdooId() {
	return odooId;
  }

  public void setOdooId(Integer odooId) {
	this.odooId = odooId;
  }


  
}