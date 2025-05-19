package com.grepp.moodlink.infra.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtil {

    @Value("${upload.path}")
    private String uploadPath;

    public List<FileDto> upload(List<MultipartFile> files, String depth) throws IOException {
        List<FileDto> fileDtos = new ArrayList<>();

        if (files == null || files.isEmpty() || files.getFirst().isEmpty()) {
            return fileDtos;
        }
        String savePath = createSavePath(depth);

        for (MultipartFile file : files) {
            String originFileName = file.getOriginalFilename();
            String renameFileName = generateRenameFileName(originFileName);
            FileDto fileDto = new FileDto(originFileName, renameFileName, savePath);
            fileDtos.add(fileDto);
            uploadFile(file, fileDto);
        }

        return fileDtos;
    }

    private void uploadFile(MultipartFile file, FileDto fileDto) throws IOException {
        Path projectRoot = Paths.get("").toAbsolutePath();
        Path fullUploadPath = projectRoot.resolve(uploadPath).resolve(fileDto.savePath());

        File directory = fullUploadPath.toFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File target = new File(fullUploadPath.toFile(), fileDto.renameFileName());
        file.transferTo(target);
    }

    private String generateRenameFileName(String originFileName) {
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + ext;
    }

    private String createSavePath(String depth) {
        LocalDate now = LocalDate.now();
        return depth + "/" +
            now.getYear() + "/" +
            now.getMonth() + "/" +
            now.getDayOfMonth() + "/";
    }

}
