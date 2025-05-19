package com.grepp.moodlink.app.model.data.music.code;

public enum MusicGenre {

    BALLAD("발라드"),
    DANCE("댄스"),
    HIPHOP("힙합"),
    RNB("R&B"),
    INDIE("인디음악"),
    ROCK_METAL("록/메탈"),
    TROT("트로트"),
    FOLK_BLUES("포크/블루스");

    private final String name;

    MusicGenre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
