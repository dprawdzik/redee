package com.scout24.hackdays;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Gson gson = new Gson();

        try {
            String exposesString = new String(Files.readAllBytes(Paths.get("./src/main/exposes.json")));
            Type listType = new TypeToken<List<Expose>>(){}.getType();
            List<Expose> exposes = gson.fromJson(exposesString, listType);
            extractKeywords(exposes);
        } catch (IOException e) {
            e.printStackTrace();
        }



        System.out.println( "Hello World!");
    }

    private static void extractKeywords(List<Expose> exposes) {
        for(Expose expose :exposes) {
            List<String> keywords = new ArrayList<>();
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
