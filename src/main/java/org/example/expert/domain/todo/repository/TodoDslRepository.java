package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoDslRepository {

    Optional<Todo> findByIdWithUser(Long todoId);

    Page<Todo> findAllByTitleAndNickName(Pageable pageable, String title, String nickName, LocalDateTime startDate, LocalDateTime endDate);

    Page<Todo> findAllByTitle(Pageable pageable, String title, LocalDateTime startDate, LocalDateTime endDate);

    Page<Todo> findAllByNickName(Pageable pageable, String nickName, LocalDateTime startDate, LocalDateTime endDate);
}
