--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.8
-- Dumped by pg_dump version 9.4.8
-- Started on 2017-05-28 23:32:28

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 1 (class 3079 OID 11855)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2041 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 192 (class 1255 OID 41485)
-- Name: borrar_hijo(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION borrar_hijo() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare 
	
begin
	
	
	if(tg_op = 'DELETE') then
	
		delete from registro_vacunas where ci_hijo = OLD.ci;
	end if;
return null;
end;
$$;


ALTER FUNCTION public.borrar_hijo() OWNER TO postgres;

--
-- TOC entry 193 (class 1255 OID 41473)
-- Name: completar_registro(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION completar_registro() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare 
	reg						integer;
	cursor_aplicacion		cursor for select id from aplicacion;
    
	
	id_vacuna 				integer;
	dosis 					integer;
	tiempo 					integer;
	
	nombre_vacuna			varchar(100);
begin
	
	
	if(tg_op = 'INSERT') then
	
		open cursor_aplicacion;
		fetch cursor_aplicacion into reg;
		while (found) loop
			select a.vacuna, a.dosis, a.tiempo_aplicacion into id_vacuna,dosis,tiempo from aplicacion a
			where a.id = reg;
			
			select nombre into nombre_vacuna from vacunas where id = id_vacuna;
			
			insert into registro_vacunas (id_vacuna,id_aplicacion,aplicada,fecha_aplicacion,nombre_vacuna,ci_hijo) 
			values (id_vacuna,reg,'No',NEW.fecha_nac + (tiempo::text||'week')::interval,nombre_vacuna || ' ' || dosis,NEW.ci);
			
			fetch cursor_aplicacion into reg;
		end loop;
		close cursor_aplicacion;
	
	end if;
return null;
end;
$$;


ALTER FUNCTION public.completar_registro() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 177 (class 1259 OID 41436)
-- Name: aplicacion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE aplicacion (
    id integer NOT NULL,
    vacuna integer,
    dosis integer,
    tiempo_aplicacion integer
);


ALTER TABLE aplicacion OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 41406)
-- Name: hijos; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hijos (
    nombre character varying,
    apellido character varying,
    email character varying,
    fecha_nac date,
    ci integer NOT NULL
);


ALTER TABLE hijos OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 41443)
-- Name: registro_vacunas; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE registro_vacunas (
    id integer NOT NULL,
    id_vacuna integer,
    id_aplicacion integer,
    aplicada character varying,
    fecha_aplicacion date,
    nombre_vacuna character varying,
    ci_hijo integer
);


ALTER TABLE registro_vacunas OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 41441)
-- Name: registro_vacunas_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE registro_vacunas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE registro_vacunas_id_seq OWNER TO postgres;

--
-- TOC entry 2042 (class 0 OID 0)
-- Dependencies: 178
-- Name: registro_vacunas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE registro_vacunas_id_seq OWNED BY registro_vacunas.id;


--
-- TOC entry 173 (class 1259 OID 41258)
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE usuarios (
    id integer NOT NULL,
    nombre character varying(32),
    correo character varying(32)
);


ALTER TABLE usuarios OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 41261)
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE usuarios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE usuarios_id_seq OWNER TO postgres;

--
-- TOC entry 2043 (class 0 OID 0)
-- Dependencies: 174
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE usuarios_id_seq OWNED BY usuarios.id;


--
-- TOC entry 176 (class 1259 OID 41428)
-- Name: vacunas; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE vacunas (
    id integer NOT NULL,
    nombre character varying
);


ALTER TABLE vacunas OWNER TO postgres;

--
-- TOC entry 1905 (class 2604 OID 41446)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY registro_vacunas ALTER COLUMN id SET DEFAULT nextval('registro_vacunas_id_seq'::regclass);


--
-- TOC entry 1904 (class 2604 OID 41263)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios ALTER COLUMN id SET DEFAULT nextval('usuarios_id_seq'::regclass);


--
-- TOC entry 2031 (class 0 OID 41436)
-- Dependencies: 177
-- Data for Name: aplicacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY aplicacion (id, vacuna, dosis, tiempo_aplicacion) FROM stdin;
1	1	1	0
2	2	1	2
3	3	1	2
4	4	1	2
5	5	1	2
6	2	2	4
7	3	2	4
8	4	2	4
9	5	2	4
10	3	3	6
11	4	3	6
12	11	1	6
13	11	2	6
14	6	1	12
15	5	3	12
16	9	1	12
17	11	3	12
18	7	1	15
19	8	1	15
20	3	4	18
21	10	1	18
22	3	5	48
23	10	2	48
24	6	2	48
\.


--
-- TOC entry 2029 (class 0 OID 41406)
-- Dependencies: 175
-- Data for Name: hijos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hijos (nombre, apellido, email, fecha_nac, ci) FROM stdin;
marcelo	caballero	marcecaballero91@gmail.com	2017-05-28	5555
\.


