package org.example.expert.domain.todo.repository;

//import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoDslRepository {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.modifiedAt BETWEEN :startDate AND :endDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.weather = :weather and t.modifiedAt BETWEEN :startDate AND :endDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtAndWeatherAndDateDesc(Pageable pageable, String weather, LocalDateTime startDate, LocalDateTime endDate);



    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.title LIKE %:title% AND u.nickName LIKE %:nickName% and t.createdAt BETWEEN :startDate AND :endDate")
    Page<Todo> findAllByTitleAndNickName(Pageable pageable, String title, String nickName, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.title LIKE %:title% and t.createdAt BETWEEN :startDate AND :endDate")
    Page<Todo> findAllByTitle(Pageable pageable, String title, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE u.nickName LIKE %:nickName% and t.createdAt BETWEEN :startDate AND :endDate")
    Page<Todo> findAllByNickName(Pageable pageable, String nickName, LocalDateTime startDate, LocalDateTime endDate);
}
