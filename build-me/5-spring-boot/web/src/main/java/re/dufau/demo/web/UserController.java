package re.dufau.demo.web;

import re.dufau.demo.dao.UserDao;
import re.dufau.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    UserDao userDao = new UserDao();

    @ResponseBody
    @GetMapping("/hello")
    public String getHello(){
        return "Hello World !!";
    }

    @ResponseBody
    @GetMapping("/users")
    public List<User> getUsers() {
        return userDao.getUsers();
    }

    @ResponseBody
    @GetMapping("/users/{name}")
    public User getUser(@PathVariable("name") String name) {
        return userDao.getUser(name);
    }

    @ResponseBody
    @PostMapping("/users")
    public void postMessage(@RequestBody User user) {
        userDao.addUser(user);
    }

}
