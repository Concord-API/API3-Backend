package com.concord.proficio.infra.configurations;

import com.concord.proficio.domain.entities.*;
import com.concord.proficio.domain.enums.ColaboradorRoleEnum;
import com.concord.proficio.infra.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(
            SetorRepository setorRepository,
            CargoRepository cargoRepository,
            EquipeRepository equipeRepository,
            ColaboradorRepository colaboradorRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // ---- SETOR ----
            Setor setorAdmin = setorRepository.findByNome("Administração")
                    .orElseGet(() -> setorRepository.save(
                            new Setor(null, "Administração", "Setor administrativo", true)
                    ));

            // ---- CARGO ----
            Cargo cargoDiretor = cargoRepository.findByNome("Diretor")
                    .orElseGet(() -> cargoRepository.save(
                            new Cargo(null, "Diretor", "Diretor do setor", true, setorAdmin)
                    ));

            // ---- EQUIPE ----
            Equipe equipeAdmin = equipeRepository.findByNome("Equipe Administrativa")
                    .orElseGet(() -> equipeRepository.save(
                            new Equipe(null, "Equipe Administrativa", "Administrador Geral", true, setorAdmin)
                    ));

            // ---- COLABORADOR ADMIN ----
            if (colaboradorRepository.findByEmail("diretor@example.com").isEmpty()) {
                Colaborador admin = new Colaborador();
                admin.setCpf("00000000000");
                admin.setNome("Richard");
                admin.setSobrenome("Cordeiro");
                admin.setEmail("diretor@example.com");
                admin.setSenha(passwordEncoder.encode("123456")); // senha default
                admin.setRole(ColaboradorRoleEnum.DIRETOR);
                admin.setStatus(true);
                admin.setCargo(cargoDiretor);
                admin.setEquipe(equipeAdmin);
                colaboradorRepository.save(admin);
            }
        };
    }
}
