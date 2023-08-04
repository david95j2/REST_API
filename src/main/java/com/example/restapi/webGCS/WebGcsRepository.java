package com.example.restapi.webGCS;

import com.example.restapi.webGCS.domain.WebGcsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebGcsRepository extends JpaRepository<WebGcsEntity, Integer> {
}
