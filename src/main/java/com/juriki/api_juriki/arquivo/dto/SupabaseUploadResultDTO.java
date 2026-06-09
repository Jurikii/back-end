package com.juriki.api_juriki.arquivo.dto;

/**
 * Dados retornados pelo Supabase Storage após upload bem-sucedido.
 * Usado internamente entre SupabaseStorageService e ArquivoService.
 */
public record SupabaseUploadResultDTO(
        String caminho,      // Caminho dentro do bucket ex: "usuarios/1/contrato.pdf"
        String urlPublica    // URL pública de acesso ao arquivo
) {}
