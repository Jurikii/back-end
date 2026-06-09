package com.juriki.api_juriki.arquivo.model;

import com.juriki.api_juriki.arquivo.enums.OrigemArquivo;
import com.juriki.api_juriki.arquivo.enums.TipoArquivo;
import com.juriki.api_juriki.processo.model.Processo;
import com.juriki.api_juriki.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "arquivos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_arquivo")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_processo")
    private Processo processo;

    @Column(name = "id_chatbot")
    private Integer idChatbot;

    @Column(name = "nome_arquivo", nullable = false, length = 255)
    private String nomeArquivo;

    @Column(name = "nome_original", length = 255)
    private String nomeOriginal;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_arquivo", length = 50)
    private TipoArquivo tipoArquivo;

    // Extensão real do arquivo: pdf, png, docx etc.
    @Column(name = "extensao", length = 10)
    private String extensao;

    // Tamanho em bytes
    @Column(name = "tamanho_bytes")
    private Long tamanho;

    @Column(name = "bucket", length = 100)
    private String bucket;

    @Column(name = "caminho_storage", columnDefinition = "TEXT")
    private String caminho;

    @Column(name = "url_publica", columnDefinition = "TEXT")
    private String urlPublica;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "origem", nullable = false, length = 50)
    private OrigemArquivo origem;

    @Column(name = "data_upload", nullable = false, updatable = false)
    private LocalDateTime dataUpload;

    @PrePersist
    protected void onCreate() {
        dataUpload = LocalDateTime.now();
    }
}
