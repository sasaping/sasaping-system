package com.sparta.promotion.server.application.service;

import com.sparta.promotion.server.domain.model.Event;
import com.sparta.promotion.server.domain.repository.EventRepository;
import com.sparta.promotion.server.exception.PromotionErrorCode;
import com.sparta.promotion.server.exception.PromotionException;
import com.sparta.promotion.server.presentation.request.EventRequest.Create;
import com.sparta.promotion.server.presentation.request.EventRequest.Update;
import com.sparta.promotion.server.presentation.response.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;

  public Event createEvent(Create request, Long userId) {
    Event event = Event.create(request);
    eventRepository.save(event);
    return event;
  }

  public Event updateEvent(Long eventId, Update request, Long userId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new PromotionException(PromotionErrorCode.EVENT_NOT_FOUND));
    event.update(request);
    eventRepository.save(event);
    return event;

  }

  public Page<EventResponse.Get> getEvents(Pageable pageable) {
    Page<Event> events = eventRepository.findAll(pageable);
    return events.map(EventResponse.Get::from);
  }

  public EventResponse.Get getEvent(Long eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new PromotionException(PromotionErrorCode.EVENT_NOT_FOUND));
    return EventResponse.Get.from(event);
  }

  public void deleteEvent(Long eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new PromotionException(PromotionErrorCode.EVENT_NOT_FOUND));
    eventRepository.delete(event);
  }

}
