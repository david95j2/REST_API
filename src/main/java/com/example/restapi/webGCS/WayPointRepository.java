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
            "join w.missionEntity m join m.userEntity u " +
            "where m.id=:mission_id and u.id=:user_id " +
            "order by w.seq")
    List<WayPointEntity> findAllByMissionIdAndLoginId(@Param("mission_id") Integer mission_id, @Param("user_id") Integer user_id);

    @Transactional
    @Modifying
    @Query("update WayPointEntity w set w.seq=w.seq+1 " +
            "where w.missionEntity.id=:mission_id and w.seq>=:seq")
    void incrementSeqGreaterThan(@Param("mission_id") Integer missionId, @Param("seq") Integer seq);

    @Transactional
    @Modifying
    @Query(value = "delete w from waypoint w " +
            "join mission m on m.id=w.mission_id " +
            "join user u on u.id=m.user_id " +
            "where w.id=:id and m.id=:mission_id " +
            "and u.id=:user_id",nativeQuery = true)
    Integer deleteByIdAndUserIdAndMissionId(@Param("id") Integer id, @Param("mission_id") Integer mission_id,
                                            @Param("user_id") Integer user_id);

    @Transactional
    @Modifying
    @Query("update WayPointEntity w set w.seq=w.seq-1 " +
            "where w.missionEntity.id=:mission_id and w.seq>=:seq")
    void decrementSeqGreaterThan(@Param("mission_id") Integer missionId, @Param("seq") Integer seq);

    @Transactional
    @Modifying
    @Query(value = "delete w from waypoint w " +
            "join mission m on m.id=w.mission_id " +
            "join user u on u.id=m.user_id " +
            "where m.id=:mission_id and u.id=:user_id",nativeQuery = true)
    Integer deleteALLByUserIdAndMissionId(@Param("mission_id") Integer mission_id,@Param("user_id") Integer user_id);
}
