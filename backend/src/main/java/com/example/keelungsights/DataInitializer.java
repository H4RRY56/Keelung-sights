package com.example.keelungsights;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SightRepository sightRepository;
    private final KeelungSightsCrawler crawler;

    public DataInitializer(SightRepository sightRepository,
                           KeelungSightsCrawler crawler) {
        this.sightRepository = sightRepository;
        this.crawler = crawler;
    }

    @Override
    public void run(String... args) throws Exception {

        if (sightRepository.count() > 0) {
            System.out.println("MongoDB 已有資料，不重新爬取。");
            return;
        }

        System.out.println("MongoDB 為空，開始爬取景點...");

        Sight[] sights = crawler.getAllItems();

        sightRepository.saveAll(Arrays.asList(sights));

        System.out.println("景點資料儲存完成！");
        System.out.println("總共：" + sightRepository.count() + " 筆");
    }
}