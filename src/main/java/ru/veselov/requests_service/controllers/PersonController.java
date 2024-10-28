package ru.veselov.requests_service.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import ru.veselov.requests_service.dto.PersonDTO;
import ru.veselov.requests_service.models.Person;
import ru.veselov.requests_service.services.PersonService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public List<PersonDTO> getPersons(@RequestParam(required = false) String name) {
        if (name != null)
            return personService.getPersonsByName(name).stream().map(this::convertToPersonDTO).collect(Collectors.toList());
        else
            return personService.getPersons().stream().map(this::convertToPersonDTO).collect(Collectors.toList());
}
/*    @GetMapping("/{personId}")
    public PersonDTO getPerson(@PathVariable Long personId) {
        return modelMapper.map(personService.getPersonById(personId), PersonDTO.class);
    }*/

    @PatchMapping("/{personId}/roles")
    public PersonDTO alterRoles(@PathVariable Long personId,
                                @RequestParam String roleName,
                                @RequestParam Boolean add) {
        Person person = personService.alterRole(personId, roleName, add);
        return modelMapper.map(person, PersonDTO.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
