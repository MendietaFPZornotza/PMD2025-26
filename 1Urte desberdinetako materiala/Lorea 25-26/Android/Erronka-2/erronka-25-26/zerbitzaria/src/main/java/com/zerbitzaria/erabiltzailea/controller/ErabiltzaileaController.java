package com.zerbitzaria.erabiltzailea.controller;

import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import com.zerbitzaria.erabiltzailea.service.ErabiltzaileaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/erabiltzaileak")
public class ErabiltzaileaController {

    private final ErabiltzaileaService service;

    public ErabiltzaileaController(ErabiltzaileaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Erabiltzailea> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Erabiltzailea> findById(@PathVariable Long id) {  // ← Long, no int
        return service.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Erabiltzailea> create(@RequestBody Erabiltzailea erabiltzailea) {
        Erabiltzailea saved = service.create(erabiltzailea);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Erabiltzailea> update(@PathVariable Long id,  // ← Long, no int
                                                @RequestBody Erabiltzailea updated) {
        Erabiltzailea result = service.update(id, updated);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {  // ← Long, no int
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
