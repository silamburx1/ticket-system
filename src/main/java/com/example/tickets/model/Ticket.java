package com.example.tickets.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150) private String title;
    @Column(nullable = false, length = 2000) private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10) private Priority priority;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 15) private Status status = Status.OPEN;
    @Column(nullable = false, length = 100) private String requesterName;
    @Column(nullable = false, length = 150) private String requesterEmail;
    @Column(length = 4000) private String solution;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String v) { title = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { description = v; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority v) { priority = v; }
    public Status getStatus() { return status; }
    public void setStatus(Status v) { status = v; }
    public String getRequesterName() { return requesterName; }
    public void setRequesterName(String v) { requesterName = v; }
    public String getRequesterEmail() { return requesterEmail; }
    public void setRequesterEmail(String v) { requesterEmail = v; }
    public String getSolution() { return solution; }
    public void setSolution(String v) { solution = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
