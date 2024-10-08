package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;

@RequiredArgsConstructor
@Repository
public class TodoDslRepositoryImpl implements TodoDslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.managers, manager).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne()
        );
    }

    @Override
    public Page<Todo> findAllByTitleAndNickName(Pageable pageable, String title, String nickName, LocalDateTime startDate, LocalDateTime endDate) {
        List<Todo> todoList = jpaQueryFactory
                .selectFrom(todo)
                .join(todo.managers, manager).fetchJoin()
                .where(
                        todo.title.contains(title),
                        manager.user.nickName.contains(nickName),
                        todo.createdAt.between(startDate, endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(todo)
                .fetchOne();

        return new PageImpl<>(todoList, pageable, total);
    }

    @Override
    public Page<Todo> findAllByTitle(Pageable pageable, String title, LocalDateTime startDate, LocalDateTime endDate) {
        List<Todo> todoList = jpaQueryFactory
                .select(todo)
                .from(todo)
                .join(todo.managers, manager).fetchJoin()
                .where(
                        todo.title.contains(title),
                        todo.createdAt.between(startDate, endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(todo)
                .fetchOne();

        return new PageImpl<>(todoList, pageable, total);
    }

    @Override
    public Page<Todo> findAllByNickName(Pageable pageable, String nickName, LocalDateTime startDate, LocalDateTime endDate) {
        List<Todo> todoList = jpaQueryFactory
                .select(todo)
                .from(todo)
                .join(todo.managers, manager).fetchJoin()
                .where(
                        manager.user.nickName.contains(nickName),
                        todo.createdAt.between(startDate, endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(todo)
                .fetchOne();

        return new PageImpl<>(todoList, pageable, total);
    }
}
