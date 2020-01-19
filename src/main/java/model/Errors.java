package model;

import java.util.ArrayList;
import java.util.List;

public class Errors {
    private List<String> errorList;

    public Errors() {
        errorList = new ArrayList<>();
    }

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
