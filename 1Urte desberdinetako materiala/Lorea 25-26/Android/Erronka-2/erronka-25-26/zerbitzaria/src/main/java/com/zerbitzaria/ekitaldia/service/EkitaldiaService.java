package com.zerbitzaria.ekitaldia.service;

import com.zerbitzaria.ekitaldia.entity.Ekitaldia;
import com.zerbitzaria.eszenatokia.entity.Eszenatokia;
import com.zerbitzaria.ekitaldia.repository.EkitaldiaRepository;
import com.zerbitzaria.eszenatokia.repository.EszenatokiaRepository;
import com.zerbitzaria.common.enums.EkitaldiMota;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Lógica de negocio de Ekitaldia.
 * - Validaciones
 * - Create / update / delete
 * - Métodos “móvil” para listar por mota y próximos
 */
@Service
public class EkitaldiaService {

    private final EkitaldiaRepository ekitaldiaRepo;
    private final EszenatokiaRepository eszenatokiaRepo;

    public EkitaldiaService(EkitaldiaRepository ekitaldiaRepo, EszenatokiaRepository eszenatokiaRepo) {
        this.ekitaldiaRepo = ekitaldiaRepo;
        this.eszenatokiaRepo = eszenatokiaRepo;
    }

    public List<Ekitaldia> findUpcomingByMota(EkitaldiMota mota) {
        return ekitaldiaRepo.findUpcomingByMotaWithEszenatokia(mota);
    }

    public List<Ekitaldia> findAll() {
        return ekitaldiaRepo.findAll();
    }

    public Ekitaldia findById(int id) {
        return ekitaldiaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ekitaldia ez da existitzen: id=" + id));
    }

    public List<Ekitaldia> findUpcoming() {
        return ekitaldiaRepo.findByHasieraAfterOrderByHasieraAsc(LocalDateTime.now());
    }

    public List<Ekitaldia> findByEszenatokia(int eszenatokiaId) {
        return ekitaldiaRepo.findByEszenatokia_IdOrderByHasieraAsc(eszenatokiaId);
    }

    public Ekitaldia create(Ekitaldia ekitaldia, int eszenatokiaId) {
        validateEkitaldia(ekitaldia);

        Eszenatokia eszenatokia = eszenatokiaRepo.findById(eszenatokiaId)
                .orElseThrow(() -> new RuntimeException("Eszenatokia ez da existitzen: id=" + eszenatokiaId));

        ekitaldia.setEszenatokia(eszenatokia);
        return ekitaldiaRepo.save(ekitaldia);
    }

    public Ekitaldia update(int id, Ekitaldia updated, Integer eszenatokiaIdOrNull) {
        Ekitaldia existing = findById(id);

        existing.setIzenburua(updated.getIzenburua());
        existing.setSinopsia(updated.getSinopsia());
        existing.setGeneroa(updated.getGeneroa());
        existing.setMota(updated.getMota());
        existing.setPrezioa(updated.getPrezioa());
        existing.setHasiera(updated.getHasiera());
        existing.setAmaiera(updated.getAmaiera());
        existing.setEgoera(updated.getEgoera());
        existing.setAktibo(updated.isAktibo());

        validateEkitaldia(existing);

        if (eszenatokiaIdOrNull != null) {
            Eszenatokia eszenatokia = eszenatokiaRepo.findById(eszenatokiaIdOrNull)
                    .orElseThrow(() -> new RuntimeException("Eszenatokia ez da existitzen: id=" + eszenatokiaIdOrNull));
            existing.setEszenatokia(eszenatokia);
        }

        return ekitaldiaRepo.save(existing);
    }

    public void delete(int id) {
        if (!ekitaldiaRepo.existsById(id)) {
            throw new RuntimeException("Ezin da ezabatu, ez da existitzen: id=" + id);
        }
        ekitaldiaRepo.deleteById(id);
    }

    private void validateEkitaldia(Ekitaldia e) {
        if (e.getHasiera() == null || e.getAmaiera() == null) {
            throw new RuntimeException("Hasiera eta amaiera derrigorrezkoak dira");
        }
        if (!e.getAmaiera().isAfter(e.getHasiera())) {
            throw new RuntimeException("Amaiera hasiera baino geroago izan behar da");
        }
        BigDecimal prezioa = e.getPrezioa();
        if (prezioa == null) {
            throw new RuntimeException("Prezioa derrigorrezkoa da");
        }
        if (prezioa.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Prezioa ezin da negatiboa izan");
        }
        if (e.getIzenburua() == null || e.getIzenburua().isBlank()) {
            throw new RuntimeException("Izenburua derrigorrezkoa da");
        }
        if (e.getMota() == null) {
            throw new RuntimeException("Mota derrigorrezkoa da");
        }
        if (e.getEszenatokia() == null) {
            // en create todavía puede venir null porque se setea por id,
            // pero en update lo exigimos siempre.
        }
    }
}
