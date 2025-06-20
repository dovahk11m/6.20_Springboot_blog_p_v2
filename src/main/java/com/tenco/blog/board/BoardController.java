package com.tenco.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller // IoC 대상 - 싱글톤 패턴 관리
public class BoardController {

    //생성자 의존 주입 = DI 처리
    private final BoardPersistRepository br;

    /*게시글 삭제 수행처리
    주소설계 Get http://localhost:8080/delete */
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Long id,
                         HttpServletRequest request) {
        br.deleteById(id);

        //PRG 패턴
        return "redirect:/";
    }

    /*게시글 수정화면 요청처리
    주소설계 Get http://localhost:8080/update-form */
    @GetMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Long id,
                             HttpServletRequest request) {
        Board board = br.findById(id);
        request.setAttribute("board", board);
        return "board/update-form";
    }

    /*게시글 수정화면 수행처리
    주소설계 POST http://localhost:8080/update */
    @PostMapping("/board/{id}/update-form")
    public String update(@PathVariable(name = "id") Long id,@RequestParam(name = "title") String title,
                         @RequestParam(name = "content") String content,
                         @RequestParam(name = "username") String username,
                         HttpServletRequest request) {
        br.updatedById(id, title, content, username);

        //PRG 패턴
        return "redirect:/board/" +id;
    }


    /* 게시글 클릭해 상세보기
    주소설계 GET http://localhost:8080/detail/3 */
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, HttpServletRequest request) {

        Board board = br.findById(id);
        request.setAttribute("board", board);

        //1차캐시 효과, DB에 접근하지 않고 바로 꺼냄

        return "board/detail";
    }

    //prefix = classpath:/templates/
    //suffix = .mustache
    //return = board/detail


    //index.mustache 파일을 반환시키는 기능
    //주소: http://localhost:8080, http://localhost:8080/index
    @GetMapping({"/", "/index"})
    public String boardList(HttpServletRequest request) {

        List<Board> boardList = br.findAll(); //DB select 결과 호출
        request.setAttribute("boardList", boardList); //뷰에 전달
        return "index";
    }

    //게시글 작성 화면 요청 처리
    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    //게시글 작성 화면 수행 처리 - DB에 값 넣기
    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO reqDTO) {

        // reqDTO 에는 사용자가 던진 값이 다 들어가 있다.
        // reqDTO 를 Board로 만들어서 DB에 넣어야 한다
        // board = new Board(reqDTO.getTitle(), reqDTO.getContent(), reqDTO.getUsername());
        Board board = reqDTO.toEntity();
        br.save(board);

        //PRG 패턴
        return "redirect:/";
    }

}//
