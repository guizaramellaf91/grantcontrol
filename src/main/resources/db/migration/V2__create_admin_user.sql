CREATE TABLE IF NOT EXISTS gcdb.usuario
(
    usuario_id   INT AUTO_INCREMENT,
    login        VARCHAR(50),
    nome         VARCHAR(100),
    senha        VARCHAR(100),
    email        VARCHAR(50),
    ativo        BOOLEAN,
    acessos      LONG,
    departamento VARCHAR(50),
    nivel        LONG,
    PRIMARY KEY (usuario_id)
);

INSERT IGNORE INTO usuario
(usuario_id,
 login,
 nome,
 senha,
 email,
 ativo,
 acessos,
 departamento,
 nivel)
VALUES (DEFAULT,
        'admin',
        'Guilherme Zaramella',
        '$2a$10$BxyYrsdIwc96/.6A1/JuGemVFWNQpKjjT0niLEas9z/00kYzCogv.',
        'guizaramellaf91@gmail.com',
        true,
        0,
        'GERAL',
        1);