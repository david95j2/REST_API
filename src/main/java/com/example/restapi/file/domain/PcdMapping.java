package com.example.restapi.file.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;



public interface PcdMapping {
    Integer getPcd_id();
    String getPcd_path();
    String getPcd_name();
    String getPcd_type();
    String getPcd_location();
    String getPcd_regdate();

    Integer getUser_id();
}
