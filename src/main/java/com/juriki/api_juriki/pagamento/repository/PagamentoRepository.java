package com.juriki.api_juriki.pagamento.repository;

import com.juriki.api_juriki.pagamento.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    List<Pagamento> findByUsuarioIdOrderByDataPagamentoDesc(Integer idUsuario);

    List<Pagamento> findByPlanoIdOrderByDataPagamentoDesc(Integer idPlano);
}
