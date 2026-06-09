package com.juriki.api_juriki.processo.model;

import com.juriki.api_juriki.advogado.model.Advogado;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "processos_advogados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoAdvogado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_processo_advogado")
    private Integer id;

    // RN09: Um advogado pode atuar em vários processos
    // RN10: Um processo pode possuir vários advogados
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_processo", nullable = false)
    private Processo processo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_advogado", nullable = false)
    private Advogado advogado;

    @Column(name = "funcao", length = 100)
    private String funcao;

    @Column(name = "data_entrada", nullable = false, updatable = false)
    private LocalDateTime dataVinculo;

    // Null = ainda ativo; preenchido quando desvinculado (RN12: troca de advogado)
    @Column(name = "data_desvinculo")
    private LocalDateTime dataDesvinculo;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @PrePersist
    protected void onCreate() {
        dataVinculo = LocalDateTime.now();
        ativo = true;
    }
}
