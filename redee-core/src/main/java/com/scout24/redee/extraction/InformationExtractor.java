package com.scout24.redee.extraction;

import java.util.List;

/**
 * Created by dprawdzik on 08.01.18.
 */
public interface InformationExtractor {

    List<Extraction> extract(String text) throws Exception;
}
