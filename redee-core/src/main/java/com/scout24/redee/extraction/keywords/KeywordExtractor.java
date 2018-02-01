package com.scout24.redee.extraction.keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class KeywordExtractor {

    private static final String NEGATE_PATTERN = "(?<!(kein.{0,2}|ohne)\\s)";
    private final KeywordsHolder[] availableKeywords;

    public KeywordExtractor(KeywordsHolder[] keywords) {
        availableKeywords = keywords;
    }

    public List<String> findKeywords(String target) {
        List<String> foundKeywords = new ArrayList<>();

        if (target == null) {
            return foundKeywords;
        }

        String preparedTarget = target.toLowerCase();

        for (KeywordsHolder keyword : availableKeywords) {
            for (String synonym : keyword.synonyms) {
                Pattern pattern = Pattern.compile(NEGATE_PATTERN + synonym.toLowerCase());
                if (pattern.matcher(preparedTarget).find()) {
                    foundKeywords.add(keyword.title);
                }
            }
        }

        return foundKeywords;
    }

}
