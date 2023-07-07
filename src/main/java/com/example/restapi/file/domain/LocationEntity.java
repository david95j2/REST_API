package com.example.restapi.file.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Getter @Setter
@Table(name = "location")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float pos_x;
    private Float pos_y;
    private Float pos_z;
    private Float roll;
    private Float pitch;
    private Float yaw;

    @OneToOne
    @JoinColumn(name = "img_id")
    private ImageEntity imageEntity;
}
