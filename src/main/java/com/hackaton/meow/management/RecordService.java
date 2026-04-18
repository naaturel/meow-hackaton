package com.hackaton.meow.management;
import com.hackaton.meow.repo.RecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {

    RecordRepo repository;

    @Autowired
    public RecordService(RecordRepo repository) {
        this.repository = repository;
    }

    public String getAir(){
        return null;
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
