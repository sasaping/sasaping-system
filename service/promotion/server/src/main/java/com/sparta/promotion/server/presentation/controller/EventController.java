package com.sparta.promotion.server.presentation.controller;

import com.sparta.common.domain.response.ApiResponse;
import com.sparta.promotion.server.application.service.EventService;
import com.sparta.promotion.server.presentation.request.EventRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

  private final EventService eventService;

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("")
  public ApiResponse<?> createEvent(@RequestBody EventRequest.Create request) {
    return ApiResponse.created(eventService.createEvent(request));
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PatchMapping("/{eventId}")
  public ApiResponse<?> updateEvent(@RequestBody EventRequest.Update request,
      @PathVariable Long eventId) {
    return ApiResponse.ok(eventService.updateEvent(eventId, request));
  }

  @GetMapping("")
  public ApiResponse<?> getEvents(Pageable pageable) {
    return ApiResponse.ok(eventService.getEvents(pageable));
  }

  @GetMapping("/{eventId}")
  public ApiResponse<?> getEvent(@PathVariable Long eventId) {
    return ApiResponse.ok(eventService.getEvent(eventId));
  }

  @DeleteMapping("/{eventId}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ApiResponse<?> deleteEvent(@PathVariable Long eventId) {
    eventService.deleteEvent(eventId);
    return ApiResponse.ok();
  }


}
