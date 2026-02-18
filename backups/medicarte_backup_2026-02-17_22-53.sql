--
-- PostgreSQL database dump
--

\restrict F3zOTBIMBFzXEjKmDehjlc73WHuedYIgTnEAngVAExvWogg0GdaHyKsCpjzOjAt

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
1	9	1	2026-01-19 11:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
2	8	1	2026-02-01 12:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
3	6	1	2026-01-24 13:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
4	5	1	2026-01-09 11:00:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
5	5	1	2026-01-19 11:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
6	5	1	2026-02-03 12:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
7	4	1	2026-01-14 13:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
8	2	1	2025-12-30 11:00:00	COMPLETADA	CLINICA	El paciente llama por síntomas de resfriado con dolor de garganta y congestión.	20
9	2	1	2026-01-29 11:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
10	1	1	2025-11-10 12:30:00	COMPLETADA	CLINICA	El paciente comenta que lleva días encontrándose cansado y con malestar general.	20
11	10	1	2026-01-24 13:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
12	2	2	2025-10-11 11:00:00	COMPLETADA	CLINICA	El paciente solicita revisión por empeoramiento de su asma en las últimas semanas.	20
13	4	4	2025-09-11 11:30:00	COMPLETADA	CLINICA	El paciente solicita cita por aumento del dolor de rodilla al caminar.	20
14	4	4	2025-11-30 12:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
15	3	4	2025-12-15 13:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
16	1	4	2026-01-19 11:00:00	COMPLETADA	CLINICA	El paciente refiere dolor en la zona lumbar tras realizar un esfuerzo físico.	20
17	6	5	2025-10-21 11:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
18	6	5	2025-12-20 12:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
19	7	6	2025-08-12 13:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
20	7	6	2025-11-10 11:00:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
21	7	6	2026-01-09 11:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
22	3	6	2025-10-31 12:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
23	1	6	2025-12-10 13:30:00	COMPLETADA	CLINICA	El paciente solicita revisión de la tensión porque ha tenido cifras altas en casa.	20
24	8	7	2025-12-10 11:00:00	COMPLETADA	CLINICA	El paciente refiere episodios de migraña más frecuentes de lo habitual.	20
25	8	7	2026-01-14 11:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
26	3	7	2026-01-24 12:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
27	9	10	2025-09-21 13:30:00	COMPLETADA	CLINICA	El paciente pide revisión para control de peso y hábitos de vida.	20
28	9	10	2025-12-10 11:00:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
29	10	10	2025-07-23 11:30:00	COMPLETADA	CLINICA	El paciente comenta mayor cansancio y solicita revisión del tratamiento.	20
30	10	10	2025-11-20 12:30:00	COMPLETADA	CLINICA	El paciente solicita cita para valoración médica.	20
31	9	1	2026-02-02 13:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
32	8	1	2026-02-15 11:00:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
34	5	1	2026-01-23 12:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
36	5	1	2026-02-17 11:00:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
37	4	1	2026-01-28 11:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
38	2	1	2026-01-13 12:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
39	2	1	2026-02-12 13:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
40	1	1	2025-11-24 11:00:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
41	10	1	2026-02-07 11:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
42	2	2	2025-10-25 12:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
43	4	4	2025-09-25 13:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
44	4	4	2025-12-14 11:00:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
45	3	4	2025-12-29 11:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
46	1	4	2026-02-02 12:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
47	6	5	2025-11-04 13:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
48	6	5	2026-01-03 11:00:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
49	7	6	2025-08-26 11:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
50	7	6	2025-11-24 12:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
51	7	6	2026-01-23 13:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
52	3	6	2025-11-14 11:00:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
53	1	6	2025-12-24 11:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
54	8	7	2025-12-24 12:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
55	8	7	2026-01-28 13:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
56	3	7	2026-02-07 11:00:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
59	10	10	2025-08-06 13:30:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
60	10	10	2025-12-04 11:00:00	PENDIENTE	APP	Cita de seguimiento solicitada tras la última consulta	20
61	6	1	2026-01-17 11:30:00	CANCELADA	APP	Cita cancelada por el paciente por incompatibilidad de horario	20
62	5	1	2026-01-27 12:30:00	CANCELADA	APP	Cita cancelada por el paciente por incompatibilidad de horario	20
57	9	10	2025-10-05 11:30:00	CANCELADA	APP	Cita de seguimiento solicitada tras la última consulta	20
58	9	10	2025-12-24 12:30:00	CANCELADA	APP	Cita de seguimiento solicitada tras la última consulta	20
63	2	1	2026-01-22 13:30:00	CANCELADA	APP	Cita cancelada por el paciente por incompatibilidad de horario	20
64	2	2	2025-10-04 11:00:00	CANCELADA	APP	Cita cancelada por el paciente por incompatibilidad de horario	20
65	3	4	2025-12-08 11:30:00	CANCELADA	APP	Cita cancelada por el paciente por incompatibilidad de horario	20
66	6	5	2025-12-13 12:30:00	CANCELADA	APP	Cita cancelada por el paciente por incompatibilidad de horario	20
67	7	6	2026-01-02 13:30:00	CANCELADA	APP	Cita cancelada por el paciente por incompatibilidad de horario	20
68	8	7	2025-12-03 11:00:00	CANCELADA	APP	Cita cancelada por el paciente por incompatibilidad de horario	20
69	9	10	2025-09-14 11:30:00	CANCELADA	APP	Cita cancelada por el paciente por incompatibilidad de horario	20
70	10	10	2025-11-13 12:30:00	CANCELADA	APP	Cita cancelada por el paciente por incompatibilidad de horario	20
33	6	1	2026-02-07 11:30:00	COMPLETADA	APP	Cita de seguimiento solicitada tras la última consulta	20
35	5	1	2026-02-02 13:30:00	CANCELADA	APP	Cita de seguimiento solicitada tras la última consulta	20
\.


