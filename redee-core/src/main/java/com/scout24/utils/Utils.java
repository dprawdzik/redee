package com.scout24.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by dprawdzik on 09.01.18.
 */
public class Utils {

    public static File urlToFile(URL url) {
        File f;
        try {
            f = new File(url.toURI());
        } catch(URISyntaxException e) {
            f = new File(url.getPath());
        }
        return f;
    }
}
