package com.juriki.api_juriki.plano.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.juriki.api_juriki.plano.model.Plano;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Integer> {

	List<Plano> findByAtivoTrue();

    boolean existsByNomePlano(String nomePlano);

}
