package com.scout24.redee.extraction.keywords.classifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TextFeatures implements Comparable {

        public static final String SNTNCE = "sntnceS";
        public static final String AMOUNT_TOTAL_DETECTIONS = "mnt-total-dtctns";
        public static String CLUE = "clueS";
        public static String NEAGTION_CLUE = "negation-clueS";
        public static String DISTANCE_CLUE_TO_NGTN = "dstnc-clue-to-ngtn";
        public static String DIRECTION_CLUE_TO_NGTN = "drctn-clue-to-ngtnS";
        public static String SECTION = "sectionS";
        public static String LENGTH_SNTNCE = "lnght-sntnce";
        public static String WRDS_BEFORE = "wrds-before";
        public static String WRDS_AFTER = "wrds-after";
        public static String CLAZZ = "class";
        private int amountOfTotalDetections;

        public static String[] FEATURE_NAMES= new String[]{CLUE, NEAGTION_CLUE,
                DISTANCE_CLUE_TO_NGTN, DIRECTION_CLUE_TO_NGTN, SECTION, LENGTH_SNTNCE, WRDS_BEFORE, WRDS_AFTER,
                SNTNCE, AMOUNT_TOTAL_DETECTIONS, CLAZZ};;
          /*
         * Clue word
         * Negation clue
         * Distance clue to negation
         * Direction clue to negation
         * Section
         * Length of sentence
         * Words before and after
         */

        public String getContent() {
            return content;
        }

        public List<String> getClues() {
            return clues;
        }

        public List<String> getNegations() {
            return negations;
        }

        public int getDistanceClueToNegation() {
            return distanceClueToNegation;
        }

        public String getDirectionClueToNegation() {
            return directionClueToNegation;
        }

        public String getSection() {
            return section;
        }

        public int getLengthSentence() {
            return lengthSentence;
        }

        public List<String> getWordsBefore() {
            return wordsBefore;
        }

        public List<String> getWordsAfter() {
            return wordsAfter;
        }

        public String getClazz() {
            return clazz;
        }

        private String content;
        private List<String> clues;
        private List<String> negations;
        private int distanceClueToNegation;
        private String directionClueToNegation = "N/A";
        private String section;
        private int lengthSentence;
        private List<String> wordsBefore;
        private List<String> wordsAfter;
        private String clazz;

        public TextFeatures(String content) {
            this.content = content.trim();
            this.clues = new ArrayList<>();
            this.negations = new ArrayList<>();
            this.wordsAfter = new ArrayList<>();
            this.wordsBefore = new ArrayList<>();
        }

        public void addKeyword(String keyword) {
            this.clues.add(keyword);
        }

        public void setClueToNegationDistance(int distance) {
            this.distanceClueToNegation = distance;
        }

        public void setSection(String section) {
            // FEATURE_NAMES[4]
            this.section = section;
        }

        public void setClass(String clazz) {
            // FEATURE_NAMES[FEATURE_NAMES.length - 1]
            this.clazz = clazz;
        }

        public boolean containsClue(String searched) {
            for (String clue : clues) {
                if (searched.equals(clue))
                    return true;
            }
            return false;
        }

        @Override
        public int compareTo(Object o) {
            return getContent().compareTo(((TextFeatures) o).getContent());
        }

        public void addNegation(String negation) {
            negation = negation.replaceAll("-", "").toLowerCase();
            if(negation.startsWith("kein"))
                negation = "kein";
            this.negations.add(negation);
        }

        public void setClueToNegationDirection(String clueToNegationDirection) {
            this.directionClueToNegation = clueToNegationDirection;
        }

        public void setLengthOfSentence(int lengthOfSentence) {
            this.lengthSentence = lengthOfSentence;
        }

        public static List<TextFeatures> sortPreferNegation(List<TextFeatures> featuress) {
            List<TextFeatures> withNegations = featuress.stream().filter(f -> !f.getNegations().isEmpty()).collect(Collectors.toList());
            if (!withNegations.isEmpty()) {
                Collections.sort(withNegations);
                return withNegations;
            }
            Collections.sort(featuress);
            return featuress;
        }

        public void setAmountOfTotalDetections(int amountOfTotalDetections) {
            this.amountOfTotalDetections = amountOfTotalDetections;
        }

        public double getAmountTotalDetections() {
            return this.amountOfTotalDetections;
        }

        public static String getFeatureName(int number) {
            return FEATURE_NAMES[number];
        }
    }