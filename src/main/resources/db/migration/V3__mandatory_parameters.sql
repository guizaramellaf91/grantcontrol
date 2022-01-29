CREATE TABLE IF NOT EXISTS gcdb.parametros_sistema
(
    parametro_id INT AUTO_INCREMENT,
    chave        VARCHAR(200),
    valor        VARCHAR(200),
    descricao    VARCHAR(200),
    PRIMARY KEY (parametro_id)
);

INSERT IGNORE INTO parametros_sistema
(parametro_id,
 chave,
 valor,
 descricao)
VALUES (DEFAULT,
        'modal_informativo',
        '1',
        'Exibe modal informativo na tela de clientes');

INSERT IGNORE INTO parametros_sistema
(parametro_id,
 chave,
 valor,
 descricao)
VALUES (DEFAULT,
        'email_de_envio',
        'grantcontrolmail@gmail.com',
        'Buscar parametro para enviar e-mail');

INSERT IGNORE INTO parametros_sistema
(parametro_id,
 chave,
 valor,
 descricao)
VALUES (DEFAULT,
        'page_size_list',
        '5',
        'Tamanho da paginação');

INSERT IGNORE INTO parametros_sistema
(parametro_id,
 chave,
 valor,
 descricao)
VALUES (DEFAULT,
        'google_charts_clients',
        '1',
        'Exibicao de graficos');