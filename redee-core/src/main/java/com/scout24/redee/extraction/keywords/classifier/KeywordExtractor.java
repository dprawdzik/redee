package com.scout24.redee.extraction.keywords.classifier;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class KeywordExtractor {

    private final String[] availableKeywords;

    public KeywordExtractor(String[] keywords) {
        availableKeywords = keywords;
    }

    public List<String> findKeywords(String target) {
        List<String> foundKeywords = new ArrayList<>();

        if (StringUtils.isBlank(target))
            return foundKeywords;

        target = target.toLowerCase();

        for (String keyword : availableKeywords) {

            if (target.contains(keyword)) {
                foundKeywords.add(keyword);
            }
        }
        return foundKeywords;
    }

}
