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
    private String pcd_path;
    private String pcd_name;
    private String pcd_type;
    private String pcd_location;
    private String pcd_regdate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
