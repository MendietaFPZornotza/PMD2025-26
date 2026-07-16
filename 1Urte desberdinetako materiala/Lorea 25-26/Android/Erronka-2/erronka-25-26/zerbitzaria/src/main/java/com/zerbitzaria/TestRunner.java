package com.zerbitzaria;

import com.zerbitzaria.ekitaldia.repository.EkitaldiaRepository;
import com.zerbitzaria.sarrera.repository.ErositakoSarreraRepository;
import com.zerbitzaria.erabiltzailea.repository.ErabiltzaileaRepository;
import com.zerbitzaria.eszenatokia.repository.EszenatokiaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestRunner implements CommandLineRunner {

    private final ErabiltzaileaRepository erabiltzaileaRepo;
    private final EszenatokiaRepository eszenatokiaRepo;
    private final EkitaldiaRepository ekitaldiaRepo;
    private final ErositakoSarreraRepository sarreraRepo;

    public TestRunner(
            ErabiltzaileaRepository erabiltzaileaRepo,
            EszenatokiaRepository eszenatokiaRepo,
            EkitaldiaRepository ekitaldiaRepo,
            ErositakoSarreraRepository sarreraRepo
    ) {
        this.erabiltzaileaRepo = erabiltzaileaRepo;
        this.eszenatokiaRepo = eszenatokiaRepo;
        this.ekitaldiaRepo = ekitaldiaRepo;
        this.sarreraRepo = sarreraRepo;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Hibernate TestRunner ===");
        System.out.println("Erabiltzaileak: " + erabiltzaileaRepo.count());
        System.out.println("Eszenatokiak:   " + eszenatokiaRepo.count());
        System.out.println("Ekitaldiak:     " + ekitaldiaRepo.count());
        System.out.println("Sarrerak:       " + sarreraRepo.count());
        System.out.println("============================");
    }
}