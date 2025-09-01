--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.5

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
-- Name: sequence_id; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sequence_id
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sequence_id OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: artexhibition; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artexhibition (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    artexhibitionstatustype_id bigint,
    site_id bigint,
    subtitle character varying(1024),
    subtitlekey character varying(512),
    info text,
    infokey character varying(512),
    website character varying(1024),
    mapurl character varying(1024),
    email character varying(1024),
    instagram character varying(1024),
    whatsapp character varying(1024),
    phone character varying(1024),
    twitter character varying(1024),
    photo bigint,
    video bigint,
    audio bigint,
    permanent boolean DEFAULT true,
    fromdate timestamp with time zone DEFAULT now() NOT NULL,
    todate timestamp with time zone DEFAULT now() NOT NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    location character varying(4096),
    intro text,
    introkey character varying(512),
    opens character varying(2048),
    openskey character varying(512)
);


ALTER TABLE public.artexhibition OWNER TO postgres;

--
-- Name: artexhibitionguide; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artexhibitionguide (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    publisher_id bigint,
    artexhibition_id bigint,
    artexhibitionguideorder integer DEFAULT 0,
    subtitle character varying(1024),
    subtitlekey character varying(512),
    official boolean DEFAULT false,
    info text,
    infokey character varying(512),
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    opens character varying(2048),
    openskey character varying(512)
);


ALTER TABLE public.artexhibitionguide OWNER TO postgres;

