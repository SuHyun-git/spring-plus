package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.GetTodosRequest;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    public Page<TodoResponse> getTodos(int page, int size, GetTodosRequest getTodosRequest) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Todo> todos;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        /**
         * 시작 날짜에 따라 만약 없다면 모든 데이터의 초기 날짜를 입력하고 값이 있다면 getTodosRequest에서 받은 String데이터를 LocalDateTime으로 변경한다.
         * 종료 날짜도 위와 같이 한다. 종료 날짜에 데이터가 없는 경우 현재 시간을 입력한다.
         */
        if (getTodosRequest.getStartDate() != null) {
            startDate = LocalDateTime.of(Integer.parseInt(getTodosRequest.getStartDate().substring(0, 4)), Integer.parseInt(getTodosRequest.getStartDate().substring(5, 7)), Integer.parseInt(getTodosRequest.getStartDate().substring(8, 10)), 0, 0, 0);
        } else {
            startDate = LocalDateTime.of(2020, 1, 31, 0, 0, 0);
        }

        if (getTodosRequest.getEndDate() != null) {
            endDate = LocalDateTime.of(Integer.parseInt(getTodosRequest.getEndDate().substring(0, 4)), Integer.parseInt(getTodosRequest.getEndDate().substring(5, 7)), Integer.parseInt(getTodosRequest.getEndDate().substring(8, 10)), 23, 59, 59);
        } else {
            endDate = LocalDateTime.now();
        }

        /**
         * 날씨에 null인 경우 날씨를 제외하고 검색하고 null이 아닌 경우 날씨도 포함해서 검색합니다.
         */
        if (getTodosRequest.getWeather() != null ) {
            todos = todoRepository.findAllByOrderByModifiedAtAndWeatherAndDateDesc(pageable, getTodosRequest.getWeather(), startDate, endDate);
        } else {
            todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable, startDate, endDate);
        }

        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }
}
