package com.juriki.api_juriki.workspace.repository;

import com.juriki.api_juriki.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Integer> {

    List<Workspace> findByUsuarioDonoId(Integer idUsuarioDono);
}
