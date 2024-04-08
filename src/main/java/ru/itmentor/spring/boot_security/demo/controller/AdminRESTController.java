package ru.itmentor.spring.boot_security.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.DTO.RoleDTO;
import ru.itmentor.spring.boot_security.demo.DTO.UserDTO;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class AdminRESTController {

    private final UserService userService;

    @GetMapping("/admin/getAllUsers")
    public List<UserDTO> getAllUser() {
        List<User> users = userService.getUserAndRoles();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(convertToUserDTO(user));
        }
        return userDTOS;

    }

    @GetMapping("/user/getProfile/{id}")
    public ResponseEntity<UserDTO> show(@PathVariable("id") long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("ROLE_ADMIN"))) {
            User user1 = userService.readUser(id);
            return ResponseEntity.ok(convertToUserDTO(user1));
        } else if (user.getId().equals(id)) {
            User user2 = userService.readUser(id);
            return ResponseEntity.ok(convertToUserDTO(user2));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @PostMapping("/admin/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        userService.save(user);
        return ResponseEntity.ok(convertToUserDTO(user));
    }

    @PatchMapping("/admin/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        User user1 = userService.readUser(id);
        if (user1 == null) {
            return ResponseEntity.notFound().build();
        }
        if (user.getName() != null) {
            user1.setName(user.getName());
        }
        if (user.getSurname() != null) {
            user1.setSurname(user.getSurname());
        }
        if (user.getAge() != 0) {
            user1.setAge(user.getAge());
        }
        if (user.getSurname() != null) {
            user1.setSurname(user.getSurname());
        }
        if (user.getPassword() != null) {
            user1.setPassword(user.getPassword());
        }
        if (user.getRoles() != null) {
            user1.setRoles(user.getRoles());
        }

        userService.update((int) id, user1);
        return ResponseEntity.ok(convertToUserDTO(user1));
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setAge(user.getAge());
        List<RoleDTO> roleDTOS = user.getRoles()
                .stream()
                .map(this::convertToRoleDTO)
                .collect(Collectors.toList());
        userDTO.setRoles(roleDTOS);
        return userDTO;
    }

    private RoleDTO convertToRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setRole(role.getRole());
        return roleDTO;
    }

}
