package com.constructora.backend.repository;

import com.constructora.backend.entity.GastoProforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GastoProformaRepository extends JpaRepository<GastoProforma, Long> {
    
    List<GastoProforma> findByProformaIdOrderByOrdenAsc(Long proformaId);
    
    @Query("SELECT g FROM GastoProforma g WHERE g.proforma.id = :proformaId ORDER BY g.orden ASC")
    List<GastoProforma> findGastosByProformaId(Long proformaId);
    
    void deleteByProformaId(Long proformaId);
}
