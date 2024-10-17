package com.sparta.promotion.server.domain.repository;

import com.sparta.promotion.server.domain.model.Event;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

  @Query("SELECT e FROM Event e WHERE e.id = :id AND e.is_deleted = false")
  Optional<Event> findById(Long id);

  @Query("SELECT e FROM Event e WHERE e.is_deleted = false")
  List<Event> findAll();

  @Query("SELECT e FROM Event e WHERE e.is_deleted = false")
  Page<Event> findAll(Pageable pageable);

}
