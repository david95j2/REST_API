package com.example.restapi.file;

import com.example.restapi.file.domain.ImageEntity;
import com.example.restapi.file.domain.PcdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    Optional<ImageEntity> findByIdAndPcdEntityId(int img_id,int pcd_id);
    List<ImageEntity> findAllByPcdEntityId(int pcd_id);
}
