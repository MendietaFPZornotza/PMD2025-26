package com.zerbitzaria.eszenatokia.service;

import com.zerbitzaria.eszenatokia.entity.Eszenatokia;
import com.zerbitzaria.eszenatokia.repository.EszenatokiaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EszenatokiaService {

    private final EszenatokiaRepository repo;

    public EszenatokiaService(EszenatokiaRepository repo) {
        this.repo = repo;
    }

    public List<Eszenatokia> findAll() {
        return repo.findAll();
    }

    public Eszenatokia findById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Eszenatokia ez da existitzen: id=" + id));
    }

    public Eszenatokia create(Eszenatokia eszenatokia) {
        validate(eszenatokia);
        eszenatokia.setEmaila(trimToNull(eszenatokia.getEmaila()));
        eszenatokia.setTelefonoa(trimToNull(eszenatokia.getTelefonoa()));
        return repo.save(eszenatokia);
    }

    public Eszenatokia update(int id, Eszenatokia updated) {
        Eszenatokia existing = findById(id);

        existing.setIzena(updated.getIzena());
        existing.setLekua(updated.getLekua());
        existing.setAforoa(updated.getAforoa());
        existing.setEmaila(trimToNull(updated.getEmaila()));
        existing.setTelefonoa(trimToNull(updated.getTelefonoa()));

        validate(existing);
        return repo.save(existing);
    }

    public void delete(int id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Ezin da ezabatu, ez da existitzen: id=" + id);
        }
        repo.deleteById(id);
    }

    // ======================
    // Validaciones
    // ======================
    private void validate(Eszenatokia e) {
        if (e.getIzena() == null || e.getIzena().isBlank()) {
            throw new RuntimeException("Izena derrigorrezkoa da");
        }
        if (e.getAforoa() == null || e.getAforoa() <= 0) {
            throw new RuntimeException("Aforoa > 0 izan behar da");
        }

        String email = trimToNull(e.getEmaila());
        if (email != null && !isValidEmail(email)) {
            throw new RuntimeException("Emaila ez da baliozkoa");
        }

        String tel = trimToNull(e.getTelefonoa());
        if (tel != null && !isValidPhone(tel)) {
            throw new RuntimeException("Telefonoa ez da baliozkoa");
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    private boolean isValidPhone(String tel) {
        return tel.matches("^[+0-9][0-9\\s-]{6,19}$");
    }

    private String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}