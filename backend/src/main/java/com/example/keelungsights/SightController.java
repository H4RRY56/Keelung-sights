package com.example.keelungsights;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SightController {
    private final SightService sightService;

    public SightController(SightService sightService){
        this.sightService = sightService;
    }

    @GetMapping("/api/sights/{zone:[a-z]+}")
    public List<Sight> getSight(@PathVariable String zone){
        return sightService.getSightsByZone(zone);
    }
}
