--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.8
-- Dumped by pg_dump version 9.4.8
-- Started on 2017-07-07 02:30:20

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 2039 (class 1262 OID 42080)
-- Name: inge2; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE inge2 WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Spanish_Spain.1252' LC_CTYPE = 'Spanish_Spain.1252';


ALTER DATABASE inge2 OWNER TO postgres;

\connect inge2

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
-- TOC entry 2042 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 192 (class 1255 OID 42170)
-- Name: borrar_hijo(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION borrar_hijo() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare 
	
begin
	
	
	if(tg_op = 'DELETE') then
	
		delete from vacunas where id_hijo = OLD.id;
	end if;
return null;
end;
$$;


ALTER FUNCTION public.borrar_hijo() OWNER TO postgres;

--
-- TOC entry 193 (class 1255 OID 42151)
-- Name: completar_registro(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION completar_registro() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare 
	reg						integer;
	cursor_aplicacion		cursor for select id from detalles_aplicacion;
    
	
	id_vacuna 				integer;
	dosis 					integer;
	tiempo 					integer;
	
	nombre_vacuna			varchar(100);
begin
	
	
	if(tg_op = 'INSERT') then
	
		open cursor_aplicacion;
		fetch cursor_aplicacion into reg;
		while (found) loop
			select a.id_detalles_vacuna, a.dosis, a.tiempo_aplicacion into id_vacuna,dosis,tiempo from detalles_aplicacion a
			where a.id = reg;
			
			select nombre into nombre_vacuna from detalles_vacuna where id = id_vacuna;
			
			insert into vacunas (aplicada,fecha_aplicacion,nombre,dosis,id_hijo) 
			values ('No',NEW.fecha_nac + (tiempo::text||'week')::interval,nombre_vacuna, dosis,NEW.id);
			
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
-- TOC entry 176 (class 1259 OID 42108)
-- Name: detalles_aplicacion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE detalles_aplicacion (
    id integer NOT NULL,
    id_detalles_vacuna integer,
    dosis integer,
    tiempo_aplicacion integer
);


ALTER TABLE detalles_aplicacion OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 42161)
-- Name: detalles_vacuna; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE detalles_vacuna (
    id integer NOT NULL,
    nombre character varying
);


ALTER TABLE detalles_vacuna OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 42089)
-- Name: hijo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hijo (
    id_padre integer,
    nombre character varying NOT NULL,
    sexo character varying NOT NULL,
    fecha_nac date NOT NULL,
    id integer NOT NULL
);


ALTER TABLE hijo OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 42139)
-- Name: hijo_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hijo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hijo_id_seq OWNER TO postgres;

--
-- TOC entry 2043 (class 0 OID 0)
-- Dependencies: 178
-- Name: hijo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hijo_id_seq OWNED BY hijo.id;


--
-- TOC entry 173 (class 1259 OID 42081)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario (
    id integer NOT NULL,
    correo character varying NOT NULL,
    nombre character varying NOT NULL
);


ALTER TABLE usuario OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 42097)
-- Name: vacunas; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE vacunas (
    id_hijo integer NOT NULL,
    nombre character varying NOT NULL,
    fecha_aplicacion date NOT NULL,
    aplicada character varying NOT NULL,
    dosis integer NOT NULL,
    id integer NOT NULL
);


ALTER TABLE vacunas OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 42128)
-- Name: vacunas_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE vacunas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vacunas_id_seq OWNER TO postgres;

--
-- TOC entry 2044 (class 0 OID 0)
-- Dependencies: 177
-- Name: vacunas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE vacunas_id_seq OWNED BY vacunas.id;


--
-- TOC entry 1905 (class 2604 OID 42141)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hijo ALTER COLUMN id SET DEFAULT nextval('hijo_id_seq'::regclass);


--
-- TOC entry 1906 (class 2604 OID 42130)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vacunas ALTER COLUMN id SET DEFAULT nextval('vacunas_id_seq'::regclass);


