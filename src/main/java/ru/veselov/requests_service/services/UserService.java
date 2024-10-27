package ru.veselov.requests_service.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.veselov.requests_service.dto.UserDTO;
import ru.veselov.requests_service.exceptions.NotFoundException;
import ru.veselov.requests_service.models.Role;
import ru.veselov.requests_service.models.User;
import ru.veselov.requests_service.repositories.RoleRepository;
import ru.veselov.requests_service.repositories.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByName(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        if(users.isEmpty())
            throw new NotFoundException(String.format("Пользователь с name %s не найден", name));
        return users;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", id)));
    }

    @Transactional
    public User alterRole(Long userId, String roleName, boolean add) {
        User user = getUserById(userId);
        Role alteredRole = roleService.getRoleByName(roleName);
        Set<Role> roles = user.getRoles();

        if (add)
            roles.add(alteredRole);
        else
            roles.remove(alteredRole);

        user.setRoles(roles);
        return userRepository.save(user);
    }
}
