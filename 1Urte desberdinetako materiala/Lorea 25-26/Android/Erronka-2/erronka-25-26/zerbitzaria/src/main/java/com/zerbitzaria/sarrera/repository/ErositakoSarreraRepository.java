package com.zerbitzaria.sarrera.repository;

import com.zerbitzaria.common.enums.SarreraEgoera;
import com.zerbitzaria.sarrera.entity.ErositakoSarrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ErositakoSarreraRepository extends JpaRepository<ErositakoSarrera, Integer> {

    Optional<ErositakoSarrera> findByKodea(String kodea);

    long countByEkitaldia_Id(Integer ekitaldiaId);

    List<ErositakoSarrera> findByErabiltzailea_Id(Integer erabiltzaileaId);

    boolean existsByKodea(String kodea);

    List<ErositakoSarrera> findByDeskargaKodea(String deskargaKodea);

    boolean existsByEkitaldia_IdAndFilaAndEserlekua(Integer ekitaldiaId, Integer fila, Integer eserlekua);

    List<ErositakoSarrera> findByEkitaldia_IdAndEgoeraNot(Integer ekitaldiaId, SarreraEgoera egoera);

    @Query("""
        select s
        from ErositakoSarrera s
        join fetch s.ekitaldia e
        left join fetch e.eszenatokia
        where s.erabiltzailea.id = :userId
        order by s.erosketarenData desc
    """)
    List<ErositakoSarrera> findUserTicketsWithEvent(@Param("userId") Integer userId);

    @Query("""
        select s
        from ErositakoSarrera s
        join fetch s.ekitaldia e
        left join fetch e.eszenatokia
        where s.deskargaKodea = :code
        order by s.erosketarenData desc
    """)
    List<ErositakoSarrera> findByDeskargaKodeaWithEvent(@Param("code") String code);

}