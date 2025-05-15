package com.grepp.moodlink.app.model.result.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curating_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuratingDetail {

    @Id
    private String id;
    private String curatingId;
    private String movieId;
    private String songId;
    private String bookId;

}
