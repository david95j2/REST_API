package com.example.restapi.file.pcd;

import com.example.restapi.file.pcd.domain.MapGroupEntity;
import com.example.restapi.file.pcd.domain.MapGroupSampleEntity;
import com.example.restapi.file.pcd.domain.MapSampleMapping;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface MapGroupSampleRepository extends JpaRepository<MapGroupSampleEntity, Integer> {
//    @Query("select mgs.mapSamplePath as fileName from MapGroupSampleEntity mgs where mgs.mapDateEntity.id = " +
//            "(select md.id from mgs.mapDateEntity md where md.mapGroupEntity.id=:map_group_id " +
//            "order by md.time desc, md.date desc) " +
//            "order by mgs.mapSampleRegdate desc ")

    @Query("select mgs.mapSamplePath as fileName " +
            "from MapGroupSampleEntity mgs " +
            "inner join mgs.mapDateEntity md " +
            "where md.mapGroupEntity.id = :map_group_id " +
            "and md.id = (select max(subMd.id) from mgs.mapDateEntity subMd where subMd.mapGroupEntity.id = :map_group_id order by subMd.date desc, subMd.time desc) " +
            "order by mgs.mapSampleRegdate desc")
    Optional<MapSampleMapping> findSampleById(@Param("map_group_id") Integer map_group_id, PageRequest pageRequest);
}
