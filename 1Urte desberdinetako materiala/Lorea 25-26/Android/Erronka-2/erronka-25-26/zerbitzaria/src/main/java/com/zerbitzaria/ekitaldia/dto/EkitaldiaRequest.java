package com.zerbitzaria.ekitaldia.dto;

import com.zerbitzaria.common.enums.EkitaldiEgoera;
import com.zerbitzaria.common.enums.EkitaldiMota;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EkitaldiaRequest {
    public String izenburua;
    public String sinopsia;
    public String generoa;
    public BigDecimal prezioa;
    public EkitaldiMota mota;
    public EkitaldiEgoera egoera;
    public boolean aktibo = true;
    private String argazkia;

    public LocalDateTime hasiera;
    public LocalDateTime amaiera;

    public int eszenatokiaId;
}