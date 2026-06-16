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
            regexp = "^(https?://)[\\w\\-]+(\\.[\\w\\-]+)+[/#?]?.*$",
            message = "Invalid URL format"
    )
    private String longUrl;
}
