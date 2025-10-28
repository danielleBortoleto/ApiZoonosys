package com.zoonosys.controllers;

import com.zoonosys.dtos.RegisterAnimalDTO;
import com.zoonosys.dtos.RegisterNewsDTO;
import com.zoonosys.dtos.UpdateAnimalDTO;
import com.zoonosys.exceptions.ResourceNotFoundException;
import com.zoonosys.models.Animal;
import com.zoonosys.models.News;
import com.zoonosys.models.User;
import com.zoonosys.security.userdetails.UserDetailsImpl;
import com.zoonosys.services.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/animals")
@Tag(name = "Animals", description = "Endpoints para gerenciamento e consulta de animais cadastrados.")
public class AnimalController {

    private final AnimalService animalService;

    @Autowired
    public AnimalController(AnimalService animalService){
        this.animalService = animalService;
    }
    @Operation(
            summary = "Registrar um novo animal",
            description = "Cria um novo animal. Requer token JWT e a autoridade 'ROLE_ADMINISTRATOR'.",
            tags = {"Animals", "Administração"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Animal registrado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animal.class))),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)."),
                    @ApiResponse(responseCode = "403", description = "Proibido (Usuário sem 'ROLE_ADMINISTRATOR')."),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (Erro de validação do DTO).")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<Animal> registerAnimal(
            @RequestBody @Valid
            RegisterAnimalDTO registerAnimalDTO,
            @AuthenticationPrincipal UserDetailsImpl authenticatedUserDetails) {
        User authenticatedUser = authenticatedUserDetails.getUser();
        Animal createdAnimal = animalService.register(registerAnimalDTO, authenticatedUser);
        return new ResponseEntity<>(createdAnimal, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar animais disponíveis para adoção (Edital Público)",
            description = "Retorna uma lista paginada de animais que estão com o status 'disponível' (idAdoptingUser é NULL e não estão em tratamento). Acesso público.",
            tags = {"Animals", "Público"}
    )
    @GetMapping("/adocao")
    public ResponseEntity<Page<Animal>> getAvailableAnimals(Pageable pageable) {
        Page<Animal> availablePage = animalService.findAvailableForAdoption(pageable);
        return new ResponseEntity<>(availablePage, HttpStatus.OK);
    }

    @Operation(
            summary = "Listar todos os animais",
            description = "Retorna uma lista de animais paginada. Acesso privado e ordenado pelos mais recentes, por padrão.",
            tags = {"Animals"},
            parameters = {
                    @Parameter(name = "page", description = "Número da página a ser buscada (inicia em 0).", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de itens por página.", example = "10"),
                    @Parameter(name = "sort", description = "Propriedade e direção da ordenação (ex: createdAt,desc).", example = "createdAt,desc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de animais paginada retornada com sucesso."
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<Animal>> getAllAnimal(Pageable pageable) {
        Page<Animal> animalPage = animalService.findAll(pageable);
        return new ResponseEntity<>(animalPage, HttpStatus.OK);
    }

    @Operation(
            summary = "Buscar animal por ID",
            description = "Retorna um animal específico com base no ID fornecido no path. Acesso privado.",
            tags = {"Animals"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Animal encontrado.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animal.class))),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado.")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable Long id) {
        Optional<Animal> animal = animalService.findById(id);

        return animal.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Animal>> searchAnimalByName(
            @RequestParam (name = "name") String name) {

        List<Animal> animalList = animalService.findByNameContainingIgnoreCase(name);
        return new ResponseEntity<>(animalList, HttpStatus.OK);
    }

    @Operation(
            summary = "Atualizar um cadastro de animal existente",
            description = "Atualiza os dados de um animal pelo ID. Requer token JWT e a autoridade 'ROLE_ADMINISTRATOR'.",
            tags = {"Animals", "Administração"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Animal atualizado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animal.class))),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrada."),
                    @ApiResponse(responseCode = "401", description = "Não autorizado."),
                    @ApiResponse(responseCode = "403", description = "Proibido (Usuário sem 'ROLE_ADMINISTRATOR')."),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida.")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Animal> updateAnimal(
            @PathVariable long id,
            @RequestBody @Valid UpdateAnimalDTO updateAnimalDTO) {
        try{
            Animal updatedAnimal = animalService.update(id, updateAnimalDTO);
            return new ResponseEntity<>(updatedAnimal, HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Deletar um cadastro de animal",
            description = "Exclui um animal pelo ID. Requer token JWT e a autoridade 'ROLE_ADMINISTRATOR'.",
            tags = {"Animals", "Administração"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Animal excluído com sucesso (No Content)."),
                    @ApiResponse(responseCode = "404", description = "Animal não encontrado."),
                    @ApiResponse(responseCode = "401", description = "Não autorizado."),
                    @ApiResponse(responseCode = "403", description = "Proibido (Usuário sem 'ROLE_ADMINISTRATOR').")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Animal> deleteAnimal(@PathVariable Long id){
        try{
            animalService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
