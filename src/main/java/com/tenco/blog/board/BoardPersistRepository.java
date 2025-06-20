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

    //게시글 삭제기능 - 영속성 컨텍스트로
    @Transactional
    public void deleteById(Long id) {
        em.remove(em.find(Board.class, id));
    }

    //게시글 수정
    //기본키 조회 em.find()
    @Transactional
    public void updatedById(Long id, String title, String content, String username) {

        Board board = em.find(Board.class, id);

        board.setTitle(title);
        board.setContent(content);
        board.setUsername(username);
    }

    //게시글 1건 조회
    //네이티브 쿼리
    //em.find() 👍 기본키 조회는 이게 낫다
    //JPQL
    public Board findById(Long id) {
        //1차캐시 활용
        //Board board = em.find(Board.class, id);
        //return board;
        return em.find(Board.class, id);
    }

    //JPQL 사용한 조회방법(학습용)
    public Board findByIdJPQL(Long id) {

        //네임드 파라미터 권장
        String jpql = " SELECT b FROM Board b WHERE b.id = :id ";

        //Query query = em.createQuery(jpql, Board.class);
        //query = query.setParameter("id", id);
        //Board board = (Board) query.getSingleResult();

        try {
            //주의: 결과가 없으면 예외 NoResultException
            return em.createQuery(jpql, Board.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    /* JPQL의 단점
    1차캐시 우회하여 항상 DB 접근
    코드가 복잡해질 수 있다.
    getSingleRuselt() 때문에 예외처리 필요
     */


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
