package com.grepp.moodlink.app.model.llm.code;

public enum ContentType {
    MOVIE("영화"),
    BOOK("도서"),
    SONG("음악");

    private final String name;

    ContentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
