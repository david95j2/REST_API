package com.example.restapi.file.image.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "img_path")
    private String imgPath;
    @Column(name = "regdate")
    private String regdate;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "img_group_id")
    private ImageGroupEntity imageGroupEntity;

    @JsonManagedReference
    @OneToOne(mappedBy = "imageEntity")
    private LocationEntity locationEntity;

    public GetImageRes toGetImageRes() {
        GetImageRes dto = new GetImageRes();
        dto.setFile_location(this.imageGroupEntity.getMapDateEntity().getMapGroupEntity().getLocation()); // Assuming this is the location
        dto.setFile_regdate(this.regdate);

        if(this.locationEntity != null) { // Assuming locationEntity could be null
            dto.setPos_x(this.locationEntity.getPosX());
            dto.setPos_y(this.locationEntity.getPosY());
            dto.setPos_z(this.locationEntity.getPosZ());
            dto.setRoll(this.locationEntity.getRoll());
            dto.setPitch(this.locationEntity.getPitch());
            dto.setYaw(this.locationEntity.getYaw());
        }

        return dto;
    }

}
