package com.example.keelungsights;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SightRepository extends MongoRepository<Sight, String> {
    List<Sight> findByZone(String zone);
}
