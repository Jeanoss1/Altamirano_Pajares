package com.altamirano_pajares.exception;

// 404: el recurso solicitado no existe
public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(String message) {
        super(message);
    }
}
