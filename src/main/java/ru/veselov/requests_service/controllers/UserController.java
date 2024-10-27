package ru.veselov.requests_service.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.veselov.requests_service.dto.UserDTO;
import ru.veselov.requests_service.models.User;
import ru.veselov.requests_service.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public List<UserDTO> getUsers(@RequestParam(required = false) String name) {
        if (name != null)
            return userService.getUsersByName(name).stream().map(this::convertToUserDTO).collect(Collectors.toList());
        else
            return userService.getUsers().stream().map(this::convertToUserDTO).collect(Collectors.toList());
}
    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable Long userId) {
        return modelMapper.map(userService.getUserById(userId), UserDTO.class);
    }

    @PatchMapping("/{userId}/roles")
    public UserDTO alterRoles(@PathVariable Long userId,
                              @RequestParam String roleName,
                              @RequestParam Boolean add) {
        User user = userService.alterRole(userId, roleName, add);
        return modelMapper.map(user, UserDTO.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
