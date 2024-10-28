package ru.veselov.requests_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.veselov.requests_service.exceptions.NotFoundException;
import ru.veselov.requests_service.models.Person;
import ru.veselov.requests_service.models.Role;
import ru.veselov.requests_service.repositories.PersonRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final RoleService roleService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true)
    public List<Person> getPersons() {
        return personRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR') || hasRole('ROLE_ADMIN')")
    @Transactional(readOnly = true)
    public List<Person> getPersonsByName(String name) {
        List<Person> people = personRepository.findByNameContainingIgnoreCase(name);
        if(people.isEmpty())
            throw new NotFoundException(String.format("Пользователь с name %s не найден", name));
        return people;
    }

    @Transactional(readOnly = true)
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", id)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public Person alterRole(Long personId, String roleName, boolean add) {
        Person person = getPersonById(personId);
        Role alteredRole = roleService.getRoleByName(roleName);
        Set<Role> roles = person.getRoles();

        if (add)
            roles.add(alteredRole);
        else
            roles.remove(alteredRole);

        person.setRoles(roles);
        return personRepository.save(person);
    }
}
