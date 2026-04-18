package com.hackaton.meow.repo;

import com.hackaton.meow.domain.Record;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Pageable;

@Repository
public interface RecordRepo extends JpaRepository<Record, Long> {
    Page<Record> findByTimeBetween(OffsetDateTime start, OffsetDateTime end, Pageable pageable);
}
