-- Create database db_sata
-- DROP DATABASE db_sata;
CREATE DATABASE db_sata
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Portuguese_Brazil.1252'
       LC_CTYPE = 'Portuguese_Brazil.1252'
       CONNECTION LIMIT = -1;


-- DROP TABLE "Ativo";
-- DROP TABLE "CotacaoAtivo";
-- DROP TABLE "CotacaoOpcao";
-- DROP TABLE "CotacaoConsultaOpcao";
-- DROP TABLE "OpcoesLiquidas";

-- Table: "Ativo"
CREATE TABLE "Ativo"
(
  "codigoAtivo" character varying(10) NOT NULL,
  "nomeEmpresa" character varying(500),
  CONSTRAINT "Ativo_pkey" PRIMARY KEY ("codigoAtivo")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "Ativo" OWNER TO postgres;

-- Table: "CotacaoAtivo"
CREATE TABLE "CotacaoAtivo"
(
  "codigoAtivo" character varying(10) NOT NULL,
  periodo timestamp without time zone NOT NULL,
  tipoperiodo character varying(4),
  abertura character varying(10),
  maxima character varying(10),
  minima character varying(10),
  fechamento character varying(10),
  ano character varying(4),
  volume character varying(20),
  CONSTRAINT "CotacaoAtivo_pkey" PRIMARY KEY ("codigoAtivo", periodo),
  CONSTRAINT "CotacaoAtivo_codigoativo_fkey" FOREIGN KEY ("codigoAtivo")
      REFERENCES "Ativo" ("codigoAtivo") MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "CotacaoAtivo" OWNER TO postgres;

-- Table: "CotacaoConsultaOpcao"
CREATE TABLE "CotacaoConsultaOpcao"
(
  "codigoAtivo" character varying(10) NOT NULL,
  dataHoraConsulta character varying(40),
  cotacao character varying(10),
  strike character varying(10),
  valorExtrinseco character varying(10),
  valorIntrinseco character varying(10),
  prcVE character varying(10),
  prcVI character varying(10)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "CotacaoConsultaOpcao" OWNER TO postgres;


-- Table: "CotacaoOpcao"
CREATE TABLE "CotacaoOpcao"
(
  "codigoAtivo" character varying(10) NOT NULL,
  periodo timestamp without time zone NOT NULL,
  tipoperiodo character varying(4),
  abertura character varying(10),
  maxima character varying(10),
  minima character varying(10),
  fechamento character varying(10),
  ano character varying(4),
  volume character varying(20)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "CotacaoOpcao" OWNER TO postgres;

-- Table: "OpcoesLiquidas"
CREATE TABLE "OpcoesLiquidas"
(
  "codigoAtivo" character varying(10) NOT NULL,
  ano character varying(4)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "OpcoesLiquidas" OWNER TO postgres;


