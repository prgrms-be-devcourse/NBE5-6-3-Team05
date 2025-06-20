package com.grepp.moodlink.app.controller.api.book;


import com.grepp.moodlink.app.model.data.book.BookService;
import com.grepp.moodlink.app.model.data.book.dto.BookDto;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/book")
public class BookApiController {

    private final BookService bookService;

    // worldcup 생성 시 도서 목록 전달(추후 LLM으로 변경 예정)
    @GetMapping("/worldcup")
    public ResponseEntity<List<Map<String,Object>>> getBooks(){
        return ResponseEntity.ok(bookService.getBookList());
    }

    // 해당 월드컵 실행 시 로드할 월드컵 컨텐츠 상세정보들 전달(title, image, 등등)
    @PostMapping("/details")
    public ResponseEntity<Map<String, BookDto>> getDetails(@RequestBody List<String> strings) {
        Map<String, BookDto> response;
        response = bookService.getDetails(strings);
        return ResponseEntity.ok(response);
    }

}
