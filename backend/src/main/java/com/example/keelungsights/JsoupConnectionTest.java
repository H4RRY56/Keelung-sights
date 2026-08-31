package com.example.keelungsights;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class JsoupConnectionTest {
    public static void main(String[] args) throws IOException {
        String url = "https://okgo.tw/buty/keelung.html";

        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();

        Elements sightLinks = document.select("a[href*=\"butyview.html?id=\"]");

        Set<String> uniqueUrls = new LinkedHashSet<>();

        for (Element sightLink : sightLinks) {
            uniqueUrls.add(sightLink.absUrl("href"));
        }
        System.out.println("原始連結數：" + sightLinks.size());
        System.out.println("去重複後：" + uniqueUrls.size());

        List<Sight> sights = new ArrayList<>();

        int count = 0;
        String targetZone = "七堵區";

        for (String detailUrl : uniqueUrls) {
            Sight sight = SightDetailTest.parseSight(detailUrl);
            if (sight.getZone().equals(targetZone)){
                sights.add(sight);
            }
            count ++;
            if (count == 20){
                break;
            }
        }

        for (Sight sight : sights){
            System.out.println(sight.getSightName());
        }
    }
}
