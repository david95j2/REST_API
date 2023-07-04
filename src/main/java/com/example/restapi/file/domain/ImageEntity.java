package com.example.restapi.file.domain;

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
}
