package ru.veselov.requests_service.dto;

import lombok.Getter;
import lombok.Setter;
import ru.veselov.requests_service.models.Role;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private List<RoleDTO> roles;
}
