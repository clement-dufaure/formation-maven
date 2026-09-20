package re.dufau.demo.dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    @Test
    public void getUsersTest(){
        UserDao userDao = new UserDao();
        assertEquals(1, userDao.getUsers().size());
    }


}