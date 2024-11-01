package ru.veselov.requests_service.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.veselov.requests_service.dto.RequestDTO;
import ru.veselov.requests_service.exceptions.IllegalParamsException;
import ru.veselov.requests_service.models.Request;
import ru.veselov.requests_service.services.RequestService;
import ru.veselov.requests_service.util.RequestStatus;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final ModelMapper modelMapper;

    @PostMapping()
    public RequestDTO addRequest(@RequestBody RequestDTO requestDTO) {
        Request request = convertToRequest(requestDTO);
        return convertToRequestDTO(requestService.addRequest(request));
    }

    @GetMapping()
    public List<RequestDTO> getRequests(
            @RequestParam(required = false, name = "person_id") Long personId,
            @RequestParam(required = false, name = "person_name") String personName,
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (personId != null && personName != null && status != null)
            throw new IllegalParamsException("Bad Request");

        List<Request> requests;
        if (personId != null)
            requests = requestService.getPersonOwnRequest(pageable, personId);
        else if (personName != null && status == RequestStatus.SENT)
            requests = requestService.getSentRequestsByPersonName(pageable, personName);
        else if (status == RequestStatus.SENT)
            requests = requestService.getSentRequests(pageable);
        else
            throw new IllegalParamsException("Bad Request");

        return requests.stream()
                .map(this::convertToRequestDTO)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{requestId}")
    public RequestDTO updateRequest(@PathVariable Long requestId, @RequestBody RequestDTO requestDTO) {
        Request request = requestService.updateRequest(requestId, convertToRequest(requestDTO));
        return convertToRequestDTO(request);
    }

    @PatchMapping("/{requestId}/status")
    public RequestDTO updateStatus(@PathVariable Long requestId, @RequestParam(name = "new_status") RequestStatus newStatus) {
        Request request = requestService.getRequestById(requestId);
        if (request.getStatus() == RequestStatus.DRAFT && newStatus == RequestStatus.SENT)
            request =  requestService.sendRequest(request, newStatus);
        else if (request.getStatus() == RequestStatus.SENT &&
                (newStatus == RequestStatus.ACCEPTED || newStatus == RequestStatus.DECLINED))
            request =  requestService.processRequest(request, newStatus);
        else
            throw new IllegalParamsException("Bad Request");

        return convertToRequestDTO(request);
    }

    private Request convertToRequest(RequestDTO requestDTO) {
        return modelMapper.map(requestDTO, Request.class);
    }

    private RequestDTO convertToRequestDTO(Request request) {
        return modelMapper.map(request, RequestDTO.class);
    }
}
