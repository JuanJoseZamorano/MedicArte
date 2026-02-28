--
-- PostgreSQL database dump
--

\restrict sxfZzlizrZxvCNSoyY6JKhZZDjNorl5XdvpxmLXcL26ztBKjR2sby5Db0iDMg8g

-- Dumped from database version 18.0
-- Dumped by pg_dump version 18.0

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
-- Name: medicarte; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA medicarte;


ALTER SCHEMA medicarte OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
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
-- Name: cita_id_cita_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.cita_id_cita_seq OWNED BY medicarte.cita.id_cita;


--
-- Name: configuracion; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.configuracion (
    clave character varying(60) NOT NULL,
    valor character varying(255)
);


ALTER TABLE medicarte.configuracion OWNER TO postgres;

--
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
-- Name: consulta_id_consulta_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.consulta_id_consulta_seq OWNED BY medicarte.consulta.id_consulta;


--
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
-- Name: episodio_id_episodio_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.episodio_id_episodio_seq OWNED BY medicarte.episodio.id_episodio;


--
-- Name: especialidad; Type: TABLE; Schema: medicarte; Owner: postgres
--

CREATE TABLE medicarte.especialidad (
    id_especialidad integer NOT NULL,
    nombre character varying(100) NOT NULL
);


ALTER TABLE medicarte.especialidad OWNER TO postgres;

--
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
-- Name: especialidad_id_especialidad_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.especialidad_id_especialidad_seq OWNED BY medicarte.especialidad.id_especialidad;


--
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
-- Name: historia_clinica_id_historia_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.historia_clinica_id_historia_seq OWNED BY medicarte.historia_clinica.id_historia;


--
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
-- Name: log_accion_id_log_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.log_accion_id_log_seq OWNED BY medicarte.log_accion.id_log;


--
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
-- Name: medico_id_medico_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.medico_id_medico_seq OWNED BY medicarte.medico.id_medico;


--
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
-- Name: paciente_id_paciente_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.paciente_id_paciente_seq OWNED BY medicarte.paciente.id_paciente;


--
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
-- Name: usuario_id_usuario_seq; Type: SEQUENCE OWNED BY; Schema: medicarte; Owner: postgres
--

ALTER SEQUENCE medicarte.usuario_id_usuario_seq OWNED BY medicarte.usuario.id_usuario;


--
-- Name: cita id_cita; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.cita ALTER COLUMN id_cita SET DEFAULT nextval('medicarte.cita_id_cita_seq'::regclass);


--
-- Name: consulta id_consulta; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.consulta ALTER COLUMN id_consulta SET DEFAULT nextval('medicarte.consulta_id_consulta_seq'::regclass);


--
-- Name: episodio id_episodio; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.episodio ALTER COLUMN id_episodio SET DEFAULT nextval('medicarte.episodio_id_episodio_seq'::regclass);


--
-- Name: especialidad id_especialidad; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.especialidad ALTER COLUMN id_especialidad SET DEFAULT nextval('medicarte.especialidad_id_especialidad_seq'::regclass);


--
-- Name: historia_clinica id_historia; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.historia_clinica ALTER COLUMN id_historia SET DEFAULT nextval('medicarte.historia_clinica_id_historia_seq'::regclass);


--
-- Name: log_accion id_log; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.log_accion ALTER COLUMN id_log SET DEFAULT nextval('medicarte.log_accion_id_log_seq'::regclass);


--
-- Name: medico id_medico; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.medico ALTER COLUMN id_medico SET DEFAULT nextval('medicarte.medico_id_medico_seq'::regclass);


--
-- Name: paciente id_paciente; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.paciente ALTER COLUMN id_paciente SET DEFAULT nextval('medicarte.paciente_id_paciente_seq'::regclass);


--
-- Name: usuario id_usuario; Type: DEFAULT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.usuario ALTER COLUMN id_usuario SET DEFAULT nextval('medicarte.usuario_id_usuario_seq'::regclass);


--
-- Data for Name: cita; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.cita (id_cita, id_paciente, id_medico, fecha_hora, estado, origen, observaciones, duracion_min) FROM stdin;
\.


--
-- Data for Name: configuracion; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.configuracion (clave, valor) FROM stdin;
\.


--
-- Data for Name: consulta; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.consulta (id_consulta, id_episodio, id_medico, id_cita, fecha_hora, motivo_consulta, anamnesis, exploracion, diagnostico, diagnostico_cod, tratamiento, observaciones, estado) FROM stdin;
\.


--
-- Data for Name: episodio; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.episodio (id_episodio, id_historia, id_especialidad, motivo, fecha_inicio, fecha_fin, estado) FROM stdin;
\.


--
-- Data for Name: especialidad; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.especialidad (id_especialidad, nombre) FROM stdin;
\.


--
-- Data for Name: historia_clinica; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.historia_clinica (id_historia, id_paciente, fecha_apertura, estado, notas) FROM stdin;
\.


--
-- Data for Name: log_accion; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.log_accion (id_log, id_usuario, accion, detalle, fecha_hora) FROM stdin;
\.