--
-- Data for Name: configuracion; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.configuracion (clave, valor) FROM stdin;
ULTIMA_FECHA_BKP	8 de febrero de 2026. 13:57
NOMBRE_CLINICA	Clinica Buenas Sauld
LOGO_CLINICA	C:\\MedicArte\\MedicArte\\src\\main\\resources\\img\\Sin título.png
\.


--
-- Data for Name: consulta; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.consulta (id_consulta, id_episodio, id_medico, id_cita, fecha_hora, motivo_consulta, anamnesis, exploracion, diagnostico, diagnostico_cod, tratamiento, observaciones, estado) FROM stdin;
1	1	1	1	2026-01-19 00:15:00	Revisión de hábitos saludables	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
2	2	1	2	2026-02-01 00:15:00	Consulta por insomnio ocasional	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
3	3	1	3	2026-01-24 00:15:00	Consulta por congestión nasal persistente	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
4	4	1	4	2026-01-09 00:15:00	Consulta por dolor abdominal agudo	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
5	5	1	5	2026-01-19 00:15:00	Seguimiento de gastroenteritis	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
6	6	1	6	2026-02-03 00:15:00	Revisión médica preventiva	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
7	7	1	7	2026-01-14 00:15:00	Revisión general anual	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
8	8	1	8	2025-12-30 00:15:00	Infección respiratoria de vías altas	Cuadro de inicio agudo con congestión nasal, odinofagia y malestar general de varios días de evolución.	Hiperemia faríngea y congestión nasal. Auscultación pulmonar normal.	Infección respiratoria alta de probable origen viral	\N	Tratamiento sintomático con analgésicos, hidratación y reposo.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
9	9	1	9	2026-01-29 00:15:00	Consulta por fatiga persistente	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
10	10	1	10	2025-11-10 00:15:00	Revisión general por malestar inespecífico	Paciente refiere cansancio generalizado y sensación de malestar sin síntomas específicos. Niega fiebre, pérdida de peso o dolor localizado.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Síndrome constitucional leve	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
11	11	1	11	2026-01-24 00:15:00	Revisión por cansancio generalizado	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
12	12	2	12	2025-10-11 00:15:00	Seguimiento de asma bronquial	Paciente asmático conocido. Refiere episodios ocasionales de disnea con el ejercicio.	Auscultación pulmonar con discretas sibilancias espiratorias.	Asma bronquial en seguimiento	\N	Ajuste de tratamiento inhalador y revisión en consulta.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
13	13	4	13	2025-09-11 00:15:00	Seguimiento de artrosis de rodilla	Dolor mecánico de rodilla con la deambulación prolongada. Rigidez matutina de corta duración.	Dolor a la movilización de rodilla. No signos inflamatorios agudos.	Artrosis de rodilla	\N	Analgesia pautada y derivación a fisioterapia.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
14	14	4	14	2025-11-30 00:15:00	Dolor mecánico de cadera	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
15	15	4	15	2025-12-15 00:15:00	Dolor lumbar crónico	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
16	16	4	16	2026-01-19 00:15:00	Dolor lumbar de esfuerzo	Dolor lumbar de aparición tras esfuerzo físico. No irradiación, sin parestesias ni alteraciones esfinterianas.	Contractura paravertebral lumbar. Movilidad limitada por dolor.	Lumbalgia mecánica	\N	Reposo relativo, analgesia con AINEs y recomendaciones posturales.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
17	17	5	17	2025-10-21 00:15:00	Control de rinitis alérgica	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
18	18	5	18	2025-12-20 00:15:00	Brotes cutáneos pruriginosos	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
19	19	6	19	2025-08-12 00:15:00	Seguimiento de insuficiencia venosa crónica	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
20	20	6	20	2025-11-10 00:15:00	Edemas en extremidades inferiores	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
21	21	6	21	2026-01-09 00:15:00	Revisión vascular periódica	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
22	22	6	22	2025-10-31 00:15:00	Control de hipercolesterolemia	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
23	23	6	23	2025-12-10 00:15:00	Control de hipertensión arterial	Paciente diagnosticado de hipertensión arterial, en seguimiento. Refiere buen cumplimiento terapéutico y controles irregulares en domicilio.	Tensión arterial en consulta 145/90 mmHg. Resto de la exploración sin hallazgos patológicos.	Hipertensión arterial esencial	\N	Reforzar medidas higiénico-dietéticas y mantener tratamiento antihipertensivo actual.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
24	24	7	24	2025-12-10 00:15:00	Valoración de migraña episódica	Crisis de cefalea pulsátil con fotofobia ocasional, autolimitadas.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Migraña episódica sin aura	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
25	25	7	25	2026-01-14 00:15:00	Seguimiento de cefaleas tensionales	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
26	26	7	26	2026-01-24 00:15:00	Revisión por mareos ocasionales	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
27	27	10	27	2025-09-21 00:15:00	Seguimiento de sobrepeso	Paciente acude para control de peso y hábitos de vida. Reconoce escasa actividad física.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Sobrepeso	\N	Recomendaciones dietéticas y aumento progresivo de actividad física.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
28	28	10	28	2025-12-10 00:15:00	Plan de control dietético	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
29	29	10	29	2025-07-23 00:15:00	Control de hipotiroidismo	Paciente en tratamiento con levotiroxina. Refiere aumento de la sensación de fatiga.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Hipotiroidismo primario en seguimiento	\N	Ajuste de dosis de levotiroxina según evolución clínica.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
30	30	10	30	2025-11-20 00:15:00	Ajuste de tratamiento hormonal	Consulta inicial relacionada con el motivo clínico del episodio.	Exploración física acorde al motivo de consulta, sin hallazgos de gravedad.	Proceso clínico en evaluación	\N	Tratamiento inicial pautado según protocolo clínico.	Primera consulta del episodio. Se informa al paciente del plan diagnóstico y terapéutico.	FINALIZADA
31	17	2	33	2026-02-07 00:15:00	Sigue todo correcto	No difiere dolor de ningun tipo	Todo parece correcto	Va estupendameinte	\N	Nada. Agua con sal para lavados	Se lo toma muy bien todo	FINALIZADA
\.


