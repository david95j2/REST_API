package com.example.restapi.file.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Getter @Setter
@Table(name = "image_location")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "pos_x")
    private Float posX;
    @Column(name = "pos_y")
    private Float posY;
    @Column(name = "pos_z")
    private Float posZ;
    private Float roll;
    private Float pitch;
    private Float yaw;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "img_id")
    private ImageEntity imageEntity;
}
