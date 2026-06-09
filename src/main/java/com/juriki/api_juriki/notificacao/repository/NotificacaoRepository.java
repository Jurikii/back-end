package com.juriki.api_juriki.notificacao.repository;

import com.juriki.api_juriki.notificacao.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {

    List<Notificacao> findByUsuarioIdOrderByDataEnvioDesc(Integer idUsuario);

    List<Notificacao> findByUsuarioIdAndLidaFalseOrderByDataEnvioDesc(Integer idUsuario);
}
