package com.zoonosys.controllers;

import com.zoonosys.dtos.UpdateCampaignsDTO;
import com.zoonosys.exceptions.ResourceNotFoundException;
import com.zoonosys.models.Campaigns;
import com.zoonosys.models.User;
import com.zoonosys.security.userdetails.UserDetailsImpl;
import com.zoonosys.services.CampaignsService;
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
import com.zoonosys.dtos.RegisterCampaignsDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/campaigns")
@Tag(name = "Campanhas", description = "Endpoints para gerenciamento e consulta de campanhas.")
public class CampaignsController {

    private final CampaignsService campaignsService;

    @Autowired
    public CampaignsController(CampaignsService campaignsService) {
        this.campaignsService = campaignsService;
    }

    @Operation(
            summary = "Registrar uma nova campanha",
            description = "Cria uma nova campanha. Requer token JWT e a autoridade 'ROLE_ADMINISTRATOR'.",
            tags = {"Campanhas", "Administração"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Campanha registrada com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Campaigns.class))),
                    @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente ou inválido)."),
                    @ApiResponse(responseCode = "403", description = "Proibido (Usuário sem 'ROLE_ADMINISTRATOR')."),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (Erro de validação do DTO).")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<Campaigns> createCampaign(
    @RequestBody @Valid
    RegisterCampaignsDTO registerCampaignsDTO,
    @AuthenticationPrincipal UserDetailsImpl authenticatedUserDetails) {
        User authenticatedUser = authenticatedUserDetails.getUser();
        Campaigns createdCampaign = campaignsService.register(registerCampaignsDTO, authenticatedUser);
        return new ResponseEntity<>(createdCampaign, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar todas as Campanhas",
            description = "Retorna uma lista de campanhas paginada. Acesso público e ordenado pelas mais recentes, por padrão.",
            tags = {"Campanhas"},
            parameters = {
                    @Parameter(name = "page", description = "Número da página a ser buscada (inicia em 0).", example = "0"),
                    @Parameter(name = "size", description = "Quantidade de itens por página.", example = "10"),
                    @Parameter(name = "sort", description = "Propriedade e direção da ordenação (ex: createdAt,desc).", example = "createdAt,desc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de campanhas paginada retornada com sucesso."
                            // O schema para Page<Campaigns> é complexo e geralmente inferido automaticamente
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<Campaigns>> getAllCampaigns(Pageable pageable){
        Page<Campaigns> campaignsPage = campaignsService.findAll(pageable);
        return new ResponseEntity<>(campaignsPage, HttpStatus.OK);
    }

    @Operation(
            summary = "Buscar campanhas por ID",
            description = "Retorna uma campanha específica com base no ID fornecido no path. Acesso público.",
            tags = {"Campanha"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Campanha encontrada.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Campaigns.class))),
                    @ApiResponse(responseCode = "404", description = "Campanha não encontrada.")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Campaigns> getCampaignsById(@PathVariable Long id){
        Optional<Campaigns> campaigns = campaignsService.findById(id);

        return campaigns.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("search")
    public ResponseEntity<List<Campaigns>> searchCampaignsByName(
            @RequestParam (name = "name") String name) {

        List<Campaigns> campaignsList = campaignsService.findByNameContainingIgnoreCase(name);
        return new ResponseEntity<>(campaignsList, HttpStatus.OK);
    }

    @Operation(
            summary = "Atualizar uma campanha existente",
            description = "Atualiza os dados de uma campanha pelo ID. Requer token JWT e a autoridade 'ROLE_ADMINISTRATOR'.",
            tags = {"Campanhas", "Administração"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Campanha atualizada com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Campaigns.class))),
                    @ApiResponse(responseCode = "404", description = "Campanha não encontrada."),
                    @ApiResponse(responseCode = "401", description = "Não autorizado."),
                    @ApiResponse(responseCode = "403", description = "Proibido (Usuário sem 'ROLE_ADMINISTRATOR')."),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida.")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Campaigns> updateCampaigns(
            @PathVariable long id,
            @RequestBody @Valid UpdateCampaignsDTO updateCampaignsDTO) {
        try{
            Campaigns updatedCampaigns = campaignsService.update(id, updateCampaignsDTO);
            return new ResponseEntity<>(updatedCampaigns, HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Deletar uma campanha",
            description = "Exclui uma campanha pelo ID. Requer token JWT e a autoridade 'ROLE_ADMINISTRATOR'.",
            tags = {"Campanhas", "Administração"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Campanha excluída com sucesso (No Content)."),
                    @ApiResponse(responseCode = "404", description = "Campanha não encontrada."),
                    @ApiResponse(responseCode = "401", description = "Não autorizado."),
                    @ApiResponse(responseCode = "403", description = "Proibido (Usuário sem 'ROLE_ADMINISTRATOR').")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Campaigns> deleteCampaigns(@PathVariable long id){
        try{
            campaignsService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
