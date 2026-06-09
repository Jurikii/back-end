package com.juriki.api_juriki.arquivo.service;

import com.juriki.api_juriki.arquivo.dto.SupabaseUploadResultDTO;
import com.juriki.api_juriki.arquivo.exception.TipoArquivoNaoPermitidoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

/**
 * Serviço responsável pela integração com o Supabase Storage.
 *
 * Fluxo documentado no backend:
 * Usuário envia documento → Frontend captura e envia para o backend →
 * Backend envia para Supabase Storage → Supabase salva no bucket →
 * Supabase gera URL → Backend salva metadados no banco →
 * Frontend exibe documento
 */
@Service
@Slf4j
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    // Extensões permitidas para upload
    private static final Set<String> EXTENSOES_PERMITIDAS = Set.of(
            "pdf", "docx", "doc", "png", "jpg", "jpeg"
    );

    // Tamanho máximo: 10MB em bytes
    private static final long TAMANHO_MAXIMO = 10 * 1024 * 1024;

    /**
     * Faz o upload de um arquivo para o Supabase Storage.
     * Retorna o caminho e a URL pública gerada pelo Supabase.
     *
     * @param arquivo   Arquivo recebido pelo controller
     * @param idUsuario ID do usuário dono do arquivo (usado no path do bucket)
     */
    public SupabaseUploadResultDTO upload(MultipartFile arquivo, Integer idUsuario) {
        String extensao = extrairExtensao(arquivo.getOriginalFilename());
        validarExtensao(extensao);
        validarTamanho(arquivo.getSize());

        // Gera nome único para evitar colisões no bucket
        String nomeUnico = UUID.randomUUID() + "." + extensao;

        // Organiza arquivos por usuário dentro do bucket: "usuarios/{id}/{nomeUnico}"
        String caminho = "usuarios/" + idUsuario + "/" + nomeUnico;

        log.info("Iniciando upload para Supabase. Caminho: {}", caminho);

        // TODO: Implementar chamada HTTP para Supabase Storage API
        // Endpoint: POST {supabaseUrl}/storage/v1/object/{bucket}/{caminho}
        // Headers: Authorization: Bearer {supabaseKey}
        //          Content-Type: {arquivo.getContentType()}
        // Body: bytes do arquivo
        // Retorno: JSON com { "Key": "{caminho}" }

        // TODO: Montar URL pública após upload bem-sucedido
        // Formato: {supabaseUrl}/storage/v1/object/public/{bucket}/{caminho}

        String urlPublica = supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + caminho;

        log.info("Upload concluído. URL pública: {}", urlPublica);

        return new SupabaseUploadResultDTO(caminho, urlPublica);
    }

    /**
     * Remove um arquivo do Supabase Storage ao deletar do banco.
     *
     * @param caminho Caminho do arquivo dentro do bucket
     */
    public void deletar(String caminho) {
        log.info("Deletando arquivo do Supabase. Caminho: {}", caminho);

        // TODO: Implementar chamada HTTP para Supabase Storage API
        // Endpoint: DELETE {supabaseUrl}/storage/v1/object/{bucket}/{caminho}
        // Headers: Authorization: Bearer {supabaseKey}
    }

    // --- Utilitários ---

    private String extrairExtensao(String nomeArquivo) {
        if (nomeArquivo == null || !nomeArquivo.contains(".")) {
            return "";
        }
        return nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1).toLowerCase();
    }

    private void validarExtensao(String extensao) {
        if (!EXTENSOES_PERMITIDAS.contains(extensao)) {
            throw new TipoArquivoNaoPermitidoException(extensao);
        }
    }

    private void validarTamanho(long tamanho) {
        if (tamanho > TAMANHO_MAXIMO) {
            throw new RuntimeException("Arquivo muito grande. O tamanho máximo permitido é 10MB.");
        }
    }
}
