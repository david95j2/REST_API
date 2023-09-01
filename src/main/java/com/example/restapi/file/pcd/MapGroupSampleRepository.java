package com.example.restapi.file.pcd;

import com.example.restapi.file.pcd.domain.MapGroupEntity;
import com.example.restapi.file.pcd.domain.MapGroupSampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapGroupSampleRepository extends JpaRepository<MapGroupSampleEntity, Integer> {

}