--
-- TOC entry 2033 (class 0 OID 41443)
-- Dependencies: 179
-- Data for Name: registro_vacunas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY registro_vacunas (id, id_vacuna, id_aplicacion, aplicada, fecha_aplicacion, nombre_vacuna, ci_hijo) FROM stdin;
1	1	1	No	2017-05-28	BCG 1	5555
2	2	2	No	2017-06-11	ROTAVIRUS 1	5555
3	3	3	No	2017-06-11	IPV/OPV 1	5555
4	4	4	No	2017-06-11	PENTAVALENTE 1	5555
5	5	5	No	2017-06-11	PCV10 1	5555
6	2	6	No	2017-06-25	ROTAVIRUS 2	5555
7	3	7	No	2017-06-25	IPV/OPV 2	5555
8	4	8	No	2017-06-25	PENTAVALENTE 2	5555
9	5	9	No	2017-06-25	PCV10 2	5555
10	3	10	No	2017-07-09	IPV/OPV 3	5555
11	4	11	No	2017-07-09	PENTAVALENTE 3	5555
12	11	12	No	2017-07-09	INFLUENZA 1	5555
13	11	13	No	2017-07-09	INFLUENZA 2	5555
14	6	14	No	2017-08-20	SPR 1	5555
15	5	15	No	2017-08-20	PCV10 3	5555
16	9	16	No	2017-08-20	AA 1	5555
17	11	17	No	2017-08-20	INFLUENZA 3	5555
18	7	18	No	2017-09-10	ANTIVARICELA 1	5555
19	8	19	No	2017-09-10	HEPATITIS A 1	5555
20	3	20	No	2017-10-01	IPV/OPV 4	5555
21	10	21	No	2017-10-01	DTP1 1	5555
22	3	22	No	2018-04-29	IPV/OPV 5	5555
23	10	23	No	2018-04-29	DTP1 2	5555
24	6	24	No	2018-04-29	SPR 2	5555
\.


--
-- TOC entry 2044 (class 0 OID 0)
-- Dependencies: 178
-- Name: registro_vacunas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('registro_vacunas_id_seq', 24, true);


--
-- TOC entry 2027 (class 0 OID 41258)
-- Dependencies: 173
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY usuarios (id, nombre, correo) FROM stdin;
4	prueba	prueba@gmail.com
5	prueba2	prueba2@gmail.com
7	marce	marcecaballero91@gmail.com
\.


--
-- TOC entry 2045 (class 0 OID 0)
-- Dependencies: 174
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('usuarios_id_seq', 7, true);


--
-- TOC entry 2030 (class 0 OID 41428)
-- Dependencies: 176
-- Data for Name: vacunas; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY vacunas (id, nombre) FROM stdin;
1	BCG
2	ROTAVIRUS
3	IPV/OPV
4	PENTAVALENTE
5	PCV10
6	SPR
7	ANTIVARICELA
8	HEPATITIS A
9	AA
10	DTP1
11	INFLUENZA
12	VPH
13	Tdpa
\.


--
-- TOC entry 1907 (class 2606 OID 41268)
-- Name: id_clave_primaria_tabla_usuario; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuarios
    ADD CONSTRAINT id_clave_primaria_tabla_usuario PRIMARY KEY (id);


--
-- TOC entry 1909 (class 2606 OID 41413)
-- Name: pk_hijo; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hijos
    ADD CONSTRAINT pk_hijo PRIMARY KEY (ci);


--
-- TOC entry 1913 (class 2606 OID 41440)
-- Name: pk_id_aplicacion; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY aplicacion
    ADD CONSTRAINT pk_id_aplicacion PRIMARY KEY (id);


--
-- TOC entry 1911 (class 2606 OID 41435)
-- Name: pk_id_vacuna; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vacunas
    ADD CONSTRAINT pk_id_vacuna PRIMARY KEY (id);


--
-- TOC entry 1915 (class 2606 OID 41451)
-- Name: pk_registro_vacunas; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY registro_vacunas
    ADD CONSTRAINT pk_registro_vacunas PRIMARY KEY (id);


--
-- TOC entry 1917 (class 2620 OID 41486)
-- Name: borrar_hijo; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER borrar_hijo AFTER DELETE ON hijos FOR EACH ROW EXECUTE PROCEDURE borrar_hijo();


--
-- TOC entry 1916 (class 2620 OID 41474)
-- Name: completar_registro; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER completar_registro AFTER INSERT ON hijos FOR EACH ROW EXECUTE PROCEDURE completar_registro();


--
-- TOC entry 2040 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2017-05-28 23:32:29

--
-- PostgreSQL database dump complete
--

