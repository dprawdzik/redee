package com.scout24.redee.extraction.stanford;

import com.scout24.redee.extraction.DateExtraction;
import com.scout24.redee.extraction.Extraction;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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
    public void extractDates() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        String dateInString = "18.12.2017 14:30";
        Date expected = sdf.parse(dateInString);

        String content = "18.12.2017 um 14:30 Uhr";
        Collection<DateExtraction> extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(extractions.iterator().next().getStart(), expected);

        content = "18:12:2017 um 14.30 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(extractions.iterator().next().getStart(), expected);

        content = "BES. MI. 10.01.18 um 15.30 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        dateInString = "10.01.2018 15:30";
        expected = sdf.parse(dateInString);
        TestCase.assertEquals(extractions.iterator().next().getStart(), expected);
    }

    @Test
    @Ignore
    public void extractDays() throws Exception {

        String content = "Montag im April";
        Collection<DateExtraction> extractions = analyser.extract(content);
        TestCase.assertTrue(extractions.size() > 0);
        // TestCase.assertEquals(extractions.get(0).getStart(), new Date());

        content = "montag im januar";
        extractions = analyser.extract(content);
        TestCase.assertTrue(extractions.size() > 0);
    }

    @Test
    @Ignore
    public void extract() throws Exception {

        String content = "18.12.2017 um 15.00 Uhr";
        Collection<DateExtraction> extractions = analyser.extract(content);

        content = "Mittwochsbesichtigung am 10.1. um 16:30 Uhr";
        extractions = analyser.extract(content);
        for (Extraction extraction : extractions) {
            System.out.println(extraction);
        }

        extractions = analyser.extract("the first day Peter Sellers war ein toller Künstler. " +
                "Besichtigungstermin am Montag 14.01.2017 und Weihnachten " +
                "um 15:00 Uhr!");
        for (Extraction extraction : extractions) {
            System.out.println(extraction);
        }
    }

}