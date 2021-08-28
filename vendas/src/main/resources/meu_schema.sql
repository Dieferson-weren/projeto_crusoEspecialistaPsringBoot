create table cliente(
    ID INTEGER primary key auto_increment,
    NOME VARCHAR(100),
    CPF varchar(11)
);

CREATE TABLE PRODUTO(
    ID INTEGER primary key auto_increment,
    DESCRICAO VARCHAR(100),
    PRECO_UNITARIO  NUMERIC(20,2)
);

CREATE TABLE PEDIDO(
    ID INTEGER primary key auto_increment,
    CLIENTE_ID INTEGER REFERENCES CLIENTE(ID),
    DATA_PEDIDO TIMESTAMP,
    STATUS varchar(20),
    TOTAL NUMERIC(20,2)
);

CREATE TABLE ITEM_PEDIDO(
    ID INTEGER primary key auto_increment,
    PEDIDO_ID INTEGER REFERENCES PEDIDO (ID),
    PRODUTO_ID INTEGER REFERENCES PRODUTO (ID),
    QUANTIDADE INTEGER
);