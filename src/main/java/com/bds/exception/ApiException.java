package com.bds.exception;

import org.springframework.http.HttpStatus;
import java.time.ZonedDateTime;
import java.util.Set;

public record ApiException(Set<String> message,
                           HttpStatus httpStatus,
                           ZonedDateTime zonedDateTime) {
}
