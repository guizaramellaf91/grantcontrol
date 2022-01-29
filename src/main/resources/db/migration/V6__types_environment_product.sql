CREATE TABLE IF NOT EXISTS tipo_ambiente
(
    tipo_ambiente_id INT AUTO_INCREMENT,
    nome_ambiente    VARCHAR(100),
    PRIMARY KEY (tipo_ambiente_id)
);

CREATE TABLE IF NOT EXISTS tipo_produto
(
    tipo_produto_id INT AUTO_INCREMENT,
    nome_produto    VARCHAR(100),
    PRIMARY KEY (tipo_produto_id)
);

INSERT IGNORE
INTO tipo_ambiente
    (nome_ambiente)
VALUES ('DEVELOPMENT ENVIRONMENT');
INSERT IGNORE
INTO tipo_ambiente
    (nome_ambiente)
VALUES ('APPROVAL ENVIRONMENT');
INSERT IGNORE
INTO tipo_ambiente
    (nome_ambiente)
VALUES ('PRODUCTION ENVIRONMENT');

INSERT IGNORE
INTO tipo_produto
    (nome_produto)
VALUES ('Linux Server');
INSERT IGNORE
INTO tipo_produto
    (nome_produto)
VALUES ('Windows Server');
INSERT IGNORE
INTO tipo_produto
    (nome_produto)
VALUES ('Database Oracle');
INSERT IGNORE
INTO tipo_produto
    (nome_produto)
VALUES ('Database MySQL');