package com.example.restapi.file.image;

import com.example.restapi.file.image.domain.GetGroupImagesMapping;
import com.example.restapi.file.image.domain.GetImageMapping;
import com.example.restapi.file.image.domain.GetImagesMapping;
import com.example.restapi.file.image.domain.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    @Query("select i.id as id, mg.location as location, i.regdate as regdate," +
            " il.posX as posX, il.posY as posY, il.posZ as posZ," +
            " il.roll as roll, il.pitch as pitch, il.yaw as yaw " +
            "from ImageEntity i " +
            "join i.imageGroupEntity ig join i.locationEntity il join ig.mapGroupEntity mg " +
            "join mg.mapEntities m join mg.userEntity u " +
            "where i.id=:img_id and m.id=:map_id and u.loginId=:login_id")
    Optional<GetImageMapping> findByIdAndMapIdAndLoginId(
            @Param("img_id") int img_id, @Param("map_id") int map_id, @Param("login_id") String login_id);

    @Query("select i "+
            "from ImageEntity i " +
            "join i.imageGroupEntity ig join i.locationEntity il join ig.mapGroupEntity mg " +
            "join mg.mapEntities m join mg.userEntity u " +
            "where i.id=:img_id and m.id=:map_id and u.loginId=:login_id")
    Optional<ImageEntity> findPathByIdAndMapIdAndLoginId(
            @Param("img_id") int img_id, @Param("map_id") int map_id, @Param("login_id") String login_id);

//    @Query("select ig.id as id, ig.imgGroupName as groupName, count (*) as count," +
//            " mg.location as location, ig.imgGroupRegdate as regdate " +
//            "from ImageEntity i " +
//            "join i.imageGroupEntity ig join ig.mapGroupEntity mg join mg.mapEntities m join mg.userEntity u " +
//            "where m.id=:map_id and u.loginId=:login_id " +
//            "group by i.imageGroupEntity.id, mg.location")
//    List<GetImagesMapping> findAllByMapIdAndLoginId(@Param("map_id") int map_id, @Param("login_id") String login_id);

    @Query("select i.id as id, i.imgPath as fileName, i.regdate as regdate, i.locationEntity.posX as posX, i.locationEntity.posY as posY, i.locationEntity.posZ as posZ, " +
            "i.locationEntity.roll as roll , i.locationEntity.pitch as pitch , i.locationEntity.yaw as yaw " +
            "from ImageEntity i " +
            "join i.imageGroupEntity ig join ig.mapGroupEntity mg join mg.mapEntities m join mg.userEntity u " +
            "where m.id=:map_id and u.loginId=:login_id")
    List<GetImagesMapping> findAllByMapIdAndLoginId(@Param("map_id") int map_id, @Param("login_id") String login_id);

//    @Query("select i.id as id, i.imgPath as imgPath, i.regdate as regdate from ImageEntity i " +
//            "join i.imageGroupEntity ig join ig.mapGroupEntity mg join mg.mapEntities m join mg.userEntity u " +
//            "where m.id=:map_id and ig.id=:group_id and u.loginId=:login_id")
//    List<GetGroupImagesMapping> findAllGroupByMapIdAndLoginId(
//            @Param("map_id") int map_id, @Param("group_id")int group_id, @Param("login_id") String login_id);
}
