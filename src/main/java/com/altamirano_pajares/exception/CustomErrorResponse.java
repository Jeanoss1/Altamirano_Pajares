package com.altamirano_pajares.exception;

import java.time.LocalDateTime;
import java.util.List;

public record CustomErrorResponse(
        LocalDateTime datetime,
        int status,
        String message,
        List<String> details,
        String path
) {
}
