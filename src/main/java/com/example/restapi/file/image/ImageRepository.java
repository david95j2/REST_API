package com.example.restapi.file.image;

import com.example.restapi.file.image.domain.GetImageMapping;
import com.example.restapi.file.image.domain.GetImagesMapping;
import com.example.restapi.file.image.domain.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {

    @Query("select i "+
            "from ImageEntity i " +
            "join i.imageGroupEntity ig join i.locationEntity il " +
            "join ig.mapDateEntity md join md.mapGroupEntity mg join mg.userEntity u " +
            "where i.id=:img_id")
    Optional<ImageEntity> findPathByIdAndMapIdAndLoginId(@Param("img_id") int img_id);

    @Query("select ig.id as id, ig.imgGroupName as groupName, count (*) as count," +
            " mg.location as location, ig.imgGroupRegdate as regdate " +
            "from ImageEntity i " +
            "join i.imageGroupEntity ig join ig.mapDateEntity md " +
            "join md.mapGroupEntity mg join mg.userEntity u " +
            "where md.id=:date_id " +
            "group by i.imageGroupEntity.id, mg.location")
    List<GetImageMapping> findAllByMapIdAndLoginId(@Param("date_id") int date_id);


    @Query("select i.id as id, i.imgPath as fileName, i.regdate as regdate, " +
            "il.posX as posX, il.posY as posY, il.posZ as posZ, il.roll as roll, il.pitch as pitch, il.yaw as yaw " +
            "from ImageEntity i join i.locationEntity il " +
            "join i.imageGroupEntity ig join ig.mapDateEntity md " +
            "join md.mapGroupEntity mg join mg.userEntity u " +
            "where ig.id=:group_id")
    List<GetImagesMapping> findAllGroupByMapIdAndLoginId(@Param("group_id")int group_id);
}
