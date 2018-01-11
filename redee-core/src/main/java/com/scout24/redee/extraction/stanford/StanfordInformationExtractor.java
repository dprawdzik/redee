package com.scout24.redee.extraction.stanford;

import com.scout24.redee.exception.ResourceException;
import com.scout24.redee.extraction.DateExtraction;
import com.scout24.redee.extraction.InformationExtractor;
import com.scout24.redee.extraction.Position;
import com.scout24.redee.utils.NameResolver;
import com.scout24.utils.Utils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.*;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by dprawdzik on 11.07.17.
 */
public class StanfordInformationExtractor implements InformationExtractor<DateExtraction> {

    private MultiPatternMatcher<CoreMap> multiMatcher;
    private StanfordCoreNLP pipeline;

    public StanfordInformationExtractor(String patternFile) throws IOException, ResourceException {

        URL url = NameResolver.resolve(patternFile);
        List<String> strings = FileUtils.readLines(Utils.urlToFile(url), "UTF-8");
        initialize(strings);
    }

    public void initialize(List<String> strings) throws IOException, ResourceException {

        Properties properties = StringUtils.argsToProperties(
                new String[]{"-props", "stanford/StanfordCoreNlpDe.properties"});
        pipeline = new StanfordCoreNLP(properties);

        Env env = TokenSequencePattern.getNewEnv();
        env.setDefaultStringMatchFlags(NodePattern.CASE_INSENSITIVE);
        // Macros!
        String separator = "[\\.:,;-]";
        String hourPattern = "Uhr|UHR|uhr|h";
        String timePattern = String.format("(?%s /\\d+%s{0,1}\\d{0,2}%s/)", "%s", separator, "%s");
        String timeStartPattern = String.format(timePattern, "$timeStart", "");
        String timeEndPattern = String.format(timePattern, "$timeEnd", "");
        String timeStartNoSpace = String.format(timePattern, "$timeStart", "h");

        env.bind("$DAY", "/[Mm]ontag|[Dd]ienstag|[Mm]ittwoch|[Dd]onnerstag|[Ff]reitag|[Ssamstag]|[Ss]onntag/");
        env.bind("$MONTH", "/[Jj]anuar|[Ff]ebruar|[Mm]ärz|[Aa]pril|[Mm]ai|[Jj]uli/");
        env.bind("$SPRTR", separator);
        env.bind("$SPRTR_ESCPD", "/[\\.:,;-]/");
        env.bind("$OCLOCK", hourPattern);
        env.bind("$TIME_START", timeStartPattern);
        env.bind("$TIME_START_NO_SPACE", timeStartNoSpace);
        env.bind("$TIME_END", timeEndPattern);
        env.bind("$TIME_INFIX", "/um|,|UM|-|\\/||ab/");

        String simpleTimePattern = String.format("\\d+%s{0,1}\\d{0,2}", separator);
        env.bind("$TIME", simpleTimePattern);
        String timeRange = "(?$timeRange /" + simpleTimePattern + "-" + simpleTimePattern + "/)";
        String timeRangeB = "(?$timeRange /" + simpleTimePattern + "-" + simpleTimePattern + "/  /\\.\\d{0,2}/)";
        // \d+[\.:,;-]{0,1}\d{0,2}\-\d+[\.:,;-]{0,1}\d{0,2}
        // 06.01.18 14:00-14:30 Uhr
        env.bind("$TIME_RANGE_A", timeRange);
        env.bind("$TIME_RANGE_B", timeRangeB);
        env.bind("$DATE", "(?$date /\\d{1,2}[\\.:,;-]\\d{1,2}[\\.:,;-]20\\d{2}/)");
        env.bind("$DATE_SHORT", "(?$date /\\d{1,2}"+ separator + "\\d{1,2}"+separator+"\\d{2}/)");

        List<TokenSequencePattern> patterns = new ArrayList<>();
        for (String string : strings) {
            TokenSequencePattern pattern = TokenSequencePattern.compile(env, string);
            patterns.add(pattern);
        }
        this.multiMatcher = TokenSequencePattern.getMultiPatternMatcher(patterns);
    }