--
-- TOC entry 2031 (class 0 OID 42108)
-- Dependencies: 176
-- Data for Name: detalles_aplicacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (1, 1, 1, 0);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (2, 2, 1, 2);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (3, 3, 1, 2);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (4, 4, 1, 2);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (5, 5, 1, 2);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (6, 2, 2, 4);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (7, 3, 2, 4);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (8, 4, 2, 4);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (9, 5, 2, 4);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (10, 3, 3, 6);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (11, 4, 3, 6);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (12, 11, 1, 6);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (13, 11, 2, 6);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (14, 6, 1, 12);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (15, 5, 3, 12);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (16, 9, 1, 12);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (17, 11, 3, 12);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (18, 7, 1, 15);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (19, 8, 1, 15);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (20, 3, 4, 18);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (21, 10, 1, 18);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (22, 3, 5, 48);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (23, 10, 2, 48);
INSERT INTO detalles_aplicacion (id, id_detalles_vacuna, dosis, tiempo_aplicacion) VALUES (24, 6, 2, 48);


--
-- TOC entry 2034 (class 0 OID 42161)
-- Dependencies: 179
-- Data for Name: detalles_vacuna; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO detalles_vacuna (id, nombre) VALUES (1, 'BCG');
INSERT INTO detalles_vacuna (id, nombre) VALUES (2, 'ROTAVIRUS');
INSERT INTO detalles_vacuna (id, nombre) VALUES (3, 'IPV/OPV');
INSERT INTO detalles_vacuna (id, nombre) VALUES (4, 'PENTAVALENTE');
INSERT INTO detalles_vacuna (id, nombre) VALUES (5, 'PCV10');
INSERT INTO detalles_vacuna (id, nombre) VALUES (6, 'SPR');
INSERT INTO detalles_vacuna (id, nombre) VALUES (7, 'ANTIVARICELA');
INSERT INTO detalles_vacuna (id, nombre) VALUES (8, 'HEPATITIS A');
INSERT INTO detalles_vacuna (id, nombre) VALUES (9, 'AA');
INSERT INTO detalles_vacuna (id, nombre) VALUES (10, 'DTP1');
INSERT INTO detalles_vacuna (id, nombre) VALUES (11, 'INFLUENZA');
INSERT INTO detalles_vacuna (id, nombre) VALUES (12, 'VPH');
INSERT INTO detalles_vacuna (id, nombre) VALUES (13, 'Tdpa');


--
-- TOC entry 2029 (class 0 OID 42089)
-- Dependencies: 174
-- Data for Name: hijo; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2045 (class 0 OID 0)
-- Dependencies: 178
-- Name: hijo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hijo_id_seq', 8, true);


--
-- TOC entry 2028 (class 0 OID 42081)
-- Dependencies: 173
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO usuario (id, correo, nombre) VALUES (1, 'marcecaballero91@gmail.com', 'marcelo');


--
-- TOC entry 2030 (class 0 OID 42097)
-- Dependencies: 175
-- Data for Name: vacunas; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2046 (class 0 OID 0)
-- Dependencies: 177
-- Name: vacunas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('vacunas_id_seq', 49, true);


--
-- TOC entry 1910 (class 2606 OID 42150)
-- Name: pk_hijo; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hijo
    ADD CONSTRAINT pk_hijo PRIMARY KEY (id);


--
-- TOC entry 1914 (class 2606 OID 42112)
-- Name: pk_id_aplicacion; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY detalles_aplicacion
    ADD CONSTRAINT pk_id_aplicacion PRIMARY KEY (id);


--
-- TOC entry 1916 (class 2606 OID 42168)
-- Name: pk_id_vacuna; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY detalles_vacuna
    ADD CONSTRAINT pk_id_vacuna PRIMARY KEY (id);


--
-- TOC entry 1908 (class 2606 OID 42088)
-- Name: pk_usuario; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT pk_usuario PRIMARY KEY (id);


--
-- TOC entry 1912 (class 2606 OID 42138)
-- Name: pk_vacunas; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY vacunas
    ADD CONSTRAINT pk_vacunas PRIMARY KEY (id);


--
-- TOC entry 1918 (class 2620 OID 42171)
-- Name: borrar_hijo; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER borrar_hijo AFTER DELETE ON hijo FOR EACH ROW EXECUTE PROCEDURE borrar_hijo();


--
-- TOC entry 1917 (class 2620 OID 42169)
-- Name: completar_registro; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER completar_registro AFTER INSERT ON hijo FOR EACH ROW EXECUTE PROCEDURE completar_registro();


--
-- TOC entry 2041 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2017-07-07 02:30:21

--
-- PostgreSQL database dump complete
--

