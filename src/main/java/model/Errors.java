package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Errors class - Class that appends Error texts to the Collection.
 * It provides formatted Error texts, that can be shown in the GUI.
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
public class Errors {
    private List<String> errorList;

    /**
     * A constructor to initialize the list of Strings
     */
    public Errors() {
        errorList = new ArrayList<>();
    }

    /**
     * Method that appends an Error text to the list of Errors
     *
     * @param txtError Text of the Error
     */
    public void addError(String txtError) {
        errorList.add(txtError);
    }

    public String getErrors() {
        if (errorList.isEmpty()) {
            return null;
        }
        return (String.join(System.lineSeparator(), errorList));
    }

    public boolean containErrors() {
        return (!errorList.isEmpty());
    }
}
