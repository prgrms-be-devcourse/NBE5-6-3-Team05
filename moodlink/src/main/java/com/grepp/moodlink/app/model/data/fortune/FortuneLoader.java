package com.grepp.moodlink.app.model.data.fortune;

import com.grepp.moodlink.app.model.data.fortune.entity.Fortune;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FortuneLoader implements CommandLineRunner {

    private final FortuneRepository fortuneRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("fortune.txt"), StandardCharsets.UTF_8);
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                Fortune fortune = new Fortune();
                fortune.setContent(line.trim());
                fortuneRepository.save(fortune);
            }
        }
    }
}