--
-- Name: artexhibitionitem; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artexhibitionitem (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    artexhibition_id bigint,
    artwork_id bigint NOT NULL,
    site_id bigint,
    floor_id bigint,
    room_id bigint,
    artexhibitionorder integer DEFAULT 0,
    mapurl character varying(1024),
    website character varying(1024),
    readcode character varying(1024),
    qcode character varying(1024),
    info text,
    infokey character varying(512),
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.artexhibitionitem OWNER TO postgres;

--
-- Name: artexhibitionstatustype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artexhibitionstatustype (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.artexhibitionstatustype OWNER TO postgres;

--
-- Name: artwork; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artwork (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    shortname character varying(64),
    artworktype_id bigint,
    subtitle character varying(1024),
    subtitlekey character varying(512),
    spec text,
    speckey character varying(512),
    info text,
    infokey character varying(512),
    photo bigint,
    video bigint,
    audio bigint,
    year integer DEFAULT 0,
    person_owner_id bigint,
    institution_owner_id bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    intro text,
    introkey character varying(512),
    site_owner_id bigint,
    usethumbnail boolean DEFAULT true
);


ALTER TABLE public.artwork OWNER TO postgres;

--
-- Name: artworkartist; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artworkartist (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512),
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    artwork_id bigint NOT NULL,
    person_id bigint NOT NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.artworkartist OWNER TO postgres;

--
-- Name: artworktype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artworktype (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.artworktype OWNER TO postgres;

--
-- Name: favorite; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.favorite (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512) DEFAULT 'name'::character varying,
    title character varying(1024),
    titlekey character varying(512),
    person_id bigint NOT NULL,
    site_id bigint NOT NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.favorite OWNER TO postgres;

--
-- Name: floor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.floor (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    floortype_id bigint,
    site_id bigint NOT NULL,
    subtitle character varying(1024),
    subtitlekey character varying(512),
    info text,
    infokey character varying(512),
    floornumber character varying(256),
    floornumberkey character varying(512),
    map bigint,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.floor OWNER TO postgres;

--
-- Name: floortype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.floortype (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.floortype OWNER TO postgres;

--
-- Name: guidecontent; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.guidecontent (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    artexhibitionguide_id bigint NOT NULL,
    artexhibitionitem_id bigint NOT NULL,
    subtitle character varying(1024),
    subtitlekey character varying(512),
    info text,
    infokey character varying(512),
    guideorder integer DEFAULT 0,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.guidecontent OWNER TO postgres;

--
-- Name: institution; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.institution (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    shortname character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    institutiontype_id bigint,
    subtitle character varying(1024),
    subtitlekey character varying(512),
    info text,
    infokey character varying(512),
    address text,
    addresskey character varying(512),
    moreinfo text,
    moreinfokey character varying(512),
    website character varying(1024),
    mapurl character varying(1024),
    email character varying(1024),
    instagram character varying(1024),
    whatsapp character varying(1024),
    phone character varying(1024),
    twitter character varying(1024),
    logo bigint,
    photo bigint,
    video bigint,
    audio bigint,
    map bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.institution OWNER TO postgres;

--
-- Name: institutionalcontent; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.institutionalcontent (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    institution_id bigint,
    site_id bigint,
    subtitle character varying(1024),
    subtitlekey character varying(512),
    info text,
    infokey character varying(512),
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.institutionalcontent OWNER TO postgres;

--
-- Name: institutiontype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.institutiontype (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.institutiontype OWNER TO postgres;

--
-- Name: objectstorage_id; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.objectstorage_id
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.objectstorage_id OWNER TO postgres;

--
-- Name: person; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.person (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    lastname character varying(512) NOT NULL,
    lastnamekey character varying(512),
    displayname character varying(512),
    nickname character varying(512),
    sex character varying(2),
    physicalid character varying(64),
    address character varying(4096),
    zipcode character varying(64),
    phone character varying(512),
    email character varying(512),
    birthdate timestamp with time zone,
    user_id bigint,
    subtitle character varying(1024),
    subtitlekey character varying(512),
    info text,
    infokey character varying(512),
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.person OWNER TO postgres;

--
-- Name: readcode_id; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.readcode_id
    START WITH 1000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.readcode_id OWNER TO postgres;

--
-- Name: resource; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.resource (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    media character varying(64),
    info text,
    infokey character varying(512),
    bucketname character varying(512) NOT NULL,
    objectname character varying(512) NOT NULL,
    binaryobject bytea,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    usethumbnail boolean DEFAULT true
);


ALTER TABLE public.resource OWNER TO postgres;

--
-- Name: room; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.room (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    roomtype_id bigint,
    floor_id bigint NOT NULL,
    roomnumber character varying(256),
    roomnumberkey character varying(512),
    subtitle character varying(1024),
    subtitlekey character varying(512),
    info text,
    infokey character varying(512),
    map bigint,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.room OWNER TO postgres;

--
-- Name: roomtype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roomtype (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.roomtype OWNER TO postgres;

--
-- Name: sequence_user_id; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sequence_user_id
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sequence_user_id OWNER TO postgres;

--
-- Name: site; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.site (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    shortname character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    sitetype_id bigint,
    institution_id bigint NOT NULL,
    website character varying(1024),
    mapurl character varying(1024),
    email character varying(1024),
    instagram character varying(1024),
    whatsapp character varying(1024),
    phone character varying(1024),
    twitter character varying(1024),
    subtitle character varying(1024),
    subtitlekey character varying(512),
    info text,
    infokey character varying(512),
    address text,
    addresskey character varying(512),
    logo bigint,
    photo bigint,
    video bigint,
    audio bigint,
    map bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    opens character varying(2048),
    openskey character varying(512),
    abstract text,
    intro text,
    introkey character varying(512)
);


ALTER TABLE public.site OWNER TO postgres;

--
-- Name: sitetype; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sitetype (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL
);


ALTER TABLE public.sitetype OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint DEFAULT nextval('public.sequence_user_id'::regclass) NOT NULL,
    name character varying(512),
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    username character varying(512) NOT NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Data for Name: artexhibition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibition (id, name, namekey, title, titlekey, artexhibitionstatustype_id, site_id, subtitle, subtitlekey, info, infokey, website, mapurl, email, instagram, whatsapp, phone, twitter, photo, video, audio, permanent, fromdate, todate, created, lastmodified, lastmodifieduser, location, intro, introkey, opens, openskey) FROM stdin;
199	Obras Maestras	obras-maestras	Obras Maestras	\N	\N	137	\N	\N	La obra maestra era el nombre que recibía la pieza artesanal que debía realizar todo oficial que quisiera acceder a la categoría de maestro en el seno de los gremios.	\N	\N	\N	\N	\N	\N	\N	\N	366	\N	\N	t	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	\N	La obra maestra era el nombre que recibía la pieza artesanal que debía realizar todo oficial que quisiera acceder a la categoría de maestro en el seno de los gremios.	\N	\N	\N
233	Grandes obras del impresionismo	grandes-obras-del-impresionismo	Grandes obras del impresionismo	\N	\N	137		\N	La extensa colección de arte impresionista y postimpresionista del Museo incluye obras de artistas destacados, como Édouard Manet, Claude Monet, Camille Pissarro, Berthe Morisot, Edgar Degas, Vincent Van Gogh, Paul Gauguin y Henri Toulouse Lautrec, entre otros.	\N	https://www.bellasartes.gob.ar/coleccion/recorridos/grandes-obras-del-impresionismo_1/.	\N	\N	\N	\N	\N	\N	364	\N	\N	t	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	\N	La extensa colección de arte impresionista y postimpresionista del museo incluye obras de artistas destacados, como Édouard Manet, Claude Monet, Camille Pissarro, Berthe Morisot, Edgar Degas, Vincent Van Gogh, Paul Gauguin y Henri Toulouse Lautrec, entre otros.	\N	\N	\N
284	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	arte-argentino-siglo-xix-hacia-la-consolidaci-n-de-un-modelo-nacional	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	arte-argentino-siglo-xix-hacia-la-consolidaci-n-de-un-modelo-nacional	\N	137		\N	La consolidación de un modelo nacional en el arte argentino fue un proceso gradual que se dio en el siglo XIX y principios del XX, marcado por la búsqueda de una identidad visual propia y la creación de instituciones que promoviesen el arte nacional. Se buscó reflejar la realidad argentina, las costumbres locales y la historia del país, diferenciándose de las influencias europeas.\\n En la segunda mitad del siglo XIX, el arte argentino se caracterizó por el retrato, donde los artistas plasmaban a los personajes relevantes de la nueva nación, siguiendo los cánones neoclásicos de la época. \\n Luego a fines del siglo XIX y principios del XX, se buscó una identidad visual propia, con la creación de instituciones como El Ateneo, donde literatos y artistas debatiían sobre la existencia de un "arte nacional".	\N	https://www.bellasartes.gob.ar	\N	\N	\N	\N	\N	\N	363	\N	\N	t	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	Planta baja sala 20	La consolidación de un modelo nacional en el arte argentino fue un proceso que se dió a fines del siglo XIX y principios del XX, marcado por la búsqueda de una identidad visual propia y la creación de instituciones que promoviesen el arte nacional. Se buscó reflejar la realidad argentina, las costumbres locales y la historia del país, diferenciándose de las influencias europeas.	\N	\N	\N
362	Museo secreto. De la reserva a la sala	museo-secreto	Museo secreto. De la reserva a la sala	\N	\N	137	\N	\N	Entre sus múltiples misiones, los museos preservan la memoria de una nación y, al mismo tiempo, como instituciones dinámicas, estimulan la construcción de nuevos sentidos para las piezas que albergan. Los modos en que las obras de una colección se dan a conocer al público moldean la relación que una comunidad entabla con la historia, vínculo que se potencia cuando una institución es permeable a las transformaciones estéticas y sociales que trae cada época. \\n En el caso de los museos de arte que poseen grandes acervos, la cantidad de obras que el visitante puede apreciar en las salas es solo una parte de un conjunto mucho más vasto; es una propuesta, como toda selección, de una de las tantas lecturas posibles para un patrimonio. Lo que se exhibe es, entonces, un panorama de artistas, períodos, géneros y temas representativos de un corpus que, en gran parte, permanece en las reservas.\\n Museo secreto, el título de esta exposición, hace referencia a un discurso pronunciado por Eduardo Schiaffino, primer director del Bellas Artes y quien impulsó la formación del acervo desde fines del siglo XIX. Publicadas en la prensa durante 1926, las palabras de Schiaffino apuntaban a la necesidad de mostrar el patrimonio que, alojado en los depósitos, permanecía vedado a los ojos de los visitantes. En los 130 años que han transcurrido desde su creación, el Museo Nacional de Bellas Artes ha logrado reunir más de 13.000 pinturas, esculturas, dibujos, grabados, objetos, instalaciones y fotografías, entre otros tipos de piezas, un conjunto cuya magnitud hace que el desafío que acompaña a la institución desde sus primeros años de existencia no solo siga vigente, sino que adquiera cada vez mayor complejidad.\\n Con la idea de expandir el universo de lo que se presenta al público, esta muestra despliega cerca de 300 obras provenientes de las reservas del Museo Nacional de Bellas Artes, realizadas por más de 250 artistas argentinos y extranjeros, desde el siglo XIV hasta la actualidad. Algunas de ellas han estado en las salas como parte de distintos guiones permanentes o en exposiciones temporarias, mientras que otras han tenido menos visibilidad. \\n La curaduría de esta muestra surge del intercambio de saberes y del trabajo colectivo entre todos los equipos del Museo. La disposición de las obras recrea el modo en que se presentaban las colecciones en el siglo XIX, como puede apreciarse a pocos metros de aquí, en la sala Guerrico. Este tipo de distribución permite disponer en el espacio un mayor número de piezas, y también emula la forma en que se las agrupa en las reservas. \\n Concebida en un esquema no lineal, la selección conecta géneros, estilos y temas que han guiado la producción de artistas de todas las épocas, con obras distribuidas en grandes núcleos a modo de constelaciones que habilitan diálogos históricos y estéticos, y que, a la vez, ponen en tensión diversas concepciones del arte como herramienta de representación. A través de este panorama expandido de la colección de arte más importante de la Argentina, invitamos a pensar nuevos vínculos entre el pasado y el presente de la historia  visual.	\N	https://www.bellasartes.gob.ar/exhibiciones/museo-secreto/	\N	\N	\N	\N	\N	\N	367	\N	368	f	2025-06-09 17:55:14.336329-03	2025-06-09 17:55:14.336329-03	2025-06-09 17:55:14.336329-03	2025-06-09 17:55:14.336329-03	100	\N	El Bellas Artes inicia su programación anual con una muestra antológica que reúne en el Pabellón de exposiciones temporarias cerca de 300 obras provenientes de las reservas.	\N	Del 22 de enero de 2025 al 31 de agosto de 2025 \\nPabellón de exposiciones temporarias	\N
454	Sendas perdidas	sendas-perdidas	Sendas perdidas	\N	\N	137	\N	\N	\N	\N	https://www.bellasartes.gob.ar/exhibiciones/german-gargano/	\N	\N	\N	\N	\N	\N	466	\N	\N	f	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	\N	Germán Gárgano fue preso político en Argentina desde 1975 hasta 1982, y  mantuvo una relación epistolar desde la cárcel con el pintor Carlos Gorriarena, con quien continuaría formándose al recuperar su libertad.  “Sendas perdidas” es su primera muestra en el Museo Nacional de Bellas Artes, con más de 170 dibujos, gouaches, acuarelas y tintas realizadas en los últimos años.	\N	\N	\N
\.


--
-- Data for Name: artexhibitionguide; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionguide (id, name, namekey, title, titlekey, publisher_id, artexhibition_id, artexhibitionguideorder, subtitle, subtitlekey, official, info, infokey, photo, video, audio, created, lastmodified, lastmodifieduser, opens, openskey) FROM stdin;
202	Obras Maestras	obras-maestras	Obras Maestras	\N	\N	199	0	\N	\N	f	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	\N	\N
248	Grandes obras del Impresionismo	\N	Grandes obras del Impresionismo	\N	\N	\N	0	\N	\N	t	\N	\N	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	\N	\N
292	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	\N	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	\N	\N	284	0	\N	\N	t	\N	\N	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	\N	\N
411	Museo Secreto	\N	Museo Secreto. Del archivo a la sala	\N	180	362	0	\N	\N	t	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	\N	\N
458	Sendas perdidas	\N	Sendas perdidas	\N	180	454	0	\N	\N	t	\N	\N	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	\N	\N
\.


--
-- Data for Name: artexhibitionitem; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionitem (id, name, namekey, title, titlekey, artexhibition_id, artwork_id, site_id, floor_id, room_id, artexhibitionorder, mapurl, website, readcode, qcode, info, infokey, created, lastmodified, lastmodifieduser) FROM stdin;
201	Morro da favela II (Pueblito)	morro-da-favela-ii-pueblito	Morro da favela II (Pueblito)	\N	199	195	137	138	140	2	\N	\N	1001	\N	Época de temática popular, la década del 40 también sería para Tarsila, la gran pintora modernista de los años 20, tiempo de volver definitivamente de sus tonos pau Brasil, es decir, retornar a los colores llamados caipiras (azul y rosa) de la decoración popular de los modestos caseríos de las regiones del interior del Brasil. Este colorido, aunque más intenso, estuvo presente en sus telas de los años 20, su período de máxima creatividad y producción, etapa de una gran síntesis de sus elementos compositivos. Después de 1930, cuando Tarsila se orientó, por razones culturales y políticas, hacia la temática social, su paleta se alteró igualmente.\\nPor esa época la artista regresó a San Pablo después de alternar varios años entre esa ciudad y Río de Janeiro, y pasó gran parte de su tiempo en su hacienda de Indaiatuba, en el interior paulista. \\nConociendo la inexistencia de un mercado de arte local, realizó encargos de pinturas, ilustraciones y colaboró con crónicas regulares sobre la vida cultural y memorias en Diário de São Paulo.\\nEn Pueblito, de 1945, encontramos una segunda versión de la famosa pintura Morro da favela (1924, colección João Estefano, San Pablo), tela tan admirada por el poeta Blaise Cendrars que le recomendó, en su momento, que preparase su primera exposición individual en París a partir de la serie que se iniciara con esta obra. La temática está inspirada en el viaje a Río con los modernistas de San Pablo acompañando a Cendrars en su primera visita al Brasil.\\nAlgunas variantes, sin embargo, surgen en esta tela, ahora en la colección del MNBA: sus dimensiones son menores que las de Morro da favela, la vegetación tiene modificaciones, dejando de ser visualmente presentada en formas sintéticas, en esta tela de 1945 aparecen como matas de plantas más desmenuzadas, más diversificadas en su ubicación, especialmente en el primer plano. Por otra parte, las pinceladas son bien evidentes, casi “impresionistas” en su aplicación sobre la tela, y el ave extraña de 1924 es reemplazada por la confrontación de dos pavos. Finalmente, el color es más suave en comparación con la pintura de veinte años antes.\\nLa obra del MNBA estuvo expuesta en Buenos Aires, La Plata y Montevideo en la exposición Veinte artistas brasileños presentada en 1945, con la organización del escritor Marques Rebelo y la intermediación efectiva de Emilio Pettoruti.\\nEn esta muestra Tarsila estuvo representada por otra pintura, Pueblito II (1944), y por un dibujo, estudio de 16 x 22 cm de la pintura de la colección del MNBA, hoy aparentemente perdido. La exposición dio lugar a dos pequeños libros, actualmente antológicos, uno de autoría de Cipriano Vitureira, de Montevideo, y el segundo de Jorge Romero Brest, La pintura brasileña contemporánea, publicado el mismo año de la exposición por la editorial Poseidón.	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
234	Effet de neige à Louveciennes	effet-de-neige-louveciennes	Effet de neige à Louveciennes	\N	233	205	137	138	151	1	\N	\N	1002	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
235	Le Pont d`Argenteuil (El Puente de Argenteuil)	le-pont-d-argenteuil-el-puente-de-argenteuil	Le Pont d`Argenteuil (El Puente de Argenteuil)	\N	233	207	137	138	151	2	\N	\N	1003	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
236	Prairies du Valhermeil près Pontoise	prairies-du-valhermeil-pr-s-pontoise	Prairies du Valhermeil près Pontoise	\N	233	209	137	138	\N	3	\N	\N	1004	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
237	Femme aux champs (Campesina)	femme-aux-champs-campesina	Femme aux champs (Campesina)	\N	233	211	137	138	152	4	\N	\N	1005	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
242	La Coiffure (El peinado)	la-coiffure-el-peinado	La Coiffure (El peinado)	\N	233	221	137	138	154	9	\N	\N	1010	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
246	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	danseuse-et-admirateur-derri-re-la-sc-ne-bailarina-y-admirador-tras-la-escena	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	\N	233	225	137	138	154	13	\N	\N	1014	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
247	La Nymphe surprise (La ninfa sorprendida)	la-nymphe-surprise-la-ninfa-sorprendida	La Nymphe surprise (La ninfa sorprendida)	\N	233	225	137	138	154	14	\N	\N	1015	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
285	La vuelta del malón	la-vuelta-del-mal-n	La vuelta del malón	la-vuelta-del-mal-n	284	270	137	138	151	1	\N	\N	1016	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
286	En Normandie	en-normandie	En Normandie	en-normandie	284	272	137	138	151	2	\N	\N	1017	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
287	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	284	274	137	138	151	3	\N	\N	1018	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
288	La vuelta al hogar	la-vuelta-al-hogar	La vuelta al hogar	la-vuelta-al-hogar	284	276	137	138	151	4	\N	\N	1019	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
289	El despertar de la criada	el-despertar-de-la-criada	El despertar de la criada	el-despertar-de-la-criada	284	278	137	138	151	5	\N	\N	1020	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
290	Reposo	reposo	Reposo	reposo	284	280	137	138	151	6	\N	\N	1021	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
291	Abel	abel	Abel	abel	284	282	137	138	151	7	\N	\N	1022	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
200	Le bain de Vénus (El baño de Venus)	le-bain-de-v-nus-el-ba-o-de-venus	Le bain de Vénus (El baño de Venus)	\N	199	197	137	138	140	1	\N	\N	1000	\N	El título original de esta pintura puede haber sido puesto por Redon, ya que al parecer la obra fue comprada al artista sin intermediarios. Los toques de azul y verde en la zona inferior derecha de la tela pueden considerarse una representación del agua, y el tocado de la figura puede evocar un personaje mitológico. Estos aspectos, sin embargo, son los únicos que justificarían el título de El baño de Venus. La figura no está desnuda, aparece (apenas) vestida y carece de otro atributo inherente a la diosa del amor. Su movimiento no corresponde a una escena de baño y el personaje demuestra una vacilación indigna de alguien nacido del mar. Con los dos brazos extendidos hacia delante y la cabeza hacia atrás, parece moverse de derecha a izquierda como si estuviese huyendo de algo –en una posición relativamente similar a la del dibujo Female Nude (Bridgestone Museum, Tokio)–. El origen de su miedo o resistencia parece ser la forma circular sobre la derecha (de la mitad del tamaño de la figura) que semeja más a un insecto o a uno de los monstruos submarinos pintados por Redon que a las flores y las hojas reconocibles sobre la izquierda. Esta interpretación se sustenta en el uso del negro para delinear y rellenar parte de esta forma, color asociado por Redon al mundo fantástico de sus primeras carbonillas y litografías.	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
238	En observation - M.Fabre, Officier de reserve	en-observation-m-fabre-officier-de-reserve	En observation - M.Fabre, Officier de reserve	\N	233	213	137	138	152	5	\N	\N	1006	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
239	Vahine no te miti (Femme a la mer) (Mujer del mar)	vahine-no-te-miti-femme-a-la-mer-mujer-del-mar	Vahine no te miti (Femme a la mer) (Mujer del mar)	\N	233	215	137	138	154	6	\N	\N	1007	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
240	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	portrait-de-suzanne-valadon-madame-suzanne-valadon-artiste-peintre-retrato-de-suzanne-valadon-pintora	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	\N	233	217	137	138	154	7	\N	\N	1008	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
241	La berge de La Seine (Orillas del Sena)	la-berge-de-la-seine-orillas-del-sena	La berge de La Seine (Orillas del Sena)	\N	233	219	137	138	154	8	\N	\N	1009	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
243	La Toilette apres le bain (El arreglo después del baño)	la-toilette-apres-le-bain-el-arreglo-despu-s-del-ba-o	La Toilette apres le bain (El arreglo después del baño)	\N	233	223	137	138	154	10	\N	\N	1011	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
244	Le Moulin de la Galette	le-moulin-de-la-galette	Le Moulin de la Galette	\N	233	225	137	138	154	11	\N	\N	1012	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
245	Portrait de Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	portrait-de-ernest-hosched-et-sa-fille-marthe-retrato-del-se-or-hosched-y-su-hija	Portrait de Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	\N	233	225	137	138	154	12	\N	\N	1013	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
408	La Emperatriz Theodora	\N	La Emperatriz Theodora	\N	362	402	137	138	140	1	\N	\N	1026	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100
409	Retrato de Juan Manuel de Rosas	\N	Retrato de Juan Manuel de Rosas	\N	362	406	137	138	140	2	\N	\N	1027	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100
410	En la costa de Valencia	\N	En la costa de Valencia	\N	362	404	137	138	140	2	\N	\N	1028	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100
455	Sendas perdidas	\N	Sendas perdidas	\N	454	448	137	139	\N	1	\N	\N	1029	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100
456	Apocalipsis	\N	Apocalipsis	\N	454	450	137	139	\N	2	\N	\N	1030	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100
457	El recurso del método	\N	El recurso del método	\N	454	452	137	139	\N	2	\N	\N	1031	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100
486	Utopia del Sur	\N	Utopia del Sur	\N	\N	482	137	139	\N	1	\N	\N	1034	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100
487	Procesión sorprendida por la lluvia	\N	Procesión sorprendida por la lluvia	\N	\N	484	137	139	\N	2	\N	\N	1035	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100
\.


--
-- Data for Name: artexhibitionstatustype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionstatustype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser) FROM stdin;
131	En preparación	coming	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
132	En curso	enabled	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
133	Terminada	terminated	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
134	Cancelada	cancelled	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
\.


--
-- Data for Name: artwork; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artwork (id, name, namekey, title, titlekey, shortname, artworktype_id, subtitle, subtitlekey, spec, speckey, info, infokey, photo, video, audio, year, person_owner_id, institution_owner_id, created, lastmodified, lastmodifieduser, intro, introkey, site_owner_id, usethumbnail) FROM stdin;
307	Quiosco de Canaletas	quiosco-de-canaletas	Quiosco de Canaletas	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	\N	135	2025-05-19 12:53:48.670893-03	2025-05-19 12:53:48.670893-03	100	\N	\N	137	t
221	La Coiffure (El peinado)La berge de La Seine (Orillas del Sena)	la-coiffure-el-peinado-la-berge-de-la-seine-orillas-del-sena	La Coiffure (El peinado)La berge de La Seine (Orillas del Sena)	\N	\N	126		\N	60 x 73 cm. Con marco: 84 x 97 cm.	\N		\N	\N	\N	\N	1891	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100		\N	137	t
406	Retrato de Juan Manuel de Rosas	retrato-de-juan-manuel-de-rosas	Retrato de Juan Manuel de Rosas	\N	\N	126		\N	Oleo sobre tela 100 x 79,8 cm.	\N	El Retrato de Juan Manuel de Rosas, pintado por el artista francés Raymond Monvoisin hacia 1842, ofrece una representación simbólica y estratégica del caudillo argentino.  A diferencia de otros cuadros oficiales donde Rosas aparece con uniforme militar, en esta obra se lo muestra con un poncho, resaltando su imagen de líder popular y no meramente militar.\\nMonvoisin se había formado en la tradición académica europea, pero para 1842 estaba más cerca del romanticismo. Rosas aparece de perfil, con expresión seria y firme, destacando su autoridad, y su mirada apunta a donde se origina el viento como  controlando la naturaleza. Mitre señaló “es el retrato más parecido que conozco de Rosas, hay mucho de emperador romano pero atenuado y corregido por un acento criollo”.\\nLa tradición artística oficial argentina considera al cuadro un estudio preliminar de una obra posterior desaparecida o que no existió. Según Rodrigo Cañete en “Historia a contrapelo del arte argentino”,  esta idea está contaminada por el prejuicio de negarse a aceptar el verdadero carácter de la obra.  Para Cañete, el artista, acogido en Chile por los enemigos de Rosas, tuvo el talento de hacer un cuadro ambigüo para que ambos bandos -unitarios y federales- puedan utilizarlo como bandera. Los rasgos de Rosas son imperiales pero carecen de la musculatura heroica de los emperadores y están levemente feminizados, y el poncho y la ropa son parecidos a los retratos de sus enemigos como gaucho bárbaro, líder de lo que Sarmiento despectivamente llamaba la “montonera árabe”.  Por su parte, para los rosistas, representa un emperador-dios-benefactor-madre tierra, al estilo de la Virgen María de las imágenes sudamericanas que la fusionan con la Pachamama india, como la Virgen María del Cerro Rico de Potosí,  pero con referencias académicas sutiles a grandes maestros como Zurbarán y Velázquez.	\N	415	\N	\N	1842	\N	135	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	El Retrato de Juan Manuel de Rosas, pintado por el artista francés Raymond Monvoisin hacia 1842, ofrece una representación simbólica y estratégica del caudillo argentino.  A diferencia de otros cuadros oficiales donde Rosas aparece con uniforme militar, en esta obra se lo muestra con un poncho, resaltando su imagen de líder popular y no meramente militar.\\nMonvoisin se había formado en la tradición académica europea, pero para 1842 estaba más cerca del romanticismo. Rosas aparece de perfil, con expresión seria y firme, destacando su autoridad, y su mirada apunta a donde se origina el viento como  controlando la naturaleza. Mitre señaló “es el retrato más parecido que conozco de Rosas, hay mucho de emperador romano pero atenuado y corregido por un acento criollo”.\\nLa tradición artística oficial argentina considera al cuadro un estudio preliminar de una obra posterior desaparecida o que no existió. Según Rodrigo Cañete en “Historia a contrapelo del arte argentino”,  esta idea está contaminada por el prejuicio de negarse a aceptar el verdadero carácter de la obra.  Para Cañete, el artista, acogido en Chile por los enemigos de Rosas, tuvo el talento de hacer un cuadro ambigüo para que ambos bandos -unitarios y federales- puedan utilizarlo como bandera. Los rasgos de Rosas son imperiales pero carecen de la musculatura heroica de los emperadores y están levemente feminizados, y el poncho y la ropa son parecidos a los retratos de sus enemigos como gaucho bárbaro, líder de lo que Sarmiento despectivamente llamaba la “montonera árabe”.  Por su parte, para los rosistas, representa un emperador-dios-benefactor-madre tierra, al estilo de la Virgen María de las imágenes sudamericanas que la fusionan con la Pachamama india, como la Virgen María del Cerro Rico de Potosí,  pero con referencias académicas sutiles a grandes maestros como Zurbarán y Velázquez.	\N	137	t
231	La Nymphe surprise (La ninfa sorprendida)	la-nymphe-surprise-la-ninfa-sorprendida	La Nymphe surprise (La ninfa sorprendida)	\N	\N	126		\N	Oleo Sobre tela 144,5 x 112,5 cm. Marco: 169,8 x 137,5 x 9,5 cm.	\N	La nymphe surprise inaugura un período clave de la trayectoria de Édouard Manet y de la historia del modernismo en la pintura francesa (1). Según Barskaya, fue terminada y enviada por el artista a la exposición de la Academia de Arte de San Petersburgo en 1861 con el título Ninfa y sátiro, dos años antes de la exposición en el Salón de Rechazados de Le déjeuner sur l’herbe y de Olympia (pintada ese mismo año aunque enviada y aceptada en el Salon des Artistes Français en 1865, ambas en el Musée d’Orsay, París). Una larga serie de estudios documentales y técnicos, así como discusiones respecto de sus posibles fuentes, revelan un proceso largo y complejo en la elaboración de esta tela, considerada el primer gran tableau-laboratoire de Manet reelaborando modelos de la gran pintura italiana y holandesa de los siglos XVI y XVII. La obra permaneció en poder del artista hasta su muerte, y existe evidencia de que Manet la consideró uno de sus cuadros más importantes.	\N	317	\N	\N	1861	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	La nymphe surprise inaugura un período clave de la trayectoria de Édouard Manet y de la historia del modernismo en la pintura francesa (1). Según Barskaya, fue terminada y enviada por el artista a la exposición de la Academia de Arte de San Petersburgo en 1861 con el título Ninfa y sátiro, dos años antes de la exposición en el Salón de Rechazados de Le déjeuner sur l’herbe y de Olympia (pintada ese mismo año aunque enviada y aceptada en el Salon des Artistes Français en 1865, ambas en el Musée d’Orsay, París). Una larga serie de estud	\N	137	t
328	Toulouse Lautrec - En observation - M.Fabre, Officier de reserve	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve	Toulouse Lautrec - En observation - M.Fabre, Officier de reserve	\N	\N	\N	\N	\N	\N	\N	\N	\N	329	\N	\N	0	\N	135	2025-05-19 14:07:30.084163-03	2025-05-19 14:07:30.084163-03	100	\N	\N	137	t
311	Quiosco de Canaletas	quiosco-de-canaletas	Quiosco de Canaletas	\N	\N	\N	\N	\N	\N	\N	\N	\N	312	\N	\N	0	\N	135	2025-05-19 13:07:58.324319-03	2025-05-19 13:07:58.324319-03	100	\N	\N	137	t
324	Portrait d'Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	portrait-d-ernest-hosched-et-sa-fille-marthe-retrato-del-se-or-hosched-y-su-hija	Portrait d'Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	\N	\N	\N	\N	\N	\N	\N	\N	\N	325	\N	\N	0	\N	135	2025-05-19 14:07:26.434656-03	2025-05-19 14:07:26.434656-03	100	\N	\N	137	t
332	Puente de La Boca	puente-de-la-boca	Puente de La Boca	\N	\N	\N	\N	\N	\N	\N	\N	\N	334	\N	\N	0	\N	135	2025-05-19 14:07:33.515503-03	2025-05-19 14:07:33.515503-03	100	\N	\N	137	t
211	Femme aux champs (Campesina)	femme-aux-champs-campesina	Femme aux champs (Campesina)	\N	\N	126		\N	Oleo Sobre tela 88 x 48 cm.	\N	Galerie Georges Petit.	\N	321	\N	\N	1882	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	Galerie Georges Petit.	\N	137	t
207	Le Pont d`Argenteuil (El Puente de Argenteuil)	le-pont-d-argenteuil-el-puente-de-argenteuil	Le Pont d`Argenteuil (El Puente de Argenteuil)	\N	\N	126	Donación Mercedes Santamarina, 1970.	\N	Oleo Sobre tela 60 x 100 cm.	\N	Esta pintura plantea algunos interrogantes de orden iconográfico. Generalmente señalado con el título de Puente de Argenteuil, el cuadro es llamado de otra manera por Daniel Wildenstein, el autor del catálogo razonado de las obras de Claude Monet (1), que individualiza más bien en el paisaje representado al puente ferroviario de Chatou (2), visto desde la isla de Chiard. Esta propuesta sería más atendible. En efecto, la obra muestra un escorzo perspectivo, de abajo arriba, de un puente ferroviario de arcos amplios. Pero sucede que el puente de Argenteuil, de igual factura, es un puente vial (Le Pont d’Argenteuil, 1870, Musée d’Orsay, París); el puente ferroviario de la misma ciudad, en cambio, "no está formado por una serie de arcos redondeados sino por una sola arcada en forma de cavidad con compartimentos” que se apoya sobre “cuatro pares de cilindros alargados de cemento” (3), similares a columnas y sin arcos (Le pont du chemin de fer à Argenteuil, 1874, Musée d’Orsay, París). La isla de Chiard, conocida también como “isla de los impresionistas”, quienes la frecuentaron regularmente para pintar en plein air (4), se encuentra a orillas del Sena, en la periferia oeste de París. Toda la zona, constituida por numerosas localidades (Argenteuil, Asnières, Chatou) muy cercanas entre sí, era un destino muy frecuentado los domingos por los parisinos que iban allí a distenderse a la orilla del agua; esos lugares estaban, por lo demás, a solo media hora de la estación Saint-Lazare. Toda la zona, y en particular la isla de Chiard, albergaba, en la riqueza de su vegetación, numerosas viviendas de artistas. El propio Monet alquiló una casa en Argenteuil entre 1872 y 1877, justamente para estar lo más cerca posible de estos temas que tanto lo fascinaban. Verdaderas novedades sociológicas, estas localidades suscitaron el interés de toda la cultura francesa, desde la llamada “masa” hasta las corrientes más sofisticadas de la vanguardia artística y literaria. En sus representaciones de Argenteuil –aunque con ese nombre debe entenderse a menudo una zona mucho más amplia Monet denota cierta ambivalencia. En algunos cuadros ofrece una visión bucólica de la que están ausentes, o casi, la mayor parte de los símbolos de la modernidad, alineándose en ese sentido a la visión de la naturaleza cotidiana de Corot y Daubigny, alejada de lo pintoresco, casi intacta. En otras obras del mismo período, en cambio, los símbolos del progreso industrial alternan y se mezclan con los de la vida rural, como para subrayar una armonía posible entre naturaleza y modernidad. Las imágenes serenas de las regatas (Le bassin d’Argenteuil, 1872, Musée d’Orsay), a menudo representadas por el artista, atraído por los juegos de agua y de luz, alternan asimismo con las arquitecturas metálicas de los puentes modernos, que después de las destrucciones de la guerra franco-prusiana estaban redefiniendo nuevamente el paisaje. El tema de los ferrocarriles había entrado en el repertorio de Monet a partir de 1874, cuando el artista pintó numerosas vistas del Sena y de los puentes que lo atraviesan fuera de París. Entre 1850 y 1870, Francia conoció un gran desarrollo industrial respaldado por la construcción de importantes infraestructuras (ferrocarriles, estaciones, puentes, viaductos) que se insertaron repentinamente en el paisaje urbano y rural, sobre todo alrededor de la capital. En 1871, 16.000 km de red ferroviaria atravesaban ya todo el territorio francés y la pintura no fue indiferente a este hecho. Monet compartió su interés por los motivos ferroviarios con varios precursores de la pintura de tema moderno, como el alemán Adolf Menzel y el inglés William Turner, de quien había visto en Londres la pintura Rain, Steam and Speed durante su estadía entre 1870 y 1871. Los puentes de Monet, al igual que las sucesivas estaciones realizadas en 1877 (en particular la de Saint-Lazare), no constituyen una “serie” en sentido estricto, sino más bien una “secuencia”, según la definición de Grace Seiberling desarrollada en torno de la misma temática. Comparadas con las famosas series de los años 90 (La catedral de Rouen, por ejemplo), estas pinturas no presentan ni una concepción unitaria ni un trabajo de armonización general efectuado en el taller. Como el puente de Argenteuil, también el de Chatou había atraído la atención de numerosos artistas, que lo representaron desde distintos puntos de vista (Pierre-Auguste Renoir, Pont de chemin de fer à Chatou ou Les marronniers roses, 1881, Musée d’Orsay). El cuadro de Buenos Aires constituye un paisaje “puro”, vaciado de la presencia humana, enteramente concentrado en la reacción rápida del artista frente a la naturaleza, traducida aquí en un follaje denso y vibrante, completamente desarrollado en tonalidades frías (azul, verde, amarillo). El punto de vista de la obra –rebajado a un nivel coincidente con la rica vegetación– desde el cual se distingue el puente ferroviario atravesado por un tren en marcha, parece querer subrayar la idea de una modernidad aparecida al artista en forma imprevista y casi por casualidad. Esta obra proviene de la rica colección impresionista de Mercedes Santamarina. El MNBA posee otra obra de Monet, La berge de la Seine (1880), proveniente de la Exposición Internacional de Arte del Centenario, de 1910.\r\n  Barbara Musetti\r\n1— Entre las biografías más recientes, véase: M. Alphant, Claude Monet. Une vie dans le paysage. Paris, Hazan, 1993; C. F. Stuckey, Monet. Un peintre, une vie, une oeuvre. Paris, Belfond, 1992. 2— D. Wildenstein, 1974, p. 1875. 3— H. P. Tucker, Monet at Argenteuil. New Haven/London, Yale University Press, 1982, p. 70. 4— Señalemos dos catálogos de exposición dedicados al paisaje impresionista: L’Impressionnisme et le paysage français, cat. exp. Paris, Réunion des musées nationaux, 1985; Landscapes of France. Impressionism and its Rivals, cat. exp. London/Boston, Hayward Gallery/ Museum of Fine Arts, 1995.\r\n1964. NIEULESEU, R., “G. Di Bellio, l’ami des impressionnistes”, Revue Roumaine d’Histoire de l’Art, Bucarest, t. 1, nº 2, p. 222, 278. 1974. WILDENSTEIN, Daniel, Claude Monet. Biographie et catalogue raisonné. Lausanne/ Paris, Bibliothèque des Arts, t. 1, nº 367, reprod. p. 1875.	\N	320	\N	\N	1875	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	Esta pintura plantea algunos interrogantes de orden iconográfico. Generalmente señalado con el título de Puente de Argenteuil, el cuadro es llamado de otra manera por Daniel Wildenstein, el autor del catálogo razonado de las obras de Claude Monet (1), que individualiza más bien en el paisaje representado al puente ferroviario de Chatou (2), visto desde la isla de Chiard. Esta propuesta sería más atendible. En efecto, la obra muestra un escorzo perspectivo, de abajo arriba, de un puente ferroviario de arcos amplios. Pero sucede que el 	\N	137	t
272	En Normandie	en-normandie	En Normandie	\N	\N	126	Colección particular. Cedida en comodato al Museo Nacional de Bellas Artes, 2021.	\N	Oleo Sobre tela 162 x 207 cm.	\N	Este óleo sobre tela de gran formato fue el envío de Obligado, por entonces radicada en Francia, al salón parisino de 1902. En ella, las figuras femeninas que dominan la composición realizan redes de pesca en el norte de la costa francesa, una actividad de vital importancia para la economía regional.\\n El cuadro presenta a un grupo de mujeres entregadas a una tarea minuciosa y sistemática. Igual de laborioso fue el trabajo de la artista para crear esta escena: ejecutó detallados estudios de cada figura y de la composición en general, a la que consideraba “el alma de toda pintura”.\\n Para conocer más sobre esta obra, invitamos a leer el ensayo de la historiadora María Lía Munilla Lacasa, publicado en el micrositio dedicado a la exposición  curada por Georgina Gluzman, que traza un paralelo entre la tela de Obligado y la emblemática “Sin pan y sin trabajo”, de De la Cárcova, y celebra esta reparación histórica hacia la obra de una de las grandes artistas del período en el país.\\n “El mundo del trabajo –o de su ausencia– es el tema que ambos cuadros abordan. Mientras que en la obra de María Obligado seis trabajadoras de la costa de Normandía, al norte de Francia, ensimisman su rutina en la tarea de tejer redes de pesca, en el óleo de Ernesto de la Cárcova se acentúa con particular dramatismo un topos pictórico europeo característico de la segunda mitad del siglo XIX: el de la pobreza urbana”, explica Munilla Lacasa.	\N	303	\N	\N	1902	\N	135	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	Este óleo sobre tela de gran formato fue el envío de Obligado, por entonces radicada en Francia, al salón parisino de 1902. En ella, las figuras femeninas que dominan la composición realizan redes de pesca en el norte de la costa francesa, una actividad de vital importancia para la economía regional.\\n El cuadro presenta a un grupo de mujeres entregadas a una tarea minuciosa y sistemática. Igual de laborioso fue el trabajo de la artista para crear esta escena: ejecutó detallados estudios de cada figura y de la composición en general, 	\N	137	t
270	La vuelta del malón	la-vuelta-del-mal-n	La vuelta del malón	\N	\N	126	Adqusición Sociedad de Estímulo	\N	Oleo Sobre tela 186,5 x 292 cm.	\N	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. Pintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtuvo una medalla (de única clase) y al regreso se exhibió nuevamente en Buenos Aires, en el segundo Salón del Ateneo en 1894. \\nFue la obra más celebrada de Della Valle. Presentaba por primera vez en las grandes dimensiones de una pintura de salón una escena que había sido un tópico central de la conquista y de la larga guerra de fronteras con las poblaciones indígenas de la pampa a lo largo del siglo XIX: el saqueo de los pueblos fronterizos, el robo de ganado, la violencia y el rapto de cautivas. En el manejo de la luz y la pincelada se advierte la huella de la formación de Della Valle en Florencia: no solo el aprendizaje con Antonio Ciseri sino también el conocimiento de los macchiaioli y los pintores del Risorgimento italiano. Algunos críticos lo vincularon con los grandes cuadros de historia del español Ulpiano Checa que se había hecho famoso por sus entradas de bárbaros en escenas de la historia de España y del imperio romano. Sin embargo, el cuadro de Della Valle entroncaba con una larga tradición no solo en las crónicas y relatos literarios inspirados en malones y cautivas, sino también en imágenes que, desde los primeros viajeros románticos europeos que recorrieron la región en la primera mitad del siglo XIX, representaron cautivas y malones. \\n En la década de 1870 Juan Manuel Blanes había realizado también algunas escenas de malones que aparecen como antecedentes de esta obra. Casi ninguna, sin embargo, había sido expuesta al público ya que tuvieron una circulación bastante restringida. La vuelta del malón fue, entonces, la primera imagen que impactó al público de Buenos Aires referida a una cuestión de fuerte valor emotivo e inequívoco significado político e ideológico. Según refiere Julio Botet, a partir de una entrevista al artista en agosto de 1892, el asunto del cuadro se inspiraba en un malón llevado por el cacique Cayutril y el capitanejo Caimán a una población no mencionada. Otro comentario (en el diario Sud-América) ubicaba el episodio en la población de 25 de Mayo. Pero más allá de la anécdota el cuadro aparece como una síntesis de los tópicos que circularon como justificación de la “campaña del desierto” de Julio A. Roca en 1879, produciendo una inversión simbólica de los términos de la conquista y el despojo. El cuadro aparece no solo como una glorificación de la figura de Roca sino que, en relación con la celebración de 1492, plantea implícitamente la campaña de exterminio como culminación de la conquista de América. Todos los elementos de la composición responden a esta idea, desplegados con nitidez y precisión significativa. La escena se desarrolla en un amanecer en el que una tormenta comienza a despejarse. El malón aparece equiparado a las fuerzas de la naturaleza desencadenadas (otro tópico de la literatura de frontera). \\nLos jinetes llevan cálices, incensarios y otros elementos de culto que indican que han saqueado una iglesia. Los indios aparecen, así, imbuidos de una connotación impía y demoníaca. El cielo ocupa más de la mitad de la composición, dividida por una línea de horizonte apenas interrumpida por las cabezas de los guerreros y sus lanzas. En la oscuridad de ese cielo se destaca luminosa la cruz que lleva uno de ellos y la larga lanza que empuña otro, como símbolos contrapuestos de civilización y barbarie. En la montura de dos de los jinetes se ven cabezas cortadas, en alusión a la crueldad del malón. En el extremo izquierdo se destaca del grupo un jinete que lleva una cautiva blanca semidesvanecida, apoyada sobre el hombro del raptor que se inclina sobre ella. Fue este el fragmento más comentado de la obra, a veces en tono de broma, aludiendo a su connotación erótica, o bien criticando cierta inadecuación del aspecto (demasiado “civilizado” y urbano) de la mujer y de su pose con el resto de la composición. La vuelta del malón fue llevada a la Exposición Colombina de Chicago por el oftalmólogo Pedro Lagleyze, amigo del artista, en medio de la desorganización y dificultades que rodearon ese envío oficial. \\nFue exhibida en el pabellón de manufacturas, como parte del envío argentino, junto a bolsas de cereales, lanas, cueros, etc. Los pocos comentarios que recibió se refirieron a la escena representada como una imagen de las dificultades que la Argentina había logrado superar para convertirse en una exitosa nación agroexportadora. Ángel Della Valle pintó una versión reducida de La vuelta del malón para obsequiar a Lagleyze al regreso. Conocida como “malón chico” ha sido con frecuencia tomada por un boceto. También pintó más tarde algunos fragmentos aislados de su gran tela: el grupo del guerrero y la cautiva y el indio que enarbola la cruz. Della Valle había comenzado a pintar cuadros de tema pampeano durante su estadía en Florencia. \\nEn 1887 envió a Buenos Aires varias obras, entre las que pudo verse un indio a caballo (En la pampa) y La banda lisa, que aparecen como tempranas aproximaciones al tema de La vuelta del malón. La pintura fue solicitada por el director del MNBA, Eduardo Schiaffino, a la familia del artista tras su muerte en 1903; esta optó por donarla a la Sociedad Estímulo de Bellas Artes con el cargo de su venta al MNBA a fin de instituir un premio anual de pintura denominado “Ángel Della Valle”.	\N	301	\N	\N	1892	\N	135	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. Pintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtu	\N	137	t
195	Morro da favela II (Pueblito)	morro-da-favela-ii-pueblito	Morro da favela II (Pueblito)	\N	\N	126		\N	Oleo Sobre tela 39,5 x 49,9 cm.	\N	Época de temática popular, la década del 40 también sería para Tarsila, la gran pintora modernista de los años 20, tiempo de volver definitivamente de sus tonos pau Brasil, es decir, retornar a los colores llamados caipiras (azul y rosa) de la decoración popular de los modestos caseríos de las regiones del interior del Brasil. Este colorido, aunque más intenso, estuvo presente en sus telas de los años 20, su período de máxima creatividad y producción, etapa de una gran síntesis de sus elementos compositivos. Después de 1930, cuando Tarsila se orientó, por razones culturales y políticas, hacia la temática social, su paleta se alteró igualmente.\\nPor esa época la artista regresó a San Pablo después de alternar varios años entre esa ciudad y Río de Janeiro, y pasó gran parte de su tiempo en su hacienda de Indaiatuba, en el interior paulista. \\nConociendo la inexistencia de un mercado de arte local, realizó encargos de pinturas, ilustraciones y colaboró con crónicas regulares sobre la vida cultural y memorias en Diário de São Paulo.\\nEn Pueblito, de 1945, encontramos una segunda versión de la famosa pintura Morro da favela (1924, colección João Estefano, San Pablo), tela tan admirada por el poeta Blaise Cendrars que le recomendó, en su momento, que preparase su primera exposición individual en París a partir de la serie que se iniciara con esta obra. La temática está inspirada en el viaje a Río con los modernistas de San Pablo acompañando a Cendrars en su primera visita al Brasil.\\nAlgunas variantes, sin embargo, surgen en esta tela, ahora en la colección del MNBA: sus dimensiones son menores que las de Morro da favela, la vegetación tiene modificaciones, dejando de ser visualmente presentada en formas sintéticas, en esta tela de 1945 aparecen como matas de plantas más desmenuzadas, más diversificadas en su ubicación, especialmente en el primer plano. Por otra parte, las pinceladas son bien evidentes, casi “impresionistas” en su aplicación sobre la tela, y el ave extraña de 1924 es reemplazada por la confrontación de dos pavos. Finalmente, el color es más suave en comparación con la pintura de veinte años antes.\\nLa obra del MNBA estuvo expuesta en Buenos Aires, La Plata y Montevideo en la exposición Veinte artistas brasileños presentada en 1945, con la organización del escritor Marques Rebelo y la intermediación efectiva de Emilio Pettoruti.\\nEn esta muestra Tarsila estuvo representada por otra pintura, Pueblito II (1944), y por un dibujo, estudio de 16 x 22 cm de la pintura de la colección del MNBA, hoy aparentemente perdido. La exposición dio lugar a dos pequeños libros, actualmente antológicos, uno de autoría de Cipriano Vitureira, de Montevideo, y el segundo de Jorge Romero Brest, La pintura brasileña contemporánea, publicado el mismo año de la exposición por la editorial Poseidón.	\N	\N	\N	\N	1945	\N	135	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	Época de temática popular, la década del 40 también sería para Tarsila, la gran pintora modernista de los años 20, tiempo de volver definitivamente de sus tonos pau Brasil, es decir, retornar a los colores llamados caipiras (azul y rosa) de la decoración popular de los modestos caseríos de las regiones del interior del Brasil. Este colorido, aunque más intenso, estuvo presente en sus telas de los años 20, su período de máxima creatividad y producción, etapa de una gran síntesis de sus elementos compositivos. Después de 1930, cuando Ta	\N	137	t
274	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	Sin pan y sin trabajo	\N	\N	126		\N	Oleo Sobre tela 125,5 x 216 cm.	\N	Sin pan y sin trabajo es el primer cuadro de tema obrero con intención de crítica social en el arte argentino. Desde el momento de su exhibición ha sido una pieza emblemática del arte nacional: comentado, reproducido, citado y reapropiado por sucesivas generaciones de artistas, historiadores y críticos hasta la actualidad. Fue pintado por Ernesto de la Cárcova en Buenos Aires al regreso de su viaje de estudios en Turín y Roma, donde había comenzado su ejecución antes de partir. Allí dejó al menos un boceto en obsequio a Pío Collivadino, el artista argentino que a su llegada ocupó el taller que De la Cárcova dejaba en la Via del Corso 12. \\n Había comenzado su formación europea en la Real Academia de Turín, donde fue admitido con una obra (Crisantemos) en la exposición de 1890. Luego había pasado a Roma, donde continuó su formación en los talleres de Antonio Mancini y Giacomo Grosso. Una obra suya (Cabeza de viejo) fue premiada con medalla de plata y adquirida en 1892 para la Galería Real de Turín; también obtuvo una medalla de oro en Milán en 1893 (1). Estos antecedentes hicieron que a su regreso, a los 28 años, fuera miembro del jurado del Ateneo, de modo que Sin pan y sin trabajo, celebrado como el gran acontecimiento artístico del Salón, quedó fuera de concurso. \\n El cuadro responde a un estilo naturalista y a una temática que tuvieron una importante presencia en los salones europeos de los años finales del siglo XIX: grandes pinturas resueltas en tonos sombríos que desplegaban escenas dramáticas de miseria y de los contemporáneos conflictos sociales urbanos. El espíritu crítico que sin duda alimentó aquellas composiciones naturalistas finiseculares se diluyó en los cuadros de salón, en el interés por figurar en grandes competencias con posiciones enfrentadas al arte académico más conservador. Sin embargo, Sin pan y sin trabajo no fue pintado para competir en un salón europeo: fue la obra con la que De la Cárcova se presentó al regreso en el segundo Salón del Ateneo en Buenos Aires, tras haberse afiliado al recién creado Centro Obrero Socialista (antecedente inmediato del Partido Socialista, fundado dos años después). \\n No había en Buenos Aires una tradición académica sino que el grupo de artistas del Ateneo procuraba dar sus primeros pasos. Por otra parte, a partir de la crisis de 1890, la inmensa afluencia de inmigrantes europeos que llegaban de Europa en busca de trabajo en Buenos Aires comenzaba a percibirse en forma conflictiva.	\N	306	\N	\N	1894	\N	135	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	Sin pan y sin trabajo es el primer cuadro de tema obrero con intención de crítica social en el arte argentino. Desde el momento de su exhibición ha sido una pieza emblemática del arte nacional: comentado, reproducido, citado y reapropiado por sucesivas generaciones de artistas, historiadores y críticos hasta la actualidad. Fue pintado por Ernesto de la Cárcova en Buenos Aires al regreso de su viaje de estudios en Turín y Roma, donde había comenzado su ejecución antes de partir. Allí dejó al menos un boceto en obsequio a Pío Collivadin	\N	137	t
223	La Toilette apres le bain (El arreglo después del baño)	la-toilette-apres-le-bain-el-arreglo-despu-s-del-ba-o	La Toilette apres le bain (El arreglo después del baño)	\N	\N	126		\N	66,5 x 78,5 cm.	\N		\N	318	\N	\N	1888	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100		\N	137	t
219	La berge de La Seine (Orillas del Sena)	la-berge-de-la-seine-orillas-del-sena	La berge de La Seine (Orillas del Sena)	\N	\N	126		\N	60 x 73 cm. Con marco: 84 x 97 cm.	\N	En la sesión del 17 de noviembre de 1910 los miembros del Concejo Deliberante de la ciudad de Buenos Aires habían votado  un proyecto que destacaba el rol de la Municipalidad para promover la cultura artística,  una de las vías podía ser la donación de obras de arte. Esta resolución, benefició  al MNBA que contó con presupuesto estatal para incrementar su patrimonio  con adquisiciones de obras que concretó la Comisión Nacional de Bellas Artes en la “Exposición Internacional de Arte del Centenario Buenos Aires 1910”.	\N	316	\N	\N	1891	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	En la sesión del 17 de noviembre de 1910 los miembros del Concejo Deliberante de la ciudad de Buenos Aires habían votado  un proyecto que destacaba el rol de la Municipalidad para promover la cultura artística,  una de las vías podía ser la donación de obras de arte. Esta resolución, benefició  al MNBA que contó con presupuesto estatal para incrementar su patrimonio  con adquisiciones de obras que concretó la Comisión Nacional de Bellas Artes en la “Exposición Internacional de Arte del Centenario Buenos Aires 1910”.	\N	137	t
205	Effet de neige à Louveciennes	effet-de-neige-louveciennes	Effet de neige à Louveciennes	\N	\N	126		\N	Oleo Sobre tela 50 x 61,5 cm.	\N	Donación Mercedes Santamarina, 1960.	\N	327	\N	\N	1877	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	Donación Mercedes Santamarina, 1960.	\N	137	t
197	Le bain de Vénus (El baño de Venus)	le-bain-de-v-nus-el-ba-o-de-venus	Le bain de Vénus (El baño de Venus)	\N	\N	126		\N	Oleo Sobre tabla 35 x 28 cm.	\N	El título original de esta pintura puede haber sido puesto por Redon, ya que al parecer la obra fue comprada al artista sin intermediarios. Los toques de azul y verde en la zona inferior derecha de la tela pueden considerarse una representación del agua, y el tocado de la figura puede evocar un personaje mitológico. Estos aspectos, sin embargo, son los únicos que justificarían el título de El baño de Venus. La figura no está desnuda, aparece (apenas) vestida y carece de otro atributo inherente a la diosa del amor. Su movimiento no corresponde a una escena de baño y el personaje demuestra una vacilación indigna de alguien nacido del mar. Con los dos brazos extendidos hacia delante y la cabeza hacia atrás, parece moverse de derecha a izquierda como si estuviese huyendo de algo –en una posición relativamente similar a la del dibujo Female Nude (Bridgestone Museum, Tokio)–. El origen de su miedo o resistencia parece ser la forma circular sobre la derecha (de la mitad del tamaño de la figura) que semeja más a un insecto o a uno de los monstruos submarinos pintados por Redon que a las flores y las hojas reconocibles sobre la izquierda. Esta interpretación se sustenta en el uso del negro para delinear y rellenar parte de esta forma, color asociado por Redon al mundo fantástico de sus primeras carbonillas y litografías.	\N	\N	\N	\N	1908	\N	135	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	El título original de esta pintura puede haber sido puesto por Redon, ya que al parecer la obra fue comprada al artista sin intermediarios. Los toques de azul y verde en la zona inferior derecha de la tela pueden considerarse una representación del agua, y el tocado de la figura puede evocar un personaje mitológico. Estos aspectos, sin embargo, son los únicos que justificarían el título de El baño de Venus. La figura no está desnuda, aparece (apenas) vestida y carece de otro atributo inherente a la diosa del amor. Su movimiento no cor	\N	137	t
213	En observation - M.Fabre, Officier de reserve	en-observation-m-fabre-officier-de-reserve	En observation - M.Fabre, Officier de reserve	\N	\N	126		\N	Oleo Sobre tela: 60,5 x 49,5 cm. - Marco: 76,2 x 66,2 cm.	\N		\N	\N	\N	\N	1891	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100		\N	137	t
227	Portrait d Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	portrait-d-ernest-hosched-et-sa-fille-marthe-retrato-del-se-or-hosched-y-su-hija	Portrait d Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	\N	\N	126		\N	66,5 x 78,5 cm.	\N	​El retrato de Ernest Hoschedé y su hija es una pintura de plein air, resuelta en amplias pinceladas visibles de color, en apariencia inconclusa, que el artista habría comenzado en ocasión de una visita al Château de Rottembourg en 1876, la residencia de la familia Hoschedé en Montgeron, según una carta de Manet a su discípula Eva Gonzalès publicada por Moreau-Nélaton. En ese mismo año Hoschedé encargó a Claude Monet unos paneles decorativos para aquella residencia (1). En los primeros años de la década de 1870, a partir de su vínculo con el grupo de los jóvenes pintores impresionistas, y sobre todo con Claude Monet, Manet había comenzado a pintar y exponer grandes pinturas de plein air (en 1874 expone Chemin de fer en el Salón), en las que, como en el retrato de Hoschedé y su hija, las figuras ocupan buena parte de la composición. Duret afirma que para sostener su manière personal frente a sus amigos impresionistas, Manet casi nunca realizó paisajes “puros” sino que en sus obras de escenas al aire libre continuó pintando figuras humanas, alrededor de las cuales el paisaje se ubicaba como fondo de la escena. 	\N	\N	\N	\N	1888	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	​El retrato de Ernest Hoschedé y su hija es una pintura de plein air, resuelta en amplias pinceladas visibles de color, en apariencia inconclusa, que el artista habría comenzado en ocasión de una visita al Château de Rottembourg en 1876, la residencia de la familia Hoschedé en Montgeron, según una carta de Manet a su discípula Eva Gonzalès publicada por Moreau-Nélaton. En ese mismo año Hoschedé encargó a Claude Monet unos paneles decorativos para aquella residencia (1). En los primeros años de la década de 1870, a partir de su vínculo	\N	137	t
225	Le Moulin de la Galette	le-moulin-de-la-galette	Le Moulin de la Galette	\N	\N	126		\N	61 x 50 cm.	\N	Fueron condiciones muy particulares, podría decirse de transición, las que llevaron a Vincent van Gogh a pintar este Moulin de la Galette, obra que se inscribe en una importante serie de vistas de París. El artista holandés llegó a la capital francesa en marzo de 1886. Allí se encontró –sin haberle siquiera avisado con anterioridad– con su hermano Theo, que llevaba siete años instalado en la ciudad y dirigía por cuenta de Boussod et Valadon una pequeña galería en el boulevard Montmartre.Cuando Van Gogh pinta su Moulin, está como deslumbrado por el contexto artístico circundante, de una infrecuente densidad: el manifiesto simbolista de Moréas, la última exposición impresionista (donde se presentó La Grande Jatte de Seurat), la publicación de las Illuminations de Rimbaud y de L’oeuvre de Zola se agolpan en el escenario cultural. Descubre además los encantos de la ciudad, las galerías y las discusiones animadas en los cafés. También las obras del Louvre, museo que visita con frecuencia. Para coronar esas experiencias, suma al frenesí un toque académico incorporándose, para afirmar sus cualidades técnicas, como alumno del taller del muy clásico Cormon. Allí alterna con Toulouse-Lautrec y Anquetin.Es sabido que Van Gogh llevó adelante su carrera con una determinación tan humilde como profunda, y sin duda esos meses parisinos movilizaron en él un poderoso deseo de crear. La Butte Montmartre formaba parte de su vida diaria, ya que se alojaba en la casa de Theo, que vivía en ese barrio. Los lazos que unían a los dos hermanos eran por cierto muy fuertes, pero en el otoño de 1886 la promiscuidad del nº 54 de la rue Lepic –desde donde el panorama de la ciudad era magnífico– comenzó a volverse pesada. Esto llevó a Vincent a reemplazar la naturaleza muerta, que podía realizar en el departamento, por el paisaje. Lo cual lo impulsó, en un primer momento, a representar los alrededores inmediatos, y por ende Montmartre (1).Por “Moulin de la Galette” se entiende el café-concert que se extendía en realidad entre dos molinos de Montmartre: el Blute-Fin y el Radet. En la muy célebre composición de Renoir titulada Le bal du Moulin de la Galette (1876, Musée d’Orsay, París), asistimos a las fiestas y los bailes que acompasaban la vida del lugar. Pero no fue el molino de viento como tal lo que había interesado entonces al pintor impresionista. Van Gogh, en cambio, adoptó una actitud totalmente distinta. Se concentró en este caso en uno de los dos edificios afectados por el café-concert: el Blute-Fin, antigua construcción en madera erigida en 1622 y que servía sobre todo para moler trigo.El punto de vista adoptado para el molino –la parte posterior del edificio– no tenía nada de original y lo elegían por la misma época montones de pintores (que saturaban el barrio de Montmartre). Se sabe sin embargo que Van Gogh ensayó en torno de su motivo varias otras vistas para circunscribirlo mejor. Es asombrosa aquí la claridad, la frescura, incluso, del cuadro, dominado por pinceladas vivas de azul que van virando al blanco, en tonalidades muy homogéneas. La perspectiva desde abajo adoptada por el pintor genera una línea del horizonte baja que deja estallar ese gran cielo luminoso. Van Gogh, que en abril de 1885 había trabajado con fervor en sus oscuros Mangeurs de pommes de terre, parece de pronto exultar al contacto con el ambiente parisino. Es verdad que ya había comenzado a aclarar su paleta en Anvers bajo la influencia de los cuadros de Rubens, pero Montmartre le inspira sobre todo, en el tratamiento de la atmósfera, una manera mucho más liviana. Se comprende, pues, lo que escribió a uno de sus amigos artistas (H. M. Levens) en 1887: “No hay otra cosa que París, y por difícil que la vida pueda ser aquí, aunque fuera peor y más dura, el aire francés limpia el cerebro y hace bien, muchísimo bien” (2). La bandera tricolor flameando al viento, representada en unas pocas pinceladas nerviosas, traduce perfectamente, por otro lado, esa plenitud triunfal en las tierras de Francia.Observemos, asimismo, que Van Gogh eligió una vista que no permite adivinar en nada las diversiones del Moulin de la Galette. Hay en él un interés pintoresco por el lugar, pero también una voluntad de mostrar un espacio de trabajo en el límite entre la ciudad y el campo, en lo que era todavía, en esa época, un barrio periférico de París, poblado de gente modesta. La pareja de personajes abajo a la derecha, además de indicar la escala, está vestida de manera humilde y casi campesina. Van Gogh desliza en su obra una dimensión social que lo conmueve particularmente.Podríamos afirmar, entonces, que este óleo es un excelente testimonio de la euforia del pintor holandés que recorre París y a la vez un ejemplo típico de tableau-laboratoire. Van Gogh experimenta en él serenamente sus conceptos plásticos, que encontrarían su realización absoluta unos meses más tarde, en el sur de Francia, en Provenza, región que le “limpia[ría] el cerebro” (como escribe él) con la intensidad combinada del genio y la locura.	\N	319	\N	\N	1886	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	Fueron condiciones muy particulares, podría decirse de transición, las que llevaron a Vincent van Gogh a pintar este Moulin de la Galette, obra que se inscribe en una importante serie de vistas de París. El artista holandés llegó a la capital francesa en marzo de 1886. Allí se encontró –sin haberle siquiera avisado con anterioridad– con su hermano Theo, que llevaba siete años instalado en la ciudad y dirigía por cuenta de Boussod et Valadon una pequeña galería en el boulevard Montmartre.Cuando Van Gogh pinta su Moulin, está como deslu	\N	137	t
282	Abel	abel	Abel	\N	\N	128		\N	Bronce 47 x 198 x 98 cm.	\N	Lucio Correa Morales, formado en Florencia con Urbano Lucchesi, realizó una vasta producción de retratos, esculturas funerarias, monumentos conmemorativos y obras decorativas, además de dedicarse a la enseñanza de la escultura en la Sociedad Estímulo de Bellas Artes, origen de la Academia en la Argentina.\r\nEn 1902 realizó un boceto de Abel en terracota y un segundo en bronce, conservados ambos en colecciones privadas, con la figura aislada del joven sin vida asesinado por su hermano Caín. En mayo de 1903 fue asociado por un crítico de La Nación con la escultura Abel del italiano Giovanni Duprè, un mármol de 1842, a pesar de las diferencias compositivas.\r\nLas expresiones del mismo Correa Morales nos revelan las verdaderas intenciones de su trabajo, pues era consciente de que “Nadie comprendió ni tal vez se supuso que ese ‘Abel’ no era el que fue muerto por Caín, sino el arte argentino muerto por sus hermanos. En esa figura hasta mi nombre le había disfrazado; […] yo gustoso habría firmado Moralaine, Moralini ó Moralai” (1). Este comentario permite interpretar la frase como alusión a la falta de desarrollo y de interés por el arte nacional: “Muerto sin descendencia como miserable / Es fácil sacar la conclusión / Que nosotros somos toda raza de Caín”.\r\nLa versión en yeso de Abel obtuvo la medalla de plata de Escultura en la Exposición Internacional de Saint Louis en 1904. Schiaffino, responsable del envío argentino, había reclamado al jurado una medalla de oro para Lucio Correa Morales pero su propuesta fue rechazada, al ser criticada la proporción inadecuada de los brazos de la figura (2). El reconocimiento internacional estimuló la posibilidad de su fundición en bronce, junto con otras obras premiadas como Las pecadoras de Rogelio Yrurtia.	\N	300	\N	\N	1902	\N	135	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	Lucio Correa Morales, formado en Florencia con Urbano Lucchesi, realizó una vasta producción de retratos, esculturas funerarias, monumentos conmemorativos y obras decorativas, además de dedicarse a la enseñanza de la escultura en la Sociedad Estímulo de Bellas Artes, origen de la Academia en la Argentina.\r\nEn 1902 realizó un boceto de Abel en terracota y un segundo en bronce, conservados ambos en colecciones privadas, con la figura aislada del joven sin vida asesinado por su hermano Caín. En mayo de 1903 fue asociado por un crítico de	\N	137	t
278	El despertar de la criada	el-despertar-de-la-criada	El despertar de la criada	\N	\N	126		\N		\N	Le lever de la bonne es un desnudo naturalista. Aun cuando el título y algunos elementos de la composición lo connotan, la pintura pertenece al género que a lo largo del siglo XIX fue campo de batalla de las audacias modernistas. No hay narratividad en la escena, se limita a presentar el cuerpo de una muchacha joven en el que se lee su pertenencia a la clase trabajadora. La sencillez del mobiliario, las ropas amontonadas sobre un banco de paja, al pie de la cama sin tender y, sobre todo, el título del cuadro, indican que se trata de una criada. Un foco de luz dirigida desde la izquierda ilumina ese cuerpo que se destaca con intensidad dramática sobre el fondo neutro de la pared de fondo. La piel de la muchacha es oscura, sobre todo en las zonas que el cuerpo de una mujer de trabajo se veía expuesto al sol: las manos, el rostro y las piernas. La criada aparece ensimismada en la tarea de dar vuelta una media para calzarla, de modo que el contraste entre los pechos y la mano castigada por la intemperie se hace más evidente. Cruzadas una sobre la otra, las piernas, gruesas y musculosas se destacan con un tratamiento naturalista que se detiene en la representación minuciosa de unos pies toscos y maltratados. El pubis, invisible tras la pierna cruzada, se ubica en el centro exacto de la composición. Ninguno de estos detalles pasó inadvertido a los críticos que, tanto en París como en Buenos Aires, comentaron el cuadro en 1887.\r\nFue pintado en París por Eduardo Sívori quien, tras haber logrado su aceptación en el Salón anual, lo envió a Buenos Aires ese mismo año sabiendo de antemano que su exhibición despertaría polémicas. Fue el primer gesto vanguardista en la historia del arte argentino.\r\nSívori ofreció su tela en donación a la Sociedad Estímulo de Bellas Artes, en cuya fundación él mismo había tenido un papel fundamental. La llegada del cuadro desde París, probablemente traído por Eduardo Schiaffino, fue precedida por una serie de artículos de prensa en los que la misma Sociedad Estímulo anunció que era un cuadro problemático, que sería de exhibición restringida, y que había recibido en París algunos comentarios (que fueron traducidos íntegramente) en los que se ponía en duda el buen gusto del artista al encarar un tema semejante.\r\nEn 1887 la pintura naturalista ocupaba un lugar destacado en el Salón de París, como una de las vías de renovación de la estética oficial de la Academia. Sin alejarse demasiado de las convenciones formales impuestas por la tradición (claroscuro, perspectiva, tratamiento de la superficie) los pintores naturalistas siguieron una línea de renovación iconográfica abierta a mediados de siglo por Gustave Courbet y Jean-François Millet, introduciendo temas derivados de la literatura de Émile Zola, o que planteaban una denuncia directa de los conflictos sociales contemporáneos, en un tono en general narrativo y melodramático. No fue el desnudo un género frecuente en la pintura naturalista. El cuadro de Sívori fue enseguida interpretado por la crítica francesa (Roger- Milès, Emery, E. Benjamin, Paul Gilbert, entre ellos) como obra derivada de Zola, un poco “excesivo” en la representación de un cuerpo que fue visto como feo, sucio y desagradable.\r\nEn Buenos Aires, donde no había habido hasta entonces más que pocas y discutidas exhibiciones de desnudos artísticos, el cuadro fue objeto no solo de una intensa polémica en la prensa (fue calificado de “indecente” y “pornográfico”) sino también de un importante alineamiento de intelectuales y artistas en su favor. En una reunión de su Comisión Directiva, el 22 de agosto de 1887, la Sociedad Estímulo de Bellas Artes decidió exhibir el cuadro en su local, cursar invitaciones especiales a los socios y a los periodistas de la capital, y abrir un álbum que recogiera las firmas de todos aquellos “que quieran manifestar al autor sus felicitaciones por los progresos realizados”. Más de 250 firmas de artistas, escritores, etc. se estamparon en ese álbum en cuyas páginas Sívori guardó además los recortes de las críticas recibidas y fotografías de ese y otros cuadros suyos que habían sido expuestos en el Salón de París hasta su regreso definitivo en 1891.\r\nLa fotografía de Le lever de la bonne conservada en ese álbum presenta algunas diferencias con el cuadro definitivo. No sabemos si las modificaciones fueron hechas antes o después de ser exhibido en el Salón de París. En la mesilla de noche puede verse una palangana y una jarra (elementos de higiene) en lugar del candelabro con una vela apagada de la versión final. Por otra parte, en la pared del fondo se vislumbra un estante con frascos y potes de tocador. Todos estos elementos pueden verse a simple vista cuando el cuadro se mira con una luz potente, como si el artista hubiera decidido dejar que aquellos arrepentimientos se adivinen en el fondo en penumbras. Pero lo más significativo es el cambio en la fisonomía de la criada. Su rostro y su peinado aparecen en la fotografía menos oscuros. La criada parece una faubourgienne en la versión de la fotografía. Tal vez más cercana a la apariencia de una prostituta (los elementos de higiene también contribuyen a ello), tema predilecto de la vanguardia y de la crítica social de la época. Aun modificada, la criada fue interpretada como prostituta y considerada pornográfica por varios de sus primeros comentadores. Su transformación es significativa. Tal vez el artista decidió alejarse del “tema” social de moda al presentarse al Salón. Tal vez decidió transformarla inequívocamente en una criada pobre para su exhibición en Buenos Aires.	\N	302	\N	\N	1887	\N	135	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	Le lever de la bonne es un desnudo naturalista. Aun cuando el título y algunos elementos de la composición lo connotan, la pintura pertenece al género que a lo largo del siglo XIX fue campo de batalla de las audacias modernistas. No hay narratividad en la escena, se limita a presentar el cuerpo de una muchacha joven en el que se lee su pertenencia a la clase trabajadora. La sencillez del mobiliario, las ropas amontonadas sobre un banco de paja, al pie de la cama sin tender y, sobre todo, el título del cuadro, indican que se trata de u	\N	137	t
276	La vuelta al hogar	la-vuelta-al-hogar	La vuelta al hogar	\N	\N	126	Adqusición Sociedad de Estímulo	\N	Oleo Sobre tela 90,5 x 118,5 cm	\N	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. Pintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtuvo una medalla (de única clase) y al regreso se exhibió nuevamente en Buenos Aires, en el segundo Salón del Ateneo en 1894. \\nFue la obra más celebrada de Della Valle. Presentaba por primera vez en las grandes dimensiones de una pintura de salón una escena que había sido un tópico central de la conquista y de la larga guerra de fronteras con las poblaciones indígenas de la pampa a lo largo del siglo XIX: el saqueo de los pueblos fronterizos, el robo de ganado, la violencia y el rapto de cautivas. En el manejo de la luz y la pincelada se advierte la huella de la formación de Della Valle en Florencia: no solo el aprendizaje con Antonio Ciseri sino también el conocimiento de los macchiaioli y los pintores del Risorgimento italiano. Algunos críticos lo vincularon con los grandes cuadros de historia del español Ulpiano Checa que se había hecho famoso por sus entradas de bárbaros en escenas de la historia de España y del imperio romano. Sin embargo, el cuadro de Della Valle entroncaba con una larga tradición no solo en las crónicas y relatos literarios inspirados en malones y cautivas, sino también en imágenes que, desde los primeros viajeros románticos europeos que recorrieron la región en la primera mitad del siglo XIX, representaron cautivas y malones. \\n En la década de 1870 Juan Manuel Blanes había realizado también algunas escenas de malones que aparecen como antecedentes de esta obra. Casi ninguna, sin embargo, había sido expuesta al público ya que tuvieron una circulación bastante restringida. La vuelta del malón fue, entonces, la primera imagen que impactó al público de Buenos Aires referida a una cuestión de fuerte valor emotivo e inequívoco significado político e ideológico. Según refiere Julio Botet, a partir de una entrevista al artista en agosto de 1892, el asunto del cuadro se inspiraba en un malón llevado por el cacique Cayutril y el capitanejo Caimán a una población no mencionada. Otro comentario (en el diario Sud-América) ubicaba el episodio en la población de 25 de Mayo. Pero más allá de la anécdota el cuadro aparece como una síntesis de los tópicos que circularon como justificación de la “campaña del desierto” de Julio A. Roca en 1879, produciendo una inversión simbólica de los términos de la conquista y el despojo. El cuadro aparece no solo como una glorificación de la figura de Roca sino que, en relación con la celebración de 1492, plantea implícitamente la campaña de exterminio como culminación de la conquista de América. Todos los elementos de la composición responden a esta idea, desplegados con nitidez y precisión significativa. La escena se desarrolla en un amanecer en el que una tormenta comienza a despejarse. El malón aparece equiparado a las fuerzas de la naturaleza desencadenadas (otro tópico de la literatura de frontera). \\nLos jinetes llevan cálices, incensarios y otros elementos de culto que indican que han saqueado una iglesia. Los indios aparecen, así, imbuidos de una connotación impía y demoníaca. El cielo ocupa más de la mitad de la composición, dividida por una línea de horizonte apenas interrumpida por las cabezas de los guerreros y sus lanzas. En la oscuridad de ese cielo se destaca luminosa la cruz que lleva uno de ellos y la larga lanza que empuña otro, como símbolos contrapuestos de civilización y barbarie. En la montura de dos de los jinetes se ven cabezas cortadas, en alusión a la crueldad del malón. En el extremo izquierdo se destaca del grupo un jinete que lleva una cautiva blanca semidesvanecida, apoyada sobre el hombro del raptor que se inclina sobre ella. Fue este el fragmento más comentado de la obra, a veces en tono de broma, aludiendo a su connotación erótica, o bien criticando cierta inadecuación del aspecto (demasiado “civilizado” y urbano) de la mujer y de su pose con el resto de la composición. La vuelta del malón fue llevada a la Exposición Colombina de Chicago por el oftalmólogo Pedro Lagleyze, amigo del artista, en medio de la desorganización y dificultades que rodearon ese envío oficial. \\nFue exhibida en el pabellón de manufacturas, como parte del envío argentino, junto a bolsas de cereales, lanas, cueros, etc. Los pocos comentarios que recibió se refirieron a la escena representada como una imagen de las dificultades que la Argentina había logrado superar para convertirse en una exitosa nación agroexportadora. Ángel Della Valle pintó una versión reducida de La vuelta del malón para obsequiar a Lagleyze al regreso. Conocida como “malón chico” ha sido con frecuencia tomada por un boceto. También pintó más tarde algunos fragmentos aislados de su gran tela: el grupo del guerrero y la cautiva y el indio que enarbola la cruz. Della Valle había comenzado a pintar cuadros de tema pampeano durante su estadía en Florencia. \\nEn 1887 envió a Buenos Aires varias obras, entre las que pudo verse un indio a caballo (En la pampa) y La banda lisa, que aparecen como tempranas aproximaciones al tema de La vuelta del malón. La pintura fue solicitada por el director del MNBA, Eduardo Schiaffino, a la familia del artista tras su muerte en 1903; esta optó por donarla a la Sociedad Estímulo de Bellas Artes con el cargo de su venta al MNBA a fin de instituir un premio anual de pintura denominado “Ángel Della Valle”.	\N	304	\N	\N	1885	\N	135	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. Pintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtu	\N	137	t
280	Reposo	reposo	Reposo	\N	\N	126	Adquirido a Schiaffino, Eduardo por Ministerio de Justicia e Instrucción Pública	\N	109 x 200 cm.	\N	​Reposo fue pintado por Eduardo Schiaffino en París y admitido en la Exposición Universal con la que se celebró el centenario de la Revolución Francesa, donde obtuvo una medalla de tercera clase (bronce). Su autor fue el fundador y primer director del Museo Nacional de Bellas Artes, además de activo promotor de las bellas artes en Buenos Aires y primer historiador del arte argentino. En su libro La pintura y la escultura en Argentina (1933) comentó su propia obra en tercera persona, destacando la importancia de aquella medalla (solo el escultor Manuel de Santa Coloma había sido premiado en París 25 años antes que él y otros 25 años habrían de pasar antes de que otro argentino, Antonio Alice, volviera a ser premiado allí).\r\nEl cuadro presenta un desnudo de espaldas, en una pose algo forzada, con las piernas extendidas y cruzadas y un brazo arqueado sobre la cabeza. El cuerpo aparece pálido y azulado en medio de un espacio enteramente azul, en el que el paño sobre el que se encuentra tendido y el fondo tienen apenas diferencias de valor. Esta ambigüedad espacial se ve interrumpida solamente por un pequeño fragmento de piso, sobre el que se advierte el borde del paño (con brillos de terciopelo), en el ángulo inferior izquierdo de la tela. Hay también cierta ambigüedad de género en ese cuerpo casi adolescente, con el cabello muy corto y la cara oculta, en una posición que aparece como una reelaboración del Hermafrodito durmiente, el mármol helenístico restaurado por Bernini, del Museo del Louvre. Es tal vez el primer cuadro de inspiración simbolista pintado por Schiaffino, quien hasta entonces había exhibido algunas “impresiones” de paisaje y escenas de toilette más cercanas al estilo y la iconografía impresionistas. Jaime de la María comentó el cuadro para La Nación (2 de julio de 1890) atribuyéndole un estilo análogo al del maestro de Schiaffino en París, Pierre Puvis de Chavannes: “la analogía de carácter podrá parecer casual, pero la de estilo se explica: Schiaffino es puvisiste”. La atmósfera simbolista de Reposo adquiere un carácter más marcado en varias obras posteriores del artista, como Vesper (inv. 5377, MNBA), Craintive o Margot, tres retratos exhibidos al año siguiente en la exposición de la Sociedad de Damas de Nuestra Señora del Carmen, o el Desnudo (sinfonía en rojo) (inv. 7463, MNBA) que expuso en el Salón del Ateneo de 1895. Margot (inv. 1656, MNBA) fue celebrada en varios artículos como la primera obra en la que se revelaba su calidad como artista. Pero la polémica que entabló con el crítico que firmaba A.Zul de Prusia a propósito de la supuesta autoría de las obras de los pensionados en Europa, terminó ese año con un duelo a pistola.\r\nSchiaffino había viajado trayendo Reposo a Buenos Aires en medio de la crisis de 1890, con el objeto de exhibirlo y gestionar la renovación de su beca de estudios. Tanto la exposición de la pintura en la vidriera de la casa Bossi (donde el año anterior su primer envío desde París había sido comentado con franca hostilidad) como el otorgamiento de la beca, recibieron comentarios muy negativos en la prensa. Un comentarista anónimo del diario La Argentina (1 de julio de 1890) lo objetó por encontrarlo deforme e indecente, “con una sans-façon que huele a la legua al Paganismo y a sus más florecientes saturnales”. El artista encaró personalmente la defensa de su cuadro, que volvió a exponer en el primer Salón del Ateneo, en 1893, y nuevamente en el cuarto Salón del Ateneo en 1896, recogiendo en ambas ocasiones severas críticas, no solo hacia la pintura sino también al hecho de volver a incluirla en los salones cuando era una obra que no era nueva y que ya había sido expuesta y criticada con dureza. Es posible advertir en el gesto de Schiaffino una posición desafiante en consonancia con su tenaz actividad orientada –en sus propias palabras– a “educar el gusto” del público de Buenos Aires, introduciendo audacias del arte moderno en un género que a lo largo del siglo XIX se había tornado emblemático de aquellas. En Buenos Aires todavía resonaba el escándalo que había suscitado, en 1887, Le lever de la bonne de Eduardo Sívori.	\N	305	\N	\N	1889	\N	135	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	​Reposo fue pintado por Eduardo Schiaffino en París y admitido en la Exposición Universal con la que se celebró el centenario de la Revolución Francesa, donde obtuvo una medalla de tercera clase (bronce). Su autor fue el fundador y primer director del Museo Nacional de Bellas Artes, además de activo promotor de las bellas artes en Buenos Aires y primer historiador del arte argentino. En su libro La pintura y la escultura en Argentina (1933) comentó su propia obra en tercera persona, destacando la importancia de aquella medalla (solo e	\N	137	t
404	En la costa de Valencia	en-la-costa-de-valencia	En la costa de Valencia	\N	\N	126		\N	Oleo sobre tela 57 x 88,5 cm.	\N	Esta escena costumbrista que representa el trabajo de los pescadores que vuelven de la faena y el juego de un niño se desarrolla posiblemente en la playa del Cabañal, frecuentada por Sorolla, que se caracterizaba por conjugar, en torno al 1900, tanto a pescadores y sus familias como a pintores en busca de temáticas que mostraran el ser regional. Esta combinación de los trabajos preindustriales de las clases populares, por una parte, y de pintura pleinairista de visos nacionalistas, por otra, brindaba una imagen optimista del Levante español distante del duro proceso de transformación del paisaje de la región por la industrialización. La mecanización y el gigantismo urbano eran la nueva fisonomía que modelaba la vida y las costumbres del Levante. Sin embargo, la actitud de huida nostálgica de la civilización hacia la rústica naturaleza de pintores como Sorolla hacía de estas costas un refugio bucólico frente a las consecuencias generadas por la Revolución Industrial.\\nDetrás de este costumbrismo de ambiente marino se encuentra la verdadera raison d’être de las búsquedas de Sorolla: la luz mediterránea. Su sensibilidad lumínica pertenece a una de las dos tendencias de la pintura española que marcaron la vuelta del siglo: la “España blanca” contrapuesta a la “España negra” de Zuloaga, Solanas y Romero de Torres, entre otros. La luz, la gran protagonista de esta pintura, es captada con rapidez y al plein air del mediodía dando como resultado una obra definida por la instantaneidad, el abocetamiento y una atmósfera vibrante producto de los reflejos del sol enceguecedor en su cenit sobre el mar.\\nSorolla ocupa un lugar especial dentro del movimiento luminista cuyos centros fueron Valencia y Sitges. A diferencia del resto de los luministas, reunió en sí influencias que supo dosificar, como la del impresionismo y la de los pintores escandinavos como Peder Severin Krøyer (1851-1909), Viggo Johansen (1851-1935) y Anders Zorn (1860-1920) o del alemán Adolph von Menzel (1815-1905), cuya obra conoció en París. En el debate de tradición-modernidad, su pintura no solo tenía la capacidad de resolver el dilema entre el conocimiento académico y la experimentación impresionista, equilibrando ambas posiciones, sino que también lograba mantener un juste milieu artístico (en un momento de vanguardias contestatarias y actitudes reaccionarias hacia ellas) que le brindó un éxito comercial internacional sin precedente en los otros luministas. Todo esto lleva a la historiografía artística a considerarlo como el pintor que clausura el movimiento luminista levantino.\\nLa captación instantánea y lumínica de las formas, próxima al impresionismo parisino pero ajena a sus inquietudes, es una constante en los pintores nacidos en el Levante español y activos a finales del siglo XIX y principios del siguiente, de quienes el estilo sorollesco se nutre. Estos, a pesar de su formación conservadora, se atrevieron a incursionar en técnicas inusuales y de gran modernidad en relación con las normas académicas que imponían el gusto por lo bituminoso, provocando un cambio sustancial en la manera de pintar. Al focalizarse en la presencia dominante del mar en el que la vista se pierde al infinito, en su atmósfera costera y en la sugestión hipnótica de la luz y sus matices, propusieron al espectador involucrarse en una percepción de la realidad que es anterior a una primera estructuración gestáltica, es decir una percepción en la cual las sensaciones anteceden a las formas. El luminismo levantino se inserta en una tendencia al aclaramiento de la paleta que tiene lugar en la pintura europea del siglo XIX, evidencia de búsquedas en relación con el color y con la luz generadas a partir del desarrollo de la pintura de paisaje y de la captación de efectos naturalistas.\\nSorolla creó una “maniera” posible de ser emulada a nivel compositivo y “digerible” temáticamente a pesar de su modernidad. Su técnica lumínica se oponía diametralmente a la paleta armada del frío academicismo que había entronizado a la pintura de historia con sus composiciones artificiosas. Sin embargo, la organización deliberada de Sorolla de masas de luz y color lo acercaban al pensamiento académico que gustaba de componer fuertemente el lienzo equilibrando las formas y los colores. La adhesión, en parte, a la técnica impresionista no debe confundirse con la adopción del descubrimiento de la impresión lumínica mediante la yuxtaposición de colores puros colocando en un plano secundario la estructura, ni tampoco con la disolución de la forma –divergencias con el impresionismo que lo unían al resto de los luministas– aunque su factura suele oscilar hacia una pincelada de coma impresionista. Estas características técnicas se observan en esta obra, como así también en La vuelta de la pesca (1898, inv. 2007, MNBA), ambas dotadas de una infraestructura muy compleja. Cierta impresión y velocidad de la poética de Sorolla nunca pudieron ser emuladas acabadamente por sus seguidores.\\nEn cuanto a la temática, el artista impuso la descripción de una edad de oro del Mediterráneo, una Arcadia donde nos presenta hombres en simple y dulce armonía con la naturaleza. El gesto de los pescadores y el pequeño jugando representan lo que fluye y es efímero. Aun así, estos instantes evocan la eternidad de los tipos humanos de la región.	\N	416	\N	\N	1898	\N	135	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	Esta escena costumbrista que representa el trabajo de los pescadores que vuelven de la faena y el juego de un niño se desarrolla posiblemente en la playa del Cabañal, frecuentada por Sorolla, que se caracterizaba por conjugar, en torno al 1900, tanto a pescadores y sus familias como a pintores en busca de temáticas que mostraran el ser regional. Esta combinación de los trabajos preindustriales de las clases populares, por una parte, y de pintura pleinairista de visos nacionalistas, por otra, brindaba una imagen optimista del Levante e	\N	137	t
229	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	danseuse-et-admirateur-derri-re-la-sc-ne-bailarina-y-admirador-tras-la-escena	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	\N	\N	126		\N	60,5 x 73,5 cm.	\N	​ fines del siglo XIX París se perfilaba como la ciudad elegida por la burguesía para desplegar su hedonismo. Los sitios preferidos para hacerlo eran los cafés, la Ópera, los jardines suburbanos, el hipódromo o la ribera. Todos ellos quedaron registrados en las pinturas de los impresionistas o post-impresionistas, que examinaron en sus obras las distintas facetas de la vida moderna, tanto para criticarla, como para adularla. Jean-Louis Forain fue uno de estos artistas, de familia modesta, hijo de un “pintor de brocha gorda”, que inició sus estudios en el atelier del pintor Jean-Léon Gérôme, para luego estudiar con Jean-Baptiste Carpeaux quien lo introdujo en la representación de escenas bíblicas en un sentido moderno, temática importante en su desarrollo artístico posterior. También fue André Gill quien en 1870 le enseñó los secretos del arte de la ilustración, que utilizó para representar escenas de la vida cotidiana.No escapó al influjo de Manet, Renoir, Degas, quienes lo acercaron a las teorías del color y de la luz, tan experimentadas por el impresionismo. Sin embargo, con quien estableció mayor afinidad, tanto a nivel artístico como humano, fue con Degas. Tan es así que en varias ocasiones pintaron juntos trabajando sobre un mismo modelo. Bailarinas, carreras de caballos, desnudos, escenas de café, se repitieron en sus obras, pero abordadas desde una perspectiva diferente; mientras que Degas puso el acento en la captura de un instante, de un momento particular, cercano a un lenguaje fotográfico, Forain se concentró en lo gestual, en la fibra expresiva de los personajes, en los caracteres que transmiten su condición social (1).Uno de los temas que ambos comparten es el backstage de la Ópera. En Bailarina y admirador tras la escena, Forain pone de relieve la particular relación, ambigua y muchas veces dolorosa, que se gestaba entre las bailarinas y los abonados a la Ópera. Estos últimos representaban una elite política, financiera y cultural, que llegaba a reservar casi la mitad de las localidades del teatro. Varias miembros del cuerpo de ballet provenían de familias pobres y solían aceptar un soporte económico a cambio de favores sexuales; otras establecían una amistad con “el protector” y solo unas pocas se casaban con ellos. El foyer, la sala de ensayo o calentamiento, oficiaba durante los intervalos como lugar de encuentro con los abonados. En este sitio transcurre la escena en cuestión, “el admirador”, que despliega su condición social a partir de los detalles de su atuendo elegante, tiene la mirada perdida en un punto fijo en el piso, y se refriega sus manos con guantes blancos. La bailarina sentada a su lado apenas gira su cabeza para observarlo, dejando caer al piso, desganadamente, el arco de violín que sostiene en sus manos. Se puede sobreentender un vínculo en las condiciones antes relatadas, a pesar de que ellos casi no interactúan. En el extremo opuesto de la composición y frente al decorado del escenario, un grupo de bailarinas cuchichea, tal vez adivinando aquello que el admirador no se anima a decirle a la mujer deseada. La resolución pictórica de la escena tiene muchas filiaciones con el tipo de factura abocetada y rápida de las obras contemporáneas de Degas, en las que se desdibujan los detalles a medida que se alejan del primer plano.	\N	315	\N	\N	1887	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	​ fines del siglo XIX París se perfilaba como la ciudad elegida por la burguesía para desplegar su hedonismo. Los sitios preferidos para hacerlo eran los cafés, la Ópera, los jardines suburbanos, el hipódromo o la ribera. Todos ellos quedaron registrados en las pinturas de los impresionistas o post-impresionistas, que examinaron en sus obras las distintas facetas de la vida moderna, tanto para criticarla, como para adularla. Jean-Louis Forain fue uno de estos artistas, de familia modesta, hijo de un “pintor de brocha gorda”, que inici	\N	137	t
402	La Emperatriz Theodora	la-emperatriz-theodora	La Emperatriz Theodora	\N	\N	126		\N	Oleo sobre tela 224,5 x 125,5 cm. - Marco: 226,5 x 127,5 cm.	\N	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, representa una visión orientalista y teatral de la emperatriz bizantina Teodora, esposa de Justiniano I,  la mujer más influyente y poderosa en la historia del Imperio romano de Oriente.\\n No es una representación histórica, sino más bien una fantasía opulenta basada en la fascinación europea del siglo XIX por el Oriente y el pasado imperial. Benjamin-Constant era un pintor de gran técnica especializado en retratos de élite y figuras históricas: poder, lujo,  sofisticación, poses calculadas,  texturas complejas como seda y terciopelo, y entornos refinados. \\n De origen humilde, Theodora se ganó la vida de joven como actriz y cortesana hasta que fue elegida por Justiniano como esposa. La imagen histórica -basada en Procopio de Cesarea en la antigüedad  y Gibbon en el siglo XVIII-  es  la de una mujer astuta, inteligente y cruel. En la actualidad se considera que ese perfil es exagerado por la visión rígida que se tenía de la función social de la mujer. La visión moderna es que fue una figura política central en el Imperio Bizantino,  co-gobernante de facto con Justiniano. Participó activamente en las decisiones de gobierno. En la revuelta de Niká de 532, convenció a Justiniano de no huir y enfrentar a sus enemigos con su famosa frase -“el púrpura es una buena mortaja”-. Promovió leyes en favor de los derechos de las mujeres: prohibió el tráfico sexual forzado,  fortaleció los derechos de las mujeres en el matrimonio y el divorcio, y fundó hogares para mujeres rescatadas de la prostitución, algo sin precedentes en la época. Como resultado de los esfuerzos de Teodora, el estado de la mujer en el Imperio Bizantino fue más elevado que el del resto de las mujeres en Europa.\\nEn la historia argentina el paralelismo inmediato es con Eva Perón.\\nBenjamin-Constant se propuso crear un ícono visual del poder femenino y del exotismo oriental. y lo que salió, quizás involuntariamente,  es todo un símbolo de modernidad: una mujer joven, poderosa, hermosa e inteligente con mirada desafiante que se atreve a ser distinta.	\N	417	\N	\N	1887	\N	135	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, representa una visión orientalista y teatral de la emperatriz bizantina Teodora, esposa de Justiniano I,  la mujer más influyente y poderosa en la historia del Imperio romano de Oriente.  .	\N	137	t
217	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	portrait-de-suzanne-valadon-madame-suzanne-valadon-artiste-peintre-retrato-de-suzanne-valadon-pintora	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	\N	\N	126		\N	 55 x 46 cm. - Marco: 77,5 x 66,5 x 6,5 cm.	\N	En 1885, con apenas veintiún años, en busca de independencia y deseoso de escapar del severo control paterno, Henri de Toulouse-Lautrec (1) abandonó el sur de Francia para ir a París. Se estableció en el barrio de Montmartre (2) con su amigo René Grenier. La integración en la vida de la butte no fue simple; contribuyeron a su inserción sus amigos, entre ellos François Gauzi, compañero de estudios en el atelier de Ferdinand Cormon.En estrecho contacto con Van Gogh, Vallotton, Bonnard, Lautrec participó plenamente del clima artístico parisino, que en ese tiempo buscaba en varios sentidos la superación del impresionismo, y orientó sus indagaciones hacia una pintura adherida a la realidad que, a través de la estilización formal, fijara los tipos psicológicamente característicos. Participante desencantado del ambiente de Montmartre –cafés-concert, prostíbulos, salas de baile, teatros–, en sus cuadros y en los numerosos bocetos tomados de la realidad Lautrec trazó toda la intimidad y la tristeza de ese submundo humano. Fundamental en ese ambiente fue el encuentro con una mujer que, antes que una seguidilla de modelos y amantes, entró en su vida y en su obra. Se trata de Marie-Clémentine Valade (3), más famosa por el nombre de Suzanne Valadon, que prestó sus rasgos a algunos de los “tipos femeninos” más conocidos de Lautrec (4). Marie-Clémentine era una jovencita sin medios materiales ni culturales, nacida en 1865, de madre modista y padre desconocido. Se aventuró en la actividad circense como acróbata, pero una caída la obligó a desistir; probó otros trabajos humildes hasta que decidió ofrecerse como modelo artística. Comenzó a ser solicitada por los mejores pintores de la época, el primero de todos Puvis de Chavannes, que fue durante cierto período también su amante. Siguieron Renoir, Manet, Gauguin; a todos regaló su belleza, de todos tomó algo. Apasionada por el dibujo, durante las sesiones de pose la joven modelo observaba a los maestros trabajando; muy pronto todos, particularmente Lautrec (5), la alentaron a seguir su pasión. Fue él quien le sugirió el nombre artístico de Suzanne Valadon, porque como la Susana bíblica, Marie estaba rodeada de “viejos ávidos”. Con este retrato titulado Madame Valadon, artiste peintre, Lautrec ratifica el rol de pintora que había asumido Marie (6).Toulouse-Lautrec solía pasar del taller al aire libre. Sus pinturas a cielo abierto constituyeron una etapa que le permitió adaptar estilística y temáticamente ciertos valores adquiridos de la pintura en plein air, no tanto en la línea de los estudios lumínicos de los impresionistas sino en una búsqueda de mayor libertad. En esta pintura Suzanne está sentada, representada frontalmente, por delante de un paisaje otoñal. Se trata muy probablemente del jardín del viejo Forest, un terreno dedicado al tiro con arco, situado en la esquina del boulevard de Clichy y la rue Caulaincourt, a pocos pasos del atelier del artista, donde Lautrec realizó distintos retratos femeninos (Justine Dieuhl, 1891, Musée d’Orsay, París). Su cuerpo está delimitado por un contorno negro bien marcado –reminiscencia del ejemplo de Degas, por quien Lautrec sentía real veneración– dentro del cual, sin embargo, los volúmenes parecen llenados sumariamente por amplios trazos de color, cuya porosidad es exaltada por la textura de la tela sin preparación. El rostro de la modelo se recorta contra un conjunto de planos cromáticos que han perdido la rigidez y el rigor del contemporáneo Retrato de Jeanne Wenz (1886, The Art Institute of Chicago), testimoniando ya la gran maestría y la verdadera ductilidad de pincelada adquirida por el artista. El fondo, una armonía de amarillos, beiges y marrones, confundidos en toques diluidos, arroja una suerte de velo azulado sobre el cuerpo de la mujer, suavizando la expresión firme del personaje. El retrato femenino suele ser un compromiso entre la elegancia y el realismo a partir de la observación directa. Los artistas mundanos tienen como única preocupación realzar la belleza y la condición social de la modelo. Un artista en boga en la época, como por ejemplo Giovanni Boldini, daba a sus modelos un aspecto lo más semejante y halagador posible. Lautrec, en cambio, más lúcido, como Van Gogh, fue a lo esencial gracias a una manera descriptiva sobria y directa, escapando a la tentación de “embellecer”. El hecho de disponer de una renta familiar le permitía eludir las obligaciones de los retratos “alimentarios” –los realizados para vivir todo el mes– y seguir solamente su fantasía; son raros, de hecho, los retratos realizados por el artista en razón de algún encargo célebre (por ejemplo, Madame de Gortzikoff ) (7). La obra de Buenos Aires debe relacionarse con un retrato de asunto análogo, conservado en la Ny Carlsberg Glyptotek de Copenhague (1886-1887). La modelo posó además para la famosa tela La buveuse o La gueule de bois (1889, Harvard Fogg Art Museum, Cambridge; el dibujo, de 1887-1888, se conserva en el Musée Toulouse-Lautrec, Albi), escena social de depravación y miseria (8).\r\n Barbara Musetti\r\n1— Sobre la vida de Lautrec, cf. U. Felbinger, Henri de Toulouse-Lautrec: sa vie et son oeuvre. Köln, Könemann, 2000; A. Simon, Toulouse-Lautrec: biographie. Tournai, La Renaissance du Livre, 1998. 2— Sobre la influencia de Montmartre en la obra de Lautrec, cf. R. Thomson; P. D. Cate y M. Weaver Chapin (dir.), Toulouse-Lautrec and Montmartre, cat. exp. Washington, National Gallery of Art, 2005; F. Maubert, Le Paris de Lautrec. Paris, Assouline, 2004; P. Vabanne, Henri de Toulouse-Lautrec: le peintre de la vie moderne. Paris, Terrail, 2003. 3— Cf. T. Diamand Rosinsk, Suzanne Valadon. Paris, Flammarion, 2005. 4— El retrato femenino figura entre los temas más recurrentes de la obra de Lautrec. Sobre este asunto, véase: Le donne di Toulouse-Lautrec, cat. exp. Milano, Mazzotta, 2001. 5— Degas, que fue mentor y amigo de Valadon y uno de los primeros en adquirir sus dibujos, los calificó de “malos y blandos” y definió a la artista como “la terrible Marie”. 6— Sobre la actividad artística de Valadon, madre del pintor Maurice Utrillo, véase: M. Restellini (dir.), Valadon-Utrillo: au tournant du siècle à Montmartre: de l’impressionnisme à l’École de Paris, cat. exp. Paris, Pinacothèque de Paris, 2009; Alexandra Charvier et al., Utrillo, Valadon, Utter: 12 rue Cortot: un atelier, trois artistes. Sannois, Musée Utrillo-Valadon, 2008. 7— Toulouse Lautrec, cat. exp. Paris, Réunion des musées nationaux, 1992, p. 133.8— Para un recorrido por toda la obra de Toulouse-Lautrec, cf. M. G. Dortu, Toulouse-Lautrec et son oeuvre. New York, Collectors Editions, 1971, vol. 1-4.	\N	323	\N	\N	1885	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	En 1885, con apenas veintiún años, en busca de independencia y deseoso de escapar del severo control paterno, Henri de Toulouse-Lautrec (1) abandonó el sur de Francia para ir a París. Se estableció en el barrio de Montmartre (2) con su amigo René Grenier. La integración en la vida de la butte no fue simple; contribuyeron a su inserción sus amigos, entre ellos François Gauzi, compañero de estudios en el atelier de Ferdinand Cormon.En estrecho contacto con Van Gogh, Vallotton, Bonnard, Lautrec participó plenamente del clima artístico pa	\N	137	t
450	Apocalipsis	apocalipsis	Apocalipsis	\N	\N	126		\N	Gouache sobre papel. 75 x 55 cm.	\N	Según Dante Alighieri, el infierno es un cono invertido, dividido en nueve círculos que se estrechan a medida que descendemos. Para acceder a él hay que cruzar el río Aqueronte, guiados por Caronte, el barquero. Ningún viajero ha regresado para confirmar esa travesía. Por eso, Gárgano nos propone su propia versión del infierno, construido pacientemente sobre el papel a lo largo de muchos años. En la trama de líneas, el artista ofrece sus indicios, y nos corresponde a nosotros cruzar el río e interpretar aquello que vemos.	\N	465	\N	\N	2017	\N	135	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	\N	\N	137	t
448	Sendas perdidas	sendas-perdidas	Sendas perdidas	\N	\N	126		\N	Carbonilla y temple sobre tela. 135 x 165 cm.	\N	Como lo han hecho artistas de todas las épocas, Gárgano nos propone su propia versión del infierno, construido pacientemente sobre el papel a lo largo de muchos años. \r\nSus trabajos no se alimentan directamente de la realidad: crean una realidad propia, que se nutre de otras imágenes. Cada obra está hecha de otras obras, y por eso mismo conforma un mundo.	\N	463	\N	\N	2015	\N	135	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	\N	\N	137	f
482	Utopia del Sur	utopia-del-sur	Utopia del Sur	\N	\N	126		\N	Oleo sobre tela 180 x 190 cm.	\N	García Uriburu le dedicó vida y obra: una proclama urgente por el cuidado del medio ambiente, “La globalización ha ligado salvajemente nuestras economías, creando una cruel dependencia que ha dividido aún más a la población mundial. Los países desarrollados contaminan el agua con fluidos tóxicos, derraman petróleo en nuestros mares y ríos, sin reparar el daño que ocasionan. Hace más de cuarenta años que intento dar una alarma contra la contaminación de ríos y mares, y es a través de mis acciones artísticas en distintos puntos del planeta que he transformado mi obra en una suerte de alerta contestataria globalizadora. Hoy y con más motivos que hace cuarenta años, sigo denunciando la contaminación del agua, y la salvaje destrucción que hacemos de las reservas del planeta. Un planeta que en nuestra ciega omnipotencia creemos inagotable e indestructible”.	\N	490	\N	\N	1993	\N	135	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100	\N	\N	137	t
484	Procesión sorprendida por la lluvia	procesi-n-sorprendida-por-la-lluvia	Procesión sorprendida por la lluvia	\N	\N	126		\N	Oleo sobre tela 63 x 102 cm. - Marco: 101,5 x 140 cm.	\N	Esta obra constituye un ejemplo sobresaliente del costumbrismo practicado por Mariano Fortuny, de gran influencia en la pintura española del último tercio del siglo XIX, aunque en este caso se aleja del tratamiento preciosista con el que alcanzó éxito y fama internacionales, al punto de haber sido considerada un boceto por la falta de atención al detalle .	\N	491	\N	\N	2017	\N	135	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100	\N	\N	137	t
209	Prairies du Valhermeil près Pontoise	prairies-du-valhermeil-pr-s-pontoise	Prairies du Valhermeil près Pontoise	\N	\N	126		\N	Oleo Sobre tela 55 x 92 cm.	\N	Donación Mercedes Santamarina, 1970.	\N	322	\N	\N	1874	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	Donación Mercedes Santamarina, 1970.	\N	137	t
215	Vahine no te miti (Femme a la mer) (Mujer del mar).	vahine-no-te-miti-femme-a-la-mer-mujer-del-mar	Vahine no te miti (Femme a la mer) (Mujer del mar).	\N	\N	126		\N	Oleo Sobre tela:92,5 x 74 cm.	\N	El MNBA tiene la suerte de poseer dos pinturas de bañistas de Gauguin, ambientada en este caso no en Francia, sino en los mares del Sur. Esta obra aparece mencionada como Estudio de espalda desnuda en una lista de pinturas realizadas durante la primera estadía en Tahití que Gauguin anotó aproximadamente en abril de 1892 en su Carnet de Tahiti (1). Fue pintada en Mataiea y se basa en un dibujo en color del mismo cuaderno de bocetos, probablemente realizado con modelo y con una cuadrícula apenas visible, útil al momento de repetir la composición en mayor escala.Cuando fue expuesta en París en 1893, Thadée Natanson describió concisamente su tema: “de esta, sentada en la arena, solamente se puede ver la espalda bronceada en medio de flores casi simétricas que la espuma borda sobre las olas” (2). La metáfora que vincula la cresta de las olas, las flores y el bordado destaca acertadamente el uso que hace Gauguin de formas polisémicas y la calidad ornamental de este motivo. Las “flores” de espuma también están relacionadas por su forma y su color con la conchilla que está en la playa y con las flores que forman parte del motivo del pareo sobre la rodilla derecha de la mujer. En contraste con otras pinturas de Gauguin que muestran una mujer tahitiana parcialmente vestida, este pareo no tiene ni espesor ni pliegues propios y está pintado sobre la pierna a la manera de los paños en una pintura manierista o de un tatuaje –realmente un “bordado” sobre la piel y la vestimenta original de muchos habitantes de los mares del Sur–. La cresta de las olas similar a una flor ya había sido utilizada por Gauguin en Bretaña (véase, por ejemplo, La playa en Le Pouldu, 1889, colección privada, Buenos Aires) y deriva del arte japonés a través de los grabados de Hiroshige.La descripción que hace Gauguin de la pintura como un Estudio de espalda desnuda confirma que el elemento esencial es el dorso de la figura. Varios autores han señalado que en el libro de 1876 La nouvelle peinture, Edmond Duranty había desafiado a los artistas a presentar figuras de espalda caracterizando su edad, su condición social y su estado psicológico (3). Degas respondió al reto, de manera muy notable sobre todo en sus pasteles de mujeres aseándose, algunas de las cuales están copiadas en una página del Album Briant de Gauguin, 1888-1889 (Museo del Louvre, París). La vista de atrás, con un énfasis en las nalgas, obviamente lo atraía y se encuentra también en la pintura Otahi, 1893 (colección privada). En Vahine no te miti, la posición incómoda de los brazos y las piernas confiere a la espalda un carácter fragmentario con reminiscencias de la escultura antigua, trasladando a esta la expresividad generalmente atribuida al rostro humano.En 1948, Raquel Edelman halló monumentalidad en esta pintura lograda a expensas de la individualidad y la sensualidad, y consideró que la misma probaba la intención de Gauguin de “dominar y sublimar su erotismo” (4). No obstante, la obra se remite a una mujer muy específica, cuya anatomía expresa un carácter –podría decirse incluso una fisonomía–. En lo que al erotismo de Gauguin respecta, es típicamente sugestivo e indirecto. Ronald Pickvance señaló que la cresta de las olas y el motivo de las flores en el pareo son “como amebas y están animados por una vitalidad orgánica” (5).Si agregamos la conchilla en el rincón, oculta en parte por la caprichosa forma de flores rojas, es evidente que el cuerpo autocontenido y semejante a un fruto de la bañista está rodeado por un ballet de criaturas animadas. Las flores rojas, como el fondo amarillo, reaparecen en Parahi te marae, 1892 (Philadelphia Museum of Art), donde las plantas imitan la sexualidad humana (6). En la pintura de Buenos Aires, la hoja grande que da lugar al título está dividida como una mano en extremidades similares a dedos y apunta hacia el trasero de la mujer (7). A Gauguin le encantaban las sugerencias anatómicas de las frutas y las flores que encontraba en Tahití, las que con frecuencia habían pasado al lenguaje y la mitología locales, y es probable que aquí haya aludido además a la semejanza entre las nalgas femeninas y la nuez llamada coco de mer, “coco de mar”, cuyo nombre científico había sido al principio Lodoicea callypige en razón, precisamente, de esta analogía (8).El título que le puso al cuadro se traduce de hecho como “mujer del mar” y está basado en la misma fórmula que empleó para Vahine no te tiare, “mujer de la flor”, 1891 (Ny Carlsberg Glyptotek, Copenhague) y Vahine no te vi, “mujer del mango”, 1892 (Baltimore Museum of Art). Hablando de esta última, Hiriata Millaud ha observado que vahine –contrariamente a hine– significa una mujer que ya tiene una vida social, y que el atributo introducido por no (“para” o “de”) puede definir el carácter de la mujer en cuestión antes que servir simplemente como una función descriptiva y mnemónica (9). La mujer en Vahine no te miti dirige su mirada y su oído hacia el océano, más específicamente hacia el mar abierto que aparece entre dos rocas o islas. Igual que las figuras de David Friedrich vistas de espalda, ella actúa por lo tanto como mediadora entre la naturaleza y el espectador, y parece realmente ser “del mar”, compenetrada con él, como una Venus tahitiana que es a la vez Anadiomene y Calipigia.\r\nDario Gamboni\r\n1— Véase: Bernard Dorival, Carnet de Tahiti. Paris, Quatre Chemins-Editart, 1954; Carnet de Tahiti. Taravao, Avant et Après, 2001. 2— Thadée Natanson, “Oeuvres récentes de Paul Gauguin”, La revue blanche, diciembre de 1893, citado en: Marla Prather y Charles F. Stuckey (ed.), 1987, p. 225. 3— Véase la entrada de Charles F. Stuckey en: Richard Brettell et al., The Art of Paul Gauguin, cat. exp. Washington, National Gallery of Art, 1988. Versión francesa Gauguin, cat. exp. Paris, Réunion des musées nationaux, 1989, nº 144.4— Raquel Edelman, 1948, p. 73-79. 5— Ronald Pickvance, Gauguin, cat. exp. Martigny, Fondation Pierre Gianadda, 1998, nº 32. 6— Dario Gamboni, “Parahi te marae: où est le temple?”, 48/14: La revue du Musée d’Orsay, Paris, nº 20, 2005, p. 6-17. 7— Véase la planta más explícitamente antropomórfica que sostiene a una pareja copulando en el manuscrito Album ancien culte mahorie (1892, Musée d’Orsay, París, RF 10755, folio 46). 8— Gauguin posteriormente grabó toda la superficie de una de esas nueces (Coco de mer, ca. 1901-1903, Albright-Knox Art Gallery, Buffalo). 9— Hiriata Millaud, “Les titres tahitiens de Gauguin” en: Ia Orana Gauguin, cat. exp. Paris, Somogy, 2003, p. 81-90.\r\n1893. NATANSON, Thadée, “Oeuvres récentes de Paul Gauguin”, La revue blanche, Paris, diciembre. 1936. Plástica, Buenos Aires, a. 2, nº 5, abril, reprod. byn p. 10. 1948. EDELMAN, Raquel, “Gauguin en Buenos Aires”, Ver y Estimar, Buenos Aires, vol. 2, nº 7-8, octubre-noviembre, p. 73-79, reprod. p. 75. 1964. WILDENSTEIN, Georges, Gauguin. Paris, Fondation Wildenstein, vol. 1, nº 465. 1977. FIELD, Richard S., Paul Gauguin: The Paintings of the First Trip to Tahiti. Tesis de doctorado, Harvard University [1963]. New York/London, Garland, nº 21.1987. PRATHER, Marla y Charles F. Stuckey, Gauguin: A retrospective. New York, Hugh Lauter Levin Associates, p. 224-226, reprod. nº 64. 1990. HADDAD, Michèle, La divine et l’impure: le nu au XIXe. Paris, Les Éditions du Jaguar, p. 49-50, reprod. color p. 51. 1993. 1893: L’Europe des peintres, cat. exp. Paris, Réunion des musées nationaux, p. 20.	\N	331	\N	\N	1892	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	El MNBA tiene la suerte de poseer dos pinturas de bañistas de Gauguin, ambientada en este caso no en Francia, sino en los mares del Sur. Esta obra aparece mencionada como Estudio de espalda desnuda en una lista de pinturas realizadas durante la primera estadía en Tahití que Gauguin anotó aproximadamente en abril de 1892 en su Carnet de Tahiti (1). Fue pintada en Mataiea y se basa en un dibujo en color del mismo cuaderno de bocetos, probablemente realizado con modelo y con una cuadrícula apenas visible, útil al momento de repetir la co	\N	137	t
452	El recurso del método	el-recurso-del-m-todo	El recurso del método	\N	\N	126		\N	Tinta sobre papel 30 x 40 cm.	\N	El recurso del método es una novela del escritor cubano Alejo Carpentier. Publicada en 1974, se adscribe al subgénero de la literatura hispanoamericana conocido como novela del dictador.	\N	464	\N	\N	2012	\N	135	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	\N	\N	137	t
\.


--
-- Data for Name: artworkartist; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artworkartist (id, name, namekey, title, titlekey, artwork_id, person_id, created, lastmodified) FROM stdin;
196	\N	\N	\N	\N	195	194	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03
198	\N	\N	\N	\N	197	193	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03
206	\N	\N	\N	\N	205	182	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
208	\N	\N	\N	\N	207	183	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
210	\N	\N	\N	\N	207	184	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
212	\N	\N	\N	\N	211	184	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
214	\N	\N	\N	\N	213	185	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
216	\N	\N	\N	\N	215	186	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
218	\N	\N	\N	\N	217	185	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
220	\N	\N	\N	\N	219	183	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
222	\N	\N	\N	\N	221	188	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
224	\N	\N	\N	\N	219	187	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
226	\N	\N	\N	\N	225	189	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
228	\N	\N	\N	\N	227	190	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
230	\N	\N	\N	\N	229	191	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
232	\N	\N	\N	\N	231	190	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
271	\N	\N	\N	\N	270	263	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03
273	\N	\N	\N	\N	272	264	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03
275	\N	\N	\N	\N	274	265	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03
277	\N	\N	\N	\N	276	266	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03
279	\N	\N	\N	\N	278	267	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03
281	\N	\N	\N	\N	280	268	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03
283	\N	\N	\N	\N	282	269	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03
313	\N	\N	\N	\N	311	308	2025-05-19 13:08:48.061148-03	2025-05-19 13:08:48.061148-03
326	\N	\N	\N	\N	324	190	2025-05-19 14:07:27.299293-03	2025-05-19 14:07:27.299293-03
330	\N	\N	\N	\N	328	185	2025-05-19 14:07:30.393177-03	2025-05-19 14:07:30.393177-03
335	\N	\N	\N	\N	332	333	2025-05-19 14:07:33.80696-03	2025-05-19 14:07:33.80696-03
403	\N	\N	\N	\N	402	401	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03
405	\N	\N	\N	\N	404	398	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03
407	\N	\N	\N	\N	406	400	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03
449	\N	\N	\N	\N	448	447	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03
451	\N	\N	\N	\N	450	447	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03
453	\N	\N	\N	\N	452	447	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03
483	\N	\N	\N	\N	482	480	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03
485	\N	\N	\N	\N	484	481	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03
\.


--
-- Data for Name: artworktype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artworktype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser) FROM stdin;
116	Vaso	vase	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
117	Disco	disk	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
118	Figura	figure	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
119	Abanico	fan	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
120	Miniatura	miniature	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
121	Grabado	engraving	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
122	Mueble	furniture	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
123	Fotografía	photo	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
124	Estructura cinética	cinetic structure	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
125	Textil	textile	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
126	Pintura	painting	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
127	Jardín	garden	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
128	Escultura	sculpture	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
129	Edificio	building	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
130	Instalación	art installation	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
\.


--
-- Data for Name: favorite; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.favorite (id, name, namekey, title, titlekey, person_id, site_id, created, lastmodified, lastmodifieduser) FROM stdin;
\.


--
-- Data for Name: floor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.floor (id, name, namekey, title, titlekey, floortype_id, site_id, subtitle, subtitlekey, info, infokey, floornumber, floornumberkey, map, photo, video, audio, created, lastmodified, lastmodifieduser) FROM stdin;
138	Planta Baja	\N	\N	\N	\N	137	Arte europeo del siglo XII al XIX. Arte argentino del siglo XIX	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
139	Primer Piso	\N	\N	\N	\N	137		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
178	Planta Baja	\N	\N	\N	\N	177	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
179	Primer Piso	\N	\N	\N	\N	177	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
343	Planta Baja	\N	\N	\N	\N	342	Arte europeo del siglo XII al XIX. Arte argentino del siglo XIX	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
344	Primer Piso	\N	\N	\N	\N	342		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
\.


--
-- Data for Name: floortype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.floortype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser) FROM stdin;
111	Exhibición	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
112	Pabellón temporario	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
113	Administración	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
\.


--
-- Data for Name: guidecontent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.guidecontent (id, name, namekey, title, titlekey, artexhibitionguide_id, artexhibitionitem_id, subtitle, subtitlekey, info, infokey, guideorder, photo, video, audio, created, lastmodified, lastmodifieduser) FROM stdin;
203	Morro da favela II (Pueblito)	morro-da-favela-ii-pueblito	Morro da favela II (Pueblito)	\N	202	201	\N	\N	\N	\N	0	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
204	Le bain de Vénus (El baño de Venus)	le-bain-de-v-nus-el-ba-o-de-venus	Le bain de Vénus (El baño de Venus)	\N	202	200	\N	\N	\N	\N	0	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
249	Effet de neige à Louveciennes	effet-de-neige-louveciennes	Effet de neige à Louveciennes	\N	248	234	\N	\N	\N	\N	1	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
250	Le Pont d`Argenteuil (El Puente de Argenteuil)	le-pont-d-argenteuil-el-puente-de-argenteuil	Le Pont d`Argenteuil (El Puente de Argenteuil)	\N	248	235	\N	\N	\N	\N	2	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
251	Prairies du Valhermeil près Pontoise	prairies-du-valhermeil-pr-s-pontoise	Prairies du Valhermeil près Pontoise	\N	248	235	\N	\N	\N	\N	3	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
252	Femme aux champs (Campesina)	femme-aux-champs-campesina	Femme aux champs (Campesina)	\N	248	237	\N	\N	\N	\N	4	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
253	En observation - M.Fabre, Officier de reserve	en-observation-m-fabre-officier-de-reserve	En observation - M.Fabre, Officier de reserve	\N	248	238	\N	\N	\N	\N	5	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
254	Vahine no te miti (Femme a la mer) (Mujer del mar)	vahine-no-te-miti-femme-a-la-mer-mujer-del-mar	Vahine no te miti (Femme a la mer) (Mujer del mar)	\N	248	239	\N	\N	\N	\N	6	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
294	En Normandie	en-normandie	En Normandie	\N	292	286	\N	\N	\N	\N	2	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
257	La Coiffure (El peinado)	la-coiffure-el-peinado	La Coiffure (El peinado)	\N	248	242	\N	\N	\N	\N	9	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
258	La Toilette apres le bain (El arreglo después del baño)	la-toilette-apres-le-bain-el-arreglo-despu-s-del-ba-o	La Toilette apres le bain (El arreglo después del baño)	\N	248	243	\N	\N	\N	\N	10	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
259	Le Moulin de la Galette	le-moulin-de-la-galette	Le Moulin de la Galette	\N	248	244	\N	\N	\N	\N	11	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
260	Portrait Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	portrait-ernest-hosched-et-sa-fille-marthe-retrato-del-se-or-hosched-y-su-hija	Portrait Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	\N	248	245	\N	\N	\N	\N	12	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
261	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	danseuse-et-admirateur-derri-re-la-sc-ne-bailarina-y-admirador-tras-la-escena	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	\N	248	246	\N	\N	\N	\N	13	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
262	La Nymphe surprise (La ninfa sorprendida)	la-nymphe-surprise-la-ninfa-sorprendida	La Nymphe surprise (La ninfa sorprendida)	\N	248	247	\N	\N	\N	\N	14	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
293	La vuelta del malón	la-vuelta-del-mal-n	La vuelta del malón	\N	292	285	\N	\N	\N	\N	1	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
295	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	Sin pan y sin trabajo	\N	292	287	\N	\N	\N	\N	3	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
296	La vuelta al hogar	la-vuelta-al-hogar	La vuelta al hogar	\N	292	288	\N	\N	\N	\N	4	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
297	El despertar de la criada	el-despertar-de-la-criada	El despertar de la criada	\N	292	289	\N	\N	\N	\N	5	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
299	Abel	abel	Abel	\N	292	291	\N	\N	\N	\N	7	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
298	Reposo	reposo	Reposo	\N	292	290	\N	\N	\N	\N	6	\N	\N	338	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100
414	En la costa de Valencia	en-la-costa-de-valencia	En la costa de Valencia	\N	411	410	\N	\N	\N	\N	3	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100
413	Retrato de Juan Manuel de Rosas	retrato-de-juan-manuel-de-rosas	Retrato de Juan Manuel de Rosas	\N	411	409	\N	\N	\N	\N	2	\N	\N	424	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100
412	La Emperatriz Theodora	la-emperatriz-theodora	La Emperatriz Theodora	\N	411	408	\N	\N	\N	\N	1	\N	\N	426	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100
255	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	portrait-de-suzanne-valadon-madame-suzanne-valadon-artiste-peintre-retrato-de-suzanne-valadon-pintora	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	\N	248	240	\N	\N	\N	\N	7	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
256	La berge de La Seine (Orillas del Sena)	la-berge-de-la-seine-orillas-del-sena	La berge de La Seine (Orillas del Sena)	\N	248	241	\N	\N	\N	\N	8	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100
459	Sendas perdidas	\N	Sendas perdidas	\N	458	455	\N	\N	\N	\N	1	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100
460	Apocalipsis	\N	Apocalipsis	\N	458	456	\N	\N	\N	\N	2	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100
461	El recurso del método	\N	El recurso del método	\N	458	457	\N	\N	\N	\N	3	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100
488	Utopia del Sur	\N	Utopia del Sur	\N	411	486	\N	\N	\N	\N	1	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100
489	Procesión sorprendida por la lluvia	\N	Procesión sorprendida por la lluvia	\N	411	487	\N	\N	\N	\N	2	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100
\.


--
-- Data for Name: institution; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institution (id, name, namekey, shortname, title, titlekey, institutiontype_id, subtitle, subtitlekey, info, infokey, address, addresskey, moreinfo, moreinfokey, website, mapurl, email, instagram, whatsapp, phone, twitter, logo, photo, video, audio, map, created, lastmodified, lastmodifieduser) FROM stdin;
135	Museo Nacional de Bellas Artes	museo-nacional-de-bellas-artes	MNBA	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
136	Museo Quinquela Martín	museo-quinquela-mart-n	Museo Quinquela	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
341	Museo de Arte Latinoaméricano de Buenos Aires	museo-de-arte-latinoam-ricano-de-buenos-aires	MALBA	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	https://www.malba.org.ar/	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
\.


--
-- Data for Name: institutionalcontent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institutionalcontent (id, name, namekey, title, titlekey, institution_id, site_id, subtitle, subtitlekey, info, infokey, photo, video, audio, created, lastmodified, lastmodifieduser) FROM stdin;
\.


--
-- Data for Name: institutiontype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institutiontype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser) FROM stdin;
100	Museo	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
101	Monumento	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
102	Centro cultural	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
103	Parque	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
104	Edificio religioso	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
105	Teatro	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
106	Plaza	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
107	Edificio histórico	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
108	Galería de Arte	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
109	Zoo	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
\.


--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person (id, name, namekey, title, titlekey, lastname, lastnamekey, displayname, nickname, sex, physicalid, address, zipcode, phone, email, birthdate, user_id, subtitle, subtitlekey, info, infokey, photo, video, audio, created, lastmodified, lastmodifieduser) FROM stdin;
182	Alfred	alfred	Sisley Alfred	\N	Sisley	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
183	Claude	claude	Monet Claude	\N	Monet	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
184	Camille	camille	Pissarro Camille	\N	Pissarro	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
185	Henri	henri	Toulouse Lautrec Henri	\N	Toulouse Lautrec	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
186	Paul	paul	Gauguin Paul	\N	Gauguin	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
187	Edgar	edgar	Degas Edgar	\N	Degas	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
188	Berthe Marie Pauline	berthe-marie-pauline	Morisot Berthe Marie Pauline	\N	Morisot	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
189	Vicent	vicent	Van Gogh Vicent	\N	Van Gogh	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
190	Edouard	edouard	Manet Edouard	\N	Manet	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
191	Jean Louis	jean-louis	Forain Jean Louis	\N	Forain	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
192	Doménikos	dom-nikos	Theotokópoulos Doménikos	\N	Theotokópoulos	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
193	Odilon	odilon	Redon Odilon	\N	Redon	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
194	Tarsila	tarsila	do Amaral Tarsila	\N	do Amaral	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
181			root 	\N	root	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	100	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
263	Ángel	ngel	Della Valle Ángel	\N	Della Valle	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100
264	María	mar-a	Obligado María	\N	Obligado	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100
265	Ernesto	ernesto	de la Cárcova Ernesto	\N	de la Cárcova	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100
266	Graciano	graciano	Mendihalarzu Graciano	\N	Mendihalarzu	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100
267	Eduardo	eduardo	Sívori Eduardo	\N	Sívori	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100
268	Eduardo	eduardo	Schiaffino Eduardo	\N	Schiaffino	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100
269	Lucio	lucio	Correa Morales Lucio	\N	Correa Morales	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100
308	Rafael	rafael	Barredas Rafael	\N	Barredas	barredas	Rafael Barredas	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:53:48.69726-03	2025-05-19 12:53:48.69726-03	100
333	Benito	benito	Quinquela Martín Benito	\N	Quinquela Martín	quinquela-mart-n	Benito Quinquela Martín	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 14:07:33.533893-03	2025-05-19 14:07:33.533893-03	100
180	Alejandro		- Alejandro	\N	-	\N	\N	\N	\N	Tolomei	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
398	Joaquín	\N	\N	\N	Sorolla y Bastida	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100
399	Kazuya	\N	\N	\N	Sakai	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100
400	Raymond Auguste	\N	\N	\N	Quinsac Monvoisin	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100
401	Jean Joseph Benjamin	\N	\N	\N	Constant	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100
447	Germán	\N	\N	\N	Gárgano	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100
480	Nicolás	\N	\N	\N	García Uriburu	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100
481	Mariano	\N	\N	\N	Fortuny y Marsal	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100
\.


--
-- Data for Name: resource; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.resource (id, name, namekey, title, titlekey, media, info, infokey, bucketname, objectname, binaryobject, created, lastmodified, lastmodifieduser, usethumbnail) FROM stdin;
300	abel.jpg	abel-jpg	\N	\N	image/jpeg	\N	\N	media	abel-100	\N	2025-05-19 12:53:18.529644-03	2025-05-19 12:53:18.529644-03	100	t
301	la-vuelta-del-malon.jpg	la-vuelta-del-malon-jpg	\N	\N	image/jpeg	\N	\N	media	la-vuelta-del-malon-101	\N	2025-05-19 12:53:35.060014-03	2025-05-19 12:53:35.060014-03	100	t
302	el-despertar-de-la-criada.jpg	el-despertar-de-la-criada-jpg	\N	\N	image/jpeg	\N	\N	media	el-despertar-de-la-criada-102	\N	2025-05-19 12:53:37.800791-03	2025-05-19 12:53:37.801906-03	100	t
303	en-normandie.jpg	en-normandie-jpg	\N	\N	image/jpeg	\N	\N	media	en-normandie-103	\N	2025-05-19 12:53:42.510528-03	2025-05-19 12:53:42.510528-03	100	t
304	la-vuelta-al-hogar.jpg	la-vuelta-al-hogar-jpg	\N	\N	image/jpeg	\N	\N	media	la-vuelta-al-hogar-104	\N	2025-05-19 12:53:44.209264-03	2025-05-19 12:53:44.209264-03	100	t
305	reposo.jpg	reposo-jpg	\N	\N	image/jpeg	\N	\N	media	reposo-105	\N	2025-05-19 12:53:45.865734-03	2025-05-19 12:53:45.865734-03	100	t
306	sin-pan-y-sin-trabajo.jpg	sin-pan-y-sin-trabajo-jpg	\N	\N	image/jpeg	\N	\N	media	sin-pan-y-sin-trabajo-106	\N	2025-05-19 12:53:47.43493-03	2025-05-19 12:53:47.43493-03	100	t
309	quiosco-de-canaletas.jpg	quiosco-de-canaletas-jpg	\N	\N	image/jpeg	\N	\N	media	quiosco-de-canaletas-107	\N	2025-05-19 12:53:48.924719-03	2025-05-19 12:53:48.924719-03	100	t
312	quiosco-de-canaletas.jpg	quiosco-de-canaletas-jpg	\N	\N	image/jpeg	\N	\N	media	quiosco-de-canaletas-108	\N	2025-05-19 13:08:46.307485-03	2025-05-19 13:08:46.307485-03	100	t
314	forain-Danseuse et admirateur derrière la scène.jpg	forain-danseuse-et-admirateur-derri-re-la-sc-ne-jpg	\N	\N	image/jpeg	\N	\N	media	forain-danseuse-et-admirateur-derri-re-la-sc-ne-109	\N	2025-05-19 14:00:28.742066-03	2025-05-19 14:00:28.742066-03	100	t
315	forain-Danseuse et admirateur derrière la scène.jpg	forain-danseuse-et-admirateur-derri-re-la-sc-ne-jpg	\N	\N	image/jpeg	\N	\N	media	forain-danseuse-et-admirateur-derri-re-la-sc-ne-110	\N	2025-05-19 14:03:00.808154-03	2025-05-19 14:03:00.808154-03	100	t
316	monet-La berge de La Seine.jpg	monet-la-berge-de-la-seine-jpg	\N	\N	image/jpeg	\N	\N	media	monet-la-berge-de-la-seine-111	\N	2025-05-19 14:04:43.610851-03	2025-05-19 14:04:43.610851-03	100	t
317	manet-La Nymphe surprise.jpg	manet-la-nymphe-surprise-jpg	\N	\N	image/jpeg	\N	\N	media	manet-la-nymphe-surprise-112	\N	2025-05-19 14:05:00.073789-03	2025-05-19 14:05:00.073789-03	100	t
318	degas-La Toilette apres le bain.jpg	degas-la-toilette-apres-le-bain-jpg	\N	\N	image/jpeg	\N	\N	media	degas-la-toilette-apres-le-bain-113	\N	2025-05-19 14:05:05.606782-03	2025-05-19 14:05:05.606782-03	100	t
319	van-gogh-Le Moulin de la Galette.jpg	van-gogh-le-moulin-de-la-galette-jpg	\N	\N	image/jpeg	\N	\N	media	van-gogh-le-moulin-de-la-galette-114	\N	2025-05-19 14:05:05.967549-03	2025-05-19 14:05:05.967549-03	100	t
320	monet-le-pont-dargenteuil.jpg	monet-le-pont-dargenteuil-jpg	\N	\N	image/jpeg	\N	\N	media	monet-le-pont-dargenteuil-115	\N	2025-05-19 14:05:06.226962-03	2025-05-19 14:05:06.226962-03	100	t
321	pissarro-femme-aux-camps.jpg	pissarro-femme-aux-camps-jpg	\N	\N	image/jpeg	\N	\N	media	pissarro-femme-aux-camps-116	\N	2025-05-19 14:05:06.531826-03	2025-05-19 14:05:06.531826-03	100	t
322	pissarro-prairires.jpg	pissarro-prairires-jpg	\N	\N	image/jpeg	\N	\N	media	pissarro-prairires-117	\N	2025-05-19 14:05:06.787645-03	2025-05-19 14:05:06.787645-03	100	t
323	toulouse-lautrec-Portrait de Suzanne Valadon.jpg	toulouse-lautrec-portrait-de-suzanne-valadon-jpg	\N	\N	image/jpeg	\N	\N	media	toulouse-lautrec-portrait-de-suzanne-valadon-118	\N	2025-05-19 14:05:07.193106-03	2025-05-19 14:05:07.193106-03	100	t
325	manet-Portrait d-Ernest Hoschedé et sa fille Marthe.jpg	manet-portrait-d-ernest-hosched-et-sa-fille-marthe-jpg	\N	\N	image/jpeg	\N	\N	media	manet-portrait-d-ernest-hosched--et-sa-fille-marthe-119	\N	2025-05-19 14:07:27.272501-03	2025-05-19 14:07:27.272501-03	100	t
327	sisley-effet-de-niege.jpg	sisley-effet-de-niege-jpg	\N	\N	image/jpeg	\N	\N	media	sisley-effet-de-niege-120	\N	2025-05-19 14:07:27.773092-03	2025-05-19 14:07:27.773092-03	100	t
329	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve.jpg	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve-jpg	\N	\N	image/jpeg	\N	\N	media	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve-121	\N	2025-05-19 14:07:30.38622-03	2025-05-19 14:07:30.38622-03	100	t
331	gaugin-Comentario sobre Vahine no te miti.jpg	gaugin-comentario-sobre-vahine-no-te-miti-jpg	\N	\N	image/jpeg	\N	\N	media	gaugin-comentario-sobre-vahine-no-te-miti-122	\N	2025-05-19 14:07:30.710928-03	2025-05-19 14:07:30.710928-03	100	t
334	benito-quinquela-martín-puente-de-la-boca.jpg	benito-quinquela-mart-n-puente-de-la-boca-jpg	\N	\N	image/jpeg	\N	\N	media	benito-quinquela-mart-n-puente-de-la-boca-123	\N	2025-05-19 14:07:33.795598-03	2025-05-19 14:07:33.795598-03	100	t
336	reposo.mp3	reposo-mp3	\N	\N	audio/mp3	\N	\N	media	reposo-125	\N	2025-05-19 17:03:01.658216-03	2025-05-19 17:03:01.658216-03	100	t
337	reposo.mp3	reposo-mp3	\N	\N	audio/mp3	\N	\N	media	reposo-126	\N	2025-05-19 17:08:05.967738-03	2025-05-19 17:08:05.967738-03	100	t
338	reposo.mp3	reposo-mp3	\N	\N	audio/mp3	\N	\N	media	reposo-127	\N	2025-05-20 11:29:21.56577-03	2025-05-20 11:29:21.56577-03	100	t
357	malba.jpg	malba-jpg	\N	\N	image/jpeg	\N	\N	media	malba-129	\N	2025-06-08 23:32:47.085864-03	2025-06-08 23:32:47.085864-03	100	t
358	mnba.jpg	mnba-jpg	\N	\N	image/jpeg	\N	\N	media	mnba-130	\N	2025-06-08 23:32:56.174359-03	2025-06-08 23:32:56.174359-03	100	t
359	quinquela.jpg	quinquela-jpg	\N	\N	image/jpeg	\N	\N	media	quinquela-131	\N	2025-06-08 23:35:16.284274-03	2025-06-08 23:35:16.284274-03	100	t
363	arte-argentino.jpg	arte-argentino-jpg	\N	\N	image/jpeg	\N	\N	media	arte-argentino-132	\N	2025-06-09 19:30:29.515396-03	2025-06-09 19:30:29.515396-03	100	t
364	impresionismo.jpg	impresionismo-jpg	\N	\N	image/jpeg	\N	\N	media	impresionismo-133	\N	2025-06-09 19:30:42.127085-03	2025-06-09 19:30:42.127085-03	100	t
365	museo-secreto.jpg	museo-secreto-jpg	\N	\N	image/jpeg	\N	\N	media	museo-secreto-134	\N	2025-06-09 19:30:42.542453-03	2025-06-09 19:30:42.542453-03	100	t
366	obras-maestras.jpg	obras-maestras-jpg	\N	\N	image/jpeg	\N	\N	media	obras-maestras-135	\N	2025-06-09 19:30:42.927536-03	2025-06-09 19:30:42.927536-03	100	t
367	museo-secreto.jpg	museo-secreto-jpg	\N	\N	image/jpeg	\N	\N	media	museo-secreto-136	\N	2025-06-10 10:48:25.570969-03	2025-06-10 10:48:25.570969-03	100	t
368	museo-secreto.mp3	museo-secreto-mp3	\N	\N	audio/mp3	\N	\N	media	museo-secreto-137	\N	2025-06-10 11:30:30.908406-03	2025-06-10 11:30:30.908406-03	100	t
415	rosas.jpg	rosas-jpg	\N	\N	image/jpeg	\N	\N	media	rosas-138	\N	2025-06-10 15:39:31.66896-03	2025-06-10 15:39:31.66896-03	100	t
416	sorolla.jpg	sorolla-jpg	\N	\N	image/jpeg	\N	\N	media	sorolla-139	\N	2025-06-10 15:39:32.077799-03	2025-06-10 15:39:32.077799-03	100	t
417	theodora.jpg	theodora-jpg	\N	\N	image/jpeg	\N	\N	media	theodora-140	\N	2025-06-10 15:39:32.341741-03	2025-06-10 15:39:32.341741-03	100	t
418	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-141	\N	2025-06-11 18:00:45.327953-03	2025-06-11 18:00:45.327953-03	100	t
419	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-142	\N	2025-06-11 18:06:38.756155-03	2025-06-11 18:06:38.756155-03	100	t
420	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-143	\N	2025-06-11 18:22:39.656749-03	2025-06-11 18:22:39.656749-03	100	t
421	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-144	\N	2025-06-11 21:47:41.748635-03	2025-06-11 21:47:41.748635-03	100	t
422	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-145	\N	2025-06-12 13:06:21.256168-03	2025-06-12 13:06:21.256168-03	100	t
423	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-146	\N	2025-06-12 13:06:22.871895-03	2025-06-12 13:06:22.871895-03	100	t
424	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-147	\N	2025-06-13 14:30:00.94748-03	2025-06-13 14:30:00.94748-03	100	t
425	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-148	\N	2025-06-13 14:30:02.052468-03	2025-06-13 14:30:02.052468-03	100	t
426	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-149	\N	2025-06-13 14:33:41.173778-03	2025-06-13 14:33:41.173778-03	100	t
462	logo-mnba.png	logo-mnba-png	\N	\N	image/png	\N	\N	media	logo-mnba-150	\N	2025-07-25 19:27:31.965004-03	2025-07-25 19:27:31.965076-03	100	t
464	recurso-metodo.jpg	recurso-metodo-jpg	\N	\N	image/jpeg	\N	\N	media	recurso-metodo-152	\N	2025-07-25 19:40:41.776111-03	2025-07-25 19:40:41.776117-03	100	t
465	apocalipsis.jpg	apocalipsis-jpg	\N	\N	image/jpeg	\N	\N	media	apocalipsis-153	\N	2025-07-25 19:40:42.845304-03	2025-07-25 19:40:42.845309-03	100	t
463	sendas-perdidas.jpg	sendas-perdidas-jpg	\N	\N	image/jpeg	\N	\N	media	sendas-perdidas-151	\N	2025-07-25 19:40:29.189818-03	2025-07-25 19:40:29.18989-03	100	f
466	sendas-perdidas-muestra-2.jpg	sendas-perdidas-muestra-2-jpg	\N	\N	image/jpeg	\N	\N	media	sendas-perdidas-muestra-2-154	\N	2025-07-25 19:50:10.302434-03	2025-07-25 19:50:10.302505-03	100	t
490	utopia-del-sur.jpg	utopia-del-sur-jpg	\N	\N	image/jpeg	\N	\N	media	utopia-del-sur-155	\N	2025-07-28 12:01:22.269836-03	2025-07-28 12:01:22.269841-03	100	f
491	procesion.jpg	procesion-jpg	\N	\N	image/jpeg	\N	\N	media	procesion-156	\N	2025-07-28 12:01:23.906137-03	2025-07-28 12:01:23.906142-03	100	f
\.


--
-- Data for Name: room; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.room (id, name, namekey, title, titlekey, roomtype_id, floor_id, roomnumber, roomnumberkey, subtitle, subtitlekey, info, infokey, map, photo, video, audio, created, lastmodified, lastmodifieduser) FROM stdin;
140	Hall Central	\N	\N	\N	\N	138	0	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
141	Tienda del Museo	\N	\N	\N	\N	138		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
142	Sala 2. Arte europeo siglo XII al XVI	\N	\N	\N	\N	138	2	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
143	Sala 3. Arte europeo siglo XV al XVIII	\N	\N	\N	\N	138	3	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
144	Sala 4. Países Bajos siglo XVII	\N	\N	\N	\N	138	4	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
145	Sala 5. Arte europeo	\N	\N	\N	\N	138	5	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
146	Sala 6. Arte europeo manierismo y barroco	\N	\N	\N	\N	138	6	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
147	Sala 7. Arte europeo siglo XVIII	\N	\N	\N	\N	138	7	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
148	Sala 8. Francisco de Goya	\N	\N	\N	\N	138	8	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
149	Sala 9. Arte europeo	\N	\N	\N	\N	138	9	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
150	Sala 10. Auguste Rodin	\N	\N	\N	\N	138	10	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
151	Sala 11. Arte francés siglo XIX	\N	\N	\N	\N	138	11	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
152	Sala 12. Arte europeo	\N	\N	\N	\N	138	12	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
153	Sala 13. Arte francés siglo XIX	\N	\N	\N	\N	138	13	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
154	Sala 14. Impresionismo y Postimpresionismo	\N	\N	\N	\N	138	14	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
155	Sala 15. Arte argentino siglo XIX	\N	\N	\N	\N	138	15	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
156	Sala 16. Arte europeo siglo XVII al XIX	\N	\N	\N	\N	138	16	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
157	Sala 17.	\N	\N	\N	\N	138	17	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
158	Sala 18. Arte francés siglo XIX	\N	\N	\N	\N	138	18	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
159	Sala 19	\N	\N	\N	\N	138	19	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
160	Sala 20. Arte argentino siglo XIX	\N	\N	\N	\N	138	20	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
161	Sala 21	\N	\N	\N	\N	138	21	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
162	Sala 22. Arte colonial	\N	\N	\N	\N	138	22	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
163	Sala 23. Arte colonial	\N	\N	\N	\N	138	23	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
164	Sala 24. Arte de los antiguos pueblos andinos	\N	\N	\N	\N	138	24	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
165	Biblioteca	\N	\N	\N	\N	138		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
166	Pabellón de exposiciones temporarias	\N	\N	\N	\N	139	100	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
167	SUM	\N	\N	\N	\N	138		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
168	Rampa primer piso - etre salas 31 y 32	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
169	Hall y escaleras	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
170	Sala 2. Arte europeo siglo XII al XVI	\N	\N	\N	\N	138		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
171	Salón 20 ARTISTAS BRASILEÑOS	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
172	Escuela de La Boca y realismo social	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
173	Arte latinoamericano y argentino 1910-1945	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
174	Exposición temporaria	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
175	Informalismo y expresionismo abstracto	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
176	Arte concreto y abstracción 1945-1970	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
345	Hall Central	\N	\N	\N	\N	343	0	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
346	Tienda del Museo	\N	\N	\N	\N	343		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
347	Sala 2. Arte europeo siglo XII al XVI	\N	\N	\N	\N	343	2	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
348	Sala 3. Arte europeo siglo XV al XVIII	\N	\N	\N	\N	343	3	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
349	Sala 4. Países Bajos siglo XVII	\N	\N	\N	\N	343	4	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
350	Escuela de La Boca y realismo social	\N	\N	\N	\N	344		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
351	Arte latinoamericano y argentino 1910-1945	\N	\N	\N	\N	344		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
352	Exposición temporaria	\N	\N	\N	\N	344		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
353	Informalismo y expresionismo abstracto	\N	\N	\N	\N	344		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
354	Arte concreto y abstracción 1945-1970	\N	\N	\N	\N	344		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100
\.


--
-- Data for Name: roomtype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roomtype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser) FROM stdin;
114	Sala de exhibición	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
115	Pasillo	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
\.


--
-- Data for Name: site; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.site (id, name, namekey, shortname, title, titlekey, sitetype_id, institution_id, website, mapurl, email, instagram, whatsapp, phone, twitter, subtitle, subtitlekey, info, infokey, address, addresskey, logo, photo, video, audio, map, created, lastmodified, lastmodifieduser, opens, openskey, abstract, intro, introkey) FROM stdin;
177	Museo Benito Quinquela Martín	museo-benito-quinquela-mart-n	Museo Quinquela	Museo Benito Quinquela Martín	\N	\N	136	https://buenosaires.gob.ar/educacion/gestion-cultural/museo-benito-quinquela-martin	\N	\N	\N	\N	54 911 66892321	\N	\N	\N	El Museo de Artistas Argentinos "Benito Quinquela Martín" está ubicado en el barrio de La Boca, de la Comuna 4 en la Ciudad Autónoma de Buenos Aires, Argentina. Fundado en el año 1938, está construido sobre terrenos originalmente donados por el pintor. En esos terrenos, en 1936 se inauguró la Escuela Pedro de Mendoza y dos años más tarde abrió las puertas el museo. Denominado originalmente Museo de Artistas Argentinos. Cuenta actualmente con la mayor colección reunida del artista plástico, más de 90 de sus obras.\\n  Martes a domingos y feriados de 11.15 a 18 (UTC-3).	\N	Av. Don Pedro de Mendoza 1835, C1169 Cdad. Autónoma de Buenos Aires	\N	\N	359	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-06-08 23:35:16.307761-03	100	\N	\N	El Museo "Benito Quinquela Martín" está ubicado en el barrio de La Boca en la Ciudad Autónoma de Buenos Aires, Argentina. Fundado en el año 1938, está construido sobre terrenos originalmente donados por el pintor. Cuenta actualmente con la mayor colección reunida del artista plástico, más de 90 de sus obras. Además, el Museo ofrece un panorama del arte figurativo argentino desde fines del siglo XIX hasta mediados del siglo XX. Obras de grandes artistas, considerados iniciadores y precursores de las artes plásticas en el país, se encuentran exhibidas en este museo, donde el artista vivió y trabajó.	El Museo "Benito Quinquela Martín" está ubicado en el barrio de La Boca en la Ciudad Autónoma de Buenos Aires, Argentina. Fundado en el año 1938, está construido sobre terrenos originalmente donados por el pintor. Cuenta actualmente con la mayor colección reunida del artista plástico, más de 90 de sus obras. Además, el Museo ofrece un panorama del arte figurativo argentino desde fines del siglo XIX hasta mediados del siglo XX. Obras de grandes artistas, considerados iniciadores y precursores de las artes plásticas en el país, se encuentran exhibidas en este museo, donde el artista vivió y trabajó.	\N
342	Museo de Arte Latinoaméricano de Buenos Aires	museo-de-arte-latinoam-ricano-de-buenos-aires	MALBA	\N	\N	\N	341	https://www.malba.org.ar/	\N	\N	\N	\N	\N	\N	\N	\N	El Museo de Arte Latinoamericano de Buenos Aires (Malba) - Fundaci¢n Costantini, m s conocido simplemente como MALBA es un museo argentino fundado en septiembre de 2001.Fue creado con el objetivo de coleccionar, preservar, estudiar y difundir el arte latinoamericano desde principios del siglo XX hasta la actualidad. Es una instituci¢n privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contempor neos de la regi¢n.\\n  Lun y Mier-Dom 12:00 a 20:00 \\n Av. Figueroa Alcorta 3415, CABA	\N	Av. Figueroa Alcorta 3415\\b C1425CLA Buenos Aires, Argentina \\b +54 11 4808 6500	\N	\N	357	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-06-08 23:32:50.867181-03	100	Jueves a lunes de 12:00 a 20:00.\\b Miércoles de 11:00 a 20:00. \\b Martes cerrado.	\N	Se specializa en el arte latinoamericano desde principios del siglo XX hasta la actualidad. \\nEs una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región.	El MALBA es una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región. Se specializa en el arte latinoamericano desde principios del siglo XX hasta la actualidad.	\N
137	Museo Nacional de Bellas Artes	museo-nacional-de-bellas-artes	MNBA	Museo Nacional de Bellas Artes	\N	\N	135	https://www.bellasartes.gob.ar	https://maps.app.goo.gl/mCePQghfM2oUjmgy6	\N	\N	\N	54 911 64324235	\N	\N	\N	El Museo Nacional de Bellas Artes (MNBA), ubicado en la Ciudad de Buenos Aires, es una de las instituciones públicas de arte más importantes de Argentina. Alberga un patrimonio sumamente diverso, que incluye más de 12 000 piezas, entre pinturas, esculturas, dibujos, grabados, textiles y objetos. Su colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad.\\n martes a viernes de 11 a 20 \\n sábados y domingos de 10 a 20 \\n(lunes: cerrado).	\N	MNBA	\N	462	358	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-07-25 19:27:35.843028-03	100	Lunes: cerrado \\b Martes: de 11:00 a 19:30 \\b Miércoles: de 11:00 a 19:30\\b Jueves: de 11:00 a 19:30 \\b Viernes: de 11:00 a 19:30 \\b Sábado: de 10:00 a 19:30 \\b Domingo: de 10:00 a 19:30	\N	Es una de las instituciones públicas de arte más importantes de Argentina.\\n Su colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad.	El MNBA es una de las instituciones públicas de arte más importantes de Argentina. Su colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad.	\N
\.


--
-- Data for Name: sitetype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sitetype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser) FROM stdin;
110	Sede central	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name, namekey, title, titlekey, username, created, lastmodified, lastmodifieduser) FROM stdin;
100	root	\N	\N	\N	root	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100
101	atolomei	\N	\N	\N	atolomei	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100
\.


--
-- Name: objectstorage_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.objectstorage_id', 156, true);


--
-- Name: readcode_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.readcode_id', 1035, true);


--
-- Name: sequence_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sequence_id', 491, true);


--
-- Name: sequence_user_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sequence_user_id', 101, true);


--
-- Name: artexhibition artexhibition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibition
    ADD CONSTRAINT artexhibition_pkey PRIMARY KEY (id);


--
-- Name: artexhibitionguide artexhibitionguide_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguide
    ADD CONSTRAINT artexhibitionguide_pkey PRIMARY KEY (id);


--
-- Name: artexhibitionitem artexhibitionitem_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_pkey PRIMARY KEY (id);


--
-- Name: artexhibitionstatustype artexhibitionstatustype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionstatustype
    ADD CONSTRAINT artexhibitionstatustype_pkey PRIMARY KEY (id);


--
-- Name: artwork artwork_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_pkey PRIMARY KEY (id);


--
-- Name: artworkartist artworkartist_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworkartist
    ADD CONSTRAINT artworkartist_pkey PRIMARY KEY (id);


--
-- Name: artworktype artworktype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworktype
    ADD CONSTRAINT artworktype_pkey PRIMARY KEY (id);


--
-- Name: favorite favorite_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favorite
    ADD CONSTRAINT favorite_pkey PRIMARY KEY (id);


--
-- Name: floor floor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floor
    ADD CONSTRAINT floor_pkey PRIMARY KEY (id);


--
-- Name: floortype floortype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floortype
    ADD CONSTRAINT floortype_pkey PRIMARY KEY (id);


--
-- Name: guidecontent guidecontent_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontent
    ADD CONSTRAINT guidecontent_pkey PRIMARY KEY (id);


--
-- Name: institution institution_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution
    ADD CONSTRAINT institution_pkey PRIMARY KEY (id);


--
-- Name: institutionalcontent institutionalcontent_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionalcontent
    ADD CONSTRAINT institutionalcontent_pkey PRIMARY KEY (id);


--
-- Name: institutiontype institutiontype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutiontype
    ADD CONSTRAINT institutiontype_pkey PRIMARY KEY (id);


--
-- Name: person person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- Name: resource resource_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.resource
    ADD CONSTRAINT resource_pkey PRIMARY KEY (id);


--
-- Name: room room_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_pkey PRIMARY KEY (id);


--
-- Name: roomtype roomtype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roomtype
    ADD CONSTRAINT roomtype_pkey PRIMARY KEY (id);


--
-- Name: site site_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_pkey PRIMARY KEY (id);


--
-- Name: sitetype sitetype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sitetype
    ADD CONSTRAINT sitetype_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: artexhibition artexhibition_artexhibitionstatustype_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibition
    ADD CONSTRAINT artexhibition_artexhibitionstatustype_id_fkey FOREIGN KEY (artexhibitionstatustype_id) REFERENCES public.artexhibitionstatustype(id) ON DELETE RESTRICT;


--
-- Name: artexhibition artexhibition_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibition
    ADD CONSTRAINT artexhibition_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibition artexhibition_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibition
    ADD CONSTRAINT artexhibition_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artexhibition artexhibition_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibition
    ADD CONSTRAINT artexhibition_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibition artexhibition_site_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibition
    ADD CONSTRAINT artexhibition_site_id_fkey FOREIGN KEY (site_id) REFERENCES public.site(id) ON DELETE RESTRICT;


--
-- Name: artexhibition artexhibition_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibition
    ADD CONSTRAINT artexhibition_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionguide artexhibitionguide_artexhibition_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguide
    ADD CONSTRAINT artexhibitionguide_artexhibition_id_fkey FOREIGN KEY (artexhibition_id) REFERENCES public.artexhibition(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionguide artexhibitionguide_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguide
    ADD CONSTRAINT artexhibitionguide_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionguide artexhibitionguide_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguide
    ADD CONSTRAINT artexhibitionguide_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionguide artexhibitionguide_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguide
    ADD CONSTRAINT artexhibitionguide_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionguide artexhibitionguide_publisher_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguide
    ADD CONSTRAINT artexhibitionguide_publisher_id_fkey FOREIGN KEY (publisher_id) REFERENCES public.person(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionguide artexhibitionguide_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguide
    ADD CONSTRAINT artexhibitionguide_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionitem artexhibitionitem_artexhibition_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_artexhibition_id_fkey FOREIGN KEY (artexhibition_id) REFERENCES public.artexhibition(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionitem artexhibitionitem_artwork_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_artwork_id_fkey FOREIGN KEY (artwork_id) REFERENCES public.artwork(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionitem artexhibitionitem_floor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_floor_id_fkey FOREIGN KEY (floor_id) REFERENCES public.floor(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionitem artexhibitionitem_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionitem artexhibitionitem_room_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.room(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionitem artexhibitionitem_site_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_site_id_fkey FOREIGN KEY (site_id) REFERENCES public.site(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionstatustype artexhibitionstatustype_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionstatustype
    ADD CONSTRAINT artexhibitionstatustype_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artwork artwork_artworktype_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_artworktype_id_fkey FOREIGN KEY (artworktype_id) REFERENCES public.artworktype(id) ON DELETE RESTRICT;


--
-- Name: artwork artwork_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artwork artwork_institution_owner_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_institution_owner_id_fkey FOREIGN KEY (institution_owner_id) REFERENCES public.institution(id) ON DELETE RESTRICT;


--
-- Name: artwork artwork_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artwork artwork_person_owner_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_person_owner_id_fkey FOREIGN KEY (person_owner_id) REFERENCES public.person(id) ON DELETE RESTRICT;


--
-- Name: artwork artwork_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artwork artwork_site_owner_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_site_owner_id_fkey FOREIGN KEY (site_owner_id) REFERENCES public.site(id) ON DELETE RESTRICT;


--
-- Name: artwork artwork_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artworkartist artworkartist_artwork_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworkartist
    ADD CONSTRAINT artworkartist_artwork_id_fkey FOREIGN KEY (artwork_id) REFERENCES public.artwork(id) ON DELETE RESTRICT;


--
-- Name: artworkartist artworkartist_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworkartist
    ADD CONSTRAINT artworkartist_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(id) ON DELETE RESTRICT;


--
-- Name: artworktype artworktype_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworktype
    ADD CONSTRAINT artworktype_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: favorite favorite_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favorite
    ADD CONSTRAINT favorite_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: favorite favorite_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favorite
    ADD CONSTRAINT favorite_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(id) ON DELETE RESTRICT;


--
-- Name: favorite favorite_site_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.favorite
    ADD CONSTRAINT favorite_site_id_fkey FOREIGN KEY (site_id) REFERENCES public.site(id) ON DELETE RESTRICT;


--
-- Name: floor floor_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floor
    ADD CONSTRAINT floor_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: floor floor_floortype_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floor
    ADD CONSTRAINT floor_floortype_id_fkey FOREIGN KEY (floortype_id) REFERENCES public.floortype(id) ON DELETE RESTRICT;


--
-- Name: floor floor_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floor
    ADD CONSTRAINT floor_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: floor floor_map_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floor
    ADD CONSTRAINT floor_map_fkey FOREIGN KEY (map) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: floor floor_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floor
    ADD CONSTRAINT floor_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: floor floor_site_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floor
    ADD CONSTRAINT floor_site_id_fkey FOREIGN KEY (site_id) REFERENCES public.site(id) ON DELETE RESTRICT;


--
-- Name: floor floor_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floor
    ADD CONSTRAINT floor_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: floortype floortype_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floortype
    ADD CONSTRAINT floortype_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: guidecontent guidecontent_artexhibitionguide_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontent
    ADD CONSTRAINT guidecontent_artexhibitionguide_id_fkey FOREIGN KEY (artexhibitionguide_id) REFERENCES public.artexhibitionguide(id) ON DELETE RESTRICT;


--
-- Name: guidecontent guidecontent_artexhibitionitem_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontent
    ADD CONSTRAINT guidecontent_artexhibitionitem_id_fkey FOREIGN KEY (artexhibitionitem_id) REFERENCES public.artexhibitionitem(id) ON DELETE RESTRICT;


--
-- Name: guidecontent guidecontent_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontent
    ADD CONSTRAINT guidecontent_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: guidecontent guidecontent_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontent
    ADD CONSTRAINT guidecontent_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: guidecontent guidecontent_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontent
    ADD CONSTRAINT guidecontent_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: guidecontent guidecontent_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontent
    ADD CONSTRAINT guidecontent_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: institution institution_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution
    ADD CONSTRAINT institution_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: institution institution_institutiontype_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution
    ADD CONSTRAINT institution_institutiontype_id_fkey FOREIGN KEY (institutiontype_id) REFERENCES public.institutiontype(id) ON DELETE RESTRICT;


--
-- Name: institution institution_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution
    ADD CONSTRAINT institution_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: institution institution_logo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution
    ADD CONSTRAINT institution_logo_fkey FOREIGN KEY (logo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: institution institution_map_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution
    ADD CONSTRAINT institution_map_fkey FOREIGN KEY (map) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: institution institution_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution
    ADD CONSTRAINT institution_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: institution institution_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution
    ADD CONSTRAINT institution_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: institutionalcontent institutionalcontent_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionalcontent
    ADD CONSTRAINT institutionalcontent_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: institutionalcontent institutionalcontent_institution_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionalcontent
    ADD CONSTRAINT institutionalcontent_institution_id_fkey FOREIGN KEY (institution_id) REFERENCES public.institution(id) ON DELETE RESTRICT;


--
-- Name: institutionalcontent institutionalcontent_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionalcontent
    ADD CONSTRAINT institutionalcontent_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: institutionalcontent institutionalcontent_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionalcontent
    ADD CONSTRAINT institutionalcontent_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: institutionalcontent institutionalcontent_site_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionalcontent
    ADD CONSTRAINT institutionalcontent_site_id_fkey FOREIGN KEY (site_id) REFERENCES public.institution(id) ON DELETE RESTRICT;


--
-- Name: institutionalcontent institutionalcontent_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionalcontent
    ADD CONSTRAINT institutionalcontent_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: institutiontype institutiontype_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutiontype
    ADD CONSTRAINT institutiontype_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: person person_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: person person_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: person person_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: person person_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: person person_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: resource resource_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.resource
    ADD CONSTRAINT resource_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: room room_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: room room_floor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_floor_id_fkey FOREIGN KEY (floor_id) REFERENCES public.floor(id) ON DELETE RESTRICT;


--
-- Name: room room_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: room room_map_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_map_fkey FOREIGN KEY (map) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: room room_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: room room_roomtype_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_roomtype_id_fkey FOREIGN KEY (roomtype_id) REFERENCES public.roomtype(id) ON DELETE RESTRICT;


--
-- Name: room room_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: roomtype roomtype_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roomtype
    ADD CONSTRAINT roomtype_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: site site_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: site site_institution_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_institution_id_fkey FOREIGN KEY (institution_id) REFERENCES public.institution(id) ON DELETE RESTRICT;


--
-- Name: site site_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: site site_logo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_logo_fkey FOREIGN KEY (logo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: site site_map_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_map_fkey FOREIGN KEY (map) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: site site_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: site site_sitetype_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_sitetype_id_fkey FOREIGN KEY (sitetype_id) REFERENCES public.sitetype(id) ON DELETE RESTRICT;


--
-- Name: site site_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: sitetype sitetype_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sitetype
    ADD CONSTRAINT sitetype_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: users users_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- PostgreSQL database dump complete
--

