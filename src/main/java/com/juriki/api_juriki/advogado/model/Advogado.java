package com.juriki.api_juriki.advogado.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.juriki.api_juriki.usuario.model.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "advogados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Advogado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_advogado")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "oab", nullable = false, length = 20)
    private String oab;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "especialidade", length = 150)
    private String especialidade;

    @Column(name = "biografia", columnDefinition = "TEXT")
    private String biografia;

    @Column(name = "valor_consulta", precision = 10, scale = 2)
    private BigDecimal valorConsulta;

    @Column(name = "experiencia_anos")
    private Integer experienciaAnos;

    @Column(name = "linkedin", length = 255)
    private String linkedin;

    @Column(name = "site_profissional", length = 255)
    private String siteProfissional;

    @Column(name = "aprovado")
    private Boolean aprovado = false;

    @Column(name = "status_profissional", length = 50)
    private String statusProfissional = "ATIVO";

    @Column(name = "nota_media", precision = 3, scale = 2)
    private BigDecimal notaMedia = BigDecimal.ZERO;

    @Column(name = "total_avaliacoes")
    private Integer totalAvaliacoes = 0;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
    
}
