package com.rustam.project.model.response;

/**
 * Created by Rustam Kadyrov on 25.06.2017.
 */

public class MessageResponse<T> {

    private T message;

    public MessageResponse() {
    }

    public MessageResponse(T message) {
        this.message = message;
    }

    public T getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "message=" + message +
                '}';
    }
}
