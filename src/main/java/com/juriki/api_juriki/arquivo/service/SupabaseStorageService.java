package com.juriki.api_juriki.arquivo.service;

import com.juriki.api_juriki.arquivo.dto.SupabaseUploadResultDTO;
import com.juriki.api_juriki.arquivo.exception.TipoArquivoNaoPermitidoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
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

    private final RestTemplate restTemplate = new RestTemplate();

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

        String nomeUnico = UUID.randomUUID() + "." + extensao;
        String caminho = "usuarios/" + idUsuario + "/" + nomeUnico;

        String url = supabaseUrl + "/storage/v1/object/" + bucket + "/" + caminho;
        log.info("Iniciando upload para Supabase. URL: {}", url);

        try {
            byte[] bytes = arquivo.getBytes();

            var headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.setContentType(
                    arquivo.getContentType() != null
                            ? org.springframework.http.MediaType.parseMediaType(arquivo.getContentType())
                            : org.springframework.http.MediaType.APPLICATION_OCTET_STREAM
            );

            var requestEntity = new org.springframework.http.HttpEntity<>(bytes, headers);
            var response = restTemplate.exchange(url, org.springframework.http.HttpMethod.POST, requestEntity, String.class);

            log.info("Upload para Supabase concluído. Status: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("Erro ao enviar arquivo para Supabase", e);
            throw new RuntimeException("Erro ao enviar arquivo para o Supabase Storage", e);
        }

        String urlPublica = supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + caminho;
        log.info("URL pública gerada: {}", urlPublica);

        return new SupabaseUploadResultDTO(caminho, urlPublica);
    }

    /**
     * Remove um arquivo do Supabase Storage ao deletar do banco.
     *
     * @param caminho Caminho do arquivo dentro do bucket
     */
    public void deletar(String caminho) {
        log.info("Deletando arquivo do Supabase. Caminho: {}", caminho);

        try {
            String url = supabaseUrl + "/storage/v1/object/" + bucket;

            var headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            var body = Map.of("prefixes", List.of(caminho));
            var requestEntity = new org.springframework.http.HttpEntity<>(body, headers);
            var response = restTemplate.exchange(url, org.springframework.http.HttpMethod.DELETE, requestEntity, String.class);

            log.info("Arquivo deletado do Supabase. Status: {}", response.getStatusCode());
        } catch (Exception e) {
            log.warn("Erro ao deletar arquivo do Supabase (pode já não existir): {}", e.getMessage());
        }
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
