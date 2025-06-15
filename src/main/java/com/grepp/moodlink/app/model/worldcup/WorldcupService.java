package com.grepp.moodlink.app.model.worldcup;

import com.grepp.moodlink.app.controller.api.worldcup.payload.WorldcupIdsRequest;
import com.grepp.moodlink.app.controller.api.worldcup.payload.WorldcupPlayResponse;
import com.grepp.moodlink.app.model.worldcup.code.ContentType;
import com.grepp.moodlink.app.model.worldcup.entity.Worldcup;
import com.grepp.moodlink.app.model.worldcup.entity.WorldcupItem;
import com.grepp.moodlink.app.model.worldcup.entity.WorldcupPlay;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.codec.digest.DigestUtils;

@Service
@RequiredArgsConstructor
public class WorldcupService {

    private final WorldcupRepository worldcupRepository;
    private final WorldcupItemRepository worldcupItemRepository;
    private final WorldcupPlayRepository worldcupPlayRepository;

    // 각 컨텐츠의 첫글자를 제외한 id는 모두 정수형으로 casting이 가능
    // 정수로 casting한 값을 비교하여 오름차순 정렬한 List 반환
    private List<String> sorting(List<String> id) {
        return id.stream()
            .sorted(Comparator.comparingInt(s -> Integer.parseInt(s.substring(1))))
            .collect(Collectors.toList());
    }

    // Worldcup의 각 contents들의 insert 쿼리문들을 던지는 메서드
    private void insertWorldcupContents(long worldcupId, List<String> ids) {
        for (String id: ids){
            WorldcupItem worldcupItem = WorldcupItem.builder()
                .contentId(id)
                .worldcupId(worldcupId)
                .build();
            worldcupItemRepository.save(worldcupItem);
        }
    }

    //Worldcup 하나에 대해 insert 쿼리문을 던지는 메서드
    private long insertWorldcup(String title, String contentType, String hashValue) {
        Worldcup worldcup = Worldcup.builder()
            .title(title)
            .contentType(ContentType.valueOf(contentType))
            .hashCode(hashValue)
            .userId(null)
            .createdAt(LocalDate.now())
            .build();
        worldcupRepository.save(worldcup);
        return worldcup.getId();
    }

    // 기존의 worldcup들과 중복확인 후, 중복이면 false 반환, 아니면 insert 후 true 반환
    @Transactional
    public boolean createWorldcup(WorldcupIdsRequest worldcupIdsRequest) {

        StringBuilder stringBuilder = new StringBuilder();

        // 입력받은 15개의 컨텐츠들을 오름차순으로 sorting
        for (String str: sorting(worldcupIdsRequest.getId())){
            // 컨텐츠들마다 ","을 뒤에 붙이고 하나의 긴 문자열 생성
            stringBuilder.append(str).append(",");
        }
        // 긴 문자열을 sha256으로 해시 값 생성 후 기존의 월드컵들과 중복확인
        // sha256이 내장되어 있는 외부 라이브러리 활용(dependency 추가)
        String hashValue = DigestUtils.sha256Hex(stringBuilder.toString());
        List<Worldcup> existing = findAll();

        for (Worldcup worldcup : existing){
            // 중복된 hash값이 있으면 return false
            // 중복된 title이 있으면 return false
            if(worldcup.getHashCode().equals(hashValue) ||
                worldcup.getTitle().equals(worldcupIdsRequest.getTitle())) return false;
        }

        long worldcupId = insertWorldcup(worldcupIdsRequest.getTitle(),worldcupIdsRequest.getContentType(),hashValue);
        insertWorldcupContents(worldcupId, worldcupIdsRequest.getId());

        return true;
    }

    // 월드컵들 list 가져오기
    public List<Worldcup> findAll() {
        return worldcupRepository.findAll();
    }

    // 월드컵의 컨텐츠들 list 가져오기
    public WorldcupPlayResponse findByWorldcupId(Long id) {
        // Worldcup의 컨텐츠들을 DTO로 변환
        List<WorldcupItem> items = worldcupItemRepository.findByWorldcupId(id);

        // 해당 월드컵의 Content 타입 가져오기
        ContentType contentType = worldcupRepository.getWorldcupById(id).getContentType();

        // item 마다 해당 월드컵에서 우승한 횟수 가져오기
        Map<String, Long> itemWinCountMap = items.stream()
            .collect(Collectors.toMap(
                WorldcupItem::getContentId,
                item -> worldcupPlayRepository.countByWinnerIdAndWorldcupId(item.getContentId(), id)
            ));

        return new WorldcupPlayResponse(items, contentType, itemWinCountMap);
    }

    // 월드컵 수행 후, 컨텐츠들의 winCount, totalCount 최신화
    // 월드컵 수행 후, play테이블에 record 하나 추가
    @Transactional
    public void updateItemsAddPlay(List<String> tournament, String worldcupId) {
        insertWorldcupPlay(worldcupId, tournament.getFirst());
        updateItems(worldcupId,tournament);
    }

    // Contents들의 각각 totalCount와 winCount 최신화
    private void updateItems(String worldcupId, List<String> tournament) {
        Long tempWorldcupId = Long.parseLong(worldcupId);
        for (int i = 0; i < tournament.size(); i++) {
            WorldcupItem item = worldcupItemRepository
                .findWorldcupItemByContentIdAndWorldcupId(tournament.get(i),tempWorldcupId);

            int winAdd = 0;
            int totalAdd;

            if (i % 16 == 8) {
                winAdd = 3;
                totalAdd =4;
            }
            else if (i % 8 == 4) {
                winAdd = 2;
                totalAdd = 3;
            }
            else if (i % 4== 2){
                winAdd = 1;
                totalAdd = 2;
            }
            else if(i % 2 == 1){
                totalAdd = 1;
            }
            else{
                winAdd = 4;
                totalAdd = 4;
            }

            item.setWinCount(item.getWinCount() + winAdd);
            item.setTotalCount(item.getTotalCount() + totalAdd);

        }
    }

    // worldcup_play테이블에 행 하나 추가
    private void insertWorldcupPlay(String worldcupId, String first) {
        WorldcupPlay worldcupPlay = WorldcupPlay.builder()
            .worldcupId(Long.parseLong(worldcupId))
            .userId(null)
            .winnerId(first)
            .createdAt(LocalDate.now())
            .build();
        worldcupPlayRepository.save(worldcupPlay);
    }

}
