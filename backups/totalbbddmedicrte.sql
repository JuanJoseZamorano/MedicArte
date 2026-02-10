--
-- PostgreSQL database dump
--

\restrict 3fg7Xbc6FqSfQLRMKkvjBVADyxy7oeFJPUFzZ69GbebphJPmMnXLWs5n85b3L2v

-- Dumped from database version 18.0
-- Dumped by pg_dump version 18.0

-- Started on 2026-02-07 11:36:00

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 19229)
-- Name: medicarte; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA medicarte;


ALTER SCHEMA medicarte OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 19230)
-- Name: cita; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.cita (
    id_cita integer NOT NULL,
    id_paciente integer NOT NULL,
    id_medico integer NOT NULL,
    fecha_hora timestamp without time zone NOT NULL,
    estado character varying(20) DEFAULT 'PENDIENTE'::character varying NOT NULL,
    origen character varying(10) DEFAULT 'CLINICA'::character varying NOT NULL,
    observaciones text,
    duracion_min smallint,
    CONSTRAINT chk_cita_estado CHECK (((estado)::text = ANY (ARRAY[('PENDIENTE'::character varying)::text, ('CONFIRMADA'::character varying)::text, ('COMPLETADA'::character varying)::text, ('CANCELADA'::character varying)::text]))),
    CONSTRAINT chk_cita_origen CHECK (((origen)::text = ANY (ARRAY[('CLINICA'::character varying)::text, ('APP'::character varying)::text])))
);


ALTER TABLE medicarte.cita OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 19245)
-- Name: cita_id_cita_seq; Type: SEQUENCE; Schema: medicarte; Owner: postgres
--

CREATE SEQUENCE medicarte.cita_id_cita_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE medicarte.cita_id_cita_seq OWNER TO postgres;

--
-- TOC entry 5024 (class 0 OID 0)
-- Dependencies: 221
-- Name: cita_id_cita_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.cita_id_cita_seq OWNED BY medicarte.cita.id_cita;


--
-- TOC entry 222 (class 1259 OID 19246)
-- Name: configuracion; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.configuracion (
    clave character varying(60) NOT NULL,
    valor character varying(255)
);


ALTER TABLE medicarte.configuracion OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 19250)
-- Name: consulta; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.consulta (
    id_consulta integer NOT NULL,
    id_episodio integer NOT NULL,
    id_medico integer NOT NULL,
    id_cita integer,
    fecha_hora timestamp without time zone DEFAULT now() NOT NULL,
    motivo_consulta character varying(255),
    anamnesis text,
    exploracion text,
    diagnostico character varying(255) NOT NULL,
    diagnostico_cod character varying(20),
    tratamiento text,
    observaciones text,
    estado character varying(20) DEFAULT 'FINALIZADA'::character varying NOT NULL,
    CONSTRAINT chk_consulta_estado CHECK (((estado)::text = ANY (ARRAY[('BORRADOR'::character varying)::text, ('FINALIZADA'::character varying)::text])))
);


ALTER TABLE medicarte.consulta OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 19264)
-- Name: consulta_id_consulta_seq; Type: SEQUENCE; Schema: medicarte; Owner: postgres
--

CREATE SEQUENCE medicarte.consulta_id_consulta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE medicarte.consulta_id_consulta_seq OWNER TO postgres;

--
-- TOC entry 5028 (class 0 OID 0)
-- Dependencies: 224
-- Name: consulta_id_consulta_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.consulta_id_consulta_seq OWNED BY medicarte.consulta.id_consulta;


--
-- TOC entry 225 (class 1259 OID 19265)
-- Name: episodio; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.episodio (
    id_episodio integer NOT NULL,
    id_historia integer NOT NULL,
    id_especialidad integer,
    motivo character varying(200) NOT NULL,
    fecha_inicio date DEFAULT CURRENT_DATE NOT NULL,
    fecha_fin date,
    estado character varying(20) DEFAULT 'ABIERTO'::character varying NOT NULL,
    CONSTRAINT chk_episodio_estado CHECK (((estado)::text = ANY (ARRAY[('ABIERTO'::character varying)::text, ('CERRADO'::character varying)::text])))
);


