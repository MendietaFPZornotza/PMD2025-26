package com.zerbitzaria.eszenatokia.controller;

import com.zerbitzaria.eszenatokia.entity.Eszenatokia;
import com.zerbitzaria.eszenatokia.service.EszenatokiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eszenatokiak")
public class EszenatokiaController {

    private final EszenatokiaService service;

    public EszenatokiaController(EszenatokiaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Eszenatokia> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Eszenatokia findById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<Eszenatokia> create(@RequestBody Eszenatokia eszenatokia) {
        return ResponseEntity.ok(service.create(eszenatokia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Eszenatokia> update(@PathVariable int id, @RequestBody Eszenatokia updated) {
        return ResponseEntity.ok(service.update(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}