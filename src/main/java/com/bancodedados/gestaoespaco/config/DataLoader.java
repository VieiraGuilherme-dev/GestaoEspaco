package com.bancodedados.gestaoespaco.config;

import com.bancodedados.gestaoespaco.model.TipoEspaco;
import com.bancodedados.gestaoespaco.model.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class DataLoader {

    @Autowired
    private DataSource dataSource;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            try (Connection conn = dataSource.getConnection()) {
                conn.setAutoCommit(false);

                if (isTableEmpty(conn, "usuario")) {
                    String sqlUsuario = "INSERT INTO usuario (nome, email, tipo, senha) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario)) {

                        // Usuário Admin (com senha padrão)
                        stmtUsuario.setString(1, "Admin do Sistema");
                        stmtUsuario.setString(2, "admin@gestao.com");
                        stmtUsuario.setString(3, TipoUsuario.ADMIN.name());
                        stmtUsuario.setString(4, "senhaadmin123");
                        stmtUsuario.addBatch(); // Adiciona ao lote

                        // Aluno Exemplo
                        stmtUsuario.setString(1, "Aluno Exemplo");
                        stmtUsuario.setString(2, "aluno@email.com");
                        stmtUsuario.setString(3, TipoUsuario.ALUNO.name());
                        stmtUsuario.setString(4, "senhaaluno123");
                        stmtUsuario.addBatch();

                        // Prof. Ana Silva
                        stmtUsuario.setString(1, "Prof. Ana Silva");
                        stmtUsuario.setString(2, "ana.silva@escola.com");
                        stmtUsuario.setString(3, TipoUsuario.PROFESSOR.name());
                        stmtUsuario.setString(4, "senhaprof123");
                        stmtUsuario.addBatch();

                        // Func. João Mendes
                        stmtUsuario.setString(1, "Func. João Mendes");
                        stmtUsuario.setString(2, "joao.mendes@escola.com");
                        stmtUsuario.setString(3, TipoUsuario.FUNCIONARIO.name());
                        stmtUsuario.setString(4, "senhafunc123");
                        stmtUsuario.addBatch();

                        stmtUsuario.executeBatch(); // Executa todas as inserções em lote
                        System.out.println("Usuários de teste inseridos na tabela 'usuario'.");
                    }
                }

                if (isTableEmpty(conn, "espaco_fisico")) {
                    String sqlEspaco = "INSERT INTO espaco_fisico (nome, tipo, capacidade) VALUES (?, ?, ?)"; // Ajustado para 'capacidade'
                    try (PreparedStatement stmtEspaco = conn.prepareStatement(sqlEspaco)) {

                        // Auditório Principal
                        stmtEspaco.setString(1, "Auditório Principal");
                        stmtEspaco.setString(2, TipoEspaco.AUDITORIO.name());
                        stmtEspaco.setInt(3, 200); // Capacidade em vez de metragem
                        stmtEspaco.addBatch();

                        // Laboratório de Informática 1
                        stmtEspaco.setString(1, "Laboratório de Informática 1");
                        stmtEspaco.setString(2, TipoEspaco.LABORATORIO.name());
                        stmtEspaco.setInt(3, 50);
                        stmtEspaco.addBatch();

                        // Sala de Aula 101
                        stmtEspaco.setString(1, "Sala de Aula 101");
                        stmtEspaco.setString(2, TipoEspaco.SALA_DE_AULA.name());
                        stmtEspaco.setInt(3, 40);
                        stmtEspaco.addBatch();

                        // Sala de Reunião Bloco B
                        stmtEspaco.setString(1, "Sala de Reunião Bloco B");
                        stmtEspaco.setString(2, TipoEspaco.SALA_DE_REUNIAO.name());
                        stmtEspaco.setInt(3, 25);
                        stmtEspaco.addBatch();

                        // Quadra Poliesportiva
                        stmtEspaco.setString(1, "Quadra Poliesportiva");
                        stmtEspaco.setString(2, TipoEspaco.QUADRA.name());
                        stmtEspaco.setInt(3, 600);
                        stmtEspaco.addBatch();

                        stmtEspaco.executeBatch();
                        System.out.println("Espaços físicos de teste inseridos na tabela 'espaco_fisico'.");
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                System.err.println("Erro ao carregar dados iniciais: " + e.getMessage());

            }
        };
    }

    // Método auxiliar para verificar se uma tabela está vazia
    private boolean isTableEmpty(Connection conn, String tableName) throws SQLException {
        try (ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return true;
    }
}