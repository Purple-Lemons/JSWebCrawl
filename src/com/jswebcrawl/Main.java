package com.jswebcrawl;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final int NUM_LIB_PAGES = 5;

    private final LibraryService libraryService = new LibraryService();
    private final OccurrencesService occurrencesService = new OccurrencesService();
    private final WebPageHelper webPageHelper = new WebPageHelper();

    public void run() {
        String searchTerm = takeInput();

        List<String> libraryNames = libraryService.getLibraryNames(NUM_LIB_PAGES);

        List<String> pages = webPageHelper.performWebSearch(searchTerm);

        Map<String, Integer> libraryOccurrences = occurrencesService.getNumberOfOccurrencesOnPages(libraryNames, pages);

        findAndDisplayResults(libraryOccurrences);
    }

    public String takeInput() {
        String searchTerm = "";
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("Enter a search term: ");
            searchTerm = scanner.nextLine();
        } while (searchTerm.equals(""));
        scanner.close();
        return searchTerm.replaceAll("\\s+", "+");
    }

    public void findAndDisplayResults(Map<String, Integer> libraryOccurrences) {
        System.out.println("Javascript libraries with most occurrences: ");
        for(int i = 1; i <= 5; i++) {
            int lastHighest = -1;
            String lastHighestLibrary = "";
            for (Map.Entry<String, Integer> entry : libraryOccurrences.entrySet()) {
                if (entry.getValue() > lastHighest) {
                    lastHighest = entry.getValue();
                    lastHighestLibrary = entry.getKey();
                }
            }

            System.out.println(i + ": " + lastHighestLibrary + " - " + lastHighest + " occurrences");
            libraryOccurrences.remove(lastHighestLibrary);
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
}
