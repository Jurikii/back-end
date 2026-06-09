package com.juriki.api_juriki.workspace.service;

import com.juriki.api_juriki.exception.RecursoNaoEncontradoException;
import com.juriki.api_juriki.usuario.model.Usuario;
import com.juriki.api_juriki.usuario.repository.UsuarioRepository;
import com.juriki.api_juriki.workspace.dto.WorkspaceAtualizarRequestDTO;
import com.juriki.api_juriki.workspace.dto.WorkspaceCriarRequestDTO;
import com.juriki.api_juriki.workspace.dto.WorkspaceResponseDTO;
import com.juriki.api_juriki.workspace.model.Workspace;
import com.juriki.api_juriki.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public WorkspaceResponseDTO criar(WorkspaceCriarRequestDTO request) {
        Usuario usuarioDono = usuarioRepository.findById(request.getIdUsuarioDono())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado."));

        Workspace workspace = Workspace.builder()
                .nome(request.getNome())
                .descricao(request.getDescricao())
                .usuarioDono(usuarioDono)
                .build();

        workspaceRepository.save(workspace);

        log.info("Workspace criado com ID {} para usuário ID {}", workspace.getId(), usuarioDono.getId());

        return toResponse(workspace);
    }

    @Transactional(readOnly = true)
    public WorkspaceResponseDTO buscarPorId(Integer idWorkspace) {
        return toResponse(buscarWorkspace(idWorkspace));
    }

    @Transactional(readOnly = true)
    public List<WorkspaceResponseDTO> listarTodos() {
        return workspaceRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WorkspaceResponseDTO> listarPorUsuario(Integer idUsuarioDono) {
        return workspaceRepository.findByUsuarioDonoId(idUsuarioDono)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public WorkspaceResponseDTO atualizar(Integer idWorkspace, WorkspaceAtualizarRequestDTO request) {
        Workspace workspace = buscarWorkspace(idWorkspace);

        if (request.getNome() != null) {
            workspace.setNome(request.getNome());
        }
        if (request.getDescricao() != null) {
            workspace.setDescricao(request.getDescricao());
        }

        workspaceRepository.save(workspace);

        log.info("Workspace ID {} atualizado.", idWorkspace);

        return toResponse(workspace);
    }

    @Transactional
    public void deletar(Integer idWorkspace) {
        Workspace workspace = buscarWorkspace(idWorkspace);
        workspaceRepository.delete(workspace);
        log.info("Workspace ID {} removido.", idWorkspace);
    }

    private Workspace buscarWorkspace(Integer idWorkspace) {
        return workspaceRepository.findById(idWorkspace)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Workspace não encontrado."));
    }

    private WorkspaceResponseDTO toResponse(Workspace workspace) {
        return WorkspaceResponseDTO.builder()
                .idWorkspace(workspace.getId())
                .nome(workspace.getNome())
                .descricao(workspace.getDescricao())
                .idUsuarioDono(workspace.getUsuarioDono().getId())
                .criadoEm(workspace.getCriadoEm())
                .build();
    }
}
