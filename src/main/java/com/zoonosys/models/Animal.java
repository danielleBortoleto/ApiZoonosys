package com.zoonosys.models;

import com.zoonosys.enums.AnimalGender;
import com.zoonosys.enums.AnimalSize;
import com.zoonosys.enums.AnimalSpecies;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Animals")
@Table(name = "animals")
@Schema(description = "Entidade de Animal persistida no banco de dados.")
public class Animal {
    @Schema(description = "ID único de animal.", example = "11")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nome do animal.", example = "Pitoco")
    @Column(nullable = false)
    private String name;

    @Schema(description = "Raça do animal. Campo Obrigatório", example = "SRC")
    @Column(nullable = false)
    private String breed;

    @Schema(description = "Descrição do animal, campo opcional.", example = "Dócil e carinhoso(a).")
    private String description;

    @Schema(description = "Campo informativo de vacinação do animal.", example = "Sim |ou| Não")
    @Column(nullable = false)
    private Boolean isVaccinated;

    @Schema(description = "Campo informativo de castração do animal.", example = "Sim |ou| Não")
    @Column(nullable = false)
    private Boolean isNeutered;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adopting_user", nullable = true)
    @Schema(description = "Usuário que adotou. NULL se disponível para adoção.")
    private User adoptingUser;

    @Schema(description = "Espécie do animal (CANINE ou FELINE).", example = "CANINE")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalSpecies species;

    @Schema(description = "Gênero do animal (MALE ou FEMALE).", example = "FEMALE")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalSize size;

    @Schema(description = "Porte do animal (SMALL, MEDIUM, LARGE).", example = "MEDIUM")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalGender gender;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
