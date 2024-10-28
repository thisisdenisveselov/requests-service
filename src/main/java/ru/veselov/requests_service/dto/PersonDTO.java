package ru.veselov.requests_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PersonDTO {
    private Long id;
    private String name;
/*    private String username;
    private String password;*/
    private List<RoleDTO> roles;
}
