package com.scout24.redee.extraction.keywords.classifier;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scout24.redee.exception.ResourceException;
import com.scout24.redee.extraction.jflex.Token;
import com.scout24.redee.extraction.jflex.TokenOperations;
import com.scout24.redee.extraction.jflex.TokenType;
import com.scout24.redee.extraction.model.LegacyExposeResponse;
import com.scout24.redee.extraction.model.RealEstate;
import com.scout24.redee.utils.NameResolver;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import jflex.Tokenizer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import weka.core.*;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dprawdzik on 11.01.18.
 */
public class ArffCreator {

    private final KeywordExtractor balconyExtractor;
    private final KeywordExtractor negationExtractor;
    private final ArrayList<Attribute> attributes;
    private final Tokenizer tokenizer;
    private final StanfordCoreNLP pipeline;
    private Map<String, Integer> nameToIndex;

    private int correlating;
    private int notCorrelating;
    private int unknownDetection;
    private int amountBalcony;
    private int amountDetectedBalcony;

    public ArffCreator() throws Exception {

        Properties properties = edu.stanford.nlp.util.StringUtils.argsToProperties(
                new String[]{"-props", "stanford/StanfordCoreNlpDe.properties"});
        pipeline = new StanfordCoreNLP(properties);

        this.nameToIndex = new HashMap<>();
        this.tokenizer = new Tokenizer();

        balconyExtractor = new KeywordExtractor(new String[]{"balkon", "loggia"});
        negationExtractor = new KeywordExtractor(new String[]{"kein", "nicht", "k e i n e n", "keinen"});

        this.attributes = createAttributes(TextFeatures.FEATURE_NAMES);
        // gather all information about the features
        for (int i = 0; i < attributes.size(); i++) {
            nameToIndex.put(TextFeatures.getFeatureName(i), i);
        }

    }

    public static void main(String[] args) throws Exception {
        ArffCreator creator = new ArffCreator();
        // creator.transform();
        creator.createArff();
    }

    /**
     * Create an ARFF file based on all given exposés.
     */
    private void createArff() throws Exception {

        List<LegacyExposeResponse> exposes = getExposes();
        List<Instance> instances = new ArrayList<>();

        // over all exposés
        for (LegacyExposeResponse expose : exposes) {

            SparseInstance instance = createFeatures(expose);
            if (instance != null) {
                instances.add(instance);
                System.out.println("expose: '" + expose.getExpose().getRealEstate().getId() + "' - instance: '" + instances.size() + "'");
            }
        }
        System.out.println("Amount of expose\t" + exposes.size());
        System.out.println("exposés with balcony\t" + amountBalcony);
        System.out.println("Correlating\t" + correlating);
        System.out.println("Not correlating\t" + notCorrelating);
        System.out.println("Unknown balcony detected\t" + unknownDetection);
        System.out.println("Detected balconies based on keywords:\t" + amountDetectedBalcony);
        writeArff(instances, attributes, "src/main/resources/weka/balcony-v08.arff");
    }

    /**
     * Create a feature vector based on the description text in the exposé
     */
    protected SparseInstance createFeatures(LegacyExposeResponse expose) throws Exception {

        RealEstate realEstate = expose.getExpose().getRealEstate();
        boolean balcony = realEstate.hasBalcony();
        Map<String, String> descriptions = getDescriptions(realEstate);
        String description = getDescriptionsStr(realEstate);
        String id = realEstate.getId();

        // just return if no keywords matched
        List<Token> keywords = tokenize(description, TokenType.BALCONY);
        if (keywords.isEmpty())
            return null;

        List<TextFeatures> featuress = new ArrayList<>();

        for (Map.Entry<String, String> entry : descriptions.entrySet()) {
            String descriptionSngle = entry.getValue();
            String section = entry.getKey();
            if (StringUtils.isBlank(descriptionSngle))
                continue;
            Annotation annotations = pipeline.process(descriptionSngle);

            List<CoreMap> sentences = annotations.get(CoreAnnotations.SentencesAnnotation.class);

            // over all sentences
            for (CoreMap sentence : sentences) {
                String sentenceStr = sentence.get(CoreAnnotations.TextAnnotation.class);
                String[] splitted = sentenceStr.split("\n");

                // over all splits
                for (String split : splitted) {
                    TextFeatures features = extractTextFeatures(balcony, section, split, id);
                    System.out.println("sentence: " + sentence);
                    if (!featuress.isEmpty() && !features.getClues().isEmpty() && !features.containsClue("N/A"))
                        System.out.println("Already got features!");
                    if (!features.containsClue("N/A"))
                        featuress.add(features);
                }
            }
        }
        correlation(balcony, id, keywords);
        TextFeatures.sortPreferNegation(featuress);
        if (!featuress.isEmpty()) {

            TextFeatures features = featuress.get(0);
            features.setAmountOfTotalDetections(featuress.size());
            return createInstanceX(attributes, features);
        } else
            return null;
        // extractFeatures(features, balcony, descriptions, description, id);
        // return createInstance(attributes, features);
    }