--
-- Data for Name: episodio; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.episodio (id_episodio, id_historia, id_especialidad, motivo, fecha_inicio, fecha_fin, estado) FROM stdin;
1	10	1	Revisión de hábitos saludables	2026-01-19	\N	ABIERTO
2	9	1	Consulta por insomnio ocasional	2026-02-01	\N	ABIERTO
3	7	1	Consulta por congestión nasal persistente	2026-01-24	\N	ABIERTO
4	6	1	Consulta por dolor abdominal agudo	2026-01-09	\N	ABIERTO
5	6	1	Seguimiento de gastroenteritis	2026-01-19	\N	ABIERTO
6	6	1	Revisión médica preventiva	2026-02-03	\N	ABIERTO
7	5	1	Revisión general anual	2026-01-14	\N	ABIERTO
8	3	1	Infección respiratoria de vías altas	2025-12-30	\N	ABIERTO
9	3	1	Consulta por fatiga persistente	2026-01-29	\N	ABIERTO
10	2	1	Revisión general por malestar inespecífico	2025-11-10	\N	ABIERTO
11	1	1	Revisión por cansancio generalizado	2026-01-24	\N	ABIERTO
12	3	2	Seguimiento de asma bronquial	2025-10-11	\N	ABIERTO
13	5	3	Seguimiento de artrosis de rodilla	2025-09-11	\N	ABIERTO
14	5	3	Dolor mecánico de cadera	2025-11-30	\N	ABIERTO
15	4	3	Dolor lumbar crónico	2025-12-15	\N	ABIERTO
16	2	3	Dolor lumbar de esfuerzo	2026-01-19	\N	ABIERTO
17	7	5	Control de rinitis alérgica	2025-10-21	\N	ABIERTO
18	7	5	Brotes cutáneos pruriginosos	2025-12-20	\N	ABIERTO
19	8	6	Seguimiento de insuficiencia venosa crónica	2025-08-12	\N	ABIERTO
20	8	6	Edemas en extremidades inferiores	2025-11-10	\N	ABIERTO
21	8	6	Revisión vascular periódica	2026-01-09	\N	ABIERTO
22	4	6	Control de hipercolesterolemia	2025-10-31	\N	ABIERTO
23	2	6	Control de hipertensión arterial	2025-12-10	\N	ABIERTO
24	9	7	Valoración de migraña episódica	2025-12-10	\N	ABIERTO
25	9	7	Seguimiento de cefaleas tensionales	2026-01-14	\N	ABIERTO
26	4	7	Revisión por mareos ocasionales	2026-01-24	\N	ABIERTO
27	10	10	Seguimiento de sobrepeso	2025-09-21	\N	ABIERTO
28	10	10	Plan de control dietético	2025-12-10	\N	ABIERTO
29	1	10	Control de hipotiroidismo	2025-07-23	\N	ABIERTO
30	1	10	Ajuste de tratamiento hormonal	2025-11-20	\N	ABIERTO
\.


