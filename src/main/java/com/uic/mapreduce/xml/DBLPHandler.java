package com.uic.mapreduce.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DBLPHandler extends DefaultHandler {

    private boolean authorFlag;
    private boolean requireElementFlag;
    private List<String> tempAuthorsList;
    private Set<String> uicAuthors;
    private boolean isUICAuthorPresent;

    private List<List<String>> listOfAuthorsList = new ArrayList<>();
    private StringBuilder data;

    /**
     * For Each XML start tag this is called and checks if the element is of the required types.
     * If so we set the boolean flag to true.
     * We also look for the author element and set the its flag to true, if found.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings")
                || qName.equalsIgnoreCase("proceedings") || qName.equalsIgnoreCase("book") ||
                qName.equalsIgnoreCase("incollection") || qName.equalsIgnoreCase("phdthesis")) {
            tempAuthorsList = new ArrayList<>();
            requireElementFlag = true;
        } else if (requireElementFlag && qName.equalsIgnoreCase("author")) {
            authorFlag = true;
        }
        data = new StringBuilder();
    }

    /**
     * For each end XML element this is called.
     * We check if its the author element based on the flag. Also if its a UIC authors then only we add it to the tempAuthorsList.
     * We set the boolean flag for a UIC author which is checked if we find a "requiredElement" flag to be present.
     * We add the tempAuthorsList to the final overall list, only if the UIC author flag was true.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (authorFlag) {
            String author = data.toString();
            if (uicAuthors.contains(author)) {
                isUICAuthorPresent = true;
                tempAuthorsList.add(author);
            }
            authorFlag = false;
        } else if (requireElementFlag) {
            if (isUICAuthorPresent) {
                listOfAuthorsList.add(tempAuthorsList);
            }
            isUICAuthorPresent = false;
            tempAuthorsList = null;
            requireElementFlag = false;
        }
    }

    /**
     * We get the data between start and end element.
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }


    public DBLPHandler(Set<String> uicAuthors) {
        this.uicAuthors = uicAuthors;
    }

    public List<List<String>> getListOfAuthorsList() {
        return listOfAuthorsList;
    }
}
