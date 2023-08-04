package com.example.restapi.file;

import com.example.restapi.file.domain.GetMapInfoMapping;
import com.example.restapi.file.domain.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MapRepository extends JpaRepository<MapEntity, Integer> {

    @Query("select m.id as id, m.mapName as fileName, mg.location as location, mg.latitude as latitude, " +
            "mg.longitude as longitude, m.regdate as regdate "+
            "from MapEntity m " +
            "join m.mapGroupEntity mg join mg.userEntity u " +
            "where mg.userEntity.loginId=:login_id")
    List<GetMapInfoMapping> findAllByUserEntity_LoginId(@Param("login_id") String login_id);

    @Query("select m " +
            "from MapEntity m " +
            "join m.mapGroupEntity mg join mg.userEntity u " +
            "where m.id=:pcd_id and mg.userEntity.loginId=:login_id")
    Optional<MapEntity> findByIdAndLoginId(@Param("pcd_id") Integer pcd_id,@Param("login_id") String login_id);
}
