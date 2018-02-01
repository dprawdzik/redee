package com.scout24.redee.extraction.keywords.classifier;

import com.scout24.redee.extraction.jflex.Token;
import com.scout24.redee.extraction.model.LegacyExposeResponse;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ArffSaver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dprawdzik on 11.01.18.
 */
public class ArffCreatorTest {
    private ArffCreator creator;

    @Before
    public void setUp() throws Exception {
        this.creator = new ArffCreator();
    }

    @Test
    public void createFeatures() throws Exception {
        LegacyExposeResponse expose = creator.getExpose("weka/legacy-expose_41286147.json");
        testExpose(expose, new Object[]{"Balkon", Double.NaN, 1000.0, "N/A", "furnishing", 4.0, Double.NaN, Double.NaN, "--Balkon/-Terrasse"});

        expose = creator.getExpose("weka/legacy-expose_41286141.json");
        testExpose(expose, new Object[]{"Balkon", "nicht", 2.0, "forward", "furnishing", 7.0, Double.NaN, Double.NaN,
                "--ein-Balkon-ist-leider-nicht-vorhanden"});
    }

    private void testExpose(LegacyExposeResponse expose, Object[] values) throws Exception {
        boolean balcony = expose.getExpose().getRealEstate().hasBalcony();
        String descriptionNote = creator.getDescriptionsStr(expose.getExpose().getRealEstate());
        List<Token> balconyClues = creator.extractBalconyClues(descriptionNote);
        SparseInstance instance = creator.createFeatures(expose);
        List<Instance> instances = new ArrayList<>();
        instances.add(instance);
        Instances dataSet = new Instances("Dataset", creator.getAttributes(), instances.size());
        dataSet.addAll(instances);
        ArffSaver arffSaverInstance = new ArffSaver();
        arffSaverInstance.setInstances(dataSet);

        String id = expose.getExpose().getRealEstate().getId();
        for (int i = 0; i < values.length; i++) {
            String expected = String.valueOf(values[i]);
            String feature = TextFeatures.getFeatureName(i);
            String message = "feature: " + feature + " for expose: " + id + ", index: " + i + " (instance: " + instance + ")";

            Attribute attribute = dataSet.get(0).attribute(i);
            String actual;
            if(attribute.numValues() != 0)
                actual = attribute.value((int) instance.value(i));
            else actual = String.valueOf(instance.value(i));

            TestCase.assertEquals(message, expected.trim(), actual.trim());
        }
        System.out.println("balcony? " + balcony + ", clues: " + balconyClues + ", instance: " +
                instance + ", description: " + descriptionNote);
    }
}