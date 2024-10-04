package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TodoDslRepositoryImpl implements TodoDslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        QTodo todo = QTodo.todo;  // QueryDSL에서 생성된 QTodo 클래스
        QUser user = QUser.user;  // QueryDSL에서 생성된 QUser 클래스

        Todo result = jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()  // LEFT JOIN과 FETCH JOIN을 사용
                .where(todo.id.eq(todoId))  // 조건: Todo의 ID가 todoId인 경우
                .fetchOne();  // 단일 결과를 반환

        return Optional.ofNullable(result);
    }
}
