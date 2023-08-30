package com.example.restapi.webGCS.domain;

import com.example.restapi.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drone")
@Getter @Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DroneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "drone_voltage_min")
    private float droneVoltageMin;
    @Column(name = "drone_voltage_max")
    private float droneVoltageMax;
    @Column(name = "drone_type")
    private String droneType;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
