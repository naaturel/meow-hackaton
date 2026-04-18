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
        return repository.findByTimeBetweenAndDevEuiIn(start, end, getWaterEui(), pageable).getContent();
    }

    public List<Record> getGaz(OffsetDateTime start, OffsetDateTime end, int size){
        Pageable pageable = PageRequest.of(0, size, Sort.by("time").descending());
        return repository.findByTimeBetweenAndDevEuiIn(start, end, getGazEui(), pageable).getContent();
    }

    public List<Record> getElectricity(OffsetDateTime start, OffsetDateTime end, int size){
        Pageable pageable = PageRequest.of(0, size, Sort.by("time").descending());
        return repository.findByTimeBetweenAndDevEuiIn(start, end, getElectricity(), pageable).getContent();
    }

    private List<String> getGazEui(){
        return List.of(
            "a84041bc185f4796"
        );
    }

    private List<String> getWaterEui(){
        return List.of(
            "a840410e818855bf",
            "a84041e4018855c2",
            "a8404102e18855bc",
            "a840417df18860ac",
            "a8404152d35d680a",
            "a84041f09188609e"
        );
    }

    private List<String> getElectricity(){
        return List.of(
            "102ceffffe0111a7",
            "102ceffffe0111e4",
            "102ceffffe011102b",
            "102ceffffe011026",
            "102ceffffe0111c0",
            "102ceffffe011134"
        );
    }
}
