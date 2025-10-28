package com.zoonosys.repositories;

import com.zoonosys.models.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findById(long id);

    List<Animal> findByNameContainingIgnoreCase(String name);

    /**
     * Busca animais disponíveis para adoção, excluindo aqueles que estão adotados
     * e aqueles que possuem um tratamento ativo.
     * @param pageable Parâmetros de paginação.
     * @return Página de animais disponíveis.
     */
    @Query("SELECT a FROM Animals a WHERE a.adoptingUser IS NULL")
    Page<Animal> findAvailableForAdoption(Pageable pageable);
}
