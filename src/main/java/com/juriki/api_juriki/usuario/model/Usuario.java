package com.juriki.api_juriki.usuario.model;

import com.juriki.api_juriki.plano.model.Plano;
import com.juriki.api_juriki.usuario.enums.EStatusConta;
import com.juriki.api_juriki.usuario.enums.ETipoUsuario;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plano")
    private Plano plano;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private ETipoUsuario tipoUsuario = ETipoUsuario.CLIENTE;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;
    
    @Column(name = "cpf", unique = true, length = 14)
    private String cpf;

    @Column(name = "email", unique = true, nullable = false, length = 150)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "foto_perfil", length = 500)
    private String fotoPerfil;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status_conta")
    private EStatusConta statusConta = EStatusConta.ATIVA;

    @Builder.Default
    @Column(name = "email_verificado")
    private Boolean emailVerificado = false;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @Column(name = "token_recuperacao", length = 255)
    private String tokenRecuperacao;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;

    public boolean isAtivo() {
        return EStatusConta.ATIVA.equals(this.statusConta) && this.deletadoEm == null;
    }

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
    
    public String getNome() {
        return nome;
    }
}
