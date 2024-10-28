package ru.veselov.requests_service.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.veselov.requests_service.exceptions.NotFoundException;
import ru.veselov.requests_service.models.Person;
import ru.veselov.requests_service.repositories.PersonRepository;
import ru.veselov.requests_service.security.PersonDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;
    private List<GrantedAuthority> authorities;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(s)
                .orElseThrow(() -> new NotFoundException("Введен неверный логин или пароль"));// exception message not working!

        authorities = person.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        PersonDetails personDetails = new PersonDetails(person, authorities);

        return personDetails;
    }
}