--
-- Data for Name: especialidad; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.especialidad (id_especialidad, nombre) FROM stdin;
1	Medicina General
2	Pediatría
3	Traumatología
4	Ginecología
5	Dermatología
6	Cardiología
7	Neurología
8	Oftalmología
9	Psiquiatría
10	Endocrinología
\.


--
-- Data for Name: historia_clinica; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.historia_clinica (id_historia, id_paciente, fecha_apertura, estado, notas) FROM stdin;
1	10	2026-02-08	ACTIVA	Historia clínica abierta en el primer registro del paciente
2	1	2026-02-08	ACTIVA	Historia clínica abierta en el primer registro del paciente
3	2	2026-02-08	ACTIVA	Historia clínica abierta en el primer registro del paciente
4	3	2026-02-08	ACTIVA	Historia clínica abierta en el primer registro del paciente
5	4	2026-02-08	ACTIVA	Historia clínica abierta en el primer registro del paciente
6	5	2026-02-08	ACTIVA	Historia clínica abierta en el primer registro del paciente
7	6	2026-02-08	ACTIVA	Historia clínica abierta en el primer registro del paciente
8	7	2026-02-08	ACTIVA	Historia clínica abierta en el primer registro del paciente
9	8	2026-02-08	ACTIVA	Historia clínica abierta en el primer registro del paciente
10	9	2026-02-08	ACTIVA	Historia clínica abierta en el primer registro del paciente
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
1	Ana López García	COL-MD-1001	1	t
2	Carlos Martínez Ruiz	COL-MD-1002	2	t
3	María Fernández Díaz	COL-MD-1003	4	t
4	Javier Gómez Torres	COL-MD-1004	3	t
5	Laura Sánchez Pérez	COL-MD-1005	5	t
6	Pedro Alonso Núñez	COL-MD-1006	6	t
7	Elena Romero Vidal	COL-MD-1007	7	t
8	David Martín Ortega	COL-MD-1008	8	t
9	Raquel Molina Castro	COL-MD-1009	9	t
10	Antonio Herrera León	COL-MD-1010	10	t
\.


