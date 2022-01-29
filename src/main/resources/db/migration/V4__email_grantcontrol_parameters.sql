CREATE TABLE IF NOT EXISTS gcdb.parametros_email
(
    parametro_email_id INT AUTO_INCREMENT,
    email_from VARCHAR(100),
    email_from_name VARCHAR(100),
    email_port INT(2),
    email_smtp_host VARCHAR(50),
    email_smtp_password VARCHAR(100),
    email_smtp_username VARCHAR(100),
    email_subject VARCHAR(200),
    config_set VARCHAR(50),
    url_envio VARCHAR(255),
    PRIMARY KEY (parametro_email_id)
);

INSERT IGNORE
INTO parametros_email
(parametro_email_id,
 email_from,
 email_from_name,
 email_port,
 email_smtp_host,
 email_smtp_password,
 email_smtp_username,
 email_subject,
 config_set,
 url_envio)
VALUES (DEFAULT,
        'grantcontrolmail@gmail.com',
        'Grant Control Mail',
        587,
        'smtp.gmail.com',
        'Gr@nTcOnTr0l#21',
        'grantcontrolmail@gmail.com',
        'GrantControl - Validation email',
        'ConfigSet',
        'http://localhost:8580/grantcontrol/'
        );