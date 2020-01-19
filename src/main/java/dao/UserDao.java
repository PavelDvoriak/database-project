package dao;

import model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class UserDao {

    public User readUser(String uName, EntityManager em) throws NoResultException {
        return em.createQuery("from User where username like :username", User.class)
                .setParameter("username", uName)
                .getSingleResult();
    }

    public User saveUser(User user, EntityManager em) {
        if (user.getId() == null) {
            em.persist(user);
        } else if (!em.contains(user)) {
            user = em.merge(user);
        }
        return user;
    }

    public List<User> readAllUsers(EntityManager em) {
        return em.createQuery("from User", User.class).getResultList();
    }
}
