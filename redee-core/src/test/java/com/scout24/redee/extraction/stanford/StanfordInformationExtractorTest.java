package com.scout24.redee.extraction.stanford;

import com.scout24.redee.extraction.Extraction;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dprawdzik on 08.01.18.
 */
public class StanfordInformationExtractorTest {

    private StanfordInformationExtractor analyser;

    @Before
    public void setUp() throws Exception {
        this.analyser = new StanfordInformationExtractor();
    }

    @Test
    @Ignore
    public void extract() throws Exception {
        List<Extraction> extractions = analyser.extract("Besichtigungstermin am Montag und am Donnerstag um 15:00 Uhr!");
        for (Extraction extraction : extractions) {
            System.out.println(extraction);
        }
    }

}