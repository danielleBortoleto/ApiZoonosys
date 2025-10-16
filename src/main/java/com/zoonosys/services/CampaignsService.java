package com.zoonosys.services;

import com.zoonosys.dtos.RegisterCampaignsDTO;
import com.zoonosys.dtos.UpdateCampaignsDTO;
import com.zoonosys.exceptions.ResourceNotFoundException;
import com.zoonosys.models.Campaigns;
import com.zoonosys.models.User;
import com.zoonosys.repositories.CampaignsRepository;
import com.zoonosys.security.SecuritySanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class CampaignsService {
    private final CampaignsRepository campaignsRepository;
    private final SecuritySanitizer sanitizer;

    /**
     * Construtor para injeção de dependência do repositório.
     * @param campaignsRepository O repositório de dados para acesso à tabela de campanhas.
     */
    @Autowired
    public CampaignsService(CampaignsRepository campaignsRepository, SecuritySanitizer sanitizer) {
        this.campaignsRepository =  campaignsRepository;

        this.sanitizer = sanitizer;
    }

    public Campaigns register(RegisterCampaignsDTO registerCampaignsDTO, User authenticatedUser){
        Campaigns campaigns = Campaigns.builder()
                .name(registerCampaignsDTO.name())
                .description(registerCampaignsDTO.description())
                .startDateTime(registerCampaignsDTO.startDateTime())
                .endDateTime(registerCampaignsDTO.endDateTime().orElse(null))
                .imageUrl(registerCampaignsDTO.imageUrl().orElse(null))
                .build();

        campaigns.setUser(authenticatedUser);
        campaigns.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // Aplicando a sanitização de segurança (XSS prevention)
        String sanitizedName = sanitizer.sanitize(campaigns.getName());
        String sanitizedDescription = sanitizer.sanitize(campaigns.getDescription());

        campaigns.setName(sanitizedName);
        campaigns.setDescription(sanitizedDescription);

        return campaignsRepository.save(campaigns);
    }

    /**
     * Busca todas as campanhas de forma paginada.
     *
     * @param pageable Objeto {@link Pageable} contendo os parâmetros de paginação (página, tamanho, ordenação).
     * @return Um objeto {@link Page<  Campaigns  >} com as campanhas e metadados de paginação.
     */
    public Page<Campaigns> findAll(Pageable pageable){
        return campaignsRepository.findAll(pageable);
    }

    /**
     * Busca uma campanha específica pelo seu ID.
     *
     * @param id O ID da campanha a ser buscada.
     * @return Um {@link Optional< Campaigns >} contendo a campanha se ela existir, ou vazio caso contrário.
     */
    public Optional<Campaigns> findById(Long id){
        return campaignsRepository.findById(id);
    }

    /**
     * Busca e retorna uma lista de campanhas cujos títulos contenham a string de busca (case-insensitive).
     *
     * @param name A string de busca para o nome da campanha.
     * @return Uma lista de objetos {@link Campaigns} que correspondem ao critério de busca.
     */
    public List<Campaigns> findByNameContainingIgnoreCase(String name){
        return campaignsRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Atualiza uma campanha existente no sistema.
     *
     * @param id O ID da campanha a ser atualizada.
     * @param updateCampaignsDTO DTO contendo os novos dados na campanha.
     * @return A entidade Campaigns atualizada.
     * @throws ResourceNotFoundException se a campanha com o ID não for encontrada.
     */
    public Campaigns update (Long id, UpdateCampaignsDTO updateCampaignsDTO){
        Campaigns campaigns = campaignsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Campanha não encontrada com o ID: " + id));
        campaigns.setName(updateCampaignsDTO.name());
        campaigns.setDescription(updateCampaignsDTO.description());
        campaigns.setStartDateTime(updateCampaignsDTO.startDateTime());
        campaigns.setEndDateTime(updateCampaignsDTO.endDateTime().orElse(null));
        campaigns.setUpdatedAt(updateCampaignsDTO.startDateTime());

        updateCampaignsDTO.imageUrl().ifPresent(campaigns::setImageUrl);

        String sanitizedName = sanitizer.sanitize(campaigns.getName());
        String sanitizedDescription = sanitizer.sanitize(campaigns.getDescription());

        campaigns.setName(sanitizedName);
        campaigns.setDescription(sanitizedDescription);
        campaigns.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return campaignsRepository.save(campaigns);
    }

    /**
     * Exclui uma campanha específica pelo seu ID.
     *
     * @param id O ID da campada a ser excluída.
     * @throws ResourceNotFoundException se a campanha com o ID não for encontrada.
     */
    public void delete(Long id){
        if (!campaignsRepository.existsById(id)){
            throw new ResourceNotFoundException("Campanha não encontrada com o ID: " + id);
        }
        campaignsRepository.deleteById(id);
    }
}
