package com.zerbitzaria.erabiltzailea.repository;

import com.zerbitzaria.common.enums.EkitaldiMota;
import com.zerbitzaria.ekitaldia.entity.Ekitaldia;
import com.zerbitzaria.erabiltzailea.entity.Erabiltzailea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ErabiltzaileaRepository
        extends JpaRepository<Erabiltzailea, Long> {

    Optional<Erabiltzailea> findByEmaila(String emaila);
//    @Query("""
//        select e
//        from Erabiltzailea
//        where e.aktibo = true
//    """)
//    List<Erabiltzailea> findByEmaila(
//            @Param("email") Erabiltzailea emaila
//    );
}