package com.example.restapi.file;

import com.example.restapi.file.domain.GetMapInfoMapping;
import com.example.restapi.file.domain.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MapRepository extends JpaRepository<MapEntity, Integer> {

    List<GetMapInfoMapping> findAllByUserEntity_LoginId(String login_id);
    Optional<MapEntity> findByIdAndUserEntityLoginId(Integer pcd_id, String login_id);
}
