package re.dufau.demo.batch;

import re.dufau.demo.dao.UserDao;
import re.dufau.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserBatch {

    private static final Logger logger = LoggerFactory.getLogger(UserBatch.class);

    public void execute() {
        UserDao userDao = new UserDao();
        userDao.getUsers().forEach(this::envoyerMail);
    }

    private void envoyerMail(User user) {
        logger.info("Envoi d'un mail à {}", user.getName());
        // TODO envoyer un mail !
    }


}
