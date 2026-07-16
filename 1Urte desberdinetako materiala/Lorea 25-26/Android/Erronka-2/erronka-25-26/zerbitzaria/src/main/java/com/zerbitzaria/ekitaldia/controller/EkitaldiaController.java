package com.zerbitzaria.ekitaldia.controller;

import com.zerbitzaria.ekitaldia.dto.EkitaldiaRequest;
import com.zerbitzaria.ekitaldia.entity.Ekitaldia;
import com.zerbitzaria.ekitaldia.service.EkitaldiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ekitaldiak")
public class EkitaldiaController {

    private final EkitaldiaService service;

    public EkitaldiaController(EkitaldiaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ekitaldia> findAll() {
        return service.findAll();
    }

    @GetMapping("/upcoming")
    public List<Ekitaldia> upcoming() {
        return service.findUpcoming();
    }

    @GetMapping("/eszenatokia/{eszenatokiaId}")
    public List<Ekitaldia> byEszenatokia(@PathVariable int eszenatokiaId) {
        return service.findByEszenatokia(eszenatokiaId);
    }

    @GetMapping("/{id}")
    public Ekitaldia findById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Ekitaldia> create(@RequestBody EkitaldiaRequest req) {
        Ekitaldia e = new Ekitaldia();
        e.setIzenburua(req.izenburua);
        e.setSinopsia(req.sinopsia);
        e.setGeneroa(req.generoa);
        e.setPrezioa(req.prezioa);
        e.setMota(req.mota);
        e.setEgoera(req.egoera);
        e.setAktibo(req.aktibo);
        e.setHasiera(req.hasiera);
        e.setAmaiera(req.amaiera);

        return ResponseEntity.ok(service.create(e, req.eszenatokiaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ekitaldia> update(@PathVariable int id, @RequestBody EkitaldiaRequest req) {
        Ekitaldia e = new Ekitaldia();
        e.setIzenburua(req.izenburua);
        e.setSinopsia(req.sinopsia);
        e.setGeneroa(req.generoa);
        e.setPrezioa(req.prezioa);
        e.setMota(req.mota);
        e.setEgoera(req.egoera);
        e.setAktibo(req.aktibo);
        e.setHasiera(req.hasiera);
        e.setAmaiera(req.amaiera);

        return ResponseEntity.ok(service.update(id, e, req.eszenatokiaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}