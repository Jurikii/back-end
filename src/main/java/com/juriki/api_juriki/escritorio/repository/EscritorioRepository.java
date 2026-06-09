package com.juriki.api_juriki.escritorio.repository;

import com.juriki.api_juriki.escritorio.model.Escritorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscritorioRepository extends JpaRepository<Escritorio, Integer> {
}