    public Collection<DateExtraction> extract(String content) throws ParseException {

        Annotation annotations = pipeline.process(content);
        Collection<DateExtraction> chunks = new HashSet<>();
        List<CoreLabel> tokens = annotations.get(CoreAnnotations.TokensAnnotation.class);
        List<SequenceMatchResult<CoreMap>> nonOverlapping = multiMatcher.findNonOverlapping(tokens);
        System.out.println("Analysing content: '" + annotations.get(CoreAnnotations.TextAnnotation.class) + "'");

        for (SequenceMatchResult<CoreMap> match : nonOverlapping) {

            for (int i = 0; i <= match.groupCount(); i++) {
                String group = match.group(i);
                System.out.println("group " + i + ": '" + group + "'");
                chunks.add(createExtraction(match));
            }
        }
        return chunks;
    }

    private DateExtraction createExtraction(SequenceMatchResult<CoreMap> group) throws ParseException {

        String dateStr = group.group("$date");
        // normalizes the date: e.g. 01.01.2018 and not 01-01-18
        dateStr = normalizeYear(dateStr);

        // starting time stamp
        Date start = createStartDate(group, dateStr);

        // ending time stamp
        Date end = createEndDate(group, dateStr);

        return new DateExtraction(start, end, group.group(0), "", Position.createEmptyPosition());
    }

    private Date createStartDate(SequenceMatchResult<CoreMap> group, String dateStr) throws ParseException {

        String timeStr = group.group("$timeStart");
        String timeRange = group.group("$timeRange");
        if(org.apache.commons.lang3.StringUtils.isNotBlank(timeRange))
            timeStr = timeRange.split("-")[0];

        return createDate(group, dateStr, timeStr);
    }

    private Date createEndDate(SequenceMatchResult<CoreMap> group, String dateStr) throws ParseException {

        String timeStr = group.group("$timeEnd");

        String timeRange = group.group("$timeRange");
        if(org.apache.commons.lang3.StringUtils.isNotBlank(timeRange))
            timeStr = timeRange.split("-")[1];

        if(org.apache.commons.lang3.StringUtils.isBlank(timeStr))
            return null;
        return createDate(group, dateStr, timeStr);
    }

    private Date createDate(SequenceMatchResult<CoreMap> group, String dateStr, String timeStr) throws ParseException {

        if(org.apache.commons.lang3.StringUtils.isNotBlank(timeStr) && timeStr.startsWith("-"))
            timeStr = timeStr.substring(1, timeStr.length());

        if(org.apache.commons.lang3.StringUtils.isBlank(timeStr) ) {
            return new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).parse(dateStr);
        } else if(timeStr.length() == 5) {
            timeStr = timeStr.replaceAll("[-_,;.]", ":");
            return new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN).parse(dateStr + " " + timeStr);
        } else
            return new SimpleDateFormat("dd.MM.yyyy HH", Locale.GERMAN).parse(dateStr + " " + timeStr);
    }

    String normalizeYear(String dateStr) {
        dateStr = dateStr.replaceAll("[:-_,;]", ".");
        String[] dateSplit = dateStr.split("\\.");

        // is it a date like day month and year?
        if(dateSplit.length > 2 && dateSplit[2].length() == 2)
            dateSplit[2] = "20" + dateSplit[2];
        else
            dateSplit = extendArraySize(dateSplit, Calendar.getInstance().get(Calendar.YEAR));

        for (int i = 0; i < 2; i++) {
            if(dateSplit[i].length() == 1)
                dateSplit[i] =  "0" + dateSplit[i];
        }

        // create the final and normalized date string.
        dateStr = String.format("%s.%s.%s", dateSplit[0], dateSplit[1], dateSplit[2]);
        return dateStr;
    }

    private static String[] extendArraySize(String[] array, int value){
        String [] temp = array.clone();
        array = new String[array.length + 1];
        System.arraycopy(temp, 0, array, 0, temp.length);
        array[array.length - 1] = String.valueOf(value);
        return array;
    }

}
