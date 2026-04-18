package com.hackaton.meow.repo;

import com.hackaton.meow.domain.Record;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;

@Repository
public interface RecordRepo extends JpaRepository<Record, Long> {
    Page<Record> findByTimeBetweenAndDevEuiIn(OffsetDateTime start, OffsetDateTime end, List<String> devEuis, Pageable pageable);
}
