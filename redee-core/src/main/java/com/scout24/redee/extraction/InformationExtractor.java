package com.scout24.redee.extraction;

import java.util.Collection;
import java.util.List;

/**
 * Created by dprawdzik on 08.01.18.
 */
public interface InformationExtractor<T extends Extraction> {

    Collection<T> extract(String text) throws Exception;
}
