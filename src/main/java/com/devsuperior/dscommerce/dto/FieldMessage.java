package com.devsuperior.dscommerce.dto;

public class FieldMessage {

    private String field;
    private String message;

    public FieldMessage(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String fieldMessage) {
        this.field = fieldMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
