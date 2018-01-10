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
        String content = "Besichtigung am Samstag, den 06.01. um 13:00 Uhr";
        Collection<DateExtraction> extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        String dateInString = "06.01.2018 13:00";
        Date expected = sdf.parse(dateInString);
        TestCase.assertEquals(expected, extractions.iterator().next().getStart());

        content = "am 8.1. um 16 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        dateInString = "08.01.2018 16:00";
        expected = sdf.parse(dateInString);
        TestCase.assertEquals(expected, extractions.iterator().next().getStart());

        dateInString = "18.12.2017 14:30";
        expected = sdf.parse(dateInString);
        content = "18.12.2017 um 14:30 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(expected, extractions.iterator().next().getStart());

        content = "18:12:2017 um 14.30 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(expected, extractions.iterator().next().getStart());

        content = "BES. MI. 10.1.18 um 15.30 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        dateInString = "10.01.2018 15:30";
        expected = sdf.parse(dateInString);
        TestCase.assertEquals(expected, extractions.iterator().next().getStart());

        content = "Montag, 18.12.2017 um 15.30 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        dateInString = "18.12.2017 15:30";
        expected = sdf.parse(dateInString);
        TestCase.assertEquals(expected, extractions.iterator().next().getStart());

        content = "DEN 10.01.2018 UM 15.30 UHR";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        dateInString = "10.01.2018 15:30";
        expected = sdf.parse(dateInString);
        TestCase.assertEquals(extractions.iterator().next().getStart(), expected);

        content = "Mittwochsbesichtigung am 10.1. um 16:30 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        dateInString = "10.01.2018 16:30";
        expected = sdf.parse(dateInString);
        TestCase.assertEquals(extractions.iterator().next().getStart(), expected);

    }

    @Test
    public void normalizeYear() throws Exception {
        String normalized = this.analyser.normalizeYear("06.01.");
        TestCase.assertEquals("06.01.2018", normalized);
        normalized = this.analyser.normalizeYear("06:01:");
        TestCase.assertEquals("06.01.2018", normalized);
        normalized = this.analyser.normalizeYear("22:12:17");
        TestCase.assertEquals("22.12.2017", normalized);
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
}