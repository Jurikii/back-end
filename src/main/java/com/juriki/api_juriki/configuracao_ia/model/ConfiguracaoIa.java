package com.juriki.api_juriki.configuracao_ia.model;

import com.juriki.api_juriki.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracoes_ia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracaoIa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_config")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "modelo_padrao", length = 100)
    private String modeloPadrao;

    @Column(name = "temperatura", precision = 3, scale = 2)
    private BigDecimal temperatura = new BigDecimal("0.7");

    @Column(name = "respostas_longas")
    private Boolean respostasLongas = true;

    @Column(name = "linguagem_juridica")
    private Boolean linguagemJuridica = true;

    @Column(name = "simplificar_termos")
    private Boolean simplificarTermos = true;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