ALTER TABLE medicarte.episodio OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 19276)
-- Name: episodio_id_episodio_seq; Type: SEQUENCE; Schema: medicarte; Owner: postgres
--

CREATE SEQUENCE medicarte.episodio_id_episodio_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE medicarte.episodio_id_episodio_seq OWNER TO postgres;

--
-- TOC entry 5031 (class 0 OID 0)
-- Dependencies: 226
-- Name: episodio_id_episodio_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.episodio_id_episodio_seq OWNED BY medicarte.episodio.id_episodio;


--
-- TOC entry 227 (class 1259 OID 19277)
-- Name: especialidad; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.especialidad (
    id_especialidad integer NOT NULL,
    nombre character varying(100) NOT NULL
);


ALTER TABLE medicarte.especialidad OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 19282)
-- Name: especialidad_id_especialidad_seq; Type: SEQUENCE; Schema: medicarte; Owner: postgres
--

CREATE SEQUENCE medicarte.especialidad_id_especialidad_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE medicarte.especialidad_id_especialidad_seq OWNER TO postgres;

--
-- TOC entry 5034 (class 0 OID 0)
-- Dependencies: 228
-- Name: especialidad_id_especialidad_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.especialidad_id_especialidad_seq OWNED BY medicarte.especialidad.id_especialidad;


--
-- TOC entry 229 (class 1259 OID 19283)
-- Name: historia_clinica; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.historia_clinica (
    id_historia integer NOT NULL,
    id_paciente integer NOT NULL,
    fecha_apertura date DEFAULT CURRENT_DATE NOT NULL,
    estado character varying(20) DEFAULT 'ACTIVA'::character varying NOT NULL,
    notas text,
    CONSTRAINT chk_historia_estado CHECK (((estado)::text = ANY (ARRAY[('ACTIVA'::character varying)::text, ('CERRADA'::character varying)::text])))
);


ALTER TABLE medicarte.historia_clinica OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 19295)
-- Name: historia_clinica_id_historia_seq; Type: SEQUENCE; Schema: medicarte; Owner: postgres
--

CREATE SEQUENCE medicarte.historia_clinica_id_historia_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE medicarte.historia_clinica_id_historia_seq OWNER TO postgres;

--
-- TOC entry 5037 (class 0 OID 0)
-- Dependencies: 230
-- Name: historia_clinica_id_historia_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.historia_clinica_id_historia_seq OWNED BY medicarte.historia_clinica.id_historia;


--
-- TOC entry 231 (class 1259 OID 19296)
-- Name: log_accion; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.log_accion (
    id_log integer NOT NULL,
    id_usuario integer,
    accion character varying(80) NOT NULL,
    detalle text,
    fecha_hora timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE medicarte.log_accion OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 19305)
-- Name: log_accion_id_log_seq; Type: SEQUENCE; Schema: medicarte; Owner: postgres
--

CREATE SEQUENCE medicarte.log_accion_id_log_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE medicarte.log_accion_id_log_seq OWNER TO postgres;

--
-- TOC entry 5040 (class 0 OID 0)
-- Dependencies: 232
-- Name: log_accion_id_log_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.log_accion_id_log_seq OWNED BY medicarte.log_accion.id_log;


--
-- TOC entry 233 (class 1259 OID 19306)
-- Name: medico; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.medico (
    id_medico integer NOT NULL,
    nombre_apellidos character varying(120) NOT NULL,
    num_colegiado character varying(40),
    id_especialidad integer,
    activo boolean DEFAULT true NOT NULL
);


ALTER TABLE medicarte.medico OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 19313)
-- Name: medico_id_medico_seq; Type: SEQUENCE; Schema: medicarte; Owner: postgres
--

