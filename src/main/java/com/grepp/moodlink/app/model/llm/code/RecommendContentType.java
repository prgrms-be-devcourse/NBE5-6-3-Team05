package com.grepp.moodlink.app.model.llm.code;

public enum RecommendContentType {
    MOVIE("영화"),
    BOOK("도서"),
    MUSIC("음악");

    private final String name;

    RecommendContentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
