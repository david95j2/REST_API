package com.example.restapi.file.domain;

import com.example.restapi.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pcd")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PcdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "pcd_path")
    private String pcdPath;
    @Column(name = "pcd_name")
    private String pcdName;
    @Column(name = "pcd_type")
    private String pcdType;
    @Column(name = "pcd_location")
    private String pcdLocation;
    @Column(name = "pcd_regdate")
    private String pcdRegdate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public GetFileRes toGetFileRes() {
        GetFileRes getFileRes = new GetFileRes();
        getFileRes.setFile_name(this.pcdName);
        getFileRes.setFile_type(this.pcdType);
        getFileRes.setFile_location(this.pcdLocation);
        getFileRes.setFile_regdate(this.pcdRegdate);
        getFileRes.setUser_loginId(this.userEntity.getLoginId());

        return getFileRes;
    }
}
