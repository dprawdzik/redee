package com.scout24.redee.extraction.jflex;

import com.scout24.redee.extraction.Extraction;
import com.scout24.redee.extraction.ExtractionImpl;
import com.scout24.redee.extraction.InformationExtractor;
import jflex.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dprawdzik on 08.01.18.
 */
public class JFlexInformationExtractor implements InformationExtractor {

    @Override
    public List<Extraction> extract(String text) throws Exception {
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.reInit(text);
        List<Extraction> extractions = new ArrayList<>();

        while(tokenizer.hasNext()) {
            Token token = tokenizer.next();
            System.out.println(token.getText() + " (" + token.getType() + ")");
            extractions.add(new ExtractionImpl(token.getText(), token.getType(), token.getPosition()));

        }
        return extractions;
    }
}
