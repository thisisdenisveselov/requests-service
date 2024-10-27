package ru.veselov.requests_service.models;

import jakarta.persistence.*;
import lombok.*;
import ru.veselov.requests_service.util.RequestStatus;

import java.sql.Timestamp;

@Entity
@Table(name = "Requests")
@Getter
@Setter
@NoArgsConstructor
public class Request {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @Column(name = "text")
    private String text;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
}
