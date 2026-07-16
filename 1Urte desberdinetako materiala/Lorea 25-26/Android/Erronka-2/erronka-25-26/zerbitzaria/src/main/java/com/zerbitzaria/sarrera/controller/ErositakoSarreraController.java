package com.zerbitzaria.sarrera.controller;

import com.zerbitzaria.sarrera.dto.ErosiRequest;
import com.zerbitzaria.sarrera.entity.ErositakoSarrera;
import com.zerbitzaria.sarrera.service.ErositakoSarreraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sarrerak")
public class ErositakoSarreraController {

    private final ErositakoSarreraService service;

    public ErositakoSarreraController(ErositakoSarreraService service) {
        this.service = service;
    }

    @GetMapping
    public List<ErositakoSarrera> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ErositakoSarrera findById(@PathVariable int id) {
        return service.findById(id);
    }

    // Historial por usuario
    @GetMapping("/erabiltzailea/{erabiltzaileaId}")
    public List<ErositakoSarrera> byUser(@PathVariable int erabiltzaileaId) {
        return service.findByErabiltzailea(erabiltzaileaId);
    }

    // Validar QR (solo consulta)
    @GetMapping("/kodea/{kodea}")
    public ErositakoSarrera byKodea(@PathVariable String kodea) {
        return service.findByKodea(kodea);
    }

    // Comprar entrada
    @PostMapping("/erosi")
    public ResponseEntity<ErositakoSarrera> erosi(@RequestBody ErosiRequest req) {
        return ResponseEntity.ok(service.erosi(req.erabiltzaileaId, req.ekitaldiaId));
    }

    // Marcar como usada (control de acceso)
    @PostMapping("/erabili/{kodea}")
    public ResponseEntity<ErositakoSarrera> markUsed(@PathVariable String kodea) {
        return ResponseEntity.ok(service.markatuErabilita(kodea));
    }

    // Cancelar
    @PostMapping("/ezeztatu/{kodea}")
    public ResponseEntity<Void> cancel(@PathVariable String kodea) {
        service.ezeztatu(kodea);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}