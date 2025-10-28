package com.zoonosys.dtos;

import com.zoonosys.enums.AnimalGender;
import com.zoonosys.enums.AnimalSize;
import com.zoonosys.enums.AnimalSpecies;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Optional;

@Schema(description = "Estrutura de dados para registrar um novo animal.")
public record RegisterAnimalDTO(

    @Schema(description = "Nome do animal. Campo obrigatório.", example = "Pitoquinho")
    @NotBlank(message = "O nome do animal não pode estar vazio")
    @Size(min = 2, max = 100, message = "O nome do animal deve ter entre 2 e 100 caracteres")
    String name,

    @NotBlank(message = "A raça do animal não pode estar vazia")
    @Schema(description = "Raça do animal. Campo obrigatório.", example = "SRC - para animais sem raça")
    @Size(max = 100, message = "A raça do animal deve ter no máximo 100 caracteres")
    String breed,

    @Schema(description = "Descrição do animal. Campo opcional.", example = "Dócil e carinhoso.")
    @Size(max = 255, message = "A descrição do animal deve ter no máximo 255 caracteres")
    Optional<String> description,

    @Schema(description = "Informação do animal sobre vacinação. Campo obrigatório.", example = "Sim ou Não")
    @NotNull(message = "A informação sobre vacinação não pode estar vazia")
    Boolean isVaccinated,

    @Schema(description = "Informação do animal sobre castração. Campo obrigatório.", example = "Sim ou Não")
    @NotNull(message = "A informação sobre castração não pode estar vazia")
    Boolean isNeutered,

    Optional<String> imageUrl,

    @Schema(description = "Espécie do animal (CANINE ou FELINE). Campo obrigatório.", example = "CANINE")
    @NotNull(message = "A espécie do animal não pode ser nula")
    AnimalSpecies species,

    @Schema(description = "Gênero do animal (MALE ou FEMALE). Campo obrigatório.", example = "FEMALE")
    @NotNull(message = "O gênero do animal não pode ser nulo")
    AnimalGender gender,

    @Schema(description = "Porte do animal (SMALL, MEDIUM, LARGE). Campo obrigatório.", example = "MEDIUM")
    @NotNull(message = "O porte do animal não pode ser nulo")
    AnimalSize size
) {}
