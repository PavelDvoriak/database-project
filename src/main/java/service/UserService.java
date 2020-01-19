package service;


import dao.UserDao;
import model.Errors;
import model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserService {
    private UserDao userDao;

    public UserService(UserDao ud) {
        this.userDao = ud;
    }

    public void createNewUser(String uName, String password, String email, EntityManager em) {
        User user = new User(uName, password, email);

        em.getTransaction().begin();

        try {
            userDao.saveUser(user, em);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public List<String> userNamesLookup(EntityManager em) {
        List<User> users = userDao.readAllUsers(em);
        List<String> result = new ArrayList<>();

        users.forEach(user -> {
            result.add(user.getUsername());
        });
        return result;
    }

    //add password hashing
    public boolean checkLogin(String uName, String pass, EntityManager em) throws NoResultException {
        if (userDao.readUser(uName, em).getPassword().equals(pass)) {
            return true;
        } else {
            return false;
        }
    }

    public Errors checkRegistration(String uName, String pass, String passCheck, String email, EntityManager em)  {
        Errors errorList = new Errors();

        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Pattern digitCasePattern = Pattern.compile("[0-9]");
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Pattern noSpecialCharsPattern = Pattern.compile("[a-zA-Z0-9]");

        List<String> usernames = userNamesLookup(em);

        usernames.forEach(username -> {
            if(username.equals(uName)) {
                errorList.addError("Username already taken.");
            }
        });

        if(!noSpecialCharsPattern.matcher(uName).find()) {
            errorList.addError("Usenrame must contain only letters and numbers.");
        }

        if(pass.length() < 8) {
            errorList.addError("Your password must be at least 8 characters long.");
        }

        if(!digitCasePattern.matcher(pass).find()) {
            errorList.addError("Your password must contain at least one number.");
        }

        if(!upperCasePattern.matcher(pass).find()) {
            errorList.addError("Your password must contain at least one upper case character");
        }

        if(!lowerCasePattern.matcher(pass).find()) {
            errorList.addError("Your password must contain at least one lower case character");
        }

        if(!pass.equals(passCheck)) {
            errorList.addError("Passwords don't match.");
        }

        if(!emailPattern.matcher(email).find()) {
            errorList.addError(("Not a valid email address."));
        }

        return errorList;
    }

}