    private SparseInstance createInstanceX(ArrayList<Attribute> attributes, TextFeatures features) {

        SparseInstance instance = new SparseInstance(attributes.size());
        // clues
        Attribute attribute = attributes.get(nameToIndex.get(TextFeatures.CLUE));
        if (!features.getClues().isEmpty())
            instance.setValue(attribute, features.getClues().get(0));

        // negation
        attribute = attributes.get(nameToIndex.get(TextFeatures.NEAGTION_CLUE));
        if (!features.getNegations().isEmpty())
            instance.setValue(attribute, features.getNegations().get(0));

        // distance
        attribute = attributes.get(nameToIndex.get(TextFeatures.DISTANCE_CLUE_TO_NGTN));
        instance.setValue(attribute, features.getDistanceClueToNegation());

        // section
        attribute = attributes.get(nameToIndex.get(TextFeatures.SECTION));
        instance.setValue(attribute, features.getSection());

        // direction
        attribute = attributes.get(nameToIndex.get(TextFeatures.DIRECTION_CLUE_TO_NGTN));
        instance.setValue(attribute, features.getDirectionClueToNegation());

        // length of sentence
        attribute = attributes.get(nameToIndex.get(TextFeatures.LENGTH_SNTNCE));
        instance.setValue(attribute, features.getLengthSentence());

        // amount of total detections in text
        attribute = attributes.get(nameToIndex.get(TextFeatures.AMOUNT_TOTAL_DETECTIONS));
        instance.setValue(attribute, features.getAmountTotalDetections());

        // add sentence itself
        attribute = attributes.get(nameToIndex.get(TextFeatures.SNTNCE));
        instance.setValue(attribute, features.getContent().replaceAll(" ", "-"));

        // clazz
        attribute = attributes.get(nameToIndex.get(TextFeatures.CLAZZ));
        instance.setValue(attribute, features.getClazz());

        return instance;
    }

    private TextFeatures extractTextFeatures(boolean balcony, String section, String sentence, String id) throws IOException {

        TextFeatures features = new TextFeatures(sentence);
        // keywords
        List<Token> keywords = tokenize(sentence, TokenType.BALCONY);
        addKeywords(features, keywords);

        // calculate distance
        TokenOperations operations = new TokenOperations();
        List<Token> tokens = tokenize(sentence, null);
        int distance = operations.calculateDistance(tokens, TokenType.BALCONY, TokenType.NEGATION);
        features.setClueToNegationDistance(distance);
        // negations
        List<Token> negations = tokenize(sentence, TokenType.NEGATION);
        addNegations(features, negations);

        System.out.println("Analysing expose: '" + id + "' with " +
                (balcony ? "" : " no") + " balcony, keywords: " + keywords + ", negation: " + negations + ", sentence: " +
                (StringUtils.isNotBlank(sentence) && sentence.length() > 50 ? sentence.substring(0, 50).replaceAll("\n", "")
                        + " ..." : sentence.replaceAll("\n", "")));

        // features.put(FEATURE_NAMES[6], Integer.valueOf(id));
        // add section
        features.setSection(String.valueOf(section));

        String direction = operations.direction(tokens, TokenType.BALCONY, TokenType.NEGATION);
        features.setClueToNegationDirection(direction);

        features.setLengthOfSentence(tokens.size());
        // add class
        features.setClass(balcony ? "balcony" : "no_balcony");
        return features;
    }

    private void correlation(boolean balcony, String id, List<Token> keywords) {
        if (balcony)
            amountBalcony++;
        if (!keywords.isEmpty())
            amountDetectedBalcony++;
        if (balcony && !keywords.isEmpty())
            correlating++;
        else if (!keywords.isEmpty()) {
            unknownDetection++;
            System.out.println("Detected missing balcony for id: '" + id + "'");
        } else if (balcony)
            notCorrelating++;
    }

