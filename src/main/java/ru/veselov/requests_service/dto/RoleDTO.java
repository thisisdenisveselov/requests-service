package ru.veselov.requests_service.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO {
    private int id;
    private String name;
}
