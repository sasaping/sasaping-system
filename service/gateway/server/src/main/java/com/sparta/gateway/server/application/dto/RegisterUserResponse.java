package com.sparta.gateway.server.application.dto;

public record RegisterUserResponse(Long rank) {

  public int getRank() {
    return rank.intValue();
  }

}
