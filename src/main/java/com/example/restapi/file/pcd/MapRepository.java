package com.example.restapi.file.pcd;

import com.example.restapi.file.pcd.domain.GetMapInfoMapping;
import com.example.restapi.file.pcd.domain.GetMapJsonMapping;
import com.example.restapi.file.pcd.domain.MapEntity;
import com.example.restapi.file.pcd.domain.MapSampleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface MapRepository extends JpaRepository<MapEntity, Integer> {

    @Query("select mg.latitude as latitude, mg.longitude as longitude, mg.location as location, m.mapPath as fileName, mg.regdate as regdate " +
            "" +
            "from MapEntity m " +
            "join m.mapGroupEntity mg join mg.userEntity u " +
            "where m.id=:pcd_id and mg.userEntity.loginId=:login_id")
    Optional<GetMapInfoMapping> findByIdAndLoginId(@Param("pcd_id") Integer pcd_id, @Param("login_id") String login_id);

    @Query("select mg.location as location, m.mapPath as fileName, mg.regdate as regdate, m.mapCount as count, m.mapArea as area " +
            "from MapEntity m " +
            "join m.mapGroupEntity mg join mg.userEntity u " +
            "where m.id=:pcd_id and mg.userEntity.loginId=:login_id")
    Optional<GetMapJsonMapping> findDetailByIdAndLoginId(@Param("pcd_id") Integer pcd_id, @Param("login_id") String login_id);


    @Query("select mgs.mapSamplePath as pcdSamplePath " +
            "from MapEntity m " +
            "join m.mapGroupEntity mg join mg.userEntity u " +
            "join mg.mapGroupSampleEntites mgs " +
            "where m.id=:pcd_id and mg.userEntity.loginId=:login_id")
    Optional<MapSampleMapping> findSampleByIdAndLoginId(@Param("pcd_id") Integer pcd_id, @Param("login_id") String login_id);


    @Query("select m " +
            "from MapEntity m " +
            "join m.mapGroupEntity mg join mg.userEntity u " +
            "where mg.id=:map_group_id and u.loginId=:login_id")
    List<MapEntity> findAllByMapGroupIdAndLoginId(@Param("map_group_id") Integer map_group_id,@Param("login_id") String login_id);
}
