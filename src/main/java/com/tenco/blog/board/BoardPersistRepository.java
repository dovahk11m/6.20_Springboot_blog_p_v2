package com.tenco.blog.board;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor // 필수 멤버변수 확인해서 생성자 꽂아준다
@Repository // IoC - 싱글톤 패턴 관리
public class BoardPersistRepository {

    /* JPA 핵심 인터페이스
    em이 두개면 안되니까 final로 선언해준다
    #성능향상 #안정성 #동기화*/

    private final EntityManager em;

    //JPQL을 사용한 게시글 목록 조회
    //JPQL이란 엔티티 객체를 대상으로 하는 객체지향 쿼리
    //Board는 엔티티 클래스명, b는 별칭
    public List<Board> findAll() {

        String jpql = " SELECT b FROM Board b ORDER BY b.id DESC ";

        //Query query = em.createQuery(jpql, Board.class);
        //List<Board> boardList = query.getResultList();
        //return boardList;
        return em.createQuery(jpql, Board.class).getResultList();
    }

    //게시글 저장기능 - 영속성 컨텍스트로
    @Transactional
    public Board save(Board board) {

        em.persist(board);
        return board;
    }

    /* 1.매개변수로 받은 board는 현재 [비영속] 상태다
    다시 말해 아직 영속성 컨텍스트로 관리되지 않는다
    데이터베이스와 연관 없는 순수한 [자바객체] 상태다

    2.em.persist(board);
    이 엔티티를 영속성 컨텍스트에 저장한다
    영속성 컨텍스트가 board 객체를 관리한다

    3.트랜잭션 커밋 시점에 Insert 쿼리가 실행된다
    이때 영속성컨텍스트 변경사항은 DB에 자동 반영된다
    board 객체의 id 필드에 DB 에서 자동 생성된 id 값을 가져온다

    4.영속상태가 된 board 객체를 반환한다
    이 시점에는 자동으로 board id 멤버변수에 DB PK 값이 할당된 상태다
     */
}
