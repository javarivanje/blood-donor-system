package com.bds.exception;

import java.util.Objects;
import java.util.Set;

public class ObjectNotValidException extends RuntimeException{

    private final Set<String> errorMessages;

    public ObjectNotValidException(Set<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Set<String> getErrorMessages() {
        return errorMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectNotValidException that = (ObjectNotValidException) o;
        return Objects.equals(errorMessages, that.errorMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessages);
    }

    @Override
    public String toString() {
        return "ObjectNotValidException{" +
                "errorMessages=" + errorMessages +
                '}';
    }
}
