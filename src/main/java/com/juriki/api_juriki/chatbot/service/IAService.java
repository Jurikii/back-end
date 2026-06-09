package com.juriki.api_juriki.chatbot.service;

import com.juriki.api_juriki.chatbot.model.ChatMensagem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IAService {

    @Value("${ia.api.url}")
    private String iaApiUrl;

    @Value("${ia.api.key}")
    private String iaApiKey;

    @Value("${ia.api.model}")
    private String iaModel;

    private static final String SYSTEM_PROMPT = """
            Você é o Juriki, um assistente jurídico inteligente. Sua função é:
            - Responder dúvidas jurídicas de forma clara e acessível
            - Traduzir termos jurídicos complexos para linguagem simples
            - Analisar documentos jurídicos quando fornecidos
            - Orientar o usuário sobre seus direitos e possibilidades legais
            - Recomendar a contratação de um advogado quando a situação exigir

            Importante: Você fornece orientação jurídica informativa, não substitui
            a consulta com um advogado habilitado para casos específicos.
            """;

    public IAResultado gerarResposta(
            String pergunta,
            List<ChatMensagem> historicoMensagens,
            String conteudoArquivo
    ) {
        log.info("Gerando resposta da IA. Histórico: {} mensagens", historicoMensagens.size());

        String contexto = montarContexto(historicoMensagens, conteudoArquivo);
        String promptCompleto = contexto + "\nUsuário: " + pergunta;

        log.info("Resposta da IA gerada com sucesso.");

        return new IAResultado(
                "Esta é uma resposta de exemplo da IA jurídica. Integração pendente.",
                0
        );
    }

    private String montarContexto(List<ChatMensagem> historico, String conteudoArquivo) {
        StringBuilder contexto = new StringBuilder();

        if (conteudoArquivo != null && !conteudoArquivo.isBlank()) {
            contexto.append("Documento anexado pelo usuário:\n")
                    .append(conteudoArquivo)
                    .append("\n\n");
        }

        if (!historico.isEmpty()) {
            contexto.append("Histórico da conversa:\n");
            historico.forEach(m -> contexto
                    .append("Usuário: ")
                    .append(m.getPergunta())
                    .append("\n")
                    .append("Juriki: ")
                    .append(m.getResposta())
                    .append("\n"));
        }

        return contexto.toString();
    }

    public record IAResultado(String resposta, int tokensConsumidos) {}
}
