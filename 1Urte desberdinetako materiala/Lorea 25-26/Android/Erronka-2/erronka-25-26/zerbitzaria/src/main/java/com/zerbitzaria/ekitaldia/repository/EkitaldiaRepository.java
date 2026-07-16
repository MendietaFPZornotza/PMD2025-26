package com.zerbitzaria.ekitaldia.repository;

import com.zerbitzaria.common.enums.EkitaldiMota;
import com.zerbitzaria.ekitaldia.entity.Ekitaldia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EkitaldiaRepository extends JpaRepository<Ekitaldia, Integer> {

    @Query("""
        select e
        from Ekitaldia e
        join fetch e.eszenatokia s
        where e.mota = :mota
        and e.aktibo = true
        order by e.hasiera asc
    """)
    List<Ekitaldia> findUpcomingByMotaWithEszenatokia(
            @Param("mota") EkitaldiMota mota
    );
    // Ya lo tenías: próximos eventos (sin filtrar mota)
    List<Ekitaldia> findByHasieraAfterOrderByHasieraAsc(LocalDateTime now);

    // Para listar por escenario
    List<Ekitaldia> findByEszenatokia_IdOrderByHasieraAsc(Integer eszenatokiaId);


}
