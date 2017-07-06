package com.rustam.project.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Rustam Kadyrov on 25.06.2017.
 */
@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest {
    private String name;

    @Builder
    public CreateUserRequest(String name) {
        this.name = name;
    }
}
