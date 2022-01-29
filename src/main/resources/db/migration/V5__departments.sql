CREATE TABLE IF NOT EXISTS gcdb.departamento
(
    departamento_id INT AUTO_INCREMENT,
    nome_departamento VARCHAR(100),
    PRIMARY KEY (departamento_id)
);

INSERT IGNORE
INTO departamento
    (nome_departamento)
VALUES ('GERAL');

INSERT IGNORE
INTO departamento
(nome_departamento)
VALUES ('CONSULTA');