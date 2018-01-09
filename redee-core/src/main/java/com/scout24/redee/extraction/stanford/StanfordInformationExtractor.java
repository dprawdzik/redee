package com.scout24.redee.extraction.stanford;

import com.scout24.redee.exception.ResourceException;
import com.scout24.redee.extraction.*;
import com.scout24.redee.utils.NameResolver;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.*;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;
import org.apache.commons.io.FileUtils;
import com.scout24.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by dprawdzik on 11.07.17.
 */
public class StanfordInformationExtractor implements InformationExtractor<DateExtraction> {

    private final MultiPatternMatcher<CoreMap> multiMatcher;
    private StanfordCoreNLP pipeline;

    public StanfordInformationExtractor() throws IOException, ResourceException {

        Properties properties = StringUtils.argsToProperties(
                new String[]{"-props", "stanford/StanfordCoreNlpDe.properties"});
        pipeline = new StanfordCoreNLP(properties);
        URL url = NameResolver.resolve("stanford/pattern/date.pttrn");
        List<String> strings = FileUtils.readLines(Utils.urlToFile(url), "UTF-8");
        Collection<TokenSequencePattern> patterns = new ArrayList<>();
        Env env = TokenSequencePattern.getNewEnv();
        env.setDefaultStringMatchFlags(NodePattern.CASE_INSENSITIVE);
        // Macros!
        env.bind("$DAY", "/[Mm]ontag|[Dd]ienstag|[Mm]ittwoch|[Dd]onnerstag|[Ff]reitag|[Ssamstag]|[Ss]onntag/");
        env.bind("$MONTH", "/[Jj]anuar|[Ff]ebruar|[Mm]ärz|[Aa]pril|[Mm]ai|[Jj]uli/");
        env.bind("$SEPARATOR", "\\.");
        env.bind("$DATEA", "(?$date /\\d+[\\.:,;-]\\d+[\\.:,;-]20\\d+/)");
        //:|,|;|-|

        for (String string : strings) {
            TokenSequencePattern pattern = TokenSequencePattern.compile(env, string);
            patterns.add(pattern);
        }
        this.multiMatcher = TokenSequencePattern.getMultiPatternMatcher(patterns);
    }

    public Collection<DateExtraction> extract(String content) throws ParseException {

        Annotation annotations = pipeline.process(content);
        Collection<DateExtraction> chunks = new HashSet<>();

        // for (CoreMap sentence : annotations.get(CoreAnnotations.SentencesAnnotation.class)) {
        // List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        List<CoreLabel> tokens = annotations.get(CoreAnnotations.TokensAnnotation.class);

        List<SequenceMatchResult<CoreMap>> nonOverlapping = multiMatcher.findNonOverlapping(tokens);
        System.out.println("Analysing sentence: '" + annotations.get(CoreAnnotations.TextAnnotation.class) + "'");

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
        String dayStr = group.group("$day");
        String monthStr = group.group("$month");
        String yearStr = group.group("$year");

        String dateStr = group.group("$date");
        dateStr = dateStr.replaceAll("[:-_,;]", ".");
        String[] dateSplit = dateStr.split("\\.");
        if(dateSplit[2].length() <= 2)
            dateStr = dateSplit[0] + "." + dateSplit[1] + ".20" + dateSplit[2];

        // starting time stamp
        String timeStartStr = group.group("$timeStart");
        if(org.apache.commons.lang3.StringUtils.isNotBlank(timeStartStr)) {
            timeStartStr = timeStartStr.replaceAll("[-_,;.]", ":");
            dateStr = dateStr + " " + timeStartStr;
        }
        Date start = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN).parse(dateStr);

        // ending time stamp
        Date end = null;
        String timeEndStr = group.group("$timeEnd");

        if(org.apache.commons.lang3.StringUtils.isNotBlank(timeEndStr)) {
            timeEndStr = timeEndStr.replace("[-_,;.]", ":");
            dateStr = dateStr + " " + timeEndStr;
            end = new SimpleDateFormat("dd.mm.yyyy HH:mm", Locale.GERMAN).parse(dateStr);
        }
        return new DateExtraction(start, end, group.group(0), "", Position.createEmptyPosition());
    }

}
