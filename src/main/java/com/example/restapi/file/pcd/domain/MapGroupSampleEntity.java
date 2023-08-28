package com.example.restapi.file.pcd.domain;

import com.example.restapi.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "map_group_sample")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MapGroupSampleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "map_sample_path")
    private String mapSamplePath;
    @Column(name = "map_sample_regdate")
    private String mapSampleRegdate;
    @Column(name = "map_sample_last_regdate")
    private String mapSampleLastRegdate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "map_group_id")
    private MapGroupEntity mapGroupEntity;
}
