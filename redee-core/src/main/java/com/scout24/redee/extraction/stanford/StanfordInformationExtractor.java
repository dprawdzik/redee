package com.scout24.redee.extraction.stanford;

import com.scout24.redee.extraction.Extraction;
import com.scout24.redee.extraction.InformationExtractor;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


/**
 * Created by dprawdzik on 11.07.17.
 */
public class StanfordInformationExtractor implements InformationExtractor {

    private StanfordCoreNLP pipeline;

    public StanfordInformationExtractor() {

        Properties properties = StringUtils.argsToProperties(
                new String[]{"-props", "stanford/StanfordCoreNlpDe.properties"});
        pipeline = new StanfordCoreNLP(properties);
    }

    public List<Extraction> extract(String content) {

        Annotation annotations = pipeline.process(content);
        List<Extraction> chunks = new ArrayList<Extraction>();

        for (CoreMap sentence : annotations.get(CoreAnnotations.SentencesAnnotation.class)) {

            List<CoreLabel> sentences = sentence.get(CoreAnnotations.TokensAnnotation.class);
            for (CoreLabel token : sentences) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                System.out.println(word + " - " + pos + " - " + ne);
            }

            // Tree sentences = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            List<Object> tokens = sentences.stream().collect(Collectors.toList());
            /*sentences.stream().filter(subTree -> subTree.label().value().equals("NP") ||
                    subTree.label().value().equals("MPN")).forEach(subtree -> {
                String image = subtree.yieldWords().stream().map(StringLabel::toString).collect(Collectors.joining(" "));


                    int start = subtree.yieldWords().get(0).beginPosition();
                    int end = subtree.yieldWords().get(subtree.yieldWords().size() - 1).endPosition();
                    Position position = new Position(Position.ParagraphType.VERBATIM.name(), start, end, "url");

                    Ref.Term term = new Ref.Term(image, image, subtree.nodeString(), position);
                    chunks.add(term);
            });*/
        }
        return chunks;
    }
}
