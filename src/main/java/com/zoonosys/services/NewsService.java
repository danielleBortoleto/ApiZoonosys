package com.zoonosys.services;

import com.zoonosys.dtos.RegisterNewsDTO;
import com.zoonosys.dtos.UpdateNewsDTO;
import com.zoonosys.models.News;
import com.zoonosys.models.User;
import com.zoonosys.repositories.NewsRepository;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.zoonosys.exceptions.ResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Serviço de negócios responsável pela lógica e manipulação das entidades {@link News}.
 * Gerencia a criação, consulta e paginação das notícias, incluindo validação e segurança.
 */
@Service
public class NewsService {
    private final NewsRepository newsRepository;

    /**
     * Política de sanitização de HTML (OWASP) para prevenir ataques XSS (Cross-Site Scripting).
     * Permite tags seguras para formatação de conteúdo (h1-h6, p, ul, li, strong, i)
     * e atributos específicos (href, src, class).
     */
    private static final PolicyFactory SANITIZER_POLICY = new HtmlPolicyBuilder()
            .allowElements("p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "li", "blockquote", "pre", "br", "div")
            .allowElements("b", "i", "strong", "em", "a", "span", "code", "img")
            .allowAttributes("href").onElements("a")
            .allowAttributes("src", "alt", "title", "width", "height").onElements("img")
            .allowAttributes("class").onElements("p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "li", "div", "span") // Exemplo: permite classes para estilização
            .toFactory();

    /**
     * Construtor para injeção de dependência do repositório.
     * @param newsRepository O repositório de dados para acesso à tabela de notícias.
     */
    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    /**
     * Registra uma nova notícia no sistema.
     * <p>
     * Antes de salvar, a notícia é sanitizada usando a {@link #SANITIZER_POLICY} para
     * remover código malicioso e garantir a integridade do conteúdo.
     *
     * @param registerNewsDTO O DTO contendo os dados da notícia a ser criada.
     * @param authenticatedUser O objeto {@link User} autenticado, definido como autor da notícia.
     * @return A entidade {@link News} salva no banco de dados.
     */
    public News register(RegisterNewsDTO registerNewsDTO, User authenticatedUser) {
        News news = News.builder()
                .title(registerNewsDTO.title())
                .content(registerNewsDTO.content())
                .imageUrl(registerNewsDTO.imageUrl().orElse(null))
                .build();

        news.setUser(authenticatedUser);
        news.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // Aplicando a sanitização de segurança (XSS prevention)
        String sanitizedTitle = SANITIZER_POLICY.sanitize(news.getTitle());
        String sanitizedContent = SANITIZER_POLICY.sanitize(news.getContent());

        news.setTitle(sanitizedTitle);
        news.setContent(sanitizedContent);

        return newsRepository.save(news);
    }

    /**
     * Busca todas as notícias de forma paginada.
     *
     * @param pageable Objeto {@link Pageable} contendo os parâmetros de paginação (página, tamanho, ordenação).
     * @return Um objeto {@link Page<News>} com as notícias e metadados de paginação.
     */
    public Page<News> findAll(Pageable pageable) {

        return newsRepository.findAll(pageable);
    }

    /**
     * Busca uma notícia específica pelo seu ID.
     *
     * @param id O ID da notícia a ser buscada.
     * @return Um {@link Optional<News>} contendo a notícia se ela existir, ou vazio caso contrário.
     */
    public Optional<News> findById(Long id) {

        return newsRepository.findById(id);
    }

    /**
     * Busca e retorna uma lista de notícias cujos títulos contenham a string de busca (case-insensitive).
     *
     * @param title A string de busca para o título da notícia.
     * @return Uma lista de objetos {@link News} que correspondem ao critério de busca.
     */
    public List<News> findByTitleContainingIgnoreCase(String title) {
        return newsRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Atualiza uma notícia existente no sistema.
     *
     * @param id O ID da notícia a ser atualizada.
     * @param updateNewsDTO o DTO contendo os novos dados na notícia.
     * @return A entidade News atualizada.
     * @throws ResourceNotFoundException se a notícia com o ID não for encontrada.
     */
    public News update (Long id, UpdateNewsDTO updateNewsDTO){
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notícia não encontrada com ID: " + id));

        news.setTitle(updateNewsDTO.title());
        news.setContent(updateNewsDTO.content());

        updateNewsDTO.imageUrl().ifPresent(news::setImageUrl);

        String sanitizedTitle = SANITIZER_POLICY.sanitize(news.getTitle());
        String sanitizedContent = SANITIZER_POLICY.sanitize(news.getContent());

        news.setTitle(sanitizedTitle);
        news.setContent(sanitizedContent);

        news.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return newsRepository.save(news);
    }

    /**
     * Exclui uma notícia específica pelo seu ID.
     *
     * @param id O ID da notícia a ser excluída.
     * @throws ResourceNotFoundException se a notícia com o ID não for encontrada.
     */
    public void delete(Long id){
        if (!newsRepository.existsById(id)){
            throw new ResourceNotFoundException("Notícia não encontrada com o ID: " + id);
        }
        newsRepository.deleteById(id);
    }
}
