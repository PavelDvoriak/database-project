package dao;

import model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

/**
 * GameDao class - Data access objects are the lowest layer of the application.
 * It has straight access to the physical data and can manipulate it.
 * This particular DAO serves the User objects.
 *
 * @author Pavel Dvoriak
 * @version 20.01.2020
 */
public class UserDao {

    /**
     * Method to retrieve a particular User object from the database,
     * based on users' username.
     *
     * @param uName Username to find a particular User object
     * @param em Entity Manager to perform ORM database operations
     * @return Requested User object
     * @throws NoResultException Error is thrown if no such User object is stored in the database
     */
    public User readUser(String uName, EntityManager em) throws NoResultException {
        return em.createQuery("from User where username like :username", User.class)
                .setParameter("username", uName)
                .getSingleResult();
    }

    /**
     * Method that stores a User object in the database, if not
     * already there.
     *
     * @param user User object to be stored
     * @param em   Entity Manager to perform ORM database operations
     * @return User object that had been stored
     */
    public User saveUser(User user, EntityManager em) {
        if (user.getId() == null) {
            em.persist(user);
        } else if (!em.contains(user)) {
            user = em.merge(user);
        }
        return user;
    }

    /**
     * Method that queries the User table for all Users stored there.
     *
     * @param em Entity Manager is used for creating queries in ORM
     * @return An empty list if there are no records in database or contains User objects
     */
    public List<User> readAllUsers(EntityManager em) {
        return em.createQuery("from User", User.class).getResultList();
    }
}
