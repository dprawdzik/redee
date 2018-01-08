package com.scout24.hackdays;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeywordExtractor {

    private final String[] availableKeywords;

    public KeywordExtractor() {
        Gson gson = new Gson();
        String exposesString = null;
        try {
            exposesString = new String(Files.readAllBytes(Paths.get("keywords.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        availableKeywords = gson.fromJson(exposesString, String[].class);
    }

    public List<String> findKeywords(String target) {
        List<String> foundKeywords = new ArrayList<>();

        if (target == null) {
            return foundKeywords;
        }

        for (String keyword : availableKeywords) {
            if (target.contains(keyword)) {
                foundKeywords.add(keyword);
            }
        }

        return foundKeywords;
    }
}
