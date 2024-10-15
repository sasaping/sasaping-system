package com.sparta.promotion.server.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import com.sparta.promotion.server.presentation.request.EventRequest.Create;
import com.sparta.promotion.server.presentation.request.EventRequest.Update;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_event")
@Entity
@SQLDelete(sql = "UPDATE p_event SET is_deleted = true WHERE id = ?")
public class Event extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String img_url;
  private String content;
  private LocalDateTime start_at;
  private LocalDateTime end_at;
  private boolean is_deleted = false;

  public static Event create(Create request) {
    return Event.builder()

        .title(request.getTitle())
        .img_url(request.getImgUrl())
        .content(request.getContent())
        .start_at(request.getStartAt())
        .end_at(request.getEndAt())
        .build();
  }

  public void update(Update request) {
    this.title = request.getTitle();
    this.img_url = request.getImgUrl();
    this.content = request.getContent();
    this.start_at = request.getStartAt();
    this.end_at = request.getEndAt();

  }

}
