package com.teamtrack.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
@Entity
@Table(
        name = "tt_Comment"
)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user ;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task ;
    private String message;
    private boolean deleted = false;
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String modifiedBy;
}
