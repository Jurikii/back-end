package com.juriki.api_juriki.arquivo.enums;

public enum OrigemArquivo {
    PROCESSO,   // Arquivo vinculado a um processo judicial (RN06)
    CHATBOT,    // Arquivo enviado na conversa com a IA para contextualização
    AVULSO      // Upload direto pelo usuário sem vínculo específico
}
