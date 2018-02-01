package com.scout24.redee.web;

import com.google.gson.Gson;
import com.scout24.redee.exception.ResourceException;
import com.scout24.redee.extraction.DateExtraction;
import com.scout24.redee.extraction.keywords.KeywordExtractor;
import com.scout24.redee.extraction.keywords.KeywordsHolder;
import com.scout24.redee.extraction.stanford.StanfordInformationExtractor;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

public class App extends NanoHTTPD {

   private static final String PARAM_METHOD = "method";
    private static final String PARAM_TARGET = "target";

    private static final String PARAM_METHOD_KEYWORDS = "keywords";
    private static final String PARAM_METHOD_DATES = "dates";

    Gson gson = new Gson();
    private KeywordExtractor keywordExtractor;
    private StanfordInformationExtractor stanfordInformationExtractor;


    public App() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");

        String keywordsString = new String(Files.readAllBytes(Paths.get( "redee-core/src/main/resources/scout24/keywords_extended.json")));
        keywordExtractor = new KeywordExtractor(gson.fromJson(keywordsString, KeywordsHolder[].class));

        try {
            stanfordInformationExtractor = new StanfordInformationExtractor("stanford/pattern/date.pttrn");
        } catch (ResourceException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, List<String>> parameters = session.getParameters();

        if (!parameters.containsKey(PARAM_METHOD)) {
            return newErrorResponse("Provide parameter `" + PARAM_METHOD + "`: `" + PARAM_METHOD_KEYWORDS + "` or `" + PARAM_METHOD_DATES + "`");
        }

        final String method = parameters.get(PARAM_METHOD).get(0);

        if (!parameters.containsKey(PARAM_TARGET)) {
            return newErrorResponse("Provide parameter `" + PARAM_TARGET + "` with strings to parse");
        }

        List<String> targets = parameters.get(PARAM_TARGET);

        switch (method) {
            case PARAM_METHOD_KEYWORDS:
                return extractKeywords(targets);
            case PARAM_METHOD_DATES:
                return extractDates(targets);
            default:
                return newErrorResponse("Unknown method!");
        }
    }

    private Response extractKeywords(List<String> targets) {
        Set<String> keywords = new HashSet<>();
        for (String target : targets) {
            keywords.addAll(keywordExtractor.findKeywords(target));
        }

        return newJsonResponse(keywords.toArray());
    }

    private Response extractDates(List<String> targets) {
        if (stanfordInformationExtractor == null) {
            return newErrorResponse("StanfordInformationExtractor is not initialized!");
        }

        List<DateExtraction> allDateExtractions = new ArrayList<>();

        for (String target : targets) {
            try {
                Collection<DateExtraction> extractions = stanfordInformationExtractor.extract(target);
                allDateExtractions.addAll(extractions);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return newJsonResponse(allDateExtractions);
    }

    private Response newJsonResponse(Object object) {
        return newFixedLengthResponse(Response.Status.OK, "application/json", gson.toJson(object));
    }

    private Response newErrorResponse(String message) {
        return newFixedLengthResponse(Response.Status.BAD_REQUEST, "application/json", gson.toJson(new Error(message)));
    }

}
