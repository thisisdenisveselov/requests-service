package ru.veselov.requests_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.veselov.requests_service.exceptions.ForbiddenActionException;
import ru.veselov.requests_service.exceptions.IllegalParamsException;
import ru.veselov.requests_service.exceptions.NotFoundException;
import ru.veselov.requests_service.models.Request;
import ru.veselov.requests_service.models.Person;
import ru.veselov.requests_service.repositories.RequestRepository;
import ru.veselov.requests_service.util.RequestStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final PersonService personService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public Request addRequest(Request request) {
        Person person = personService.getPersonById(request.getOwner().getId());

        request.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        request.setStatus(RequestStatus.DRAFT);
        request.setOwner(person);

        return requestRepository.save(request);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public Request updateRequest(Long requestId, Request updatedRequest) {
        Request request = getRequestById(requestId);
        if (request.getStatus() != RequestStatus.DRAFT)
            throw new ForbiddenActionException("Изменение заявок в статусе отличном от DRAFT не доступно");

        Person person = personService.getPersonById(updatedRequest.getOwner().getId());

        updatedRequest.setId(requestId);
        updatedRequest.setCreatedAt(request.getCreatedAt());
        updatedRequest.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        updatedRequest.setStatus(RequestStatus.DRAFT);
        updatedRequest.setOwner(person);

        return requestRepository.save(updatedRequest);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional(readOnly = true)
    public List<Request> getPersonOwnRequest(Pageable pageable, Long personId) {
        Person person = personService.getPersonById(personId);
        return requestRepository.findByOwner(pageable, person);
    }

    @Transactional(readOnly = true)
    public Request getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Заявка с id %d не найдена", id)));
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @Transactional(readOnly = true)
    public List<Request> getSentRequests(Pageable pageable) {
        List<Request> requests = requestRepository.findByStatus(pageable, RequestStatus.SENT);
        return modifyText(requests);
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @Transactional(readOnly = true)
    public List<Request> getSentRequestsByPersonName(Pageable pageable, String userName) {
        Person person = getPersonByName(userName);
        List<Request> requests =  requestRepository.findByOwnerAndStatus(pageable, person, RequestStatus.SENT);
        return modifyText(requests);
    }

    private static List<Request> modifyText(List<Request> requests) {
        requests.forEach(request -> {
            String modifiedText = addDashes(request.getText());
            request.setText(modifiedText);
        });
        return requests;
    }

    private static String addDashes(String str) {
        return str.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining("-"));
    }

    private Person getPersonByName(String personName) {
        List<Person> people = personService.getPersonsByName(personName);
        if(people.size() > 1)
            throw new IllegalParamsException(String.format("Найдено более одного пользователя с именем %s. Уточните поиск.", personName));
        return people.get(0);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public Request sendRequest(Request request, RequestStatus newStatus) {
        request.setStatus(newStatus);

        return requestRepository.save(request);
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @Transactional
    public Request processRequest(Request request, RequestStatus newStatus) {
        request.setStatus(newStatus);

        return requestRepository.save(request);
    }
}
