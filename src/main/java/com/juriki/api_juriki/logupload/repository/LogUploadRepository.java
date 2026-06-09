package com.juriki.api_juriki.logupload.repository;

import com.juriki.api_juriki.logupload.model.LogUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogUploadRepository extends JpaRepository<LogUpload, Integer> {

    List<LogUpload> findByUsuarioIdOrderByDataUploadDesc(Integer idUsuario);
}
