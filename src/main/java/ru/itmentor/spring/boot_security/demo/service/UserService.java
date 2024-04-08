package ru.itmentor.spring.boot_security.demo.service;


import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService  {

    List<User> getUserAndRoles();

    User readUser(long id);

    Set<Role> getRolesById(long id);

    void save(User user);

    void delete(long id);

    void update(int id, User user);

}
