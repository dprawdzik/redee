package com.scout24.redee.extraction.stanford;

import com.scout24.redee.extraction.DateExtraction;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by dprawdzik on 08.01.18.
 */
public class StanfordInformationExtractorTest {

    private StanfordInformationExtractor analyser;

    @Before
    public void setUp() throws Exception {
        this.analyser = new StanfordInformationExtractor("stanford/pattern/date.pttrn");
    }

    @Test
    public void extractDatesSpecial() throws Exception {

        String content = "Super Zustand!! Ruhige 3 Zimmer Wohnung im Berlin Mitte - Besichtigungstermin 09.01.2018 19.00-19.30";
        Collection<DateExtraction> extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        TestCase.assertEquals(sdf.parse("09.01.2018 19:00"), extractions.iterator().next().getStart());
        TestCase.assertEquals(sdf.parse("09.01.2018 19:30"), extractions.iterator().next().getEnd());
    }

    @Test
    public void extractDates() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String content = "Besichtigung am Samstag, den 06.01. um 13:00 Uhr";
        Collection<DateExtraction> extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        String dateInString = "06.01.2018 13:00";
        Date expected = sdf.parse(dateInString);
        TestCase.assertEquals(expected, extractions.iterator().next().getStart());

        content = "Super Zustand!! Ruhige 3 Zimmer Wohnung im Berlin Mitte - Besichtigungstermin 09.01.2018 19.00-19.30";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(sdf.parse("09.01.2018 19:00"), extractions.iterator().next().getStart());
        TestCase.assertEquals(sdf.parse("09.01.2018 19:30"), extractions.iterator().next().getEnd());

        content = "BES: FREITAG um 18UHR! *Wunderschöne frisch renov Whg in san Altbau*Dielen*Stuck*EBK*Balkon*";
        content = "Sanierte-1,5-Zimmer- Wohnung im Wedding.Besichtigung am 11.1. um 9.00 Uhr";
content = "Dachgeschoss im Erstbezug - Besichtigung 12.Jan. 16 Uhr";
        content = "* bezugsfreie, sanierte Einraumwohnung in Lankwitz * Besichtigung am 02.01.2018";
        // time ranges
        content = "Besichtigung am 06.01.18 14:00-14:30 Uhr 2-Zimmer-Wohnung in Schmargendorf (Wilmersdorf), Berlin";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(sdf.parse("06.01.2018 14:00"), extractions.iterator().next().getStart());
        TestCase.assertEquals(sdf.parse("06.01.2018 14:30"), extractions.iterator().next().getEnd());

        content = "Super Zustand!! Ruhige 3 Zimmer Wohnung im Berlin Mitte - Besichtigungstermin 09.01.2018 19.00-19.30";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(sdf.parse("09.01.2018 19:00"), extractions.iterator().next().getStart());
        TestCase.assertEquals(sdf.parse("09.01.2018 19:30"), extractions.iterator().next().getEnd());

        content = "Süße, ruhige Erdgeschosswohnung im Prenzlauer Berg! Besichtigung 9.1. von 16.30-17.30!";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(sdf.parse("09.01.2018 16:30"), extractions.iterator().next().getStart());
        TestCase.assertEquals(sdf.parse("09.01.2018 17:30"), extractions.iterator().next().getEnd());

        content = "Sanierte Altbauwohnung in Friedenau - Offene Besichtigung am 12.01.18 von 13 bis 14 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(sdf.parse("12.01.2018 13:00"), extractions.iterator().next().getStart());
        TestCase.assertEquals(sdf.parse("12.01.2018 14:00"), extractions.iterator().next().getEnd());

        content = "Sanierte Altbauwohnung in Friedenau - Offene Besichtigung am 12.01.18 von 13-14 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(sdf.parse("12.01.2018 13:00"), extractions.iterator().next().getStart());
        TestCase.assertEquals(sdf.parse("12.01.2018 14:00"), extractions.iterator().next().getEnd());

        // no dot
        content = "SONNTAGSBESICHTIGUNG: 14.01 um 13 Uhr! RENOVIERTE WG-geeignete 3-Zimmer-Whg! CLICK HERE FOR ENGLISH!";
        content = "BESICHTIGUNG: Mittwoch den 10.01 um 17:30 Uhr! Einmalige Drei Zimmer Wohnung! Ab Sofort!";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        dateInString = "10.01.2018 17:30";
        expected = sdf.parse(dateInString);
        TestCase.assertEquals(expected, extractions.iterator().next().getStart());

        // comma & hyphen
        content = "KORDES IMMOBILIEN: Öffentliche Besichtigung 16.01.2018, 16.00 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(sdf.parse("16.01.2018 16:00"), extractions.iterator().next().getStart());

        content = "Sonniges Reihenhaus über 5 Etagen, max. 5 Personen,Besichtigung-Samstag, 13.1.17, 13 Uhr";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(sdf.parse("13.01.2017 13:00"), extractions.iterator().next().getStart());

        content = "***VENERDI - RUHIGE 2 ZIMMERWOHNUNG MITTEN IN PRENZLBERG. BESICHTIGUNG: 11.01.2018 - 18.00 UHR***";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(sdf.parse("11.01.2018 18:00"), extractions.iterator().next().getStart());

        content = "BESICHTIGUNG 15.01. / 12:00 Uhr **********  schicke 2 Zimmerwohnung mit EBK, Balkon, mod.Bad";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(sdf.parse("15.01.2018 12:00"), extractions.iterator().next().getStart());

        content = "Erstbezug nach Sanierung, sehr hochwertig möblierte 2 Zi. Whg.- Besichtigung 30.12.17, ab 12h";
        extractions = analyser.extract(content);
        TestCase.assertEquals(1, extractions.size());
        TestCase.assertEquals(sdf.parse("30.12.2017 12:00"), extractions.iterator().next().getStart());

        content = "Sanierte-1,5-Zimmer- Wohnung im Wedding.Besichtigung am 11.1. um 9.00 Uhr";
        // 4. OG, Badewanne, Parkett, EBK - Besichtigungstermin: Samstag, den 13.01./10 Uhr...Prenzl`Berg
        // Besichtigung am 13.1. um 13:30 h: Helle und ruhige Dachgeschoss-Wohnung in Pankow-Wilhelmsruh
        content = "Besichtigung 13.1. um 11 h: Zauberhafte ruhige 2-Zi.-Whg. im Südwesten Berlins im ruhigen Mariendorf";
        content = "Dachgeschoss im Erstbezug - Besichtigung 12.Jan. 16 Uhr";

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