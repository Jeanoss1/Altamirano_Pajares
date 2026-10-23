package com.altamirano_pajares.exception;

// 400: los datos tienen la forma correcta pero rompen una regla de negocio (duplicado, rango de fechas...)
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
