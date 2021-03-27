package com.springboot.demo.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * Utils for {@link Document} operation.
 *
 * @author Beck.Xu
 * @since 26/03/2021
 */
public class DocumentUtils {

    private static final SAXReader reader = new SAXReader();

    public static Document parse(InputStream inputStream) throws DocumentException {
        return reader.read(inputStream);
    }
}
