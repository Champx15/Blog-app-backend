package com.champ.Dto;

import java.time.LocalDateTime;

public class LoginResponseDto {
    private String jwt;
    private LocalDateTime time;

    public LoginResponseDto(String jwt) {
        this.jwt = jwt;
        this.time =  LocalDateTime.now();
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LoginResponseDto() {
    }
}
