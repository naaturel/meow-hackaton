package com.hackaton.meow.repo;

import com.hackaton.meow.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface RecordRepo extends JpaRepository<Record, Long> {
    List<Record> findByDeviceName(String name);
    List<Record> findByTimeBetween(OffsetDateTime start, OffsetDateTime end);

}
