package com.coopcredit.application.infrastructure.adapter.in.web.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.time.Instant;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {
    private URI type;
    private String title;
    private int status;
    private String detail;
    private URI instance;
    private Instant timestamp;
    private String traceId;
}
