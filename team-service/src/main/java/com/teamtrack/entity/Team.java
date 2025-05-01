package com.teamtrack.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "tt_team"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Team {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String name;



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

    @ElementCollection
    private List<Long> userIds;




}
