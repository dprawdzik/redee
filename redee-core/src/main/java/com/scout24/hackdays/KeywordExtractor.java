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
import java.util.Map;
import java.util.regex.Pattern;

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

    public List<String> findKeywordsRegex(String target) {
        List<String> foundKeywords = new ArrayList<>();

        if (target == null) {
            return foundKeywords;
        }

        String preparedTarget = target.toLowerCase();

        for (String keyword : availableKeywords) {
            Pattern pattern = Pattern.compile("(?<!(kein.{0,2}|ohne)\\s)\\b" + keyword.toLowerCase());
            if (pattern.matcher(preparedTarget).find()) {
                foundKeywords.add(keyword);
            }
        }

        return foundKeywords;
    }

}
