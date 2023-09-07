package com.example.restapi.webGCS;

import com.example.restapi.webGCS.domain.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<MissionEntity, Integer> {
    @Query("select m from MissionEntity m " +
            "join m.userEntity u where u.loginId=:login_id")
    List<MissionEntity> findAllByLoginId(@Param("login_id") String login_id);
}
