package com.grepp.moodlink.app.model.recomend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like_detail_books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDetailBooks extends LikeDetailBase {


    private String bookId;

}