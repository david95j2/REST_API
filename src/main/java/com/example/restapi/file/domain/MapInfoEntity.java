package com.example.restapi.file.domain;

import com.example.restapi.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "map_info")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MapInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String location;
    @Column(name = "file_regdate")
    private String fileRegdate;
    private String structure;
    private Integer distance;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "map_id")
    private MapEntity mapEntity;
}