CREATE SEQUENCE medicarte.medico_id_medico_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE medicarte.medico_id_medico_seq OWNER TO postgres;

--
-- TOC entry 5043 (class 0 OID 0)
-- Dependencies: 234
-- Name: medico_id_medico_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.medico_id_medico_seq OWNED BY medicarte.medico.id_medico;


--
-- TOC entry 235 (class 1259 OID 19314)
-- Name: paciente; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.paciente (
    id_paciente integer NOT NULL,
    dni character varying(15),
    nombre character varying(80) NOT NULL,
    apellidos character varying(120) NOT NULL,
    fecha_nacimiento date,
    sexo character varying(10),
    telefono character varying(30),
    email character varying(120),
    direccion character varying(200),
    provincia character varying(80),
    cp character varying(10),
    aseguradora character varying(120),
    num_poliza character varying(50),
    nuhsa character varying(20),
    nuss character varying(20),
    nhc character varying(20),
    grupo_sanguineo character varying(3),
    alergias text,
    antecedentes_personales text,
    antecedentes_familiares text,
    tratamiento_actual text,
    creado_en timestamp without time zone DEFAULT now() NOT NULL,
    foto_path character varying(255)
);


ALTER TABLE medicarte.paciente OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 19324)
-- Name: paciente_id_paciente_seq; Type: SEQUENCE; Schema: medicarte; Owner: postgres
--

CREATE SEQUENCE medicarte.paciente_id_paciente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE medicarte.paciente_id_paciente_seq OWNER TO postgres;

--
-- TOC entry 5046 (class 0 OID 0)
-- Dependencies: 236
-- Name: paciente_id_paciente_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.paciente_id_paciente_seq OWNED BY medicarte.paciente.id_paciente;


--
-- TOC entry 237 (class 1259 OID 19325)
-- Name: usuario; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.usuario (
    id_usuario integer NOT NULL,
    username character varying(60) NOT NULL,
    password_hash character varying(255) NOT NULL,
    rol character varying(20) NOT NULL,
    id_medico integer,
    activo boolean DEFAULT true NOT NULL
);


ALTER TABLE medicarte.usuario OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 19334)
-- Name: usuario_id_usuario_seq; Type: SEQUENCE; Schema: medicarte; Owner: postgres
--

CREATE SEQUENCE medicarte.usuario_id_usuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE medicarte.usuario_id_usuario_seq OWNER TO postgres;

--
-- TOC entry 5049 (class 0 OID 0)
-- Dependencies: 238
-- Name: usuario_id_usuario_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.usuario_id_usuario_seq OWNED BY medicarte.usuario.id_usuario;


--
-- TOC entry 4800 (class 2604 OID 19335)
-- Name: cita id_cita; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.cita ALTER COLUMN id_cita SET DEFAULT nextval('medicarte.cita_id_cita_seq'::regclass);


--
-- TOC entry 4803 (class 2604 OID 19336)
-- Name: consulta id_consulta; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.consulta ALTER COLUMN id_consulta SET DEFAULT nextval('medicarte.consulta_id_consulta_seq'::regclass);


--
-- TOC entry 4806 (class 2604 OID 19337)
-- Name: episodio id_episodio; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.episodio ALTER COLUMN id_episodio SET DEFAULT nextval('medicarte.episodio_id_episodio_seq'::regclass);


--
-- TOC entry 4809 (class 2604 OID 19338)
-- Name: especialidad id_especialidad; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.especialidad ALTER COLUMN id_especialidad SET DEFAULT nextval('medicarte.especialidad_id_especialidad_seq'::regclass);


--
-- TOC entry 4810 (class 2604 OID 19339)
-- Name: historia_clinica id_historia; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.historia_clinica ALTER COLUMN id_historia SET DEFAULT nextval('medicarte.historia_clinica_id_historia_seq'::regclass);


