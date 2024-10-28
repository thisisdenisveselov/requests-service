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


/*    @Transactional(readOnly = true)
    public List<Request> getAllRequests(Pageable pageable) {
        return requestRepository.findAll(pageable).stream()
                .collect(Collectors.toList());
    }*/

    @Transactional(readOnly = true)
    public Request getRequestById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Заявка с id %d не найдена", id)));
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")//???
    @Transactional(readOnly = true)
    public List<Request> getRequestByStatus(Pageable pageable, RequestStatus status) {
        return requestRepository.findByStatus(pageable, status);
    }

    @Transactional(readOnly = true)
    public List<Request> getRequestByUserId(Pageable pageable, Long userId) {
        Person person = personService.getPersonById(userId);
        return requestRepository.findByOwner(pageable, person);
    }

    @Transactional(readOnly = true)
    public List<Request> getRequestByUserName(Pageable pageable, String userName) {
        Person person = getUserByName(userName);
        return requestRepository.findByOwner(pageable, person);
    }

    @Transactional(readOnly = true)
    public List<Request> getRequestByUserNameAndStatus(Pageable pageable, String userName, RequestStatus status) {
        Person person = getUserByName(userName);
        return requestRepository.findByOwnerAndStatus(pageable, person, status);
    }

    private Person getUserByName(String userName) {
        List<Person> people = personService.getPersonsByName(userName);
        if(people.size() > 1)
            throw new IllegalParamsException(String.format("Найдено более одного пользователя с именем %s. Уточните поиск.", userName));
        return people.get(0);
    }

    @Transactional
    public Request updateStatus(Long requestId, RequestStatus newStatus) {
        Request request = getRequestById(requestId);
        request.setStatus(newStatus);

        return requestRepository.save(request);
    }
}
