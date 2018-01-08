package com.scout24.hackdays;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        List<Expose> exposes = getExposes();
        extractKeywords(exposes);
        System.out.println( "Hello World!");
    }

    private static List<Expose> getExposes() {
        Gson gson = new Gson();
        try {
            String exposesString = new String(Files.readAllBytes(Paths.get("exposes.json")));
            Type listType = new TypeToken<List<Expose>>(){}.getType();
            return gson.fromJson(exposesString, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private static void extractKeywords(List<Expose> exposes) {
        for(Expose expose :exposes) {
            Set<String> keywords = new HashSet<>();
            keywords.addAll(KeywordExtractor.findKeywords(expose.title));
            keywords.addAll(KeywordExtractor.findKeywords(expose.descriptionNote));
            keywords.addAll(KeywordExtractor.findKeywords(expose.furnishingNote));
            keywords.addAll(KeywordExtractor.findKeywords(expose.locationNote));
            keywords.addAll(KeywordExtractor.findKeywords(expose.otherNote));

            System.out.println("Expose id: " + expose.id);
            System.out.println("Keywords: " + keywords);
        }
    }
}
