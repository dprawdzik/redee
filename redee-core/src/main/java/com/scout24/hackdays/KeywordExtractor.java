package com.scout24.hackdays;

import java.util.ArrayList;
import java.util.List;

public class KeywordExtractor {

    private static final String[] KEYWORDS = new String[]{
            // Equipment
            "Einbauküche",
            "Garage",
            "Stellplatz",
            "Balkon",
            "Terrasse",
            "Garten",
            "Gartenmitnutzung",
            "Parkanlage",
            "Personenaufzug",
            "Gäste-WC",
            "Keller",
            "Hobbyraum",
            // Transport
            "U-Bahn",
            "S-Bahn",
            "Tram",
            "Bus",
            // Other
            "Fahrradkeller",
            "Grünblick",
            "Wannenbad",
            "Südbalkon",
            "Echtholzparkett",
            "Laminatfußboden",
            "Fußbodenheizung"
    };

    public static List<String> findKeywords(String target) {
        List<String> keywords = new ArrayList<>();

        if (target == null) {
            return keywords;
        }

        for (String keyword : KEYWORDS) {
            if (target.contains(keyword)) {
                keywords.add(keyword);
            }
        }

        return keywords;
    }
}
