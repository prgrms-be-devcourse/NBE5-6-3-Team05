package com.grepp.moodlink.infra.util.file;

public record FileDto(
    String originFileName,
    String renameFileName,
    String savePath
) {

}
