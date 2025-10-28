package com.zoonosys.services;

import com.zoonosys.dtos.RegisterAnimalDTO;
import com.zoonosys.dtos.UpdateAnimalDTO;
import com.zoonosys.exceptions.ResourceNotFoundException;
import com.zoonosys.models.Animal;
import com.zoonosys.models.User;
import com.zoonosys.repositories.AnimalRepository;
import com.zoonosys.security.SecuritySanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Serviço de negócios responsável pela lógica e manipulação das entidades {@link Animal}.
 * Gerencia a criação, consulta e paginação dos animais, incluindo validação e segurança.
 */
@Service
public class AnimalService {
    private final AnimalRepository animalRepository;

    /**
     * Construtor para injeção de dependência do repositório.
     * @param animalRepository O repositório de dados para acesso à tabela de animais.
     */
    @Autowired
    public AnimalService(AnimalRepository animalRepository, SecuritySanitizer sanitizer) {
        this.animalRepository = animalRepository;

    }

    public Animal register(RegisterAnimalDTO registerAnimalDTO, User authenticatedUser){
        Animal animal = Animal.builder()
                .name(registerAnimalDTO.name())
                .description(registerAnimalDTO.description().orElse(null))
                .breed(registerAnimalDTO.breed())
                .species(registerAnimalDTO.species())
                .gender(registerAnimalDTO.gender())
                .size(registerAnimalDTO.size())
                .isVaccinated(registerAnimalDTO.isVaccinated())
                .isNeutered(registerAnimalDTO.isNeutered())
                .imageUrl(registerAnimalDTO.imageUrl().orElse(null))
                .adoptingUser(null)
                .build();

        animal.setUser(authenticatedUser);
        animal.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return animalRepository.save(animal);
    }

    /**
     * Busca todos os animais de forma paginada.
     *
     * @param pageable Objeto {@link Pageable} contendo os parâmetros de paginação (página, tamanho, ordenação).
     * @return Um objeto {@link Page<  Animal  >} com os animais e metadados de paginação.
     */
    public Page<Animal> findAll(Pageable pageable){
        return animalRepository.findAll(pageable);
    }

    /**
     * Busca um animal específico pelo seu ID.
     *
     * @param id O ID do animal a ser buscado.
     * @return Um {@link Optional< Animal >} contendo o animal se ele existir, ou vazio caso contrário.
     */
    public Optional<Animal> findById(Long id){
        return animalRepository.findById(id);
    }

    /**
     * Busca e retorna uma lista de animais cujos nomes contenham a string de busca (case-insensitive).
     *
     * @param name A string de busca para o nome do animal.
     * @return Uma lista de objetos {@link Animal} que correspondem ao critério de busca.
     */
    public List<Animal> findByNameContainingIgnoreCase(String name){
        return animalRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Busca todos os animais que estão disponíveis para adoção (Edital Público).
     * Critérios: Não adotado (adoptingUser IS NULL) e não em tratamento ativo.
     *
     * @param pageable Objeto {@link Pageable} contendo os parâmetros de paginação.
     * @return Uma página de animais disponíveis.
     */
    public Page<Animal> findAvailableForAdoption(Pageable pageable) {
        return animalRepository.findAvailableForAdoption(pageable);
    }

    /**
     * Atualiza um animal existente no sistema.
     *
     * @param id O ID do cadastro do animal a ser atualizado.
     * @param updateAnimalDTO o DTO contendo os novos dados no cadastro do animal.
     * @return A entidade Animal atualizada.
     * @throws ResourceNotFoundException se o animal com o ID não for encontrado.
     */
    public Animal update (Long id, UpdateAnimalDTO updateAnimalDTO){
        Animal animal = animalRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Animal não encontrado com ID: " + id));

        animal.setName(updateAnimalDTO.name());
        animal.setSpecies(updateAnimalDTO.species());
        animal.setGender(updateAnimalDTO.gender());
        animal.setSize(updateAnimalDTO.size());
        animal.setDescription(updateAnimalDTO.description().orElse(null));
        animal.setBreed(updateAnimalDTO.breed());
        animal.setIsVaccinated(updateAnimalDTO.isVaccinated());
        animal.setIsNeutered(updateAnimalDTO.isNeutered());

        updateAnimalDTO.imageUrl().ifPresent(animal::setImageUrl);

        animal.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return animalRepository.save(animal);
    }

    /**
     * Exclui um cadastro de animal específico pelo seu ID.
     *
     * @param id O ID do animal a ser excluído.
     * @throws ResourceNotFoundException se o animal com o ID não for encontrado.
     */
    public void delete(Long id){
        if(!animalRepository.existsById(id)){
            throw new ResourceNotFoundException("Animal não encontrado com o ID: " + id);
        }
        animalRepository.deleteById(id);
    }
}
