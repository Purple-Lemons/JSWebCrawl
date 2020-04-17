package com.jswebcrawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebPageHelper {

    public WebPageHelper() {

    }

    public String getPage(String path) {
        URL url;
        InputStream inputStream = null;
        BufferedReader bufferedReader;
        String line;
        StringBuilder pageContent = new StringBuilder();

        try {
            url = new URL(path);
            inputStream = url.openStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = bufferedReader.readLine()) != null) {
                pageContent.append(line);
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        return pageContent.toString();
    }

    public List<String> performWebSearch(String searchTerm) {
        // Had to use Bing - Google only allows search through their custom API and then it only functions
        // on specified sites or something, and then it only allows 100 requests per day for free
        String pageContent = getPage("https://www.bing.com/search?q="+searchTerm);

        List<String> searchResults = new ArrayList<>();

        String stringToFind = "<h2><a href=\"";
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = pageContent.indexOf(stringToFind, lastIndex);

            if (lastIndex != -1) {
                int startPos = lastIndex + stringToFind.length();
                int endPos = pageContent.indexOf("\"", startPos);

                String webAddress = pageContent.substring(startPos, endPos);
                searchResults.add(webAddress);

                lastIndex += stringToFind.length();
            }
        }

        return getPages(searchResults);
    }

    // This would be another good choice for concurrency since we could retrieve all of the pages simultaneously
    private List<String> getPages(List<String> paths) {
        return paths.stream().map(this::getPage).collect(Collectors.toList());
    }
}
