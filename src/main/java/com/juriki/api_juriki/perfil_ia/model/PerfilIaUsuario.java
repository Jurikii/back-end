package com.juriki.api_juriki.perfil_ia.model;

import com.juriki.api_juriki.perfil_ia.enums.EEstiloResposta;
import com.juriki.api_juriki.perfil_ia.enums.ENivelJuridico;
import com.juriki.api_juriki.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "perfil_ia_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilIaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_juridico")
    private ENivelJuridico nivelJuridico = ENivelJuridico.LEIGO;

    @Enumerated(EnumType.STRING)
    @Column(name = "estilo_resposta")
    private EEstiloResposta estiloResposta = EEstiloResposta.SIMPLIFICADO;

    @Column(name = "objetivo_uso", length = 255)
    private String objetivoUso;

    @Column(name = "areas_interesse", columnDefinition = "TEXT")
    private String areasInteresse;

    @Column(name = "linguagem_preferida", length = 50)
    private String linguagemPreferida = "PT-BR";

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}
