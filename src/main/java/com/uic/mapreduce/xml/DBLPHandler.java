package com.uic.mapreduce.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DBLPHandler extends DefaultHandler {

    private boolean authorFlag;
    private boolean articleFlag;
    private List<String> tempAuthorsList;
    private Set<String> uicAuthors;
    private boolean isUICAuthorPresent;

    public DBLPHandler(Set<String> uicAuthors) {
        this.uicAuthors = uicAuthors;
    }

    public List<List<String>> getListOfAuthorsList() {
        return listOfAuthorsList;
    }

    private List<List<String>> listOfAuthorsList = new ArrayList<>();
    private StringBuilder data;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        System.out.println(qName);
        if (qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings")
                || qName.equalsIgnoreCase("proceedings") || qName.equalsIgnoreCase("book") ||
                qName.equalsIgnoreCase("incollection") || qName.equalsIgnoreCase("phdthesis")) {
            tempAuthorsList = new ArrayList<>();
            articleFlag = true;
        } else if (articleFlag && qName.equalsIgnoreCase("author")) {
            authorFlag = true;
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (authorFlag) {
            String author = data.toString();
            if (uicAuthors.contains(author)) {
                isUICAuthorPresent = true;
				tempAuthorsList.add(author);
            }
            authorFlag = false;
        } else if (articleFlag) {
            if (isUICAuthorPresent) {
                listOfAuthorsList.add(tempAuthorsList);
            }
            isUICAuthorPresent = false;
            tempAuthorsList = null;
            articleFlag = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }
}
