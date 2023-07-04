package com.example.restapi.file;

import com.example.restapi.file.domain.PcdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PcdRepository extends JpaRepository<PcdEntity, Integer> {

}
