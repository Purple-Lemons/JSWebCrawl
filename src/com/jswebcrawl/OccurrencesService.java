package com.jswebcrawl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This handle the counting of the occurrences of library names on the provided web pages.
 *
 * Test Plan: given the time I would write unit tests for 'getNumberOfOccurrencesOnPages' that tests the results given
 * inputs:
 * empty libraryNames, empty pages - this should return an empty map
 * empty libraryNames, some list of strings for pages - this should return an empty map
 * a list of 3 library names, a list of pages: one with no occurrences of the 3 library names, one with
 *      no occurrences of one of the lib names, one with 1 occurrence of one of the lib names, one with 2 occurrences of
 *      one of the lib names.
 *      This should return a map with the expected number of occurrences
 */
public class OccurrencesService {

    public OccurrencesService() {
    }

    public Map<String, Integer> getNumberOfOccurrencesOnPages(List<String> libraryNames, List<String> pages) {
        Map<String, Integer> totalOccurrences = new HashMap<>();

        // This would be another ideal candidate for multithreading as these pages again could be parsed
        // simultaneously. In this case totalOccurrences would need to be a ConcurrentHashMap
        for (String page : pages) {
            Map<String, Integer> occurrences = getNumberOfOccurrencesOnPage(page, libraryNames);

            occurrences.forEach((key, numOccurrences) -> {
                if (totalOccurrences.containsKey(key)) {
                    totalOccurrences.put(key, totalOccurrences.get(key) + numOccurrences);
                } else {
                    totalOccurrences.put(key, numOccurrences);
                }
            });
        }

        return totalOccurrences;
    }

    private Map<String, Integer> getNumberOfOccurrencesOnPage(String page, List<String> libraryNames) {
        Map<String, Integer> occurrences = new HashMap<>();

        for (String libraryName : libraryNames) {
            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = page.indexOf(libraryName, lastIndex);

                if (lastIndex != -1) {
                    if(occurrences.containsKey(libraryName)) {
                        int currentValue = occurrences.get(libraryName);
                        occurrences.put(libraryName, currentValue + 1);
                    } else {
                        occurrences.put(libraryName, 1);
                    }

                    lastIndex += libraryName.length();
                }
            }
        }

        return occurrences;
    }
}
