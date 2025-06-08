package com.grepp.moodlink.app.model.data.book.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_genre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String name;
    Boolean activated = true;

    public BookGenre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void unActivated() {
        this.activated = false;
    }

}