--
-- TOC entry 4813 (class 2604 OID 19340)
-- Name: log_accion id_log; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.log_accion ALTER COLUMN id_log SET DEFAULT nextval('medicarte.log_accion_id_log_seq'::regclass);


--
-- TOC entry 4815 (class 2604 OID 19341)
-- Name: medico id_medico; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.medico ALTER COLUMN id_medico SET DEFAULT nextval('medicarte.medico_id_medico_seq'::regclass);


--
-- TOC entry 4817 (class 2604 OID 19342)
-- Name: paciente id_paciente; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.paciente ALTER COLUMN id_paciente SET DEFAULT nextval('medicarte.paciente_id_paciente_seq'::regclass);


--
-- TOC entry 4819 (class 2604 OID 19343)
-- Name: usuario id_usuario; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.usuario ALTER COLUMN id_usuario SET DEFAULT nextval('medicarte.usuario_id_usuario_seq'::regclass);


--
-- TOC entry 4827 (class 2606 OID 19345)
-- Name: cita cita_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.cita
    ADD CONSTRAINT cita_pkey PRIMARY KEY (id_cita);


--
-- TOC entry 4832 (class 2606 OID 19347)
-- Name: configuracion configuracion_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.configuracion
    ADD CONSTRAINT configuracion_pkey PRIMARY KEY (clave);


--
-- TOC entry 4834 (class 2606 OID 19349)
-- Name: consulta consulta_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.consulta
    ADD CONSTRAINT consulta_pkey PRIMARY KEY (id_consulta);


--
-- TOC entry 4838 (class 2606 OID 19351)
-- Name: episodio episodio_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.episodio
    ADD CONSTRAINT episodio_pkey PRIMARY KEY (id_episodio);


--
-- TOC entry 4842 (class 2606 OID 19353)
-- Name: especialidad especialidad_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.especialidad
    ADD CONSTRAINT especialidad_pkey PRIMARY KEY (id_especialidad);


--
-- TOC entry 4844 (class 2606 OID 19355)
-- Name: historia_clinica historia_clinica_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.historia_clinica
    ADD CONSTRAINT historia_clinica_pkey PRIMARY KEY (id_historia);


--
-- TOC entry 4849 (class 2606 OID 19357)
-- Name: log_accion log_accion_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.log_accion
    ADD CONSTRAINT log_accion_pkey PRIMARY KEY (id_log);


--
-- TOC entry 4851 (class 2606 OID 19359)
-- Name: medico medico_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.medico
    ADD CONSTRAINT medico_pkey PRIMARY KEY (id_medico);


--
-- TOC entry 4853 (class 2606 OID 19361)
-- Name: paciente paciente_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.paciente
    ADD CONSTRAINT paciente_pkey PRIMARY KEY (id_paciente);


--
-- TOC entry 4847 (class 2606 OID 19363)
-- Name: historia_clinica uq_historia_paciente; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.historia_clinica
    ADD CONSTRAINT uq_historia_paciente UNIQUE (id_paciente);


--
-- TOC entry 4856 (class 2606 OID 19365)
-- Name: usuario uq_usuario_username; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.usuario
    ADD CONSTRAINT uq_usuario_username UNIQUE (username);


--
-- TOC entry 4858 (class 2606 OID 19367)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);


--
-- TOC entry 4828 (class 1259 OID 19368)
-- Name: idx_cita_fecha; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_cita_fecha ON medicarte.cita USING btree (fecha_hora);


--
-- TOC entry 4829 (class 1259 OID 19369)
-- Name: idx_cita_medico; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_cita_medico ON medicarte.cita USING btree (id_medico);


--
-- TOC entry 4830 (class 1259 OID 19370)
-- Name: idx_cita_paciente; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_cita_paciente ON medicarte.cita USING btree (id_paciente);


--
-- TOC entry 4835 (class 1259 OID 19371)
-- Name: idx_consulta_episodio; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_consulta_episodio ON medicarte.consulta USING btree (id_episodio);