    protected Map<String, String> getDescriptions(RealEstate realEstate) {
        Map<String, String> paragraphs = new HashMap<>();
        String value = realEstate.getDescriptionNote();
        paragraphs.put("description", (StringUtils.isNotBlank(value) ? value : ""));
        value = realEstate.getFurnishingNote();
        paragraphs.put("furnishing", (StringUtils.isNotBlank(value) ? value : ""));
        value = realEstate.getOtherNote();
        paragraphs.put("other", (StringUtils.isNotBlank(value) ? value : ""));
        return paragraphs;
    }

    protected String getDescriptionsStr(RealEstate realEstate) {
        return String.format("%s %s %s %s", realEstate.getDescriptionNote(),
                realEstate.getFurnishingNote(),
                realEstate.getLocationNote(),
                realEstate.getOtherNote());
    }

    protected List<Token> extractBalconyClues(String description) throws Exception {
        return tokenize(description, TokenType.BALCONY);
    }

    private List<Token> tokenize(String description, String type) throws IOException {
        tokenizer.reInit(description);
        List<Token> tokens = new ArrayList<>();
        while (tokenizer.hasNext()) {
            Token next = tokenizer.next();
            if (type == null) {
                tokens.add(next);
            } else if (next.getType().equals(type)) {
                tokens.add(next);
            }
        }
        return tokens;
    }

    private void addKeywords(TextFeatures features, List<Token> values) {

        if (values.isEmpty()) {
            features.addKeyword("N/A");
            return;
        }
        for (Token keyword : values) {
            features.addKeyword(keyword.getText().replaceAll(" ", "-"));
        }
    }

    private void addNegations(TextFeatures features, List<Token> values) {

        if (values.isEmpty()) {
            // features.addNegation("N/A");
            return;
        }
        for (Token keyword : values) {
            features.addNegation(keyword.getText().replaceAll(" ", "-"));
        }
    }


    protected List<LegacyExposeResponse> getExposes() throws IOException {
        Gson gson = new Gson();
        String fileName = "legacy-expose_apartmentrent_20-pages.json";

        String directory = "/Users/dprawdzik/#Projects/Hackday/2018.Q1/3-Data/Expose/Training/";
        List<LegacyExposeResponse> exposes = new ArrayList<>();

        List<File> filesInFolder = Files.walk(Paths.get(directory))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        for (File file : filesInFolder) {
            String json = FileUtils.readFileToString(file, "UTF-8");
            Type listType = new TypeToken<LegacyExposeResponse>() {
            }.getType();
            System.out.println(file);
            if (file.getName().endsWith(".json")) {
                LegacyExposeResponse expose = gson.fromJson(json, listType);
                exposes.add(expose);
            }
        }
        return exposes;
    }

    private void writeArff(List<Instance> instances, ArrayList<Attribute> attributes, String pathname) throws IOException {
        Instances newDataset = new Instances("Dataset", attributes, instances.size());
        newDataset.addAll(instances);
        ArffSaver arffSaverInstance = new ArffSaver();

        arffSaverInstance.setInstances(newDataset);
        arffSaverInstance.setFile(new File(pathname));
        String s = newDataset.get(0).toString();
        System.out.println(s);
        arffSaverInstance.writeBatch();
    }

    protected ArrayList<Attribute> createAttributes(String[] features) {
        ArrayList<Attribute> attributes = new ArrayList<>();
        AttributeCreator creator = new AttributeCreator();
        // create Attributes for dimensions
        for (int i = 0; i < features.length; i++) {
            String feature = features[i];
            Attribute attribute = creator.createAttribute(feature, i);
            attributes.add(attribute);
        }
        return attributes;
    }

    public LegacyExposeResponse getExpose(String path) throws IOException, ResourceException {

        Gson gson = new Gson();
        URL url = NameResolver.resolve(path);
        String json = FileUtils.readFileToString(com.scout24.utils.Utils.urlToFile(url), "UTF-8");
        Type listType = new TypeToken<LegacyExposeResponse>() {
        }.getType();
        LegacyExposeResponse expose = gson.fromJson(json, listType);

        return expose;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }
}
