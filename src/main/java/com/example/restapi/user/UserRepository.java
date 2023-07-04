package com.example.restapi.user;

import com.example.restapi.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Modifying
    @Query("delete from UserEntity u where u.id= :id")
    void deleteUser(@Param("id") int id);

    Optional<UserEntity> findByLoginId(String loginId);
    Optional<UserEntity> findByPassword(String passwd);
    Optional<UserEntity> findByLoginIdAndPassword(String loginId, String passwd);
}