--
-- TOC entry 4836 (class 1259 OID 19372)
-- Name: idx_consulta_medico; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_consulta_medico ON medicarte.consulta USING btree (id_medico);


--
-- TOC entry 4839 (class 1259 OID 19373)
-- Name: idx_epi_especialidad; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_epi_especialidad ON medicarte.episodio USING btree (id_especialidad);


--
-- TOC entry 4840 (class 1259 OID 19374)
-- Name: idx_epi_historia; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_epi_historia ON medicarte.episodio USING btree (id_historia);


--
-- TOC entry 4845 (class 1259 OID 19375)
-- Name: idx_historia_paciente; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_historia_paciente ON medicarte.historia_clinica USING btree (id_paciente);


--
-- TOC entry 4854 (class 1259 OID 19376)
-- Name: idx_usuario_medico; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_usuario_medico ON medicarte.usuario USING btree (id_medico);


--
-- TOC entry 4859 (class 2606 OID 19377)
-- Name: cita cita_id_medico_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.cita
    ADD CONSTRAINT cita_id_medico_fkey FOREIGN KEY (id_medico) REFERENCES medicarte.medico(id_medico) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4860 (class 2606 OID 19382)
-- Name: cita cita_id_paciente_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.cita
    ADD CONSTRAINT cita_id_paciente_fkey FOREIGN KEY (id_paciente) REFERENCES medicarte.paciente(id_paciente) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4861 (class 2606 OID 19387)
-- Name: consulta consulta_id_cita_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.consulta
    ADD CONSTRAINT consulta_id_cita_fkey FOREIGN KEY (id_cita) REFERENCES medicarte.cita(id_cita) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4862 (class 2606 OID 19392)
-- Name: consulta consulta_id_episodio_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.consulta
    ADD CONSTRAINT consulta_id_episodio_fkey FOREIGN KEY (id_episodio) REFERENCES medicarte.episodio(id_episodio) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4863 (class 2606 OID 19397)
-- Name: consulta consulta_id_medico_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.consulta
    ADD CONSTRAINT consulta_id_medico_fkey FOREIGN KEY (id_medico) REFERENCES medicarte.medico(id_medico) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4864 (class 2606 OID 19402)
-- Name: episodio episodio_id_especialidad_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.episodio
    ADD CONSTRAINT episodio_id_especialidad_fkey FOREIGN KEY (id_especialidad) REFERENCES medicarte.especialidad(id_especialidad) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4865 (class 2606 OID 19407)
-- Name: episodio episodio_id_historia_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.episodio
    ADD CONSTRAINT episodio_id_historia_fkey FOREIGN KEY (id_historia) REFERENCES medicarte.historia_clinica(id_historia) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4866 (class 2606 OID 19412)
-- Name: historia_clinica historia_clinica_id_paciente_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.historia_clinica
    ADD CONSTRAINT historia_clinica_id_paciente_fkey FOREIGN KEY (id_paciente) REFERENCES medicarte.paciente(id_paciente) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4867 (class 2606 OID 19417)
-- Name: log_accion log_accion_id_usuario_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.log_accion
    ADD CONSTRAINT log_accion_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES medicarte.usuario(id_usuario) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4868 (class 2606 OID 19422)
-- Name: medico medico_id_especialidad_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.medico
    ADD CONSTRAINT medico_id_especialidad_fkey FOREIGN KEY (id_especialidad) REFERENCES medicarte.especialidad(id_especialidad) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4869 (class 2606 OID 19427)
-- Name: usuario usuario_id_medico_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.usuario
    ADD CONSTRAINT usuario_id_medico_fkey FOREIGN KEY (id_medico) REFERENCES medicarte.medico(id_medico) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 5022 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA medicarte; Type: ACL; Schema: -; Owner: postgres
--

GRANT USAGE ON SCHEMA medicarte TO app_admin;
GRANT USAGE ON SCHEMA medicarte TO app_empleado;
GRANT USAGE ON SCHEMA medicarte TO app_cliente;