--
-- Data for Name: paciente; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.paciente (id_paciente, dni, nombre, apellidos, fecha_nacimiento, sexo, telefono, email, direccion, provincia, cp, aseguradora, num_poliza, nuhsa, nuss, nhc, grupo_sanguineo, alergias, antecedentes_personales, antecedentes_familiares, tratamiento_actual, creado_en, foto_path) FROM stdin;
10	01234567K	Natalia	Cruz Medina	1973-02-09	MUJER	610234567	natalia.cruz@gmail.com	Avenida América 33	Madrid	28002	Sanitas	SAN-6655	NUHSA-0010	NUSS-6655	NHC-0010	AB-	Alergia a marisco	Hipotiroidismo	Hermana con hipotiroidismo	Levotiroxina	2026-02-08 14:24:27.93871	data\\fotos\\paciente_10.png
1	12345678A	Juan	García López	1985-03-12	HOMBRE	612345678	juan.garcia@gmail.com	Calle Mayor 12	Madrid	28001	Sanitas	SAN-1001	NUHSA-0001	NUSS-1001	NHC-0001	A+	Ninguna conocida	Hipertensión leve	Padre con diabetes tipo II	Enalapril 5mg	2026-02-08 14:24:27.93871	data\\fotos\\paciente_1.png
2	23456789B	María	Fernández Ruiz	1992-07-25	MUJER	622456789	maria.fernandez@gmail.com	Avenida de la Paz 45	Madrid	28017	Adeslas	ADE-2034	NUHSA-0002	NUSS-2034	NHC-0002	O+	Alergia a penicilina	Asma leve	Madre asmática	Salbutamol inhalador	2026-02-08 14:24:27.93871	data\\fotos\\paciente_2.png
3	34567890C	Luis	Martín Sánchez	1978-11-03	HOMBRE	633567890	luis.martin@gmail.com	Calle Alcalá 233	Madrid	28028	Asisa	ASI-7788	NUHSA-0003	NUSS-7788	NHC-0003	B+	Ninguna	Colesterol elevado	Hermano con cardiopatía	Atorvastatina	2026-02-08 14:24:27.93871	data\\fotos\\paciente_3.png
5	56789012E	Pedro	López Hernández	2001-09-30	HOMBRE	655789012	pedro.lopez@gmail.com	Calle Toledo 89	Madrid	28005	Privado	PRI-9001	NUHSA-0005	NUSS-9001	NHC-0005	A-	Ninguna	Sin antecedentes relevantes	Sin antecedentes relevantes	Ninguno	2026-02-08 14:24:27.93871	data\\fotos\\paciente_5.png
6	67890123F	Lucía	Navarro Ortega	1989-05-06	MUJER	666890123	lucia.navarro@gmail.com	Calle Serrano 102	Madrid	28006	Sanitas	SAN-3322	NUHSA-0006	NUSS-3322	NHC-0006	O-	Alergia a ácaros	Rinitis alérgica	Padre con alergias	Antihistamínicos	2026-02-08 14:24:27.93871	data\\fotos\\paciente_6.png
7	78901234G	Antonio	Romero Díaz	1959-12-21	HOMBRE	677901234	antonio.romero@gmail.com	Paseo del Prado 14	Madrid	28014	Adeslas	ADE-8899	NUHSA-0007	NUSS-8899	NHC-0007	B-	Ninguna	Insuficiencia venosa	Padre con varices	Medias de compresión	2026-02-08 14:24:27.93871	data\\fotos\\paciente_7.png
8	89012345H	Elena	Torres Gil	1996-04-02	MUJER	688012345	elena.torres@gmail.com	Calle Goya 55	Madrid	28009	Asisa	ASI-4411	NUHSA-0008	NUSS-4411	NHC-0008	A+	Ninguna	Migrañas ocasionales	Madre con migrañas	Paracetamol	2026-02-08 14:24:27.93871	data\\fotos\\paciente_8.png
9	90123456J	Raúl	Santos Vega	1982-08-14	HOMBRE	699123456	raul.santos@gmail.com	Calle Bravo Murillo 210	Madrid	28020	DKV	DKV-7712	NUHSA-0009	NUSS-7712	NHC-0009	O+	Ninguna	Sobrepeso	Padre con obesidad	Dieta controlada	2026-02-08 14:24:27.93871	data\\fotos\\paciente_9.png
4	45678901D	Carmen	Pérez Molina	1965-01-18	MUJER	644678901	carmen.perez@gmail.com	Plaza España 7	Madrid	28008	DKV	DKV-5566	NUHSA-0004	NUSS-5566	NHC-0004	AB+	Intolerancia a lactosa	Artrosis de rodilla	Madre con artrosis	Ibuprofeno puntual	2026-02-08 14:24:27.93871	data\\fotos\\paciente_4.png
\.


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: medicarte; Owner: postgres
--

