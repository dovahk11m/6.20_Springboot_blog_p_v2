package com.tenco.blog.repository;

import com.tenco.blog.model.Board;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // IoC 대상
public class BoardNativeRepository {

//    // DI : 의존성 주입 : 스프링이 자동으로 객체를 주입
//    private BoardNativeRepository boardNativeRepository;
//    public BoardController(BoardNativeRepository boardNativeRepository) {
//        this.boardNativeRepository = boardNativeRepository;
//    }

    // JPA 의 핵심 인터페이스
    // 데이터 베이스와의 모든 작업을 담당
    private EntityManager em;

    //생성자 ! 위에 둘 것
    public BoardNativeRepository(EntityManager em) {
        this.em = em;
    }


    //게시글 업데이트 메서드
    @Transactional
    public void updateById(Long id, String title, String content, String username) {
        // update 쿼리, where 필수
        String sqlStr = " UPDATE board_tb SET title = ?, content = ?, username = ? " +
                " WHERE id = ? ";
        Query query = em.createNativeQuery(sqlStr);

        query.setParameter(1,title);
        query.setParameter(2,content);
        query.setParameter(3,username);
        query.setParameter(4,id);

        int updateRows = query.executeUpdate();
        System.out.println("수정된 행:"+updateRows);
    }




    //특정 게시글을 삭제하는 메서드
    @Transactional
    public void deleteById(Long id) {

        Query query = em.createNativeQuery("delete from board_tb where id = ? ");
        query.setParameter(1, id);

        //
        query.executeUpdate();
    }










    public Board findById(Long id) {

        String sqlStr = "select * from board_tb where id = ? ";
        Query query =  em.createNativeQuery(sqlStr, Board.class);

        // SQL Injection 방지 - 파라미터 바인딩
        // 직접 문자열을 연결하지 않고
        // ? 사용해서 안전하게 값을 전달하려는 것이다.

        query.setParameter(1, id);

        // query.getSingleResult() 단일 결과만 반환하는 메서드
        // 주의: null 리턴되면 예외발생 - 예외처리 해줘야 한다
        // 주의: 결과가 2개 나와도 NonUniqueResultException

        return (Board) query.getSingleResult();

    }








    // 게시글 목록 조회
    public List<Board> findAll() {
        // 쿼리 기술 --> 네이티브 쿼리
        // Board.class
        Query query = em.createNativeQuery("select * from board_tb order by id desc ", Board.class);
        // query.getResultList(); : 여러 행의 결과를 List 객체로 반환
        // 하나만? query.getSingleResult(); 단일 결과만 반환(한개의 row 데이터만 있을때)
//         List<Board> list = query.getResultList();
//         return list;
        return query.getResultList();
    }

    // 생성자를 확인해서 자동으로 Entity Manager 객체를 주입시킨다
    // DI 처리 Dependent injection

    @Transactional
    public void save(String username, String title, String content) {
        Query query = em.createNativeQuery(
                "insert into board_tb(username, title, content, created_at) " +
                        " values(?, ?, ?, now()) ");

        query.setParameter(1, username);
        query.setParameter(2, title);
        query.setParameter(3, content);

        query.executeUpdate();

    }


}
