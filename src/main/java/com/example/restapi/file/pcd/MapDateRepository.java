package com.example.restapi.file.pcd;

import com.example.restapi.file.pcd.domain.GetDateMapping;
import com.example.restapi.file.pcd.domain.MapDateEntity;
import com.example.restapi.file.pcd.domain.MapGroupSampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapDateRepository extends JpaRepository<MapDateEntity, Integer> {
    @Query("select md.id as id, md.date as date, md.time as time, m.id as pcdId " +
            "from MapDateEntity md " +
            "join md.mapEntities m join md.mapGroupEntity mg join mg.userEntity u " +
            "where mg.id=:map_group_id and m.mapPath like %:file_name%")
    List<GetDateMapping> findAllByMapGroupIdAndLoginId(@Param("map_group_id") Integer map_group_id, @Param("file_name") String file_name);
}
