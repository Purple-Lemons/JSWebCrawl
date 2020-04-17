package com.jswebcrawl;

import java.util.ArrayList;
import java.util.List;

/**
 * This is used to pull a list of the top Javascript libraries in terms of popularity. This is done by parsing names
 * of libraries from the list on https://www.javascripting.com/.
 * This does have one obvious drawback that this list will turn up in some search results, slightly skewing our results.
 * However there is only one mention of each library, so this will be minimal in most cases.
 */
public class LibraryService {

    WebPageHelper webPageHelper;

    public LibraryService() {
        webPageHelper = new WebPageHelper();
    }

    /**
     * Gets a list of popular libraries from https://www.javascripting.com/. This takes a param for the number of pages
     * to parse, so we could use a very large list of libraries but using just the first 5 or 10 pages should be a safe
     * bet on account of the list being sorted by popularity.
     * @param numPages the number of pages of results to get
     * @return a List of js library names
     */
    public List<String> getLibraryNames(int numPages) {
        List<String> libraryNames = new ArrayList<>();

        // This would be an ideal candidate for concurrency as we could get all of the pages simultaneously to save time.
        // In this case libraryNames would need to be a Collections.synchronisedList
        for (int i = 0; i < numPages; i++) {
            String pageContent = webPageHelper.getPage("https://www.javascripting.com/?p=" + i);

            libraryNames.addAll(parseLibsFromPage(pageContent));
        }

        return libraryNames;
    }

    private List<String> parseLibsFromPage(String pageContent) {
        List<String> libraryNames = new ArrayList<>();

        String stringToFind = "<a href=\"/view";
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = pageContent.indexOf(stringToFind, lastIndex);

            if (lastIndex != -1) {
                int startPos = pageContent.indexOf("\">", lastIndex) + 2;
                int endPos = pageContent.indexOf("</a>", startPos);

                String libName = pageContent.substring(startPos, endPos);

                libraryNames.add(libName);

                lastIndex += stringToFind.length();
            }
        }

        return libraryNames;
    }

}
