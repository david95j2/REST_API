package com.example.restapi.file.pcd;

import com.example.restapi.file.pcd.domain.GetGroupInfoMapping;
import com.example.restapi.file.pcd.domain.GetMapInfoMapping;
import com.example.restapi.file.pcd.domain.MapGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapGroupRepository extends JpaRepository<MapGroupEntity, Integer> {
    @Query("select mg.id as mapGroupId, mg.location as location, mg.latitude as latitude, mg.regdate as regdate " +
            ", mg.longitude as longitude, count(distinct mgs.id) as sampleCount, count(distinct m.id) as mapCount " +
            "from MapGroupEntity mg " +
            "join mg.userEntity u join mg.mapGroupSampleEntites mgs join mg.mapEntities m " +
            "where u.loginId=:login_id " +
            "group by mg.id")
    List<GetGroupInfoMapping> findAllByLoginId(@Param("login_id") String login_id);


}
