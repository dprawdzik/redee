package com.scout24.hackdays;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeywordsExtractorPlayground {
    public static void main(String[] args) {
        Gson gson = new Gson();

        try {
            String exposesString = new String(Files.readAllBytes(Paths.get("redee-core/src/main/resources/scout24/exposes.json")));
            Type listType = new TypeToken<List<Expose>>() {
            }.getType();
            List<Expose> exposes = gson.fromJson(exposesString, listType);
            extractKeywords(exposes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractKeywords(List<Expose> exposes) {
        KeywordExtractor extractor = new KeywordExtractor();
        for (Expose expose : exposes) {
            Set<String> keywords = new HashSet<>();
            keywords.addAll(extractor.findKeywords(expose.title));
            keywords.addAll(extractor.findKeywords(expose.descriptionNote));
            keywords.addAll(extractor.findKeywords(expose.furnishingNote));
            keywords.addAll(extractor.findKeywords(expose.locationNote));
            keywords.addAll(extractor.findKeywords(expose.otherNote));

            Set<String> keywordsRegex = new HashSet<>();
            keywordsRegex.addAll(extractor.findKeywordsRegex(expose.title));
            keywordsRegex.addAll(extractor.findKeywordsRegex(expose.descriptionNote));
            keywordsRegex.addAll(extractor.findKeywordsRegex(expose.furnishingNote));
            keywordsRegex.addAll(extractor.findKeywordsRegex(expose.locationNote));
            keywordsRegex.addAll(extractor.findKeywordsRegex(expose.otherNote));

            System.out.println("Expose id: " + expose.id);
            System.out.println("Keywords     : " + keywords);
            System.out.println("KeywordsRegex: " + keywordsRegex);
        }
    }
}
