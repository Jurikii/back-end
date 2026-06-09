package com.juriki.api_juriki.arquivo.service;

import com.juriki.api_juriki.arquivo.dto.ArquivoResponseDTO;
import com.juriki.api_juriki.arquivo.dto.ArquivoUploadRequestDTO;
import com.juriki.api_juriki.arquivo.dto.SupabaseUploadResultDTO;
import com.juriki.api_juriki.arquivo.enums.OrigemArquivo;
import com.juriki.api_juriki.arquivo.enums.TipoArquivo;
import com.juriki.api_juriki.arquivo.exception.ArquivoAcessoNegadoException;
import com.juriki.api_juriki.arquivo.exception.ArquivoNotFoundException;
import com.juriki.api_juriki.arquivo.mapper.ArquivoMapper;
import com.juriki.api_juriki.arquivo.model.Arquivo;
import com.juriki.api_juriki.arquivo.repository.ArquivoRepository;
import com.juriki.api_juriki.processo.model.Processo;
import com.juriki.api_juriki.processo.repository.ProcessoRepository;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArquivoService {

    private final ArquivoRepository arquivoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProcessoRepository processoRepository;
    private final SupabaseStorageService supabaseStorageService;
    private final ArquivoMapper mapper;

    // Limite de espaço por usuário: 500MB em bytes
    private static final long LIMITE_ESPACO_USUARIO = 500L * 1024 * 1024;

    /**
     * Faz o upload de um arquivo.
     * RN06: Se origem = PROCESSO, idProcesso é obrigatório.
     * RN11: Usuário pode realizar upload de documentos.
     *
     * Fluxo: Frontend envia arquivo → Backend valida → Envia para Supabase →
     * Supabase retorna URL → Backend salva metadados no banco
     */
    @Transactional
    public ArquivoResponseDTO upload(Integer idUsuario, MultipartFile file, ArquivoUploadRequestDTO dto) {
        log.info("Iniciando upload de arquivo para usuário ID: {}", idUsuario);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + idUsuario));

        if (dto.origem() == OrigemArquivo.PROCESSO && dto.idProcesso() == null) {
            throw new RuntimeException("Arquivos de processo precisam estar vinculados a um processo (RN06).");
        }

        long espacoUsado = arquivoRepository.somarTamanhoTotalPorUsuario(idUsuario);
        if (espacoUsado + file.getSize() > LIMITE_ESPACO_USUARIO) {
            throw new RuntimeException("Limite de armazenamento atingido. Libere espaço antes de fazer novos uploads.");
        }

        SupabaseUploadResultDTO uploadResult = supabaseStorageService.upload(file, idUsuario);

        Processo processo = null;
        if (dto.idProcesso() != null) {
            processo = processoRepository.findById(dto.idProcesso())
                    .orElseThrow(() -> new RuntimeException("Processo não encontrado: " + dto.idProcesso()));
        }

        String extensao = extrairExtensao(file.getOriginalFilename());

        Arquivo arquivo = Arquivo.builder()
                .usuario(usuario)
                .processo(processo)
                .idChatbot(dto.idChatbot())
                .nomeArquivo(gerarNomeArquivo(file.getOriginalFilename()))
                .nomeOriginal(file.getOriginalFilename())
                .tipoArquivo(dto.tipoArquivo())
                .extensao(extensao)
                .tamanho(file.getSize())
                .caminho(uploadResult.caminho())
                .urlPublica(uploadResult.urlPublica())
                .origem(dto.origem())
                .build();

        arquivo = arquivoRepository.save(arquivo);

        log.info("Arquivo ID {} salvo com sucesso para usuário ID {}", arquivo.getId(), idUsuario);
        return mapper.toResponseDTO(arquivo);
    }

    /**
     * Lista todos os arquivos de um usuário.
     */
    @Transactional(readOnly = true)
    public List<ArquivoResponseDTO> listarPorUsuario(Integer idUsuario) {
        return arquivoRepository.findByUsuarioIdOrderByDataUploadDesc(idUsuario)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    /**
     * Lista arquivos de um usuário filtrando por tipo.
     */
    @Transactional(readOnly = true)
    public List<ArquivoResponseDTO> listarPorTipo(Integer idUsuario, TipoArquivo tipo) {
        return arquivoRepository.findByUsuarioIdAndTipoArquivoOrderByDataUploadDesc(idUsuario, tipo)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    /**
     * Lista arquivos vinculados a um processo específico.
     * RN06: Processo pode conter vários documentos.
     */
    @Transactional(readOnly = true)
    public List<ArquivoResponseDTO> listarPorProcesso(Integer idProcesso) {
        return arquivoRepository.findByProcessoIdOrderByDataUploadDesc(idProcesso)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    /**
     * Lista arquivos enviados em um chat com a IA.
     */
    @Transactional(readOnly = true)
    public List<ArquivoResponseDTO> listarPorChatbot(Integer idChatbot) {
        return arquivoRepository.findByIdChatbotOrderByDataUploadDesc(idChatbot)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    /**
     * Busca um arquivo por ID validando que pertence ao usuário.
     */
    @Transactional(readOnly = true)
    public ArquivoResponseDTO buscarPorId(Integer id, Integer idUsuario) {
        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() -> new ArquivoNotFoundException(id));

        verificarPropriedade(arquivo, idUsuario);

        return mapper.toResponseDTO(arquivo);
    }

    /**
     * Deleta um arquivo do banco e do Supabase Storage.
     */
    @Transactional
    public void deletar(Integer id, Integer idUsuario) {
        Arquivo arquivo = arquivoRepository.findById(id)
                .orElseThrow(() -> new ArquivoNotFoundException(id));

        verificarPropriedade(arquivo, idUsuario);

        // Remove do Supabase Storage primeiro
        supabaseStorageService.deletar(arquivo.getCaminho());

        // Remove do banco
        arquivoRepository.delete(arquivo);

        log.info("Arquivo ID {} deletado pelo usuário ID {}", id, idUsuario);
    }

    /**
     * Retorna o espaço usado pelo usuário em bytes.
     */
    @Transactional(readOnly = true)
    public long buscarEspacoUsado(Integer idUsuario) {
        return arquivoRepository.somarTamanhoTotalPorUsuario(idUsuario);
    }

    // --- Utilitários ---

    private void verificarPropriedade(Arquivo arquivo, Integer idUsuario) {
        if (!arquivo.getUsuario().getId().equals(idUsuario)) {
            throw new ArquivoAcessoNegadoException();
        }
    }

    private String extrairExtensao(String nomeArquivo) {
        if (nomeArquivo == null || !nomeArquivo.contains(".")) return "";
        return nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1).toLowerCase();
    }

    private String gerarNomeArquivo(String nomeOriginal) {
        if (nomeOriginal == null) return "arquivo";
        // Remove caracteres especiais mantendo a extensão
        return nomeOriginal.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
