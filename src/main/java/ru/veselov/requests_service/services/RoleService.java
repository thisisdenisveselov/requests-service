package ru.veselov.requests_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.veselov.requests_service.exceptions.NotFoundException;
import ru.veselov.requests_service.models.Role;
import ru.veselov.requests_service.repositories.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRoleByName(String name){
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(String.format("Роль с name %s не найдена", name)));
    }
}
