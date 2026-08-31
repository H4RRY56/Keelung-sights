package com.example.keelungsights;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class SightDetailTest {
    public static void main(String[] args) throws IOException {
        String url = "https://okgo.tw/butyview.html?id=455";

        Sight sight = parseSight(url);

        System.out.println(sight);
    }

    public static Sight parseSight(String url) throws IOException {
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();

        Element container = document.selectFirst("div.sec3.word.Resize");
        if (container == null){
            return null;
        }

        Element nameElement = container.selectFirst("h2");

        String sightName = nameElement.text();

        Element zoneElement = container.selectFirst("a[href*=\"town.html\"]");
        String zone = zoneElement.text();

        Element photoElement = container.selectFirst("#Buty_Title_Pic img");
        String photoURL = "";
        if (photoElement != null){
            photoURL = photoElement.absUrl("src");
        }

        Element firstP = container.selectFirst("p");
        Element descriptionElement = firstP.nextElementSibling();
        String description = descriptionElement.text();

        String contextText = container.ownText();
        int addressIndex = contextText.indexOf("地址：");
        String address = contextText.substring(addressIndex + "地址：".length());
        address = address.trim();

        Sight sight = new Sight();

        sight.setSightName(sightName);
        sight.setZone(zone);
        sight.setPhotoURL(photoURL);
        sight.setDescription(description);
        sight.setAddress(address);
        sight.setCategory("");

        return sight;
    }
}