COPY medicarte.usuario (id_usuario, username, password_hash, rol, id_medico, activo) FROM stdin;
1	admin	$2a$10$eMKYFgf49r5qO8M.i2s6EuHZicX2/p.n5S.1dERi7cwPAwaXdMT.O	ADMIN	\N	t
3	medico_maría_fernandez	$2a$10$SsIIngn6BnhSVbc8hLwDPOthNbgJHhdrmczlbiECzHnnm1j8Y2xm.	MEDICO	3	t
4	medico_javier_gómez	$2a$10$SsIIngn6BnhSVbc8hLwDPOthNbgJHhdrmczlbiECzHnnm1j8Y2xm.	MEDICO	4	t
5	medico_laura_sanchez	$2a$10$SsIIngn6BnhSVbc8hLwDPOthNbgJHhdrmczlbiECzHnnm1j8Y2xm.	MEDICO	5	t
6	medico_pedro_alonso	$2a$10$SsIIngn6BnhSVbc8hLwDPOthNbgJHhdrmczlbiECzHnnm1j8Y2xm.	MEDICO	6	t
7	medico_elena_romero	$2a$10$SsIIngn6BnhSVbc8hLwDPOthNbgJHhdrmczlbiECzHnnm1j8Y2xm.	MEDICO	7	t
8	medico_david_martín	$2a$10$SsIIngn6BnhSVbc8hLwDPOthNbgJHhdrmczlbiECzHnnm1j8Y2xm.	MEDICO	8	t
9	medico_raquel_molina	$2a$10$SsIIngn6BnhSVbc8hLwDPOthNbgJHhdrmczlbiECzHnnm1j8Y2xm.	MEDICO	9	t
10	medico_antonio_herrera	$2a$10$SsIIngn6BnhSVbc8hLwDPOthNbgJHhdrmczlbiECzHnnm1j8Y2xm.	MEDICO	10	t
2	medico1	$2a$10$SsIIngn6BnhSVbc8hLwDPOthNbgJHhdrmczlbiECzHnnm1j8Y2xm.	MEDICO	2	t
\.


--
-- Name: cita_id_cita_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.cita_id_cita_seq', 70, true);


--
-- Name: consulta_id_consulta_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.consulta_id_consulta_seq', 31, true);


--
-- Name: episodio_id_episodio_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.episodio_id_episodio_seq', 30, true);


--
-- Name: especialidad_id_especialidad_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.especialidad_id_especialidad_seq', 10, true);


--
-- Name: historia_clinica_id_historia_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.historia_clinica_id_historia_seq', 10, true);


--
-- Name: log_accion_id_log_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.log_accion_id_log_seq', 1, false);


--
-- Name: medico_id_medico_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.medico_id_medico_seq', 10, true);


--
-- Name: paciente_id_paciente_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.paciente_id_paciente_seq', 10, true);


--
-- Name: usuario_id_usuario_seq; Type: SEQUENCE SET; Schema: medicarte; Owner: postgres
--

SELECT pg_catalog.setval('medicarte.usuario_id_usuario_seq', 10, true);


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

\unrestrict F3zOTBIMBFzXEjKmDehjlc73WHuedYIgTnEAngVAExvWogg0GdaHyKsCpjzOjAt

