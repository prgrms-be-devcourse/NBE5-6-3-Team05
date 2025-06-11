package com.grepp.moodlink.app.controller.api.worldcup;


import com.grepp.moodlink.app.controller.api.worldcup.payload.WorldcupIdsRequest;
import com.grepp.moodlink.app.controller.api.worldcup.payload.WorldcupPlayRequest;
import com.grepp.moodlink.app.model.worldcup.WorldcupService;
import com.grepp.moodlink.app.model.worldcup.entity.Worldcup;
import com.grepp.moodlink.app.model.worldcup.entity.WorldcupItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/worldcup")
public class WorldcupApiController {

    private final WorldcupService worldcupService;

    // 월드컵 페이지에 월드컵들 출력
    @GetMapping("/list")
    public List<Worldcup> getAllWorldcups() {
        return worldcupService.findAll();
    }

    // 월드컵 생성 시 중복확인(컨텐츠들의 집합, 월드컵의 제목) 후 생성
    @PostMapping("/create")
    public ResponseEntity<Boolean> createWorldcup(
        @RequestBody WorldcupIdsRequest worldcupIdsRequest) {
        boolean result = worldcupService.createWorldcup(worldcupIdsRequest);
        return ResponseEntity.ok(result);
    }

    // 월드컵 id의 contents들 가져오기
    @GetMapping("/{id}/items")
    public List<WorldcupItem> getWorldcupContentIds(@PathVariable("id") Long id) {
        return worldcupService.findByWorldcupId(id);
    }

    @PostMapping("/result")
    public void WorldcupPlayResult(@RequestBody WorldcupPlayRequest worldcupPlayRequest) {
        worldcupService.updateItemsAddPlay(worldcupPlayRequest.getTournament(),
            worldcupPlayRequest.getWorldcupId());
    }

}