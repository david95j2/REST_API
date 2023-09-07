package com.example.restapi.webGCS;

import com.example.restapi.webGCS.domain.MissionEntity;
import com.example.restapi.webGCS.domain.WayPointEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WayPointRepository extends JpaRepository<WayPointEntity, Integer> {
    @Query("select w from WayPointEntity w " +
            "join w.missionEntity m " +
            "where m.id=:mission_id " +
            "order by w.seq")
    List<WayPointEntity> findAllByMissionId(@Param("mission_id") Integer mission_id);

    @Transactional
    @Modifying
    @Query("update WayPointEntity w set w.seq=w.seq+1 " +
            "where w.missionEntity.id=:mission_id and w.seq>=:seq")
    void incrementSeqGreaterThan(@Param("mission_id") Integer missionId, @Param("seq") Integer seq);


    @Transactional
    @Modifying
    @Query("update WayPointEntity w set w.seq=w.seq-1 " +
            "where w.missionEntity.id=:mission_id and w.seq>=:seq")
    void decrementSeqGreaterThan(@Param("mission_id") Integer missionId, @Param("seq") Integer seq);

    @Transactional
    @Modifying
    @Query(value = "delete w from waypoint w " +
            "join mission m on m.id=w.mission_id " +
            "where m.id=:mission_id",nativeQuery = true)
    Integer deleteALLByMissionId(@Param("mission_id") Integer mission_id);
}
