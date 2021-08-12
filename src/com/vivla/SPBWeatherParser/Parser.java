package com.vivla.SPBWeatherParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final ArrayList<String> array = new ArrayList<>();

    private Document getPage() throws Exception {
        String url = "http://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    public ArrayList<String> getArray() {
        return array;
    }

    private String getDateFromString(String dateString) throws Exception {
        Pattern p = Pattern.compile("\\d{2}\\.\\d{2}");
        Matcher m = p.matcher(dateString);
        if (m.find()) {
            return m.group();
        }
        throw new Exception("Can`t find nothing");
    }

    private int printPartsOfValues(Elements values, int index) {
        int iterationCount = 4;

        if (index == 0) {
            Element valueLn = values.get(3);
            boolean isMorning = valueLn.text().contains("Утро");
            boolean isNoon = valueLn.text().contains("День");
            boolean isEvening = valueLn.text().contains("Вечер");
            if (isMorning)
                iterationCount = 3;
            else if (isNoon)
                iterationCount = 2;
            else if (isEvening)
                iterationCount = 1;
        }

        for (int i = 0; i < iterationCount; i++) {
            Element valueLine = values.get(index + i);
            for (Element td : valueLine.select("td")) {
                array.add(td.text());
            }
        }
        return iterationCount;
    }

    public Parser() throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");

        int index = 0;
        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            array.add(date);
            int iterationCount = printPartsOfValues(values, index);
            index = index + iterationCount;
        }
        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i));
        }
    }
}
