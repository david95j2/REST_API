package com.example.restapi.file.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
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
    private String img_path;
    private String img_name;
    private String img_type;
    private String img_regdate;


    @ManyToOne
    @JoinColumn(name = "pcd_id")
    private PcdEntity pcdEntity;

    @JsonIgnore
    @OneToOne(mappedBy = "imageEntity")
    private LocationEntity locationEntity;

    public GetImageRes toGetImageRes() {
        GetImageRes dto = new GetImageRes();
        dto.setFile_name(this.img_name);
        dto.setFile_type(this.img_type);
        dto.setFile_location(this.pcdEntity.getPcd_location()); // Assuming this is the location
        dto.setFile_regdate(this.img_regdate);

        if(this.locationEntity != null) { // Assuming locationEntity could be null
            dto.setPos_x(this.locationEntity.getPos_x());
            dto.setPos_y(this.locationEntity.getPos_y());
            dto.setPos_z(this.locationEntity.getPos_z());
            dto.setRoll(this.locationEntity.getRoll());
            dto.setPitch(this.locationEntity.getPitch());
            dto.setYaw(this.locationEntity.getYaw());
        }

        return dto;
    }

}
