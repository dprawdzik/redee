package com.scout24.redee.utils;

import com.scout24.redee.exception.ResourceException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;

/**
 * Created by dprawdzik on 26.02.14.
 */
public class StopWordIndex extends HashSet<String> {

    private final boolean caseInSensitive;

    public StopWordIndex(String stopWordFile) throws ResourceException {
        this(stopWordFile, true);
    }

    public StopWordIndex(String stopWordFile, boolean caseInSensitive) throws ResourceException {
        this.caseInSensitive = caseInSensitive;
        URL resolved = NameResolver.resolve(stopWordFile);
        String content = null;
        try {
            InputStream inputStream = resolved.openStream();
            content = IOUtils.toString(inputStream, Encoding.UTF_8);
        } catch (IOException e) {
            throw new ResourceException(e);
        }
        String[] split = content.split("\n");
        for (String stopped : split) {
            stopped = stopped.trim();
            if(stopped.startsWith("# "))
                continue;
            if (stopped.length() == 0)
                continue;
            if (caseInSensitive) super.add(stopped.toLowerCase());
            else super.add(stopped);
        }
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof String) {
            String s = (String) o;
            if (caseInSensitive)
                return super.contains(s.toLowerCase());
            return super.contains(s);
        }
        return super.contains(o);
    }

}