--
-- Data for Name: medico; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.medico (id_medico, nombre_apellidos, num_colegiado, id_especialidad, activo) FROM stdin;
\.


--
-- Data for Name: paciente; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.paciente (id_paciente, dni, nombre, apellidos, fecha_nacimiento, sexo, telefono, email, direccion, provincia, cp, aseguradora, num_poliza, nuhsa, nuss, nhc, grupo_sanguineo, alergias, antecedentes_personales, antecedentes_familiares, tratamiento_actual, creado_en, foto_path) FROM stdin;
\.


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.usuario (id_usuario, username, password_hash, rol, id_medico, activo) FROM stdin;
1	admin	$2a$10$eMKYFgf49r5qO8M.i2s6EuHZicX2/p.n5S.1dERi7cwPAwaXdMT.O	ADMIN	\N	t
\.


--
-- Name: cita_id_cita_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.cita_id_cita_seq', 1, false);


--
-- Name: consulta_id_consulta_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.consulta_id_consulta_seq', 1, false);


--
-- Name: episodio_id_episodio_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.episodio_id_episodio_seq', 1, false);


--
-- Name: especialidad_id_especialidad_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.especialidad_id_especialidad_seq', 1, false);


--
-- Name: historia_clinica_id_historia_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.historia_clinica_id_historia_seq', 1, false);


--
-- Name: log_accion_id_log_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.log_accion_id_log_seq', 1, false);


--
-- Name: medico_id_medico_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.medico_id_medico_seq', 1, false);


--
-- Name: paciente_id_paciente_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.paciente_id_paciente_seq', 1, false);


--
-- Name: usuario_id_usuario_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.usuario_id_usuario_seq', 1, false);


--
-- Name: cita cita_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.cita
    ADD CONSTRAINT cita_pkey PRIMARY KEY (id_cita);


--
-- Name: configuracion configuracion_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.configuracion
    ADD CONSTRAINT configuracion_pkey PRIMARY KEY (clave);


--
-- Name: consulta consulta_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.consulta
    ADD CONSTRAINT consulta_pkey PRIMARY KEY (id_consulta);


--
-- Name: episodio episodio_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.episodio
    ADD CONSTRAINT episodio_pkey PRIMARY KEY (id_episodio);


--
-- Name: especialidad especialidad_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.especialidad
    ADD CONSTRAINT especialidad_pkey PRIMARY KEY (id_especialidad);


--
-- Name: historia_clinica historia_clinica_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.historia_clinica
    ADD CONSTRAINT historia_clinica_pkey PRIMARY KEY (id_historia);


--
-- Name: log_accion log_accion_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.log_accion
    ADD CONSTRAINT log_accion_pkey PRIMARY KEY (id_log);


--
-- Name: medico medico_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.medico
    ADD CONSTRAINT medico_pkey PRIMARY KEY (id_medico);


--
-- Name: paciente paciente_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.paciente
    ADD CONSTRAINT paciente_pkey PRIMARY KEY (id_paciente);


--
-- Name: historia_clinica uq_historia_paciente; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.historia_clinica
    ADD CONSTRAINT uq_historia_paciente UNIQUE (id_paciente);


--
-- Name: usuario uq_usuario_username; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.usuario
    ADD CONSTRAINT uq_usuario_username UNIQUE (username);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);


--
-- Name: idx_cita_fecha; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_cita_fecha ON medicarte.cita USING btree (fecha_hora);


--
-- Name: idx_cita_medico; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_cita_medico ON medicarte.cita USING btree (id_medico);


--
-- Name: idx_cita_paciente; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_cita_paciente ON medicarte.cita USING btree (id_paciente);


--
-- Name: idx_consulta_episodio; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_consulta_episodio ON medicarte.consulta USING btree (id_episodio);


--
-- Name: idx_consulta_medico; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_consulta_medico ON medicarte.consulta USING btree (id_medico);


--
-- Name: idx_epi_especialidad; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_epi_especialidad ON medicarte.episodio USING btree (id_especialidad);


--
-- Name: idx_epi_historia; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_epi_historia ON medicarte.episodio USING btree (id_historia);


--
-- Name: idx_historia_paciente; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_historia_paciente ON medicarte.historia_clinica USING btree (id_paciente);


--
-- Name: idx_usuario_medico; Type: INDEX; Schema: medicarte; Owner: postgres
--

CREATE INDEX idx_usuario_medico ON medicarte.usuario USING btree (id_medico);


