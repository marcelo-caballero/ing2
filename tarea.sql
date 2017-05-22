--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.8
-- Dumped by pg_dump version 9.4.8
-- Started on 2017-05-21 22:35:36

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
-- TOC entry 2010 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

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
-- TOC entry 2011 (class 0 OID 0)
-- Dependencies: 174
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE usuarios_id_seq OWNED BY usuarios.id;


--
-- TOC entry 1886 (class 2604 OID 41263)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios ALTER COLUMN id SET DEFAULT nextval('usuarios_id_seq'::regclass);


--
-- TOC entry 2002 (class 0 OID 41406)
-- Dependencies: 175
-- Data for Name: hijos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hijos (nombre, apellido, email, fecha_nac, ci) FROM stdin;
juan	perez	marcecaballero91@gmail.com	2015-05-16	5555
jorge	fulano	marcecaballero91@gmail.com	2016-03-12	3333
jose	perez	marcecaballero91@gmail.com	2016-04-20	4444
preuba	prueba	prueba	2016-03-12	5634
\.


--
-- TOC entry 2000 (class 0 OID 41258)
-- Dependencies: 173
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY usuarios (id, nombre, correo) FROM stdin;
1	marcelo	marcecaballero91@gmail.com
4	prueba	prueba@gmail.com
5	prueba2	prueba2@gmail.com
\.


--
-- TOC entry 2012 (class 0 OID 0)
-- Dependencies: 174
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('usuarios_id_seq', 5, true);


--
-- TOC entry 1888 (class 2606 OID 41268)
-- Name: id_clave_primaria_tabla_usuario; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuarios
    ADD CONSTRAINT id_clave_primaria_tabla_usuario PRIMARY KEY (id);


--
-- TOC entry 1890 (class 2606 OID 41413)
-- Name: pk_hijo; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hijos
    ADD CONSTRAINT pk_hijo PRIMARY KEY (ci);


--
-- TOC entry 2009 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2017-05-21 22:35:38

--
-- PostgreSQL database dump complete
--

