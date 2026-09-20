package re.dufau.demo.dao;

import re.dufau.demo.model.User;

import java.util.ArrayList;
import java.util.List;
public class UserDao {

    static List<User> users = new ArrayList<>();

    static {
        User user = new User();
        user.setName("default-user");
        user.setMail("mail@default-user");
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser(String name){
        return users.stream().filter(user -> user.getName().equals(name)).findFirst().orElse(null);
    }

    public void addUser(User u){
        users.add(u);
    }

}
