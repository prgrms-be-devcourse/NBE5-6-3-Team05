package com.grepp.moodlink.app.model.recomend.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class LikeDetailBase {


    private Long likesId;

}
