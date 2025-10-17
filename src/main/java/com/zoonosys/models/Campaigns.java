package com.zoonosys.models;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity (name = "Campaigns")
@Table(name = "Campaigns")
@Schema(description = "Entidade de Campanha persistida no banco de dados.")
public class Campaigns {

    @Schema(description = "ID único da Campanha.", example = "10")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nome da campanha após sanitização.", example = "Campanha de Vacinação para Raiva")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Timestamp startDateTime;

    @Column
    private Timestamp endDateTime;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
