package com.example.restapi.file.pcd.domain;

import com.example.restapi.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "map_group_date")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MapDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "map_date")
    private String date;
    @Column(name = "map_time")
    private String time;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "map_group_id")
    private MapGroupEntity mapGroupEntity;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "mapDateEntity")
    private List<MapEntity> mapEntities;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "mapDateEntity")
    private List<MapGroupSampleEntity> mapGroupSampleEntities;
}
