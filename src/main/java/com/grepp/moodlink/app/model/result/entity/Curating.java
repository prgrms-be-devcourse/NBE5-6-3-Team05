package com.grepp.moodlink.app.model.result.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curating")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curating {
    @Id
    private String id;
    private String userId;
    private LocalDate curatingDate;

}
