package service;


import dao.UserDao;
import model.Errors;
import model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * UserService class - Class that holds the business rules of the application.
 * A layer between DAO and Controller - Database and GUI.
 * Primarily handles logic of User objects.
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
public class UserService {
    private UserDao userDao;

    /**
     * A constructor to create an instance of UserService object.
     * It assigns a userDao to connect the business and database layers.
     *
     * @param ud UserDao that handles database operations connected with User objects
     */
    public UserService(UserDao ud) {
        this.userDao = ud;
    }

    /**
     * Method that creates an instance of User object
     * and pass it to DAO to store in the database.
     *
     * @param uName User object attribute username
     * @param password User object attribute password
     * @param email User object attribute email
     * @param em Entity Manager to create a transaction
     */
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

    /**
     * Method to request a list of all stored usernames from DAO.
     *
     * @param em Entity Manager to pass to the DAO
     * @return List of usernames stored in the database
     */
    public List<String> getAllUsernames(EntityManager em) {
        List<User> users = userDao.readAllUsers(em);
        List<String> result = new ArrayList<>();

        users.forEach(user -> {
            result.add(user.getUsername());
        });
        return result;
    }

    /**
     * Method that requests a User object stored in database based on its username attribute.
     * Checks if the given password equals with the one stored in database for that User record.
     * If it does return that User object, otherwise returns null
     *
     * @param uName username given by the User attempting to log into the application
     * @param pass password given by the User attempting to log into the application
     * @param em Entity Manager to pass to the DAO
     * @return User object if the given attributes correspond, null if not
     * @throws NoResultException In case there is no such User object in the database
     */
    public User checkLogin(String uName, String pass, EntityManager em) throws NoResultException {
        User user = userDao.readUser(uName, em);
        if (user.getPassword().equals(pass)) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * Method that performs several checks on the attributes
     * given by the User during registration.
     * Compares the attributes with designed Patterns
     *
     * @param uName Username given by a user during registration
     * @param pass Password given by a user during registration
     * @param passCheck Password check given by a user during registration
     * @param email Email given by a user during registration
     * @param em Entity Manager to pass to the DAO
     * @return List of Errors, that is empty if attributes are correct
     */
    public Errors checkRegistration(String uName, String pass, String passCheck, String email, EntityManager em) {
        Errors errorList = new Errors();

        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Pattern digitCasePattern = Pattern.compile("[0-9]");
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Pattern noSpecialCharsPattern = Pattern.compile("[a-zA-Z0-9]");

        List<String> usernames = getAllUsernames(em);

        usernames.forEach(username -> {
            if (username.equals(uName)) {
                errorList.addError("Username already taken.");
            }
        });

        if (!noSpecialCharsPattern.matcher(uName).find()) {
            errorList.addError("Usenrame must contain only letters and numbers.");
        }

        if (pass.length() < 8) {
            errorList.addError("Your password must be at least 8 characters long.");
        }

        if (!digitCasePattern.matcher(pass).find()) {
            errorList.addError("Your password must contain at least one number.");
        }

        if (!upperCasePattern.matcher(pass).find()) {
            errorList.addError("Your password must contain at least one upper case character");
        }

        if (!lowerCasePattern.matcher(pass).find()) {
            errorList.addError("Your password must contain at least one lower case character");
        }

        if (!pass.equals(passCheck)) {
            errorList.addError("Passwords don't match.");
        }

        if (!emailPattern.matcher(email).find()) {
            errorList.addError(("Not a valid email address."));
        }

        return errorList;
    }

}
