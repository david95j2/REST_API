package com.example.restapi.file.pcd;

import com.example.restapi.file.pcd.domain.GetMapInfoMapping;
import com.example.restapi.file.pcd.domain.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MapRepository extends JpaRepository<MapEntity, Integer> {

    @Query("select mg.latitude as latitude, mg.longitude as longitude, mg.location as location, m.mapPath as fileName, md.date as date,md.time as time " +
            "from MapEntity m " +
            "join m.mapDateEntity md join md.mapGroupEntity mg join mg.userEntity u " +
            "where m.id=:pcd_id")
    Optional<GetMapInfoMapping> findByIdAndLoginId(@Param("pcd_id") Integer pcd_id);


    @Query("select m " +
            "from MapEntity m " +
            "join m.mapDateEntity md join md.mapGroupEntity mg join mg.userEntity u " +
            "where md.id=:pcd_date_id and mg.id=:pcd_group_id")
    List<MapEntity> findAllByGroupIdAndDateId(@Param("pcd_group_id") Integer map_group_id,@Param("pcd_date_id") Integer map_date_id);
}
