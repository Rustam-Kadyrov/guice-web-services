package com.rustam.project.model.exception;

/**
 * Created by Rustam Kadyrov on 06.07.2017.
 */
public class NotFoundException extends ValidationException {

    public NotFoundException(String message) {
        super(message);
    }
}
