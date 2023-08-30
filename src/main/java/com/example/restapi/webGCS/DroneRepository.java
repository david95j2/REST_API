package com.example.restapi.webGCS;

import com.example.restapi.webGCS.domain.DroneEntity;
import com.example.restapi.webGCS.domain.MissionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<DroneEntity, Integer> {
    @Query("select d from DroneEntity d " +
            "join d.userEntity u where u.loginId=:login_id")
    List<DroneEntity> findAllByLoginId(@Param("login_id") String loginId);

    Optional<DroneEntity> findByIdAndUserEntityId(Integer drone_id, Integer user_id);

    @Transactional
    @Modifying
    @Query("update DroneEntity d set d.droneVoltageMin=:droneVoltageMin, d.droneVoltageMax=:droneVoltageMax " +
            "where d.id=:drone_id and d.userEntity.id=:user_id")
    Integer updateVoltage(@Param("drone_id") Integer drone_id, @Param("droneVoltageMin") float droneVoltageMin,
                       @Param("droneVoltageMax") float droneVoltageMax,@Param("user_id") Integer user_id);
}
