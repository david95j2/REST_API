package com.example.restapi.file.domain;

import com.example.restapi.user.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(name = "map_name")
    private String mapName;
    @Column(name = "map_type")
    private String mapType;
    @Column(name = "map_count")
    private Integer mapCount;
    @Column(name = "map_area")
    private Integer mapArea;
    @Column(name = "regdate")
    private String regdate;
    @Column(name = "last_regdate")
    private String lastRegdate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @JsonManagedReference
    @OneToOne(mappedBy = "mapEntity")
    private MapInfoEntity mapInfoEntity;

    public GetFileRes toGetFileRes() {
        GetFileRes getFileRes = new GetFileRes();
        getFileRes.setFile_name(this.mapName);
        getFileRes.setFile_type(this.mapType);
        getFileRes.setMap_count(this.mapCount);
        getFileRes.setMap_area(this.mapArea);
        getFileRes.setFile_regdate(this.mapInfoEntity.getFileRegdate());
        getFileRes.setFile_location(this.mapInfoEntity.getLocation());
        getFileRes.setUser_loginId(this.userEntity.getLoginId());

        return getFileRes;
    }
}
