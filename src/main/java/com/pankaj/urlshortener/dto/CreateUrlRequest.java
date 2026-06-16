package com.pankaj.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUrlRequest {

    @NotBlank
    @Pattern(
            regexp = "^(https?://).+",
            message = "URL must start with http:// or https://"
    )
    private String longUrl;
}
