package com.scout24.redee.extraction.keywords;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scout24.redee.extraction.model.Expose;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeywordsExtractorPlayground {

    private static final String RESOURCES_PATH = "redee-core/src/main/resources/scout24/";

    private static KeywordExtractor extractor;

    public static void main(String[] args) {
        Gson gson = new Gson();

        try {
            String exposesString = new String(Files.readAllBytes(Paths.get(RESOURCES_PATH + "exposes_big.json")));
            Type listType = new TypeToken<List<Expose>>() {
            }.getType();
            List<Expose> exposes = gson.fromJson(exposesString, listType);

            String keywordsString = new String(Files.readAllBytes(Paths.get(RESOURCES_PATH + "keywords.json")));
            extractor = new KeywordExtractor(gson.fromJson(keywordsString, String[].class));

            extractKeywords(exposes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractKeywords(List<Expose> exposes) {
        System.out.println("Exposes count: " + exposes.size());
        for (Expose expose : exposes) {
            Set<String> keywords = new HashSet<>();
            keywords.addAll(extractor.findKeywords(expose.title));
            keywords.addAll(extractor.findKeywords(expose.descriptionNote));
            keywords.addAll(extractor.findKeywords(expose.furnishingNote));
            keywords.addAll(extractor.findKeywords(expose.locationNote));
            keywords.addAll(extractor.findKeywords(expose.otherNote));

            System.out.println("Expose id: " + expose.id);
            System.out.println("Keywords     : " + keywords);
        }
    }
}
