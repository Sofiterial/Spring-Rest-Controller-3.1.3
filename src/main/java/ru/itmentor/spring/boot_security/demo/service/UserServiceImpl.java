package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public List<User> getUserAndRoles() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User readUser(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Role> getRolesById(long id) {
        return userRepository.getRolesById(id);
    }

    @Transactional
    @Override
    public void save(User user) {
        userRepository.save(user);

    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.deleteById(id);

    }

    @Transactional
    @Override
    public void update(int id, User user) {
        user.setId((long) id);
        userRepository.save(user);


    }
}
