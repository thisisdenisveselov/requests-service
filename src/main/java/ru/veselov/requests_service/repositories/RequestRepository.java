package ru.veselov.requests_service.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.veselov.requests_service.dto.RequestDTO;
import ru.veselov.requests_service.models.Request;
import ru.veselov.requests_service.models.User;
import ru.veselov.requests_service.util.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByOwner(Pageable pageable, User owner);
    List<Request> findByStatus(Pageable pageable, RequestStatus status);
    List<Request> findByOwnerAndStatus(Pageable pageable, User owner, RequestStatus status);
}