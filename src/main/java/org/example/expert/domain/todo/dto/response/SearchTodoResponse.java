package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

@Getter
public class SearchTodoResponse {

    private final String title;
    private final int managersCount;
    private final int commentsCount;

    public SearchTodoResponse(String title, int managersCount, int commentsCount) {
        this.title = title;
        this.managersCount = managersCount;
        this.commentsCount = commentsCount;
    }
}
