package com.example.keelungsights;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class KeelungSightsCrawler {

    private static final String BASE_URL =
            "https://okgo.tw/buty/keelung.html";

    private static final Set<String> VALID_ZONES = Set.of(
            "七堵區",
            "中正區",
            "中山區",
            "仁愛區",
            "信義區",
            "安樂區",
            "暖暖區"
    );

    public static void main(String[] args) throws IOException {

        KeelungSightsCrawler crawler =
                new KeelungSightsCrawler();

        Sight[] sights =
                crawler.getItems("qidu");

        for (Sight s : sights) {
            System.out.println(s);
        }
    }

    public Sight[] getAllItems() throws IOException {

        Document document = Jsoup.connect(BASE_URL)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();

        Elements sightLinks =
                document.select(
                        "a[href*=\"butyview.html?id=\"]"
                );

        if (sightLinks.isEmpty()) {
            throw new IllegalStateException(
                    "找不到任何景點連結，OKGO 網頁結構可能已改變。"
            );
        }

        Set<String> uniqueUrls =
                new LinkedHashSet<>();

        for (Element sightLink : sightLinks) {

            String detailUrl =
                    sightLink.absUrl("href");

            if (detailUrl.isBlank()) {
                System.err.println(
                        "發現空白景點網址，已跳過。"
                );
                continue;
            }

            if (!detailUrl.startsWith(
                    "https://okgo.tw/")) {

                System.err.println(
                        "發現非 OKGO 網址，已跳過："
                                + detailUrl
                );

                continue;
            }

            uniqueUrls.add(detailUrl);
        }

        if (uniqueUrls.isEmpty()) {
            throw new IllegalStateException(
                    "景點連結經過檢查後全部無效。"
            );
        }

        List<Sight> sights =
                new ArrayList<>();

        int successCount = 0;
        int failCount = 0;

        for (String detailUrl : uniqueUrls) {

            try {

                Sight sight =
                        parseSight(detailUrl);

                if (sight != null) {

                    sights.add(sight);
                    successCount++;

                } else {

                    failCount++;
                }

            } catch (IOException e) {

                failCount++;

                System.err.println(
                        "景點頁面讀取失敗："
                                + detailUrl
                );

                System.err.println(
                        "錯誤原因："
                                + e.getMessage()
                );
            }
        }

        System.out.println(
                "爬蟲完成，成功："
                        + successCount
                        + " 筆，失敗："
                        + failCount
                        + " 筆。"
        );

        return sights.toArray(
                new Sight[0]
        );
    }

    public Sight[] getItems(String zone)
            throws IOException {

        if (zone == null
                || zone.isBlank()) {

            System.err.println(
                    "行政區參數不可為空。"
            );

            return new Sight[0];
        }

        zone = zone.trim()
                .toLowerCase(Locale.ROOT);

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

                System.err.println(
                        "不支援的行政區代碼："
                                + zone
                );

                return new Sight[0];
        }

        Sight[] allSights =
                getAllItems();

        List<Sight> filteredSights =
                new ArrayList<>();

        for (Sight sight : allSights) {

            if (targetZone.equals(
                    sight.getZone())) {

                filteredSights.add(sight);
            }
        }

        return filteredSights.toArray(
                new Sight[0]
        );
    }

    private Sight parseSight(String url)
            throws IOException {

        if (url == null
                || url.isBlank()) {

            System.err.println(
                    "景點網址為空，已跳過。"
            );

            return null;
        }

        Document document =
                Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .get();

        Element container =
                document.selectFirst(
                        "div.sec3.word.Resize"
                );

        if (container == null) {

            System.err.println(
                    "找不到景點主要資料區塊："
                            + url
            );

            return null;
        }

        Element nameElement =
                container.selectFirst("h2");

        if (nameElement == null
                || nameElement.text().isBlank()) {

            System.err.println(
                    "找不到景點名稱："
                            + url
            );

            return null;
        }

        String sightName =
                nameElement.text().trim();

        Element zoneElement =
                container.selectFirst(
                        "a[href*=\"town.html\"]"
                );

        if (zoneElement == null
                || zoneElement.text().isBlank()) {

            System.err.println(
                    "找不到景點行政區："
                            + url
            );

            return null;
        }

        String zone =
                zoneElement.text().trim();

        if (!VALID_ZONES.contains(zone)) {

            System.err.println(
                    "發現未知行政區："
                            + zone
                            + "，URL："
                            + url
            );

            return null;
        }

        String photoURL = "";

        Element photoElement =
                container.selectFirst(
                        "#Buty_Title_Pic img"
                );

        if (photoElement != null) {

            String tempPhotoURL =
                    photoElement.absUrl("src");

            if (!tempPhotoURL.isBlank()) {
                photoURL = tempPhotoURL;
            }
        }

        String description = "";

        Element firstP =
                container.selectFirst("p");

        if (firstP != null) {

            Element descriptionElement =
                    firstP.nextElementSibling();

            if (descriptionElement != null) {

                String text =
                        descriptionElement.text()
                                .trim();

                if (!text.isBlank()) {
                    description = text;
                }
            }
        }

        String address = "";

        String contentText =
                container.ownText();

        int addressIndex =
                contentText.indexOf("地址：");

        if (addressIndex != -1) {

            address =
                    contentText.substring(
                            addressIndex
                                    + "地址：".length()
                    ).trim();

            if (address.equals("-")) {
                address = "";
            }
        }

        Sight sight = new Sight();

        sight.setSightName(sightName);
        sight.setZone(zone);
        sight.setCategory("");
        sight.setPhotoURL(photoURL);
        sight.setDescription(description);
        sight.setAddress(address);

        return sight;
    }
}