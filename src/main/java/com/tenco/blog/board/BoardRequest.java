package com.tenco.blog.board;

/* 😎 클라이언트로부터 넘어온 데이터를
Object 로 변환해 전달하는 DTO 역할을 담당한다. */

import lombok.Data;

public class BoardRequest {

    // 정적 내부 클래스로 기능별 DTO를 관리한다
    //BoardRequest.saveDTO..
    //게시글 저장 요청 데이터
    @Data
    public static class SaveDTO {
        private String title;
        private String content;
        private String username;

       /* DTO에서 Entity로 변환하는 메서드를 만든다
       이는 계층간 데이터 변환을 명확하게 분리하기 위해서다 */

        public Board toEntity() {
            return new Board(title, content, username);
        }

    }//

}
