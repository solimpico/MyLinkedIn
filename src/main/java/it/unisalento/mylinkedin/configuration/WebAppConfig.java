package it.unisalento.mylinkedin.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // TODO Auto-generated method stub
        registry.addMapping("/*").allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS");
    }

}

/*
 * Questa @Configuration permette di attivare i cors. Attivando i cors diamo la possibilità a servizi
 * risiedenti su domani esterni al nostro di accedere alle nostre REST API (presente nel nostro dominio)
 * Occorre specificare con quali metodi rendere disponibili le REST API.
 * È fondamentale per l'integrazione con angular per la  user interface.
 */
