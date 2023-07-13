package com.example.restapi.file.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public interface GetPcdInfoMapping {
    String getUserEntityLoginId();
    String getPcdName();
    String getPcdLocation();
    String getPcdRegdate();
}
