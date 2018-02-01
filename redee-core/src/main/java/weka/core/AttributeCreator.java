package weka.core;

import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dprawdzik on 11.01.18.
 */
public class AttributeCreator {


    public Attribute createAttribute(String feature, int index) {

        Attribute attribute = new Attribute(feature);

        if (feature.endsWith("S")) {
            attribute = new Attribute(feature, true);
        } else if (feature.endsWith("R"))
            attribute = new Attribute(feature);
        else if (feature.equals("class")) {
            List<String> clazz = new ArrayList<>();
            clazz.add("balcony");
            clazz.add("no_balcony");
            attribute = new Attribute(feature, clazz);
        }

        attribute.setIndex(index);
        return attribute;
    }
}
