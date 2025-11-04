#!/bin/bash

# Este script executa os comandos SQL usando o sqlplus,
# garantindo que o usuário SYSDBA seja usado para a criação dos novos usuários.

# Conecta ao banco de dados XEPDB1 como SYSDBA, usando a senha definida
# na variável de ambiente ORACLE_PASSWORD (a mesma do docker-compose.yml).
sqlplus "sys/$ORACLE_PASSWORD@XEPDB1 as sysdba" <<EOF

-- Criação do usuário DEV_USER
CREATE USER DEV_USER IDENTIFIED BY a8a32cafa5879938c763;
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE, CREATE VIEW, CREATE TRIGGER TO DEV_USER;
ALTER USER DEV_USER QUOTA UNLIMITED ON USERS;

-- Criação do usuário TEST_USER
CREATE USER TEST_USER IDENTIFIED BY a8a32cafa5879938c763;
GRANT CREATE SESSION, CREATE TABLE, CREATE SEQUENCE, CREATE VIEW, CREATE TRIGGER TO TEST_USER;
ALTER USER TEST_USER QUOTA UNLIMITED ON USERS;

EXIT;
EOF

echo "Usuários DEV_USER e TEST_USER criados e configurados."