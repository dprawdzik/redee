package com.scout24.redee.extraction.jflex;

import com.scout24.redee.extraction.Extraction;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dprawdzik on 08.01.18.
 */
public class JFlexInformationExtractorTest {
    @Test
    public void extract() throws Exception {
        JFlexInformationExtractor extractor = new JFlexInformationExtractor();
        List<Extraction> tokens = extractor.extract("12.01.2018");
        TestCase.assertEquals(tokens.size(), 1);
        TestCase.assertEquals(tokens.get(0).getText(), "12.01.2018");
        TestCase.assertEquals(tokens.get(0).getType(), TokenType.DATE);

        tokens = extractor.extract("12.01.2018 um 15:00 Uhr");
        TestCase.assertEquals(tokens.size(), 1);
        TestCase.assertEquals(tokens.get(0).getText(), "12.01.2018 um 15:00 Uhr");
        TestCase.assertEquals(tokens.get(0).getType(), TokenType.APPOINTMENT);
    }

}