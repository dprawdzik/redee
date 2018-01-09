package com.scout24.redee.utils;



import com.scout24.redee.exception.ResourceException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;


public final class NameResolver {

    private NameResolver() {
        // 
    }


    public static URL resolve(final String name) throws ResourceException {

        if (name == null) {
            throw new ResourceException("Cannot create resource from 'null'!");
        }

        String nameTrimmed = name;

        if (nameTrimmed.toLowerCase(Locale.getDefault()).startsWith("file:")) {

            nameTrimmed = nameTrimmed.substring(5);
            File file = new File(nameTrimmed.replace("file:", ""));
            if (!file.exists()) {
                throw new ResourceException("Cannot resolve '" + nameTrimmed + "' to location.");
            }

            try {
                return file.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new ResourceException("Cannot resolve '" + nameTrimmed + "' to location.", e);
            }
        } else if (nameTrimmed.toLowerCase(Locale.getDefault()).startsWith("http:")) {
            try {
                return new URL(name);
            } catch (MalformedURLException e) {
                throw new ResourceException("Cannot resolve '" + nameTrimmed + "' to location.");
            }
        }

        URL url = null;
        // First, try to locate this resource through the current
        // context classloader.
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            url = contextClassLoader.getResource(nameTrimmed);
        }

        // Next, try to locate this resource through this class's classloader
        if (url == null) {
            url = NameResolver.class.getClassLoader().getResource(nameTrimmed);
        }

        // Next, try to locate this resource through the system classloader
        if (url == null) {
            url = ClassLoader.getSystemClassLoader().getResource(nameTrimmed);
        }

        // Still nothing found ?
        if (url == null) {
            throw new ResourceException("Cannot resolve '" + nameTrimmed + "' to location.");
        }

        return url;
    }

    public static File urlToFile(URL url) {
        File file;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        }
        return file;
    }
}
