package com.example.restapi.file.pcd;

import com.example.restapi.file.pcd.domain.GetGroupInfoMapping;
import com.example.restapi.file.pcd.domain.MapGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MapGroupRepository extends JpaRepository<MapGroupEntity, Integer> {

    @Query("SELECT mg " +
            "FROM MapGroupEntity mg " +
            "join mg.userEntity u " +
            "WHERE u.loginId=:login_id")
    List<GetGroupInfoMapping> findMapGroupByUserId(@Param("login_id") String login_id);

    Optional<MapGroupEntity> findByLocation(String location);
}
