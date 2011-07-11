-- Table: "Ativo"

-- DROP TABLE "Ativo";

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

-- DROP TABLE "CotacaoAtivo";

CREATE TABLE "CotacaoAtivo"
(
  "codigoAtivo" character varying(10) NOT NULL,
  "periodo" timestamp without time zone NOT NULL,
  "tipoperiodo" character varying(4),
  "abertura" character varying(10),
  "maxima" character varying(10),
  "minima" character varying(10),
  "fechamento" character varying(10),
  "ano" character varying(4),
  CONSTRAINT "CotacaoAtivo_pkey" PRIMARY KEY ("codigoAtivo", "periodo"),
  CONSTRAINT "CotacaoAtivo_codigoativo_fkey" FOREIGN KEY ("codigoAtivo")
      REFERENCES "Ativo" ("codigoAtivo") MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "CotacaoAtivo" OWNER TO postgres;


