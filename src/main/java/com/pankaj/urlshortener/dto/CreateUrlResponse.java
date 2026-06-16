package com.pankaj.urlshortener.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class CreateUrlResponse {

    private String shortUrl;
}