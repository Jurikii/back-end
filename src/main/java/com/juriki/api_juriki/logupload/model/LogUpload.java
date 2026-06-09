package com.juriki.api_juriki.logupload.model;

import com.juriki.api_juriki.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs_uploads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "nome_arquivo", length = 255)
    private String nomeArquivo;

    @Column(name = "bucket", length = 100)
    private String bucket;

    @Column(name = "status_upload", length = 50)
    private String statusUpload;

    @Column(name = "mensagem", columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "data_upload", updatable = false)
    private LocalDateTime dataUpload;

    @PrePersist
    protected void onCreate() {
        dataUpload = LocalDateTime.now();
    }
}
