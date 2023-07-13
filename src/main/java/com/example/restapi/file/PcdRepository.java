package com.example.restapi.file;

import com.example.restapi.file.domain.GetPcdInfoMapping;
import com.example.restapi.file.domain.PcdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PcdRepository extends JpaRepository<PcdEntity, Integer> {

    List<GetPcdInfoMapping> findAllByUserEntity_LoginId(String login_id);
    Optional<PcdEntity> findByIdAndUserEntityLoginId(int pcd_id, String login_id);
}
