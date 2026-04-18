package com.hackaton.meow.management;
import com.hackaton.meow.domain.Record;
import com.hackaton.meow.repo.RecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class RecordService {

    RecordRepo repository;

    @Autowired
    public RecordService(RecordRepo repository) {
        this.repository = repository;
    }

    public List<Record> getWater(OffsetDateTime start, OffsetDateTime end, int size) {

        Pageable pageable = PageRequest.of(0, size, Sort.by("time").descending());
        return repository.findByTimeBetween(start, end, pageable)
            .getContent()
            .stream()
            .filter(r -> isWater(r.getDevEui()))
            .toList();
    }

    public List<Record> getGaz(OffsetDateTime start, OffsetDateTime end, int size){
        Pageable pageable = PageRequest.of(0, size, Sort.by("time").descending());
        return repository.findByTimeBetween(start, end, pageable)
            .getContent()
            .stream()
            .filter(r -> isGaz(r.getDevEui()))
            .toList();
    }

    public List<Record> getElectricity(OffsetDateTime start, OffsetDateTime end, int size){
        Pageable pageable = PageRequest.of(0, size, Sort.by("time").descending());
        return repository.findByTimeBetween(start, end, pageable)
            .getContent()
            .stream()
            .filter(r -> isElectricity(r.getDevEui()))
            .toList();
    }

    private boolean isGaz(String id){
        return id.equalsIgnoreCase("a84041bc185f4796");
    }

    private boolean isWater(String id){
        return id.equalsIgnoreCase("a840410e818855bf")
            || id.equalsIgnoreCase("a84041e4018855c2")
            || id.equalsIgnoreCase("a8404102e18855bc")
            || id.equalsIgnoreCase("a840417df18860ac")
            || id.equalsIgnoreCase("a8404152d35d680a")
            || id.equalsIgnoreCase("a84041f09188609e");
    }

    private boolean isElectricity(String id){
        return id.equalsIgnoreCase("102ceffffe0111a7")
            || id.equalsIgnoreCase("102ceffffe0111e4")
            || id.equalsIgnoreCase("102ceffffe011102b")
            || id.equalsIgnoreCase("102ceffffe011026")
            || id.equalsIgnoreCase("102ceffffe0111c0")
            || id.equalsIgnoreCase("102ceffffe011134");
    }


}
