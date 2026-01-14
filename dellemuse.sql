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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: aa1; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.aa1 (
    artwork_id bigint NOT NULL,
    artist_id bigint NOT NULL,
    person_id bigint
);


ALTER TABLE public.aa1 OWNER TO postgres;

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
    fromdate timestamp with time zone DEFAULT now(),
    todate timestamp with time zone DEFAULT now(),
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    location character varying(4096),
    intro text,
    introkey character varying(512),
    opens character varying(2048),
    openskey character varying(512),
    spec text,
    shortname character varying(1024),
    shortnamekey character varying(1024),
    state integer DEFAULT 1,
    language character varying(24),
    draft text,
    masterlanguage character varying(24) DEFAULT 'es'::character varying,
    translation integer DEFAULT 0,
    usethumbnail boolean DEFAULT true,
    audioautogenerate boolean DEFAULT true,
    speechaudio bigint,
    ordinal integer DEFAULT 0
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
    openskey character varying(512),
    state integer DEFAULT 1,
    language character varying(24) DEFAULT 'es'::character varying,
    draft text,
    masterlanguage character varying(24) DEFAULT 'es'::character varying,
    translation integer DEFAULT 0,
    usethumbnail boolean DEFAULT true,
    intro text,
    spec text,
    audioautogenerate boolean DEFAULT true,
    speechaudio bigint,
    audio_id bigint,
    ordinal integer DEFAULT 0
);


ALTER TABLE public.artexhibitionguide OWNER TO postgres;

--
-- Name: artexhibitionguiderecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artexhibitionguiderecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    artexhibitionguide_id bigint,
    language character varying(24) NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    namekey character varying(512),
    state integer DEFAULT 0,
    intro text,
    usethumbnail boolean DEFAULT true,
    name_hash bigint DEFAULT 0,
    subtitle_hash bigint DEFAULT 0,
    info_hash bigint DEFAULT 0,
    intro_hash bigint DEFAULT 0,
    spec text,
    spec_hash bigint DEFAULT 0,
    otherjson text,
    otherjson_hash bigint DEFAULT 0,
    opens text,
    opens_hash bigint DEFAULT 0,
    audioautogenerate boolean DEFAULT true,
    audioauto boolean DEFAULT false
);


ALTER TABLE public.artexhibitionguiderecord OWNER TO postgres;

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
    lastmodifieduser bigint NOT NULL,
    state integer DEFAULT 1,
    floorstr character varying(1024),
    roomstr character varying(1024),
    language character varying(24),
    draft text,
    masterlanguage character varying(24) DEFAULT 'es'::character varying,
    translation integer DEFAULT 0,
    photo bigint,
    audio bigint,
    video bigint,
    subtitle character varying(2048),
    usethumbnail boolean DEFAULT true,
    intro text,
    spec text,
    opens text,
    audioautogenerate boolean DEFAULT true,
    speechaudio bigint
);


ALTER TABLE public.artexhibitionitem OWNER TO postgres;

--
-- Name: artexhibitionitemrecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artexhibitionitemrecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    artexhibitionitem_id bigint,
    language character varying(24) NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    opens character varying(2048),
    info text,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    namekey character varying(512),
    state integer DEFAULT 0,
    intro text,
    usethumbnail boolean DEFAULT true,
    name_hash bigint,
    subtitle_hash bigint,
    info_hash bigint,
    intro_hash bigint,
    spec text,
    spec_hash bigint DEFAULT 0,
    otherjson text,
    otherjson_hash bigint DEFAULT 0,
    opens_hash bigint DEFAULT 0,
    audioautogenerate boolean DEFAULT true,
    audioauto boolean DEFAULT false
);


ALTER TABLE public.artexhibitionitemrecord OWNER TO postgres;

--
-- Name: artexhibitionrecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artexhibitionrecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    artexhibition_id bigint,
    language character varying(24) NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    opens character varying(2048),
    info text,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    namekey character varying(512),
    state integer DEFAULT 0,
    intro text,
    usethumbnail boolean DEFAULT true,
    name_hash bigint DEFAULT 0,
    subtitle_hash bigint DEFAULT 0,
    info_hash bigint DEFAULT 0,
    intro_hash bigint DEFAULT 0,
    spec text,
    otherjson_hash bigint DEFAULT 0,
    otherjson text,
    spec_hash bigint DEFAULT 0,
    opens_hash bigint DEFAULT 0,
    audioautogenerate boolean DEFAULT true,
    audioauto boolean DEFAULT false
);


ALTER TABLE public.artexhibitionrecord OWNER TO postgres;

--
-- Name: artexhibitionsection; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artexhibitionsection (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    artexhibition_id bigint,
    language character varying(24) NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    state integer DEFAULT 0,
    photo bigint,
    video bigint,
    audio bigint,
    masterlanguage character varying(24) DEFAULT 'es'::character varying,
    translation integer DEFAULT 0,
    draft text,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    intro text,
    spec text,
    opens text,
    audioautogenerate boolean DEFAULT true,
    namekey character varying(512),
    usethumbnail boolean DEFAULT true,
    speechaudio bigint
);


ALTER TABLE public.artexhibitionsection OWNER TO postgres;

--
-- Name: artexhibitionsectionrecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artexhibitionsectionrecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    artexhibitionsection_id bigint,
    language character varying(24) NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    namekey character varying(512),
    state integer DEFAULT 0,
    intro text,
    usethumbnail boolean DEFAULT true,
    name_hash bigint,
    subtitle_hash bigint,
    info_hash bigint,
    intro_hash bigint,
    spec_hash bigint DEFAULT 0,
    otherjson text,
    otherjson_hash bigint DEFAULT 0,
    opens text,
    opens_hash bigint DEFAULT 0,
    audioautogenerate boolean DEFAULT true,
    audioauto boolean DEFAULT false
);


ALTER TABLE public.artexhibitionsectionrecord OWNER TO postgres;

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
    lastmodifieduser bigint NOT NULL,
    state integer DEFAULT 1,
    language character varying(24),
    draft text,
    audioautogenerate boolean DEFAULT true
);


ALTER TABLE public.artexhibitionstatustype OWNER TO postgres;

--
-- Name: artist; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artist (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    language character varying(24) NOT NULL,
    masterlanguage character varying(24) DEFAULT 'es'::character varying,
    person_id bigint,
    nickname character varying(512),
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    name character varying(1024),
    state integer DEFAULT 3,
    audio bigint,
    photo bigint,
    video bigint,
    info text,
    intro text,
    opens text,
    spec text,
    speechaudio bigint,
    audioautogenerate boolean DEFAULT true,
    subtitle character varying(1024),
    translation integer DEFAULT 0
);


ALTER TABLE public.artist OWNER TO postgres;

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
    usethumbnail boolean DEFAULT true,
    url character varying(1024),
    state integer DEFAULT 1,
    qrcode bigint,
    language character varying(24),
    draft text,
    translation integer DEFAULT 0,
    masterlanguage character varying(24) DEFAULT 'es'::character varying,
    opens text,
    audioautogenerate boolean DEFAULT true,
    speechaudio bigint,
    source character varying(1024),
    epoch character varying(1024),
    objecttype integer DEFAULT 0
);


ALTER TABLE public.artwork OWNER TO postgres;

--
-- Name: artwork_artist; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artwork_artist (
    artwork_id bigint NOT NULL,
    artist_id bigint NOT NULL,
    person_id bigint
);


ALTER TABLE public.artwork_artist OWNER TO postgres;

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
-- Name: artworkrecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artworkrecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    language character varying(24) NOT NULL,
    artwork_id bigint,
    name character varying(512),
    shortname character varying(64),
    subtitle character varying(1024),
    spec text,
    info text,
    intro text,
    photo bigint,
    video bigint,
    audio bigint,
    usethumbnail boolean DEFAULT true,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    namekey character varying(512),
    state integer DEFAULT 1,
    name_hash bigint DEFAULT 0,
    subtitle_hash bigint DEFAULT 0,
    info_hash bigint DEFAULT 0,
    intro_hash bigint DEFAULT 0,
    spec_hash bigint DEFAULT 0,
    otherjson text,
    otherjson_hash bigint DEFAULT 0,
    opens text,
    opens_hash bigint DEFAULT 0,
    audioautogenerate boolean DEFAULT true,
    audioauto boolean DEFAULT false
);


ALTER TABLE public.artworkrecord OWNER TO postgres;

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
    lastmodifieduser bigint NOT NULL,
    language character varying(24),
    draft text,
    audioautogenerate boolean DEFAULT true
);


ALTER TABLE public.artworktype OWNER TO postgres;

--
-- Name: audio_id; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id OWNER TO postgres;

--
-- Name: audio_id_137_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_137_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_137_seq OWNER TO postgres;

--
-- Name: audio_id_177_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_177_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_177_seq OWNER TO postgres;

--
-- Name: audio_id_342_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_342_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_342_seq OWNER TO postgres;

--
-- Name: audio_id_537_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_537_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_537_seq OWNER TO postgres;

--
-- Name: audio_id_552_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_552_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_552_seq OWNER TO postgres;

--
-- Name: audio_id_555_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_555_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_555_seq OWNER TO postgres;

--
-- Name: audio_id_564_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_564_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_564_seq OWNER TO postgres;

--
-- Name: audio_id_753_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_753_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_753_seq OWNER TO postgres;

--
-- Name: audio_id_860_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_860_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_860_seq OWNER TO postgres;

--
-- Name: audiostudio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.audiostudio (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    guidecontent_id bigint,
    artexhibitionguide_id bigint,
    info text,
    settings jsonb,
    settings_json json,
    audiospeechmusic bigint,
    audiospeech bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    audioautogenerate boolean DEFAULT true,
    language character varying(24) DEFAULT 'es'::character varying,
    name character varying(512),
    state integer DEFAULT 1,
    json_data jsonb,
    artexhibitionguiderecord_id bigint,
    artexhibitionitemrecord_id bigint,
    artexhibitionrecord_id bigint,
    artexhibitionsectionrecord_id bigint,
    artworkrecord_id bigint,
    guidecontentrecord_id bigint,
    institutionrecord_id bigint,
    personrecord_id bigint,
    siterecord_id bigint,
    audio_speech_hash bigint DEFAULT '-1'::integer,
    audio_speech_music_hash bigint DEFAULT '-1'::integer,
    musicurl character varying(4096)
);


ALTER TABLE public.audiostudio OWNER TO postgres;

--
-- Name: audit_id; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audit_id
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audit_id OWNER TO postgres;

--
-- Name: audit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.audit (
    id bigint DEFAULT nextval('public.audit_id'::regclass) NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    action bigint NOT NULL,
    objectclassname character varying(512) NOT NULL,
    objectid bigint NOT NULL,
    description text,
    json_data jsonb,
    descriptionkey character varying(1024)
);


ALTER TABLE public.audit OWNER TO postgres;

--
-- Name: awpe; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.awpe (
    artwork_id bigint NOT NULL,
    person_id bigint NOT NULL
);


ALTER TABLE public.awpe OWNER TO postgres;

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
    lastmodifieduser bigint NOT NULL,
    language character varying(24),
    draft text
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
    lastmodifieduser bigint NOT NULL,
    state integer DEFAULT 1,
    language character varying(24),
    draft text,
    audioautogenerate boolean DEFAULT true
);


ALTER TABLE public.floor OWNER TO postgres;

--
-- Name: floorrecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.floorrecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    floor_id bigint,
    language character varying(24) NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    audioautogenerate boolean DEFAULT true
);


ALTER TABLE public.floorrecord OWNER TO postgres;

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
    lastmodifieduser bigint NOT NULL,
    language character varying(24),
    draft text,
    audioautogenerate boolean DEFAULT true
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
    lastmodifieduser bigint NOT NULL,
    state integer DEFAULT 1,
    audiokey character varying(1024),
    videokey character varying(1024),
    language character varying(24) DEFAULT 'es'::character varying,
    draft text,
    masterlanguage character varying(24) DEFAULT 'es'::character varying,
    translation integer DEFAULT 0,
    usethumbnail boolean DEFAULT true,
    intro text,
    spec text,
    opens text,
    audioautogenerate boolean DEFAULT true,
    speechaudio bigint,
    audio_id bigint
);


ALTER TABLE public.guidecontent OWNER TO postgres;

--
-- Name: guidecontentrecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.guidecontentrecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    guidecontent_id bigint,
    language character varying(24) NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    namekey character varying(512),
    state integer DEFAULT 0,
    usethumbnail boolean DEFAULT true,
    name_hash bigint DEFAULT 0,
    subtitle_hash bigint DEFAULT 0,
    info_hash bigint DEFAULT 0,
    intro_hash bigint DEFAULT 0,
    spec text,
    spec_hash bigint DEFAULT 0,
    otherjson text,
    otherjson_hash bigint DEFAULT 0,
    opens text,
    opens_hash bigint DEFAULT 0,
    audioautogenerate boolean DEFAULT true,
    intro text,
    audioauto boolean DEFAULT false
);


ALTER TABLE public.guidecontentrecord OWNER TO postgres;

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
    lastmodifieduser bigint NOT NULL,
    state integer DEFAULT 1,
    language character varying(24) DEFAULT 'es'::character varying,
    draft text,
    masterlanguage character varying(24) DEFAULT 'es'::character varying,
    translation integer DEFAULT 0,
    usethumbnail boolean DEFAULT true,
    intro text,
    spec text,
    opens text,
    audioautogenerate boolean DEFAULT true,
    speechaudio bigint,
    zoneid character varying(512) DEFAULT 'America/Buenos_Aires'::character varying
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
    lastmodifieduser bigint NOT NULL,
    state integer DEFAULT 1,
    language character varying(24),
    draft text,
    audioautogenerate boolean DEFAULT true
);


ALTER TABLE public.institutionalcontent OWNER TO postgres;

--
-- Name: institutionrecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.institutionrecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    institution_id bigint,
    language character varying(24) NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    namekey character varying(512),
    state integer DEFAULT 0,
    intro text,
    usethumbnail boolean DEFAULT true,
    subtitle_hash bigint DEFAULT 0,
    name_hash bigint DEFAULT 0,
    info_hash bigint DEFAULT 0,
    intro_hash bigint DEFAULT 0,
    spec text,
    spec_hash bigint DEFAULT 0,
    otherjson text,
    otherjson_hash bigint DEFAULT 0,
    opens text,
    opens_hash bigint DEFAULT 0,
    audioautogenerate boolean DEFAULT true,
    audioauto boolean DEFAULT false
);


ALTER TABLE public.institutionrecord OWNER TO postgres;

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
    lastmodifieduser bigint NOT NULL,
    state integer DEFAULT 1,
    language character varying(24),
    draft text,
    audioautogenerate boolean DEFAULT true
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
    name character varying(512),
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
    lastmodifieduser bigint NOT NULL,
    sortlastfirstname character varying(2048),
    isex integer DEFAULT 1,
    webpage character varying(2048),
    state integer DEFAULT 1,
    language character varying(24),
    draft text,
    translation integer DEFAULT 0,
    masterlanguage character varying(24) DEFAULT 'es'::character varying,
    usethumbnail boolean DEFAULT true,
    intro text,
    spec text,
    opens text,
    audioautogenerate boolean DEFAULT true,
    speechaudio bigint
);


ALTER TABLE public.person OWNER TO postgres;

--
-- Name: personrecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.personrecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    person_id bigint,
    language character varying(24) NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    intro text,
    state integer DEFAULT 0,
    usethumbnail boolean DEFAULT true,
    name_hash bigint DEFAULT 0,
    subtitle_hash bigint DEFAULT 0,
    info_hash bigint DEFAULT 0,
    intro_hash bigint DEFAULT 0,
    spec text,
    spec_hash bigint DEFAULT 0,
    otherjson text,
    otherjson_hash bigint DEFAULT 0,
    opens text,
    opens_hash bigint DEFAULT 0,
    audioautogenerate boolean DEFAULT true,
    audioauto boolean DEFAULT false,
    lastname character varying(512)
);


ALTER TABLE public.personrecord OWNER TO postgres;

--
-- Name: privilege; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.privilege (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    state integer DEFAULT 1
);


ALTER TABLE public.privilege OWNER TO postgres;

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
    usethumbnail boolean DEFAULT true,
    size bigint DEFAULT 0,
    height integer DEFAULT 0,
    width integer DEFAULT 0,
    state integer DEFAULT 1,
    durationmilliseconds bigint DEFAULT 0,
    tag character varying(512),
    language character varying(24),
    draft text,
    audioautogenerate boolean DEFAULT false,
    audit text,
    filename character varying(2028)
);


ALTER TABLE public.resource OWNER TO postgres;

--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    state integer DEFAULT 1
);


ALTER TABLE public.role OWNER TO postgres;

--
-- Name: rolegeneral; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rolegeneral (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    state integer DEFAULT 1,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    key character varying(32)
);


ALTER TABLE public.rolegeneral OWNER TO postgres;

--
-- Name: roleinstitution; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roleinstitution (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    institution_id bigint NOT NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    state bigint DEFAULT 1 NOT NULL,
    key character varying(32)
);


ALTER TABLE public.roleinstitution OWNER TO postgres;

--
-- Name: roles_privileges; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles_privileges (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    role_id bigint NOT NULL,
    privilege_id bigint NOT NULL,
    state integer DEFAULT 1
);


ALTER TABLE public.roles_privileges OWNER TO postgres;

--
-- Name: rolesite; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rolesite (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    site_id bigint NOT NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    state bigint DEFAULT 1 NOT NULL,
    key character varying(32)
);


ALTER TABLE public.rolesite OWNER TO postgres;

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
    lastmodifieduser bigint NOT NULL,
    state integer DEFAULT 1,
    language character varying(24),
    draft text,
    audioautogenerate boolean DEFAULT true
);


ALTER TABLE public.room OWNER TO postgres;

--
-- Name: roomrecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roomrecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    room_id bigint,
    language character varying(24) NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    audioautogenerate boolean DEFAULT true
);


ALTER TABLE public.roomrecord OWNER TO postgres;

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
    lastmodifieduser bigint NOT NULL,
    state integer DEFAULT 1,
    language character varying(24),
    audioautogenerate boolean DEFAULT true
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
    introkey character varying(512),
    state integer DEFAULT 1,
    language character varying(24) DEFAULT 'es'::character varying,
    draft text,
    masterlanguage character varying(24) DEFAULT 'es'::character varying,
    translation integer DEFAULT 0,
    usethumbnail boolean DEFAULT true,
    spec text,
    audioautogenerate boolean DEFAULT true,
    zoneid character varying(512) DEFAULT 'America/Buenos_Aires'::character varying,
    speechaudio bigint,
    languages json,
    labelpermanentexhibitions character varying(1024) DEFAULT 'Colección permanente'::character varying,
    labeltemporaryexhibitions character varying(1024) DEFAULT 'Exhibiciones temporarias'::character varying,
    sortalphabetical boolean DEFAULT true
);


ALTER TABLE public.site OWNER TO postgres;

--
-- Name: siteartist; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.siteartist (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512),
    site_id bigint NOT NULL,
    artist_id bigint NOT NULL,
    state integer DEFAULT 1,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text
);


ALTER TABLE public.siteartist OWNER TO postgres;

--
-- Name: siterecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.siterecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    site_id bigint,
    language character varying(24) NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    photo bigint,
    video bigint,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    namekey character varying(512),
    state integer DEFAULT 0,
    intro text,
    name_hash bigint,
    subtitle_hash bigint,
    info_hash bigint,
    intro_hash bigint,
    usethumbnail boolean DEFAULT true,
    opens text,
    opens_hash bigint DEFAULT 0,
    abstract_hash bigint DEFAULT 0,
    abstract text,
    spec text,
    spec_hash bigint DEFAULT 0,
    address text,
    address_hash bigint DEFAULT 0,
    otherjson text,
    otherjson_hash bigint DEFAULT 0,
    audioautogenerate boolean DEFAULT true,
    audioauto boolean DEFAULT false,
    labeltemporaryexhibitions character varying(1024),
    labelpermanentexhibitions character varying(1024)
);


ALTER TABLE public.siterecord OWNER TO postgres;

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
    lastmodifieduser bigint NOT NULL,
    language character varying(24),
    draft text,
    audioautogenerate boolean DEFAULT true
);


ALTER TABLE public.sitetype OWNER TO postgres;

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    user_id bigint NOT NULL,
    general_role_id bigint,
    site_role_id bigint,
    institution_role_id bigint
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint DEFAULT nextval('public.sequence_user_id'::regclass) NOT NULL,
    name character varying(512),
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint,
    state integer DEFAULT 1,
    language character varying(24) DEFAULT 'es'::character varying,
    draft text,
    audioautogenerate boolean DEFAULT true,
    locale character varying(64) DEFAULT 'es'::character varying,
    zoneid character varying(512) DEFAULT 'America/Buenos_aires'::character varying,
    password character varying(512)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Data for Name: aa1; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.aa1 (artwork_id, artist_id, person_id) FROM stdin;
215	186	186
217	185	185
311	308	308
328	185	185
207	183	183
213	185	185
207	184	184
195	194	194
221	188	188
332	333	333
589	588	588
592	593	593
603	602	602
768	763	763
225	189	189
272	264	264
582	581	581
270	263	263
280	268	268
274	265	265
211	184	184
402	401	401
452	447	447
404	398	398
219	187	187
448	447	447
197	193	193
\.


--
-- Data for Name: artexhibition; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibition (id, name, namekey, title, titlekey, artexhibitionstatustype_id, site_id, subtitle, subtitlekey, info, infokey, website, mapurl, email, instagram, whatsapp, phone, twitter, photo, video, audio, permanent, fromdate, todate, created, lastmodified, lastmodifieduser, location, intro, introkey, opens, openskey, spec, shortname, shortnamekey, state, language, draft, masterlanguage, translation, usethumbnail, audioautogenerate, speechaudio, ordinal) FROM stdin;
233	Grandes obras del impresionismo	grandes-obras-del-impresionismo	Grandes obras del impresionismo	\N	\N	137		\N	La extensa colección de arte impresionista y postimpresionista del Museo incluye obras de artistas destacados, como Édouard Manet, Claude Monet, Camille Pissarro, Berthe Morisot, Edgar Degas, Vincent Van Gogh, Paul Gauguin y Henri Toulouse Lautrec, entre otros.	\N	https://www.bellasartes.gob.ar/coleccion/recorridos/grandes-obras-del-impresionismo_1/.	\N	\N	\N	\N	\N	\N	364	\N	\N	t	2026-05-19 00:00:00-03	2027-05-19 00:00:00-03	2025-05-19 12:29:43.192411-03	2025-11-23 21:55:04.61508-03	100	\N	La extensa colección de arte impresionista y postimpresionista del museo incluye obras de artistas destacados, como Édouard Manet, Claude Monet, Camille Pissarro, Berthe Morisot, Edgar Degas, Vincent Van Gogh, Paul Gauguin y Henri Toulouse Lautrec, entre otros.	\N	\N	\N	\N	\N	\N	3	es	\N	es	0	t	t	\N	0
362	Museo Secreto	museo-secreto	Museo Secreto	\N	\N	137	De la reserva a la sala	\N	Con la idea de expandir el universo de lo que se presenta al público, esta muestra despliega cerca de 300 obras provenientes de las reservas del Museo Nacional de Bellas Artes, realizadas por más de 250 artistas argentinos y extranjeros, desde el siglo XIV hasta la actualidad. Algunas de ellas han estado en las salas como parte de distintos guiones permanentes o en exposiciones temporarias, mientras que otras han tenido menos visibilidad.	\N	https://www.bellasartes.gob.ar/exhibiciones/museo-secreto/	\N	\N	\N	\N	\N	\N	367	\N	368	f	2025-05-10 00:00:00-03	2026-11-20 00:00:00-03	2025-06-09 17:55:14.336329-03	2025-11-23 21:52:32.935649-03	100	\N	El Bellas Artes inicia su programación anual con una muestra antológica que reúne en el Pabellón de exposiciones temporarias cerca de 300 obras provenientes de las reservas.	\N	Del 22 de enero de 2025 al 31 de agosto de 2025 \r\nPabellón de exposiciones temporarias	\N	\N	\N	\N	3	es	\N	es	0	t	t	\N	0
199	Obras Maestras	obras-maestras	Obras Maestras	\N	\N	137	\N	\N	La obra maestra era el nombre que recibía la pieza artesanal que debía realizar todo oficial que quisiera acceder a la categoría de maestro en el seno de los gremios.	\N	\N	\N	\N	\N	\N	\N	\N	638	\N	\N	t	2026-05-19 00:00:00-03	2027-05-19 00:00:00-03	2025-05-19 12:29:38.885286-03	2025-11-23 21:55:58.818251-03	100	\N	La obra maestra era el nombre que recibía la pieza artesanal que debía realizar todo oficial que quisiera acceder a la categoría de maestro en el seno de los gremios.	\N	\N	\N	\N	\N	\N	3	es	\N	es	0	t	t	\N	0
600	Tercer ojo	new	\N	\N	\N	342	Colección Costantini en Malba	\N	Se exhiben las obras maestras del museo –de artistas como Frida Kahlo, Diego Rivera, Tarsila do Amaral, Xul Solar, Joaquín Torres García, Emilio Pettoruti, Wifredo Lam, Roberto Matta, Maria Martins, Remedios Varo, Antonio Berni y Jorge de la Vega, entre otros–, junto con las grandes adquisiciones realizadas por Costantini en los últimos años. Muchas de estas piezas se presentan públicamente en esta ocasión luego de permanecer fuera del circuito internacional por varias décadas.\r\n\r\nLa muestra presenta obras clave de la modernidad latinoamericana como Autorretrato con chango y loro (1942) de Frida Kahlo, Armonía (Autorretrato sugerente) (1956) de Remedios Varo, Abaporu (1928) de Tarsila do Amaral, Baile en Tehuantepec (1928) de Diego Rivera, Omi Obini (1943) de Wifredo Lam, Las distracciones de Dagoberto (1945) de Leonora Carrington, Tocadora de banjo (1925) de Victor Brecheret, Autorretrato (1951) de Alice Rahon, y Cobra grande (1943) de Maria Martins.\r\n\r\nCurada por María Amalia García –curadora en jefe de Malba– Tercer ojo se despliega en el primer nivel del museo y está dividida en dos grandes núcleos conceptuales: Habitar y Transformar. Estos ejes sintonizan con nuestras preocupaciones actuales más acuciantes, como la sustentabilidad del planeta, las demandas sociales y los diversos modos de la subjetividad y la autorrepresentación. Son puertas de acceso que combinan abordajes temáticos, formales y materiales, y señalan las derivas de los movimientos artísticos latinoamericanos.\r\n\r\nEl título de la exposición, Tercer ojo, surge de la obra de Kahlo Diego y yo (1949), que formó parte de esta exposición hasta marzo de 2024. Representa la obsesión amorosa de la pareja de artistas y alude, además, a la visión intuitiva y definida que supone la creación de una colección.	\N	https://malba.org.ar/evento/tercer-ojo__coleccion-costantini_en-malba/	\N	\N	\N	\N	\N	\N	601	\N	\N	t	\N	\N	2025-09-19 21:48:44.424111-03	2025-09-25 16:51:35.088045-03	100	Nivel 1	Una exposición que reúne más de 220 obras icónicas del arte latinoamericano en un recorrido que pone en diálogo la Colección Malba y la de su fundador, Eduardo F. Costantini. La muestra propone el despliegue de un acervo en transformación que va cambiando de forma a lo largo del tiempo, iluminando los momentos claves del arte de la región en diálogo con temas artísticos y sociales tanto históricos como contemporáneos.	\N	Visitas guiadas\r\nMiércoles, viernes y sábados a las 16:00.\r\nIncluidas con el ticket de ingreso al museo.	\N	Curaduría\r\nMaría Amalia García	\N	\N	1	es	\N	es	0	t	t	\N	0
597	Carrie Bencardino. El desentierro del diablo	new	\N	\N	\N	342	\N	\N	El desentierro del diablo, curada por Carlos Gutiérrez, surge del reconocimiento de una posible crisis de la imaginación, alimentada por los vaivenes políticos y el avance de un tipo de pensamiento que tiende a disolver los vínculos entre las personas. Conformada mayormente por pinturas, la muestra busca transformar la sala en un espacio donde se cruzan distintos espacios: es casi un bar, casi un cine, casi un club. Del mismo modo, las pinturas funcionan de manera similar, mostrando situaciones que podrían suceder en cualquier lugar, real o fantástico. Para Bencardino, resulta imperante la necesidad de engendrar nuevas herramientas para construir otros mundos.\r\n\r\nEl trabajo de Bencardino se alimenta de imágenes encontradas en libros, revistas, tapas de discos, videoclips, internet y su archivo personal de objetos y otros materiales que circulan en la cultura de masas y sus plataformas. Sus referencias provienen de un imaginario afectivo muy particular: las estéticas de las comunidades queer y las adolescencias de su generación, los códigos visuales de las escenas contraculturales (como el punk, y los distintos subgéneros del metal), el comic y la ilustración (como Ciruelo, Victoria Francés, Luis Royo, Boris Vallejo y Magalí Villeneuve), y el imaginario fantástico literario (especialmente, de William Blake y J.R.R. Tolkien), entre muchas otras referencias. Procesa y distorsiona estas imágenes digitalmente, y a partir de esas nuevas imágenes elabora sus pinturas.\r\n\r\nTrabaja desde un posicionamiento crítico sobre la circulación y la reapropiación de las imágenes en la era contemporánea, donde las fuentes y referentes visuales se multiplican, se entremezclan y se transforman constantemente. En su pintura, la apropiación no es un fin en sí mismo, sino un medio para interrogar la memoria cultural, las iconografías compartidas y la relación afectiva que establecemos con las imágenes.\r\n\r\nLa exposición cuenta también con una pieza de video que es un monólogo sobre la incidencia de distintos artistas, el surrealismo y el pensamiento mágico en la configuración del imaginario de Bencardino. La muestra destaca así las conexiones entre la imaginación –o más precisamente, la capacidad de imaginar– y la política, involucrándose con la producción de imágenes y recuperando indicios pasados que pudieran torcer las ideas totalizantes del relato colectivo. Así, este proyecto intenta disputar lugares donde las ideas y el deseo de un horizonte común no nos encuentre del todo agotados.	\N	https://malba.org.ar/evento/carrie-bencardino-el-desentierro-del-diablo/	\N	\N	\N	\N	\N	\N	599	\N	\N	f	\N	\N	2025-09-19 20:31:16.484359-03	2025-10-24 23:33:03.394564-03	100	Nivel -1	El &quot;desentierro del diablo&quot; es una celebración ancestral del Carnaval jujeño en Argentina, que marca el inicio de los festejos con el desentierro simbólico del Pujllay (o espíritu del carnaval), una figura que representa la alegría y el espíritu festivo.	\N	Visitas guiadas\r\nLunes a las 17:00.\r\nIncluidas con el ticket de ingreso al museo.	\N	Curaduría\r\nCarlos Gutiérrez	El desentierro del diablo	\N	1	es	\N	es	0	t	t	\N	0
454	Sendas perdidas	sendas-perdidas	Sendas perdidas	\N	\N	137	\N	\N	Exposición temporal dedicada al artista argentino Germán Gárgano, que reunió más de 170 de sus obras (dibujos, acuarelas, tintas y gouaches) en el Museo Nacional de Bellas Artes. La muestra, curada por Pablo De Monte, exploró los universos visuales del artista, incluyendo ecos de su experiencia carcelaria, y se exhibió entre julio y agosto de 2025.	\N	https://www.bellasartes.gob.ar/exhibiciones/german-gargano/	\N	\N	\N	\N	\N	\N	466	\N	\N	f	2026-07-25 00:00:00-03	2028-07-25 00:00:00-03	2025-07-25 18:47:09.47387-03	2025-11-23 21:54:09.16539-03	100	\N	Germán Gárgano fue preso político en Argentina desde 1975 hasta 1982, y  mantuvo una relación epistolar desde la cárcel con el pintor Carlos Gorriarena, con quien continuaría formándose al recuperar su libertad.  “Sendas perdidas” es su primera muestra en el Museo Nacional de Bellas Artes, con más de 170 dibujos, gouaches, acuarelas y tintas realizadas en los últimos años.	\N	\N	\N	\N	\N	\N	3	es	\N	es	0	t	t	\N	0
774	Alberto Churba. Diseño Infinito	\N	\N	\N	\N	753	\N	\N	El Museo Nacional de Arte Decorativo presenta ALBERTO CHURBA. DISEÑO INFINITO, la primera retrospectiva dedicada a este destacado diseñador y artista decorativo argentino. \r\n\r\nEntre las décadas de 1960 y 1990 lideró el Estudio CH, firma que transformó la escena de la decoración en el país.\r\n\r\nLa muestra, curada por Sandra Hillar y Wustavo Quiroga, revisa su legado con un enfoque especial en diversas áreas —textiles, alfombras, vidrios, objetos y mobiliario—, todas reconocidas por su elevada calidad plástica y técnica. Como ejemplo de su genio creador, el célebre sillón Cinta forma parte de las colecciones del MoMA de Nueva York y del Victoria &amp;amp;amp;amp;amp;amp;amp; Albert Museum de Londres, entre otras instituciones.\r\n\r\nLos diseños de Alberto Churba marcaron una época y continúan siendo una referencia audaz para las nuevas generaciones. Desde el ámbito familiar, impulsó una práctica de diseño y emprendimiento que trascendió su apellido, dejando una huella indiscutible en la cultura argentina.\r\n\r\nLa exposición reunirá numerosas piezas de colecciones privadas e institucionales que, por primera vez, se activarán junto al autor para narrar su trayectoria, su proceso creativo y el impacto de su obra en la historia del diseño nacional.	\N	https://museoartedecorativo.cultura.gob.ar/exhibicion/alberto-churba-diseno-infinito/	\N	\N	\N	\N	\N	\N	781	\N	\N	f	\N	\N	2025-10-29 13:25:50.663635-03	2025-10-29 19:11:10.734376-03	100	\N	El Museo Nacional de Arte Decorativo presenta una exposición homenaje a Alberto Churba, referente indiscutido del diseño nacional. Alberto Curba. Diseño Infinito invita a explorar su universo creativo a través de piezas emblemáticas que marcaron un antes y un después en la historia del diseño en América Latina.	\N	Mié a dom | 13 a 19 h | Entrada libre y gratuita	\N	\N	Diseño Infinito	\N	\N	es	\N	es	\N	f	f	\N	0
757	Viaggio in Italia: 1920-1950, La Edad de Oro del Afiche Turístico Italiano	\N	\N	\N	\N	753	El diseño italiano, desde los artificios del Art Déco hasta el diseño gráfico Mid-Century	\N	El Instituto Italiano de Cultura de Buenos Aires (IIC) y el Museo Nacional de Arte Decorativo presentan la exposición "Viaggio in Italia.\r\nLa edad de oro del afiche turístico italiano (1520-1550)" una muestra que invita a recorrer la evolución de la gráfica publicitaria italiana dedicada al turismo a través de una extraordinaria selección de afiches originales provenientes de la colección de Alessandro Bellenda.\r\n\r\nRealizados entre las décadas de 1920 y 1950, los afiches exhibidos fueron producidos por el ENIT (Ente Nacional Italiano para el Turismo) y las Ferrovie dello Stato (ferrocarriles estatales), organismos que promovieron una nueva estética visual para dar a conocer Italia al mundo.\r\n\r\nLas obras llevan la firma de grandes maestros del cartelismo italiano como Marcello Dudovich, Mario Borgoni, Gino Boccasile, Aurelio Craffonara y Franz Lenhart, entre otros.\r\n\r\nMediante el uso de colores vibrantes, figuras idealizadas y escenarios icónicos, estos artistas crearon un verdadero canon estético del viaje turístico en Italia, consolidando una imagen del país que aún hoy sigue inspirando a diseñadores y creativos, reseña una comunicación del IIC.\r\n\r\nMás allá de su valor artístico, la exposición ofrece una lectura histórica y sociológica: los afiches no solo narran la evolución del diseño gráfico italiano, sino también la construcción del imaginario del "bel paese" (país hermoso) y las formas en que Italia se presentó al público internacional durante un período de intensas transformaciones sociales y culturales.\r\n\r\n"Viaggio in Italia" se inaugura en el marco de la Semana de la Lengua Italiana en el Mundo, importante cita anual que se desarrolla globalmente a través de la red de Institutos Italianos de Cultura y Embajadas de Italia, y que celebra el idioma y la creatividad italiana en sus múltiples expresiones.\r\n \r\nLa muestra se presenta en dos sedes: el Museo Nacional de Arte Decorativo, donde se exhibe el núcleo principal, y la Sala Roma del Istituto Italiano di Cultura, que albergará una selección complementaria de piezas gráficas.	\N	\N	https://maps.app.goo.gl/XHiRj9nd8zt8VTBv8	\N	\N	\N	\N	\N	759	\N	\N	f	2025-10-20 00:00:00-03	2026-01-20 00:00:00-03	2025-10-28 14:59:23.944328-03	2025-10-30 13:53:26.562168-03	100	\N	Exposición temporaria del Museo Nacional de Arte Decorativo coproducida con el Instituto Italiano de Cultura, que celebra la historia del afiche turístico.\r\nUna muestra que reúne 70 afiches creados entre 1920 y 1950 por destacados ilustradores italianos como Marcello Dudovich, Mario Borgoni, Gino Boccasile, Aurelio Craffonara y Franz Lenhart. Obras que marcaron la identidad visual de toda una época y difundieron la imagen de Italia en el mundo.	\N	Del 20 Oct. 2025\r\nHasta el 20 Enero 2026	\N	\N	Viaggio in Italia	\N	\N	es	\N	es	\N	f	f	\N	0
839	borrar esta new	\N	\N	\N	\N	537	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	f	\N	\N	2025-11-23 21:44:14.807191-03	2025-11-23 21:45:18.120577-03	100	\N	\N	\N	\N	\N	\N	\N	\N	5	es	\N	es	\N	t	f	\N	0
830	newsss	\N	\N	\N	\N	537	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	f	\N	\N	2025-11-23 19:51:03.61056-03	2025-11-26 11:43:46.776796-03	100	\N	\N	\N	\N	\N	\N	ddd	\N	5	es	\N	es	\N	t	f	\N	0
864	Wifredo Lam, cuando no duermo, sueño	\N	\N	\N	\N	860	Arte y descolonización	\N	Las pinturas de Wifredo Lam expandieron los horizontes del modernismo al crear un espacio significativo para la belleza y la profundidad de la cultura de la diáspora negra. Nacido en Cuba a principios del siglo XX, Lam forjó sus convicciones políticas y su compromiso con la pintura moderna en la Europa devastada por la guerra en la década de 1930. Su exilio y regreso al Caribe tras 18 años en el extranjero lo impulsaron a reimaginar radicalmente su proyecto artístico a través de las historias afrocaribeñas.\r\n\r\nPara Lam, de ascendencia africana y china, la creación de su nuevo y vívido imaginario fue más que una forma de autorreflexión. Declaró célebremente que su arte era un "acto de descolonización". Sus experimentos formales, sus figuras y paisajes transformadores, y su afinidad por la poesía y la colaboración le permitieron romper con las estructuras coloniales que encontró en el arte y en la vida. “Sabía que corría el riesgo de no ser comprendido ni por el hombre de la calle ni por los demás”, dijo Lam, “pero una imagen verdadera tiene el poder de poner a trabajar la imaginación, aunque lleve tiempo”. Wifredo Lam: When I Don’t Sleep, I Dream es la primera retrospectiva en Estados Unidos que presenta la trayectoria completa de la extraordinaria visión de Lam, invitándonos a ver el mundo de una manera nueva.	\N	https://www.moma.org/calendar/exhibitions/5877	\N	\N	\N	\N	\N	\N	19611	\N	\N	f	2025-10-20 00:00:00-03	2026-01-20 00:00:00-03	2025-11-28 10:51:25.218691-03	2026-01-04 17:59:31.491565-03	100	MoMA, Floor 3, 3 East\r\nThe Robert B. Menschel Galleries	Primera retrospectiva en Estados Unidos en presentar la trayectoria completa de la extraordinaria visión de Wilfredo Lam (1902-1982), invitándonos a ver el mundo de una manera nueva. Lam declaró célebremente que su arte era un "acto de descolonización".	\N	En horarios que está abierto el Museo.	\N	\N	Wifredo Lam	\N	1	es	\N	es	\N	t	f	\N	0
875	Colección 1880s-1940s	\N	\N	\N	\N	860	Arte moderno latinoamericano	\N	Organizadas en un orden cronológico aproximado, cada una de las presentaciones de esta planta explora un tema específico. Una sala puede estar dedicada a un artista, un medio o disciplina específicos, un lugar particular en un momento histórico o una idea creativa compartida. Un programa continuo de reinstalación frecuente presentará una amplia gama de obras de arte en nuevas combinaciones, un recordatorio de que se pueden explorar innumerables ideas e historias a través de la dinámica colección del Museo.	\N	\N	\N	\N	\N	\N	\N	\N	17171	\N	\N	t	\N	\N	2025-11-28 21:32:20.050863-03	2025-11-28 21:32:20.050889-03	100	Piso 5	Organizadas en un orden cronológico aproximado, cada una de las presentaciones de esta planta explora un tema específico. Una sala puede estar dedicada a un artista, un medio o disciplina específicos, un lugar particular en un momento histórico o una idea creativa compartida	\N	\N	\N	\N	\N	\N	1	es	\N	es	\N	t	f	\N	0
284	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	arte-argentino-siglo-xix-hacia-la-consolidaci-n-de-un-modelo-nacional	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	arte-argentino-siglo-xix-hacia-la-consolidaci-n-de-un-modelo-nacional	\N	137		\N	La consolidación de un modelo nacional en el arte argentino fue un proceso gradual que se dio en el siglo XIX y principios del XX, marcado por la búsqueda de una identidad visual propia y la creación de instituciones que promoviesen el arte nacional. Se buscó reflejar la realidad argentina, las costumbres locales y la historia del país, diferenciándose de las influencias europeas.\\n En la segunda mitad del siglo XIX, el arte argentino se caracterizó por el retrato, donde los artistas plasmaban a los personajes relevantes de la nueva nación, siguiendo los cánones neoclásicos de la época. \\n Luego a fines del siglo XIX y principios del XX, se buscó una identidad visual propia, con la creación de instituciones como El Ateneo, donde literatos y artistas debatiían sobre la existencia de un "arte nacional".	\N	https://www.bellasartes.gob.ar	\N	\N	\N	\N	\N	\N	363	\N	\N	t	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	2025-11-23 21:54:24.078937-03	100	Planta baja sala 20	La consolidación de un modelo nacional en el arte argentino fue un proceso que se dió a fines del siglo XIX y principios del XX, marcado por la búsqueda de una identidad visual propia y la creación de instituciones que promoviesen el arte nacional. Se buscó reflejar la realidad argentina, las costumbres locales y la historia del país, diferenciándose de las influencias europeas.	\N	\N	\N	\N	\N	\N	3	es	\N	es	0	t	t	\N	0
870	Odili Donald Odita	\N	\N	\N	\N	860	Canciones de la vida	\N	En el nuevo protecto a gran escala de Odili Donald Odita, colores brillantes y patrones abstractos crean un caleidoscopio en cascada. Y por primera vez en el proceso de desarrollo del artista, la música es su principal fuente de inspiración. "La música me inspira a reflexionar sobre los problemas en mis pinturas", ha declarado Odita.\r\n\r\nCanciones de la Vida se pintó a lo largo de seis semanas, y los visitantes pudieron ver cómo la obra, específica para el lugar, se extendía por las paredes y columnas. Realizada con pintura acrílica de látex mate, la instalación, que abarca desde el suelo hasta el techo, ofrece una experiencia inmersiva, y Odita describe sus colores como expresiones de libertad y cambio. \r\n\r\nCada sección pintada se basa en una selección de canciones que amplían la concepción del artista del lugar como un espacio de encuentro para personas de diferentes ámbitos. "Hay una especie de energía comunitaria debido a la forma en que la música puede afectar al cuerpo", ha declarado Odita. "Así es como quiero que mis pinturas funcionen también".	\N	\N	\N	\N	\N	\N	\N	\N	874	\N	\N	f	2025-10-20 00:00:00-03	2026-04-24 00:00:00-03	2025-11-28 12:18:14.011324-03	2025-11-28 12:18:14.011344-03	100	MLM, Piso 1	En el nuevo proyecto a gran escala de Odili Donald Odita colores brillantes y patrones abstractos crean un caleidoscopio en cascada. Y por primera vez en el proceso de desarrollo del artista, la música es su principal fuente de inspiración. "La música me inspira a reflexionar sobre los problemas en mis pinturas", ha declarado Odita.	\N	\N	\N	\N	\N	\N	1	es	\N	es	\N	t	f	\N	0
27921	El mundo del trabajo en la colonia	\N	\N	\N	\N	537	\N	\N	En Buenos Aires, la élite estaba formada por comerciantes y funcionarios. Los religiosos y aquellos que ejercían ciertas profesiones, como abogados, médicos y pequeños comerciantes, no pertenecían a la élite pero tampoco eran parte de “la plebe”. Esta estaba integrada por quienes hacían trabajos manuales y no especializados: jornaleros (empleados en tareas de la construcción o el puerto), vendedores ambulantes, repartidores, peones o criados, quienes se dedicaban a lavar o planchar, etcétera.\r\n\r\nLa población esclavizada era muy numerosa. La conformaban, en su gran mayoría, personas africanas y afrodescendientes. Las tareas que realizaban consistían principalmente en la elaboración de manufacturas y las labores domésticas.\r\n\r\nEn general, en las colecciones de los museos se conservan pocos testimonios materiales de los sectores populares. Sin embargo, los objetos que pertenecieron a las élites no solo hablan de ellas: concentran relaciones sociales y de poder.	\N	\N	\N	\N	\N	\N	\N	\N	29858	\N	\N	t	\N	\N	2026-01-10 18:23:36.131098-03	2026-01-10 18:23:36.131105-03	100	Planta baja.\r\nSalta 2	\N	\N	\N	\N	\N	\N	\N	1	es	\N	es	\N	t	t	\N	1
27577	El Cabildo de Buenos Aires	\N	\N	\N	\N	537	\N	\N	La historia del Cabildo comenzó en 1580. Ese año, Juan de Garay fundó la ciudad de La Trinidad, que más tarde pasó a llamarse por el nombre de su puerto: Santa María del Buen Ayre.\r\n\r\nLos cabildos se originaron en España como forma de administración de las ciudades y sus alrededores. Luego fueron trasladados a los dominios americanos. Según las Leyes de Indias, que regulaban la vida en los territorios del Imperio español, toda ciudad debía contar con un Cabildo. Este se ubicaba siempre en la plaza principal, junto a otras instituciones importantes, como la iglesia mayor. En Buenos Aires contenía, además, la cárcel urbana.\r\n\r\nEl Cabildo era la única autoridad elegida por la sociedad local. Los virreyes, los gobernadores y otros funcionarios importantes eran nombrados desde España. En cambio, los miembros del Cabildo representaban a los habitantes de Buenos Aires. Solo los llamados "vecinos" (varones, blancos y con prestigio social) podían integrarlo. Los cabildantes se reunían periódicamente para discutir asuntos importantes para la comunidad.\r\n\r\nHacia fines del siglo XVIII, Buenos Aires pasó de ser una ciudad marginal del Imperio español a convertirse en la capital del Virreinato del Río de la Plata. Su importancia creciente implicó que el Cabildo asumiera cada vez más facultades.\r\n\r\nEl Cabildo tuvo un papel político fundamental en los años posteriores a la revolución de 1810. En 1821, fue disuelto. Desde entonces, la organización de la ciudad quedó en manos de la Legislatura de Buenos Aires, creada un año antes.	\N	\N	\N	\N	\N	\N	\N	\N	30290	\N	\N	t	\N	\N	2026-01-10 18:19:31.083043-03	2026-01-13 15:22:34.922734-03	100	Primer piso.\r\nSala 6.	\N	\N	\N	\N	\N	\N	\N	3	es	\N	es	\N	t	t	\N	0
28796	Cabildos, afectos patrióticos	\N	\N	\N	\N	537	\N	\N	Durante la época colonial, la fundación de una nueva ciudad en el territorio americano iba acompañada por la constitución de un cabildo, sede de la administración general de la ciudad y representante legal de la misma, ante el reinado español.\r\n\r\nEn el siglo XVIII, el proceso conocido como “reformas borbónicas” modificó muchas de las atribuciones y funcionamientos de los cabildos, los cuales continuaron siendo sedes de la administración general.\r\n\r\nLas grandes ciudades construyeron importantes edificios para sus cabildos. La ciudad de Buenos Aires, fundada en 1580, tuvo una primera sede del Cabildo —de dos habitaciones— en 1608.\r\n\r\nEn 1725 se inició la construcción del edificio de once arcos y su obra finalizó cuarenta años después.\r\n\r\nLos “aires modernistas” de fines del siglo XIX y comienzos del XX modificaron su fachada. Durante 1940 se reconstruyó y restauró, tal como hoy lo conocemos, con un fin cultural, patriótico y representativo.\r\n\r\nEste Cabildo es motivo de grandes emociones que recordamos, que nos invaden… Y nos transportan a ese pasado épico al evocar aquellas gloriosas jornadas de mayo.\r\n\r\nEl Cabildo es símbolo de nuestra historia, de nuestros vínculos, de nuestros afectos y de nuestra nación.	\N	\N	\N	\N	\N	\N	\N	\N	32034	\N	\N	f	2025-10-20 00:00:00-03	2027-01-20 00:00:00-03	2026-01-10 18:36:04.133966-03	2026-01-13 04:53:13.917071-03	100	Pabellón de exhibiciones temporarias.\r\nPlanta baja.	\N	\N	\N	\N	\N	\N	\N	3	es	\N	es	\N	t	t	\N	0
37404	Los Primeros Pasos. El tiempo de las Invasiones Inglesas, antesala de la Revolución	\N	\N	\N	\N	537	\N	\N	Durante el proceso histórico de la Reconquista (1806) y la Defensa (1807), las milicias voluntarias fortalecieron su sentido de pertenencia y de defensa de la ciudad. La muestra es una invitación a recorrer algunas de las figuras destacadas de aquel período a través de retratos en óleo sobre tela, objetos, banderas, entre otros elementos.	\N	\N	\N	\N	\N	\N	\N	\N	37553	\N	\N	f	2024-08-01 00:00:00-03	2024-11-10 00:00:00-03	2026-01-12 16:03:42.465267-03	2026-01-13 04:56:47.491806-03	100	\N	\N	\N	Miércoles a domingos de 10.30 a 18 h.	\N	\N	Los Primeros Pasos	\N	3	es	\N	es	\N	t	t	\N	0
28384	Las invasiones inglesas al Río de la Plata	\N	\N	\N	\N	537	\N	\N	Entre 1806 y 1807 el Río de la Plata fue escenario de las Invasiones Inglesas: un conjunto de operaciones militares que implicaron a Buenos Aires y Montevideo. Aunque inicialmente ambas ciudades cayeron bajo dominio inglés, fueron recuperadas por las fuerzas locales.\r\n\r\nEl interés británico en estos territorios era principalmente económico: frente al control napoleónico de Europa continental, Inglaterra necesitaba abrir nuevos mercados para sus manufacturas, y las ciudades del Río de la Plata tenían una ubicación estratégica.\r\n\r\nEn Buenos Aires, las Invasiones Inglesas evidenciaron las profundas fallas del Virreinato: ni el virrey ni las tropas regulares fueron capaces de defender la ciudad, y esa tarea recayó en manos de sus habitantes. La Reconquista de Buenos Aires (en 1806) y su Defensa (en 1807) contaron con una amplia y diversa participación popular. Uno de sus aspectos destacados fue la conformación de milicias locales, en las que la plebe desempeñó un rol fundamental.\r\n\r\nLos episodios de la Reconquista y la Defensa de Buenos Aires ocupan un lugar importante en la historia patria. A partir de aquellos sucesos, se construyó un relato épico sobre la resistencia local frente al invasor extranjero.	\N	\N	\N	\N	\N	\N	\N	\N	29025	\N	\N	t	\N	\N	2026-01-10 18:34:21.528244-03	2026-01-13 04:56:22.222077-03	100	Pimer piso.\r\nSala 4.	\N	\N	\N	\N	\N	\N	\N	3	es	\N	es	\N	t	t	\N	4
37829	El Patio del Cabildo	\N	\N	\N	\N	537	\N	\N	En el patio puede observarse un aljibe de estilo barroco americano, de 1835, que pertenecía a la casa donde nació y murió Manuel Belgrano (que estaba en la actual avenida Belgrano 430, antes llamada Santo Domingo), político, militar y creador de la Bandera Argentina y otro, de estilo neoclásico, cuya placa afirma que perteneció al solar de Venezuela 1070, que desde 1855 perteneció a Doña Mercedes López de Osornio de Chaves (hermana de Agustina López de Osornio, que fue la madre de Rosas.	\N	\N	\N	\N	\N	\N	\N	\N	37908	\N	\N	t	\N	\N	2026-01-13 04:38:25.551133-03	2026-01-13 04:55:58.776705-03	100	\N	\N	\N	\N	\N	\N	\N	\N	3	es	\N	es	\N	t	t	\N	7
28498	Semana de Mayo	\N	\N	\N	\N	537	\N	\N	Los acontecimientos de mayo de 1810 se dieron en un clima de agitación generalizada que fue el resultado de una crisis de legitimidad de las autoridades virreinales.\r\n\r\nEn 1808 Francia había invadido España. Napoleón Bonaparte había obligado al rey Fernando VII a abdicar y lo mantenía prisionero. En ese contexto, en distintas ciudades españolas se habían formado juntas de gobierno para conservar la soberanía del rey legítimo, que pronto se agruparon en una Junta Central en Sevilla. El estado de efervescencia se trasladó a las ciudades americanas, que en algunos casos también intentaron formar juntas de gobierno. Para controlar la situación, en julio de 1809 la Junta Central nombró a Baltasar Hidalgo de Cisneros como nuevo virrey del Río de la Plata. Pero en enero de 1810, cuando Napoleón logró controlar gran parte del territorio español peninsular, la Junta Central fue disuelta.\r\n\r\nLa noticia llegó al Río de la Plata en mayo de 1810 y puso en crisis la autoridad de Cisneros. Los habitantes de la ciudad comenzaron a organizarse y pidieron que se celebrara un Cabildo abierto para discutir la situación. Este se concretó el 22 de mayo: tras un debate acalorado entre los vecinos, se decidió destituir al virrey y dejar la soberanía en manos del Cabildo hasta que se formara una junta de gobierno. En los días siguientes, se discutió quiénes serían los integrantes de esa junta, y el propio virrey fue propuesto para presidirla.\r\n\r\nEsa alternativa causó rechazo en buena parte de la población. En la mañana del 25 de mayo, una multitud se reunió frente al Cabildo acompañada por el cuerpo de Patricios. Presentó un petitorio con más de 400 firmas en el que se detallaban los integrantes propuestos para formar la junta. La movilización callejera fue decisiva: ante la presión popular y miliciana, el Cabildo aceptó los nombres. Ese día, la Primera Junta de Gobierno juró en esta sala. Se había producido un cambio muy importante: un gobierno nombrado sin intervención de España.	\N	\N	\N	\N	\N	\N	\N	\N	30582	\N	\N	t	\N	\N	2026-01-10 18:34:52.816511-03	2026-01-13 04:56:58.485248-03	100	\N	\N	\N	\N	\N	\N	\N	\N	3	es	\N	es	\N	t	t	\N	5
28035	La cárcel del Cabildo	\N	\N	\N	\N	537	\N	\N	La ciudad de Buenos Aires fue fundada por segunda vez en 1580, pero recién en 1608 tuvo su primera cárcel. Ubicada en el edificio del Cabildo, esta fue muy importante durante el período colonial y las primeras décadas independientes. Llegó a tener hasta cinco calabozos. Aunque el Cabildo fue disuelto en 1821, la cárcel siguió funcionando hasta 1877, cuando fue reemplazada por la Penitenciaría Nacional.\r\n\r\nLa función principal de la cárcel era la guarda temporal de las personas acusadas de haber cometido delitos que todavía no habían sido sentenciadas. Es decir, la reclusión no era considerada en sí misma una pena. Sin embargo, las lamentables condiciones de vida en los calabozos hicieron que la custodia de los detenidos se transformara en un verdadero castigo anticipado. Eso provocó actos de resistencia, desde fugas individuales y colectivas hasta la escritura de peticiones dirigidas a las máximas autoridades, como el virrey.\r\n\r\nLas penas que finalmente se aplicaban eran el destierro, el presidio, los trabajos forzados, los azotes o la muerte. Los acusados eran juzgados no solo por sus actos, sino por sus características personales y su modo de vida. También podían ser torturados legalmente. No se imponían las mismas penas a los pobres que a los poderosos: el funcionamiento de la cárcel era un reflejo de la sociedad colonial, donde la desigualdad entre las personas estaba avalada por la ley.\r\n\r\nAdemás de ser un espacio de custodia o de castigo anticipado, la cárcel se usaba como método de coacción. Por ejemplo, se enviaba allí a los deudores o a los hombres que habían incumplido una promesa de matrimonio. También tenía una función correctiva: muchas veces se encerraba a sujetos que no habían cometido ningún delito, pero que eran considerados desobedientes por sus tutores o dueños. Hijos, esposas y esclavos eran encarcelados por poco tiempo por padres, maridos y amos sin que se abriera ningún proceso judicial ni se respetara el derecho de defensa.	\N	\N	\N	\N	\N	\N	\N	\N	29477	\N	\N	t	\N	\N	2026-01-10 18:24:11.857962-03	2026-01-13 04:56:10.877505-03	100	\N	\N	\N	\N	\N	\N	\N	\N	3	es	\N	es	\N	t	t	\N	3
28682	Los años revolucionarios	\N	\N	\N	\N	537	\N	\N	Los acontecimientos de mayo de 1810 se dieron en un clima de agitación generalizada que fue el resultado de una crisis de legitimidad de las autoridades virreinales.\r\n\r\nEn 1808 Francia había invadido España. Napoleón Bonaparte había obligado al rey Fernando VII a abdicar y lo mantenía prisionero. En ese contexto, en distintas ciudades españolas se habían formado juntas de gobierno para conservar la soberanía del rey legítimo, que pronto se agruparon en una Junta Central en Sevilla. El estado de efervescencia se trasladó a las ciudades americanas, que en algunos casos también intentaron formar juntas de gobierno. Para controlar la situación, en julio de 1809 la Junta Central nombró a Baltasar Hidalgo de Cisneros como nuevo virrey del Río de la Plata. Pero en enero de 1810, cuando Napoleón logró controlar gran parte del territorio español peninsular, la Junta Central fue disuelta.\r\n\r\nLa noticia llegó al Río de la Plata en mayo de 1810 y puso en crisis la autoridad de Cisneros. Los habitantes de la ciudad comenzaron a organizarse y pidieron que se celebrara un Cabildo abierto para discutir la situación. Este se concretó el 22 de mayo: tras un debate acalorado entre los vecinos, se decidió destituir al virrey y dejar la soberanía en manos del Cabildo hasta que se formara una junta de gobierno. En los días siguientes, se discutió quiénes serían los integrantes de esa junta, y el propio virrey fue propuesto para presidirla.\r\n\r\nEsa alternativa causó rechazo en buena parte de la población. En la mañana del 25 de mayo, una multitud se reunió frente al Cabildo acompañada por el cuerpo de Patricios. Presentó un petitorio con más de 400 firmas en el que se detallaban los integrantes propuestos para formar la junta. La movilización callejera fue decisiva: ante la presión popular y miliciana, el Cabildo aceptó los nombres. Ese día, la Primera Junta de Gobierno juró en esta sala. Se había producido un cambio muy importante: un gobierno nombrado sin intervención de España.	\N	\N	\N	\N	\N	\N	\N	\N	30693	\N	\N	t	\N	\N	2026-01-10 18:35:13.855121-03	2026-01-13 04:56:34.987721-03	100	\N	\N	\N	\N	\N	\N	\N	\N	3	es	\N	es	\N	t	t	\N	6
\.


--
-- Data for Name: artexhibitionguide; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionguide (id, name, namekey, title, titlekey, publisher_id, artexhibition_id, artexhibitionguideorder, subtitle, subtitlekey, official, info, infokey, photo, video, audio, created, lastmodified, lastmodifieduser, opens, openskey, state, language, draft, masterlanguage, translation, usethumbnail, intro, spec, audioautogenerate, speechaudio, audio_id, ordinal) FROM stdin;
202	Obras Maestras	obras-maestras	Obras Maestras	\N	180	199	0	\N	\N	f	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	\N	\N	1	es	\N	es	0	t	\N	\N	f	\N	31	0
292	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	\N	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	\N	180	284	0	\N	\N	t	\N	\N	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	\N	\N	1	es	\N	es	0	t	\N	\N	f	\N	32	0
624	Carrie Bencardino El desentierro del diablo	carrie-bencardino-el-desentierro-del-diablo	\N	\N	180	597	0	\N	\N	f	\N	\N	\N	\N	\N	2025-09-25 11:10:46.040436-03	2025-09-25 11:10:46.040452-03	100	\N	\N	1	es	\N	es	0	t	\N	\N	f	\N	33	0
458	Sendas perdidas	\N	Sendas perdidas	\N	180	454	0	\N	\N	t	Germán Gárgano es un pintor argentino. Nace en 1953, en Buenos Aires, Argentina. Cursando sus estudios de medicina es detenido por razones políticas en 1975, situación que se prolonga hasta fines de 1982.	\N	\N	\N	817	2025-07-25 18:47:09.47387-03	2025-11-19 16:41:25.891306-03	100	\N	\N	1	es	\N	es	0	t	\N	\N	f	\N	34	0
786	Viaggio in Italia: 1920-1950, La Edad de Oro del Afiche Turístico Italiano	\N	\N	\N	\N	757	0	El diseño italiano, desde los artificios del Art Déco hasta el diseño gráfico Mid-Century	\N	t	La muestra comprende tres décadas atravesados por el nacimiento y auge del fascismo y la  figura de Benito Mussolini, la alianza con la Alemania nazi durante la Segunda Guerra Mundial, y la posguerra.\r\n\r\nEntre 1920 y 1950, el turismo en Italia se transformó en un fenómeno de masas y el afiche fue una herramienta clave:  síntesis entre arte, diseño y promoción cultural, pieza publicitaria que a la vez era obra de arte que reflejaba la cultura y la estética de su tiempo.\r\n\r\nEn la década del 20 se produce una transición desde el eclecticismo, el Liberty o Art Nouveau de principios de siglo para pasar en los 30 a un estilo modernista más sobrio con influencia de vanguardias europeas, como el cubismo, y especialmente el Futurismo italiano, un movimiento artístico fundado por Filippo Tommaso Marinetti  que se caracterizó por la exaltación de la velocidad, la tecnología, la modernidad, la violencia y el dinamismo, rechazando las tradiciones y el pasado.\r\n\r\nDurante el fascismo, estas obras turísticas escaparon al control de la censura, pero no de la influencia de la promoción política. Al igual que en Alemania y la Unión Soviética la vanguardia artística que exaltaba el nuevo hombre resultó demasiado libre; en Italia cedió su lugar al “realismo del fascismo”, un arte similar al realismo soviético, que se llamó “el retorno al orden”. \r\n\r\nLa muestra incluye obras de ilustradores italianos de gran renombre, como Marcello Dudovich y Mario Borgoni, que jugaron un papel crucial en la creación de una identidad turística para el país; y también de Marcello Nizzoli, quién en la postguerra diseñaría las célebres máquinas de escribir portátiles Olivetti.	\N	\N	\N	790	2025-10-30 13:44:50.917557-03	2025-10-30 14:56:07.855997-03	100	\N	\N	1	es	\N	es	\N	f	\N	\N	f	\N	36	0
248	Grandes obras del Impresionismo	\N	Grandes obras del Impresionismo	\N	180	233	0	\N	\N	t	\N	\N	\N	\N	637	2025-05-19 12:29:43.192411-03	2025-10-02 14:51:27.613198-03	100	\N	\N	3	es	\N	es	0	t	\N	\N	f	\N	35	0
894	Wifredo Lam, cuando no duermo, sueño	\N	\N	\N	\N	864	0	\N	\N	t	Se trata de la retrospectiva más extensa dedicada al artista en Argentina, abarcando las seis décadas de la prolífica carrera de Lam.\r\nLa exposición incluye más de 130 obras de arte de las décadas de 1920 a 1970, incluyendo pinturas, obras a gran escala sobre papel, dibujos colaborativos, libros ilustrados, grabados, cerámicas y material de archivo, con préstamos clave del Estate of Wifredo Lam, París. La retrospectiva revela cómo Lam, un artista nacido en Cuba que pasó la mayor parte de su vida en España, Francia e Italia, llegó a encarnar la figura del artista transnacional en el siglo XX.	\N	\N	\N	911	2025-11-29 07:51:58.975092-03	2025-11-29 07:51:58.975099-03	100	\N	\N	1	es	\N	es	\N	t	\N	\N	f	\N	1	0
799	Alberto Churba. Diseño Infinito	\N	\N	\N	\N	774	0	\N	\N	t	La muestra “Diseño Infinito” presenta 40 años de producción del reconocido diseñador argentino, con piezas históricas que van desde óleos a diferentes objetos, textiles y mobiliario de autor.	\N	\N	\N	\N	2025-11-11 21:50:04.54671-03	2025-11-11 21:50:36.325046-03	100	\N	\N	1	es	\N	es	\N	f	\N	\N	f	\N	37	0
411	Museo Secreto	\N	Museo Secreto. Del archivo a la sala	\N	180	362	0	De la reserva a la sala	\N	t	Entre sus múltiples misiones, los museos preservan la memoria de una nación y, al mismo tiempo, como instituciones dinámicas, estimulan la construcción de nuevos sentidos para las piezas que albergan. Los modos en que las obras de una colección se dan a conocer al público moldean la relación que una comunidad entabla con la historia, vínculo que se potencia cuando una institución es permeable a las transformaciones estéticas y sociales que trae cada época.	\N	\N	\N	13112	2025-06-10 15:14:26.074639-03	2025-11-12 13:22:35.854925-03	100	\N	\N	3	es	\N	es	0	t	\N	\N	f	\N	38	0
33825	El Cabildo de Buenos Aires	\N	\N	\N	\N	27577	0	\N	\N	t	La historia del Cabildo comenzó en 1580. Ese año, Juan de Garay fundó la ciudad de La Trinidad, que más tarde pasó a llamarse por el nombre de su puerto: Santa María del Buen Ayre.\r\n\r\nLos cabildos se originaron en España como forma de administración de las ciudades y sus alrededores. Luego fueron trasladados a los dominios americanos. Según las Leyes de Indias, que regulaban la vida en los territorios del Imperio español, toda ciudad debía contar con un Cabildo. Este se ubicaba siempre en la plaza principal, junto a otras instituciones importantes, como la iglesia mayor. En Buenos Aires contenía, además, la cárcel urbana.\r\n\r\nEl Cabildo era la única autoridad elegida por la sociedad local. Los virreyes, los gobernadores y otros funcionarios importantes eran nombrados desde España. En cambio, los miembros del Cabildo representaban a los habitantes de Buenos Aires. Solo los llamados "vecinos" (varones, blancos y con prestigio social) podían integrarlo. Los cabildantes se reunían periódicamente para discutir asuntos importantes para la comunidad.\r\n\r\nHacia fines del siglo XVIII, Buenos Aires pasó de ser una ciudad marginal del Imperio español a convertirse en la capital del Virreinato del Río de la Plata. Su importancia creciente implicó que el Cabildo asumiera cada vez más facultades.\r\n\r\nEl Cabildo tuvo un papel político fundamental en los años posteriores a la revolución de 1810. En 1821, fue disuelto. Desde entonces, la organización de la ciudad quedó en manos de la Legislatura de Buenos Aires, creada un año antes.	\N	\N	\N	34122	2026-01-11 16:12:14.724287-03	2026-01-11 16:12:14.724291-03	101	\N	\N	1	es	\N	es	\N	t	\N	\N	t	\N	1	0
\.


--
-- Data for Name: artexhibitionguiderecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionguiderecord (id, artexhibitionguide_id, language, name, subtitle, info, photo, video, audio, created, lastmodified, lastmodifieduser, draft, namekey, state, intro, usethumbnail, name_hash, subtitle_hash, info_hash, intro_hash, spec, spec_hash, otherjson, otherjson_hash, opens, opens_hash, audioautogenerate, audioauto) FROM stdin;
738	458	en	Sendas perdidas	\N	\N	\N	\N	\N	2025-10-26 06:34:32.846201-03	2025-10-26 06:34:32.84621-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	f
739	458	pt-BR	Sendas perdidas	\N	\N	\N	\N	\N	2025-10-26 06:34:34.6778-03	2025-10-26 06:34:34.677809-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	f
787	786	es	Viaggio in Italia: 1920-1950, La Edad de Oro del Afiche Turístico Italiano	\N	\N	\N	\N	\N	2025-10-30 13:44:50.932403-03	2025-10-30 13:44:50.932413-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	f
789	786	pt-BR	Viagem à Itália: 1920-1950, A Era de Ouro do Cartaz Turístico Italiano	Design italiano, do artifício da Art Déco ao design gráfico de meados do século XX.	A exposição abrange três décadas marcadas pelo nascimento e ascensão do fascismo e pela figura de Benito Mussolini, pela aliança com a Alemanha nazista durante a Segunda Guerra Mundial e pelo período pós-guerra.\r\n\r\nEntre 1920 e 1950, o turismo na Itália se transformou em um fenômeno de massa, e o cartaz foi uma ferramenta fundamental: uma síntese de arte, design e promoção cultural, uma peça publicitária que era também uma obra de arte, refletindo a cultura e a estética de sua época.\r\n\r\nNa década de 1920, ocorreu uma transição do ecletismo, do Liberty ou da Art Nouveau do início do século XX para um estilo modernista mais contido na década de 1930, influenciado por movimentos de vanguarda europeus como o Cubismo e, especialmente, o Futurismo italiano, um movimento artístico fundado por Filippo Tommaso Marinetti, caracterizado pela exaltação da velocidade, da tecnologia, da modernidade, da violência e do dinamismo, rejeitando as tradições e o passado.\r\n\r\nDurante a era fascista, essas obras turísticas escaparam da censura, mas não da influência da promoção política. Assim como na Alemanha e na União Soviética, a vanguarda artística que exaltava o homem novo mostrou-se excessivamente livre; na Itália, deu lugar ao “realismo fascista”, uma arte semelhante ao realismo soviético, que foi chamada de “retorno à ordem”.\r\n\r\nA exposição inclui obras de renomados ilustradores italianos como Marcello Dudovich e Mario Borgoni, que desempenharam um papel crucial na criação de uma identidade turística para o país; e também de Marcello Nizzoli, que no período pós-guerra desenharia as famosas máquinas de escrever portáteis Olivetti.	\N	\N	792	2025-10-30 13:44:50.934874-03	2025-11-06 22:10:58.419984-03	100	\N	\N	\N	\N	f	1046687329	-1330029945	825679811	0	\N	0	\N	0	\N	0	f	t
800	799	es	Alberto Churba. Diseño Infinito	\N	\N	\N	\N	\N	2025-11-11 21:50:04.54881-03	2025-11-11 21:50:04.54882-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	f
852	202	en	Obras Maestras	\N	\N	\N	\N	\N	2025-11-27 17:11:43.47496-03	2025-11-27 17:11:43.474968-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	f	f
801	799	en	Alberto Churba. Diseño Infinito	\N	\N	\N	\N	\N	2025-11-11 21:50:04.550867-03	2025-11-11 21:50:04.550874-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	f
788	786	en	Viaggio in Italia: 1920-1950, The Golden Age of the Italian Tourist Poster	Italian design, from the artifice of Art Deco to Mid-Century graphic design	The exhibition covers three decades marked by the birth and rise of fascism and the figure of Benito Mussolini, the alliance with Nazi Germany during World War II, and the postwar period.\r\n\r\nBetween 1920 and 1950, tourism in Italy transformed into a mass phenomenon, and the poster was a key tool: a synthesis of art, design, and cultural promotion, an advertising piece that was also a work of art reflecting the culture and aesthetics of its time.\r\n\r\nIn the 1920s, a transition occurred from the eclecticism, Liberty, or Art Nouveau of the early 20th century to a more restrained modernist style in the 1930s, influenced by European avant-garde movements such as Cubism, and especially Italian Futurism, an artistic movement founded by Filippo Tommaso Marinetti characterized by the exaltation of speed, technology, modernity, violence, and dynamism, rejecting traditions and the past.\r\n\r\nDuring the Fascist era, these tourist works escaped censorship, but not the influence of political promotion. As in Germany and the Soviet Union, the artistic avant-garde that exalted the new man proved too free; in Italy, it gave way to “Fascist realism,” an art similar to Soviet realism, which was called “the return to order.”\r\n\r\nThe exhibition includes works by renowned Italian illustrators such as Marcello Dudovich and Mario Borgoni, who played a crucial role in creating a tourist identity for the country; and also by Marcello Nizzoli, who in the postwar period would design the famous Olivetti portable typewriters.	\N	\N	791	2025-10-30 13:44:50.934169-03	2025-11-06 22:10:14.451708-03	100	\N	\N	\N	\N	f	1046687329	-1330029945	825679811	0	\N	0	\N	0	\N	0	f	t
802	799	pt-BR	Alberto Churba. Diseño Infinito	\N	\N	\N	\N	\N	2025-11-11 21:50:04.552004-03	2025-11-11 21:50:04.552009-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	f
853	202	pt-BR	Obras Maestras	\N	\N	\N	\N	\N	2025-11-27 17:11:45.855821-03	2025-11-27 17:11:45.855827-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	f	f
895	894	es	Wifredo Lam, cuando no duermo, sueño	\N	\N	\N	\N	\N	2025-11-29 07:51:58.978285-03	2025-11-29 07:51:58.978288-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	f	f
897	894	pt-BR	Wifredo Lam, cuando no duermo, sueño	\N	\N	\N	\N	\N	2025-11-29 07:51:58.979805-03	2025-11-29 07:51:58.979807-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	f	f
735	411	pt-BR	Museu Secreto	Da reserva ao quarto	Entre suas muitas missões, os museus preservam a memória de uma nação e, ao mesmo tempo, como instituições dinâmicas, estimulam a construção de novos significados para as peças que abrigam. As maneiras pelas quais as obras de uma coleção são apresentadas ao público moldam a relação que uma comunidade estabelece com a história, um vínculo que se fortalece quando uma instituição se mostra aberta às transformações estéticas e sociais que cada época traz.	\N	\N	\N	2025-10-25 14:22:54.501191-03	2025-12-08 12:11:15.074398-03	100	\N	\N	\N	\N	f	284200020	374998587	-1809980365	0	\N	0	\N	0	\N	0	t	f
734	411	en	Secret Museum	From reservation to the room	Among their many missions, museums preserve a nation's memory and, at the same time, as dynamic institutions, stimulate the construction of new meanings for the pieces they house. The ways in which the works in a collection are presented to the public shape the relationship a community establishes with history, a bond that is strengthened when an institution is open to the aesthetic and social transformations that each era brings.	\N	\N	\N	2025-10-25 14:21:59.386284-03	2025-12-08 12:19:05.098143-03	100	\N	\N	\N	\N	f	284200020	374998587	-1809980365	0	\N	0	\N	0	\N	0	t	f
1484	248	en	Grandes obras del Impresionismo	\N	\N	\N	\N	\N	2025-12-14 14:31:54.609955-03	2025-12-14 14:31:54.609965-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
1485	248	pt-BR	Grandes obras del Impresionismo	\N	\N	\N	\N	\N	2025-12-14 14:31:57.183953-03	2025-12-14 14:31:57.183965-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
896	894	en	Wilfredo Lan, when I dont sleep, I dream	\N	\N	\N	\N	\N	2025-11-29 07:51:58.979321-03	2025-11-29 07:51:58.979322-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	f	f
33826	33825	es	El Cabildo de Buenos Aires	\N	\N	\N	\N	\N	2026-01-11 16:12:14.731222-03	2026-01-11 16:12:14.731224-03	101	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
33827	33825	en	The Buenos Aires City Council	\N	The history of the Cabildo began in 1580. That year, Juan de Garay founded the city of La Trinidad, which was later renamed after its port: Santa María del Buen Ayre.\n\nCabildos originated in Spain as a form of administration for cities and their surrounding areas. They were later transferred to the American colonies. According to the Laws of the Indies, which regulated life in the territories of the Spanish Empire, every city was required to have a Cabildo. This was always located in the main square, next to other important institutions, such as the main church. In Buenos Aires, it also housed the city jail.\n\nThe Cabildo was the only authority elected by the local community. Viceroys, governors, and other important officials were appointed from Spain. In contrast, the members of the Cabildo represented the inhabitants of Buenos Aires. Only those called "vecinos" (male, white, and of social standing) could be members. The Cabildo members met regularly to discuss matters of importance to the community.\n\nTowards the end of the 18th century, Buenos Aires went from being a marginal city of the Spanish Empire to becoming the capital of the Viceroyalty of the Río de la Plata. Its growing importance meant that the Cabildo (city council) assumed increasingly more powers.\n\nThe Cabildo played a fundamental political role in the years following the 1810 revolution. In 1821, it was dissolved. From then on, the organization of the city was in the hands of the Buenos Aires Legislature, created a year earlier.	\N	\N	34165	2026-01-11 16:12:14.732128-03	2026-01-11 16:21:29.085514-03	101	\N	\N	1	\N	t	1535855056	0	1942838104	0	\N	0	\N	0	\N	0	t	f
33828	33825	pt-BR	Câmara Municipal de Buenos Aires	\N	A história do Cabildo começou em 1580. Naquele ano, Juan de Garay fundou a cidade de La Trinidad, que mais tarde foi renomeada em homenagem ao seu porto: Santa María del Buen Ayre.\n\nOs Cabildos surgiram na Espanha como uma forma de administração para as cidades e seus arredores. Posteriormente, foram transferidos para as colônias americanas. De acordo com as Leis das Índias, que regulamentavam a vida nos territórios do Império Espanhol, toda cidade era obrigada a ter um Cabildo. Este sempre se localizava na praça principal, próximo a outras instituições importantes, como a igreja principal. Em Buenos Aires, também abrigava a cadeia da cidade.\n\nO Cabildo era a única autoridade eleita pela comunidade local. Vice-reis, governadores e outros funcionários importantes eram nomeados pela Espanha. Em contrapartida, os membros do Cabildo representavam os habitantes de Buenos Aires. Somente os chamados "vecinos" (homens brancos e de posição social elevada) podiam ser membros. Os membros do Cabildo se reuniam regularmente para discutir assuntos importantes para a comunidade.\n\nNo final do século XVIII, Buenos Aires deixou de ser uma cidade marginal do Império Espanhol para se tornar a capital do Vice-Reino do Rio da Prata. Sua crescente importância fez com que o Cabildo (conselho municipal) assumisse cada vez mais poderes.\n\nO Cabildo desempenhou um papel político fundamental nos anos que se seguiram à Revolução de 1810. Em 1821, foi dissolvido. A partir de então, a organização da cidade ficou a cargo da Assembleia Legislativa de Buenos Aires, criada um ano antes.	\N	\N	34208	2026-01-11 16:12:14.732665-03	2026-01-11 16:22:58.719272-03	101	\N	\N	1	\N	t	1535855056	0	1942838104	0	\N	0	\N	0	\N	0	t	f
\.


--
-- Data for Name: artexhibitionitem; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionitem (id, name, namekey, title, titlekey, artexhibition_id, artwork_id, site_id, floor_id, room_id, artexhibitionorder, mapurl, website, readcode, qcode, info, infokey, created, lastmodified, lastmodifieduser, state, floorstr, roomstr, language, draft, masterlanguage, translation, photo, audio, video, subtitle, usethumbnail, intro, spec, opens, audioautogenerate, speechaudio) FROM stdin;
634	El recurso del método	el-recurso-del-m-todo	\N	\N	\N	452	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-29 15:58:54.034924-03	2025-09-29 15:58:54.034941-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
633	Reposo	reposo	\N	\N	\N	280	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-29 15:21:43.233762-03	2025-09-29 15:21:43.233789-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
487	Procesión sorprendida por la lluvia	\N	Procesión sorprendida por la lluvia	\N	362	484	137	139	\N	4	\N	\N	1035	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-10-01 13:38:17.808303-03	100	3	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
409	Retrato de Juan Manuel de Rosas	\N	Retrato de Juan Manuel de Rosas	\N	362	406	137	138	140	2	\N	\N	1027	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
247	La Nymphe surprise (La ninfa sorprendida)	la-nymphe-surprise-la-ninfa-sorprendida	La Nymphe surprise (La ninfa sorprendida)	\N	233	225	137	138	154	14	\N	\N	1015	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
285	La vuelta del malón	la-vuelta-del-mal-n	La vuelta del malón	la-vuelta-del-mal-n	284	270	137	138	151	1	\N	\N	1016	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
290	Reposo	reposo	Reposo	reposo	284	280	137	138	151	6	\N	\N	1021	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
287	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	284	274	137	138	151	3	\N	\N	1018	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
243	La Toilette apres le bain (El arreglo después del baño)	la-toilette-apres-le-bain-el-arreglo-despu-s-del-ba-o	La Toilette apres le bain (El arreglo después del baño)	\N	233	223	137	138	154	10	\N	\N	1011	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
834	La berge de La Seine (Orillas del Sena)	\N	\N	\N	\N	219	\N	\N	\N	6	\N	\N	\N	\N	\N	\N	2025-11-23 19:52:35.866098-03	2025-11-23 19:52:35.866108-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	f	\N
456	Apocalipsis	\N	Apocalipsis	\N	454	450	137	139	\N	2	\N	\N	1030	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
615	O Impossivel	o-impossivel	\N	\N	\N	592	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 10:59:00.915806-03	2025-09-24 10:59:00.915878-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
244	Le Moulin de la Galette	le-moulin-de-la-galette	Le Moulin de la Galette	\N	233	225	137	138	154	11	\N	\N	1012	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
239	Vahine no te miti (Femme a la mer) (Mujer del mar)	vahine-no-te-miti-femme-a-la-mer-mujer-del-mar	Vahine no te miti (Femme a la mer) (Mujer del mar)	\N	233	215	137	138	154	6	\N	\N	1007	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
35278	Traje de calle del alférez real	\N	\N	\N	27577	34719	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2026-01-12 10:41:41.312036-03	2026-01-12 10:41:41.312045-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	t	\N
782	Sillón cinta	\N	\N	\N	774	768	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-10-29 13:41:23.2673-03	2025-10-29 13:41:23.267322-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	f	\N	\N	\N	f	\N
40769	Vara de mando de Manuel Mansilla	\N	\N	\N	27577	40219	\N	\N	\N	2	\N	\N	\N	\N	\N	\N	2026-01-13 15:22:34.907999-03	2026-01-13 15:22:34.908009-03	100	1	Planta Baja	1	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	t	\N
16643	Je suis (Yo soy)	\N	\N	\N	864	16103	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	2026-01-04 17:59:28.813742-03	2026-01-04 17:59:28.81375-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	t	\N
890	La jungla	\N	\N	\N	864	884	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-11-29 07:51:43.660506-03	2025-11-29 07:51:43.660512-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	f	\N
457	El recurso del método	\N	El recurso del método	\N	454	452	137	139	\N	2	\N	\N	1031	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
16647	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	864	12377	\N	\N	\N	2	\N	\N	\N	\N	\N	\N	2026-01-04 17:59:31.486215-03	2026-01-04 17:59:31.48622-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	t	\N
455	Sendas perdidas	\N	Sendas perdidas	\N	454	448	137	139	\N	1	\N	\N	1029	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
408	La Emperatriz Theodora	\N	La Emperatriz Theodora	\N	362	402	137	138	140	2	\N	\N	1026	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-10-01 13:37:56.097839-03	100	3	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
236	Prairies du Valhermeil près Pontoise	prairies-du-valhermeil-pr-s-pontoise	Prairies du Valhermeil près Pontoise	\N	233	209	137	138	\N	3	\N	\N	1004	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
410	En la costa de Valencia	\N	En la costa de Valencia	\N	362	404	137	138	140	1	\N	\N	1028	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-10-01 13:37:44.824832-03	100	3	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
201	Morro da favela II (Pueblito)	morro-da-favela-ii-pueblito	Morro da favela II (Pueblito)	\N	199	195	137	138	140	2	\N	\N	1001	\N	Época de temática popular, la década del 40 también sería para Tarsila, la gran pintora modernista de los años 20, tiempo de volver definitivamente de sus tonos pau Brasil, es decir, retornar a los colores llamados caipiras (azul y rosa) de la decoración popular de los modestos caseríos de las regiones del interior del Brasil. Este colorido, aunque más intenso, estuvo presente en sus telas de los años 20, su período de máxima creatividad y producción, etapa de una gran síntesis de sus elementos compositivos. Después de 1930, cuando Tarsila se orientó, por razones culturales y políticas, hacia la temática social, su paleta se alteró igualmente.\\nPor esa época la artista regresó a San Pablo después de alternar varios años entre esa ciudad y Río de Janeiro, y pasó gran parte de su tiempo en su hacienda de Indaiatuba, en el interior paulista. \\nConociendo la inexistencia de un mercado de arte local, realizó encargos de pinturas, ilustraciones y colaboró con crónicas regulares sobre la vida cultural y memorias en Diário de São Paulo.\\nEn Pueblito, de 1945, encontramos una segunda versión de la famosa pintura Morro da favela (1924, colección João Estefano, San Pablo), tela tan admirada por el poeta Blaise Cendrars que le recomendó, en su momento, que preparase su primera exposición individual en París a partir de la serie que se iniciara con esta obra. La temática está inspirada en el viaje a Río con los modernistas de San Pablo acompañando a Cendrars en su primera visita al Brasil.\\nAlgunas variantes, sin embargo, surgen en esta tela, ahora en la colección del MNBA: sus dimensiones son menores que las de Morro da favela, la vegetación tiene modificaciones, dejando de ser visualmente presentada en formas sintéticas, en esta tela de 1945 aparecen como matas de plantas más desmenuzadas, más diversificadas en su ubicación, especialmente en el primer plano. Por otra parte, las pinceladas son bien evidentes, casi “impresionistas” en su aplicación sobre la tela, y el ave extraña de 1924 es reemplazada por la confrontación de dos pavos. Finalmente, el color es más suave en comparación con la pintura de veinte años antes.\\nLa obra del MNBA estuvo expuesta en Buenos Aires, La Plata y Montevideo en la exposición Veinte artistas brasileños presentada en 1945, con la organización del escritor Marques Rebelo y la intermediación efectiva de Emilio Pettoruti.\\nEn esta muestra Tarsila estuvo representada por otra pintura, Pueblito II (1944), y por un dibujo, estudio de 16 x 22 cm de la pintura de la colección del MNBA, hoy aparentemente perdido. La exposición dio lugar a dos pequeños libros, actualmente antológicos, uno de autoría de Cipriano Vitureira, de Montevideo, y el segundo de Jorge Romero Brest, La pintura brasileña contemporánea, publicado el mismo año de la exposición por la editorial Poseidón.	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
238	En observation - M.Fabre, Officier de reserve	en-observation-m-fabre-officier-de-reserve	En observation - M.Fabre, Officier de reserve	\N	233	213	137	138	152	5	\N	\N	1006	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
237	Femme aux champs (Campesina)	femme-aux-champs-campesina	Femme aux champs (Campesina)	\N	233	211	137	138	152	4	\N	\N	1005	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
242	La Coiffure (El peinado)	la-coiffure-el-peinado	La Coiffure (El peinado)	\N	233	221	137	138	154	9	\N	\N	1010	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
240	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	portrait-de-suzanne-valadon-madame-suzanne-valadon-artiste-peintre-retrato-de-suzanne-valadon-pintora	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	\N	233	217	137	138	154	7	\N	\N	1008	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
616	Troncos	troncos	\N	\N	\N	589	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 11:30:16.706215-03	2025-09-24 11:30:16.706229-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
635	En Normandie	en-normandie	\N	\N	\N	272	\N	\N	\N	6	\N	\N	\N	\N	\N	\N	2025-10-01 10:57:21.298364-03	2025-10-01 10:57:21.298382-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
622	Diván	div-n	\N	\N	597	619	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 11:39:24.765554-03	2025-09-24 11:39:24.765567-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
613	El mal	el-mal	\N	\N	597	603	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-23 18:53:44.207449-03	2025-09-23 18:53:44.420125-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
617	The Disasters of Mysticism	the-disasters-of-mysticism	\N	\N	\N	582	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 11:30:47.572528-03	2025-09-24 11:30:47.572541-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
614	Abaporu	abaporu	\N	\N	600	575	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-23 18:57:57.544907-03	2025-09-23 18:57:57.544938-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
626	Troncos	troncos	\N	\N	600	589	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-25 16:51:35.086883-03	2025-09-25 16:51:35.086893-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
291	Abel	abel	Abel	abel	284	282	137	138	151	7	\N	\N	1022	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
618	Quiosco de Canaletas	quiosco-de-canaletas	\N	\N	\N	311	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 11:30:54.292583-03	2025-09-24 11:30:54.292595-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
627	La vuelta del malón	la-vuelta-del-mal-n	\N	\N	\N	270	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-27 21:09:16.258142-03	2025-09-27 21:09:16.25816-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
486	Utopia del Sur	\N	Utopia del Sur	\N	362	482	137	139	\N	1	\N	\N	1034	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-11-29 14:35:57.928954-03	100	5	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
623	O Impossivel	o-impossivel	\N	\N	\N	592	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 11:43:51.626523-03	2025-09-24 11:43:51.626534-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
289	El despertar de la criada	el-despertar-de-la-criada	El despertar de la criada	el-despertar-de-la-criada	284	278	137	138	151	5	\N	\N	1020	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-09-30 16:47:26.38454-03	100	3	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
286	En Normandie	en-normandie	En Normandie	en-normandie	284	272	137	138	151	2	\N	\N	1017	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
288	La vuelta al hogar	la-vuelta-al-hogar	La vuelta al hogar	la-vuelta-al-hogar	284	276	137	138	151	4	\N	\N	1019	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
246	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	danseuse-et-admirateur-derri-re-la-sc-ne-bailarina-y-admirador-tras-la-escena	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	\N	233	225	137	138	154	13	\N	\N	1014	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
234	Effet de neige à Louveciennes	effet-de-neige-louveciennes	Effet de neige à Louveciennes	\N	233	205	137	138	151	1	\N	\N	1002	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
241	La berge de La Seine (Orillas del Sena)	la-berge-de-la-seine-orillas-del-sena	La berge de La Seine (Orillas del Sena)	\N	233	219	137	138	154	8	\N	\N	1009	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
625	El circo más lindo del mundo	el-circo-m-s-lindo-del-mundo	\N	\N	600	585	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-25 16:51:24.66241-03	2025-09-25 16:51:24.662421-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
632	La vuelta al hogar	la-vuelta-al-hogar	\N	\N	362	276	\N	\N	\N	3	\N	\N	\N	\N	\N	\N	2025-09-29 15:12:06.087863-03	2025-10-01 13:38:08.163137-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
235	Le Pont d`Argenteuil (El Puente de Argenteuil)	le-pont-d-argenteuil-el-puente-de-argenteuil	Le Pont d`Argenteuil (El Puente de Argenteuil)	\N	233	207	137	138	151	2	\N	\N	1003	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
245	Portrait de Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	portrait-de-ernest-hosched-et-sa-fille-marthe-retrato-del-se-or-hosched-y-su-hija	Portrait de Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	\N	233	225	137	138	154	12	\N	\N	1013	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
200	Le bain de Vénus (El baño de Venus)	le-bain-de-v-nus-el-ba-o-de-venus	Le bain de Vénus (El baño de Venus)	\N	199	197	137	138	140	1	\N	\N	1000	\N	El título original de esta pintura puede haber sido puesto por Redon, ya que al parecer la obra fue comprada al artista sin intermediarios. Los toques de azul y verde en la zona inferior derecha de la tela pueden considerarse una representación del agua, y el tocado de la figura puede evocar un personaje mitológico. Estos aspectos, sin embargo, son los únicos que justificarían el título de El baño de Venus. La figura no está desnuda, aparece (apenas) vestida y carece de otro atributo inherente a la diosa del amor. Su movimiento no corresponde a una escena de baño y el personaje demuestra una vacilación indigna de alguien nacido del mar. Con los dos brazos extendidos hacia delante y la cabeza hacia atrás, parece moverse de derecha a izquierda como si estuviese huyendo de algo –en una posición relativamente similar a la del dibujo Female Nude (Bridgestone Museum, Tokio)–. El origen de su miedo o resistencia parece ser la forma circular sobre la derecha (de la mitad del tamaño de la figura) que semeja más a un insecto o a uno de los monstruos submarinos pintados por Redon que a las flores y las hojas reconocibles sobre la izquierda. Esta interpretación se sustenta en el uso del negro para delinear y rellenar parte de esta forma, color asociado por Redon al mundo fantástico de sus primeras carbonillas y litografías.	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N
\.


--
-- Data for Name: artexhibitionitemrecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionitemrecord (id, artexhibitionitem_id, language, name, subtitle, opens, info, photo, video, audio, created, lastmodified, lastmodifieduser, draft, namekey, state, intro, usethumbnail, name_hash, subtitle_hash, info_hash, intro_hash, spec, spec_hash, otherjson, otherjson_hash, opens_hash, audioautogenerate, audioauto) FROM stdin;
719	408	en	La Emperatriz Theodora	\N	\N	\N	\N	\N	\N	2025-10-24 16:35:34.088552-03	2025-10-24 16:35:34.088569-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	t	f
720	410	en	En la costa de Valencia	\N	\N	\N	\N	\N	\N	2025-10-24 16:39:55.563405-03	2025-10-24 16:39:55.563434-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	t	f
721	410	pt-BR	En la costa de Valencia	\N	\N	\N	\N	\N	\N	2025-10-24 16:39:57.838885-03	2025-10-24 16:39:57.838897-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	t	f
722	408	pt-BR	La Emperatriz Theodora	\N	\N	\N	\N	\N	\N	2025-10-24 16:40:29.36607-03	2025-10-24 16:40:29.366083-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	t	f
723	632	en	La vuelta al hogar	\N	\N	\N	\N	\N	\N	2025-10-24 16:44:32.466865-03	2025-10-24 16:44:32.466877-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	t	f
742	632	pt-BR	La vuelta al hogar	\N	\N	\N	\N	\N	\N	2025-10-26 14:29:25.182956-03	2025-10-26 14:29:25.183031-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
783	782	es	Sillón cinta	\N	\N	\N	\N	\N	\N	2025-10-29 13:41:23.269743-03	2025-10-29 13:41:23.269751-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
784	782	en	Sillón cinta	\N	\N	\N	\N	\N	\N	2025-10-29 13:41:23.272708-03	2025-10-29 13:41:23.272715-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
785	782	pt-BR	Sillón cinta	\N	\N	\N	\N	\N	\N	2025-10-29 13:41:23.277667-03	2025-10-29 13:41:23.277677-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
835	834	es	La berge de La Seine (Orillas del Sena)	\N	\N	\N	\N	\N	\N	2025-11-23 19:52:35.867588-03	2025-11-23 19:52:35.867592-03	100	\N	\N	1	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
836	834	en	La berge de La Seine (Orillas del Sena)	\N	\N	\N	\N	\N	\N	2025-11-23 19:52:35.868369-03	2025-11-23 19:52:35.868371-03	100	\N	\N	1	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
837	834	pt-BR	La berge de La Seine (Orillas del Sena)	\N	\N	\N	\N	\N	\N	2025-11-23 19:52:35.868783-03	2025-11-23 19:52:35.868784-03	100	\N	\N	1	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
846	201	en	Morro da favela II (Pueblito)	\N	\N	\N	\N	\N	\N	2025-11-26 12:09:12.317567-03	2025-11-26 12:09:12.317572-03	100	\N	\N	1	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
847	201	pt-BR	Morro da favela II (Pueblito)	\N	\N	\N	\N	\N	\N	2025-11-26 12:09:14.772025-03	2025-11-26 12:09:14.772029-03	100	\N	\N	1	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
891	890	es	La jungla	\N	\N	\N	\N	\N	\N	2025-11-29 07:51:43.663926-03	2025-11-29 07:51:43.663928-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	f	f
892	890	en	La jungla	\N	\N	\N	\N	\N	\N	2025-11-29 07:51:43.664826-03	2025-11-29 07:51:43.664827-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	f	f
893	890	pt-BR	La jungla	\N	\N	\N	\N	\N	\N	2025-11-29 07:51:43.66525-03	2025-11-29 07:51:43.665251-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	f	f
16644	16643	es	Je suis (Yo soy)	\N	\N	\N	\N	\N	\N	2026-01-04 17:59:28.823214-03	2026-01-04 17:59:28.823218-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
16645	16643	en	Je suis (Yo soy)	\N	\N	\N	\N	\N	\N	2026-01-04 17:59:28.82668-03	2026-01-04 17:59:28.826682-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
16646	16643	pt-BR	Je suis (Yo soy)	\N	\N	\N	\N	\N	\N	2026-01-04 17:59:28.827666-03	2026-01-04 17:59:28.827667-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
16648	16647	es	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	\N	\N	\N	2026-01-04 17:59:31.488205-03	2026-01-04 17:59:31.488207-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
16649	16647	en	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	\N	\N	\N	2026-01-04 17:59:31.489648-03	2026-01-04 17:59:31.48965-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
16650	16647	pt-BR	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	\N	\N	\N	2026-01-04 17:59:31.49044-03	2026-01-04 17:59:31.490441-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
35279	35278	es	Traje de calle del alférez real	\N	\N	\N	\N	\N	\N	2026-01-12 10:41:41.31719-03	2026-01-12 10:41:41.317198-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
35280	35278	en	Traje de calle del alférez real	\N	\N	\N	\N	\N	\N	2026-01-12 10:41:41.320817-03	2026-01-12 10:41:41.320818-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
35281	35278	pt-BR	Traje de calle del alférez real	\N	\N	\N	\N	\N	\N	2026-01-12 10:41:41.321481-03	2026-01-12 10:41:41.321482-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
40770	40769	es	Vara de mando de Manuel Mansilla	\N	\N	\N	\N	\N	\N	2026-01-13 15:22:34.919908-03	2026-01-13 15:22:34.919913-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
40771	40769	en	Vara de mando de Manuel Mansilla	\N	\N	\N	\N	\N	\N	2026-01-13 15:22:34.921134-03	2026-01-13 15:22:34.921137-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
40772	40769	pt-BR	Vara de mando de Manuel Mansilla	\N	\N	\N	\N	\N	\N	2026-01-13 15:22:34.921965-03	2026-01-13 15:22:34.921968-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
\.


--
-- Data for Name: artexhibitionrecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionrecord (id, artexhibition_id, language, name, subtitle, opens, info, photo, video, audio, created, lastmodified, lastmodifieduser, draft, namekey, state, intro, usethumbnail, name_hash, subtitle_hash, info_hash, intro_hash, spec, otherjson_hash, otherjson, spec_hash, opens_hash, audioautogenerate, audioauto) FROM stdin;
699	454	en	Lost paths	\N	\N	A temporary exhibition dedicated to the Argentine artist Germán Gárgano, which brought together more than 170 of his works (drawings, watercolors, inks, and gouaches) at the National Museum of Fine Arts. The exhibition, curated by Pablo De Monte, explored the artist's visual universes, including echoes of his prison experience, and was on display between July and August 2025.	\N	\N	\N	2025-10-23 18:26:01.009438-03	2025-12-18 17:59:29.322719-03	100	\N	\N	\N	Germán Gárgano was a political prisoner in Argentina from 1975 to 1982, and maintained a correspondence from prison with the painter Carlos Gorriarena, with whom he continued his artistic training after his release. “Lost Paths” is his first exhibition at the National Museum of Fine Arts, featuring more than 170 drawings, gouaches, watercolors, and ink works created in recent years.	f	-1047921254	0	-2027998053	45047957	\N	0	\N	0	0	t	f
707	284	en	Argentine art in the 19th century. Towards the consolidation of a national model		\N	The consolidation of a national model in Argentine art was a gradual process that took place in the 19th and early 20th centuries, marked by the search for a distinct visual identity and the creation of institutions that promoted national art. The aim was to reflect Argentine reality, local customs, and the country's history, differentiating itself from European influences. In the second half of the 19th century, Argentine art was characterized by portraiture, where artists depicted the important figures of the new nation, following the neoclassical canons of the time. Then, at the end of the 19th and beginning of the 20th centuries, a distinct visual identity was sought, with the creation of institutions such as El Ateneo, where writers and artists debated the existence of a "national art."	\N	\N	\N	2025-10-24 12:16:49.769543-03	2025-12-18 17:59:52.43356-03	100	\N	\N	\N	The consolidation of a national model in Argentine art was a process that took place in the late 19th and early 20th centuries, marked by the search for a distinct visual identity and the creation of institutions that promoted national art. The aim was to reflect Argentine reality, local customs, and the country's history, differentiating itself from European influences.	f	-527621307	0	914022020	-417385168	\N	0	\N	0	0	t	f
700	454	pt-BR	Sendas perdidas	\N	\N	\N	\N	\N	\N	2025-10-23 18:26:05.536482-03	2025-10-23 18:26:05.536494-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	t	f
708	284	pt-BR	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	\N	\N	\N	\N	\N	\N	2025-10-24 12:16:56.932677-03	2025-10-24 12:16:56.932694-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	t	f
724	199	en	Masterpieces	\N	\N	The masterpiece was the name given to the handcrafted piece that every official wishing to reach the category of master within the guilds had to make.	\N	\N	\N	2025-10-24 17:40:37.29307-03	2025-10-24 17:40:37.293082-03	100	\N	\N	\N	\N	f	1403191617	0	689680852	0	\N	0	\N	0	0	t	f
725	199	pt-BR	Obras-primas	\N	\N	Obra-prima era o nome dado à peça artesanal que todo oficial que desejasse atingir a categoria de mestre dentro das guildas tinha que fazer.	\N	\N	\N	2025-10-24 17:41:42.10035-03	2025-10-24 17:41:47.520527-03	100	\N	\N	\N	\N	f	1403191617	0	689680852	0	\N	0	\N	0	0	t	f
728	600	en	Tercer ojo	\N	\N	\N	\N	\N	\N	2025-10-24 22:54:43.565482-03	2025-10-24 22:54:43.565495-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	t	f
726	597	en	Carrie Bencardino: The Devil's Disinterment	\N	\N	The Devil's Exhumation, curated by Carlos Gutiérrez, emerges from the recognition of a potential crisis of the imagination, fueled by political ups and downs and the advance of a type of thinking that tends to dissolve ties between people. Comprised mostly of paintings, the exhibition seeks to transform the gallery into a space where different spaces intersect: it's almost a bar, almost a movie theater, almost a club. Likewise, the paintings function in a similar way, depicting situations that could happen anywhere, real or fantastical. For Bencardino, the need to generate new tools to build other worlds is imperative.\n\nBencardino's work draws on images found in books, magazines, album covers, music videos, the internet, and his personal archive of objects and other materials circulating in mass culture and its platforms. Her references come from a very particular affective imaginary: the aesthetics of queer communities and the adolescents of her generation, the visual codes of countercultural scenes (such as punk and the various subgenres of metal), comics and illustration (such as Ciruelo, Victoria Francés, Luis Royo, Boris Vallejo, and Magalí Villeneuve), and the literary fantasy imaginary (especially William Blake and J.R.R. Tolkien), among many other references. She processes and distorts these images digitally, and from these new images, she creates her paintings.\n\nShe works from a critical position on the circulation and reappropriation of images in the contemporary era, where visual sources and referents multiply, intermingle, and are constantly transformed. In her painting, appropriation is not an end in itself, but a means to interrogate cultural memory, shared iconographies, and the affective relationship we establish with images.\n\nThe exhibition also features a video piece that is a monologue about the impact of different artists, surrealism, and magical thinking on the formation of Bencardino's imaginary. The exhibition thus highlights the connections between imagination—or more precisely, the capacity to imagine—and politics, engaging with the production of images and recovering past clues that might distort the totalizing ideas of the collective narrative. Thus, this project attempts to contest places where ideas and the desire for a common horizon do not find us completely exhausted.	\N	\N	\N	2025-10-24 22:27:02.376296-03	2025-10-24 23:33:13.135368-03	100	\N	\N	\N	The "devil's burial" is an ancient celebration of the Jujuy Carnival in Argentina, which marks the beginning of the festivities with the symbolic unearthing of the Pujllay (or spirit of the carnival), a figure that represents joy and the festive spirit.	f	1193539414	0	569258230	-959293584	\N	0	\N	0	0	t	f
727	597	pt-BR	Carrie Bencardino: O Desenterro do Diabo	\N	\N	A Exumação do Diabo, com curadoria de Carlos Gutiérrez, surge do reconhecimento de uma potencial crise do imaginário, alimentada por altos e baixos políticos e pelo avanço de um pensamento que tende a dissolver os laços entre as pessoas. Composta majoritariamente por pinturas, a exposição busca transformar a galeria em um espaço onde diferentes espaços se cruzam: é quase um bar, quase um cinema, quase uma boate. Da mesma forma, as pinturas funcionam de forma semelhante, retratando situações que poderiam acontecer em qualquer lugar, reais ou fantásticas. Para Bencardino, a necessidade de gerar novas ferramentas para construir outros mundos é imperativa.\n\nO trabalho de Bencardino se baseia em imagens encontradas em livros, revistas, capas de álbuns, videoclipes, na internet e em seu arquivo pessoal de objetos e outros materiais que circulam na cultura de massa e suas plataformas. Suas referências vêm de um imaginário afetivo muito particular: a estética das comunidades queer e dos adolescentes de sua geração, os códigos visuais de cenas contraculturais (como o punk e os vários subgêneros do metal), quadrinhos e ilustração (como Ciruelo, Victoria Francés, Luis Royo, Boris Vallejo e Magalí Villeneuve) e o imaginário literário fantástico (especialmente William Blake e J.R.R. Tolkien), entre muitas outras referências. Ela processa e distorce essas imagens digitalmente e, a partir dessas novas imagens, cria suas pinturas.\n\nEla trabalha a partir de uma posição crítica sobre a circulação e a reapropriação de imagens na era contemporânea, onde fontes e referentes visuais se multiplicam, se entrelaçam e são constantemente transformados. Em sua pintura, a apropriação não é um fim em si mesma, mas um meio para questionar a memória cultural, as iconografias compartilhadas e a relação afetiva que estabelecemos com as imagens.\n\nA exposição também apresenta um vídeo que é um monólogo sobre o impacto de diferentes artistas, do surrealismo e do pensamento mágico na formação do imaginário de Bencardino. A exposição, portanto, destaca as conexões entre imaginação — ou, mais precisamente, a capacidade de imaginar — e política, interagindo com a produção de imagens e recuperando pistas do passado que podem distorcer as ideias totalizantes da narrativa coletiva. Assim, este projeto busca contestar lugares onde as ideias e o desejo por um horizonte comum não nos encontram completamente esgotados.	\N	\N	\N	2025-10-24 22:52:25.145995-03	2025-10-24 23:33:27.284117-03	100	\N	\N	\N	O "enterro do diabo" é uma antiga celebração do Carnaval de Jujuy, na Argentina, que marca o início das festividades com o desenterramento simbólico do Pujllay (ou espírito do carnaval), uma figura que representa a alegria e o espírito festivo.	f	1193539414	0	569258230	-959293584	\N	0	\N	0	0	t	f
698	362	pt-BR	Museu Secreto	Da reserva ao quarto	\N	Entre suas muitas missões, os museus preservam a memória de uma nação e, ao mesmo tempo, como instituições dinâmicas, estimulam a construção de novos significados para as peças que abrigam. As formas como as obras de uma coleção são divulgadas ao público moldam a relação que uma comunidade estabelece com a história, vínculo que se fortalece quando uma instituição está aberta às transformações estéticas e sociais trazidas por cada época.\n\nNo caso de museus de arte com grandes coleções, o número de obras que os visitantes podem apreciar nas galerias é apenas uma parte de um conjunto muito mais amplo; é uma proposta, como qualquer seleção, de uma das muitas interpretações possíveis de um patrimônio. O que se expõe é, portanto, um panorama de artistas, períodos, gêneros e temas representativos de um corpus que, em sua maior parte, permanece em reserva.\n\n"Museu secreto", o título desta exposição, faz referência a um discurso proferido por Eduardo Schiaffino, o primeiro diretor do Museu de Belas Artes e que impulsionou a formação do acervo a partir do final do século XIX.\n\nPublicadas na imprensa em 1926, as palavras de Schiaffino apontavam para a necessidade de dar visibilidade ao patrimônio que, guardado em depósitos, permanecia oculto aos olhos dos visitantes. Nos 130 anos de sua criação, o Museu Nacional de Belas Artes conseguiu reunir mais de 13.000 pinturas, esculturas, desenhos, gravuras, objetos, instalações e fotografias, entre outros tipos de peças, um acervo cuja magnitude torna o desafio que acompanha a instituição desde seus primórdios não apenas relevante, mas também cada vez mais complexo.\n\nCom o objetivo de ampliar o universo do que é apresentado ao público, esta exposição apresenta cerca de 300 obras do acervo do Museu Nacional de Belas Artes, criadas por mais de 250 artistas argentinos e internacionais, desde o século XIV até os dias atuais. Algumas dessas obras estiveram nas galerias como parte de diversos projetos permanentes ou em exposições temporárias, enquanto outras tiveram menor visibilidade. A curadoria desta exposição surge da troca de conhecimentos e do trabalho coletivo de todas as equipes do Museu. A disposição das obras recria a forma como as coleções eram apresentadas no século XIX, como pode ser visto a poucos metros daqui, na Sala Guerrico. Esse tipo de distribuição permite que um maior número de peças seja disposto no espaço e também emula a forma como elas são agrupadas nas reservas.\n\nConcebida em um esquema não linear, a seleção conecta gêneros, estilos e temas que nortearam a produção de artistas de todas as épocas, com obras distribuídas em grandes conjuntos, como constelações, que possibilitam diálogos históricos e estéticos e, ao mesmo tempo, tensionam diversas concepções de arte como ferramenta de representação. Por meio deste panorama expandido da coleção de arte mais importante da Argentina, convidamos você a considerar novas conexões entre o passado e o presente da história visual.	\N	\N	\N	2025-10-23 18:24:42.79766-03	2025-10-24 23:36:48.905202-03	100	\N	\N	\N	O Museu de Belas Artes inicia sua programação anual com uma exposição antológica que reúne cerca de 300 obras provenientes das reservas do Pavilhão de Exposições Temporárias.	f	284200020	374998587	1979805713	-1604831975	\N	0	\N	0	0	t	f
697	362	en	Secret Museum	From the reservation to the room	From January 22, 2025 to August 31, 2025 \nTemporary Exhibition Pavilion	Among their many missions, museums preserve the memory of a nation and, at the same time, as dynamic institutions, stimulate the construction of new meanings for the pieces they house. The ways in which the works in a collection are made known to the public shape the relationship a community establishes with history, a bond that is strengthened when an institution is open to the aesthetic and social transformations brought about by each era.\n\nIn the case of art museums with large collections, the number of works that visitors can appreciate in the galleries is only a part of a much broader set; it is a proposal, like any selection, of one of the many possible interpretations of a heritage. What is exhibited is, therefore, an overview of artists, periods, genres, and themes representative of a corpus that, for the most part, remains in reserve.\n\n"Museo secreto," the title of this exhibition, refers to a speech given by Eduardo Schiaffino, the first director of the Bellas Artes Museum and who promoted the formation of the collection from the late 19th century.\n\nPublished in the press in 1926, Schiaffino's words pointed to the need to showcase the heritage that, housed in storage, remained hidden from the eyes of visitors. In the 130 years since its creation, the Museo Nacional de Bellas Artes (National Museum of Fine Arts) has managed to gather more than 13,000 paintings, sculptures, drawings, prints, objects, installations, and photographs, among other types of pieces, a collection whose magnitude makes the challenge that has accompanied the institution since its early years not only continue to be relevant but also increasingly complex. \n\nWith the aim of expanding the universe of what is presented to the public, this exhibition displays nearly 300 works from the Museo Nacional de Bellas Artes's collections, created by more than 250 Argentine and international artists, from the 14th century to the present day. Some of these works have been in the galleries as part of various permanent projects or in temporary exhibitions, while others have had less visibility. The curation of this exhibition emerges from the exchange of knowledge and the collective work of all the Museum's teams. The arrangement of the works recreates the way collections were presented in the 19th century, as can be seen a few meters from here in the Guerrico Room. This type of distribution allows for a greater number of pieces to be arranged in the space and also emulates the way they are grouped in the reserves.\n\nConceived in a nonlinear scheme, the selection connects genres, styles, and themes that have guided the production of artists from all eras, with works distributed in large clusters like constellations that enable historical and aesthetic dialogues and, at the same time, bring into tension diverse conceptions of art as a tool of representation. Through this expanded panorama of Argentina's most important art collection, we invite you to consider new connections between the past and present of visual history.	\N	\N	\N	2025-10-23 18:23:44.15552-03	2025-10-25 14:12:54.322233-03	100	\N	\N	\N	The Bellas Artes Museum begins its annual program with an anthology exhibition that brings together nearly 300 works from reserves in the Temporary Exhibition Pavilion.	f	284200020	374998587	1979805713	-1604831975	\N	0	\N	0	-1023661827	t	f
831	830	es	new	\N	\N	\N	\N	\N	\N	2025-11-23 19:51:03.625554-03	2025-11-23 19:51:03.625562-03	100	\N	\N	1	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
760	757	en	Viaggio in Italia: 1920-1950, The Golden Age of the Italian Tourist Poster	Italian design, from the artifices of Art Deco to Mid-Century graphic design	From October 20, 2025\nTo January 20, 2026	The Italian Institute of Culture of Buenos Aires (IIC) and the National Museum of Decorative Art present the exhibition "Viaggio in Italia. \nThe Golden Age of the Italian Tourist Poster (1520-1550)", an exhibition that invites you to explore the evolution of Italian tourism advertising graphics through an extraordinary selection of original posters from the Alessandro Bellenda collection. \nMade between the 1920s and 1950s, the posters on display were produced by ENIT (the Italian National Agency for Tourism) and the Ferrovie dello Stato (State Railways), organizations that promoted a new visual aesthetic to introduce Italy to the world. \nThe works bear the signatures of great masters of Italian poster design such as Marcello Dudovich, Mario Borgoni, Gino Boccasile, Aurelio Craffonara, and Franz Lenhart, among others. Through the use of vibrant colors, idealized figures, and iconic settings, these artists created a true aesthetic canon of Italian tourism, consolidating an image of the country that continues to inspire designers and creatives today, according to a communication from the IIC.\nBeyond its artistic value, the exhibition offers a historical and sociological interpretation: the posters not only narrate the evolution of Italian graphic design, but also the construction of the imagery of the "bel paese" and the ways in which Italy presented itself to the international public during a period of intense social and cultural transformation.\n"Viaggio in Italia" opens within the framework of the Week of the Italian Language in the World, an important annual event held globally through the network of Italian Cultural Institutes and Italian Embassies, which celebrates the Italian language and creativity in its many expressions.\nThe exhibition is being presented in two locations: the National Museum of Decorative Art, where the main core is on display, and the Sala Roma of the Istituto Italiano di Cultura, which will house a complementary selection of graphic pieces.	\N	\N	\N	2025-10-28 15:16:12.894916-03	2025-10-28 15:17:55.435341-03	100	\N	\N	\N	A temporary exhibition by the National Museum of Decorative Art, co-produced with the Italian Institute of Culture, celebrating the history of the tourist poster.\nAn exhibition featuring 70 posters created between 1920 and 1950 by prominent Italian illustrators such as Marcello Dudovich, Mario Borgoni, Gino Boccasile, Aurelio Craffonara, and Franz Lenhart. These works shaped the visual identity of an entire era and spread the image of Italy around the world.	f	1046687329	-1330029945	-1410424120	-1415063601	\N	0	\N	0	236242382	f	f
761	757	pt-BR	Viaggio in Italia: 1920-1950, a era de ouro do cartaz turístico italiano	Design italiano, dos artifícios da Art Déco ao design gráfico de meados do século	De 20 de outubro de 2025\nA 20 de janeiro de 2026	O Instituto Italiano de Cultura de Buenos Aires (IIC) e o Museu Nacional de Artes Decorativas apresentam a exposição "Viaggio in Italia.\nA Era de Ouro do Cartaz Turístico Italiano (1520-1550)", uma exposição que convida a explorar a evolução da arte gráfica publicitária turística italiana por meio de uma extraordinária seleção de cartazes originais da coleção Alessandro Bellenda.\nProduzidos entre as décadas de 1920 e 1950, os cartazes em exposição foram produzidos pela ENIT (Agência Nacional Italiana de Turismo) e pelas Ferrovie dello Stato (Ferrovias Estatais), organizações que promoviam uma nova estética visual para apresentar a Itália ao mundo.\nAs obras trazem a assinatura de grandes mestres do design de cartazes italianos, como Marcello Dudovich, Mario Borgoni, Gino Boccasile, Aurelio Craffonara e Franz Lenhart, entre outros. Por meio do uso de cores vibrantes, figuras idealizadas e cenários icônicos, esses artistas criaram um verdadeiro cânone estético do turismo italiano, consolidando uma imagem do país que continua a inspirar designers e criativos até hoje, de acordo com um comunicado do IIC.\nAlém do seu valor artístico, a exposição oferece uma interpretação histórica e sociológica: os cartazes não apenas narram a evolução do design gráfico italiano, mas também a construção do imaginário do "bel paese" e as formas como a Itália se apresentou ao público internacional durante um período de intensa transformação social e cultural.\n"Viaggio in Italia" abre no âmbito da Semana da Língua Italiana no Mundo, um importante evento anual realizado globalmente pela rede de Institutos Culturais Italianos e Embaixadas Italianas, que celebra a língua e a criatividade italianas em suas múltiplas expressões.\nA exposição está sendo apresentada em dois locais: o Museu Nacional de Artes Decorativas, onde o núcleo principal está em exposição, e a Sala Roma do Istituto Italiano di Cultura, que abrigará uma seleção complementar de peças gráficas.	\N	\N	\N	2025-10-28 15:16:26.328307-03	2025-10-28 15:24:58.353806-03	100	\N	\N	\N	Uma exposição temporária do Museu Nacional de Artes Decorativas, coproduzida com o Instituto Italiano de Cultura, que celebra a história do cartaz turístico.\nUma exposição com 70 cartazes criados entre 1920 e 1950 por renomados ilustradores italianos como Marcello Dudovich, Mario Borgoni, Gino Boccasile, Aurelio Craffonara e Franz Lenhart. Essas obras moldaram a identidade visual de toda uma época e difundiram a imagem da Itália pelo mundo.	f	1046687329	-1330029945	-1410424120	-1415063601	\N	0	\N	0	236242382	f	f
775	774	es	new	\N	\N	\N	\N	\N	\N	2025-10-29 13:25:50.676686-03	2025-10-29 13:25:50.676703-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
776	774	en	new	\N	\N	\N	\N	\N	\N	2025-10-29 13:25:50.67879-03	2025-10-29 13:25:50.678801-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
777	774	pt-BR	Alberto Churba. Projeto Infinito	\N	De quarta a domingo | 13h às 19h | Entrada gratuita	O Museu Nacional de Artes Decorativas apresenta ALBERTO CHURBA. DESIGN INFINITO, a primeira retrospectiva dedicada a este proeminente designer e artista decorativo argentino.\n\nEntre as décadas de 1960 e 1990, ele liderou o Estudio CH, um escritório que transformou o cenário do design de interiores no país.\n\nA exposição, com curadoria de Sandra Hillar e Wustavo Quiroga, revisita seu legado com foco especial em diversas áreas — têxteis, tapetes, vidro, objetos e mobiliário —, todas reconhecidas por sua alta qualidade artística e técnica. Como exemplo de seu gênio criativo, a célebre cadeira Cinta integra as coleções do MoMA em Nova York e do Victoria & Albert Museum em Londres, entre outras instituições.\n\nOs designs de Alberto Churba definiram uma era e continuam sendo uma referência marcante para as novas gerações. De dentro de sua família, ele fomentou uma prática de design e empreendedorismo que transcendeu seu sobrenome, deixando uma marca inegável na cultura argentina.\n\nA exposição reunirá inúmeras peças de coleções particulares e institucionais que, pela primeira vez, serão apresentadas juntamente com o artista para narrar sua carreira, seu processo criativo e o impacto de sua obra na história do design argentino.	\N	\N	\N	2025-10-29 13:25:50.679742-03	2025-10-30 13:40:20.567599-03	100	\N	\N	\N	O Museu Nacional de Artes Decorativas apresenta uma exposição em homenagem a Alberto Churba, ícone incontestável do design argentino. "Alberto Churba: Design Infinito" convida os visitantes a explorar seu universo criativo por meio de peças emblemáticas que marcaram um ponto de virada na história do design na América Latina.	f	-231637515	0	-1271020507	-376545563	\N	0	\N	0	-700471454	f	f
832	830	en	new	\N	\N	\N	\N	\N	\N	2025-11-23 19:51:03.627318-03	2025-11-23 19:51:03.627321-03	100	\N	\N	1	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
833	830	pt-BR	new	\N	\N	\N	\N	\N	\N	2025-11-23 19:51:03.627729-03	2025-11-23 19:51:03.627731-03	100	\N	\N	1	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
840	839	es	new	\N	\N	\N	\N	\N	\N	2025-11-23 21:44:14.80823-03	2025-11-23 21:44:14.808232-03	100	\N	\N	1	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
841	839	en	new	\N	\N	\N	\N	\N	\N	2025-11-23 21:44:14.808803-03	2025-11-23 21:44:14.808805-03	100	\N	\N	1	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
842	839	pt-BR	new	\N	\N	\N	\N	\N	\N	2025-11-23 21:44:14.8099-03	2025-11-23 21:44:14.809901-03	100	\N	\N	1	\N	f	0	0	0	0	\N	0	\N	0	0	f	f
865	864	es	new	\N	\N	\N	\N	\N	\N	2025-11-28 10:51:25.223006-03	2025-11-28 10:51:25.223012-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	f	f
867	864	pt-BR	new	\N	\N	\N	\N	\N	\N	2025-11-28 10:51:25.225604-03	2025-11-28 10:51:25.225607-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	f	f
871	870	es	new	\N	\N	\N	\N	\N	\N	2025-11-28 12:18:14.015753-03	2025-11-28 12:18:14.01576-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	f	f
876	875	es	new	\N	\N	\N	\N	\N	\N	2025-11-28 21:32:20.05396-03	2025-11-28 21:32:20.053964-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	f	f
878	875	pt-BR	new	\N	\N	\N	\N	\N	\N	2025-11-28 21:32:20.056968-03	2025-11-28 21:32:20.056969-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	f	f
872	870	en	Odili Donald Odita	Songs of Life	\N	In Odili Donald Odita’s new large-scale commission in the MoMA lobby, bright colors and abstract patterns create a cascading kaleidoscope. And for the first time in the artist’s development process, music is his primary source of inspiration. “Music inspires me to reflect on issues in my paintings,” Odita said.\n\nSongs of Life was painted over six weeks, and visitors could watch as the site-specific work unfolded across the walls and columns. Executed with matte latex acrylic paint, the floor-to-ceiling installation offers an immersive experience, with Odita describing its colors as expressions of freedom and change. Each painted section is based on a selection of songs that expand upon the artist’s conception of the MoMA lobby as a meeting place for people from different backgrounds. “There’s a kind of communal energy because of the way music can affect the body,” Odita said. “That’s how I want my paintings to work, too.”	\N	\N	\N	2025-11-28 12:18:14.021592-03	2025-11-30 08:17:58.589577-03	100	\N	\N	1	In Odili Donald Odita's new large-scale commission in the MoMA lobby, bright colors and abstract patterns create a cascading kaleidoscope. And for the first time in the artist's development, music is his primary source of inspiration. "Music inspires me to reflect on the issues in my paintings," Odita has stated.	t	255346054	-1118522919	-700400329	1965399687	\N	0	\N	0	0	f	f
873	870	pt-BR	Odili Donald Odita	Canções da Vida	\N	Na nova obra de grande escala encomendada a Odili Donald Odita para o saguão do MoMA, cores vibrantes e padrões abstratos criam um caleidoscópio em cascata. E, pela primeira vez no processo criativo do artista, a música é sua principal fonte de inspiração. "A música me inspira a refletir sobre questões em minhas pinturas", disse Odita.\n\n"Songs of Life" foi pintada ao longo de seis semanas, e os visitantes puderam acompanhar o desenrolar da obra, criada especificamente para o local, nas paredes e colunas. Executada com tinta acrílica látex fosca, a instalação que vai do chão ao teto oferece uma experiência imersiva, com Odita descrevendo suas cores como expressões de liberdade e mudança. Cada seção pintada é baseada em uma seleção de músicas que expandem a concepção do artista sobre o saguão do MoMA como um ponto de encontro para pessoas de diferentes origens. "Há uma espécie de energia comunitária devido à maneira como a música pode afetar o corpo", disse Odita. "É assim que eu quero que minhas pinturas funcionem também."	\N	\N	\N	2025-11-28 12:18:14.022727-03	2025-11-30 08:21:42.21124-03	100	\N	\N	1	Na nova obra de grande escala encomendada a Odili Donald Odita para o saguão do MoMA, cores vibrantes e padrões abstratos criam um caleidoscópio em cascata. E, pela primeira vez na trajetória do artista, a música é sua principal fonte de inspiração. "A música me inspira a refletir sobre os temas presentes em minhas pinturas", afirmou Odita.	t	255346054	-1118522919	-700400329	1965399687	\N	0	\N	0	0	f	f
866	864	en	Wifredo Lam, when I don't sleep, I dream	\N	During the museum's opening hours.	Wifredo Lam's paintings expanded the horizons of modernism by creating a meaningful space for the beauty and depth of Black diaspora culture. Born in Cuba at the beginning of the 20th century, Lam forged his political convictions and commitment to modern painting in war-torn Europe in the 1930s. His exile and return to the Caribbean after 18 years abroad prompted him to radically reimagine his artistic project through Afro-Caribbean narratives.\n\nFor Lam, of African and Chinese descent, the creation of his new and vivid imagery was more than a form of self-reflection. He famously declared that his art was an "act of decolonization." His formal experiments, his transformative figures and landscapes, and his affinity for poetry and collaboration allowed him to break with the colonial structures he encountered in art and life. “I knew I risked not being understood by the average person or anyone else,” Lam said, “but a true image has the power to set the imagination to work, even if it takes time.” Wifredo Lam: When I Don’t Sleep, I Dream is the first retrospective in the United States to present the complete trajectory of Lam’s extraordinary vision, inviting us to see the world in a new way.	\N	\N	\N	2025-11-28 10:51:25.224543-03	2025-11-30 08:43:09.820236-03	100	\N	\N	1	The first retrospective in the United States to present the complete trajectory of the extraordinary vision of Wilfredo Lam (1902-1982), inviting us to see the world in a new way. Lam famously declared that his art was an "act of decolonization."	t	1072737894	0	-963763460	1463637158	\N	0	\N	0	1960630036	f	f
877	875	en	Collection 1880s-1940s	Modern Latin American art	\N	Organized in roughly chronological order, each of the presentations on this floor explores a specific theme. A room might be dedicated to a particular artist, medium, or discipline; a specific place at a particular time in history; or a shared creative idea. An ongoing program of frequent reinstallation will present a wide range of artworks in new combinations, a reminder that countless ideas and stories can be explored through the Museum's dynamic collection.	\N	\N	\N	2025-11-28 21:32:20.055935-03	2025-12-18 16:49:42.409707-03	100	\N	\N	1	Organized in roughly chronological order, each of the exhibits on this floor explores a specific theme. A room might be dedicated to a specific artist, medium, or discipline, a particular place at a historical moment, or a shared creative idea.	t	-1410467467	1682012182	217347460	2097107023	\N	0	\N	0	0	f	f
1495	233	en	Great works of Impressionism		\N	The Museum's extensive collection of Impressionist and Post-Impressionist art includes works by prominent artists such as Édouard Manet, Claude Monet, Camille Pissarro, Berthe Morisot, Edgar Degas, Vincent Van Gogh, Paul Gauguin, and Henri Toulouse Lautrec, among others.	\N	\N	\N	2025-12-18 18:00:11.101521-03	2025-12-18 18:00:14.283583-03	100	\N	\N	1	The museum's extensive collection of Impressionist and Post-Impressionist art includes works by prominent artists such as Édouard Manet, Claude Monet, Camille Pissarro, Berthe Morisot, Edgar Degas, Vincent Van Gogh, Paul Gauguin, and Henri Toulouse Lautrec, among others.	t	611113174	0	943164252	943164252	\N	0	\N	0	0	t	f
27578	27577	es	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:19:31.096812-03	2026-01-10 18:19:31.096818-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
27579	27577	en	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:19:31.099027-03	2026-01-10 18:19:31.099031-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
27580	27577	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:19:31.100019-03	2026-01-10 18:19:31.100022-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
27922	27921	es	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:23:36.133204-03	2026-01-10 18:23:36.133208-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
27923	27921	en	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:23:36.134448-03	2026-01-10 18:23:36.134453-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
27924	27921	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:23:36.135221-03	2026-01-10 18:23:36.135224-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28036	28035	es	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:24:11.859339-03	2026-01-10 18:24:11.859344-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28037	28035	en	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:24:11.860545-03	2026-01-10 18:24:11.86055-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28038	28035	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:24:11.861488-03	2026-01-10 18:24:11.861492-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28385	28384	es	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:34:21.529834-03	2026-01-10 18:34:21.529835-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28386	28384	en	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:34:21.53049-03	2026-01-10 18:34:21.53049-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28387	28384	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:34:21.530954-03	2026-01-10 18:34:21.530954-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28499	28498	es	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:34:52.817639-03	2026-01-10 18:34:52.81764-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28500	28498	en	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:34:52.81828-03	2026-01-10 18:34:52.818281-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28501	28498	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:34:52.818889-03	2026-01-10 18:34:52.81889-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28683	28682	es	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:35:13.857112-03	2026-01-10 18:35:13.857114-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28684	28682	en	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:35:13.858102-03	2026-01-10 18:35:13.858105-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28685	28682	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:35:13.859135-03	2026-01-10 18:35:13.859136-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28797	28796	es	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:36:04.135362-03	2026-01-10 18:36:04.135363-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28798	28796	en	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:36:04.136713-03	2026-01-10 18:36:04.136716-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
28799	28796	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-01-10 18:36:04.137634-03	2026-01-10 18:36:04.137636-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
37405	37404	es	new	\N	\N	\N	\N	\N	\N	2026-01-12 16:03:42.467498-03	2026-01-12 16:03:42.467503-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
37406	37404	en	new	\N	\N	\N	\N	\N	\N	2026-01-12 16:03:42.469678-03	2026-01-12 16:03:42.469681-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
37407	37404	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-01-12 16:03:42.470417-03	2026-01-12 16:03:42.47042-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
37830	37829	es	new	\N	\N	\N	\N	\N	\N	2026-01-13 04:38:25.553408-03	2026-01-13 04:38:25.553411-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
37831	37829	en	new	\N	\N	\N	\N	\N	\N	2026-01-13 04:38:25.554565-03	2026-01-13 04:38:25.554568-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
37832	37829	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-01-13 04:38:25.555535-03	2026-01-13 04:38:25.555541-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
\.


--
-- Data for Name: artexhibitionsection; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionsection (id, artexhibition_id, language, name, subtitle, info, state, photo, video, audio, masterlanguage, translation, draft, created, lastmodified, lastmodifieduser, intro, spec, opens, audioautogenerate, namekey, usethumbnail, speechaudio) FROM stdin;
\.


--
-- Data for Name: artexhibitionsectionrecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionsectionrecord (id, artexhibitionsection_id, language, name, subtitle, info, photo, video, audio, created, lastmodified, lastmodifieduser, draft, namekey, state, intro, usethumbnail, name_hash, subtitle_hash, info_hash, intro_hash, spec_hash, otherjson, otherjson_hash, opens, opens_hash, audioautogenerate, audioauto) FROM stdin;
\.


--
-- Data for Name: artexhibitionstatustype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionstatustype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser, state, language, draft, audioautogenerate) FROM stdin;
131	En preparación	coming	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	1	\N	\N	t
132	En curso	enabled	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	1	\N	\N	t
133	Terminada	terminated	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	1	\N	\N	t
134	Cancelada	cancelled	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	1	\N	\N	t
\.


--
-- Data for Name: artist; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artist (id, language, masterlanguage, person_id, nickname, created, lastmodified, lastmodifieduser, draft, name, state, audio, photo, video, info, intro, opens, spec, speechaudio, audioautogenerate, subtitle, translation) FROM stdin;
5031	es	es	194	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5032	es	es	188	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5033	es	es	182	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5034	es	es	183	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5035	es	es	184	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5036	es	es	185	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5037	es	es	186	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5038	es	es	187	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5039	es	es	189	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5040	es	es	190	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5041	es	es	191	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5042	es	es	192	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5043	es	es	193	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5045	es	es	263	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5046	es	es	264	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5047	es	es	265	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5048	es	es	266	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5049	es	es	267	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5050	es	es	268	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5051	es	es	269	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5052	es	es	308	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5053	es	es	333	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5055	es	es	398	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5056	es	es	399	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5057	es	es	400	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5058	es	es	447	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5059	es	es	401	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5060	es	es	480	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5061	es	es	588	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5062	es	es	593	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5063	es	es	581	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5064	es	es	602	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5065	es	es	481	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5066	es	es	763	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5067	es	es	880	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
5068	es	es	1113	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
\.


--
-- Data for Name: artwork; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artwork (id, name, namekey, title, titlekey, shortname, artworktype_id, subtitle, subtitlekey, spec, speckey, info, infokey, photo, video, audio, year, person_owner_id, institution_owner_id, created, lastmodified, lastmodifieduser, intro, introkey, site_owner_id, usethumbnail, url, state, qrcode, language, draft, translation, masterlanguage, opens, audioautogenerate, speechaudio, source, epoch, objecttype) FROM stdin;
22902	Zambezia Zambezia	\N	\N	\N	\N	\N	\N	\N	\N	\N	"Zambezia, Zambezia" es una famosa pintura surrealista de 1950 del artista cubano Wifredo Lam, que representa una figura totémica, mitad humana y mitad animal (inspirada en la "mujer-caballo" de la santería), utilizando su característico estilo que fusiona elementos africanos, chinos y europeos en un mundo poético y mítico.\r\n\r\nLa obra combina formas orgánicas, líneas angulares y un profundo sentido del movimiento, creando una atmósfera onírica y un lenguaje visual único.\r\nInspiración: Lam se inspiró en los ritos de la santería y los orishas, así como en sus propias experiencias culturales, para crear sus icónicas figuras híbridas y totémicas.	\N	23041	\N	\N	0	\N	\N	2026-01-07 08:27:21.438823-03	2026-01-07 08:27:21.438829-03	100	\N	\N	860	t	\N	1	\N	..	\N	\N	es	\N	t	\N	\N	\N	0
225	Le Moulin de la Galette	le-moulin-de-la-galette	Le Moulin de la Galette	\N	\N	126		\N	61 x 50 cm.	\N	Fueron condiciones muy particulares, podría decirse de transición, las que llevaron a Vincent van Gogh a pintar este Moulin de la Galette, obra que se inscribe en una importante serie de vistas de París. El artista holandés llegó a la capital francesa en marzo de 1886. Allí se encontró –sin haberle siquiera avisado con anterioridad– con su hermano Theo, que llevaba siete años instalado en la ciudad y dirigía por cuenta de Boussod et Valadon una pequeña galería en el boulevard Montmartre.\r\n\r\nCuando Van Gogh pinta su Moulin, está como deslumbrado por el contexto artístico circundante, de una infrecuente densidad: el manifiesto simbolista de Moréas, la última exposición impresionista (donde se presentó La Grande Jatte de Seurat), la publicación de las Illuminations de Rimbaud y de L’oeuvre de Zola se agolpan en el escenario cultural. Descubre además los encantos de la ciudad, las galerías y las discusiones animadas en los cafés. También las obras del Louvre, museo que visita con frecuencia. Para coronar esas experiencias, suma al frenesí un toque académico incorporándose, para afirmar sus cualidades técnicas, como alumno del taller del muy clásico Cormon. Allí alterna con Toulouse-Lautrec y Anquetin.\r\n\r\nEs sabido que Van Gogh llevó adelante su carrera con una determinación tan humilde como profunda, y sin duda esos meses parisinos movilizaron en él un poderoso deseo de crear. La Butte Montmartre formaba parte de su vida diaria, ya que se alojaba en la casa de Theo, que vivía en ese barrio. Los lazos que unían a los dos hermanos eran por cierto muy fuertes, pero en el otoño de 1886 la promiscuidad del nº 54 de la rue Lepic –desde donde el panorama de la ciudad era magnífico– comenzó a volverse pesada. Esto llevó a Vincent a reemplazar la naturaleza muerta, que podía realizar en el departamento, por el paisaje. Lo cual lo impulsó, en un primer momento, a representar los alrededores inmediatos, y por ende Montmartre (1).Por “Moulin de la Galette” se entiende el café-concert que se extendía en realidad entre dos molinos de Montmartre: el Blute-Fin y el Radet. \r\n\r\nEn la muy célebre composición de Renoir titulada Le bal du Moulin de la Galette (1876, Musée d’Orsay, París), asistimos a las fiestas y los bailes que acompasaban la vida del lugar. Pero no fue el molino de viento como tal lo que había interesado entonces al pintor impresionista. Van Gogh, en cambio, adoptó una actitud totalmente distinta. Se concentró en este caso en uno de los dos edificios afectados por el café-concert: el Blute-Fin, antigua construcción en madera erigida en 1622 y que servía sobre todo para moler trigo.El punto de vista adoptado para el molino –la parte posterior del edificio– no tenía nada de original y lo elegían por la misma época montones de pintores (que saturaban el barrio de Montmartre). \r\n\r\nSe sabe sin embargo que Van Gogh ensayó en torno de su motivo varias otras vistas para circunscribirlo mejor. Es asombrosa aquí la claridad, la frescura, incluso, del cuadro, dominado por pinceladas vivas de azul que van virando al blanco, en tonalidades muy homogéneas. La perspectiva desde abajo adoptada por el pintor genera una línea del horizonte baja que deja estallar ese gran cielo luminoso. Van Gogh, que en abril de 1885 había trabajado con fervor en sus oscuros Mangeurs de pommes de terre, parece de pronto exultar al contacto con el ambiente parisino. Es verdad que ya había comenzado a aclarar su paleta en Anvers bajo la influencia de los cuadros de Rubens, pero Montmartre le inspira sobre todo, en el tratamiento de la atmósfera, una manera mucho más liviana. Se comprende, pues, lo que escribió a uno de sus amigos artistas (H. M. Levens) en 1887: “No hay otra cosa que París, y por difícil que la vida pueda ser aquí, aunque fuera peor y más dura, el aire francés limpia el cerebro y hace bien, muchísimo bien”. \r\n\r\nLa bandera tricolor flameando al viento, representada en unas pocas pinceladas nerviosas, traduce perfectamente, por otro lado, esa plenitud triunfal en las tierras de Francia.Observemos, asimismo, que Van Gogh eligió una vista que no permite adivinar en nada las diversiones del Moulin de la Galette. \r\n\r\nHay en él un interés pintoresco por el lugar, pero también una voluntad de mostrar un espacio de trabajo en el límite entre la ciudad y el campo, en lo que era todavía, en esa época, un barrio periférico de París, poblado de gente modesta. La pareja de personajes abajo a la derecha, además de indicar la escala, está vestida de manera humilde y casi campesina. Van Gogh desliza en su obra una dimensión social que lo conmueve particularmente.Podríamos afirmar, entonces, que este óleo es un excelente testimonio de la euforia del pintor holandés que recorre París y a la vez un ejemplo típico de tableau-laboratoire. Van Gogh experimenta en él serenamente sus conceptos plásticos, que encontrarían su realización absoluta unos meses más tarde, en el sur de Francia, en Provenza, región que le “limpia[ría] el cerebro” (como escribe él) con la intensidad combinada del genio y la locura.	\N	319	\N	\N	1886	\N	135	2025-05-19 12:29:43.192411-03	2025-10-06 15:44:04.113106-03	100	Fueron condiciones muy particulares, podría decirse de transición, las que llevaron a Vincent van Gogh a pintar este Moulin de la Galette, obra que se inscribe en una importante serie de vistas de París. El artista holandés llegó a la capital francesa en marzo de 1886. Allí se encontró –sin haberle siquiera avisado con anterioridad– con su hermano Theo, que llevaba siete años instalado en la ciudad y dirigía por cuenta de Boussod et Valadon una pequeña galería en el boulevard Montmartre.Cuando Van Gogh pinta su Moulin, está como deslu	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
585	El circo más lindo del mundo	new	\N	\N	\N	\N	\N	\N	Técnica: Témpera y grafito sobre papel\r\n47,7 x 50,5 cm\r\nNro. de inventario: 2001.11\r\nDonación: Eduardo F. Costantini, Buenos Aires	\N	El deseo de Barradas de apresar el tiempo que fluye (representando su fluencia) ya estaba presente en los ágiles croquis de café realizados en Montevideo, tendientes a captar la impronta de una situación, de un gesto, de una contingencia espacial. Ese temperamento se tamizó en el contacto con la nueva plástica europea, donde aquella primera voluntad impresionista asimiló el estructuralismo de los cubistas y el dinamismo de los futuristas (especialmente de Gino Severini). Ésta es, sintéticamente, la línea de la cual procede su vibracionismo barcelonés, al que Barradas le imprimió un espíritu jovial, aun cuando pueden distinguirse en él (entre 1917 y 1919) varias modalidades. Tampoco puede descartarse que algunas de estas modalidades tengan relación con el estrecho vínculo que este pintor mantuvo con su colega uruguayo Joaquín Torres García, quien en ese momento se encontraba abocado a la captación del dinamismo urbano y con quien llevó a cabo una exposición conjunta en Galerías Dalmau a fines de 1917.\r\n\r\nEl circo más lindo del mundo pertenece a la serie que realizó fundamentalmente en 1918, caracterizada por una especie de terror vacuo respecto del plano pictórico, el cual es sometido a una saturación rítmica y cromática que apenas permite discernir la intención figurativa que anima por momentos algunos de sus fragmentos interiores. Barradas solía procesar estas pinturas a partir de breves bocetos de café que le servían como una suerte de ayuda memoria para la elaboración plástica de taller.\r\n\r\nAntonio de Ignacios, hermano de Barradas, recogió en su libro una frase del pintor que vendría a definir la idea plástica del vibracionismo (y también de la etapa clownista que le sigue) como un ejercicio dinámico de la memoria visual: “Quiero pintar lo que queda de una persona cuando se va”. Es como decir: “Pinto lo que queda en mi mente de una escena cuando ella desaparece”. Se trataría de una poética que, a fuerza de ser pintura “mental”, reminiscente, configura una representación de ausencias. Es significativo, en este sentido, el título que Barradas otorgó a una conferencia que pronunció en 1921, durante una velada ultraísta madrileña: “El anti-yo, estudio teórico sobre el clownismo y dibujos en la pizarra”. Su obsesión se centraba en la relación del artista con lo cotidiano visible, mediada a través de la obra. De acuerdo con esta fórmula, lo real no sería sino un reflejo del “yo”, y la obra plástica, una formalización de lo que él denominaba el “anti-yo”, una suerte de desprendimiento, de residuo resultante de la identificación inicial del artista con el objeto que lo mira.\r\n\r\nReconociendo en la pintura un acto de autorrepresentación, llegó a decir: “En este mismo momento veo una mesa, una botella y un caballo en un espejo. Es claro: no es la mesa, la botella, el caballo lo que en realidad VEO; me VEO YO”.1\r\n\r\nEn El circo más lindo del mundo vuelve a encontrarse la incorporación de letras, palabras, números –característica ya presente en sus acuarelas y dibujos de 1917–, así como la imagen infaltable de la rueda radial, un signo que adquirió estatus simbólico en sus pinturas durante este período.	\N	586	\N	\N	1918	\N	\N	2025-09-19 10:14:22.893352-03	2025-09-21 19:52:36.304081-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
404	En la costa de Valencia	en-la-costa-de-valencia	En la costa de Valencia	\N	\N	126		\N	Oleo sobre tela 57 x 88,5 cm.	\N	Esta escena costumbrista que representa el trabajo de los pescadores que vuelven de la faena y el juego de un niño se desarrolla posiblemente en la playa del Cabañal, frecuentada por Sorolla, que se caracterizaba por conjugar, en torno al 1900, tanto a pescadores y sus familias como a pintores en busca de temáticas que mostraran el ser regional. Esta combinación de los trabajos preindustriales de las clases populares, por una parte, y de pintura pleinairista de visos nacionalistas, por otra, brindaba una imagen optimista del Levante español distante del duro proceso de transformación del paisaje de la región por la industrialización. La mecanización y el gigantismo urbano eran la nueva fisonomía que modelaba la vida y las costumbres del Levante. Sin embargo, la actitud de huida nostálgica de la civilización hacia la rústica naturaleza de pintores como Sorolla hacía de estas costas un refugio bucólico frente a las consecuencias generadas por la Revolución Industrial.\\nDetrás de este costumbrismo de ambiente marino se encuentra la verdadera raison d’être de las búsquedas de Sorolla: la luz mediterránea. \r\n\r\nSu sensibilidad lumínica pertenece a una de las dos tendencias de la pintura española que marcaron la vuelta del siglo: la “España blanca” contrapuesta a la “España negra” de Zuloaga, Solanas y Romero de Torres, entre otros. La luz, la gran protagonista de esta pintura, es captada con rapidez y al plein air del mediodía dando como resultado una obra definida por la instantaneidad, el abocetamiento y una atmósfera vibrante producto de los reflejos del sol enceguecedor en su cenit sobre el mar.\r\n\r\nSorolla ocupa un lugar especial dentro del movimiento luminista cuyos centros fueron Valencia y Sitges. \r\n\r\nA diferencia del resto de los luministas, reunió en sí influencias que supo dosificar, como la del impresionismo y la de los pintores escandinavos como Peder Severin Krøyer (1851-1909), Viggo Johansen (1851-1935) y Anders Zorn (1860-1920) o del alemán Adolph von Menzel (1815-1905), cuya obra conoció en París. En el debate de tradición-modernidad, su pintura no solo tenía la capacidad de resolver el dilema entre el conocimiento académico y la experimentación impresionista, equilibrando ambas posiciones, sino que también lograba mantener un juste milieu artístico (en un momento de vanguardias contestatarias y actitudes reaccionarias hacia ellas) que le brindó un éxito comercial internacional sin precedente en los otros luministas. Todo esto lleva a la historiografía artística a considerarlo como el pintor que clausura el movimiento luminista levantino.\\nLa captación instantánea y lumínica de las formas, próxima al impresionismo parisino pero ajena a sus inquietudes, es una constante en los pintores nacidos en el Levante español y activos a finales del siglo XIX y principios del siguiente, de quienes el estilo sorollesco se nutre. \r\n\r\nEstos, a pesar de su formación conservadora, se atrevieron a incursionar en técnicas inusuales y de gran modernidad en relación con las normas académicas que imponían el gusto por lo bituminoso, provocando un cambio sustancial en la manera de pintar. Al focalizarse en la presencia dominante del mar en el que la vista se pierde al infinito, en su atmósfera costera y en la sugestión hipnótica de la luz y sus matices, propusieron al espectador involucrarse en una percepción de la realidad que es anterior a una primera estructuración gestáltica, es decir una percepción en la cual las sensaciones anteceden a las formas. El luminismo levantino se inserta en una tendencia al aclaramiento de la paleta que tiene lugar en la pintura europea del siglo XIX, evidencia de búsquedas en relación con el color y con la luz generadas a partir del desarrollo de la pintura de paisaje y de la captación de efectos naturalistas.\r\n\r\nSorolla creó una “maniera” posible de ser emulada a nivel compositivo y “digerible” temáticamente a pesar de su modernidad. Su técnica lumínica se oponía diametralmente a la paleta armada del frío academicismo que había entronizado a la pintura de historia con sus composiciones artificiosas. Sin embargo, la organización deliberada de Sorolla de masas de luz y color lo acercaban al pensamiento académico que gustaba de componer fuertemente el lienzo equilibrando las formas y los colores. La adhesión, en parte, a la técnica impresionista no debe confundirse con la adopción del descubrimiento de la impresión lumínica mediante la yuxtaposición de colores puros colocando en un plano secundario la estructura, ni tampoco con la disolución de la forma –divergencias con el impresionismo que lo unían al resto de los luministas– aunque su factura suele oscilar hacia una pincelada de coma impresionista. Estas características técnicas se observan en esta obra, como así también en La vuelta de la pesca (1898, inv. 2007, MNBA), ambas dotadas de una infraestructura muy compleja. Cierta impresión y velocidad de la poética de Sorolla nunca pudieron ser emuladas acabadamente por sus seguidores.\r\n\r\nEn cuanto a la temática, el artista impuso la descripción de una edad de oro del Mediterráneo, una Arcadia donde nos presenta hombres en simple y dulce armonía con la naturaleza. El gesto de los pescadores y el pequeño jugando representan lo que fluye y es efímero. Aun así, estos instantes evocan la eternidad de los tipos humanos de la región.	\N	416	\N	\N	1898	\N	135	2025-06-10 15:14:26.074639-03	2025-10-29 08:49:28.621703-03	100	Esta escena costumbrista que representa el trabajo de los pescadores que vuelven de la faena y el juego de un niño se desarrolla posiblemente en la playa del Cabañal, frecuentada por Sorolla, que se caracterizaba por conjugar, en torno al 1900, tanto a pescadores y sus familias como a pintores en busca de temáticas que mostraran el ser regional. Esta combinación de los trabajos preindustriales de las clases populares, por una parte, y de pintura pleinairista de visos nacionalistas, por otra, brindaba una imagen optimista del Levante e	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
406	Retrato de Juan Manuel de Rosas	retrato-de-juan-manuel-de-rosas	Retrato de Juan Manuel de Rosas	\N	\N	126		\N	Oleo sobre tela 100 x 79,8 cm.	\N	El Retrato de Juan Manuel de Rosas, pintado por el artista francés Raymond Monvoisin hacia 1842, ofrece una representación simbólica y estratégica del caudillo argentino.  A diferencia de otros cuadros oficiales donde Rosas aparece con uniforme militar, en esta obra se lo muestra con un poncho, resaltando su imagen de líder popular y no meramente militar.\\nMonvoisin se había formado en la tradición académica europea, pero para 1842 estaba más cerca del romanticismo. \r\n\r\nRosas aparece de perfil, con expresión seria y firme, destacando su autoridad, y su mirada apunta a donde se origina el viento como  controlando la naturaleza. Mitre señaló “es el retrato más parecido que conozco de Rosas, hay mucho de emperador romano pero atenuado y corregido por un acento criollo”.\r\n\r\nLa tradición artística oficial argentina considera al cuadro un estudio preliminar de una obra posterior desaparecida o que no existió. Según Rodrigo Cañete en “Historia a contrapelo del arte argentino”,  esta idea está contaminada por el prejuicio de negarse a aceptar el verdadero carácter de la obra.  Para Cañete, el artista, acogido en Chile por los enemigos de Rosas, tuvo el talento de hacer un cuadro ambigüo para que ambos bandos -unitarios y federales- puedan utilizarlo como bandera. \r\n\r\nLos rasgos de Rosas son imperiales pero carecen de la musculatura heroica de los emperadores y están levemente feminizados, y el poncho y la ropa son parecidos a los retratos de sus enemigos como gaucho bárbaro, líder de lo que Sarmiento despectivamente llamaba la “montonera árabe”.  \r\n\r\nPor su parte, para los rosistas, representa un emperador-dios-benefactor-madre tierra, al estilo de la Virgen María de las imágenes sudamericanas que la fusionan con la Pachamama india, como la Virgen María del Cerro Rico de Potosí,  pero con referencias académicas sutiles a grandes maestros como Zurbarán y Velázquez.	\N	415	\N	\N	1842	\N	135	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	El Retrato de Juan Manuel de Rosas, pintado por el artista francés Raymond Monvoisin hacia 1842, ofrece una representación simbólica y estratégica del caudillo argentino.  A diferencia de otros cuadros oficiales donde Rosas aparece con uniforme militar, en esta obra se lo muestra con un poncho, resaltando su imagen de líder popular y no meramente militar.\\nMonvoisin se había formado en la tradición académica europea, pero para 1842 estaba más cerca del romanticismo. Rosas aparece de perfil, con expresión seria y firme, destacando su autoridad, y su mirada apunta a donde se origina el viento como  controlando la naturaleza. Mitre señaló “es el retrato más parecido que conozco de Rosas, hay mucho de emperador romano pero atenuado y corregido por un acento criollo”.\\nLa tradición artística oficial argentina considera al cuadro un estudio preliminar de una obra posterior desaparecida o que no existió. Según Rodrigo Cañete en “Historia a contrapelo del arte argentino”,  esta idea está contaminada por el prejuicio de negarse a aceptar el verdadero carácter de la obra.  Para Cañete, el artista, acogido en Chile por los enemigos de Rosas, tuvo el talento de hacer un cuadro ambigüo para que ambos bandos -unitarios y federales- puedan utilizarlo como bandera. Los rasgos de Rosas son imperiales pero carecen de la musculatura heroica de los emperadores y están levemente feminizados, y el poncho y la ropa son parecidos a los retratos de sus enemigos como gaucho bárbaro, líder de lo que Sarmiento despectivamente llamaba la “montonera árabe”.  Por su parte, para los rosistas, representa un emperador-dios-benefactor-madre tierra, al estilo de la Virgen María de las imágenes sudamericanas que la fusionan con la Pachamama india, como la Virgen María del Cerro Rico de Potosí,  pero con referencias académicas sutiles a grandes maestros como Zurbarán y Velázquez.	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
229	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	danseuse-et-admirateur-derri-re-la-sc-ne-bailarina-y-admirador-tras-la-escena	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	\N	\N	126		\N	60,5 x 73,5 cm.	\N	​A fines del siglo XIX París se perfilaba como la ciudad elegida por la burguesía para desplegar su hedonismo. \r\nLos sitios preferidos para hacerlo eran los cafés, la Ópera, los jardines suburbanos, el hipódromo o la ribera. Todos ellos quedaron registrados en las pinturas de los impresionistas o post-impresionistas, que examinaron en sus obras las distintas facetas de la vida moderna, tanto para criticarla, como para adularla. Jean-Louis Forain fue uno de estos artistas, de familia modesta, hijo de un “pintor de brocha gorda”, que inició sus estudios en el atelier del pintor Jean-Léon Gérôme, para luego estudiar con Jean-Baptiste Carpeaux quien lo introdujo en la representación de escenas bíblicas en un sentido moderno, temática importante en su desarrollo artístico posterior. \r\n\r\nTambién fue André Gill quien en 1870 le enseñó los secretos del arte de la ilustración, que utilizó para representar escenas de la vida cotidiana.No escapó al influjo de Manet, Renoir, Degas, quienes lo acercaron a las teorías del color y de la luz, tan experimentadas por el impresionismo. Sin embargo, con quien estableció mayor afinidad, tanto a nivel artístico como humano, fue con Degas. Tan es así que en varias ocasiones pintaron juntos trabajando sobre un mismo modelo. Bailarinas, carreras de caballos, desnudos, escenas de café, se repitieron en sus obras, pero abordadas desde una perspectiva diferente; mientras que Degas puso el acento en la captura de un instante, de un momento particular, cercano a un lenguaje fotográfico, Forain se concentró en lo gestual, en la fibra expresiva de los personajes, en los caracteres que transmiten su condición social (1).Uno de los temas que ambos comparten es el backstage de la Ópera. \r\n\r\nEn Bailarina y admirador tras la escena, Forain pone de relieve la particular relación, ambigua y muchas veces dolorosa, que se gestaba entre las bailarinas y los abonados a la Ópera. Estos últimos representaban una elite política, financiera y cultural, que llegaba a reservar casi la mitad de las localidades del teatro. Varias miembros del cuerpo de ballet provenían de familias pobres y solían aceptar un soporte económico a cambio de favores sexuales; otras establecían una amistad con “el protector” y solo unas pocas se casaban con ellos. El foyer, la sala de ensayo o calentamiento, oficiaba durante los intervalos como lugar de encuentro con los abonados. En este sitio transcurre la escena en cuestión, “el admirador”, que despliega su condición social a partir de los detalles de su atuendo elegante, tiene la mirada perdida en un punto fijo en el piso, y se refriega sus manos con guantes blancos. La bailarina sentada a su lado apenas gira su cabeza para observarlo, dejando caer al piso, desganadamente, el arco de violín que sostiene en sus manos. Se puede sobreentender un vínculo en las condiciones antes relatadas, a pesar de que ellos casi no interactúan. En el extremo opuesto de la composición y frente al decorado del escenario, un grupo de bailarinas cuchichea, tal vez adivinando aquello que el admirador no se anima a decirle a la mujer deseada. La resolución pictórica de la escena tiene muchas filiaciones con el tipo de factura abocetada y rápida de las obras contemporáneas de Degas, en las que se desdibujan los detalles a medida que se alejan del primer plano.	\N	314	\N	\N	1887	\N	135	2025-05-19 12:29:43.192411-03	2025-10-07 17:04:19.065482-03	100	​ fines del siglo XIX París se perfilaba como la ciudad elegida por la burguesía para desplegar su hedonismo. Los sitios preferidos para hacerlo eran los cafés, la Ópera, los jardines suburbanos, el hipódromo o la ribera. Todos ellos quedaron registrados en las pinturas de los impresionistas o post-impresionistas, que examinaron en sus obras las distintas facetas de la vida moderna, tanto para criticarla, como para adularla. Jean-Louis Forain fue uno de estos artistas, de familia modesta, hijo de un “pintor de brocha gorda”, que inici	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
217	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	portrait-de-suzanne-valadon-madame-suzanne-valadon-artiste-peintre-retrato-de-suzanne-valadon-pintora	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	\N	\N	126		\N	55 x 46 cm. - Marco: 77,5 x 66,5 x 6,5 cm.	\N	En 1885, con apenas veintiún años, en busca de independencia y deseoso de escapar del severo control paterno, Henri de Toulouse-Lautrec (1) abandonó el sur de Francia para ir a París. Se estableció en el barrio de Montmartre (2) con su amigo René Grenier. La integración en la vida de la butte no fue simple; contribuyeron a su inserción sus amigos, entre ellos François Gauzi, compañero de estudios en el atelier de Ferdinand Cormon.En estrecho contacto con Van Gogh, Vallotton, Bonnard, Lautrec participó plenamente del clima artístico parisino, que en ese tiempo buscaba en varios sentidos la superación del impresionismo, y orientó sus indagaciones hacia una pintura adherida a la realidad que, a través de la estilización formal, fijara los tipos psicológicamente característicos. Participante desencantado del ambiente de Montmartre –cafés-concert, prostíbulos, salas de baile, teatros–, en sus cuadros y en los numerosos bocetos tomados de la realidad Lautrec trazó toda la intimidad y la tristeza de ese submundo humano. Fundamental en ese ambiente fue el encuentro con una mujer que, antes que una seguidilla de modelos y amantes, entró en su vida y en su obra. Se trata de Marie-Clémentine Valade (3), más famosa por el nombre de Suzanne Valadon, que prestó sus rasgos a algunos de los “tipos femeninos” más conocidos de Lautrec (4). Marie-Clémentine era una jovencita sin medios materiales ni culturales, nacida en 1865, de madre modista y padre desconocido. Se aventuró en la actividad circense como acróbata, pero una caída la obligó a desistir; probó otros trabajos humildes hasta que decidió ofrecerse como modelo artística. Comenzó a ser solicitada por los mejores pintores de la época, el primero de todos Puvis de Chavannes, que fue durante cierto período también su amante. Siguieron Renoir, Manet, Gauguin; a todos regaló su belleza, de todos tomó algo. Apasionada por el dibujo, durante las sesiones de pose la joven modelo observaba a los maestros trabajando; muy pronto todos, particularmente Lautrec (5), la alentaron a seguir su pasión. Fue él quien le sugirió el nombre artístico de Suzanne Valadon, porque como la Susana bíblica, Marie estaba rodeada de “viejos ávidos”. Con este retrato titulado Madame Valadon, artiste peintre, Lautrec ratifica el rol de pintora que había asumido Marie (6).Toulouse-Lautrec solía pasar del taller al aire libre. Sus pinturas a cielo abierto constituyeron una etapa que le permitió adaptar estilística y temáticamente ciertos valores adquiridos de la pintura en plein air, no tanto en la línea de los estudios lumínicos de los impresionistas sino en una búsqueda de mayor libertad. En esta pintura Suzanne está sentada, representada frontalmente, por delante de un paisaje otoñal. Se trata muy probablemente del jardín del viejo Forest, un terreno dedicado al tiro con arco, situado en la esquina del boulevard de Clichy y la rue Caulaincourt, a pocos pasos del atelier del artista, donde Lautrec realizó distintos retratos femeninos (Justine Dieuhl, 1891, Musée d’Orsay, París). Su cuerpo está delimitado por un contorno negro bien marcado –reminiscencia del ejemplo de Degas, por quien Lautrec sentía real veneración– dentro del cual, sin embargo, los volúmenes parecen llenados sumariamente por amplios trazos de color, cuya porosidad es exaltada por la textura de la tela sin preparación. El rostro de la modelo se recorta contra un conjunto de planos cromáticos que han perdido la rigidez y el rigor del contemporáneo Retrato de Jeanne Wenz (1886, The Art Institute of Chicago), testimoniando ya la gran maestría y la verdadera ductilidad de pincelada adquirida por el artista. El fondo, una armonía de amarillos, beiges y marrones, confundidos en toques diluidos, arroja una suerte de velo azulado sobre el cuerpo de la mujer, suavizando la expresión firme del personaje. El retrato femenino suele ser un compromiso entre la elegancia y el realismo a partir de la observación directa. Los artistas mundanos tienen como única preocupación realzar la belleza y la condición social de la modelo. Un artista en boga en la época, como por ejemplo Giovanni Boldini, daba a sus modelos un aspecto lo más semejante y halagador posible. Lautrec, en cambio, más lúcido, como Van Gogh, fue a lo esencial gracias a una manera descriptiva sobria y directa, escapando a la tentación de “embellecer”. El hecho de disponer de una renta familiar le permitía eludir las obligaciones de los retratos “alimentarios” –los realizados para vivir todo el mes– y seguir solamente su fantasía; son raros, de hecho, los retratos realizados por el artista en razón de algún encargo célebre (por ejemplo, Madame de Gortzikoff ) (7). La obra de Buenos Aires debe relacionarse con un retrato de asunto análogo, conservado en la Ny Carlsberg Glyptotek de Copenhague (1886-1887). La modelo posó además para la famosa tela La buveuse o La gueule de bois (1889, Harvard Fogg Art Museum, Cambridge; el dibujo, de 1887-1888, se conserva en el Musée Toulouse-Lautrec, Albi), escena social de depravación y miseria (8).\r\n Barbara Musetti\r\n1— Sobre la vida de Lautrec, cf. U. Felbinger, Henri de Toulouse-Lautrec: sa vie et son oeuvre. Köln, Könemann, 2000; A. Simon, Toulouse-Lautrec: biographie. Tournai, La Renaissance du Livre, 1998. 2— Sobre la influencia de Montmartre en la obra de Lautrec, cf. R. Thomson; P. D. Cate y M. Weaver Chapin (dir.), Toulouse-Lautrec and Montmartre, cat. exp. Washington, National Gallery of Art, 2005; F. Maubert, Le Paris de Lautrec. Paris, Assouline, 2004; P. Vabanne, Henri de Toulouse-Lautrec: le peintre de la vie moderne. Paris, Terrail, 2003. 3— Cf. T. Diamand Rosinsk, Suzanne Valadon. Paris, Flammarion, 2005. 4— El retrato femenino figura entre los temas más recurrentes de la obra de Lautrec. Sobre este asunto, véase: Le donne di Toulouse-Lautrec, cat. exp. Milano, Mazzotta, 2001. 5— Degas, que fue mentor y amigo de Valadon y uno de los primeros en adquirir sus dibujos, los calificó de “malos y blandos” y definió a la artista como “la terrible Marie”. 6— Sobre la actividad artística de Valadon, madre del pintor Maurice Utrillo, véase: M. Restellini (dir.), Valadon-Utrillo: au tournant du siècle à Montmartre: de l’impressionnisme à l’École de Paris, cat. exp. Paris, Pinacothèque de Paris, 2009; Alexandra Charvier et al., Utrillo, Valadon, Utter: 12 rue Cortot: un atelier, trois artistes. Sannois, Musée Utrillo-Valadon, 2008. 7— Toulouse Lautrec, cat. exp. Paris, Réunion des musées nationaux, 1992, p. 133.8— Para un recorrido por toda la obra de Toulouse-Lautrec, cf. M. G. Dortu, Toulouse-Lautrec et son oeuvre. New York, Collectors Editions, 1971, vol. 1-4.	\N	762	\N	\N	1885	\N	135	2025-05-19 12:29:43.192411-03	2025-10-29 08:53:37.590357-03	100	En 1885, con apenas veintiún años, en busca de independencia y deseoso de escapar del severo control paterno, Henri de Toulouse-Lautrec (1) abandonó el sur de Francia para ir a París. Se estableció en el barrio de Montmartre (2) con su amigo René Grenier. La integración en la vida de la butte no fue simple; contribuyeron a su inserción sus amigos, entre ellos François Gauzi, compañero de estudios en el atelier de Ferdinand Cormon.En estrecho contacto con Van Gogh, Vallotton, Bonnard, Lautrec participó plenamente del clima artístico pa	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
221	La Coiffure (El peinado)La berge de La Seine (Orillas del Sena)	la-coiffure-el-peinado-la-berge-de-la-seine-orillas-del-sena	La Coiffure (El peinado)La berge de La Seine (Orillas del Sena)	\N	\N	126		\N	60 x 73 cm. Con marco: 84 x 97 cm.	\N		\N	316	\N	\N	1891	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100		\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
448	Sendas perdidas	sendas-perdidas	Sendas perdidas	\N	\N	126		\N	Carbonilla y temple sobre tela. 135 x 165 cm.	\N	Como lo han hecho artistas de todas las épocas, Gárgano nos propone su propia versión del infierno, construido pacientemente sobre el papel a lo largo de muchos años. \r\nSus trabajos no se alimentan directamente de la realidad: crean una realidad propia, que se nutre de otras imágenes. Cada obra está hecha de otras obras, y por eso mismo conforma un mundo.	\N	463	\N	\N	2015	\N	135	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	\N	\N	137	f	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
328	Toulouse Lautrec - En observation - M.Fabre, Officier de reserve	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve	Toulouse Lautrec - En observation - M.Fabre, Officier de reserve	\N	\N	\N	\N	\N	\N	\N	\N	\N	329	\N	\N	0	\N	135	2025-05-19 14:07:30.084163-03	2025-05-19 14:07:30.084163-03	100	\N	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
311	Quiosco de Canaletas	quiosco-de-canaletas	Quiosco de Canaletas	\N	\N	\N	\N	\N	Técnica: Acuarela, gouache y grafito sobre papel\r\n47,5 x 62 cm\r\nNro. de inventario: 2001.13\r\nDonación: Eduardo F. Costantini, Buenos Aires	\N	Balcanización social y emergencia de la individualidad, vivencia íntima de la vida cotidiana, contingencia y temporalidad son datos del ambiente cultural al que fue sensible Barradas durante su segunda estadía en Barcelona (1916-1918), un ambiente que compartió con su coterráneo Joaquín Torres García y, en parte, también con el pintor ecléctico español Celso Lagar. Fue precisamente a través de este último como conoció a Torres García en agosto de 1917. Lagar venía de una larga estadía en París, donde había tenido contacto con el cubismo de Picasso y de Juan Gris y también con el orfismo de los Delaunay. Este dato no es menor, dado que, en su relación con Barradas, éste debió de ser receptivo a ciertas ideas de Lagar, y especialmente al uso de las formas curvas que heredaba de la estética orfista. La amistad llegó al punto de que el pintor uruguayo, arribado a la capital española en agosto de 1918, le organizó una exposición a Lagar en el Ateneo de Madrid en noviembre de ese año.\r\n\r\nEn una conferencia sobre Barradas que Torres García brindó en Montevideo en 1936, decía:\r\n\r\nTodos saben la pasión de Barradas por el café, […] tenía su mesa favorita que se podía reconocer fácilmente por estar llena de esos dibujos con que siempre acompañaba sus peroraciones, mesa junto a seis cristales que daban a la plaza y desde la cual se veía todo el vibracionismo callejero.1\r\n\r\nTorres García utilizó aquí el término vibracionismo, que fue con el que Barradas bautizó su obra temprana barcelonesa (1917-1918). Su primera enunciación pública posiblemente haya sido en la revista Arc Voltaic, dirigida por Joan Salvat Papasseit, aparecida en febrero de 1918. En ella Barradas publicó un dibujo (Bonanitingui) hecho en 1917, que llevaba como leyenda “Dibuix Vibracionista”, y el término se repetía en la carátula de la revista: “Vibracionisme de idees; Poemes en ondes Hertzianes”.	\N	309	\N	\N	1918	\N	135	2025-05-19 13:07:58.324319-03	2025-09-21 19:53:04.104268-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
219	La berge de La Seine (Orillas del Sena)	la-berge-de-la-seine-orillas-del-sena	La berge de La Seine (Orillas del Sena)	\N	\N	126		\N	60 x 73 cm. Con marco: 84 x 97 cm.	\N	En la sesión del 17 de noviembre de 1910 los miembros del Concejo Deliberante de la ciudad de Buenos Aires habían votado  un proyecto que destacaba el rol de la Municipalidad para promover la cultura artística,  una de las vías podía ser la donación de obras de arte. Esta resolución, benefició  al MNBA que contó con presupuesto estatal para incrementar su patrimonio  con adquisiciones de obras que concretó la Comisión Nacional de Bellas Artes en la “Exposición Internacional de Arte del Centenario Buenos Aires 1910”.	\N	316	\N	\N	1891	\N	135	2025-05-19 12:29:43.192411-03	2025-10-06 17:49:29.06594-03	100	En la sesión del 17 de noviembre de 1910 los miembros del Concejo Deliberante de la ciudad de Buenos Aires habían votado  un proyecto que destacaba el rol de la Municipalidad para promover la cultura artística,  una de las vías podía ser la donación de obras de arte. Esta resolución, benefició  al MNBA que contó con presupuesto estatal para incrementar su patrimonio  con adquisiciones de obras que concretó la Comisión Nacional de Bellas Artes en la “Exposición Internacional de Arte del Centenario Buenos Aires 1910”.	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
484	Procesión sorprendida por la lluvia	procesi-n-sorprendida-por-la-lluvia	Procesión sorprendida por la lluvia	\N	\N	126		\N	Oleo sobre tela 63 x 102 cm. - Marco: 101,5 x 140 cm.	\N	Esta obra constituye un ejemplo sobresaliente del costumbrismo practicado por Mariano Fortuny, de gran influencia en la pintura española del último tercio del siglo XIX, aunque en este caso se aleja del tratamiento preciosista con el que alcanzó éxito y fama internacionales, al punto de haber sido considerada un boceto por la falta de atención al detalle .	\N	491	\N	\N	2017	\N	135	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100	\N	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
278	El despertar de la criada	el-despertar-de-la-criada	El despertar de la criada	\N	\N	126		\N		\N	Le lever de la bonne es un desnudo naturalista. Aun cuando el título y algunos elementos de la composición lo connotan, la pintura pertenece al género que a lo largo del siglo XIX fue campo de batalla de las audacias modernistas. No hay narratividad en la escena, se limita a presentar el cuerpo de una muchacha joven en el que se lee su pertenencia a la clase trabajadora. La sencillez del mobiliario, las ropas amontonadas sobre un banco de paja, al pie de la cama sin tender y, sobre todo, el título del cuadro, indican que se trata de una criada. Un foco de luz dirigida desde la izquierda ilumina ese cuerpo que se destaca con intensidad dramática sobre el fondo neutro de la pared de fondo. La piel de la muchacha es oscura, sobre todo en las zonas que el cuerpo de una mujer de trabajo se veía expuesto al sol: las manos, el rostro y las piernas. La criada aparece ensimismada en la tarea de dar vuelta una media para calzarla, de modo que el contraste entre los pechos y la mano castigada por la intemperie se hace más evidente. Cruzadas una sobre la otra, las piernas, gruesas y musculosas se destacan con un tratamiento naturalista que se detiene en la representación minuciosa de unos pies toscos y maltratados. El pubis, invisible tras la pierna cruzada, se ubica en el centro exacto de la composición. Ninguno de estos detalles pasó inadvertido a los críticos que, tanto en París como en Buenos Aires, comentaron el cuadro en 1887.\r\n\r\nFue pintado en París por Eduardo Sívori quien, tras haber logrado su aceptación en el Salón anual, lo envió a Buenos Aires ese mismo año sabiendo de antemano que su exhibición despertaría polémicas. Fue el primer gesto vanguardista en la historia del arte argentino.\r\n\r\nEn 1887 la pintura naturalista ocupaba un lugar destacado en el Salón de París, como una de las vías de renovación de la estética oficial de la Academia. Sin alejarse demasiado de las convenciones formales impuestas por la tradición (claroscuro, perspectiva, tratamiento de la superficie) los pintores naturalistas siguieron una línea de renovación iconográfica abierta a mediados de siglo por Gustave Courbet y Jean-François Millet, introduciendo temas derivados de la literatura de Émile Zola, o que planteaban una denuncia directa de los conflictos sociales contemporáneos, en un tono en general narrativo y melodramático. No fue el desnudo un género frecuente en la pintura naturalista. El cuadro de Sívori fue enseguida interpretado por la crítica francesa (Roger- Milès, Emery, E. Benjamin, Paul Gilbert, entre ellos) como obra derivada de Zola, un poco “excesivo” en la representación de un cuerpo que fue visto como feo, sucio y desagradable.\r\nEn Buenos Aires, donde no había habido hasta entonces más que pocas y discutidas exhibiciones de desnudos artísticos, el cuadro fue objeto no solo de una intensa polémica en la prensa (fue calificado de “indecente” y “pornográfico”) sino también de un importante alineamiento de intelectuales y artistas en su favor. En una reunión de su Comisión Directiva, el 22 de agosto de 1887, la Sociedad Estímulo de Bellas Artes decidió exhibir el cuadro en su local, cursar invitaciones especiales a los socios y a los periodistas de la capital, y abrir un álbum que recogiera las firmas de todos aquellos “que quieran manifestar al autor sus felicitaciones por los progresos realizados”. Más de 250 firmas de artistas, escritores, etc. se estamparon en ese álbum en cuyas páginas Sívori guardó además los recortes de las críticas recibidas y fotografías de ese y otros cuadros suyos que habían sido expuestos en el Salón de París hasta su regreso definitivo en 1891.\r\n\r\nLa fotografía de Le lever de la bonne conservada en ese álbum presenta algunas diferencias con el cuadro definitivo. No sabemos si las modificaciones fueron hechas antes o después de ser exhibido en el Salón de París. En la mesilla de noche puede verse una palangana y una jarra (elementos de higiene) en lugar del candelabro con una vela apagada de la versión final. Por otra parte, en la pared del fondo se vislumbra un estante con frascos y potes de tocador. Todos estos elementos pueden verse a simple vista cuando el cuadro se mira con una luz potente, como si el artista hubiera decidido dejar que aquellos arrepentimientos se adivinen en el fondo en penumbras. Pero lo más significativo es el cambio en la fisonomía de la criada. Su rostro y su peinado aparecen en la fotografía menos oscuros. La criada parece una faubourgienne en la versión de la fotografía. Tal vez más cercana a la apariencia de una prostituta (los elementos de higiene también contribuyen a ello), tema predilecto de la vanguardia y de la crítica social de la época. Aun modificada, la criada fue interpretada como prostituta y considerada pornográfica por varios de sus primeros comentadores. Su transformación es significativa. Tal vez el artista decidió alejarse del “tema” social de moda al presentarse al Salón. Tal vez decidió transformarla inequívocamente en una criada pobre para su exhibición en Buenos Aires.	\N	302	\N	\N	1887	\N	135	2025-05-19 12:30:21.186648-03	2025-10-07 17:09:05.457055-03	100	Le lever de la bonne es un desnudo naturalista. Aun cuando el título y algunos elementos de la composición lo connotan, la pintura pertenece al género que a lo largo del siglo XIX fue campo de batalla de las audacias modernistas. No hay narratividad en la escena, se limita a presentar el cuerpo de una muchacha joven en el que se lee su pertenencia a la clase trabajadora. La sencillez del mobiliario, las ropas amontonadas sobre un banco de paja, al pie de la cama sin tender y, sobre todo, el título del cuadro, indican que se trata de u	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
270	La vuelta del malón	la-vuelta-del-mal-n	La vuelta del malón	\N	\N	126	Adqusición Sociedad de Estímulo	\N	Oleo Sobre tela 186,5 x 292 cm.	\N	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. \r\n\r\nPintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtuvo una medalla (de única clase) y al regreso se exhibió nuevamente en Buenos Aires, en el segundo Salón del Ateneo en 1894. \r\n\r\nFue la obra más celebrada de Della Valle. Presentaba por primera vez en las grandes dimensiones de una pintura de salón una escena que había sido un tópico central de la conquista y de la larga guerra de fronteras con las poblaciones indígenas de la pampa a lo largo del siglo XIX: el saqueo de los pueblos fronterizos, el robo de ganado, la violencia y el rapto de cautivas. \r\n\r\nEn el manejo de la luz y la pincelada se advierte la huella de la formación de Della Valle en Florencia: no solo el aprendizaje con Antonio Ciseri sino también el conocimiento de los macchiaioli y los pintores del Risorgimento italiano. Algunos críticos lo vincularon con los grandes cuadros de historia del español Ulpiano Checa que se había hecho famoso por sus entradas de bárbaros en escenas de la historia de España y del imperio romano. Sin embargo, el cuadro de Della Valle entroncaba con una larga tradición no solo en las crónicas y relatos literarios inspirados en malones y cautivas, sino también en imágenes que, desde los primeros viajeros románticos europeos que recorrieron la región en la primera mitad del siglo XIX, representaron cautivas y malones. \\n En la década de 1870 Juan Manuel Blanes había realizado también algunas escenas de malones que aparecen como antecedentes de esta obra. Casi ninguna, sin embargo, había sido expuesta al público ya que tuvieron una circulación bastante restringida. La vuelta del malón fue, entonces, la primera imagen que impactó al público de Buenos Aires referida a una cuestión de fuerte valor emotivo e inequívoco significado político e ideológico. Según refiere Julio Botet, a partir de una entrevista al artista en agosto de 1892, el asunto del cuadro se inspiraba en un malón llevado por el cacique Cayutril y el capitanejo Caimán a una población no mencionada. Otro comentario (en el diario Sud-América) ubicaba el episodio en la población de 25 de Mayo. Pero más allá de la anécdota el cuadro aparece como una síntesis de los tópicos que circularon como justificación de la “campaña del desierto” de Julio A. Roca en 1879, produciendo una inversión simbólica de los términos de la conquista y el despojo. El cuadro aparece no solo como una glorificación de la figura de Roca sino que, en relación con la celebración de 1492, plantea implícitamente la campaña de exterminio como culminación de la conquista de América. Todos los elementos de la composición responden a esta idea, desplegados con nitidez y precisión significativa. La escena se desarrolla en un amanecer en el que una tormenta comienza a despejarse. El malón aparece equiparado a las fuerzas de la naturaleza desencadenadas (otro tópico de la literatura de frontera). \\nLos jinetes llevan cálices, incensarios y otros elementos de culto que indican que han saqueado una iglesia. Los indios aparecen, así, imbuidos de una connotación impía y demoníaca. El cielo ocupa más de la mitad de la composición, dividida por una línea de horizonte apenas interrumpida por las cabezas de los guerreros y sus lanzas. En la oscuridad de ese cielo se destaca luminosa la cruz que lleva uno de ellos y la larga lanza que empuña otro, como símbolos contrapuestos de civilización y barbarie. En la montura de dos de los jinetes se ven cabezas cortadas, en alusión a la crueldad del malón. En el extremo izquierdo se destaca del grupo un jinete que lleva una cautiva blanca semidesvanecida, apoyada sobre el hombro del raptor que se inclina sobre ella. Fue este el fragmento más comentado de la obra, a veces en tono de broma, aludiendo a su connotación erótica, o bien criticando cierta inadecuación del aspecto (demasiado “civilizado” y urbano) de la mujer y de su pose con el resto de la composición. La vuelta del malón fue llevada a la Exposición Colombina de Chicago por el oftalmólogo Pedro Lagleyze, amigo del artista, en medio de la desorganización y dificultades que rodearon ese envío oficial. \\nFue exhibida en el pabellón de manufacturas, como parte del envío argentino, junto a bolsas de cereales, lanas, cueros, etc. Los pocos comentarios que recibió se refirieron a la escena representada como una imagen de las dificultades que la Argentina había logrado superar para convertirse en una exitosa nación agroexportadora. Ángel Della Valle pintó una versión reducida de La vuelta del malón para obsequiar a Lagleyze al regreso. Conocida como “malón chico” ha sido con frecuencia tomada por un boceto. También pintó más tarde algunos fragmentos aislados de su gran tela: el grupo del guerrero y la cautiva y el indio que enarbola la cruz. Della Valle había comenzado a pintar cuadros de tema pampeano durante su estadía en Florencia. \\nEn 1887 envió a Buenos Aires varias obras, entre las que pudo verse un indio a caballo (En la pampa) y La banda lisa, que aparecen como tempranas aproximaciones al tema de La vuelta del malón. La pintura fue solicitada por el director del MNBA, Eduardo Schiaffino, a la familia del artista tras su muerte en 1903; esta optó por donarla a la Sociedad Estímulo de Bellas Artes con el cargo de su venta al MNBA a fin de instituir un premio anual de pintura denominado “Ángel Della Valle”.	\N	301	\N	\N	1892	\N	135	2025-05-19 12:30:21.186648-03	2025-10-06 17:36:54.590141-03	100	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. Pintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtu	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
197	Le bain de Vénus (El baño de Venus)	le-bain-de-v-nus-el-ba-o-de-venus	Le bain de Vénus (El baño de Venus)	\N	\N	126		\N	Oleo Sobre tabla 35 x 28 cm.	\N	El título original de esta pintura puede haber sido puesto por Redon, ya que al parecer la obra fue comprada al artista sin intermediarios. Los toques de azul y verde en la zona inferior derecha de la tela pueden considerarse una representación del agua, y el tocado de la figura puede evocar un personaje mitológico.  \r\n\r\nEstos aspectos, sin embargo, son los únicos que justificarían el título de El baño de Venus. La figura no está desnuda, aparece (apenas) vestida y carece de otro atributo inherente a la diosa del amor. Su movimiento no corresponde a una escena de baño y el personaje demuestra una vacilación indigna de alguien nacido del mar. Con los dos brazos extendidos hacia delante y la cabeza hacia atrás, parece moverse de derecha a izquierda como si estuviese huyendo de algo –en una posición relativamente similar a la del dibujo Female Nude (Bridgestone Museum, Tokio)–. \r\n\r\nEl origen de su miedo o resistencia parece ser la forma circular sobre la derecha (de la mitad del tamaño de la figura) que semeja más a un insecto o a uno de los monstruos submarinos pintados por Redon que a las flores y las hojas reconocibles sobre la izquierda. Esta interpretación se sustenta en el uso del negro para delinear y rellenar parte de esta forma, color asociado por Redon al mundo fantástico de sus primeras carbonillas y litografías.	\N	639	\N	\N	1908	\N	135	2025-05-19 12:29:38.885286-03	2025-10-03 20:41:13.926535-03	100	El título original de esta pintura puede haber sido puesto por Redon, ya que al parecer la obra fue comprada al artista sin intermediarios. Los toques de azul y verde en la zona inferior derecha de la tela pueden considerarse una representación del agua, y el tocado de la figura puede evocar un personaje mitológico. Estos aspectos, sin embargo, son los únicos que justificarían el título de El baño de Venus. La figura no está desnuda, aparece (apenas) vestida y carece de otro atributo inherente a la diosa del amor. Su movimiento no cor	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
276	La vuelta al hogar	la-vuelta-al-hogar	La vuelta al hogar	\N	\N	126	Adqusición Sociedad de Estímulo	\N	Oleo Sobre tela 90,5 x 118,5 cm	\N	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. Pintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtuvo una medalla (de única clase) y al regreso se exhibió nuevamente en Buenos Aires, en el segundo Salón del Ateneo en 1894. \\nFue la obra más celebrada de Della Valle. Presentaba por primera vez en las grandes dimensiones de una pintura de salón una escena que había sido un tópico central de la conquista y de la larga guerra de fronteras con las poblaciones indígenas de la pampa a lo largo del siglo XIX: el saqueo de los pueblos fronterizos, el robo de ganado, la violencia y el rapto de cautivas. \r\n\r\nEn el manejo de la luz y la pincelada se advierte la huella de la formación de Della Valle en Florencia: no solo el aprendizaje con Antonio Ciseri sino también el conocimiento de los macchiaioli y los pintores del Risorgimento italiano. Algunos críticos lo vincularon con los grandes cuadros de historia del español Ulpiano Checa que se había hecho famoso por sus entradas de bárbaros en escenas de la historia de España y del imperio romano. Sin embargo, el cuadro de Della Valle entroncaba con una larga tradición no solo en las crónicas y relatos literarios inspirados en malones y cautivas, sino también en imágenes que, desde los primeros viajeros románticos europeos que recorrieron la región en la primera mitad del siglo XIX, representaron cautivas y malones. \\n En la década de 1870 Juan Manuel Blanes había realizado también algunas escenas de malones que aparecen como antecedentes de esta obra. Casi ninguna, sin embargo, había sido expuesta al público ya que tuvieron una circulación bastante restringida. La vuelta del malón fue, entonces, la primera imagen que impactó al público de Buenos Aires referida a una cuestión de fuerte valor emotivo e inequívoco significado político e ideológico. Según refiere Julio Botet, a partir de una entrevista al artista en agosto de 1892, el asunto del cuadro se inspiraba en un malón llevado por el cacique Cayutril y el capitanejo Caimán a una población no mencionada. Otro comentario (en el diario Sud-América) ubicaba el episodio en la población de 25 de Mayo. Pero más allá de la anécdota el cuadro aparece como una síntesis de los tópicos que circularon como justificación de la “campaña del desierto” de Julio A. Roca en 1879, produciendo una inversión simbólica de los términos de la conquista y el despojo. \r\n\r\nEl cuadro aparece no solo como una glorificación de la figura de Roca sino que, en relación con la celebración de 1492, plantea implícitamente la campaña de exterminio como culminación de la conquista de América. Todos los elementos de la composición responden a esta idea, desplegados con nitidez y precisión significativa. La escena se desarrolla en un amanecer en el que una tormenta comienza a despejarse. El malón aparece equiparado a las fuerzas de la naturaleza desencadenadas (otro tópico de la literatura de frontera). \\nLos jinetes llevan cálices, incensarios y otros elementos de culto que indican que han saqueado una iglesia. Los indios aparecen, así, imbuidos de una connotación impía y demoníaca. El cielo ocupa más de la mitad de la composición, dividida por una línea de horizonte apenas interrumpida por las cabezas de los guerreros y sus lanzas. En la oscuridad de ese cielo se destaca luminosa la cruz que lleva uno de ellos y la larga lanza que empuña otro, como símbolos contrapuestos de civilización y barbarie. En la montura de dos de los jinetes se ven cabezas cortadas, en alusión a la crueldad del malón. En el extremo izquierdo se destaca del grupo un jinete que lleva una cautiva blanca semidesvanecida, apoyada sobre el hombro del raptor que se inclina sobre ella. Fue este el fragmento más comentado de la obra, a veces en tono de broma, aludiendo a su connotación erótica, o bien criticando cierta inadecuación del aspecto (demasiado “civilizado” y urbano) de la mujer y de su pose con el resto de la composición. La vuelta del malón fue llevada a la Exposición Colombina de Chicago por el oftalmólogo Pedro Lagleyze, amigo del artista, en medio de la desorganización y dificultades que rodearon ese envío oficial. \\nFue exhibida en el pabellón de manufacturas, como parte del envío argentino, junto a bolsas de cereales, lanas, cueros, etc. Los pocos comentarios que recibió se refirieron a la escena representada como una imagen de las dificultades que la Argentina había logrado superar para convertirse en una exitosa nación agroexportadora. Ángel Della Valle pintó una versión reducida de La vuelta del malón para obsequiar a Lagleyze al regreso. Conocida como “malón chico” ha sido con frecuencia tomada por un boceto. También pintó más tarde algunos fragmentos aislados de su gran tela: el grupo del guerrero y la cautiva y el indio que enarbola la cruz. \r\n\r\nDella Valle había comenzado a pintar cuadros de tema pampeano durante su estadía en Florencia. \\nEn 1887 envió a Buenos Aires varias obras, entre las que pudo verse un indio a caballo (En la pampa) y La banda lisa, que aparecen como tempranas aproximaciones al tema de La vuelta del malón. La pintura fue solicitada por el director del MNBA, Eduardo Schiaffino, a la familia del artista tras su muerte en 1903; esta optó por donarla a la Sociedad Estímulo de Bellas Artes con el cargo de su venta al MNBA a fin de instituir un premio anual de pintura denominado “Ángel Della Valle”.	\N	304	\N	\N	1885	\N	135	2025-05-19 12:30:21.186648-03	2025-11-02 16:33:09.316234-03	100	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. Pintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtu	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
592	O Impossivel	new	\N	\N	\N	\N	\N	\N	écnica: Yeso\r\n182 × 175 × 91 cm\r\nNro. de inventario: 1998.03\r\nDonación: Eduardo F. Costantini, 2001	\N	En 1946, en su quinta exposición individual, realizada en la Valentine Gallery, en Nueva York, Maria Martins presentó una primera versión, en yeso, de la escultura O impossível, con largos brazos y de un tamaño menor que la del acervo de Malba.  Se trataba, muy probablemente, del molde usado en la fundición en bronce de la pieza que se ve en una fotografía de taller publicada en el catálogo de su primera exhibición en París, en 1948, Les Statues magiques de Maria, que incluía también otra imagen de taller con una gran versión en yeso, que hoy pertenece a Malba, al lado de However!! (1947), lo que nos hace suponer que la obra aquí presentada fue producida entre 1947 y 1948. \r\n\r\nLa muestra de París, que repitió muchas de las obras expuestas el año anterior en la galería neoyorquina Julien Levy, es la primera que registra más de un O impossível entre los trabajos exhibidos. \r\n\r\nEn la edición especial del catálogo preparado para la muestra de 1946, Maria había incluido nueve aguafuertes, cuatro de los cuales eran páginas del poema titulado Explication, en cuyo comienzo se lee: \r\n\r\n“Sé que mis Diosas y sé que mis Monstruos \r\nsiempre te parecerán sensuales y bárbaros”.	\N	594	\N	\N	1945	\N	\N	2025-09-19 20:05:51.441802-03	2025-09-19 20:11:12.295819-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
195	Morro da favela II (Pueblito)	morro-da-favela-ii-pueblito	Morro da favela II (Pueblito)	\N	\N	126		\N	Oleo Sobre tela 39,5 x 49,9 cm.	\N	Época de temática popular, la década del 40 también sería para Tarsila, la gran pintora modernista de los años 20, tiempo de volver definitivamente de sus tonos pau Brasil, es decir, retornar a los colores llamados caipiras (azul y rosa) de la decoración popular de los modestos caseríos de las regiones del interior del Brasil. Este colorido, aunque más intenso, estuvo presente en sus telas de los años 20, su período de máxima creatividad y producción, etapa de una gran síntesis de sus elementos compositivos. Después de 1930, cuando Tarsila se orientó, por razones culturales y políticas, hacia la temática social, su paleta se alteró igualmente.\\nPor esa época la artista regresó a San Pablo después de alternar varios años entre esa ciudad y Río de Janeiro, y pasó gran parte de su tiempo en su hacienda de Indaiatuba, en el interior paulista. \\nConociendo la inexistencia de un mercado de arte local, realizó encargos de pinturas, ilustraciones y colaboró con crónicas regulares sobre la vida cultural y memorias en Diário de São Paulo.\\nEn Pueblito, de 1945, encontramos una segunda versión de la famosa pintura Morro da favela (1924, colección João Estefano, San Pablo), tela tan admirada por el poeta Blaise Cendrars que le recomendó, en su momento, que preparase su primera exposición individual en París a partir de la serie que se iniciara con esta obra. La temática está inspirada en el viaje a Río con los modernistas de San Pablo acompañando a Cendrars en su primera visita al Brasil.\\nAlgunas variantes, sin embargo, surgen en esta tela, ahora en la colección del MNBA: sus dimensiones son menores que las de Morro da favela, la vegetación tiene modificaciones, dejando de ser visualmente presentada en formas sintéticas, en esta tela de 1945 aparecen como matas de plantas más desmenuzadas, más diversificadas en su ubicación, especialmente en el primer plano. Por otra parte, las pinceladas son bien evidentes, casi “impresionistas” en su aplicación sobre la tela, y el ave extraña de 1924 es reemplazada por la confrontación de dos pavos. Finalmente, el color es más suave en comparación con la pintura de veinte años antes.\\nLa obra del MNBA estuvo expuesta en Buenos Aires, La Plata y Montevideo en la exposición Veinte artistas brasileños presentada en 1945, con la organización del escritor Marques Rebelo y la intermediación efectiva de Emilio Pettoruti.\\nEn esta muestra Tarsila estuvo representada por otra pintura, Pueblito II (1944), y por un dibujo, estudio de 16 x 22 cm de la pintura de la colección del MNBA, hoy aparentemente perdido. La exposición dio lugar a dos pequeños libros, actualmente antológicos, uno de autoría de Cipriano Vitureira, de Montevideo, y el segundo de Jorge Romero Brest, La pintura brasileña contemporánea, publicado el mismo año de la exposición por la editorial Poseidón.	\N	\N	\N	\N	1945	\N	135	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	Época de temática popular, la década del 40 también sería para Tarsila, la gran pintora modernista de los años 20, tiempo de volver definitivamente de sus tonos pau Brasil, es decir, retornar a los colores llamados caipiras (azul y rosa) de la decoración popular de los modestos caseríos de las regiones del interior del Brasil. Este colorido, aunque más intenso, estuvo presente en sus telas de los años 20, su período de máxima creatividad y producción, etapa de una gran síntesis de sus elementos compositivos. Después de 1930, cuando Ta	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
213	En observation - M.Fabre, Officier de reserve	en-observation-m-fabre-officier-de-reserve	En observation - M.Fabre, Officier de reserve	\N	\N	126		\N	Oleo Sobre tela: 60,5 x 49,5 cm. - Marco: 76,2 x 66,2 cm.	\N		\N	329	\N	\N	1891	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100		\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
575	Abaporu	new	\N	\N	\N	\N	\N	\N	Técnica: Óleo sobre tela\r\n85,3 x 73 cm\r\nNro. de inventario: 2003.33\r\nDonación: Eduardo F. Costantini, 2001	\N	Al reflexionar sobre Abaporu, Tarsila aventuró una hipótesis sobre su origen:\r\n\r\n&amp;amp;amp;amp;amp;quot;Recordé que, cuando era pequeña y vivía en una hacienda enorme […] las criadas me llevaban –con un grupo de niñas– a un cuarto viejo y amenazaban: “¡Vamos a mostrarles una cosa increíble! ¡Una aparición! ¡De aquel agujero va a caer un fantasma! ¡Va a caer un brazo, una pierna!”, y nosotras quedábamos horrorizadas. Todo eso quedó en mi psiquis, tal vez en el subconsciente.&amp;amp;amp;amp;amp;quot;\r\n\r\nAunque Tarsila atribuyese la matriz de ese ser fantástico a las historias que oía en su infancia, es forzoso recordar que, durante la década de 1920, la artista estuvo varias veces en la capital francesa, atenta a las manifestaciones de vanguardia: Aragon, Arp, Artaud, Brancusi, Breton, Cendrars y Rousseau, ente otros, formaban parte de su repertorio. Probablemente La création du monde, presentado en 1923 por los Ballets Suecos, con música de Milhaud, escenografía de Léger y guión de Cendrars, de tema primitivo y estética innovadora, haya motivado a Tarsila a pintar A negra (MAC-USP), figura antropofágica avant la lettre, que antecede a Abaporuen cinco años.	\N	568	\N	\N	1928	\N	\N	2025-09-18 15:47:56.92008-03	2025-09-19 20:04:55.580193-03	100	\N	\N	342	t	https://g.co/arts/zVtPRdJTYMdj2cxy9	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
274	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	Sin pan y sin trabajo	\N	\N	126		\N	Oleo Sobre tela 125,5 x 216 cm.	\N	Sin pan y sin trabajo es el primer cuadro de tema obrero con intención de crítica social en el arte argentino. Desde el momento de su exhibición ha sido una pieza emblemática del arte nacional: comentado, reproducido, citado y reapropiado por sucesivas generaciones de artistas, historiadores y críticos hasta la actualidad. Fue pintado por Ernesto de la Cárcova en Buenos Aires al regreso de su viaje de estudios en Turín y Roma, donde había comenzado su ejecución antes de partir. Allí dejó al menos un boceto en obsequio a Pío Collivadino, el artista argentino que a su llegada ocupó el taller que De la Cárcova dejaba en la Via del Corso 12. \\n Había comenzado su formación europea en la Real Academia de Turín, donde fue admitido con una obra (Crisantemos) en la exposición de 1890. Luego había pasado a Roma, donde continuó su formación en los talleres de Antonio Mancini y Giacomo Grosso. Una obra suya (Cabeza de viejo) fue premiada con medalla de plata y adquirida en 1892 para la Galería Real de Turín; también obtuvo una medalla de oro en Milán en 1893 (1). Estos antecedentes hicieron que a su regreso, a los 28 años, fuera miembro del jurado del Ateneo, de modo que Sin pan y sin trabajo, celebrado como el gran acontecimiento artístico del Salón, quedó fuera de concurso. \\n El cuadro responde a un estilo naturalista y a una temática que tuvieron una importante presencia en los salones europeos de los años finales del siglo XIX: grandes pinturas resueltas en tonos sombríos que desplegaban escenas dramáticas de miseria y de los contemporáneos conflictos sociales urbanos. El espíritu crítico que sin duda alimentó aquellas composiciones naturalistas finiseculares se diluyó en los cuadros de salón, en el interés por figurar en grandes competencias con posiciones enfrentadas al arte académico más conservador. Sin embargo, Sin pan y sin trabajo no fue pintado para competir en un salón europeo: fue la obra con la que De la Cárcova se presentó al regreso en el segundo Salón del Ateneo en Buenos Aires, tras haberse afiliado al recién creado Centro Obrero Socialista (antecedente inmediato del Partido Socialista, fundado dos años después). \\n No había en Buenos Aires una tradición académica sino que el grupo de artistas del Ateneo procuraba dar sus primeros pasos. Por otra parte, a partir de la crisis de 1890, la inmensa afluencia de inmigrantes europeos que llegaban de Europa en busca de trabajo en Buenos Aires comenzaba a percibirse en forma conflictiva.	\N	306	\N	\N	1894	\N	135	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	Sin pan y sin trabajo es el primer cuadro de tema obrero con intención de crítica social en el arte argentino. Desde el momento de su exhibición ha sido una pieza emblemática del arte nacional: comentado, reproducido, citado y reapropiado por sucesivas generaciones de artistas, historiadores y críticos hasta la actualidad. Fue pintado por Ernesto de la Cárcova en Buenos Aires al regreso de su viaje de estudios en Turín y Roma, donde había comenzado su ejecución antes de partir. Allí dejó al menos un boceto en obsequio a Pío Collivadin	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
582	The Disasters of Mysticism	new	\N	\N	\N	\N	\N	\N	Técnica: Óleo sobre tela\r\n97.5 × 130,5 cm\r\nNro. de inventario: 2003.37\r\nDonación: Eduardo F. Costantini	\N	The Disasters of Mysticism es una obra realizada por Matta en 1942 en Nueva York, ciudad en la que residía desde 1939. Allí formó parte del nutrido grupo de artistas que habían huido de la guerra en Europa. Entre todos, se destacaba la presencia de los surrealistas, no solo porque allí estaba la plana mayor del movimiento (André Breton, Max Ernst, Marcel Duchamp, Yves Tanguy, Man Ray, André Masson y otros), sino porque el surrealismo era el movimiento más pujante y dinámico de las vanguardias europeas en el momento de comenzar la guerra.\r\n\r\nMatta pinta The Disasters of Mysticism en un año par- ticularmente importante para los surrealistas. En marzo de 1942, en la Pierre Matisse Gallery, se realiza la exposición Artists in Exile.1 En octubre se inaugura la exhibición que haría conocer el surrealismo en Nueva York, First Papers of Surrealism,2 en la Whitelaw Reid Mansion. En ambas está presente Matta. Además, también en marzo de ese año, el artista lleva a cabo una muestra individual, Matta: Paintings and Oil Pencils, en la Pierre Matisse Gallery. The Disasters of Mysticism, además, se ubica en un momento especialmente significativo tanto en la obra pictórica como en los desarrollos intelectuales de Matta. Como pintura, forma parte de un grupo de obras, que pueden situarse entre 1942 y 1944, en las que el espacio se presenta con fondos oscuros y formas luminosas, como fosforescentes.3 Seguimos en el mundo de los paisajes surrealistas inaugurado con las morphologies psychologiques y los inscapes, pero ahora, con esta particularidad cromática, este espacio parece sumergirnos en la inmensidad del espacio cósmico o en las penumbras de los fondos marinos.	\N	583	\N	\N	1942	\N	\N	2025-09-19 10:04:52.094595-03	2025-09-21 19:55:49.419279-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
215	Vahine no te miti (Femme a la mer) (Mujer del mar).	vahine-no-te-miti-femme-a-la-mer-mujer-del-mar	Vahine no te miti (Femme a la mer) (Mujer del mar).	\N	\N	126		\N	Oleo Sobre tela:92,5 x 74 cm.	\N	El MNBA tiene la suerte de poseer dos pinturas de bañistas de Gauguin, ambientada en este caso no en Francia, sino en los mares del Sur. Esta obra aparece mencionada como Estudio de espalda desnuda en una lista de pinturas realizadas durante la primera estadía en Tahití que Gauguin anotó aproximadamente en abril de 1892 en su Carnet de Tahiti (1). Fue pintada en Mataiea y se basa en un dibujo en color del mismo cuaderno de bocetos, probablemente realizado con modelo y con una cuadrícula apenas visible, útil al momento de repetir la composición en mayor escala.Cuando fue expuesta en París en 1893, Thadée Natanson describió concisamente su tema: “de esta, sentada en la arena, solamente se puede ver la espalda bronceada en medio de flores casi simétricas que la espuma borda sobre las olas” (2). La metáfora que vincula la cresta de las olas, las flores y el bordado destaca acertadamente el uso que hace Gauguin de formas polisémicas y la calidad ornamental de este motivo. Las “flores” de espuma también están relacionadas por su forma y su color con la conchilla que está en la playa y con las flores que forman parte del motivo del pareo sobre la rodilla derecha de la mujer. En contraste con otras pinturas de Gauguin que muestran una mujer tahitiana parcialmente vestida, este pareo no tiene ni espesor ni pliegues propios y está pintado sobre la pierna a la manera de los paños en una pintura manierista o de un tatuaje –realmente un “bordado” sobre la piel y la vestimenta original de muchos habitantes de los mares del Sur–. La cresta de las olas similar a una flor ya había sido utilizada por Gauguin en Bretaña (véase, por ejemplo, La playa en Le Pouldu, 1889, colección privada, Buenos Aires) y deriva del arte japonés a través de los grabados de Hiroshige.La descripción que hace Gauguin de la pintura como un Estudio de espalda desnuda confirma que el elemento esencial es el dorso de la figura. Varios autores han señalado que en el libro de 1876 La nouvelle peinture, Edmond Duranty había desafiado a los artistas a presentar figuras de espalda caracterizando su edad, su condición social y su estado psicológico (3). Degas respondió al reto, de manera muy notable sobre todo en sus pasteles de mujeres aseándose, algunas de las cuales están copiadas en una página del Album Briant de Gauguin, 1888-1889 (Museo del Louvre, París). La vista de atrás, con un énfasis en las nalgas, obviamente lo atraía y se encuentra también en la pintura Otahi, 1893 (colección privada). En Vahine no te miti, la posición incómoda de los brazos y las piernas confiere a la espalda un carácter fragmentario con reminiscencias de la escultura antigua, trasladando a esta la expresividad generalmente atribuida al rostro humano.En 1948, Raquel Edelman halló monumentalidad en esta pintura lograda a expensas de la individualidad y la sensualidad, y consideró que la misma probaba la intención de Gauguin de “dominar y sublimar su erotismo” (4). No obstante, la obra se remite a una mujer muy específica, cuya anatomía expresa un carácter –podría decirse incluso una fisonomía–. En lo que al erotismo de Gauguin respecta, es típicamente sugestivo e indirecto. Ronald Pickvance señaló que la cresta de las olas y el motivo de las flores en el pareo son “como amebas y están animados por una vitalidad orgánica” (5).Si agregamos la conchilla en el rincón, oculta en parte por la caprichosa forma de flores rojas, es evidente que el cuerpo autocontenido y semejante a un fruto de la bañista está rodeado por un ballet de criaturas animadas. Las flores rojas, como el fondo amarillo, reaparecen en Parahi te marae, 1892 (Philadelphia Museum of Art), donde las plantas imitan la sexualidad humana (6). En la pintura de Buenos Aires, la hoja grande que da lugar al título está dividida como una mano en extremidades similares a dedos y apunta hacia el trasero de la mujer (7). A Gauguin le encantaban las sugerencias anatómicas de las frutas y las flores que encontraba en Tahití, las que con frecuencia habían pasado al lenguaje y la mitología locales, y es probable que aquí haya aludido además a la semejanza entre las nalgas femeninas y la nuez llamada coco de mer, “coco de mar”, cuyo nombre científico había sido al principio Lodoicea callypige en razón, precisamente, de esta analogía (8).El título que le puso al cuadro se traduce de hecho como “mujer del mar” y está basado en la misma fórmula que empleó para Vahine no te tiare, “mujer de la flor”, 1891 (Ny Carlsberg Glyptotek, Copenhague) y Vahine no te vi, “mujer del mango”, 1892 (Baltimore Museum of Art). Hablando de esta última, Hiriata Millaud ha observado que vahine –contrariamente a hine– significa una mujer que ya tiene una vida social, y que el atributo introducido por no (“para” o “de”) puede definir el carácter de la mujer en cuestión antes que servir simplemente como una función descriptiva y mnemónica (9). La mujer en Vahine no te miti dirige su mirada y su oído hacia el océano, más específicamente hacia el mar abierto que aparece entre dos rocas o islas. Igual que las figuras de David Friedrich vistas de espalda, ella actúa por lo tanto como mediadora entre la naturaleza y el espectador, y parece realmente ser “del mar”, compenetrada con él, como una Venus tahitiana que es a la vez Anadiomene y Calipigia.\r\nDario Gamboni\r\n1— Véase: Bernard Dorival, Carnet de Tahiti. Paris, Quatre Chemins-Editart, 1954; Carnet de Tahiti. Taravao, Avant et Après, 2001. 2— Thadée Natanson, “Oeuvres récentes de Paul Gauguin”, La revue blanche, diciembre de 1893, citado en: Marla Prather y Charles F. Stuckey (ed.), 1987, p. 225. 3— Véase la entrada de Charles F. Stuckey en: Richard Brettell et al., The Art of Paul Gauguin, cat. exp. Washington, National Gallery of Art, 1988. Versión francesa Gauguin, cat. exp. Paris, Réunion des musées nationaux, 1989, nº 144.4— Raquel Edelman, 1948, p. 73-79. 5— Ronald Pickvance, Gauguin, cat. exp. Martigny, Fondation Pierre Gianadda, 1998, nº 32. 6— Dario Gamboni, “Parahi te marae: où est le temple?”, 48/14: La revue du Musée d’Orsay, Paris, nº 20, 2005, p. 6-17. 7— Véase la planta más explícitamente antropomórfica que sostiene a una pareja copulando en el manuscrito Album ancien culte mahorie (1892, Musée d’Orsay, París, RF 10755, folio 46). 8— Gauguin posteriormente grabó toda la superficie de una de esas nueces (Coco de mer, ca. 1901-1903, Albright-Knox Art Gallery, Buffalo). 9— Hiriata Millaud, “Les titres tahitiens de Gauguin” en: Ia Orana Gauguin, cat. exp. Paris, Somogy, 2003, p. 81-90.\r\n1893. NATANSON, Thadée, “Oeuvres récentes de Paul Gauguin”, La revue blanche, Paris, diciembre. 1936. Plástica, Buenos Aires, a. 2, nº 5, abril, reprod. byn p. 10. 1948. EDELMAN, Raquel, “Gauguin en Buenos Aires”, Ver y Estimar, Buenos Aires, vol. 2, nº 7-8, octubre-noviembre, p. 73-79, reprod. p. 75. 1964. WILDENSTEIN, Georges, Gauguin. Paris, Fondation Wildenstein, vol. 1, nº 465. 1977. FIELD, Richard S., Paul Gauguin: The Paintings of the First Trip to Tahiti. Tesis de doctorado, Harvard University [1963]. New York/London, Garland, nº 21.1987. PRATHER, Marla y Charles F. Stuckey, Gauguin: A retrospective. New York, Hugh Lauter Levin Associates, p. 224-226, reprod. nº 64. 1990. HADDAD, Michèle, La divine et l’impure: le nu au XIXe. Paris, Les Éditions du Jaguar, p. 49-50, reprod. color p. 51. 1993. 1893: L’Europe des peintres, cat. exp. Paris, Réunion des musées nationaux, p. 20.	\N	331	\N	\N	1892	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	El MNBA tiene la suerte de poseer dos pinturas de bañistas de Gauguin, ambientada en este caso no en Francia, sino en los mares del Sur. Esta obra aparece mencionada como Estudio de espalda desnuda en una lista de pinturas realizadas durante la primera estadía en Tahití que Gauguin anotó aproximadamente en abril de 1892 en su Carnet de Tahiti (1). Fue pintada en Mataiea y se basa en un dibujo en color del mismo cuaderno de bocetos, probablemente realizado con modelo y con una cuadrícula apenas visible, útil al momento de repetir la co	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
223	La Toilette apres le bain (El arreglo después del baño)	la-toilette-apres-le-bain-el-arreglo-despu-s-del-ba-o	La Toilette apres le bain (El arreglo después del baño)	\N	\N	126		\N	66,5 x 78,5 cm.	\N		\N	318	\N	\N	1888	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100		\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
231	La Nymphe surprise (La ninfa sorprendida)	la-nymphe-surprise-la-ninfa-sorprendida	La Nymphe surprise (La ninfa sorprendida)	\N	\N	126		\N	Oleo Sobre tela 144,5 x 112,5 cm. Marco: 169,8 x 137,5 x 9,5 cm.	\N	La nymphe surprise inaugura un período clave de la trayectoria de Édouard Manet y de la historia del modernismo en la pintura francesa (1). Según Barskaya, fue terminada y enviada por el artista a la exposición de la Academia de Arte de San Petersburgo en 1861 con el título Ninfa y sátiro, dos años antes de la exposición en el Salón de Rechazados de Le déjeuner sur l’herbe y de Olympia (pintada ese mismo año aunque enviada y aceptada en el Salon des Artistes Français en 1865, ambas en el Musée d’Orsay, París). Una larga serie de estudios documentales y técnicos, así como discusiones respecto de sus posibles fuentes, revelan un proceso largo y complejo en la elaboración de esta tela, considerada el primer gran tableau-laboratoire de Manet reelaborando modelos de la gran pintura italiana y holandesa de los siglos XVI y XVII. La obra permaneció en poder del artista hasta su muerte, y existe evidencia de que Manet la consideró uno de sus cuadros más importantes.	\N	317	\N	\N	1861	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	La nymphe surprise inaugura un período clave de la trayectoria de Édouard Manet y de la historia del modernismo en la pintura francesa (1). Según Barskaya, fue terminada y enviada por el artista a la exposición de la Academia de Arte de San Petersburgo en 1861 con el título Ninfa y sátiro, dos años antes de la exposición en el Salón de Rechazados de Le déjeuner sur l’herbe y de Olympia (pintada ese mismo año aunque enviada y aceptada en el Salon des Artistes Français en 1865, ambas en el Musée d’Orsay, París). Una larga serie de estud	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
619	Diván	new	\N	\N	\N	\N	\N	\N	Oleo sobre tela.	\N	Trabaja desde un posicionamiento crítico sobre la circulación y la reapropiación de las imágenes en la era contemporánea, donde las fuentes y referentes visuales se multiplican, se entremezclan y se transforman constantemente. En su pintura, la apropiación no es un fin en sí mismo, sino un medio para interrogar la memoria cultural, las iconografías compartidas y la relación afectiva que establecemos con las imágenes.	\N	620	\N	\N	2025	\N	\N	2025-09-24 11:32:30.482011-03	2025-09-24 11:34:01.615389-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
332	Puente de La Boca	puente-de-la-boca	Puente de La Boca	\N	\N	\N	\N	\N	\N	\N	\N	\N	334	\N	\N	0	\N	135	2025-05-19 14:07:33.515503-03	2025-10-07 11:25:38.909122-03	100	\N	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
768	Sillón cinta	\N	\N	\N	\N	\N	\N	\N	\N	\N	Con curaduría de Sandra Hillar y Wustavo Quiroga, y producción de Satsch Gallery, el recorrido estará organizado en cinco salas temáticas que atraviesan distintos formatos, materiales y lenguajes trabajados por Churba.	\N	772	\N	\N	1990	\N	\N	2025-10-29 09:26:42.092857-03	2025-10-29 09:31:53.128206-03	100	\N	\N	753	t	\N	3	\N	es	\N	\N	es	\N	f	\N	\N	\N	0
482	Utopia del Sur	utopia-del-sur	Utopia del Sur	\N	\N	126		\N	Oleo sobre tela 180 x 190 cm.	\N	García Uriburu le dedicó vida y obra: una proclama urgente por el cuidado del medio ambiente, “La globalización ha ligado salvajemente nuestras economías, creando una cruel dependencia que ha dividido aún más a la población mundial. Los países desarrollados contaminan el agua con fluidos tóxicos, derraman petróleo en nuestros mares y ríos, sin reparar el daño que ocasionan. Hace más de cuarenta años que intento dar una alarma contra la contaminación de ríos y mares, y es a través de mis acciones artísticas en distintos puntos del planeta que he transformado mi obra en una suerte de alerta contestataria globalizadora. Hoy y con más motivos que hace cuarenta años, sigo denunciando la contaminación del agua, y la salvaje destrucción que hacemos de las reservas del planeta. Un planeta que en nuestra ciega omnipotencia creemos inagotable e indestructible”.	\N	490	\N	\N	1993	\N	135	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100	\N	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
209	Prairies du Valhermeil près Pontoise	prairies-du-valhermeil-pr-s-pontoise	Prairies du Valhermeil près Pontoise	\N	\N	126		\N	Oleo Sobre tela 55 x 92 cm.	\N	Donación Mercedes Santamarina, 1970.	\N	\N	\N	\N	1874	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	Donación Mercedes Santamarina, 1970.	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
207	Le Pont d`Argenteuil (El Puente de Argenteuil)	le-pont-d-argenteuil-el-puente-de-argenteuil	Le Pont d`Argenteuil (El Puente de Argenteuil)	\N	\N	126	Donación Mercedes Santamarina, 1970.	\N	Oleo Sobre tela 60 x 100 cm.	\N	Esta pintura plantea algunos interrogantes de orden iconográfico. Generalmente señalado con el título de Puente de Argenteuil, el cuadro es llamado de otra manera por Daniel Wildenstein, el autor del catálogo razonado de las obras de Claude Monet (1), que individualiza más bien en el paisaje representado al puente ferroviario de Chatou (2), visto desde la isla de Chiard. Esta propuesta sería más atendible. En efecto, la obra muestra un escorzo perspectivo, de abajo arriba, de un puente ferroviario de arcos amplios. Pero sucede que el puente de Argenteuil, de igual factura, es un puente vial (Le Pont d’Argenteuil, 1870, Musée d’Orsay, París); el puente ferroviario de la misma ciudad, en cambio, "no está formado por una serie de arcos redondeados sino por una sola arcada en forma de cavidad con compartimentos” que se apoya sobre “cuatro pares de cilindros alargados de cemento” (3), similares a columnas y sin arcos (Le pont du chemin de fer à Argenteuil, 1874, Musée d’Orsay, París). La isla de Chiard, conocida también como “isla de los impresionistas”, quienes la frecuentaron regularmente para pintar en plein air (4), se encuentra a orillas del Sena, en la periferia oeste de París. Toda la zona, constituida por numerosas localidades (Argenteuil, Asnières, Chatou) muy cercanas entre sí, era un destino muy frecuentado los domingos por los parisinos que iban allí a distenderse a la orilla del agua; esos lugares estaban, por lo demás, a solo media hora de la estación Saint-Lazare. Toda la zona, y en particular la isla de Chiard, albergaba, en la riqueza de su vegetación, numerosas viviendas de artistas. El propio Monet alquiló una casa en Argenteuil entre 1872 y 1877, justamente para estar lo más cerca posible de estos temas que tanto lo fascinaban. Verdaderas novedades sociológicas, estas localidades suscitaron el interés de toda la cultura francesa, desde la llamada “masa” hasta las corrientes más sofisticadas de la vanguardia artística y literaria. En sus representaciones de Argenteuil –aunque con ese nombre debe entenderse a menudo una zona mucho más amplia Monet denota cierta ambivalencia. En algunos cuadros ofrece una visión bucólica de la que están ausentes, o casi, la mayor parte de los símbolos de la modernidad, alineándose en ese sentido a la visión de la naturaleza cotidiana de Corot y Daubigny, alejada de lo pintoresco, casi intacta. En otras obras del mismo período, en cambio, los símbolos del progreso industrial alternan y se mezclan con los de la vida rural, como para subrayar una armonía posible entre naturaleza y modernidad. Las imágenes serenas de las regatas (Le bassin d’Argenteuil, 1872, Musée d’Orsay), a menudo representadas por el artista, atraído por los juegos de agua y de luz, alternan asimismo con las arquitecturas metálicas de los puentes modernos, que después de las destrucciones de la guerra franco-prusiana estaban redefiniendo nuevamente el paisaje. El tema de los ferrocarriles había entrado en el repertorio de Monet a partir de 1874, cuando el artista pintó numerosas vistas del Sena y de los puentes que lo atraviesan fuera de París. Entre 1850 y 1870, Francia conoció un gran desarrollo industrial respaldado por la construcción de importantes infraestructuras (ferrocarriles, estaciones, puentes, viaductos) que se insertaron repentinamente en el paisaje urbano y rural, sobre todo alrededor de la capital. En 1871, 16.000 km de red ferroviaria atravesaban ya todo el territorio francés y la pintura no fue indiferente a este hecho. Monet compartió su interés por los motivos ferroviarios con varios precursores de la pintura de tema moderno, como el alemán Adolf Menzel y el inglés William Turner, de quien había visto en Londres la pintura Rain, Steam and Speed durante su estadía entre 1870 y 1871. Los puentes de Monet, al igual que las sucesivas estaciones realizadas en 1877 (en particular la de Saint-Lazare), no constituyen una “serie” en sentido estricto, sino más bien una “secuencia”, según la definición de Grace Seiberling desarrollada en torno de la misma temática. Comparadas con las famosas series de los años 90 (La catedral de Rouen, por ejemplo), estas pinturas no presentan ni una concepción unitaria ni un trabajo de armonización general efectuado en el taller. Como el puente de Argenteuil, también el de Chatou había atraído la atención de numerosos artistas, que lo representaron desde distintos puntos de vista (Pierre-Auguste Renoir, Pont de chemin de fer à Chatou ou Les marronniers roses, 1881, Musée d’Orsay). El cuadro de Buenos Aires constituye un paisaje “puro”, vaciado de la presencia humana, enteramente concentrado en la reacción rápida del artista frente a la naturaleza, traducida aquí en un follaje denso y vibrante, completamente desarrollado en tonalidades frías (azul, verde, amarillo). El punto de vista de la obra –rebajado a un nivel coincidente con la rica vegetación– desde el cual se distingue el puente ferroviario atravesado por un tren en marcha, parece querer subrayar la idea de una modernidad aparecida al artista en forma imprevista y casi por casualidad. Esta obra proviene de la rica colección impresionista de Mercedes Santamarina. El MNBA posee otra obra de Monet, La berge de la Seine (1880), proveniente de la Exposición Internacional de Arte del Centenario, de 1910.\r\n  Barbara Musetti\r\n1— Entre las biografías más recientes, véase: M. Alphant, Claude Monet. Une vie dans le paysage. Paris, Hazan, 1993; C. F. Stuckey, Monet. Un peintre, une vie, une oeuvre. Paris, Belfond, 1992. 2— D. Wildenstein, 1974, p. 1875. 3— H. P. Tucker, Monet at Argenteuil. New Haven/London, Yale University Press, 1982, p. 70. 4— Señalemos dos catálogos de exposición dedicados al paisaje impresionista: L’Impressionnisme et le paysage français, cat. exp. Paris, Réunion des musées nationaux, 1985; Landscapes of France. Impressionism and its Rivals, cat. exp. London/Boston, Hayward Gallery/ Museum of Fine Arts, 1995.\r\n1964. NIEULESEU, R., “G. Di Bellio, l’ami des impressionnistes”, Revue Roumaine d’Histoire de l’Art, Bucarest, t. 1, nº 2, p. 222, 278. 1974. WILDENSTEIN, Daniel, Claude Monet. Biographie et catalogue raisonné. Lausanne/ Paris, Bibliothèque des Arts, t. 1, nº 367, reprod. p. 1875.	\N	334	\N	\N	1875	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	Esta pintura plantea algunos interrogantes de orden iconográfico. Generalmente señalado con el título de Puente de Argenteuil, el cuadro es llamado de otra manera por Daniel Wildenstein, el autor del catálogo razonado de las obras de Claude Monet (1), que individualiza más bien en el paisaje representado al puente ferroviario de Chatou (2), visto desde la isla de Chiard. Esta propuesta sería más atendible. En efecto, la obra muestra un escorzo perspectivo, de abajo arriba, de un puente ferroviario de arcos amplios. Pero sucede que el 	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
324	Portrait d'Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	portrait-d-ernest-hosched-et-sa-fille-marthe-retrato-del-se-or-hosched-y-su-hija	Portrait d'Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	\N	\N	\N	\N	\N	\N	\N	\N	\N	325	\N	\N	0	\N	135	2025-05-19 14:07:26.434656-03	2025-05-19 14:07:26.434656-03	100	\N	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
272	En Normandie	en-normandie	En Normandie	\N	\N	126	Colección particular. Cedida en comodato al Museo Nacional de Bellas Artes, 2021.	\N	Oleo Sobre tela 162 x 207 cm.	\N	Este óleo sobre tela de gran formato fue el envío de Obligado, por entonces radicada en Francia, al salón parisino de 1902. En ella, las figuras femeninas que dominan la composición realizan redes de pesca en el norte de la costa francesa, una actividad de vital importancia para la economía regional.\\n El cuadro presenta a un grupo de mujeres entregadas a una tarea minuciosa y sistemática. Igual de laborioso fue el trabajo de la artista para crear esta escena: ejecutó detallados estudios de cada figura y de la composición en general, a la que consideraba “el alma de toda pintura”.\r\n\r\nPara conocer más sobre esta obra, invitamos a leer el ensayo de la historiadora María Lía Munilla Lacasa, publicado en el micrositio dedicado a la exposición  curada por Georgina Gluzman, que traza un paralelo entre la tela de Obligado y la emblemática “Sin pan y sin trabajo”, de De la Cárcova, y celebra esta reparación histórica hacia la obra de una de las grandes artistas del período en el país.\\n “El mundo del trabajo –o de su ausencia– es el tema que ambos cuadros abordan. \r\n\r\nMientras que en la obra de María Obligado seis trabajadoras de la costa de Normandía, al norte de Francia, ensimisman su rutina en la tarea de tejer redes de pesca, en el óleo de Ernesto de la Cárcova se acentúa con particular dramatismo un topos pictórico europeo característico de la segunda mitad del siglo XIX: el de la pobreza urbana”, explica Munilla Lacasa.	\N	303	\N	\N	1902	\N	135	2025-05-19 12:30:21.186648-03	2025-10-29 08:52:06.061462-03	100	Este óleo sobre tela de gran formato fue el envío de Obligado, por entonces radicada en Francia, al salón parisino de 1902. En ella, las figuras femeninas que dominan la composición realizan redes de pesca en el norte de la costa francesa, una actividad de vital importancia para la economía regional.\\n El cuadro presenta a un grupo de mujeres entregadas a una tarea minuciosa y sistemática. Igual de laborioso fue el trabajo de la artista para crear esta escena: ejecutó detallados estudios de cada figura y de la composición en general, 	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
282	Abel	abel	Abel	\N	\N	128		\N	Bronce 47 x 198 x 98 cm.	\N	Lucio Correa Morales, formado en Florencia con Urbano Lucchesi, realizó una vasta producción de retratos, esculturas funerarias, monumentos conmemorativos y obras decorativas, además de dedicarse a la enseñanza de la escultura en la Sociedad Estímulo de Bellas Artes, origen de la Academia en la Argentina.\r\nEn 1902 realizó un boceto de Abel en terracota y un segundo en bronce, conservados ambos en colecciones privadas, con la figura aislada del joven sin vida asesinado por su hermano Caín. En mayo de 1903 fue asociado por un crítico de La Nación con la escultura Abel del italiano Giovanni Duprè, un mármol de 1842, a pesar de las diferencias compositivas.\r\n\r\nLas expresiones del mismo Correa Morales nos revelan las verdaderas intenciones de su trabajo, pues era consciente de que “Nadie comprendió ni tal vez se supuso que ese ‘Abel’ no era el que fue muerto por Caín, sino el arte argentino muerto por sus hermanos. En esa figura hasta mi nombre le había disfrazado; […] yo gustoso habría firmado Moralaine, Moralini ó Moralai”. Este comentario permite interpretar la frase como alusión a la falta de desarrollo y de interés por el arte nacional: “Muerto sin descendencia como miserable / Es fácil sacar la conclusión / Que nosotros somos toda raza de Caín”.\r\n\r\nLa versión en yeso de Abel obtuvo la medalla de plata de Escultura en la Exposición Internacional de Saint Louis en 1904. Schiaffino, responsable del envío argentino, había reclamado al jurado una medalla de oro para Lucio Correa Morales pero su propuesta fue rechazada, al ser criticada la proporción inadecuada de los brazos de la figura (2). El reconocimiento internacional estimuló la posibilidad de su fundición en bronce, junto con otras obras premiadas como Las pecadoras de Rogelio Yrurtia.	\N	300	\N	\N	1902	\N	135	2025-05-19 12:30:21.186648-03	2025-10-25 15:00:20.715353-03	100	Lucio Correa Morales, formado en Florencia con Urbano Lucchesi, realizó una vasta producción de retratos, esculturas funerarias, monumentos conmemorativos y obras decorativas, además de dedicarse a la enseñanza de la escultura en la Sociedad Estímulo de Bellas Artes, origen de la Academia en la Argentina.\r\nEn 1902 realizó un boceto de Abel en terracota y un segundo en bronce, conservados ambos en colecciones privadas, con la figura aislada del joven sin vida asesinado por su hermano Caín. En mayo de 1903 fue asociado por un crítico de	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
884	La jungla	\N	\N	\N	\N	\N	\N	\N	Gouache, Papel y Lienzo\r\nTécnica\tÓleo sobre papel kraft\r\nDimensiones: 2.84 x 2.92 mts	\N	La Jungla es un cuadro al óleo, pintado por Wifredo Lam en 1943. Se exhibe en el MoMA, Museo de Arte Moderno de Nueva York, EUA. Este cuadro ha sido interpretado como la síntesis de un ciclo antillano, en virtud del espacio barroco dominante y de la atmósfera creada por la asociación de lo humano, lo animal, lo vegetal y lo divino. Hay en él un vocabulario visual que evolucionó desde el paisaje de corte académico hacia un tema y un lenguaje de arte moderno. En este óleo parecen fusionarse visiones y vivencias del pintor; el mítico paisaje insular, la incorporación de contenidos e iconografías procedentes de los sistemas mágico-religiosos de origen africano extendidos en Cuba y el Caribe, conjugándose en toda su definición.	\N	889	\N	\N	1943	\N	\N	2025-11-29 07:38:16.171156-03	2026-01-04 17:58:09.058955-03	100	\N	\N	860	t	\N	3	\N	..	\N	\N	es	\N	f	\N	\N	\N	0
589	Troncos	new	\N	\N	\N	\N	\N	\N	Técnica: Acuarela y gouache sobre papel montado sobre cartón\r\n31,7 x 46,8 cm\r\nNro. de inventario: 2001.184\r\nDonación: Eduardo F. Costantini, Buenos Aires	\N	Para analizar Troncos uno puede situarse en dos lugares distintos. Primero, es posible pensar la obra en el contexto de la producción de Xul Solar. Y luego, tratar de ver si su estudio iconográfico nos permite comprender cuál es su sentido.\r\n\r\nAl reconstruir su contexto de producción, entendido aquí como aquello que rodea a la obra, lo primero que se verifica es la existencia de cuatro pinturas que comparten casi la misma iconografía y probablemente el mismo tema, aunque por lo menos dos de ellas –una de la colección del Museo Nacional de Bellas Artes (MNBA) y la segunda de la colección del Museo Xul Solar - Fundación Pan Klub (MXS)– se alejan en la elección de la paleta y también en su estilo, a lo que se debe agregar que sus dimensiones son menores. De una tercera pieza solo conocemos un fragmento (también perteneciente a la colección de la Fundación Pan Klub), que se diferencia del resto del conjunto por la técnica y el soporte elegidos, óleo sobre cartón, lo que lo sitúa como parte de otro conjunto mayor de óleos realizados aproximadamente entre 1918 y 1920. Por último, una cuarta obra, hoy desaparecida, que se corresponde con la versión estudiada aquí –aunque presenta pequeñas diferencias que, para ser reconocidas, obligan a un cuidadoso examen de ambas pinturas– y, si bien no lo sabemos con certeza, es probable que compartiera también las mismas medidas.	\N	590	\N	\N	1919	\N	\N	2025-09-19 10:50:26.423731-03	2025-09-21 19:51:23.930402-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
452	El recurso del método	el-recurso-del-m-todo	El recurso del método	\N	\N	126		\N	Tinta sobre papel 30 x 40 cm.	\N	El recurso del método es una novela del escritor cubano Alejo Carpentier.  \r\nPublicada en 1974, pertenece al subgénero de la literatura hispanoamericana conocido como novela del dictador.	\N	464	\N	\N	2012	\N	135	2025-07-25 18:47:09.47387-03	2025-10-07 17:14:38.558215-03	100	\N	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
205	Effet de neige à Louveciennes	effet-de-neige-louveciennes	Effet de neige à Louveciennes	\N	\N	126		\N	Oleo Sobre tela 50 x 61,5 cm.	\N	Donación Mercedes Santamarina, 1960.	\N	327	\N	\N	1877	\N	135	2025-05-19 12:29:43.192411-03	2025-10-07 17:07:26.573077-03	100	Donación Mercedes Santamarina, 1960.	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
40219	Vara de mando de Manuel Mansilla	\N	\N	\N	\N	\N	\N	\N	Madera carey, oro y bronce facetado.\r\nNro. de inventario 1248.	\N	La vara es un símbolo de poder que distinguía a las autoridades del Cabildo cuando ejercían funciones en la ciudad.\r\nEsta perteneció a Manuel Mansilla, aguacil mayor del Cabildo entre 1975 y 1821. Como Mansilla ejercía un cargo perpetuo, la pieza lleva en la empuñadura un monograma con sus iniciales.	\N	40298	\N	\N	0	\N	\N	2026-01-13 15:08:24.683212-03	2026-01-13 15:08:24.683226-03	100	\N	\N	537	t	\N	1	\N	es	\N	\N	es	\N	t	\N	Original del siglo XIX	Siglo XVIII y XIX	1
34719	Traje de calle del alférez real	\N	\N	\N	\N	\N	\N	\N	Recreación a partier del original (2023).\r\nNro. inventario 1047.	\N	El alférez real era uno de los principales funcionarios del Cabildo. Se encargaba de representar al rey en las ceremonias públicas portando el estandarte real. Además, se ocupaba de organizar las milicias en tiempos de guerra.\r\n\r\nEn las ceremonias públicas, el alférez real lucía un traje como este. La recreación fue realizada a partir del traje que perteneció a Francisco Antonio de Escalada, último alférez real del Cabildo de Buenos Aires, que forma parte de la colección del Museo. Está formado por calzón corto, chaleco y casaca. Se usaba con camisa blanca, medias del mismo color y zapatos con taco alto y grandes hebillas. En la época, este tipo de vestimenta era un signo de prestigio: contar con un traje como este era muy costoso.\r\n\r\nEsta recreación se realizó para proteger la pieza original de la exposición constante. Para replicar las medidas, se levantaron moldes a partir del traje original. \r\nSe seleccionó una tela similar y se la tiñó buscando una aproximación al color que tenía el traje en el momento de su uso.	\N	35197	\N	\N	0	\N	\N	2026-01-12 10:28:58.706081-03	2026-01-13 16:35:46.099565-03	100	\N	\N	537	t	\N	3	\N	es	\N	\N	es	\N	t	\N	Original del siglo XVIII	Siglo XVIII	1
12377	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	\N	\N	\N	\N	Oil on canvas. 60 × 50" (152.4 × 127 cm).	\N	Las religiones afrocubanas incluyen el Lucumí, o Santería, del pueblo de habla yoruba del suroeste de Nigeria, que es el culto a los orishas o espíritus. También tenemos el Palo, de África Central, las prácticas de Benín llamadas Arará, así como ideas europeas o cristianas.\r\n\r\nEl título de esta pintura, Malembo Sombrío, Dios de la Encrucijada, hace referencia al orisha de la tradición lucumí o santería.\r\nLa palabra "Malembo" en el título podría referirse a un puerto negrero en la costa occidental de África. \r\nPara cuando Lam nació en 1902, solo habían pasado 16 años desde la abolición de la esclavitud en Cuba.\r\n\r\nEn gran parte de la obra de Lam se observa lo que podría identificarse como una figura similar a la de Eleguá: esta pequeña cabeza abovedada. Eleguá es el orisha de la encrucijada. Tiene la capacidad única de abrir puertas, comunicarse entre idiomas y atraer la buena fortuna y alejar la mala suerte.	\N	12516	\N	\N	1943	\N	\N	2025-12-26 16:07:57.143085-03	2026-01-04 17:58:22.333557-03	100	\N	\N	860	t	\N	3	\N	..	\N	\N	es	\N	t	\N	\N	\N	0
402	La Emperatriz Theodora	la-emperatriz-theodora	La Emperatriz Theodora	\N	\N	126		\N	Oleo sobre tela 224,5 x 125,5 cm. - Marco: 226,5 x 127,5 cm.	\N	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, representa una visión orientalista y teatral de la emperatriz bizantina Teodora, esposa de Justiniano I,  la mujer más influyente y poderosa en la historia del Imperio romano de Oriente.\r\n\r\nNo es una representación histórica, sino más bien una fantasía opulenta basada en la fascinación europea del siglo XIX por el Oriente y el pasado imperial. Benjamin-Constant era un pintor de gran técnica especializado en retratos de élite y figuras históricas: poder, lujo,  sofisticación, poses calculadas,  texturas complejas como seda y terciopelo, y entornos refinados. \r\n\r\nDe origen humilde, Theodora se ganó la vida de joven como actriz y cortesana hasta que fue elegida por Justiniano como esposa. La imagen histórica -basada en Procopio de Cesarea en la antigüedad  y Gibbon en el siglo XVIII-  es  la de una mujer astuta, inteligente y cruel. En la actualidad se considera que ese perfil es exagerado por la visión rígida que se tenía de la función social de la mujer. La visión moderna es que fue una figura política central en el Imperio Bizantino,  co-gobernante de facto con Justiniano. Participó activamente en las decisiones de gobierno. En la revuelta de Niká de 532, convenció a Justiniano de no huir y enfrentar a sus enemigos con su famosa frase -“el púrpura es una buena mortaja”-. Promovió leyes en favor de los derechos de las mujeres: prohibió el tráfico sexual forzado,  fortaleció los derechos de las mujeres en el matrimonio y el divorcio, y fundó hogares para mujeres rescatadas de la prostitución, algo sin precedentes en la época. Como resultado de los esfuerzos de Teodora, el estado de la mujer en el Imperio Bizantino fue más elevado que el del resto de las mujeres en Europa.\r\n\r\nEn la historia argentina el paralelismo inmediato es con Eva Perón.\r\n\r\nBenjamin-Constant se propuso crear un ícono visual del poder femenino y del exotismo oriental. y lo que salió, quizás involuntariamente,  es todo un símbolo de modernidad: una mujer joven, poderosa, hermosa e inteligente con mirada desafiante que se atreve a ser distinta.	\N	417	\N	\N	1887	\N	135	2025-06-10 15:14:26.074639-03	2025-10-07 14:57:44.176318-03	100	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, representa una visión orientalista y teatral de la emperatriz bizantina Teodora, esposa de Justiniano I,  la mujer más influyente y poderosa en la historia del Imperio romano de Oriente.  .	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
603	El mal	new	\N	\N	\N	\N	\N	\N	Su universo es un surrealismo punk y sudoroso, una brea fantástica que chorrea por las paredes del museo. Son pinturas que invitan a ser miradas una y otra vez, hasta que los ojos se agoten y ya no puedan distinguir entre la realidad y su vecina, la pesadilla.	\N	El trabajo de Bencardino se alimenta de imágenes encontradas en libros, revistas, tapas de discos, videoclips, internet y su archivo personal de objetos y otros materiales que circulan en la cultura de masas y sus plataformas. Sus referencias provienen de un imaginario afectivo muy particular: las estéticas de las comunidades queer y las adolescencias de su generación, los códigos visuales de las escenas contraculturales (como el punk, y los distintos subgéneros del metal), el comic y la ilustración (como Ciruelo, Victoria Francés, Luis Royo, Boris Vallejo y Magalí Villeneuve), y el imaginario fantástico literario (especialmente, de William Blake y J.R.R. Tolkien), entre muchas otras referencias. Procesa y distorsiona estas imágenes digitalmente, y a partir de esas nuevas imágenes elabora sus pinturas.	\N	604	\N	\N	2025	\N	\N	2025-09-21 19:58:27.991765-03	2025-09-21 20:02:52.780835-03	100	\N	\N	342	t	https://malba.org.ar/evento/carrie-bencardino-el-desentierro-del-diablo/	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
211	Femme aux champs (Campesina)	femme-aux-champs-campesina	Femme aux champs (Campesina)	\N	\N	126		\N	Oleo Sobre tela 88 x 48 cm.	\N	Relacionado con sus ideas socialistas y cercanas al anarquismo, se interesó por plasmar el trabajo del campesino y pintó la vida rural francesa.\r\nGalerie Georges Petit.	\N	576	\N	\N	1882	\N	135	2025-05-19 12:29:43.192411-03	2025-09-19 00:09:27.407699-03	100	Galerie Georges Petit.	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
280	Reposo	reposo	Reposo	\N	\N	126	Adquirido a Schiaffino, Eduardo por Ministerio de Justicia e Instrucción Pública	\N	109 x 200 cm.	\N	​Reposo fue pintado por Eduardo Schiaffino en París y admitido en la Exposición Universal con la que se celebró el centenario de la Revolución Francesa, donde obtuvo una medalla de tercera clase (bronce). Su autor fue el fundador y primer director del Museo Nacional de Bellas Artes, además de activo promotor de las bellas artes en Buenos Aires y primer historiador del arte argentino. En su libro La pintura y la escultura en Argentina (1933) comentó su propia obra en tercera persona, destacando la importancia de aquella medalla (solo el escultor Manuel de Santa Coloma había sido premiado en París 25 años antes que él y otros 25 años habrían de pasar antes de que otro argentino, Antonio Alice, volviera a ser premiado allí).\r\nEl cuadro presenta un desnudo de espaldas, en una pose algo forzada, con las piernas extendidas y cruzadas y un brazo arqueado sobre la cabeza. El cuerpo aparece pálido y azulado en medio de un espacio enteramente azul, en el que el paño sobre el que se encuentra tendido y el fondo tienen apenas diferencias de valor. Esta ambigüedad espacial se ve interrumpida solamente por un pequeño fragmento de piso, sobre el que se advierte el borde del paño (con brillos de terciopelo), en el ángulo inferior izquierdo de la tela. Hay también cierta ambigüedad de género en ese cuerpo casi adolescente, con el cabello muy corto y la cara oculta, en una posición que aparece como una reelaboración del Hermafrodito durmiente, el mármol helenístico restaurado por Bernini, del Museo del Louvre. Es tal vez el primer cuadro de inspiración simbolista pintado por Schiaffino, quien hasta entonces había exhibido algunas “impresiones” de paisaje y escenas de toilette más cercanas al estilo y la iconografía impresionistas. Jaime de la María comentó el cuadro para La Nación (2 de julio de 1890) atribuyéndole un estilo análogo al del maestro de Schiaffino en París, Pierre Puvis de Chavannes: “la analogía de carácter podrá parecer casual, pero la de estilo se explica: Schiaffino es puvisiste”. La atmósfera simbolista de Reposo adquiere un carácter más marcado en varias obras posteriores del artista, como Vesper (inv. 5377, MNBA), Craintive o Margot, tres retratos exhibidos al año siguiente en la exposición de la Sociedad de Damas de Nuestra Señora del Carmen, o el Desnudo (sinfonía en rojo) (inv. 7463, MNBA) que expuso en el Salón del Ateneo de 1895. Margot (inv. 1656, MNBA) fue celebrada en varios artículos como la primera obra en la que se revelaba su calidad como artista. Pero la polémica que entabló con el crítico que firmaba A.Zul de Prusia a propósito de la supuesta autoría de las obras de los pensionados en Europa, terminó ese año con un duelo a pistola.\r\nSchiaffino había viajado trayendo Reposo a Buenos Aires en medio de la crisis de 1890, con el objeto de exhibirlo y gestionar la renovación de su beca de estudios. Tanto la exposición de la pintura en la vidriera de la casa Bossi (donde el año anterior su primer envío desde París había sido comentado con franca hostilidad) como el otorgamiento de la beca, recibieron comentarios muy negativos en la prensa. Un comentarista anónimo del diario La Argentina (1 de julio de 1890) lo objetó por encontrarlo deforme e indecente, “con una sans-façon que huele a la legua al Paganismo y a sus más florecientes saturnales”. El artista encaró personalmente la defensa de su cuadro, que volvió a exponer en el primer Salón del Ateneo, en 1893, y nuevamente en el cuarto Salón del Ateneo en 1896, recogiendo en ambas ocasiones severas críticas, no solo hacia la pintura sino también al hecho de volver a incluirla en los salones cuando era una obra que no era nueva y que ya había sido expuesta y criticada con dureza. Es posible advertir en el gesto de Schiaffino una posición desafiante en consonancia con su tenaz actividad orientada –en sus propias palabras– a “educar el gusto” del público de Buenos Aires, introduciendo audacias del arte moderno en un género que a lo largo del siglo XIX se había tornado emblemático de aquellas. En Buenos Aires todavía resonaba el escándalo que había suscitado, en 1887, Le lever de la bonne de Eduardo Sívori.	\N	305	\N	\N	1889	\N	135	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	​Reposo fue pintado por Eduardo Schiaffino en París y admitido en la Exposición Universal con la que se celebró el centenario de la Revolución Francesa, donde obtuvo una medalla de tercera clase (bronce). Su autor fue el fundador y primer director del Museo Nacional de Bellas Artes, además de activo promotor de las bellas artes en Buenos Aires y primer historiador del arte argentino. En su libro La pintura y la escultura en Argentina (1933) comentó su propia obra en tercera persona, destacando la importancia de aquella medalla (solo e	\N	137	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0
16103	Je suis (Yo soy)	\N	\N	\N	\N	\N	\N	\N	\N	\N	Esta pintura presenta la figura de una mujer-caballo. La idea proviene de las religiones afrocubanas. En estas tradiciones, los iniciados participan en ceremonias donde sus cuerpos son poseídos por espíritus, llamados orishas.\r\n\r\nMartin Tsang es antropólogo y practicante de religiones afrocubanas.\r\n\r\nAntropólogo, Martin Tsang: Una de las maneras en que se describe la posesión es cuando el orisha desciende temporalmente a la tierra y monta a la persona, como un caballo. Esta idea es un motivo recurrente en la obra de Lam, y considero que la iconografía o el simbolismo de la mujer-caballo refleja la importancia de la mujer en los espacios religiosos afrocubanos. Las mujeres afrocubanas eran las guardianas del conocimiento. Dirigían ceremonias, comunidades e iniciaciones.	\N	16212	\N	\N	1949	\N	\N	2026-01-04 17:48:00.073762-03	2026-01-04 17:57:55.024221-03	100	\N	\N	860	t	\N	3	\N	..	\N	\N	es	\N	t	\N	\N	\N	0
450	Apocalipsis	apocalipsis	Apocalipsis	\N	\N	126		\N	Gouache sobre papel  75 x 55 cm.	\N	Según Dante Alighieri, el infierno es un cono invertido, dividido en nueve círculos que se estrechan a medida que descendemos. \r\n\r\nPara acceder a él hay que cruzar el río Aqueronte, guiados por Caronte, el barquero. Ningún viajero ha regresado para confirmar esa travesía. Por eso, Gárgano nos propone su propia versión del infierno, construido pacientemente sobre el papel a lo largo de muchos años. En la trama de líneas, el artista ofrece sus indicios, y nos corresponde a nosotros cruzar el río e interpretar aquello que vemos.	\N	465	\N	\N	2017	\N	135	2025-07-25 18:47:09.47387-03	2025-10-25 12:42:55.274049-03	100	\N	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0
\.


--
-- Data for Name: artwork_artist; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artwork_artist (artwork_id, artist_id, person_id) FROM stdin;
450	5058	\N
592	5062	593
452	5058	\N
311	5052	308
22902	5067	\N
582	5063	\N
768	5066	763
12377	5067	\N
589	5061	588
16103	5067	\N
603	5064	602
884	5067	\N
328	5036	\N
448	5058	\N
274	5047	\N
229	5041	\N
205	5033	\N
213	5036	\N
211	5035	\N
219	5038	\N
221	5032	\N
217	5036	\N
225	5039	\N
272	5046	\N
270	5045	\N
207	5034	\N
280	5050	\N
207	5035	\N
215	5037	\N
482	5060	\N
406	5057	\N
197	5043	\N
402	5059	\N
404	5055	\N
195	5031	\N
332	5053	\N
\.


--
-- Data for Name: artworkartist; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artworkartist (id, name, namekey, title, titlekey, artwork_id, person_id, created, lastmodified) FROM stdin;
198	\N	\N	\N	\N	197	193	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03
208	\N	\N	\N	\N	207	183	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
210	\N	\N	\N	\N	207	184	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
216	\N	\N	\N	\N	215	186	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
218	\N	\N	\N	\N	217	185	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
224	\N	\N	\N	\N	219	187	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03
275	\N	\N	\N	\N	274	265	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03
281	\N	\N	\N	\N	280	268	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03
313	\N	\N	\N	\N	311	308	2025-05-19 13:08:48.061148-03	2025-05-19 13:08:48.061148-03
330	\N	\N	\N	\N	328	185	2025-05-19 14:07:30.393177-03	2025-05-19 14:07:30.393177-03
449	\N	\N	\N	\N	448	447	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03
500	\N	\N	\N	\N	272	264	2025-08-19 13:37:01.998074-03	2025-08-19 13:37:01.998074-03
502	\N	\N	\N	\N	213	185	2025-08-19 13:37:03.192838-03	2025-08-19 13:37:03.192838-03
508	\N	\N	\N	\N	211	184	2025-08-19 14:51:33.802827-03	2025-08-19 14:51:33.802827-03
512	\N	\N	\N	\N	195	194	2025-08-19 15:01:35.931293-03	2025-08-19 15:01:35.931293-03
520	\N	\N	\N	\N	221	188	2025-08-20 09:51:46.76827-03	2025-08-20 09:51:46.76827-03
522	\N	\N	\N	\N	332	333	2025-08-20 15:19:05.762287-03	2025-08-20 15:19:05.762287-03
539	\N	\N	\N	\N	270	263	2025-08-29 09:38:43.344861-03	2025-08-29 09:38:43.344861-03
546	\N	\N	\N	\N	452	447	2025-08-29 14:00:38.585866-03	2025-08-29 14:00:38.585866-03
584	\N	\N	\N	\N	582	581	2025-09-19 10:06:01.75917-03	2025-09-19 10:06:01.75917-03
591	\N	\N	\N	\N	589	588	2025-09-19 10:51:24.120513-03	2025-09-19 10:51:24.120513-03
595	\N	\N	\N	\N	592	593	2025-09-19 20:09:56.204906-03	2025-09-19 20:09:56.204906-03
605	\N	\N	\N	\N	603	602	2025-09-21 20:02:52.782561-03	2025-09-21 20:02:52.782561-03
773	\N	\N	\N	\N	768	763	2025-10-29 09:31:53.131027-03	2025-10-29 09:31:53.131027-03
\.


--
-- Data for Name: artworkrecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artworkrecord (id, language, artwork_id, name, shortname, subtitle, spec, info, intro, photo, video, audio, usethumbnail, created, lastmodified, lastmodifieduser, draft, namekey, state, name_hash, subtitle_hash, info_hash, intro_hash, spec_hash, otherjson, otherjson_hash, opens, opens_hash, audioautogenerate, audioauto) FROM stdin;
22903	es	22902	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-07 08:27:21.472363-03	2026-01-07 08:27:21.472367-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
22904	en	22902	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-07 08:27:21.474041-03	2026-01-07 08:27:21.474041-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
22905	pt-BR	22902	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-07 08:27:21.474711-03	2026-01-07 08:27:21.474711-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
693	en	450	Apocalypse	\N		Gouache on paper 75 x 55 cm.	According to Dante Alighieri, Hell is an inverted cone, divided into nine circles that narrow as we descend. \n\nTo reach it, one must cross the River Acheron, guided by Charon, the ferryman. No traveler has ever returned to confirm that journey. Therefore, Gargano offers us his own version of Hell, patiently constructed on paper over many years. In the interweaving of lines, the artist offers his clues, and it is up to us to cross the river and interpret what we see.	\N	\N	\N	\N	f	2025-10-23 15:18:36.645799-03	2025-10-25 12:51:37.196501-03	100	\N	\N	\N	-1978804888	0	-909355096	0	-1726264413	\N	0	\N	0	t	f
694	en	272	En Normandie	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-23 15:20:06.595377-03	2025-10-23 15:20:06.595393-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
696	en	219	The Banks of the Seine	\N		\N	At the meeting of November 17, 1910, the members of the Buenos Aires City Council voted on a bill highlighting the role of the Municipality in promoting artistic culture, one of the possible avenues being the donation of works of art. This resolution benefited the MNBA, which had a state budget to increase its assets with the acquisition of works by the National Commission of Fine Arts during the "Buenos Aires 1910 International Centennial Art Exposition."	\N	\N	\N	\N	f	2025-10-23 15:38:00.588639-03	2025-10-23 15:38:00.589119-03	100	\N	\N	\N	1416721435	0	579260735	0	0	\N	0	\N	0	t	f
695	en	282	Abel	\N		\N	Lucio Correa Morales, trained in Florence by Urbano Lucchesi, produced a vast output of portraits, funerary sculptures, commemorative monuments, and decorative works. He also dedicated himself to teaching sculpture at the Sociedad Estímulo de Bellas Artes, the origin of the Academy in Argentina.\nIn 1902, he made a terracotta sketch of Abel and a second one in bronze, both preserved in private collections, featuring the isolated figure of the lifeless young man murdered by his brother Cain. In May 1903, a critic from La Nación associated the work with the Italian Giovanni Duprè's marble sculpture Abel, a work from 1842, despite the compositional differences.\n\nCorrea Morales's own expressions reveal the true intentions of his work, as he was aware that "No one understood, nor perhaps even imagined, that this 'Abel' was not the one killed by Cain, but rather Argentine art killed by its brothers. In that figure, even my name had been disguised; […] I would gladly have signed Moralaine, Moralini, or Moralai." This comment allows us to interpret the phrase as an allusion to the lack of development and interest in national art: "Dead without descendants like a wretch / It is easy to draw the conclusion / That we are all the race of Cain."\n\nThe plaster version of Abel won the silver medal for Sculpture at the International Exposition of Saint Louis in 1904. Schiaffino, responsible for the Argentine submission, had requested a gold medal from the jury for Lucio Correa Morales, but his proposal was rejected due to criticism of the inadequate proportions of the figure's arms (2). International recognition encouraged the possibility of its casting in bronze, along with other award-winning works such as Las pecadoras by Rogelio Yrurtia.	Lucio Correa Morales, trained in Florence with Urbano Lucchesi, produced a vast output of portraits, funerary sculptures, commemorative monuments, and decorative works. He also dedicated himself to teaching sculpture at the Sociedad Estímulo de Bellas Artes, the origin of the Academy in Argentina.\nIn 1902, he made a terracotta sketch of Abel and a second one in bronze, both preserved in private collections, featuring the isolated figure of the lifeless young man murdered by his brother Cain. In May 1903, he was associated with a critic of	\N	\N	\N	f	2025-10-23 15:20:40.6022-03	2025-10-25 12:41:02.521786-03	100	\N	\N	\N	2987144	0	721249886	1966264190	0	\N	0	\N	0	t	f
34720	es	34719	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-12 10:28:58.719833-03	2026-01-12 10:28:58.71984-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
34721	en	34719	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-12 10:28:58.722596-03	2026-01-12 10:28:58.722604-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
34722	pt-BR	34719	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-12 10:28:58.723934-03	2026-01-12 10:28:58.723937-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
40220	es	40219	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-13 15:08:24.708849-03	2026-01-13 15:08:24.708853-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
40221	en	40219	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-13 15:08:24.710197-03	2026-01-13 15:08:24.710198-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
40222	pt-BR	40219	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-13 15:08:24.710621-03	2026-01-13 15:08:24.710622-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
743	pt-BR	205	Effet de neige à Louveciennes	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-27 12:23:22.694133-03	2025-10-27 12:23:22.694146-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	f	f
701	pt-BR	282	Abel	\N		\N	Lucio Correa Morales, formado em Florença por Urbano Lucchesi, produziu uma vasta produção de retratos, esculturas funerárias, monumentos comemorativos e obras decorativas. Dedicou-se também ao ensino de escultura na Sociedad Estímulo de Bellas Artes, origem da Academia na Argentina.\nEm 1902, realizou um esboço de Abel em terracota e um segundo em bronze, ambos preservados em coleções particulares, apresentando a figura isolada do jovem sem vida assassinado por seu irmão Caim. Em maio de 1903, um crítico do jornal La Nación associou a obra à escultura em mármore Abel, do italiano Giovanni Duprè, de 1842, apesar das diferenças composicionais.\n\nAs próprias expressões de Correa Morales revelam as verdadeiras intenções de sua obra, pois ele estava ciente de que "Ninguém entendia, nem talvez imaginasse, que este 'Abel' não era aquele morto por Caim, mas sim a arte argentina morta por seus irmãos. Naquela figura, até meu nome havia sido disfarçado; [...] Eu teria assinado de bom grado Moralaine, Moralini ou Moralai". Esse comentário nos permite interpretar a frase como uma alusão à falta de desenvolvimento e interesse pela arte nacional: "Morto sem descendência como um miserável / É fácil tirar a conclusão / Que somos todos da raça de Caim".\n\nA versão em gesso de Abel conquistou a medalha de prata na categoria Escultura na Exposição Internacional de Saint Louis, em 1904. Schiaffino, responsável pela submissão argentina, havia solicitado ao júri uma medalha de ouro para Lucio Correa Morales, mas sua proposta foi rejeitada devido às críticas às proporções inadequadas dos braços da figura (2). O reconhecimento internacional estimulou a possibilidade de sua fundição em bronze, junto com outras obras premiadas, como Las pecadoras, de Rogelio Yrurtia.	\N	\N	\N	\N	f	2025-10-23 18:36:13.182252-03	2025-10-24 23:19:04.851636-03	100	\N	\N	\N	2987144	0	721249886	0	0	\N	0	\N	0	t	f
729	pt-BR	450	Apocalipse	\N		Guache sobre papel 75 x 55 cm.	Segundo Dante Alighieri, o Inferno é um cone invertido, dividido em nove círculos que se estreitam à medida que descemos.\n\nPara alcançá-lo, é preciso atravessar o Rio Aqueronte, guiado por Caronte, o barqueiro. Nenhum viajante jamais retornou para confirmar essa jornada. Assim, Gargano nos oferece sua própria versão do Inferno, pacientemente construída no papel ao longo de muitos anos. No entrelaçamento de linhas, o artista oferece suas pistas, e cabe a nós atravessar o rio e interpretar o que vemos.	\N	\N	\N	\N	f	2025-10-24 23:05:24.58625-03	2025-10-25 12:51:23.614565-03	100	\N	\N	\N	-1978804888	0	-909355096	0	-1726264413	\N	0	\N	0	t	f
769	es	768	new	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-29 09:26:46.768925-03	2025-10-29 09:26:47.278353-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	f	f
770	en	768	new	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-29 09:27:01.539872-03	2025-10-29 09:27:01.539897-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	f	f
771	pt-BR	768	new	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-29 09:27:02.428459-03	2025-10-29 09:27:02.428474-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	f	f
731	en	278	El despertar de la criada	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-25 13:21:18.836288-03	2025-10-25 13:21:18.836302-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
709	en	276	La vuelta al hogar	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-24 12:18:09.452697-03	2025-10-24 12:18:09.452711-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
710	pt-BR	276	La vuelta al hogar	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-24 12:18:11.467571-03	2025-10-24 12:18:11.467588-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
711	en	270	La vuelta del malón	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-24 12:24:12.79085-03	2025-10-24 12:24:12.790863-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
712	pt-BR	270	La vuelta del malón	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-24 12:24:14.853949-03	2025-10-24 12:24:14.853961-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
713	en	205	Effet de neige à Louveciennes	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-24 12:47:13.413439-03	2025-10-24 12:47:13.41345-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
714	pt-BR	452	El recurso del método	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-24 12:48:55.152478-03	2025-10-24 12:48:55.152491-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
715	en	452	El recurso del método	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-24 12:48:56.840114-03	2025-10-24 12:48:56.84012-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
730	pt-BR	272	En Normandie	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-24 23:52:16.278162-03	2025-10-24 23:52:16.27818-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
885	es	884	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2025-11-29 07:38:16.181603-03	2025-11-29 07:38:16.181606-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	f	f
887	pt-BR	884	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2025-11-29 07:38:16.18453-03	2025-11-29 07:38:16.184531-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	f	f
1496	en	402	Empress Theodora	\N		Oil on canvas 224.5 x 125.5 cm. - Frame: 226.5 x 127.5 cm.	The painting Theodora, painted by Jean-Joseph Benjamin-Constant in 1887, presents an Orientalist and theatrical vision of the Byzantine Empress Theodora, wife of Justinian I, the most influential and powerful woman in the history of the Eastern Roman Empire.\n\nIt is not a historical representation, but rather an opulent fantasy based on the 19th-century European fascination with the Orient and the imperial past. Benjamin-Constant was a highly skilled painter specializing in portraits of elite and historical figures: power, luxury, sophistication, calculated poses, complex textures such as silk and velvet, and refined settings.\n\nOf humble origins, Theodora earned her living as a young woman as an actress and courtesan until she was chosen by Justinian as his wife. The historical image—based on Procopius of Caesarea in antiquity and Gibbon in the 18th century—is that of a cunning, intelligent, and cruel woman. Today, this profile is considered exaggerated due to the rigid view of women's social role at the time. The modern view is that she was a central political figure in the Byzantine Empire, a de facto co-ruler with Justinian. She actively participated in government decisions. In the Nika Revolt of 532, she convinced Justinian not to flee and to confront his enemies with her famous phrase, "Purple makes a good shroud." She promoted laws in favor of women's rights: she prohibited forced sex trafficking, strengthened women's rights in marriage and divorce, and founded homes for women rescued from prostitution, something unprecedented at the time. As a result of Theodora's efforts, the status of women in the Byzantine Empire was higher than that of women in the rest of Europe.\n\nIn Argentine history, the immediate parallel is with Eva Perón.\n\nBenjamin-Constant set out to create a visual icon of female power and oriental exoticism. And what emerged, perhaps unintentionally, is a symbol of modernity: a young, powerful, beautiful and intelligent woman with a defiant gaze who dares to be different.	The painting Theodora, painted by Jean-Joseph Benjamin-Constant in 1887, represents an orientalist and theatrical vision of the Byzantine Empress Theodora, wife of Justinian I, the most influential and powerful woman in the history of the Eastern Roman Empire.	\N	\N	\N	t	2025-12-19 08:15:44.636301-03	2025-12-19 08:15:53.115456-03	100	\N	\N	\N	-90724174	0	1629620206	-1836300673	1668072257	\N	0	\N	0	t	f
1500	en	582	The Disasters of Mysticism	\N	\N	\N	\N	\N	\N	\N	\N	t	2025-12-20 13:01:09.893577-03	2025-12-20 13:01:09.8936-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
1501	pt-BR	582	The Disasters of Mysticism	\N	\N	\N	\N	\N	\N	\N	\N	t	2025-12-20 13:01:12.38285-03	2025-12-20 13:01:12.382853-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
886	en	884	The jungle	\N	\N	Gouache, Paper and Canvas\nTechnique: Oil on kraft paper\nDimensions: 2.84 x 2.92 meters	The Jungle is an oil painting by Wifredo Lam, painted in 1943. It is exhibited at MoMA, the Museum of Modern Art in New York City. This painting has been interpreted as the synthesis of an Antillean cycle, due to the dominant Baroque space and the atmosphere created by the association of the human, the animal, the plant, and the divine. It contains a visual vocabulary that evolved from academic landscape painting toward a theme and language of modern art. In this oil painting, the painter's visions and experiences seem to merge; the mythical island landscape, the incorporation of content and iconography from the magical-religious systems of African origin prevalent in Cuba and the Caribbean, all come together in their full definition.	\N	\N	\N	\N	t	2025-11-29 07:38:16.183563-03	2025-12-23 18:13:13.810293-03	100	\N	\N	\N	1098792388	0	-847270365	0	2072025561	\N	0	\N	0	f	f
7518	en	482	Utopia del Sur	\N	\N	\N	\N	\N	\N	\N	\N	t	2025-12-24 18:46:51.164555-03	2025-12-24 18:46:51.164564-03	101	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
12378	es	12377	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2025-12-26 16:07:57.148304-03	2025-12-26 16:07:57.148307-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
12379	en	12377	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2025-12-26 16:07:57.159466-03	2025-12-26 16:07:57.159467-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
12380	pt-BR	12377	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2025-12-26 16:07:57.160541-03	2025-12-26 16:07:57.160542-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
16104	es	16103	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-04 17:48:00.079237-03	2026-01-04 17:48:00.079245-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
16105	en	16103	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-04 17:48:00.085683-03	2026-01-04 17:48:00.085685-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
16106	pt-BR	16103	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-04 17:48:00.08701-03	2026-01-04 17:48:00.087012-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
\.


--
-- Data for Name: artworktype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artworktype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser, language, draft, audioautogenerate) FROM stdin;
116	Vaso	vase	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
117	Disco	disk	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
118	Figura	figure	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
119	Abanico	fan	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
120	Miniatura	miniature	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
121	Grabado	engraving	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
122	Mueble	furniture	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
123	Fotografía	photo	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
124	Estructura cinética	cinetic structure	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
125	Textil	textile	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
126	Pintura	painting	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
127	Jardín	garden	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
128	Escultura	sculpture	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
129	Edificio	building	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
130	Instalación	art installation	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
\.


--
-- Data for Name: audiostudio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.audiostudio (id, guidecontent_id, artexhibitionguide_id, info, settings, settings_json, audiospeechmusic, audiospeech, created, lastmodified, lastmodifieduser, audioautogenerate, language, name, state, json_data, artexhibitionguiderecord_id, artexhibitionitemrecord_id, artexhibitionrecord_id, artexhibitionsectionrecord_id, artworkrecord_id, guidecontentrecord_id, institutionrecord_id, personrecord_id, siterecord_id, audio_speech_hash, audio_speech_music_hash, musicurl) FROM stdin;
828	412	\N	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, representa una visión orientalista y teatral de la emperatriz bizantina Teodora, esposa de Justiniano I,  la mujer más influyente y poderosa en la historia del Imperio romano de Oriente.\r\n\r\nNo es una representación histórica, sino más bien una fantasía opulenta basada en la fascinación europea del siglo XIX por el Oriente y el pasado imperial. Benjamin-Constant era un pintor de gran técnica especializado en retratos de élite y figuras históricas: poder, lujo,  sofisticación, poses calculadas,  texturas complejas como seda y terciopelo, y entornos refinados. \r\n\r\nDe origen humilde, Theodora se ganó la vida de joven como actriz y cortesana hasta que fue elegida por Justiniano como esposa. La imagen histórica -basada en Procopio de Cesarea en la antigüedad  y Gibbon en el siglo XVIII-  es  la de una mujer astuta, inteligente y cruel. En la actualidad se considera que ese perfil es exagerado por la visión rígida que se tenía de la función social de la mujer. La visión moderna es que fue una figura política central en el Imperio Bizantino,  co-gobernante de facto con Justiniano. Participó activamente en las decisiones de gobierno. En la revuelta de Niká de 532, convenció a Justiniano de no huir y enfrentar a sus enemigos con su famosa frase -“el púrpura es una buena mortaja”-. Promovió leyes en favor de los derechos de las mujeres: prohibió el tráfico sexual forzado,  fortaleció los derechos de las mujeres en el matrimonio y el divorcio, y fundó hogares para mujeres rescatadas de la prostitución, algo sin precedentes en la época. Como resultado de los esfuerzos de Teodora, el estado de la mujer en el Imperio Bizantino fue más elevado que el del resto de las mujeres en Europa.\r\n\r\nEn la historia argentina el paralelismo inmediato es con Eva Perón.\r\n\r\nBenjamin-Constant se propuso crear un ícono visual del poder femenino y del exotismo oriental. y lo que salió, quizás involuntariamente,  es todo un símbolo de modernidad: una mujer joven, poderosa, hermosa e inteligente con mirada desafiante que se atreve a ser distinta.	\N	\N	\N	\N	2025-11-21 10:15:45.817915-03	2025-11-21 10:15:45.817929-03	100	f	es	La Emperatriz Theodora	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N
805	\N	458	Germán Gárgano es un pintor argentino. Nace en 1953, en Buenos Aires, Argentina. Cursando sus estudios de medicina es detenido por razones políticas en 1975, situación que se prolonga hasta fines de 1982.	\N	\N	818	817	2025-11-13 08:54:20.62889-03	2025-11-19 16:41:25.882375-03	100	f	es	Sendas perdidas	\N	{"end": "30", "music": "", "speed": "10.0", "start": "30", "stability": "10.0", "similarity": "10.0", "fadeDurationSec": "14", "introDurationSec": "28", "voiceOverlapDurationSec": "7"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	1827940477	1818781226	https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3
798	\N	786	La muestra comprende tres décadas atravesados por el nacimiento y auge del fascismo y la  figura de Benito Mussolini, la alianza con la Alemania nazi durante la Segunda Guerra Mundial, y la posguerra.\r\n\r\nEntre 1920 y 1950, el turismo en Italia se transformó en un fenómeno de masas y el afiche fue una herramienta clave:  síntesis entre arte, diseño y promoción cultural, pieza publicitaria que a la vez era obra de arte que reflejaba la cultura y la estética de su tiempo.\r\n\r\nEn la década del 20 se produce una transición desde el eclecticismo, el Liberty o Art Nouveau de principios de siglo para pasar en los 30 a un estilo modernista más sobrio con influencia de vanguardias europeas, como el cubismo, y especialmente el Futurismo italiano, un movimiento artístico fundado por Filippo Tommaso Marinetti  que se caracterizó por la exaltación de la velocidad, la tecnología, la modernidad, la violencia y el dinamismo, rechazando las tradiciones y el pasado.\r\n\r\nDurante el fascismo, estas obras turísticas escaparon al control de la censura, pero no de la influencia de la promoción política. Al igual que en Alemania y la Unión Soviética la vanguardia artística que exaltaba el nuevo hombre resultó demasiado libre; en Italia cedió su lugar al “realismo del fascismo”, un arte similar al realismo soviético, que se llamó “el retorno al orden”. \r\n\r\nLa muestra incluye obras de ilustradores italianos de gran renombre, como Marcello Dudovich y Mario Borgoni, que jugaron un papel crucial en la creación de una identidad turística para el país; y también de Marcello Nizzoli, quién en la postguerra diseñaría las célebres máquinas de escribir portátiles Olivetti.	\N	\N	\N	\N	2025-11-11 21:44:23.570488-03	2025-11-11 21:44:23.570494-03	100	f	es	Viaggio in Italia: 1920-1950, La Edad de Oro del Afiche Turístico Italiano	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	-1	-1	\N
803	\N	799	La muestra “Diseño Infinito” presenta 40 años de producción del reconocido diseñador argentino, con piezas históricas que van desde óleos a diferentes objetos, textiles y mobiliario de autor.	\N	\N	\N	804	2025-11-11 21:52:15.031216-03	2025-11-11 22:00:42.364473-03	100	f	es	Alberto Churba. Diseño Infinito	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	-1	-1	\N
838	\N	202	\N	\N	\N	\N	\N	2025-11-23 21:43:08.719573-03	2025-11-23 21:43:08.719576-03	100	f	es	Obras Maestras	1	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N
848	\N	\N	The painting Theodora, painted by Jean-Joseph Benjamin-Constant in 1887, depicts an Orientalist and theatrical vision of the Byzantine Empress Theodora, wife of Justinian I, the most influential and powerful woman in the history of the Eastern Roman Empire.\n\nIt is not a historical representation, but rather an opulent fantasy based on the 19th-century European fascination with the Orient and the imperial past. Benjamin-Constant was a highly technical painter specializing in portraits of elite figures and historical figures: power, luxury, sophistication, calculated poses, complex textures such as silk and velvet, and refined settings.\n\nOf humble origins, Theodora earned her living as a young woman as an actress and courtesan until she was chosen by Justinian as his wife. The historical image—based on Procopius of Caesarea in antiquity and Gibbon in the 18th century—is that of a cunning, intelligent, and cruel woman. This profile is now considered exaggerated due to the rigid view of women's social role. The modern view is that she was a central political figure in the Byzantine Empire, de facto co-ruler with Justinian. She actively participated in government decisions. In the Nika revolt of 532, she convinced Justinian not to flee and instead confront his enemies with her famous phrase, "purple is a good shroud." She promoted laws favoring women's rights: she prohibited forced sex trafficking, strengthened women's rights in marriage and divorce, and founded homes for women rescued from prostitution, something unprecedented at the time. As a result of Theodora's efforts, the status of women in the Byzantine Empire was higher than that of women in Europe.\n\nIn Argentine history, the immediate parallel is with Eva Perón.\n\nBenjamin-Constant set out to create a visual icon of feminine power and oriental exoticism. And what emerged, perhaps unintentionally, is a symbol of modernity: a young, powerful, beautiful, and intelligent woman with a defiant gaze who dares to be different.	\N	\N	\N	\N	2025-11-27 12:21:28.852456-03	2025-11-27 12:21:28.852476-03	100	f	en	Empress Theodora	\N	\N	\N	\N	\N	\N	\N	746	\N	\N	\N	0	0	\N
810	\N	\N	\N	\N	\N	\N	\N	2025-11-14 09:29:38.536008-03	2025-11-14 09:29:38.536019-03	100	f	en	Sendas perdidas	\N	\N	738	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N
811	\N	\N	\N	\N	\N	\N	\N	2025-11-14 14:58:23.698686-03	2025-11-14 14:58:23.698701-03	100	f	pt-BR	Sendas perdidas	\N	\N	739	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N
849	\N	\N	\N	\N	\N	\N	\N	2025-11-27 12:21:44.343492-03	2025-11-27 12:21:44.343502-03	100	f	pt-BR	La Emperatriz Theodora	\N	\N	\N	\N	\N	\N	\N	747	\N	\N	\N	0	0	\N
854	\N	\N	\N	\N	\N	\N	\N	2025-11-27 17:12:17.145233-03	2025-11-27 17:12:17.14524-03	100	f	pt-BR	Obras Maestras	1	\N	853	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N
825	414	\N	Joaquín Sorolla, el "Maestro de la Luz", inmortalizó la costa de Valencia, especialmente la Playa de la Malvarrosa, capturando la luz mediterránea y escenas costumbristas de pescadores y niños jugando en obras icónicas como "En la costa de Valencia" (1898) y "Corriendo por la playa" (1908), destacando por su estilo luminista, pinceladas vibrantes y la representación de la vida cotidiana valenciana, llena de movimiento y reflejos del sol sobre el mar.	\N	\N	\N	\N	2025-11-20 14:51:23.800806-03	2025-11-20 14:51:23.800812-03	100	f	\N	En la costa de Valencia	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N
796	\N	411	Entre sus múltiples misiones, los museos preservan la memoria de una nación y, al mismo tiempo, como instituciones dinámicas, estimulan la construcción de nuevos sentidos para las piezas que albergan. Los modos en que las obras de una colección se dan a conocer al público moldean la relación que una comunidad entabla con la historia, vínculo que se potencia cuando una institución es permeable a las transformaciones estéticas y sociales que trae cada época.	\N	\N	13112	815	2025-11-11 17:34:51.04344-03	2025-11-17 15:03:29.286601-03	100	f	es	Museo Secreto	\N	{"speed": "10.0", "stability": "10.0", "similarity": "10.0", "fadeDurationSec": "12", "introDurationSec": "25", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	-1771865989	369284446	https://archive.org/download/WolfgangAmadeusMozartPianoSonataInAK311TurkishMarchFromTheTrumanShow/Wolfgang%20Amadeus%20Mozart%20-%20Piano%20Sonata%20in%20A%20K311%2C%20Turkish%20March%20%28From%20The%20Truman%20Show%29.mp3
829	488	\N	Desde el Legado Nicolás García Uriburu trabajamos para preservar y difundir su legado artístico. Custodiamos su colección, gestionamos integralmente su obra y procesamos su archivo documental con el fin de mantener vigente su mensaje y contribuir a la reflexión sobre arte y ecología.	\N	\N	\N	1106	2025-11-21 13:44:33.775451-03	2025-11-21 13:44:33.775524-03	100	f	es	Utopia del Sur	\N	{"speed": "9.0", "stability": "82.0", "similarity": "53.0"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	-767214436	0	\N
916	\N	\N	Wifredo Lam, the most universal of Cuban painters, introduced Black culture into Cuban painting and developed a groundbreaking body of work that integrates elements of African and Chinese origin present in Cuba.\n\nThe painting has been interpreted as the synthesis of an Antillean cycle, by virtue of the dominant Baroque space and the atmosphere created by the association of the human, the animal, the vegetal, and the divine. It contains a visual vocabulary that evolved from academic landscape painting toward a theme and language of modern art. In this oil painting, the painter's visions and experiences seem to merge: the mythical island landscape, the incorporation of content and iconography from the magical-religious systems of African origin prevalent in Cuba and the Caribbean.\n\nIt is considered a synthesis of his political philosophy, where European Surrealism and Cubism are mixed with the power of myth characteristic of the syncretic cults of the Caribbean.	\N	\N	918	22521	2025-11-30 18:30:59.493109-03	2025-11-30 18:30:59.493112-03	100	f	en	The jungle	\N	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "7"}	\N	\N	\N	\N	\N	901	\N	\N	\N	664551921	1589746096	https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3
898	\N	894	Se trata de la retrospectiva más extensa dedicada al artista en Argentina, abarcando las seis décadas de la prolífica carrera de Lam.\r\nLa exposición incluye más de 130 obras de arte de las décadas de 1920 a 1970, incluyendo pinturas, obras a gran escala sobre papel, dibujos colaborativos, libros ilustrados, grabados, cerámicas y material de archivo, con préstamos clave del Estate of Wifredo Lam, París. La retrospectiva revela cómo Lam, un artista nacido en Cuba que pasó la mayor parte de su vida en España, Francia e Italia, llegó a encarnar la figura del artista transnacional en el siglo XX.	\N	\N	911	906	2025-11-29 07:52:34.045037-03	2025-11-29 07:52:34.045048-03	100	f	es	Wifredo Lam, cuando no duermo, sueño	1	{"speed": "97.0", "stability": "82.0", "similarity": "53.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	-1261488223	1589746094	https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3
1105	\N	\N	Joaquín Sorolla, o "Mestre da Luz", imortalizou o litoral de Valência, em especial a Praia de Malvarrosa, capturando a luz do Mediterrâneo e cenas cotidianas de pescadores e crianças brincando em obras icônicas como "Na Costa de Valência" (1898) e "Correndo na Praia" (1908), destacando-se pelo seu estilo luminista, pinceladas vibrantes e a representação da vida diária valenciana, repleta de movimento e reflexos do sol no mar.	\N	\N	\N	\N	2025-12-08 12:15:34.522168-03	2025-12-08 12:15:34.522174-03	100	t	pt-BR	Na costa de Valência	\N	\N	\N	\N	\N	\N	\N	737	\N	\N	\N	0	0	\N
923	\N	\N	\N	\N	\N	\N	\N	2025-12-04 10:01:41.1209-03	2025-12-04 10:01:41.120914-03	100	f	en	Utopia del Sur	\N	\N	\N	\N	\N	\N	\N	922	\N	\N	\N	0	0	\N
1103	\N	\N	Entre suas muitas missões, os museus preservam a memória de uma nação e, ao mesmo tempo, como instituições dinâmicas, estimulam a construção de novos significados para as peças que abrigam. As maneiras pelas quais as obras de uma coleção são apresentadas ao público moldam a relação que uma comunidade estabelece com a história, um vínculo que se fortalece quando uma instituição se mostra aberta às transformações estéticas e sociais que cada época traz.	\N	\N	\N	1104	2025-12-08 12:11:19.198255-03	2025-12-08 12:11:19.198286-03	100	t	pt-BR	Museu Secreto	\N	{"speed": "9.0", "stability": "82.0", "similarity": "53.0"}	735	\N	\N	\N	\N	\N	\N	\N	\N	30043779	0	\N
924	\N	\N	Among their many missions, museums preserve a nation's memory and, at the same time, as dynamic institutions, stimulate the construction of new meanings for the pieces they house. The ways in which the works in a collection are presented to the public shape the relationship a community establishes with history, a bond that is strengthened when an institution is open to the aesthetic and social transformations that each era brings.	\N	\N	\N	1107	2025-12-04 10:06:04.642233-03	2025-12-04 10:06:04.64224-03	100	f	en	Secret Museum	\N	{"speed": "9.0", "stability": "82.0", "similarity": "53.0"}	734	\N	\N	\N	\N	\N	\N	\N	\N	11660576	0	\N
1109	459	\N	\N	\N	\N	\N	\N	2025-12-11 16:22:07.475258-03	2025-12-11 16:22:07.475264-03	100	t	es	Sendas perdidas	1	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N
1110	\N	\N	\N	\N	\N	\N	\N	2025-12-11 16:22:18.373637-03	2025-12-11 16:22:18.373642-03	100	t	en	Sendas perdidas	\N	\N	\N	\N	\N	\N	\N	903	\N	\N	\N	0	0	\N
7329	\N	\N	No Legado Nicolás García Uriburu, trabalhamos para preservar e divulgar seu legado artístico. Salvaguardamos sua coleção, gerenciamos sua obra de forma abrangente e processamos seu arquivo documental para manter sua mensagem relevante e contribuir para a reflexão sobre arte e ecologia.	\N	\N	\N	\N	2025-12-24 17:36:23.625307-03	2025-12-24 17:36:23.625325-03	101	t	pt-BR	Utopia do Sul	\N	\N	\N	\N	\N	\N	\N	1108	\N	\N	\N	0	0	\N
912	\N	\N		\N	\N	914	913	2025-11-30 17:57:53.562704-03	2025-11-30 17:57:53.562711-03	100	f	pt-BR	A selva	\N	{"speed": "9.0", "stability": "82.0", "similarity": "53.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "8"}	\N	\N	\N	\N	\N	902	\N	\N	\N	-2107848187	1589746097	https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3
18635	18156	\N	Las religiones afrocubanas incluyen el Lucumí, o Santería, del pueblo de habla yoruba del suroeste de Nigeria, que es el culto a los orishas o espíritus. También tenemos el Palo, de África Central, las prácticas de Benín llamadas Arará, así como ideas europeas o cristianas.\r\n\r\nEl título de esta pintura, Malembo Sombrío, Dios de la Encrucijada, hace referencia al orisha de la tradición lucumí o santería.\r\nLa palabra "Malembo" en el título podría referirse a un puerto negrero en la costa occidental de África. \r\nPara cuando Lam nació en 1902, solo habían pasado 16 años desde la abolición de la esclavitud en Cuba.\r\n\r\nEn gran parte de la obra de Lam se observa lo que podría identificarse como una figura similar a la de Eleguá: esta pequeña cabeza abovedada. Eleguá es el orisha de la encrucijada. Tiene la capacidad única de abrir puertas, comunicarse entre idiomas y atraer la buena fortuna y alejar la mala suerte.	\N	\N	\N	\N	2026-01-05 10:48:56.418356-03	2026-01-05 10:48:56.418362-03	101	t	es	Le Sombre Malembo, Dieu du carrefour	1	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N
22018	\N	\N	\N	\N	\N	\N	\N	2026-01-06 14:30:46.378032-03	2026-01-06 14:30:46.378062-03	101	t	en	Wilfredo Lan, when I dont sleep, I dream	1	\N	896	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N
915	899	\N	Wifredo Lam, el más universal de los pintores cubanos. Introdujo la cultura negra en la pintura cubana y desarrolló una renovadora obra que integra elementos de origen africano y chino presentes en Cuba. \r\n\r\nEl cuadro ha sido interpretado como la síntesis de un ciclo antillano, en virtud del espacio barroco dominante y de la atmósfera creada por la asociación de lo humano, lo animal, lo vegetal y lo divino. Hay en él un vocabulario visual que evolucionó desde el paisaje de corte académico hacia un tema y un lenguaje de arte moderno. En este óleo parecen fusionarse visiones y vivencias del pintor; el mítico paisaje insular, la incorporación de contenidos e iconografías procedentes de los sistemas mágico-religiosos de origen africano extendidos en Cuba y el Caribe\r\n\r\nSe la considera una síntesis de su política, donde se mezclan surrealismo y cubismo europeos con el poder del mito característico de los cultos sincréticos del Caribe.	\N	\N	22475	22474	2025-11-30 18:24:31.420696-03	2025-11-30 18:24:31.42071-03	100	f	es	La jungla	1	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	-1022410287	1589746094	https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3
34201	\N	\N	A história do Cabildo começou em 1580. Naquele ano, Juan de Garay fundou a cidade de La Trinidad, que mais tarde foi renomeada em homenagem ao seu porto: Santa María del Buen Ayre.\n\nOs Cabildos surgiram na Espanha como uma forma de administração para as cidades e seus arredores. Posteriormente, foram transferidos para as colônias americanas. De acordo com as Leis das Índias, que regulamentavam a vida nos territórios do Império Espanhol, toda cidade era obrigada a ter um Cabildo. Este sempre se localizava na praça principal, próximo a outras instituições importantes, como a igreja principal. Em Buenos Aires, também abrigava a cadeia da cidade.\n\nO Cabildo era a única autoridade eleita pela comunidade local. Vice-reis, governadores e outros funcionários importantes eram nomeados pela Espanha. Em contrapartida, os membros do Cabildo representavam os habitantes de Buenos Aires. Somente os chamados "vecinos" (homens brancos e de posição social elevada) podiam ser membros. Os membros do Cabildo se reuniam regularmente para discutir assuntos importantes para a comunidade.\n\nNo final do século XVIII, Buenos Aires deixou de ser uma cidade marginal do Império Espanhol para se tornar a capital do Vice-Reino do Rio da Prata. Sua crescente importância fez com que o Cabildo (conselho municipal) assumisse cada vez mais poderes.\n\nO Cabildo desempenhou um papel político fundamental nos anos que se seguiram à Revolução de 1810. Em 1821, foi dissolvido. A partir de então, a organização da cidade ficou a cargo da Assembleia Legislativa de Buenos Aires, criada um ano antes.	\N	\N	34208	34207	2026-01-11 16:23:00.873482-03	2026-01-11 16:23:00.873485-03	101	t	pt-BR	Câmara Municipal de Buenos Aires	1	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	33828	\N	\N	\N	\N	\N	\N	\N	\N	-1357823488	181369522	https://archive.org/download/dm-2530-247/DM%202530%20247%E2%80%A2%281%29%20Albinoni%20T.%2C%20Adagio%20en%20sol%20mineur.mp3
34074	\N	33825	La historia del Cabildo comenzó en 1580. Ese año, Juan de Garay fundó la ciudad de La Trinidad, que más tarde pasó a llamarse por el nombre de su puerto: Santa María del Buen Ayre.\r\n\r\nLos cabildos se originaron en España como forma de administración de las ciudades y sus alrededores. Luego fueron trasladados a los dominios americanos. Según las Leyes de Indias, que regulaban la vida en los territorios del Imperio español, toda ciudad debía contar con un Cabildo. Este se ubicaba siempre en la plaza principal, junto a otras instituciones importantes, como la iglesia mayor. En Buenos Aires contenía, además, la cárcel urbana.\r\n\r\nEl Cabildo era la única autoridad elegida por la sociedad local. Los virreyes, los gobernadores y otros funcionarios importantes eran nombrados desde España. En cambio, los miembros del Cabildo representaban a los habitantes de Buenos Aires. Solo los llamados "vecinos" (varones, blancos y con prestigio social) podían integrarlo. Los cabildantes se reunían periódicamente para discutir asuntos importantes para la comunidad.\r\n\r\nHacia fines del siglo XVIII, Buenos Aires pasó de ser una ciudad marginal del Imperio español a convertirse en la capital del Virreinato del Río de la Plata. Su importancia creciente implicó que el Cabildo asumiera cada vez más facultades.\r\n\r\nEl Cabildo tuvo un papel político fundamental en los años posteriores a la revolución de 1810. En 1821, fue disuelto. Desde entonces, la organización de la ciudad quedó en manos de la Legislatura de Buenos Aires, creada un año antes.	\N	\N	34122	34080	2026-01-11 16:13:00.089788-03	2026-01-11 16:13:00.089793-03	101	t	es	El Cabildo de Buenos Aires	1	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	784487367	1466060882	https://dn721806.ca.archive.org/0/items/dm-2530-247/DM%202530%20247%E2%80%A2%282%29%20Pachebel%20J.%2C%20Canon%20et%20gigue%20en%20r%C3%A9%20mineur%20pour%203%20violons%20%26%20bc.mp3
34158	\N	\N	The history of the Cabildo began in 1580. That year, Juan de Garay founded the city of La Trinidad, which was later renamed after its port: Santa María del Buen Ayre.\n\nCabildos originated in Spain as a form of administration for cities and their surrounding areas. They were later transferred to the American colonies. According to the Laws of the Indies, which regulated life in the territories of the Spanish Empire, every city was required to have a Cabildo. This was always located in the main square, next to other important institutions, such as the main church. In Buenos Aires, it also housed the city jail.\n\nThe Cabildo was the only authority elected by the local community. Viceroys, governors, and other important officials were appointed from Spain. In contrast, the members of the Cabildo represented the inhabitants of Buenos Aires. Only those called "vecinos" (male, white, and of social standing) could be members. The Cabildo members met regularly to discuss matters of importance to the community.\n\nTowards the end of the 18th century, Buenos Aires went from being a marginal city of the Spanish Empire to becoming the capital of the Viceroyalty of the Río de la Plata. Its growing importance meant that the Cabildo (city council) assumed increasingly more powers.\n\nThe Cabildo played a fundamental political role in the years following the 1810 revolution. In 1821, it was dissolved. From then on, the organization of the city was in the hands of the Buenos Aires Legislature, created a year earlier.	\N	\N	34165	34164	2026-01-11 16:21:31.477383-03	2026-01-11 16:21:31.477389-03	101	t	en	The Buenos Aires City Council	1	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	33827	\N	\N	\N	\N	\N	\N	\N	\N	816563443	181369522	https://archive.org/download/dm-2530-247/DM%202530%20247%E2%80%A2%281%29%20Albinoni%20T.%2C%20Adagio%20en%20sol%20mineur.mp3
35496	35357	\N	El alférez real era uno de los principales funcionarios del Cabildo. Se encargaba de representar al rey en las ceremonias públicas portando el estandarte real. Además, se ocupaba de organizar las milicias en tiempos de guerra.\r\n\r\nEn las ceremonias públicas, el alférez real lucía un traje como este. La recreación fue realizada a partir del traje que perteneció a Francisco Antonio de Escalada, último alférez real del Cabildo de Buenos Aires, que forma parte de la colección del Museo. Está formado por calzón corto, chaleco y casaca. Se usaba con camisa blanca, medias del mismo color y zapatos con taco alto y grandes hebillas. En la época, este tipo de vestimenta era un signo de prestigio: contar con un traje como este era muy costoso.\r\n\r\nEsta recreación se realizó para proteger la pieza original de la exposición constante. Para replicar las medidas, se levantaron moldes a partir del traje original. \r\nSe seleccionó una tela similar y se la tiñó buscando una aproximación al color que tenía el traje en el momento de su uso.	\N	\N	35503	35502	2026-01-12 10:42:25.409476-03	2026-01-12 10:42:25.409486-03	101	t	es	Traje de calle del alférez real	1	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	1703197939	1466060882	https://dn721806.ca.archive.org/0/items/dm-2530-247/DM%202530%20247%E2%80%A2%282%29%20Pachebel%20J.%2C%20Canon%20et%20gigue%20en%20r%C3%A9%20mineur%20pour%203%20violons%20%26%20bc.mp3
\.


--
-- Data for Name: audit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.audit (id, lastmodified, lastmodifieduser, action, objectclassname, objectid, description, json_data, descriptionkey) FROM stdin;
120	2025-11-26 09:32:19.863527-03	100	0	institution	135	\N	\N	\N
121	2025-11-26 09:32:19.877223-03	100	0	institution	136	\N	\N	\N
122	2025-11-26 09:32:19.880473-03	100	0	institution	341	\N	\N	\N
123	2025-11-26 09:32:19.883491-03	100	0	institution	553	\N	\N	\N
124	2025-11-26 09:32:19.886641-03	100	0	institution	821	\N	\N	\N
125	2025-11-26 09:32:19.889596-03	100	0	institution	561	\N	\N	\N
126	2025-11-26 09:32:19.89227-03	100	0	institution	749	\N	\N	\N
127	2025-11-26 09:32:19.894908-03	100	0	institution	547	\N	\N	\N
128	2025-11-26 09:32:19.897504-03	100	0	institution	529	\N	\N	\N
129	2025-11-26 09:49:24.045273-03	100	0	site	552	\N	\N	\N
130	2025-11-26 09:49:24.05676-03	100	0	site	564	\N	\N	\N
131	2025-11-26 09:49:24.059483-03	100	0	site	137	\N	\N	\N
132	2025-11-26 09:49:24.062325-03	100	0	site	342	\N	\N	\N
133	2025-11-26 09:49:24.064875-03	100	0	site	555	\N	\N	\N
134	2025-11-26 09:49:24.067639-03	100	0	site	537	\N	\N	\N
135	2025-11-26 09:49:24.07003-03	100	0	site	177	\N	\N	\N
136	2025-11-26 09:49:24.072232-03	100	0	site	753	\N	\N	\N
137	2025-11-26 09:49:24.07974-03	100	0	siterecord	718	\N	\N	\N
138	2025-11-26 09:49:24.082078-03	100	0	siterecord	716	\N	\N	\N
139	2025-11-26 09:49:24.084145-03	100	0	siterecord	732	\N	\N	\N
140	2025-11-26 09:49:24.086096-03	100	0	siterecord	733	\N	\N	\N
141	2025-11-26 09:49:24.087896-03	100	0	siterecord	755	\N	\N	\N
142	2025-11-26 09:49:24.089642-03	100	0	siterecord	717	\N	\N	\N
143	2025-11-26 09:49:24.091518-03	100	0	siterecord	754	\N	\N	\N
144	2025-11-26 09:49:24.099388-03	100	0	artexhibition	362	\N	\N	\N
145	2025-11-26 09:49:24.101475-03	100	0	artexhibition	233	\N	\N	\N
146	2025-11-26 09:49:24.103743-03	100	0	artexhibition	199	\N	\N	\N
147	2025-11-26 09:49:24.105623-03	100	0	artexhibition	284	\N	\N	\N
148	2025-11-26 09:49:24.107364-03	100	0	artexhibition	600	\N	\N	\N
149	2025-11-26 09:49:24.109229-03	100	0	artexhibition	454	\N	\N	\N
150	2025-11-26 09:49:24.110877-03	100	0	artexhibition	597	\N	\N	\N
151	2025-11-26 09:49:24.112691-03	100	0	artexhibition	774	\N	\N	\N
152	2025-11-26 09:49:24.114613-03	100	0	artexhibition	757	\N	\N	\N
153	2025-11-26 09:49:24.116508-03	100	0	artexhibition	839	\N	\N	\N
154	2025-11-26 09:49:24.118328-03	100	0	artexhibition	830	\N	\N	\N
155	2025-11-26 09:49:24.124739-03	100	0	artexhibitionguide	202	\N	\N	\N
156	2025-11-26 09:49:24.126733-03	100	0	artexhibitionguide	292	\N	\N	\N
157	2025-11-26 09:49:24.128832-03	100	0	artexhibitionguide	624	\N	\N	\N
158	2025-11-26 09:49:24.13099-03	100	0	artexhibitionguide	458	\N	\N	\N
159	2025-11-26 09:49:24.132862-03	100	0	artexhibitionguide	248	\N	\N	\N
160	2025-11-26 09:49:24.134596-03	100	0	artexhibitionguide	786	\N	\N	\N
161	2025-11-26 09:49:24.136293-03	100	0	artexhibitionguide	799	\N	\N	\N
162	2025-11-26 09:49:24.138183-03	100	0	artexhibitionguide	411	\N	\N	\N
163	2025-11-26 09:49:24.145172-03	100	0	artexhibitionguiderecord	734	\N	\N	\N
164	2025-11-26 09:49:24.14713-03	100	0	artexhibitionguiderecord	735	\N	\N	\N
165	2025-11-26 09:49:24.149037-03	100	0	artexhibitionguiderecord	738	\N	\N	\N
166	2025-11-26 09:49:24.151077-03	100	0	artexhibitionguiderecord	739	\N	\N	\N
167	2025-11-26 09:49:24.15283-03	100	0	artexhibitionguiderecord	787	\N	\N	\N
168	2025-11-26 09:49:24.154568-03	100	0	artexhibitionguiderecord	789	\N	\N	\N
169	2025-11-26 09:49:24.156597-03	100	0	artexhibitionguiderecord	800	\N	\N	\N
170	2025-11-26 09:49:24.158777-03	100	0	artexhibitionguiderecord	801	\N	\N	\N
171	2025-11-26 09:49:24.160555-03	100	0	artexhibitionguiderecord	788	\N	\N	\N
172	2025-11-26 09:49:24.162281-03	100	0	artexhibitionguiderecord	802	\N	\N	\N
173	2025-11-26 09:49:24.166641-03	100	0	user	100	\N	\N	\N
174	2025-11-26 09:49:24.168358-03	100	0	user	101	\N	\N	\N
175	2025-11-26 09:49:24.178058-03	100	0	person	194	\N	\N	\N
176	2025-11-26 09:49:24.180228-03	100	0	person	188	\N	\N	\N
177	2025-11-26 09:49:24.182157-03	100	0	person	182	\N	\N	\N
178	2025-11-26 09:49:24.184402-03	100	0	person	183	\N	\N	\N
179	2025-11-26 09:49:24.186287-03	100	0	person	184	\N	\N	\N
180	2025-11-26 09:49:24.188111-03	100	0	person	185	\N	\N	\N
181	2025-11-26 09:49:24.189853-03	100	0	person	186	\N	\N	\N
182	2025-11-26 09:49:24.191638-03	100	0	person	187	\N	\N	\N
183	2025-11-26 09:49:24.1937-03	100	0	person	189	\N	\N	\N
184	2025-11-26 09:49:24.195556-03	100	0	person	190	\N	\N	\N
185	2025-11-26 09:49:24.197325-03	100	0	person	191	\N	\N	\N
186	2025-11-26 09:49:24.199152-03	100	0	person	192	\N	\N	\N
187	2025-11-26 09:49:24.201004-03	100	0	person	193	\N	\N	\N
188	2025-11-26 09:49:24.202893-03	100	0	person	181	\N	\N	\N
189	2025-11-26 09:49:24.204746-03	100	0	person	263	\N	\N	\N
190	2025-11-26 09:49:24.206504-03	100	0	person	264	\N	\N	\N
191	2025-11-26 09:49:24.208216-03	100	0	person	265	\N	\N	\N
192	2025-11-26 09:49:24.209987-03	100	0	person	266	\N	\N	\N
193	2025-11-26 09:49:24.211753-03	100	0	person	267	\N	\N	\N
194	2025-11-26 09:49:24.213491-03	100	0	person	268	\N	\N	\N
195	2025-11-26 09:49:24.215222-03	100	0	person	269	\N	\N	\N
196	2025-11-26 09:49:24.216912-03	100	0	person	308	\N	\N	\N
197	2025-11-26 09:49:24.218564-03	100	0	person	333	\N	\N	\N
198	2025-11-26 09:49:24.220703-03	100	0	person	180	\N	\N	\N
199	2025-11-26 09:49:24.222633-03	100	0	person	398	\N	\N	\N
200	2025-11-26 09:49:24.224665-03	100	0	person	399	\N	\N	\N
201	2025-11-26 09:49:24.226474-03	100	0	person	400	\N	\N	\N
202	2025-11-26 09:49:24.228255-03	100	0	person	447	\N	\N	\N
203	2025-11-26 09:49:24.22999-03	100	0	person	401	\N	\N	\N
204	2025-11-26 09:49:24.231779-03	100	0	person	480	\N	\N	\N
205	2025-11-26 09:49:24.23353-03	100	0	person	588	\N	\N	\N
206	2025-11-26 09:49:24.235283-03	100	0	person	593	\N	\N	\N
207	2025-11-26 09:49:24.237157-03	100	0	person	581	\N	\N	\N
208	2025-11-26 09:49:24.239009-03	100	0	person	602	\N	\N	\N
209	2025-11-26 09:49:24.240783-03	100	0	person	481	\N	\N	\N
210	2025-11-26 09:49:24.245856-03	100	0	person	763	\N	\N	\N
211	2025-11-26 09:49:24.260061-03	100	0	resource	813	\N	\N	\N
212	2025-11-26 09:49:24.26313-03	100	0	resource	844	\N	\N	\N
213	2025-11-26 09:49:24.265567-03	100	0	resource	423	\N	\N	\N
214	2025-11-26 09:49:24.267737-03	100	0	resource	636	\N	\N	\N
215	2025-11-26 09:49:24.269886-03	100	0	resource	368	\N	\N	\N
216	2025-11-26 09:49:24.271914-03	100	0	resource	419	\N	\N	\N
217	2025-11-26 09:49:24.273685-03	100	0	resource	797	\N	\N	\N
218	2025-11-26 09:49:24.275427-03	100	0	resource	806	\N	\N	\N
219	2025-11-26 09:49:24.277302-03	100	0	resource	758	\N	\N	\N
220	2025-11-26 09:49:24.279215-03	100	0	resource	565	\N	\N	\N
221	2025-11-26 09:49:24.280997-03	100	0	resource	590	\N	\N	\N
222	2025-11-26 09:49:24.282743-03	100	0	resource	814	\N	\N	\N
223	2025-11-26 09:49:24.284681-03	100	0	resource	422	\N	\N	\N
224	2025-11-26 09:49:24.286478-03	100	0	resource	336	\N	\N	\N
225	2025-11-26 09:49:24.288255-03	100	0	resource	421	\N	\N	\N
226	2025-11-26 09:49:24.289924-03	100	0	resource	424	\N	\N	\N
227	2025-11-26 09:49:24.291585-03	100	0	resource	418	\N	\N	\N
228	2025-11-26 09:49:24.293271-03	100	0	resource	420	\N	\N	\N
229	2025-11-26 09:49:24.294975-03	100	0	resource	807	\N	\N	\N
230	2025-11-26 09:49:24.296676-03	100	0	resource	762	\N	\N	\N
231	2025-11-26 09:49:24.298305-03	100	0	resource	641	\N	\N	\N
232	2025-11-26 09:49:24.299924-03	100	0	resource	642	\N	\N	\N
233	2025-11-26 09:49:24.301658-03	100	0	resource	316	\N	\N	\N
234	2025-11-26 09:49:24.303367-03	100	0	resource	750	\N	\N	\N
235	2025-11-26 09:49:24.305224-03	100	0	resource	560	\N	\N	\N
236	2025-11-26 09:49:24.306909-03	100	0	resource	562	\N	\N	\N
237	2025-11-26 09:49:24.308687-03	100	0	resource	554	\N	\N	\N
238	2025-11-26 09:49:24.310508-03	100	0	resource	583	\N	\N	\N
239	2025-11-26 09:49:24.312301-03	100	0	resource	586	\N	\N	\N
240	2025-11-26 09:49:24.313988-03	100	0	resource	778	\N	\N	\N
241	2025-11-26 09:49:24.315646-03	100	0	resource	815	\N	\N	\N
242	2025-11-26 09:49:24.317364-03	100	0	resource	804	\N	\N	\N
243	2025-11-26 09:49:24.319292-03	100	0	resource	535	\N	\N	\N
244	2025-11-26 09:49:24.321032-03	100	0	resource	317	\N	\N	\N
245	2025-11-26 09:49:24.32278-03	100	0	resource	321	\N	\N	\N
246	2025-11-26 09:49:24.324634-03	100	0	resource	319	\N	\N	\N
247	2025-11-26 09:49:24.327071-03	100	0	resource	808	\N	\N	\N
248	2025-11-26 09:49:24.329069-03	100	0	resource	816	\N	\N	\N
249	2025-11-26 09:49:24.330879-03	100	0	resource	809	\N	\N	\N
250	2025-11-26 09:49:24.332661-03	100	0	resource	558	\N	\N	\N
251	2025-11-26 09:49:24.334399-03	100	0	resource	559	\N	\N	\N
252	2025-11-26 09:49:24.336371-03	100	0	resource	620	\N	\N	\N
253	2025-11-26 09:49:24.337957-03	100	0	resource	640	\N	\N	\N
254	2025-11-26 09:49:24.33955-03	100	0	resource	563	\N	\N	\N
255	2025-11-26 09:49:24.341148-03	100	0	resource	779	\N	\N	\N
256	2025-11-26 09:49:24.342973-03	100	0	resource	780	\N	\N	\N
257	2025-11-26 09:49:24.344856-03	100	0	resource	531	\N	\N	\N
258	2025-11-26 09:49:24.346547-03	100	0	resource	306	\N	\N	\N
259	2025-11-26 09:49:24.348126-03	100	0	resource	491	\N	\N	\N
260	2025-11-26 09:49:24.349527-03	100	0	resource	463	\N	\N	\N
261	2025-11-26 09:49:24.35107-03	100	0	resource	301	\N	\N	\N
262	2025-11-26 09:49:24.352721-03	100	0	resource	817	\N	\N	\N
263	2025-11-26 09:49:24.354303-03	100	0	resource	812	\N	\N	\N
264	2025-11-26 09:49:24.355868-03	100	0	resource	790	\N	\N	\N
265	2025-11-26 09:49:24.357402-03	100	0	resource	576	\N	\N	\N
266	2025-11-26 09:49:24.358959-03	100	0	resource	598	\N	\N	\N
267	2025-11-26 09:49:24.360484-03	100	0	resource	309	\N	\N	\N
268	2025-11-26 09:49:24.361925-03	100	0	resource	534	\N	\N	\N
269	2025-11-26 09:49:24.363573-03	100	0	resource	312	\N	\N	\N
270	2025-11-26 09:49:24.365088-03	100	0	resource	331	\N	\N	\N
271	2025-11-26 09:49:24.366438-03	100	0	resource	357	\N	\N	\N
272	2025-11-26 09:49:24.367859-03	100	0	resource	358	\N	\N	\N
273	2025-11-26 09:49:24.369158-03	100	0	resource	359	\N	\N	\N
274	2025-11-26 09:49:24.370421-03	100	0	resource	363	\N	\N	\N
275	2025-11-26 09:49:24.371713-03	100	0	resource	367	\N	\N	\N
276	2025-11-26 09:49:24.373107-03	100	0	resource	416	\N	\N	\N
277	2025-11-26 09:49:24.374569-03	100	0	resource	365	\N	\N	\N
278	2025-11-26 09:49:24.375979-03	100	0	resource	415	\N	\N	\N
279	2025-11-26 09:49:24.377682-03	100	0	resource	417	\N	\N	\N
280	2025-11-26 09:49:24.379022-03	100	0	resource	464	\N	\N	\N
281	2025-11-26 09:49:24.380241-03	100	0	resource	601	\N	\N	\N
282	2025-11-26 09:49:24.381543-03	100	0	resource	320	\N	\N	\N
283	2025-11-26 09:49:24.382889-03	100	0	resource	772	\N	\N	\N
284	2025-11-26 09:49:24.384358-03	100	0	resource	759	\N	\N	\N
285	2025-11-26 09:49:24.385946-03	100	0	resource	751	\N	\N	\N
286	2025-11-26 09:49:24.387734-03	100	0	resource	756	\N	\N	\N
287	2025-11-26 09:49:24.389215-03	100	0	resource	818	\N	\N	\N
288	2025-11-26 09:49:24.390532-03	100	0	resource	462	\N	\N	\N
289	2025-11-26 09:49:24.391955-03	100	0	resource	568	\N	\N	\N
290	2025-11-26 09:49:24.393485-03	100	0	resource	538	\N	\N	\N
291	2025-11-26 09:49:24.394894-03	100	0	resource	536	\N	\N	\N
292	2025-11-26 09:49:24.396262-03	100	0	resource	638	\N	\N	\N
293	2025-11-26 09:49:24.39798-03	100	0	resource	300	\N	\N	\N
294	2025-11-26 09:49:24.399354-03	100	0	resource	490	\N	\N	\N
295	2025-11-26 09:49:24.400606-03	100	0	resource	329	\N	\N	\N
296	2025-11-26 09:49:24.40182-03	100	0	resource	530	\N	\N	\N
297	2025-11-26 09:49:24.403333-03	100	0	resource	532	\N	\N	\N
298	2025-11-26 09:49:24.40484-03	100	0	resource	594	\N	\N	\N
299	2025-11-26 09:49:24.406868-03	100	0	resource	528	\N	\N	\N
300	2025-11-26 09:49:24.408835-03	100	0	resource	604	\N	\N	\N
301	2025-11-26 09:49:24.410769-03	100	0	resource	752	\N	\N	\N
302	2025-11-26 09:49:24.412025-03	100	0	resource	305	\N	\N	\N
303	2025-11-26 09:49:24.413285-03	100	0	resource	315	\N	\N	\N
304	2025-11-26 09:49:24.414471-03	100	0	resource	318	\N	\N	\N
305	2025-11-26 09:49:24.415581-03	100	0	resource	302	\N	\N	\N
306	2025-11-26 09:49:24.416819-03	100	0	resource	314	\N	\N	\N
307	2025-11-26 09:49:24.418123-03	100	0	resource	466	\N	\N	\N
308	2025-11-26 09:49:24.419341-03	100	0	resource	303	\N	\N	\N
309	2025-11-26 09:49:24.420522-03	100	0	resource	304	\N	\N	\N
310	2025-11-26 09:49:24.421932-03	100	0	resource	639	\N	\N	\N
311	2025-11-26 09:49:24.42329-03	100	0	resource	548	\N	\N	\N
312	2025-11-26 09:49:24.424639-03	100	0	resource	533	\N	\N	\N
313	2025-11-26 09:49:24.425861-03	100	0	resource	364	\N	\N	\N
314	2025-11-26 09:49:24.427059-03	100	0	resource	366	\N	\N	\N
315	2025-11-26 09:49:24.428488-03	100	0	resource	465	\N	\N	\N
316	2025-11-26 09:49:24.429865-03	100	0	resource	843	\N	\N	\N
317	2025-11-26 09:49:24.431155-03	100	0	resource	556	\N	\N	\N
318	2025-11-26 09:49:24.432586-03	100	0	resource	557	\N	\N	\N
319	2025-11-26 09:49:24.433983-03	100	0	resource	781	\N	\N	\N
320	2025-11-26 09:49:24.435458-03	100	0	resource	643	\N	\N	\N
321	2025-11-26 09:49:24.436976-03	100	0	resource	791	\N	\N	\N
322	2025-11-26 09:49:24.438408-03	100	0	resource	425	\N	\N	\N
323	2025-11-26 09:49:24.43987-03	100	0	resource	338	\N	\N	\N
324	2025-11-26 09:49:24.441264-03	100	0	resource	426	\N	\N	\N
325	2025-11-26 09:49:24.442598-03	100	0	resource	337	\N	\N	\N
326	2025-11-26 09:49:24.444096-03	100	0	resource	637	\N	\N	\N
327	2025-11-26 09:49:24.445713-03	100	0	resource	599	\N	\N	\N
328	2025-11-26 09:49:24.447346-03	100	0	resource	323	\N	\N	\N
329	2025-11-26 09:49:24.449015-03	100	0	resource	334	\N	\N	\N
330	2025-11-26 09:49:24.450561-03	100	0	resource	322	\N	\N	\N
331	2025-11-26 09:49:24.451993-03	100	0	resource	327	\N	\N	\N
332	2025-11-26 09:49:24.453405-03	100	0	resource	325	\N	\N	\N
333	2025-11-26 09:49:24.45486-03	100	0	resource	792	\N	\N	\N
334	2025-11-26 09:49:24.467075-03	100	0	artwork	225	\N	\N	\N
335	2025-11-26 09:49:24.469356-03	100	0	artwork	585	\N	\N	\N
336	2025-11-26 09:49:24.471174-03	100	0	artwork	404	\N	\N	\N
337	2025-11-26 09:49:24.472904-03	100	0	artwork	229	\N	\N	\N
338	2025-11-26 09:49:24.474461-03	100	0	artwork	406	\N	\N	\N
339	2025-11-26 09:49:24.475947-03	100	0	artwork	217	\N	\N	\N
340	2025-11-26 09:49:24.4775-03	100	0	artwork	484	\N	\N	\N
341	2025-11-26 09:49:24.479188-03	100	0	artwork	276	\N	\N	\N
342	2025-11-26 09:49:24.480794-03	100	0	artwork	592	\N	\N	\N
343	2025-11-26 09:49:24.482362-03	100	0	artwork	219	\N	\N	\N
344	2025-11-26 09:49:24.484012-03	100	0	artwork	197	\N	\N	\N
345	2025-11-26 09:49:24.48563-03	100	0	artwork	221	\N	\N	\N
346	2025-11-26 09:49:24.487206-03	100	0	artwork	278	\N	\N	\N
347	2025-11-26 09:49:24.488762-03	100	0	artwork	448	\N	\N	\N
348	2025-11-26 09:49:24.490233-03	100	0	artwork	328	\N	\N	\N
349	2025-11-26 09:49:24.491741-03	100	0	artwork	311	\N	\N	\N
350	2025-11-26 09:49:24.493276-03	100	0	artwork	195	\N	\N	\N
351	2025-11-26 09:49:24.495009-03	100	0	artwork	213	\N	\N	\N
352	2025-11-26 09:49:24.49659-03	100	0	artwork	231	\N	\N	\N
353	2025-11-26 09:49:24.498045-03	100	0	artwork	619	\N	\N	\N
354	2025-11-26 09:49:24.499485-03	100	0	artwork	575	\N	\N	\N
355	2025-11-26 09:49:24.50101-03	100	0	artwork	274	\N	\N	\N
356	2025-11-26 09:49:24.502545-03	100	0	artwork	332	\N	\N	\N
357	2025-11-26 09:49:24.50408-03	100	0	artwork	482	\N	\N	\N
358	2025-11-26 09:49:24.505627-03	100	0	artwork	768	\N	\N	\N
359	2025-11-26 09:49:24.507111-03	100	0	artwork	582	\N	\N	\N
360	2025-11-26 09:49:24.508571-03	100	0	artwork	215	\N	\N	\N
361	2025-11-26 09:49:24.509955-03	100	0	artwork	270	\N	\N	\N
362	2025-11-26 09:49:24.511411-03	100	0	artwork	209	\N	\N	\N
363	2025-11-26 09:49:24.51284-03	100	0	artwork	223	\N	\N	\N
364	2025-11-26 09:49:24.514235-03	100	0	artwork	207	\N	\N	\N
365	2025-11-26 09:49:24.515607-03	100	0	artwork	324	\N	\N	\N
366	2025-11-26 09:49:24.51699-03	100	0	artwork	272	\N	\N	\N
367	2025-11-26 09:49:24.5185-03	100	0	artwork	282	\N	\N	\N
368	2025-11-26 09:49:24.519999-03	100	0	artwork	589	\N	\N	\N
369	2025-11-26 09:49:24.521406-03	100	0	artwork	452	\N	\N	\N
370	2025-11-26 09:49:24.522806-03	100	0	artwork	205	\N	\N	\N
371	2025-11-26 09:49:24.524167-03	100	0	artwork	402	\N	\N	\N
372	2025-11-26 09:49:24.525583-03	100	0	artwork	603	\N	\N	\N
373	2025-11-26 09:49:24.527568-03	100	0	artwork	211	\N	\N	\N
374	2025-11-26 09:49:24.529618-03	100	0	artwork	280	\N	\N	\N
375	2025-11-26 09:49:24.531061-03	100	0	artwork	450	\N	\N	\N
376	2025-11-26 09:50:09.462525-03	100	0	artworkrecord	693	\N	\N	\N
377	2025-11-26 09:50:09.475204-03	100	0	artworkrecord	694	\N	\N	\N
378	2025-11-26 09:50:09.477447-03	100	0	artworkrecord	696	\N	\N	\N
379	2025-11-26 09:50:09.47979-03	100	0	artworkrecord	695	\N	\N	\N
380	2025-11-26 09:50:09.48179-03	100	0	artworkrecord	743	\N	\N	\N
381	2025-11-26 09:50:09.483434-03	100	0	artworkrecord	701	\N	\N	\N
382	2025-11-26 09:50:09.484982-03	100	0	artworkrecord	729	\N	\N	\N
383	2025-11-26 09:50:09.486521-03	100	0	artworkrecord	769	\N	\N	\N
384	2025-11-26 09:50:09.488054-03	100	0	artworkrecord	770	\N	\N	\N
385	2025-11-26 09:50:09.489472-03	100	0	artworkrecord	771	\N	\N	\N
386	2025-11-26 09:50:09.490943-03	100	0	artworkrecord	731	\N	\N	\N
387	2025-11-26 09:50:09.492329-03	100	0	artworkrecord	709	\N	\N	\N
388	2025-11-26 09:50:09.493809-03	100	0	artworkrecord	710	\N	\N	\N
389	2025-11-26 09:50:09.495415-03	100	0	artworkrecord	711	\N	\N	\N
390	2025-11-26 09:50:09.497243-03	100	0	artworkrecord	712	\N	\N	\N
391	2025-11-26 09:50:09.498802-03	100	0	artworkrecord	713	\N	\N	\N
392	2025-11-26 09:50:09.500381-03	100	0	artworkrecord	714	\N	\N	\N
393	2025-11-26 09:50:09.502082-03	100	0	artworkrecord	715	\N	\N	\N
394	2025-11-26 09:50:09.503587-03	100	0	artworkrecord	730	\N	\N	\N
395	2025-11-26 09:50:09.510604-03	100	0	artexhibitionitem	244	\N	\N	\N
396	2025-11-26 09:50:09.51234-03	100	0	artexhibitionitem	782	\N	\N	\N
397	2025-11-26 09:50:09.514046-03	100	0	artexhibitionitem	834	\N	\N	\N
398	2025-11-26 09:50:09.515645-03	100	0	artexhibitionitem	239	\N	\N	\N
399	2025-11-26 09:50:09.517174-03	100	0	artexhibitionitem	634	\N	\N	\N
400	2025-11-26 09:50:09.51886-03	100	0	artexhibitionitem	633	\N	\N	\N
401	2025-11-26 09:50:09.520499-03	100	0	artexhibitionitem	615	\N	\N	\N
402	2025-11-26 09:50:09.522108-03	100	0	artexhibitionitem	408	\N	\N	\N
403	2025-11-26 09:50:09.523555-03	100	0	artexhibitionitem	487	\N	\N	\N
404	2025-11-26 09:50:09.525045-03	100	0	artexhibitionitem	409	\N	\N	\N
405	2025-11-26 09:50:09.526641-03	100	0	artexhibitionitem	456	\N	\N	\N
406	2025-11-26 09:50:09.528174-03	100	0	artexhibitionitem	457	\N	\N	\N
407	2025-11-26 09:50:09.529621-03	100	0	artexhibitionitem	455	\N	\N	\N
408	2025-11-26 09:50:09.531165-03	100	0	artexhibitionitem	238	\N	\N	\N
409	2025-11-26 09:50:09.53264-03	100	0	artexhibitionitem	237	\N	\N	\N
410	2025-11-26 09:50:09.534197-03	100	0	artexhibitionitem	242	\N	\N	\N
411	2025-11-26 09:50:09.535802-03	100	0	artexhibitionitem	201	\N	\N	\N
412	2025-11-26 09:50:09.537467-03	100	0	artexhibitionitem	410	\N	\N	\N
413	2025-11-26 09:50:09.539368-03	100	0	artexhibitionitem	616	\N	\N	\N
414	2025-11-26 09:50:09.541112-03	100	0	artexhibitionitem	486	\N	\N	\N
415	2025-11-26 09:50:09.542672-03	100	0	artexhibitionitem	617	\N	\N	\N
416	2025-11-26 09:50:09.544165-03	100	0	artexhibitionitem	246	\N	\N	\N
417	2025-11-26 09:50:09.545636-03	100	0	artexhibitionitem	234	\N	\N	\N
418	2025-11-26 09:50:09.547153-03	100	0	artexhibitionitem	241	\N	\N	\N
419	2025-11-26 09:50:09.54877-03	100	0	artexhibitionitem	247	\N	\N	\N
420	2025-11-26 09:50:09.550233-03	100	0	artexhibitionitem	618	\N	\N	\N
421	2025-11-26 09:50:09.551672-03	100	0	artexhibitionitem	627	\N	\N	\N
422	2025-11-26 09:50:09.553146-03	100	0	artexhibitionitem	243	\N	\N	\N
423	2025-11-26 09:50:09.554587-03	100	0	artexhibitionitem	623	\N	\N	\N
424	2025-11-26 09:50:09.556226-03	100	0	artexhibitionitem	635	\N	\N	\N
425	2025-11-26 09:50:09.55773-03	100	0	artexhibitionitem	291	\N	\N	\N
426	2025-11-26 09:50:09.559221-03	100	0	artexhibitionitem	289	\N	\N	\N
427	2025-11-26 09:50:09.560644-03	100	0	artexhibitionitem	286	\N	\N	\N
428	2025-11-26 09:50:09.562046-03	100	0	artexhibitionitem	288	\N	\N	\N
429	2025-11-26 09:50:09.563543-03	100	0	artexhibitionitem	285	\N	\N	\N
430	2025-11-26 09:50:09.564998-03	100	0	artexhibitionitem	290	\N	\N	\N
431	2025-11-26 09:50:09.566563-03	100	0	artexhibitionitem	622	\N	\N	\N
432	2025-11-26 09:50:09.568184-03	100	0	artexhibitionitem	287	\N	\N	\N
433	2025-11-26 09:50:09.571171-03	100	0	artexhibitionitem	613	\N	\N	\N
434	2025-11-26 09:50:09.572619-03	100	0	artexhibitionitem	614	\N	\N	\N
435	2025-11-26 09:50:09.574043-03	100	0	artexhibitionitem	626	\N	\N	\N
436	2025-11-26 09:50:09.575544-03	100	0	artexhibitionitem	632	\N	\N	\N
437	2025-11-26 09:50:09.576971-03	100	0	artexhibitionitem	625	\N	\N	\N
438	2025-11-26 09:50:09.578376-03	100	0	artexhibitionitem	235	\N	\N	\N
439	2025-11-26 09:50:09.58003-03	100	0	artexhibitionitem	245	\N	\N	\N
440	2025-11-26 09:50:09.581756-03	100	0	artexhibitionitem	240	\N	\N	\N
441	2025-11-26 09:50:09.583209-03	100	0	artexhibitionitem	236	\N	\N	\N
442	2025-11-26 09:50:09.584625-03	100	0	artexhibitionitem	200	\N	\N	\N
443	2025-11-26 09:50:09.590168-03	100	0	artexhibitionitemrecord	719	\N	\N	\N
444	2025-11-26 09:50:09.591624-03	100	0	artexhibitionitemrecord	720	\N	\N	\N
445	2025-11-26 09:50:09.593005-03	100	0	artexhibitionitemrecord	721	\N	\N	\N
446	2025-11-26 09:50:09.594313-03	100	0	artexhibitionitemrecord	722	\N	\N	\N
447	2025-11-26 09:50:09.595638-03	100	0	artexhibitionitemrecord	723	\N	\N	\N
448	2025-11-26 09:50:09.597141-03	100	0	artexhibitionitemrecord	742	\N	\N	\N
449	2025-11-26 09:50:09.59844-03	100	0	artexhibitionitemrecord	783	\N	\N	\N
450	2025-11-26 09:50:09.599766-03	100	0	artexhibitionitemrecord	784	\N	\N	\N
451	2025-11-26 09:50:09.60106-03	100	0	artexhibitionitemrecord	785	\N	\N	\N
452	2025-11-26 09:50:09.602379-03	100	0	artexhibitionitemrecord	835	\N	\N	\N
453	2025-11-26 09:50:09.603738-03	100	0	artexhibitionitemrecord	836	\N	\N	\N
454	2025-11-26 09:50:09.605102-03	100	0	artexhibitionitemrecord	837	\N	\N	\N
455	2025-11-26 09:50:09.610271-03	100	0	guidecontent	203	\N	\N	\N
456	2025-11-26 09:50:09.611741-03	100	0	guidecontent	204	\N	\N	\N
457	2025-11-26 09:50:09.613631-03	100	0	guidecontent	249	\N	\N	\N
458	2025-11-26 09:50:09.615233-03	100	0	guidecontent	250	\N	\N	\N
459	2025-11-26 09:50:09.616681-03	100	0	guidecontent	251	\N	\N	\N
460	2025-11-26 09:50:09.618074-03	100	0	guidecontent	252	\N	\N	\N
461	2025-11-26 09:50:09.61958-03	100	0	guidecontent	253	\N	\N	\N
462	2025-11-26 09:50:09.621149-03	100	0	guidecontent	254	\N	\N	\N
463	2025-11-26 09:50:09.622628-03	100	0	guidecontent	488	\N	\N	\N
464	2025-11-26 09:50:09.623996-03	100	0	guidecontent	294	\N	\N	\N
465	2025-11-26 09:50:09.625351-03	100	0	guidecontent	257	\N	\N	\N
466	2025-11-26 09:50:09.626694-03	100	0	guidecontent	258	\N	\N	\N
467	2025-11-26 09:50:09.628185-03	100	0	guidecontent	259	\N	\N	\N
468	2025-11-26 09:50:09.629544-03	100	0	guidecontent	260	\N	\N	\N
469	2025-11-26 09:50:09.630818-03	100	0	guidecontent	261	\N	\N	\N
470	2025-11-26 09:50:09.634439-03	100	0	guidecontent	262	\N	\N	\N
471	2025-11-26 09:50:09.636011-03	100	0	guidecontent	295	\N	\N	\N
472	2025-11-26 09:50:09.637662-03	100	0	guidecontent	296	\N	\N	\N
473	2025-11-26 09:50:09.639204-03	100	0	guidecontent	297	\N	\N	\N
474	2025-11-26 09:50:09.64056-03	100	0	guidecontent	299	\N	\N	\N
475	2025-11-26 09:50:09.641877-03	100	0	guidecontent	298	\N	\N	\N
476	2025-11-26 09:50:09.643221-03	100	0	guidecontent	255	\N	\N	\N
477	2025-11-26 09:50:09.644463-03	100	0	guidecontent	256	\N	\N	\N
478	2025-11-26 09:50:09.645711-03	100	0	guidecontent	413	\N	\N	\N
479	2025-11-26 09:50:09.647051-03	100	0	guidecontent	293	\N	\N	\N
480	2025-11-26 09:50:09.648307-03	100	0	guidecontent	460	\N	\N	\N
481	2025-11-26 09:50:09.649543-03	100	0	guidecontent	461	\N	\N	\N
482	2025-11-26 09:50:09.650752-03	100	0	guidecontent	414	\N	\N	\N
483	2025-11-26 09:50:09.651948-03	100	0	guidecontent	412	\N	\N	\N
484	2025-11-26 09:50:09.653044-03	100	0	guidecontent	459	\N	\N	\N
485	2025-11-26 09:50:09.659093-03	100	0	guidecontentrecord	736	\N	\N	\N
486	2025-11-26 09:50:09.660523-03	100	0	guidecontentrecord	737	\N	\N	\N
487	2025-11-26 09:50:09.66179-03	100	0	guidecontentrecord	740	\N	\N	\N
488	2025-11-26 09:50:09.663076-03	100	0	guidecontentrecord	741	\N	\N	\N
489	2025-11-26 09:50:09.664339-03	100	0	guidecontentrecord	747	\N	\N	\N
490	2025-11-26 09:50:09.665579-03	100	0	guidecontentrecord	746	\N	\N	\N
491	2025-11-26 09:50:09.666912-03	100	0	guidecontentrecord	819	\N	\N	\N
492	2025-11-26 09:50:09.668311-03	100	0	guidecontentrecord	820	\N	\N	\N
493	2025-11-26 09:50:09.678977-03	100	0	audiostudio	828	\N	\N	\N
494	2025-11-26 09:50:09.680971-03	100	0	audiostudio	805	\N	\N	\N
495	2025-11-26 09:50:09.682388-03	100	0	audiostudio	825	\N	\N	\N
496	2025-11-26 09:50:09.68373-03	100	0	audiostudio	798	\N	\N	\N
497	2025-11-26 09:50:09.685421-03	100	0	audiostudio	803	\N	\N	\N
498	2025-11-26 09:50:09.686651-03	100	0	audiostudio	796	\N	\N	\N
499	2025-11-26 09:50:09.687875-03	100	0	audiostudio	829	\N	\N	\N
500	2025-11-26 09:50:09.689078-03	100	0	audiostudio	838	\N	\N	\N
501	2025-11-26 09:50:09.690424-03	100	0	audiostudio	810	\N	\N	\N
502	2025-11-26 09:50:09.691739-03	100	0	audiostudio	811	\N	\N	\N
503	2025-11-26 11:43:46.777463-03	100	2	artexhibition	830	\N	\N	\N
505	2025-11-26 12:09:12.318366-03	100	0	artexhibitionitemrecord	846	\N	\N	\N
506	2025-11-26 12:09:14.772778-03	100	0	artexhibitionitemrecord	847	\N	\N	\N
507	2025-11-26 13:47:12.013102-03	100	0	artexhibitionrecord	699	\N	\N	\N
508	2025-11-26 13:47:12.025393-03	100	0	artexhibitionrecord	700	\N	\N	\N
509	2025-11-26 13:47:12.028532-03	100	0	artexhibitionrecord	707	\N	\N	\N
510	2025-11-26 13:47:12.031475-03	100	0	artexhibitionrecord	708	\N	\N	\N
511	2025-11-26 13:47:12.033946-03	100	0	artexhibitionrecord	724	\N	\N	\N
512	2025-11-26 13:47:12.03657-03	100	0	artexhibitionrecord	725	\N	\N	\N
513	2025-11-26 13:47:12.038841-03	100	0	artexhibitionrecord	728	\N	\N	\N
514	2025-11-26 13:47:12.040953-03	100	0	artexhibitionrecord	726	\N	\N	\N
515	2025-11-26 13:47:12.043034-03	100	0	artexhibitionrecord	727	\N	\N	\N
516	2025-11-26 13:47:12.045076-03	100	0	artexhibitionrecord	698	\N	\N	\N
517	2025-11-26 13:47:12.046946-03	100	0	artexhibitionrecord	697	\N	\N	\N
518	2025-11-26 13:47:12.049087-03	100	0	artexhibitionrecord	831	\N	\N	\N
519	2025-11-26 13:47:12.051103-03	100	0	artexhibitionrecord	760	\N	\N	\N
520	2025-11-26 13:47:12.053171-03	100	0	artexhibitionrecord	761	\N	\N	\N
521	2025-11-26 13:47:12.054874-03	100	0	artexhibitionrecord	775	\N	\N	\N
522	2025-11-26 13:47:12.056546-03	100	0	artexhibitionrecord	776	\N	\N	\N
523	2025-11-26 13:47:12.058305-03	100	0	artexhibitionrecord	777	\N	\N	\N
524	2025-11-26 13:47:12.060067-03	100	0	artexhibitionrecord	832	\N	\N	\N
525	2025-11-26 13:47:12.061892-03	100	0	artexhibitionrecord	833	\N	\N	\N
526	2025-11-26 13:47:12.063937-03	100	0	artexhibitionrecord	840	\N	\N	\N
527	2025-11-26 13:47:12.065921-03	100	0	artexhibitionrecord	841	\N	\N	\N
528	2025-11-26 13:47:12.067828-03	100	0	artexhibitionrecord	842	\N	\N	\N
529	2025-11-26 14:24:08.290251-03	100	2	site	137	shortName	\N	\N
531	2025-11-27 12:55:09.754831-03	100	2	site	137	masterlanguage	\N	\N
532	2025-11-27 12:55:33.002183-03	100	2	site	137	masterlanguage	\N	\N
535	2025-11-27 17:11:43.486634-03	100	0	artexhibitionguiderecord	852	\N	\N	\N
536	2025-11-27 17:11:45.856576-03	100	0	artexhibitionguiderecord	853	\N	\N	\N
537	2025-11-27 17:12:17.146961-03	100	0	audiostudio	854	\N	\N	\N
538	2025-11-28 10:29:38.279758-03	100	0	institution	855	\N	\N	\N
539	2025-11-28 10:29:38.290472-03	100	0	institutionrecord	856	\N	\N	\N
540	2025-11-28 10:29:38.291683-03	100	0	institutionrecord	857	\N	\N	\N
541	2025-11-28 10:29:38.2926-03	100	0	institutionrecord	858	\N	\N	\N
542	2025-11-28 10:29:38.293086-03	100	2	institution	855	\N	\N	\N
543	2025-11-28 10:31:13.677733-03	100	2	institution	855	\N	\N	\N
544	2025-11-28 10:36:27.766871-03	100	2	institution	855	\N	\N	\N
545	2025-11-28 10:41:29.306001-03	100	0	resource	859	\N	\N	\N
546	2025-11-28 10:41:29.312726-03	100	2	institution	855	\N	\N	\N
547	2025-11-28 10:43:15.609937-03	100	2	institution	855	\N	\N	\N
548	2025-11-28 10:43:20.522214-03	100	0	siterecord	861	\N	\N	\N
549	2025-11-28 10:43:20.52296-03	100	0	siterecord	862	\N	\N	\N
550	2025-11-28 10:43:20.523689-03	100	0	siterecord	863	\N	\N	\N
551	2025-11-28 10:43:20.524156-03	100	0	site	860	\N	\N	\N
552	2025-11-28 10:43:20.524686-03	100	2	institution	855	add site	\N	\N
553	2025-11-28 10:45:53.828234-03	100	2	site	860	masterlanguage, opens	\N	\N
554	2025-11-28 10:51:25.221435-03	100	0	artexhibition	864	\N	\N	\N
555	2025-11-28 10:51:25.223992-03	100	0	artexhibitionrecord	865	\N	\N	\N
556	2025-11-28 10:51:25.225019-03	100	0	artexhibitionrecord	866	\N	\N	\N
557	2025-11-28 10:51:25.225993-03	100	0	artexhibitionrecord	867	\N	\N	\N
558	2025-11-28 10:54:16.178793-03	100	0	resource	868	\N	\N	\N
559	2025-11-28 12:10:17.703345-03	100	0	resource	869	\N	\N	\N
560	2025-11-28 12:18:14.013047-03	100	0	artexhibition	870	\N	\N	\N
561	2025-11-28 12:18:14.020345-03	100	0	artexhibitionrecord	871	\N	\N	\N
562	2025-11-28 12:18:14.022052-03	100	0	artexhibitionrecord	872	\N	\N	\N
563	2025-11-28 12:18:14.023432-03	100	0	artexhibitionrecord	873	\N	\N	\N
564	2025-11-28 12:19:24.322253-03	100	0	resource	874	\N	\N	\N
565	2025-11-28 21:32:20.052311-03	100	0	artexhibition	875	\N	\N	\N
566	2025-11-28 21:32:20.055198-03	100	0	artexhibitionrecord	876	\N	\N	\N
567	2025-11-28 21:32:20.056413-03	100	0	artexhibitionrecord	877	\N	\N	\N
568	2025-11-28 21:32:20.057415-03	100	0	artexhibitionrecord	878	\N	\N	\N
569	2025-11-28 21:36:07.005039-03	100	0	resource	879	\N	\N	\N
570	2025-11-29 07:31:04.744897-03	100	2	site	860	subtitle, address	\N	\N
571	2025-11-29 07:32:12.871541-03	100	0	person	880	\N	\N	\N
572	2025-11-29 07:32:12.875527-03	100	0	personrecord	881	\N	\N	\N
573	2025-11-29 07:32:12.885807-03	100	0	personrecord	882	\N	\N	\N
574	2025-11-29 07:32:12.887897-03	100	0	personrecord	883	\N	\N	\N
575	2025-11-29 07:38:16.180181-03	100	0	artwork	884	\N	\N	\N
576	2025-11-29 07:38:16.182749-03	100	0	artworkrecord	885	\N	\N	\N
577	2025-11-29 07:38:16.184178-03	100	0	artworkrecord	886	\N	\N	\N
578	2025-11-29 07:38:16.184773-03	100	0	artworkrecord	887	\N	\N	\N
579	2025-11-29 07:40:33.867468-03	100	0	resource	889	\N	\N	\N
580	2025-11-29 07:51:43.662927-03	100	0	artexhibitionitem	890	\N	\N	\N
581	2025-11-29 07:51:43.664548-03	100	0	artexhibitionitemrecord	891	\N	\N	\N
582	2025-11-29 07:51:43.664999-03	100	0	artexhibitionitemrecord	892	\N	\N	\N
583	2025-11-29 07:51:43.665414-03	100	0	artexhibitionitemrecord	893	\N	\N	\N
584	2025-11-29 07:51:43.665677-03	100	2	artexhibition	864	add item	\N	\N
585	2025-11-29 07:51:58.9767-03	100	0	artexhibitionguide	894	\N	\N	\N
586	2025-11-29 07:51:58.978945-03	100	0	artexhibitionguiderecord	895	\N	\N	\N
587	2025-11-29 07:51:58.979545-03	100	0	artexhibitionguiderecord	896	\N	\N	\N
588	2025-11-29 07:51:58.98002-03	100	0	artexhibitionguiderecord	897	\N	\N	\N
589	2025-11-29 07:52:34.048014-03	100	0	audiostudio	898	\N	\N	\N
590	2025-11-29 08:30:01.566491-03	100	2	site	860	name	\N	\N
591	2025-11-29 13:23:06.956876-03	100	2	site	860	shortName	\N	\N
592	2025-11-29 13:24:34.677585-03	100	2	site	860	shortName	\N	\N
593	2025-11-29 13:35:14.789819-03	100	0	guidecontent	899	\N	\N	\N
594	2025-11-29 13:35:14.791207-03	100	0	guidecontentrecord	900	\N	\N	\N
595	2025-11-29 13:35:14.791645-03	100	0	guidecontentrecord	901	\N	\N	\N
596	2025-11-29 13:35:14.792101-03	100	0	guidecontentrecord	902	\N	\N	\N
597	2025-11-29 13:51:07.15638-03	100	2	site	860	shortName	\N	\N
598	2025-11-29 14:35:57.929674-03	100	2	artexhibitionitem	486	\N	\N	\N
599	2025-11-29 21:30:43.272432-03	100	0	guidecontentrecord	903	\N	\N	\N
600	2025-11-30 08:17:58.5905-03	100	2	artexhibitionrecord	872	\N	\N	\N
601	2025-11-30 08:21:42.211465-03	100	2	artexhibitionrecord	873	\N	\N	\N
602	2025-11-30 08:43:09.821378-03	100	2	artexhibitionrecord	866	\N	\N	\N
603	2025-11-30 09:06:31.73629-03	100	0	siterecord	904	\N	\N	\N
604	2025-11-30 09:06:38.613152-03	100	2	siterecord	904	\N	\N	\N
605	2025-11-30 09:09:35.152585-03	100	0	siterecord	905	\N	\N	\N
606	2025-11-30 09:09:39.647031-03	100	2	siterecord	905	\N	\N	\N
607	2025-11-30 09:09:58.191942-03	100	2	siterecord	905	\N	\N	\N
608	2025-11-30 14:56:55.1686-03	100	2	artexhibitionguide	894	info	\N	\N
609	2025-11-30 14:57:24.829857-03	100	0	resource	906	\N	\N	\N
610	2025-11-30 14:59:01.297926-03	100	0	resource	907	\N	\N	\N
611	2025-11-30 15:01:11.604623-03	100	0	resource	908	\N	\N	\N
612	2025-11-30 17:28:50.242831-03	100	2	artexhibitionguide	894	integrate audioStudio	\N	\N
613	2025-11-30 17:43:29.920069-03	100	0	resource	909	\N	\N	\N
614	2025-11-30 17:43:29.925466-03	100	2	audiostudio	898	add music	\N	\N
615	2025-11-30 17:44:13.223384-03	100	0	resource	910	\N	\N	\N
616	2025-11-30 17:44:13.230047-03	100	2	audiostudio	898	add music	\N	\N
617	2025-11-30 17:48:33.887316-03	100	0	resource	911	\N	\N	\N
618	2025-11-30 17:48:33.899623-03	100	2	audiostudio	898	add music	\N	\N
619	2025-11-30 17:49:06.29324-03	100	2	artexhibitionguide	894	integrate audioStudio	\N	\N
620	2025-11-30 17:54:40.914565-03	100	2	artexhibition	875	subtitle	\N	\N
621	2025-11-30 17:57:48.104818-03	100	2	guidecontentrecord	902	translate	\N	\N
622	2025-11-30 17:57:53.564091-03	100	0	audiostudio	912	\N	\N	\N
623	2025-11-30 18:19:26.229561-03	100	0	resource	913	\N	\N	\N
624	2025-11-30 18:19:26.232923-03	100	2	audiostudio	912	generate audio voice	\N	\N
625	2025-11-30 18:20:46.450777-03	100	0	resource	914	\N	\N	\N
626	2025-11-30 18:20:46.454823-03	100	2	audiostudio	912	add music	\N	\N
627	2025-11-30 18:24:31.422733-03	100	0	audiostudio	915	\N	\N	\N
628	2025-11-30 18:26:27.387526-03	100	2	guidecontentrecord	902	integrate audioStudio	\N	\N
629	2025-11-30 18:30:33.209752-03	100	2	guidecontentrecord	901	translate	\N	\N
630	2025-11-30 18:30:59.493974-03	100	0	audiostudio	916	\N	\N	\N
631	2025-11-30 18:31:20.060867-03	100	0	resource	917	\N	\N	\N
632	2025-11-30 18:31:20.063992-03	100	2	audiostudio	916	generate audio voice	\N	\N
633	2025-11-30 18:32:01.242014-03	100	0	resource	918	\N	\N	\N
634	2025-11-30 18:32:01.245018-03	100	2	audiostudio	916	add music	\N	\N
635	2025-11-30 18:33:20.91477-03	100	2	guidecontentrecord	901	integrate audioStudio	\N	\N
636	2025-12-01 10:37:12.896899-03	100	0	resource	919	\N	\N	\N
637	2025-12-01 10:37:12.9139-03	100	2	site	177	info, logo	\N	\N
638	2025-12-01 10:38:09.438741-03	100	2	site	177	info	\N	\N
639	2025-12-01 10:38:25.850424-03	100	0	siterecord	920	\N	\N	\N
640	2025-12-01 10:38:28.207744-03	100	0	siterecord	921	\N	\N	\N
641	2025-12-04 10:01:38.71196-03	100	0	guidecontentrecord	922	\N	\N	\N
642	2025-12-04 10:01:41.123492-03	100	0	audiostudio	923	\N	\N	\N
643	2025-12-04 10:06:04.644665-03	100	0	audiostudio	924	\N	\N	\N
644	2025-12-05 07:33:50.23249-03	100	0	siterecord	934	\N	\N	\N
645	2025-12-05 07:33:54.75442-03	100	0	siterecord	935	\N	\N	\N
646	2025-12-05 20:41:43.538751-03	100	0	institutionrecord	941	\N	\N	\N
647	2025-12-06 17:35:08.506788-03	100	0	roleinstitution	958	\N	\N	\N
648	2025-12-06 17:35:08.607216-03	100	0	roleinstitution	959	\N	\N	\N
649	2025-12-06 17:35:08.617694-03	100	0	roleinstitution	960	\N	\N	\N
650	2025-12-06 17:35:08.628235-03	100	0	roleinstitution	961	\N	\N	\N
651	2025-12-06 17:35:08.638882-03	100	0	roleinstitution	962	\N	\N	\N
652	2025-12-06 17:35:08.650967-03	100	0	roleinstitution	963	\N	\N	\N
653	2025-12-06 17:35:08.660695-03	100	0	roleinstitution	964	\N	\N	\N
654	2025-12-06 17:35:08.670049-03	100	0	roleinstitution	965	\N	\N	\N
655	2025-12-06 17:35:08.679753-03	100	0	roleinstitution	966	\N	\N	\N
656	2025-12-06 17:35:08.688849-03	100	0	roleinstitution	967	\N	\N	\N
657	2025-12-06 17:35:42.313077-03	100	0	rolesite	968	\N	\N	\N
658	2025-12-06 17:35:42.325622-03	100	0	rolesite	969	\N	\N	\N
659	2025-12-06 17:35:42.335699-03	100	0	rolesite	970	\N	\N	\N
660	2025-12-06 17:35:42.346096-03	100	0	rolesite	971	\N	\N	\N
661	2025-12-06 17:35:42.356614-03	100	0	rolesite	972	\N	\N	\N
662	2025-12-06 17:35:42.366629-03	100	0	rolesite	973	\N	\N	\N
663	2025-12-06 17:35:42.375971-03	100	0	rolesite	974	\N	\N	\N
664	2025-12-06 17:35:42.384822-03	100	0	rolesite	975	\N	\N	\N
665	2025-12-06 17:35:42.393534-03	100	0	rolesite	976	\N	\N	\N
666	2025-12-06 17:36:20.614836-03	100	0	roleinstitution	977	\N	\N	\N
667	2025-12-06 17:36:20.629463-03	100	0	roleinstitution	978	\N	\N	\N
668	2025-12-06 17:36:20.63349-03	100	0	roleinstitution	979	\N	\N	\N
669	2025-12-06 17:36:20.637687-03	100	0	roleinstitution	980	\N	\N	\N
670	2025-12-06 17:36:20.641536-03	100	0	roleinstitution	981	\N	\N	\N
671	2025-12-06 17:36:20.64495-03	100	0	roleinstitution	982	\N	\N	\N
672	2025-12-06 17:36:20.647774-03	100	0	roleinstitution	983	\N	\N	\N
673	2025-12-06 17:36:20.650791-03	100	0	roleinstitution	984	\N	\N	\N
674	2025-12-06 17:36:20.65413-03	100	0	roleinstitution	985	\N	\N	\N
675	2025-12-06 17:36:20.657847-03	100	0	roleinstitution	986	\N	\N	\N
676	2025-12-06 17:44:09.910022-03	100	0	roleinstitution	987	\N	\N	\N
677	2025-12-06 17:44:09.927324-03	100	0	roleinstitution	988	\N	\N	\N
678	2025-12-06 17:44:09.931648-03	100	0	roleinstitution	989	\N	\N	\N
679	2025-12-06 17:44:09.93541-03	100	0	roleinstitution	990	\N	\N	\N
680	2025-12-06 17:44:09.938907-03	100	0	roleinstitution	991	\N	\N	\N
681	2025-12-06 17:44:09.942718-03	100	0	roleinstitution	992	\N	\N	\N
682	2025-12-06 17:44:09.945674-03	100	0	roleinstitution	993	\N	\N	\N
683	2025-12-06 17:44:09.948447-03	100	0	roleinstitution	994	\N	\N	\N
684	2025-12-06 17:44:09.951302-03	100	0	roleinstitution	995	\N	\N	\N
685	2025-12-06 17:44:09.954197-03	100	0	roleinstitution	996	\N	\N	\N
686	2025-12-06 17:44:29.702257-03	100	0	rolesite	997	\N	\N	\N
687	2025-12-06 17:44:29.707331-03	100	0	rolesite	998	\N	\N	\N
688	2025-12-06 17:44:29.711118-03	100	0	rolesite	999	\N	\N	\N
689	2025-12-06 17:44:29.715086-03	100	0	rolesite	1000	\N	\N	\N
690	2025-12-06 17:44:29.718571-03	100	0	rolesite	1001	\N	\N	\N
691	2025-12-06 17:44:29.722224-03	100	0	rolesite	1002	\N	\N	\N
692	2025-12-06 17:44:29.725586-03	100	0	rolesite	1003	\N	\N	\N
693	2025-12-06 17:44:29.728929-03	100	0	rolesite	1004	\N	\N	\N
694	2025-12-06 17:44:29.732611-03	100	0	rolesite	1005	\N	\N	\N
695	2025-12-07 09:35:35.047578-03	100	0	rolesite	1012	\N	\N	\N
696	2025-12-07 09:35:35.061525-03	100	0	rolesite	1013	\N	\N	\N
697	2025-12-07 09:35:35.063932-03	100	0	rolesite	1014	\N	\N	\N
698	2025-12-07 09:35:35.066093-03	100	0	rolesite	1015	\N	\N	\N
699	2025-12-07 09:35:35.068184-03	100	0	rolesite	1016	\N	\N	\N
700	2025-12-07 09:35:35.070755-03	100	0	rolesite	1017	\N	\N	\N
701	2025-12-07 09:35:35.072915-03	100	0	rolesite	1018	\N	\N	\N
702	2025-12-07 09:35:35.074746-03	100	0	rolesite	1019	\N	\N	\N
703	2025-12-07 09:35:35.076432-03	100	0	rolesite	1020	\N	\N	\N
705	2025-12-07 15:05:29.171044-03	100	2	user	101	remove role	{"id": "937", "name": "audit", "class": "RoleGeneral", "action": "2", "subaction": "remove role"}	\N
706	2025-12-07 15:28:46.532717-03	100	2	user	101	remove role	{"id": "937", "name": "audit", "class": "RoleGeneral", "action": "2", "subaction": "remove role"}	\N
707	2025-12-07 15:29:24.3509-03	100	2	user	101	add role	{"id": "987", "name": "admin", "class": "RoleInstitution", "action": "2", "subaction": "add role"}	\N
708	2025-12-07 15:29:39.579038-03	100	2	user	101	remove role	{"id": "987", "name": "admin", "class": "RoleInstitution", "action": "2", "subaction": "remove role"}	\N
709	2025-12-08 12:08:27.340498-03	100	2	guidecontent	414	\N	\N	\N
710	2025-12-08 12:11:15.076978-03	100	2	artexhibitionguiderecord	735	\N	\N	\N
711	2025-12-08 12:11:19.201778-03	100	0	audiostudio	1103	\N	\N	\N
712	2025-12-08 12:11:34.465206-03	100	0	resource	1104	\N	\N	\N
713	2025-12-08 12:11:34.471061-03	100	2	audiostudio	1103	\N	\N	\N
714	2025-12-08 12:15:27.726027-03	100	2	guidecontentrecord	737	\N	\N	\N
715	2025-12-08 12:15:34.522888-03	100	0	audiostudio	1105	\N	\N	\N
716	2025-12-08 12:16:57.076819-03	100	2	guidecontent	488	\N	\N	\N
717	2025-12-08 12:17:11.80054-03	100	0	resource	1106	\N	\N	\N
718	2025-12-08 12:17:11.804501-03	100	2	audiostudio	829	\N	\N	\N
719	2025-12-08 12:19:05.098773-03	100	2	artexhibitionguiderecord	734	\N	\N	\N
720	2025-12-08 12:19:24.5029-03	100	0	resource	1107	\N	\N	\N
721	2025-12-08 12:19:24.505793-03	100	2	audiostudio	924	\N	\N	\N
722	2025-12-08 17:57:56.732251-03	100	0	guidecontentrecord	1108	\N	\N	\N
723	2025-12-11 16:22:07.504625-03	100	0	audiostudio	1109	\N	\N	\N
724	2025-12-11 16:22:18.374849-03	100	0	audiostudio	1110	\N	\N	\N
725	2025-12-11 18:26:14.82494-03	100	0	person	1113	\N	\N	\N
726	2025-12-11 18:26:14.829726-03	100	0	personrecord	1114	\N	\N	\N
727	2025-12-11 18:26:14.837925-03	100	0	personrecord	1115	\N	\N	\N
728	2025-12-11 18:26:14.84012-03	100	0	personrecord	1116	\N	\N	\N
729	2025-12-11 18:26:30.461572-03	100	2	person	1113	\N	\N	\N
730	2025-12-11 21:03:44.768074-03	100	2	user	101	add role	{"id": "1000", "name": "admin", "class": "RoleSite", "action": "2", "subaction": "add role"}	\N
731	2025-12-11 21:03:54.267799-03	100	2	user	101	add role	{"id": "987", "name": "admin", "class": "RoleInstitution", "action": "2", "subaction": "add role"}	\N
732	2025-12-11 21:04:04.906923-03	100	2	user	101	add role	{"id": "1015", "name": "editor", "class": "RoleSite", "action": "2", "subaction": "add role"}	\N
735	2025-12-13 07:42:05.949609-03	100	0	user	1459	\N	\N	\N
736	2025-12-13 16:37:20.943589-03	100	2	site	137	\N	\N	\N
737	2025-12-13 18:27:17.943065-03	100	2	institution	855	\N	\N	\N
738	2025-12-13 18:27:34.593877-03	100	2	institution	855	\N	\N	\N
739	2025-12-14 13:17:23.136743-03	100	2	institution	547	\N	\N	\N
740	2025-12-14 13:28:10.063596-03	100	2	institution	547	\N	\N	\N
741	2025-12-14 13:29:04.881434-03	100	2	institution	547	\N	\N	\N
742	2025-12-14 13:38:06.828522-03	100	2	institution	547	\N	\N	\N
743	2025-12-14 13:41:14.683802-03	100	2	institution	547	\N	\N	\N
744	2025-12-14 13:45:28.290961-03	100	2	institution	547	\N	\N	\N
745	2025-12-14 14:04:33.486059-03	100	2	institution	547	\N	\N	\N
746	2025-12-14 14:09:45.675733-03	100	2	institution	547	\N	\N	\N
747	2025-12-14 14:15:35.432935-03	100	2	institution	547	\N	\N	\N
748	2025-12-14 14:16:44.04438-03	100	2	institution	547	zoneid	\N	\N
751	2025-12-14 14:31:54.621202-03	100	0	artexhibitionguiderecord	1484	\N	\N	\N
752	2025-12-14 14:31:57.185126-03	100	0	artexhibitionguiderecord	1485	\N	\N	\N
753	2025-12-15 11:16:19.719936-03	100	0	resource	1490	\N	\N	\N
754	2025-12-15 11:16:19.738734-03	100	2	site	860	logo	\N	\N
755	2025-12-15 12:14:46.779617-03	100	0	resource	1491	\N	\N	\N
756	2025-12-15 12:14:46.798279-03	100	2	site	552	logo	\N	\N
757	2025-12-15 12:56:29.285965-03	100	0	resource	1492	\N	\N	\N
758	2025-12-15 12:56:29.304577-03	100	2	site	860	logo	\N	\N
759	2025-12-15 13:07:36.337252-03	100	0	resource	1493	\N	\N	\N
760	2025-12-15 13:07:36.341984-03	100	2	site	860	logo	\N	\N
761	2025-12-17 12:50:02.550567-03	100	0	guidecontentrecord	1494	\N	\N	\N
762	2025-12-18 13:53:58.907435-03	100	2	guidecontentrecord	736	translate	\N	\N
763	2025-12-18 13:54:18.654885-03	100	2	guidecontentrecord	740	translate	\N	\N
764	2025-12-18 16:17:42.383201-03	100	2	guidecontentrecord	922	translate	\N	\N
765	2025-12-18 16:18:23.152908-03	100	2	guidecontent	413	info	\N	\N
766	2025-12-18 16:18:28.712154-03	100	2	guidecontentrecord	740	translate	\N	\N
767	2025-12-18 16:49:42.411995-03	100	2	artexhibitionrecord	877	translate	\N	\N
768	2025-12-18 17:59:29.323594-03	100	2	artexhibitionrecord	699	translate	\N	\N
769	2025-12-18 17:59:52.435081-03	100	2	artexhibitionrecord	707	translate	\N	\N
770	2025-12-18 18:00:11.103713-03	100	0	artexhibitionrecord	1495	\N	\N	\N
771	2025-12-18 18:00:14.285213-03	100	2	artexhibitionrecord	1495	translate	\N	\N
772	2025-12-18 18:04:36.092743-03	100	2	siterecord	862	translate	\N	\N
773	2025-12-19 08:15:44.645792-03	100	0	artworkrecord	1496	\N	\N	\N
774	2025-12-19 08:15:53.116114-03	100	2	artworkrecord	1496	translate	\N	\N
775	2025-12-19 08:54:35.095898-03	100	2	institutionrecord	857	translate	\N	\N
776	2025-12-19 12:51:52.335605-03	100	2	institution	821	shortName	\N	\N
777	2025-12-19 12:55:33.08335-03	100	2	institution	821	name	\N	\N
778	2025-12-19 12:57:35.606834-03	100	2	institution	529	name	\N	\N
779	2025-12-19 13:00:05.60375-03	100	2	institution	529	name	\N	\N
780	2025-12-19 13:02:07.535971-03	100	2	institution	529	name	\N	\N
781	2025-12-19 13:05:31.950116-03	100	2	institution	529	name	\N	\N
782	2025-12-19 13:09:45.634148-03	100	2	institution	529	name	\N	\N
783	2025-12-19 13:24:28.670505-03	100	2	institutionrecord	941	name	\N	\N
784	2025-12-19 13:42:24.551971-03	100	2	InstitutionRecord	941	name	\N	\N
785	2025-12-19 13:43:42.895632-03	100	2	InstitutionRecord	941	name	\N	\N
786	2025-12-19 13:57:30.661234-03	100	2	InstitutionRecord	941	name	\N	\N
787	2025-12-19 13:58:19.558518-03	100	2	InstitutionRecord	941	name	\N	\N
788	2025-12-19 14:19:19.305404-03	100	2	Institution	821	name	\N	\N
789	2025-12-19 14:19:32.58608-03	100	2	InstitutionRecord	823	name	\N	\N
790	2025-12-19 16:00:03.053827-03	100	2	ArtWork	229	artist, year	\N	\N
791	2025-12-19 17:06:52.554245-03	100	2	ArtWork	575	info, artist, year	\N	\N
792	2025-12-20 11:01:20.690633-03	100	2	ArtWork	402	artist, year	\N	\N
793	2025-12-20 12:29:36.041642-03	100	0	Resource	1499	\N	\N	\N
794	2025-12-20 12:29:36.049159-03	100	2	Site	552	logo	\N	\N
795	2025-12-20 12:33:20.295243-03	100	2	ArtWork	450	artist, year	\N	\N
796	2025-12-20 12:56:13.303101-03	100	2	ArtWork	225	artist, year	\N	\N
797	2025-12-20 12:57:13.710558-03	100	2	ArtWork	225	artist, year	\N	\N
798	2025-12-20 13:01:09.895639-03	100	0	ArtWorkRecord	1500	\N	\N	\N
799	2025-12-20 13:01:12.38362-03	100	0	ArtWorkRecord	1501	\N	\N	\N
800	2025-12-20 15:44:48.771218-03	100	2	ArtWork	402	artist, year	\N	\N
801	2025-12-21 15:07:34.738749-03	100	2	Site	552		\N	\N
802	2025-12-21 15:07:49.6232-03	100	2	Site	552		\N	\N
803	2025-12-21 15:08:02.303348-03	100	2	Site	552	zoneid	\N	\N
804	2025-12-21 15:08:36.655924-03	100	2	Site	552	zoneid	\N	\N
805	2025-12-21 15:17:03.155797-03	100	2	User	101	locale	\N	\N
806	2025-12-21 17:34:34.909599-03	100	2	Person	1113	address, phone, email	\N	\N
807	2025-12-22 10:55:08.875713-03	101	2	User	101	locale	\N	\N
808	2025-12-22 13:28:57.849159-03	100	0	Resource	1730	\N	\N	\N
809	2025-12-22 13:28:57.870252-03	101	2	Person	1113	photo	\N	\N
810	2025-12-23 18:12:18.022554-03	101	2	ArtExhibition	864	subtitle	\N	\N
811	2025-12-23 18:13:13.811021-03	101	2	ArtWorkRecord	886	translate	\N	\N
812	2025-12-24 08:42:06.719795-03	101	2	User	101	locale	\N	\N
813	2025-12-24 10:24:47.765166-03	101	2	ArtWork	450	artist, year	\N	\N
814	2025-12-24 16:46:07.452329-03	101	2	ArtWork	229	artist, year	\N	\N
815	2025-12-24 16:46:48.041236-03	101	2	ArtWork	205	artist, year	\N	\N
816	2025-12-24 17:36:15.833852-03	101	2	GuideContentRecord	1108	translate	\N	\N
817	2025-12-24 17:36:23.628404-03	101	0	AudioStudio	7329	\N	\N	\N
818	2025-12-24 18:46:51.169672-03	101	0	ArtWorkRecord	7518	\N	\N	\N
819	2025-12-24 19:10:59.654767-03	101	2	User	101	add role	{"id": "996", "name": "admin", "class": "RoleInstitution", "action": "2", "subaction": "add role"}	\N
820	2025-12-26 09:54:45.398726-03	101	2	ArtWork	272	year	\N	\N
821	2025-12-26 10:01:26.273259-03	101	2	ArtWork	272	martists, year	\N	\N
822	2025-12-26 10:04:05.128543-03	101	2	ArtWork	272	martists, year	\N	\N
823	2025-12-26 10:12:47.711757-03	101	2	ArtWork	272	martists	\N	\N
824	2025-12-26 10:16:10.087455-03	101	2	ArtWork	272	martists	\N	\N
825	2025-12-26 10:30:25.718045-03	101	2	ArtWork	272	artists	\N	\N
826	2025-12-26 16:07:57.145498-03	100	0	ArtWork	12377	\N	\N	\N
827	2025-12-26 16:07:57.158222-03	100	0	ArtWorkRecord	12378	\N	\N	\N
828	2025-12-26 16:07:57.159952-03	100	0	ArtWorkRecord	12379	\N	\N	\N
829	2025-12-26 16:07:57.161561-03	100	0	ArtWorkRecord	12380	\N	\N	\N
830	2025-12-26 16:10:13.712163-03	100	0	Resource	12516	\N	\N	\N
831	2025-12-26 16:10:13.719002-03	101	2	ArtWork	12377	info, photo, year	\N	\N
832	2025-12-26 16:10:50.665446-03	101	2	ArtWork	12377	name	\N	\N
833	2025-12-26 16:26:12.886765-03	101	2	ArtWork	12377	artists, info	\N	\N
834	2025-12-26 16:29:47.347212-03	101	2	ArtWork	12377	spec, year	\N	\N
835	2025-12-27 09:05:37.597047-03	100	0	Resource	13112	\N	\N	\N
836	2025-12-27 09:05:37.613238-03	101	2	AudioStudio	796	add music	\N	\N
837	2025-12-27 09:08:04.037589-03	101	2	ArtExhibitionGuide	411	integrate audioStudio	\N	\N
838	2025-12-27 14:57:14.05744-03	101	2	ArtWork	482	artists	\N	\N
839	2026-01-04 17:37:28.121037-03	101	2	ArtWork	406	artists	\N	\N
840	2026-01-04 17:38:46.412609-03	101	2	ArtWork	406	info	\N	\N
841	2026-01-04 17:40:56.141161-03	101	2	ArtExhibition	870	info, intro	\N	\N
842	2026-01-04 17:41:17.673392-03	101	2	ArtExhibition	870	info, intro, location	\N	\N
843	2026-01-04 17:48:00.075887-03	100	0	ArtWork	16103	\N	\N	\N
844	2026-01-04 17:48:00.084657-03	100	0	ArtWorkRecord	16104	\N	\N	\N
845	2026-01-04 17:48:00.086337-03	100	0	ArtWorkRecord	16105	\N	\N	\N
846	2026-01-04 17:48:00.087569-03	100	0	ArtWorkRecord	16106	\N	\N	\N
847	2026-01-04 17:49:50.454176-03	100	0	Resource	16212	\N	\N	\N
848	2026-01-04 17:49:50.461918-03	101	2	ArtWork	16103	artists, name, info, photo, year	\N	\N
849	2026-01-04 17:57:55.024589-03	101	2	ArtWork	16103	translate	\N	\N
850	2026-01-04 17:58:09.05919-03	101	2	ArtWork	884	translate	\N	\N
851	2026-01-04 17:58:22.333916-03	101	2	ArtWork	12377	translate	\N	\N
852	2026-01-04 17:59:28.819972-03	100	0	ArtExhibitionItem	16643	\N	\N	\N
853	2026-01-04 17:59:28.825801-03	100	0	ArtExhibitionItemRecord	16644	\N	\N	\N
854	2026-01-04 17:59:28.827172-03	100	0	ArtExhibitionItemRecord	16645	\N	\N	\N
855	2026-01-04 17:59:28.828111-03	100	0	ArtExhibitionItemRecord	16646	\N	\N	\N
856	2026-01-04 17:59:28.830015-03	100	2	ArtExhibition	864	add item	{"id": "16643", "name": "Je suis (Yo soy)", "class": "ArtExhibitionItem", "action": "2", "subaction": "add item"}	\N
857	2026-01-04 17:59:31.487329-03	100	0	ArtExhibitionItem	16647	\N	\N	\N
858	2026-01-04 17:59:31.488865-03	100	0	ArtExhibitionItemRecord	16648	\N	\N	\N
859	2026-01-04 17:59:31.49006-03	100	0	ArtExhibitionItemRecord	16649	\N	\N	\N
860	2026-01-04 17:59:31.490802-03	100	0	ArtExhibitionItemRecord	16650	\N	\N	\N
861	2026-01-04 17:59:31.49193-03	100	2	ArtExhibition	864	add item	{"id": "16647", "name": "Le Sombre Malembo, Dieu du carrefour", "class": "ArtExhibitionItem", "action": "2", "subaction": "add item"}	\N
862	2026-01-04 18:01:43.49508-03	101	2	ArtExhibition	870	info	\N	\N
863	2026-01-04 18:07:59.872698-03	100	0	Resource	17171	\N	\N	\N
864	2026-01-04 18:07:59.883207-03	101	2	ArtExhibition	875	location, photo	\N	\N
865	2026-01-04 18:12:01.091021-03	101	2	User	101	locale	\N	\N
866	2026-01-05 04:45:07.096915-03	101	2	User	101	locale	\N	\N
867	2026-01-05 06:17:15.026464-03	101	2	User	101	locale	\N	\N
868	2026-01-05 09:50:22.216657-03	101	2	User	101	locale	\N	\N
869	2026-01-05 10:43:44.900039-03	100	0	GuideContent	18152	\N	\N	\N
870	2026-01-05 10:43:44.902605-03	100	0	GuideContentRecord	18153	\N	\N	\N
871	2026-01-05 10:43:44.903237-03	100	0	GuideContentRecord	18154	\N	\N	\N
872	2026-01-05 10:43:44.903827-03	100	0	GuideContentRecord	18155	\N	\N	\N
873	2026-01-05 10:43:48.01828-03	100	0	GuideContent	18156	\N	\N	\N
874	2026-01-05 10:43:48.020212-03	100	0	GuideContentRecord	18157	\N	\N	\N
875	2026-01-05 10:43:48.021911-03	100	0	GuideContentRecord	18158	\N	\N	\N
876	2026-01-05 10:43:48.023397-03	100	0	GuideContentRecord	18159	\N	\N	\N
877	2026-01-05 10:47:36.442318-03	101	2	ArtWork	12377	info	\N	\N
878	2026-01-05 10:48:31.610612-03	101	2	GuideContent	18156	info	\N	\N
879	2026-01-05 10:48:56.422506-03	101	0	AudioStudio	18635	\N	\N	\N
880	2026-01-05 13:49:41.527858-03	101	2	GuideContentRecord	901	info	\N	\N
881	2026-01-05 13:50:05.180995-03	101	2	GuideContent	899	info	\N	\N
882	2026-01-05 13:50:47.863572-03	101	2	GuideContentRecord	902	info	\N	\N
883	2026-01-05 15:37:54.682879-03	101	2	GuideContentRecord	901		\N	\N
884	2026-01-05 15:46:00.162109-03	100	0	Resource	19611	\N	\N	\N
885	2026-01-05 15:46:00.1715-03	101	2	ArtExhibition	864	photo	\N	\N
886	2026-01-05 15:50:48.51656-03	101	0	AudioStudio	20072	\N	\N	\N
887	2026-01-05 16:02:19.402819-03	101	2	GuideContentRecord	901		\N	\N
888	2026-01-05 16:05:44.938304-03	101	2	GuideContentRecord	901		\N	\N
889	2026-01-05 16:58:50.104931-03	101	2	GuideContentRecord	901		\N	\N
890	2026-01-06 13:04:18.063471-03	101	2	GuideContentRecord	901		\N	\N
891	2026-01-06 14:30:46.381233-03	101	0	AudioStudio	22018	\N	\N	\N
892	2026-01-06 14:41:51.594189-03	100	0	Resource	22474	\N	\N	\N
893	2026-01-06 14:41:51.601056-03	101	2	AudioStudio	915	generate audio voice	\N	\N
894	2026-01-06 14:55:41.374643-03	100	0	Resource	22475	\N	\N	\N
895	2026-01-06 14:55:41.380664-03	101	2	AudioStudio	915	add music	\N	\N
896	2026-01-06 15:06:04.944571-03	101	2	GuideContent	899	integrate audioStudio	\N	\N
897	2026-01-06 15:11:18.615738-03	101	2	GuideContentRecord	901	translate	\N	\N
898	2026-01-06 15:11:45.500441-03	100	0	Resource	22521	\N	\N	\N
899	2026-01-06 15:11:45.505852-03	101	2	AudioStudio	916	generate audio voice	\N	\N
900	2026-01-06 15:18:27.36554-03	101	2	GuideContentRecord	901	integrate audioStudio	\N	\N
901	2026-01-07 08:27:21.456191-03	100	0	ArtWork	22902	\N	\N	\N
902	2026-01-07 08:27:21.473619-03	100	0	ArtWorkRecord	22903	\N	\N	\N
903	2026-01-07 08:27:21.47438-03	100	0	ArtWorkRecord	22904	\N	\N	\N
904	2026-01-07 08:27:21.47492-03	100	0	ArtWorkRecord	22905	\N	\N	\N
905	2026-01-07 08:28:37.817456-03	100	0	Resource	23041	\N	\N	\N
906	2026-01-07 08:28:37.834529-03	101	2	ArtWork	22902	artists, photo	\N	\N
907	2026-01-07 08:31:51.631907-03	101	2	ArtWork	22902	name	\N	\N
908	2026-01-07 14:25:33.442763-03	101	2	ArtWork	22902	name	\N	\N
909	2026-01-07 14:27:33.00089-03	101	2	ArtWork	22902	name, info	\N	\N
910	2026-01-09 19:18:13.984659-03	101	2	Site	555	languages	\N	\N
911	2026-01-09 19:19:10.618906-03	101	2	Site	555	languages	\N	\N
912	2026-01-09 19:19:54.72202-03	101	2	Site	137		\N	\N
913	2026-01-09 19:20:33.964212-03	101	2	Site	552		\N	\N
914	2026-01-09 19:20:58.388063-03	101	2	Site	537		\N	\N
915	2026-01-09 19:21:22.670018-03	101	2	Site	564		\N	\N
916	2026-01-09 19:21:49.15266-03	101	2	Site	564		\N	\N
917	2026-01-10 18:19:31.095003-03	100	0	ArtExhibition	27577	\N	\N	\N
918	2026-01-10 18:19:31.098387-03	100	0	ArtExhibitionRecord	27578	\N	\N	\N
919	2026-01-10 18:19:31.099491-03	100	0	ArtExhibitionRecord	27579	\N	\N	\N
920	2026-01-10 18:19:31.10045-03	100	0	ArtExhibitionRecord	27580	\N	\N	\N
921	2026-01-10 18:20:17.952944-03	101	2	ArtExhibition	27577	permanent, name	\N	\N
922	2026-01-10 18:21:46.646253-03	101	2	ArtExhibition	27577	ordinal	\N	\N
923	2026-01-10 18:23:36.13216-03	100	0	ArtExhibition	27921	\N	\N	\N
924	2026-01-10 18:23:36.133993-03	100	0	ArtExhibitionRecord	27922	\N	\N	\N
925	2026-01-10 18:23:36.134864-03	100	0	ArtExhibitionRecord	27923	\N	\N	\N
926	2026-01-10 18:23:36.135602-03	100	0	ArtExhibitionRecord	27924	\N	\N	\N
927	2026-01-10 18:24:06.66806-03	101	2	ArtExhibition	27921	ordinal, name	\N	\N
928	2026-01-10 18:24:11.858577-03	100	0	ArtExhibition	28035	\N	\N	\N
929	2026-01-10 18:24:11.860011-03	100	0	ArtExhibitionRecord	28036	\N	\N	\N
930	2026-01-10 18:24:11.86101-03	100	0	ArtExhibitionRecord	28037	\N	\N	\N
931	2026-01-10 18:24:11.861914-03	100	0	ArtExhibitionRecord	28038	\N	\N	\N
932	2026-01-10 18:24:24.11518-03	101	2	ArtExhibition	28035	ordinal, name	\N	\N
933	2026-01-10 18:24:53.595506-03	101	2	ArtExhibition	27577	permanent	\N	\N
934	2026-01-10 18:25:00.069413-03	101	2	ArtExhibition	27577	permanent	\N	\N
935	2026-01-10 18:34:21.529075-03	100	0	ArtExhibition	28384	\N	\N	\N
936	2026-01-10 18:34:21.530163-03	100	0	ArtExhibitionRecord	28385	\N	\N	\N
937	2026-01-10 18:34:21.530721-03	100	0	ArtExhibitionRecord	28386	\N	\N	\N
938	2026-01-10 18:34:21.531164-03	100	0	ArtExhibitionRecord	28387	\N	\N	\N
939	2026-01-10 18:34:41.782982-03	101	2	ArtExhibition	28384	name	\N	\N
940	2026-01-10 18:34:52.817134-03	100	0	ArtExhibition	28498	\N	\N	\N
941	2026-01-10 18:34:52.817967-03	100	0	ArtExhibitionRecord	28499	\N	\N	\N
942	2026-01-10 18:34:52.818563-03	100	0	ArtExhibitionRecord	28500	\N	\N	\N
943	2026-01-10 18:34:52.819148-03	100	0	ArtExhibitionRecord	28501	\N	\N	\N
944	2026-01-10 18:35:02.709218-03	101	2	ArtExhibition	28498	name	\N	\N
945	2026-01-10 18:35:08.959098-03	101	2	ArtExhibition	28498	name, ordinal	\N	\N
946	2026-01-10 18:35:13.856303-03	100	0	ArtExhibition	28682	\N	\N	\N
947	2026-01-10 18:35:13.857563-03	100	0	ArtExhibitionRecord	28683	\N	\N	\N
948	2026-01-10 18:35:13.858624-03	100	0	ArtExhibitionRecord	28684	\N	\N	\N
949	2026-01-10 18:35:13.859528-03	100	0	ArtExhibitionRecord	28685	\N	\N	\N
950	2026-01-10 18:35:26.387105-03	101	2	ArtExhibition	28682	ordinal, name	\N	\N
951	2026-01-10 18:36:04.134842-03	100	0	ArtExhibition	28796	\N	\N	\N
952	2026-01-10 18:36:04.136187-03	100	0	ArtExhibitionRecord	28797	\N	\N	\N
953	2026-01-10 18:36:04.137241-03	100	0	ArtExhibitionRecord	28798	\N	\N	\N
954	2026-01-10 18:36:04.138118-03	100	0	ArtExhibitionRecord	28799	\N	\N	\N
955	2026-01-10 18:36:20.091641-03	101	2	ArtExhibition	28796	name	\N	\N
956	2026-01-11 09:23:40.839783-03	100	0	Resource	29025	\N	\N	\N
957	2026-01-11 09:23:40.850113-03	101	2	ArtExhibition	28384	photo	\N	\N
958	2026-01-11 09:25:49.470491-03	101	2	ArtExhibition	28384	photo, name, info	\N	\N
959	2026-01-11 09:41:58.75956-03	101	2	ArtExhibition	28035	permanent, info	\N	\N
960	2026-01-11 09:42:13.688211-03	100	0	Resource	29336	\N	\N	\N
961	2026-01-11 09:42:59.664797-03	101	2	ArtExhibition	28035	permanent, info, photo	\N	\N
962	2026-01-11 09:43:16.673074-03	100	0	Resource	29477	\N	\N	\N
963	2026-01-11 09:43:16.679713-03	101	2	ArtExhibition	28035	permanent, info, photo	\N	\N
964	2026-01-11 09:53:25.682318-03	100	0	Resource	29858	\N	\N	\N
965	2026-01-11 09:53:25.690994-03	101	2	ArtExhibition	27921	photo	\N	\N
966	2026-01-11 09:55:27.026648-03	101	2	ArtExhibition	27921	photo, name, info	\N	\N
967	2026-01-11 10:13:58.506143-03	100	0	Resource	30179	\N	\N	\N
968	2026-01-11 10:13:58.520929-03	101	2	ArtExhibition	28796	photo	\N	\N
969	2026-01-11 10:15:25.496624-03	100	0	Resource	30290	\N	\N	\N
970	2026-01-11 10:15:25.50272-03	101	2	ArtExhibition	27577	photo	\N	\N
971	2026-01-11 10:19:32.935503-03	100	0	Resource	30401	\N	\N	\N
972	2026-01-11 10:19:32.946989-03	101	2	ArtExhibition	28682	photo	\N	\N
973	2026-01-11 10:20:55.685007-03	101	2	ArtExhibition	28682	photo, info	\N	\N
974	2026-01-11 10:21:24.136814-03	100	0	Resource	30582	\N	\N	\N
975	2026-01-11 10:21:24.140697-03	101	2	ArtExhibition	28498	info, photo	\N	\N
976	2026-01-11 10:24:48.071214-03	100	0	Resource	30693	\N	\N	\N
977	2026-01-11 10:24:48.077298-03	101	2	ArtExhibition	28682	photo	\N	\N
978	2026-01-11 10:27:16.185419-03	101	2	ArtExhibition	28384	permanent	\N	\N
979	2026-01-11 10:27:24.733354-03	101	2	ArtExhibition	27921	permanent	\N	\N
980	2026-01-11 10:27:38.522962-03	101	2	ArtExhibition	28498	permanent	\N	\N
981	2026-01-11 10:27:50.682319-03	101	2	ArtExhibition	28682	permanent	\N	\N
982	2026-01-11 14:08:07.308075-03	101	2	ArtExhibition	28796	info, from, to	\N	\N
983	2026-01-11 14:17:10.707078-03	101	2	ArtExhibition	27577	ordinal	\N	\N
984	2026-01-11 14:35:25.169775-03	100	0	Resource	32034	\N	\N	\N
985	2026-01-11 14:35:25.175245-03	101	2	ArtExhibition	28796	location, photo	\N	\N
986	2026-01-11 14:40:32.227825-03	101	2	ArtExhibition	27921	location	\N	\N
987	2026-01-11 14:41:07.464367-03	101	2	ArtExhibition	28384	location	\N	\N
988	2026-01-11 14:46:02.881973-03	101	2	ArtExhibition	28384	ordinal	\N	\N
989	2026-01-11 15:57:33.318643-03	101	2	ArtExhibition	27577	location, info	\N	\N
990	2026-01-11 15:59:56.819044-03	101	2	ArtExhibition	27577	info	\N	\N
991	2026-01-11 16:12:14.730295-03	101	0	ArtExhibitionGuide	33825	\N	\N	\N
992	2026-01-11 16:12:14.731817-03	101	0	ArtExhibitionGuideRecord	33826	\N	\N	\N
993	2026-01-11 16:12:14.732385-03	101	0	ArtExhibitionGuideRecord	33827	\N	\N	\N
994	2026-01-11 16:12:14.732903-03	101	0	ArtExhibitionGuideRecord	33828	\N	\N	\N
995	2026-01-11 16:12:56.592281-03	101	2	ArtExhibitionGuide	33825	info	\N	\N
996	2026-01-11 16:13:00.09173-03	101	0	AudioStudio	34074	\N	\N	\N
997	2026-01-11 16:13:34.05216-03	100	0	Resource	34080	\N	\N	\N
998	2026-01-11 16:13:34.056273-03	101	2	AudioStudio	34074	generate audio voice	\N	\N
999	2026-01-11 16:15:24.021732-03	100	0	Resource	34081	\N	\N	\N
1000	2026-01-11 16:15:24.028589-03	101	2	AudioStudio	34074	add music	\N	\N
1001	2026-01-11 16:20:48.880981-03	100	0	Resource	34122	\N	\N	\N
1002	2026-01-11 16:20:48.884386-03	101	2	AudioStudio	34074	add music	\N	\N
1003	2026-01-11 16:21:15.336077-03	101	2	ArtExhibitionGuide	33825	integrate audioStudio	\N	\N
1004	2026-01-11 16:21:29.086256-03	101	2	ArtExhibitionGuideRecord	33827	translate	\N	\N
1005	2026-01-11 16:21:31.478876-03	101	0	AudioStudio	34158	\N	\N	\N
1006	2026-01-11 16:21:58.215976-03	100	0	Resource	34164	\N	\N	\N
1007	2026-01-11 16:21:58.218891-03	101	2	AudioStudio	34158	generate audio voice	\N	\N
1008	2026-01-11 16:22:44.837305-03	100	0	Resource	34165	\N	\N	\N
1009	2026-01-11 16:22:44.84021-03	101	2	AudioStudio	34158	add music	\N	\N
1010	2026-01-11 16:22:49.15139-03	101	2	ArtExhibitionGuideRecord	33827	integrate audioStudio	\N	\N
1011	2026-01-11 16:22:58.719531-03	101	2	ArtExhibitionGuideRecord	33828	translate	\N	\N
1012	2026-01-11 16:23:00.874544-03	101	0	AudioStudio	34201	\N	\N	\N
1013	2026-01-11 16:23:31.498903-03	100	0	Resource	34207	\N	\N	\N
1014	2026-01-11 16:23:31.501419-03	101	2	AudioStudio	34201	generate audio voice	\N	\N
1015	2026-01-11 16:23:49.964713-03	100	0	Resource	34208	\N	\N	\N
1016	2026-01-11 16:23:49.967262-03	101	2	AudioStudio	34201	add music	\N	\N
1017	2026-01-11 16:23:54.426775-03	101	2	ArtExhibitionGuideRecord	33828	integrate audioStudio	\N	\N
1018	2026-01-12 10:28:58.71801-03	100	0	ArtWork	34719	\N	\N	\N
1019	2026-01-12 10:28:58.721489-03	100	0	ArtWorkRecord	34720	\N	\N	\N
1020	2026-01-12 10:28:58.723348-03	100	0	ArtWorkRecord	34721	\N	\N	\N
1021	2026-01-12 10:28:58.724336-03	100	0	ArtWorkRecord	34722	\N	\N	\N
1022	2026-01-12 10:32:28.75278-03	100	0	Person	34768	\N	\N	\N
1023	2026-01-12 10:32:28.760417-03	100	0	PersonRecord	34769	\N	\N	\N
1024	2026-01-12 10:32:28.766086-03	100	0	PersonRecord	34770	\N	\N	\N
1025	2026-01-12 10:32:28.770615-03	100	0	PersonRecord	34771	\N	\N	\N
1026	2026-01-12 10:32:51.704051-03	101	2	Person	34768	state, name, lastname	\N	\N
1027	2026-01-12 10:34:18.707387-03	101	2	Person	34768	name	\N	\N
1028	2026-01-12 10:40:12.091183-03	101	2	ArtWork	34719	name, info, year	\N	\N
1029	2026-01-12 10:41:14.674763-03	100	0	Resource	35197	\N	\N	\N
1030	2026-01-12 10:41:14.679682-03	101	2	ArtWork	34719	name, info, year, photo	\N	\N
1031	2026-01-12 10:41:41.314681-03	100	0	ArtExhibitionItem	35278	\N	\N	\N
1032	2026-01-12 10:41:41.320226-03	100	0	ArtExhibitionItemRecord	35279	\N	\N	\N
1033	2026-01-12 10:41:41.321142-03	100	0	ArtExhibitionItemRecord	35280	\N	\N	\N
1034	2026-01-12 10:41:41.321833-03	100	0	ArtExhibitionItemRecord	35281	\N	\N	\N
1035	2026-01-12 10:41:41.32277-03	100	2	ArtExhibition	27577	add item	{"id": "35278", "name": "Traje de calle del alférez real", "class": "ArtExhibitionItem", "action": "2", "subaction": "add item"}	\N
1036	2026-01-12 10:42:05.996435-03	100	0	GuideContent	35357	\N	\N	\N
1037	2026-01-12 10:42:05.997545-03	100	0	GuideContentRecord	35358	\N	\N	\N
1038	2026-01-12 10:42:05.998135-03	100	0	GuideContentRecord	35359	\N	\N	\N
1039	2026-01-12 10:42:05.998581-03	100	0	GuideContentRecord	35360	\N	\N	\N
1040	2026-01-12 10:42:20.579777-03	101	2	GuideContent	35357	info	\N	\N
1041	2026-01-12 10:42:25.412147-03	101	0	AudioStudio	35496	\N	\N	\N
1042	2026-01-12 10:42:45.602932-03	100	0	Resource	35502	\N	\N	\N
1043	2026-01-12 10:42:45.607116-03	101	2	AudioStudio	35496	generate audio voice	\N	\N
1044	2026-01-12 10:43:24.101511-03	100	0	Resource	35503	\N	\N	\N
1045	2026-01-12 10:43:24.106628-03	101	2	AudioStudio	35496	add music	\N	\N
1046	2026-01-12 10:43:27.8545-03	101	2	GuideContent	35357	integrate audioStudio	\N	\N
1047	2026-01-12 15:20:15.714095-03	101	2	ArtWork	34719	source	\N	\N
1048	2026-01-12 15:29:11.317587-03	101	2	ArtWork	34719	epoch	\N	\N
1049	2026-01-12 15:29:54.627876-03	101	2	ArtWork	34719	spec	\N	\N
1050	2026-01-12 15:53:12.38604-03	101	2	ArtWork	34719	year	\N	\N
1051	2026-01-12 15:55:52.228221-03	101	2	ArtWork	34719	year	\N	\N
1052	2026-01-12 15:57:59.445185-03	101	2	ArtWork	34719	year, spec	\N	\N
1053	2026-01-12 16:03:42.46631-03	100	0	ArtExhibition	37404	\N	\N	\N
1054	2026-01-12 16:03:42.469098-03	100	0	ArtExhibitionRecord	37405	\N	\N	\N
1055	2026-01-12 16:03:42.470095-03	100	0	ArtExhibitionRecord	37406	\N	\N	\N
1056	2026-01-12 16:03:42.470799-03	100	0	ArtExhibitionRecord	37407	\N	\N	\N
1057	2026-01-12 16:05:52.059473-03	101	2	ArtExhibition	37404	opens, name, shortname, info, from, to	\N	\N
1058	2026-01-12 16:07:19.242431-03	100	0	Resource	37553	\N	\N	\N
1059	2026-01-12 16:07:19.246625-03	101	2	ArtExhibition	37404	opens, name, shortname, info, from, to, photo	\N	\N
1060	2026-01-13 04:38:25.552414-03	100	0	ArtExhibition	37829	\N	\N	\N
1061	2026-01-13 04:38:25.554012-03	100	0	ArtExhibitionRecord	37830	\N	\N	\N
1062	2026-01-13 04:38:25.555037-03	100	0	ArtExhibitionRecord	37831	\N	\N	\N
1063	2026-01-13 04:38:25.555931-03	100	0	ArtExhibitionRecord	37832	\N	\N	\N
1064	2026-01-13 04:44:53.589137-03	100	0	Resource	37908	\N	\N	\N
1065	2026-01-13 04:44:53.601069-03	101	2	ArtExhibition	37829	ordinal, permanent, name, info, photo	\N	\N
1066	2026-01-13 04:53:13.918323-03	101	2	ArtExhibition	28796	translate	\N	\N
1067	2026-01-13 04:55:32.460398-03	101	2	ArtExhibition	27577	translate	\N	\N
1068	2026-01-13 04:55:58.777319-03	101	2	ArtExhibition	37829	translate	\N	\N
1069	2026-01-13 04:56:10.87782-03	101	2	ArtExhibition	28035	translate	\N	\N
1070	2026-01-13 04:56:22.222283-03	101	2	ArtExhibition	28384	translate	\N	\N
1071	2026-01-13 04:56:34.987964-03	101	2	ArtExhibition	28682	translate	\N	\N
1072	2026-01-13 04:56:47.492305-03	101	2	ArtExhibition	37404	translate	\N	\N
1073	2026-01-13 04:56:58.485701-03	101	2	ArtExhibition	28498	translate	\N	\N
1074	2026-01-13 10:22:09.763605-03	101	2	ArtWork	884	artists	\N	\N
1075	2026-01-13 15:08:24.706891-03	100	0	ArtWork	40219	\N	\N	\N
1076	2026-01-13 15:08:24.709877-03	100	0	ArtWorkRecord	40220	\N	\N	\N
1077	2026-01-13 15:08:24.710411-03	100	0	ArtWorkRecord	40221	\N	\N	\N
1078	2026-01-13 15:08:24.710801-03	100	0	ArtWorkRecord	40222	\N	\N	\N
1079	2026-01-13 15:09:42.20507-03	100	0	Resource	40298	\N	\N	\N
1080	2026-01-13 15:09:42.215289-03	101	2	ArtWork	40219	objectType, source, name, photo	\N	\N
1081	2026-01-13 15:13:09.401997-03	101	2	ArtWork	40219	epoch, spec, info	\N	\N
1082	2026-01-13 15:13:50.892156-03	101	2	ArtWork	40219	source	\N	\N
1083	2026-01-13 15:22:34.91817-03	100	0	ArtExhibitionItem	40769	\N	\N	\N
1084	2026-01-13 15:22:34.920649-03	100	0	ArtExhibitionItemRecord	40770	\N	\N	\N
1085	2026-01-13 15:22:34.921491-03	100	0	ArtExhibitionItemRecord	40771	\N	\N	\N
1086	2026-01-13 15:22:34.922316-03	100	0	ArtExhibitionItemRecord	40772	\N	\N	\N
1087	2026-01-13 15:22:34.924261-03	100	2	ArtExhibition	27577	add item	{"id": "40769", "name": "Vara de mando de Manuel Mansilla", "class": "ArtExhibitionItem", "action": "2", "subaction": "add item"}	\N
1088	2026-01-13 15:24:05.237407-03	100	0	GuideContent	40893	\N	\N	\N
1089	2026-01-13 15:24:05.238556-03	100	0	GuideContentRecord	40894	\N	\N	\N
1090	2026-01-13 15:24:05.239072-03	100	0	GuideContentRecord	40895	\N	\N	\N
1091	2026-01-13 15:24:05.239705-03	100	0	GuideContentRecord	40896	\N	\N	\N
1092	2026-01-13 15:42:48.141813-03	101	2	ArtExhibitionItem	40769	floor, room, order	\N	\N
1093	2026-01-13 15:43:49.976029-03	101	2	GuideContent	40893	info	\N	\N
1094	2026-01-13 15:44:15.152921-03	101	2	GuideContentRecord	40895	translate	\N	\N
1095	2026-01-13 15:44:33.684692-03	101	2	GuideContentRecord	40896	translate	\N	\N
1096	2026-01-13 16:35:46.100157-03	101	2	ArtWork	34719	translate	\N	\N
\.


--
-- Data for Name: awpe; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.awpe (artwork_id, person_id) FROM stdin;
\.


--
-- Data for Name: favorite; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.favorite (id, name, namekey, title, titlekey, person_id, site_id, created, lastmodified, lastmodifieduser, language, draft) FROM stdin;
\.


--
-- Data for Name: floor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.floor (id, name, namekey, title, titlekey, floortype_id, site_id, subtitle, subtitlekey, info, infokey, floornumber, floornumberkey, map, photo, video, audio, created, lastmodified, lastmodifieduser, state, language, draft, audioautogenerate) FROM stdin;
138	Planta Baja	\N	\N	\N	\N	137	Arte europeo del siglo XII al XIX. Arte argentino del siglo XIX	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
139	Primer Piso	\N	\N	\N	\N	137		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
178	Planta Baja	\N	\N	\N	\N	177	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
179	Primer Piso	\N	\N	\N	\N	177	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
343	Planta Baja	\N	\N	\N	\N	342	Arte europeo del siglo XII al XIX. Arte argentino del siglo XIX	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
344	Primer Piso	\N	\N	\N	\N	342		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
\.


--
-- Data for Name: floorrecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.floorrecord (id, floor_id, language, name, subtitle, info, photo, video, audio, created, lastmodified, lastmodifieduser, draft, audioautogenerate) FROM stdin;
\.


--
-- Data for Name: floortype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.floortype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser, language, draft, audioautogenerate) FROM stdin;
111	Exhibición	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
112	Pabellón temporario	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
113	Administración	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
\.


--
-- Data for Name: guidecontent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.guidecontent (id, name, namekey, title, titlekey, artexhibitionguide_id, artexhibitionitem_id, subtitle, subtitlekey, info, infokey, guideorder, photo, video, audio, created, lastmodified, lastmodifieduser, state, audiokey, videokey, language, draft, masterlanguage, translation, usethumbnail, intro, spec, opens, audioautogenerate, speechaudio, audio_id) FROM stdin;
203	Morro da favela II (Pueblito)	morro-da-favela-ii-pueblito	Morro da favela II (Pueblito)	\N	202	201	\N	\N	\N	\N	0	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	1
204	Le bain de Vénus (El baño de Venus)	le-bain-de-v-nus-el-ba-o-de-venus	Le bain de Vénus (El baño de Venus)	\N	202	200	\N	\N	\N	\N	0	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	2
249	Effet de neige à Louveciennes	effet-de-neige-louveciennes	Effet de neige à Louveciennes	\N	248	234	\N	\N	\N	\N	1	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	3
250	Le Pont d`Argenteuil (El Puente de Argenteuil)	le-pont-d-argenteuil-el-puente-de-argenteuil	Le Pont d`Argenteuil (El Puente de Argenteuil)	\N	248	235	\N	\N	\N	\N	2	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	4
251	Prairies du Valhermeil près Pontoise	prairies-du-valhermeil-pr-s-pontoise	Prairies du Valhermeil près Pontoise	\N	248	235	\N	\N	\N	\N	3	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	5
252	Femme aux champs (Campesina)	femme-aux-champs-campesina	Femme aux champs (Campesina)	\N	248	237	\N	\N	\N	\N	4	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	6
253	En observation - M.Fabre, Officier de reserve	en-observation-m-fabre-officier-de-reserve	En observation - M.Fabre, Officier de reserve	\N	248	238	\N	\N	\N	\N	5	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	7
254	Vahine no te miti (Femme a la mer) (Mujer del mar)	vahine-no-te-miti-femme-a-la-mer-mujer-del-mar	Vahine no te miti (Femme a la mer) (Mujer del mar)	\N	248	239	\N	\N	\N	\N	6	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	8
899	La jungla	\N	\N	\N	894	890	\N	\N	Wifredo Lam, el más universal de los pintores cubanos. Introdujo la cultura negra en la pintura cubana y desarrolló una renovadora obra que integra elementos de origen africano y chino presentes en Cuba. \r\n\r\nEl cuadro ha sido interpretado como la síntesis de un ciclo antillano, en virtud del espacio barroco dominante y de la atmósfera creada por la asociación de lo humano, lo animal, lo vegetal y lo divino. Hay en él un vocabulario visual que evolucionó desde el paisaje de corte académico hacia un tema y un lenguaje de arte moderno. En este óleo parecen fusionarse visiones y vivencias del pintor; el mítico paisaje insular, la incorporación de contenidos e iconografías procedentes de los sistemas mágico-religiosos de origen africano extendidos en Cuba y el Caribe\r\n\r\nSe la considera una síntesis de su política, donde se mezclan surrealismo y cubismo europeos con el poder del mito característico de los cultos sincréticos del Caribe.	\N	0	\N	\N	22475	2025-11-29 13:35:14.787459-03	2025-11-29 13:35:14.787466-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	f	\N	2
35357	Traje de calle del alférez real	\N	\N	\N	33825	35278	\N	\N	El alférez real era uno de los principales funcionarios del Cabildo. Se encargaba de representar al rey en las ceremonias públicas portando el estandarte real. Además, se ocupaba de organizar las milicias en tiempos de guerra.\r\n\r\nEn las ceremonias públicas, el alférez real lucía un traje como este. La recreación fue realizada a partir del traje que perteneció a Francisco Antonio de Escalada, último alférez real del Cabildo de Buenos Aires, que forma parte de la colección del Museo. Está formado por calzón corto, chaleco y casaca. Se usaba con camisa blanca, medias del mismo color y zapatos con taco alto y grandes hebillas. En la época, este tipo de vestimenta era un signo de prestigio: contar con un traje como este era muy costoso.\r\n\r\nEsta recreación se realizó para proteger la pieza original de la exposición constante. Para replicar las medidas, se levantaron moldes a partir del traje original. \r\nSe seleccionó una tela similar y se la tiñó buscando una aproximación al color que tenía el traje en el momento de su uso.	\N	0	\N	\N	35503	2026-01-12 10:42:05.995277-03	2026-01-12 10:42:05.995281-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	2
488	Utopia del Sur	\N	Utopia del Sur	\N	411	486	\N	\N	Desde el Legado Nicolás García Uriburu trabajamos para preservar y difundir su legado artístico. Custodiamos su colección, gestionamos integralmente su obra y procesamos su archivo documental con el fin de mantener vigente su mensaje y contribuir a la reflexión sobre arte y ecología.	\N	1	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-11-21 16:07:15.938459-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	9
412	La Emperatriz Theodora	la-emperatriz-theodora	La Emperatriz Theodora	\N	411	408	\N	\N	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, representa una visión orientalista y teatral de la emperatriz bizantina Teodora, esposa de Justiniano I,  la mujer más influyente y poderosa en la historia del Imperio romano de Oriente.\r\n\r\nNo es una representación histórica, sino más bien una fantasía opulenta basada en la fascinación europea del siglo XIX por el Oriente y el pasado imperial. Benjamin-Constant era un pintor de gran técnica especializado en retratos de élite y figuras históricas: poder, lujo,  sofisticación, poses calculadas,  texturas complejas como seda y terciopelo, y entornos refinados. \r\n\r\nDe origen humilde, Theodora se ganó la vida de joven como actriz y cortesana hasta que fue elegida por Justiniano como esposa. La imagen histórica -basada en Procopio de Cesarea en la antigüedad  y Gibbon en el siglo XVIII-  es  la de una mujer astuta, inteligente y cruel. En la actualidad se considera que ese perfil es exagerado por la visión rígida que se tenía de la función social de la mujer. La visión moderna es que fue una figura política central en el Imperio Bizantino,  co-gobernante de facto con Justiniano. Participó activamente en las decisiones de gobierno. En la revuelta de Niká de 532, convenció a Justiniano de no huir y enfrentar a sus enemigos con su famosa frase -“el púrpura es una buena mortaja”-. Promovió leyes en favor de los derechos de las mujeres: prohibió el tráfico sexual forzado,  fortaleció los derechos de las mujeres en el matrimonio y el divorcio, y fundó hogares para mujeres rescatadas de la prostitución, algo sin precedentes en la época. Como resultado de los esfuerzos de Teodora, el estado de la mujer en el Imperio Bizantino fue más elevado que el del resto de las mujeres en Europa.\r\n\r\nEn la historia argentina el paralelismo inmediato es con Eva Perón.\r\n\r\nBenjamin-Constant se propuso crear un ícono visual del poder femenino y del exotismo oriental. y lo que salió, quizás involuntariamente,  es todo un símbolo de modernidad: una mujer joven, poderosa, hermosa e inteligente con mirada desafiante que se atreve a ser distinta.	\N	1	\N	\N	426	2025-06-10 15:14:26.074639-03	2025-10-28 12:26:06.382343-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	29
40893	Vara de mando de Manuel Mansilla	\N	\N	\N	33825	40769	\N	\N	La vara es un símbolo de poder que distinguía a las autoridades del Cabildo cuando ejercían funciones en la ciudad.\r\nEsta perteneció a Manuel Mansilla, aguacil mayor del Cabildo entre 1975 y 1821. Como Mansilla ejercía un cargo perpetuo, la pieza lleva en la empuñadura un monograma con sus iniciales.	\N	0	\N	\N	\N	2026-01-13 15:24:05.236551-03	2026-01-13 15:24:05.236558-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	3
294	En Normandie	en-normandie	En Normandie	\N	292	286	\N	\N	\N	\N	2	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	10
257	La Coiffure (El peinado)	la-coiffure-el-peinado	La Coiffure (El peinado)	\N	248	242	\N	\N	\N	\N	9	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	11
258	La Toilette apres le bain (El arreglo después del baño)	la-toilette-apres-le-bain-el-arreglo-despu-s-del-ba-o	La Toilette apres le bain (El arreglo después del baño)	\N	248	243	\N	\N	\N	\N	10	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	12
259	Le Moulin de la Galette	le-moulin-de-la-galette	Le Moulin de la Galette	\N	248	244	\N	\N	\N	\N	11	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	13
260	Portrait Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	portrait-ernest-hosched-et-sa-fille-marthe-retrato-del-se-or-hosched-y-su-hija	Portrait Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	\N	248	245	\N	\N	\N	\N	12	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	14
261	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	danseuse-et-admirateur-derri-re-la-sc-ne-bailarina-y-admirador-tras-la-escena	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	\N	248	246	\N	\N	\N	\N	13	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	15
262	La Nymphe surprise (La ninfa sorprendida)	la-nymphe-surprise-la-ninfa-sorprendida	La Nymphe surprise (La ninfa sorprendida)	\N	248	247	\N	\N	\N	\N	14	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	16
295	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	Sin pan y sin trabajo	\N	292	287	\N	\N	\N	\N	3	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	17
296	La vuelta al hogar	la-vuelta-al-hogar	La vuelta al hogar	\N	292	288	\N	\N	\N	\N	4	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	18
297	El despertar de la criada	el-despertar-de-la-criada	El despertar de la criada	\N	292	289	\N	\N	\N	\N	5	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	19
299	Abel	abel	Abel	\N	292	291	\N	\N	\N	\N	7	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	20
298	Reposo	reposo	Reposo	\N	292	290	\N	\N	\N	\N	6	\N	\N	338	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	21
255	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	portrait-de-suzanne-valadon-madame-suzanne-valadon-artiste-peintre-retrato-de-suzanne-valadon-pintora	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	\N	248	240	\N	\N	\N	\N	7	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	22
256	La berge de La Seine (Orillas del Sena)	la-berge-de-la-seine-orillas-del-sena	La berge de La Seine (Orillas del Sena)	\N	248	241	\N	\N	\N	\N	8	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	23
413	Retrato de Juan Manuel de Rosas	retrato-de-juan-manuel-de-rosas	Retrato de Juan Manuel de Rosas	\N	411	409	El gran restaurador	\N	El Retrato de Juan Manuel de Rosas, pintado por el artista francés Raymond Monvoisin hacia 1842, ofrece una representación simbólica y estratégica del caudillo argentino.  A diferencia de otros cuadros oficiales donde Rosas aparece con uniforme militar, en esta obra se lo muestra con un poncho, resaltando su imagen de líder popular y no meramente militar.\\nMonvoisin se había formado en la tradición académica europea, pero para 1842 estaba más cerca del romanticismo. Rosas aparece de perfil, con expresión seria y firme, destacando su autoridad, y su mirada apunta a donde se origina el viento como  controlando la naturaleza. Mitre señaló “es el retrato más parecido que conozco de Rosas, hay mucho de emperador romano pero atenuado y corregido por un acento criollo”.\\nLa tradición artística oficial argentina considera al cuadro un estudio preliminar de una obra posterior desaparecida o que no existió. Según Rodrigo Cañete en “Historia a contrapelo del arte argentino”,  esta idea está contaminada por el prejuicio de negarse a aceptar el verdadero carácter de la obra.  Para Cañete, el artista, acogido en Chile por los enemigos de Rosas, tuvo el talento de hacer un cuadro ambigüo para que ambos bandos -unitarios y federales- puedan utilizarlo como bandera. Los rasgos de Rosas son imperiales pero carecen de la musculatura heroica de los emperadores y están levemente feminizados, y el poncho y la ropa son parecidos a los retratos de sus enemigos como gaucho bárbaro, líder de lo que Sarmiento despectivamente llamaba la “montonera árabe”.  Por su parte, para los rosistas, representa un emperador-dios-benefactor-madre tierra, al estilo de la Virgen María de las imágenes sudamericanas que la fusionan con la Pachamama india, como la Virgen María del Cerro Rico de Potosí,  pero con referencias académicas sutiles a grandes maestros como Zurbarán y Velázquez.	\N	2	\N	\N	424	2025-06-10 15:14:26.074639-03	2025-09-30 10:07:01.235109-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	24
18152	Je suis (Yo soy)	\N	\N	\N	894	16643	\N	\N	\N	\N	0	\N	\N	\N	2026-01-05 10:43:44.89555-03	2026-01-05 10:43:44.895556-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	3
18156	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	894	16647	\N	\N	Las religiones afrocubanas incluyen el Lucumí, o Santería, del pueblo de habla yoruba del suroeste de Nigeria, que es el culto a los orishas o espíritus. También tenemos el Palo, de África Central, las prácticas de Benín llamadas Arará, así como ideas europeas o cristianas.\r\n\r\nEl título de esta pintura, Malembo Sombrío, Dios de la Encrucijada, hace referencia al orisha de la tradición lucumí o santería.\r\nLa palabra "Malembo" en el título podría referirse a un puerto negrero en la costa occidental de África. \r\nPara cuando Lam nació en 1902, solo habían pasado 16 años desde la abolición de la esclavitud en Cuba.\r\n\r\nEn gran parte de la obra de Lam se observa lo que podría identificarse como una figura similar a la de Eleguá: esta pequeña cabeza abovedada. Eleguá es el orisha de la encrucijada. Tiene la capacidad única de abrir puertas, comunicarse entre idiomas y atraer la buena fortuna y alejar la mala suerte.	\N	0	\N	\N	\N	2026-01-05 10:43:48.016993-03	2026-01-05 10:43:48.016996-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	4
459	Sendas perdidas	\N	Sendas perdidas	\N	458	455	\N	\N	\N	\N	1	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-11-23 19:55:45.874884-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	30
414	En la costa de Valencia	en-la-costa-de-valencia	En la costa de Valencia	\N	411	410	\N	\N	Joaquín Sorolla, el "Maestro de la Luz", inmortalizó la costa de Valencia, especialmente la Playa de la Malvarrosa, capturando la luz mediterránea y escenas costumbristas de pescadores y niños jugando en obras icónicas como "En la costa de Valencia" (1898) y "Corriendo por la playa" (1908), destacando por su estilo luminista, pinceladas vibrantes y la representación de la vida cotidiana valenciana, llena de movimiento y reflejos del sol sobre el mar.	\N	3	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-10-01 16:10:10.760825-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	28
293	La vuelta del malón	la-vuelta-del-mal-n	La vuelta del malón	\N	292	285	\N	\N	\N	\N	1	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-09-30 16:48:47.212018-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	25
460	Apocalipsis	\N	Apocalipsis	\N	458	456	\N	\N	\N	\N	2	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-10-01 13:36:52.773329-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	26
461	El recurso del método	\N	El recurso del método	\N	458	457	\N	\N	\N	\N	3	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-10-01 13:37:03.415513-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	27
\.


--
-- Data for Name: guidecontentrecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.guidecontentrecord (id, guidecontent_id, language, name, subtitle, info, photo, video, audio, created, lastmodified, lastmodifieduser, draft, namekey, state, usethumbnail, name_hash, subtitle_hash, info_hash, intro_hash, spec, spec_hash, otherjson, otherjson_hash, opens, opens_hash, audioautogenerate, intro, audioauto) FROM stdin;
741	413	pt-BR	Retrato de Juan Manuel de Rosas	\N	\N	\N	\N	\N	2025-10-26 13:15:23.412529-03	2025-10-26 13:15:23.41254-03	100	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	\N	f
747	412	pt-BR	La Emperatriz Theodora	\N	\N	\N	\N	\N	2025-10-27 14:22:40.784208-03	2025-10-27 14:22:40.784222-03	100	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	\N	f
746	412	en	Empress Theodora	\N	The painting Theodora, painted by Jean-Joseph Benjamin-Constant in 1887, depicts an Orientalist and theatrical vision of the Byzantine Empress Theodora, wife of Justinian I, the most influential and powerful woman in the history of the Eastern Roman Empire.\n\nIt is not a historical representation, but rather an opulent fantasy based on the 19th-century European fascination with the Orient and the imperial past. Benjamin-Constant was a highly technical painter specializing in portraits of elite figures and historical figures: power, luxury, sophistication, calculated poses, complex textures such as silk and velvet, and refined settings.\n\nOf humble origins, Theodora earned her living as a young woman as an actress and courtesan until she was chosen by Justinian as his wife. The historical image—based on Procopius of Caesarea in antiquity and Gibbon in the 18th century—is that of a cunning, intelligent, and cruel woman. This profile is now considered exaggerated due to the rigid view of women's social role. The modern view is that she was a central political figure in the Byzantine Empire, de facto co-ruler with Justinian. She actively participated in government decisions. In the Nika revolt of 532, she convinced Justinian not to flee and instead confront his enemies with her famous phrase, "purple is a good shroud." She promoted laws favoring women's rights: she prohibited forced sex trafficking, strengthened women's rights in marriage and divorce, and founded homes for women rescued from prostitution, something unprecedented at the time. As a result of Theodora's efforts, the status of women in the Byzantine Empire was higher than that of women in Europe.\n\nIn Argentine history, the immediate parallel is with Eva Perón.\n\nBenjamin-Constant set out to create a visual icon of feminine power and oriental exoticism. And what emerged, perhaps unintentionally, is a symbol of modernity: a young, powerful, beautiful, and intelligent woman with a defiant gaze who dares to be different.	\N	\N	\N	2025-10-27 14:22:38.757195-03	2025-10-28 12:59:31.453189-03	100	\N	\N	\N	f	-90724174	0	1629620206	0	\N	0	\N	0	\N	0	f	\N	f
819	460	en	Apocalipsis	\N	\N	\N	\N	\N	2025-11-20 08:17:29.468878-03	2025-11-20 08:17:29.468887-03	100	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	\N	f
820	460	pt-BR	Apocalipsis	\N	\N	\N	\N	\N	2025-11-20 08:17:34.019636-03	2025-11-20 08:17:34.019642-03	100	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	\N	f
903	459	en	Sendas perdidas	\N	\N	\N	\N	\N	2025-11-29 21:30:43.261445-03	2025-11-29 21:30:43.261457-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	f	\N	f
35358	35357	es	Traje de calle del alférez real	\N	\N	\N	\N	\N	2026-01-12 10:42:05.997068-03	2026-01-12 10:42:05.99707-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f
1494	459	pt-BR	Sendas perdidas	\N	\N	\N	\N	\N	2025-12-17 12:50:02.532431-03	2025-12-17 12:50:02.532442-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f
35359	35357	en	Traje de calle del alférez real	\N	\N	\N	\N	\N	2026-01-12 10:42:05.997822-03	2026-01-12 10:42:05.997823-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f
737	414	pt-BR	Na costa de Valência	\N	Joaquín Sorolla, o "Mestre da Luz", imortalizou o litoral de Valência, em especial a Praia de Malvarrosa, capturando a luz do Mediterrâneo e cenas cotidianas de pescadores e crianças brincando em obras icônicas como "Na Costa de Valência" (1898) e "Correndo na Praia" (1908), destacando-se pelo seu estilo luminista, pinceladas vibrantes e a representação da vida diária valenciana, repleta de movimento e reflexos do sol no mar.	\N	\N	\N	2025-10-25 17:27:53.087335-03	2025-12-08 12:15:27.725212-03	100	\N	\N	\N	f	-1476067600	0	-1452833376	0	\N	0	\N	0	\N	0	f	\N	f
736	414	en	On the coast of Valencia	\N	Joaquín Sorolla, the "Master of Light", immortalized the coast of Valencia, especially Malvarrosa Beach, capturing the Mediterranean light and everyday scenes of fishermen and children playing in iconic works such as "On the Coast of Valencia" (1898) and "Running on the Beach" (1908), standing out for his luminist style, vibrant brushstrokes and the representation of Valencian daily life, full of movement and reflections of the sun on the sea.	\N	\N	\N	2025-10-25 17:27:21.91188-03	2025-12-18 13:53:58.905467-03	100	\N	\N	\N	f	-1476067600	0	-1452833376	0	\N	0	\N	0	\N	0	f	\N	f
35360	35357	pt-BR	Traje de calle del alférez real	\N	\N	\N	\N	\N	2026-01-12 10:42:05.998374-03	2026-01-12 10:42:05.998374-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f
922	488	en	Southern Utopia	\N	At the Nicolás García Uriburu Legacy, we work to preserve and disseminate his artistic legacy. We safeguard his collection, manage his work comprehensively, and process his documentary archive in order to keep his message relevant and contribute to reflection on art and ecology.	\N	\N	\N	2025-12-04 10:01:38.699045-03	2025-12-18 16:17:42.382131-03	100	\N	\N	\N	t	-2142313869	0	-909044814	0	\N	0	\N	0	\N	0	f	\N	f
740	413	en	Portrait of Juan Manuel de Rosas	The great restorer	The Portrait of Juan Manuel de Rosas, painted by the French artist Raymond Monvoisin around 1842, offers a symbolic and strategic representation of the Argentine caudillo. Unlike other official portraits where Rosas appears in military uniform, in this work he is shown wearing a poncho, highlighting his image as a popular leader and not merely a military figure. Monvoisin had trained in the European academic tradition, but by 1842 he was closer to Romanticism. Rosas appears in profile, with a serious and firm expression, emphasizing his authority, and his gaze points toward the source of the wind, as if controlling nature. Mitre noted, "It is the most accurate portrait I know of Rosas; there is much of the Roman emperor in it, but tempered and corrected by a Creole accent." The official Argentine artistic tradition considers the painting a preliminary study for a later work that has disappeared or never existed. According to Rodrigo Cañete in "Historia a contrapelo del arte argentino" (A History Against the Grain of Argentine Art), this idea is tainted by the prejudice of refusing to accept the true nature of the work. According to Cañete, the artist, welcomed in Chile by Rosas's enemies, had the talent to create an ambiguous painting that both sides—Unitarians and Federalists—could use as a symbol. Rosas's features are imperial but lack the heroic musculature of emperors and are slightly feminized, and his poncho and clothing resemble portraits of his enemies as a barbaric gaucho, leader of what Sarmiento contemptuously called the "Arab mob." For the Rosas supporters, he represents an emperor-god-benefactor-mother earth, in the style of the Virgin Mary in South American images that merge her with the indigenous Pachamama, like the Virgin Mary of Cerro Rico in Potosí, but with subtle academic references to great masters like Zurbarán and Velázquez.	\N	\N	\N	2025-10-26 13:15:16.71149-03	2025-12-18 16:18:28.711548-03	100	\N	\N	\N	f	-1516229879	685711169	-1497012282	0	\N	0	\N	0	\N	0	f	\N	f
1108	488	pt-BR	Utopia do Sul	\N	No Legado Nicolás García Uriburu, trabalhamos para preservar e divulgar seu legado artístico. Salvaguardamos sua coleção, gerenciamos sua obra de forma abrangente e processamos seu arquivo documental para manter sua mensagem relevante e contribuir para a reflexão sobre arte e ecologia.	\N	\N	\N	2025-12-08 17:57:56.718589-03	2025-12-24 17:36:15.831513-03	100	\N	\N	\N	t	-2142313869	0	-909044814	0	\N	0	\N	0	\N	0	t	\N	f
18153	18152	es	Je suis (Yo soy)	\N	\N	\N	\N	\N	2026-01-05 10:43:44.901766-03	2026-01-05 10:43:44.901768-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f
18154	18152	en	Je suis (Yo soy)	\N	\N	\N	\N	\N	2026-01-05 10:43:44.902947-03	2026-01-05 10:43:44.902947-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f
18155	18152	pt-BR	Je suis (Yo soy)	\N	\N	\N	\N	\N	2026-01-05 10:43:44.903578-03	2026-01-05 10:43:44.903578-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f
18157	18156	es	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	\N	\N	2026-01-05 10:43:48.019403-03	2026-01-05 10:43:48.019405-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f
18158	18156	en	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	\N	\N	2026-01-05 10:43:48.021261-03	2026-01-05 10:43:48.021263-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f
18159	18156	pt-BR	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	\N	\N	2026-01-05 10:43:48.022806-03	2026-01-05 10:43:48.022808-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f
902	899	pt-BR	A selva	\N		\N	\N	914	2025-11-29 13:35:14.791836-03	2025-11-30 17:57:48.104084-03	100	\N	\N	\N	t	1098792388	0	402120604	0	\N	0	\N	0	\N	0	f	\N	f
901	899	en	The jungle	\N	Wifredo Lam, the most universal of Cuban painters, introduced Black culture into Cuban painting and developed a groundbreaking body of work that integrates elements of African and Chinese origin present in Cuba.\n\nThe painting has been interpreted as the synthesis of an Antillean cycle, by virtue of the dominant Baroque space and the atmosphere created by the association of the human, the animal, the vegetal, and the divine. It contains a visual vocabulary that evolved from academic landscape painting toward a theme and language of modern art. In this oil painting, the painter's visions and experiences seem to merge: the mythical island landscape, the incorporation of content and iconography from the magical-religious systems of African origin prevalent in Cuba and the Caribbean.\n\nIt is considered a synthesis of his political philosophy, where European Surrealism and Cubism are mixed with the power of myth characteristic of the syncretic cults of the Caribbean.	\N	\N	918	2025-11-29 13:35:14.791452-03	2026-01-06 15:11:18.615127-03	100	\N	\N	\N	t	1098792388	0	1842841698	0	\N	0	\N	0	\N	0	f	\N	f
40894	40893	es	Vara de mando de Manuel Mansilla	\N	\N	\N	\N	\N	2026-01-13 15:24:05.238085-03	2026-01-13 15:24:05.238089-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f
40895	40893	en	Manuel Mansilla's staff of office	\N	The staff is a symbol of power that distinguished the authorities of the Cabildo (town council) when they held office in the city.\nThis one belonged to Manuel Mansilla, chief constable of the Cabildo between 1795 and 1821. As Mansilla held a perpetual position, the staff bears a monogram with his initials on the handle.	\N	\N	\N	2026-01-13 15:24:05.238844-03	2026-01-13 15:44:15.152576-03	100	\N	\N	1	t	-1331906940	0	-995107724	0	\N	0	\N	0	\N	0	t	\N	f
40896	40893	pt-BR	Equipe de escritório de Manuel Mansilla	\N	O bastão é um símbolo de poder que distinguia as autoridades do Cabildo (conselho municipal) quando exerciam funções na cidade.\nEste pertenceu a Manuel Mansilla, chefe de polícia do Cabildo entre 1795 e 1821. Como Mansilla ocupava um cargo perpétuo, o bastão traz um monograma com suas iniciais no cabo.	\N	\N	\N	2026-01-13 15:24:05.239319-03	2026-01-13 15:44:33.684338-03	100	\N	\N	1	t	-1331906940	0	-995107724	0	\N	0	\N	0	\N	0	t	\N	f
\.


--
-- Data for Name: institution; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institution (id, name, namekey, shortname, title, titlekey, institutiontype_id, subtitle, subtitlekey, info, infokey, address, addresskey, moreinfo, moreinfokey, website, mapurl, email, instagram, whatsapp, phone, twitter, logo, photo, video, audio, map, created, lastmodified, lastmodifieduser, state, language, draft, masterlanguage, translation, usethumbnail, intro, spec, opens, audioautogenerate, speechaudio, zoneid) FROM stdin;
135	Museo Nacional de Bellas Artes	museo-nacional-de-bellas-artes	MNBA	\N	\N	\N	Posee la mayor colección de arte argentino y una de las más importantes de arte universal en toda Latinoamérica.	\N	El Museo Nacional de Bellas Artes (MNBA), ubicado en la Ciudad de Buenos Aires, es una de las instituciones públicas de arte más importantes de Argentina. Alberga un patrimonio sumamente diverso, que incluye más de 12 000 piezas, entre pinturas, esculturas, dibujos, grabados, textiles y objetos. Su colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad.	\N	Av. Del Libertador 1473 \r\nCiudad Autónoma de Buenos Aires \r\nArgentina	\N	\N	\N	https://www.bellasartes.gob.ar/	https://maps.app.goo.gl/UmGLqFoGb2vkP1UV6	\N	\N	+54 (011) 5288-9900	+54 (011) 5288-9900	\N	535	556	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-08-29 08:48:11.347636-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires
136	Museo Quinquela Martín	museo-quinquela-mart-n	Museo Quinquela	\N	\N	\N	En la casa donde vivió Quinquela.	\N	El Museo de Bellas Artes de La Boca de Artistas Argentinos &quot;Benito Quinquela Martín&quot; ubicado en el corazón de La Boca es una de las Instituciones que el artista boquense donó al barrio con la intención de crear un polo de desarrollo cultural, educativo y sanitario. \r\n\r\nComprometido con los procesos educativos, el Museo, promueve una concepción del arte como factor decisivo en los procesos cotidianos de construcción de identidad.	\N	Avenida Don Pedro de Mendoza 1835, \r\nCiudad Autónoma de Buenos Aires\r\nC1169	\N	\N	\N	https://buenosaires.gob.ar/educacion/gestion-cultural/museo-benito-quinquela-martin	\N	\N	\N	\N	\N	\N	\N	559	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-10-24 13:04:58.067073-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires
341	Museo de Arte Latinoaméricano de Buenos Aires	museo-de-arte-latinoam-ricano-de-buenos-aires	MALBA	\N	\N	\N		\N	El MALBA es una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región. Se specializa en el arte latinoamericano desde principios del siglo XX hasta la actualidad.	\N	Av. Figueroa Alcorta 3415\r\nC1425CLA Buenos Aires, Argentina\r\n+54 11 4808 6500	\N	\N	\N	https://www.malba.org.ar/	https://maps.app.goo.gl/YYBFEzrL9WkGoQc97	\N	\N	\N	011 4808-6500	\N	538	560	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-10-25 08:10:23.44224-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires
553	Museo Moderno	new	\N	new	new	\N	En el barrio de San Telmo, el museo alberga más de 7000 obras de arte argentino e internacional.	\N	En el corazón del Casco Histórico, en pleno barrio de San Telmo, el Museo Moderno pone a tu disposición una rica colección del arte argentino de los últimos años.\r\nLa propuesta del Moderno es acercarte a las artes visuales a través de sus 11.000 m2 y sus más de 7.000 obras.\r\nA su vez, el edificio es un fiel exponente de las construcciones inglesas de la era industrial del siglo XIX, con su estructura de hierro, grandes aberturas y su fachada de ladrillo a la vista. \r\nLa experiencia se complementa con el bar, que ofrece un excelente café de especialidad y un menú diseñado especialmente. No te olvides de visitar la tienda donde encontrarás todos los libros editados por el Moderno, una oportunidad para sumergirte en el universo de los artistas a través de estas publicaciones.	\N	Avenida San Juan 350	\N	\N	\N	museomoderno.org	\N	info@museomoderno.org	\N	\N	011 4361 6919	\N	\N	554	\N	\N	\N	2025-09-10 18:11:10.779338-03	2025-09-10 18:13:51.010464-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires
547	Basílica Nuestra Señora del Pilar	new	Iglesia del Pilar	\N	new	\N	Se inauguró en 1732. Conserva retablos, imaginería y ornamentos originales.	\N	La iglesia de Nuestra Señora del Pilar es una basílica ubicada en el barrio de Recoleta en Buenos Aires; en su día formó parte del convento de Franciscanos recoletos. Su construcción, que concluyó en 1732, se debe al mecenas aragonés Juan de Narbona.  Desde el siglo XIX es una de las parroquias de la ciudad de Buenos Aires y el segundo templo más antiguo de la ciudad.	\N	Junín 1898 \r\nCiudad Autónoma de Buenos Aires	\N	\N	\N	https://basilicadelpilar.org/	https://maps.app.goo.gl/x1ceVM3UppVU4P4u9	\N	\N	\N	\N	\N	\N	558	\N	\N	\N	2025-08-29 16:42:37.508525-03	2025-11-23 14:08:55.926126-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires
821	institucion no usada	\N	nousada	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-11-20 08:20:16.4303-03	2025-11-23 21:19:51.061528-03	100	5	es	\N	es	\N	f	\N	\N	\N	f	\N	America/Buenos_Aires
561	Colección de Arte Amalia Lacroze de Fortabat	new	Museo Fortabat	new	new	\N	Posee más de 150 obras de reconocidos artistas internacionales.	\N	Esta importante colección privada de arte (inaugurada en 2008 y reabierta en 2012) posee más de 150 obras de reconocidos artistas internacionales como Rodin, Warhol, Turner, Dalí y Blanes, así como de artistas argentinos tales como Badii, Berni, Quinquela Martín, Noé, Pérez Celis, Fader, Soldi y Xul Solar, entre otros.	\N	OLGA  COSSETTINI  141	\N	\N	\N	www.coleccionfortabat.org.ar	\N	\N	\N	\N	\N	\N	563	562	\N	\N	\N	2025-09-12 14:08:34.685796-03	2025-09-12 14:13:40.801927-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires
749	Museo Nacional de Arte Decorativo	\N	Museo de Arte Decorativo	\N	\N	\N	\N	\N	Colección de piezas de artes decorativas europeas y orientales, esculturas, tapices y pinturas de los siglos XVI al XIX.	\N	Av. del Libertador 1902, Ciudad de Buenos Aires.	\N	\N	\N	https://museoartedecorativo.cultura.gob.ar/	\N	decorativocontacto@gmail.com	\N	\N	+54 (011) 4806-8306	\N	751	752	\N	\N	\N	2025-10-28 14:37:01.611961-03	2025-10-28 14:49:35.311694-03	100	3	es	\N	es	\N	f	\N	\N	\N	f	\N	America/Buenos_Aires
529	Cabildo de Buenos Aires	new		\N	\N	\N	La sede de la administración colonial ocupa el mismo lugar desde 1580 y es un emblema de la historia viva. Visitá el corazón de la Revolución de Mayo.	\N	En este edificio funcionó el cabildo colonial fundado por Juan de Garay en 1580 durante la segunda fundación de la ciudad de Buenos Aires y que luego de la Revolución de Mayo de 1810, que derrocó al virrey español Baltasar Hidalgo de Cisneros y derivó en la guerra que llevó a la independencia de las Provincias Unidas del Río de la Plata, se transformó en una Junta de Gobierno que funcionó hasta su disolución en 1821 por el gobernador de Buenos Aires Martín Rodríguez.	\N	Bolívar 65\r\nCiudad Autónoma de Buenos Aires	\N	\N	\N	https://cabildonacional.cultura.gob.ar/	https://maps.app.goo.gl/eooviVBRmuy38Zcc7	cabildonacional@gmail.com	\N	\N	(011) 15 22642778\r\n(011) 4342-6729 / 4334-1782	\N	536	557	\N	\N	\N	2025-08-26 17:08:31.243467-03	2025-11-25 10:44:35.929448-03	100	1	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires
855	Museo Latinoamericano de Arte Moderno	\N	MAM	\N	\N	\N	\N	\N	En el Museo de Arte Moderno  celebramos la creatividad, la apertura, la tolerancia y la generosidad. Nuestro objetivo es ser espacios inclusivos, tanto presenciales como virtuales, donde se aceptan diversas posturas culturales, artísticas, sociales y políticas. Nos comprometemos a compartir el arte moderno y contemporáneo más sugerente, y esperamos que nos acompañen a explorar el arte, las ideas y los problemas de nuestro tiempo.	\N	11 West 53 Street, Manhattan	\N	\N	\N	https://www.moma.org	\N	\N	\N	\N	\N	\N	\N	859	\N	\N	\N	2025-11-28 10:29:38.266203-03	2025-11-28 10:43:15.609876-03	100	1	es	\N	es	\N	t	\N	\N	\N	f	\N	America/Buenos_Aires
\.


--
-- Data for Name: institutionalcontent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institutionalcontent (id, name, namekey, title, titlekey, institution_id, site_id, subtitle, subtitlekey, info, infokey, photo, video, audio, created, lastmodified, lastmodifieduser, state, language, draft, audioautogenerate) FROM stdin;
\.


--
-- Data for Name: institutionrecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institutionrecord (id, institution_id, language, name, subtitle, info, photo, video, audio, created, lastmodified, lastmodifieduser, draft, namekey, state, intro, usethumbnail, subtitle_hash, name_hash, info_hash, intro_hash, spec, spec_hash, otherjson, otherjson_hash, opens, opens_hash, audioautogenerate, audioauto) FROM stdin;
691	341	pt-BR	Museu de Arte Latino-Americana de Buenos Aires	Pinturas de Constantini	O MALBA é uma instituição privada sem fins lucrativos que preserva e expõe um acervo de aproximadamente 400 obras dos principais artistas modernos e contemporâneos da região. Especializa-se em arte latino-americana do início do século XX até os dias atuais.	\N	\N	\N	2025-10-23 14:02:23.160757-03	2025-10-23 14:02:23.160764-03	100	\N	\N	\N	\N	f	1461115856	-1939312907	1465754903	0	\N	0	\N	0	\N	0	t	f
705	553	en	Museo Moderno	\N	\N	\N	\N	\N	2025-10-24 10:31:47.859244-03	2025-10-24 10:31:47.859255-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	t	f
706	553	pt-BR	Museo Moderno	\N	\N	\N	\N	\N	2025-10-24 10:31:50.079668-03	2025-10-24 10:31:50.079676-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	t	f
704	136	en	Quinquela Martín Museum	In the house where Quinquela lived.	The Benito Quinquela Martín Museum of Fine Arts of La Boca, a museum of Argentine artists, located in the heart of La Boca, is one of the institutions that the Boca artist donated to the neighborhood with the intention of creating a center for cultural, educational, and healthcare development.\n\nCommitted to educational processes, the Museum promotes a concept of art as a decisive factor in the daily processes of identity building.	\N	\N	\N	2025-10-24 10:22:10.326397-03	2025-10-24 10:22:10.326403-03	100	\N	\N	\N	\N	f	-1146537808	1721673141	-2000556722	0	\N	0	\N	0	\N	0	t	f
692	341	en	Museum of Latin American Art of Buenos Aires	Constantini's paintings	MALBA is a private, non-profit institution that preserves and exhibits a collection of approximately 400 works by the region&#39;s leading modern and contemporary artists. It specializes in Latin American art from the early 20th century to the present.	\N	\N	\N	2025-10-23 14:30:09.938437-03	2025-10-23 14:30:09.938455-03	100	\N	\N	\N	\N	f	1001393393	-1939312907	1465754903	0	\N	0	\N	0	\N	0	t	f
690	136	pt-BR	Museu Quinquela Martín	Na casa onde Quinquela morava.	O Museu de Belas Artes Benito Quinquela Martín de La Boca, um museu de artistas argentinos, localizado no coração de La Boca, é uma das instituições que o artista de Boca doou ao bairro com a intenção de criar um centro de desenvolvimento cultural, educacional e de saúde.\n\nComprometido com processos educacionais, o Museu promove a concepção da arte como fator decisivo nos processos cotidianos de construção de identidade.	\N	\N	\N	2025-10-23 14:01:12.389036-03	2025-10-23 14:01:12.389062-03	100	\N	\N	\N	\N	f	-1146537808	1721673141	-2000556722	0	\N	0	\N	0	\N	0	t	f
689	135	en	National Museum of Fine Arts	It has the largest collection of Argentine art and one of the most important collections of universal art in all of Latin America.	The National Museum of Fine Arts (MNBA), located in the City of Buenos Aires, is one of the most important public art institutions in Argentina. \r\n\r\nIt houses a very diverse collection, including more than 12,000 pieces, including paintings, sculptures, drawings, prints, textiles, and objects. \r\nIts collection includes pre-Columbian, colonial, Argentine, and international art, spanning the 3rd century BC to the present.	\N	\N	\N	2025-10-23 12:03:15.325376-03	2025-10-23 12:03:15.325392-03	100	\N	\N	\N	\N	f	1113148317	289070387	869905709	0	\N	0	\N	0	\N	0	t	f
702	547	en	Basilica of Our Lady of the Pillar	It was inaugurated in 1732. It preserves original altarpieces, imagery and ornaments.	The Church of Our Lady of the Pillar is a basilica located in the Recoleta neighborhood of Buenos Aires; it was once part of the Franciscan Recollect convent. Its construction, completed in 1732, was due to the Aragonese patron Juan de Narbona. Since the 19th century, it has been one of the parishes of Buenos Aires and the second oldest church in the city.	\N	\N	\N	2025-10-23 18:36:26.182086-03	2025-10-25 00:08:27.246694-03	100	\N	\N	\N	\N	f	-1127882839	357236140	-1218405812	0	\N	0	\N	0	\N	0	t	f
703	547	pt-BR	Basílica de Nossa Senhora do Pilar	Foi inaugurada em 1732. Conserva retábulos, imagens e ornamentos originais.	A Igreja de Nossa Senhora do Pilar é uma basílica localizada no bairro da Recoleta, em Buenos Aires; antigamente fazia parte do convento franciscano recoleto. Sua construção, concluída em 1732, deveu-se ao padroeiro aragonês Juan de Narbona. Desde o século XIX, é uma das paróquias de Buenos Aires e a segunda igreja mais antiga da cidade.	\N	\N	\N	2025-10-23 18:36:27.92656-03	2025-10-25 08:12:59.956258-03	100	\N	\N	\N	\N	f	-1127882839	357236140	-1218405812	0	\N	0	\N	0	\N	0	t	f
688	135	pt-BR	Museu Nacional de Belas Artes	Possui o maior acervo de arte argentina e um dos mais importantes acervos de arte universal de toda a América Latina.	O Museu Nacional de Belas Artes (MNBA), localizado na Cidade de Buenos Aires, é uma das instituições de arte pública mais importantes da Argentina. Abriga um acervo extremamente diversificado, com mais de 12.000 peças, entre pinturas, esculturas, desenhos, gravuras, tecidos e objetos. Seu acervo abrange arte pré-colombiana, colonial, argentina e internacional, abrangendo o século III a.C. até o presente.	\N	\N	\N	2025-10-22 22:46:42.444606-03	2025-10-22 22:46:42.444619-03	100	\N	\N	\N	\N	f	1113148317	289070387	869905709	0	\N	0	\N	0	\N	0	t	f
744	561	en	Colección de Arte Amalia Lacroze de Fortabat	\N	\N	\N	\N	\N	2025-10-27 14:17:39.791274-03	2025-10-27 14:17:39.791282-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	f
745	561	pt-BR	Coleção de Arte Amalia Lacroze de Fortabat	Possui mais de 150 obras de renomados artistas internacionais.	Esta importante coleção de arte privada (inaugurada em 2008 e reaberta em 2012) conta com mais de 150 obras de renomados artistas internacionais como Rodin, Warhol, Turner, Dalí e Blanes, além de artistas argentinos como Badii, Berni, Quinquela Martín, Noé, Pérez Celis, Fader, Soldi e Xul Solar, entre outros.	\N	\N	\N	2025-10-27 14:18:16.001116-03	2025-10-27 14:18:19.069625-03	100	\N	\N	\N	\N	f	1077638368	1141487491	100525823	0	\N	0	\N	0	\N	0	f	f
822	821	es	new	\N	\N	\N	\N	\N	2025-11-20 08:20:16.434523-03	2025-11-20 08:20:16.434554-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	f
824	821	pt-BR	new	\N	\N	\N	\N	\N	2025-11-20 08:20:16.439397-03	2025-11-20 08:20:16.439401-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	f
856	855	es	new	\N	\N	\N	\N	\N	2025-11-28 10:29:38.289095-03	2025-11-28 10:29:38.28911-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	f	f
858	855	pt-BR	new	\N	\N	\N	\N	\N	2025-11-28 10:29:38.292185-03	2025-11-28 10:29:38.292192-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	f	f
857	855	en	Latin American Museum of Modern Art	\N	At the Museum of Modern Art, we celebrate creativity, openness, tolerance, and generosity. Our goal is to be inclusive spaces, both in person and online, where diverse cultural, artistic, social, and political perspectives are welcome. We are committed to sharing the most thought-provoking modern and contemporary art, and we hope you will join us in exploring the art, ideas, and issues of our time.	\N	\N	\N	2025-11-28 10:29:38.291219-03	2025-12-19 08:54:35.095415-03	100	\N	\N	\N	\N	t	0	-112572694	421228180	0	\N	0	\N	0	\N	0	f	f
941	529	en	Cabildo de Buenos Aires	\N	\N	\N	\N	\N	2025-12-05 20:41:43.52772-03	2025-12-05 20:41:43.527728-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
823	821	en	not used	\N	\N	\N	\N	\N	2025-11-20 08:20:16.437676-03	2025-11-20 08:20:16.437681-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	f
\.


--
-- Data for Name: institutiontype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institutiontype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser, state, language, draft, audioautogenerate) FROM stdin;
100	Museo	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	3	\N	\N	t
101	Monumento	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	3	\N	\N	t
102	Centro cultural	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	3	\N	\N	t
103	Parque	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	3	\N	\N	t
104	Edificio religioso	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	3	\N	\N	t
105	Teatro	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	3	\N	\N	t
106	Plaza	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	3	\N	\N	t
107	Edificio histórico	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	3	\N	\N	t
108	Galería de Arte	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	3	\N	\N	t
109	Zoo	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	3	\N	\N	t
\.


--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person (id, name, namekey, title, titlekey, lastname, lastnamekey, displayname, nickname, sex, physicalid, address, zipcode, phone, email, birthdate, user_id, subtitle, subtitlekey, info, infokey, photo, video, audio, created, lastmodified, lastmodifieduser, sortlastfirstname, isex, webpage, state, language, draft, translation, masterlanguage, usethumbnail, intro, spec, opens, audioautogenerate, speechaudio) FROM stdin;
194	Tarsila	tarsila	do Amaral Tarsila	\N	do Amaral	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	do amaral tarsila	0	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
188	Berthe Marie Pauline	berthe-marie-pauline	Morisot Berthe Marie Pauline	\N	Morisot	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	morisot berthe marie pauline	0	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
182	Alfred	alfred	Sisley Alfred	\N	Sisley	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	sisley alfred	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
183	Claude	claude	Monet Claude	\N	Monet	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	monet claude	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
184	Camille	camille	Pissarro Camille	\N	Pissarro	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	pissarro camille	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
185	Henri	henri	Toulouse Lautrec Henri	\N	Toulouse Lautrec	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	toulouse lautrec henri	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
186	Paul	paul	Gauguin Paul	\N	Gauguin	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	gauguin paul	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
187	Edgar	edgar	Degas Edgar	\N	Degas	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	degas edgar	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
189	Vicent	vicent	Van Gogh Vicent	\N	Van Gogh	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	van gogh vicent	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
190	Edouard	edouard	Manet Edouard	\N	Manet	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	manet edouard	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
191	Jean Louis	jean-louis	Forain Jean Louis	\N	Forain	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	forain jean louis	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
192	Doménikos	dom-nikos	Theotokópoulos Doménikos	\N	Theotokópoulos	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	theotokópoulos doménikos	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
193	Odilon	odilon	Redon Odilon	\N	Redon	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	redon odilon	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
181			root 	\N	root	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	100	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	root 	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
263	Ángel	ngel	Della Valle Ángel	\N	Della Valle	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	della valle Ángel	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
264	María	mar-a	Obligado María	\N	Obligado	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	obligado maría	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
265	Ernesto	ernesto	de la Cárcova Ernesto	\N	de la Cárcova	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	de la cárcova ernesto	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
266	Graciano	graciano	Mendihalarzu Graciano	\N	Mendihalarzu	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	mendihalarzu graciano	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
267	Eduardo	eduardo	Sívori Eduardo	\N	Sívori	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	sívori eduardo	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
268	Eduardo	eduardo	Schiaffino Eduardo	\N	Schiaffino	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	schiaffino eduardo	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
269	Lucio	lucio	Correa Morales Lucio	\N	Correa Morales	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	correa morales lucio	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
308	Rafael	rafael	Barredas Rafael	\N	Barredas	barredas	Rafael Barredas	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:53:48.69726-03	2025-05-19 12:53:48.69726-03	100	barredas rafael	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
333	Benito	benito	Quinquela Martín Benito	\N	Quinquela Martín	quinquela-mart-n	Benito Quinquela Martín	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 14:07:33.533893-03	2025-05-19 14:07:33.533893-03	100	quinquela martín benito	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
180	Alejandro		- Alejandro	\N	-	\N	\N	\N	\N	Tolomei	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	- alejandro	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
398	Joaquín	\N	\N	\N	Sorolla y Bastida	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	sorolla y bastida joaquín	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
399	Kazuya	\N	\N	\N	Sakai	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	sakai kazuya	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
400	Raymond Auguste	\N	\N	\N	Quinsac Monvoisin	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	quinsac monvoisin raymond auguste	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
447	Germán	\N	\N	\N	Gárgano	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	gárgano germán	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
401	Jean Joseph Benjamin	\N	\N	\N	Constant	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	constant jean joseph benjamin	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
480	Nicolás	\N	\N	\N	García Uriburu	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100	garcía uriburu nicolás	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
588	Alejandro	new	new new	\N	Xul Solar	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-09-19 10:50:03.260804-03	2025-09-19 10:50:12.994796-03	100	xul solar alejandro	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
593	María	new	new new	\N	Martins	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-09-19 20:06:54.869553-03	2025-09-19 20:07:10.855372-03	100	martins maría	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
581	Roberto	new	new new	\N	Matta	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-09-19 09:55:19.878194-03	2025-09-21 19:56:06.818865-03	100	matta roberto	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
602	Carrie	new	new new	\N	Bencardino	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-09-21 19:57:10.72936-03	2025-09-21 19:57:45.082858-03	100	bencardino carrie	1	\N	1	es	\N	0	es	t	\N	\N	\N	t	\N
481	Mariano	\N	\N	\N	Fortuny y Marsal	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-10-15 18:34:58.958093-03	100	fortuny y marsal mariano	1	\N	2	es	\N	0	es	t	\N	\N	\N	t	\N
763	Alberto	\N	\N	\N	Churba	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-10-29 08:56:13.397897-03	2025-10-29 09:33:45.135039-03	100	churba alberto	1	\N	3	es	\N	\N	es	f	\N	\N	\N	f	\N
880	Wilfredo	\N	\N	\N	Lam	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-11-29 07:32:12.850213-03	2025-11-29 07:32:12.850221-03	100	lam wilfredo	1	\N	1	es	\N	\N	es	t	\N	\N	\N	f	\N
1113	Alejandro	\N	\N	\N	Tolomei	new	\N	\N	\N	\N	Montañeses 1853	\N	5491161194075	atolomei@novamens.com	\N	101	\N	\N	\N	\N	1730	\N	\N	2025-12-11 18:26:14.807884-03	2025-12-11 18:26:14.80789-03	100	tolomei alejandro	1	\N	3	es	\N	\N	es	t	\N	\N	\N	t	\N
34768	ff	\N	\N	\N	Cabildo de Buenos Aires	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2026-01-12 10:32:28.745169-03	2026-01-12 10:32:28.745178-03	100	cabildo de buenos aires ff	1	\N	3	es	\N	\N	es	t	\N	\N	\N	t	\N
\.


--
-- Data for Name: personrecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.personrecord (id, person_id, language, name, subtitle, info, photo, video, audio, created, lastmodified, lastmodifieduser, draft, intro, state, usethumbnail, name_hash, subtitle_hash, info_hash, intro_hash, spec, spec_hash, otherjson, otherjson_hash, opens, opens_hash, audioautogenerate, audioauto, lastname) FROM stdin;
881	880	es	new	\N	\N	\N	\N	\N	2025-11-29 07:32:12.874439-03	2025-11-29 07:32:12.874442-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	f	f	\N
882	880	en	new	\N	\N	\N	\N	\N	2025-11-29 07:32:12.885375-03	2025-11-29 07:32:12.885379-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	f	f	\N
883	880	pt-BR	new	\N	\N	\N	\N	\N	2025-11-29 07:32:12.88769-03	2025-11-29 07:32:12.887691-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	f	f	\N
1114	1113	es	new	\N	\N	\N	\N	\N	2025-12-11 18:26:14.828688-03	2025-12-11 18:26:14.82869-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	\N
1115	1113	en	new	\N	\N	\N	\N	\N	2025-12-11 18:26:14.837577-03	2025-12-11 18:26:14.837578-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	\N
1116	1113	pt-BR	new	\N	\N	\N	\N	\N	2025-12-11 18:26:14.839872-03	2025-12-11 18:26:14.839872-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	\N
34769	34768	es	new	\N	\N	\N	\N	\N	2026-01-12 10:32:28.759135-03	2026-01-12 10:32:28.759146-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	new
34770	34768	en	new	\N	\N	\N	\N	\N	2026-01-12 10:32:28.765435-03	2026-01-12 10:32:28.765441-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	new
34771	34768	pt-BR	new	\N	\N	\N	\N	\N	2026-01-12 10:32:28.770063-03	2026-01-12 10:32:28.770067-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	new
\.


--
-- Data for Name: privilege; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.privilege (id, name, created, lastmodified, lastmodifieduser, state) FROM stdin;
925	read	2025-12-04 11:17:41.927896-03	2025-12-04 11:17:41.927896-03	100	1
926	create	2025-12-04 11:18:36.786763-03	2025-12-04 11:18:36.786763-03	100	1
927	write	2025-12-04 11:18:36.788393-03	2025-12-04 11:18:36.788393-03	100	1
928	delete	2025-12-04 11:18:36.789107-03	2025-12-04 11:18:36.789107-03	100	1
\.


--
-- Data for Name: resource; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.resource (id, name, namekey, title, titlekey, media, info, infokey, bucketname, objectname, binaryobject, created, lastmodified, lastmodifieduser, usethumbnail, size, height, width, state, durationmilliseconds, tag, language, draft, audioautogenerate, audit, filename) FROM stdin;
813	sendas-perdidas-458-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	sendas-perdidas-458-music-218	\N	2025-11-17 14:45:00.695281-03	2025-11-17 14:45:01.434009-03	100	f	5249179	0	0	\N	328000	\N	\N	\N	f		sendas-perdidas-458-music.mp3
844	mnba-captura-3.png	\N	\N	\N	image/png	\N	\N	media	mnba-captura-3-225	\N	2025-11-24 13:06:31.802949-03	2025-11-24 13:06:31.875047-03	100	t	2690312	1024	1536	\N	0	\N	\N	\N	f		mnba-captura-3.png
911	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	wifredo-lam-cuando-no-duermo-sue-o-894-music-237	\N	2025-11-30 17:48:33.882903-03	2025-11-30 17:48:33.882906-03	100	f	917032	0	0	\N	57000	\N	\N	\N	f		wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3
423	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-146	\N	2025-06-12 13:06:22.871895-03	2025-11-12 11:33:17.707344-03	100	t	1879249	0	0	1	156000	\N	\N	\N	f		theodora.mp3
636	museo-sectreto.mp3	museo-sectreto-mp3	\N	\N	audio/mp3	\N	\N	media	museo-sectreto-187	\N	2025-10-01 16:14:53.604237-03	2025-11-12 11:33:17.707382-03	100	t	1641953	0	0	\N	137000	\N	\N	\N	f		museo-sectreto.mp3
1492	mlam-logo.svg	\N	\N	\N	image/svg+xml	\N	\N	media	mlam-logo-248	\N	2025-12-15 12:56:29.276536-03	2025-12-15 12:56:29.276538-03	100	f	1183	0	0	\N	0	\N	\N	\N	f	\N	mlam-logo.svg
1493	mlam-logo-2.svg	\N	\N	\N	image/svg+xml	\N	\N	media	mlam-logo-2-249	\N	2025-12-15 13:07:36.336743-03	2025-12-15 13:07:36.336744-03	100	f	1188	0	0	\N	0	\N	\N	\N	f	\N	mlam-logo-2.svg
19611	omiobini.jpg	\N	\N	\N	image/jpeg	\N	\N	media	omiobini-256	\N	2026-01-05 15:46:00.160709-03	2026-01-05 15:46:00.160716-03	100	f	252264	415	750	\N	0	\N	\N	\N	f		omiobini.jpg
30179	20260110_142007.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_142007-265	\N	2026-01-11 10:13:58.500478-03	2026-01-11 10:13:58.500484-03	100	f	575227	1800	4000	3	0	\N	\N	\N	f		20260110_142007.jpg
30290	20260110_134326.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_134326-266	\N	2026-01-11 10:15:25.495509-03	2026-01-11 10:15:25.495522-03	100	f	1542933	1800	4000	3	0	\N	\N	\N	f		20260110_134326.jpg
32034	20260110_141921.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_141921-270	\N	2026-01-11 14:35:25.168993-03	2026-01-11 14:35:25.168996-03	100	f	2872664	1800	4000	3	0	\N	\N	\N	f		20260110_141921.jpg
40298	20260110_133910.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_133910-283	\N	2026-01-13 15:09:42.204061-03	2026-01-13 15:09:42.204062-03	100	f	309505	2996	544	3	0	\N	\N	\N	f		20260110_133910.jpg
368	museo-secreto.mp3	museo-secreto-mp3	\N	\N	audio/mp3	\N	\N	media	museo-secreto-137	\N	2025-06-10 11:30:30.908406-03	2025-11-12 11:33:17.701903-03	100	t	703216	0	0	1	88000	\N	\N	\N	f		museo-secreto.mp3
419	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-142	\N	2025-06-11 18:06:38.756155-03	2025-11-12 11:33:17.784477-03	100	t	1630668	0	0	1	136000	\N	\N	\N	f		rosas.mp3
797	411-museo-secreto.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	411-museo-secreto-211	\N	2025-11-11 17:42:27.056974-03	2025-11-12 12:15:24.110301-03	100	f	5351148	0	0	\N	0	\N	\N	\N	f	Error -> 1284 secs | 4167.560747663551 bytes/sec	411-museo-secreto.mp3
806	museo-secreto-411.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	museo-secreto-411-213	\N	2025-11-13 09:55:32.630612-03	2025-11-13 10:10:14.148298-03	100	f	742368	0	0	\N	0	\N	\N	\N	f	Error -> 180 secs | 4124.266666666666 bytes/sec	museo-secreto-411.mp3
758	viaggio-italia.webp	\N	\N	\N	image/webp	\N	\N	media	viaggio-italia-200	\N	2025-10-28 15:03:13.133952-03	2025-10-28 15:03:13.133954-03	100	f	29436	0	0	\N	0	\N	\N	\N	f	\N	viaggio-italia.webp
565	Museo_malba_logo.png	museo-malba-logo-png	\N	\N	image/png	\N	\N	media	museo_malba_logo-175	\N	2025-09-15 11:12:42.960448-03	2025-09-15 11:12:43.040887-03	100	t	8728	78	424	1	0	\N	\N	\N	f	\N	Museo_malba_logo.png
590	troncos-xul-solar.jpg	troncos-xul-solar-jpg	\N	\N	image/jpeg	\N	\N	media	troncos-xul-solar-180	\N	2025-09-19 10:51:24.112219-03	2025-09-19 10:51:24.176399-03	100	t	56549	767	1140	1	0	\N	\N	\N	f	\N	troncos-xul-solar.jpg
814	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-219	\N	2025-11-17 14:53:52.93607-03	2025-11-17 14:53:53.086918-03	100	f	484606	0	0	\N	59000	\N	\N	\N	f		sendas-perdidas-458.mp3
859	mamba-4.jpg	\N	\N	\N	image/jpeg	\N	\N	media	mamba-4-226	\N	2025-11-28 10:41:29.304633-03	2025-11-28 10:41:29.304643-03	100	f	160333	448	1024	\N	0	\N	\N	\N	f		mamba-4.jpg
868	Lam-The-Jungle-thumb.jpg	\N	\N	\N	image/jpeg	\N	\N	media	lam-the-jungle-thumb-227	\N	2025-11-28 10:54:16.177028-03	2025-11-28 10:54:16.177036-03	100	f	725383	714	1188	\N	0	\N	\N	\N	f		Lam-The-Jungle-thumb.jpg
913	a-selva-902.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	a-selva-902-238	\N	2025-11-30 18:19:26.229231-03	2025-11-30 18:19:26.229232-03	100	f	1599673	0	0	\N	0	\N	\N	\N	f	\N	a-selva-902.mp3
422	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-145	\N	2025-06-12 13:06:21.256168-03	2025-11-12 11:33:17.70737-03	100	t	1747592	0	0	1	145000	\N	\N	\N	f		rosas.mp3
336	reposo.mp3	reposo-mp3	\N	\N	audio/mp3	\N	\N	media	reposo-125	\N	2025-05-19 17:03:01.658216-03	2025-11-12 11:33:17.733539-03	100	t	6210291	0	0	1	304000	\N	\N	\N	f		reposo.mp3
421	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-144	\N	2025-06-11 21:47:41.748635-03	2025-11-12 11:33:17.784402-03	100	t	1873920	0	0	1	156000	\N	\N	\N	f		theodora.mp3
424	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-147	\N	2025-06-13 14:30:00.94748-03	2025-11-12 11:33:17.850208-03	100	t	1512803	0	0	1	126000	\N	\N	\N	f		rosas.mp3
418	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-141	\N	2025-06-11 18:00:45.327953-03	2025-11-12 11:33:17.917488-03	100	t	1630668	0	0	1	136000	\N	\N	\N	f		rosas.mp3
420	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-143	\N	2025-06-11 18:22:39.656749-03	2025-11-12 11:33:17.997241-03	100	t	1656059	0	0	1	138000	\N	\N	\N	f		rosas.mp3
914	a-selva-902-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	a-selva-902-music-239	\N	2025-11-30 18:20:46.450382-03	2025-11-30 18:20:46.450384-03	100	f	1231755	0	0	\N	0	\N	\N	\N	f	\N	a-selva-902-music.mp3
917	the-jungle-901.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	the-jungle-901-240	\N	2025-11-30 18:31:20.060439-03	2025-11-30 18:31:20.06044-03	100	f	1657112	0	0	\N	0	\N	\N	\N	f	Error -> 338 secs | 4902.698224852071 bytes/sec	the-jungle-901.mp3
918	the-jungle-901-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	the-jungle-901-music-241	\N	2025-11-30 18:32:01.241715-03	2025-11-30 18:32:01.241716-03	100	f	1311585	0	0	\N	0	\N	\N	\N	f	\N	the-jungle-901-music.mp3
1499	Bellasartes_logo.png	\N	\N	\N	image/png	\N	\N	media	bellasartes_logo-250	\N	2025-12-20 12:29:36.040729-03	2025-12-20 12:29:36.040731-03	100	t	8953	60	525	\N	0	\N	\N	\N	f		Bellasartes_logo.png
22474	la-jungla-899.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	la-jungla-899-257	\N	2026-01-06 14:41:51.591231-03	2026-01-06 14:41:51.591233-03	100	f	1635022	0	0	3	0	\N	\N	\N	f	Error -> 409 secs | 3997.60880195599 bytes/sec	la-jungla-899.mp3
22475	la-jungla-899-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	la-jungla-899-music-258	\N	2026-01-06 14:55:41.3739-03	2026-01-06 14:55:41.373901-03	100	f	1233009	0	0	3	0	\N	\N	\N	f	\N	la-jungla-899-music.mp3
807	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-214	\N	2025-11-13 13:26:03.500316-03	2025-11-13 13:26:11.031749-03	100	f	443283	0	0	\N	15000	\N	\N	\N	f		sendas-perdidas-458.mp3
30401	Cabildo_Abierto_-_Pedro_Subercaseaux.jpg	\N	\N	\N	image/jpeg	\N	\N	media	cabildo_abierto_-_pedro_subercaseaux-267	\N	2026-01-11 10:19:32.933588-03	2026-01-11 10:19:32.933595-03	100	f	234132	900	1200	3	0	\N	\N	\N	f		Cabildo_Abierto_-_Pedro_Subercaseaux.jpg
762	portrait-suzanne.jpg	\N	\N	\N	image/jpeg	\N	\N	media	portrait-suzanne-202	\N	2025-10-29 08:53:37.577633-03	2025-10-29 08:53:37.669001-03	100	f	2137276	2104	1808	\N	0	\N	\N	\N	f	\N	portrait-suzanne.jpg
641	red-flower-3794782_640.jpg	red-flower-3794782-640-jpg	\N	\N	image/jpeg	\N	\N	avatar	red-flower-3794782_640-193	\N	2025-10-06 11:27:25.276051-03	2025-11-11 22:16:07.558095-03	100	t	103917	427	640	\N	0	avatar	\N	\N	f	\N	red-flower-3794782_640.jpg
642	rose-5326516_640.jpg	rose-5326516-640-jpg	\N	\N	image/jpeg	\N	\N	avatar	rose-5326516_640-194	\N	2025-10-06 11:27:41.984373-03	2025-11-11 22:16:07.630641-03	100	t	60754	435	640	\N	0	avatar	\N	\N	f	\N	rose-5326516_640.jpg
316	monet-La berge de La Seine.jpg	monet-la-berge-de-la-seine-jpg	\N	\N	image/jpeg	\N	\N	media	monet-la-berge-de-la-seine-111	\N	2025-05-19 14:04:43.610851-03	2025-09-02 17:28:34.529306-03	100	t	3047342	2236	2748	1	0	\N	\N	\N	f	\N	monet-La berge de La Seine.jpg
750	Museo_Nacional_de_Arte_Decorativo.jpg	\N	\N	\N	image/jpeg	\N	\N	media	museo_nacional_de_arte_decorativo-196	\N	2025-10-28 14:45:44.299288-03	2025-11-11 22:16:07.697216-03	100	t	1238466	1774	2456	\N	0	\N	\N	\N	f	\N	Museo_Nacional_de_Arte_Decorativo.jpg
560	malba1500x610fachada2023.jpg	malba1500x610fachada2023-jpg	\N	\N	image/jpeg	\N	\N	media	malba1500x610fachada2023-172	\N	2025-09-10 18:27:17.308694-03	2025-09-10 18:27:17.382329-03	100	t	329178	610	1500	1	0	\N	\N	\N	f	\N	malba1500x610fachada2023.jpg
562	museo_coleccion_fortabat_1200_fachada (1).jpg	museo-coleccion-fortabat-1200-fachada-1-jpg	\N	\N	image/jpeg	\N	\N	media	museo_coleccion_fortabat_1200_fachada-(1)-173	\N	2025-09-12 14:11:30.154597-03	2025-10-02 13:42:24.645427-03	100	t	116441	489	1200	1	0	\N	\N	\N	f	\N	museo_coleccion_fortabat_1200_fachada (1).jpg
554	fachada-mumoderno-1500x610.jpg	fachada-mumoderno-1500x610-jpg	\N	\N	image/jpeg	\N	\N	media	fachada-mumoderno-1500x610-167	\N	2025-09-10 18:13:51.004224-03	2025-10-02 13:42:24.646068-03	100	t	1011343	610	1500	1	0	\N	\N	\N	f	\N	fachada-mumoderno-1500x610.jpg
583	Matta-The-disasters-of-Mysticism-122.jpg	matta-the-disasters-of-mysticism-122-jpg	\N	\N	image/jpeg	\N	\N	media	matta-the-disasters-of-mysticism-122-178	\N	2025-09-19 10:06:01.748986-03	2025-09-19 10:06:01.816049-03	100	t	115465	559	750	1	0	\N	\N	\N	f	\N	Matta-The-disasters-of-Mysticism-122.jpg
586	barredas-circo.jpg	barredas-circo-jpg	\N	\N	image/jpeg	\N	\N	media	barredas-circo-179	\N	2025-09-19 10:15:23.967119-03	2025-09-19 10:15:24.025412-03	100	t	204273	714	750	1	0	\N	\N	\N	f	\N	barredas-circo.jpg
778	churba-1.jpg	\N	\N	\N	image/jpeg	\N	\N	media	churba-1-204	\N	2025-10-29 13:31:47.169455-03	2025-10-29 13:31:47.247026-03	100	f	50681	1152	768	\N	0	\N	\N	\N	f	\N	churba-1.jpg
30582	Cabildo_Abierto_-_Pedro_Subercaseaux.jpg	\N	\N	\N	image/jpeg	\N	\N	media	cabildo_abierto_-_pedro_subercaseaux-268	\N	2026-01-11 10:21:24.136375-03	2026-01-11 10:21:24.136378-03	100	f	234132	900	1200	3	0	\N	\N	\N	f		Cabildo_Abierto_-_Pedro_Subercaseaux.jpg
30693	20260110_141221.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_141221-269	\N	2026-01-11 10:24:48.070752-03	2026-01-11 10:24:48.070755-03	100	f	703692	1568	1579	3	0	\N	\N	\N	f		20260110_141221.jpg
34080	el-cabildo-de-buenos-aires-33825.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	el-cabildo-de-buenos-aires-33825-271	\N	2026-01-11 16:13:34.051609-03	2026-01-11 16:13:34.05161-03	100	f	2739003	0	0	3	0	\N	\N	\N	f	Error -> 609 secs | 4497.541871921182 bytes/sec	el-cabildo-de-buenos-aires-33825.mp3
34081	el-cabildo-de-buenos-aires-33825-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	el-cabildo-de-buenos-aires-33825-music-272	\N	2026-01-11 16:15:24.021431-03	2026-01-11 16:15:24.021432-03	100	f	1904542	0	0	3	119000	\N	\N	\N	f		el-cabildo-de-buenos-aires-33825-music.mp3
34165	the-buenos-aires-city-council-33827-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	the-buenos-aires-city-council-33827-music-275	\N	2026-01-11 16:22:44.836975-03	2026-01-11 16:22:44.836976-03	100	f	1986471	0	0	3	0	\N	\N	\N	f	\N	the-buenos-aires-city-council-33827-music.mp3
34207	c-mara-municipal-de-buenos-aires-33828.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	c-mara-municipal-de-buenos-aires-33828-276	\N	2026-01-11 16:23:31.498577-03	2026-01-11 16:23:31.498578-03	100	f	2849001	0	0	3	0	\N	\N	\N	f	\N	c-mara-municipal-de-buenos-aires-33828.mp3
815	museo-secreto-411.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	museo-secreto-411-220	\N	2025-11-17 15:03:29.278571-03	2025-11-17 15:03:29.486974-03	100	f	744773	0	0	\N	73000	\N	\N	\N	f		museo-secreto-411.mp3
869	Lam-The-Jungle-thumb.jpg	\N	\N	\N	image/jpeg	\N	\N	media	lam-the-jungle-thumb-228	\N	2025-11-28 12:10:17.697511-03	2025-11-28 12:10:17.697518-03	100	f	725383	714	1188	\N	0	\N	\N	\N	f		Lam-The-Jungle-thumb.jpg
874	odili.png	\N	\N	\N	image/png	\N	\N	media	odili-229	\N	2025-11-28 12:19:24.321955-03	2025-11-28 12:19:24.321958-03	100	t	2219201	948	1422	\N	0	\N	\N	\N	f		odili.png
804	alberto-churba--dise-o-infinito-799.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	alberto-churba--dise-o-infinito-799-212	\N	2025-11-11 22:00:25.864127-03	2025-11-12 11:33:17.864102-03	100	f	321019	0	0	\N	9000	\N	\N	\N	f		alberto-churba--dise-o-infinito-799.mp3
535	Bellasartes_logo.png	bellasartes-logo-png	\N	\N	image/png	\N	\N	media	bellasartes_logo-163	\N	2025-08-29 08:48:04.94776-03	2025-09-02 17:28:34.380823-03	100	t	8953	60	525	1	0	\N	\N	\N	f	\N	Bellasartes_logo.png
317	manet-La Nymphe surprise.jpg	manet-la-nymphe-surprise-jpg	\N	\N	image/jpeg	\N	\N	media	manet-la-nymphe-surprise-112	\N	2025-05-19 14:05:00.073789-03	2025-09-02 17:28:34.537292-03	100	t	1052179	2594	1999	1	0	\N	\N	\N	f	\N	manet-La Nymphe surprise.jpg
321	pissarro-femme-aux-camps.jpg	pissarro-femme-aux-camps-jpg	\N	\N	image/jpeg	\N	\N	media	pissarro-femme-aux-camps-116	\N	2025-05-19 14:05:06.531826-03	2025-09-02 17:28:34.547738-03	100	t	2154642	3544	1851	1	0	\N	\N	\N	f	\N	pissarro-femme-aux-camps.jpg
319	van-gogh-Le Moulin de la Galette.jpg	van-gogh-le-moulin-de-la-galette-jpg	\N	\N	image/jpeg	\N	\N	media	van-gogh-le-moulin-de-la-galette-114	\N	2025-05-19 14:05:05.967549-03	2025-09-02 17:28:34.550979-03	100	t	2912483	2773	2272	1	0	\N	\N	\N	f	\N	van-gogh-Le Moulin de la Galette.jpg
808	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-215	\N	2025-11-13 13:30:21.579222-03	2025-11-13 13:30:43.582412-03	100	f	462762	0	0	\N	105000	\N	\N	\N	f		sendas-perdidas-458.mp3
879	moma-collection.png	\N	\N	\N	image/png	\N	\N	media	moma-collection-230	\N	2025-11-28 21:36:07.00466-03	2025-11-28 21:36:07.004665-03	100	t	3728681	659	1986	\N	0	\N	\N	\N	f		moma-collection.png
889	Lam-The-Jungle-thumb.jpg	\N	\N	\N	image/jpeg	\N	\N	media	lam-the-jungle-thumb-231	\N	2025-11-29 07:40:33.867089-03	2025-11-29 07:40:33.867091-03	100	f	725383	714	1188	\N	0	\N	\N	\N	f		Lam-The-Jungle-thumb.jpg
919	logo-mq.png	\N	\N	\N	image/png	\N	\N	media	logo-mq-242	\N	2025-12-01 10:37:12.891217-03	2025-12-01 10:37:12.89122-03	100	t	57480	511	1064	\N	0	\N	\N	\N	f		logo-mq.png
1730	tolomei.jpg	\N	\N	\N	image/jpeg	\N	\N	media	tolomei-251	\N	2025-12-22 13:28:57.842626-03	2025-12-22 13:28:57.842628-03	100	f	23059	288	288	\N	0	\N	\N	\N	f		tolomei.jpg
22521	the-jungle-901.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	the-jungle-901-259	\N	2026-01-06 15:11:45.499829-03	2026-01-06 15:11:45.499833-03	100	f	1672709	0	0	3	132000	\N	\N	\N	f		the-jungle-901.mp3
34122	el-cabildo-de-buenos-aires-33825-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	el-cabildo-de-buenos-aires-33825-music-273	\N	2026-01-11 16:20:48.880477-03	2026-01-11 16:20:48.880478-03	100	f	1904551	0	0	3	0	\N	\N	\N	f	\N	el-cabildo-de-buenos-aires-33825-music.mp3
34164	the-buenos-aires-city-council-33827.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	the-buenos-aires-city-council-33827-274	\N	2026-01-11 16:21:58.215567-03	2026-01-11 16:21:58.215567-03	100	f	2556218	0	0	3	0	\N	\N	\N	f	\N	the-buenos-aires-city-council-33827.mp3
816	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-221	\N	2025-11-19 16:17:31.111812-03	2025-11-19 16:17:31.617544-03	100	f	502375	0	0	\N	126000	\N	\N	\N	f		sendas-perdidas-458.mp3
809	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-216	\N	2025-11-13 14:02:03.385651-03	2025-11-13 14:02:03.888352-03	100	f	447201	0	0	\N	49000	\N	\N	\N	f		sendas-perdidas-458.mp3
906	wifredo-lam,-cuando-no-duermo,-sue-o-894.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	wifredo-lam,-cuando-no-duermo,-sue-o-894-232	\N	2025-11-30 14:57:24.829115-03	2025-11-30 14:57:24.829119-03	100	f	1124345	0	0	\N	0	\N	\N	\N	f	Error -> 240 secs | 4684.770833333333 bytes/sec	wifredo-lam,-cuando-no-duermo,-sue-o-894.mp3
558	iglesia-del-pilar-recoleta-pano-1500x610.jpg	iglesia-del-pilar-recoleta-pano-1500x610-jpg	\N	\N	image/jpeg	\N	\N	media	iglesia-del-pilar-recoleta-pano-1500x610-170	\N	2025-09-10 18:22:34.367699-03	2025-09-10 18:22:34.437144-03	100	t	252479	610	1500	1	0	\N	\N	\N	f	\N	iglesia-del-pilar-recoleta-pano-1500x610.jpg
559	museo-quinquela-martin1500x610-24.jpg	museo-quinquela-martin1500x610-24-jpg	\N	\N	image/jpeg	\N	\N	media	museo-quinquela-martin1500x610-24-171	\N	2025-09-10 18:24:50.233503-03	2025-09-10 18:24:50.303667-03	100	t	178941	610	1500	1	0	\N	\N	\N	f	\N	museo-quinquela-martin1500x610-24.jpg
620	divan.jpg	divan-jpg	\N	\N	image/jpeg	\N	\N	media	divan-186	\N	2025-09-24 11:34:01.607671-03	2025-09-24 11:34:01.673732-03	100	t	131775	570	768	1	0	\N	\N	\N	f	\N	divan.jpg
640	barredas-circo.jpg	barredas-circo-jpg	\N	\N	image/jpeg	\N	\N	media	barredas-circo-191	\N	2025-10-03 17:48:37.416316-03	2025-10-03 17:48:37.507906-03	100	t	204273	714	750	\N	0	\N	\N	\N	f	\N	barredas-circo.jpg
779	01ALBERTO-CHURBA-EXPOSICION-1-1-768x512.jpg	\N	\N	\N	image/jpeg	\N	\N	media	01alberto-churba-exposicion-1-1-768x512-205	\N	2025-10-29 13:33:30.878597-03	2025-10-29 13:33:30.941812-03	100	f	67543	512	768	\N	0	\N	\N	\N	f	\N	01ALBERTO-CHURBA-EXPOSICION-1-1-768x512.jpg
780	sillon-cinta.jpg	\N	\N	\N	image/jpeg	\N	\N	media	sillon-cinta-206	\N	2025-10-29 13:35:18.957927-03	2025-10-29 13:35:19.021391-03	100	f	39077	810	1440	\N	0	\N	\N	\N	f	\N	sillon-cinta.jpg
531	MNBA_fachada.jpg	mnba-fachada-jpg	\N	\N	image/jpeg	\N	\N	media	mnba_fachada-159	\N	2025-08-28 13:27:26.719745-03	2025-09-02 17:28:34.369327-03	100	t	185426	489	1200	1	0	\N	\N	\N	f	\N	MNBA_fachada.jpg
306	sin-pan-y-sin-trabajo.jpg	sin-pan-y-sin-trabajo-jpg	\N	\N	image/jpeg	\N	\N	media	sin-pan-y-sin-trabajo-106	\N	2025-05-19 12:53:47.43493-03	2025-09-02 17:28:34.458968-03	100	t	1604125	2251	4000	1	0	\N	\N	\N	f	\N	sin-pan-y-sin-trabajo.jpg
491	procesion.jpg	procesion-jpg	\N	\N	image/jpeg	\N	\N	media	procesion-156	\N	2025-07-28 12:01:23.906137-03	2025-09-02 17:28:34.466325-03	100	t	2240912	1540	2504	1	0	\N	\N	\N	f	\N	procesion.jpg
463	sendas-perdidas.jpg	sendas-perdidas-jpg	\N	\N	image/jpeg	\N	\N	media	sendas-perdidas-151	\N	2025-07-25 19:40:29.189818-03	2025-09-02 17:28:34.386105-03	100	t	1894439	1676	2048	1	0	\N	\N	\N	f	\N	sendas-perdidas.jpg
301	la-vuelta-del-malon.jpg	la-vuelta-del-malon-jpg	\N	\N	image/jpeg	\N	\N	media	la-vuelta-del-malon-101	\N	2025-05-19 12:53:35.060014-03	2025-09-02 17:28:34.447773-03	100	t	2597596	3118	4979	1	0	\N	\N	\N	f	\N	la-vuelta-del-malon.jpg
1104	museu-secreto-735.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	museu-secreto-735-243	\N	2025-12-08 12:11:34.464538-03	2025-12-08 12:11:34.464556-03	100	f	838670	0	0	\N	129000	\N	\N	\N	f		museu-secreto-735.mp3
1107	secret-museum-734.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	secret-museum-734-245	\N	2025-12-08 12:19:24.502489-03	2025-12-08 12:19:24.502491-03	100	f	738764	0	0	\N	0	\N	\N	\N	f	\N	secret-museum-734.mp3
563	logo-fortabat.svg	logo-fortabat-svg	\N	\N	image/svg	\N	\N	media	logo-fortabat-174	\N	2025-09-12 14:13:40.798216-03	2025-09-12 14:13:40.798223-03	100	f	2884	0	0	1	0	\N	\N	\N	f	\N	logo-fortabat.svg
12516	Malembo.jpg	\N	\N	\N	image/jpeg	\N	\N	media	malembo-252	\N	2025-12-26 16:10:13.711287-03	2025-12-26 16:10:13.711291-03	100	f	1159349	2000	1660	\N	0	\N	\N	\N	f		Malembo.jpg
23041	zambezia-zambezia-1950.jpg	\N	\N	\N	image/jpeg	\N	\N	media	zambezia-zambezia-1950-260	\N	2026-01-07 08:28:37.815067-03	2026-01-07 08:28:37.815068-03	100	f	25382	490	422	3	0	\N	\N	\N	f		zambezia-zambezia-1950.jpg
34208	c-mara-municipal-de-buenos-aires-33828-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	c-mara-municipal-de-buenos-aires-33828-music-277	\N	2026-01-11 16:23:49.964405-03	2026-01-11 16:23:49.964406-03	100	f	2188345	0	0	3	137000	\N	\N	\N	f		c-mara-municipal-de-buenos-aires-33828-music.mp3
817	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-222	\N	2025-11-19 16:27:42.964744-03	2025-11-19 16:27:43.100957-03	100	f	443915	0	0	\N	31000	\N	\N	\N	f		sendas-perdidas-458.mp3
907	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	wifredo-lam,-cuando-no-duermo,-sue-o-894-music-233	\N	2025-11-30 14:59:01.297522-03	2025-11-30 14:59:01.297525-03	100	f	5249179	0	0	\N	0	\N	\N	\N	f	\N	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3
908	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	wifredo-lam,-cuando-no-duermo,-sue-o-894-music-234	\N	2025-11-30 15:01:11.604176-03	2025-11-30 15:01:11.604179-03	100	f	5249179	0	0	\N	0	\N	\N	\N	f	\N	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3
1106	utopia-del-sur-488.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	utopia-del-sur-488-244	\N	2025-12-08 12:17:11.800093-03	2025-12-08 12:17:11.800096-03	100	f	490952	0	0	\N	0	\N	\N	\N	f	\N	utopia-del-sur-488.mp3
812	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-217	\N	2025-11-16 19:52:45.748554-03	2025-11-16 19:52:45.929498-03	100	f	485574	0	0	\N	45000	\N	\N	\N	f		sendas-perdidas-458.mp3
790	viaggio-in-italia-spa.mp3	\N	\N	\N	audio/mp3	\N	\N	media	viaggio-in-italia-spa-208	\N	2025-10-30 14:56:07.85288-03	2025-11-12 11:33:17.855448-03	100	f	1723708	0	0	\N	108000	\N	\N	\N	f		viaggio-in-italia-spa.mp3
576	femme-champs.jpg	femme-champs-jpg	\N	\N	image/jpeg	\N	\N	media	femme-champs-177	\N	2025-09-19 00:09:08.62643-03	2025-09-19 00:09:08.708713-03	100	t	2154642	3544	1851	1	0	\N	\N	\N	f	\N	femme-champs.jpg
598	carrie-bencardino-1.jpg	carrie-bencardino-1-jpg	\N	\N	image/jpeg	\N	\N	media	carrie-bencardino-1-182	\N	2025-09-19 21:27:21.095501-03	2025-09-19 21:27:21.171658-03	100	t	126693	310	560	1	0	\N	\N	\N	f	\N	carrie-bencardino-1.jpg
13112	museo-secreto-411-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	museo-secreto-411-music-253	\N	2025-12-27 09:05:37.591113-03	2025-12-27 09:05:37.591125-03	100	f	769084	0	0	\N	48000	\N	\N	\N	f		museo-secreto-411-music.mp3
29025	invasiones-inglesas.jpeg	\N	\N	\N	image/jpeg	\N	\N	media	invasiones-inglesas-261	\N	2026-01-11 09:23:40.837624-03	2026-01-11 09:23:40.837627-03	100	f	1371047	2829	3454	3	0	\N	\N	\N	f		invasiones-inglesas.jpeg
35197	20260110_134326.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_134326-278	\N	2026-01-12 10:41:14.67399-03	2026-01-12 10:41:14.673993-03	100	f	1542933	1800	4000	3	0	\N	\N	\N	f		20260110_134326.jpg
309	quiosco-de-canaletas.jpg	quiosco-de-canaletas-jpg	\N	\N	image/jpeg	\N	\N	media	quiosco-de-canaletas-107	\N	2025-05-19 12:53:48.924719-03	2025-09-02 17:28:34.454518-03	100	t	213641	577	750	1	0	\N	\N	\N	f	\N	quiosco-de-canaletas.jpg
534	Escuela-y-museo-BQM-768x605.jpg	escuela-y-museo-bqm-768x605-jpg	\N	\N	image/jpeg	\N	\N	media	escuela-y-museo-bqm-768x605-162	\N	2025-08-28 21:38:59.934379-03	2025-09-02 17:28:34.454965-03	100	t	97970	605	768	1	0	\N	\N	\N	f	\N	Escuela-y-museo-BQM-768x605.jpg
312	quiosco-de-canaletas.jpg	quiosco-de-canaletas-jpg	\N	\N	image/jpeg	\N	\N	media	quiosco-de-canaletas-108	\N	2025-05-19 13:08:46.307485-03	2025-09-02 17:28:34.520816-03	100	t	213641	577	750	1	0	\N	\N	\N	f	\N	quiosco-de-canaletas.jpg
331	gaugin-Comentario sobre Vahine no te miti.jpg	gaugin-comentario-sobre-vahine-no-te-miti-jpg	\N	\N	image/jpeg	\N	\N	media	gaugin-comentario-sobre-vahine-no-te-miti-122	\N	2025-05-19 14:07:30.710928-03	2025-09-02 17:28:34.626734-03	100	t	2153679	3135	2495	1	0	\N	\N	\N	f	\N	gaugin-Comentario sobre Vahine no te miti.jpg
357	malba.jpg	malba-jpg	\N	\N	image/jpeg	\N	\N	media	malba-129	\N	2025-06-08 23:32:47.085864-03	2025-09-02 17:28:34.626956-03	100	t	929219	1500	2000	1	0	\N	\N	\N	f	\N	malba.jpg
358	mnba.jpg	mnba-jpg	\N	\N	image/jpeg	\N	\N	media	mnba-130	\N	2025-06-08 23:32:56.174359-03	2025-09-02 17:28:34.626865-03	100	t	533937	1365	2048	1	0	\N	\N	\N	f	\N	mnba.jpg
359	quinquela.jpg	quinquela-jpg	\N	\N	image/jpeg	\N	\N	media	quinquela-131	\N	2025-06-08 23:35:16.284274-03	2025-09-02 17:28:34.627917-03	100	t	12635	199	253	1	0	\N	\N	\N	f	\N	quinquela.jpg
363	arte-argentino.jpg	arte-argentino-jpg	\N	\N	image/jpeg	\N	\N	media	arte-argentino-132	\N	2025-06-09 19:30:29.515396-03	2025-09-02 17:28:34.628922-03	100	t	657966	1067	2000	1	0	\N	\N	\N	f	\N	arte-argentino.jpg
367	museo-secreto.jpg	museo-secreto-jpg	\N	\N	image/jpeg	\N	\N	media	museo-secreto-136	\N	2025-06-10 10:48:25.570969-03	2025-09-02 17:28:34.699068-03	100	t	556211	1365	2047	1	0	\N	\N	\N	f	\N	museo-secreto.jpg
416	sorolla.jpg	sorolla-jpg	\N	\N	image/jpeg	\N	\N	media	sorolla-139	\N	2025-06-10 15:39:32.077799-03	2025-09-02 17:28:34.699593-03	100	t	507011	1332	2048	1	0	\N	\N	\N	f	\N	sorolla.jpg
365	museo-secreto.jpg	museo-secreto-jpg	\N	\N	image/jpeg	\N	\N	media	museo-secreto-134	\N	2025-06-09 19:30:42.542453-03	2025-09-02 17:28:34.700011-03	100	t	446108	2015	1100	1	0	\N	\N	\N	f	\N	museo-secreto.jpg
415	rosas.jpg	rosas-jpg	\N	\N	image/jpeg	\N	\N	media	rosas-138	\N	2025-06-10 15:39:31.66896-03	2025-09-02 17:28:34.701326-03	100	t	597943	2048	1651	1	0	\N	\N	\N	f	\N	rosas.jpg
417	theodora.jpg	theodora-jpg	\N	\N	image/jpeg	\N	\N	media	theodora-140	\N	2025-06-10 15:39:32.341741-03	2025-09-02 17:28:34.701368-03	100	t	446108	2015	1100	1	0	\N	\N	\N	f	\N	theodora.jpg
464	recurso-metodo.jpg	recurso-metodo-jpg	\N	\N	image/jpeg	\N	\N	media	recurso-metodo-152	\N	2025-07-25 19:40:41.776111-03	2025-09-02 17:28:34.701497-03	100	t	690874	1533	2048	1	0	\N	\N	\N	f	\N	recurso-metodo.jpg
601	tercer-ojo-mexico.jpg	tercer-ojo-mexico-jpg	\N	\N	image/jpeg	\N	\N	media	tercer-ojo-mexico-184	\N	2025-09-19 21:52:02.31562-03	2025-09-19 21:52:02.379299-03	100	t	118325	415	750	1	0	\N	\N	\N	f	\N	tercer-ojo-mexico.jpg
320	monet-le-pont-dargenteuil.jpg	monet-le-pont-dargenteuil-jpg	\N	\N	image/jpeg	\N	\N	media	monet-le-pont-dargenteuil-115	\N	2025-05-19 14:05:06.226962-03	2025-09-02 17:28:34.535994-03	100	t	82102	504	846	1	0	\N	\N	\N	f	\N	monet-le-pont-dargenteuil.jpg
772	sillon-cinta.jpg	\N	\N	\N	image/jpeg	\N	\N	media	sillon-cinta-203	\N	2025-10-29 09:31:53.123043-03	2025-10-29 09:31:53.205352-03	100	f	39077	810	1440	\N	0	\N	\N	\N	f	\N	sillon-cinta.jpg
759	viaggio-italia.webp	\N	\N	\N	image/webp	\N	\N	media	viaggio-italia-201	\N	2025-10-28 15:05:10.618374-03	2025-10-28 15:05:10.618375-03	100	f	29436	0	0	\N	0	\N	\N	\N	f	\N	viaggio-italia.webp
751	Museo_arte_decorativo_logo.png	\N	\N	\N	image/png	\N	\N	media	museo_arte_decorativo_logo-197	\N	2025-10-28 14:45:44.37819-03	2025-10-28 14:45:44.442327-03	100	t	17023	216	266	\N	0	\N	\N	\N	f	\N	Museo_arte_decorativo_logo.png
756	museo_arte_decorativo_1200_2.jpg	\N	\N	\N	image/jpeg	\N	\N	media	museo_arte_decorativo_1200_2-199	\N	2025-10-28 14:54:22.616866-03	2025-10-28 14:54:22.676108-03	100	t	152388	489	1200	\N	0	\N	\N	\N	f	\N	museo_arte_decorativo_1200_2.jpg
818	sendas-perdidas-458-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	sendas-perdidas-458-music-223	\N	2025-11-19 16:39:10.992124-03	2025-11-19 16:39:11.209041-03	100	f	5249179	0	0	\N	328000	\N	\N	\N	f		sendas-perdidas-458-music.mp3
462	logo-mnba.png	logo-mnba-png	\N	\N	image/png	\N	\N	media	logo-mnba-150	\N	2025-07-25 19:27:31.965004-03	2025-09-02 17:28:34.370005-03	100	t	6708	94	802	1	0	\N	\N	\N	f	\N	logo-mnba.png
568	Do-Amaral-Abaporu-067-1.jpg	do-amaral-abaporu-067-1-jpg	\N	\N	image/jpeg	\N	\N	media	do-amaral-abaporu-067-1-176	\N	2025-09-18 15:06:25.424901-03	2025-09-18 15:06:25.503139-03	100	t	158504	878	750	1	0	\N	\N	\N	f	\N	Do-Amaral-Abaporu-067-1.jpg
538	Museo_malba_logo.png	museo-malba-logo-png	\N	\N	image/png	\N	\N	media	museo_malba_logo-165	\N	2025-08-29 09:30:02.271399-03	2025-09-02 17:28:34.374516-03	100	t	8728	78	424	1	0	\N	\N	\N	f	\N	Museo_malba_logo.png
536	21-cabildo-800x800px.png	21-cabildo-800x800px-png	\N	\N	image/png	\N	\N	media	21-cabildo-800x800px-164	\N	2025-08-29 09:22:51.0849-03	2025-09-02 17:28:34.375691-03	100	t	14950	800	800	1	0	\N	\N	\N	f	\N	21-cabildo-800x800px.png
638	cunsolo.jpg	cunsolo-jpg	\N	\N	image/jpeg	\N	\N	media	cunsolo-189	\N	2025-10-02 18:34:27.211566-03	2025-10-02 18:34:27.318351-03	100	t	3027507	3116	3604	\N	0	\N	\N	\N	f	\N	cunsolo.jpg
300	abel.jpg	abel-jpg	\N	\N	image/jpeg	\N	\N	media	abel-100	\N	2025-05-19 12:53:18.529644-03	2025-09-02 17:28:11.622213-03	100	t	2454212	2848	4288	1	0	\N	\N	\N	f	\N	abel.jpg
490	utopia-del-sur.jpg	utopia-del-sur-jpg	\N	\N	image/jpeg	\N	\N	media	utopia-del-sur-155	\N	2025-07-28 12:01:22.269836-03	2025-09-02 17:28:34.376015-03	100	t	1056849	2128	2000	1	0	\N	\N	\N	f	\N	utopia-del-sur.jpg
329	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve.jpg	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve-jpg	\N	\N	image/jpeg	\N	\N	media	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve-121	\N	2025-05-19 14:07:30.38622-03	2025-09-02 17:28:34.377465-03	100	t	870974	2400	1960	1	0	\N	\N	\N	f	\N	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve.jpg
530	Cabildo_de_Buenos_Aires_2023.jpg	cabildo-de-buenos-aires-2023-jpg	\N	\N	image/jpeg	\N	\N	media	cabildo_de_buenos_aires_2023-158	\N	2025-08-28 10:02:25.822268-03	2025-09-02 17:28:34.504562-03	100	t	380905	1024	1280	1	0	\N	\N	\N	f	\N	Cabildo_de_Buenos_Aires_2023.jpg
532	procesion.jpg	procesion-jpg	\N	\N	image/jpeg	\N	\N	media	procesion-160	\N	2025-08-28 19:01:15.963413-03	2025-09-02 17:28:34.512219-03	100	t	2240912	1540	2504	1	0	\N	\N	\N	f	\N	procesion.jpg
594	Martins-O-impossivel-119.jpg	martins-o-impossivel-119-jpg	\N	\N	image/jpeg	\N	\N	media	martins-o-impossivel-119-181	\N	2025-09-19 20:09:56.199367-03	2025-09-19 20:09:56.262323-03	100	t	67671	1045	750	1	0	\N	\N	\N	f	\N	Martins-O-impossivel-119.jpg
528	Museo de Arte Latinoaméricano de Buenos Aires	museo-de-arte-latinoam-ricano-de-buenos-aires	\N	\N	malba.jpg	\N	\N	media	malba-157	\N	2025-08-26 14:16:07.097636-03	2025-08-28 13:07:42.700421-03	100	t	329178	0	0	1	0	\N	\N	\N	f	\N	Museo de Arte Latinoaméricano de Buenos Aires
604	el-mal.jpg	el-mal-jpg	\N	\N	image/jpeg	\N	\N	media	el-mal-185	\N	2025-09-21 19:59:51.154748-03	2025-09-21 19:59:51.220017-03	100	t	101814	768	721	1	0	\N	\N	\N	f	\N	el-mal.jpg
752	Museo_Nacional_de_Arte_Decorativo-2.jpg	\N	\N	\N	image/jpeg	\N	\N	media	museo_nacional_de_arte_decorativo-2-198	\N	2025-10-28 14:49:35.309092-03	2025-11-11 22:16:42.686934-03	100	t	901625	1110	2452	\N	0	\N	\N	\N	f	\N	Museo_Nacional_de_Arte_Decorativo-2.jpg
909	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	wifredo-lam-cuando-no-duermo-sue-o-894-music-235	\N	2025-11-30 17:43:29.919197-03	2025-11-30 17:43:29.919203-03	100	f	5249179	0	0	\N	328000	\N	\N	\N	f		wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3
16212	1949_LAM_Je-suis.jpg	\N	\N	\N	image/jpeg	\N	\N	media	1949_lam_je-suis-254	\N	2026-01-04 17:49:50.452533-03	2026-01-04 17:49:50.452535-03	100	f	490088	2000	1750	\N	0	\N	\N	\N	f		1949_LAM_Je-suis.jpg
1490	ai_studio_code.svg	\N	\N	\N	image/svg+xml	\N	\N	media	ai_studio_code-246	\N	2025-12-15 11:16:19.714578-03	2025-12-15 11:16:19.714582-03	100	f	1183	0	0	\N	0	\N	\N	\N	f	\N	ai_studio_code.svg
29336	20260110_135415.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_135415-262	\N	2026-01-11 09:42:13.687307-03	2026-01-11 09:42:13.687311-03	100	f	1703000	1800	4000	3	0	\N	\N	\N	f		20260110_135415.jpg
35502	traje-de-calle-del-alf-rez-real-35357.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	traje-de-calle-del-alf-rez-real-35357-279	\N	2026-01-12 10:42:45.602524-03	2026-01-12 10:42:45.602527-03	100	f	1810524	0	0	3	0	\N	\N	\N	f	\N	traje-de-calle-del-alf-rez-real-35357.mp3
305	reposo.jpg	reposo-jpg	\N	\N	image/jpeg	\N	\N	media	reposo-105	\N	2025-05-19 12:53:45.865734-03	2025-09-02 17:28:34.450806-03	100	t	657966	1067	2000	1	0	\N	\N	\N	f	\N	reposo.jpg
315	forain-Danseuse et admirateur derrière la scène.jpg	forain-danseuse-et-admirateur-derri-re-la-sc-ne-jpg	\N	\N	image/jpeg	\N	\N	media	forain-danseuse-et-admirateur-derri-re-la-sc-ne-110	\N	2025-05-19 14:03:00.808154-03	2025-09-02 17:28:34.524228-03	100	t	1788278	2604	3160	1	0	\N	\N	\N	f	\N	forain-Danseuse et admirateur derrière la scène.jpg
318	degas-La Toilette apres le bain.jpg	degas-la-toilette-apres-le-bain-jpg	\N	\N	image/jpeg	\N	\N	media	degas-la-toilette-apres-le-bain-113	\N	2025-05-19 14:05:05.606782-03	2025-09-02 17:28:34.529228-03	100	t	497166	1725	2000	1	0	\N	\N	\N	f	\N	degas-La Toilette apres le bain.jpg
302	el-despertar-de-la-criada.jpg	el-despertar-de-la-criada-jpg	\N	\N	image/jpeg	\N	\N	media	el-despertar-de-la-criada-102	\N	2025-05-19 12:53:37.800791-03	2025-09-02 17:28:12.176293-03	100	t	1093118	6187	4138	1	0	\N	\N	\N	f	\N	el-despertar-de-la-criada.jpg
314	forain-Danseuse et admirateur derrière la scène.jpg	forain-danseuse-et-admirateur-derri-re-la-sc-ne-jpg	\N	\N	image/jpeg	\N	\N	media	forain-danseuse-et-admirateur-derri-re-la-sc-ne-109	\N	2025-05-19 14:00:28.742066-03	2025-09-02 17:28:34.365234-03	100	t	1788278	2604	3160	1	0	\N	\N	\N	f	\N	forain-Danseuse et admirateur derrière la scène.jpg
35503	traje-de-calle-del-alf-rez-real-35357-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	traje-de-calle-del-alf-rez-real-35357-music-280	\N	2026-01-12 10:43:24.101217-03	2026-01-12 10:43:24.101219-03	100	f	1352426	0	0	3	0	\N	\N	\N	f	\N	traje-de-calle-del-alf-rez-real-35357-music.mp3
466	sendas-perdidas-muestra-2.jpg	sendas-perdidas-muestra-2-jpg	\N	\N	image/jpeg	\N	\N	media	sendas-perdidas-muestra-2-154	\N	2025-07-25 19:50:10.302434-03	2025-09-02 17:28:34.365581-03	100	t	525551	1365	2047	1	0	\N	\N	\N	f	\N	sendas-perdidas-muestra-2.jpg
303	en-normandie.jpg	en-normandie-jpg	\N	\N	image/jpeg	\N	\N	media	en-normandie-103	\N	2025-05-19 12:53:42.510528-03	2025-09-02 17:28:34.447772-03	100	t	354419	944	1200	1	0	\N	\N	\N	f	\N	en-normandie.jpg
304	la-vuelta-al-hogar.jpg	la-vuelta-al-hogar-jpg	\N	\N	image/jpeg	\N	\N	media	la-vuelta-al-hogar-104	\N	2025-05-19 12:53:44.209264-03	2025-09-02 17:28:34.44846-03	100	t	796475	1491	2000	1	0	\N	\N	\N	f	\N	la-vuelta-al-hogar.jpg
639	banio-venus.jpg	banio-venus-jpg	\N	\N	image/jpeg	\N	\N	media	banio-venus-190	\N	2025-10-02 18:37:00.677647-03	2025-10-02 18:37:00.74375-03	100	t	2281261	2048	1555	\N	0	\N	\N	\N	f	\N	banio-venus.jpg
548	pilar.JPG	pilar-jpg	\N	\N	image/jpeg	\N	\N	media	pilar-166	\N	2025-08-29 16:49:49.079774-03	2025-09-02 17:28:34.433374-03	100	t	773453	1445	1920	1	0	\N	\N	\N	f	\N	pilar.JPG
533	quinquela.webp	quinquela-webp	\N	\N	image/webp	\N	\N	media	quinquela-161	\N	2025-08-28 21:37:23.026809-03	2025-08-28 21:37:23.026815-03	100	t	217278	0	0	1	0	\N	\N	\N	f	\N	quinquela.webp
364	impresionismo.jpg	impresionismo-jpg	\N	\N	image/jpeg	\N	\N	media	impresionismo-133	\N	2025-06-09 19:30:42.127085-03	2025-09-02 17:28:34.706511-03	100	t	2912483	2773	2272	1	0	\N	\N	\N	f	\N	impresionismo.jpg
366	obras-maestras.jpg	obras-maestras-jpg	\N	\N	image/jpeg	\N	\N	media	obras-maestras-135	\N	2025-06-09 19:30:42.927536-03	2025-09-02 17:28:34.706511-03	100	t	2279851	3757	2622	1	0	\N	\N	\N	f	\N	obras-maestras.jpg
465	apocalipsis.jpg	apocalipsis-jpg	\N	\N	image/jpeg	\N	\N	media	apocalipsis-153	\N	2025-07-25 19:40:42.845304-03	2025-09-02 17:28:34.70748-03	100	t	2025274	2048	1536	1	0	\N	\N	\N	f	\N	apocalipsis.jpg
843	museo-arte.jpg	\N	\N	\N	image/jpeg	\N	\N	media	museo-arte-224	\N	2025-11-24 10:45:05.627232-03	2025-11-24 10:45:05.721794-03	100	f	120077	412	1024	\N	0	\N	\N	\N	f		museo-arte.jpg
910	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	wifredo-lam-cuando-no-duermo-sue-o-894-music-236	\N	2025-11-30 17:44:13.222877-03	2025-11-30 17:44:13.22288-03	100	f	5249179	0	0	\N	0	\N	\N	\N	f	\N	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3
1491	ai_studio_code(2).svg	\N	\N	\N	image/svg+xml	\N	\N	media	ai_studio_code(2)-247	\N	2025-12-15 12:14:46.773054-03	2025-12-15 12:14:46.773057-03	100	f	1183	0	0	\N	0	\N	\N	\N	f	\N	ai_studio_code(2).svg
556	museo-nacional-bellas-artes-1500x610-fachada.jpg	museo-nacional-bellas-artes-1500x610-fachada-jpg	\N	\N	image/jpeg	\N	\N	media	museo-nacional-bellas-artes-1500x610-fachada-168	\N	2025-09-10 18:18:19.182713-03	2025-09-10 18:18:19.264414-03	100	t	128416	610	1500	1	0	\N	\N	\N	f	\N	museo-nacional-bellas-artes-1500x610-fachada.jpg
17171	vangogh1.jpg	\N	\N	\N	image/jpeg	\N	\N	media	vangogh1-255	\N	2026-01-04 18:07:59.869945-03	2026-01-04 18:07:59.869948-03	100	f	1615265	1602	2000	\N	0	\N	\N	\N	f		vangogh1.jpg
557	cabildo-wide.jpg	cabildo-wide-jpg	\N	\N	image/jpeg	\N	\N	media	cabildo-wide-169	\N	2025-09-10 18:19:44.109945-03	2025-09-10 18:19:44.17457-03	100	t	140592	400	1600	1	0	\N	\N	\N	f	\N	cabildo-wide.jpg
781	sillon-cinta.jpg	\N	\N	\N	image/jpeg	\N	\N	media	sillon-cinta-207	\N	2025-10-29 13:38:40.42156-03	2025-10-29 13:38:40.484282-03	100	f	89091	727	1440	\N	0	\N	\N	\N	f	\N	sillon-cinta.jpg
643	flower-4989102_640.jpg	flower-4989102-640-jpg	\N	\N	image/jpeg	\N	\N	avatar	flower-4989102_640-195	\N	2025-10-06 11:27:53.11119-03	2025-11-11 22:18:20.046405-03	100	t	67817	480	640	\N	0	avatar	\N	\N	f	\N	flower-4989102_640.jpg
791	viaggio-in-italia-eng.mp3	\N	\N	\N	audio/mp3	\N	\N	media	viaggio-in-italia-eng-209	\N	2025-10-30 14:57:35.274434-03	2025-11-12 11:33:17.85555-03	100	f	1648058	0	0	\N	103000	\N	\N	\N	f		viaggio-in-italia-eng.mp3
425	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-148	\N	2025-06-13 14:30:02.052468-03	2025-11-12 11:33:17.875737-03	100	t	1724395	0	0	1	144000	\N	\N	\N	f		theodora.mp3
338	reposo.mp3	reposo-mp3	\N	\N	audio/mp3	\N	\N	media	reposo-127	\N	2025-05-20 11:29:21.56577-03	2025-11-12 11:33:17.924614-03	100	t	6210291	0	0	1	304000	\N	\N	\N	f		reposo.mp3
426	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-149	\N	2025-06-13 14:33:41.173778-03	2025-11-12 11:33:17.946583-03	100	t	1724395	0	0	1	144000	\N	\N	\N	f		theodora.mp3
337	reposo.mp3	reposo-mp3	\N	\N	audio/mp3	\N	\N	media	reposo-126	\N	2025-05-19 17:08:05.967738-03	2025-11-12 11:33:18.007135-03	100	t	6210291	0	0	1	304000	\N	\N	\N	f		reposo.mp3
637	reposo.mp3	reposo-mp3	\N	\N	audio/mp3	\N	\N	media	reposo-188	\N	2025-10-02 14:51:25.450064-03	2025-11-12 11:33:18.079722-03	100	t	6210291	0	0	\N	304000	\N	\N	\N	f		reposo.mp3
599	carrie-bencardino-2.jpg	carrie-bencardino-2-jpg	\N	\N	image/jpeg	\N	\N	media	carrie-bencardino-2-183	\N	2025-09-19 21:35:08.389486-03	2025-09-19 21:35:08.473541-03	100	t	80622	641	960	1	0	\N	\N	\N	f	\N	carrie-bencardino-2.jpg
323	toulouse-lautrec-Portrait de Suzanne Valadon.jpg	toulouse-lautrec-portrait-de-suzanne-valadon-jpg	\N	\N	image/jpeg	\N	\N	media	toulouse-lautrec-portrait-de-suzanne-valadon-118	\N	2025-05-19 14:05:07.193106-03	2025-09-02 17:28:34.602897-03	100	t	2137276	2104	1808	1	0	\N	\N	\N	f	\N	toulouse-lautrec-Portrait de Suzanne Valadon.jpg
334	benito-quinquela-martín-puente-de-la-boca.jpg	benito-quinquela-mart-n-puente-de-la-boca-jpg	\N	\N	image/jpeg	\N	\N	media	benito-quinquela-mart-n-puente-de-la-boca-123	\N	2025-05-19 14:07:33.795598-03	2025-09-02 17:28:34.60294-03	100	t	119371	470	569	1	0	\N	\N	\N	f	\N	benito-quinquela-martín-puente-de-la-boca.jpg
322	pissarro-prairires.jpg	pissarro-prairires-jpg	\N	\N	image/jpeg	\N	\N	media	pissarro-prairires-117	\N	2025-05-19 14:05:06.787645-03	2025-09-02 17:28:34.602897-03	100	t	1142828	1187	2000	1	0	\N	\N	\N	f	\N	pissarro-prairires.jpg
327	sisley-effet-de-niege.jpg	sisley-effet-de-niege-jpg	\N	\N	image/jpeg	\N	\N	media	sisley-effet-de-niege-120	\N	2025-05-19 14:07:27.773092-03	2025-09-02 17:28:34.626583-03	100	t	2545840	2094	2560	1	0	\N	\N	\N	f	\N	sisley-effet-de-niege.jpg
325	manet-Portrait d-Ernest Hoschedé et sa fille Marthe.jpg	manet-portrait-d-ernest-hosched-et-sa-fille-marthe-jpg	\N	\N	image/jpeg	\N	\N	media	manet-portrait-d-ernest-hosched--et-sa-fille-marthe-119	\N	2025-05-19 14:07:27.272501-03	2025-09-02 17:28:34.626645-03	100	t	2196334	1890	2560	1	0	\N	\N	\N	f	\N	manet-Portrait d-Ernest Hoschedé et sa fille Marthe.jpg
792	viaggio-in-italia-pt.mp3	\N	\N	\N	audio/mp3	\N	\N	media	viaggio-in-italia-pt-210	\N	2025-10-30 14:58:04.114809-03	2025-11-12 11:33:17.991493-03	100	f	1880443	0	0	\N	118000	\N	\N	\N	f		viaggio-in-italia-pt.mp3
29477	20260110_135630.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_135630-263	\N	2026-01-11 09:43:16.672505-03	2026-01-11 09:43:16.672511-03	100	f	1613985	1816	4032	3	0	\N	\N	\N	f		20260110_135630.jpg
29858	20260110_135049.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_135049-264	\N	2026-01-11 09:53:25.681492-03	2026-01-11 09:53:25.681509-03	100	f	1563385	3187	1814	3	0	\N	\N	\N	f		20260110_135049.jpg
37553	ii.jpg	\N	\N	\N	image/jpeg	\N	\N	media	ii-281	\N	2026-01-12 16:07:19.241962-03	2026-01-12 16:07:19.241964-03	100	f	326985	1080	1920	3	0	\N	\N	\N	f		ii.jpg
37908	patio.jpg	\N	\N	\N	image/jpeg	\N	\N	media	patio-282	\N	2026-01-13 04:44:53.588781-03	2026-01-13 04:44:53.588783-03	100	f	1884356	2047	1339	3	0	\N	\N	\N	f		patio.jpg
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id, name, created, lastmodified, lastmodifieduser, state) FROM stdin;
929	ROLE_ADMIN	2025-12-04 11:19:57.690482-03	2025-12-04 11:19:57.690482-03	100	1
930	ROLE_ADMIN	2025-12-04 11:21:28.567677-03	2025-12-04 11:21:28.567677-03	100	1
931	INSTITUTION_ADMIN	2025-12-04 11:21:28.570442-03	2025-12-04 11:21:28.570442-03	100	1
932	SITE_ADMIN	2025-12-04 11:21:28.571882-03	2025-12-04 11:21:28.571882-03	100	1
933	SITE_EDITOR	2025-12-04 11:21:28.573208-03	2025-12-04 11:21:28.573208-03	100	1
\.


--
-- Data for Name: rolegeneral; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rolegeneral (id, name, state, created, lastmodified, lastmodifieduser, key) FROM stdin;
936	admin	1	2025-12-05 09:49:32.504471-03	2025-12-05 09:49:32.504471-03	100	admin
937	audit	1	2025-12-05 09:50:11.22832-03	2025-12-05 09:50:11.22832-03	100	audit
\.


--
-- Data for Name: roleinstitution; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roleinstitution (id, name, institution_id, created, lastmodified, lastmodifieduser, state, key) FROM stdin;
987	admin	135	2025-12-06 17:44:09.903763-03	2025-12-06 17:44:09.903773-03	100	3	admin
988	admin	136	2025-12-06 17:44:09.926592-03	2025-12-06 17:44:09.9266-03	100	3	admin
989	admin	341	2025-12-06 17:44:09.93104-03	2025-12-06 17:44:09.931049-03	100	3	admin
990	admin	553	2025-12-06 17:44:09.934889-03	2025-12-06 17:44:09.934894-03	100	3	admin
991	admin	821	2025-12-06 17:44:09.938426-03	2025-12-06 17:44:09.938431-03	100	3	admin
992	admin	561	2025-12-06 17:44:09.942316-03	2025-12-06 17:44:09.942322-03	100	3	admin
993	admin	749	2025-12-06 17:44:09.945283-03	2025-12-06 17:44:09.945289-03	100	3	admin
994	admin	547	2025-12-06 17:44:09.948043-03	2025-12-06 17:44:09.948048-03	100	3	admin
995	admin	529	2025-12-06 17:44:09.950917-03	2025-12-06 17:44:09.950923-03	100	3	admin
996	admin	855	2025-12-06 17:44:09.953742-03	2025-12-06 17:44:09.953747-03	100	3	admin
\.


--
-- Data for Name: roles_privileges; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles_privileges (id, role_id, privilege_id, state) FROM stdin;
\.


--
-- Data for Name: rolesite; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rolesite (id, name, site_id, created, lastmodified, lastmodifieduser, state, key) FROM stdin;
997	admin	552	2025-12-06 17:44:29.701177-03	2025-12-06 17:44:29.701188-03	100	3	admin
998	admin	564	2025-12-06 17:44:29.706773-03	2025-12-06 17:44:29.706782-03	100	3	admin
999	admin	753	2025-12-06 17:44:29.710643-03	2025-12-06 17:44:29.71065-03	100	3	admin
1000	admin	137	2025-12-06 17:44:29.714577-03	2025-12-06 17:44:29.714584-03	100	3	admin
1001	admin	860	2025-12-06 17:44:29.718118-03	2025-12-06 17:44:29.718125-03	100	3	admin
1002	admin	342	2025-12-06 17:44:29.72176-03	2025-12-06 17:44:29.721767-03	100	3	admin
1003	admin	555	2025-12-06 17:44:29.725071-03	2025-12-06 17:44:29.725076-03	100	3	admin
1004	admin	537	2025-12-06 17:44:29.728419-03	2025-12-06 17:44:29.728427-03	100	3	admin
1005	admin	177	2025-12-06 17:44:29.732145-03	2025-12-06 17:44:29.732153-03	100	3	admin
1012	editor	552	2025-12-07 09:35:35.042519-03	2025-12-07 09:35:35.042527-03	100	3	editor
1013	editor	564	2025-12-07 09:35:35.060966-03	2025-12-07 09:35:35.060975-03	100	3	editor
1014	editor	753	2025-12-07 09:35:35.063496-03	2025-12-07 09:35:35.063501-03	100	3	editor
1015	editor	137	2025-12-07 09:35:35.065651-03	2025-12-07 09:35:35.065656-03	100	3	editor
1016	editor	860	2025-12-07 09:35:35.067741-03	2025-12-07 09:35:35.067746-03	100	3	editor
1017	editor	342	2025-12-07 09:35:35.07036-03	2025-12-07 09:35:35.070365-03	100	3	editor
1018	editor	555	2025-12-07 09:35:35.072527-03	2025-12-07 09:35:35.072532-03	100	3	editor
1019	editor	537	2025-12-07 09:35:35.074316-03	2025-12-07 09:35:35.074336-03	100	3	editor
1020	editor	177	2025-12-07 09:35:35.076052-03	2025-12-07 09:35:35.076058-03	100	3	editor
\.


--
-- Data for Name: room; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.room (id, name, namekey, title, titlekey, roomtype_id, floor_id, roomnumber, roomnumberkey, subtitle, subtitlekey, info, infokey, map, photo, video, audio, created, lastmodified, lastmodifieduser, state, language, draft, audioautogenerate) FROM stdin;
140	Hall Central	\N	\N	\N	\N	138	0	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
141	Tienda del Museo	\N	\N	\N	\N	138		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
142	Sala 2. Arte europeo siglo XII al XVI	\N	\N	\N	\N	138	2	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
143	Sala 3. Arte europeo siglo XV al XVIII	\N	\N	\N	\N	138	3	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
144	Sala 4. Países Bajos siglo XVII	\N	\N	\N	\N	138	4	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
145	Sala 5. Arte europeo	\N	\N	\N	\N	138	5	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
146	Sala 6. Arte europeo manierismo y barroco	\N	\N	\N	\N	138	6	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
147	Sala 7. Arte europeo siglo XVIII	\N	\N	\N	\N	138	7	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
148	Sala 8. Francisco de Goya	\N	\N	\N	\N	138	8	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
149	Sala 9. Arte europeo	\N	\N	\N	\N	138	9	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
150	Sala 10. Auguste Rodin	\N	\N	\N	\N	138	10	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
151	Sala 11. Arte francés siglo XIX	\N	\N	\N	\N	138	11	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
152	Sala 12. Arte europeo	\N	\N	\N	\N	138	12	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
153	Sala 13. Arte francés siglo XIX	\N	\N	\N	\N	138	13	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
154	Sala 14. Impresionismo y Postimpresionismo	\N	\N	\N	\N	138	14	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
155	Sala 15. Arte argentino siglo XIX	\N	\N	\N	\N	138	15	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
156	Sala 16. Arte europeo siglo XVII al XIX	\N	\N	\N	\N	138	16	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
157	Sala 17.	\N	\N	\N	\N	138	17	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
158	Sala 18. Arte francés siglo XIX	\N	\N	\N	\N	138	18	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
159	Sala 19	\N	\N	\N	\N	138	19	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
160	Sala 20. Arte argentino siglo XIX	\N	\N	\N	\N	138	20	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
161	Sala 21	\N	\N	\N	\N	138	21	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
162	Sala 22. Arte colonial	\N	\N	\N	\N	138	22	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
163	Sala 23. Arte colonial	\N	\N	\N	\N	138	23	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
164	Sala 24. Arte de los antiguos pueblos andinos	\N	\N	\N	\N	138	24	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
165	Biblioteca	\N	\N	\N	\N	138		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
166	Pabellón de exposiciones temporarias	\N	\N	\N	\N	139	100	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
167	SUM	\N	\N	\N	\N	138		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
168	Rampa primer piso - etre salas 31 y 32	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
169	Hall y escaleras	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
170	Sala 2. Arte europeo siglo XII al XVI	\N	\N	\N	\N	138		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
171	Salón 20 ARTISTAS BRASILEÑOS	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
172	Escuela de La Boca y realismo social	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
173	Arte latinoamericano y argentino 1910-1945	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
174	Exposición temporaria	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
175	Informalismo y expresionismo abstracto	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
176	Arte concreto y abstracción 1945-1970	\N	\N	\N	\N	139		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	t
345	Hall Central	\N	\N	\N	\N	343	0	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
346	Tienda del Museo	\N	\N	\N	\N	343		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
347	Sala 2. Arte europeo siglo XII al XVI	\N	\N	\N	\N	343	2	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
348	Sala 3. Arte europeo siglo XV al XVIII	\N	\N	\N	\N	343	3	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
349	Sala 4. Países Bajos siglo XVII	\N	\N	\N	\N	343	4	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
350	Escuela de La Boca y realismo social	\N	\N	\N	\N	344		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
351	Arte latinoamericano y argentino 1910-1945	\N	\N	\N	\N	344		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
352	Exposición temporaria	\N	\N	\N	\N	344		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
353	Informalismo y expresionismo abstracto	\N	\N	\N	\N	344		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
354	Arte concreto y abstracción 1945-1970	\N	\N	\N	\N	344		\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-05-30 00:37:14.101263-03	100	1	\N	\N	t
\.


--
-- Data for Name: roomrecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roomrecord (id, room_id, language, name, subtitle, info, photo, video, audio, created, lastmodified, lastmodifieduser, draft, audioautogenerate) FROM stdin;
\.


--
-- Data for Name: roomtype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roomtype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser, state, language, audioautogenerate) FROM stdin;
114	Sala de exhibición	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	1	\N	t
115	Pasillo	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	1	\N	t
\.


--
-- Data for Name: site; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.site (id, name, namekey, shortname, title, titlekey, sitetype_id, institution_id, website, mapurl, email, instagram, whatsapp, phone, twitter, subtitle, subtitlekey, info, infokey, address, addresskey, logo, photo, video, audio, map, created, lastmodified, lastmodifieduser, opens, openskey, abstract, intro, introkey, state, language, draft, masterlanguage, translation, usethumbnail, spec, audioautogenerate, zoneid, speechaudio, languages, labelpermanentexhibitions, labeltemporaryexhibitions, sortalphabetical) FROM stdin;
552	Basílica Nuestra Señora del Pilar	bas-lica-nuestra-se-ora-del-pilar	Iglesia del Pilar	Basílica Nuestra Señora del Pilar	bas-lica-nuestra-se-ora-del-pilar	\N	547	https://basilicadelpilar.org/	https://maps.app.goo.gl/x1ceVM3UppVU4P4u9	\N	\N	\N	4806 2209	\N	Se inauguró en 1732. Conserva retablos, imaginería y ornamentos originales	\N	La Iglesia de Nuestra Señora del Pilar fue inaugurada en 1732 y declarada Monumento Histórico Nacional en 1942. Está ubicada en el barrio de Recoleta y fue proyectada por los arquitectos jesuitas Bianchi y Prímoli. Hoy en día, se conservan sus majestuosos retablos, imaginería y ornamentos originales. En su origen estaban en las afueras de la ciudad, teniendo en cuenta las reglas de la orden de recogimiento y separación. El que aportó el dinero para la construcción fue un vecino proveniente de Zaragoza y puso la condición de que venerara a la Virgen del Pilar, muy popular en su ciudad de origen.  \r\n\r\nLa iglesia consta de una sola nave con un crucero muy desarrollado, cubierto por bóveda vaída. Las capillas laterales son poco profundas. En su interior, se destaca el retablo mayor (barroco), con la imagen titular en el centro y a sus costados dos santos franciscanos. El altar mayor es una pieza muy singular, con ornamentación inca del Alto Perú, muy ricamente trabajado en plata.\r\n\r\nLos altares laterales de la única nave, también son barrocos. La talla de madera de San Pedro de Alcántara –copatrono de la iglesia– es del siglo XVIII y se le atribuye al escultor Alonso Cano, mientras que el Altar de las Reliquias, según la tradición, fue un regalo del rey Carlos III de España. El púlpito es también de factura barroca.	\N	Junín 1898 \r\nCiudad Autónoma de Buenos Aires	\N	1499	558	\N	\N	\N	2025-09-03 17:31:49.976266-03	2025-10-03 20:48:57.298804-03	100	\N	\N	\N	\N	\N	1	..	\N	es	0	t	\N	t	America/Buenos_Aires	\N	{"en": "en", "pt-BR": "pt-BR"}	Colección permanente	Exhibiciones temporarias	t
564	Colección de Arte Amalia Lacroze de Fortabat	colecci-n-de-arte-amalia-lacroze-de-fortabat	Museo Fortabat	Colección de Arte Amalia Lacroze de Fortabat	colecci-n-de-arte-amalia-lacroze-de-fortabat	\N	561	www.coleccionfortabat.org.ar	\N	\N	\N	\N	\N	\N	Posee más de 150 obras de reconocidos artistas internacionales.	\N	Esta importante colección privada de arte (inaugurada en 2008 y reabierta en 2012) posee más de 150 obras de reconocidos artistas internacionales como Rodin, Warhol, Turner, Dalí y Blanes, así como de artistas argentinos tales como Badii, Berni, Quinquela Martín, Noé, Pérez Celis, Fader, Soldi y Xul Solar, entre otros.	\N	OLGA  COSSETTINI  141	\N	563	562	\N	\N	\N	2025-09-12 14:14:03.166733-03	2025-10-03 20:50:41.244989-03	100	\N	\N	\N	\N	\N	1	es	\N	es	0	t	\N	t	America/Buenos_Aires	\N	{"en": "en", "pt-BR": "pt-BR"}	Colección permanente	Exhibiciones temporarias	t
753	Museo Nacional de Arte Decorativo	\N	Museo de Arte Decorativo	\N	\N	\N	749	https://museoartedecorativo.cultura.gob.ar/	https://maps.app.goo.gl/XHiRj9nd8zt8VTBv8	decorativocontacto@gmail.com	\N	\N	+54 (011) 4806-8306	\N	Arte decorativo europeo y oriental de los siglos XVI al XIX	\N	El Museo Nacional de Arte Decorativo, de la Ciudad de Buenos Aires, está ubicado en lo que fuera una residencia particular por lo que constituye una casa-museo. Su colección está compuesta por más de 6000 piezas, entre las que se destacan esculturas, pinturas, tapices, armas, libros, cerámicas, mobiliario y miniaturas, fundamentalmente europeas y orientales, de los siglos XVI al XX. \r\n\r\nEs un museo nacional en el que, a través de sus  exhibiciones permanentes y temporarias, programas públicos y diferentes actividades culturales, se propone un espacio para el diálogo y el intercambio de conocimientos. Todos los públicos son invitados a acercarse al patrimonio, la colección y su historia y a reflexionar sobre esta casa-museo.	\N	Av. del Libertador 1902, Ciudad de Buenos Aires.	\N	751	756	\N	\N	\N	2025-10-28 14:50:12.810972-03	2025-10-30 12:12:47.119058-03	100	Miércoles de 13:00 a 19:00hs\r\nJueves de 13:00 a 19:00hs\r\nViernes de 13:00 a 19:00hs\r\nSábado de 13:00 a 19:00hs\r\nDomingo de 13:00 a 19:00hs	\N	\N	\N	\N	1	es	\N	es	\N	f	\N	f	America/Buenos_Aires	\N	\N	Colección permanente	Exhibiciones temporarias	t
860	Museo Latinoamericano de Arte Moderno	\N	MLM	\N	\N	\N	855	https://www.moma.org	\N	\N	\N	\N	\N	\N	Arte latinoamericano del siglo XX y XXI	\N	En el Museo de Arte Moderno  celebramos la creatividad, la apertura, la tolerancia y la generosidad. Nuestro objetivo es ser espacios inclusivos, tanto presenciales como virtuales, donde se aceptan diversas posturas culturales, artísticas, sociales y políticas. Nos comprometemos a compartir el arte moderno y contemporáneo más sugerente, y esperamos que nos acompañen a explorar el arte, las ideas y los problemas de nuestro tiempo.	\N	Moreau de Justo 2045. \r\nCiudad de Buenos Aires	\N	1493	859	\N	\N	\N	2025-11-28 10:43:20.500065-03	2025-11-28 10:43:20.500081-03	100	Lunes cerrado.\r\nMartes  a Viernes: 10 a 20 hs.\r\nSabados: 10 a 21 hs.\r\nDomingos: 10 a 21 hs.	\N	\N	\N	\N	1	..	\N	es	\N	t	\N	f	America/Buenos_Aires	\N	\N	Colección permanente	Exhibiciones temporarias	t
137	Museo Nacional de Bellas Artes	museo-nacional-de-bellas-artes	MNBA	Museo Nacional de Bellas Artes	\N	\N	135	https://www.bellasartes.gob.ar	https://maps.app.goo.gl/mCePQghfM2oUjmgy6	\N	\N	\N	54 911 64324235	\N	Posee la mayor colección de arte local y una de las más importantes de arte universal en toda Latinoamérica.	\N	El Museo Nacional de Bellas Artes es uno de los más importantes de Latinoamérica. \r\nSu colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad. \r\nEstá ubicado en el barrio de Recoleta y fue inaugurado en la Navidad de 1896.  \r\nDentro del arte internacional se destacan obras de El Greco, Goya, Rodin, Rembrandt, Rubens, Renoir, Degas, Cézanne, Chagall y Picasso. Entre los pintores argentinos más importantes, vas a sorprenderte con obras de Cándido López, Lino Enea Spilimbergo, Prilidiano Pueyrredón, Fernando Fader, Benito Quinquela Martín, Xul Solar, Antonio Berni, Marta Minujín, Emilio Pettoruti, Carlos Alonso, Antonio Seguí y León Ferrari. Además, posee un importante conjunto de arte latinoamericano, que reúne obras de Pedro Figari, Joaquín Torres García, Tarsila Do Amaral, Diego Rivera y Jesús Rafael Soto, entre otras.\r\nCuenta también con una sala de fotografía, otra de arte andino precolombino, dos terrazas de esculturas y una biblioteca con 150.000 ejemplares.	\N	MNBA	\N	462	531	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-11-24 13:06:31.807443-03	100	Lunes: cerrado \r\nMartes: de 11:00 a 19:30 \r\nMiércoles: de 11:00 a 19:30\r\nJueves: de 11:00 a 19:30\r\nViernes: de 11:00 a 19:30 \r\nSábado: de 10:00 a 19:30 \r\nDomingo: de 10:00 a 19:30	\N	Es una de las instituciones públicas de arte más importantes de Argentina.\\n Su colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad.	El Museo Nacional de Bellas Artes (MNBA), ubicado en la Ciudad de Buenos Aires, es una de las instituciones públicas de arte más importantes de Argentina. Alberga un patrimonio sumamente diverso, que incluye más de 12 000 piezas, entre pinturas, esculturas, dibujos, grabados, textiles y objetos. Su colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad.	\N	1	..	\N	es	0	t	\N	t	America/Argentina/Buenos_Aires	\N	{"en": "en", "pt-BR": "pt-BR"}	Colección permanente	Exhibiciones temporarias	t
555	Museo Moderno	museo-moderno	\N	Museo Moderno	museo-moderno	\N	553	https://museomoderno.org	https://maps.app.goo.gl/6AABaQxZXCyhcwzF7	info@museomoderno.org	\N	\N	011 4361 6919	\N	En el barrio de San Telmo, el museo alberga más de 7000 obras de arte argentino e internacional.	\N	En el corazón del Casco Histórico, en pleno barrio de San Telmo, el Museo Moderno pone a tu disposición una rica colección del arte argentino de los últimos años.\r\nLa propuesta del Moderno es acercarte a las artes visuales a través de sus 11.000 m2 y sus más de 7.000 obras.\r\nA su vez, el edificio es un fiel exponente de las construcciones inglesas de la era industrial del siglo XIX, con su estructura de hierro, grandes aberturas y su fachada de ladrillo a la vista. \r\nLa experiencia se complementa con el bar, que ofrece un excelente café de especialidad y un menú diseñado especialmente. No te olvides de visitar la tienda donde encontrarás todos los libros editados por el Moderno, una oportunidad para sumergirte en el universo de los artistas a través de estas publicaciones.	\N	Avenida San Juan 350	\N	\N	554	\N	\N	\N	2025-09-10 18:16:23.266138-03	2025-10-03 20:51:41.499541-03	100	\N	\N	\N	\N	\N	1	es	\N	es	0	t	\N	t	America/Buenos_Aires	\N	{"en": "en", "pt-BR": "pt-BR"}	Colección permanente	Exhibiciones temporarias	t
342	Museo de Arte Latinoaméricano de Buenos Aires	museo-de-arte-latinoam-ricano-de-buenos-aires	MALBA	Museo de Arte Latinoaméricano de Buenos Aires	\N	\N	341	https://www.malba.org.ar/	https://maps.app.goo.gl/VBMMA9osiJjECMoq6	\N	\N	\N	+54 11 4808 6500	\N	Alberga una de las colecciones de arte latinoamericano más importantes.	\N	El MALBA es una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región. Se specializa en el arte latinoamericano desde principios del siglo XX hasta la actualidad.	\N	Av. Figueroa Alcorta 3415\r\nC1425CLA Buenos Aires, Argentina	\N	565	560	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-09-19 09:41:50.633097-03	100	Jueves a lunes de 12:00 a 20:00.\r\nMiércoles de 11:00 a 20:00. \r\nMartes cerrado.	\N	Se specializa en el arte latinoamericano desde principios del siglo XX hasta la actualidad. \\nEs una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región.	El MALBA es una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región. Se specializa en el arte latinoamericano desde principios del siglo XX hasta la actualidad.	\N	1	es	\N	es	0	t	\N	t	America/Buenos_Aires	\N	\N	Colección permanente	Exhibiciones temporarias	t
177	Museo Benito Quinquela Martín	museo-benito-quinquela-mart-n	Museo Quinquela	Museo Benito Quinquela Martín	\N	\N	136	https://buenosaires.gob.ar/educacion/gestion-cultural/museo-benito-quinquela-martin	\N	\N	\N	\N	54 911 66892321	\N	Cuenta con la más amplia colección de óleos de este emblemático artista plástico y es todo un ícono del barrio de La Boca.	\N	El Museo "Benito Quinquela Martín" está ubicado en el barrio de La Boca en la Ciudad Autónoma de Buenos Aires, Argentina. \r\nFundado en el año 1938, está construido sobre terrenos originalmente donados por el pintor. Cuenta actualmente con la mayor colección reunida del artista plástico, más de 90 de sus obras. Además, el Museo ofrece un panorama del arte figurativo argentino desde fines del siglo XIX hasta mediados del siglo XX. Obras de grandes artistas, considerados iniciadores y precursores de las artes plásticas en el país, se encuentran exhibidas en este museo, donde el artista vivió y trabajó.	\N	Av. Don Pedro de Mendoza 1835, C1169 Cdad. Autónoma de Buenos Aires	\N	919	559	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-10-03 20:51:04.976098-03	100	\N	\N	El Museo "Benito Quinquela Martín" está ubicado en el barrio de La Boca en la Ciudad Autónoma de Buenos Aires, Argentina. Fundado en el año 1938, está construido sobre terrenos originalmente donados por el pintor. Cuenta actualmente con la mayor colección reunida del artista plástico, más de 90 de sus obras. Además, el Museo ofrece un panorama del arte figurativo argentino desde fines del siglo XIX hasta mediados del siglo XX. Obras de grandes artistas, considerados iniciadores y precursores de las artes plásticas en el país, se encuentran exhibidas en este museo, donde el artista vivió y trabajó.	El Museo "Benito Quinquela Martín" está ubicado en el barrio de La Boca en la Ciudad Autónoma de Buenos Aires, Argentina. Fundado en el año 1938, está construido sobre terrenos originalmente donados por el pintor. Cuenta actualmente con la mayor colección reunida del artista plástico, más de 90 de sus obras. Además, el Museo ofrece un panorama del arte figurativo argentino desde fines del siglo XIX hasta mediados del siglo XX. Obras de grandes artistas, considerados iniciadores y precursores de las artes plásticas en el país, se encuentran exhibidas en este museo, donde el artista vivió y trabajó.	\N	1	..	\N	es	0	t	\N	t	America/Buenos_Aires	\N	\N	Colección permanente	Exhibiciones temporarias	t
537	Cabildo de Buenos Aires	cabildo-de-buenos-aires	\N	Cabildo de Buenos Aires	cabildo-de-buenos-aires	\N	529	https://cabildonacional.cultura.gob.ar/	https://maps.app.goo.gl/eooviVBRmuy38Zcc7	cabildonacional@gmail.com	\N	\N	(011) 15 22642778\r\n(011) 4342-6729 / 4334-1782	\N	La sede de la administración colonial ocupa el mismo lugar desde 1580 y es un emblema de la historia viva. \r\nVisitá el corazón de la Revolución de Mayo.	\N	En este edificio funcionó el cabildo colonial fundado por Juan de Garay en 1580 durante la segunda fundación de la ciudad de Buenos Aires y que luego de la Revolución de Mayo de 1810, que derrocó al virrey español Baltasar Hidalgo de Cisneros y derivó en la guerra que llevó a la independencia de las Provincias Unidas del Río de la Plata, se transformó en una Junta de Gobierno que funcionó hasta su disolución en 1821 por el gobernador de Buenos Aires Martín Rodríguez.	\N	Bolívar 65\r\nCiudad Autónoma de Buenos Aires	\N	536	557	\N	\N	\N	2025-08-29 09:26:13.956557-03	2025-10-03 20:50:12.166903-03	100	Lunes. Cerrado\r\nMartes. Cerrado\r\nMiércoles. Abierto de 10:30 a 18:00hs\r\nJueves. Abierto de 10:30 a 18:00hs\r\nViernes. Abierto de 10:30 a 18:00hs\r\nSábado. Abierto de 10:30 a 18:00hs\r\nDomingo. Abierto de 10:30 a 18:00hs	\N	\N	En este edificio funcionó el cabildo colonial fundado por Juan de Garay en 1580 durante la segunda fundación de la ciudad de Buenos Aires y que luego de la Revolución de Mayo de 1810, que derrocó al virrey español Baltasar Hidalgo de Cisneros y derivó en la guerra que llevó a la independencia de las Provincias Unidas del Río de la Plata, se transformó en una Junta de Gobierno que funcionó hasta su disolución en 1821 por el gobernador de Buenos Aires Martín Rodríguez.	\N	1	es	\N	es	0	t	\N	t	America/Buenos_Aires	\N	{"en": "en", "pt-BR": "pt-BR"}	Colección permanente	Exhibiciones temporarias	t
\.


--
-- Data for Name: siteartist; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.siteartist (id, name, site_id, artist_id, state, created, lastmodified, lastmodifieduser, draft) FROM stdin;
\.


--
-- Data for Name: siterecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.siterecord (id, site_id, language, name, subtitle, info, photo, video, audio, created, lastmodified, lastmodifieduser, draft, namekey, state, intro, name_hash, subtitle_hash, info_hash, intro_hash, usethumbnail, opens, opens_hash, abstract_hash, abstract, spec, spec_hash, address, address_hash, otherjson, otherjson_hash, audioautogenerate, audioauto, labeltemporaryexhibitions, labelpermanentexhibitions) FROM stdin;
718	137	pt-BR	Museu Nacional de Belas Artes	Possui o maior acervo de arte argentina e um dos mais importantes acervos de arte universal de toda a América Latina.	O Museu Nacional de Belas Artes é um dos mais importantes da América Latina.\nSeu acervo inclui arte pré-colombiana, colonial, argentina e internacional, abrangendo o século III a.C. até os dias atuais.\nEstá localizado no bairro da Recoleta e foi inaugurado no Natal de 1896.\nDestaques da arte internacional incluem obras de El Greco, Goya, Rodin, Rembrandt, Rubens, Renoir, Degas, Cézanne, Chagall e Picasso. Entre os pintores argentinos mais importantes, você se surpreenderá com obras de Cándido López, Lino Enea Spilimbergo, Prilidiano Pueyrredón, Fernando Fader, Benito Quinquela Martín, Xul Solar, Antonio Berni, Marta Minujín, Emilio Pettoruti, Carlos Alonso, Antonio Seguí e León Ferrari. Abriga também um importante acervo de arte latino-americana, incluindo obras de Pedro Figari, Joaquín Torres García, Tarsila Do Amaral, Diego Rivera e Jesús Rafael Soto, entre outros.\nConta ainda com uma galeria de fotografia, uma galeria de arte andina pré-colombiana, dois terraços de esculturas e uma biblioteca com 150.000 exemplares.	\N	\N	\N	2025-10-24 15:28:37.024444-03	2025-10-28 14:19:58.797756-03	100	\N	\N	\N	O Museu Nacional de Belas Artes (MNBA), localizado na Cidade de Buenos Aires, é uma das instituições de arte pública mais importantes da Argentina. Abriga um acervo extremamente diversificado, com mais de 12.000 peças, entre pinturas, esculturas, desenhos, gravuras, tecidos e objetos. Seu acervo abrange arte pré-colombiana, colonial, argentina e internacional, abrangendo o século III a.C. até o presente.	289070387	1113148317	-453030878	869905709	f	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
716	342	en	Museo de Arte Latinoaméricano de Buenos Aires	\N	\N	\N	\N	\N	2025-10-24 14:16:03.842406-03	2025-10-24 14:16:03.842411-03	100	\N	\N	\N	\N	0	0	0	0	f	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
732	552	en	Basílica Nuestra Señora del Pilar	\N	\N	\N	\N	\N	2025-10-25 14:08:41.839912-03	2025-10-25 14:08:41.83994-03	100	\N	\N	\N	\N	0	0	0	0	f	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
733	552	pt-BR	Basílica Nuestra Señora del Pilar	\N	\N	\N	\N	\N	2025-10-25 14:10:36.53506-03	2025-10-25 14:10:36.535072-03	100	\N	\N	\N	\N	0	0	0	0	f	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
755	753	pt-BR	Museu Nacional de Artes Decorativas	Arte decorativa europeia e oriental dos séculos XVI ao XIX	O Museu Nacional de Artes Decorativas, localizado na cidade de Buenos Aires, está instalado no que antes era uma residência particular, servindo como uma casa-museu. Seu acervo compreende mais de 6.000 peças, incluindo esculturas, pinturas, tapeçarias, armas, livros, cerâmicas, móveis e miniaturas, principalmente europeias e orientais, do século XVI ao XX. É um museu nacional que, por meio de suas exposições permanentes e temporárias, programas públicos e diversas atividades culturais, oferece um espaço de diálogo e troca de conhecimentos. Todos os públicos são convidados a explorar o patrimônio, o acervo e sua história, e a refletir sobre esta casa-museu.	\N	\N	\N	2025-10-28 14:52:51.381161-03	2025-10-28 14:57:23.070702-03	100	\N	\N	\N	\N	1933197467	-549519145	-2078451995	0	f	\N	0	0	\N	\N	0	\N	0	\N	0	f	f	\N	\N
717	137	en	National Museum of Fine Arts	It has the largest collection of Argentine art and one of the most important collections of universal art in all of Latin America.	The National Museum of Fine Arts is one of the most important in Latin America. \nIts collection includes pre-Columbian, colonial, Argentine, and international art, spanning the 3rd century BC to the present. \nIt is located in the Recoleta neighborhood and was inaugurated at Christmas 1896. \nInternational art highlights include works by El Greco, Goya, Rodin, Rembrandt, Rubens, Renoir, Degas, Cézanne, Chagall, and Picasso. Among the most important Argentine painters, you will be amazed by works by Cándido López, Lino Enea Spilimbergo, Prilidiano Pueyrredón, Fernando Fader, Benito Quinquela Martín, Xul Solar, Antonio Berni, Marta Minujín, Emilio Pettoruti, Carlos Alonso, Antonio Seguí, and León Ferrari. It also houses an important collection of Latin American art, including works by Pedro Figari, Joaquín Torres García, Tarsila Do Amaral, Diego Rivera, and Jesús Rafael Soto, among others.\nIt also features a photography gallery, a gallery of pre-Columbian Andean art, two sculpture terraces, and a library with 150,000 copies.	\N	\N	\N	2025-10-24 15:14:27.289261-03	2025-10-28 14:19:44.307508-03	100	\N	\N	\N	The National Museum of Fine Arts (MNBA), located in the City of Buenos Aires, is one of the most important public art institutions in Argentina. It houses an extremely diverse collection, including more than 12,000 pieces, including paintings, sculptures, drawings, prints, textiles, and objects. Its collection includes pre-Columbian, colonial, Argentine, and international art, spanning the 3rd century BC to the present.	289070387	1113148317	-453030878	869905709	f	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
920	177	en	Museo Benito Quinquela Martín	\N	\N	\N	\N	\N	2025-12-01 10:38:25.848407-03	2025-12-01 10:38:25.848415-03	100	\N	\N	\N	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	f	f	\N	\N
754	753	en	National Museum of Decorative Art	European and Oriental decorative art from the 16th to the 19th centuries	The National Museum of Decorative Art, located in the city of Buenos Aires, is housed in what was once a private residence, thus serving as a house-museum. Its collection comprises more than 6,000 pieces, including sculptures, paintings, tapestries, weapons, books, ceramics, furniture, and miniatures, primarily European and Oriental, from the 16th to the 20th centuries. It is a national museum that, through its permanent and temporary exhibitions, public programs, and various cultural activities, offers a space for dialogue and the exchange of knowledge. All audiences are invited to explore the heritage, the collection, and its history, and to reflect on this house-museum.	\N	\N	\N	2025-10-28 14:50:35.95607-03	2025-10-28 14:57:31.425593-03	100	\N	\N	\N	\N	1933197467	-549519145	-2078451995	0	f	\N	0	0	\N	\N	0	\N	0	\N	0	f	f	\N	\N
861	860	es	Museo de Arte Moderno	\N	\N	\N	\N	\N	2025-11-28 10:43:20.521213-03	2025-11-28 10:43:20.521219-03	100	\N	\N	\N	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	f	f	\N	\N
863	860	pt-BR	Museo de Arte Moderno	\N	\N	\N	\N	\N	2025-11-28 10:43:20.523395-03	2025-11-28 10:43:20.523399-03	100	\N	\N	\N	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	f	f	\N	\N
904	555	en	Modern Museum	Located in the San Telmo neighborhood, the museum houses more than 7000 works of Argentine and international art.	In the heart of the Historic Quarter, in the San Telmo neighborhood, the Museum of Modern Art offers a rich collection of recent Argentine art.\n\nThe Museum's mission is to bring you closer to the visual arts through its 11,000 m2 and more than 7,000 works.\nThe building itself is a prime example of 19th-century English industrial architecture, with its iron structure, large windows, and exposed brick facade.\n\nThe experience is enhanced by the café, which offers excellent specialty coffee and a specially designed menu. Don't forget to visit the shop, where you'll find all the books published by the Museum—an opportunity to immerse yourself in the world of the artists through these publications.	\N	\N	\N	2025-11-30 09:06:31.724253-03	2025-11-30 09:06:38.612835-03	100	\N	\N	\N	\N	-459014715	690661447	1301743507	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	f	f	\N	\N
905	555	pt-BR	Museu Moderno	Localizado no bairro de San Telmo, o museu abriga mais de 7.000 obras de arte argentina e internacional.	No coração do Centro Histórico, no bairro de San Telmo, o Museu de Arte Moderna oferece uma rica coleção de arte argentina recente.\r\nA missão do Museu é aproximar você das artes visuais através de seus 11.000 m² e mais de 7.000 obras.\r\nO próprio edifício é um excelente exemplo da arquitetura industrial inglesa do século XIX, com sua estrutura de ferro, grandes janelas e fachada de tijolos aparentes.\r\nA experiência é enriquecida pelo café, que oferece um excelente café especial e um cardápio especialmente elaborado. Não deixe de visitar a loja, onde você encontrará todos os livros publicados pelo Museu — uma oportunidade de mergulhar no universo dos artistas através dessas publicações.	\N	\N	\N	2025-11-30 09:09:35.142037-03	2025-11-30 09:09:58.191525-03	100	\N	\N	\N	\N	-459014715	690661447	1301743507	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	f	f	\N	\N
921	177	pt-BR	Museo Benito Quinquela Martín	\N	\N	\N	\N	\N	2025-12-01 10:38:28.206876-03	2025-12-01 10:38:28.206879-03	100	\N	\N	\N	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	f	f	\N	\N
934	537	en	Cabildo de Buenos Aires	\N	\N	\N	\N	\N	2025-12-05 07:33:50.216818-03	2025-12-05 07:33:50.216826-03	100	\N	\N	\N	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
935	537	pt-BR	Cabildo de Buenos Aires	\N	\N	\N	\N	\N	2025-12-05 07:33:54.753365-03	2025-12-05 07:33:54.753368-03	100	\N	\N	\N	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
862	860	en	Latin American Museum of Modern Art	Latin American art of the 20th and 21st centuries	At the Museum of Modern Art, we celebrate creativity, openness, tolerance, and generosity. Our goal is to be inclusive spaces, both in person and online, where diverse cultural, artistic, social, and political perspectives are welcome. We are committed to sharing the most thought-provoking modern and contemporary art, and we hope you will join us in exploring the art, ideas, and issues of our time.	\N	\N	\N	2025-11-28 10:43:20.522668-03	2025-12-18 18:04:36.091005-03	100	\N	\N	\N	\N	-112572694	1286921917	421228180	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	f	f	\N	\N
\.


--
-- Data for Name: sitetype; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sitetype (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser, language, draft, audioautogenerate) FROM stdin;
110	Sede central	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-05-19 12:29:34.500268-03	100	\N	\N	t
\.


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_roles (id, user_id, general_role_id, site_role_id, institution_role_id) FROM stdin;
42192	101	936	\N	\N
42193	101	\N	\N	996
42194	101	\N	\N	987
42195	101	\N	1015	\N
42196	101	\N	1000	\N
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser, state, language, draft, audioautogenerate, locale, zoneid, password) FROM stdin;
1459	new	\N	\N	\N	2025-12-13 07:42:05.874287-03	2025-12-13 07:42:05.874306-03	100	3	es	\N	t	es	America/Buenos_Aires	$2a$10$BYMyEdPIANdXWuiRHYqbhOTh2ALWAefFYn3bBG1TlIpRA1WAvGVWe
100	root	\N	\N	\N	2025-05-19 12:29:34.500268-03	2025-11-04 13:16:39.022455-03	100	1	en	\N	t	es	America/Buenos_Aires	$2a$10$vlESj0PiND5tAA.FB6mcJ.0rmy1PYv.96xgSP.NXTt9RSAX/N.nCy
101	atolomei	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-11-04 13:16:47.414447-03	100	1	es	\N	t	es	America/Buenos_Aires	$2a$10$HjFAZ3OZnxf7lfS016iFlOcXyGv1K27dgyDyJuafPNqN65JTOaEJq
\.


--
-- Name: audio_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id', 40, true);


--
-- Name: audio_id_137_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_137_seq', 39, true);


--
-- Name: audio_id_177_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_177_seq', 1, false);


--
-- Name: audio_id_342_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_342_seq', 39, true);


--
-- Name: audio_id_537_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_537_seq', 3, true);


--
-- Name: audio_id_552_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_552_seq', 1, false);


--
-- Name: audio_id_555_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_555_seq', 1, false);


--
-- Name: audio_id_564_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_564_seq', 1, false);


--
-- Name: audio_id_753_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_753_seq', 39, true);


--
-- Name: audio_id_860_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_860_seq', 4, true);


--
-- Name: audit_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audit_id', 1096, true);


--
-- Name: objectstorage_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.objectstorage_id', 283, true);


--
-- Name: readcode_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.readcode_id', 1035, true);


--
-- Name: sequence_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sequence_id', 42196, true);


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
-- Name: artexhibitionguiderecord artexhibitionguiderecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguiderecord
    ADD CONSTRAINT artexhibitionguiderecord_pkey PRIMARY KEY (id);


--
-- Name: artexhibitionitem artexhibitionitem_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_pkey PRIMARY KEY (id);


--
-- Name: artexhibitionitemrecord artexhibitionitemrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitemrecord
    ADD CONSTRAINT artexhibitionitemrecord_pkey PRIMARY KEY (id);


--
-- Name: artexhibitionrecord artexhibitionrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionrecord
    ADD CONSTRAINT artexhibitionrecord_pkey PRIMARY KEY (id);


--
-- Name: artexhibitionsection artexhibitionsection_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsection
    ADD CONSTRAINT artexhibitionsection_pkey PRIMARY KEY (id);


--
-- Name: artexhibitionsectionrecord artexhibitionsectionrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsectionrecord
    ADD CONSTRAINT artexhibitionsectionrecord_pkey PRIMARY KEY (id);


--
-- Name: artexhibitionstatustype artexhibitionstatustype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionstatustype
    ADD CONSTRAINT artexhibitionstatustype_pkey PRIMARY KEY (id);


--
-- Name: artist artist_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artist
    ADD CONSTRAINT artist_pkey PRIMARY KEY (id);


--
-- Name: aa1 artwork_artist_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aa1
    ADD CONSTRAINT artwork_artist_pkey PRIMARY KEY (artwork_id, artist_id);


--
-- Name: artwork_artist artwork_artist_pkey1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork_artist
    ADD CONSTRAINT artwork_artist_pkey1 PRIMARY KEY (artwork_id, artist_id);


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
-- Name: artworkrecord artworkrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworkrecord
    ADD CONSTRAINT artworkrecord_pkey PRIMARY KEY (id);


--
-- Name: artworktype artworktype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworktype
    ADD CONSTRAINT artworktype_pkey PRIMARY KEY (id);


--
-- Name: audiostudio audiostudio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_pkey PRIMARY KEY (id);


--
-- Name: audit audit_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit
    ADD CONSTRAINT audit_pkey PRIMARY KEY (id);


--
-- Name: awpe awpe_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.awpe
    ADD CONSTRAINT awpe_pkey PRIMARY KEY (artwork_id, person_id);


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
-- Name: floorrecord floorrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floorrecord
    ADD CONSTRAINT floorrecord_pkey PRIMARY KEY (id);


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
-- Name: guidecontentrecord guidecontentrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontentrecord
    ADD CONSTRAINT guidecontentrecord_pkey PRIMARY KEY (id);


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
-- Name: institutionrecord institutionrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionrecord
    ADD CONSTRAINT institutionrecord_pkey PRIMARY KEY (id);


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
-- Name: personrecord personrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personrecord
    ADD CONSTRAINT personrecord_pkey PRIMARY KEY (id);


--
-- Name: privilege privilege_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.privilege
    ADD CONSTRAINT privilege_pkey PRIMARY KEY (id);


--
-- Name: resource resource_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.resource
    ADD CONSTRAINT resource_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: rolegeneral rolegeneral_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rolegeneral
    ADD CONSTRAINT rolegeneral_pkey PRIMARY KEY (id);


--
-- Name: roleinstitution roleinstitution_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roleinstitution
    ADD CONSTRAINT roleinstitution_pkey PRIMARY KEY (id);


--
-- Name: roles_privileges roles_privileges_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles_privileges
    ADD CONSTRAINT roles_privileges_pkey PRIMARY KEY (id);


--
-- Name: rolesite rolesite_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rolesite
    ADD CONSTRAINT rolesite_pkey PRIMARY KEY (id);


--
-- Name: room room_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room
    ADD CONSTRAINT room_pkey PRIMARY KEY (id);


--
-- Name: roomrecord roomrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roomrecord
    ADD CONSTRAINT roomrecord_pkey PRIMARY KEY (id);


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
-- Name: siteartist siteartist_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siteartist
    ADD CONSTRAINT siteartist_pkey PRIMARY KEY (id);


--
-- Name: siterecord siterecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siterecord
    ADD CONSTRAINT siterecord_pkey PRIMARY KEY (id);


--
-- Name: sitetype sitetype_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sitetype
    ADD CONSTRAINT sitetype_pkey PRIMARY KEY (id);


--
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


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
-- Name: artexhibition artexhibition_speechaudio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibition
    ADD CONSTRAINT artexhibition_speechaudio_fkey FOREIGN KEY (speechaudio) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: artexhibitionguide artexhibitionguide_speechaudio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguide
    ADD CONSTRAINT artexhibitionguide_speechaudio_fkey FOREIGN KEY (speechaudio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionguide artexhibitionguide_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguide
    ADD CONSTRAINT artexhibitionguide_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionguiderecord artexhibitionguiderecord_artexhibitionguide_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguiderecord
    ADD CONSTRAINT artexhibitionguiderecord_artexhibitionguide_id_fkey FOREIGN KEY (artexhibitionguide_id) REFERENCES public.artexhibitionguide(id) ON DELETE CASCADE;


--
-- Name: artexhibitionguiderecord artexhibitionguiderecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguiderecord
    ADD CONSTRAINT artexhibitionguiderecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionguiderecord artexhibitionguiderecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguiderecord
    ADD CONSTRAINT artexhibitionguiderecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionguiderecord artexhibitionguiderecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguiderecord
    ADD CONSTRAINT artexhibitionguiderecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionguiderecord artexhibitionguiderecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionguiderecord
    ADD CONSTRAINT artexhibitionguiderecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: artexhibitionitem artexhibitionitem_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: artexhibitionitem artexhibitionitem_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: artexhibitionitem artexhibitionitem_speechaudio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_speechaudio_fkey FOREIGN KEY (speechaudio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionitem artexhibitionitem_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitem
    ADD CONSTRAINT artexhibitionitem_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionitemrecord artexhibitionitemrecord_artexhibitionitem_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitemrecord
    ADD CONSTRAINT artexhibitionitemrecord_artexhibitionitem_id_fkey FOREIGN KEY (artexhibitionitem_id) REFERENCES public.artexhibitionitem(id) ON DELETE CASCADE;


--
-- Name: artexhibitionitemrecord artexhibitionitemrecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitemrecord
    ADD CONSTRAINT artexhibitionitemrecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionitemrecord artexhibitionitemrecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitemrecord
    ADD CONSTRAINT artexhibitionitemrecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionitemrecord artexhibitionitemrecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitemrecord
    ADD CONSTRAINT artexhibitionitemrecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionitemrecord artexhibitionitemrecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionitemrecord
    ADD CONSTRAINT artexhibitionitemrecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionrecord artexhibitionrecord_artexhibition_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionrecord
    ADD CONSTRAINT artexhibitionrecord_artexhibition_id_fkey FOREIGN KEY (artexhibition_id) REFERENCES public.artexhibition(id) ON DELETE CASCADE;


--
-- Name: artexhibitionrecord artexhibitionrecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionrecord
    ADD CONSTRAINT artexhibitionrecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionrecord artexhibitionrecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionrecord
    ADD CONSTRAINT artexhibitionrecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionrecord artexhibitionrecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionrecord
    ADD CONSTRAINT artexhibitionrecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionrecord artexhibitionrecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionrecord
    ADD CONSTRAINT artexhibitionrecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionsection artexhibitionsection_artexhibition_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsection
    ADD CONSTRAINT artexhibitionsection_artexhibition_id_fkey FOREIGN KEY (artexhibition_id) REFERENCES public.artexhibition(id) ON DELETE CASCADE;


--
-- Name: artexhibitionsection artexhibitionsection_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsection
    ADD CONSTRAINT artexhibitionsection_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionsection artexhibitionsection_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsection
    ADD CONSTRAINT artexhibitionsection_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionsection artexhibitionsection_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsection
    ADD CONSTRAINT artexhibitionsection_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionsection artexhibitionsection_speechaudio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsection
    ADD CONSTRAINT artexhibitionsection_speechaudio_fkey FOREIGN KEY (speechaudio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionsection artexhibitionsection_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsection
    ADD CONSTRAINT artexhibitionsection_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionsectionrecord artexhibitionsectionrecord_artexhibitionsection_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsectionrecord
    ADD CONSTRAINT artexhibitionsectionrecord_artexhibitionsection_id_fkey FOREIGN KEY (artexhibitionsection_id) REFERENCES public.artexhibitionsection(id) ON DELETE CASCADE;


--
-- Name: artexhibitionsectionrecord artexhibitionsectionrecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsectionrecord
    ADD CONSTRAINT artexhibitionsectionrecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionsectionrecord artexhibitionsectionrecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsectionrecord
    ADD CONSTRAINT artexhibitionsectionrecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionsectionrecord artexhibitionsectionrecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsectionrecord
    ADD CONSTRAINT artexhibitionsectionrecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionsectionrecord artexhibitionsectionrecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionsectionrecord
    ADD CONSTRAINT artexhibitionsectionrecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artexhibitionstatustype artexhibitionstatustype_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artexhibitionstatustype
    ADD CONSTRAINT artexhibitionstatustype_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artist artist_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artist
    ADD CONSTRAINT artist_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artist artist_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artist
    ADD CONSTRAINT artist_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artist artist_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artist
    ADD CONSTRAINT artist_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(id) ON DELETE CASCADE;


--
-- Name: artist artist_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artist
    ADD CONSTRAINT artist_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artist artist_speechaudio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artist
    ADD CONSTRAINT artist_speechaudio_fkey FOREIGN KEY (speechaudio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artist artist_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artist
    ADD CONSTRAINT artist_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artwork_artist artwork_artist_artist_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork_artist
    ADD CONSTRAINT artwork_artist_artist_id_fkey FOREIGN KEY (artist_id) REFERENCES public.artist(id) ON DELETE CASCADE;


--
-- Name: artwork_artist artwork_artist_artwork_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork_artist
    ADD CONSTRAINT artwork_artist_artwork_id_fkey FOREIGN KEY (artwork_id) REFERENCES public.artwork(id) ON DELETE CASCADE;


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
-- Name: aa1 artwork_person_artwork_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.aa1
    ADD CONSTRAINT artwork_person_artwork_id_fkey FOREIGN KEY (artwork_id) REFERENCES public.artwork(id);


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
-- Name: artwork artwork_qrcode_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_qrcode_fkey FOREIGN KEY (qrcode) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artwork artwork_site_owner_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_site_owner_id_fkey FOREIGN KEY (site_owner_id) REFERENCES public.site(id) ON DELETE RESTRICT;


--
-- Name: artwork artwork_speechaudio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artwork
    ADD CONSTRAINT artwork_speechaudio_fkey FOREIGN KEY (speechaudio) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: artworkrecord artworkrecord_artwork_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworkrecord
    ADD CONSTRAINT artworkrecord_artwork_id_fkey FOREIGN KEY (artwork_id) REFERENCES public.artwork(id) ON DELETE CASCADE;


--
-- Name: artworkrecord artworkrecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworkrecord
    ADD CONSTRAINT artworkrecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artworkrecord artworkrecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworkrecord
    ADD CONSTRAINT artworkrecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artworkrecord artworkrecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworkrecord
    ADD CONSTRAINT artworkrecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artworkrecord artworkrecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworkrecord
    ADD CONSTRAINT artworkrecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artworktype artworktype_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artworktype
    ADD CONSTRAINT artworktype_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: audiostudio audiostudio_artexhibitionguide_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_artexhibitionguide_id_fkey FOREIGN KEY (artexhibitionguide_id) REFERENCES public.artexhibitionguide(id) ON DELETE CASCADE;


--
-- Name: audiostudio audiostudio_artexhibitionguiderecord_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_artexhibitionguiderecord_id_fkey FOREIGN KEY (artexhibitionguiderecord_id) REFERENCES public.artexhibitionguiderecord(id) ON DELETE CASCADE;


--
-- Name: audiostudio audiostudio_artexhibitionitemrecord_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_artexhibitionitemrecord_id_fkey FOREIGN KEY (artexhibitionitemrecord_id) REFERENCES public.artexhibitionitemrecord(id) ON DELETE CASCADE;


--
-- Name: audiostudio audiostudio_artexhibitionrecord_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_artexhibitionrecord_id_fkey FOREIGN KEY (artexhibitionrecord_id) REFERENCES public.artexhibitionrecord(id) ON DELETE CASCADE;


--
-- Name: audiostudio audiostudio_artexhibitionsectionrecord_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_artexhibitionsectionrecord_id_fkey FOREIGN KEY (artexhibitionsectionrecord_id) REFERENCES public.artexhibitionsectionrecord(id) ON DELETE CASCADE;


--
-- Name: audiostudio audiostudio_artworkrecord_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_artworkrecord_id_fkey FOREIGN KEY (artworkrecord_id) REFERENCES public.artworkrecord(id) ON DELETE CASCADE;


--
-- Name: audiostudio audiostudio_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_audio_fkey FOREIGN KEY (audiospeechmusic) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: audiostudio audiostudio_guidecontent_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_guidecontent_id_fkey FOREIGN KEY (guidecontent_id) REFERENCES public.guidecontent(id) ON DELETE CASCADE;


--
-- Name: audiostudio audiostudio_guidecontentrecord_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_guidecontentrecord_id_fkey FOREIGN KEY (guidecontentrecord_id) REFERENCES public.guidecontentrecord(id) ON DELETE CASCADE;


--
-- Name: audiostudio audiostudio_institutionrecord_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_institutionrecord_id_fkey FOREIGN KEY (institutionrecord_id) REFERENCES public.institutionrecord(id) ON DELETE CASCADE;


--
-- Name: audiostudio audiostudio_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: audiostudio audiostudio_personrecord_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_personrecord_id_fkey FOREIGN KEY (personrecord_id) REFERENCES public.personrecord(id) ON DELETE CASCADE;


--
-- Name: audiostudio audiostudio_siterecord_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_siterecord_id_fkey FOREIGN KEY (siterecord_id) REFERENCES public.siterecord(id) ON DELETE CASCADE;


--
-- Name: audiostudio audiostudio_speechaudio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_speechaudio_fkey FOREIGN KEY (audiospeech) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: audit audit_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit
    ADD CONSTRAINT audit_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: awpe awpe_artwork_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.awpe
    ADD CONSTRAINT awpe_artwork_id_fkey FOREIGN KEY (artwork_id) REFERENCES public.artwork(id);


--
-- Name: awpe awpe_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.awpe
    ADD CONSTRAINT awpe_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(id);


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
-- Name: floorrecord floorrecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floorrecord
    ADD CONSTRAINT floorrecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: floorrecord floorrecord_floor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floorrecord
    ADD CONSTRAINT floorrecord_floor_id_fkey FOREIGN KEY (floor_id) REFERENCES public.floor(id) ON DELETE CASCADE;


--
-- Name: floorrecord floorrecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floorrecord
    ADD CONSTRAINT floorrecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: floorrecord floorrecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floorrecord
    ADD CONSTRAINT floorrecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: floorrecord floorrecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.floorrecord
    ADD CONSTRAINT floorrecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: guidecontent guidecontent_speechaudio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontent
    ADD CONSTRAINT guidecontent_speechaudio_fkey FOREIGN KEY (speechaudio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: guidecontent guidecontent_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontent
    ADD CONSTRAINT guidecontent_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: guidecontentrecord guidecontentrecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontentrecord
    ADD CONSTRAINT guidecontentrecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: guidecontentrecord guidecontentrecord_guidecontent_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontentrecord
    ADD CONSTRAINT guidecontentrecord_guidecontent_id_fkey FOREIGN KEY (guidecontent_id) REFERENCES public.guidecontent(id) ON DELETE CASCADE;


--
-- Name: guidecontentrecord guidecontentrecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontentrecord
    ADD CONSTRAINT guidecontentrecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: guidecontentrecord guidecontentrecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontentrecord
    ADD CONSTRAINT guidecontentrecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: guidecontentrecord guidecontentrecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontentrecord
    ADD CONSTRAINT guidecontentrecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: institution institution_speechaudio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institution
    ADD CONSTRAINT institution_speechaudio_fkey FOREIGN KEY (speechaudio) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: institutionrecord institutionrecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionrecord
    ADD CONSTRAINT institutionrecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: institutionrecord institutionrecord_institution_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionrecord
    ADD CONSTRAINT institutionrecord_institution_id_fkey FOREIGN KEY (institution_id) REFERENCES public.institution(id) ON DELETE CASCADE;


--
-- Name: institutionrecord institutionrecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionrecord
    ADD CONSTRAINT institutionrecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: institutionrecord institutionrecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionrecord
    ADD CONSTRAINT institutionrecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: institutionrecord institutionrecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.institutionrecord
    ADD CONSTRAINT institutionrecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: person person_speechaudio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.person
    ADD CONSTRAINT person_speechaudio_fkey FOREIGN KEY (speechaudio) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: personrecord personrecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personrecord
    ADD CONSTRAINT personrecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: personrecord personrecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personrecord
    ADD CONSTRAINT personrecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: personrecord personrecord_person_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personrecord
    ADD CONSTRAINT personrecord_person_id_fkey FOREIGN KEY (person_id) REFERENCES public.person(id) ON DELETE CASCADE;


--
-- Name: personrecord personrecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personrecord
    ADD CONSTRAINT personrecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: personrecord personrecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.personrecord
    ADD CONSTRAINT personrecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: privilege privilege_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.privilege
    ADD CONSTRAINT privilege_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: resource resource_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.resource
    ADD CONSTRAINT resource_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: role role_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: rolegeneral rolegeneral_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rolegeneral
    ADD CONSTRAINT rolegeneral_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: roleinstitution roleinstitution_institution_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roleinstitution
    ADD CONSTRAINT roleinstitution_institution_id_fkey FOREIGN KEY (institution_id) REFERENCES public.institution(id) ON DELETE CASCADE;


--
-- Name: roleinstitution roleinstitution_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roleinstitution
    ADD CONSTRAINT roleinstitution_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: roles_privileges roles_privileges_privilege_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles_privileges
    ADD CONSTRAINT roles_privileges_privilege_id_fkey FOREIGN KEY (privilege_id) REFERENCES public.privilege(id) ON DELETE CASCADE;


--
-- Name: roles_privileges roles_privileges_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles_privileges
    ADD CONSTRAINT roles_privileges_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.role(id) ON DELETE CASCADE;


--
-- Name: rolesite rolesite_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rolesite
    ADD CONSTRAINT rolesite_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: rolesite rolesite_site_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rolesite
    ADD CONSTRAINT rolesite_site_id_fkey FOREIGN KEY (site_id) REFERENCES public.site(id) ON DELETE CASCADE;


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
-- Name: roomrecord roomrecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roomrecord
    ADD CONSTRAINT roomrecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: roomrecord roomrecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roomrecord
    ADD CONSTRAINT roomrecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: roomrecord roomrecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roomrecord
    ADD CONSTRAINT roomrecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: roomrecord roomrecord_room_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roomrecord
    ADD CONSTRAINT roomrecord_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.room(id) ON DELETE CASCADE;


--
-- Name: roomrecord roomrecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roomrecord
    ADD CONSTRAINT roomrecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: site site_speechaudio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_speechaudio_fkey FOREIGN KEY (speechaudio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: site site_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: siteartist siteartist_artist_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siteartist
    ADD CONSTRAINT siteartist_artist_id_fkey FOREIGN KEY (artist_id) REFERENCES public.site(id) ON DELETE CASCADE;


--
-- Name: siteartist siteartist_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siteartist
    ADD CONSTRAINT siteartist_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: siteartist siteartist_site_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siteartist
    ADD CONSTRAINT siteartist_site_id_fkey FOREIGN KEY (site_id) REFERENCES public.site(id) ON DELETE CASCADE;


--
-- Name: siterecord siterecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siterecord
    ADD CONSTRAINT siterecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: siterecord siterecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siterecord
    ADD CONSTRAINT siterecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: siterecord siterecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siterecord
    ADD CONSTRAINT siterecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: siterecord siterecord_site_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siterecord
    ADD CONSTRAINT siterecord_site_id_fkey FOREIGN KEY (site_id) REFERENCES public.site(id) ON DELETE CASCADE;


--
-- Name: siterecord siterecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.siterecord
    ADD CONSTRAINT siterecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: sitetype sitetype_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sitetype
    ADD CONSTRAINT sitetype_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: user_roles user_roles_general_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_general_role_id_fkey FOREIGN KEY (general_role_id) REFERENCES public.rolegeneral(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_institution_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_institution_role_id_fkey FOREIGN KEY (institution_role_id) REFERENCES public.roleinstitution(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_site_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_site_role_id_fkey FOREIGN KEY (site_role_id) REFERENCES public.rolesite(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: users users_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- PostgreSQL database dump complete
--

