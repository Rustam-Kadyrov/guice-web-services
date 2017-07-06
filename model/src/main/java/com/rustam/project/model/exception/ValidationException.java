package com.rustam.project.model.exception;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