--
-- Name: cita cita_id_medico_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.cita
    ADD CONSTRAINT cita_id_medico_fkey FOREIGN KEY (id_medico) REFERENCES medicarte.medico(id_medico) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: cita cita_id_paciente_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.cita
    ADD CONSTRAINT cita_id_paciente_fkey FOREIGN KEY (id_paciente) REFERENCES medicarte.paciente(id_paciente) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: consulta consulta_id_cita_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.consulta
    ADD CONSTRAINT consulta_id_cita_fkey FOREIGN KEY (id_cita) REFERENCES medicarte.cita(id_cita) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: consulta consulta_id_episodio_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.consulta
    ADD CONSTRAINT consulta_id_episodio_fkey FOREIGN KEY (id_episodio) REFERENCES medicarte.episodio(id_episodio) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: consulta consulta_id_medico_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.consulta
    ADD CONSTRAINT consulta_id_medico_fkey FOREIGN KEY (id_medico) REFERENCES medicarte.medico(id_medico) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: episodio episodio_id_especialidad_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.episodio
    ADD CONSTRAINT episodio_id_especialidad_fkey FOREIGN KEY (id_especialidad) REFERENCES medicarte.especialidad(id_especialidad) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: episodio episodio_id_historia_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.episodio
    ADD CONSTRAINT episodio_id_historia_fkey FOREIGN KEY (id_historia) REFERENCES medicarte.historia_clinica(id_historia) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: historia_clinica historia_clinica_id_paciente_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.historia_clinica
    ADD CONSTRAINT historia_clinica_id_paciente_fkey FOREIGN KEY (id_paciente) REFERENCES medicarte.paciente(id_paciente) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: log_accion log_accion_id_usuario_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.log_accion
    ADD CONSTRAINT log_accion_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES medicarte.usuario(id_usuario) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: medico medico_id_especialidad_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.medico
    ADD CONSTRAINT medico_id_especialidad_fkey FOREIGN KEY (id_especialidad) REFERENCES medicarte.especialidad(id_especialidad) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: usuario usuario_id_medico_fkey; Type: FK CONSTRAINT; Schema: medicarte; Owner: postgres
--

ALTER TABLE ONLY medicarte.usuario
    ADD CONSTRAINT usuario_id_medico_fkey FOREIGN KEY (id_medico) REFERENCES medicarte.medico(id_medico) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: SCHEMA medicarte; Type: ACL; Schema: -; Owner: postgres
--

GRANT USAGE ON SCHEMA medicarte TO app_admin;
GRANT USAGE ON SCHEMA medicarte TO app_empleado;
GRANT USAGE ON SCHEMA medicarte TO app_cliente;


--
-- Name: TABLE cita; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.cita TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.cita TO app_empleado;
GRANT SELECT,INSERT ON TABLE medicarte.cita TO app_cliente;


--
-- Name: SEQUENCE cita_id_cita_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.cita_id_cita_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.cita_id_cita_seq TO app_empleado;


--
-- Name: TABLE configuracion; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.configuracion TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.configuracion TO app_empleado;


--
-- Name: TABLE consulta; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.consulta TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.consulta TO app_empleado;


--
-- Name: SEQUENCE consulta_id_consulta_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.consulta_id_consulta_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.consulta_id_consulta_seq TO app_empleado;


--
-- Name: TABLE episodio; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.episodio TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.episodio TO app_empleado;


--
-- Name: SEQUENCE episodio_id_episodio_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.episodio_id_episodio_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.episodio_id_episodio_seq TO app_empleado;


--
-- Name: TABLE especialidad; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.especialidad TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.especialidad TO app_empleado;
GRANT SELECT ON TABLE medicarte.especialidad TO app_cliente;


--
-- Name: SEQUENCE especialidad_id_especialidad_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.especialidad_id_especialidad_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.especialidad_id_especialidad_seq TO app_empleado;


--
-- Name: TABLE historia_clinica; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.historia_clinica TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.historia_clinica TO app_empleado;


--
-- Name: SEQUENCE historia_clinica_id_historia_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.historia_clinica_id_historia_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.historia_clinica_id_historia_seq TO app_empleado;


--
-- Name: TABLE log_accion; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.log_accion TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.log_accion TO app_empleado;


--
-- Name: SEQUENCE log_accion_id_log_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.log_accion_id_log_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.log_accion_id_log_seq TO app_empleado;


--
-- Name: TABLE medico; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.medico TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.medico TO app_empleado;


--
-- Name: SEQUENCE medico_id_medico_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.medico_id_medico_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.medico_id_medico_seq TO app_empleado;


--
-- Name: TABLE paciente; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.paciente TO app_admin;
GRANT SELECT,INSERT,UPDATE ON TABLE medicarte.paciente TO app_empleado;


--
-- Name: SEQUENCE paciente_id_paciente_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.paciente_id_paciente_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.paciente_id_paciente_seq TO app_empleado;


--
-- Name: TABLE usuario; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON TABLE medicarte.usuario TO app_admin;


--
-- Name: SEQUENCE usuario_id_usuario_seq; Type: ACL; Schema: medicarte; Owner: postgres
--

GRANT ALL ON SEQUENCE medicarte.usuario_id_usuario_seq TO app_admin;
GRANT SELECT,USAGE ON SEQUENCE medicarte.usuario_id_usuario_seq TO app_empleado;


--
-- PostgreSQL database dump complete
--

\unrestrict sxfZzlizrZxvCNSoyY6JKhZZDjNorl5XdvpxmLXcL26ztBKjR2sby5Db0iDMg8g

