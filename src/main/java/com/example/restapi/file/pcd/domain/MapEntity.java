package com.example.restapi.file.pcd.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "map")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "map_path")
    private String mapPath;
    @Column(name = "map_count")
    private Integer mapCount;
    @Column(name = "map_area")
    private Integer mapArea;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "map_group_date_id")
    private MapDateEntity mapDateEntity;

    public GetFileRes toGetFileRes() {
        GetFileRes getFileRes = new GetFileRes();
        getFileRes.setMap_count(this.mapCount);
        getFileRes.setMap_area(this.mapArea);
        getFileRes.setFile_location(this.mapDateEntity.getMapGroupEntity().getLocation());
        getFileRes.setUser_loginId(this.mapDateEntity.getMapGroupEntity().getUserEntity().getLoginId());
        return getFileRes;
    }
}
