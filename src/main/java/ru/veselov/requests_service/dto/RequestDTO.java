package ru.veselov.requests_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {
    private Long id;
    private String status;
    private String text;
    private UserDTO owner;
}
