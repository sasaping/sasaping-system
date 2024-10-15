package com.sparta.promotion.server.presentation.response;

import com.sparta.promotion.server.domain.model.Event;
import lombok.Getter;
import lombok.Setter;


public class EventResponse {

  @Getter
  @Setter
  public static class Get {

    private Long id;
    private String title;
    private String imgUrl;
    private String content;
    private String startAt;
    private String endAt;

    public static Get from(Event event) {
      Get response = new Get();
      response.id = event.getId();
      response.title = event.getTitle();
      response.imgUrl = event.getImg_url();
      response.content = event.getContent();
      response.startAt = event.getStart_at().toString();
      response.endAt = event.getEnd_at().toString();
      return response;
    }

  }

}
