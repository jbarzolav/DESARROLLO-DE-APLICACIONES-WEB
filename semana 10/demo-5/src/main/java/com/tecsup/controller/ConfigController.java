package com.tecsup.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/configuracion")
public class ConfigController {

    @Value("${empresa.ambiente}")
    private String ambiente;

    @Value("${server.port}")
    private String puerto;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    private final Environment environment;

    public ConfigController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping
    public ConfiguracionResponse obtenerConfiguracion() {
        String perfil = Arrays.stream(environment.getActiveProfiles())
                .findFirst()
                .orElse("default");

        return new ConfiguracionResponse(
                ambiente,
                perfil,
                puerto,
                obtenerBaseDatos()
        );
    }

    private String obtenerBaseDatos() {
        String baseDatos = datasourceUrl.substring(datasourceUrl.lastIndexOf('/') + 1);
        return baseDatos.split("\\?")[0];
    }

    public record ConfiguracionResponse(
            String ambiente,
            String perfil,
            String puerto,
            String baseDatos
    ) {
    }
}
