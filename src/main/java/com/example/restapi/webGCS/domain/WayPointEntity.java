package com.example.restapi.webGCS.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "waypoint")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class WayPointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private Integer seq;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private String wait;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "mission_id")
    private MissionEntity missionEntity;

    public static WayPointEntity from(PostWayPointReq postWayPointReq, MissionEntity missionEntity) {
        return WayPointEntity.builder()
                .seq(postWayPointReq.getSeq())
                .latitude(postWayPointReq.getLatitude())
                .longitude(postWayPointReq.getLongitude())
                .altitude(postWayPointReq.getAltitude())
                .wait(postWayPointReq.getWait())
                .missionEntity(missionEntity)
                .build();
    }
}
