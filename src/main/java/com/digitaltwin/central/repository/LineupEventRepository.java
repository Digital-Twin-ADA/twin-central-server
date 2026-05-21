package com.digitaltwin.central.repository;

import com.digitaltwin.central.model.LineupEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineupEventRepository extends JpaRepository<LineupEvent, Long> {
    List<LineupEvent> findAllByOrderByStartsAtAsc();
}
