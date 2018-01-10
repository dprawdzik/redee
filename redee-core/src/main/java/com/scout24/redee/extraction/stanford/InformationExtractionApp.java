package com.scout24.redee.extraction.stanford;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scout24.redee.exception.ResourceException;
import com.scout24.redee.extraction.DateExtraction;
import com.scout24.redee.extraction.model.Expose;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

/**
 * Created by dprawdzik on 10.01.18.
 */
public class InformationExtractionApp {

    public static void main(String[] args) throws IOException, ResourceException, ParseException {
        extract();
    }

    private static void extract() throws IOException, ResourceException, ParseException {
        Gson gson = new Gson();
        StanfordInformationExtractor extractor = new StanfordInformationExtractor();
        String json = FileUtils.readFileToString(new File("/Users/dprawdzik/#Projects/Hackday/2018.Q1/3-Data/Expose/expose-20-pages.json"), "UTF-8");
        Type listType = new TypeToken<List<Expose>>() {}.getType();
        List<Expose> exposes = gson.fromJson(json, listType);
        for (Expose expose : exposes) {
            String[] contents = {expose.title, expose.descriptionNote, expose.locationNote};
            for (String content : contents) {
                if(content == null)
                    continue;
                System.out.println((content.length() > 100) ? content.substring(0, 100) + "..." : content);
                Collection<DateExtraction> extractions = extractor.extract(content);
                if((content.contains("Besicht") || content.contains("BES")) && extractions.size() == 0) {
                        System.out.println("Something went wrong!");
                    System.out.println(content);
                } else {
                    for (DateExtraction extraction : extractions) {
                        System.out.println(extraction.getText());
                        System.out.println(extraction.getStart());
                    }
                }
            }
        }

    }
}
