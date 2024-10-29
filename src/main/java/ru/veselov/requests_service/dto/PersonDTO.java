package ru.veselov.requests_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PersonDTO {
    private Long id;
    private String name;
    private Set<RoleDTO> roles;
}
