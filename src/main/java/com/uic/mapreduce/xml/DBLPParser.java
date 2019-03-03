package com.uic.mapreduce.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class DBLPParser {
    private static final Logger logger = LoggerFactory.getLogger(DBLPParser.class);

    public static void main(String args[]) {

        try {

            System.setProperty("entityExpansionLimit", "10000000");

            //Read UIC authors list
            Scanner file = new Scanner(new File(args[1]));
            Set<String> uicAuthors = new HashSet<>();
            // For each word in the input
            while (file.hasNextLine()) {
                uicAuthors.add(file.nextLine().trim());
            }


            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DBLPHandler handle = new DBLPHandler(uicAuthors);

            saxParser.parse(args[0], handle);

            List<List<String>> result = handle.getListOfAuthorsList();
            StringBuilder str;
            for (List<String> temp : result) {
                str = new StringBuilder();
                int count = 0;
                for (String tempStr : temp) {
                    str.append(tempStr);
                    if (++count < temp.size()) {
                        str.append(",");
                    }
                }
                logger.info(str.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}