--
-- TOC entry 5023 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE cita; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.cita TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.cita TO app_empleado;
GRANT SELECT,INSERT ON TABLE medicarte.cita TO app_cliente;


--
-- TOC entry 5025 (class 0 OID 0)
-- Dependencies: 221
-- Name: SEQUENCE cita_id_cita_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.cita_id_cita_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.cita_id_cita_seq TO app_empleado;


--
-- TOC entry 5026 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE configuracion; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.configuracion TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.configuracion TO app_empleado;


--
-- TOC entry 5027 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE consulta; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.consulta TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.consulta TO app_empleado;


--
-- TOC entry 5029 (class 0 OID 0)
-- Dependencies: 224
-- Name: SEQUENCE consulta_id_consulta_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.consulta_id_consulta_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.consulta_id_consulta_seq TO app_empleado;


--
-- TOC entry 5030 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE episodio; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.episodio TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.episodio TO app_empleado;


--
-- TOC entry 5032 (class 0 OID 0)
-- Dependencies: 226
-- Name: SEQUENCE episodio_id_episodio_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.episodio_id_episodio_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.episodio_id_episodio_seq TO app_empleado;


--
-- TOC entry 5033 (class 0 OID 0)
-- Dependencies: 227
-- Name: TABLE especialidad; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.especialidad TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.especialidad TO app_empleado;
GRANT SELECT ON TABLE medicarte.especialidad TO app_cliente;


--
-- TOC entry 5035 (class 0 OID 0)
-- Dependencies: 228
-- Name: SEQUENCE especialidad_id_especialidad_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.especialidad_id_especialidad_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.especialidad_id_especialidad_seq TO app_empleado;


--
-- TOC entry 5036 (class 0 OID 0)
-- Dependencies: 229
-- Name: TABLE historia_clinica; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.historia_clinica TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.historia_clinica TO app_empleado;


--
-- TOC entry 5038 (class 0 OID 0)
-- Dependencies: 230
-- Name: SEQUENCE historia_clinica_id_historia_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.historia_clinica_id_historia_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.historia_clinica_id_historia_seq TO app_empleado;


--
-- TOC entry 5039 (class 0 OID 0)
-- Dependencies: 231
-- Name: TABLE log_accion; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.log_accion TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.log_accion TO app_empleado;


--
-- TOC entry 5041 (class 0 OID 0)
-- Dependencies: 232
-- Name: SEQUENCE log_accion_id_log_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.log_accion_id_log_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.log_accion_id_log_seq TO app_empleado;


--
-- TOC entry 5042 (class 0 OID 0)
-- Dependencies: 233
-- Name: TABLE medico; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.medico TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.medico TO app_empleado;


--
-- TOC entry 5044 (class 0 OID 0)
-- Dependencies: 234
-- Name: SEQUENCE medico_id_medico_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.medico_id_medico_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.medico_id_medico_seq TO app_empleado;


--
-- TOC entry 5045 (class 0 OID 0)
-- Dependencies: 235
-- Name: TABLE paciente; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.paciente TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.paciente TO app_empleado;


--
-- TOC entry 5047 (class 0 OID 0)
-- Dependencies: 236
-- Name: SEQUENCE paciente_id_paciente_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.paciente_id_paciente_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.paciente_id_paciente_seq TO app_empleado;


--
-- TOC entry 5048 (class 0 OID 0)
-- Dependencies: 237
-- Name: TABLE usuario; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.usuario TO app_admin;


--
-- TOC entry 5050 (class 0 OID 0)
-- Dependencies: 238
-- Name: SEQUENCE usuario_id_usuario_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.usuario_id_usuario_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.usuario_id_usuario_seq TO app_empleado;


-- Completed on 2026-02-07 11:36:01

--
-- PostgreSQL database dump complete
--

\unrestrict 3fg7Xbc6FqSfQLRMKkvjBVADyxy7oeFJPUFzZ69GbebphJPmMnXLWs5n85b3L2v

