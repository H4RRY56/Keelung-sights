package com.example.keelungsights;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SightService {
    private final SightRepository sightRepository;

    public SightService(SightRepository sightRepository){
        this.sightRepository = sightRepository;
    }

    public List<Sight> getSightsByZone(String zone) {

        String targetZone;

        switch (zone) {
            case "qidu":
                targetZone = "七堵區";
                break;

            case "zhongzheng":
                targetZone = "中正區";
                break;

            case "zhongshan":
                targetZone = "中山區";
                break;

            case "renai":
                targetZone = "仁愛區";
                break;

            case "xinyi":
                targetZone = "信義區";
                break;

            case "anle":
                targetZone = "安樂區";
                break;

            case "nuannuan":
                targetZone = "暖暖區";
                break;

            default:
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Zone not found"
                );
        }

        return sightRepository.findByZone(targetZone);
    }
}

