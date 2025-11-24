package com.concord.proficio.infra.configurations;

import com.concord.proficio.domain.entities.*;
import com.concord.proficio.domain.enums.ColaboradorRoleEnum;
import com.concord.proficio.domain.enums.GeneroEnum;
import com.concord.proficio.infra.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;

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
                            new Setor(null, "Administração", "Setor administrativo",null ,true)
                    ));

            // ---- CARGO ----
            Cargo cargoDiretor = cargoRepository.findByNome("Diretor de Tecnologia").orElseGet(() -> {
                Cargo c = new Cargo();
                c.setNome("Diretor de Tecnologia");
                c.setDescricao("Responsável estratégico de TI");
                c.setRole(ColaboradorRoleEnum.DIRETOR);
                c.setStatus(true);
                c.setSetor(setorAdmin);
                return cargoRepository.save(c);
            });
            Cargo cargoGestor = cargoRepository.findByNome("Gestor de Projetos").orElseGet(() -> {
                Cargo c = new Cargo();
                c.setNome("Gestor de Projetos");
                c.setDescricao("Coordena projetos do setor");
                c.setRole(ColaboradorRoleEnum.GESTOR);
                c.setStatus(true);
                c.setSetor(setorAdmin);
                return cargoRepository.save(c);
            });
            Cargo cargoColaborador = cargoRepository.findByNome("Desenvolvedor Backend").orElseGet(() -> {
                Cargo c = new Cargo();
                c.setNome("Desenvolvedor Backend");
                c.setDescricao("Desenvolvimento de APIs e serviços");
                c.setRole(ColaboradorRoleEnum.COLABORADOR);
                c.setStatus(true);
                c.setSetor(setorAdmin);
                return cargoRepository.save(c);
            });

            // ---- EQUIPE ----
            Equipe equipeAdmin = equipeRepository.findByNome("Equipe Administrativa")
                    .orElseGet(() -> equipeRepository.save(
                            new Equipe(null, "Equipe Administrativa", new ArrayList<>(), null, true, setorAdmin)
                    ));

            // ---- COLABORADOR ADMIN (Diretor) ----
            if (colaboradorRepository.findByEmail("diretor@example.com").isEmpty()) {
                Colaborador admin = new Colaborador();
                admin.setCpf("00000000000");
                admin.setNome("Richard");
                admin.setSobrenome("Cordeiro");
                admin.setEmail("diretor@example.com");
                admin.setSenha(passwordEncoder.encode("123456"));
                admin.setStatus(true);
                admin.setCargo(cargoDiretor);
                admin.setEquipe(equipeAdmin);
                admin.setGenero(GeneroEnum.MASCULINO);
                Colaborador adminSaved = colaboradorRepository.save(admin);
                if (setorAdmin.getDiretor() == null) {
                    setorAdmin.setDiretor(adminSaved);
                    setorRepository.save(setorAdmin);
                }
            }

            // ---- GESTOR PADRÃO ----
            Colaborador gestor = colaboradorRepository.findByEmail("gestor@example.com").orElseGet(() -> {
                Colaborador g = new Colaborador();
                g.setCpf("11111111111");
                g.setNome("Victor");
                g.setSobrenome("Nogueira");
                g.setEmail("gestor@example.com");
                g.setSenha(passwordEncoder.encode("123456"));
                g.setStatus(true);
                g.setCargo(cargoGestor);
                g.setEquipe(equipeAdmin);
                g.setGenero(GeneroEnum.MASCULINO);
                return colaboradorRepository.save(g);
            });

            // garante gestor na equipe administrativa
            if (equipeAdmin.getGestor() == null) {
                equipeAdmin.setGestor(gestor);
                equipeRepository.save(equipeAdmin);
            }

            // ---- COLABORADOR PADRÃO ----
            if (colaboradorRepository.findByEmail("colaborador@example.com").isEmpty()) {
                Colaborador c = new Colaborador();
                c.setCpf("22222222222");
                c.setNome("Joao");
                c.setSobrenome("Andrade");
                c.setEmail("colaborador@example.com");
                c.setSenha(passwordEncoder.encode("123456"));
                c.setStatus(true);
                c.setCargo(cargoColaborador);
                c.setEquipe(equipeAdmin);
                c.setGenero(GeneroEnum.MASCULINO);
                colaboradorRepository.save(c);
            }
        };
    }
}
