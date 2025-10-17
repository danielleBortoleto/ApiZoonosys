package com.zoonosys.security;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class SecuritySanitizer {
    /**
            * Política de sanitização de HTML (OWASP) para prevenir ataques XSS (Cross-Site Scripting).
            * Permite tags seguras para formatação de conteúdo (h1-h6, p, ul, li, strong, i)
            * e atributos específicos (href, src, class).
     *  @param html O conteúdo de texto a ser sanitizado.
     *  @return O conteúdo sanitizado.
     */
    private static final PolicyFactory sanitizePolicy = new HtmlPolicyBuilder()
            .allowElements("p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "li", "blockquote", "pre", "br", "div")
            .allowElements("b", "i", "strong", "em", "a", "span", "code", "img")
            .allowAttributes("href").onElements("a")
            .allowAttributes("src", "alt", "title", "width", "height").onElements("img")
            .allowAttributes("class").onElements("p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "li", "div", "span") // Exemplo: permite classes para estilização
            .toFactory();

    public String sanitize(String html) {
        if (html == null) {
            return null;
        } return sanitizePolicy.sanitize(html);
    }
}
