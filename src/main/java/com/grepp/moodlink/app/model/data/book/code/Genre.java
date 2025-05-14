package com.grepp.moodlink.app.model.data.book.code;

public enum Genre {

    FAIRY_TALE("동화"),
    LIGHT_NOVEL("라이트노벨"),
    MARTIAL_ARTS("무협소설"),
    ESSAY("수필"),
    THRILLER("스릴러"),
    POETRY("시집"),
    DETECTIVE("추리소설"),
    FANTASY("판타지소설"),
    PROGRAMMING("프로그래밍"),
    HUMANITIES("인문학"),
    PSYCHOLOGY("심리학"),
    PHILOSOPHY("철학"),
    ANTHROPOLOGY("문화인류학"),
    RELIGION("종교학"),
    KOREAN_NOVEL("한국소설"),
    FOREIGN_NOVEL("외국소설"),
    CLASSIC_LITERATURE("고전문학"),
    MODERN_LITERATURE("현대문학"),
    SOCIOLOGY("사회학");

    private final String name;

    Genre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
