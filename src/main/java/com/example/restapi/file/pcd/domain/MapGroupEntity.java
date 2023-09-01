package com.example.restapi.file.pcd.domain;

import com.example.restapi.file.image.domain.LocationEntity;
import com.example.restapi.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "map_group")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MapGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String location;
    private Float latitude;
    private Float longitude;
    @Column(name = "map_group_regdate")
    private String regdate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "mapGroupEntity")
    private List<MapGroupSampleEntity> mapGroupSampleEntites;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "mapGroupEntity")
    private List<MapEntity> mapEntities;
}
