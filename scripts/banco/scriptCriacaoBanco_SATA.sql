-- Table: "CotacaoAtivo"

-- DROP TABLE "CotacaoAtivo";

CREATE TABLE "CotacaoAtivo"
(
  codigoativo character varying(10) NOT NULL,
  periodo timestamp without time zone NOT NULL,
  tipoperiodo character varying(4),
  abertura character varying(15),
  maxima character varying(15),
  minima character varying(15),
  fechamento character varying(15),
  ano character varying(4),
  CONSTRAINT "CotacaoAtivo_pkey" PRIMARY KEY (codigoativo, periodo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "CotacaoAtivo" OWNER TO postgres;

