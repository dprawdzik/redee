package com.scout24.redee.extraction.keywords;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;



public class KeywordExtractorTest {

    KeywordsHolder[] availableKeywords = new KeywordsHolder[]{
            new KeywordsHolder("Einbauküche", new String[]{"Einbauküche"}),
            new KeywordsHolder("Garage", new String[]{"Garage"}),
            new KeywordsHolder("Terrasse", new String[]{"Terrasse"}),
            new KeywordsHolder("Garten", new String[]{"Garten"}),
            new KeywordsHolder("Keller", new String[]{"Keller"})
    };
    KeywordExtractor keywordExtractor;

    @Before
    public void setUp() {
        keywordExtractor = new KeywordExtractor(availableKeywords);
    }

    @Test
    public void shouldFindKeywords() {
        String[] expectedKeywords = new String[]{
                "Garage",
                "Terrasse",
        };

        String[] keywords = keywordExtractor
                .findKeywords("Das Haus mit Garage und Terrasse")
                .toArray(new String[0]);

        assertArrayEquals(expectedKeywords, keywords);
    }


    @Test
    public void shouldNegate() {
        String[] expectedKeywords = new String[0];

        String[] keywords = keywordExtractor
                .findKeywords("Das Haus ohne Garage. Kein Terrasse, kein Keller!")
                .toArray(new String[0]);

        assertArrayEquals(expectedKeywords, keywords);
    }

}
