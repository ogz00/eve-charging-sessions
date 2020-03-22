package com.oguz.demo.evechargingsessions.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class IdentityRequest {

    @NotBlank
    @NotNull
    private String stationId;
}
