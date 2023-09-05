package com.example.restapi.file.image.domain;

import com.example.restapi.file.pcd.domain.MapEntity;
import com.example.restapi.file.pcd.domain.MapGroupEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "image_group")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
public class ImageGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "img_group_name")
    private String imgGroupName;
    private Integer distance;
    @Column(name = "img_group_regdate")
    private String imgGroupRegdate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "map_id")
    private MapEntity mapEntity;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "imageGroupEntity")
    private List<ImageEntity> imageEntity;
}
