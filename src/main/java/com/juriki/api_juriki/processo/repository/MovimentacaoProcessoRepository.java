package com.juriki.api_juriki.processo.repository;

import com.juriki.api_juriki.processo.model.MovimentacaoProcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoProcessoRepository extends JpaRepository<MovimentacaoProcesso, Integer> {

    List<MovimentacaoProcesso> findByProcessoIdOrderByDataMovimentacaoDesc(Integer idProcesso);
}
