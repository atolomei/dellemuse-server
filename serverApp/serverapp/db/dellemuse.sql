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
    ordinal integer DEFAULT 0,
    accessible boolean DEFAULT false
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
    speechaudio bigint,
    audio_id bigint,
    artwork_audio_id bigint
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
-- Name: artist_sites; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artist_sites (
    artist_id bigint NOT NULL,
    site_id bigint NOT NULL
);


ALTER TABLE public.artist_sites OWNER TO postgres;

--
-- Name: artistrecord; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.artistrecord (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    language character varying(24) NOT NULL,
    artist_id bigint,
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
    draft text
);


ALTER TABLE public.artistrecord OWNER TO postgres;

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
    objecttype integer DEFAULT 0,
    audio_id bigint
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
-- Name: audio_id_42268_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_42268_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_42268_seq OWNER TO postgres;

--
-- Name: audio_id_50368_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_50368_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_50368_seq OWNER TO postgres;

--
-- Name: audio_id_50393_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audio_id_50393_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audio_id_50393_seq OWNER TO postgres;

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
    musicurl character varying(4096),
    infoaccessible text,
    audiospeechaccesible bigint,
    audiospeechmusicaccesible bigint,
    audio_speech_accesible_hash bigint DEFAULT '-1'::integer,
    audio_speech_music_accesible_hash bigint DEFAULT '-1'::integer,
    music_id bigint
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
    audio_id bigint,
    infoaccessible text,
    infoaccessibleisprimary boolean DEFAULT false,
    onlyaccesibleversion boolean DEFAULT false,
    audioaccessible bigint,
    artwork_audio_id bigint,
    artexhibition_item_audio_id bigint
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
    audioauto boolean DEFAULT false,
    infoaccessible text,
    infoaccesible_hash bigint DEFAULT 0,
    audioaccessible bigint
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
    zoneid character varying(512) DEFAULT 'America/Buenos_Aires'::character varying,
    languages json
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
-- Name: music; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.music (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    url character varying(4096),
    state integer DEFAULT 3,
    info text,
    composer bigint,
    composerstr character varying(512),
    performerstr character varying(512),
    genrestr character varying(512),
    draft text,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    info_json jsonb,
    license character varying(4096),
    technical_info text,
    royaltyfree boolean DEFAULT false
);


ALTER TABLE public.music OWNER TO postgres;

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
    filename character varying(2028),
    meta_json json
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
-- Name: voice; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.voice (
    id bigint DEFAULT nextval('public.sequence_id'::regclass) NOT NULL,
    name character varying(512) NOT NULL,
    namekey character varying(512),
    title character varying(1024),
    titlekey character varying(512),
    url character varying(4096),
    state integer DEFAULT 3,
    voiceid character varying(512),
    language character varying(64) DEFAULT 'es'::character varying,
    languageregion character varying(64),
    comment text,
    voicesettings jsonb,
    info text,
    infokey character varying(512),
    draft text,
    audio bigint,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    sex character varying(24)
);


ALTER TABLE public.voice OWNER TO postgres;

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
454	Sendas perdidas	sendas-perdidas	Sendas perdidas	\N	\N	137	\N	\N	Exposición temporal dedicada al artista argentino Germán Gárgano, que reunió más de 170 de sus obras (dibujos, acuarelas, tintas y gouaches) en el Museo Nacional de Bellas Artes. La muestra, curada por Pablo De Monte, exploró los universos visuales del artista, incluyendo ecos de su experiencia carcelaria, y se exhibió entre julio y agosto de 2025.	\N	https://www.bellasartes.gob.ar/exhibiciones/german-gargano/	\N	\N	\N	\N	\N	\N	466	\N	\N	f	2026-07-25 00:00:00-03	2028-07-25 00:00:00-03	2025-07-25 18:47:09.47387-03	2026-02-07 17:01:21.167409-03	100	\N	Germán Gárgano fue preso político en Argentina desde 1975 hasta 1982, y  mantuvo una relación epistolar desde la cárcel con el pintor Carlos Gorriarena, con quien continuaría formándose al recuperar su libertad.  “Sendas perdidas” es su primera muestra en el Museo Nacional de Bellas Artes, con más de 170 dibujos, gouaches, acuarelas y tintas realizadas en los últimos años.	\N	\N	\N	\N	\N	\N	3	es	\N	es	0	t	t	\N	0
597	Carrie Bencardino. El desentierro del diablo	new	\N	\N	\N	342	\N	\N	El desentierro del diablo, curada por Carlos Gutiérrez, surge del reconocimiento de una posible crisis de la imaginación, alimentada por los vaivenes políticos y el avance de un tipo de pensamiento que tiende a disolver los vínculos entre las personas. Conformada mayormente por pinturas, la muestra busca transformar la sala en un espacio donde se cruzan distintos espacios: es casi un bar, casi un cine, casi un club. Del mismo modo, las pinturas funcionan de manera similar, mostrando situaciones que podrían suceder en cualquier lugar, real o fantástico. Para Bencardino, resulta imperante la necesidad de engendrar nuevas herramientas para construir otros mundos.\r\n\r\nEl trabajo de Bencardino se alimenta de imágenes encontradas en libros, revistas, tapas de discos, videoclips, internet y su archivo personal de objetos y otros materiales que circulan en la cultura de masas y sus plataformas. Sus referencias provienen de un imaginario afectivo muy particular: las estéticas de las comunidades queer y las adolescencias de su generación, los códigos visuales de las escenas contraculturales (como el punk, y los distintos subgéneros del metal), el comic y la ilustración (como Ciruelo, Victoria Francés, Luis Royo, Boris Vallejo y Magalí Villeneuve), y el imaginario fantástico literario (especialmente, de William Blake y J.R.R. Tolkien), entre muchas otras referencias. Procesa y distorsiona estas imágenes digitalmente, y a partir de esas nuevas imágenes elabora sus pinturas.\r\n\r\nTrabaja desde un posicionamiento crítico sobre la circulación y la reapropiación de las imágenes en la era contemporánea, donde las fuentes y referentes visuales se multiplican, se entremezclan y se transforman constantemente. En su pintura, la apropiación no es un fin en sí mismo, sino un medio para interrogar la memoria cultural, las iconografías compartidas y la relación afectiva que establecemos con las imágenes.\r\n\r\nLa exposición cuenta también con una pieza de video que es un monólogo sobre la incidencia de distintos artistas, el surrealismo y el pensamiento mágico en la configuración del imaginario de Bencardino. La muestra destaca así las conexiones entre la imaginación –o más precisamente, la capacidad de imaginar– y la política, involucrándose con la producción de imágenes y recuperando indicios pasados que pudieran torcer las ideas totalizantes del relato colectivo. Así, este proyecto intenta disputar lugares donde las ideas y el deseo de un horizonte común no nos encuentre del todo agotados.	\N	https://malba.org.ar/evento/carrie-bencardino-el-desentierro-del-diablo/	\N	\N	\N	\N	\N	\N	599	\N	\N	f	\N	\N	2025-09-19 20:31:16.484359-03	2025-10-24 23:33:03.394564-03	100	Nivel -1	El &quot;desentierro del diablo&quot; es una celebración ancestral del Carnaval jujeño en Argentina, que marca el inicio de los festejos con el desentierro simbólico del Pujllay (o espíritu del carnaval), una figura que representa la alegría y el espíritu festivo.	\N	Visitas guiadas\r\nLunes a las 17:00.\r\nIncluidas con el ticket de ingreso al museo.	\N	Curaduría\r\nCarlos Gutiérrez	El desentierro del diablo	\N	1	es	\N	es	0	t	t	\N	0
774	Alberto Churba. Diseño Infinito	\N	\N	\N	\N	753	\N	\N	El Museo Nacional de Arte Decorativo presenta ALBERTO CHURBA. DISEÑO INFINITO, la primera retrospectiva dedicada a este destacado diseñador y artista decorativo argentino. \r\n\r\nEntre las décadas de 1960 y 1990 lideró el Estudio CH, firma que transformó la escena de la decoración en el país.\r\n\r\nLa muestra, curada por Sandra Hillar y Wustavo Quiroga, revisa su legado con un enfoque especial en diversas áreas —textiles, alfombras, vidrios, objetos y mobiliario—, todas reconocidas por su elevada calidad plástica y técnica. Como ejemplo de su genio creador, el célebre sillón Cinta forma parte de las colecciones del MoMA de Nueva York y del Victoria &amp;amp;amp;amp;amp;amp;amp; Albert Museum de Londres, entre otras instituciones.\r\n\r\nLos diseños de Alberto Churba marcaron una época y continúan siendo una referencia audaz para las nuevas generaciones. Desde el ámbito familiar, impulsó una práctica de diseño y emprendimiento que trascendió su apellido, dejando una huella indiscutible en la cultura argentina.\r\n\r\nLa exposición reunirá numerosas piezas de colecciones privadas e institucionales que, por primera vez, se activarán junto al autor para narrar su trayectoria, su proceso creativo y el impacto de su obra en la historia del diseño nacional.	\N	https://museoartedecorativo.cultura.gob.ar/exhibicion/alberto-churba-diseno-infinito/	\N	\N	\N	\N	\N	\N	781	\N	\N	f	\N	\N	2025-10-29 13:25:50.663635-03	2025-10-29 19:11:10.734376-03	100	\N	El Museo Nacional de Arte Decorativo presenta una exposición homenaje a Alberto Churba, referente indiscutido del diseño nacional. Alberto Curba. Diseño Infinito invita a explorar su universo creativo a través de piezas emblemáticas que marcaron un antes y un después en la historia del diseño en América Latina.	\N	Mié a dom | 13 a 19 h | Entrada libre y gratuita	\N	\N	Diseño Infinito	\N	\N	es	\N	es	\N	f	f	\N	0
757	Viaggio in Italia: 1920-1950, La Edad de Oro del Afiche Turístico Italiano	\N	\N	\N	\N	753	El diseño italiano, desde los artificios del Art Déco hasta el diseño gráfico Mid-Century	\N	El Instituto Italiano de Cultura de Buenos Aires (IIC) y el Museo Nacional de Arte Decorativo presentan la exposición "Viaggio in Italia.\r\nLa edad de oro del afiche turístico italiano (1520-1550)" una muestra que invita a recorrer la evolución de la gráfica publicitaria italiana dedicada al turismo a través de una extraordinaria selección de afiches originales provenientes de la colección de Alessandro Bellenda.\r\n\r\nRealizados entre las décadas de 1920 y 1950, los afiches exhibidos fueron producidos por el ENIT (Ente Nacional Italiano para el Turismo) y las Ferrovie dello Stato (ferrocarriles estatales), organismos que promovieron una nueva estética visual para dar a conocer Italia al mundo.\r\n\r\nLas obras llevan la firma de grandes maestros del cartelismo italiano como Marcello Dudovich, Mario Borgoni, Gino Boccasile, Aurelio Craffonara y Franz Lenhart, entre otros.\r\n\r\nMediante el uso de colores vibrantes, figuras idealizadas y escenarios icónicos, estos artistas crearon un verdadero canon estético del viaje turístico en Italia, consolidando una imagen del país que aún hoy sigue inspirando a diseñadores y creativos, reseña una comunicación del IIC.\r\n\r\nMás allá de su valor artístico, la exposición ofrece una lectura histórica y sociológica: los afiches no solo narran la evolución del diseño gráfico italiano, sino también la construcción del imaginario del "bel paese" (país hermoso) y las formas en que Italia se presentó al público internacional durante un período de intensas transformaciones sociales y culturales.\r\n\r\n"Viaggio in Italia" se inaugura en el marco de la Semana de la Lengua Italiana en el Mundo, importante cita anual que se desarrolla globalmente a través de la red de Institutos Italianos de Cultura y Embajadas de Italia, y que celebra el idioma y la creatividad italiana en sus múltiples expresiones.\r\n \r\nLa muestra se presenta en dos sedes: el Museo Nacional de Arte Decorativo, donde se exhibe el núcleo principal, y la Sala Roma del Istituto Italiano di Cultura, que albergará una selección complementaria de piezas gráficas.	\N	\N	https://maps.app.goo.gl/XHiRj9nd8zt8VTBv8	\N	\N	\N	\N	\N	759	\N	\N	f	2025-10-20 00:00:00-03	2026-01-20 00:00:00-03	2025-10-28 14:59:23.944328-03	2025-10-30 13:53:26.562168-03	100	\N	Exposición temporaria del Museo Nacional de Arte Decorativo coproducida con el Instituto Italiano de Cultura, que celebra la historia del afiche turístico.\r\nUna muestra que reúne 70 afiches creados entre 1920 y 1950 por destacados ilustradores italianos como Marcello Dudovich, Mario Borgoni, Gino Boccasile, Aurelio Craffonara y Franz Lenhart. Obras que marcaron la identidad visual de toda una época y difundieron la imagen de Italia en el mundo.	\N	Del 20 Oct. 2025\r\nHasta el 20 Enero 2026	\N	\N	Viaggio in Italia	\N	\N	es	\N	es	\N	f	f	\N	0
839	borrar esta new	\N	\N	\N	\N	537	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	f	\N	\N	2025-11-23 21:44:14.807191-03	2025-11-23 21:45:18.120577-03	100	\N	\N	\N	\N	\N	\N	\N	\N	5	es	\N	es	\N	t	f	\N	0
830	newsss	\N	\N	\N	\N	537	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	f	\N	\N	2025-11-23 19:51:03.61056-03	2025-11-26 11:43:46.776796-03	100	\N	\N	\N	\N	\N	\N	ddd	\N	5	es	\N	es	\N	t	f	\N	0
864	Wifredo Lam, cuando no duermo, sueño	\N	\N	\N	\N	860	Arte y descolonización	\N	Las pinturas de Wifredo Lam expandieron los horizontes del modernismo al crear un espacio significativo para la belleza y la profundidad de la cultura de la diáspora negra. Nacido en Cuba a principios del siglo XX, Lam forjó sus convicciones políticas y su compromiso con la pintura moderna en la Europa devastada por la guerra en la década de 1930. Su exilio y regreso al Caribe tras 18 años en el extranjero lo impulsaron a reimaginar radicalmente su proyecto artístico a través de las historias afrocaribeñas.\r\n\r\nPara Lam, de ascendencia africana y china, la creación de su nuevo y vívido imaginario fue más que una forma de autorreflexión. Declaró célebremente que su arte era un "acto de descolonización". Sus experimentos formales, sus figuras y paisajes transformadores, y su afinidad por la poesía y la colaboración le permitieron romper con las estructuras coloniales que encontró en el arte y en la vida. “Sabía que corría el riesgo de no ser comprendido ni por el hombre de la calle ni por los demás”, dijo Lam, “pero una imagen verdadera tiene el poder de poner a trabajar la imaginación, aunque lleve tiempo”. Wifredo Lam: When I Don’t Sleep, I Dream es la primera retrospectiva en Estados Unidos que presenta la trayectoria completa de la extraordinaria visión de Lam, invitándonos a ver el mundo de una manera nueva.	\N	https://www.moma.org/calendar/exhibitions/5877	\N	\N	\N	\N	\N	\N	19611	\N	\N	f	2025-10-20 00:00:00-03	2026-01-20 00:00:00-03	2025-11-28 10:51:25.218691-03	2026-01-04 17:59:31.491565-03	100	MoMA, Floor 3, 3 East\r\nThe Robert B. Menschel Galleries	Primera retrospectiva en Estados Unidos en presentar la trayectoria completa de la extraordinaria visión de Wilfredo Lam (1902-1982), invitándonos a ver el mundo de una manera nueva. Lam declaró célebremente que su arte era un "acto de descolonización".	\N	En horarios que está abierto el Museo.	\N	\N	Wifredo Lam	\N	1	es	\N	es	\N	t	f	\N	0
875	Colección 1880s-1940s	\N	\N	\N	\N	860	Arte moderno latinoamericano	\N	Organizadas en un orden cronológico aproximado, cada una de las presentaciones de esta planta explora un tema específico. Una sala puede estar dedicada a un artista, un medio o disciplina específicos, un lugar particular en un momento histórico o una idea creativa compartida. Un programa continuo de reinstalación frecuente presentará una amplia gama de obras de arte en nuevas combinaciones, un recordatorio de que se pueden explorar innumerables ideas e historias a través de la dinámica colección del Museo.	\N	\N	\N	\N	\N	\N	\N	\N	17171	\N	\N	t	\N	\N	2025-11-28 21:32:20.050863-03	2025-11-28 21:32:20.050889-03	100	Piso 5	Organizadas en un orden cronológico aproximado, cada una de las presentaciones de esta planta explora un tema específico. Una sala puede estar dedicada a un artista, un medio o disciplina específicos, un lugar particular en un momento histórico o una idea creativa compartida	\N	\N	\N	\N	\N	\N	1	es	\N	es	\N	t	f	\N	0
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
284	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	arte-argentino-siglo-xix-hacia-la-consolidaci-n-de-un-modelo-nacional	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	arte-argentino-siglo-xix-hacia-la-consolidaci-n-de-un-modelo-nacional	\N	137		\N	La consolidación de un modelo nacional en el arte argentino fue un proceso gradual que se dio en el siglo XIX y principios del XX, marcado por la búsqueda de una identidad visual propia y la creación de instituciones que promoviesen el arte nacional. Se buscó reflejar la realidad argentina, las costumbres locales y la historia del país, diferenciándose de las influencias europeas.\\n En la segunda mitad del siglo XIX, el arte argentino se caracterizó por el retrato, donde los artistas plasmaban a los personajes relevantes de la nueva nación, siguiendo los cánones neoclásicos de la época. \\n Luego a fines del siglo XIX y principios del XX, se buscó una identidad visual propia, con la creación de instituciones como El Ateneo, donde literatos y artistas debatiían sobre la existencia de un "arte nacional".	\N	https://www.bellasartes.gob.ar	\N	\N	\N	\N	\N	\N	363	\N	\N	t	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	2025-11-23 21:54:24.078937-03	100	Planta baja sala 20	La consolidación de un modelo nacional en el arte argentino fue un proceso que se dió a fines del siglo XIX y principios del XX, marcado por la búsqueda de una identidad visual propia y la creación de instituciones que promoviesen el arte nacional. Se buscó reflejar la realidad argentina, las costumbres locales y la historia del país, diferenciándose de las influencias europeas.	\N	\N	\N	\N	\N	\N	3	es	\N	es	0	t	t	\N	0
42429	Arquitectura	\N	\N	\N	\N	42268	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	42508	\N	\N	t	\N	\N	2026-01-21 05:40:16.033317-03	2026-01-21 05:40:16.033332-03	100	\N	\N	\N	\N	\N	\N	\N	\N	1	es	\N	es	\N	t	t	\N	0
50254	new	\N	\N	\N	\N	137	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	f	\N	\N	2026-02-07 17:05:47.024102-03	2026-02-07 17:06:36.441064-03	100	\N	\N	\N	\N	\N	\N	\N	\N	5	..	\N	es	\N	t	t	\N	0
50340	new	\N	\N	\N	\N	137	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	f	\N	\N	2026-02-10 16:00:33.929536-03	2026-02-10 16:28:57.353936-03	100	\N	\N	\N	\N	\N	\N	\N	\N	5	..	\N	es	\N	t	t	\N	0
50435	Legados del hacer	\N	\N	\N	\N	50393	Homenaje a Teresa Anchorena	\N	Legados del hacer. Homenaje a Teresa Anchorena” propone una mirada que se aleja de la clasificación rígida para activar vínculos entre piezas destacadas de la colección del Fondo Nacional de las Artes. Vasijas, cestos, textiles, facones y estribos dialogan en un mismo espacio, donde materiales, técnicas y épocas se entrecruzan.\r\n\r\nEl recorrido prioriza el encuentro y la afinidad por sobre la taxonomía. Las obras se vinculan por sus colores, sus usos y los saberes que las atraviesan, poniendo en valor el hacer artesanal como una práctica viva y colectiva, constitutiva de la identidad cultural argentina.\r\n\r\nLa muestra rinde también homenaje a Teresa Anchorena, directora de Patrimonio y Artesanías del FNA fallecida este año e impulsora de esta iniciativa. Su labor y compromiso fueron clave para la puesta en valor del patrimonio del organismo y para construir una relación respetuosa y activa con los territorios, los oficios y las comunidades que, desde tiempos ancestrales, construyen ese legado que hoy se comparte con el público.	\N	\N	\N	\N	\N	\N	\N	\N	50442	\N	\N	f	2025-12-18 00:00:00-03	2026-02-18 00:00:00-03	2026-02-11 14:50:46.658815-03	2026-02-12 16:18:36.584769-03	100	\N	\N	\N	\N	\N	\N	\N	\N	3	es	\N	es	\N	t	t	\N	0
\.


--
-- Data for Name: artexhibitionguide; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionguide (id, name, namekey, title, titlekey, publisher_id, artexhibition_id, artexhibitionguideorder, subtitle, subtitlekey, official, info, infokey, photo, video, audio, created, lastmodified, lastmodifieduser, opens, openskey, state, language, draft, masterlanguage, translation, usethumbnail, intro, spec, audioautogenerate, speechaudio, audio_id, ordinal, accessible) FROM stdin;
624	Carrie Bencardino El desentierro del diablo	carrie-bencardino-el-desentierro-del-diablo	\N	\N	180	597	0	\N	\N	f	\N	\N	\N	\N	\N	2025-09-25 11:10:46.040436-03	2025-09-25 11:10:46.040452-03	100	\N	\N	1	es	\N	es	0	t	\N	\N	f	\N	33	0	f
458	Sendas perdidas	\N	Sendas perdidas	\N	180	454	0	\N	\N	t	Germán Gárgano es un pintor argentino. Nace en 1953, en Buenos Aires, Argentina. Cursando sus estudios de medicina es detenido por razones políticas en 1975, situación que se prolonga hasta fines de 1982.	\N	\N	\N	817	2025-07-25 18:47:09.47387-03	2025-11-19 16:41:25.891306-03	100	\N	\N	1	es	\N	es	0	t	\N	\N	f	\N	34	0	f
202	Obras Maestras	obras-maestras	Obras Maestras	\N	180	199	0	\N	\N	f	La obra maestra era el nombre que recibía la pieza artesanal que debía realizar todo oficial que quisiera acceder a la categoría de maestro en el seno de los gremios.	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	\N	\N	1	es	\N	es	0	t	\N	\N	f	\N	31	0	f
292	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	\N	Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional	\N	180	284	0	\N	\N	t	\N	\N	\N	\N	\N	2025-05-19 12:30:25.063882-03	2026-02-15 22:02:34.054146-03	100	\N	\N	3	es	\N	es	0	t	\N	\N	f	\N	32	0	f
248	Grandes obras del Impresionismo	\N	Grandes obras del Impresionismo	\N	180	233	0	\N	\N	t	\N	\N	\N	\N	637	2025-05-19 12:29:43.192411-03	2025-10-02 14:51:27.613198-03	100	\N	\N	3	es	\N	es	0	t	\N	\N	f	\N	35	0	f
894	Wifredo Lam, cuando no duermo, sueño	\N	\N	\N	\N	864	0	\N	\N	t	Se trata de la retrospectiva más extensa dedicada al artista en Argentina, abarcando las seis décadas de la prolífica carrera de Lam.\r\nLa exposición incluye más de 130 obras de arte de las décadas de 1920 a 1970, incluyendo pinturas, obras a gran escala sobre papel, dibujos colaborativos, libros ilustrados, grabados, cerámicas y material de archivo, con préstamos clave del Estate of Wifredo Lam, París. La retrospectiva revela cómo Lam, un artista nacido en Cuba que pasó la mayor parte de su vida en España, Francia e Italia, llegó a encarnar la figura del artista transnacional en el siglo XX.	\N	\N	\N	911	2025-11-29 07:51:58.975092-03	2025-11-29 07:51:58.975099-03	100	\N	\N	1	es	\N	es	\N	t	\N	\N	f	\N	1	0	f
799	Alberto Churba. Diseño Infinito	\N	\N	\N	\N	774	0	\N	\N	t	La muestra “Diseño Infinito” presenta 40 años de producción del reconocido diseñador argentino, con piezas históricas que van desde óleos a diferentes objetos, textiles y mobiliario de autor.	\N	\N	\N	\N	2025-11-11 21:50:04.54671-03	2025-11-11 21:50:36.325046-03	100	\N	\N	1	es	\N	es	\N	f	\N	\N	f	\N	37	0	f
411	Museo Secreto	\N	Museo Secreto. Del archivo a la sala	\N	180	362	0	De la reserva a la sala	\N	t	Entre sus múltiples misiones, los museos preservan la memoria de una nación y, al mismo tiempo, como instituciones dinámicas, estimulan la construcción de nuevos sentidos para las piezas que albergan. Los modos en que las obras de una colección se dan a conocer al público moldean la relación que una comunidad entabla con la historia, vínculo que se potencia cuando una institución es permeable a las transformaciones estéticas y sociales que trae cada época.	\N	\N	\N	50377	2025-06-10 15:14:26.074639-03	2025-11-12 13:22:35.854925-03	100	\N	\N	3	es	\N	es	0	t	\N	\N	f	\N	38	0	f
786	Viaggio in Italia: 1920-1950, La Edad de Oro del Afiche Turístico Italiano	\N	\N	\N	\N	757	0	El diseño italiano, desde los artificios del Art Déco hasta el diseño gráfico Mid-Century	\N	t	La muestra comprende tres décadas atravesados por el nacimiento y auge del fascismo y la  figura de Benito Mussolini, la alianza con la Alemania nazi durante la Segunda Guerra Mundial, y la posguerra.\r\n\r\nEntre 1920 y 1950, el turismo en Italia se transformó en un fenómeno de masas y el afiche fue una herramienta clave:  síntesis entre arte, diseño y promoción cultural, pieza publicitaria que a la vez era obra de arte que reflejaba la cultura y la estética de su tiempo.\r\n\r\nEn la década del 20 se produce una transición desde el eclecticismo, el Liberty o Art Nouveau de principios de siglo para pasar en los 30 a un estilo modernista más sobrio con influencia de vanguardias europeas, como el cubismo, y especialmente el Futurismo italiano, un movimiento artístico fundado por Filippo Tommaso Marinetti  que se caracterizó por la exaltación de la velocidad, la tecnología, la modernidad, la violencia y el dinamismo, rechazando las tradiciones y el pasado.\r\n\r\nDurante el fascismo, estas obras turísticas escaparon al control de la censura, pero no de la influencia de la promoción política. Al igual que en Alemania y la Unión Soviética la vanguardia artística que exaltaba el nuevo hombre resultó demasiado libre; en Italia cedió su lugar al “realismo del fascismo”, un arte similar al realismo soviético, que se llamó “el retorno al orden”. \r\n\r\nLa muestra incluye obras de ilustradores italianos de gran renombre, como Marcello Dudovich y Mario Borgoni, que jugaron un papel crucial en la creación de una identidad turística para el país; y también de Marcello Nizzoli, quién en la postguerra diseñaría las célebres máquinas de escribir portátiles Olivetti.	\N	\N	\N	790	2025-10-30 13:44:50.917557-03	2025-10-30 14:56:07.855997-03	100	\N	\N	1	es	\N	es	\N	f	\N	\N	f	\N	36	0	f
50443	Legados del hacer	\N	\N	\N	\N	50435	0	Homenaje a Teresa Anchorena	\N	t	Legados del hacer incluye vasijas, cestos, textiles, facones y estribos de la colección del Fondo Nacional de las Artes. \r\nLas obras ponen en valor el hacer artesanal como una práctica viva y colectiva, constitutiva de la identidad cultural argentina.\r\n\r\nLa muestra rinde también homenaje a Teresa Anchorena, directora de Patrimonio y Artesanías del FNA fallecida este año e impulsora de esta iniciativa. Su labor y compromiso fueron clave para la puesta en valor del patrimonio del organismo y para construir una relación respetuosa y activa con los territorios, los oficios y las comunidades que, desde tiempos ancestrales, construyen ese legado que hoy se comparte con el público.	\N	\N	\N	50669	2026-02-11 14:53:25.385409-03	2026-02-11 14:53:25.385417-03	100	\N	\N	3	es	\N	es	\N	t	\N	\N	t	\N	1	0	f
50261	Museo Secreto	\N	\N	\N	\N	362	0	\N	\N	t	\N	\N	\N	\N	\N	2026-02-07 17:06:59.391356-03	2026-02-07 17:20:52.792615-03	100	\N	\N	3	es	\N	es	\N	t	\N	\N	t	\N	40	0	t
50306	Arquitectura	\N	\N	\N	\N	42429	0	\N	\N	t	\N	\N	\N	\N	\N	2026-02-08 19:05:59.211284-03	2026-02-08 19:05:59.211287-03	100	\N	\N	1	es	\N	es	\N	t	\N	\N	t	\N	1	0	f
50313	Arquitectura	\N	\N	\N	\N	42429	0	\N	\N	t	\N	\N	\N	\N	\N	2026-02-08 19:06:07.334022-03	2026-02-08 19:06:07.334027-03	100	\N	\N	1	es	\N	es	\N	t	\N	\N	t	\N	2	0	t
33825	El Cabildo de Buenos Aires	\N	\N	\N	\N	27577	0	\N	\N	t	La historia del Cabildo comenzó en 1580. Ese año, Juan de Garay fundó la ciudad de La Trinidad, que más tarde pasó a llamarse por el nombre de su puerto: Santa María del Buen Ayre.\r\n\r\nLos cabildos se originaron en España como forma de administración de las ciudades y sus alrededores. Luego fueron trasladados a los dominios americanos. Según las Leyes de Indias, que regulaban la vida en los territorios del Imperio español, toda ciudad debía contar con un Cabildo. Este se ubicaba siempre en la plaza principal, junto a otras instituciones importantes, como la iglesia mayor. En Buenos Aires contenía, además, la cárcel urbana.\r\n\r\nEl Cabildo era la única autoridad elegida por la sociedad local. Los virreyes, los gobernadores y otros funcionarios importantes eran nombrados desde España. En cambio, los miembros del Cabildo representaban a los habitantes de Buenos Aires. Solo los llamados "vecinos" (varones, blancos y con prestigio social) podían integrarlo. Los cabildantes se reunían periódicamente para discutir asuntos importantes para la comunidad.\r\n\r\nHacia fines del siglo XVIII, Buenos Aires pasó de ser una ciudad marginal del Imperio español a convertirse en la capital del Virreinato del Río de la Plata. Su importancia creciente implicó que el Cabildo asumiera cada vez más facultades.\r\n\r\nEl Cabildo tuvo un papel político fundamental en los años posteriores a la revolución de 1810. En 1821, fue disuelto. Desde entonces, la organización de la ciudad quedó en manos de la Legislatura de Buenos Aires, creada un año antes.	\N	\N	\N	50598	2026-01-11 16:12:14.724287-03	2026-01-11 16:12:14.724291-03	101	\N	\N	1	es	\N	es	\N	t	\N	\N	t	\N	1	0	f
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
734	411	en	Secret Museum	From reservation to the room	Among their many missions, museums preserve a nation's memory and, at the same time, as dynamic institutions, stimulate the construction of new meanings for the pieces they house. The ways in which the works in a collection are presented to the public shape the relationship a community establishes with history, a bond that is strengthened when an institution is open to the aesthetic and social transformations that each era brings.	\N	\N	\N	2025-10-25 14:21:59.386284-03	2025-12-08 12:19:05.098143-03	100	\N	\N	\N	\N	f	284200020	374998587	-1809980365	0	\N	0	\N	0	\N	0	t	f
1484	248	en	Grandes obras del Impresionismo	\N	\N	\N	\N	\N	2025-12-14 14:31:54.609955-03	2025-12-14 14:31:54.609965-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
1485	248	pt-BR	Grandes obras del Impresionismo	\N	\N	\N	\N	\N	2025-12-14 14:31:57.183953-03	2025-12-14 14:31:57.183965-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
896	894	en	Wilfredo Lan, when I dont sleep, I dream	\N	\N	\N	\N	\N	2025-11-29 07:51:58.979321-03	2025-11-29 07:51:58.979322-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	f	f
33826	33825	es	El Cabildo de Buenos Aires	\N	\N	\N	\N	\N	2026-01-11 16:12:14.731222-03	2026-01-11 16:12:14.731224-03	101	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
33827	33825	en	The Buenos Aires City Council	\N	The history of the Cabildo began in 1580. That year, Juan de Garay founded the city of La Trinidad, which was later renamed after its port: Santa María del Buen Ayre.\n\nCabildos originated in Spain as a form of administration for cities and their surrounding areas. They were later transferred to the American colonies. According to the Laws of the Indies, which regulated life in the territories of the Spanish Empire, every city was required to have a Cabildo. This was always located in the main square, next to other important institutions, such as the main church. In Buenos Aires, it also housed the city jail.\n\nThe Cabildo was the only authority elected by the local community. Viceroys, governors, and other important officials were appointed from Spain. In contrast, the members of the Cabildo represented the inhabitants of Buenos Aires. Only those called "vecinos" (male, white, and of social standing) could be members. The Cabildo members met regularly to discuss matters of importance to the community.\n\nTowards the end of the 18th century, Buenos Aires went from being a marginal city of the Spanish Empire to becoming the capital of the Viceroyalty of the Río de la Plata. Its growing importance meant that the Cabildo (city council) assumed increasingly more powers.\n\nThe Cabildo played a fundamental political role in the years following the 1810 revolution. In 1821, it was dissolved. From then on, the organization of the city was in the hands of the Buenos Aires Legislature, created a year earlier.	\N	\N	34165	2026-01-11 16:12:14.732128-03	2026-01-11 16:21:29.085514-03	101	\N	\N	1	\N	t	1535855056	0	1942838104	0	\N	0	\N	0	\N	0	t	f
33828	33825	pt-BR	Câmara Municipal de Buenos Aires	\N	A história do Cabildo começou em 1580. Naquele ano, Juan de Garay fundou a cidade de La Trinidad, que mais tarde foi renomeada em homenagem ao seu porto: Santa María del Buen Ayre.\n\nOs Cabildos surgiram na Espanha como uma forma de administração para as cidades e seus arredores. Posteriormente, foram transferidos para as colônias americanas. De acordo com as Leis das Índias, que regulamentavam a vida nos territórios do Império Espanhol, toda cidade era obrigada a ter um Cabildo. Este sempre se localizava na praça principal, próximo a outras instituições importantes, como a igreja principal. Em Buenos Aires, também abrigava a cadeia da cidade.\n\nO Cabildo era a única autoridade eleita pela comunidade local. Vice-reis, governadores e outros funcionários importantes eram nomeados pela Espanha. Em contrapartida, os membros do Cabildo representavam os habitantes de Buenos Aires. Somente os chamados "vecinos" (homens brancos e de posição social elevada) podiam ser membros. Os membros do Cabildo se reuniam regularmente para discutir assuntos importantes para a comunidade.\n\nNo final do século XVIII, Buenos Aires deixou de ser uma cidade marginal do Império Espanhol para se tornar a capital do Vice-Reino do Rio da Prata. Sua crescente importância fez com que o Cabildo (conselho municipal) assumisse cada vez mais poderes.\n\nO Cabildo desempenhou um papel político fundamental nos anos que se seguiram à Revolução de 1810. Em 1821, foi dissolvido. A partir de então, a organização da cidade ficou a cargo da Assembleia Legislativa de Buenos Aires, criada um ano antes.	\N	\N	34208	2026-01-11 16:12:14.732665-03	2026-01-11 16:22:58.719272-03	101	\N	\N	1	\N	t	1535855056	0	1942838104	0	\N	0	\N	0	\N	0	t	f
50262	50261	es	Museo Secreto	\N	\N	\N	\N	\N	2026-02-07 17:06:59.392613-03	2026-02-07 17:06:59.392614-03	100	\N	\N	3	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50263	50261	en	Museo Secreto	\N	\N	\N	\N	\N	2026-02-07 17:06:59.393309-03	2026-02-07 17:06:59.39331-03	100	\N	\N	3	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50264	50261	pt-BR	Museo Secreto	\N	\N	\N	\N	\N	2026-02-07 17:06:59.393794-03	2026-02-07 17:06:59.393794-03	100	\N	\N	3	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50265	50261	it	Museo Secreto	\N	\N	\N	\N	\N	2026-02-07 17:06:59.394228-03	2026-02-07 17:06:59.394229-03	100	\N	\N	3	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50266	50261	fr	Museo Secreto	\N	\N	\N	\N	\N	2026-02-07 17:06:59.3947-03	2026-02-07 17:06:59.3947-03	100	\N	\N	3	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50267	50261	ger	Museo Secreto	\N	\N	\N	\N	\N	2026-02-07 17:06:59.395158-03	2026-02-07 17:06:59.395158-03	100	\N	\N	3	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50307	50306	es	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:05:59.21278-03	2026-02-08 19:05:59.212782-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50308	50306	en	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:05:59.213553-03	2026-02-08 19:05:59.213554-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50309	50306	pt-BR	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:05:59.21443-03	2026-02-08 19:05:59.214431-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50310	50306	it	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:05:59.214792-03	2026-02-08 19:05:59.214792-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50311	50306	fr	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:05:59.21515-03	2026-02-08 19:05:59.215151-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50312	50306	ger	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:05:59.215461-03	2026-02-08 19:05:59.215461-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50314	50313	es	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:06:07.33607-03	2026-02-08 19:06:07.336075-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50315	50313	en	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:06:07.33689-03	2026-02-08 19:06:07.336891-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50316	50313	pt-BR	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:06:07.337511-03	2026-02-08 19:06:07.337512-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50317	50313	it	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:06:07.338133-03	2026-02-08 19:06:07.338134-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50318	50313	fr	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:06:07.338652-03	2026-02-08 19:06:07.338652-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50319	50313	ger	Arquitectura	\N	\N	\N	\N	\N	2026-02-08 19:06:07.339168-03	2026-02-08 19:06:07.339169-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
735	411	pt-BR	Museu Secreto	Da reserva ao quarto	Entre suas muitas missões, os museus preservam a memória de uma nação e, ao mesmo tempo, como instituições dinâmicas, estimulam a construção de novos significados para as peças que abrigam. As maneiras pelas quais as obras de uma coleção são apresentadas ao público moldam a relação que uma comunidade estabelece com a história, um vínculo que se fortalece quando uma instituição se mostra aberta às transformações estéticas e sociais que cada época traz.	\N	\N	1104	2025-10-25 14:22:54.501191-03	2025-12-08 12:11:15.074398-03	100	\N	\N	\N	\N	f	284200020	374998587	-1809980365	0	\N	0	\N	0	\N	0	t	f
50444	50443	es	Legados del hacer	\N	\N	\N	\N	\N	2026-02-11 14:53:25.388715-03	2026-02-11 14:53:25.388717-03	100	\N	\N	3	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50447	50443	it	Legados del hacer	\N	\N	\N	\N	\N	2026-02-11 14:53:25.391359-03	2026-02-11 14:53:25.39136-03	100	\N	\N	3	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50449	50443	ger	Legados del hacer	\N	\N	\N	\N	\N	2026-02-11 14:53:25.39245-03	2026-02-11 14:53:25.392451-03	100	\N	\N	3	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50448	50443	fr	Héritages de l'action	Hommage à Teresa Anchorena	L'exposition « Héritages de l'artisanat » présente des récipients, des paniers, des textiles, des couteaux et des étriers issus de la collection du Fonds national des arts.\n\nCes œuvres mettent en lumière l'artisanat comme une pratique vivante et collective, partie intégrante de l'identité culturelle argentine.\n\nL'exposition rend également hommage à Teresa Anchorena, directrice du patrimoine et de l'artisanat au Fonds national des arts, décédée cette année et figure emblématique de cette initiative. Son travail et son engagement ont été essentiels à la promotion du patrimoine de l'organisation et à l'établissement d'une relation respectueuse et active avec les territoires, les artisanats et les communautés qui, depuis des temps immémoriaux, ont bâti cet héritage désormais accessible au public.	\N	\N	50716	2026-02-11 14:53:25.391981-03	2026-02-11 17:23:50.337939-03	100	\N	\N	3	\N	t	601640535	1133852263	-1796686587	0	\N	0	\N	0	\N	0	t	f
50446	50443	pt-BR	Legados do fazer	Homenagem a Teresa Anchorena	A exposição "Legados do Artesanato" inclui peças como vasos, cestos, têxteis, facas e estribos da coleção do Fundo Nacional de Artes.\n\nAs obras destacam o artesanato como uma prática viva e coletiva, parte integrante da identidade cultural argentina.\n\nA exposição também presta homenagem a Teresa Anchorena, Diretora de Patrimônio e Artesanato do Fundo Nacional de Artes, falecida este ano e uma força motriz por trás desta iniciativa. Seu trabalho e dedicação foram fundamentais para a promoção do patrimônio da organização e para a construção de uma relação respeitosa e ativa com os territórios, os ofícios e as comunidades que, desde tempos ancestrais, construíram esse legado que agora é compartilhado com o público.	\N	\N	\N	2026-02-11 14:53:25.390779-03	2026-02-11 17:13:47.74699-03	100	\N	\N	3	\N	t	601640535	1133852263	-1796686587	0	\N	0	\N	0	\N	0	t	f
50445	50443	en	Legacies of doing	Tribute to Teresa Anchorena	Legacies of Crafting includes vessels, baskets, textiles, knives, and stirrups from the National Arts Fund's collection.\r\n\r\nThe works highlight artisanal crafting as a living and collective practice, integral to Argentine cultural identity.\r\n\r\nThe exhibition also pays tribute to Teresa Anchorena, Director of Heritage and Crafts at the National Arts Fund, who passed away this year and was a driving force behind this initiative. Her work and commitment were key to promoting the organization's heritage and building a respectful and active relationship with the territories, crafts, and communities that, since ancestral times, have constructed this legacy that is now shared with the public.	\N	\N	50715	2026-02-11 14:53:25.390169-03	2026-02-11 16:42:54.802818-03	100	\N	\N	3	\N	t	601640535	1133852263	-1796686587	0	\N	0	\N	0	\N	0	t	f
\.


--
-- Data for Name: artexhibitionitem; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artexhibitionitem (id, name, namekey, title, titlekey, artexhibition_id, artwork_id, site_id, floor_id, room_id, artexhibitionorder, mapurl, website, readcode, qcode, info, infokey, created, lastmodified, lastmodifieduser, state, floorstr, roomstr, language, draft, masterlanguage, translation, photo, audio, video, subtitle, usethumbnail, intro, spec, opens, audioautogenerate, speechaudio, audio_id, artwork_audio_id) FROM stdin;
408	La Emperatriz Theodora	\N	La Emperatriz Theodora	\N	362	402	137	138	140	2	\N	\N	1026	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-10-01 13:37:56.097839-03	100	3	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
634	El recurso del método	el-recurso-del-m-todo	\N	\N	\N	452	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-29 15:58:54.034924-03	2025-09-29 15:58:54.034941-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
633	Reposo	reposo	\N	\N	\N	280	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-29 15:21:43.233762-03	2025-09-29 15:21:43.233789-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
487	Procesión sorprendida por la lluvia	\N	Procesión sorprendida por la lluvia	\N	362	484	137	139	\N	4	\N	\N	1035	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-10-01 13:38:17.808303-03	100	3	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
409	Retrato de Juan Manuel de Rosas	\N	Retrato de Juan Manuel de Rosas	\N	362	406	137	138	140	2	\N	\N	1027	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	3	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
455	Sendas perdidas	\N	Sendas perdidas	\N	454	448	137	139	\N	1	\N	\N	1029	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
247	La Nymphe surprise (La ninfa sorprendida)	la-nymphe-surprise-la-ninfa-sorprendida	La Nymphe surprise (La ninfa sorprendida)	\N	233	225	137	138	154	14	\N	\N	1015	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
50564	Costurero	\N	\N	\N	50435	50556	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2026-02-12 16:18:36.576852-03	2026-02-12 16:27:34.132661-03	100	3	\N	\N	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
243	La Toilette apres le bain (El arreglo después del baño)	la-toilette-apres-le-bain-el-arreglo-despu-s-del-ba-o	La Toilette apres le bain (El arreglo después del baño)	\N	233	223	137	138	154	10	\N	\N	1011	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
834	La berge de La Seine (Orillas del Sena)	\N	\N	\N	\N	219	\N	\N	\N	6	\N	\N	\N	\N	\N	\N	2025-11-23 19:52:35.866098-03	2025-11-23 19:52:35.866108-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	f	\N	\N	\N
615	O Impossivel	o-impossivel	\N	\N	\N	592	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 10:59:00.915806-03	2025-09-24 10:59:00.915878-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
244	Le Moulin de la Galette	le-moulin-de-la-galette	Le Moulin de la Galette	\N	233	225	137	138	154	11	\N	\N	1012	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
239	Vahine no te miti (Femme a la mer) (Mujer del mar)	vahine-no-te-miti-femme-a-la-mer-mujer-del-mar	Vahine no te miti (Femme a la mer) (Mujer del mar)	\N	233	215	137	138	154	6	\N	\N	1007	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
40769	Vara de mando de Manuel Mansilla	\N	\N	\N	27577	40219	\N	\N	\N	2	\N	\N	\N	\N	\N	\N	2026-01-13 15:22:34.907999-03	2026-01-13 15:22:34.908009-03	100	1	Planta Baja	1	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
285	La vuelta del malón	la-vuelta-del-mal-n	La vuelta del malón	la-vuelta-del-mal-n	284	270	137	138	151	1	\N	\N	1016	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
290	Reposo	reposo	Reposo	reposo	284	280	137	138	151	6	\N	\N	1021	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
287	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	284	274	137	138	151	3	\N	\N	1018	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
782	Sillón cinta	\N	\N	\N	774	768	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-10-29 13:41:23.2673-03	2025-10-29 13:41:23.267322-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	f	\N	\N	\N	f	\N	\N	\N
16643	Je suis (Yo soy)	\N	\N	\N	864	16103	\N	\N	\N	1	\N	\N	\N	\N	\N	\N	2026-01-04 17:59:28.813742-03	2026-01-04 17:59:28.81375-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
890	La jungla	\N	\N	\N	864	884	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-11-29 07:51:43.660506-03	2025-11-29 07:51:43.660512-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	f	\N	\N	\N
16647	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	864	12377	\N	\N	\N	2	\N	\N	\N	\N	\N	\N	2026-01-04 17:59:31.486215-03	2026-01-04 17:59:31.48622-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
456	Apocalipsis	\N	Apocalipsis	\N	454	450	137	139	\N	2	\N	\N	1030	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
35278	Traje de calle del alférez real	\N	\N	\N	27577	34719	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2026-01-12 10:41:41.312036-03	2026-01-12 10:41:41.312045-03	100	1	\N	\N	es	\N	es	\N	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
457	El recurso del método	\N	El recurso del método	\N	454	452	137	139	\N	2	\N	\N	1031	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
236	Prairies du Valhermeil près Pontoise	prairies-du-valhermeil-pr-s-pontoise	Prairies du Valhermeil près Pontoise	\N	233	209	137	138	\N	3	\N	\N	1004	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
238	En observation - M.Fabre, Officier de reserve	en-observation-m-fabre-officier-de-reserve	En observation - M.Fabre, Officier de reserve	\N	233	213	137	138	152	5	\N	\N	1006	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
237	Femme aux champs (Campesina)	femme-aux-champs-campesina	Femme aux champs (Campesina)	\N	233	211	137	138	152	4	\N	\N	1005	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
201	Morro da favela II (Pueblito)	morro-da-favela-ii-pueblito	Morro da favela II (Pueblito)	\N	199	195	137	138	140	2	\N	\N	1001	\N	Época de temática popular, la década del 40 también sería para Tarsila, la gran pintora modernista de los años 20, tiempo de volver definitivamente de sus tonos pau Brasil, es decir, retornar a los colores llamados caipiras (azul y rosa) de la decoración popular de los modestos caseríos de las regiones del interior del Brasil. Este colorido, aunque más intenso, estuvo presente en sus telas de los años 20, su período de máxima creatividad y producción, etapa de una gran síntesis de sus elementos compositivos. Después de 1930, cuando Tarsila se orientó, por razones culturales y políticas, hacia la temática social, su paleta se alteró igualmente.\\nPor esa época la artista regresó a San Pablo después de alternar varios años entre esa ciudad y Río de Janeiro, y pasó gran parte de su tiempo en su hacienda de Indaiatuba, en el interior paulista. \\nConociendo la inexistencia de un mercado de arte local, realizó encargos de pinturas, ilustraciones y colaboró con crónicas regulares sobre la vida cultural y memorias en Diário de São Paulo.\\nEn Pueblito, de 1945, encontramos una segunda versión de la famosa pintura Morro da favela (1924, colección João Estefano, San Pablo), tela tan admirada por el poeta Blaise Cendrars que le recomendó, en su momento, que preparase su primera exposición individual en París a partir de la serie que se iniciara con esta obra. La temática está inspirada en el viaje a Río con los modernistas de San Pablo acompañando a Cendrars en su primera visita al Brasil.\\nAlgunas variantes, sin embargo, surgen en esta tela, ahora en la colección del MNBA: sus dimensiones son menores que las de Morro da favela, la vegetación tiene modificaciones, dejando de ser visualmente presentada en formas sintéticas, en esta tela de 1945 aparecen como matas de plantas más desmenuzadas, más diversificadas en su ubicación, especialmente en el primer plano. Por otra parte, las pinceladas son bien evidentes, casi “impresionistas” en su aplicación sobre la tela, y el ave extraña de 1924 es reemplazada por la confrontación de dos pavos. Finalmente, el color es más suave en comparación con la pintura de veinte años antes.\\nLa obra del MNBA estuvo expuesta en Buenos Aires, La Plata y Montevideo en la exposición Veinte artistas brasileños presentada en 1945, con la organización del escritor Marques Rebelo y la intermediación efectiva de Emilio Pettoruti.\\nEn esta muestra Tarsila estuvo representada por otra pintura, Pueblito II (1944), y por un dibujo, estudio de 16 x 22 cm de la pintura de la colección del MNBA, hoy aparentemente perdido. La exposición dio lugar a dos pequeños libros, actualmente antológicos, uno de autoría de Cipriano Vitureira, de Montevideo, y el segundo de Jorge Romero Brest, La pintura brasileña contemporánea, publicado el mismo año de la exposición por la editorial Poseidón.	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
242	La Coiffure (El peinado)	la-coiffure-el-peinado	La Coiffure (El peinado)	\N	233	221	137	138	154	9	\N	\N	1010	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
240	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	portrait-de-suzanne-valadon-madame-suzanne-valadon-artiste-peintre-retrato-de-suzanne-valadon-pintora	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	\N	233	217	137	138	154	7	\N	\N	1008	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
410	En la costa de Valencia	\N	En la costa de Valencia	\N	362	404	137	138	140	1	\N	\N	1028	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-10-01 13:37:44.824832-03	100	3	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
616	Troncos	troncos	\N	\N	\N	589	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 11:30:16.706215-03	2025-09-24 11:30:16.706229-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
246	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	danseuse-et-admirateur-derri-re-la-sc-ne-bailarina-y-admirador-tras-la-escena	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	\N	233	225	137	138	154	13	\N	\N	1014	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
635	En Normandie	en-normandie	\N	\N	\N	272	\N	\N	\N	6	\N	\N	\N	\N	\N	\N	2025-10-01 10:57:21.298364-03	2025-10-01 10:57:21.298382-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
234	Effet de neige à Louveciennes	effet-de-neige-louveciennes	Effet de neige à Louveciennes	\N	233	205	137	138	151	1	\N	\N	1002	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
241	La berge de La Seine (Orillas del Sena)	la-berge-de-la-seine-orillas-del-sena	La berge de La Seine (Orillas del Sena)	\N	233	219	137	138	154	8	\N	\N	1009	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
622	Diván	div-n	\N	\N	597	619	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 11:39:24.765554-03	2025-09-24 11:39:24.765567-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
613	El mal	el-mal	\N	\N	597	603	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-23 18:53:44.207449-03	2025-09-23 18:53:44.420125-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
617	The Disasters of Mysticism	the-disasters-of-mysticism	\N	\N	\N	582	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 11:30:47.572528-03	2025-09-24 11:30:47.572541-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
614	Abaporu	abaporu	\N	\N	600	575	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-23 18:57:57.544907-03	2025-09-23 18:57:57.544938-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
626	Troncos	troncos	\N	\N	600	589	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-25 16:51:35.086883-03	2025-09-25 16:51:35.086893-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
291	Abel	abel	Abel	abel	284	282	137	138	151	7	\N	\N	1022	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
618	Quiosco de Canaletas	quiosco-de-canaletas	\N	\N	\N	311	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 11:30:54.292583-03	2025-09-24 11:30:54.292595-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
627	La vuelta del malón	la-vuelta-del-mal-n	\N	\N	\N	270	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-27 21:09:16.258142-03	2025-09-27 21:09:16.25816-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
289	El despertar de la criada	el-despertar-de-la-criada	El despertar de la criada	el-despertar-de-la-criada	284	278	137	138	151	5	\N	\N	1020	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-09-30 16:47:26.38454-03	100	3	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
286	En Normandie	en-normandie	En Normandie	en-normandie	284	272	137	138	151	2	\N	\N	1017	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
288	La vuelta al hogar	la-vuelta-al-hogar	La vuelta al hogar	la-vuelta-al-hogar	284	276	137	138	151	4	\N	\N	1019	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
623	O Impossivel	o-impossivel	\N	\N	\N	592	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-24 11:43:51.626523-03	2025-09-24 11:43:51.626534-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
486	Utopia del Sur	\N	Utopia del Sur	\N	362	482	137	139	\N	1	\N	\N	1034	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-11-29 14:35:57.928954-03	100	5	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
625	El circo más lindo del mundo	el-circo-m-s-lindo-del-mundo	\N	\N	600	585	\N	\N	\N	0	\N	\N	\N	\N	\N	\N	2025-09-25 16:51:24.66241-03	2025-09-25 16:51:24.662421-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
235	Le Pont d`Argenteuil (El Puente de Argenteuil)	le-pont-d-argenteuil-el-puente-de-argenteuil	Le Pont d`Argenteuil (El Puente de Argenteuil)	\N	233	207	137	138	151	2	\N	\N	1003	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
245	Portrait de Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	portrait-de-ernest-hosched-et-sa-fille-marthe-retrato-del-se-or-hosched-y-su-hija	Portrait de Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	\N	233	225	137	138	154	12	\N	\N	1013	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
632	La vuelta al hogar	la-vuelta-al-hogar	\N	\N	362	276	\N	\N	\N	3	\N	\N	\N	\N	\N	\N	2025-09-29 15:12:06.087863-03	2025-10-01 13:38:08.163137-03	100	3	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
200	Le bain de Vénus (El baño de Venus)	le-bain-de-v-nus-el-ba-o-de-venus	Le bain de Vénus (El baño de Venus)	\N	199	197	137	138	140	1	\N	\N	1000	\N	El título original de esta pintura puede haber sido puesto por Redon, ya que al parecer la obra fue comprada al artista sin intermediarios. Los toques de azul y verde en la zona inferior derecha de la tela pueden considerarse una representación del agua, y el tocado de la figura puede evocar un personaje mitológico. Estos aspectos, sin embargo, son los únicos que justificarían el título de El baño de Venus. La figura no está desnuda, aparece (apenas) vestida y carece de otro atributo inherente a la diosa del amor. Su movimiento no corresponde a una escena de baño y el personaje demuestra una vacilación indigna de alguien nacido del mar. Con los dos brazos extendidos hacia delante y la cabeza hacia atrás, parece moverse de derecha a izquierda como si estuviese huyendo de algo –en una posición relativamente similar a la del dibujo Female Nude (Bridgestone Museum, Tokio)–. El origen de su miedo o resistencia parece ser la forma circular sobre la derecha (de la mitad del tamaño de la figura) que semeja más a un insecto o a uno de los monstruos submarinos pintados por Redon que a las flores y las hojas reconocibles sobre la izquierda. Esta interpretación se sustenta en el uso del negro para delinear y rellenar parte de esta forma, color asociado por Redon al mundo fantástico de sus primeras carbonillas y litografías.	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	es	\N	es	0	\N	\N	\N	\N	t	\N	\N	\N	t	\N	\N	\N
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
50565	50564	es	Costurero	\N	\N	\N	\N	\N	\N	2026-02-12 16:18:36.580667-03	2026-02-12 16:18:36.580671-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50566	50564	en	Costurero	\N	\N	\N	\N	\N	\N	2026-02-12 16:18:36.582055-03	2026-02-12 16:18:36.582057-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50567	50564	pt-BR	Costurero	\N	\N	\N	\N	\N	\N	2026-02-12 16:18:36.5827-03	2026-02-12 16:18:36.582701-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50568	50564	it	Costurero	\N	\N	\N	\N	\N	\N	2026-02-12 16:18:36.583235-03	2026-02-12 16:18:36.583236-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50569	50564	fr	Costurero	\N	\N	\N	\N	\N	\N	2026-02-12 16:18:36.583851-03	2026-02-12 16:18:36.583852-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50570	50564	ger	Costurero	\N	\N	\N	\N	\N	\N	2026-02-12 16:18:36.584357-03	2026-02-12 16:18:36.584358-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
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
42430	42429	es	new	\N	\N	\N	\N	\N	\N	2026-01-21 05:40:16.03854-03	2026-01-21 05:40:16.038552-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
42431	42429	en	new	\N	\N	\N	\N	\N	\N	2026-01-21 05:40:16.040999-03	2026-01-21 05:40:16.041002-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
42432	42429	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-01-21 05:40:16.042108-03	2026-01-21 05:40:16.042111-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50255	50254	es	new	\N	\N	\N	\N	\N	\N	2026-02-07 17:05:47.04269-03	2026-02-07 17:05:47.042695-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50256	50254	en	new	\N	\N	\N	\N	\N	\N	2026-02-07 17:05:47.044578-03	2026-02-07 17:05:47.044587-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50257	50254	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-02-07 17:05:47.045525-03	2026-02-07 17:05:47.045527-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50258	50254	it	new	\N	\N	\N	\N	\N	\N	2026-02-07 17:05:47.046449-03	2026-02-07 17:05:47.046453-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50259	50254	fr	new	\N	\N	\N	\N	\N	\N	2026-02-07 17:05:47.047412-03	2026-02-07 17:05:47.047415-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50260	50254	ger	new	\N	\N	\N	\N	\N	\N	2026-02-07 17:05:47.048218-03	2026-02-07 17:05:47.048221-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50341	50340	es	new	\N	\N	\N	\N	\N	\N	2026-02-10 16:00:33.953022-03	2026-02-10 16:00:33.953024-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50342	50340	en	new	\N	\N	\N	\N	\N	\N	2026-02-10 16:00:33.954797-03	2026-02-10 16:00:33.954797-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50343	50340	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-02-10 16:00:33.95536-03	2026-02-10 16:00:33.95536-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50344	50340	it	new	\N	\N	\N	\N	\N	\N	2026-02-10 16:00:33.955935-03	2026-02-10 16:00:33.955936-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50345	50340	fr	new	\N	\N	\N	\N	\N	\N	2026-02-10 16:00:33.956691-03	2026-02-10 16:00:33.956691-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50346	50340	ger	new	\N	\N	\N	\N	\N	\N	2026-02-10 16:00:33.957135-03	2026-02-10 16:00:33.957135-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50436	50435	es	new	\N	\N	\N	\N	\N	\N	2026-02-11 14:50:46.664259-03	2026-02-11 14:50:46.664264-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50437	50435	en	new	\N	\N	\N	\N	\N	\N	2026-02-11 14:50:46.667947-03	2026-02-11 14:50:46.66795-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50438	50435	pt-BR	new	\N	\N	\N	\N	\N	\N	2026-02-11 14:50:46.668726-03	2026-02-11 14:50:46.668727-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50439	50435	it	new	\N	\N	\N	\N	\N	\N	2026-02-11 14:50:46.669484-03	2026-02-11 14:50:46.669486-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50440	50435	fr	new	\N	\N	\N	\N	\N	\N	2026-02-11 14:50:46.670231-03	2026-02-11 14:50:46.670233-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
50441	50435	ger	new	\N	\N	\N	\N	\N	\N	2026-02-11 14:50:46.670904-03	2026-02-11 14:50:46.670905-03	100	\N	\N	1	\N	t	0	0	0	0	\N	0	\N	0	0	t	f
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
5040	es	es	190	\N	2025-12-23 18:26:11.820315-03	2025-12-23 18:26:11.820315-03	100	\N	\N	3	\N	\N	\N	\N	\N	\N	\N	\N	t	\N	0
\.


--
-- Data for Name: artist_sites; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artist_sites (artist_id, site_id) FROM stdin;
5052	177
5052	137
5052	860
5036	137
5037	137
5038	137
5039	137
5033	177
5042	137
5033	137
5033	860
5046	137
5047	137
5048	137
5049	137
5050	137
5053	137
5056	137
5059	137
5060	137
5061	137
5062	137
5063	137
5065	137
5068	137
5045	177
5045	137
5036	860
5037	860
5038	860
5039	860
5045	860
5042	860
5046	860
5047	860
5048	860
5049	860
5050	860
5057	177
5057	137
5053	860
5056	860
5057	860
5059	860
5060	860
5061	860
5062	860
5063	860
5065	860
5068	860
5036	177
5037	177
5038	177
5039	177
5042	177
5046	177
5047	177
5048	177
5049	177
5050	177
5058	177
5058	137
5053	177
5056	177
5058	860
5059	177
5060	177
5061	177
5062	177
5063	177
5055	177
5065	177
5055	137
5055	860
5068	177
5031	177
5031	137
5031	860
5034	177
5034	137
5034	860
5066	177
5066	137
5066	860
5064	860
5067	137
5067	860
5067	42268
5043	177
5041	177
5041	137
5041	860
5043	137
5043	860
5040	177
5040	137
5040	860
5035	177
5035	137
5035	860
5051	177
5051	137
5051	860
5032	177
5032	137
5032	860
\.


--
-- Data for Name: artistrecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artistrecord (id, language, artist_id, name, shortname, subtitle, spec, info, intro, photo, video, audio, usethumbnail, created, lastmodified, lastmodifieduser, draft) FROM stdin;
\.


--
-- Data for Name: artwork; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artwork (id, name, namekey, title, titlekey, shortname, artworktype_id, subtitle, subtitlekey, spec, speckey, info, infokey, photo, video, audio, year, person_owner_id, institution_owner_id, created, lastmodified, lastmodifieduser, intro, introkey, site_owner_id, usethumbnail, url, state, qrcode, language, draft, translation, masterlanguage, opens, audioautogenerate, speechaudio, source, epoch, objecttype, audio_id) FROM stdin;
225	Le Moulin de la Galette	le-moulin-de-la-galette	Le Moulin de la Galette	\N	\N	126		\N	61 x 50 cm.	\N	Fueron condiciones muy particulares, podría decirse de transición, las que llevaron a Vincent van Gogh a pintar este Moulin de la Galette, obra que se inscribe en una importante serie de vistas de París. El artista holandés llegó a la capital francesa en marzo de 1886. Allí se encontró –sin haberle siquiera avisado con anterioridad– con su hermano Theo, que llevaba siete años instalado en la ciudad y dirigía por cuenta de Boussod et Valadon una pequeña galería en el boulevard Montmartre.\r\n\r\nCuando Van Gogh pinta su Moulin, está como deslumbrado por el contexto artístico circundante, de una infrecuente densidad: el manifiesto simbolista de Moréas, la última exposición impresionista (donde se presentó La Grande Jatte de Seurat), la publicación de las Illuminations de Rimbaud y de L’oeuvre de Zola se agolpan en el escenario cultural. Descubre además los encantos de la ciudad, las galerías y las discusiones animadas en los cafés. También las obras del Louvre, museo que visita con frecuencia. Para coronar esas experiencias, suma al frenesí un toque académico incorporándose, para afirmar sus cualidades técnicas, como alumno del taller del muy clásico Cormon. Allí alterna con Toulouse-Lautrec y Anquetin.\r\n\r\nEs sabido que Van Gogh llevó adelante su carrera con una determinación tan humilde como profunda, y sin duda esos meses parisinos movilizaron en él un poderoso deseo de crear. La Butte Montmartre formaba parte de su vida diaria, ya que se alojaba en la casa de Theo, que vivía en ese barrio. Los lazos que unían a los dos hermanos eran por cierto muy fuertes, pero en el otoño de 1886 la promiscuidad del nº 54 de la rue Lepic –desde donde el panorama de la ciudad era magnífico– comenzó a volverse pesada. Esto llevó a Vincent a reemplazar la naturaleza muerta, que podía realizar en el departamento, por el paisaje. Lo cual lo impulsó, en un primer momento, a representar los alrededores inmediatos, y por ende Montmartre (1).Por “Moulin de la Galette” se entiende el café-concert que se extendía en realidad entre dos molinos de Montmartre: el Blute-Fin y el Radet. \r\n\r\nEn la muy célebre composición de Renoir titulada Le bal du Moulin de la Galette (1876, Musée d’Orsay, París), asistimos a las fiestas y los bailes que acompasaban la vida del lugar. Pero no fue el molino de viento como tal lo que había interesado entonces al pintor impresionista. Van Gogh, en cambio, adoptó una actitud totalmente distinta. Se concentró en este caso en uno de los dos edificios afectados por el café-concert: el Blute-Fin, antigua construcción en madera erigida en 1622 y que servía sobre todo para moler trigo.El punto de vista adoptado para el molino –la parte posterior del edificio– no tenía nada de original y lo elegían por la misma época montones de pintores (que saturaban el barrio de Montmartre). \r\n\r\nSe sabe sin embargo que Van Gogh ensayó en torno de su motivo varias otras vistas para circunscribirlo mejor. Es asombrosa aquí la claridad, la frescura, incluso, del cuadro, dominado por pinceladas vivas de azul que van virando al blanco, en tonalidades muy homogéneas. La perspectiva desde abajo adoptada por el pintor genera una línea del horizonte baja que deja estallar ese gran cielo luminoso. Van Gogh, que en abril de 1885 había trabajado con fervor en sus oscuros Mangeurs de pommes de terre, parece de pronto exultar al contacto con el ambiente parisino. Es verdad que ya había comenzado a aclarar su paleta en Anvers bajo la influencia de los cuadros de Rubens, pero Montmartre le inspira sobre todo, en el tratamiento de la atmósfera, una manera mucho más liviana. Se comprende, pues, lo que escribió a uno de sus amigos artistas (H. M. Levens) en 1887: “No hay otra cosa que París, y por difícil que la vida pueda ser aquí, aunque fuera peor y más dura, el aire francés limpia el cerebro y hace bien, muchísimo bien”. \r\n\r\nLa bandera tricolor flameando al viento, representada en unas pocas pinceladas nerviosas, traduce perfectamente, por otro lado, esa plenitud triunfal en las tierras de Francia.Observemos, asimismo, que Van Gogh eligió una vista que no permite adivinar en nada las diversiones del Moulin de la Galette. \r\n\r\nHay en él un interés pintoresco por el lugar, pero también una voluntad de mostrar un espacio de trabajo en el límite entre la ciudad y el campo, en lo que era todavía, en esa época, un barrio periférico de París, poblado de gente modesta. La pareja de personajes abajo a la derecha, además de indicar la escala, está vestida de manera humilde y casi campesina. Van Gogh desliza en su obra una dimensión social que lo conmueve particularmente.Podríamos afirmar, entonces, que este óleo es un excelente testimonio de la euforia del pintor holandés que recorre París y a la vez un ejemplo típico de tableau-laboratoire. Van Gogh experimenta en él serenamente sus conceptos plásticos, que encontrarían su realización absoluta unos meses más tarde, en el sur de Francia, en Provenza, región que le “limpia[ría] el cerebro” (como escribe él) con la intensidad combinada del genio y la locura.	\N	319	\N	\N	1886	\N	135	2025-05-19 12:29:43.192411-03	2025-10-06 15:44:04.113106-03	100	Fueron condiciones muy particulares, podría decirse de transición, las que llevaron a Vincent van Gogh a pintar este Moulin de la Galette, obra que se inscribe en una importante serie de vistas de París. El artista holandés llegó a la capital francesa en marzo de 1886. Allí se encontró –sin haberle siquiera avisado con anterioridad– con su hermano Theo, que llevaba siete años instalado en la ciudad y dirigía por cuenta de Boussod et Valadon una pequeña galería en el boulevard Montmartre.Cuando Van Gogh pinta su Moulin, está como deslu	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	49
22902	Zambezia Zambezia	\N	\N	\N	\N	\N	\N	\N	\N	\N	"Zambezia, Zambezia" es una famosa pintura surrealista de 1950 del artista cubano Wifredo Lam, que representa una figura totémica, mitad humana y mitad animal (inspirada en la "mujer-caballo" de la santería), utilizando su característico estilo que fusiona elementos africanos, chinos y europeos en un mundo poético y mítico.\r\n\r\nLa obra combina formas orgánicas, líneas angulares y un profundo sentido del movimiento, creando una atmósfera onírica y un lenguaje visual único.\r\nInspiración: Lam se inspiró en los ritos de la santería y los orishas, así como en sus propias experiencias culturales, para crear sus icónicas figuras híbridas y totémicas.	\N	23041	\N	\N	0	\N	\N	2026-01-07 08:27:21.438823-03	2026-01-07 08:27:21.438829-03	100	\N	\N	860	t	\N	1	\N	..	\N	\N	es	\N	t	\N	\N	\N	0	5
585	El circo más lindo del mundo	new	\N	\N	\N	\N	\N	\N	Técnica: Témpera y grafito sobre papel\r\n47,7 x 50,5 cm\r\nNro. de inventario: 2001.11\r\nDonación: Eduardo F. Costantini, Buenos Aires	\N	El deseo de Barradas de apresar el tiempo que fluye (representando su fluencia) ya estaba presente en los ágiles croquis de café realizados en Montevideo, tendientes a captar la impronta de una situación, de un gesto, de una contingencia espacial. Ese temperamento se tamizó en el contacto con la nueva plástica europea, donde aquella primera voluntad impresionista asimiló el estructuralismo de los cubistas y el dinamismo de los futuristas (especialmente de Gino Severini). Ésta es, sintéticamente, la línea de la cual procede su vibracionismo barcelonés, al que Barradas le imprimió un espíritu jovial, aun cuando pueden distinguirse en él (entre 1917 y 1919) varias modalidades. Tampoco puede descartarse que algunas de estas modalidades tengan relación con el estrecho vínculo que este pintor mantuvo con su colega uruguayo Joaquín Torres García, quien en ese momento se encontraba abocado a la captación del dinamismo urbano y con quien llevó a cabo una exposición conjunta en Galerías Dalmau a fines de 1917.\r\n\r\nEl circo más lindo del mundo pertenece a la serie que realizó fundamentalmente en 1918, caracterizada por una especie de terror vacuo respecto del plano pictórico, el cual es sometido a una saturación rítmica y cromática que apenas permite discernir la intención figurativa que anima por momentos algunos de sus fragmentos interiores. Barradas solía procesar estas pinturas a partir de breves bocetos de café que le servían como una suerte de ayuda memoria para la elaboración plástica de taller.\r\n\r\nAntonio de Ignacios, hermano de Barradas, recogió en su libro una frase del pintor que vendría a definir la idea plástica del vibracionismo (y también de la etapa clownista que le sigue) como un ejercicio dinámico de la memoria visual: “Quiero pintar lo que queda de una persona cuando se va”. Es como decir: “Pinto lo que queda en mi mente de una escena cuando ella desaparece”. Se trataría de una poética que, a fuerza de ser pintura “mental”, reminiscente, configura una representación de ausencias. Es significativo, en este sentido, el título que Barradas otorgó a una conferencia que pronunció en 1921, durante una velada ultraísta madrileña: “El anti-yo, estudio teórico sobre el clownismo y dibujos en la pizarra”. Su obsesión se centraba en la relación del artista con lo cotidiano visible, mediada a través de la obra. De acuerdo con esta fórmula, lo real no sería sino un reflejo del “yo”, y la obra plástica, una formalización de lo que él denominaba el “anti-yo”, una suerte de desprendimiento, de residuo resultante de la identificación inicial del artista con el objeto que lo mira.\r\n\r\nReconociendo en la pintura un acto de autorrepresentación, llegó a decir: “En este mismo momento veo una mesa, una botella y un caballo en un espejo. Es claro: no es la mesa, la botella, el caballo lo que en realidad VEO; me VEO YO”.1\r\n\r\nEn El circo más lindo del mundo vuelve a encontrarse la incorporación de letras, palabras, números –característica ya presente en sus acuarelas y dibujos de 1917–, así como la imagen infaltable de la rueda radial, un signo que adquirió estatus simbólico en sus pinturas durante este período.	\N	586	\N	\N	1918	\N	\N	2025-09-19 10:14:22.893352-03	2025-09-21 19:52:36.304081-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0	40
50701	Poncho	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	50708	\N	\N	1970	\N	\N	2026-02-16 14:14:28.890726-03	2026-02-16 14:19:17.150386-03	100	\N	\N	50393	t	\N	3	\N	es	\N	\N	es	\N	t	\N	Salta	\N	5	\N
406	Retrato de Juan Manuel de Rosas	retrato-de-juan-manuel-de-rosas	Retrato de Juan Manuel de Rosas	\N	\N	126		\N	Oleo sobre tela 100 x 79,8 cm.	\N	El Retrato de Juan Manuel de Rosas, pintado por el artista francés Raymond Monvoisin hacia 1842, ofrece una representación simbólica y estratégica del caudillo argentino.  A diferencia de otros cuadros oficiales donde Rosas aparece con uniforme militar, en esta obra se lo muestra con un poncho, resaltando su imagen de líder popular y no meramente militar.\r\n\r\nMonvoisin se había formado en la tradición académica europea, pero para 1842 estaba más cerca del romanticismo. \r\n\r\nRosas aparece de perfil, con expresión seria y firme, destacando su autoridad, y su mirada apunta a donde se origina el viento como  controlando la naturaleza. Mitre señaló “es el retrato más parecido que conozco de Rosas, hay mucho de emperador romano pero atenuado y corregido por un acento criollo”.\r\n\r\nLa tradición artística oficial argentina considera al cuadro un estudio preliminar de una obra posterior desaparecida o que no existió. Según Rodrigo Cañete en “Historia a contrapelo del arte argentino”,  esta idea está contaminada por el prejuicio de negarse a aceptar el verdadero carácter de la obra.  Para Cañete, el artista, acogido en Chile por los enemigos de Rosas, tuvo el talento de hacer un cuadro ambigüo para que ambos bandos -unitarios y federales- puedan utilizarlo como bandera. \r\n\r\nLos rasgos de Rosas son imperiales pero carecen de la musculatura heroica de los emperadores y están levemente feminizados, y el poncho y la ropa son parecidos a los retratos de sus enemigos como gaucho bárbaro, líder de lo que Sarmiento despectivamente llamaba la “montonera árabe”.  \r\n\r\nPor su parte, para los rosistas, representa un emperador-dios-benefactor-madre tierra, al estilo de la Virgen María de las imágenes sudamericanas que la fusionan con la Pachamama india, como la Virgen María del Cerro Rico de Potosí,  pero con referencias académicas sutiles a grandes maestros como Zurbarán y Velázquez.	\N	415	\N	\N	1842	\N	135	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	El Retrato de Juan Manuel de Rosas, pintado por el artista francés Raymond Monvoisin hacia 1842, ofrece una representación simbólica y estratégica del caudillo argentino.  A diferencia de otros cuadros oficiales donde Rosas aparece con uniforme militar, en esta obra se lo muestra con un poncho, resaltando su imagen de líder popular y no meramente militar.\\nMonvoisin se había formado en la tradición académica europea, pero para 1842 estaba más cerca del romanticismo. Rosas aparece de perfil, con expresión seria y firme, destacando su autoridad, y su mirada apunta a donde se origina el viento como  controlando la naturaleza. Mitre señaló “es el retrato más parecido que conozco de Rosas, hay mucho de emperador romano pero atenuado y corregido por un acento criollo”.\\nLa tradición artística oficial argentina considera al cuadro un estudio preliminar de una obra posterior desaparecida o que no existió. Según Rodrigo Cañete en “Historia a contrapelo del arte argentino”,  esta idea está contaminada por el prejuicio de negarse a aceptar el verdadero carácter de la obra.  Para Cañete, el artista, acogido en Chile por los enemigos de Rosas, tuvo el talento de hacer un cuadro ambigüo para que ambos bandos -unitarios y federales- puedan utilizarlo como bandera. Los rasgos de Rosas son imperiales pero carecen de la musculatura heroica de los emperadores y están levemente feminizados, y el poncho y la ropa son parecidos a los retratos de sus enemigos como gaucho bárbaro, líder de lo que Sarmiento despectivamente llamaba la “montonera árabe”.  Por su parte, para los rosistas, representa un emperador-dios-benefactor-madre tierra, al estilo de la Virgen María de las imágenes sudamericanas que la fusionan con la Pachamama india, como la Virgen María del Cerro Rico de Potosí,  pero con referencias académicas sutiles a grandes maestros como Zurbarán y Velázquez.	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	53
229	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	danseuse-et-admirateur-derri-re-la-sc-ne-bailarina-y-admirador-tras-la-escena	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	\N	\N	126		\N	60,5 x 73,5 cm.	\N	​A fines del siglo XIX París se perfilaba como la ciudad elegida por la burguesía para desplegar su hedonismo. \r\nLos sitios preferidos para hacerlo eran los cafés, la Ópera, los jardines suburbanos, el hipódromo o la ribera. Todos ellos quedaron registrados en las pinturas de los impresionistas o post-impresionistas, que examinaron en sus obras las distintas facetas de la vida moderna, tanto para criticarla, como para adularla. Jean-Louis Forain fue uno de estos artistas, de familia modesta, hijo de un “pintor de brocha gorda”, que inició sus estudios en el atelier del pintor Jean-Léon Gérôme, para luego estudiar con Jean-Baptiste Carpeaux quien lo introdujo en la representación de escenas bíblicas en un sentido moderno, temática importante en su desarrollo artístico posterior. \r\n\r\nTambién fue André Gill quien en 1870 le enseñó los secretos del arte de la ilustración, que utilizó para representar escenas de la vida cotidiana.No escapó al influjo de Manet, Renoir, Degas, quienes lo acercaron a las teorías del color y de la luz, tan experimentadas por el impresionismo. Sin embargo, con quien estableció mayor afinidad, tanto a nivel artístico como humano, fue con Degas. Tan es así que en varias ocasiones pintaron juntos trabajando sobre un mismo modelo. Bailarinas, carreras de caballos, desnudos, escenas de café, se repitieron en sus obras, pero abordadas desde una perspectiva diferente; mientras que Degas puso el acento en la captura de un instante, de un momento particular, cercano a un lenguaje fotográfico, Forain se concentró en lo gestual, en la fibra expresiva de los personajes, en los caracteres que transmiten su condición social (1).Uno de los temas que ambos comparten es el backstage de la Ópera. \r\n\r\nEn Bailarina y admirador tras la escena, Forain pone de relieve la particular relación, ambigua y muchas veces dolorosa, que se gestaba entre las bailarinas y los abonados a la Ópera. Estos últimos representaban una elite política, financiera y cultural, que llegaba a reservar casi la mitad de las localidades del teatro. Varias miembros del cuerpo de ballet provenían de familias pobres y solían aceptar un soporte económico a cambio de favores sexuales; otras establecían una amistad con “el protector” y solo unas pocas se casaban con ellos. El foyer, la sala de ensayo o calentamiento, oficiaba durante los intervalos como lugar de encuentro con los abonados. En este sitio transcurre la escena en cuestión, “el admirador”, que despliega su condición social a partir de los detalles de su atuendo elegante, tiene la mirada perdida en un punto fijo en el piso, y se refriega sus manos con guantes blancos. La bailarina sentada a su lado apenas gira su cabeza para observarlo, dejando caer al piso, desganadamente, el arco de violín que sostiene en sus manos. Se puede sobreentender un vínculo en las condiciones antes relatadas, a pesar de que ellos casi no interactúan. En el extremo opuesto de la composición y frente al decorado del escenario, un grupo de bailarinas cuchichea, tal vez adivinando aquello que el admirador no se anima a decirle a la mujer deseada. La resolución pictórica de la escena tiene muchas filiaciones con el tipo de factura abocetada y rápida de las obras contemporáneas de Degas, en las que se desdibujan los detalles a medida que se alejan del primer plano.	\N	314	\N	\N	1887	\N	135	2025-05-19 12:29:43.192411-03	2025-10-07 17:04:19.065482-03	100	​ fines del siglo XIX París se perfilaba como la ciudad elegida por la burguesía para desplegar su hedonismo. Los sitios preferidos para hacerlo eran los cafés, la Ópera, los jardines suburbanos, el hipódromo o la ribera. Todos ellos quedaron registrados en las pinturas de los impresionistas o post-impresionistas, que examinaron en sus obras las distintas facetas de la vida moderna, tanto para criticarla, como para adularla. Jean-Louis Forain fue uno de estos artistas, de familia modesta, hijo de un “pintor de brocha gorda”, que inici	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	54
311	Quiosco de Canaletas	quiosco-de-canaletas	Quiosco de Canaletas	\N	\N	\N	\N	\N	Técnica: Acuarela, gouache y grafito sobre papel\r\n47,5 x 62 cm\r\nNro. de inventario: 2001.13\r\nDonación: Eduardo F. Costantini, Buenos Aires	\N	Balcanización social y emergencia de la individualidad, vivencia íntima de la vida cotidiana, contingencia y temporalidad son datos del ambiente cultural al que fue sensible Barradas durante su segunda estadía en Barcelona (1916-1918), un ambiente que compartió con su coterráneo Joaquín Torres García y, en parte, también con el pintor ecléctico español Celso Lagar. Fue precisamente a través de este último como conoció a Torres García en agosto de 1917. Lagar venía de una larga estadía en París, donde había tenido contacto con el cubismo de Picasso y de Juan Gris y también con el orfismo de los Delaunay. Este dato no es menor, dado que, en su relación con Barradas, éste debió de ser receptivo a ciertas ideas de Lagar, y especialmente al uso de las formas curvas que heredaba de la estética orfista. La amistad llegó al punto de que el pintor uruguayo, arribado a la capital española en agosto de 1918, le organizó una exposición a Lagar en el Ateneo de Madrid en noviembre de ese año.\r\n\r\nEn una conferencia sobre Barradas que Torres García brindó en Montevideo en 1936, decía:\r\n\r\nTodos saben la pasión de Barradas por el café, […] tenía su mesa favorita que se podía reconocer fácilmente por estar llena de esos dibujos con que siempre acompañaba sus peroraciones, mesa junto a seis cristales que daban a la plaza y desde la cual se veía todo el vibracionismo callejero.1\r\n\r\nTorres García utilizó aquí el término vibracionismo, que fue con el que Barradas bautizó su obra temprana barcelonesa (1917-1918). Su primera enunciación pública posiblemente haya sido en la revista Arc Voltaic, dirigida por Joan Salvat Papasseit, aparecida en febrero de 1918. En ella Barradas publicó un dibujo (Bonanitingui) hecho en 1917, que llevaba como leyenda “Dibuix Vibracionista”, y el término se repetía en la carátula de la revista: “Vibracionisme de idees; Poemes en ondes Hertzianes”.	\N	309	\N	\N	1918	\N	135	2025-05-19 13:07:58.324319-03	2025-09-21 19:53:04.104268-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0	41
448	Sendas perdidas	sendas-perdidas	Sendas perdidas	\N	\N	126		\N	Carbonilla y temple sobre tela. 135 x 165 cm.	\N	Como lo han hecho artistas de todas las épocas, Gárgano nos propone su propia versión del infierno, construido pacientemente sobre el papel a lo largo de muchos años. \r\nSus trabajos no se alimentan directamente de la realidad: crean una realidad propia, que se nutre de otras imágenes. Cada obra está hecha de otras obras, y por eso mismo conforma un mundo.	\N	463	\N	\N	2015	\N	135	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	\N	\N	137	f	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	55
404	En la costa de Valencia	en-la-costa-de-valencia	En la costa de Valencia	\N	\N	126		\N	Oleo sobre tela 57 x 88,5 cm.	\N	Esta escena costumbrista que representa el trabajo de los pescadores que vuelven de la faena y el juego de un niño se desarrolla posiblemente en la playa del Cabañal, frecuentada por Sorolla, que se caracterizaba por conjugar, en torno al 1900, tanto a pescadores y sus familias como a pintores en busca de temáticas que mostraran el ser regional. Esta combinación de los trabajos preindustriales de las clases populares, por una parte, y de pintura pleinairista de visos nacionalistas, por otra, brindaba una imagen optimista del Levante español distante del duro proceso de transformación del paisaje de la región por la industrialización. La mecanización y el gigantismo urbano eran la nueva fisonomía que modelaba la vida y las costumbres del Levante. Sin embargo, la actitud de huida nostálgica de la civilización hacia la rústica naturaleza de pintores como Sorolla hacía de estas costas un refugio bucólico frente a las consecuencias generadas por la Revolución Industrial.\\nDetrás de este costumbrismo de ambiente marino se encuentra la verdadera raison d’être de las búsquedas de Sorolla: la luz mediterránea. \r\n\r\nSu sensibilidad lumínica pertenece a una de las dos tendencias de la pintura española que marcaron la vuelta del siglo: la “España blanca” contrapuesta a la “España negra” de Zuloaga, Solanas y Romero de Torres, entre otros. La luz, la gran protagonista de esta pintura, es captada con rapidez y al plein air del mediodía dando como resultado una obra definida por la instantaneidad, el abocetamiento y una atmósfera vibrante producto de los reflejos del sol enceguecedor en su cenit sobre el mar.\r\n\r\nSorolla ocupa un lugar especial dentro del movimiento luminista cuyos centros fueron Valencia y Sitges. \r\n\r\nA diferencia del resto de los luministas, reunió en sí influencias que supo dosificar, como la del impresionismo y la de los pintores escandinavos como Peder Severin Krøyer (1851-1909), Viggo Johansen (1851-1935) y Anders Zorn (1860-1920) o del alemán Adolph von Menzel (1815-1905), cuya obra conoció en París. En el debate de tradición-modernidad, su pintura no solo tenía la capacidad de resolver el dilema entre el conocimiento académico y la experimentación impresionista, equilibrando ambas posiciones, sino que también lograba mantener un juste milieu artístico (en un momento de vanguardias contestatarias y actitudes reaccionarias hacia ellas) que le brindó un éxito comercial internacional sin precedente en los otros luministas. Todo esto lleva a la historiografía artística a considerarlo como el pintor que clausura el movimiento luminista levantino.\\nLa captación instantánea y lumínica de las formas, próxima al impresionismo parisino pero ajena a sus inquietudes, es una constante en los pintores nacidos en el Levante español y activos a finales del siglo XIX y principios del siguiente, de quienes el estilo sorollesco se nutre. \r\n\r\nEstos, a pesar de su formación conservadora, se atrevieron a incursionar en técnicas inusuales y de gran modernidad en relación con las normas académicas que imponían el gusto por lo bituminoso, provocando un cambio sustancial en la manera de pintar. Al focalizarse en la presencia dominante del mar en el que la vista se pierde al infinito, en su atmósfera costera y en la sugestión hipnótica de la luz y sus matices, propusieron al espectador involucrarse en una percepción de la realidad que es anterior a una primera estructuración gestáltica, es decir una percepción en la cual las sensaciones anteceden a las formas. El luminismo levantino se inserta en una tendencia al aclaramiento de la paleta que tiene lugar en la pintura europea del siglo XIX, evidencia de búsquedas en relación con el color y con la luz generadas a partir del desarrollo de la pintura de paisaje y de la captación de efectos naturalistas.\r\n\r\nSorolla creó una “maniera” posible de ser emulada a nivel compositivo y “digerible” temáticamente a pesar de su modernidad. Su técnica lumínica se oponía diametralmente a la paleta armada del frío academicismo que había entronizado a la pintura de historia con sus composiciones artificiosas. Sin embargo, la organización deliberada de Sorolla de masas de luz y color lo acercaban al pensamiento académico que gustaba de componer fuertemente el lienzo equilibrando las formas y los colores. La adhesión, en parte, a la técnica impresionista no debe confundirse con la adopción del descubrimiento de la impresión lumínica mediante la yuxtaposición de colores puros colocando en un plano secundario la estructura, ni tampoco con la disolución de la forma –divergencias con el impresionismo que lo unían al resto de los luministas– aunque su factura suele oscilar hacia una pincelada de coma impresionista. Estas características técnicas se observan en esta obra, como así también en La vuelta de la pesca (1898, inv. 2007, MNBA), ambas dotadas de una infraestructura muy compleja. Cierta impresión y velocidad de la poética de Sorolla nunca pudieron ser emuladas acabadamente por sus seguidores.\r\n\r\nEn cuanto a la temática, el artista impuso la descripción de una edad de oro del Mediterráneo, una Arcadia donde nos presenta hombres en simple y dulce armonía con la naturaleza. El gesto de los pescadores y el pequeño jugando representan lo que fluye y es efímero. Aun así, estos instantes evocan la eternidad de los tipos humanos de la región.	\N	416	\N	\N	1898	\N	135	2025-06-10 15:14:26.074639-03	2025-10-29 08:49:28.621703-03	100	Esta escena costumbrista que representa el trabajo de los pescadores que vuelven de la faena y el juego de un niño se desarrolla posiblemente en la playa del Cabañal, frecuentada por Sorolla, que se caracterizaba por conjugar, en torno al 1900, tanto a pescadores y sus familias como a pintores en busca de temáticas que mostraran el ser regional. Esta combinación de los trabajos preindustriales de las clases populares, por una parte, y de pintura pleinairista de visos nacionalistas, por otra, brindaba una imagen optimista del Levante e	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	50
221	La Coiffure (El peinado)La berge de La Seine (Orillas del Sena)	la-coiffure-el-peinado-la-berge-de-la-seine-orillas-del-sena	La Coiffure (El peinado)La berge de La Seine (Orillas del Sena)	\N	\N	126		\N	60 x 73 cm. Con marco: 84 x 97 cm.	\N		\N	316	\N	\N	1891	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100		\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	51
217	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	portrait-de-suzanne-valadon-madame-suzanne-valadon-artiste-peintre-retrato-de-suzanne-valadon-pintora	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	\N	\N	126		\N	55 x 46 cm. - Marco: 77,5 x 66,5 x 6,5 cm.	\N	En 1885, con apenas veintiún años, en busca de independencia y deseoso de escapar del severo control paterno, Henri de Toulouse-Lautrec (1) abandonó el sur de Francia para ir a París. Se estableció en el barrio de Montmartre (2) con su amigo René Grenier. La integración en la vida de la butte no fue simple; contribuyeron a su inserción sus amigos, entre ellos François Gauzi, compañero de estudios en el atelier de Ferdinand Cormon.En estrecho contacto con Van Gogh, Vallotton, Bonnard, Lautrec participó plenamente del clima artístico parisino, que en ese tiempo buscaba en varios sentidos la superación del impresionismo, y orientó sus indagaciones hacia una pintura adherida a la realidad que, a través de la estilización formal, fijara los tipos psicológicamente característicos. Participante desencantado del ambiente de Montmartre –cafés-concert, prostíbulos, salas de baile, teatros–, en sus cuadros y en los numerosos bocetos tomados de la realidad Lautrec trazó toda la intimidad y la tristeza de ese submundo humano. Fundamental en ese ambiente fue el encuentro con una mujer que, antes que una seguidilla de modelos y amantes, entró en su vida y en su obra. Se trata de Marie-Clémentine Valade (3), más famosa por el nombre de Suzanne Valadon, que prestó sus rasgos a algunos de los “tipos femeninos” más conocidos de Lautrec (4). Marie-Clémentine era una jovencita sin medios materiales ni culturales, nacida en 1865, de madre modista y padre desconocido. Se aventuró en la actividad circense como acróbata, pero una caída la obligó a desistir; probó otros trabajos humildes hasta que decidió ofrecerse como modelo artística. Comenzó a ser solicitada por los mejores pintores de la época, el primero de todos Puvis de Chavannes, que fue durante cierto período también su amante. Siguieron Renoir, Manet, Gauguin; a todos regaló su belleza, de todos tomó algo. Apasionada por el dibujo, durante las sesiones de pose la joven modelo observaba a los maestros trabajando; muy pronto todos, particularmente Lautrec (5), la alentaron a seguir su pasión. Fue él quien le sugirió el nombre artístico de Suzanne Valadon, porque como la Susana bíblica, Marie estaba rodeada de “viejos ávidos”. Con este retrato titulado Madame Valadon, artiste peintre, Lautrec ratifica el rol de pintora que había asumido Marie (6).Toulouse-Lautrec solía pasar del taller al aire libre. Sus pinturas a cielo abierto constituyeron una etapa que le permitió adaptar estilística y temáticamente ciertos valores adquiridos de la pintura en plein air, no tanto en la línea de los estudios lumínicos de los impresionistas sino en una búsqueda de mayor libertad. En esta pintura Suzanne está sentada, representada frontalmente, por delante de un paisaje otoñal. Se trata muy probablemente del jardín del viejo Forest, un terreno dedicado al tiro con arco, situado en la esquina del boulevard de Clichy y la rue Caulaincourt, a pocos pasos del atelier del artista, donde Lautrec realizó distintos retratos femeninos (Justine Dieuhl, 1891, Musée d’Orsay, París). Su cuerpo está delimitado por un contorno negro bien marcado –reminiscencia del ejemplo de Degas, por quien Lautrec sentía real veneración– dentro del cual, sin embargo, los volúmenes parecen llenados sumariamente por amplios trazos de color, cuya porosidad es exaltada por la textura de la tela sin preparación. El rostro de la modelo se recorta contra un conjunto de planos cromáticos que han perdido la rigidez y el rigor del contemporáneo Retrato de Jeanne Wenz (1886, The Art Institute of Chicago), testimoniando ya la gran maestría y la verdadera ductilidad de pincelada adquirida por el artista. El fondo, una armonía de amarillos, beiges y marrones, confundidos en toques diluidos, arroja una suerte de velo azulado sobre el cuerpo de la mujer, suavizando la expresión firme del personaje. El retrato femenino suele ser un compromiso entre la elegancia y el realismo a partir de la observación directa. Los artistas mundanos tienen como única preocupación realzar la belleza y la condición social de la modelo. Un artista en boga en la época, como por ejemplo Giovanni Boldini, daba a sus modelos un aspecto lo más semejante y halagador posible. Lautrec, en cambio, más lúcido, como Van Gogh, fue a lo esencial gracias a una manera descriptiva sobria y directa, escapando a la tentación de “embellecer”. El hecho de disponer de una renta familiar le permitía eludir las obligaciones de los retratos “alimentarios” –los realizados para vivir todo el mes– y seguir solamente su fantasía; son raros, de hecho, los retratos realizados por el artista en razón de algún encargo célebre (por ejemplo, Madame de Gortzikoff ) (7). La obra de Buenos Aires debe relacionarse con un retrato de asunto análogo, conservado en la Ny Carlsberg Glyptotek de Copenhague (1886-1887). La modelo posó además para la famosa tela La buveuse o La gueule de bois (1889, Harvard Fogg Art Museum, Cambridge; el dibujo, de 1887-1888, se conserva en el Musée Toulouse-Lautrec, Albi), escena social de depravación y miseria (8).\r\n Barbara Musetti\r\n1— Sobre la vida de Lautrec, cf. U. Felbinger, Henri de Toulouse-Lautrec: sa vie et son oeuvre. Köln, Könemann, 2000; A. Simon, Toulouse-Lautrec: biographie. Tournai, La Renaissance du Livre, 1998. 2— Sobre la influencia de Montmartre en la obra de Lautrec, cf. R. Thomson; P. D. Cate y M. Weaver Chapin (dir.), Toulouse-Lautrec and Montmartre, cat. exp. Washington, National Gallery of Art, 2005; F. Maubert, Le Paris de Lautrec. Paris, Assouline, 2004; P. Vabanne, Henri de Toulouse-Lautrec: le peintre de la vie moderne. Paris, Terrail, 2003. 3— Cf. T. Diamand Rosinsk, Suzanne Valadon. Paris, Flammarion, 2005. 4— El retrato femenino figura entre los temas más recurrentes de la obra de Lautrec. Sobre este asunto, véase: Le donne di Toulouse-Lautrec, cat. exp. Milano, Mazzotta, 2001. 5— Degas, que fue mentor y amigo de Valadon y uno de los primeros en adquirir sus dibujos, los calificó de “malos y blandos” y definió a la artista como “la terrible Marie”. 6— Sobre la actividad artística de Valadon, madre del pintor Maurice Utrillo, véase: M. Restellini (dir.), Valadon-Utrillo: au tournant du siècle à Montmartre: de l’impressionnisme à l’École de Paris, cat. exp. Paris, Pinacothèque de Paris, 2009; Alexandra Charvier et al., Utrillo, Valadon, Utter: 12 rue Cortot: un atelier, trois artistes. Sannois, Musée Utrillo-Valadon, 2008. 7— Toulouse Lautrec, cat. exp. Paris, Réunion des musées nationaux, 1992, p. 133.8— Para un recorrido por toda la obra de Toulouse-Lautrec, cf. M. G. Dortu, Toulouse-Lautrec et son oeuvre. New York, Collectors Editions, 1971, vol. 1-4.	\N	762	\N	\N	1885	\N	135	2025-05-19 12:29:43.192411-03	2025-10-29 08:53:37.590357-03	100	En 1885, con apenas veintiún años, en busca de independencia y deseoso de escapar del severo control paterno, Henri de Toulouse-Lautrec (1) abandonó el sur de Francia para ir a París. Se estableció en el barrio de Montmartre (2) con su amigo René Grenier. La integración en la vida de la butte no fue simple; contribuyeron a su inserción sus amigos, entre ellos François Gauzi, compañero de estudios en el atelier de Ferdinand Cormon.En estrecho contacto con Van Gogh, Vallotton, Bonnard, Lautrec participó plenamente del clima artístico pa	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	52
328	Toulouse Lautrec - En observation - M.Fabre, Officier de reserve	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve	Toulouse Lautrec - En observation - M.Fabre, Officier de reserve	\N	\N	\N	\N	\N	\N	\N	\N	\N	329	\N	\N	0	\N	135	2025-05-19 14:07:30.084163-03	2025-05-19 14:07:30.084163-03	100	\N	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	56
219	La berge de La Seine (Orillas del Sena)	la-berge-de-la-seine-orillas-del-sena	La berge de La Seine (Orillas del Sena)	\N	\N	126		\N	60 x 73 cm. Con marco: 84 x 97 cm.	\N	En la sesión del 17 de noviembre de 1910 los miembros del Concejo Deliberante de la ciudad de Buenos Aires habían votado  un proyecto que destacaba el rol de la Municipalidad para promover la cultura artística,  una de las vías podía ser la donación de obras de arte. Esta resolución, benefició  al MNBA que contó con presupuesto estatal para incrementar su patrimonio  con adquisiciones de obras que concretó la Comisión Nacional de Bellas Artes en la “Exposición Internacional de Arte del Centenario Buenos Aires 1910”.	\N	316	\N	\N	1891	\N	135	2025-05-19 12:29:43.192411-03	2025-10-06 17:49:29.06594-03	100	En la sesión del 17 de noviembre de 1910 los miembros del Concejo Deliberante de la ciudad de Buenos Aires habían votado  un proyecto que destacaba el rol de la Municipalidad para promover la cultura artística,  una de las vías podía ser la donación de obras de arte. Esta resolución, benefició  al MNBA que contó con presupuesto estatal para incrementar su patrimonio  con adquisiciones de obras que concretó la Comisión Nacional de Bellas Artes en la “Exposición Internacional de Arte del Centenario Buenos Aires 1910”.	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	57
484	Procesión sorprendida por la lluvia	procesi-n-sorprendida-por-la-lluvia	Procesión sorprendida por la lluvia	\N	\N	126		\N	Oleo sobre tela 63 x 102 cm. - Marco: 101,5 x 140 cm.	\N	Esta obra constituye un ejemplo sobresaliente del costumbrismo practicado por Mariano Fortuny, de gran influencia en la pintura española del último tercio del siglo XIX, aunque en este caso se aleja del tratamiento preciosista con el que alcanzó éxito y fama internacionales, al punto de haber sido considerada un boceto por la falta de atención al detalle .	\N	491	\N	\N	2017	\N	135	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100	\N	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	58
278	El despertar de la criada	el-despertar-de-la-criada	El despertar de la criada	\N	\N	126		\N		\N	Le lever de la bonne es un desnudo naturalista. Aun cuando el título y algunos elementos de la composición lo connotan, la pintura pertenece al género que a lo largo del siglo XIX fue campo de batalla de las audacias modernistas. No hay narratividad en la escena, se limita a presentar el cuerpo de una muchacha joven en el que se lee su pertenencia a la clase trabajadora. La sencillez del mobiliario, las ropas amontonadas sobre un banco de paja, al pie de la cama sin tender y, sobre todo, el título del cuadro, indican que se trata de una criada. Un foco de luz dirigida desde la izquierda ilumina ese cuerpo que se destaca con intensidad dramática sobre el fondo neutro de la pared de fondo. La piel de la muchacha es oscura, sobre todo en las zonas que el cuerpo de una mujer de trabajo se veía expuesto al sol: las manos, el rostro y las piernas. La criada aparece ensimismada en la tarea de dar vuelta una media para calzarla, de modo que el contraste entre los pechos y la mano castigada por la intemperie se hace más evidente. Cruzadas una sobre la otra, las piernas, gruesas y musculosas se destacan con un tratamiento naturalista que se detiene en la representación minuciosa de unos pies toscos y maltratados. El pubis, invisible tras la pierna cruzada, se ubica en el centro exacto de la composición. Ninguno de estos detalles pasó inadvertido a los críticos que, tanto en París como en Buenos Aires, comentaron el cuadro en 1887.\r\n\r\nFue pintado en París por Eduardo Sívori quien, tras haber logrado su aceptación en el Salón anual, lo envió a Buenos Aires ese mismo año sabiendo de antemano que su exhibición despertaría polémicas. Fue el primer gesto vanguardista en la historia del arte argentino.\r\n\r\nEn 1887 la pintura naturalista ocupaba un lugar destacado en el Salón de París, como una de las vías de renovación de la estética oficial de la Academia. Sin alejarse demasiado de las convenciones formales impuestas por la tradición (claroscuro, perspectiva, tratamiento de la superficie) los pintores naturalistas siguieron una línea de renovación iconográfica abierta a mediados de siglo por Gustave Courbet y Jean-François Millet, introduciendo temas derivados de la literatura de Émile Zola, o que planteaban una denuncia directa de los conflictos sociales contemporáneos, en un tono en general narrativo y melodramático. No fue el desnudo un género frecuente en la pintura naturalista. El cuadro de Sívori fue enseguida interpretado por la crítica francesa (Roger- Milès, Emery, E. Benjamin, Paul Gilbert, entre ellos) como obra derivada de Zola, un poco “excesivo” en la representación de un cuerpo que fue visto como feo, sucio y desagradable.\r\nEn Buenos Aires, donde no había habido hasta entonces más que pocas y discutidas exhibiciones de desnudos artísticos, el cuadro fue objeto no solo de una intensa polémica en la prensa (fue calificado de “indecente” y “pornográfico”) sino también de un importante alineamiento de intelectuales y artistas en su favor. En una reunión de su Comisión Directiva, el 22 de agosto de 1887, la Sociedad Estímulo de Bellas Artes decidió exhibir el cuadro en su local, cursar invitaciones especiales a los socios y a los periodistas de la capital, y abrir un álbum que recogiera las firmas de todos aquellos “que quieran manifestar al autor sus felicitaciones por los progresos realizados”. Más de 250 firmas de artistas, escritores, etc. se estamparon en ese álbum en cuyas páginas Sívori guardó además los recortes de las críticas recibidas y fotografías de ese y otros cuadros suyos que habían sido expuestos en el Salón de París hasta su regreso definitivo en 1891.\r\n\r\nLa fotografía de Le lever de la bonne conservada en ese álbum presenta algunas diferencias con el cuadro definitivo. No sabemos si las modificaciones fueron hechas antes o después de ser exhibido en el Salón de París. En la mesilla de noche puede verse una palangana y una jarra (elementos de higiene) en lugar del candelabro con una vela apagada de la versión final. Por otra parte, en la pared del fondo se vislumbra un estante con frascos y potes de tocador. Todos estos elementos pueden verse a simple vista cuando el cuadro se mira con una luz potente, como si el artista hubiera decidido dejar que aquellos arrepentimientos se adivinen en el fondo en penumbras. Pero lo más significativo es el cambio en la fisonomía de la criada. Su rostro y su peinado aparecen en la fotografía menos oscuros. La criada parece una faubourgienne en la versión de la fotografía. Tal vez más cercana a la apariencia de una prostituta (los elementos de higiene también contribuyen a ello), tema predilecto de la vanguardia y de la crítica social de la época. Aun modificada, la criada fue interpretada como prostituta y considerada pornográfica por varios de sus primeros comentadores. Su transformación es significativa. Tal vez el artista decidió alejarse del “tema” social de moda al presentarse al Salón. Tal vez decidió transformarla inequívocamente en una criada pobre para su exhibición en Buenos Aires.	\N	302	\N	\N	1887	\N	135	2025-05-19 12:30:21.186648-03	2025-10-07 17:09:05.457055-03	100	Le lever de la bonne es un desnudo naturalista. Aun cuando el título y algunos elementos de la composición lo connotan, la pintura pertenece al género que a lo largo del siglo XIX fue campo de batalla de las audacias modernistas. No hay narratividad en la escena, se limita a presentar el cuerpo de una muchacha joven en el que se lee su pertenencia a la clase trabajadora. La sencillez del mobiliario, las ropas amontonadas sobre un banco de paja, al pie de la cama sin tender y, sobre todo, el título del cuadro, indican que se trata de u	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	59
270	La vuelta del malón	la-vuelta-del-mal-n	La vuelta del malón	\N	\N	126	Adqusición Sociedad de Estímulo	\N	Oleo Sobre tela 186,5 x 292 cm.	\N	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. \r\n\r\nPintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtuvo una medalla (de única clase) y al regreso se exhibió nuevamente en Buenos Aires, en el segundo Salón del Ateneo en 1894. \r\n\r\nFue la obra más celebrada de Della Valle. Presentaba por primera vez en las grandes dimensiones de una pintura de salón una escena que había sido un tópico central de la conquista y de la larga guerra de fronteras con las poblaciones indígenas de la pampa a lo largo del siglo XIX: el saqueo de los pueblos fronterizos, el robo de ganado, la violencia y el rapto de cautivas. \r\n\r\nEn el manejo de la luz y la pincelada se advierte la huella de la formación de Della Valle en Florencia: no solo el aprendizaje con Antonio Ciseri sino también el conocimiento de los macchiaioli y los pintores del Risorgimento italiano. Algunos críticos lo vincularon con los grandes cuadros de historia del español Ulpiano Checa que se había hecho famoso por sus entradas de bárbaros en escenas de la historia de España y del imperio romano. Sin embargo, el cuadro de Della Valle entroncaba con una larga tradición no solo en las crónicas y relatos literarios inspirados en malones y cautivas, sino también en imágenes que, desde los primeros viajeros románticos europeos que recorrieron la región en la primera mitad del siglo XIX, representaron cautivas y malones. \\n En la década de 1870 Juan Manuel Blanes había realizado también algunas escenas de malones que aparecen como antecedentes de esta obra. Casi ninguna, sin embargo, había sido expuesta al público ya que tuvieron una circulación bastante restringida. La vuelta del malón fue, entonces, la primera imagen que impactó al público de Buenos Aires referida a una cuestión de fuerte valor emotivo e inequívoco significado político e ideológico. Según refiere Julio Botet, a partir de una entrevista al artista en agosto de 1892, el asunto del cuadro se inspiraba en un malón llevado por el cacique Cayutril y el capitanejo Caimán a una población no mencionada. Otro comentario (en el diario Sud-América) ubicaba el episodio en la población de 25 de Mayo. Pero más allá de la anécdota el cuadro aparece como una síntesis de los tópicos que circularon como justificación de la “campaña del desierto” de Julio A. Roca en 1879, produciendo una inversión simbólica de los términos de la conquista y el despojo. El cuadro aparece no solo como una glorificación de la figura de Roca sino que, en relación con la celebración de 1492, plantea implícitamente la campaña de exterminio como culminación de la conquista de América. Todos los elementos de la composición responden a esta idea, desplegados con nitidez y precisión significativa. La escena se desarrolla en un amanecer en el que una tormenta comienza a despejarse. El malón aparece equiparado a las fuerzas de la naturaleza desencadenadas (otro tópico de la literatura de frontera). \\nLos jinetes llevan cálices, incensarios y otros elementos de culto que indican que han saqueado una iglesia. Los indios aparecen, así, imbuidos de una connotación impía y demoníaca. El cielo ocupa más de la mitad de la composición, dividida por una línea de horizonte apenas interrumpida por las cabezas de los guerreros y sus lanzas. En la oscuridad de ese cielo se destaca luminosa la cruz que lleva uno de ellos y la larga lanza que empuña otro, como símbolos contrapuestos de civilización y barbarie. En la montura de dos de los jinetes se ven cabezas cortadas, en alusión a la crueldad del malón. En el extremo izquierdo se destaca del grupo un jinete que lleva una cautiva blanca semidesvanecida, apoyada sobre el hombro del raptor que se inclina sobre ella. Fue este el fragmento más comentado de la obra, a veces en tono de broma, aludiendo a su connotación erótica, o bien criticando cierta inadecuación del aspecto (demasiado “civilizado” y urbano) de la mujer y de su pose con el resto de la composición. La vuelta del malón fue llevada a la Exposición Colombina de Chicago por el oftalmólogo Pedro Lagleyze, amigo del artista, en medio de la desorganización y dificultades que rodearon ese envío oficial. \\nFue exhibida en el pabellón de manufacturas, como parte del envío argentino, junto a bolsas de cereales, lanas, cueros, etc. Los pocos comentarios que recibió se refirieron a la escena representada como una imagen de las dificultades que la Argentina había logrado superar para convertirse en una exitosa nación agroexportadora. Ángel Della Valle pintó una versión reducida de La vuelta del malón para obsequiar a Lagleyze al regreso. Conocida como “malón chico” ha sido con frecuencia tomada por un boceto. También pintó más tarde algunos fragmentos aislados de su gran tela: el grupo del guerrero y la cautiva y el indio que enarbola la cruz. Della Valle había comenzado a pintar cuadros de tema pampeano durante su estadía en Florencia. \\nEn 1887 envió a Buenos Aires varias obras, entre las que pudo verse un indio a caballo (En la pampa) y La banda lisa, que aparecen como tempranas aproximaciones al tema de La vuelta del malón. La pintura fue solicitada por el director del MNBA, Eduardo Schiaffino, a la familia del artista tras su muerte en 1903; esta optó por donarla a la Sociedad Estímulo de Bellas Artes con el cargo de su venta al MNBA a fin de instituir un premio anual de pintura denominado “Ángel Della Valle”.	\N	301	\N	\N	1892	\N	135	2025-05-19 12:30:21.186648-03	2025-10-06 17:36:54.590141-03	100	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. Pintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtu	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	60
276	La vuelta al hogar	la-vuelta-al-hogar	La vuelta al hogar	\N	\N	126	Adqusición Sociedad de Estímulo	\N	Oleo Sobre tela 90,5 x 118,5 cm	\N	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. Pintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtuvo una medalla (de única clase) y al regreso se exhibió nuevamente en Buenos Aires, en el segundo Salón del Ateneo en 1894. \\nFue la obra más celebrada de Della Valle. Presentaba por primera vez en las grandes dimensiones de una pintura de salón una escena que había sido un tópico central de la conquista y de la larga guerra de fronteras con las poblaciones indígenas de la pampa a lo largo del siglo XIX: el saqueo de los pueblos fronterizos, el robo de ganado, la violencia y el rapto de cautivas. \r\n\r\nEn el manejo de la luz y la pincelada se advierte la huella de la formación de Della Valle en Florencia: no solo el aprendizaje con Antonio Ciseri sino también el conocimiento de los macchiaioli y los pintores del Risorgimento italiano. Algunos críticos lo vincularon con los grandes cuadros de historia del español Ulpiano Checa que se había hecho famoso por sus entradas de bárbaros en escenas de la historia de España y del imperio romano. Sin embargo, el cuadro de Della Valle entroncaba con una larga tradición no solo en las crónicas y relatos literarios inspirados en malones y cautivas, sino también en imágenes que, desde los primeros viajeros románticos europeos que recorrieron la región en la primera mitad del siglo XIX, representaron cautivas y malones. \\n En la década de 1870 Juan Manuel Blanes había realizado también algunas escenas de malones que aparecen como antecedentes de esta obra. Casi ninguna, sin embargo, había sido expuesta al público ya que tuvieron una circulación bastante restringida. La vuelta del malón fue, entonces, la primera imagen que impactó al público de Buenos Aires referida a una cuestión de fuerte valor emotivo e inequívoco significado político e ideológico. Según refiere Julio Botet, a partir de una entrevista al artista en agosto de 1892, el asunto del cuadro se inspiraba en un malón llevado por el cacique Cayutril y el capitanejo Caimán a una población no mencionada. Otro comentario (en el diario Sud-América) ubicaba el episodio en la población de 25 de Mayo. Pero más allá de la anécdota el cuadro aparece como una síntesis de los tópicos que circularon como justificación de la “campaña del desierto” de Julio A. Roca en 1879, produciendo una inversión simbólica de los términos de la conquista y el despojo. \r\n\r\nEl cuadro aparece no solo como una glorificación de la figura de Roca sino que, en relación con la celebración de 1492, plantea implícitamente la campaña de exterminio como culminación de la conquista de América. Todos los elementos de la composición responden a esta idea, desplegados con nitidez y precisión significativa. La escena se desarrolla en un amanecer en el que una tormenta comienza a despejarse. El malón aparece equiparado a las fuerzas de la naturaleza desencadenadas (otro tópico de la literatura de frontera). \\nLos jinetes llevan cálices, incensarios y otros elementos de culto que indican que han saqueado una iglesia. Los indios aparecen, así, imbuidos de una connotación impía y demoníaca. El cielo ocupa más de la mitad de la composición, dividida por una línea de horizonte apenas interrumpida por las cabezas de los guerreros y sus lanzas. En la oscuridad de ese cielo se destaca luminosa la cruz que lleva uno de ellos y la larga lanza que empuña otro, como símbolos contrapuestos de civilización y barbarie. En la montura de dos de los jinetes se ven cabezas cortadas, en alusión a la crueldad del malón. En el extremo izquierdo se destaca del grupo un jinete que lleva una cautiva blanca semidesvanecida, apoyada sobre el hombro del raptor que se inclina sobre ella. Fue este el fragmento más comentado de la obra, a veces en tono de broma, aludiendo a su connotación erótica, o bien criticando cierta inadecuación del aspecto (demasiado “civilizado” y urbano) de la mujer y de su pose con el resto de la composición. La vuelta del malón fue llevada a la Exposición Colombina de Chicago por el oftalmólogo Pedro Lagleyze, amigo del artista, en medio de la desorganización y dificultades que rodearon ese envío oficial. \\nFue exhibida en el pabellón de manufacturas, como parte del envío argentino, junto a bolsas de cereales, lanas, cueros, etc. Los pocos comentarios que recibió se refirieron a la escena representada como una imagen de las dificultades que la Argentina había logrado superar para convertirse en una exitosa nación agroexportadora. Ángel Della Valle pintó una versión reducida de La vuelta del malón para obsequiar a Lagleyze al regreso. Conocida como “malón chico” ha sido con frecuencia tomada por un boceto. También pintó más tarde algunos fragmentos aislados de su gran tela: el grupo del guerrero y la cautiva y el indio que enarbola la cruz. \r\n\r\nDella Valle había comenzado a pintar cuadros de tema pampeano durante su estadía en Florencia. \\nEn 1887 envió a Buenos Aires varias obras, entre las que pudo verse un indio a caballo (En la pampa) y La banda lisa, que aparecen como tempranas aproximaciones al tema de La vuelta del malón. La pintura fue solicitada por el director del MNBA, Eduardo Schiaffino, a la familia del artista tras su muerte en 1903; esta optó por donarla a la Sociedad Estímulo de Bellas Artes con el cargo de su venta al MNBA a fin de instituir un premio anual de pintura denominado “Ángel Della Valle”.	\N	304	\N	\N	1885	\N	135	2025-05-19 12:30:21.186648-03	2025-11-02 16:33:09.316234-03	100	La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. Pintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtu	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	61
197	Le bain de Vénus (El baño de Venus)	le-bain-de-v-nus-el-ba-o-de-venus	Le bain de Vénus (El baño de Venus)	\N	\N	126		\N	Oleo Sobre tabla 35 x 28 cm.	\N	El título original de esta pintura puede haber sido puesto por Redon, ya que al parecer la obra fue comprada al artista sin intermediarios. Los toques de azul y verde en la zona inferior derecha de la tela pueden considerarse una representación del agua, y el tocado de la figura puede evocar un personaje mitológico.  \r\n\r\nEstos aspectos, sin embargo, son los únicos que justificarían el título de El baño de Venus. La figura no está desnuda, aparece (apenas) vestida y carece de otro atributo inherente a la diosa del amor. Su movimiento no corresponde a una escena de baño y el personaje demuestra una vacilación indigna de alguien nacido del mar. Con los dos brazos extendidos hacia delante y la cabeza hacia atrás, parece moverse de derecha a izquierda como si estuviese huyendo de algo –en una posición relativamente similar a la del dibujo Female Nude (Bridgestone Museum, Tokio)–. \r\n\r\nEl origen de su miedo o resistencia parece ser la forma circular sobre la derecha (de la mitad del tamaño de la figura) que semeja más a un insecto o a uno de los monstruos submarinos pintados por Redon que a las flores y las hojas reconocibles sobre la izquierda. Esta interpretación se sustenta en el uso del negro para delinear y rellenar parte de esta forma, color asociado por Redon al mundo fantástico de sus primeras carbonillas y litografías.	\N	639	\N	\N	1908	\N	135	2025-05-19 12:29:38.885286-03	2025-10-03 20:41:13.926535-03	100	El título original de esta pintura puede haber sido puesto por Redon, ya que al parecer la obra fue comprada al artista sin intermediarios. Los toques de azul y verde en la zona inferior derecha de la tela pueden considerarse una representación del agua, y el tocado de la figura puede evocar un personaje mitológico. Estos aspectos, sin embargo, son los únicos que justificarían el título de El baño de Venus. La figura no está desnuda, aparece (apenas) vestida y carece de otro atributo inherente a la diosa del amor. Su movimiento no cor	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	62
592	O Impossivel	new	\N	\N	\N	\N	\N	\N	écnica: Yeso\r\n182 × 175 × 91 cm\r\nNro. de inventario: 1998.03\r\nDonación: Eduardo F. Costantini, 2001	\N	En 1946, en su quinta exposición individual, realizada en la Valentine Gallery, en Nueva York, Maria Martins presentó una primera versión, en yeso, de la escultura O impossível, con largos brazos y de un tamaño menor que la del acervo de Malba.  Se trataba, muy probablemente, del molde usado en la fundición en bronce de la pieza que se ve en una fotografía de taller publicada en el catálogo de su primera exhibición en París, en 1948, Les Statues magiques de Maria, que incluía también otra imagen de taller con una gran versión en yeso, que hoy pertenece a Malba, al lado de However!! (1947), lo que nos hace suponer que la obra aquí presentada fue producida entre 1947 y 1948. \r\n\r\nLa muestra de París, que repitió muchas de las obras expuestas el año anterior en la galería neoyorquina Julien Levy, es la primera que registra más de un O impossível entre los trabajos exhibidos. \r\n\r\nEn la edición especial del catálogo preparado para la muestra de 1946, Maria había incluido nueve aguafuertes, cuatro de los cuales eran páginas del poema titulado Explication, en cuyo comienzo se lee: \r\n\r\n“Sé que mis Diosas y sé que mis Monstruos \r\nsiempre te parecerán sensuales y bárbaros”.	\N	594	\N	\N	1945	\N	\N	2025-09-19 20:05:51.441802-03	2025-09-19 20:11:12.295819-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0	42
195	Morro da favela II (Pueblito)	morro-da-favela-ii-pueblito	Morro da favela II (Pueblito)	\N	\N	126		\N	Oleo Sobre tela 39,5 x 49,9 cm.	\N	Época de temática popular, la década del 40 también sería para Tarsila, la gran pintora modernista de los años 20, tiempo de volver definitivamente de sus tonos pau Brasil, es decir, retornar a los colores llamados caipiras (azul y rosa) de la decoración popular de los modestos caseríos de las regiones del interior del Brasil. Este colorido, aunque más intenso, estuvo presente en sus telas de los años 20, su período de máxima creatividad y producción, etapa de una gran síntesis de sus elementos compositivos. Después de 1930, cuando Tarsila se orientó, por razones culturales y políticas, hacia la temática social, su paleta se alteró igualmente.\\nPor esa época la artista regresó a San Pablo después de alternar varios años entre esa ciudad y Río de Janeiro, y pasó gran parte de su tiempo en su hacienda de Indaiatuba, en el interior paulista. \\nConociendo la inexistencia de un mercado de arte local, realizó encargos de pinturas, ilustraciones y colaboró con crónicas regulares sobre la vida cultural y memorias en Diário de São Paulo.\\nEn Pueblito, de 1945, encontramos una segunda versión de la famosa pintura Morro da favela (1924, colección João Estefano, San Pablo), tela tan admirada por el poeta Blaise Cendrars que le recomendó, en su momento, que preparase su primera exposición individual en París a partir de la serie que se iniciara con esta obra. La temática está inspirada en el viaje a Río con los modernistas de San Pablo acompañando a Cendrars en su primera visita al Brasil.\\nAlgunas variantes, sin embargo, surgen en esta tela, ahora en la colección del MNBA: sus dimensiones son menores que las de Morro da favela, la vegetación tiene modificaciones, dejando de ser visualmente presentada en formas sintéticas, en esta tela de 1945 aparecen como matas de plantas más desmenuzadas, más diversificadas en su ubicación, especialmente en el primer plano. Por otra parte, las pinceladas son bien evidentes, casi “impresionistas” en su aplicación sobre la tela, y el ave extraña de 1924 es reemplazada por la confrontación de dos pavos. Finalmente, el color es más suave en comparación con la pintura de veinte años antes.\\nLa obra del MNBA estuvo expuesta en Buenos Aires, La Plata y Montevideo en la exposición Veinte artistas brasileños presentada en 1945, con la organización del escritor Marques Rebelo y la intermediación efectiva de Emilio Pettoruti.\\nEn esta muestra Tarsila estuvo representada por otra pintura, Pueblito II (1944), y por un dibujo, estudio de 16 x 22 cm de la pintura de la colección del MNBA, hoy aparentemente perdido. La exposición dio lugar a dos pequeños libros, actualmente antológicos, uno de autoría de Cipriano Vitureira, de Montevideo, y el segundo de Jorge Romero Brest, La pintura brasileña contemporánea, publicado el mismo año de la exposición por la editorial Poseidón.	\N	\N	\N	\N	1945	\N	135	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	Época de temática popular, la década del 40 también sería para Tarsila, la gran pintora modernista de los años 20, tiempo de volver definitivamente de sus tonos pau Brasil, es decir, retornar a los colores llamados caipiras (azul y rosa) de la decoración popular de los modestos caseríos de las regiones del interior del Brasil. Este colorido, aunque más intenso, estuvo presente en sus telas de los años 20, su período de máxima creatividad y producción, etapa de una gran síntesis de sus elementos compositivos. Después de 1930, cuando Ta	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	64
582	The Disasters of Mysticism	new	\N	\N	\N	\N	\N	\N	Técnica: Óleo sobre tela\r\n97.5 × 130,5 cm\r\nNro. de inventario: 2003.37\r\nDonación: Eduardo F. Costantini	\N	The Disasters of Mysticism es una obra realizada por Matta en 1942 en Nueva York, ciudad en la que residía desde 1939. Allí formó parte del nutrido grupo de artistas que habían huido de la guerra en Europa. Entre todos, se destacaba la presencia de los surrealistas, no solo porque allí estaba la plana mayor del movimiento (André Breton, Max Ernst, Marcel Duchamp, Yves Tanguy, Man Ray, André Masson y otros), sino porque el surrealismo era el movimiento más pujante y dinámico de las vanguardias europeas en el momento de comenzar la guerra.\r\n\r\nMatta pinta The Disasters of Mysticism en un año par- ticularmente importante para los surrealistas. En marzo de 1942, en la Pierre Matisse Gallery, se realiza la exposición Artists in Exile.1 En octubre se inaugura la exhibición que haría conocer el surrealismo en Nueva York, First Papers of Surrealism,2 en la Whitelaw Reid Mansion. En ambas está presente Matta. Además, también en marzo de ese año, el artista lleva a cabo una muestra individual, Matta: Paintings and Oil Pencils, en la Pierre Matisse Gallery. The Disasters of Mysticism, además, se ubica en un momento especialmente significativo tanto en la obra pictórica como en los desarrollos intelectuales de Matta. Como pintura, forma parte de un grupo de obras, que pueden situarse entre 1942 y 1944, en las que el espacio se presenta con fondos oscuros y formas luminosas, como fosforescentes.3 Seguimos en el mundo de los paisajes surrealistas inaugurado con las morphologies psychologiques y los inscapes, pero ahora, con esta particularidad cromática, este espacio parece sumergirnos en la inmensidad del espacio cósmico o en las penumbras de los fondos marinos.	\N	583	\N	\N	1942	\N	\N	2025-09-19 10:04:52.094595-03	2025-09-21 19:55:49.419279-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0	44
223	La Toilette apres le bain (El arreglo después del baño)	la-toilette-apres-le-bain-el-arreglo-despu-s-del-ba-o	La Toilette apres le bain (El arreglo después del baño)	\N	\N	126		\N	66,5 x 78,5 cm.	\N		\N	318	\N	\N	1888	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100		\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	66
215	Vahine no te miti (Femme a la mer) (Mujer del mar).	vahine-no-te-miti-femme-a-la-mer-mujer-del-mar	Vahine no te miti (Femme a la mer) (Mujer del mar).	\N	\N	126		\N	Oleo Sobre tela:92,5 x 74 cm.	\N	El MNBA tiene la suerte de poseer dos pinturas de bañistas de Gauguin, ambientada en este caso no en Francia, sino en los mares del Sur. Esta obra aparece mencionada como Estudio de espalda desnuda en una lista de pinturas realizadas durante la primera estadía en Tahití que Gauguin anotó aproximadamente en abril de 1892 en su Carnet de Tahiti (1). Fue pintada en Mataiea y se basa en un dibujo en color del mismo cuaderno de bocetos, probablemente realizado con modelo y con una cuadrícula apenas visible, útil al momento de repetir la composición en mayor escala.Cuando fue expuesta en París en 1893, Thadée Natanson describió concisamente su tema: “de esta, sentada en la arena, solamente se puede ver la espalda bronceada en medio de flores casi simétricas que la espuma borda sobre las olas” (2). La metáfora que vincula la cresta de las olas, las flores y el bordado destaca acertadamente el uso que hace Gauguin de formas polisémicas y la calidad ornamental de este motivo. Las “flores” de espuma también están relacionadas por su forma y su color con la conchilla que está en la playa y con las flores que forman parte del motivo del pareo sobre la rodilla derecha de la mujer. En contraste con otras pinturas de Gauguin que muestran una mujer tahitiana parcialmente vestida, este pareo no tiene ni espesor ni pliegues propios y está pintado sobre la pierna a la manera de los paños en una pintura manierista o de un tatuaje –realmente un “bordado” sobre la piel y la vestimenta original de muchos habitantes de los mares del Sur–. La cresta de las olas similar a una flor ya había sido utilizada por Gauguin en Bretaña (véase, por ejemplo, La playa en Le Pouldu, 1889, colección privada, Buenos Aires) y deriva del arte japonés a través de los grabados de Hiroshige.La descripción que hace Gauguin de la pintura como un Estudio de espalda desnuda confirma que el elemento esencial es el dorso de la figura. Varios autores han señalado que en el libro de 1876 La nouvelle peinture, Edmond Duranty había desafiado a los artistas a presentar figuras de espalda caracterizando su edad, su condición social y su estado psicológico (3). Degas respondió al reto, de manera muy notable sobre todo en sus pasteles de mujeres aseándose, algunas de las cuales están copiadas en una página del Album Briant de Gauguin, 1888-1889 (Museo del Louvre, París). La vista de atrás, con un énfasis en las nalgas, obviamente lo atraía y se encuentra también en la pintura Otahi, 1893 (colección privada). En Vahine no te miti, la posición incómoda de los brazos y las piernas confiere a la espalda un carácter fragmentario con reminiscencias de la escultura antigua, trasladando a esta la expresividad generalmente atribuida al rostro humano.En 1948, Raquel Edelman halló monumentalidad en esta pintura lograda a expensas de la individualidad y la sensualidad, y consideró que la misma probaba la intención de Gauguin de “dominar y sublimar su erotismo” (4). No obstante, la obra se remite a una mujer muy específica, cuya anatomía expresa un carácter –podría decirse incluso una fisonomía–. En lo que al erotismo de Gauguin respecta, es típicamente sugestivo e indirecto. Ronald Pickvance señaló que la cresta de las olas y el motivo de las flores en el pareo son “como amebas y están animados por una vitalidad orgánica” (5).Si agregamos la conchilla en el rincón, oculta en parte por la caprichosa forma de flores rojas, es evidente que el cuerpo autocontenido y semejante a un fruto de la bañista está rodeado por un ballet de criaturas animadas. Las flores rojas, como el fondo amarillo, reaparecen en Parahi te marae, 1892 (Philadelphia Museum of Art), donde las plantas imitan la sexualidad humana (6). En la pintura de Buenos Aires, la hoja grande que da lugar al título está dividida como una mano en extremidades similares a dedos y apunta hacia el trasero de la mujer (7). A Gauguin le encantaban las sugerencias anatómicas de las frutas y las flores que encontraba en Tahití, las que con frecuencia habían pasado al lenguaje y la mitología locales, y es probable que aquí haya aludido además a la semejanza entre las nalgas femeninas y la nuez llamada coco de mer, “coco de mar”, cuyo nombre científico había sido al principio Lodoicea callypige en razón, precisamente, de esta analogía (8).El título que le puso al cuadro se traduce de hecho como “mujer del mar” y está basado en la misma fórmula que empleó para Vahine no te tiare, “mujer de la flor”, 1891 (Ny Carlsberg Glyptotek, Copenhague) y Vahine no te vi, “mujer del mango”, 1892 (Baltimore Museum of Art). Hablando de esta última, Hiriata Millaud ha observado que vahine –contrariamente a hine– significa una mujer que ya tiene una vida social, y que el atributo introducido por no (“para” o “de”) puede definir el carácter de la mujer en cuestión antes que servir simplemente como una función descriptiva y mnemónica (9). La mujer en Vahine no te miti dirige su mirada y su oído hacia el océano, más específicamente hacia el mar abierto que aparece entre dos rocas o islas. Igual que las figuras de David Friedrich vistas de espalda, ella actúa por lo tanto como mediadora entre la naturaleza y el espectador, y parece realmente ser “del mar”, compenetrada con él, como una Venus tahitiana que es a la vez Anadiomene y Calipigia.\r\nDario Gamboni\r\n1— Véase: Bernard Dorival, Carnet de Tahiti. Paris, Quatre Chemins-Editart, 1954; Carnet de Tahiti. Taravao, Avant et Après, 2001. 2— Thadée Natanson, “Oeuvres récentes de Paul Gauguin”, La revue blanche, diciembre de 1893, citado en: Marla Prather y Charles F. Stuckey (ed.), 1987, p. 225. 3— Véase la entrada de Charles F. Stuckey en: Richard Brettell et al., The Art of Paul Gauguin, cat. exp. Washington, National Gallery of Art, 1988. Versión francesa Gauguin, cat. exp. Paris, Réunion des musées nationaux, 1989, nº 144.4— Raquel Edelman, 1948, p. 73-79. 5— Ronald Pickvance, Gauguin, cat. exp. Martigny, Fondation Pierre Gianadda, 1998, nº 32. 6— Dario Gamboni, “Parahi te marae: où est le temple?”, 48/14: La revue du Musée d’Orsay, Paris, nº 20, 2005, p. 6-17. 7— Véase la planta más explícitamente antropomórfica que sostiene a una pareja copulando en el manuscrito Album ancien culte mahorie (1892, Musée d’Orsay, París, RF 10755, folio 46). 8— Gauguin posteriormente grabó toda la superficie de una de esas nueces (Coco de mer, ca. 1901-1903, Albright-Knox Art Gallery, Buffalo). 9— Hiriata Millaud, “Les titres tahitiens de Gauguin” en: Ia Orana Gauguin, cat. exp. Paris, Somogy, 2003, p. 81-90.\r\n1893. NATANSON, Thadée, “Oeuvres récentes de Paul Gauguin”, La revue blanche, Paris, diciembre. 1936. Plástica, Buenos Aires, a. 2, nº 5, abril, reprod. byn p. 10. 1948. EDELMAN, Raquel, “Gauguin en Buenos Aires”, Ver y Estimar, Buenos Aires, vol. 2, nº 7-8, octubre-noviembre, p. 73-79, reprod. p. 75. 1964. WILDENSTEIN, Georges, Gauguin. Paris, Fondation Wildenstein, vol. 1, nº 465. 1977. FIELD, Richard S., Paul Gauguin: The Paintings of the First Trip to Tahiti. Tesis de doctorado, Harvard University [1963]. New York/London, Garland, nº 21.1987. PRATHER, Marla y Charles F. Stuckey, Gauguin: A retrospective. New York, Hugh Lauter Levin Associates, p. 224-226, reprod. nº 64. 1990. HADDAD, Michèle, La divine et l’impure: le nu au XIXe. Paris, Les Éditions du Jaguar, p. 49-50, reprod. color p. 51. 1993. 1893: L’Europe des peintres, cat. exp. Paris, Réunion des musées nationaux, p. 20.	\N	331	\N	\N	1892	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	El MNBA tiene la suerte de poseer dos pinturas de bañistas de Gauguin, ambientada en este caso no en Francia, sino en los mares del Sur. Esta obra aparece mencionada como Estudio de espalda desnuda en una lista de pinturas realizadas durante la primera estadía en Tahití que Gauguin anotó aproximadamente en abril de 1892 en su Carnet de Tahiti (1). Fue pintada en Mataiea y se basa en un dibujo en color del mismo cuaderno de bocetos, probablemente realizado con modelo y con una cuadrícula apenas visible, útil al momento de repetir la co	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	63
213	En observation - M.Fabre, Officier de reserve	en-observation-m-fabre-officier-de-reserve	En observation - M.Fabre, Officier de reserve	\N	\N	126		\N	Oleo Sobre tela: 60,5 x 49,5 cm. - Marco: 76,2 x 66,2 cm.	\N		\N	329	\N	\N	1891	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100		\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	65
274	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	Sin pan y sin trabajo	\N	\N	126		\N	Oleo Sobre tela 125,5 x 216 cm.	\N	Sin pan y sin trabajo es el primer cuadro de tema obrero con intención de crítica social en el arte argentino. Desde el momento de su exhibición ha sido una pieza emblemática del arte nacional: comentado, reproducido, citado y reapropiado por sucesivas generaciones de artistas, historiadores y críticos hasta la actualidad. Fue pintado por Ernesto de la Cárcova en Buenos Aires al regreso de su viaje de estudios en Turín y Roma, donde había comenzado su ejecución antes de partir. Allí dejó al menos un boceto en obsequio a Pío Collivadino, el artista argentino que a su llegada ocupó el taller que De la Cárcova dejaba en la Via del Corso 12. \\n Había comenzado su formación europea en la Real Academia de Turín, donde fue admitido con una obra (Crisantemos) en la exposición de 1890. Luego había pasado a Roma, donde continuó su formación en los talleres de Antonio Mancini y Giacomo Grosso. Una obra suya (Cabeza de viejo) fue premiada con medalla de plata y adquirida en 1892 para la Galería Real de Turín; también obtuvo una medalla de oro en Milán en 1893 (1). Estos antecedentes hicieron que a su regreso, a los 28 años, fuera miembro del jurado del Ateneo, de modo que Sin pan y sin trabajo, celebrado como el gran acontecimiento artístico del Salón, quedó fuera de concurso. \\n El cuadro responde a un estilo naturalista y a una temática que tuvieron una importante presencia en los salones europeos de los años finales del siglo XIX: grandes pinturas resueltas en tonos sombríos que desplegaban escenas dramáticas de miseria y de los contemporáneos conflictos sociales urbanos. El espíritu crítico que sin duda alimentó aquellas composiciones naturalistas finiseculares se diluyó en los cuadros de salón, en el interés por figurar en grandes competencias con posiciones enfrentadas al arte académico más conservador. Sin embargo, Sin pan y sin trabajo no fue pintado para competir en un salón europeo: fue la obra con la que De la Cárcova se presentó al regreso en el segundo Salón del Ateneo en Buenos Aires, tras haberse afiliado al recién creado Centro Obrero Socialista (antecedente inmediato del Partido Socialista, fundado dos años después). \\n No había en Buenos Aires una tradición académica sino que el grupo de artistas del Ateneo procuraba dar sus primeros pasos. Por otra parte, a partir de la crisis de 1890, la inmensa afluencia de inmigrantes europeos que llegaban de Europa en busca de trabajo en Buenos Aires comenzaba a percibirse en forma conflictiva.	\N	306	\N	\N	1894	\N	135	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	Sin pan y sin trabajo es el primer cuadro de tema obrero con intención de crítica social en el arte argentino. Desde el momento de su exhibición ha sido una pieza emblemática del arte nacional: comentado, reproducido, citado y reapropiado por sucesivas generaciones de artistas, historiadores y críticos hasta la actualidad. Fue pintado por Ernesto de la Cárcova en Buenos Aires al regreso de su viaje de estudios en Turín y Roma, donde había comenzado su ejecución antes de partir. Allí dejó al menos un boceto en obsequio a Pío Collivadin	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	67
231	La Nymphe surprise (La ninfa sorprendida)	la-nymphe-surprise-la-ninfa-sorprendida	La Nymphe surprise (La ninfa sorprendida)	\N	\N	126		\N	Oleo Sobre tela 144,5 x 112,5 cm. Marco: 169,8 x 137,5 x 9,5 cm.	\N	La nymphe surprise inaugura un período clave de la trayectoria de Édouard Manet y de la historia del modernismo en la pintura francesa (1). Según Barskaya, fue terminada y enviada por el artista a la exposición de la Academia de Arte de San Petersburgo en 1861 con el título Ninfa y sátiro, dos años antes de la exposición en el Salón de Rechazados de Le déjeuner sur l’herbe y de Olympia (pintada ese mismo año aunque enviada y aceptada en el Salon des Artistes Français en 1865, ambas en el Musée d’Orsay, París). Una larga serie de estudios documentales y técnicos, así como discusiones respecto de sus posibles fuentes, revelan un proceso largo y complejo en la elaboración de esta tela, considerada el primer gran tableau-laboratoire de Manet reelaborando modelos de la gran pintura italiana y holandesa de los siglos XVI y XVII. La obra permaneció en poder del artista hasta su muerte, y existe evidencia de que Manet la consideró uno de sus cuadros más importantes.	\N	317	\N	\N	1861	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	La nymphe surprise inaugura un período clave de la trayectoria de Édouard Manet y de la historia del modernismo en la pintura francesa (1). Según Barskaya, fue terminada y enviada por el artista a la exposición de la Academia de Arte de San Petersburgo en 1861 con el título Ninfa y sátiro, dos años antes de la exposición en el Salón de Rechazados de Le déjeuner sur l’herbe y de Olympia (pintada ese mismo año aunque enviada y aceptada en el Salon des Artistes Français en 1865, ambas en el Musée d’Orsay, París). Una larga serie de estud	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	68
619	Diván	new	\N	\N	\N	\N	\N	\N	Oleo sobre tela.	\N	Trabaja desde un posicionamiento crítico sobre la circulación y la reapropiación de las imágenes en la era contemporánea, donde las fuentes y referentes visuales se multiplican, se entremezclan y se transforman constantemente. En su pintura, la apropiación no es un fin en sí mismo, sino un medio para interrogar la memoria cultural, las iconografías compartidas y la relación afectiva que establecemos con las imágenes.	\N	620	\N	\N	2025	\N	\N	2025-09-24 11:32:30.482011-03	2025-09-24 11:34:01.615389-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0	45
332	Puente de La Boca	puente-de-la-boca	Puente de La Boca	\N	\N	\N	\N	\N	\N	\N	\N	\N	334	\N	\N	0	\N	135	2025-05-19 14:07:33.515503-03	2025-10-07 11:25:38.909122-03	100	\N	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	69
768	Sillón cinta	\N	\N	\N	\N	\N	\N	\N	\N	\N	Con curaduría de Sandra Hillar y Wustavo Quiroga, y producción de Satsch Gallery, el recorrido estará organizado en cinco salas temáticas que atraviesan distintos formatos, materiales y lenguajes trabajados por Churba.	\N	772	\N	\N	1990	\N	\N	2025-10-29 09:26:42.092857-03	2025-10-29 09:31:53.128206-03	100	\N	\N	753	t	\N	3	\N	es	\N	\N	es	\N	f	\N	\N	\N	0	40
209	Prairies du Valhermeil près Pontoise	prairies-du-valhermeil-pr-s-pontoise	Prairies du Valhermeil près Pontoise	\N	\N	126		\N	Oleo Sobre tela 55 x 92 cm.	\N	Donación Mercedes Santamarina, 1970.	\N	\N	\N	\N	1874	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	Donación Mercedes Santamarina, 1970.	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	70
482	Utopia del Sur	utopia-del-sur	Utopia del Sur	\N	\N	126		\N	Oleo sobre tela 180 x 190 cm.	\N	García Uriburu le dedicó vida y obra: una proclama urgente por el cuidado del medio ambiente, “La globalización ha ligado salvajemente nuestras economías, creando una cruel dependencia que ha dividido aún más a la población mundial. Los países desarrollados contaminan el agua con fluidos tóxicos, derraman petróleo en nuestros mares y ríos, sin reparar el daño que ocasionan. Hace más de cuarenta años que intento dar una alarma contra la contaminación de ríos y mares, y es a través de mis acciones artísticas en distintos puntos del planeta que he transformado mi obra en una suerte de alerta contestataria globalizadora. Hoy y con más motivos que hace cuarenta años, sigo denunciando la contaminación del agua, y la salvaje destrucción que hacemos de las reservas del planeta. Un planeta que en nuestra ciega omnipotencia creemos inagotable e indestructible”.	\N	490	\N	\N	1993	\N	135	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100	\N	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	71
207	Le Pont d`Argenteuil (El Puente de Argenteuil)	le-pont-d-argenteuil-el-puente-de-argenteuil	Le Pont d`Argenteuil (El Puente de Argenteuil)	\N	\N	126	Donación Mercedes Santamarina, 1970.	\N	Oleo Sobre tela 60 x 100 cm.	\N	Esta pintura plantea algunos interrogantes de orden iconográfico. Generalmente señalado con el título de Puente de Argenteuil, el cuadro es llamado de otra manera por Daniel Wildenstein, el autor del catálogo razonado de las obras de Claude Monet (1), que individualiza más bien en el paisaje representado al puente ferroviario de Chatou (2), visto desde la isla de Chiard. Esta propuesta sería más atendible. En efecto, la obra muestra un escorzo perspectivo, de abajo arriba, de un puente ferroviario de arcos amplios. Pero sucede que el puente de Argenteuil, de igual factura, es un puente vial (Le Pont d’Argenteuil, 1870, Musée d’Orsay, París); el puente ferroviario de la misma ciudad, en cambio, "no está formado por una serie de arcos redondeados sino por una sola arcada en forma de cavidad con compartimentos” que se apoya sobre “cuatro pares de cilindros alargados de cemento” (3), similares a columnas y sin arcos (Le pont du chemin de fer à Argenteuil, 1874, Musée d’Orsay, París). La isla de Chiard, conocida también como “isla de los impresionistas”, quienes la frecuentaron regularmente para pintar en plein air (4), se encuentra a orillas del Sena, en la periferia oeste de París. Toda la zona, constituida por numerosas localidades (Argenteuil, Asnières, Chatou) muy cercanas entre sí, era un destino muy frecuentado los domingos por los parisinos que iban allí a distenderse a la orilla del agua; esos lugares estaban, por lo demás, a solo media hora de la estación Saint-Lazare. Toda la zona, y en particular la isla de Chiard, albergaba, en la riqueza de su vegetación, numerosas viviendas de artistas. El propio Monet alquiló una casa en Argenteuil entre 1872 y 1877, justamente para estar lo más cerca posible de estos temas que tanto lo fascinaban. Verdaderas novedades sociológicas, estas localidades suscitaron el interés de toda la cultura francesa, desde la llamada “masa” hasta las corrientes más sofisticadas de la vanguardia artística y literaria. En sus representaciones de Argenteuil –aunque con ese nombre debe entenderse a menudo una zona mucho más amplia Monet denota cierta ambivalencia. En algunos cuadros ofrece una visión bucólica de la que están ausentes, o casi, la mayor parte de los símbolos de la modernidad, alineándose en ese sentido a la visión de la naturaleza cotidiana de Corot y Daubigny, alejada de lo pintoresco, casi intacta. En otras obras del mismo período, en cambio, los símbolos del progreso industrial alternan y se mezclan con los de la vida rural, como para subrayar una armonía posible entre naturaleza y modernidad. Las imágenes serenas de las regatas (Le bassin d’Argenteuil, 1872, Musée d’Orsay), a menudo representadas por el artista, atraído por los juegos de agua y de luz, alternan asimismo con las arquitecturas metálicas de los puentes modernos, que después de las destrucciones de la guerra franco-prusiana estaban redefiniendo nuevamente el paisaje. El tema de los ferrocarriles había entrado en el repertorio de Monet a partir de 1874, cuando el artista pintó numerosas vistas del Sena y de los puentes que lo atraviesan fuera de París. Entre 1850 y 1870, Francia conoció un gran desarrollo industrial respaldado por la construcción de importantes infraestructuras (ferrocarriles, estaciones, puentes, viaductos) que se insertaron repentinamente en el paisaje urbano y rural, sobre todo alrededor de la capital. En 1871, 16.000 km de red ferroviaria atravesaban ya todo el territorio francés y la pintura no fue indiferente a este hecho. Monet compartió su interés por los motivos ferroviarios con varios precursores de la pintura de tema moderno, como el alemán Adolf Menzel y el inglés William Turner, de quien había visto en Londres la pintura Rain, Steam and Speed durante su estadía entre 1870 y 1871. Los puentes de Monet, al igual que las sucesivas estaciones realizadas en 1877 (en particular la de Saint-Lazare), no constituyen una “serie” en sentido estricto, sino más bien una “secuencia”, según la definición de Grace Seiberling desarrollada en torno de la misma temática. Comparadas con las famosas series de los años 90 (La catedral de Rouen, por ejemplo), estas pinturas no presentan ni una concepción unitaria ni un trabajo de armonización general efectuado en el taller. Como el puente de Argenteuil, también el de Chatou había atraído la atención de numerosos artistas, que lo representaron desde distintos puntos de vista (Pierre-Auguste Renoir, Pont de chemin de fer à Chatou ou Les marronniers roses, 1881, Musée d’Orsay). El cuadro de Buenos Aires constituye un paisaje “puro”, vaciado de la presencia humana, enteramente concentrado en la reacción rápida del artista frente a la naturaleza, traducida aquí en un follaje denso y vibrante, completamente desarrollado en tonalidades frías (azul, verde, amarillo). El punto de vista de la obra –rebajado a un nivel coincidente con la rica vegetación– desde el cual se distingue el puente ferroviario atravesado por un tren en marcha, parece querer subrayar la idea de una modernidad aparecida al artista en forma imprevista y casi por casualidad. Esta obra proviene de la rica colección impresionista de Mercedes Santamarina. El MNBA posee otra obra de Monet, La berge de la Seine (1880), proveniente de la Exposición Internacional de Arte del Centenario, de 1910.\r\n  Barbara Musetti\r\n1— Entre las biografías más recientes, véase: M. Alphant, Claude Monet. Une vie dans le paysage. Paris, Hazan, 1993; C. F. Stuckey, Monet. Un peintre, une vie, une oeuvre. Paris, Belfond, 1992. 2— D. Wildenstein, 1974, p. 1875. 3— H. P. Tucker, Monet at Argenteuil. New Haven/London, Yale University Press, 1982, p. 70. 4— Señalemos dos catálogos de exposición dedicados al paisaje impresionista: L’Impressionnisme et le paysage français, cat. exp. Paris, Réunion des musées nationaux, 1985; Landscapes of France. Impressionism and its Rivals, cat. exp. London/Boston, Hayward Gallery/ Museum of Fine Arts, 1995.\r\n1964. NIEULESEU, R., “G. Di Bellio, l’ami des impressionnistes”, Revue Roumaine d’Histoire de l’Art, Bucarest, t. 1, nº 2, p. 222, 278. 1974. WILDENSTEIN, Daniel, Claude Monet. Biographie et catalogue raisonné. Lausanne/ Paris, Bibliothèque des Arts, t. 1, nº 367, reprod. p. 1875.	\N	334	\N	\N	1875	\N	135	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	Esta pintura plantea algunos interrogantes de orden iconográfico. Generalmente señalado con el título de Puente de Argenteuil, el cuadro es llamado de otra manera por Daniel Wildenstein, el autor del catálogo razonado de las obras de Claude Monet (1), que individualiza más bien en el paisaje representado al puente ferroviario de Chatou (2), visto desde la isla de Chiard. Esta propuesta sería más atendible. En efecto, la obra muestra un escorzo perspectivo, de abajo arriba, de un puente ferroviario de arcos amplios. Pero sucede que el 	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	74
324	Portrait d'Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	portrait-d-ernest-hosched-et-sa-fille-marthe-retrato-del-se-or-hosched-y-su-hija	Portrait d'Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	\N	\N	\N	\N	\N	\N	\N	\N	\N	325	\N	\N	0	\N	135	2025-05-19 14:07:26.434656-03	2025-05-19 14:07:26.434656-03	100	\N	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	75
272	En Normandie	en-normandie	En Normandie	\N	\N	126	Colección particular. Cedida en comodato al Museo Nacional de Bellas Artes, 2021.	\N	Oleo Sobre tela 162 x 207 cm.	\N	Este óleo sobre tela de gran formato fue el envío de Obligado, por entonces radicada en Francia, al salón parisino de 1902. En ella, las figuras femeninas que dominan la composición realizan redes de pesca en el norte de la costa francesa, una actividad de vital importancia para la economía regional.\\n El cuadro presenta a un grupo de mujeres entregadas a una tarea minuciosa y sistemática. Igual de laborioso fue el trabajo de la artista para crear esta escena: ejecutó detallados estudios de cada figura y de la composición en general, a la que consideraba “el alma de toda pintura”.\r\n\r\nPara conocer más sobre esta obra, invitamos a leer el ensayo de la historiadora María Lía Munilla Lacasa, publicado en el micrositio dedicado a la exposición  curada por Georgina Gluzman, que traza un paralelo entre la tela de Obligado y la emblemática “Sin pan y sin trabajo”, de De la Cárcova, y celebra esta reparación histórica hacia la obra de una de las grandes artistas del período en el país.\\n “El mundo del trabajo –o de su ausencia– es el tema que ambos cuadros abordan. \r\n\r\nMientras que en la obra de María Obligado seis trabajadoras de la costa de Normandía, al norte de Francia, ensimisman su rutina en la tarea de tejer redes de pesca, en el óleo de Ernesto de la Cárcova se acentúa con particular dramatismo un topos pictórico europeo característico de la segunda mitad del siglo XIX: el de la pobreza urbana”, explica Munilla Lacasa.	\N	303	\N	\N	1902	\N	135	2025-05-19 12:30:21.186648-03	2025-10-29 08:52:06.061462-03	100	Este óleo sobre tela de gran formato fue el envío de Obligado, por entonces radicada en Francia, al salón parisino de 1902. En ella, las figuras femeninas que dominan la composición realizan redes de pesca en el norte de la costa francesa, una actividad de vital importancia para la economía regional.\\n El cuadro presenta a un grupo de mujeres entregadas a una tarea minuciosa y sistemática. Igual de laborioso fue el trabajo de la artista para crear esta escena: ejecutó detallados estudios de cada figura y de la composición en general, 	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	72
282	Abel	abel	Abel	\N	\N	128		\N	Bronce 47 x 198 x 98 cm.	\N	Lucio Correa Morales, formado en Florencia con Urbano Lucchesi, realizó una vasta producción de retratos, esculturas funerarias, monumentos conmemorativos y obras decorativas, además de dedicarse a la enseñanza de la escultura en la Sociedad Estímulo de Bellas Artes, origen de la Academia en la Argentina.\r\nEn 1902 realizó un boceto de Abel en terracota y un segundo en bronce, conservados ambos en colecciones privadas, con la figura aislada del joven sin vida asesinado por su hermano Caín. En mayo de 1903 fue asociado por un crítico de La Nación con la escultura Abel del italiano Giovanni Duprè, un mármol de 1842, a pesar de las diferencias compositivas.\r\n\r\nLas expresiones del mismo Correa Morales nos revelan las verdaderas intenciones de su trabajo, pues era consciente de que “Nadie comprendió ni tal vez se supuso que ese ‘Abel’ no era el que fue muerto por Caín, sino el arte argentino muerto por sus hermanos. En esa figura hasta mi nombre le había disfrazado; […] yo gustoso habría firmado Moralaine, Moralini ó Moralai”. Este comentario permite interpretar la frase como alusión a la falta de desarrollo y de interés por el arte nacional: “Muerto sin descendencia como miserable / Es fácil sacar la conclusión / Que nosotros somos toda raza de Caín”.\r\n\r\nLa versión en yeso de Abel obtuvo la medalla de plata de Escultura en la Exposición Internacional de Saint Louis en 1904. Schiaffino, responsable del envío argentino, había reclamado al jurado una medalla de oro para Lucio Correa Morales pero su propuesta fue rechazada, al ser criticada la proporción inadecuada de los brazos de la figura (2). El reconocimiento internacional estimuló la posibilidad de su fundición en bronce, junto con otras obras premiadas como Las pecadoras de Rogelio Yrurtia.	\N	300	\N	\N	1902	\N	135	2025-05-19 12:30:21.186648-03	2025-10-25 15:00:20.715353-03	100	Lucio Correa Morales, formado en Florencia con Urbano Lucchesi, realizó una vasta producción de retratos, esculturas funerarias, monumentos conmemorativos y obras decorativas, además de dedicarse a la enseñanza de la escultura en la Sociedad Estímulo de Bellas Artes, origen de la Academia en la Argentina.\r\nEn 1902 realizó un boceto de Abel en terracota y un segundo en bronce, conservados ambos en colecciones privadas, con la figura aislada del joven sin vida asesinado por su hermano Caín. En mayo de 1903 fue asociado por un crítico de	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	73
452	El recurso del método	el-recurso-del-m-todo	El recurso del método	\N	\N	126		\N	Tinta sobre papel 30 x 40 cm.	\N	El recurso del método es una novela del escritor cubano Alejo Carpentier.  \r\nPublicada en 1974, pertenece al subgénero de la literatura hispanoamericana conocido como novela del dictador.	\N	464	\N	\N	2012	\N	135	2025-07-25 18:47:09.47387-03	2025-10-07 17:14:38.558215-03	100	\N	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	76
205	Effet de neige à Louveciennes	effet-de-neige-louveciennes	Effet de neige à Louveciennes	\N	\N	126		\N	Oleo Sobre tela 50 x 61,5 cm.	\N	Donación Mercedes Santamarina, 1960.	\N	327	\N	\N	1877	\N	135	2025-05-19 12:29:43.192411-03	2025-10-07 17:07:26.573077-03	100	Donación Mercedes Santamarina, 1960.	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	77
40219	Vara de mando de Manuel Mansilla	\N	\N	\N	\N	\N	\N	\N	Madera carey, oro y bronce facetado.\r\nNro. de inventario 1248.	\N	La vara es un símbolo de poder que distinguía a las autoridades del Cabildo cuando ejercían funciones en la ciudad.\r\nEsta perteneció a Manuel Mansilla, aguacil mayor del Cabildo entre 1975 y 1821. Como Mansilla ejercía un cargo perpetuo, la pieza lleva en la empuñadura un monograma con sus iniciales.	\N	40298	\N	\N	0	\N	\N	2026-01-13 15:08:24.683212-03	2026-01-13 15:08:24.683226-03	100	\N	\N	537	t	\N	1	\N	es	\N	\N	es	\N	t	\N	Original del siglo XIX	Siglo XVIII y XIX	1	4
884	La jungla	\N	\N	\N	\N	\N	\N	\N	Gouache, Papel y Lienzo\r\nTécnica\tÓleo sobre papel kraft\r\nDimensiones: 2.84 x 2.92 mts	\N	La Jungla es un cuadro al óleo, pintado por Wifredo Lam en 1943. Se exhibe en el MoMA, Museo de Arte Moderno de Nueva York, EUA. Este cuadro ha sido interpretado como la síntesis de un ciclo antillano, en virtud del espacio barroco dominante y de la atmósfera creada por la asociación de lo humano, lo animal, lo vegetal y lo divino. Hay en él un vocabulario visual que evolucionó desde el paisaje de corte académico hacia un tema y un lenguaje de arte moderno. En este óleo parecen fusionarse visiones y vivencias del pintor; el mítico paisaje insular, la incorporación de contenidos e iconografías procedentes de los sistemas mágico-religiosos de origen africano extendidos en Cuba y el Caribe, conjugándose en toda su definición.	\N	889	\N	\N	1943	\N	\N	2025-11-29 07:38:16.171156-03	2026-01-04 17:58:09.058955-03	100	\N	\N	860	t	\N	3	\N	..	\N	\N	es	\N	f	\N	\N	\N	0	6
50556	Costurero	\N	\N	\N	\N	\N	\N	\N	Paja de trigo, chala teñida y flor de doca.\r\nQuilino, Córdoba, 1974.\r\nArtesana: L. M. de Jaime.\r\nAlto 28 cm × largo 23 cm × ancho 12 cm.	\N		\N	50563	\N	\N	1974	\N	\N	2026-02-12 16:11:21.410566-03	2026-02-12 16:13:55.093954-03	100	\N	\N	50393	t	\N	3	\N	es	\N	\N	es	\N	t	\N	Artesana: L. M. de Jaime	\N	1	2
42814	Museo MACA	\N	\N	\N	\N	\N	\N	\N	\N	\N	Es la primera estructura espacial compleja que se diseña en Uruguay utilizando elementos de madera laminada encolada de sección variable y de doble curvatura. Tiene una serie de pórticos elaborados con maderas nacionales de eucalyptus grandis. Estas estructuras requieren de un tipo de enfoque de modelado y de un diseño estructural que es distinto al tradicional.\r\n\r\n el MACA es un espectacular proyecto de curvas variables que encontró en la madera laminada encolada el aliado perfecto para resolver, de un modo natural y elegante, el complejo diseño estructural.\r\n\r\nLa estructura fue diseñada y calculada por un estudio de ingenieros uruguayos y españoles especialistas en diseño, cálculo y construcción con madera. Se genera, de esa forma, una estructura con un espacio, concebido por el Arq. Carlos Ott, muy amplio, muy agradable, porque todas las piezas de este pórtico quedan a la vista.	\N	42893	\N	\N	0	\N	\N	2026-01-21 09:48:23.149415-03	2026-01-21 09:48:23.149423-03	100	\N	\N	42268	t	\N	1	\N	es	\N	\N	es	\N	t	\N	Arquitecto Ott	\N	2	3
211	Femme aux champs (Campesina)	femme-aux-champs-campesina	Femme aux champs (Campesina)	\N	\N	126		\N	Oleo Sobre tela 88 x 48 cm.	\N	Relacionado con sus ideas socialistas y cercanas al anarquismo, se interesó por plasmar el trabajo del campesino y pintó la vida rural francesa.\r\nGalerie Georges Petit.	\N	576	\N	\N	1882	\N	135	2025-05-19 12:29:43.192411-03	2025-09-19 00:09:27.407699-03	100	Galerie Georges Petit.	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	79
50571	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	\N	\N	2026-02-12 17:43:04.135894-03	2026-02-12 17:43:04.13591-03	100	\N	\N	137	t	\N	1	\N	..	\N	\N	es	\N	t	\N	\N	\N	0	80
50578	Crucifijo	\N	\N	\N	\N	\N	\N	\N	Madera, yeso, pintura y cabello natural.\r\nSalta, 1970.\r\nAlto 65 cm × largo 18 cm × ancho 18 cm.	\N	\N	\N	50585	\N	\N	1970	\N	\N	2026-02-12 17:56:15.014401-03	2026-02-12 17:56:15.014406-03	100	\N	\N	50393	t	\N	3	\N	es	\N	\N	es	\N	t	\N	Artesano: Andrés Arancibia	\N	4	3
12377	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	\N	\N	\N	\N	Oil on canvas. 60 × 50" (152.4 × 127 cm).	\N	Las religiones afrocubanas incluyen el Lucumí, o Santería, del pueblo de habla yoruba del suroeste de Nigeria, que es el culto a los orishas o espíritus. También tenemos el Palo, de África Central, las prácticas de Benín llamadas Arará, así como ideas europeas o cristianas.\r\n\r\nEl título de esta pintura, Malembo Sombrío, Dios de la Encrucijada, hace referencia al orisha de la tradición lucumí o santería.\r\nLa palabra "Malembo" en el título podría referirse a un puerto negrero en la costa occidental de África. \r\nPara cuando Lam nació en 1902, solo habían pasado 16 años desde la abolición de la esclavitud en Cuba.\r\n\r\nEn gran parte de la obra de Lam se observa lo que podría identificarse como una figura similar a la de Eleguá: esta pequeña cabeza abovedada. Eleguá es el orisha de la encrucijada. Tiene la capacidad única de abrir puertas, comunicarse entre idiomas y atraer la buena fortuna y alejar la mala suerte.	\N	12516	\N	\N	1943	\N	\N	2025-12-26 16:07:57.143085-03	2026-01-04 17:58:22.333557-03	100	\N	\N	860	t	\N	3	\N	..	\N	\N	es	\N	t	\N	\N	\N	0	7
402	La Emperatriz Theodora	la-emperatriz-theodora	La Emperatriz Theodora	\N	\N	126		\N	Oleo sobre tela 224,5 x 125,5 cm. - Marco: 226,5 x 127,5 cm.	\N	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, representa una visión orientalista y teatral de la emperatriz bizantina Teodora, esposa de Justiniano I,  la mujer más influyente y poderosa en la historia del Imperio romano de Oriente.\r\n\r\nNo es una representación histórica, sino más bien una fantasía opulenta basada en la fascinación europea del siglo XIX por el Oriente y el pasado imperial. Benjamin-Constant era un pintor de gran técnica especializado en retratos de élite y figuras históricas: poder, lujo,  sofisticación, poses calculadas,  texturas complejas como seda y terciopelo, y entornos refinados. \r\n\r\nDe origen humilde, Theodora se ganó la vida de joven como actriz y cortesana hasta que fue elegida por Justiniano como esposa. La imagen histórica -basada en Procopio de Cesarea en la antigüedad  y Gibbon en el siglo XVIII-  es  la de una mujer astuta, inteligente y cruel. En la actualidad se considera que ese perfil es exagerado por la visión rígida que se tenía de la función social de la mujer. La visión moderna es que fue una figura política central en el Imperio Bizantino,  co-gobernante de facto con Justiniano. Participó activamente en las decisiones de gobierno. En la revuelta de Niká de 532, convenció a Justiniano de no huir y enfrentar a sus enemigos con su famosa frase -“el púrpura es una buena mortaja”-. Promovió leyes en favor de los derechos de las mujeres: prohibió el tráfico sexual forzado,  fortaleció los derechos de las mujeres en el matrimonio y el divorcio, y fundó hogares para mujeres rescatadas de la prostitución, algo sin precedentes en la época. Como resultado de los esfuerzos de Teodora, el estado de la mujer en el Imperio Bizantino fue más elevado que el del resto de las mujeres en Europa.\r\n\r\nEn la historia argentina el paralelismo inmediato es con Eva Perón.\r\n\r\nBenjamin-Constant se propuso crear un ícono visual del poder femenino y del exotismo oriental. y lo que salió, quizás involuntariamente,  es todo un símbolo de modernidad: una mujer joven, poderosa, hermosa e inteligente con mirada desafiante que se atreve a ser distinta.	\N	417	\N	\N	1887	\N	135	2025-06-10 15:14:26.074639-03	2025-10-07 14:57:44.176318-03	100	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, representa una visión orientalista y teatral de la emperatriz bizantina Teodora, esposa de Justiniano I,  la mujer más influyente y poderosa en la historia del Imperio romano de Oriente.  .	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	78
43114	MACA Pabellon 2	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	43193	\N	\N	0	\N	\N	2026-01-21 10:00:06.423694-03	2026-01-21 10:00:06.423706-03	100	\N	\N	42268	t	\N	1	\N	es	\N	\N	es	\N	t	\N	Carlos Ott	\N	2	4
34719	Traje de calle del alférez real	\N	\N	\N	\N	\N	\N	\N	Recreación a partier del original (2023).\r\nNro. inventario 1047.	\N	El alférez real era uno de los principales funcionarios del Cabildo. Se encargaba de representar al rey en las ceremonias públicas portando el estandarte real. Además, se ocupaba de organizar las milicias en tiempos de guerra.\r\n\r\nEn las ceremonias públicas, el alférez real lucía un traje como este. La recreación fue realizada a partir del traje que perteneció a Francisco Antonio de Escalada, último alférez real del Cabildo de Buenos Aires, que forma parte de la colección del Museo. Está formado por calzón corto, chaleco y casaca. Se usaba con camisa blanca, medias del mismo color y zapatos con taco alto y grandes hebillas. En la época, este tipo de vestimenta era un signo de prestigio: contar con un traje como este era muy costoso.\r\n\r\nEsta recreación se realizó para proteger la pieza original de la exposición constante. Para replicar las medidas, se levantaron moldes a partir del traje original. \r\nSe seleccionó una tela similar y se la tiñó buscando una aproximación al color que tenía el traje en el momento de su uso.	\N	35197	\N	\N	0	\N	\N	2026-01-12 10:28:58.706081-03	2026-01-13 16:35:46.099565-03	100	\N	\N	537	t	\N	3	\N	es	\N	\N	es	\N	t	\N	Original del siglo XVIII	Siglo XVIII	1	5
16103	Je suis (Yo soy)	\N	\N	\N	\N	\N	\N	\N	\N	\N	Esta pintura presenta la figura de una mujer-caballo. La idea proviene de las religiones afrocubanas. En estas tradiciones, los iniciados participan en ceremonias donde sus cuerpos son poseídos por espíritus, llamados orishas.\r\n\r\nMartin Tsang es antropólogo y practicante de religiones afrocubanas.\r\n\r\nAntropólogo, Martin Tsang: Una de las maneras en que se describe la posesión es cuando el orisha desciende temporalmente a la tierra y monta a la persona, como un caballo. Esta idea es un motivo recurrente en la obra de Lam, y considero que la iconografía o el simbolismo de la mujer-caballo refleja la importancia de la mujer en los espacios religiosos afrocubanos. Las mujeres afrocubanas eran las guardianas del conocimiento. Dirigían ceremonias, comunidades e iniciaciones.	\N	16212	\N	\N	1949	\N	\N	2026-01-04 17:48:00.073762-03	2026-01-04 17:57:55.024221-03	100	\N	\N	860	t	\N	3	\N	..	\N	\N	es	\N	t	\N	\N	\N	0	8
280	Reposo	reposo	Reposo	\N	\N	126	Adquirido a Schiaffino, Eduardo por Ministerio de Justicia e Instrucción Pública	\N	109 x 200 cm.	\N	​Reposo fue pintado por Eduardo Schiaffino en París y admitido en la Exposición Universal con la que se celebró el centenario de la Revolución Francesa, donde obtuvo una medalla de tercera clase (bronce). Su autor fue el fundador y primer director del Museo Nacional de Bellas Artes, además de activo promotor de las bellas artes en Buenos Aires y primer historiador del arte argentino. En su libro La pintura y la escultura en Argentina (1933) comentó su propia obra en tercera persona, destacando la importancia de aquella medalla (solo el escultor Manuel de Santa Coloma había sido premiado en París 25 años antes que él y otros 25 años habrían de pasar antes de que otro argentino, Antonio Alice, volviera a ser premiado allí).\r\nEl cuadro presenta un desnudo de espaldas, en una pose algo forzada, con las piernas extendidas y cruzadas y un brazo arqueado sobre la cabeza. El cuerpo aparece pálido y azulado en medio de un espacio enteramente azul, en el que el paño sobre el que se encuentra tendido y el fondo tienen apenas diferencias de valor. Esta ambigüedad espacial se ve interrumpida solamente por un pequeño fragmento de piso, sobre el que se advierte el borde del paño (con brillos de terciopelo), en el ángulo inferior izquierdo de la tela. Hay también cierta ambigüedad de género en ese cuerpo casi adolescente, con el cabello muy corto y la cara oculta, en una posición que aparece como una reelaboración del Hermafrodito durmiente, el mármol helenístico restaurado por Bernini, del Museo del Louvre. Es tal vez el primer cuadro de inspiración simbolista pintado por Schiaffino, quien hasta entonces había exhibido algunas “impresiones” de paisaje y escenas de toilette más cercanas al estilo y la iconografía impresionistas. Jaime de la María comentó el cuadro para La Nación (2 de julio de 1890) atribuyéndole un estilo análogo al del maestro de Schiaffino en París, Pierre Puvis de Chavannes: “la analogía de carácter podrá parecer casual, pero la de estilo se explica: Schiaffino es puvisiste”. La atmósfera simbolista de Reposo adquiere un carácter más marcado en varias obras posteriores del artista, como Vesper (inv. 5377, MNBA), Craintive o Margot, tres retratos exhibidos al año siguiente en la exposición de la Sociedad de Damas de Nuestra Señora del Carmen, o el Desnudo (sinfonía en rojo) (inv. 7463, MNBA) que expuso en el Salón del Ateneo de 1895. Margot (inv. 1656, MNBA) fue celebrada en varios artículos como la primera obra en la que se revelaba su calidad como artista. Pero la polémica que entabló con el crítico que firmaba A.Zul de Prusia a propósito de la supuesta autoría de las obras de los pensionados en Europa, terminó ese año con un duelo a pistola.\r\nSchiaffino había viajado trayendo Reposo a Buenos Aires en medio de la crisis de 1890, con el objeto de exhibirlo y gestionar la renovación de su beca de estudios. Tanto la exposición de la pintura en la vidriera de la casa Bossi (donde el año anterior su primer envío desde París había sido comentado con franca hostilidad) como el otorgamiento de la beca, recibieron comentarios muy negativos en la prensa. Un comentarista anónimo del diario La Argentina (1 de julio de 1890) lo objetó por encontrarlo deforme e indecente, “con una sans-façon que huele a la legua al Paganismo y a sus más florecientes saturnales”. El artista encaró personalmente la defensa de su cuadro, que volvió a exponer en el primer Salón del Ateneo, en 1893, y nuevamente en el cuarto Salón del Ateneo en 1896, recogiendo en ambas ocasiones severas críticas, no solo hacia la pintura sino también al hecho de volver a incluirla en los salones cuando era una obra que no era nueva y que ya había sido expuesta y criticada con dureza. Es posible advertir en el gesto de Schiaffino una posición desafiante en consonancia con su tenaz actividad orientada –en sus propias palabras– a “educar el gusto” del público de Buenos Aires, introduciendo audacias del arte moderno en un género que a lo largo del siglo XIX se había tornado emblemático de aquellas. En Buenos Aires todavía resonaba el escándalo que había suscitado, en 1887, Le lever de la bonne de Eduardo Sívori.	\N	305	\N	\N	1889	\N	135	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	​Reposo fue pintado por Eduardo Schiaffino en París y admitido en la Exposición Universal con la que se celebró el centenario de la Revolución Francesa, donde obtuvo una medalla de tercera clase (bronce). Su autor fue el fundador y primer director del Museo Nacional de Bellas Artes, además de activo promotor de las bellas artes en Buenos Aires y primer historiador del arte argentino. En su libro La pintura y la escultura en Argentina (1933) comentó su propia obra en tercera persona, destacando la importancia de aquella medalla (solo e	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	81
50608	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	\N	\N	2026-02-14 09:22:49.342389-03	2026-02-14 09:22:49.342417-03	100	\N	\N	177	t	\N	1	\N	..	\N	\N	es	\N	t	\N	\N	\N	0	1
575	Abaporu	new	\N	\N	\N	\N	\N	\N	Técnica: Óleo sobre tela\r\n85,3 x 73 cm\r\nNro. de inventario: 2003.33\r\nDonación: Eduardo F. Costantini, 2001	\N	Al reflexionar sobre Abaporu, Tarsila aventuró una hipótesis sobre su origen:\r\n\r\n&amp;amp;amp;amp;amp;quot;Recordé que, cuando era pequeña y vivía en una hacienda enorme […] las criadas me llevaban –con un grupo de niñas– a un cuarto viejo y amenazaban: “¡Vamos a mostrarles una cosa increíble! ¡Una aparición! ¡De aquel agujero va a caer un fantasma! ¡Va a caer un brazo, una pierna!”, y nosotras quedábamos horrorizadas. Todo eso quedó en mi psiquis, tal vez en el subconsciente.&amp;amp;amp;amp;amp;quot;\r\n\r\nAunque Tarsila atribuyese la matriz de ese ser fantástico a las historias que oía en su infancia, es forzoso recordar que, durante la década de 1920, la artista estuvo varias veces en la capital francesa, atenta a las manifestaciones de vanguardia: Aragon, Arp, Artaud, Brancusi, Breton, Cendrars y Rousseau, ente otros, formaban parte de su repertorio. Probablemente La création du monde, presentado en 1923 por los Ballets Suecos, con música de Milhaud, escenografía de Léger y guión de Cendrars, de tema primitivo y estética innovadora, haya motivado a Tarsila a pintar A negra (MAC-USP), figura antropofágica avant la lettre, que antecede a Abaporuen cinco años.	\N	568	\N	\N	1928	\N	\N	2025-09-18 15:47:56.92008-03	2025-09-19 20:04:55.580193-03	100	\N	\N	342	t	https://g.co/arts/zVtPRdJTYMdj2cxy9	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0	43
589	Troncos	new	\N	\N	\N	\N	\N	\N	Técnica: Acuarela y gouache sobre papel montado sobre cartón\r\n31,7 x 46,8 cm\r\nNro. de inventario: 2001.184\r\nDonación: Eduardo F. Costantini, Buenos Aires	\N	Para analizar Troncos uno puede situarse en dos lugares distintos. Primero, es posible pensar la obra en el contexto de la producción de Xul Solar. Y luego, tratar de ver si su estudio iconográfico nos permite comprender cuál es su sentido.\r\n\r\nAl reconstruir su contexto de producción, entendido aquí como aquello que rodea a la obra, lo primero que se verifica es la existencia de cuatro pinturas que comparten casi la misma iconografía y probablemente el mismo tema, aunque por lo menos dos de ellas –una de la colección del Museo Nacional de Bellas Artes (MNBA) y la segunda de la colección del Museo Xul Solar - Fundación Pan Klub (MXS)– se alejan en la elección de la paleta y también en su estilo, a lo que se debe agregar que sus dimensiones son menores. De una tercera pieza solo conocemos un fragmento (también perteneciente a la colección de la Fundación Pan Klub), que se diferencia del resto del conjunto por la técnica y el soporte elegidos, óleo sobre cartón, lo que lo sitúa como parte de otro conjunto mayor de óleos realizados aproximadamente entre 1918 y 1920. Por último, una cuarta obra, hoy desaparecida, que se corresponde con la versión estudiada aquí –aunque presenta pequeñas diferencias que, para ser reconocidas, obligan a un cuidadoso examen de ambas pinturas– y, si bien no lo sabemos con certeza, es probable que compartiera también las mismas medidas.	\N	590	\N	\N	1919	\N	\N	2025-09-19 10:50:26.423731-03	2025-09-21 19:51:23.930402-03	100	\N	\N	342	t	\N	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0	46
603	El mal	new	\N	\N	\N	\N	\N	\N	Su universo es un surrealismo punk y sudoroso, una brea fantástica que chorrea por las paredes del museo. Son pinturas que invitan a ser miradas una y otra vez, hasta que los ojos se agoten y ya no puedan distinguir entre la realidad y su vecina, la pesadilla.	\N	El trabajo de Bencardino se alimenta de imágenes encontradas en libros, revistas, tapas de discos, videoclips, internet y su archivo personal de objetos y otros materiales que circulan en la cultura de masas y sus plataformas. Sus referencias provienen de un imaginario afectivo muy particular: las estéticas de las comunidades queer y las adolescencias de su generación, los códigos visuales de las escenas contraculturales (como el punk, y los distintos subgéneros del metal), el comic y la ilustración (como Ciruelo, Victoria Francés, Luis Royo, Boris Vallejo y Magalí Villeneuve), y el imaginario fantástico literario (especialmente, de William Blake y J.R.R. Tolkien), entre muchas otras referencias. Procesa y distorsiona estas imágenes digitalmente, y a partir de esas nuevas imágenes elabora sus pinturas.	\N	604	\N	\N	2025	\N	\N	2025-09-21 19:58:27.991765-03	2025-09-21 20:02:52.780835-03	100	\N	\N	342	t	https://malba.org.ar/evento/carrie-bencardino-el-desentierro-del-diablo/	1	\N	es	\N	0	es	\N	t	\N	\N	\N	0	47
450	Apocalipsis	apocalipsis	Apocalipsis	\N	\N	126		\N	Gouache sobre papel  75 x 55 cm.	\N	Según Dante Alighieri, el infierno es un cono invertido, dividido en nueve círculos que se estrechan a medida que descendemos. \r\n\r\nPara acceder a él hay que cruzar el río Aqueronte, guiados por Caronte, el barquero. Ningún viajero ha regresado para confirmar esa travesía. Por eso, Gárgano nos propone su propia versión del infierno, construido pacientemente sobre el papel a lo largo de muchos años. En la trama de líneas, el artista ofrece sus indicios, y nos corresponde a nosotros cruzar el río e interpretar aquello que vemos.	\N	465	\N	\N	2017	\N	135	2025-07-25 18:47:09.47387-03	2025-10-25 12:42:55.274049-03	100	\N	\N	137	t	\N	3	\N	es	\N	0	es	\N	t	\N	\N	\N	0	82
\.


--
-- Data for Name: artwork_artist; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.artwork_artist (artwork_id, artist_id, person_id) FROM stdin;
592	5062	593
311	5052	308
205	5033	\N
22902	5067	\N
582	5063	\N
768	5066	763
12377	5067	\N
589	5061	588
16103	5067	\N
603	5064	602
884	5067	\N
229	5041	\N
406	5057	\N
272	5046	\N
50571	5040	\N
213	5036	\N
50571	5032	\N
482	5060	\N
328	5036	\N
211	5035	\N
219	5038	\N
448	5058	\N
221	5032	\N
402	5059	\N
270	5045	\N
452	5058	\N
197	5043	\N
404	5055	\N
225	5039	\N
207	5034	\N
207	5035	\N
195	5031	\N
217	5036	\N
280	5050	\N
332	5053	\N
274	5047	\N
450	5058	\N
215	5037	\N
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
50557	es	50556	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 16:11:21.440749-03	2026-02-12 16:11:21.440753-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
693	en	450	Apocalypse	\N		Gouache on paper 75 x 55 cm.	According to Dante Alighieri, Hell is an inverted cone, divided into nine circles that narrow as we descend. \n\nTo reach it, one must cross the River Acheron, guided by Charon, the ferryman. No traveler has ever returned to confirm that journey. Therefore, Gargano offers us his own version of Hell, patiently constructed on paper over many years. In the interweaving of lines, the artist offers his clues, and it is up to us to cross the river and interpret what we see.	\N	\N	\N	\N	f	2025-10-23 15:18:36.645799-03	2025-10-25 12:51:37.196501-03	100	\N	\N	\N	-1978804888	0	-909355096	0	-1726264413	\N	0	\N	0	t	f
694	en	272	En Normandie	\N	\N	\N	\N	\N	\N	\N	\N	f	2025-10-23 15:20:06.595377-03	2025-10-23 15:20:06.595393-03	100	\N	\N	\N	0	0	0	0	0	\N	0	\N	0	t	f
696	en	219	The Banks of the Seine	\N		\N	At the meeting of November 17, 1910, the members of the Buenos Aires City Council voted on a bill highlighting the role of the Municipality in promoting artistic culture, one of the possible avenues being the donation of works of art. This resolution benefited the MNBA, which had a state budget to increase its assets with the acquisition of works by the National Commission of Fine Arts during the "Buenos Aires 1910 International Centennial Art Exposition."	\N	\N	\N	\N	f	2025-10-23 15:38:00.588639-03	2025-10-23 15:38:00.589119-03	100	\N	\N	\N	1416721435	0	579260735	0	0	\N	0	\N	0	t	f
695	en	282	Abel	\N		\N	Lucio Correa Morales, trained in Florence by Urbano Lucchesi, produced a vast output of portraits, funerary sculptures, commemorative monuments, and decorative works. He also dedicated himself to teaching sculpture at the Sociedad Estímulo de Bellas Artes, the origin of the Academy in Argentina.\nIn 1902, he made a terracotta sketch of Abel and a second one in bronze, both preserved in private collections, featuring the isolated figure of the lifeless young man murdered by his brother Cain. In May 1903, a critic from La Nación associated the work with the Italian Giovanni Duprè's marble sculpture Abel, a work from 1842, despite the compositional differences.\n\nCorrea Morales's own expressions reveal the true intentions of his work, as he was aware that "No one understood, nor perhaps even imagined, that this 'Abel' was not the one killed by Cain, but rather Argentine art killed by its brothers. In that figure, even my name had been disguised; […] I would gladly have signed Moralaine, Moralini, or Moralai." This comment allows us to interpret the phrase as an allusion to the lack of development and interest in national art: "Dead without descendants like a wretch / It is easy to draw the conclusion / That we are all the race of Cain."\n\nThe plaster version of Abel won the silver medal for Sculpture at the International Exposition of Saint Louis in 1904. Schiaffino, responsible for the Argentine submission, had requested a gold medal from the jury for Lucio Correa Morales, but his proposal was rejected due to criticism of the inadequate proportions of the figure's arms (2). International recognition encouraged the possibility of its casting in bronze, along with other award-winning works such as Las pecadoras by Rogelio Yrurtia.	Lucio Correa Morales, trained in Florence with Urbano Lucchesi, produced a vast output of portraits, funerary sculptures, commemorative monuments, and decorative works. He also dedicated himself to teaching sculpture at the Sociedad Estímulo de Bellas Artes, the origin of the Academy in Argentina.\nIn 1902, he made a terracotta sketch of Abel and a second one in bronze, both preserved in private collections, featuring the isolated figure of the lifeless young man murdered by his brother Cain. In May 1903, he was associated with a critic of	\N	\N	\N	f	2025-10-23 15:20:40.6022-03	2025-10-25 12:41:02.521786-03	100	\N	\N	\N	2987144	0	721249886	1966264190	0	\N	0	\N	0	t	f
34720	es	34719	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-12 10:28:58.719833-03	2026-01-12 10:28:58.71984-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
34721	en	34719	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-12 10:28:58.722596-03	2026-01-12 10:28:58.722604-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
34722	pt-BR	34719	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-12 10:28:58.723934-03	2026-01-12 10:28:58.723937-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
40220	es	40219	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-13 15:08:24.708849-03	2026-01-13 15:08:24.708853-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
40221	en	40219	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-13 15:08:24.710197-03	2026-01-13 15:08:24.710198-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50558	en	50556	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 16:11:21.443876-03	2026-02-12 16:11:21.443886-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50559	pt-BR	50556	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 16:11:21.444761-03	2026-02-12 16:11:21.444764-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50560	it	50556	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 16:11:21.44534-03	2026-02-12 16:11:21.445342-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
40222	pt-BR	40219	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-13 15:08:24.710621-03	2026-01-13 15:08:24.710622-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
42815	es	42814	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-21 09:48:23.154471-03	2026-01-21 09:48:23.15448-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
42816	en	42814	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-21 09:48:23.157101-03	2026-01-21 09:48:23.157106-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
42817	pt-BR	42814	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-21 09:48:23.15866-03	2026-01-21 09:48:23.158662-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
43115	es	43114	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-21 10:00:06.425476-03	2026-01-21 10:00:06.425483-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
43116	en	43114	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-21 10:00:06.427731-03	2026-01-21 10:00:06.427736-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
43117	pt-BR	43114	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-01-21 10:00:06.428498-03	2026-01-21 10:00:06.428501-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50561	fr	50556	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 16:11:21.446113-03	2026-02-12 16:11:21.446116-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50562	ger	50556	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 16:11:21.446901-03	2026-02-12 16:11:21.446904-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50572	es	50571	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:43:04.139309-03	2026-02-12 17:43:04.139328-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50573	en	50571	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:43:04.14214-03	2026-02-12 17:43:04.142145-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50574	pt-BR	50571	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:43:04.14376-03	2026-02-12 17:43:04.143763-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50575	it	50571	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:43:04.145417-03	2026-02-12 17:43:04.145422-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50576	fr	50571	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:43:04.146641-03	2026-02-12 17:43:04.146643-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50577	ger	50571	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:43:04.147235-03	2026-02-12 17:43:04.147236-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50579	es	50578	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:56:15.01777-03	2026-02-12 17:56:15.017774-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50580	en	50578	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:56:15.019317-03	2026-02-12 17:56:15.01932-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50581	pt-BR	50578	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:56:15.02067-03	2026-02-12 17:56:15.020673-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50582	it	50578	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:56:15.021613-03	2026-02-12 17:56:15.021615-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50583	fr	50578	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:56:15.022335-03	2026-02-12 17:56:15.022337-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50584	ger	50578	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-12 17:56:15.022914-03	2026-02-12 17:56:15.022915-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50609	es	50608	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-14 09:22:49.379736-03	2026-02-14 09:22:49.379742-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50610	en	50608	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-14 09:22:49.381499-03	2026-02-14 09:22:49.3815-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
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
50611	pt-BR	50608	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-14 09:22:49.382097-03	2026-02-14 09:22:49.382098-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50612	it	50608	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-14 09:22:49.382645-03	2026-02-14 09:22:49.382647-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50613	fr	50608	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-14 09:22:49.383198-03	2026-02-14 09:22:49.383199-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50614	ger	50608	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-14 09:22:49.383691-03	2026-02-14 09:22:49.383692-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50702	es	50701	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-16 14:14:28.894162-03	2026-02-16 14:14:28.894174-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50703	en	50701	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-16 14:14:28.89839-03	2026-02-16 14:14:28.898395-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50704	pt-BR	50701	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-16 14:14:28.899623-03	2026-02-16 14:14:28.899627-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50705	it	50701	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-16 14:14:28.900874-03	2026-02-16 14:14:28.900876-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50706	fr	50701	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-16 14:14:28.901611-03	2026-02-16 14:14:28.901613-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
50707	ger	50701	new	\N	\N	\N	\N	\N	\N	\N	\N	t	2026-02-16 14:14:28.902957-03	2026-02-16 14:14:28.902958-03	100	\N	\N	1	0	0	0	0	0	\N	0	\N	0	t	f
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

COPY public.audiostudio (id, guidecontent_id, artexhibitionguide_id, info, settings, settings_json, audiospeechmusic, audiospeech, created, lastmodified, lastmodifieduser, audioautogenerate, language, name, state, json_data, artexhibitionguiderecord_id, artexhibitionitemrecord_id, artexhibitionrecord_id, artexhibitionsectionrecord_id, artworkrecord_id, guidecontentrecord_id, institutionrecord_id, personrecord_id, siterecord_id, audio_speech_hash, audio_speech_music_hash, musicurl, infoaccessible, audiospeechaccesible, audiospeechmusicaccesible, audio_speech_accesible_hash, audio_speech_music_accesible_hash, music_id) FROM stdin;
805	\N	458	Germán Gárgano es un pintor argentino. Nace en 1953, en Buenos Aires, Argentina. Cursando sus estudios de medicina es detenido por razones políticas en 1975, situación que se prolonga hasta fines de 1982.	\N	\N	818	817	2025-11-13 08:54:20.62889-03	2025-11-19 16:41:25.882375-03	100	f	es	Sendas perdidas	\N	{"end": "30", "music": "", "speed": "10.0", "start": "30", "stability": "10.0", "similarity": "10.0", "fadeDurationSec": "14", "introDurationSec": "28", "voiceOverlapDurationSec": "7"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	1827940477	1818781226	https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3	\N	\N	\N	-1	-1	\N
798	\N	786	La muestra comprende tres décadas atravesados por el nacimiento y auge del fascismo y la  figura de Benito Mussolini, la alianza con la Alemania nazi durante la Segunda Guerra Mundial, y la posguerra.\r\n\r\nEntre 1920 y 1950, el turismo en Italia se transformó en un fenómeno de masas y el afiche fue una herramienta clave:  síntesis entre arte, diseño y promoción cultural, pieza publicitaria que a la vez era obra de arte que reflejaba la cultura y la estética de su tiempo.\r\n\r\nEn la década del 20 se produce una transición desde el eclecticismo, el Liberty o Art Nouveau de principios de siglo para pasar en los 30 a un estilo modernista más sobrio con influencia de vanguardias europeas, como el cubismo, y especialmente el Futurismo italiano, un movimiento artístico fundado por Filippo Tommaso Marinetti  que se caracterizó por la exaltación de la velocidad, la tecnología, la modernidad, la violencia y el dinamismo, rechazando las tradiciones y el pasado.\r\n\r\nDurante el fascismo, estas obras turísticas escaparon al control de la censura, pero no de la influencia de la promoción política. Al igual que en Alemania y la Unión Soviética la vanguardia artística que exaltaba el nuevo hombre resultó demasiado libre; en Italia cedió su lugar al “realismo del fascismo”, un arte similar al realismo soviético, que se llamó “el retorno al orden”. \r\n\r\nLa muestra incluye obras de ilustradores italianos de gran renombre, como Marcello Dudovich y Mario Borgoni, que jugaron un papel crucial en la creación de una identidad turística para el país; y también de Marcello Nizzoli, quién en la postguerra diseñaría las célebres máquinas de escribir portátiles Olivetti.	\N	\N	\N	\N	2025-11-11 21:44:23.570488-03	2025-11-11 21:44:23.570494-03	100	f	es	Viaggio in Italia: 1920-1950, La Edad de Oro del Afiche Turístico Italiano	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	-1	-1	\N	\N	\N	\N	-1	-1	\N
803	\N	799	La muestra “Diseño Infinito” presenta 40 años de producción del reconocido diseñador argentino, con piezas históricas que van desde óleos a diferentes objetos, textiles y mobiliario de autor.	\N	\N	\N	804	2025-11-11 21:52:15.031216-03	2025-11-11 22:00:42.364473-03	100	f	es	Alberto Churba. Diseño Infinito	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	-1	-1	\N	\N	\N	\N	-1	-1	\N
848	\N	\N	The painting Theodora, painted by Jean-Joseph Benjamin-Constant in 1887, depicts an Orientalist and theatrical vision of the Byzantine Empress Theodora, wife of Justinian I, the most influential and powerful woman in the history of the Eastern Roman Empire.\n\nIt is not a historical representation, but rather an opulent fantasy based on the 19th-century European fascination with the Orient and the imperial past. Benjamin-Constant was a highly technical painter specializing in portraits of elite figures and historical figures: power, luxury, sophistication, calculated poses, complex textures such as silk and velvet, and refined settings.\n\nOf humble origins, Theodora earned her living as a young woman as an actress and courtesan until she was chosen by Justinian as his wife. The historical image—based on Procopius of Caesarea in antiquity and Gibbon in the 18th century—is that of a cunning, intelligent, and cruel woman. This profile is now considered exaggerated due to the rigid view of women's social role. The modern view is that she was a central political figure in the Byzantine Empire, de facto co-ruler with Justinian. She actively participated in government decisions. In the Nika revolt of 532, she convinced Justinian not to flee and instead confront his enemies with her famous phrase, "purple is a good shroud." She promoted laws favoring women's rights: she prohibited forced sex trafficking, strengthened women's rights in marriage and divorce, and founded homes for women rescued from prostitution, something unprecedented at the time. As a result of Theodora's efforts, the status of women in the Byzantine Empire was higher than that of women in Europe.\n\nIn Argentine history, the immediate parallel is with Eva Perón.\n\nBenjamin-Constant set out to create a visual icon of feminine power and oriental exoticism. And what emerged, perhaps unintentionally, is a symbol of modernity: a young, powerful, beautiful, and intelligent woman with a defiant gaze who dares to be different.	\N	\N	\N	\N	2025-11-27 12:21:28.852456-03	2025-11-27 12:21:28.852476-03	100	f	en	Empress Theodora	\N	\N	\N	\N	\N	\N	\N	746	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
810	\N	\N	\N	\N	\N	\N	\N	2025-11-14 09:29:38.536008-03	2025-11-14 09:29:38.536019-03	100	f	en	Sendas perdidas	\N	\N	738	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
811	\N	\N	\N	\N	\N	\N	\N	2025-11-14 14:58:23.698686-03	2025-11-14 14:58:23.698701-03	100	f	pt-BR	Sendas perdidas	\N	\N	739	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
849	\N	\N	\N	\N	\N	\N	\N	2025-11-27 12:21:44.343492-03	2025-11-27 12:21:44.343502-03	100	f	pt-BR	La Emperatriz Theodora	\N	\N	\N	\N	\N	\N	\N	747	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
854	\N	\N	\N	\N	\N	\N	\N	2025-11-27 17:12:17.145233-03	2025-11-27 17:12:17.14524-03	100	f	pt-BR	Obras Maestras	1	\N	853	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
825	414	\N	Joaquín Sorolla, el "Maestro de la Luz", inmortalizó la costa de Valencia, especialmente la Playa de la Malvarrosa, capturando la luz mediterránea y escenas costumbristas de pescadores y niños jugando en obras icónicas como "En la costa de Valencia" (1898) y "Corriendo por la playa" (1908), destacando por su estilo luminista, pinceladas vibrantes y la representación de la vida cotidiana valenciana, llena de movimiento y reflejos del sol sobre el mar.	\N	\N	\N	\N	2025-11-20 14:51:23.800806-03	2025-11-20 14:51:23.800812-03	100	f	\N	En la costa de Valencia	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
829	488	\N	Desde el Legado Nicolás García Uriburu trabajamos para preservar y difundir su legado artístico. Custodiamos su colección, gestionamos integralmente su obra y procesamos su archivo documental con el fin de mantener vigente su mensaje y contribuir a la reflexión sobre arte y ecología.	\N	\N	\N	1106	2025-11-21 13:44:33.775451-03	2025-11-21 13:44:33.775524-03	100	f	es	Utopia del Sur	\N	{"speed": "9.0", "stability": "82.0", "similarity": "53.0"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	-767214436	0	\N	\N	\N	\N	-1	-1	\N
838	\N	202	La obra maestra era el nombre que recibía la pieza artesanal que debía realizar todo oficial que quisiera acceder a la categoría de maestro en el seno de los gremios.	\N	\N	\N	50467	2025-11-23 21:43:08.719573-03	2025-11-23 21:43:08.719576-03	100	f	es	Obras Maestras	1	{"speed": "0.9", "voiceid": "TOFW0dONbX4o9MmkxwBB", "stability": "0.82", "similarity": "0.53"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	1943021136	0	\N	\N	\N	\N	-1	-1	\N
916	\N	\N	Wifredo Lam, the most universal of Cuban painters, introduced Black culture into Cuban painting and developed a groundbreaking body of work that integrates elements of African and Chinese origin present in Cuba.\n\nThe painting has been interpreted as the synthesis of an Antillean cycle, by virtue of the dominant Baroque space and the atmosphere created by the association of the human, the animal, the vegetal, and the divine. It contains a visual vocabulary that evolved from academic landscape painting toward a theme and language of modern art. In this oil painting, the painter's visions and experiences seem to merge: the mythical island landscape, the incorporation of content and iconography from the magical-religious systems of African origin prevalent in Cuba and the Caribbean.\n\nIt is considered a synthesis of his political philosophy, where European Surrealism and Cubism are mixed with the power of myth characteristic of the syncretic cults of the Caribbean.	\N	\N	918	22521	2025-11-30 18:30:59.493109-03	2025-11-30 18:30:59.493112-03	100	f	en	The jungle	\N	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "7"}	\N	\N	\N	\N	\N	901	\N	\N	\N	664551921	1589746096	https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3	\N	\N	\N	-1	-1	\N
898	\N	894	Se trata de la retrospectiva más extensa dedicada al artista en Argentina, abarcando las seis décadas de la prolífica carrera de Lam.\r\nLa exposición incluye más de 130 obras de arte de las décadas de 1920 a 1970, incluyendo pinturas, obras a gran escala sobre papel, dibujos colaborativos, libros ilustrados, grabados, cerámicas y material de archivo, con préstamos clave del Estate of Wifredo Lam, París. La retrospectiva revela cómo Lam, un artista nacido en Cuba que pasó la mayor parte de su vida en España, Francia e Italia, llegó a encarnar la figura del artista transnacional en el siglo XX.	\N	\N	911	906	2025-11-29 07:52:34.045037-03	2025-11-29 07:52:34.045048-03	100	f	es	Wifredo Lam, cuando no duermo, sueño	1	{"speed": "97.0", "stability": "82.0", "similarity": "53.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	-1261488223	1589746094	https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3	\N	\N	\N	-1	-1	\N
1105	\N	\N	Joaquín Sorolla, o "Mestre da Luz", imortalizou o litoral de Valência, em especial a Praia de Malvarrosa, capturando a luz do Mediterrâneo e cenas cotidianas de pescadores e crianças brincando em obras icônicas como "Na Costa de Valência" (1898) e "Correndo na Praia" (1908), destacando-se pelo seu estilo luminista, pinceladas vibrantes e a representação da vida diária valenciana, repleta de movimento e reflexos do sol no mar.	\N	\N	\N	\N	2025-12-08 12:15:34.522168-03	2025-12-08 12:15:34.522174-03	100	t	pt-BR	Na costa de Valência	\N	\N	\N	\N	\N	\N	\N	737	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
923	\N	\N	\N	\N	\N	\N	\N	2025-12-04 10:01:41.1209-03	2025-12-04 10:01:41.120914-03	100	f	en	Utopia del Sur	\N	\N	\N	\N	\N	\N	\N	922	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
1103	\N	\N	Entre suas muitas missões, os museus preservam a memória de uma nação e, ao mesmo tempo, como instituições dinâmicas, estimulam a construção de novos significados para as peças que abrigam. As maneiras pelas quais as obras de uma coleção são apresentadas ao público moldam a relação que uma comunidade estabelece com a história, um vínculo que se fortalece quando uma instituição se mostra aberta às transformações estéticas e sociais que cada época traz.	\N	\N	\N	1104	2025-12-08 12:11:19.198255-03	2025-12-08 12:11:19.198286-03	100	t	pt-BR	Museu Secreto	\N	{"speed": "9.0", "stability": "82.0", "similarity": "53.0"}	735	\N	\N	\N	\N	\N	\N	\N	\N	30043779	0	\N	\N	\N	\N	-1	-1	\N
924	\N	\N	Among their many missions, museums preserve a nation's memory and, at the same time, as dynamic institutions, stimulate the construction of new meanings for the pieces they house. The ways in which the works in a collection are presented to the public shape the relationship a community establishes with history, a bond that is strengthened when an institution is open to the aesthetic and social transformations that each era brings.	\N	\N	\N	1107	2025-12-04 10:06:04.642233-03	2025-12-04 10:06:04.64224-03	100	f	en	Secret Museum	\N	{"speed": "9.0", "stability": "82.0", "similarity": "53.0"}	734	\N	\N	\N	\N	\N	\N	\N	\N	11660576	0	\N	\N	\N	\N	-1	-1	\N
1109	459	\N	\N	\N	\N	\N	\N	2025-12-11 16:22:07.475258-03	2025-12-11 16:22:07.475264-03	100	t	es	Sendas perdidas	1	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
1110	\N	\N	\N	\N	\N	\N	\N	2025-12-11 16:22:18.373637-03	2025-12-11 16:22:18.373642-03	100	t	en	Sendas perdidas	\N	\N	\N	\N	\N	\N	\N	903	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
7329	\N	\N	No Legado Nicolás García Uriburu, trabalhamos para preservar e divulgar seu legado artístico. Salvaguardamos sua coleção, gerenciamos sua obra de forma abrangente e processamos seu arquivo documental para manter sua mensagem relevante e contribuir para a reflexão sobre arte e ecologia.	\N	\N	\N	\N	2025-12-24 17:36:23.625307-03	2025-12-24 17:36:23.625325-03	101	t	pt-BR	Utopia do Sul	\N	\N	\N	\N	\N	\N	\N	1108	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
912	\N	\N		\N	\N	914	913	2025-11-30 17:57:53.562704-03	2025-11-30 17:57:53.562711-03	100	f	pt-BR	A selva	\N	{"speed": "9.0", "stability": "82.0", "similarity": "53.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "8"}	\N	\N	\N	\N	\N	902	\N	\N	\N	-2107848187	1589746097	https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3	\N	\N	\N	-1	-1	\N
18635	18156	\N	Las religiones afrocubanas incluyen el Lucumí, o Santería, del pueblo de habla yoruba del suroeste de Nigeria, que es el culto a los orishas o espíritus. También tenemos el Palo, de África Central, las prácticas de Benín llamadas Arará, así como ideas europeas o cristianas.\r\n\r\nEl título de esta pintura, Malembo Sombrío, Dios de la Encrucijada, hace referencia al orisha de la tradición lucumí o santería.\r\nLa palabra "Malembo" en el título podría referirse a un puerto negrero en la costa occidental de África. \r\nPara cuando Lam nació en 1902, solo habían pasado 16 años desde la abolición de la esclavitud en Cuba.\r\n\r\nEn gran parte de la obra de Lam se observa lo que podría identificarse como una figura similar a la de Eleguá: esta pequeña cabeza abovedada. Eleguá es el orisha de la encrucijada. Tiene la capacidad única de abrir puertas, comunicarse entre idiomas y atraer la buena fortuna y alejar la mala suerte.	\N	\N	\N	\N	2026-01-05 10:48:56.418356-03	2026-01-05 10:48:56.418362-03	101	t	es	Le Sombre Malembo, Dieu du carrefour	1	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
22018	\N	\N	\N	\N	\N	\N	\N	2026-01-06 14:30:46.378032-03	2026-01-06 14:30:46.378062-03	101	t	en	Wilfredo Lan, when I dont sleep, I dream	1	\N	896	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N	\N	\N	\N	-1	-1	\N
915	899	\N	Wifredo Lam, el más universal de los pintores cubanos. Introdujo la cultura negra en la pintura cubana y desarrolló una renovadora obra que integra elementos de origen africano y chino presentes en Cuba. \r\n\r\nEl cuadro ha sido interpretado como la síntesis de un ciclo antillano, en virtud del espacio barroco dominante y de la atmósfera creada por la asociación de lo humano, lo animal, lo vegetal y lo divino. Hay en él un vocabulario visual que evolucionó desde el paisaje de corte académico hacia un tema y un lenguaje de arte moderno. En este óleo parecen fusionarse visiones y vivencias del pintor; el mítico paisaje insular, la incorporación de contenidos e iconografías procedentes de los sistemas mágico-religiosos de origen africano extendidos en Cuba y el Caribe\r\n\r\nSe la considera una síntesis de su política, donde se mezclan surrealismo y cubismo europeos con el poder del mito característico de los cultos sincréticos del Caribe.	\N	\N	22475	22474	2025-11-30 18:24:31.420696-03	2025-11-30 18:24:31.42071-03	100	f	es	La jungla	1	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	-1022410287	1589746094	https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3	\N	\N	\N	-1	-1	\N
34201	\N	\N	A história do Cabildo começou em 1580. Naquele ano, Juan de Garay fundou a cidade de La Trinidad, que mais tarde foi renomeada em homenagem ao seu porto: Santa María del Buen Ayre.\n\nOs Cabildos surgiram na Espanha como uma forma de administração para as cidades e seus arredores. Posteriormente, foram transferidos para as colônias americanas. De acordo com as Leis das Índias, que regulamentavam a vida nos territórios do Império Espanhol, toda cidade era obrigada a ter um Cabildo. Este sempre se localizava na praça principal, próximo a outras instituições importantes, como a igreja principal. Em Buenos Aires, também abrigava a cadeia da cidade.\n\nO Cabildo era a única autoridade eleita pela comunidade local. Vice-reis, governadores e outros funcionários importantes eram nomeados pela Espanha. Em contrapartida, os membros do Cabildo representavam os habitantes de Buenos Aires. Somente os chamados "vecinos" (homens brancos e de posição social elevada) podiam ser membros. Os membros do Cabildo se reuniam regularmente para discutir assuntos importantes para a comunidade.\n\nNo final do século XVIII, Buenos Aires deixou de ser uma cidade marginal do Império Espanhol para se tornar a capital do Vice-Reino do Rio da Prata. Sua crescente importância fez com que o Cabildo (conselho municipal) assumisse cada vez mais poderes.\n\nO Cabildo desempenhou um papel político fundamental nos anos que se seguiram à Revolução de 1810. Em 1821, foi dissolvido. A partir de então, a organização da cidade ficou a cargo da Assembleia Legislativa de Buenos Aires, criada um ano antes.	\N	\N	34208	34207	2026-01-11 16:23:00.873482-03	2026-01-11 16:23:00.873485-03	101	t	pt-BR	Câmara Municipal de Buenos Aires	1	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	33828	\N	\N	\N	\N	\N	\N	\N	\N	-1357823488	181369522	https://archive.org/download/dm-2530-247/DM%202530%20247%E2%80%A2%281%29%20Albinoni%20T.%2C%20Adagio%20en%20sol%20mineur.mp3	\N	\N	\N	-1	-1	\N
50462	\N	\N	L'exposition « Héritages de l'artisanat » présente des récipients, des paniers, des textiles, des couteaux et des étriers issus de la collection du Fonds national des arts.\n\nCes œuvres mettent en lumière l'artisanat comme une pratique vivante et collective, partie intégrante de l'identité culturelle argentine.\n\nL'exposition rend également hommage à Teresa Anchorena, directrice du patrimoine et de l'artisanat au Fonds national des arts, décédée cette année et figure emblématique de cette initiative. Son travail et son engagement ont été essentiels à la promotion du patrimoine de l'organisation et à l'établissement d'une relation respectueuse et active avec les territoires, les artisanats et les communautés qui, depuis des temps immémoriaux, ont bâti cet héritage désormais accessible au public.	\N	\N	50716	50480	2026-02-11 17:23:53.62609-03	2026-02-11 17:23:53.626099-03	100	t	fr	Héritages de l'action	3	{"speed": "0.9", "voiceid": "1a3lMdKLUcfcMtvN772u", "startSec": "0", "stability": "0.82", "similarity": "0.53", "fadeDurationSec": "6", "introDurationSec": "25", "voiceOverlapDurationSec": "3"}	50448	\N	\N	\N	\N	\N	\N	\N	\N	-1947766240	520308239	\N	\N	\N	\N	0	0	\N
796	\N	411	Entre sus múltiples misiones, los museos preservan la memoria de una nación y, al mismo tiempo, como instituciones dinámicas, estimulan la construcción de nuevos sentidos para las piezas que albergan. Los modos en que las obras de una colección se dan a conocer al público moldean la relación que una comunidad entabla con la historia, vínculo que se potencia cuando una institución es permeable a las transformaciones estéticas y sociales que trae cada época.	\N	\N	50668	815	2025-11-11 17:34:51.04344-03	2025-11-17 15:03:29.286601-03	100	f	es	Museo Secreto	\N	{"speed": "10.0", "startSec": "36", "stability": "10.0", "similarity": "10.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	-1771865989	155884603		\N	\N	\N	-1	-1	\N
34158	\N	\N	The history of the Cabildo began in 1580. That year, Juan de Garay founded the city of La Trinidad, which was later renamed after its port: Santa María del Buen Ayre.\n\nCabildos originated in Spain as a form of administration for cities and their surrounding areas. They were later transferred to the American colonies. According to the Laws of the Indies, which regulated life in the territories of the Spanish Empire, every city was required to have a Cabildo. This was always located in the main square, next to other important institutions, such as the main church. In Buenos Aires, it also housed the city jail.\n\nThe Cabildo was the only authority elected by the local community. Viceroys, governors, and other important officials were appointed from Spain. In contrast, the members of the Cabildo represented the inhabitants of Buenos Aires. Only those called "vecinos" (male, white, and of social standing) could be members. The Cabildo members met regularly to discuss matters of importance to the community.\n\nTowards the end of the 18th century, Buenos Aires went from being a marginal city of the Spanish Empire to becoming the capital of the Viceroyalty of the Río de la Plata. Its growing importance meant that the Cabildo (city council) assumed increasingly more powers.\n\nThe Cabildo played a fundamental political role in the years following the 1810 revolution. In 1821, it was dissolved. From then on, the organization of the city was in the hands of the Buenos Aires Legislature, created a year earlier.	\N	\N	34165	34164	2026-01-11 16:21:31.477383-03	2026-01-11 16:21:31.477389-03	101	t	en	The Buenos Aires City Council	1	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	33827	\N	\N	\N	\N	\N	\N	\N	\N	816563443	181369522	https://archive.org/download/dm-2530-247/DM%202530%20247%E2%80%A2%281%29%20Albinoni%20T.%2C%20Adagio%20en%20sol%20mineur.mp3	\N	\N	\N	-1	-1	\N
50303	50282	\N	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, presenta una visión orientalista, teatral y profundamente simbólica de la emperatriz bizantina Teodora, esposa del emperador Justiniano I, una de las mujeres más influyentes y poderosas en la historia del Imperio Romano de Oriente.\r\n\r\nNo se trata de una representación histórica literal, sino de una fantasía opulenta, propia de la fascinación europea del siglo XIX por el Oriente y por los antiguos imperios. Benjamin-Constant, reconocido por sus retratos de élite y figuras históricas, pone aquí en juego su virtuosismo técnico: el lujo, la sofisticación, las poses calculadas, y la representación minuciosa de texturas ricas como la seda, el terciopelo, el metal y la piel.\r\n\r\nLa emperatriz aparece representada de cuerpo entero, sentada en una posición elevada, casi como en un trono bajo. Su figura ocupa el centro del cuadro. El cuerpo está levemente girado hacia la izquierda, mientras que el rostro mira de frente al espectador, con una mirada fija, intensa y desafiante. No sonríe. Su expresión transmite autoridad, inteligencia y una calma segura, casi intimidante.\r\n\r\nViste una túnica larga y pesada, ricamente ornamentada, de tonos claros y dorados, que cae en pliegues profundos hasta el suelo. Sobre la cabeza lleva un elaborado tocado o corona, adornado con piedras preciosas que brillan sutilmente. Los brazos están cubiertos de joyas, pulseras y anillos, que refuerzan la idea de riqueza y poder. La piel clara contrasta con los colores cálidos del entorno.\r\n\r\nEl espacio que la rodea es cerrado y lujoso. Detrás de ella se perciben cortinados oscuros, posiblemente de terciopelo, y superficies decoradas con motivos orientales. La iluminación es suave pero dirigida: la luz parece caer principalmente sobre el rostro y el cuerpo de Teodora, separándola del fondo y convirtiéndola en el foco absoluto de la escena.\r\n\r\nDe origen humilde, Teodora se ganó la vida de joven como actriz y cortesana hasta que fue elegida por Justiniano como esposa. Las fuentes históricas antiguas, como Procopio de Cesarea, y más tarde Edward Gibbon, la describieron como astuta, inteligente y cruel, una imagen hoy considerada exagerada y condicionada por la visión rígida del rol femenino. Actualmente se la reconoce como co-gobernante de facto, una figura política central del Imperio Bizantino.\r\n\r\nDurante la revuelta de Niká en 532, convenció a Justiniano de no huir y enfrentar a sus enemigos con la célebre frase: “el púrpura es una buena mortaja”. Impulsó leyes a favor de los derechos de las mujeres, prohibió el tráfico sexual forzado, fortaleció derechos matrimoniales y fundó hogares para mujeres rescatadas de la prostitución, algo sin precedentes en su tiempo. Gracias a sus acciones, la situación de las mujeres en el Imperio Bizantino fue más avanzada que en gran parte de Europa.\r\n\r\nEn la historia argentina, el paralelismo inmediato es con Eva Perón.\r\n\r\nBenjamin-Constant buscó crear un ícono visual del poder femenino y del exotismo oriental. El resultado, quizás involuntario, es también un símbolo de modernidad: una mujer joven, poderosa, hermosa e inteligente, que sostiene la mirada del espectador y se atreve a ser distinta.	\N	\N	50337	50336	2026-02-08 11:46:25.023707-03	2026-02-08 11:46:25.023715-03	100	t	es	La Emperatriz Theodora	3	{"accesible-speed": "0.0", "fadeDurationSec": "13", "introDurationSec": "20", "accesible-stability": "0.0", "accesible-similarity": "0.0", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	-1863525103	1589747055	https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3	\N	\N	\N	0	0	\N
35496	35357	\N	El alférez real era uno de los principales funcionarios del Cabildo. Se encargaba de representar al rey en las ceremonias públicas portando el estandarte real. Además, se ocupaba de organizar las milicias en tiempos de guerra.\r\n\r\nEn las ceremonias públicas, el alférez real lucía un traje como este. La recreación fue realizada a partir del traje que perteneció a Francisco Antonio de Escalada, último alférez real del Cabildo de Buenos Aires, que forma parte de la colección del Museo. Está formado por calzón corto, chaleco y casaca. Se usaba con camisa blanca, medias del mismo color y zapatos con taco alto y grandes hebillas. En la época, este tipo de vestimenta era un signo de prestigio: contar con un traje como este era muy costoso.\r\n\r\nEsta recreación se realizó para proteger la pieza original de la exposición constante. Para replicar las medidas, se levantaron moldes a partir del traje original. \r\nSe seleccionó una tela similar y se la tiñó buscando una aproximación al color que tenía el traje en el momento de su uso.	\N	\N	35503	35502	2026-01-12 10:42:25.409476-03	2026-01-12 10:42:25.409486-03	101	t	es	Traje de calle del alférez real	1	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	1703197939	1466060882	https://dn721806.ca.archive.org/0/items/dm-2530-247/DM%202530%20247%E2%80%A2%282%29%20Pachebel%20J.%2C%20Canon%20et%20gigue%20en%20r%C3%A9%20mineur%20pour%203%20violons%20%26%20bc.mp3	\N	\N	\N	-1	-1	\N
50304	\N	\N	\N	\N	\N	\N	\N	2026-02-08 15:23:05.440382-03	2026-02-08 15:23:05.44041-03	100	t	en	La Emperatriz Theodora	1	\N	\N	\N	\N	\N	\N	50284	\N	\N	\N	0	0	\N	\N	\N	\N	0	0	\N
50305	\N	\N	\N	\N	\N	\N	\N	2026-02-08 19:00:04.704909-03	2026-02-08 19:00:04.704928-03	100	t	en	Museo Secreto	3	\N	50263	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N	\N	\N	\N	0	0	\N
50335	\N	50261	\N	\N	\N	\N	\N	2026-02-08 20:17:00.43344-03	2026-02-08 20:17:00.433451-03	100	t	es	Museo Secreto	3	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N	\N	\N	\N	0	0	\N
50347	\N	\N	A pintura Teodora, de Jean-Joseph Benjamin-Constant, de 1887, apresenta uma visão orientalista, teatral e profundamente simbólica da imperatriz bizantina Teodora, esposa do imperador Justiniano I, uma das mulheres mais influentes e poderosas da história do Império Romano do Oriente.\n\nNão se trata de uma representação histórica literal, mas sim de uma fantasia opulenta, característica do fascínio europeu do século XIX pelo Oriente e pelos impérios antigos. Benjamin-Constant, renomado por seus retratos de elites e figuras históricas, demonstra aqui seu virtuosismo técnico: o luxo, a sofisticação, as poses calculadas e a representação meticulosa de texturas ricas como seda, veludo, metal e couro.\n\nA imperatriz é retratada de corpo inteiro, sentada em uma posição elevada, quase como em um trono baixo. Sua figura ocupa o centro da pintura. O corpo está ligeiramente voltado para a esquerda, enquanto o rosto encara o observador com um olhar fixo, intenso e desafiador. Ela não sorri. Sua expressão transmite autoridade, inteligência e uma calma confiante, quase intimidante.\n\nEla veste um longo e pesado robe ricamente ornamentado em tons claros e dourados, que cai em profundas pregas até o chão. Na cabeça, usa um elaborado adorno ou coroa, cravejado de pedras preciosas que brilham sutilmente. Seus braços estão cobertos de joias, pulseiras e anéis, reforçando a ideia de riqueza e poder. Sua pele clara contrasta com as cores quentes do ambiente.\n\nO espaço ao seu redor é fechado e luxuoso. Atrás dela, cortinas escuras, possivelmente de veludo, e superfícies decoradas com motivos orientais são visíveis. A iluminação é suave, mas direcionada: a luz parece incidir principalmente sobre o rosto e o corpo de Teodora, separando-a do fundo e tornando-a o foco absoluto da cena.\n\nDe origem humilde, Teodora ganhava a vida como jovem atriz e cortesã até ser escolhida por Justiniano para ser sua esposa. Fontes históricas antigas, como Procópio de Cesareia e, posteriormente, Edward Gibbon, a descreveram como astuta, inteligente e cruel — uma imagem hoje considerada exagerada e influenciada por uma visão rígida do papel feminino. Atualmente, ela é reconhecida como uma corregente de fato, uma figura política central do Império Bizantino.\n\nDurante a Revolta de Nika, em 532, ela convenceu Justiniano a não fugir e a confrontar seus inimigos com a famosa frase: “A púrpura é uma bela mortalha”. Ela promoveu leis em favor dos direitos das mulheres, proibiu o tráfico sexual forçado, fortaleceu os direitos matrimoniais e fundou lares para mulheres resgatadas da prostituição — algo sem precedentes em sua época. Graças às suas ações, a situação das mulheres no Império Bizantino era mais avançada do que em grande parte da Europa.\n\nNa história argentina, o paralelo imediato é com Eva Perón.\n\nBenjamin-Constant buscava criar um ícone visual de poder feminino e exotismo oriental. O resultado, talvez não intencional, é também um símbolo da modernidade: uma mulher jovem, poderosa, bonita e inteligente que prende o olhar do espectador e ousa ser diferente.	\N	\N	\N	\N	2026-02-10 16:38:27.190838-03	2026-02-10 16:38:27.190845-03	100	t	pt-BR	Imperatriz Teodora	1	\N	\N	\N	\N	\N	\N	50285	\N	\N	\N	0	0	\N	\N	\N	\N	0	0	\N
50452	\N	\N	Legacies of Crafting includes vessels, baskets, textiles, knives, and stirrups from the National Arts Fund's collection.\r\n\r\nThe works highlight artisanal crafting as a living and collective practice, integral to Argentine cultural identity.\r\n\r\nThe exhibition also pays tribute to Teresa Anchorena, Director of Heritage and Crafts at the National Arts Fund, who passed away this year and was a driving force behind this initiative. Her work and commitment were key to promoting the organization's heritage and building a respectful and active relationship with the territories, crafts, and communities that, since ancestral times, have constructed this legacy that is now shared with the public.	\N	\N	50715	50459	2026-02-11 16:18:41.349325-03	2026-02-11 16:18:41.349344-03	100	t	en	Legacies of doing	3	{"speed": "0.9", "voiceid": "dAlhI9qAHVIjXuVppzhW", "startSec": "6", "stability": "0.82", "similarity": "0.53", "fadeDurationSec": "12", "introDurationSec": "25", "voiceOverlapDurationSec": "3"}	50445	\N	\N	\N	\N	\N	\N	\N	\N	241722447	989717050	\N	\N	\N	\N	0	0	\N
50460	\N	\N	A exposição "Legados do Artesanato" inclui peças como vasos, cestos, têxteis, facas e estribos da coleção do Fundo Nacional de Artes.\n\nAs obras destacam o artesanato como uma prática viva e coletiva, parte integrante da identidade cultural argentina.\n\nA exposição também presta homenagem a Teresa Anchorena, Diretora de Patrimônio e Artesanato do Fundo Nacional de Artes, falecida este ano e uma força motriz por trás desta iniciativa. Seu trabalho e dedicação foram fundamentais para a promoção do patrimônio da organização e para a construção de uma relação respeitosa e ativa com os territórios, os ofícios e as comunidades que, desde tempos ancestrais, construíram esse legado que agora é compartilhado com o público.	\N	\N	\N	\N	2026-02-11 17:13:49.953643-03	2026-02-11 17:13:49.953652-03	100	t	pt-BR	Legados do fazer	3	\N	50446	\N	\N	\N	\N	\N	\N	\N	\N	0	0	\N	\N	\N	\N	0	0	\N
828	412	\N	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, representa una visión orientalista y teatral de la emperatriz bizantina Teodora, esposa de Justiniano I,  la mujer más influyente y poderosa en la historia del Imperio romano de Oriente.\r\n\r\nNo es una representación histórica, sino más bien una fantasía opulenta basada en la fascinación europea del siglo XIX por el Oriente y el pasado imperial. Benjamin-Constant era un pintor de gran técnica especializado en retratos de élite y figuras históricas: poder, lujo,  sofisticación, poses calculadas,  texturas complejas como seda y terciopelo, y entornos refinados. \r\n\r\nDe origen humilde, Theodora se ganó la vida de joven como actriz y cortesana hasta que fue elegida por Justiniano como esposa. La imagen histórica -basada en Procopio de Cesarea en la antigüedad  y Gibbon en el siglo XVIII-  es  la de una mujer astuta, inteligente y cruel. En la actualidad se considera que ese perfil es exagerado por la visión rígida que se tenía de la función social de la mujer. La visión moderna es que fue una figura política central en el Imperio Bizantino,  co-gobernante de facto con Justiniano. Participó activamente en las decisiones de gobierno. En la revuelta de Niká de 532, convenció a Justiniano de no huir y enfrentar a sus enemigos con su famosa frase -“el púrpura es una buena mortaja”-. Promovió leyes en favor de los derechos de las mujeres: prohibió el tráfico sexual forzado,  fortaleció los derechos de las mujeres en el matrimonio y el divorcio, y fundó hogares para mujeres rescatadas de la prostitución, algo sin precedentes en la época. Como resultado de los esfuerzos de Teodora, el estado de la mujer en el Imperio Bizantino fue más elevado que el del resto de las mujeres en Europa.\r\n\r\nEn la historia argentina el paralelismo inmediato es con Eva Perón.\r\n\r\nBenjamin-Constant se propuso crear un ícono visual del poder femenino y del exotismo oriental. y lo que salió, quizás involuntariamente,  es todo un símbolo de modernidad: una mujer joven, poderosa, hermosa e inteligente con mirada desafiante que se atreve a ser distinta.	\N	\N	\N	50464	2025-11-21 10:15:45.817915-03	2025-11-21 10:15:45.817929-03	100	f	es	La Emperatriz Theodora	\N	{"speed": "0.9", "voiceid": "LudcwvHIZaqQOcQfVZSY", "stability": "0.82", "similarity": "0.53"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	-588260489	0	\N	\N	\N	\N	-1	-1	\N
50465	\N	\N	O Retrato de Juan Manuel de Rosas, pintado pelo artista francês Raymond Monvoisin por volta de 1842, oferece uma representação simbólica e estratégica do caudilho argentino. Ao contrário de outros retratos oficiais em que Rosas aparece em uniforme militar, nesta obra ele é retratado usando um poncho, destacando sua imagem como líder popular e não meramente como figura militar. Monvoisin havia sido formado na tradição acadêmica europeia, mas em 1842 já se aproximava do Romantismo.\n\nRosas aparece de perfil, com uma expressão séria e firme, enfatizando sua autoridade, e seu olhar está voltado para a origem do vento, como se controlasse a natureza. Mitre observou: "É o retrato mais preciso que conheço de Rosas; há muito do imperador romano nele, mas suavizado e corrigido por um sotaque crioulo."\n\nA tradição artística oficial argentina considera a pintura um estudo preliminar para uma obra posterior que desapareceu ou nunca existiu. Segundo Rodrigo Cañete em “Historia a contrapelo del arte argentino” (História contra a corrente da arte argentina), essa ideia é contaminada pelo preconceito de se recusar a aceitar a verdadeira natureza da obra.\n\nO artista, acolhido no Chile pelos inimigos de Rosas, tinha o talento de criar uma pintura ambígua para que ambos os lados — unitaristas e federalistas — pudessem usá-la como símbolo. Os traços de Rosas são imperiais, mas carecem da musculatura heroica dos imperadores e são ligeiramente feminizados, e o poncho e as vestes são semelhantes aos retratos de seus inimigos como um gaúcho bárbaro, líder do que Sarmiento chamava desdenhosamente de “turba árabe”.\n\nPara os partidários de Rosas, ele representa um imperador-deus-benfeitor-mãe terra, no estilo da Virgem Maria em imagens sul-americanas que a fundem com a Pachamama indígena, como a Virgem Maria de Cerro Rico em Potosí, mas com sutis referências acadêmicas a grandes mestres como Zurbarán e Velázquez.	\N	\N	\N	50466	2026-02-11 17:47:52.726063-03	2026-02-11 17:47:52.726085-03	100	t	pt-BR	Retrato de Juan Manuel de Rosas	\N	{"speed": "0.9", "voiceid": "AaeZyyi87RCxtFnHPS3e", "stability": "0.82", "similarity": "0.53"}	\N	\N	\N	\N	\N	741	\N	\N	\N	-983414512	0	\N	\N	\N	\N	0	0	\N
34074	\N	33825	La historia del Cabildo comenzó en 1580. Ese año, Juan de Garay fundó la ciudad de La Trinidad, que más tarde pasó a llamarse por el nombre de su puerto: Santa María del Buen Ayre.\r\n\r\nLos cabildos se originaron en España como forma de administración de las ciudades y sus alrededores. Luego fueron trasladados a los dominios americanos. Según las Leyes de Indias, que regulaban la vida en los territorios del Imperio español, toda ciudad debía contar con un Cabildo. Este se ubicaba siempre en la plaza principal, junto a otras instituciones importantes, como la iglesia mayor. En Buenos Aires contenía, además, la cárcel urbana.\r\n\r\nEl Cabildo era la única autoridad elegida por la sociedad local. Los virreyes, los gobernadores y otros funcionarios importantes eran nombrados desde España. En cambio, los miembros del Cabildo representaban a los habitantes de Buenos Aires. Solo los llamados "vecinos" (varones, blancos y con prestigio social) podían integrarlo. Los cabildantes se reunían periódicamente para discutir asuntos importantes para la comunidad.\r\n\r\nHacia fines del siglo XVIII, Buenos Aires pasó de ser una ciudad marginal del Imperio español a convertirse en la capital del Virreinato del Río de la Plata. Su importancia creciente implicó que el Cabildo asumiera cada vez más facultades.\r\n\r\nEl Cabildo tuvo un papel político fundamental en los años posteriores a la revolución de 1810. En 1821, fue disuelto. Desde entonces, la organización de la ciudad quedó en manos de la Legislatura de Buenos Aires, creada un año antes.	\N	\N	50598	34080	2026-01-11 16:13:00.089788-03	2026-01-11 16:13:00.089793-03	101	t	es	El Cabildo de Buenos Aires	1	{"speed": "0.0", "stability": "0.0", "similarity": "0.0", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	784487367	1453432749	https://dn720709.ca.archive.org/0/items/atahualpa-yupanqui-camino-del-indio/04Una%20Cancion%20En%20La%20Monta%C3%B1a.mp3	\N	\N	\N	-1	-1	\N
50450	\N	50443	Legados del hacer incluye vasijas, cestos, textiles, facones y estribos de la colección del Fondo Nacional de las Artes. \r\nLas obras ponen en valor el hacer artesanal como una práctica viva y colectiva, constitutiva de la identidad cultural argentina.\r\n\r\nLa muestra rinde también homenaje a Teresa Anchorena, directora de Patrimonio y Artesanías del FNA fallecida este año e impulsora de esta iniciativa. Su labor y compromiso fueron clave para la puesta en valor del patrimonio del organismo y para construir una relación respetuosa y activa con los territorios, los oficios y las comunidades que, desde tiempos ancestrales, construyen ese legado que hoy se comparte con el público.	\N	\N	50669	50595	2026-02-11 15:08:30.46821-03	2026-02-11 15:08:30.468215-03	100	t	es	Legados del hacer	3	{"speed": "0.9", "voiceid": "9rvdnhrYoXoUt4igKpBw", "startSec": "0", "stability": "0.82", "similarity": "0.53", "fadeDurationSec": "12", "introDurationSec": "20", "voiceOverlapDurationSec": "5"}	\N	\N	\N	\N	\N	\N	\N	\N	\N	1939546285	-1193603813	\N	\N	\N	\N	0	0	\N
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
1097	2026-01-21 04:29:06.382567-03	100	0	Institution	42202	\N	\N	\N
1098	2026-01-21 04:29:06.386857-03	100	0	InstitutionRecord	42203	\N	\N	\N
1099	2026-01-21 04:29:06.388114-03	100	0	InstitutionRecord	42204	\N	\N	\N
1100	2026-01-21 04:29:06.389382-03	100	0	InstitutionRecord	42205	\N	\N	\N
1101	2026-01-21 04:29:06.390039-03	100	2	Institution	42202	\N	\N	\N
1102	2026-01-21 04:29:06.392055-03	100	0	Role	42206	\N	\N	\N
1103	2026-01-21 04:33:59.554215-03	101	2	Institution	42202	name, shortName, website	\N	\N
1104	2026-01-21 04:50:37.370056-03	100	0	Resource	42257	\N	\N	\N
1105	2026-01-21 04:50:37.386568-03	101	2	Institution	42202	photo	\N	\N
1106	2026-01-21 04:55:46.991636-03	101	2	Institution	42202	photo, info, address	\N	\N
1107	2026-01-21 04:56:29.656088-03	100	0	SiteRecord	42269	\N	\N	\N
1108	2026-01-21 04:56:29.657238-03	100	0	SiteRecord	42270	\N	\N	\N
1109	2026-01-21 04:56:29.658061-03	100	0	SiteRecord	42271	\N	\N	\N
1110	2026-01-21 04:56:29.658468-03	100	0	Site	42268	\N	\N	\N
1111	2026-01-21 04:56:29.658922-03	100	2	Institution	42202	add site	\N	\N
1112	2026-01-21 04:56:29.661249-03	100	0	Role	42272	\N	\N	\N
1113	2026-01-21 04:56:29.662326-03	100	0	Role	42273	\N	\N	\N
1114	2026-01-21 04:58:21.270632-03	101	2	Site	42268	sortAlphabetical, mapurl	\N	\N
1115	2026-01-21 05:40:16.036012-03	100	0	ArtExhibition	42429	\N	\N	\N
1116	2026-01-21 05:40:16.040244-03	100	0	ArtExhibitionRecord	42430	\N	\N	\N
1117	2026-01-21 05:40:16.041576-03	100	0	ArtExhibitionRecord	42431	\N	\N	\N
1118	2026-01-21 05:40:16.042567-03	100	0	ArtExhibitionRecord	42432	\N	\N	\N
1119	2026-01-21 05:41:03.060445-03	100	0	Resource	42508	\N	\N	\N
1120	2026-01-21 05:41:03.070388-03	101	2	ArtExhibition	42429	permanent, name, photo	\N	\N
1121	2026-01-21 05:41:52.52998-03	101	2	ArtExhibition	42429	permanent, name, photo	\N	\N
1122	2026-01-21 09:36:05.194171-03	101	2	ArtExhibition	42429	name	\N	\N
1123	2026-01-21 09:48:23.152439-03	100	0	ArtWork	42814	\N	\N	\N
1124	2026-01-21 09:48:23.156029-03	100	0	ArtWorkRecord	42815	\N	\N	\N
1125	2026-01-21 09:48:23.157933-03	100	0	ArtWorkRecord	42816	\N	\N	\N
1126	2026-01-21 09:48:23.159269-03	100	0	ArtWorkRecord	42817	\N	\N	\N
1127	2026-01-21 09:49:11.570589-03	100	0	Resource	42893	\N	\N	\N
1128	2026-01-21 09:49:11.578213-03	101	2	ArtWork	42814	objectType, source, name, photo	\N	\N
1129	2026-01-21 09:54:04.577433-03	101	2	ArtWork	42814	objectType	\N	\N
1130	2026-01-21 09:59:04.137364-03	101	2	ArtWork	42814	objectType, info	\N	\N
1131	2026-01-21 10:00:06.424772-03	100	0	ArtWork	43114	\N	\N	\N
1132	2026-01-21 10:00:06.427157-03	100	0	ArtWorkRecord	43115	\N	\N	\N
1133	2026-01-21 10:00:06.428124-03	100	0	ArtWorkRecord	43116	\N	\N	\N
1134	2026-01-21 10:00:06.428889-03	100	0	ArtWorkRecord	43117	\N	\N	\N
1135	2026-01-21 10:03:05.65974-03	100	0	Resource	43193	\N	\N	\N
1136	2026-01-21 10:03:05.664896-03	101	2	ArtWork	43114	objectType, source, name, photo	\N	\N
1137	2026-02-04 16:05:05.314115-03	101	2	GuideContentRecord	747	translate	\N	\N
1138	2026-02-04 17:16:47.345851-03	101	2	Institution	529	translate	\N	\N
1139	2026-02-04 22:04:16.353176-03	101	2	Site	137	translate	\N	\N
1140	2026-02-07 07:01:52.994762-03	100	2	Institution	529	languages	\N	\N
1141	2026-02-07 07:02:08.902481-03	100	2	Institution	529	languages	\N	\N
1142	2026-02-07 07:02:59.920263-03	100	2	User	100	locale	\N	\N
1143	2026-02-07 07:25:09.307168-03	100	2	GuideContent	414	subtitle	\N	\N
1144	2026-02-07 07:25:15.029512-03	100	2	GuideContent	414	subtitle	\N	\N
1145	2026-02-07 17:01:21.171374-03	100	2	ArtExhibition	454	translate	\N	\N
1146	2026-02-07 17:05:47.040339-03	100	0	ArtExhibition	50254	\N	\N	\N
1147	2026-02-07 17:05:47.043988-03	100	0	ArtExhibitionRecord	50255	\N	\N	\N
1148	2026-02-07 17:05:47.045034-03	100	0	ArtExhibitionRecord	50256	\N	\N	\N
1149	2026-02-07 17:05:47.04589-03	100	0	ArtExhibitionRecord	50257	\N	\N	\N
1150	2026-02-07 17:05:47.046903-03	100	0	ArtExhibitionRecord	50258	\N	\N	\N
1151	2026-02-07 17:05:47.047783-03	100	0	ArtExhibitionRecord	50259	\N	\N	\N
1152	2026-02-07 17:05:47.048574-03	100	0	ArtExhibitionRecord	50260	\N	\N	\N
1153	2026-02-07 17:06:36.442447-03	100	2	ArtExhibition	50254	translate	\N	\N
1154	2026-02-07 17:06:59.392133-03	100	0	ArtExhibitionGuide	50261	\N	\N	\N
1155	2026-02-07 17:06:59.393048-03	100	0	ArtExhibitionGuideRecord	50262	\N	\N	\N
1156	2026-02-07 17:06:59.393545-03	100	0	ArtExhibitionGuideRecord	50263	\N	\N	\N
1157	2026-02-07 17:06:59.393984-03	100	0	ArtExhibitionGuideRecord	50264	\N	\N	\N
1158	2026-02-07 17:06:59.394424-03	100	0	ArtExhibitionGuideRecord	50265	\N	\N	\N
1159	2026-02-07 17:06:59.3949-03	100	0	ArtExhibitionGuideRecord	50266	\N	\N	\N
1160	2026-02-07 17:06:59.395361-03	100	0	ArtExhibitionGuideRecord	50267	\N	\N	\N
1161	2026-02-07 17:07:31.187594-03	100	2	ArtExhibitionGuide	50261	accesible, name	\N	\N
1162	2026-02-07 17:14:11.318695-03	100	2	ArtExhibitionGuide	50261	translate	\N	\N
1163	2026-02-07 17:20:52.792956-03	100	2	ArtExhibitionGuide	50261	translate	\N	\N
1164	2026-02-07 20:18:34.173885-03	100	2	ArtExhibitionGuide	50261	name	\N	\N
1165	2026-02-07 21:00:27.650989-03	100	0	GuideContent	50268	\N	\N	\N
1166	2026-02-07 21:00:27.657385-03	100	0	GuideContentRecord	50269	\N	\N	\N
1167	2026-02-07 21:00:27.657993-03	100	0	GuideContentRecord	50270	\N	\N	\N
1168	2026-02-07 21:00:27.658534-03	100	0	GuideContentRecord	50271	\N	\N	\N
1169	2026-02-07 21:00:27.658955-03	100	0	GuideContentRecord	50272	\N	\N	\N
1170	2026-02-07 21:00:27.659715-03	100	0	GuideContentRecord	50273	\N	\N	\N
1171	2026-02-07 21:00:27.660173-03	100	0	GuideContentRecord	50274	\N	\N	\N
1172	2026-02-07 21:12:16.881989-03	100	0	GuideContent	50275	\N	\N	\N
1173	2026-02-07 21:12:16.883707-03	100	0	GuideContentRecord	50276	\N	\N	\N
1174	2026-02-07 21:12:16.884374-03	100	0	GuideContentRecord	50277	\N	\N	\N
1175	2026-02-07 21:12:16.885044-03	100	0	GuideContentRecord	50278	\N	\N	\N
1176	2026-02-07 21:12:16.885696-03	100	0	GuideContentRecord	50279	\N	\N	\N
1177	2026-02-07 21:12:16.886403-03	100	0	GuideContentRecord	50280	\N	\N	\N
1178	2026-02-07 21:12:16.887225-03	100	0	GuideContentRecord	50281	\N	\N	\N
1179	2026-02-07 21:12:42.225515-03	100	0	GuideContent	50282	\N	\N	\N
1180	2026-02-07 21:12:42.226339-03	100	0	GuideContentRecord	50283	\N	\N	\N
1181	2026-02-07 21:12:42.226964-03	100	0	GuideContentRecord	50284	\N	\N	\N
1182	2026-02-07 21:12:42.227717-03	100	0	GuideContentRecord	50285	\N	\N	\N
1183	2026-02-07 21:12:42.228582-03	100	0	GuideContentRecord	50286	\N	\N	\N
1184	2026-02-07 21:12:42.229322-03	100	0	GuideContentRecord	50287	\N	\N	\N
1185	2026-02-07 21:12:42.229965-03	100	0	GuideContentRecord	50288	\N	\N	\N
1186	2026-02-07 21:12:48.341632-03	100	0	GuideContent	50289	\N	\N	\N
1187	2026-02-07 21:12:48.342557-03	100	0	GuideContentRecord	50290	\N	\N	\N
1188	2026-02-07 21:12:48.343374-03	100	0	GuideContentRecord	50291	\N	\N	\N
1189	2026-02-07 21:12:48.344178-03	100	0	GuideContentRecord	50292	\N	\N	\N
1190	2026-02-07 21:12:48.345374-03	100	0	GuideContentRecord	50293	\N	\N	\N
1191	2026-02-07 21:12:48.346265-03	100	0	GuideContentRecord	50294	\N	\N	\N
1192	2026-02-07 21:12:48.347137-03	100	0	GuideContentRecord	50295	\N	\N	\N
1193	2026-02-07 21:12:50.15039-03	100	0	GuideContent	50296	\N	\N	\N
1194	2026-02-07 21:12:50.151556-03	100	0	GuideContentRecord	50297	\N	\N	\N
1195	2026-02-07 21:12:50.152472-03	100	0	GuideContentRecord	50298	\N	\N	\N
1196	2026-02-07 21:12:50.153307-03	100	0	GuideContentRecord	50299	\N	\N	\N
1197	2026-02-07 21:12:50.154207-03	100	0	GuideContentRecord	50300	\N	\N	\N
1198	2026-02-07 21:12:50.155007-03	100	0	GuideContentRecord	50301	\N	\N	\N
1199	2026-02-07 21:12:50.157514-03	100	0	GuideContentRecord	50302	\N	\N	\N
1200	2026-02-07 21:13:11.924587-03	100	2	GuideContent	50282	translate	\N	\N
1201	2026-02-08 11:46:25.040059-03	100	0	AudioStudio	50303	\N	\N	\N
1202	2026-02-08 15:23:05.460435-03	100	0	AudioStudio	50304	\N	\N	\N
1203	2026-02-08 19:00:04.720631-03	100	0	AudioStudio	50305	\N	\N	\N
1204	2026-02-08 19:05:59.21228-03	100	0	ArtExhibitionGuide	50306	\N	\N	\N
1205	2026-02-08 19:05:59.213298-03	100	0	ArtExhibitionGuideRecord	50307	\N	\N	\N
1206	2026-02-08 19:05:59.213884-03	100	0	ArtExhibitionGuideRecord	50308	\N	\N	\N
1207	2026-02-08 19:05:59.214609-03	100	0	ArtExhibitionGuideRecord	50309	\N	\N	\N
1208	2026-02-08 19:05:59.21493-03	100	0	ArtExhibitionGuideRecord	50310	\N	\N	\N
1209	2026-02-08 19:05:59.215303-03	100	0	ArtExhibitionGuideRecord	50311	\N	\N	\N
1210	2026-02-08 19:05:59.215589-03	100	0	ArtExhibitionGuideRecord	50312	\N	\N	\N
1211	2026-02-08 19:06:07.334953-03	100	0	ArtExhibitionGuide	50313	\N	\N	\N
1212	2026-02-08 19:06:07.336537-03	100	0	ArtExhibitionGuideRecord	50314	\N	\N	\N
1213	2026-02-08 19:06:07.337174-03	100	0	ArtExhibitionGuideRecord	50315	\N	\N	\N
1214	2026-02-08 19:06:07.337807-03	100	0	ArtExhibitionGuideRecord	50316	\N	\N	\N
1215	2026-02-08 19:06:07.33836-03	100	0	ArtExhibitionGuideRecord	50317	\N	\N	\N
1216	2026-02-08 19:06:07.338907-03	100	0	ArtExhibitionGuideRecord	50318	\N	\N	\N
1217	2026-02-08 19:06:07.339379-03	100	0	ArtExhibitionGuideRecord	50319	\N	\N	\N
1218	2026-02-08 19:06:16.953724-03	100	2	ArtExhibitionGuide	50313	accesible	\N	\N
1219	2026-02-08 20:17:00.451719-03	100	0	AudioStudio	50335	\N	\N	\N
1220	2026-02-08 20:24:36.249035-03	100	2	GuideContent	50282	info	\N	\N
1221	2026-02-08 20:26:52.601513-03	100	0	Resource	50336	\N	\N	\N
1222	2026-02-08 20:26:52.608449-03	100	2	AudioStudio	50303	generate audio voice	\N	\N
1223	2026-02-08 20:27:46.773266-03	100	0	Resource	50337	\N	\N	\N
1224	2026-02-08 20:27:46.77887-03	100	2	AudioStudio	50303	add music	\N	\N
1225	2026-02-08 20:28:01.770717-03	100	2	GuideContent	50282	integrate audioStudio	\N	\N
1226	2026-02-10 09:28:04.404405-03	100	2	GuideContent	413	info	\N	\N
1227	2026-02-10 16:00:33.951333-03	100	0	ArtExhibition	50340	\N	\N	\N
1228	2026-02-10 16:00:33.954059-03	100	0	ArtExhibitionRecord	50341	\N	\N	\N
1229	2026-02-10 16:00:33.955121-03	100	0	ArtExhibitionRecord	50342	\N	\N	\N
1230	2026-02-10 16:00:33.955616-03	100	0	ArtExhibitionRecord	50343	\N	\N	\N
1231	2026-02-10 16:00:33.956361-03	100	0	ArtExhibitionRecord	50344	\N	\N	\N
1232	2026-02-10 16:00:33.956913-03	100	0	ArtExhibitionRecord	50345	\N	\N	\N
1233	2026-02-10 16:00:33.957361-03	100	0	ArtExhibitionRecord	50346	\N	\N	\N
1234	2026-02-10 16:28:57.354614-03	100	2	ArtExhibition	50340	translate	\N	\N
1235	2026-02-10 16:38:22.880424-03	100	2	GuideContentRecord	50285	translate	\N	\N
1236	2026-02-10 16:38:27.193401-03	100	0	AudioStudio	50347	\N	\N	\N
1237	2026-02-10 18:51:24.765576-03	100	0	Institution	50359	\N	\N	\N
1238	2026-02-10 18:51:24.768577-03	100	0	InstitutionRecord	50360	\N	\N	\N
1239	2026-02-10 18:51:24.769611-03	100	0	InstitutionRecord	50361	\N	\N	\N
1240	2026-02-10 18:51:24.770362-03	100	0	InstitutionRecord	50362	\N	\N	\N
1241	2026-02-10 18:51:24.771467-03	100	0	InstitutionRecord	50363	\N	\N	\N
1242	2026-02-10 18:51:24.773045-03	100	0	InstitutionRecord	50364	\N	\N	\N
1243	2026-02-10 18:51:24.773931-03	100	0	InstitutionRecord	50365	\N	\N	\N
1244	2026-02-10 18:51:24.774222-03	100	2	Institution	50359	\N	\N	\N
1245	2026-02-10 18:51:24.780967-03	100	0	Role	50366	\N	\N	\N
1246	2026-02-10 18:55:58.195614-03	100	2	Institution	50359	name, website	\N	\N
1247	2026-02-10 18:59:23.435024-03	100	0	Resource	50367	\N	\N	\N
1248	2026-02-10 18:59:23.452818-03	100	2	Institution	50359	name, website, info, photo	\N	\N
1249	2026-02-10 18:59:59.283849-03	100	2	Institution	50359	name, website, info, photo, subtitle	\N	\N
1250	2026-02-10 19:01:16.916259-03	100	2	Institution	50359	name, website, info, photo, subtitle	\N	\N
1251	2026-02-10 19:01:46.767994-03	100	0	SiteRecord	50369	\N	\N	\N
1252	2026-02-10 19:01:46.768589-03	100	0	SiteRecord	50370	\N	\N	\N
1253	2026-02-10 19:01:46.769466-03	100	0	SiteRecord	50371	\N	\N	\N
1254	2026-02-10 19:01:46.769847-03	100	0	SiteRecord	50372	\N	\N	\N
1255	2026-02-10 19:01:46.770168-03	100	0	SiteRecord	50373	\N	\N	\N
1256	2026-02-10 19:01:46.770481-03	100	0	SiteRecord	50374	\N	\N	\N
1257	2026-02-10 19:01:46.770633-03	100	0	Site	50368	\N	\N	\N
1258	2026-02-10 19:01:46.770848-03	100	2	Institution	50359	add site	\N	\N
1259	2026-02-10 19:01:46.772471-03	100	0	Role	50375	\N	\N	\N
1260	2026-02-10 19:01:46.772902-03	100	0	Role	50376	\N	\N	\N
1261	2026-02-10 19:03:31.598036-03	100	2	Site	50368	opens, address	\N	\N
1262	2026-02-10 22:29:01.583091-03	100	0	Resource	50377	\N	\N	\N
1263	2026-02-10 22:29:01.594782-03	100	2	AudioStudio	796	add music	\N	\N
1264	2026-02-10 22:29:27.043655-03	100	2	ArtExhibitionGuide	411	integrate audioStudio	\N	\N
1265	2026-02-11 07:46:59.004133-03	100	0	Resource	50378	\N	\N	\N
1266	2026-02-11 07:46:59.013103-03	100	2	AudioStudio	796	add music	\N	\N
1267	2026-02-11 08:21:31.22154-03	100	2	ArtExhibitionGuideRecord	735	integrate audioStudio	\N	\N
1268	2026-02-11 09:06:17.630381-03	100	0	Institution	50384	\N	\N	\N
1269	2026-02-11 09:06:17.633991-03	100	0	InstitutionRecord	50385	\N	\N	\N
1270	2026-02-11 09:06:17.635307-03	100	0	InstitutionRecord	50386	\N	\N	\N
1271	2026-02-11 09:06:17.636089-03	100	0	InstitutionRecord	50387	\N	\N	\N
1272	2026-02-11 09:06:17.637173-03	100	0	InstitutionRecord	50388	\N	\N	\N
1273	2026-02-11 09:06:17.638476-03	100	0	InstitutionRecord	50389	\N	\N	\N
1274	2026-02-11 09:06:17.639211-03	100	0	InstitutionRecord	50390	\N	\N	\N
1275	2026-02-11 09:06:17.639622-03	100	2	Institution	50384	\N	\N	\N
1276	2026-02-11 09:06:17.642114-03	100	0	Role	50391	\N	\N	\N
1277	2026-02-11 09:08:44.533128-03	100	0	Resource	50392	\N	\N	\N
1278	2026-02-11 09:08:44.545917-03	100	2	Institution	50384	languages, info, photo	\N	\N
1279	2026-02-11 09:10:09.629528-03	100	2	Institution	50384	languages, info, photo, address	\N	\N
1280	2026-02-11 09:10:13.966763-03	100	0	SiteRecord	50394	\N	\N	\N
1281	2026-02-11 09:10:13.967442-03	100	0	SiteRecord	50395	\N	\N	\N
1282	2026-02-11 09:10:13.96834-03	100	0	SiteRecord	50396	\N	\N	\N
1283	2026-02-11 09:10:13.969094-03	100	0	SiteRecord	50397	\N	\N	\N
1284	2026-02-11 09:10:13.96959-03	100	0	SiteRecord	50398	\N	\N	\N
1285	2026-02-11 09:10:13.969985-03	100	0	SiteRecord	50399	\N	\N	\N
1286	2026-02-11 09:10:13.970257-03	100	0	Site	50393	\N	\N	\N
1287	2026-02-11 09:10:13.970503-03	100	2	Institution	50384	add site	\N	\N
1288	2026-02-11 09:10:13.972075-03	100	0	Role	50400	\N	\N	\N
1289	2026-02-11 09:10:13.972583-03	100	0	Role	50401	\N	\N	\N
1290	2026-02-11 09:16:28.978633-03	100	2	Site	50393	permanentExhibitionsLabel, temporaryExhibitionsLabel, languages, opens, mapurl	\N	\N
1291	2026-02-11 09:17:13.408366-03	100	2	Site	50393	name	\N	\N
1292	2026-02-11 09:23:32.51565-03	100	0	Resource	50402	\N	\N	\N
1293	2026-02-11 09:23:32.527113-03	100	2	Site	50393	logo	\N	\N
1294	2026-02-11 09:28:22.988345-03	100	0	Resource	50403	\N	\N	\N
1295	2026-02-11 09:28:22.993891-03	100	2	Site	50393	logo	\N	\N
1296	2026-02-11 09:31:33.774491-03	100	2	Site	50393	translate	\N	\N
1297	2026-02-11 09:32:27.627453-03	100	2	SiteRecord	50398	translate	\N	\N
1298	2026-02-11 09:36:15.194408-03	100	0	Person	50404	\N	\N	\N
1299	2026-02-11 09:36:15.198203-03	100	0	PersonRecord	50405	\N	\N	\N
1300	2026-02-11 09:36:15.202384-03	100	0	PersonRecord	50406	\N	\N	\N
1301	2026-02-11 09:36:15.204297-03	100	0	PersonRecord	50407	\N	\N	\N
1302	2026-02-11 09:36:15.206603-03	100	0	PersonRecord	50408	\N	\N	\N
1303	2026-02-11 09:36:15.208188-03	100	0	PersonRecord	50409	\N	\N	\N
1304	2026-02-11 09:36:15.209949-03	100	0	PersonRecord	50410	\N	\N	\N
1305	2026-02-11 09:38:10.945482-03	100	0	Resource	50411	\N	\N	\N
1306	2026-02-11 09:38:10.954648-03	100	2	Person	50404	name, lastname, phone, photo	\N	\N
1307	2026-02-11 09:38:26.883182-03	100	2	Person	50404	name, lastname, phone, photo, state	\N	\N
1308	2026-02-11 09:45:48.65957-03	100	0	User	50412	\N	\N	\N
1309	2026-02-11 09:46:06.098679-03	100	2	User	50412	username	\N	\N
1310	2026-02-11 09:50:33.184566-03	100	2	User	50412	add role	{"id": "936", "name": "admin", "class": "RoleGeneral", "action": "2", "subaction": "add role"}	\N
1311	2026-02-11 09:50:38.16295-03	100	2	User	50412	add role	{"id": "50400", "name": "admin", "class": "RoleSite", "action": "2", "subaction": "add role"}	\N
1312	2026-02-11 09:50:39.381122-03	100	2	User	50412	add role	{"id": "50401", "name": "editor", "class": "RoleSite", "action": "2", "subaction": "add role"}	\N
1313	2026-02-11 09:53:53.120388-03	100	2	Institution	50384	name	\N	\N
1314	2026-02-11 09:54:12.633926-03	100	2	Institution	50384	translate	\N	\N
1315	2026-02-11 14:43:31.281151-03	100	0	Resource	50434	\N	\N	\N
1316	2026-02-11 14:43:31.292464-03	100	2	Site	50393	photo	\N	\N
1317	2026-02-11 14:50:46.662355-03	100	0	ArtExhibition	50435	\N	\N	\N
1318	2026-02-11 14:50:46.667283-03	100	0	ArtExhibitionRecord	50436	\N	\N	\N
1319	2026-02-11 14:50:46.668345-03	100	0	ArtExhibitionRecord	50437	\N	\N	\N
1320	2026-02-11 14:50:46.669103-03	100	0	ArtExhibitionRecord	50438	\N	\N	\N
1321	2026-02-11 14:50:46.669877-03	100	0	ArtExhibitionRecord	50439	\N	\N	\N
1322	2026-02-11 14:50:46.670642-03	100	0	ArtExhibitionRecord	50440	\N	\N	\N
1323	2026-02-11 14:50:46.671154-03	100	0	ArtExhibitionRecord	50441	\N	\N	\N
1324	2026-02-11 14:51:44.128734-03	100	0	Resource	50442	\N	\N	\N
1325	2026-02-11 14:51:44.143276-03	100	2	ArtExhibition	50435	name, subtitle, info, photo, from, to	\N	\N
1326	2026-02-11 14:52:13.663173-03	100	2	ArtExhibition	50435	translate	\N	\N
1327	2026-02-11 14:53:25.387936-03	100	0	ArtExhibitionGuide	50443	\N	\N	\N
1328	2026-02-11 14:53:25.389906-03	100	0	ArtExhibitionGuideRecord	50444	\N	\N	\N
1329	2026-02-11 14:53:25.390499-03	100	0	ArtExhibitionGuideRecord	50445	\N	\N	\N
1330	2026-02-11 14:53:25.391116-03	100	0	ArtExhibitionGuideRecord	50446	\N	\N	\N
1331	2026-02-11 14:53:25.391674-03	100	0	ArtExhibitionGuideRecord	50447	\N	\N	\N
1332	2026-02-11 14:53:25.392225-03	100	0	ArtExhibitionGuideRecord	50448	\N	\N	\N
1333	2026-02-11 14:53:25.392647-03	100	0	ArtExhibitionGuideRecord	50449	\N	\N	\N
1334	2026-02-11 15:05:43.329174-03	100	2	ArtExhibitionGuide	50443	info	\N	\N
1335	2026-02-11 15:08:10.891181-03	100	2	ArtExhibitionGuide	50443	subtitle	\N	\N
1336	2026-02-11 15:08:30.470179-03	100	0	AudioStudio	50450	\N	\N	\N
1337	2026-02-11 15:19:58.577477-03	100	0	Resource	50451	\N	\N	\N
1338	2026-02-11 15:19:58.589533-03	100	2	AudioStudio	50450	generate audio voice	\N	\N
1339	2026-02-11 16:18:36.635825-03	100	2	ArtExhibitionGuideRecord	50445	translate	\N	\N
1340	2026-02-11 16:18:41.35186-03	100	0	AudioStudio	50452	\N	\N	\N
1341	2026-02-11 16:19:26.722779-03	100	0	Resource	50453	\N	\N	\N
1342	2026-02-11 16:19:26.727352-03	100	2	AudioStudio	50452	generate audio voice	\N	\N
1343	2026-02-11 16:22:36.080389-03	100	2	ArtExhibitionGuide	50443	info	\N	\N
1344	2026-02-11 16:27:00.362883-03	100	0	Resource	50454	\N	\N	\N
1345	2026-02-11 16:27:00.377215-03	100	2	AudioStudio	50450	generate audio voice	\N	\N
1346	2026-02-11 16:33:06.172058-03	100	0	Resource	50455	\N	\N	\N
1347	2026-02-11 16:33:06.175685-03	100	2	AudioStudio	50450	generate audio voice	\N	\N
1348	2026-02-11 16:38:50.09955-03	100	2	ArtExhibitionGuide	50443	info	\N	\N
1349	2026-02-11 16:40:43.667347-03	100	2	ArtExhibitionGuide	50443	info	\N	\N
1350	2026-02-11 16:41:05.462481-03	100	0	Resource	50456	\N	\N	\N
1351	2026-02-11 16:41:05.470508-03	100	2	AudioStudio	50450	generate audio voice	\N	\N
1352	2026-02-11 16:42:54.803214-03	100	2	ArtExhibitionGuideRecord	50445	translate	\N	\N
1353	2026-02-11 16:44:24.323499-03	100	0	Resource	50457	\N	\N	\N
1354	2026-02-11 16:44:24.32722-03	100	2	AudioStudio	50452	generate audio voice	\N	\N
1355	2026-02-11 16:51:57.062666-03	100	0	Resource	50458	\N	\N	\N
1356	2026-02-11 16:51:57.076431-03	100	2	AudioStudio	50452	generate audio voice	\N	\N
1357	2026-02-11 16:54:08.714386-03	100	0	Resource	50459	\N	\N	\N
1358	2026-02-11 16:54:08.724408-03	100	2	AudioStudio	50452	generate audio voice	\N	\N
1359	2026-02-11 17:13:47.747968-03	100	2	ArtExhibitionGuideRecord	50446	translate	\N	\N
1360	2026-02-11 17:13:49.955054-03	100	0	AudioStudio	50460	\N	\N	\N
1361	2026-02-11 17:23:50.338662-03	100	2	ArtExhibitionGuideRecord	50448	translate	\N	\N
1362	2026-02-11 17:23:53.62714-03	100	0	AudioStudio	50462	\N	\N	\N
1363	2026-02-11 17:41:11.709079-03	100	0	Resource	50463	\N	\N	\N
1364	2026-02-11 17:41:44.57384-03	100	2	AudioStudio	50462	generate audio voice	\N	\N
1365	2026-02-11 17:46:27.003602-03	100	0	Resource	50464	\N	\N	\N
1370	2026-02-11 17:48:49.058338-03	100	2	AudioStudio	50465	generate audio voice	\N	\N
1371	2026-02-11 17:50:13.314132-03	100	2	ArtExhibitionGuide	202	info	\N	\N
1372	2026-02-11 17:50:26.085612-03	100	0	Resource	50467	\N	\N	\N
1366	2026-02-11 17:46:27.00812-03	100	2	AudioStudio	828	generate audio voice	\N	\N
1367	2026-02-11 17:47:52.727185-03	100	0	AudioStudio	50465	\N	\N	\N
1368	2026-02-11 17:48:10.087339-03	100	2	GuideContentRecord	741	translate	\N	\N
1369	2026-02-11 17:48:49.04742-03	100	0	Resource	50466	\N	\N	\N
1373	2026-02-11 17:50:26.09736-03	100	2	AudioStudio	838	generate audio voice	\N	\N
1374	2026-02-11 21:18:11.07599-03	100	0	Resource	50471	\N	\N	\N
1375	2026-02-11 21:19:28.497985-03	100	0	Resource	50472	\N	\N	\N
1376	2026-02-11 21:19:28.519239-03	100	2	Voice	50461	audio	\N	\N
1377	2026-02-11 21:28:01.805938-03	100	0	Resource	50473	\N	\N	\N
1378	2026-02-11 21:28:01.82426-03	100	2	Voice	50338	audio	\N	\N
1379	2026-02-11 21:28:22.649739-03	100	0	Resource	50474	\N	\N	\N
1380	2026-02-11 21:28:22.657815-03	100	2	Voice	50382	audio	\N	\N
1381	2026-02-11 21:28:51.257859-03	100	0	Resource	50475	\N	\N	\N
1382	2026-02-11 21:28:51.271235-03	100	2	Voice	50351	audio	\N	\N
1383	2026-02-11 21:29:24.642657-03	100	0	Resource	50476	\N	\N	\N
1384	2026-02-11 21:29:24.65284-03	100	2	Voice	50380	info, audio	\N	\N
1385	2026-02-11 21:31:26.641523-03	100	0	Resource	50477	\N	\N	\N
1386	2026-02-11 21:31:26.659516-03	100	2	Voice	50339	audio	\N	\N
1387	2026-02-11 21:32:00.671338-03	100	0	Resource	50478	\N	\N	\N
1388	2026-02-11 21:32:00.685172-03	100	2	Voice	50349	audio	\N	\N
1389	2026-02-11 21:32:22.105541-03	100	0	Resource	50479	\N	\N	\N
1390	2026-02-11 21:32:22.114758-03	100	2	Voice	50350	audio	\N	\N
1391	2026-02-11 21:34:35.618227-03	100	0	Resource	50480	\N	\N	\N
1392	2026-02-11 21:34:35.627874-03	100	2	AudioStudio	50462	generate audio voice	\N	\N
1393	2026-02-11 21:35:17.749179-03	100	0	Resource	50481	\N	\N	\N
1394	2026-02-11 21:35:17.75882-03	100	2	Voice	50381	audio	\N	\N
1395	2026-02-11 21:36:08.053494-03	100	0	Resource	50482	\N	\N	\N
1396	2026-02-11 21:36:08.061288-03	100	2	Voice	50379	audio	\N	\N
1397	2026-02-11 21:37:04.366565-03	100	0	Resource	50483	\N	\N	\N
1398	2026-02-11 21:37:04.373936-03	100	2	Voice	50355	audio	\N	\N
1399	2026-02-11 21:38:11.874956-03	100	0	Resource	50484	\N	\N	\N
1400	2026-02-11 21:38:11.880542-03	100	2	Voice	50357	audio	\N	\N
1401	2026-02-11 21:39:12.238939-03	100	0	Resource	50485	\N	\N	\N
1402	2026-02-11 21:39:12.244733-03	100	2	Voice	50358	info, audio	\N	\N
1403	2026-02-12 08:08:45.678875-03	100	0	Music	50489	\N	\N	\N
1404	2026-02-12 08:56:59.107573-03	100	0	Music	50490	\N	\N	\N
1405	2026-02-12 08:58:37.286097-03	100	0	Music	50491	\N	\N	\N
1406	2026-02-12 08:59:14.118711-03	100	0	Music	50492	\N	\N	\N
1407	2026-02-12 08:59:47.694067-03	100	0	Music	50493	\N	\N	\N
1408	2026-02-12 09:00:00.149829-03	100	2	Music	50493	state, name, info	\N	\N
1409	2026-02-12 09:08:27.328346-03	100	0	Music	50494	\N	\N	\N
1410	2026-02-12 09:08:42.90236-03	100	2	Music	50494	name, info	\N	\N
1411	2026-02-12 10:26:52.853206-03	100	0	Resource	50495	\N	\N	\N
1412	2026-02-12 10:26:52.872164-03	100	2	Music	50494	audio	\N	\N
1413	2026-02-12 10:28:46.083088-03	100	2	Music	50494	audio, name, info	\N	\N
1414	2026-02-12 10:31:35.678673-03	100	2	Music	50493	name, info	\N	\N
1415	2026-02-12 10:31:55.399972-03	100	0	Resource	50496	\N	\N	\N
1416	2026-02-12 10:31:55.406433-03	100	2	Music	50493	name, info, audio	\N	\N
1417	2026-02-12 10:32:13.097527-03	100	2	Music	50493	name, info, audio	\N	\N
1418	2026-02-12 10:32:24.263285-03	100	2	Music	50493	name, info, audio, state	\N	\N
1419	2026-02-12 10:32:35.423685-03	100	2	Music	50494	state	\N	\N
1420	2026-02-12 10:33:02.889216-03	100	2	Music	50493	name	\N	\N
1421	2026-02-12 10:33:14.748342-03	100	2	Music	50494	name	\N	\N
1422	2026-02-12 10:59:02.017385-03	100	0	Music	50497	\N	\N	\N
1423	2026-02-12 10:59:27.915265-03	100	0	Resource	50498	\N	\N	\N
1424	2026-02-12 10:59:27.928368-03	100	2	Music	50497	state, audio	\N	\N
1425	2026-02-12 10:59:58.149518-03	100	2	Music	50497	name	\N	\N
1426	2026-02-12 11:00:09.653865-03	100	0	Music	50499	\N	\N	\N
1427	2026-02-12 11:00:21.856767-03	100	0	Resource	50500	\N	\N	\N
1428	2026-02-12 11:00:21.866092-03	100	2	Music	50499	state, audio	\N	\N
1429	2026-02-12 11:00:36.477753-03	100	2	Music	50499	state, audio, name	\N	\N
1430	2026-02-12 11:00:51.585129-03	100	2	Music	50499	name	\N	\N
1431	2026-02-12 11:00:56.160658-03	100	0	Music	50501	\N	\N	\N
1432	2026-02-12 11:01:05.692135-03	100	2	Music	50501		\N	\N
1433	2026-02-12 11:01:23.406921-03	100	2	Music	50501	state	\N	\N
1434	2026-02-12 11:01:54.122692-03	100	0	Music	50502	\N	\N	\N
1435	2026-02-12 11:02:01.85963-03	100	0	Resource	50503	\N	\N	\N
1436	2026-02-12 11:02:01.870084-03	100	2	Music	50502	audio	\N	\N
1437	2026-02-12 11:02:18.228418-03	100	2	Music	50502	name	\N	\N
1438	2026-02-12 11:02:24.012218-03	100	2	Music	50502	name, state	\N	\N
1439	2026-02-12 11:02:55.245152-03	100	0	Resource	50504	\N	\N	\N
1440	2026-02-12 11:02:55.256591-03	100	2	Music	50501	audio	\N	\N
1441	2026-02-12 11:03:10.359478-03	100	2	Music	50501	audio, name	\N	\N
1442	2026-02-12 11:03:21.695089-03	100	2	Music	50501	name	\N	\N
1443	2026-02-12 11:03:32.560798-03	100	2	Music	50502	name	\N	\N
1444	2026-02-12 11:07:21.401179-03	100	0	Music	50505	\N	\N	\N
1445	2026-02-12 11:07:42.542169-03	100	0	Resource	50506	\N	\N	\N
1446	2026-02-12 11:07:42.554529-03	100	2	Music	50505	audio	\N	\N
1447	2026-02-12 11:08:07.163351-03	100	2	Music	50505	state, name	\N	\N
1448	2026-02-12 11:09:22.588084-03	100	0	Music	50507	\N	\N	\N
1449	2026-02-12 11:09:37.087558-03	100	0	Resource	50508	\N	\N	\N
1450	2026-02-12 11:09:37.101244-03	100	2	Music	50507	state, audio	\N	\N
1451	2026-02-12 11:09:51.845112-03	100	2	Music	50507	state, audio, name	\N	\N
1452	2026-02-12 11:10:30.666746-03	100	0	Music	50509	\N	\N	\N
1453	2026-02-12 11:11:02.301499-03	100	0	Resource	50510	\N	\N	\N
1454	2026-02-12 11:11:02.311277-03	100	2	Music	50509	state, name, audio	\N	\N
1455	2026-02-12 11:11:24.097428-03	100	0	Music	50511	\N	\N	\N
1456	2026-02-12 11:11:47.84854-03	100	0	Resource	50512	\N	\N	\N
1457	2026-02-12 11:11:47.863072-03	100	2	Music	50511	name, audio	\N	\N
1458	2026-02-12 11:12:07.920675-03	100	2	Music	50511	state	\N	\N
1459	2026-02-12 12:02:12.493612-03	100	0	Music	50513	\N	\N	\N
1460	2026-02-12 12:02:28.630127-03	100	2	Music	50513	state	\N	\N
1461	2026-02-12 12:03:12.898172-03	100	0	Resource	50514	\N	\N	\N
1462	2026-02-12 12:03:12.902743-03	100	2	Music	50513	state, audio	\N	\N
1463	2026-02-12 12:03:29.361962-03	100	2	Music	50513	state, audio, name	\N	\N
1464	2026-02-12 12:03:50.202699-03	100	2	Music	50513	name	\N	\N
1465	2026-02-12 12:03:55.426041-03	100	0	Music	50515	\N	\N	\N
1466	2026-02-12 12:04:04.002297-03	100	2	Music	50515		\N	\N
1467	2026-02-12 12:06:57.608345-03	100	0	Resource	50516	\N	\N	\N
1468	2026-02-12 12:06:57.61324-03	100	2	Music	50515	state, audio	\N	\N
1469	2026-02-12 12:07:18.344791-03	100	2	Music	50515	state, audio, name	\N	\N
1470	2026-02-12 12:07:23.328217-03	100	0	Music	50517	\N	\N	\N
1471	2026-02-12 12:07:38.185891-03	100	2	Music	50517		\N	\N
1472	2026-02-12 12:08:20.598155-03	100	0	Resource	50518	\N	\N	\N
1473	2026-02-12 12:08:20.610198-03	100	2	Music	50517	audio	\N	\N
1474	2026-02-12 12:08:36.107729-03	100	2	Music	50517	audio, state, name	\N	\N
1475	2026-02-12 12:08:49.259969-03	100	2	Music	50507	name	\N	\N
1476	2026-02-12 12:09:02.353137-03	100	2	Music	50505	name	\N	\N
1477	2026-02-12 12:09:24.850278-03	100	2	Music	50517	name	\N	\N
1478	2026-02-12 12:09:30.090908-03	100	2	Music	50517	name	\N	\N
1479	2026-02-12 12:09:46.288344-03	100	2	Music	50513	name	\N	\N
1480	2026-02-12 12:13:44.528614-03	100	0	Music	50519	\N	\N	\N
1481	2026-02-12 12:13:53.192249-03	100	2	Music	50519		\N	\N
1482	2026-02-12 12:14:35.207389-03	100	2	Music	50519		\N	\N
1483	2026-02-12 12:15:15.258784-03	100	0	Resource	50520	\N	\N	\N
1484	2026-02-12 12:15:15.26369-03	100	2	Music	50519	state, audio	\N	\N
1485	2026-02-12 12:15:30.900589-03	100	2	Music	50519	state, audio, name	\N	\N
1486	2026-02-12 13:24:33.92954-03	100	0	Music	50521	\N	\N	\N
1487	2026-02-12 13:24:50.11495-03	100	0	Resource	50522	\N	\N	\N
1488	2026-02-12 13:24:50.146624-03	100	2	Music	50521	state, audio	\N	\N
1489	2026-02-12 13:26:36.036767-03	100	2	Music	50521	info	\N	\N
1490	2026-02-12 13:27:22.815158-03	100	2	Music	50521	info, name	\N	\N
1491	2026-02-12 13:27:40.334899-03	100	2	Music	50521	name	\N	\N
1492	2026-02-12 13:30:39.712268-03	100	2	Music	50511	info	\N	\N
1493	2026-02-12 13:32:34.429466-03	100	2	Music	50501	info	\N	\N
1494	2026-02-12 13:33:11.453974-03	100	2	Music	50494	name, info	\N	\N
1495	2026-02-12 13:33:23.782898-03	100	2	Music	50494	name, info	\N	\N
1496	2026-02-12 13:33:50.953532-03	100	2	Music	50493	name, info	\N	\N
1497	2026-02-12 13:34:08.435378-03	100	2	Music	50499	info	\N	\N
1498	2026-02-12 13:34:32.893287-03	100	2	Music	50521	name, info	\N	\N
1499	2026-02-12 13:34:42.447452-03	100	2	Music	50509	info	\N	\N
1500	2026-02-12 13:34:52.974304-03	100	2	Music	50511	info	\N	\N
1501	2026-02-12 13:35:34.698242-03	100	2	Music	50502	info	\N	\N
1502	2026-02-12 13:35:50.364603-03	100	2	Music	50497	info	\N	\N
1503	2026-02-12 13:35:59.901817-03	100	2	Music	50519	name, info	\N	\N
1504	2026-02-12 13:36:12.525894-03	100	2	Music	50519	name, info	\N	\N
1505	2026-02-12 13:36:26.2581-03	100	2	Music	50513		\N	\N
1506	2026-02-12 13:36:35.837987-03	100	2	Music	50515		\N	\N
1507	2026-02-12 13:36:45.760472-03	100	2	Music	50517		\N	\N
1508	2026-02-12 13:40:01.719259-03	100	0	Music	50523	\N	\N	\N
1509	2026-02-12 13:40:10.523265-03	100	0	Resource	50524	\N	\N	\N
1510	2026-02-12 13:40:10.530847-03	100	2	Music	50523	audio	\N	\N
1511	2026-02-12 13:41:10.914325-03	100	2	Music	50523	name, info	\N	\N
1512	2026-02-12 13:41:20.385202-03	100	2	Music	50523	name, info	\N	\N
1513	2026-02-12 13:45:29.240596-03	100	0	Music	50525	\N	\N	\N
1514	2026-02-12 13:45:40.326279-03	100	0	Resource	50526	\N	\N	\N
1515	2026-02-12 13:45:40.335335-03	100	2	Music	50525	state, audio	\N	\N
1516	2026-02-12 13:45:48.941059-03	100	2	Music	50525	state, audio, name, info	\N	\N
1517	2026-02-12 13:45:58.103316-03	100	2	Music	50525	state, audio, name, info	\N	\N
1518	2026-02-12 13:46:11.981667-03	100	2	Music	50523	state	\N	\N
1519	2026-02-12 14:35:15.3793-03	100	0	Music	50527	\N	\N	\N
1520	2026-02-12 14:37:53.630522-03	100	0	Resource	50528	\N	\N	\N
1521	2026-02-12 14:37:53.63961-03	100	2	Music	50527	state, audio	\N	\N
1522	2026-02-12 14:38:39.558004-03	100	2	Music	50527	state, audio, name, info	\N	\N
1523	2026-02-12 14:38:46.14298-03	100	0	Music	50529	\N	\N	\N
1524	2026-02-12 14:39:04.222592-03	100	0	Resource	50530	\N	\N	\N
1525	2026-02-12 14:39:04.231938-03	100	2	Music	50529	audio	\N	\N
1526	2026-02-12 14:39:29.743405-03	100	2	Music	50529	audio, name, info	\N	\N
1527	2026-02-12 14:39:37.10253-03	100	0	Music	50531	\N	\N	\N
1528	2026-02-12 14:39:47.679093-03	100	0	Resource	50532	\N	\N	\N
1529	2026-02-12 14:39:47.687392-03	100	2	Music	50531	audio	\N	\N
1530	2026-02-12 14:40:18.117708-03	100	2	Music	50531	audio, state, name, info	\N	\N
1531	2026-02-12 14:40:22.829368-03	100	0	Music	50533	\N	\N	\N
1532	2026-02-12 14:40:31.355951-03	100	0	Resource	50534	\N	\N	\N
1533	2026-02-12 14:40:31.362686-03	100	2	Music	50533	audio	\N	\N
1534	2026-02-12 14:40:58.224115-03	100	2	Music	50533	audio, name, info	\N	\N
1535	2026-02-12 14:41:05.135623-03	100	2	Music	50533	audio, name, info, state	\N	\N
1536	2026-02-12 14:41:24.854127-03	100	2	Music	50529	state	\N	\N
1537	2026-02-12 14:44:05.068953-03	100	0	Music	50535	\N	\N	\N
1538	2026-02-12 14:44:13.675003-03	100	0	Resource	50536	\N	\N	\N
1539	2026-02-12 14:44:13.684111-03	100	2	Music	50535	audio	\N	\N
1540	2026-02-12 14:44:26.610891-03	100	2	Music	50535	audio, state	\N	\N
1541	2026-02-12 14:44:40.876578-03	100	0	Resource	50537	\N	\N	\N
1542	2026-02-12 14:45:36.000711-03	100	2	Music	50535	name, info	\N	\N
1543	2026-02-12 14:45:43.517276-03	100	0	Music	50538	\N	\N	\N
1544	2026-02-12 14:45:54.841132-03	100	0	Resource	50539	\N	\N	\N
1545	2026-02-12 14:45:54.846208-03	100	2	Music	50538	state, audio	\N	\N
1546	2026-02-12 14:46:35.072654-03	100	2	Music	50538	state, audio, name, info	\N	\N
1547	2026-02-12 14:46:39.565821-03	100	0	Music	50540	\N	\N	\N
1548	2026-02-12 14:46:46.593136-03	100	0	Resource	50541	\N	\N	\N
1549	2026-02-12 14:46:46.60043-03	100	2	Music	50540	audio	\N	\N
1550	2026-02-12 14:47:06.542718-03	100	2	Music	50540	audio, name, info	\N	\N
1551	2026-02-12 14:47:14.533143-03	100	2	Music	50540	audio, name, info	\N	\N
1552	2026-02-12 14:47:21.965895-03	100	2	Music	50540	audio, name, info, state	\N	\N
1553	2026-02-12 14:49:17.362072-03	100	0	Music	50542	\N	\N	\N
1554	2026-02-12 14:49:26.358173-03	100	0	Resource	50543	\N	\N	\N
1555	2026-02-12 14:49:26.363998-03	100	2	Music	50542	audio	\N	\N
1556	2026-02-12 14:49:38.286149-03	100	2	Music	50542	audio, name, info	\N	\N
1557	2026-02-12 14:49:44.681195-03	100	0	Music	50544	\N	\N	\N
1558	2026-02-12 14:49:52.536998-03	100	0	Resource	50545	\N	\N	\N
1559	2026-02-12 14:49:52.542063-03	100	2	Music	50544	audio	\N	\N
1560	2026-02-12 14:50:05.227831-03	100	2	Music	50544	audio, name, info	\N	\N
1561	2026-02-12 14:50:18.185723-03	100	2	Music	50544	state	\N	\N
1562	2026-02-12 14:50:30.86407-03	100	2	Music	50542	state	\N	\N
1563	2026-02-12 14:51:28.106319-03	100	0	Music	50546	\N	\N	\N
1564	2026-02-12 14:52:13.993936-03	100	0	Resource	50547	\N	\N	\N
1565	2026-02-12 14:52:13.99868-03	100	2	Music	50546	audio	\N	\N
1566	2026-02-12 14:53:45.485778-03	100	2	Music	50546	name, info	\N	\N
1567	2026-02-12 14:53:58.35838-03	100	2	Music	50546	state	\N	\N
1568	2026-02-12 14:54:14.523155-03	100	0	Music	50548	\N	\N	\N
1569	2026-02-12 14:55:47.495679-03	100	0	Resource	50549	\N	\N	\N
1570	2026-02-12 14:55:47.502556-03	100	2	Music	50548	state, audio	\N	\N
1571	2026-02-12 14:56:22.530275-03	100	2	Music	50548	state, audio, name, info	\N	\N
1572	2026-02-12 14:56:38.932296-03	100	2	Music	50548	state, audio, name, info	\N	\N
1573	2026-02-12 14:58:10.096627-03	100	0	Music	50550	\N	\N	\N
1574	2026-02-12 14:58:21.622429-03	100	0	Resource	50551	\N	\N	\N
1575	2026-02-12 14:58:21.626031-03	100	2	Music	50550	state, audio	\N	\N
1576	2026-02-12 14:58:57.273406-03	100	2	Music	50550	state, audio, name	\N	\N
1577	2026-02-12 14:59:07.629285-03	100	2	Music	50550	state, audio, name, info	\N	\N
1578	2026-02-12 15:07:47.197799-03	100	0	Music	50552	\N	\N	\N
1579	2026-02-12 15:08:00.468551-03	100	0	Resource	50553	\N	\N	\N
1580	2026-02-12 15:08:00.475588-03	100	2	Music	50552	audio	\N	\N
1581	2026-02-12 15:08:17.073697-03	100	2	Music	50552	audio, name, info	\N	\N
1582	2026-02-12 15:08:23.161022-03	100	2	Music	50552	audio, name, info, state	\N	\N
1583	2026-02-12 15:08:59.585573-03	100	2	Music	50527	name	\N	\N
1584	2026-02-12 15:20:07.493413-03	100	0	Music	50554	\N	\N	\N
1585	2026-02-12 15:20:16.598787-03	100	2	Music	50554		\N	\N
1586	2026-02-12 15:21:42.672935-03	100	2	Music	50554		\N	\N
1587	2026-02-12 15:22:19.484299-03	100	2	Music	50554		\N	\N
1588	2026-02-12 15:23:14.767062-03	100	0	Resource	50555	\N	\N	\N
1589	2026-02-12 15:23:14.774894-03	100	2	Music	50554	state, audio	\N	\N
1590	2026-02-12 15:23:29.782347-03	100	2	Music	50554	state, audio, name, info	\N	\N
1591	2026-02-12 16:11:21.439154-03	100	0	ArtWork	50556	\N	\N	\N
1592	2026-02-12 16:11:21.443084-03	100	0	ArtWorkRecord	50557	\N	\N	\N
1593	2026-02-12 16:11:21.444445-03	100	0	ArtWorkRecord	50558	\N	\N	\N
1594	2026-02-12 16:11:21.445054-03	100	0	ArtWorkRecord	50559	\N	\N	\N
1595	2026-02-12 16:11:21.445702-03	100	0	ArtWorkRecord	50560	\N	\N	\N
1596	2026-02-12 16:11:21.44652-03	100	0	ArtWorkRecord	50561	\N	\N	\N
1597	2026-02-12 16:11:21.44719-03	100	0	ArtWorkRecord	50562	\N	\N	\N
1598	2026-02-12 16:13:11.49042-03	100	2	ArtWork	50556	objectType, source, name, info, year	\N	\N
1599	2026-02-12 16:13:31.714087-03	100	0	Resource	50563	\N	\N	\N
1600	2026-02-12 16:13:31.719129-03	100	2	ArtWork	50556	photo	\N	\N
1601	2026-02-12 16:13:55.094376-03	100	2	ArtWork	50556	translate	\N	\N
1602	2026-02-12 16:16:57.630766-03	100	2	ArtWork	50556	objectType	\N	\N
1603	2026-02-12 16:17:07.658392-03	100	2	ArtWork	50556	objectType	\N	\N
1604	2026-02-12 16:17:47.607484-03	100	2	ArtWork	50556	objectType, spec, info	\N	\N
1605	2026-02-12 16:18:36.579748-03	100	0	ArtExhibitionItem	50564	\N	\N	\N
1606	2026-02-12 16:18:36.581666-03	100	0	ArtExhibitionItemRecord	50565	\N	\N	\N
1607	2026-02-12 16:18:36.582435-03	100	0	ArtExhibitionItemRecord	50566	\N	\N	\N
1608	2026-02-12 16:18:36.583015-03	100	0	ArtExhibitionItemRecord	50567	\N	\N	\N
1609	2026-02-12 16:18:36.583586-03	100	0	ArtExhibitionItemRecord	50568	\N	\N	\N
1610	2026-02-12 16:18:36.584126-03	100	0	ArtExhibitionItemRecord	50569	\N	\N	\N
1611	2026-02-12 16:18:36.584551-03	100	0	ArtExhibitionItemRecord	50570	\N	\N	\N
1612	2026-02-12 16:18:36.585728-03	100	2	ArtExhibition	50435	add item	{"id": "50564", "name": "Costurero", "class": "ArtExhibitionItem", "action": "2", "subaction": "add item"}	\N
1613	2026-02-12 16:27:34.133623-03	100	2	ArtExhibitionItem	50564	translate	\N	\N
1614	2026-02-12 16:35:02.063661-03	100	2	Music	50544	state	\N	\N
1615	2026-02-12 17:41:09.33411-03	100	2	Voice	50382	state	\N	\N
1616	2026-02-12 17:43:04.138268-03	100	0	ArtWork	50571	\N	\N	\N
1617	2026-02-12 17:43:04.141095-03	100	0	ArtWorkRecord	50572	\N	\N	\N
1618	2026-02-12 17:43:04.142935-03	100	0	ArtWorkRecord	50573	\N	\N	\N
1619	2026-02-12 17:43:04.144401-03	100	0	ArtWorkRecord	50574	\N	\N	\N
1620	2026-02-12 17:43:04.146159-03	100	0	ArtWorkRecord	50575	\N	\N	\N
1621	2026-02-12 17:43:04.146934-03	100	0	ArtWorkRecord	50576	\N	\N	\N
1622	2026-02-12 17:43:04.147558-03	100	0	ArtWorkRecord	50577	\N	\N	\N
1623	2026-02-12 17:56:15.016685-03	100	0	ArtWork	50578	\N	\N	\N
1624	2026-02-12 17:56:15.018626-03	100	0	ArtWorkRecord	50579	\N	\N	\N
1625	2026-02-12 17:56:15.020059-03	100	0	ArtWorkRecord	50580	\N	\N	\N
1626	2026-02-12 17:56:15.021204-03	100	0	ArtWorkRecord	50581	\N	\N	\N
1627	2026-02-12 17:56:15.021918-03	100	0	ArtWorkRecord	50582	\N	\N	\N
1628	2026-02-12 17:56:15.022639-03	100	0	ArtWorkRecord	50583	\N	\N	\N
1629	2026-02-12 17:56:15.023145-03	100	0	ArtWorkRecord	50584	\N	\N	\N
1630	2026-02-12 18:05:55.403551-03	100	2	Music	50554	state	\N	\N
1631	2026-02-12 18:12:36.746433-03	100	0	Resource	50585	\N	\N	\N
1632	2026-02-12 18:12:36.76-03	100	2	ArtWork	50578	objectType, photo	\N	\N
1633	2026-02-12 18:14:14.728772-03	100	2	ArtWork	50578	source, spec, name, year	\N	\N
1634	2026-02-13 00:44:15.562779-03	100	0	Resource	50586	\N	\N	\N
1635	2026-02-13 00:44:42.028965-03	100	2	AudioStudio	50450	add music	\N	\N
1636	2026-02-13 00:46:25.29221-03	100	2	ArtExhibitionGuide	50443	integrate audioStudio	\N	\N
1637	2026-02-13 00:49:32.672-03	100	0	Music	50587	\N	\N	\N
1638	2026-02-13 00:49:56.24844-03	100	0	Resource	50588	\N	\N	\N
1639	2026-02-13 00:49:56.256389-03	100	2	Music	50587	audio	\N	\N
1640	2026-02-13 00:50:43.784233-03	100	2	Music	50587	audio, name, info	\N	\N
1641	2026-02-13 00:50:53.487394-03	100	0	Music	50589	\N	\N	\N
1642	2026-02-13 00:51:47.514023-03	100	0	Music	50590	\N	\N	\N
1643	2026-02-13 00:51:58.098069-03	100	0	Resource	50591	\N	\N	\N
1644	2026-02-13 00:51:58.103576-03	100	2	Music	50590	audio	\N	\N
1645	2026-02-13 00:52:40.899986-03	100	0	Music	50592	\N	\N	\N
1646	2026-02-13 00:52:52.639009-03	100	0	Resource	50593	\N	\N	\N
1647	2026-02-13 00:52:52.645774-03	100	2	Music	50592	audio	\N	\N
1648	2026-02-13 00:53:25.692763-03	100	2	Music	50592	audio, name, info	\N	\N
1649	2026-02-13 00:54:25.251609-03	100	2	Music	50590	name, info	\N	\N
1650	2026-02-13 00:54:37.544981-03	100	2	Music	50590	name, info, state	\N	\N
1651	2026-02-13 00:55:51.400175-03	100	0	Resource	50594	\N	\N	\N
1652	2026-02-13 00:55:51.403061-03	100	2	AudioStudio	50450	add music	\N	\N
1653	2026-02-13 00:57:11.651283-03	100	0	Resource	50595	\N	\N	\N
1654	2026-02-13 00:57:11.655752-03	100	2	AudioStudio	50450	generate audio voice	\N	\N
1655	2026-02-13 00:58:38.688056-03	100	0	Resource	50596	\N	\N	\N
1656	2026-02-13 00:58:38.692422-03	100	2	AudioStudio	50450	add music	\N	\N
1657	2026-02-13 00:59:05.829713-03	100	2	ArtExhibitionGuide	50443	integrate audioStudio	\N	\N
1658	2026-02-13 01:00:40.410091-03	100	0	Resource	50597	\N	\N	\N
1659	2026-02-13 01:00:40.413291-03	100	2	AudioStudio	50450	add music	\N	\N
1660	2026-02-13 01:01:09.17263-03	100	2	ArtExhibitionGuide	50443	integrate audioStudio	\N	\N
1661	2026-02-13 01:06:08.579653-03	100	0	Resource	50598	\N	\N	\N
1662	2026-02-13 01:06:08.582332-03	100	2	AudioStudio	34074	add music	\N	\N
1663	2026-02-13 01:06:35.95708-03	100	2	ArtExhibitionGuide	33825	integrate audioStudio	\N	\N
1664	2026-02-13 08:01:43.897939-03	100	2	User	101	translate	\N	\N
1665	2026-02-13 08:46:53.790678-03	100	2	Music	50589	state	\N	\N
1666	2026-02-13 10:57:34.628227-03	100	0	Music	50604	\N	\N	\N
1667	2026-02-13 10:57:45.752329-03	100	0	Resource	50605	\N	\N	\N
1668	2026-02-13 10:57:45.780513-03	100	2	Music	50604	audio	\N	\N
1669	2026-02-13 10:59:23.38635-03	100	2	Music	50604	audio, name, info	\N	\N
1670	2026-02-13 11:00:50.166915-03	100	0	Music	50606	\N	\N	\N
1671	2026-02-13 11:01:01.825203-03	100	0	Resource	50607	\N	\N	\N
1672	2026-02-13 11:01:01.850507-03	100	2	Music	50606	audio	\N	\N
1673	2026-02-13 11:02:06.842136-03	100	2	Music	50606	audio, name, info	\N	\N
1674	2026-02-13 11:26:30.767554-03	100	2	Music	50606	info, license, url	\N	\N
1675	2026-02-13 11:32:03.804199-03	100	2	Music	50590	info, license, url	\N	\N
1676	2026-02-13 11:32:23.667861-03	100	2	Music	50590	info, license, url	\N	\N
1677	2026-02-13 11:33:08.553643-03	100	2	Music	50587	license, url	\N	\N
1678	2026-02-13 11:33:29.424725-03	100	2	Music	50592	info, license, url	\N	\N
1679	2026-02-13 11:33:50.909605-03	100	2	Music	50544	url	\N	\N
1680	2026-02-13 11:34:34.096093-03	100	2	Music	50544	url, info	\N	\N
1681	2026-02-13 11:34:56.446041-03	100	2	Music	50542	info, url	\N	\N
1682	2026-02-13 11:37:22.717992-03	100	2	Music	50542	info	\N	\N
1683	2026-02-13 13:08:20.322934-03	100	2	Music	50529	url	\N	\N
1684	2026-02-13 14:38:16.623268-03	100	2	Music	50493	url	\N	\N
1685	2026-02-13 14:50:01.75613-03	100	2	Person	34768	name	\N	\N
1686	2026-02-13 14:50:20.026572-03	100	2	Person	34768	translate	\N	\N
1687	2026-02-14 09:22:49.376931-03	100	0	ArtWork	50608	\N	\N	\N
1688	2026-02-14 09:22:49.381007-03	100	0	ArtWorkRecord	50609	\N	\N	\N
1689	2026-02-14 09:22:49.381791-03	100	0	ArtWorkRecord	50610	\N	\N	\N
1690	2026-02-14 09:22:49.382355-03	100	0	ArtWorkRecord	50611	\N	\N	\N
1691	2026-02-14 09:22:49.382937-03	100	0	ArtWorkRecord	50612	\N	\N	\N
1692	2026-02-14 09:22:49.383457-03	100	0	ArtWorkRecord	50613	\N	\N	\N
1693	2026-02-14 09:22:49.383899-03	100	0	ArtWorkRecord	50614	\N	\N	\N
1694	2026-02-14 17:08:34.366605-03	100	2	Person	308	webpage	\N	\N
1695	2026-02-14 17:08:40.020938-03	100	2	Person	308	webpage	\N	\N
1696	2026-02-14 17:35:47.492125-03	100	0	Resource	50615	\N	\N	\N
1697	2026-02-14 17:35:47.516766-03	100	2	Person	602	photo	\N	\N
1698	2026-02-14 17:42:32.712191-03	100	2	Artist	5067	sites	\N	\N
1699	2026-02-14 17:43:09.919231-03	100	2	Artist	5064	sites	\N	\N
1700	2026-02-14 17:45:41.450052-03	100	2	Artist	5064	sites	\N	\N
1701	2026-02-14 17:46:23.070478-03	100	2	Artist	5067	sites	\N	\N
1702	2026-02-14 20:32:03.109741-03	100	2	ArtWork	50571	artists	\N	\N
1703	2026-02-15 09:37:07.990568-03	100	2	Music	50501	info, url	\N	\N
1704	2026-02-15 09:37:51.917558-03	100	2	Music	50606	url	\N	\N
1705	2026-02-15 09:38:26.453517-03	100	2	Music	50606	url, license	\N	\N
1706	2026-02-15 09:38:50.727737-03	100	2	Music	50606	url, license, name	\N	\N
1707	2026-02-15 09:41:45.188277-03	100	2	Music	50544	info	\N	\N
1708	2026-02-15 09:42:17.857404-03	100	2	Music	50542	info	\N	\N
1709	2026-02-15 09:42:35.400343-03	100	2	Music	50542	info	\N	\N
1710	2026-02-15 09:47:12.27742-03	100	0	Music	50626	\N	\N	\N
1711	2026-02-15 09:47:24.945094-03	100	0	Resource	50627	\N	\N	\N
1712	2026-02-15 09:47:24.955736-03	100	2	Music	50626	audio	\N	\N
1713	2026-02-15 09:48:37.356185-03	100	2	Music	50626	audio, state, name, info, license, url	\N	\N
1714	2026-02-15 09:49:18.704748-03	100	0	Music	50628	\N	\N	\N
1715	2026-02-15 09:49:37.450599-03	100	0	Resource	50629	\N	\N	\N
1716	2026-02-15 09:49:37.478228-03	100	2	Music	50628	audio	\N	\N
1717	2026-02-15 09:50:31.647403-03	100	2	Music	50628	audio, info	\N	\N
1718	2026-02-15 09:51:48.028554-03	100	2	Music	50628	audio, info, name	\N	\N
1719	2026-02-15 09:52:00.926919-03	100	2	Music	50628	audio, info, name, license	\N	\N
1720	2026-02-15 09:52:12.605499-03	100	2	Music	50628	audio, info, name, license	\N	\N
1721	2026-02-15 09:55:50.222026-03	100	0	Music	50630	\N	\N	\N
1722	2026-02-15 09:56:06.461599-03	100	0	Resource	50631	\N	\N	\N
1723	2026-02-15 09:56:06.492937-03	100	2	Music	50630	audio	\N	\N
1724	2026-02-15 09:57:31.729543-03	100	2	Music	50630	audio, name, info, license	\N	\N
1725	2026-02-15 09:57:42.454661-03	100	2	Music	50630	audio, name, info, license, url	\N	\N
1726	2026-02-15 09:59:22.30355-03	100	0	Music	50632	\N	\N	\N
1727	2026-02-15 09:59:29.569213-03	100	0	Resource	50633	\N	\N	\N
1728	2026-02-15 09:59:29.587002-03	100	2	Music	50632	audio	\N	\N
1729	2026-02-15 10:00:31.087662-03	100	2	Music	50632	audio, name, info, url	\N	\N
1730	2026-02-15 10:01:47.307045-03	100	0	Music	50634	\N	\N	\N
1731	2026-02-15 10:01:53.978265-03	100	0	Resource	50635	\N	\N	\N
1732	2026-02-15 10:01:53.992697-03	100	2	Music	50634	audio	\N	\N
1733	2026-02-15 10:02:51.320344-03	100	2	Music	50634	audio, name, info, url	\N	\N
1734	2026-02-15 10:03:50.543713-03	100	2	Music	50634	audio, name, info, url, license	\N	\N
1735	2026-02-15 10:04:39.548692-03	100	0	Music	50636	\N	\N	\N
1736	2026-02-15 10:04:46.400454-03	100	0	Resource	50637	\N	\N	\N
1737	2026-02-15 10:04:46.409925-03	100	2	Music	50636	audio	\N	\N
1738	2026-02-15 10:05:06.417482-03	100	2	Music	50636	audio, name, info	\N	\N
1739	2026-02-15 10:06:01.371447-03	100	2	Music	50636	audio, name, info, license	\N	\N
1740	2026-02-15 10:06:11.473339-03	100	2	Music	50636	audio, name, info, license, url	\N	\N
1741	2026-02-15 10:07:34.511902-03	100	2	Music	50525	state	\N	\N
1742	2026-02-15 10:10:35.875346-03	100	0	Music	50638	\N	\N	\N
1743	2026-02-15 10:10:54.563721-03	100	0	Resource	50639	\N	\N	\N
1744	2026-02-15 10:10:54.58652-03	100	2	Music	50638	audio	\N	\N
1745	2026-02-15 10:12:06.753516-03	100	2	Music	50638	audio, name, info, license, url	\N	\N
1746	2026-02-15 10:12:28.85436-03	100	2	Music	50638	name	\N	\N
1747	2026-02-15 10:12:37.426245-03	100	2	Music	50638	name, state	\N	\N
1748	2026-02-15 10:14:41.014345-03	100	0	Music	50640	\N	\N	\N
1749	2026-02-15 10:15:14.794449-03	100	0	Resource	50641	\N	\N	\N
1750	2026-02-15 10:15:14.808509-03	100	2	Music	50640	license, audio	\N	\N
1751	2026-02-15 10:15:40.028465-03	100	2	Music	50640	license, audio, info, url	\N	\N
1752	2026-02-15 10:16:14.683069-03	100	2	Music	50640	name	\N	\N
1753	2026-02-15 10:16:38.866306-03	100	2	Music	50527	state	\N	\N
1754	2026-02-15 10:17:40.655656-03	100	2	Music	50501	info	\N	\N
1755	2026-02-15 10:18:00.464899-03	100	2	Music	50501	info, state	\N	\N
1756	2026-02-15 10:31:46.863524-03	100	2	Music	50531	state	\N	\N
1757	2026-02-15 10:31:55.600897-03	100	2	Music	50529	state	\N	\N
1758	2026-02-15 10:34:06.172013-03	100	2	Music	50533	state	\N	\N
1759	2026-02-15 13:55:01.171865-03	100	0	Music	50642	\N	\N	\N
1760	2026-02-15 13:55:09.893236-03	100	0	Resource	50643	\N	\N	\N
1761	2026-02-15 13:55:09.916975-03	100	2	Music	50642	audio	\N	\N
1762	2026-02-15 13:56:35.333227-03	100	2	Music	50642	audio, name, info, license, url	\N	\N
1763	2026-02-15 13:58:07.790156-03	100	0	Music	50644	\N	\N	\N
1764	2026-02-15 13:58:16.264897-03	100	0	Resource	50645	\N	\N	\N
1765	2026-02-15 13:58:16.284712-03	100	2	Music	50644	audio	\N	\N
1766	2026-02-15 14:00:01.066801-03	100	2	Music	50644	audio, name, info, license, url	\N	\N
1767	2026-02-15 14:02:17.353207-03	100	2	Music	50493	state	\N	\N
1768	2026-02-15 14:03:38.883725-03	100	2	Music	50604	info, license, url	\N	\N
1769	2026-02-15 14:04:30.823955-03	100	2	Music	50499	state, info	\N	\N
1770	2026-02-15 14:05:43.566667-03	100	2	Music	50535	state	\N	\N
1771	2026-02-15 14:05:48.443138-03	100	0	Music	50646	\N	\N	\N
1772	2026-02-15 14:06:17.332216-03	100	0	Resource	50647	\N	\N	\N
1773	2026-02-15 14:06:17.336686-03	100	2	Music	50646	audio	\N	\N
1774	2026-02-15 14:06:46.718058-03	100	2	Music	50646	audio, name, info	\N	\N
1775	2026-02-15 14:06:52.614271-03	100	2	Music	50646	audio, name, info, state	\N	\N
1776	2026-02-15 14:07:18.740045-03	100	2	Music	50646	audio, name, info, state, license, url	\N	\N
1777	2026-02-15 14:07:54.360182-03	100	2	Music	50646	audio, name, info, state, license, url	\N	\N
1778	2026-02-15 14:29:12.440459-03	100	0	Music	50648	\N	\N	\N
1779	2026-02-15 14:29:20.821116-03	100	0	Resource	50649	\N	\N	\N
1780	2026-02-15 14:29:20.847525-03	100	2	Music	50648	audio	\N	\N
1781	2026-02-15 14:36:10.195245-03	100	2	Music	50648	technicalinfo, name, info, url	\N	\N
1782	2026-02-15 14:36:31.369394-03	100	2	Music	50648	technicalinfo, name, info, url, license	\N	\N
1783	2026-02-15 14:37:55.057448-03	100	0	Music	50650	\N	\N	\N
1784	2026-02-15 14:38:02.139016-03	100	0	Resource	50651	\N	\N	\N
1785	2026-02-15 14:38:02.151998-03	100	2	Music	50650	audio	\N	\N
1786	2026-02-15 14:38:22.899791-03	100	2	Music	50650	audio, name, info	\N	\N
1787	2026-02-15 14:39:27.771669-03	100	2	Music	50650	audio, name, info, state, technicalinfo, license, url	\N	\N
1788	2026-02-15 14:44:23.314129-03	100	2	Music	50592	license	\N	\N
1789	2026-02-15 14:45:11.881923-03	100	2	Music	50544	state, license	\N	\N
1790	2026-02-15 15:01:22.477423-03	100	2	Music	50606	royaltyfree	\N	\N
1791	2026-02-15 15:05:02.857083-03	100	2	Music	50626	royaltyfree	\N	\N
1792	2026-02-15 15:05:47.823911-03	100	2	Music	50630	royaltyfree	\N	\N
1793	2026-02-15 15:09:05.656023-03	100	2	Music	50632	license	\N	\N
1794	2026-02-15 15:09:51.272951-03	100	2	Music	50628	royaltyfree, technicalinfo, license	\N	\N
1795	2026-02-15 15:10:36.54576-03	100	2	Music	50648	royaltyfree	\N	\N
1796	2026-02-15 15:11:22.492555-03	100	2	Music	50648	royaltyfree	\N	\N
1797	2026-02-15 15:16:32.851453-03	100	0	Music	50652	\N	\N	\N
1798	2026-02-15 15:16:40.365495-03	100	0	Resource	50653	\N	\N	\N
1799	2026-02-15 15:16:40.373107-03	100	2	Music	50652	audio	\N	\N
1800	2026-02-15 15:18:13.504465-03	100	2	Music	50652	audio, state, royaltyfree, name, info, license, url	\N	\N
1801	2026-02-15 15:22:34.276958-03	100	0	Music	50654	\N	\N	\N
1802	2026-02-15 15:22:42.222038-03	100	0	Resource	50655	\N	\N	\N
1803	2026-02-15 15:22:42.23212-03	100	2	Music	50654	audio	\N	\N
1804	2026-02-15 15:23:46.867287-03	100	2	Music	50654	audio, state, royaltyfree, name, info, license	\N	\N
1805	2026-02-15 15:24:02.065034-03	100	2	Music	50654	audio, state, royaltyfree, name, info, license, url	\N	\N
1806	2026-02-15 15:25:02.216204-03	100	0	Music	50656	\N	\N	\N
1807	2026-02-15 15:25:13.106455-03	100	0	Resource	50657	\N	\N	\N
1808	2026-02-15 15:25:13.11406-03	100	2	Music	50656	audio	\N	\N
1809	2026-02-15 15:25:41.939484-03	100	2	Music	50656	audio, royaltyfree, name, url	\N	\N
1810	2026-02-15 15:26:44.065733-03	100	2	Music	50656	audio, royaltyfree, name, url, info, license	\N	\N
1811	2026-02-15 15:27:35.404064-03	100	0	Music	50658	\N	\N	\N
1812	2026-02-15 15:28:10.963016-03	100	0	Resource	50659	\N	\N	\N
1813	2026-02-15 15:28:10.96943-03	100	2	Music	50658	url, audio	\N	\N
1814	2026-02-15 15:29:20.271671-03	100	2	Music	50658	url, audio, royaltyfree, state, name, info	\N	\N
1815	2026-02-15 15:29:32.456208-03	100	2	Music	50658	url, audio, royaltyfree, state, name, info, license	\N	\N
1816	2026-02-15 15:31:01.337855-03	100	0	Music	50660	\N	\N	\N
1817	2026-02-15 15:31:21.980779-03	100	0	Resource	50661	\N	\N	\N
1818	2026-02-15 15:31:21.986571-03	100	2	Music	50660	audio	\N	\N
1819	2026-02-15 15:32:11.097021-03	100	2	Music	50660	audio, name, royaltyfree, info, license, url	\N	\N
1820	2026-02-15 15:32:26.420341-03	100	2	Music	50660	audio, name, royaltyfree, info, license, url, state	\N	\N
1821	2026-02-15 15:34:30.909578-03	100	0	Music	50662	\N	\N	\N
1822	2026-02-15 15:34:38.017556-03	100	0	Resource	50663	\N	\N	\N
1823	2026-02-15 15:34:38.023113-03	100	2	Music	50662	audio	\N	\N
1824	2026-02-15 15:34:44.894503-03	100	2	Music	50662	audio, name, info	\N	\N
1825	2026-02-15 15:35:19.09253-03	100	2	Music	50662	audio, name, info, royaltyfree, license, url	\N	\N
1826	2026-02-15 15:36:16.450077-03	100	2	Music	50544	royaltyfree	\N	\N
1827	2026-02-15 15:37:51.647169-03	100	0	Music	50664	\N	\N	\N
1828	2026-02-15 15:38:14.054956-03	100	0	Resource	50665	\N	\N	\N
1829	2026-02-15 15:38:14.059957-03	100	2	Music	50664	audio	\N	\N
1830	2026-02-15 15:39:46.569376-03	100	2	Music	50664	audio, royaltyfree, name, info, license, url	\N	\N
1831	2026-02-15 15:39:55.581331-03	100	2	Music	50664	audio, royaltyfree, name, info, license, url, state	\N	\N
1832	2026-02-15 15:41:48.56122-03	100	0	Music	50666	\N	\N	\N
1833	2026-02-15 15:42:04.807834-03	100	0	Resource	50667	\N	\N	\N
1834	2026-02-15 15:42:04.812863-03	100	2	Music	50666	audio	\N	\N
1835	2026-02-15 15:43:16.721778-03	100	2	Music	50666	audio, royaltyfree, info, license, url	\N	\N
1836	2026-02-15 15:43:36.438163-03	100	2	Music	50666	audio, royaltyfree, info, license, url, state, name	\N	\N
1837	2026-02-15 16:33:01.136532-03	100	0	Resource	50668	\N	\N	\N
1838	2026-02-15 16:33:01.149212-03	100	2	AudioStudio	796	add music	\N	\N
1839	2026-02-15 16:36:23.504099-03	100	0	Resource	50669	\N	\N	\N
1840	2026-02-15 16:36:23.507592-03	100	2	AudioStudio	50450	add music	\N	\N
1841	2026-02-15 16:36:51.944968-03	100	2	ArtExhibitionGuide	50443	integrate audioStudio	\N	\N
1842	2026-02-15 22:02:34.055931-03	100	2	ArtExhibitionGuide	292	translate	\N	\N
1843	2026-02-15 22:08:07.794005-03	100	2	Music	50654	name	\N	\N
1844	2026-02-15 22:14:53.459409-03	100	0	Music	50675	\N	\N	\N
1845	2026-02-15 22:15:00.115247-03	100	0	Resource	50676	\N	\N	\N
1846	2026-02-15 22:15:00.121332-03	100	2	Music	50675	audio	\N	\N
1847	2026-02-15 22:16:09.162834-03	100	2	Music	50675	audio, state, royaltyfree, name, info, license, url	\N	\N
1848	2026-02-15 22:16:44.330538-03	100	0	Music	50677	\N	\N	\N
1849	2026-02-15 22:16:52.349245-03	100	0	Resource	50678	\N	\N	\N
1850	2026-02-15 22:16:52.36158-03	100	2	Music	50677	audio	\N	\N
1851	2026-02-15 22:17:38.432964-03	100	2	Music	50677	audio, royaltyfree, name, info, license, url	\N	\N
1852	2026-02-15 22:20:16.262795-03	100	0	Music	50679	\N	\N	\N
1853	2026-02-15 22:20:23.066993-03	100	0	Resource	50680	\N	\N	\N
1854	2026-02-15 22:20:23.072798-03	100	2	Music	50679	audio	\N	\N
1855	2026-02-15 22:21:09.058925-03	100	2	Music	50679	audio, royaltyfree, name, info, license, url	\N	\N
1856	2026-02-15 22:23:24.291944-03	100	0	Music	50681	\N	\N	\N
1857	2026-02-15 22:23:34.118811-03	100	0	Resource	50682	\N	\N	\N
1858	2026-02-15 22:23:34.125669-03	100	2	Music	50681	audio	\N	\N
1859	2026-02-15 22:24:06.830093-03	100	2	Music	50681	audio, state, royaltyfree, name, info, license	\N	\N
1860	2026-02-15 22:24:13.799962-03	100	0	Music	50683	\N	\N	\N
1861	2026-02-15 22:24:21.933936-03	100	0	Resource	50684	\N	\N	\N
1862	2026-02-15 22:24:21.939144-03	100	2	Music	50683	audio	\N	\N
1863	2026-02-15 22:25:22.628516-03	100	2	Music	50683	audio, name, state, royaltyfree, info, license, url	\N	\N
1864	2026-02-15 22:25:46.811035-03	100	0	Music	50685	\N	\N	\N
1865	2026-02-15 22:26:01.752762-03	100	0	Resource	50686	\N	\N	\N
1866	2026-02-15 22:26:01.758357-03	100	2	Music	50685	audio	\N	\N
1867	2026-02-15 22:26:50.193643-03	100	2	Music	50685	audio, name, state, royaltyfree, info, license, url	\N	\N
1868	2026-02-15 22:27:19.377222-03	100	0	Music	50687	\N	\N	\N
1869	2026-02-15 22:27:26.15137-03	100	0	Resource	50688	\N	\N	\N
1870	2026-02-15 22:27:26.156875-03	100	2	Music	50687	audio	\N	\N
1871	2026-02-15 22:28:05.459907-03	100	2	Music	50687	audio, name, info, license, url	\N	\N
1872	2026-02-16 08:01:52.035054-03	100	2	ArtWork	406	info	\N	\N
1876	2026-02-16 12:32:54.946145-03	100	2	ArtWork	225	audioid	\N	\N
1877	2026-02-16 12:32:54.957693-03	100	2	GuideContent	259	audioid	\N	\N
1878	2026-02-16 12:32:54.976725-03	100	2	GuideContent	260	audioid	\N	\N
1879	2026-02-16 12:32:54.979775-03	100	2	GuideContent	261	audioid	\N	\N
1880	2026-02-16 12:32:54.982732-03	100	2	GuideContent	262	audioid	\N	\N
1881	2026-02-16 12:32:55.014798-03	100	2	ArtWork	22902	audioid	\N	\N
1882	2026-02-16 12:32:55.022693-03	100	2	ArtWork	585	audioid	\N	\N
1883	2026-02-16 12:32:55.030468-03	100	2	ArtWork	404	audioid	\N	\N
1884	2026-02-16 12:32:55.032692-03	100	2	GuideContent	50275	audioid	\N	\N
1885	2026-02-16 12:32:55.040209-03	100	2	GuideContent	414	audioid	\N	\N
1886	2026-02-16 12:32:55.050521-03	100	2	ArtWork	221	audioid	\N	\N
1887	2026-02-16 12:32:55.052669-03	100	2	GuideContent	257	audioid	\N	\N
1888	2026-02-16 12:32:55.065506-03	100	2	ArtWork	217	audioid	\N	\N
1889	2026-02-16 12:32:55.067335-03	100	2	GuideContent	255	audioid	\N	\N
1890	2026-02-16 12:32:55.080882-03	100	2	ArtWork	406	audioid	\N	\N
1891	2026-02-16 12:32:55.083615-03	100	2	GuideContent	50296	audioid	\N	\N
1892	2026-02-16 12:32:55.091341-03	100	2	GuideContent	413	audioid	\N	\N
1893	2026-02-16 12:32:55.100607-03	100	2	ArtWork	229	audioid	\N	\N
1894	2026-02-16 12:32:55.107057-03	100	2	ArtWork	311	audioid	\N	\N
1895	2026-02-16 12:32:55.112904-03	100	2	ArtWork	448	audioid	\N	\N
1896	2026-02-16 12:32:55.114556-03	100	2	GuideContent	459	audioid	\N	\N
1897	2026-02-16 12:32:55.122408-03	100	2	ArtWork	328	audioid	\N	\N
1898	2026-02-16 12:32:55.126824-03	100	2	ArtWork	219	audioid	\N	\N
1899	2026-02-16 12:32:55.128166-03	100	2	GuideContent	256	audioid	\N	\N
1900	2026-02-16 12:32:55.139429-03	100	2	ArtWork	484	audioid	\N	\N
1901	2026-02-16 12:32:55.141044-03	100	2	GuideContent	50289	audioid	\N	\N
1902	2026-02-16 12:32:55.148833-03	100	2	ArtWork	278	audioid	\N	\N
1903	2026-02-16 12:32:55.15019-03	100	2	GuideContent	297	audioid	\N	\N
1904	2026-02-16 12:32:55.16081-03	100	2	ArtWork	270	audioid	\N	\N
1905	2026-02-16 12:32:55.162247-03	100	2	GuideContent	293	audioid	\N	\N
1906	2026-02-16 12:32:55.173446-03	100	2	ArtWork	276	audioid	\N	\N
1907	2026-02-16 12:32:55.174833-03	100	2	GuideContent	50268	audioid	\N	\N
1908	2026-02-16 12:32:55.177105-03	100	2	GuideContent	296	audioid	\N	\N
1909	2026-02-16 12:32:55.188237-03	100	2	ArtWork	197	audioid	\N	\N
1910	2026-02-16 12:32:55.189793-03	100	2	GuideContent	204	audioid	\N	\N
1911	2026-02-16 12:32:55.200379-03	100	2	ArtWork	592	audioid	\N	\N
1912	2026-02-16 12:32:55.204893-03	100	2	ArtWork	215	audioid	\N	\N
1913	2026-02-16 12:32:55.206155-03	100	2	GuideContent	254	audioid	\N	\N
1914	2026-02-16 12:32:55.214479-03	100	2	ArtWork	195	audioid	\N	\N
1915	2026-02-16 12:32:55.21571-03	100	2	GuideContent	203	audioid	\N	\N
1916	2026-02-16 12:32:55.22278-03	100	2	ArtWork	575	audioid	\N	\N
1917	2026-02-16 12:32:55.227312-03	100	2	ArtWork	582	audioid	\N	\N
1918	2026-02-16 12:32:55.231611-03	100	2	ArtWork	213	audioid	\N	\N
1919	2026-02-16 12:32:55.232851-03	100	2	GuideContent	253	audioid	\N	\N
1920	2026-02-16 12:32:55.239527-03	100	2	ArtWork	223	audioid	\N	\N
1921	2026-02-16 12:32:55.241223-03	100	2	GuideContent	258	audioid	\N	\N
1922	2026-02-16 12:32:55.248756-03	100	2	ArtWork	274	audioid	\N	\N
1923	2026-02-16 12:32:55.250073-03	100	2	GuideContent	295	audioid	\N	\N
1924	2026-02-16 12:32:55.257227-03	100	2	ArtWork	231	audioid	\N	\N
1925	2026-02-16 12:32:55.262061-03	100	2	ArtWork	619	audioid	\N	\N
1926	2026-02-16 12:32:55.266074-03	100	2	ArtWork	332	audioid	\N	\N
1927	2026-02-16 12:32:55.270756-03	100	2	ArtWork	768	audioid	\N	\N
1928	2026-02-16 12:32:55.274709-03	100	2	ArtWork	209	audioid	\N	\N
1929	2026-02-16 12:32:55.278688-03	100	2	ArtWork	482	audioid	\N	\N
1930	2026-02-16 12:32:55.279782-03	100	2	GuideContent	488	audioid	\N	\N
1931	2026-02-16 12:32:55.291291-03	100	2	ArtWork	272	audioid	\N	\N
1932	2026-02-16 12:32:55.292492-03	100	2	GuideContent	294	audioid	\N	\N
1933	2026-02-16 12:32:55.298966-03	100	2	ArtWork	282	audioid	\N	\N
1934	2026-02-16 12:32:55.30018-03	100	2	GuideContent	299	audioid	\N	\N
1935	2026-02-16 12:32:55.308132-03	100	2	ArtWork	207	audioid	\N	\N
1936	2026-02-16 12:32:55.309398-03	100	2	GuideContent	250	audioid	\N	\N
1937	2026-02-16 12:32:55.312127-03	100	2	GuideContent	251	audioid	\N	\N
1938	2026-02-16 12:32:55.317262-03	100	2	ArtWork	324	audioid	\N	\N
1939	2026-02-16 12:32:55.321276-03	100	2	ArtWork	884	audioid	\N	\N
1940	2026-02-16 12:32:55.322417-03	100	2	GuideContent	899	audioid	\N	\N
1941	2026-02-16 12:32:55.3291-03	100	2	ArtWork	589	audioid	\N	\N
1942	2026-02-16 12:32:55.333181-03	100	2	ArtWork	452	audioid	\N	\N
1943	2026-02-16 12:32:55.334315-03	100	2	GuideContent	461	audioid	\N	\N
1944	2026-02-16 12:32:55.340485-03	100	2	ArtWork	205	audioid	\N	\N
1945	2026-02-16 12:32:55.341606-03	100	2	GuideContent	249	audioid	\N	\N
1946	2026-02-16 12:32:55.34815-03	100	2	ArtWork	40219	audioid	\N	\N
1947	2026-02-16 12:32:55.349243-03	100	2	GuideContent	40893	audioid	\N	\N
1948	2026-02-16 12:32:55.354759-03	100	2	ArtWork	50556	audioid	\N	\N
1949	2026-02-16 12:32:55.358612-03	100	2	ArtWork	42814	audioid	\N	\N
1950	2026-02-16 12:32:55.362536-03	100	2	ArtWork	34719	audioid	\N	\N
1951	2026-02-16 12:32:55.363617-03	100	2	GuideContent	35357	audioid	\N	\N
1952	2026-02-16 12:32:55.369735-03	100	2	ArtWork	12377	audioid	\N	\N
1953	2026-02-16 12:32:55.3708-03	100	2	GuideContent	18156	audioid	\N	\N
1954	2026-02-16 12:32:55.377858-03	100	2	ArtWork	402	audioid	\N	\N
1955	2026-02-16 12:32:55.379279-03	100	2	GuideContent	412	audioid	\N	\N
1956	2026-02-16 12:32:55.382647-03	100	2	GuideContent	50282	audioid	\N	\N
1957	2026-02-16 12:32:55.390611-03	100	2	ArtWork	43114	audioid	\N	\N
1958	2026-02-16 12:32:55.394222-03	100	2	ArtWork	603	audioid	\N	\N
1959	2026-02-16 12:32:55.397948-03	100	2	ArtWork	211	audioid	\N	\N
1960	2026-02-16 12:32:55.399019-03	100	2	GuideContent	252	audioid	\N	\N
1961	2026-02-16 12:32:55.405576-03	100	2	ArtWork	50571	audioid	\N	\N
1962	2026-02-16 12:32:55.409795-03	100	2	ArtWork	50578	audioid	\N	\N
1963	2026-02-16 12:32:55.413168-03	100	2	ArtWork	16103	audioid	\N	\N
1964	2026-02-16 12:32:55.414333-03	100	2	GuideContent	18152	audioid	\N	\N
1965	2026-02-16 12:32:55.419826-03	100	2	ArtWork	280	audioid	\N	\N
1966	2026-02-16 12:32:55.420863-03	100	2	GuideContent	298	audioid	\N	\N
1967	2026-02-16 12:32:55.428102-03	100	2	ArtWork	50608	audioid	\N	\N
1968	2026-02-16 12:32:55.43129-03	100	2	ArtWork	450	audioid	\N	\N
1969	2026-02-16 12:32:55.432306-03	100	2	GuideContent	460	audioid	\N	\N
1970	2026-02-16 12:38:36.481249-03	100	0	Music	50689	\N	\N	\N
1971	2026-02-16 12:38:45.955426-03	100	0	Resource	50690	\N	\N	\N
1972	2026-02-16 12:38:45.978607-03	100	2	Music	50689	audio	\N	\N
1973	2026-02-16 12:39:01.210938-03	100	2	Music	50689	audio	\N	\N
1974	2026-02-16 12:40:58.375234-03	100	2	Music	50689	audio, state, royaltyfree, name, info, license, url	\N	\N
1975	2026-02-16 12:41:28.247137-03	100	0	Music	50691	\N	\N	\N
1976	2026-02-16 12:41:44.417471-03	100	0	Resource	50692	\N	\N	\N
1977	2026-02-16 12:41:44.42386-03	100	2	Music	50691	audio	\N	\N
1978	2026-02-16 12:42:51.639294-03	100	2	Music	50691	audio, royaltyfree, name, info, license, url	\N	\N
1979	2026-02-16 12:43:47.73105-03	100	0	Music	50693	\N	\N	\N
1980	2026-02-16 12:43:54.741029-03	100	0	Resource	50694	\N	\N	\N
1981	2026-02-16 12:43:54.747395-03	100	2	Music	50693	audio	\N	\N
1982	2026-02-16 12:44:14.379124-03	100	2	Music	50693	audio, state, name	\N	\N
1983	2026-02-16 12:45:52.994539-03	100	2	Music	50693	audio, state, name	\N	\N
1984	2026-02-16 12:47:06.664969-03	100	2	Music	50693	audio, state, name, royaltyfree, info, license, url	\N	\N
1985	2026-02-16 12:47:19.385561-03	100	0	Music	50695	\N	\N	\N
1986	2026-02-16 12:48:24.254434-03	100	0	Resource	50696	\N	\N	\N
1987	2026-02-16 12:48:24.26893-03	100	2	Music	50695	audio	\N	\N
1988	2026-02-16 12:49:51.54263-03	100	2	Music	50695	audio, state, royaltyfree, name, info, license, url	\N	\N
1989	2026-02-16 12:50:02.053162-03	100	2	Music	50691	Published	\N	\N
1990	2026-02-16 12:51:16.467335-03	100	0	Music	50697	\N	\N	\N
1991	2026-02-16 12:51:25.433702-03	100	0	Resource	50698	\N	\N	\N
1992	2026-02-16 12:51:25.444342-03	100	2	Music	50697	audio	\N	\N
1993	2026-02-16 12:52:30.282666-03	100	2	Music	50697	audio, state, royaltyfree, name, info, license, url	\N	\N
1994	2026-02-16 12:53:33.507854-03	100	0	Music	50699	\N	\N	\N
1995	2026-02-16 12:54:00.658618-03	100	0	Resource	50700	\N	\N	\N
1996	2026-02-16 12:54:00.664154-03	100	2	Music	50699	audio	\N	\N
1997	2026-02-16 12:55:21.53346-03	100	2	Music	50699	audio, state, royaltyfree, name, info, license, url	\N	\N
1998	2026-02-16 14:14:28.892859-03	100	0	ArtWork	50701	\N	\N	\N
1999	2026-02-16 14:14:28.897668-03	100	0	ArtWorkRecord	50702	\N	\N	\N
2000	2026-02-16 14:14:28.899049-03	100	0	ArtWorkRecord	50703	\N	\N	\N
2001	2026-02-16 14:14:28.900309-03	100	0	ArtWorkRecord	50704	\N	\N	\N
2002	2026-02-16 14:14:28.90123-03	100	0	ArtWorkRecord	50705	\N	\N	\N
2003	2026-02-16 14:14:28.902378-03	100	0	ArtWorkRecord	50706	\N	\N	\N
2004	2026-02-16 14:14:28.903221-03	100	0	ArtWorkRecord	50707	\N	\N	\N
2005	2026-02-16 14:16:19.599676-03	100	0	Resource	50708	\N	\N	\N
2006	2026-02-16 14:16:19.605933-03	100	2	ArtWork	50701	photo	\N	\N
2007	2026-02-16 14:18:56.584811-03	100	2	ArtWork	50701	photo, objectType, source, name, year	\N	\N
2008	2026-02-16 14:19:17.150468-03	100	2	ArtWork	50701	Published	\N	\N
2009	2026-02-16 15:40:18.572691-03	100	2	ArtWork	50701	audioid	\N	\N
2010	2026-02-16 15:41:46.606949-03	100	2	ArtWork	50701	audioid	\N	\N
2011	2026-02-16 16:15:45.144406-03	100	2	Person	180	Edition	\N	\N
2012	2026-02-16 16:15:47.209727-03	100	2	Person	180	Published	\N	\N
2013	2026-02-16 16:15:54.800422-03	100	2	User	100	Published	\N	\N
2014	2026-02-16 18:07:56.794014-03	100	0	Music	50709	\N	\N	\N
2015	2026-02-16 18:08:07.25373-03	100	0	Resource	50710	\N	\N	\N
2016	2026-02-16 18:08:07.26184-03	100	2	Music	50709	audio	\N	\N
2017	2026-02-16 18:09:18.563856-03	100	2	Music	50709	audio, state, royaltyfree, name, info, license, url	\N	\N
2018	2026-02-16 18:11:25.502998-03	100	0	Music	50711	\N	\N	\N
2019	2026-02-16 18:11:36.613073-03	100	0	Resource	50712	\N	\N	\N
2020	2026-02-16 18:11:36.644783-03	100	2	Music	50711	audio	\N	\N
2021	2026-02-16 18:12:21.587012-03	100	2	Music	50711	audio	\N	\N
2022	2026-02-16 18:15:00.021807-03	100	2	Music	50711	audio, info, technicalinfo	\N	\N
2023	2026-02-16 18:15:56.573142-03	100	2	Music	50711	audio, info, technicalinfo, state, royaltyfree, name, license, url	\N	\N
2024	2026-02-16 18:17:09.541014-03	100	0	Music	50713	\N	\N	\N
2025	2026-02-16 18:17:29.105785-03	100	0	Resource	50714	\N	\N	\N
2026	2026-02-16 18:17:29.117326-03	100	2	Music	50713	audio	\N	\N
2027	2026-02-16 18:18:11.84317-03	100	2	Music	50713	audio, royaltyfree, technicalinfo, name, info, license, url	\N	\N
2028	2026-02-16 18:18:23.573785-03	100	2	Music	50713	Published	\N	\N
2029	2026-02-16 18:19:30.72722-03	100	2	ArtExhibitionGuideRecord	50445	info	\N	\N
2030	2026-02-16 18:20:14.129154-03	100	0	Resource	50715	\N	\N	\N
2031	2026-02-16 18:20:14.137952-03	100	2	AudioStudio	50452	add music	\N	\N
2032	2026-02-16 18:20:53.574229-03	100	2	ArtExhibitionGuideRecord	50445	integrate audioStudio	\N	\N
2033	2026-02-16 18:22:29.623318-03	100	0	Resource	50716	\N	\N	\N
2034	2026-02-16 18:22:29.627948-03	100	2	AudioStudio	50462	add music	\N	\N
2035	2026-02-16 18:22:32.815629-03	100	2	ArtExhibitionGuideRecord	50448	integrate audioStudio	\N	\N
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

COPY public.guidecontent (id, name, namekey, title, titlekey, artexhibitionguide_id, artexhibitionitem_id, subtitle, subtitlekey, info, infokey, guideorder, photo, video, audio, created, lastmodified, lastmodifieduser, state, audiokey, videokey, language, draft, masterlanguage, translation, usethumbnail, intro, spec, opens, audioautogenerate, speechaudio, audio_id, infoaccessible, infoaccessibleisprimary, onlyaccesibleversion, audioaccessible, artwork_audio_id, artexhibition_item_audio_id) FROM stdin;
899	La jungla	\N	\N	\N	894	890	\N	\N	Wifredo Lam, el más universal de los pintores cubanos. Introdujo la cultura negra en la pintura cubana y desarrolló una renovadora obra que integra elementos de origen africano y chino presentes en Cuba. \r\n\r\nEl cuadro ha sido interpretado como la síntesis de un ciclo antillano, en virtud del espacio barroco dominante y de la atmósfera creada por la asociación de lo humano, lo animal, lo vegetal y lo divino. Hay en él un vocabulario visual que evolucionó desde el paisaje de corte académico hacia un tema y un lenguaje de arte moderno. En este óleo parecen fusionarse visiones y vivencias del pintor; el mítico paisaje insular, la incorporación de contenidos e iconografías procedentes de los sistemas mágico-religiosos de origen africano extendidos en Cuba y el Caribe\r\n\r\nSe la considera una síntesis de su política, donde se mezclan surrealismo y cubismo europeos con el poder del mito característico de los cultos sincréticos del Caribe.	\N	0	\N	\N	22475	2025-11-29 13:35:14.787459-03	2025-11-29 13:35:14.787466-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	f	\N	2	\N	f	f	\N	6	\N
249	Effet de neige à Louveciennes	effet-de-neige-louveciennes	Effet de neige à Louveciennes	\N	248	234	\N	\N	\N	\N	1	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	3	\N	f	f	\N	77	\N
35357	Traje de calle del alférez real	\N	\N	\N	33825	35278	\N	\N	El alférez real era uno de los principales funcionarios del Cabildo. Se encargaba de representar al rey en las ceremonias públicas portando el estandarte real. Además, se ocupaba de organizar las milicias en tiempos de guerra.\r\n\r\nEn las ceremonias públicas, el alférez real lucía un traje como este. La recreación fue realizada a partir del traje que perteneció a Francisco Antonio de Escalada, último alférez real del Cabildo de Buenos Aires, que forma parte de la colección del Museo. Está formado por calzón corto, chaleco y casaca. Se usaba con camisa blanca, medias del mismo color y zapatos con taco alto y grandes hebillas. En la época, este tipo de vestimenta era un signo de prestigio: contar con un traje como este era muy costoso.\r\n\r\nEsta recreación se realizó para proteger la pieza original de la exposición constante. Para replicar las medidas, se levantaron moldes a partir del traje original. \r\nSe seleccionó una tela similar y se la tiñó buscando una aproximación al color que tenía el traje en el momento de su uso.	\N	0	\N	\N	35503	2026-01-12 10:42:05.995277-03	2026-01-12 10:42:05.995281-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	2	\N	f	f	\N	5	\N
252	Femme aux champs (Campesina)	femme-aux-champs-campesina	Femme aux champs (Campesina)	\N	248	237	\N	\N	\N	\N	4	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	6	\N	f	f	\N	79	\N
50268	La vuelta al hogar	\N	\N	\N	50261	632	\N	\N	\N	\N	0	\N	\N	\N	2026-02-07 21:00:27.648328-03	2026-02-07 21:00:27.648335-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	41	\N	f	f	\N	61	\N
204	Le bain de Vénus (El baño de Venus)	le-bain-de-v-nus-el-ba-o-de-venus	Le bain de Vénus (El baño de Venus)	\N	202	200	\N	\N	\N	\N	0	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	2	\N	f	f	\N	62	\N
254	Vahine no te miti (Femme a la mer) (Mujer del mar)	vahine-no-te-miti-femme-a-la-mer-mujer-del-mar	Vahine no te miti (Femme a la mer) (Mujer del mar)	\N	248	239	\N	\N	\N	\N	6	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	8	\N	f	f	\N	63	\N
203	Morro da favela II (Pueblito)	morro-da-favela-ii-pueblito	Morro da favela II (Pueblito)	\N	202	201	\N	\N	\N	\N	0	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	1	\N	f	f	\N	64	\N
253	En observation - M.Fabre, Officier de reserve	en-observation-m-fabre-officier-de-reserve	En observation - M.Fabre, Officier de reserve	\N	248	238	\N	\N	\N	\N	5	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	7	\N	f	f	\N	65	\N
250	Le Pont d`Argenteuil (El Puente de Argenteuil)	le-pont-d-argenteuil-el-puente-de-argenteuil	Le Pont d`Argenteuil (El Puente de Argenteuil)	\N	248	235	\N	\N	\N	\N	2	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	4	\N	f	f	\N	74	\N
251	Prairies du Valhermeil près Pontoise	prairies-du-valhermeil-pr-s-pontoise	Prairies du Valhermeil près Pontoise	\N	248	235	\N	\N	\N	\N	3	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	5	\N	f	f	\N	74	\N
294	En Normandie	en-normandie	En Normandie	\N	292	286	\N	\N	\N	\N	2	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	10	\N	f	f	\N	72	\N
50282	La Emperatriz Theodora	\N	\N	\N	50261	408	\N	\N	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, presenta una visión orientalista, teatral y profundamente simbólica de la emperatriz bizantina Teodora, esposa del emperador Justiniano I, una de las mujeres más influyentes y poderosas en la historia del Imperio Romano de Oriente.\r\n\r\nNo se trata de una representación histórica literal, sino de una fantasía opulenta, propia de la fascinación europea del siglo XIX por el Oriente y por los antiguos imperios. Benjamin-Constant, reconocido por sus retratos de élite y figuras históricas, pone aquí en juego su virtuosismo técnico: el lujo, la sofisticación, las poses calculadas, y la representación minuciosa de texturas ricas como la seda, el terciopelo, el metal y la piel.\r\n\r\nLa emperatriz aparece representada de cuerpo entero, sentada en una posición elevada, casi como en un trono bajo. Su figura ocupa el centro del cuadro. El cuerpo está levemente girado hacia la izquierda, mientras que el rostro mira de frente al espectador, con una mirada fija, intensa y desafiante. No sonríe. Su expresión transmite autoridad, inteligencia y una calma segura, casi intimidante.\r\n\r\nViste una túnica larga y pesada, ricamente ornamentada, de tonos claros y dorados, que cae en pliegues profundos hasta el suelo. Sobre la cabeza lleva un elaborado tocado o corona, adornado con piedras preciosas que brillan sutilmente. Los brazos están cubiertos de joyas, pulseras y anillos, que refuerzan la idea de riqueza y poder. La piel clara contrasta con los colores cálidos del entorno.\r\n\r\nEl espacio que la rodea es cerrado y lujoso. Detrás de ella se perciben cortinados oscuros, posiblemente de terciopelo, y superficies decoradas con motivos orientales. La iluminación es suave pero dirigida: la luz parece caer principalmente sobre el rostro y el cuerpo de Teodora, separándola del fondo y convirtiéndola en el foco absoluto de la escena.\r\n\r\nDe origen humilde, Teodora se ganó la vida de joven como actriz y cortesana hasta que fue elegida por Justiniano como esposa. Las fuentes históricas antiguas, como Procopio de Cesarea, y más tarde Edward Gibbon, la describieron como astuta, inteligente y cruel, una imagen hoy considerada exagerada y condicionada por la visión rígida del rol femenino. Actualmente se la reconoce como co-gobernante de facto, una figura política central del Imperio Bizantino.\r\n\r\nDurante la revuelta de Niká en 532, convenció a Justiniano de no huir y enfrentar a sus enemigos con la célebre frase: “el púrpura es una buena mortaja”. Impulsó leyes a favor de los derechos de las mujeres, prohibió el tráfico sexual forzado, fortaleció derechos matrimoniales y fundó hogares para mujeres rescatadas de la prostitución, algo sin precedentes en su tiempo. Gracias a sus acciones, la situación de las mujeres en el Imperio Bizantino fue más avanzada que en gran parte de Europa.\r\n\r\nEn la historia argentina, el paralelismo inmediato es con Eva Perón.\r\n\r\nBenjamin-Constant buscó crear un ícono visual del poder femenino y del exotismo oriental. El resultado, quizás involuntario, es también un símbolo de modernidad: una mujer joven, poderosa, hermosa e inteligente, que sostiene la mirada del espectador y se atreve a ser distinta.	\N	0	\N	\N	50337	2026-02-07 21:12:42.22506-03	2026-02-07 21:13:11.924321-03	100	3	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	43	\N	f	f	\N	78	\N
298	Reposo	reposo	Reposo	\N	292	290	\N	\N	\N	\N	6	\N	\N	338	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	21	\N	f	f	\N	81	\N
261	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	danseuse-et-admirateur-derri-re-la-sc-ne-bailarina-y-admirador-tras-la-escena	Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)	\N	248	246	\N	\N	\N	\N	13	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	15	\N	f	f	\N	49	\N
50296	Retrato de Juan Manuel de Rosas	\N	\N	\N	50261	409	\N	\N	\N	\N	0	\N	\N	\N	2026-02-07 21:12:50.149669-03	2026-02-07 21:12:50.149678-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	45	\N	f	f	\N	53	\N
295	Sin pan y sin trabajo	sin-pan-y-sin-trabajo	Sin pan y sin trabajo	\N	292	287	\N	\N	\N	\N	3	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	17	\N	f	f	\N	67	\N
50275	En la costa de Valencia	\N	\N	\N	50261	410	\N	\N	\N	\N	0	\N	\N	\N	2026-02-07 21:12:16.876947-03	2026-02-07 21:12:16.876957-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	42	\N	f	f	\N	50	\N
257	La Coiffure (El peinado)	la-coiffure-el-peinado	La Coiffure (El peinado)	\N	248	242	\N	\N	\N	\N	9	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	11	\N	f	f	\N	51	\N
255	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	portrait-de-suzanne-valadon-madame-suzanne-valadon-artiste-peintre-retrato-de-suzanne-valadon-pintora	Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)	\N	248	240	\N	\N	\N	\N	7	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	22	\N	f	f	\N	52	\N
256	La berge de La Seine (Orillas del Sena)	la-berge-de-la-seine-orillas-del-sena	La berge de La Seine (Orillas del Sena)	\N	248	241	\N	\N	\N	\N	8	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	23	\N	f	f	\N	57	\N
299	Abel	abel	Abel	\N	292	291	\N	\N	\N	\N	7	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	20	\N	f	f	\N	73	\N
488	Utopia del Sur	\N	Utopia del Sur	\N	411	486	\N	\N	Desde el Legado Nicolás García Uriburu trabajamos para preservar y difundir su legado artístico. Custodiamos su colección, gestionamos integralmente su obra y procesamos su archivo documental con el fin de mantener vigente su mensaje y contribuir a la reflexión sobre arte y ecología.	\N	1	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-11-21 16:07:15.938459-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	9	\N	f	f	\N	71	\N
40893	Vara de mando de Manuel Mansilla	\N	\N	\N	33825	40769	\N	\N	La vara es un símbolo de poder que distinguía a las autoridades del Cabildo cuando ejercían funciones en la ciudad.\r\nEsta perteneció a Manuel Mansilla, aguacil mayor del Cabildo entre 1975 y 1821. Como Mansilla ejercía un cargo perpetuo, la pieza lleva en la empuñadura un monograma con sus iniciales.	\N	0	\N	\N	\N	2026-01-13 15:24:05.236551-03	2026-01-13 15:24:05.236558-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	3	\N	f	f	\N	4	\N
259	Le Moulin de la Galette	le-moulin-de-la-galette	Le Moulin de la Galette	\N	248	244	\N	\N	\N	\N	11	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	13	\N	f	f	\N	49	\N
50289	Procesión sorprendida por la lluvia	\N	\N	\N	50261	487	\N	\N	\N	\N	0	\N	\N	\N	2026-02-07 21:12:48.341099-03	2026-02-07 21:12:48.341104-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	44	\N	f	f	\N	58	\N
297	El despertar de la criada	el-despertar-de-la-criada	El despertar de la criada	\N	292	289	\N	\N	\N	\N	5	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	19	\N	f	f	\N	59	\N
260	Portrait Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	portrait-ernest-hosched-et-sa-fille-marthe-retrato-del-se-or-hosched-y-su-hija	Portrait Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)	\N	248	245	\N	\N	\N	\N	12	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	14	\N	f	f	\N	49	\N
296	La vuelta al hogar	la-vuelta-al-hogar	La vuelta al hogar	\N	292	288	\N	\N	\N	\N	4	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-05-19 12:30:25.063882-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	18	\N	f	f	\N	61	\N
262	La Nymphe surprise (La ninfa sorprendida)	la-nymphe-surprise-la-ninfa-sorprendida	La Nymphe surprise (La ninfa sorprendida)	\N	248	247	\N	\N	\N	\N	14	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	16	\N	f	f	\N	49	\N
258	La Toilette apres le bain (El arreglo después del baño)	la-toilette-apres-le-bain-el-arreglo-despu-s-del-ba-o	La Toilette apres le bain (El arreglo después del baño)	\N	248	243	\N	\N	\N	\N	10	\N	\N	\N	2025-05-19 12:29:43.192411-03	2025-05-19 12:29:43.192411-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	12	\N	f	f	\N	66	\N
18152	Je suis (Yo soy)	\N	\N	\N	894	16643	\N	\N	\N	\N	0	\N	\N	\N	2026-01-05 10:43:44.89555-03	2026-01-05 10:43:44.895556-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	3	\N	f	f	\N	8	\N
460	Apocalipsis	\N	Apocalipsis	\N	458	456	\N	\N	\N	\N	2	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-10-01 13:36:52.773329-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	26	\N	f	f	\N	82	\N
414	En la costa de Valencia	en-la-costa-de-valencia	En la costa de Valencia	\N	411	410		\N	Joaquín Sorolla, el "Maestro de la Luz", inmortalizó la costa de Valencia, especialmente la Playa de la Malvarrosa, capturando la luz mediterránea y escenas costumbristas de pescadores y niños jugando en obras icónicas como "En la costa de Valencia" (1898) y "Corriendo por la playa" (1908), destacando por su estilo luminista, pinceladas vibrantes y la representación de la vida cotidiana valenciana, llena de movimiento y reflejos del sol sobre el mar.	\N	3	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-10-01 16:10:10.760825-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	28	\N	f	f	\N	50	\N
413	Retrato de Juan Manuel de Rosas	retrato-de-juan-manuel-de-rosas	Retrato de Juan Manuel de Rosas	\N	411	409	El gran restaurador	\N	El Retrato de Juan Manuel de Rosas, pintado por el artista francés Raymond Monvoisin hacia 1842, ofrece una representación simbólica y estratégica del caudillo argentino.  A diferencia de otros cuadros oficiales donde Rosas aparece con uniforme militar, en esta obra se lo muestra con un poncho, resaltando su imagen de líder popular y no meramente militar.\\nMonvoisin se había formado en la tradición académica europea, pero para 1842 estaba más cerca del romanticismo. \r\n\r\nRosas aparece de perfil, con expresión seria y firme, destacando su autoridad, y su mirada apunta a donde se origina el viento como  controlando la naturaleza. Mitre señaló “es el retrato más parecido que conozco de Rosas, hay mucho de emperador romano pero atenuado y corregido por un acento criollo”.\r\n\r\nLa tradición artística oficial argentina considera al cuadro un estudio preliminar de una obra posterior desaparecida o que no existió. Según Rodrigo Cañete en “Historia a contrapelo del arte argentino”,  esta idea está contaminada por el prejuicio de negarse a aceptar el verdadero carácter de la obra.  \r\n\r\nEl artista, acogido en Chile por los enemigos de Rosas, tuvo el talento de hacer un cuadro ambigüo para que ambos bandos -unitarios y federales- puedan utilizarlo como bandera. Los rasgos de Rosas son imperiales pero carecen de la musculatura heroica de los emperadores y están levemente feminizados, y el poncho y la ropa son parecidos a los retratos de sus enemigos como gaucho bárbaro, líder de lo que Sarmiento despectivamente llamaba la “montonera árabe”.  \r\n\r\nPor su parte, para los rosistas, representa un emperador-dios-benefactor-madre tierra, al estilo de la Virgen María de las imágenes sudamericanas que la fusionan con la Pachamama india, como la Virgen María del Cerro Rico de Potosí,  pero con referencias académicas sutiles a grandes maestros como Zurbarán y Velázquez.	\N	2	\N	\N	424	2025-06-10 15:14:26.074639-03	2025-09-30 10:07:01.235109-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	24	\N	f	f	\N	53	\N
459	Sendas perdidas	\N	Sendas perdidas	\N	458	455	\N	\N	\N	\N	1	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-11-23 19:55:45.874884-03	100	1	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	30	\N	f	f	\N	55	\N
293	La vuelta del malón	la-vuelta-del-mal-n	La vuelta del malón	\N	292	285	\N	\N	\N	\N	1	\N	\N	\N	2025-05-19 12:30:25.063882-03	2025-09-30 16:48:47.212018-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	25	\N	f	f	\N	60	\N
461	El recurso del método	\N	El recurso del método	\N	458	457	\N	\N	\N	\N	3	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-10-01 13:37:03.415513-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	27	\N	f	f	\N	76	\N
18156	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	894	16647	\N	\N	Las religiones afrocubanas incluyen el Lucumí, o Santería, del pueblo de habla yoruba del suroeste de Nigeria, que es el culto a los orishas o espíritus. También tenemos el Palo, de África Central, las prácticas de Benín llamadas Arará, así como ideas europeas o cristianas.\r\n\r\nEl título de esta pintura, Malembo Sombrío, Dios de la Encrucijada, hace referencia al orisha de la tradición lucumí o santería.\r\nLa palabra "Malembo" en el título podría referirse a un puerto negrero en la costa occidental de África. \r\nPara cuando Lam nació en 1902, solo habían pasado 16 años desde la abolición de la esclavitud en Cuba.\r\n\r\nEn gran parte de la obra de Lam se observa lo que podría identificarse como una figura similar a la de Eleguá: esta pequeña cabeza abovedada. Eleguá es el orisha de la encrucijada. Tiene la capacidad única de abrir puertas, comunicarse entre idiomas y atraer la buena fortuna y alejar la mala suerte.	\N	0	\N	\N	\N	2026-01-05 10:43:48.016993-03	2026-01-05 10:43:48.016996-03	100	1	\N	\N	es	\N	es	\N	t	\N	\N	\N	t	\N	4	\N	f	f	\N	7	\N
412	La Emperatriz Theodora	la-emperatriz-theodora	La Emperatriz Theodora	\N	411	408	\N	\N	El cuadro Theodora, pintado por Jean-Joseph Benjamin-Constant en 1887, representa una visión orientalista y teatral de la emperatriz bizantina Teodora, esposa de Justiniano I,  la mujer más influyente y poderosa en la historia del Imperio romano de Oriente.\r\n\r\nNo es una representación histórica, sino más bien una fantasía opulenta basada en la fascinación europea del siglo XIX por el Oriente y el pasado imperial. Benjamin-Constant era un pintor de gran técnica especializado en retratos de élite y figuras históricas: poder, lujo,  sofisticación, poses calculadas,  texturas complejas como seda y terciopelo, y entornos refinados. \r\n\r\nDe origen humilde, Theodora se ganó la vida de joven como actriz y cortesana hasta que fue elegida por Justiniano como esposa. La imagen histórica -basada en Procopio de Cesarea en la antigüedad  y Gibbon en el siglo XVIII-  es  la de una mujer astuta, inteligente y cruel. En la actualidad se considera que ese perfil es exagerado por la visión rígida que se tenía de la función social de la mujer. La visión moderna es que fue una figura política central en el Imperio Bizantino,  co-gobernante de facto con Justiniano. Participó activamente en las decisiones de gobierno. En la revuelta de Niká de 532, convenció a Justiniano de no huir y enfrentar a sus enemigos con su famosa frase -“el púrpura es una buena mortaja”-. Promovió leyes en favor de los derechos de las mujeres: prohibió el tráfico sexual forzado,  fortaleció los derechos de las mujeres en el matrimonio y el divorcio, y fundó hogares para mujeres rescatadas de la prostitución, algo sin precedentes en la época. Como resultado de los esfuerzos de Teodora, el estado de la mujer en el Imperio Bizantino fue más elevado que el del resto de las mujeres en Europa.\r\n\r\nEn la historia argentina el paralelismo inmediato es con Eva Perón.\r\n\r\nBenjamin-Constant se propuso crear un ícono visual del poder femenino y del exotismo oriental. y lo que salió, quizás involuntariamente,  es todo un símbolo de modernidad: una mujer joven, poderosa, hermosa e inteligente con mirada desafiante que se atreve a ser distinta.	\N	1	\N	\N	426	2025-06-10 15:14:26.074639-03	2025-10-28 12:26:06.382343-03	100	3	\N	\N	es	\N	es	0	t	\N	\N	\N	f	\N	29	\N	f	f	\N	78	\N
\.


--
-- Data for Name: guidecontentrecord; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.guidecontentrecord (id, guidecontent_id, language, name, subtitle, info, photo, video, audio, created, lastmodified, lastmodifieduser, draft, namekey, state, usethumbnail, name_hash, subtitle_hash, info_hash, intro_hash, spec, spec_hash, otherjson, otherjson_hash, opens, opens_hash, audioautogenerate, intro, audioauto, infoaccessible, infoaccesible_hash, audioaccessible) FROM stdin;
746	412	en	Empress Theodora	\N	The painting Theodora, painted by Jean-Joseph Benjamin-Constant in 1887, depicts an Orientalist and theatrical vision of the Byzantine Empress Theodora, wife of Justinian I, the most influential and powerful woman in the history of the Eastern Roman Empire.\n\nIt is not a historical representation, but rather an opulent fantasy based on the 19th-century European fascination with the Orient and the imperial past. Benjamin-Constant was a highly technical painter specializing in portraits of elite figures and historical figures: power, luxury, sophistication, calculated poses, complex textures such as silk and velvet, and refined settings.\n\nOf humble origins, Theodora earned her living as a young woman as an actress and courtesan until she was chosen by Justinian as his wife. The historical image—based on Procopius of Caesarea in antiquity and Gibbon in the 18th century—is that of a cunning, intelligent, and cruel woman. This profile is now considered exaggerated due to the rigid view of women's social role. The modern view is that she was a central political figure in the Byzantine Empire, de facto co-ruler with Justinian. She actively participated in government decisions. In the Nika revolt of 532, she convinced Justinian not to flee and instead confront his enemies with her famous phrase, "purple is a good shroud." She promoted laws favoring women's rights: she prohibited forced sex trafficking, strengthened women's rights in marriage and divorce, and founded homes for women rescued from prostitution, something unprecedented at the time. As a result of Theodora's efforts, the status of women in the Byzantine Empire was higher than that of women in Europe.\n\nIn Argentine history, the immediate parallel is with Eva Perón.\n\nBenjamin-Constant set out to create a visual icon of feminine power and oriental exoticism. And what emerged, perhaps unintentionally, is a symbol of modernity: a young, powerful, beautiful, and intelligent woman with a defiant gaze who dares to be different.	\N	\N	\N	2025-10-27 14:22:38.757195-03	2025-10-28 12:59:31.453189-03	100	\N	\N	\N	f	-90724174	0	1629620206	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
819	460	en	Apocalipsis	\N	\N	\N	\N	\N	2025-11-20 08:17:29.468878-03	2025-11-20 08:17:29.468887-03	100	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
820	460	pt-BR	Apocalipsis	\N	\N	\N	\N	\N	2025-11-20 08:17:34.019636-03	2025-11-20 08:17:34.019642-03	100	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
903	459	en	Sendas perdidas	\N	\N	\N	\N	\N	2025-11-29 21:30:43.261445-03	2025-11-29 21:30:43.261457-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
35358	35357	es	Traje de calle del alférez real	\N	\N	\N	\N	\N	2026-01-12 10:42:05.997068-03	2026-01-12 10:42:05.99707-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
1494	459	pt-BR	Sendas perdidas	\N	\N	\N	\N	\N	2025-12-17 12:50:02.532431-03	2025-12-17 12:50:02.532442-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
35359	35357	en	Traje de calle del alférez real	\N	\N	\N	\N	\N	2026-01-12 10:42:05.997822-03	2026-01-12 10:42:05.997823-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
737	414	pt-BR	Na costa de Valência	\N	Joaquín Sorolla, o "Mestre da Luz", imortalizou o litoral de Valência, em especial a Praia de Malvarrosa, capturando a luz do Mediterrâneo e cenas cotidianas de pescadores e crianças brincando em obras icônicas como "Na Costa de Valência" (1898) e "Correndo na Praia" (1908), destacando-se pelo seu estilo luminista, pinceladas vibrantes e a representação da vida diária valenciana, repleta de movimento e reflexos do sol no mar.	\N	\N	\N	2025-10-25 17:27:53.087335-03	2025-12-08 12:15:27.725212-03	100	\N	\N	\N	f	-1476067600	0	-1452833376	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
736	414	en	On the coast of Valencia	\N	Joaquín Sorolla, the "Master of Light", immortalized the coast of Valencia, especially Malvarrosa Beach, capturing the Mediterranean light and everyday scenes of fishermen and children playing in iconic works such as "On the Coast of Valencia" (1898) and "Running on the Beach" (1908), standing out for his luminist style, vibrant brushstrokes and the representation of Valencian daily life, full of movement and reflections of the sun on the sea.	\N	\N	\N	2025-10-25 17:27:21.91188-03	2025-12-18 13:53:58.905467-03	100	\N	\N	\N	f	-1476067600	0	-1452833376	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
35360	35357	pt-BR	Traje de calle del alférez real	\N	\N	\N	\N	\N	2026-01-12 10:42:05.998374-03	2026-01-12 10:42:05.998374-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
922	488	en	Southern Utopia	\N	At the Nicolás García Uriburu Legacy, we work to preserve and disseminate his artistic legacy. We safeguard his collection, manage his work comprehensively, and process his documentary archive in order to keep his message relevant and contribute to reflection on art and ecology.	\N	\N	\N	2025-12-04 10:01:38.699045-03	2025-12-18 16:17:42.382131-03	100	\N	\N	\N	t	-2142313869	0	-909044814	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
740	413	en	Portrait of Juan Manuel de Rosas	The great restorer	The Portrait of Juan Manuel de Rosas, painted by the French artist Raymond Monvoisin around 1842, offers a symbolic and strategic representation of the Argentine caudillo. Unlike other official portraits where Rosas appears in military uniform, in this work he is shown wearing a poncho, highlighting his image as a popular leader and not merely a military figure. Monvoisin had trained in the European academic tradition, but by 1842 he was closer to Romanticism. Rosas appears in profile, with a serious and firm expression, emphasizing his authority, and his gaze points toward the source of the wind, as if controlling nature. Mitre noted, "It is the most accurate portrait I know of Rosas; there is much of the Roman emperor in it, but tempered and corrected by a Creole accent." The official Argentine artistic tradition considers the painting a preliminary study for a later work that has disappeared or never existed. According to Rodrigo Cañete in "Historia a contrapelo del arte argentino" (A History Against the Grain of Argentine Art), this idea is tainted by the prejudice of refusing to accept the true nature of the work. According to Cañete, the artist, welcomed in Chile by Rosas's enemies, had the talent to create an ambiguous painting that both sides—Unitarians and Federalists—could use as a symbol. Rosas's features are imperial but lack the heroic musculature of emperors and are slightly feminized, and his poncho and clothing resemble portraits of his enemies as a barbaric gaucho, leader of what Sarmiento contemptuously called the "Arab mob." For the Rosas supporters, he represents an emperor-god-benefactor-mother earth, in the style of the Virgin Mary in South American images that merge her with the indigenous Pachamama, like the Virgin Mary of Cerro Rico in Potosí, but with subtle academic references to great masters like Zurbarán and Velázquez.	\N	\N	\N	2025-10-26 13:15:16.71149-03	2025-12-18 16:18:28.711548-03	100	\N	\N	\N	f	-1516229879	685711169	-1497012282	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
1108	488	pt-BR	Utopia do Sul	\N	No Legado Nicolás García Uriburu, trabalhamos para preservar e divulgar seu legado artístico. Salvaguardamos sua coleção, gerenciamos sua obra de forma abrangente e processamos seu arquivo documental para manter sua mensagem relevante e contribuir para a reflexão sobre arte e ecologia.	\N	\N	\N	2025-12-08 17:57:56.718589-03	2025-12-24 17:36:15.831513-03	100	\N	\N	\N	t	-2142313869	0	-909044814	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
18153	18152	es	Je suis (Yo soy)	\N	\N	\N	\N	\N	2026-01-05 10:43:44.901766-03	2026-01-05 10:43:44.901768-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
18154	18152	en	Je suis (Yo soy)	\N	\N	\N	\N	\N	2026-01-05 10:43:44.902947-03	2026-01-05 10:43:44.902947-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
18155	18152	pt-BR	Je suis (Yo soy)	\N	\N	\N	\N	\N	2026-01-05 10:43:44.903578-03	2026-01-05 10:43:44.903578-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
18157	18156	es	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	\N	\N	2026-01-05 10:43:48.019403-03	2026-01-05 10:43:48.019405-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
18158	18156	en	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	\N	\N	2026-01-05 10:43:48.021261-03	2026-01-05 10:43:48.021263-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
18159	18156	pt-BR	Le Sombre Malembo, Dieu du carrefour	\N	\N	\N	\N	\N	2026-01-05 10:43:48.022806-03	2026-01-05 10:43:48.022808-03	100	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
902	899	pt-BR	A selva	\N		\N	\N	914	2025-11-29 13:35:14.791836-03	2025-11-30 17:57:48.104084-03	100	\N	\N	\N	t	1098792388	0	402120604	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
901	899	en	The jungle	\N	Wifredo Lam, the most universal of Cuban painters, introduced Black culture into Cuban painting and developed a groundbreaking body of work that integrates elements of African and Chinese origin present in Cuba.\n\nThe painting has been interpreted as the synthesis of an Antillean cycle, by virtue of the dominant Baroque space and the atmosphere created by the association of the human, the animal, the vegetal, and the divine. It contains a visual vocabulary that evolved from academic landscape painting toward a theme and language of modern art. In this oil painting, the painter's visions and experiences seem to merge: the mythical island landscape, the incorporation of content and iconography from the magical-religious systems of African origin prevalent in Cuba and the Caribbean.\n\nIt is considered a synthesis of his political philosophy, where European Surrealism and Cubism are mixed with the power of myth characteristic of the syncretic cults of the Caribbean.	\N	\N	918	2025-11-29 13:35:14.791452-03	2026-01-06 15:11:18.615127-03	100	\N	\N	\N	t	1098792388	0	1842841698	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
40894	40893	es	Vara de mando de Manuel Mansilla	\N	\N	\N	\N	\N	2026-01-13 15:24:05.238085-03	2026-01-13 15:24:05.238089-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
40895	40893	en	Manuel Mansilla's staff of office	\N	The staff is a symbol of power that distinguished the authorities of the Cabildo (town council) when they held office in the city.\nThis one belonged to Manuel Mansilla, chief constable of the Cabildo between 1795 and 1821. As Mansilla held a perpetual position, the staff bears a monogram with his initials on the handle.	\N	\N	\N	2026-01-13 15:24:05.238844-03	2026-01-13 15:44:15.152576-03	100	\N	\N	1	t	-1331906940	0	-995107724	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
40896	40893	pt-BR	Equipe de escritório de Manuel Mansilla	\N	O bastão é um símbolo de poder que distinguia as autoridades do Cabildo (conselho municipal) quando exerciam funções na cidade.\nEste pertenceu a Manuel Mansilla, chefe de polícia do Cabildo entre 1795 e 1821. Como Mansilla ocupava um cargo perpétuo, o bastão traz um monograma com suas iniciais no cabo.	\N	\N	\N	2026-01-13 15:24:05.239319-03	2026-01-13 15:44:33.684338-03	100	\N	\N	1	t	-1331906940	0	-995107724	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
741	413	pt-BR	Retrato de Juan Manuel de Rosas	O grande restaurador	O Retrato de Juan Manuel de Rosas, pintado pelo artista francês Raymond Monvoisin por volta de 1842, oferece uma representação simbólica e estratégica do caudilho argentino. Ao contrário de outros retratos oficiais em que Rosas aparece em uniforme militar, nesta obra ele é retratado usando um poncho, destacando sua imagem como líder popular e não meramente como figura militar. Monvoisin havia sido formado na tradição acadêmica europeia, mas em 1842 já se aproximava do Romantismo.\n\nRosas aparece de perfil, com uma expressão séria e firme, enfatizando sua autoridade, e seu olhar está voltado para a origem do vento, como se controlasse a natureza. Mitre observou: "É o retrato mais preciso que conheço de Rosas; há muito do imperador romano nele, mas suavizado e corrigido por um sotaque crioulo."\n\nA tradição artística oficial argentina considera a pintura um estudo preliminar para uma obra posterior que desapareceu ou nunca existiu. Segundo Rodrigo Cañete em “Historia a contrapelo del arte argentino” (História contra a corrente da arte argentina), essa ideia é contaminada pelo preconceito de se recusar a aceitar a verdadeira natureza da obra.\n\nO artista, acolhido no Chile pelos inimigos de Rosas, tinha o talento de criar uma pintura ambígua para que ambos os lados — unitaristas e federalistas — pudessem usá-la como símbolo. Os traços de Rosas são imperiais, mas carecem da musculatura heroica dos imperadores e são ligeiramente feminizados, e o poncho e as vestes são semelhantes aos retratos de seus inimigos como um gaúcho bárbaro, líder do que Sarmiento chamava desdenhosamente de “turba árabe”.\n\nPara os partidários de Rosas, ele representa um imperador-deus-benfeitor-mãe terra, no estilo da Virgem Maria em imagens sul-americanas que a fundem com a Pachamama indígena, como a Virgem Maria de Cerro Rico em Potosí, mas com sutis referências acadêmicas a grandes mestres como Zurbarán e Velázquez.	\N	\N	\N	2025-10-26 13:15:23.412529-03	2026-02-11 17:48:10.086763-03	100	\N	\N	\N	f	-1516229879	685711169	-489209037	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
747	412	pt-BR	Imperatriz Teodora	\N	O quadro Teodora, pintado por Jean-Joseph Benjamin-Constant em 1887, apresenta uma visão orientalista e teatral da imperatriz bizantina Teodora, esposa de Justiniano I, a mulher mais influente e poderosa da história do Império Romano do Oriente.\n\nNão se trata de uma representação histórica, mas sim de uma fantasia opulenta baseada na fascinação europeia do século XIX pelo Oriente e pelo passado imperial. Benjamin-Constant era um pintor altamente talentoso, especializado em retratos de figuras históricas e da elite: poder, luxo, sofisticação, poses calculadas, texturas complexas como seda e veludo, e cenários refinados.\n\nDe origem humilde, Teodora ganhava a vida como atriz e cortesã quando jovem, até ser escolhida por Justiniano para ser sua esposa. A imagem histórica — baseada em Procópio de Cesareia, na Antiguidade, e em Gibbon, no século XVIII — é a de uma mulher astuta, inteligente e cruel. Hoje, esse perfil é considerado exagerado devido à visão rígida do papel social da mulher na época. A visão moderna é que ela foi uma figura política central no Império Bizantino, uma corregente de fato com Justiniano. Ela participou ativamente das decisões governamentais. Na Revolta de Nika, em 532, convenceu Justiniano a não fugir e a confrontar seus inimigos com sua famosa frase: "O púrpura é uma boa mortalha". Ela promoveu leis em favor dos direitos das mulheres: proibiu o tráfico sexual forçado, fortaleceu os direitos das mulheres no casamento e no divórcio e fundou casas para mulheres resgatadas da prostituição, algo sem precedentes na época. Como resultado dos esforços de Teodora, o status das mulheres no Império Bizantino era superior ao das mulheres no resto da Europa.\n\nNa história argentina, o paralelo imediato é com Eva Perón.\n\nBenjamin-Constant propôs-se a criar um ícone visual de poder feminino e exotismo oriental. E o que surgiu, talvez sem intenção, foi um símbolo da modernidade: uma mulher jovem, poderosa, bonita e inteligente, com um olhar desafiador, que ousa ser diferente.	\N	\N	\N	2025-10-27 14:22:40.784208-03	2026-02-04 16:05:05.311276-03	100	\N	\N	\N	f	-90724174	0	1629620206	0	\N	0	\N	0	\N	0	f	\N	f	\N	0	\N
50269	50268	es	La vuelta al hogar	\N	\N	\N	\N	\N	2026-02-07 21:00:27.656248-03	2026-02-07 21:00:27.656252-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50270	50268	en	La vuelta al hogar	\N	\N	\N	\N	\N	2026-02-07 21:00:27.657732-03	2026-02-07 21:00:27.657732-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50271	50268	pt-BR	La vuelta al hogar	\N	\N	\N	\N	\N	2026-02-07 21:00:27.658318-03	2026-02-07 21:00:27.658319-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50272	50268	it	La vuelta al hogar	\N	\N	\N	\N	\N	2026-02-07 21:00:27.658732-03	2026-02-07 21:00:27.658732-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50273	50268	fr	La vuelta al hogar	\N	\N	\N	\N	\N	2026-02-07 21:00:27.659314-03	2026-02-07 21:00:27.659315-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50274	50268	ger	La vuelta al hogar	\N	\N	\N	\N	\N	2026-02-07 21:00:27.659959-03	2026-02-07 21:00:27.65996-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50276	50275	es	En la costa de Valencia	\N	\N	\N	\N	\N	2026-02-07 21:12:16.883182-03	2026-02-07 21:12:16.883188-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50277	50275	en	En la costa de Valencia	\N	\N	\N	\N	\N	2026-02-07 21:12:16.884049-03	2026-02-07 21:12:16.884053-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50278	50275	pt-BR	En la costa de Valencia	\N	\N	\N	\N	\N	2026-02-07 21:12:16.884724-03	2026-02-07 21:12:16.884731-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50279	50275	it	En la costa de Valencia	\N	\N	\N	\N	\N	2026-02-07 21:12:16.885381-03	2026-02-07 21:12:16.885385-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50280	50275	fr	En la costa de Valencia	\N	\N	\N	\N	\N	2026-02-07 21:12:16.886026-03	2026-02-07 21:12:16.886029-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50281	50275	ger	En la costa de Valencia	\N	\N	\N	\N	\N	2026-02-07 21:12:16.886883-03	2026-02-07 21:12:16.886887-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50283	50282	es	La Emperatriz Theodora	\N	\N	\N	\N	\N	2026-02-07 21:12:42.225985-03	2026-02-07 21:12:42.225988-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50284	50282	en	La Emperatriz Theodora	\N	\N	\N	\N	\N	2026-02-07 21:12:42.22668-03	2026-02-07 21:12:42.226683-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50286	50282	it	La Emperatriz Theodora	\N	\N	\N	\N	\N	2026-02-07 21:12:42.228198-03	2026-02-07 21:12:42.228202-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50287	50282	fr	La Emperatriz Theodora	\N	\N	\N	\N	\N	2026-02-07 21:12:42.228993-03	2026-02-07 21:12:42.228995-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50288	50282	ger	La Emperatriz Theodora	\N	\N	\N	\N	\N	2026-02-07 21:12:42.229661-03	2026-02-07 21:12:42.229664-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50290	50289	es	Procesión sorprendida por la lluvia	\N	\N	\N	\N	\N	2026-02-07 21:12:48.342161-03	2026-02-07 21:12:48.342164-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50291	50289	en	Procesión sorprendida por la lluvia	\N	\N	\N	\N	\N	2026-02-07 21:12:48.343001-03	2026-02-07 21:12:48.343004-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50292	50289	pt-BR	Procesión sorprendida por la lluvia	\N	\N	\N	\N	\N	2026-02-07 21:12:48.343802-03	2026-02-07 21:12:48.343805-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50293	50289	it	Procesión sorprendida por la lluvia	\N	\N	\N	\N	\N	2026-02-07 21:12:48.344744-03	2026-02-07 21:12:48.344748-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50294	50289	fr	Procesión sorprendida por la lluvia	\N	\N	\N	\N	\N	2026-02-07 21:12:48.345863-03	2026-02-07 21:12:48.345866-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50295	50289	ger	Procesión sorprendida por la lluvia	\N	\N	\N	\N	\N	2026-02-07 21:12:48.346758-03	2026-02-07 21:12:48.346762-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50297	50296	es	Retrato de Juan Manuel de Rosas	\N	\N	\N	\N	\N	2026-02-07 21:12:50.151073-03	2026-02-07 21:12:50.151077-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50298	50296	en	Retrato de Juan Manuel de Rosas	\N	\N	\N	\N	\N	2026-02-07 21:12:50.152079-03	2026-02-07 21:12:50.152084-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50299	50296	pt-BR	Retrato de Juan Manuel de Rosas	\N	\N	\N	\N	\N	2026-02-07 21:12:50.15294-03	2026-02-07 21:12:50.152943-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50300	50296	it	Retrato de Juan Manuel de Rosas	\N	\N	\N	\N	\N	2026-02-07 21:12:50.153817-03	2026-02-07 21:12:50.153832-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50301	50296	fr	Retrato de Juan Manuel de Rosas	\N	\N	\N	\N	\N	2026-02-07 21:12:50.154628-03	2026-02-07 21:12:50.15463-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50302	50296	ger	Retrato de Juan Manuel de Rosas	\N	\N	\N	\N	\N	2026-02-07 21:12:50.157111-03	2026-02-07 21:12:50.157114-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
50285	50282	pt-BR	Imperatriz Teodora	\N	A pintura Teodora, de Jean-Joseph Benjamin-Constant, de 1887, apresenta uma visão orientalista, teatral e profundamente simbólica da imperatriz bizantina Teodora, esposa do imperador Justiniano I, uma das mulheres mais influentes e poderosas da história do Império Romano do Oriente.\n\nNão se trata de uma representação histórica literal, mas sim de uma fantasia opulenta, característica do fascínio europeu do século XIX pelo Oriente e pelos impérios antigos. Benjamin-Constant, renomado por seus retratos de elites e figuras históricas, demonstra aqui seu virtuosismo técnico: o luxo, a sofisticação, as poses calculadas e a representação meticulosa de texturas ricas como seda, veludo, metal e couro.\n\nA imperatriz é retratada de corpo inteiro, sentada em uma posição elevada, quase como em um trono baixo. Sua figura ocupa o centro da pintura. O corpo está ligeiramente voltado para a esquerda, enquanto o rosto encara o observador com um olhar fixo, intenso e desafiador. Ela não sorri. Sua expressão transmite autoridade, inteligência e uma calma confiante, quase intimidante.\n\nEla veste um longo e pesado robe ricamente ornamentado em tons claros e dourados, que cai em profundas pregas até o chão. Na cabeça, usa um elaborado adorno ou coroa, cravejado de pedras preciosas que brilham sutilmente. Seus braços estão cobertos de joias, pulseiras e anéis, reforçando a ideia de riqueza e poder. Sua pele clara contrasta com as cores quentes do ambiente.\n\nO espaço ao seu redor é fechado e luxuoso. Atrás dela, cortinas escuras, possivelmente de veludo, e superfícies decoradas com motivos orientais são visíveis. A iluminação é suave, mas direcionada: a luz parece incidir principalmente sobre o rosto e o corpo de Teodora, separando-a do fundo e tornando-a o foco absoluto da cena.\n\nDe origem humilde, Teodora ganhava a vida como jovem atriz e cortesã até ser escolhida por Justiniano para ser sua esposa. Fontes históricas antigas, como Procópio de Cesareia e, posteriormente, Edward Gibbon, a descreveram como astuta, inteligente e cruel — uma imagem hoje considerada exagerada e influenciada por uma visão rígida do papel feminino. Atualmente, ela é reconhecida como uma corregente de fato, uma figura política central do Império Bizantino.\n\nDurante a Revolta de Nika, em 532, ela convenceu Justiniano a não fugir e a confrontar seus inimigos com a famosa frase: “A púrpura é uma bela mortalha”. Ela promoveu leis em favor dos direitos das mulheres, proibiu o tráfico sexual forçado, fortaleceu os direitos matrimoniais e fundou lares para mulheres resgatadas da prostituição — algo sem precedentes em sua época. Graças às suas ações, a situação das mulheres no Império Bizantino era mais avançada do que em grande parte da Europa.\n\nNa história argentina, o paralelo imediato é com Eva Perón.\n\nBenjamin-Constant buscava criar um ícone visual de poder feminino e exotismo oriental. O resultado, talvez não intencional, é também um símbolo da modernidade: uma mulher jovem, poderosa, bonita e inteligente que prende o olhar do espectador e ousa ser diferente.	\N	\N	\N	2026-02-07 21:12:42.227397-03	2026-02-10 16:38:22.879705-03	100	\N	\N	1	t	-90724174	0	1087636386	0	\N	0	\N	0	\N	0	t	\N	f	\N	0	\N
\.


--
-- Data for Name: institution; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.institution (id, name, namekey, shortname, title, titlekey, institutiontype_id, subtitle, subtitlekey, info, infokey, address, addresskey, moreinfo, moreinfokey, website, mapurl, email, instagram, whatsapp, phone, twitter, logo, photo, video, audio, map, created, lastmodified, lastmodifieduser, state, language, draft, masterlanguage, translation, usethumbnail, intro, spec, opens, audioautogenerate, speechaudio, zoneid, languages) FROM stdin;
135	Museo Nacional de Bellas Artes	museo-nacional-de-bellas-artes	MNBA	\N	\N	\N	Posee la mayor colección de arte argentino y una de las más importantes de arte universal en toda Latinoamérica.	\N	El Museo Nacional de Bellas Artes (MNBA), ubicado en la Ciudad de Buenos Aires, es una de las instituciones públicas de arte más importantes de Argentina. Alberga un patrimonio sumamente diverso, que incluye más de 12 000 piezas, entre pinturas, esculturas, dibujos, grabados, textiles y objetos. Su colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad.	\N	Av. Del Libertador 1473 \r\nCiudad Autónoma de Buenos Aires \r\nArgentina	\N	\N	\N	https://www.bellasartes.gob.ar/	https://maps.app.goo.gl/UmGLqFoGb2vkP1UV6	\N	\N	+54 (011) 5288-9900	+54 (011) 5288-9900	\N	535	556	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-08-29 08:48:11.347636-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires	\N
136	Museo Quinquela Martín	museo-quinquela-mart-n	Museo Quinquela	\N	\N	\N	En la casa donde vivió Quinquela.	\N	El Museo de Bellas Artes de La Boca de Artistas Argentinos &quot;Benito Quinquela Martín&quot; ubicado en el corazón de La Boca es una de las Instituciones que el artista boquense donó al barrio con la intención de crear un polo de desarrollo cultural, educativo y sanitario. \r\n\r\nComprometido con los procesos educativos, el Museo, promueve una concepción del arte como factor decisivo en los procesos cotidianos de construcción de identidad.	\N	Avenida Don Pedro de Mendoza 1835, \r\nCiudad Autónoma de Buenos Aires\r\nC1169	\N	\N	\N	https://buenosaires.gob.ar/educacion/gestion-cultural/museo-benito-quinquela-martin	\N	\N	\N	\N	\N	\N	\N	559	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-10-24 13:04:58.067073-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires	\N
341	Museo de Arte Latinoaméricano de Buenos Aires	museo-de-arte-latinoam-ricano-de-buenos-aires	MALBA	\N	\N	\N		\N	El MALBA es una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región. Se specializa en el arte latinoamericano desde principios del siglo XX hasta la actualidad.	\N	Av. Figueroa Alcorta 3415\r\nC1425CLA Buenos Aires, Argentina\r\n+54 11 4808 6500	\N	\N	\N	https://www.malba.org.ar/	https://maps.app.goo.gl/YYBFEzrL9WkGoQc97	\N	\N	\N	011 4808-6500	\N	538	560	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-10-25 08:10:23.44224-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires	\N
553	Museo Moderno	new	\N	new	new	\N	En el barrio de San Telmo, el museo alberga más de 7000 obras de arte argentino e internacional.	\N	En el corazón del Casco Histórico, en pleno barrio de San Telmo, el Museo Moderno pone a tu disposición una rica colección del arte argentino de los últimos años.\r\nLa propuesta del Moderno es acercarte a las artes visuales a través de sus 11.000 m2 y sus más de 7.000 obras.\r\nA su vez, el edificio es un fiel exponente de las construcciones inglesas de la era industrial del siglo XIX, con su estructura de hierro, grandes aberturas y su fachada de ladrillo a la vista. \r\nLa experiencia se complementa con el bar, que ofrece un excelente café de especialidad y un menú diseñado especialmente. No te olvides de visitar la tienda donde encontrarás todos los libros editados por el Moderno, una oportunidad para sumergirte en el universo de los artistas a través de estas publicaciones.	\N	Avenida San Juan 350	\N	\N	\N	museomoderno.org	\N	info@museomoderno.org	\N	\N	011 4361 6919	\N	\N	554	\N	\N	\N	2025-09-10 18:11:10.779338-03	2025-09-10 18:13:51.010464-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires	\N
547	Basílica Nuestra Señora del Pilar	new	Iglesia del Pilar	\N	new	\N	Se inauguró en 1732. Conserva retablos, imaginería y ornamentos originales.	\N	La iglesia de Nuestra Señora del Pilar es una basílica ubicada en el barrio de Recoleta en Buenos Aires; en su día formó parte del convento de Franciscanos recoletos. Su construcción, que concluyó en 1732, se debe al mecenas aragonés Juan de Narbona.  Desde el siglo XIX es una de las parroquias de la ciudad de Buenos Aires y el segundo templo más antiguo de la ciudad.	\N	Junín 1898 \r\nCiudad Autónoma de Buenos Aires	\N	\N	\N	https://basilicadelpilar.org/	https://maps.app.goo.gl/x1ceVM3UppVU4P4u9	\N	\N	\N	\N	\N	\N	558	\N	\N	\N	2025-08-29 16:42:37.508525-03	2025-11-23 14:08:55.926126-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires	\N
821	institucion no usada	\N	nousada	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-11-20 08:20:16.4303-03	2025-11-23 21:19:51.061528-03	100	5	es	\N	es	\N	f	\N	\N	\N	f	\N	America/Buenos_Aires	\N
561	Colección de Arte Amalia Lacroze de Fortabat	new	Museo Fortabat	new	new	\N	Posee más de 150 obras de reconocidos artistas internacionales.	\N	Esta importante colección privada de arte (inaugurada en 2008 y reabierta en 2012) posee más de 150 obras de reconocidos artistas internacionales como Rodin, Warhol, Turner, Dalí y Blanes, así como de artistas argentinos tales como Badii, Berni, Quinquela Martín, Noé, Pérez Celis, Fader, Soldi y Xul Solar, entre otros.	\N	OLGA  COSSETTINI  141	\N	\N	\N	www.coleccionfortabat.org.ar	\N	\N	\N	\N	\N	\N	563	562	\N	\N	\N	2025-09-12 14:08:34.685796-03	2025-09-12 14:13:40.801927-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires	\N
749	Museo Nacional de Arte Decorativo	\N	Museo de Arte Decorativo	\N	\N	\N	\N	\N	Colección de piezas de artes decorativas europeas y orientales, esculturas, tapices y pinturas de los siglos XVI al XIX.	\N	Av. del Libertador 1902, Ciudad de Buenos Aires.	\N	\N	\N	https://museoartedecorativo.cultura.gob.ar/	\N	decorativocontacto@gmail.com	\N	\N	+54 (011) 4806-8306	\N	751	752	\N	\N	\N	2025-10-28 14:37:01.611961-03	2025-10-28 14:49:35.311694-03	100	3	es	\N	es	\N	f	\N	\N	\N	f	\N	America/Buenos_Aires	\N
50359	Museo del Holocausto	\N	\N	\N	\N	\N	Montevideo 919, Ciudad Autónoma de Buenos Aires.	\N	Es un espacio que integra la historia del Holocausto-Shoá y sus repercusiones en la Argentina, con el objetivo de educar a las nuevas generaciones y preservar la memoria. A través de objetos, documentos y testimonios de los sobrevivientes se exhibe un recorrido que da cuenta del proceso de exterminio de seis millones de judíos a manos de la Alemania nazi.	\N	\N	\N	\N	\N	https://museodelholocausto.org.ar/	\N	\N	\N	\N	\N	\N	\N	50367	\N	\N	\N	2026-02-10 18:51:24.762525-03	2026-02-10 18:51:24.762531-03	100	3	es	\N	es	\N	t	\N	\N	\N	t	\N	\N	{"en": "en", "es": "es", "pt-BR": "pt-BR"}
42202	Museo de Arte Contemporáneo Atchugarry	\N	MACA	\N	\N	\N	\N	\N	El Museo de Arte Contemporáneo Atchugarry (MACA) es un museo privado de acceso gratuito ubicado en Maldonado, Uruguay.\r\nFue impulsado por el escultor uruguayo Pablo Atchugarry y llevado adelante por la fundación que también lleva su nombre. \r\nEl edificio fue obra del arquitecto Carlos Ott. Para la construcción del mismo se utilizó la especie de eucaliptus red grandis, consta de una amplia superficie y cuenta con cuatro salas de exposiciones, una sala multifuncional y una sala de cine.	\N	Manantiales, Maldonado\r\nRuta 104 kilómetro 4	\N	\N	\N	https://macamuseo.org/museo	\N	\N	\N	\N	\N	\N	\N	42257	\N	\N	\N	2026-01-21 04:29:06.368665-03	2026-01-21 04:29:06.368691-03	100	3	es	\N	es	\N	t	\N	\N	\N	t	\N	\N	\N
855	Museo Latinoamericano de Arte Moderno	\N	MAM	\N	\N	\N	\N	\N	En el Museo de Arte Moderno  celebramos la creatividad, la apertura, la tolerancia y la generosidad. Nuestro objetivo es ser espacios inclusivos, tanto presenciales como virtuales, donde se aceptan diversas posturas culturales, artísticas, sociales y políticas. Nos comprometemos a compartir el arte moderno y contemporáneo más sugerente, y esperamos que nos acompañen a explorar el arte, las ideas y los problemas de nuestro tiempo.	\N	11 West 53 Street, Manhattan	\N	\N	\N	https://www.moma.org	\N	\N	\N	\N	\N	\N	\N	859	\N	\N	\N	2025-11-28 10:29:38.266203-03	2025-11-28 10:43:15.609876-03	100	3	es	\N	es	\N	t	\N	\N	\N	f	\N	America/Buenos_Aires	\N
529	Cabildo de Buenos Aires	new		\N	\N	\N	La sede de la administración colonial ocupa el mismo lugar desde 1580 y es un emblema de la historia viva. Visitá el corazón de la Revolución de Mayo.	\N	En este edificio funcionó el cabildo colonial fundado por Juan de Garay en 1580 durante la segunda fundación de la ciudad de Buenos Aires y que luego de la Revolución de Mayo de 1810, que derrocó al virrey español Baltasar Hidalgo de Cisneros y derivó en la guerra que llevó a la independencia de las Provincias Unidas del Río de la Plata, se transformó en una Junta de Gobierno que funcionó hasta su disolución en 1821 por el gobernador de Buenos Aires Martín Rodríguez.	\N	Bolívar 65\r\nCiudad Autónoma de Buenos Aires	\N	\N	\N	https://cabildonacional.cultura.gob.ar/	https://maps.app.goo.gl/eooviVBRmuy38Zcc7	cabildonacional@gmail.com	\N	\N	(011) 15 22642778\r\n(011) 4342-6729 / 4334-1782	\N	536	557	\N	\N	\N	2025-08-26 17:08:31.243467-03	2026-02-04 17:16:47.345066-03	100	3	es	\N	es	0	t	\N	\N	\N	t	\N	America/Buenos_Aires	\N
50384	Casa de Victoria Ocampo	\N	\N	\N	\N	\N	\N	\N	El proyecto de la casa es la culminación de un proceso de búsqueda de Victoria Ocampo respecto de la arquitectura y del diseño moderno, que se nutre de la vanguardia europea, y que surge después de haber construido una primera casa moderna en la Ciudad de Mar del Plata, Provincia de Buenos Aires y de encargar a Charles Édouard Jeanneret-Gris, más conocido como Le Corbusier, un diseño que nunca se construyó.\r\n\r\nEl proyecto definitivo lo realizó el arquitecto Alejandro Bustillo. \r\n\r\nSe trata de una vivienda de arquitectura moderna inspirada en múltiples referencias a la arquitectura de vanguardia europea, en especial francesa, con exteriores que revelan influencias del pintoresquismo e interiores organizados a través de múltiples ejes interconectados y de simetrías parciales derivadas del clasicismo francés del siglo XVIII.\r\n\r\nEn el año 1931, en sus inmediaciones, tuvo lugar la fundación de la mítica revista literaria “Sur”, una de las más importantes de su época.\r\nEn el año 1940, tras la muerte de su padre, Victoria resolvió vender la casa y mudarse a Villa Ocampo.	\N	Rufino de Elizalde 2831, CABA.	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	50392	\N	\N	\N	2026-02-11 09:06:17.61129-03	2026-02-11 09:54:12.633197-03	100	3	es	\N	es	\N	t	\N	\N	\N	t	\N	\N	{"en": "en", "es": "es", "pt-BR": "pt-BR"}
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
50360	50359	es	new	\N	\N	\N	\N	\N	2026-02-10 18:51:24.766604-03	2026-02-10 18:51:24.766608-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50361	50359	en	new	\N	\N	\N	\N	\N	2026-02-10 18:51:24.769103-03	2026-02-10 18:51:24.769105-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50362	50359	pt-BR	new	\N	\N	\N	\N	\N	2026-02-10 18:51:24.769991-03	2026-02-10 18:51:24.769992-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50363	50359	it	new	\N	\N	\N	\N	\N	2026-02-10 18:51:24.770962-03	2026-02-10 18:51:24.770963-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
941	529	en	Cabildo de Buenos Aires	\N	\N	\N	\N	\N	2025-12-05 20:41:43.52772-03	2025-12-05 20:41:43.527728-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
823	821	en	not used	\N	\N	\N	\N	\N	2025-11-20 08:20:16.437676-03	2025-11-20 08:20:16.437681-03	100	\N	\N	\N	\N	f	0	0	0	0	\N	0	\N	0	\N	0	f	f
42203	42202	es	new	\N	\N	\N	\N	\N	2026-01-21 04:29:06.385523-03	2026-01-21 04:29:06.385534-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
42204	42202	en	new	\N	\N	\N	\N	\N	2026-01-21 04:29:06.387517-03	2026-01-21 04:29:06.387524-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
42205	42202	pt-BR	new	\N	\N	\N	\N	\N	2026-01-21 04:29:06.388784-03	2026-01-21 04:29:06.388794-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50364	50359	fr	new	\N	\N	\N	\N	\N	2026-02-10 18:51:24.772096-03	2026-02-10 18:51:24.772097-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50365	50359	ger	new	\N	\N	\N	\N	\N	2026-02-10 18:51:24.773513-03	2026-02-10 18:51:24.773514-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50385	50384	es	new	\N	\N	\N	\N	\N	2026-02-11 09:06:17.632648-03	2026-02-11 09:06:17.632665-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50386	50384	en	new	\N	\N	\N	\N	\N	2026-02-11 09:06:17.634695-03	2026-02-11 09:06:17.634709-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50387	50384	pt-BR	new	\N	\N	\N	\N	\N	2026-02-11 09:06:17.635688-03	2026-02-11 09:06:17.635692-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50388	50384	it	new	\N	\N	\N	\N	\N	2026-02-11 09:06:17.636614-03	2026-02-11 09:06:17.63662-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50389	50384	fr	new	\N	\N	\N	\N	\N	2026-02-11 09:06:17.637974-03	2026-02-11 09:06:17.637982-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
50390	50384	ger	new	\N	\N	\N	\N	\N	2026-02-11 09:06:17.638902-03	2026-02-11 09:06:17.638906-03	100	\N	\N	\N	\N	t	0	0	0	0	\N	0	\N	0	\N	0	t	f
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
-- Data for Name: music; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.music (id, name, url, state, info, composer, composerstr, performerstr, genrestr, draft, audio, created, lastmodified, lastmodifieduser, info_json, license, technical_info, royaltyfree) FROM stdin;
50652	Mozart Piano Concerto No. 15 in B flat major K450 II. Andante	https://commons.wikimedia.org/wiki/File:Mozart_Piano_Concerto_No._15_in_B_flat_major_K450_II._Andante_(Mozart)_European_Archive.ogg	3	Mozart Piano Concerto No. 15 in B flat major K450 II. Andante (Mozart) European Archive	\N	\N	\N	\N	\N	50653	2026-02-15 15:16:32.849542-03	2026-02-15 15:16:32.849558-03	100	\N	This sound recording is not protected because it was published in the European Union more than 50 (Directive 2006/116/EC) or 70 years ago (Directive 2011/77/EU) and the rights of its performers and producers have expired.	\N	t
50697	Vivaldi - The Four Seasons - Summer Mvt 2: Adagio	http://JohnHarrisonViolin.com	3	artist. John Harrison - Violin\r\nyear. February 6, 2000\r\nalbum. Vivaldi: The Four Seasons\r\n\r\nJohn Harrison, Violin\r\nRobert Turizziani, Conductor\r\nWichita State University Chamber Players	\N	\N	\N	\N	\N	50698	2026-02-16 12:51:16.46547-03	2026-02-16 12:51:16.465503-03	100	\N	This file is licensed under Creative Commons \r\nAttribution ShareAlike 1.0 License:\r\nhttp://creativecommons.org/licenses/by-sa/1.0/	\N	t
50521	Donizetti - Una Furtiva Lagrima	\N	3	artist: Andrea Bocelli\r\nyear: 2003\r\nalbum: Viaggio Italiano\r\ngenre: Crossover\r\ntitle: Una Furtiva Lagrima (Gaetano Donizetti)\r\ntrack: 5	\N	\N	\N	\N	\N	50522	2026-02-12 13:24:33.901923-03	2026-02-12 13:24:33.901957-03	100	\N	\N	\N	f
50699	Vivaldi - Winter Mvt 4: Allegro non molto	https://commons.wikimedia.org/wiki/File:Vivaldi_-_Four_Seasons_4_Winter_mvt_1_Allegro_non_molto_-_John_Harrison_violin.oga	3	artist. John Harrison - Violin\r\nyear. February 6, 2000\r\nalbum. Vivaldi: The Four Seasons	\N	\N	\N	\N	\N	50700	2026-02-16 12:53:33.505817-03	2026-02-16 12:53:33.505824-03	100	\N	This file is licensed under Creative Commons\r\nAttribution ShareAlike 1.0 License:\r\nhttp://creativecommons.org/licenses/by-sa/1.0/	\N	t
50509	Hayden - concerto No 1 moderato	\N	3	album: Haydn Cello Concertos 1 & 2\r\ntrack: 1	\N	\N	\N	\N	\N	50510	2026-02-12 11:10:30.665514-03	2026-02-12 11:10:30.66552-03	100	\N	\N	\N	f
50511	Haydn - Concerto No 1 Adagio	\N	3	album: Haydn Cello Concertos 1 & 2\r\ntrack: 2	\N	\N	\N	\N	\N	50512	2026-02-12 11:11:24.096782-03	2026-02-12 11:11:24.096786-03	100	\N	\N	\N	f
50502	Pachebel - canon et gigue en re mineur pour 3 violons	\N	3	comment: https://archive.org/details/dm-2530-247	\N	\N	\N	\N	\N	50503	2026-02-12 11:01:54.121097-03	2026-02-12 11:01:54.121104-03	100	\N	\N	\N	f
50515	Vivaldi - concerto no 4 in f minor _linverno ii. largo	\N	3	\N	\N	\N	\N	\N	\N	50516	2026-02-12 12:03:55.424942-03	2026-02-12 12:03:55.424949-03	100	\N	\N	\N	f
50658	Beethoven - Sonata Op. 31 No. 2 (The Tempest) - Second Movement	https://commons.wikimedia.org/wiki/File:Ludwig_van_Beethoven_-_Sonata_Op._31_No._2_(The_Tempest)_-_Second_Movement.ogg	3	Interpreter: Stefano Ligoratti\r\nDate\t21 July 2010	\N	\N	\N	\N	\N	50659	2026-02-15 15:27:35.402628-03	2026-02-15 15:27:35.402636-03	100	\N	Creative Commons Attribution-Share Alike 4.0 International license.	\N	t
50660	Beethoven - Sonata No. 8 in C Minor "Pathétique", Op. 13 - III. Rondo - Allegro	https://commons.wikimedia.org/wiki/File:Beethoven,_Sonata_No._8_in_C_Minor_Pathetique,_Op._13_-_III._Rondo_-_Allegro.ogg	3	artist. Paul Pitman\r\nalbum. Beethoven - Complete Piano Sonatas Vol. I\r\ncomposer. Ludwig Van Beethoven\r\nSonata No. 8 in C Minor Pathetique, Op. 13 - III. Rondo - allegro	\N	\N	\N	\N	\N	50661	2026-02-15 15:31:01.33678-03	2026-02-15 15:31:01.336786-03	100	\N	Creative Commons CC0 1.0 Universal Public Domain Dedication.	\N	t
50656	Beethoven - Violin Sonata No.5, Op.24	https://commons.wikimedia.org/wiki/File:Violin_Sonata_No.5,_Op.24_(Beethoven)_2.ogg	3	Violin Sonata No. 5, op. 24 — 2nd movement\r\nWolfgang Schneiderhan, Wilhelm Kempff	\N	\N	\N	\N	\N	50657	2026-02-15 15:25:02.214965-03	2026-02-15 15:25:02.214975-03	100	\N	This sound recording is not protected because it was published in the European Union more than 50 (Directive 2006/116/EC) or 70 years ago (Directive 2011/77/EU) and the rights of its performers and producers have expired.	\N	t
50662	Beethoven - Sonata No. 8 in C Minor "Pathétique", Op. 13 - I. Grave - Allegro di molto e con brio	https://commons.wikimedia.org/wiki/File:Beethoven,_Sonata_No._8_in_C_Minor_Pathetique,_Op._13_-_I._Grave_-_Allegro_di_molto_e_con_brio.ogg	3	artist. Paul Pitman\r\nalbum. Beethoven - Complete Piano Sonatas Vol. I\r\ncomposer. Ludwig Van Beethoven	\N	\N	\N	\N	\N	50663	2026-02-15 15:34:30.908386-03	2026-02-15 15:34:30.908392-03	100	\N	Creative Commons CC0 1.0 Universal Public Domain Dedication.	\N	t
50654	Beethoven - VIOLIN SONATA #2, 3rd mvt.	https://commons.wikimedia.org/wiki/File:Violinist_CARRIE_REHKOPF-BEETHOVEN_VIOLIN_SONATA_2_3rd_mvt.ogg	3	Violin Sonata No. 2, 3rd movement (allegro piacevole).\r\nartist. Violinist  CARRIE REHKOPF	\N	\N	\N	\N	\N	50655	2026-02-15 15:22:34.27451-03	2026-02-15 15:22:34.274518-03	100	\N	Creative Commons Attribution-Share Alike 3.0 Unported license.	\N	t
50540	Mozart - piano concerto no. 23 in a major, k.488 - 3. allegro assai.	\N	3	Publication date 1786 Topics Mozart Piano Concerto No. 23 in A major, K.488 Item Size 61.5M\r\nMozart Piano Concerto No. 23 in A major, K.488\r\nAddeddate 2021-08-25 07:02:01 Identifier 01-andras-schiff-sandor-vegh-camerata-academica-des-mozarteums-salzburg-mozart-p	\N	\N	\N	\N	\N	50541	2026-02-12 14:46:39.564764-03	2026-02-12 14:46:39.564769-03	100	\N	\N	\N	f
50709	Tchaikovsky - Swan Lake Op.20 - Act II Pt.1	https://commons.wikimedia.org/wiki/File:Tchaikovsky_-_Swan_Lake_Op.20_-_Act_II_Pt.1.ogg	3	Tchaikovsky - Swan Lake Op.20 - Act II Pt.1\r\n Unknown date	\N	\N	\N	\N	\N	50710	2026-02-16 18:07:56.768734-03	2026-02-16 18:07:56.768753-03	100	\N	Public domain\t\r\nThis work is in the public domain in its country of origin and other countries and areas where the copyright term is the author's life plus 70 years or fewer.	\N	t
50589	new	\N	5	\N	\N	\N	\N	\N	\N	\N	2026-02-13 00:50:53.486177-03	2026-02-13 00:50:53.48619-03	100	\N	\N	\N	f
50632	Caruso - George Frideric Handel, Ombra mai fu	https://commons.wikimedia.org/wiki/File:Enrico_Caruso,_George_Frideric_Handel,_Ombra_mai_fu_(Serse).ogg	3	"Ombra mai fu" from Handel's 1738 opera Serse (a.k.a. Xerxes), sung by Enrico Caruso. Recorded 29 Jan 1920, Matrix: C-23714, Victor Cat: 88617\r\n\r\ndurationSeconds: 256\r\nformat: Ogg\r\naudioFormat: audio/vorbis\r\nbitrate: 160\r\nsampleRate: 44100	\N	\N	\N	\N	\N	50633	2026-02-15 09:59:22.301103-03	2026-02-15 09:59:22.301117-03	100	\N	Public Domain	\N	t
50628	Gounod -  JOHN MICHEL CELLO-BACH AVE MARIA	\N	3	duration: 301.17\r\naudioFormat: audio/vorbis\r\n\r\nAve Maria composed by Charles Gounod. \r\nAve Maria was composed based off of the harmony and texture of Johann Sebastian Bach's Prelude No.1 in C Major from Well-Tempered Clavier Book I (BWV 846).\r\nOriginally composed in 1859\r\nComposer: Charles Gounod (1818–1893)\r\nContemporary Performer: John Michel, a cello professor at Central Washington University	\N	\N	\N	\N	\N	50629	2026-02-15 09:49:18.702904-03	2026-02-15 09:49:18.702912-03	100	\N	Public Domain	https://commons.wikimedia.org/wiki/File:JOHN_MICHEL_CELLO-BACH_AVE_MARIA.ogg	t
50664	Beethoven - sonata 'waldstein', op. 53 - ii. introduzione- adagio molto	https://commons.wikimedia.org/wiki/File:Ludwig_van_Beethoven_-_sonata_%27waldstein%27,_op._53_-_ii._introduzione-_adagio_molto.ogg	3	Sonata 'Waldstein', op. 53 - ii. introduzione - adagio molto	\N	\N	\N	\N	\N	50665	2026-02-15 15:37:51.646649-03	2026-02-15 15:37:51.646652-03	100	\N	This work has been released into the public domain by its author, Musopen. This applies worldwide.	\N	t
50666	Pachelbel - Canon in D Major. Bass continuo with three violins. Harp duet doubling the parts.	https://commons.wikimedia.org/wiki/File:Canon_in_D_Major_(ISRC_USUAN1100301).mp3	3	Canon in D Major\r\nPachelbel's Canon in D Major. Bass continuo with three violins. Harp duet doubling the parts.\r\nDate\t1 January 2006	\N	\N	\N	\N	\N	50667	2026-02-15 15:41:48.560253-03	2026-02-15 15:41:48.560258-03	100	\N	Creative Commons Attribution 3.0 Unported license.	\N	t
50636	Caruso - l'elisir d'amore - una furtiva lagrima	https://commons.wikimedia.org/wiki/File:Enrico_Caruso,_L%27elisir_d%27amore,_Una_furtiva_lagrima.ogg	3	"Una furtiva lagrima" from Gaetano Donizetti's 1832 opera L'elisir d'amore, sung by Enrico Caruso on 26 November 1911 for the Victor Talking Machine Company in Camden, New Jersey\r\ndurationSeconds: 261\r\nformat: Ogg\r\naudioFormat: audio/vorbis\r\nbitrate: 160\r\nsampleRate: 44100	\N	\N	\N	\N	\N	50637	2026-02-15 10:04:39.547264-03	2026-02-15 10:04:39.547286-03	100	\N	public domain	\N	t
50711	Tchaikovsky - Swan Lake Op.20 - Act I Intro	https://commons.wikimedia.org/wiki/File:Tchaikovsky_-_Swan_Lake_Op.20_-_Act_I_Intro.ogg	3	English: Tchaikovsky - Swan Lake Op.20 - Act I Intro\r\nDate\tUnknown date	\N	\N	\N	\N	\N	50712	2026-02-16 18:11:25.486478-03	2026-02-16 18:11:25.486505-03	100	\N	Public domain\t\r\nThis work is in the public domain in its country of origin and other countries and areas where the copyright term is the author's life plus 70 years or fewer.	durationSeconds. 1130\r\nformat. Ogg\r\naudioFormat. audio/vorbis\r\nbitrate. 121	t
50634	Caruso - Ave Maria	https://archive.org/download/Caruso_part1/Caruso-AveMaria.mp3	3	1913 recording by Enrico Caruso. The music by Percy Kahn was first published in USA that same year.\r\ndurationSeconds: 250\r\nformat: Ogg\r\naudioFormat: audio/vorbis\r\nbitrate: 128\r\nsampleRate: 44100	\N	\N	\N	\N	\N	50635	2026-02-15 10:01:47.305239-03	2026-02-15 10:01:47.305257-03	100	\N	Public domain	\N	t
50630	Bach, Johann Sebastian - Suite No.2 in B Minor - X. Badinerie	https://commons.wikimedia.org/wiki/File:Bach,_Johann_Sebastian_-_Suite_No.2_in_B_Minor_-_X._Badinerie.ogg	3	BWV 1067 - Bach, Johann Sebastian - Suite No.2 in B Minor - X. Badinerie\r\n\r\ndurationSeconds: 83\r\nformat: Ogg\r\naudioFormat: audio/vorbis\r\nbitrate: 125\r\nsampleRate: 44100	\N	\N	\N	\N	\N	50631	2026-02-15 09:55:50.204495-03	2026-02-15 09:55:50.204525-03	100	\N	Creative Commons CC0 1.0 Universal Public Domain Dedication.	\N	t
50713	Tchaikovsky - Swan Lake Op.20 - Act IV Intro	https://commons.wikimedia.org/wiki/File:Tchaikovsky_-_Swan_Lake_Op.20_-_Act_IV_Intro.ogg	3	Tchaikovsky - Swan Lake Op.20 - Act IV Intro\r\nDate\tUnknown date	\N	\N	\N	\N	\N	50714	2026-02-16 18:17:09.538806-03	2026-02-16 18:18:23.573643-03	100	\N	Public domain\t\r\nThis work is in the public domain in its country of origin and other countries and areas where the copyright term is the author's life plus 70 years or fewer.	durationSeconds. 1262\r\nformat. Ogg\r\naudioFormat. audio/vorbis\r\nbitrate. 123	t
50501	Albinoni - adagio en sol mineur	https://archive.org/details/dm-2530-247	3	Albinoni • Pachebel • Boccherini • Respighi (Deutsche Grammophon)\r\nTopics Tomaso Abinoni, Johann Pachelbel, Luigi Boccherini, Ottorino Respighi, Herbert von Karajan\r\ndurationSeconds: 605\r\nformat: Mp3\r\naudioFormat: audio/mpeg\r\nbitrate: ~164	\N	\N	\N	\N	\N	50504	2026-02-12 11:00:56.15932-03	2026-02-12 11:00:56.159325-03	100	\N	\N	\N	f
50497	Respighi -  Danses antiques, 3e suite pour orchestre à cordes	\N	3	comment: https://archive.org/details/dm-2530-247	\N	\N	\N	\N	\N	50498	2026-02-12 10:59:02.014691-03	2026-02-12 10:59:02.014708-03	100	\N	\N	\N	f
50519	Vivaldi - Concerto in G Major (P. 135) for 2 violins & 2 cellos: II. Largo	\N	3	artist: Huguette Fernandez and Ginette Cales, Violins / Bernard Fonteny and Constance Maurelet, Cellos\r\nyear: 1965\r\nalbum: VIVALDI: Concerti\r\ncomposer: Vivaldi\r\ngenre: Classical\r\nalbumArtist: Jean-Francois Paillard / Paillard Chamber Orchestra\r\ncomment: Anne-Marie Beckensteiner, Harpsichord\r\n\r\nMHS 659 (stereo) (Erato)\r\nRecorded ca. 1965\r\nDigital transfer by F. Reeder, June, 2016\r\ntitle: Concerto in G Major (P. 135) for 2 violins & 2 cellos: II. Largo\r\ntrack: 11	\N	\N	\N	\N	\N	50520	2026-02-12 12:13:44.527986-03	2026-02-12 12:13:44.527992-03	100	\N	\N	\N	f
50507	Mozart - piano concerto no. 26 in d major, k. 537 _coronation_ - 2. (larghetto)	\N	3	\N	\N	\N	\N	\N	\N	50508	2026-02-12 11:09:22.586321-03	2026-02-12 11:09:22.586326-03	100	\N	\N	\N	f
50505	Mozart - piano concerto no. 26 in d major, k. 537	\N	3	\N	\N	\N	\N	\N	\N	50506	2026-02-12 11:07:21.399714-03	2026-02-12 11:07:21.39972-03	100	\N	\N	\N	f
50517	Vivaldi - concerto-no-4infminor_inverno_iiii-allegro	\N	3	\N	\N	\N	\N	\N	\N	50518	2026-02-12 12:07:23.327245-03	2026-02-12 12:07:23.327252-03	100	\N	\N	\N	f
50513	Vivaldi -  concerto no. 4 in f minor _linverno__ i. allegro non moltono. 4 in f minor _linverno__ i. allegro	\N	3	\N	\N	\N	\N	\N	\N	50514	2026-02-12 12:02:12.491963-03	2026-02-12 12:02:12.491981-03	100	\N	\N	\N	f
50523	Shostakovich - Piano Concerto No. 2 mov II	\N	3	Piano Concerto No. 2\r\nby Dimitri Shostakovich\r\n\r\nTopics Shostakovich, Bernstein, piano, concerto, LP Item Size 45.8M\r\nPiano Concerto No. 2, Op. 101 by Dimitri Shostakovich (1906-1975). The New York Philharmonic conducted by Leonard Bernstein (1918-1990) with the conductor at the piano. In three movements, with the second and third played without interruption. Bernstein was an accomplished pianist and could have chosen to pursue a concert and recording career based on that talent alone. Recorded in 1959. Transfer from the original Columbia stereo LP ML 5337 by Bob Varney.\r\nAddeddate 2020-10-27 03:38:53 Collection_added hifidelity Identifier piano-concerto-no.-2	\N	\N	\N	\N	\N	50524	2026-02-12 13:40:01.716117-03	2026-02-12 13:40:01.716133-03	100	\N	\N	\N	f
50538	Mozart - piano concerto no. 23 in a major, k.488 - 2. adagio	\N	3	Publication date 1786 Topics Mozart Piano Concerto No. 23 in A major, K.488 Item Size 61.5M\r\nMozart Piano Concerto No. 23 in A major, K.488\r\nAddeddate 2021-08-25 07:02:01 Identifier 01-andras-schiff-sandor-vegh-camerata-academica-des-mozarteums-salzburg-mozart-p	\N	\N	\N	\N	\N	50539	2026-02-12 14:45:43.516429-03	2026-02-12 14:45:43.516434-03	100	\N	\N	\N	f
50494	Beethoven - Piano Sonata #14 In C Sharp Minor, Op. 27/2, "Moonlight" - 1. Adagio Sostenuto	\N	3	disc_no: 1\r\nartist: Arthur Rubinstein\r\nyear: 1962\r\nalbum: Beethoven: Piano Sonatas #8, 13, 23 & 26\r\ncomposer: Ludwig Van Beethoven\r\ngenre: Classical\r\ncomment: 0\r\ntitle: Beethoven: Piano Sonata #14 In C Sharp Minor, Op. 27/2, "Moonlight" - 1. Adagio Sostenuto\r\ntrack: 4	\N	\N	\N	\N	\N	50495	2026-02-12 09:08:27.315888-03	2026-02-12 09:08:27.315905-03	100	\N	\N	\N	f
50554	Beethoven - Concerto No 5 In E Flat , Op. 73 ("Emperor"): II. Adagio Un Poco Mosso;  III. Rondo	\N	3	artist: Ludwig van Beethoven; Jascha Heifetz; Arthur Rubinstein; Boston Symphony Orchestra; Charles Munch; Erich Leinsdorf\r\nalbum: Favorite Beethoven Concertos: Violin Concerto & "Emperor" Concerto\r\ncomment: https://archive.org/details/lp_favorite-beethoven-concertos-violin-concer_ludwig-van-beethoven-jascha-heifetz-arthur\r\ntitle: Concerto No 5 In E Flat , Op. 73 ("Emperor"): II. Adagio Un Poco Mosso;  III. Rondo\r\ntrack: 1	\N	\N	\N	\N	\N	50555	2026-02-12 15:20:07.491149-03	2026-02-12 15:20:07.491155-03	100	\N	\N	\N	f
50606	Albinoni - Adagio in g minor - arr for alto saxophone and piano	https://upload.wikimedia.org/wikipedia/commons/e/e1/Tomaso_Giovanni_Albinoni_-_Adagio_in_G_minor_-_Arr_for_alto_saxophone_and_piano_-_David_Hernando_Vitores.ogg	3	duration: 214.36\r\naudioFormat: audio/vorbis\r\n\r\nAdagio in G minor - Arr for alto saxophone and piano.	\N	\N	\N	\N	\N	50607	2026-02-13 11:00:50.164961-03	2026-02-13 11:00:50.164974-03	100	\N	Tomaso Giovanni Albinoni, CC BY-SA 4.0 \r\nWikimedia Commons	\N	t
50499	Boccherini - quintettino	\N	3	duration: 752.7578735351562\r\ndurationSeconds: 753\r\nformat: Mp3\r\naudioFormat: audio/mpeg\r\nbitrate: ~162\r\ncomment: https://archive.org/details/dm-2530-247\r\nsampleRate: 44100	\N	\N	\N	\N	\N	50500	2026-02-12 11:00:09.652112-03	2026-02-12 11:00:09.652132-03	100	\N	\N	\N	f
50527	Chopin -  Nocturne No. 1: Andante cantabile - Baremboim	\N	3	artist: Daniel Barenboim\r\nyear: 1982-01-01\r\nalbum: Chopin - The Complete Nocturnes (CD 1: nos. 1-11)\r\ngenre: Classical\r\ncomment: https://archive.org/details/chopin-nocturnes\r\ntitle: fa mag  op. 15 No. 1: Andante cantabile\r\ntrack: 04	\N	\N	\N	\N	\N	50528	2026-02-12 14:35:15.377045-03	2026-02-12 14:35:15.377051-03	100	\N	\N	\N	f
50531	Chopin -  Nocturne op. 9 No. 2: Andante - Barenboim	\N	3	artist: Daniel Barenboim\r\nyear: 1982-01-01\r\nalbum: Chopin - The Complete Nocturnes (CD 1: nos. 1-11)\r\ngenre: Classical\r\ncomment: https://archive.org/details/chopin-nocturnes\r\ntitle: mi bem mag op. 9 No. 2: Andante\r\ntrack: 02	\N	\N	\N	\N	\N	50532	2026-02-12 14:39:37.100983-03	2026-02-12 14:39:37.100992-03	100	\N	\N	\N	f
50529	Chopin -  Nocturne si bem minl op. 9 No. 1: Larghetto Daniel Barenboim	https://archive.org/details/chopin-nocturnes	3	artist: Daniel Barenboim\r\nyear: 1982-01-01\r\nalbum: Chopin - The Complete Nocturnes (CD 1: nos. 1-11)\r\ngenre: Classical\r\ncomment: https://archive.org/details/chopin-nocturnes\r\ntitle: si bem minl op. 9 No. 1: Larghetto\r\ntrack: 01	\N	\N	\N	\N	\N	50530	2026-02-12 14:38:46.141779-03	2026-02-12 14:38:46.141783-03	100	\N	\N	\N	f
50533	Chopin - Nocturne sol min op. 15 No. 3: Lento - Baremboim	\N	3	artist: Daniel Barenboim\r\nyear: 1982-01-01\r\nalbum: Chopin - The Complete Nocturnes (CD 1: nos. 1-11)\r\ngenre: Classical\r\ncomment: https://archive.org/details/chopin-nocturnes\r\ntitle: sol min op. 15 No. 3: Lento\r\ntrack: 06	\N	\N	\N	\N	\N	50534	2026-02-12 14:40:22.827684-03	2026-02-12 14:40:22.827693-03	100	\N	\N	\N	f
50535	Mozart - piano concerto no. 23 in a major, k.488 - 1. allegro	\N	3	Mozart Piano Concerto No. 23 in A major, K.488\r\nby Mozart\r\n\r\nPublication date 1786 Topics Mozart Piano Concerto No. 23 in A major, K.488 Item Size 61.5M\r\nMozart Piano Concerto No. 23 in A major, K.488\r\nAdded date 2021-08-25 07:02:01 Identifier 01-andras-schiff-sandor-vegh-camerata-academica-des-mozarteums-salzburg-mozart	\N	\N	\N	\N	\N	50536	2026-02-12 14:44:05.067711-03	2026-02-12 14:44:05.067716-03	100	\N	\N	\N	f
50525	Toquinho - Aquarela	\N	3	disc_no: 2\r\nartist: Toquinho\r\nyear: 1983\r\nalbum: Aquarela\r\ngenre: MPB\r\nalbumArtist: Toquinho\r\ncomment: https://archive.org/details/1983-toquinho-aquarela\r\ntitle: Aquarela\r\ntrack: 7	\N	\N	\N	\N	\N	50526	2026-02-12 13:45:29.2387-03	2026-02-12 13:45:29.238711-03	100	\N	\N	\N	f
50675	Bach - Goldberg Variations, BWV. 988	https://commons.wikimedia.org/wiki/File:Bach,_J_S_-_Goldberg_Variations,_BWV988_-_02._Variation_1_(Shelley_Katz).flac	3	artist. Shelley Katz\r\nyear. 2012\r\nalbum. Musopen Kickstarter Project\r\ncomposer. Johann Sebastian Bach	\N	\N	\N	\N	\N	50676	2026-02-15 22:14:53.455363-03	2026-02-15 22:14:53.45539-03	100	\N	This work has been released into the public domain by its author, Musopen. This applies worldwide.	\N	t
50590	Atahualpa Yupanqui - Piedra Y Camino	https://archive.org/details/atahualpa-yupanqui-camino-del-indio	3	Atahualpa Yupanqui – Camino Del Indio\r\nby Atahualpa Yupanqui\r\n\r\nPublication date 1957 Topics Folk, World, Folk Music, Argentina, Argentinean Music Language Spanish Item Size 35.1M\r\nAtahualpa Yupanqui – Camino Del Indio	\N	\N	\N	\N	\N	50591	2026-02-13 00:51:47.512575-03	2026-02-13 00:51:47.5126-03	100	\N	RCA Victor – AVL-3086 | Vinyl, LP, Compilation, Mono | Argentina | 1957	\N	f
50542	Bach - Brandenburg Concertos, Vol. 1 No. 1 In F Major (Bwv.1046): 1st. Mov.: Allegro	https://archive.org/details/lp_brandenburg-concertos-complete_johann-sebastian-bach-karl-mnchinger-stutt	3	Brandenburg Concertos, Vol. 1 No. 1 In F Major (Bwv.1046): 1st. Mov.: Allegro\r\nBrandenburg Concertos (Complete)\r\nby Johann Sebastian Bach; Karl Münchinger; Stuttgarter Kammerorchester\r\nLondon Records (CSA2301)\r\nPublication date 1959 \r\nTopics Classical, Baroque Contributor Internet Archive Language Italian\r\nSize 5.1G	\N	\N	\N	\N	\N	50543	2026-02-12 14:49:17.360793-03	2026-02-12 14:49:17.360805-03	100	\N	\N	\N	f
50546	Haydn Cello Concertos - concerto no. 1 - adagio	\N	3	album: Haydn Cello Concertos 1 & 2\r\ntrack: 2	\N	\N	\N	\N	\N	50547	2026-02-12 14:51:28.105345-03	2026-02-12 14:51:28.10535-03	100	\N	\N	\N	f
50548	Bach - sonata no. 1 - 3rd - andante	\N	3	artist: Bach Mehuhin One\r\nalbumArtist: Bach Mehuhin One\r\ntrack: 3\r\n\r\nSix Sonatas for Violin and Harpsichord\r\nby Johann Sebastian Bach\r\n\r\nTopics Bach, Menuhin, Malcolm, Gauntlett, violin, harpsichord, LP Item Size 245.1M\r\nSix Sonatas for Violin and Harpsichord by Johann Sebastian Bach (1685-1750). Yehudi Menuhin (1916-1999), violin; George Malcolm (1917-1997), harpsichord and Ambrose Gauntlett (1889-1978), viola da gamba. Recorded in England in 1962. Transfer from the original mono Angel double LP 3629 B by Bob Varney.	\N	\N	\N	\N	\N	50549	2026-02-12 14:54:14.522001-03	2026-02-12 14:54:14.522006-03	100	\N	\N	\N	f
50550	Bach - sonata no. 6 - 2nd - largo	\N	3	artist: Bach Mehuhin One\r\nalbumArtist: Bach Mehuhin One\r\ntrack: 10\r\nSix Sonatas for Violin and Harpsichord\r\nby Johann Sebastian Bach\r\n\r\nTopics Bach, Menuhin, Malcolm, Gauntlett, violin, harpsichord, LP Item Size 245.1M\r\nSix Sonatas for Violin and Harpsichord by Johann Sebastian Bach (1685-1750). Yehudi Menuhin (1916-1999), violin; George Malcolm (1917-1997), harpsichord and Ambrose Gauntlett (1889-1978), viola da gamba. Recorded in England in 1962. Transfer from the original mono Angel double LP 3629 B by Bob Varney.	\N	\N	\N	\N	\N	50551	2026-02-12 14:58:10.095983-03	2026-02-12 14:58:10.095986-03	100	\N	\N	\N	f
50552	Haydn - Concerto In D Major, Op. 101: Adagio	\N	3	artist: Jacqueline du Pré; Joseph Haydn; Matthias Georg Monn; Sir John Barbirolli; The London Symphony Orchestra\r\nalbum: Haydn: Cello Concerto In D / Monn: Cello Concerto In G Minor\r\ncomment: https://archive.org/details/lp_haydn-cello-concerto-in-d-monn-cello-conc_jacqueline-du-pr-joseph-haydn-matthias-geo\r\ntitle: Concerto In D Major, Op. 101: Adagio\r\ntrack: 2	\N	\N	\N	\N	\N	50553	2026-02-12 15:07:47.194151-03	2026-02-12 15:07:47.194157-03	100	\N	\N	\N	f
50544	Bach - Air In D From 3rd Suite	https://archive.org/details/lp_brandenburg-concertos-complete_johann-sebastian-bach-karl-mnchinger-stutt	3	Brandenburg Concertos (Complete)\r\nby Johann Sebastian Bach; Karl Münchinger; Stuttgarter Kammerorchester\r\nLondon Records (CSA2301)\r\nAir In D From 3rd Suite\r\ntrack: 4\r\naudioFormat: audio/mpeg\r\nPublication date 1959 \r\nTopics Classical, Baroque Contributor Internet Archive Language Italian	\N	\N	\N	\N	\N	50545	2026-02-12 14:49:44.67995-03	2026-02-12 14:49:44.679955-03	100	\N	Public domain since 2029	\N	f
50587	Atahualpa Yupanqui - Vidala Del Silencio	https://archive.org/details/atahualpa-yupanqui-camino-del-indio	3	title: Vidala Del Silencio\r\nAtahualpa Yupanqui – Camino Del Indio\r\nby Atahualpa Yupanqui\r\nPublication date 1957 Topics Folk, World, Folk Music, Argentina, Argentinean Music Language Spanish Item Size 35.1M\r\nAtahualpa Yupanqui – Camino Del Indio	\N	\N	\N	\N	\N	50588	2026-02-13 00:49:32.668166-03	2026-02-13 00:49:32.668191-03	100	\N	Publication date 1957 Topics Folk, World, Folk Music, Argentina, Argentinean Music Language Spanish	\N	t
50642	Mozart - Concerto No.1 in F major II. Andante	https://commons.wikimedia.org/wiki/File:Mozart_Concerto_No.1_in_F_major_II._Andante_(Mozart)_European_Archive.ogg	3	Mozart Concerto No.1 in F major II. Andante (Mozart) European Archive\r\n\r\ndurationSeconds: 283\r\nformat: Ogg\r\naudioFormat: audio/vorbis\r\nbitrate: 217\r\nsampleRate: 44100	\N	\N	\N	\N	\N	50643	2026-02-15 13:55:01.140454-03	2026-02-15 13:55:01.140473-03	100	\N	This sound recording is not protected because it was published in the European Union more than 50 (Directive 2006/116/EC) or 70 years ago (Directive 2011/77/EU) and the rights of its performers and producers have expired.	\N	t
50646	Mozart - Piano Concerto 23 in A major, K 488, II Adagio	https://en.wikipedia.org/wiki/File:Mozart_Piano_Concerto_23_in_A_major,_K_488,_II_Adagio._MYAC_Symphony_Orchestra,_Amir_Siraj.ogg	3	MYAC Symphony Orchestra featuring Amir Siraj, piano\r\nRecorded on Sunday August 20th at Millennium Park\r\nhttp://www.chicagomusicreport.com/ for a performance review.\r\n12 September 2017\r\ndurationSeconds: 434\r\nformat: Ogg\r\naudioFormat: audio/vorbis\r\nbitrate: 192\r\nsampleRate: 44100	\N	\N	\N	\N	\N	50647	2026-02-15 14:05:48.441992-03	2026-02-15 14:05:48.441998-03	100	\N	This file is licensed under the Creative Commons Attribution 3.0 Unported license.	\N	t
50677	Bach - Goldberg Variations, BWV. 988 -var 23	https://commons.wikimedia.org/wiki/File:Bach,_J_S_-_Goldberg_Variations,_BWV988_-_24._Variation_23_(Shelley_Katz).flac	3	artist. Shelley Katz\r\nyear. 2012\r\nalbum. Musopen Kickstarter Project\r\ncomposer. Johann Sebastian Bach	\N	\N	\N	\N	\N	50678	2026-02-15 22:16:44.328398-03	2026-02-15 22:16:44.328403-03	100	\N	This work has been released into the public domain by its author, Musopen. This applies worldwide.	\N	t
50679	Bach - Prelude & Fugue in A Minor, BWV 543: Fuga	https://commons.wikimedia.org/wiki/File:Johann_Sebastian_Bach_Fugue_in_A_minor_BWV_543_Robert_K%C3%B6bler_Silbermann-Organ.mp3	3	artist. Robert Köbler\r\nyear. 2009\r\nalbum. BACH: Organ Music on Silbermann Organs, Vol. 7\r\ncomposer. Johann Sebastian Bach	\N	\N	\N	\N	\N	50680	2026-02-15 22:20:16.261183-03	2026-02-15 22:20:16.261189-03	100	\N	This file is licensed under the Creative Commons Attribution 1.0 Generic license.	\N	t
50681	Bach - Prelude (BWV 1007)	\N	3	artist. Bach, Chris\r\nyear. 2024-12-22T02:44:22-05:00	\N	\N	\N	\N	\N	50682	2026-02-15 22:23:24.290681-03	2026-02-15 22:23:24.290691-03	100	\N	PUBLIC DOMAIN	\N	t
50683	Bach - 02 Cello Suite no. 1 in G major, BWV 1007- II. Allemande	https://commons.wikimedia.org/wiki/File:Bach_-_Cello_Suite_no._1_in_G_major,_BWV_1007_-_II._Allemande.ogg	3	artist. 02 Cello Suite no. 1 in G major, BWV 1007- II. Allemande\r\nyear. 1962\r\nBach, Cello Suite no. 1 in G major, BWV 1007, II. Allemande. Interprété par Pablo Casals.\r\nDate\t1929/1939.\r\nSource\tCD audio, Naxos Historical, restoration by Ward Marston, 2000.\r\nAuthor\tBach, Johann-Sebastian.	\N	\N	\N	\N	\N	50684	2026-02-15 22:24:13.798921-03	2026-02-15 22:24:13.798927-03	100	\N	This sound recording is not protected because it was published in the European Union more than 50 (Directive 2006/116/EC) or 70 years ago (Directive 2011/77/EU) and the rights of its performers and producers have expired.	\N	t
50493	Beethoven - Piano Sonata No. 8 in C Minor, Op. 13 "Pathétique": 2. Adagio cantabile	https://archive.org/details/wilhelmkempff-beethoven-sonatasnos.7-12remastered201924-96	3	artist: Wilhelm Kempff\r\nyear: 1965\r\nalbum: Beethoven: Sonatas Nos. 7 - 12 (Remastered)\r\ncomposer: Ludwig van Beethoven\r\ngenre: Classical\r\nalbumArtist: Wilhelm Kempff\r\ncomment: https://archive.org/details/wilhelmkempff-beethoven-sonatasnos.7-12remastered201924-96\r\ntitle: Piano Sonata No. 8 in C Minor, Op. 13 "Pathétique": 2. Adagio cantabile\r\ntrack: 6	\N	\N	\N	\N	\N	50496	2026-02-12 08:59:47.692766-03	2026-02-12 08:59:47.69277-03	100	\N	\N	\N	f
50648	Mozart - Concerto No. 12 in A: II. Andante	http://archive.org/details/MOZARTPianoConcertosNos.101217182025	3	durationSeconds. 510\r\nartist. Robert Casadesus\r\nyear. 1955\r\nalbum. MOZART: Piano Concertos\r\ncomposer. Mozart	\N	\N	\N	\N	\N	50649	2026-02-15 14:29:12.425071-03	2026-02-15 14:29:12.425103-03	100	\N	Attribution-Noncommercial-No Derivative Works 3.0	http://archive.org/details/MOZARTPianoConcertosNos.101217182025	f
50650	Mozart - Concerto No. 20 in D Minor: I. Allegro	https://archive.org/details/MOZARTPianoConcertosNos.101217182025/13.+Concerto+No.+20+in+D+Minor-+I.+Allegro.mp3	3	artist. Robert Casadesus\r\nyear. 1956\r\nalbum. MOZART: Piano Concertos\r\ncomposer. Mozart\r\nMOZART: Piano Concertos Nos. 10, 12, 17, 18, 20 & 25\r\nCleveland Orchestra\r\nGeorge Szell, conductor	\N	\N	\N	\N	\N	50651	2026-02-15 14:37:55.055645-03	2026-02-15 14:37:55.05566-03	100	\N	Attribution-Noncommercial-No Derivative Works 3.0	durationSeconds. 789	f
50685	Bach - 04 Cello Suite no. 1 in G major, BWV 1007- IV. Sarabande	https://commons.wikimedia.org/wiki/File:Bach_-_Cello_Suite_no._1_in_G_major,_BWV_1007_-_IV._Sarabande.ogg	3	artist. 04 Cello Suite no. 1 in G major, BWV 1007- IV. Sarabande\r\nyear. 1962\r\nBach, Cello Suite no. 1 in G major, BWV 1007, IV. Sarabande. Interprété par Pablo Casals.\r\nDate\t1929/1939.\r\nSource\tCD audio, Naxos Historical, restoration by Ward Marston, 2000.\r\nAuthor\tBach, Johann-Sebastian.	\N	\N	\N	\N	\N	50686	2026-02-15 22:25:46.808683-03	2026-02-15 22:25:46.80869-03	100	\N	This sound recording is not protected because it was published in the European Union more than 50 (Directive 2006/116/EC) or 70 years ago (Directive 2011/77/EU) and the rights of its performers and producers have expired.	\N	t
50640	Chopin - Nocturne No. 1 in B-flat minor, Op. 9 No. 1	https://commons.wikimedia.org/wiki/File:Chopin_-_Nocturne_No._1_in_B-flat_minor,_Op._9_No._1_(Olga_Gurevich).flac	3	durationSeconds: 337\r\nartist: Olga Gurevich\r\nalbum: Musopen: The Complete Works of Frédéric Chopin\r\nformat: Flac\r\ngenre: Classical\r\naudioFormat: audio/x-flac\r\nbitrate: 949\r\nsampleRate: 96000	\N	\N	\N	\N	\N	50641	2026-02-15 10:14:41.013-03	2026-02-15 10:14:41.013007-03	100	\N	Frédéric Chopin, Public domain, via Wikimedia Commons	\N	t
50644	Mozart - Symphonie Nr. 40 g-moll, KV 550	https://en.wikipedia.org/wiki/File:Wolfgang_Amadeus_Mozart_-_Symphony_40_g-moll_-_1._Molto_allegro.ogg	3	Symphony 40 g-moll, KV 550 - 1. Molto allegro\r\ndurationSeconds: 493\r\nartist: Fulda Symphonic Orchestra, Conductor: Simon Schindler; Performer: Franziska Früh (violin)\r\nyear: 2001-03-18 (recorded)\r\nalbum: 2. Benefiz Symphonie-Konzert\r\ncomposer: Wolfgang Amadeus Mozart\r\nformat: Ogg\r\naudioFormat: audio/vorbis\r\nbitrate: 224\r\ntitle: Symphonie Nr. 40 g-moll, KV 550\r\nsampleRate: 44100\r\nduration: 493.2\r\ngenre: classical\r\ntrack: 5\r\nConductor: Simon Schindler\r\nLocation: Grosser Saal der Orangerie Fulda	\N	\N	\N	\N	\N	50645	2026-02-15 13:58:07.788504-03	2026-02-15 13:58:07.78851-03	100	\N	EFF: Open Audio License version 1	\N	t
50687	Bach - 06 Cello Suite no. 1 in G major, BWV 1007- VI. Gigue	https://commons.wikimedia.org/wiki/File:Bach_-_Cello_Suite_no._1_in_G_major,_BWV_1007_-_VI._Gigue.ogg	3	artist. 06 Cello Suite no. 1 in G major, BWV 1007- VI. Gigue\r\nyear. 1962\r\nBach, Cello Suite no. 1 in G major, BWV 1007, VI. Gigue. Interprété par Pablo Casals.\r\nDate\t1929/1939.\r\nSource\tCD audio, Naxos Historical, restoration by Ward Marston, 2000.	\N	\N	\N	\N	\N	50688	2026-02-15 22:27:19.376212-03	2026-02-15 22:27:19.376216-03	100	\N	This sound recording is not protected because it was published in the European Union more than 50 (Directive 2006/116/EC) or 70 years ago (Directive 2011/77/EU) and the rights of its performers and producers have expired.	\N	f
50604	Beethoven - sonata no. 14 in c sharp minor 'moonlight', op. 27 no. 2 - i. adagio sostenuto	https://upload.wikimedia.org/wikipedia/commons/4/48/Ludwig_van_Beethoven_-_sonata_no._14_in_c_sharp_minor_%27moonlight%27%2C_op._27_no._2_-_i._adagio_sostenuto.ogg	3	Ludwig van Beethoven, Public domain, via Wikimedia Commons\r\nSonata No. 14 in C Sharp Minor "Moonlight", Op. 27 No. 2, 1st movement (adagio sostenuto), performed by Paul Pitman for Musopen.	\N	\N	\N	\N	\N	50605	2026-02-13 10:57:34.609831-03	2026-02-13 10:57:34.609879-03	100	\N	Creative Commons Attribution-Share Alike 2.0 Generic license.	\N	t
50626	Bach - Flute Sonata E minor (BWV 1034) - 3. Andante	https://commons.wikimedia.org/wiki/File:Bach_-_Flute_Sonata_Emin_-_3._Andante.ogg	3	duration: 220.15\r\naudioFormat: audio/vorbis\r\nFlute Sonata E minor (BWV 1034) - 3. Andante\r\n\r\nThese recordings(1974) come from Pan104 titled "Complete and Authentic Works from the Neue Bach Gesellshaft." There is one more Sonata which is agreed on to be authentic, but it is incomplete in the 1st movement. It will be found in Vol2.\r\n\r\nThe traverso was made by Engelhard of Nuernberg, who was born in 1758. The pitch is A=419 herz. This flute is from the de Wit collection in Amsterdam and was kindly lent by the owner for the recording. The harpsichord is an Italian replica, similar to the one used for the Italian harpsichord recording, except for a nasard stop, an additional row of jacks close to the plucking point.\r\n\r\nThe tuning for the B minor sonatas which traverses many keys is the Werkmeister #1, a well temperament. The remaining sonatas are in mean tone tuning. The realizations of the bass for sonatas in E major and minor were done by Martha Goldstein.	\N	\N	\N	\N	\N	50627	2026-02-15 09:47:12.274918-03	2026-02-15 09:47:12.274953-03	100	\N	Creative Commons Attribution-Share Alike 2.0 Generic license.	\N	t
50638	Beethoven - Piano Concerto No. 4, op. 58, G major (1st movement, allegro moderato)	https://commons.wikimedia.org/wiki/File:Beethoven_concerto4_1.ogg	3	English: Piano Concerto No. 4, op. 58, G major (1st movement, allegro moderato)\r\nDate\tbetween 1805 and 1806\r\nSource\thttp://hebb.mit.edu/FreeMusic/ (404)\r\nAuthor\tComposer:\r\nLudwig van Beethoven (1770–1827)  wikidata:Q255 s:en:Author:Ludwig van Beethoven q:en:Ludwig van Beethoven\r\nPiano performance: Debbie Hu\r\ndurationSeconds: 1073\r\nformat: Ogg\r\naudioFormat: audio/vorbis\r\nbitrate: 149\r\nsampleRate: 44100	\N	\N	\N	\N	\N	50639	2026-02-15 10:10:35.874528-03	2026-02-15 10:10:35.874533-03	100	\N	Creative Commons Attribution-Share Alike 2.0 Generic license.	\N	t
50592	Atahualpa Yupanqui – Camino Del Indio	https://archive.org/details/atahualpa-yupanqui-camino-del-indio	3	Atahualpa Yupanqui – Camino Del Indio\r\nby Atahualpa Yupanqui\r\n\r\nPublication date 1957 Topics Folk, World, Folk Music, Argentina, Argentinean Music Language Spanish Item Size 35.1M	\N	\N	\N	\N	\N	50593	2026-02-13 00:52:40.898946-03	2026-02-13 00:52:40.898974-03	100	\N	RCA Victor – AVL-3086 | Vinyl, LP, Compilation, Mono | Argentina | 1957\r\nPublic domain since 2027.	\N	t
50689	Vivaldi - Concerto No. 1 in E major, Op. 8, RV 269, "La primavera" (Spring) — Movement 1: Allegro	https://commons.wikimedia.org/wiki/File:Vivaldi_-_Four_Seasons_1_Spring_mvt_1_Allegro_-_John_Harrison_violin.oga	3	Concerto No. 1 in E major, Op. 8, RV 269, "La primavera" (Spring) — Movement 1: Allegro from The Four Seasons by Antonio Vivaldi\r\nJohn Harrison — Violin / Robert Turizziani — Conductor / Wichita State University Chamber Players\r\nDate\tcomposed 1723 and published in 1725, performed on 2000-02-06	\N	\N	\N	\N	\N	50690	2026-02-16 12:38:36.477-03	2026-02-16 12:38:36.477024-03	100	\N	Creative Commons license\r\nThis file is licensed under the Creative Commons Attribution ShareAlike license, versions 1.0, 2.0, 2.5, 3.0, 4.0	\N	t
50693	Vivaldi - Autumn Mvt 2: Adagio molto	https://commons.wikimedia.org/wiki/File:Violinist_CARRIE_REHKOPF-BEETHOVEN_VIOLIN_SONATA_2_3rd_mvt.ogg	3	artist. John Harrison - Violin\r\nyear. February 6, 2000\r\nalbum. Vivaldi: The Four Seasons\r\n\r\nJohn Harrison, Violin\r\nRobert Turizziani, Conductor\r\nWichita State University Chamber Players\r\nhttp://JohnHarrisonViolin.com	\N	\N	\N	\N	\N	50694	2026-02-16 12:43:47.729327-03	2026-02-16 12:43:47.729335-03	100	\N	This file is licensed under Creative Commons\r\nAttribution ShareAlike 1.0 License:\r\nhttp://creativecommons.org/licenses/by-sa/1.0/	\N	t
50695	Vivaldi - The Four Seasons - Autumn Mvt 3: Allegro	https://commons.wikimedia.org/wiki/File:Vivaldi_-_Four_Seasons_3_Autumn_mvt_3_Allegro_-_John_Harrison_violin.oga	3	artist. John Harrison - Violin\r\nyear. February 6, 2000\r\nalbum. Vivaldi: The Four Seasons\r\nJohn Harrison, Violin\r\nRobert Turizziani, Conductor\r\nWichita State University Chamber Players\r\nhttp://JohnHarrisonViolin.com	\N	\N	\N	\N	\N	50696	2026-02-16 12:47:19.38387-03	2026-02-16 12:47:19.383884-03	100	\N	This file is licensed under Creative Commons\r\nAttribution ShareAlike 1.0 License:\r\nhttp://creativecommons.org/licenses/by-sa/1.0/	\N	t
50691	Vivaldi - Four Seasons 1 Spring mvt 2 Largo	https://commons.wikimedia.org/wiki/File:Vivaldi_-_Four_Seasons_1_Spring_mvt_2_Largo_-_John_Harrison_violin.oga	3	Vivaldi - Four Seasons 1 Spring mvt 2 Largo \r\nConcerto No. 1 in E major, Op. 8, RV 269, "La primavera" (Spring) — Movement 2: Largo\r\nfrom The Four Seasons by Antonio Vivaldi\r\nLive, Unedited performance — 2m:51.639s\r\n\r\nWiedemann Recital Hall, Wichita State University\r\n\r\nJohn Harrison — Violin / Robert Turizziani — Conductor / Wichita State University Chamber Players\r\nDate\tComposed 1723; Recorded February 6, 2000	\N	\N	\N	\N	\N	50692	2026-02-16 12:41:28.245579-03	2026-02-16 12:50:02.053051-03	100	\N	This file is licensed under the Creative Commons Attribution ShareAlike license, versions 1.0, 2.0, 2.5, 3.0, 4.0	\N	t
\.


--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.person (id, name, namekey, title, titlekey, lastname, lastnamekey, displayname, nickname, sex, physicalid, address, zipcode, phone, email, birthdate, user_id, subtitle, subtitlekey, info, infokey, photo, video, audio, created, lastmodified, lastmodifieduser, sortlastfirstname, isex, webpage, state, language, draft, translation, masterlanguage, usethumbnail, intro, spec, opens, audioautogenerate, speechaudio) FROM stdin;
269	Lucio	lucio	Correa Morales Lucio	\N	Correa Morales	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	correa morales lucio	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
185	Henri	henri	Toulouse Lautrec Henri	\N	Toulouse Lautrec	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	toulouse lautrec henri	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
192	Doménikos	dom-nikos	Theotokópoulos Doménikos	\N	Theotokópoulos	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	theotokópoulos doménikos	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
308	Rafael	rafael	Barredas Rafael	\N	Barredas	barredas	Rafael Barredas	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:53:48.69726-03	2025-05-19 12:53:48.69726-03	100	barredas rafael	1		3	es	\N	0	es	t	\N	\N	\N	t	\N
265	Ernesto	ernesto	de la Cárcova Ernesto	\N	de la Cárcova	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	de la cárcova ernesto	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
187	Edgar	edgar	Degas Edgar	\N	Degas	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	degas edgar	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
263	Ángel	ngel	Della Valle Ángel	\N	Della Valle	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	della valle Ángel	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
194	Tarsila	tarsila	do Amaral Tarsila	\N	do Amaral	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	do amaral tarsila	0	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
191	Jean Louis	jean-louis	Forain Jean Louis	\N	Forain	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	forain jean louis	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
186	Paul	paul	Gauguin Paul	\N	Gauguin	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	gauguin paul	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
190	Edouard	edouard	Manet Edouard	\N	Manet	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	manet edouard	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
266	Graciano	graciano	Mendihalarzu Graciano	\N	Mendihalarzu	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	mendihalarzu graciano	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
183	Claude	claude	Monet Claude	\N	Monet	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	monet claude	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
188	Berthe Marie Pauline	berthe-marie-pauline	Morisot Berthe Marie Pauline	\N	Morisot	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	morisot berthe marie pauline	0	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
264	María	mar-a	Obligado María	\N	Obligado	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	obligado maría	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
184	Camille	camille	Pissarro Camille	\N	Pissarro	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	pissarro camille	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
333	Benito	benito	Quinquela Martín Benito	\N	Quinquela Martín	quinquela-mart-n	Benito Quinquela Martín	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 14:07:33.533893-03	2025-05-19 14:07:33.533893-03	100	quinquela martín benito	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
193	Odilon	odilon	Redon Odilon	\N	Redon	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	redon odilon	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
181			root 	\N	root	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	100	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	root 	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
182	Alfred	alfred	Sisley Alfred	\N	Sisley	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	sisley alfred	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
268	Eduardo	eduardo	Schiaffino Eduardo	\N	Schiaffino	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	schiaffino eduardo	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
267	Eduardo	eduardo	Sívori Eduardo	\N	Sívori	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:30:21.186648-03	2025-05-19 12:30:21.186648-03	100	sívori eduardo	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
189	Vicent	vicent	Van Gogh Vicent	\N	Van Gogh	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-05-19 12:29:38.885286-03	100	van gogh vicent	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
447	Germán	\N	\N	\N	Gárgano	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-07-25 18:47:09.47387-03	2025-07-25 18:47:09.47387-03	100	gárgano germán	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
880	Wilfredo	\N	\N	\N	Lam	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-11-29 07:32:12.850213-03	2025-11-29 07:32:12.850221-03	100	lam wilfredo	1	\N	3	es	\N	\N	es	t	\N	\N	\N	f	\N
593	María	new	new new	\N	Martins	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-09-19 20:06:54.869553-03	2025-09-19 20:07:10.855372-03	100	martins maría	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
581	Roberto	new	new new	\N	Matta	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-09-19 09:55:19.878194-03	2025-09-21 19:56:06.818865-03	100	matta roberto	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
400	Raymond Auguste	\N	\N	\N	Quinsac Monvoisin	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	quinsac monvoisin raymond auguste	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
399	Kazuya	\N	\N	\N	Sakai	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	sakai kazuya	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
398	Joaquín	\N	\N	\N	Sorolla y Bastida	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	sorolla y bastida joaquín	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
588	Alejandro	new	new new	\N	Xul Solar	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-09-19 10:50:03.260804-03	2025-09-19 10:50:12.994796-03	100	xul solar alejandro	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
602	Carrie	new	new new	\N	Bencardino	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	50615	\N	\N	2025-09-21 19:57:10.72936-03	2025-09-21 19:57:45.082858-03	100	bencardino carrie	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
180	Alejandro		- Alejandro	\N	-	\N	\N	\N	\N	Tolomei	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-05-19 12:29:38.885286-03	2026-02-16 16:15:47.20962-03	100	- alejandro	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
481	Mariano	\N	\N	\N	Fortuny y Marsal	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-10-15 18:34:58.958093-03	100	fortuny y marsal mariano	1	\N	2	es	\N	0	es	t	\N	\N	\N	t	\N
763	Alberto	\N	\N	\N	Churba	new	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-10-29 08:56:13.397897-03	2025-10-29 09:33:45.135039-03	100	churba alberto	1	\N	3	es	\N	\N	es	f	\N	\N	\N	f	\N
1113	Alejandro	\N	\N	\N	Tolomei	new	\N	\N	\N	\N	Montañeses 1853	\N	5491161194075	atolomei@novamens.com	\N	101	\N	\N	\N	\N	1730	\N	\N	2025-12-11 18:26:14.807884-03	2025-12-11 18:26:14.80789-03	100	tolomei alejandro	1	\N	3	es	\N	\N	es	t	\N	\N	\N	t	\N
50404	Gabriel	\N	\N	\N	Miremont	new	\N	\N	\N	\N	\N	\N	+54 9 11 3121-5667	\N	\N	50412	\N	\N	\N	\N	50411	\N	\N	2026-02-11 09:36:15.18413-03	2026-02-11 09:36:15.184159-03	100	miremont gabriel	1	\N	3	es	\N	\N	es	t	\N	\N	\N	t	\N
401	Jean Joseph Benjamin	\N	\N	\N	Constant	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-06-10 15:14:26.074639-03	2025-06-10 15:14:26.074639-03	100	constant jean joseph benjamin	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
480	Nicolás	\N	\N	\N	García Uriburu	\N	\N	\N	\N	-	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	2025-07-28 11:45:14.593005-03	2025-07-28 11:45:14.593005-03	100	garcía uriburu nicolás	1	\N	3	es	\N	0	es	t	\N	\N	\N	t	\N
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
50405	50404	es	new	\N	\N	\N	\N	\N	2026-02-11 09:36:15.197134-03	2026-02-11 09:36:15.197137-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	new
50406	50404	en	new	\N	\N	\N	\N	\N	2026-02-11 09:36:15.202016-03	2026-02-11 09:36:15.202017-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	new
50407	50404	pt-BR	new	\N	\N	\N	\N	\N	2026-02-11 09:36:15.204001-03	2026-02-11 09:36:15.204001-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	new
50408	50404	it	new	\N	\N	\N	\N	\N	2026-02-11 09:36:15.206282-03	2026-02-11 09:36:15.206295-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	new
50409	50404	fr	new	\N	\N	\N	\N	\N	2026-02-11 09:36:15.207943-03	2026-02-11 09:36:15.207943-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	new
50410	50404	ger	new	\N	\N	\N	\N	\N	2026-02-11 09:36:15.209719-03	2026-02-11 09:36:15.209719-03	100	\N	\N	1	t	0	0	0	0	\N	0	\N	0	\N	0	t	f	new
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

COPY public.resource (id, name, namekey, title, titlekey, media, info, infokey, bucketname, objectname, binaryobject, created, lastmodified, lastmodifieduser, usethumbnail, size, height, width, state, durationmilliseconds, tag, language, draft, audioautogenerate, audit, filename, meta_json) FROM stdin;
813	sendas-perdidas-458-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	sendas-perdidas-458-music-218	\N	2025-11-17 14:45:00.695281-03	2025-11-17 14:45:01.434009-03	100	f	5249179	0	0	\N	328000	\N	\N	\N	f		sendas-perdidas-458-music.mp3	\N
844	mnba-captura-3.png	\N	\N	\N	image/png	\N	\N	media	mnba-captura-3-225	\N	2025-11-24 13:06:31.802949-03	2025-11-24 13:06:31.875047-03	100	t	2690312	1024	1536	\N	0	\N	\N	\N	f		mnba-captura-3.png	\N
911	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	wifredo-lam-cuando-no-duermo-sue-o-894-music-237	\N	2025-11-30 17:48:33.882903-03	2025-11-30 17:48:33.882906-03	100	f	917032	0	0	\N	57000	\N	\N	\N	f		wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N
423	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-146	\N	2025-06-12 13:06:22.871895-03	2025-11-12 11:33:17.707344-03	100	t	1879249	0	0	1	156000	\N	\N	\N	f		theodora.mp3	\N
636	museo-sectreto.mp3	museo-sectreto-mp3	\N	\N	audio/mp3	\N	\N	media	museo-sectreto-187	\N	2025-10-01 16:14:53.604237-03	2025-11-12 11:33:17.707382-03	100	t	1641953	0	0	\N	137000	\N	\N	\N	f		museo-sectreto.mp3	\N
1492	mlam-logo.svg	\N	\N	\N	image/svg+xml	\N	\N	media	mlam-logo-248	\N	2025-12-15 12:56:29.276536-03	2025-12-15 12:56:29.276538-03	100	f	1183	0	0	\N	0	\N	\N	\N	f	\N	mlam-logo.svg	\N
1493	mlam-logo-2.svg	\N	\N	\N	image/svg+xml	\N	\N	media	mlam-logo-2-249	\N	2025-12-15 13:07:36.336743-03	2025-12-15 13:07:36.336744-03	100	f	1188	0	0	\N	0	\N	\N	\N	f	\N	mlam-logo-2.svg	\N
19611	omiobini.jpg	\N	\N	\N	image/jpeg	\N	\N	media	omiobini-256	\N	2026-01-05 15:46:00.160709-03	2026-01-05 15:46:00.160716-03	100	f	252264	415	750	\N	0	\N	\N	\N	f		omiobini.jpg	\N
30179	20260110_142007.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_142007-265	\N	2026-01-11 10:13:58.500478-03	2026-01-11 10:13:58.500484-03	100	f	575227	1800	4000	3	0	\N	\N	\N	f		20260110_142007.jpg	\N
30290	20260110_134326.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_134326-266	\N	2026-01-11 10:15:25.495509-03	2026-01-11 10:15:25.495522-03	100	f	1542933	1800	4000	3	0	\N	\N	\N	f		20260110_134326.jpg	\N
32034	20260110_141921.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_141921-270	\N	2026-01-11 14:35:25.168993-03	2026-01-11 14:35:25.168996-03	100	f	2872664	1800	4000	3	0	\N	\N	\N	f		20260110_141921.jpg	\N
40298	20260110_133910.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_133910-283	\N	2026-01-13 15:09:42.204061-03	2026-01-13 15:09:42.204062-03	100	f	309505	2996	544	3	0	\N	\N	\N	f		20260110_133910.jpg	\N
50367	fachada-del-museo-del.jpg	\N	\N	\N	image/jpeg	\N	\N	media	fachada-del-museo-del-290	\N	2026-02-10 18:59:23.433007-03	2026-02-10 18:59:23.433009-03	100	t	198235	770	1000	3	0	\N	\N	\N	f		fachada-del-museo-del.jpg	\N
50434	ocampo.jpg	\N	\N	\N	image/jpeg	\N	\N	media	ocampo-297	\N	2026-02-11 14:43:31.279773-03	2026-02-11 14:43:31.279775-03	100	t	1257790	1759	3118	3	0	\N	\N	\N	f		ocampo.jpg	\N
50456	legados-del-hacer-50443.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	legados-del-hacer-50443-303	\N	2026-02-11 16:41:05.461469-03	2026-02-11 16:41:05.461476-03	100	f	1240297	0	0	3	0	\N	\N	\N	f	Error -> 282 secs | 4398.2163120567375 bytes/sec	legados-del-hacer-50443.mp3	\N
50467	obras-maestras-202.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	obras-maestras-202-312	\N	2026-02-11 17:50:26.083763-03	2026-02-11 17:50:26.083768-03	100	f	272269	0	0	3	0	\N	\N	\N	f	\N	obras-maestras-202.mp3	\N
50479	test-marcela-es.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-marcela-es-321	\N	2026-02-11 21:32:22.10495-03	2026-02-11 21:32:22.104956-03	100	f	1240297	0	0	3	0	\N	\N	\N	f	\N	test-marcela-es.mp3	\N
50480	h-ritages-de-laction-50448.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	h-ritages-de-laction-50448-322	\N	2026-02-11 21:34:35.617563-03	2026-02-11 21:34:35.617565-03	100	f	1298139	0	0	3	148000	\N	\N	\N	f		h-ritages-de-laction-50448.mp3	\N
368	museo-secreto.mp3	museo-secreto-mp3	\N	\N	audio/mp3	\N	\N	media	museo-secreto-137	\N	2025-06-10 11:30:30.908406-03	2025-11-12 11:33:17.701903-03	100	t	703216	0	0	1	88000	\N	\N	\N	f		museo-secreto.mp3	\N
419	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-142	\N	2025-06-11 18:06:38.756155-03	2025-11-12 11:33:17.784477-03	100	t	1630668	0	0	1	136000	\N	\N	\N	f		rosas.mp3	\N
797	411-museo-secreto.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	411-museo-secreto-211	\N	2025-11-11 17:42:27.056974-03	2025-11-12 12:15:24.110301-03	100	f	5351148	0	0	\N	0	\N	\N	\N	f	Error -> 1284 secs | 4167.560747663551 bytes/sec	411-museo-secreto.mp3	\N
806	museo-secreto-411.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	museo-secreto-411-213	\N	2025-11-13 09:55:32.630612-03	2025-11-13 10:10:14.148298-03	100	f	742368	0	0	\N	0	\N	\N	\N	f	Error -> 180 secs | 4124.266666666666 bytes/sec	museo-secreto-411.mp3	\N
758	viaggio-italia.webp	\N	\N	\N	image/webp	\N	\N	media	viaggio-italia-200	\N	2025-10-28 15:03:13.133952-03	2025-10-28 15:03:13.133954-03	100	f	29436	0	0	\N	0	\N	\N	\N	f	\N	viaggio-italia.webp	\N
565	Museo_malba_logo.png	museo-malba-logo-png	\N	\N	image/png	\N	\N	media	museo_malba_logo-175	\N	2025-09-15 11:12:42.960448-03	2025-09-15 11:12:43.040887-03	100	t	8728	78	424	1	0	\N	\N	\N	f	\N	Museo_malba_logo.png	\N
590	troncos-xul-solar.jpg	troncos-xul-solar-jpg	\N	\N	image/jpeg	\N	\N	media	troncos-xul-solar-180	\N	2025-09-19 10:51:24.112219-03	2025-09-19 10:51:24.176399-03	100	t	56549	767	1140	1	0	\N	\N	\N	f	\N	troncos-xul-solar.jpg	\N
50516	02 Rudolf Baumgartner Festival Strings Lucerne Antonio Vivaldi — Concerto No 4 in F Minor _LInverno II. Largo.mp3	\N	\N	\N	audio/mp3	\N	\N	media	02-rudolf-baumgartner-festival-strings-lucerne-antonio-vivaldi---concerto-no-4-in-f-minor-_linverno-ii-largo-343	\N	2026-02-12 12:06:57.607341-03	2026-02-12 12:06:57.607393-03	100	f	5686378	0	0	3	142000	\N	\N	\N	f		02 Rudolf Baumgartner Festival Strings Lucerne Antonio Vivaldi — Concerto No 4 in F Minor _LInverno II. Largo.mp3	\N
50518	03 Rudolf Baumgartner-Festival Strings Lucerne-Antonio Vivaldi—Concerto-No-4inFMinor_Inverno_IIII-Allegro.mp3	\N	\N	\N	audio/mp3	\N	\N	media	03-rudolf-baumgartner-festival-strings-lucerne-antonio-vivaldi-concerto-no-4infminor_inverno_iiii-allegro-345	\N	2026-02-12 12:08:20.597207-03	2026-02-12 12:08:20.597212-03	100	f	7071913	0	0	3	0	\N	\N	\N	f	\N	03 Rudolf Baumgartner-Festival Strings Lucerne-Antonio Vivaldi—Concerto-No-4inFMinor_Inverno_IIII-Allegro.mp3	\N
50520	11. Concerto in G Major (P. 135) for 2 violins & 2 cellos- II. Largo.mp3	\N	\N	\N	audio/mp3	\N	\N	media	11-concerto-in-g-major-p-135-for-2-violins--2-cellos--ii-largo-348	\N	2026-02-12 12:15:15.258497-03	2026-02-12 12:15:15.2585-03	100	f	5448603	0	0	3	201000	\N	\N	\N	f		11. Concerto in G Major (P. 135) for 2 violins & 2 cellos- II. Largo.mp3	\N
50522	Gaetano Donizetti (L' elisir d'amore) - Una furtiva lagrima.mp3	\N	\N	\N	audio/mp3	\N	\N	media	gaetano-donizetti-l-elisir-damore---una-furtiva-lagrima-349	\N	2026-02-12 13:24:50.1136-03	2026-02-12 13:24:50.113606-03	100	f	6115543	0	0	3	255000	\N	\N	\N	f		Gaetano Donizetti (L' elisir d'amore) - Una furtiva lagrima.mp3	\N
50524	02 Andante - Allegro.mp3	\N	\N	\N	audio/mp3	\N	\N	media	02-andante---allegro-350	\N	2026-02-12 13:40:10.521666-03	2026-02-12 13:40:10.521669-03	100	f	28930886	0	0	3	724000	\N	\N	\N	f		02 Andante - Allegro.mp3	\N
50526	07 - Aquarela.mp3	\N	\N	\N	audio/mp3	\N	\N	media	07---aquarela-351	\N	2026-02-12 13:45:40.325698-03	2026-02-12 13:45:40.3257-03	100	f	6572339	0	0	3	0	\N	\N	\N	f	\N	07 - Aquarela.mp3	\N
50543	01.01. Brandenburg Concertos, Vol. 1 No. 1 In F Major (Bwv.1046)_ 1st. Mov._ Allegro.mp3	\N	\N	\N	audio/mp3	\N	\N	media	0101-brandenburg-concertos-vol-1-no-1-in-f-major-bwv1046_-1st-mov_-allegro-360	\N	2026-02-12 14:49:26.357616-03	2026-02-12 14:49:26.357617-03	100	f	6392135	0	0	3	256000	\N	\N	\N	f		01.01. Brandenburg Concertos, Vol. 1 No. 1 In F Major (Bwv.1046)_ 1st. Mov._ Allegro.mp3	\N
50545	02.04. Air In D From 3rd Suite.mp3	\N	\N	\N	audio/mp3	\N	\N	media	0204-air-in-d-from-3rd-suite-361	\N	2026-02-12 14:49:52.536482-03	2026-02-12 14:49:52.536484-03	100	f	6130506	0	0	3	0	\N	\N	\N	f	\N	02.04. Air In D From 3rd Suite.mp3	\N
50547	02- Concerto No. 1 - Adagio (1).mp3	\N	\N	\N	audio/mp3	\N	\N	media	02--concerto-no-1---adagio-1-362	\N	2026-02-12 14:52:13.993638-03	2026-02-12 14:52:13.99364-03	100	f	20616156	0	0	3	0	\N	\N	\N	f	\N	02- Concerto No. 1 - Adagio (1).mp3	\N
814	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-219	\N	2025-11-17 14:53:52.93607-03	2025-11-17 14:53:53.086918-03	100	f	484606	0	0	\N	59000	\N	\N	\N	f		sendas-perdidas-458.mp3	\N
859	mamba-4.jpg	\N	\N	\N	image/jpeg	\N	\N	media	mamba-4-226	\N	2025-11-28 10:41:29.304633-03	2025-11-28 10:41:29.304643-03	100	f	160333	448	1024	\N	0	\N	\N	\N	f		mamba-4.jpg	\N
868	Lam-The-Jungle-thumb.jpg	\N	\N	\N	image/jpeg	\N	\N	media	lam-the-jungle-thumb-227	\N	2025-11-28 10:54:16.177028-03	2025-11-28 10:54:16.177036-03	100	f	725383	714	1188	\N	0	\N	\N	\N	f		Lam-The-Jungle-thumb.jpg	\N
913	a-selva-902.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	a-selva-902-238	\N	2025-11-30 18:19:26.229231-03	2025-11-30 18:19:26.229232-03	100	f	1599673	0	0	\N	0	\N	\N	\N	f	\N	a-selva-902.mp3	\N
422	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-145	\N	2025-06-12 13:06:21.256168-03	2025-11-12 11:33:17.70737-03	100	t	1747592	0	0	1	145000	\N	\N	\N	f		rosas.mp3	\N
336	reposo.mp3	reposo-mp3	\N	\N	audio/mp3	\N	\N	media	reposo-125	\N	2025-05-19 17:03:01.658216-03	2025-11-12 11:33:17.733539-03	100	t	6210291	0	0	1	304000	\N	\N	\N	f		reposo.mp3	\N
421	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-144	\N	2025-06-11 21:47:41.748635-03	2025-11-12 11:33:17.784402-03	100	t	1873920	0	0	1	156000	\N	\N	\N	f		theodora.mp3	\N
424	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-147	\N	2025-06-13 14:30:00.94748-03	2025-11-12 11:33:17.850208-03	100	t	1512803	0	0	1	126000	\N	\N	\N	f		rosas.mp3	\N
418	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-141	\N	2025-06-11 18:00:45.327953-03	2025-11-12 11:33:17.917488-03	100	t	1630668	0	0	1	136000	\N	\N	\N	f		rosas.mp3	\N
420	rosas.mp3	rosas-mp3	\N	\N	audio/mp3	\N	\N	media	rosas-143	\N	2025-06-11 18:22:39.656749-03	2025-11-12 11:33:17.997241-03	100	t	1656059	0	0	1	138000	\N	\N	\N	f		rosas.mp3	\N
914	a-selva-902-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	a-selva-902-music-239	\N	2025-11-30 18:20:46.450382-03	2025-11-30 18:20:46.450384-03	100	f	1231755	0	0	\N	0	\N	\N	\N	f	\N	a-selva-902-music.mp3	\N
917	the-jungle-901.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	the-jungle-901-240	\N	2025-11-30 18:31:20.060439-03	2025-11-30 18:31:20.06044-03	100	f	1657112	0	0	\N	0	\N	\N	\N	f	Error -> 338 secs | 4902.698224852071 bytes/sec	the-jungle-901.mp3	\N
918	the-jungle-901-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	the-jungle-901-music-241	\N	2025-11-30 18:32:01.241715-03	2025-11-30 18:32:01.241716-03	100	f	1311585	0	0	\N	0	\N	\N	\N	f	\N	the-jungle-901-music.mp3	\N
1499	Bellasartes_logo.png	\N	\N	\N	image/png	\N	\N	media	bellasartes_logo-250	\N	2025-12-20 12:29:36.040729-03	2025-12-20 12:29:36.040731-03	100	t	8953	60	525	\N	0	\N	\N	\N	f		Bellasartes_logo.png	\N
22474	la-jungla-899.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	la-jungla-899-257	\N	2026-01-06 14:41:51.591231-03	2026-01-06 14:41:51.591233-03	100	f	1635022	0	0	3	0	\N	\N	\N	f	Error -> 409 secs | 3997.60880195599 bytes/sec	la-jungla-899.mp3	\N
22475	la-jungla-899-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	la-jungla-899-music-258	\N	2026-01-06 14:55:41.3739-03	2026-01-06 14:55:41.373901-03	100	f	1233009	0	0	3	0	\N	\N	\N	f	\N	la-jungla-899-music.mp3	\N
807	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-214	\N	2025-11-13 13:26:03.500316-03	2025-11-13 13:26:11.031749-03	100	f	443283	0	0	\N	15000	\N	\N	\N	f		sendas-perdidas-458.mp3	\N
30401	Cabildo_Abierto_-_Pedro_Subercaseaux.jpg	\N	\N	\N	image/jpeg	\N	\N	media	cabildo_abierto_-_pedro_subercaseaux-267	\N	2026-01-11 10:19:32.933588-03	2026-01-11 10:19:32.933595-03	100	f	234132	900	1200	3	0	\N	\N	\N	f		Cabildo_Abierto_-_Pedro_Subercaseaux.jpg	\N
762	portrait-suzanne.jpg	\N	\N	\N	image/jpeg	\N	\N	media	portrait-suzanne-202	\N	2025-10-29 08:53:37.577633-03	2025-10-29 08:53:37.669001-03	100	f	2137276	2104	1808	\N	0	\N	\N	\N	f	\N	portrait-suzanne.jpg	\N
641	red-flower-3794782_640.jpg	red-flower-3794782-640-jpg	\N	\N	image/jpeg	\N	\N	avatar	red-flower-3794782_640-193	\N	2025-10-06 11:27:25.276051-03	2025-11-11 22:16:07.558095-03	100	t	103917	427	640	\N	0	avatar	\N	\N	f	\N	red-flower-3794782_640.jpg	\N
642	rose-5326516_640.jpg	rose-5326516-640-jpg	\N	\N	image/jpeg	\N	\N	avatar	rose-5326516_640-194	\N	2025-10-06 11:27:41.984373-03	2025-11-11 22:16:07.630641-03	100	t	60754	435	640	\N	0	avatar	\N	\N	f	\N	rose-5326516_640.jpg	\N
316	monet-La berge de La Seine.jpg	monet-la-berge-de-la-seine-jpg	\N	\N	image/jpeg	\N	\N	media	monet-la-berge-de-la-seine-111	\N	2025-05-19 14:04:43.610851-03	2025-09-02 17:28:34.529306-03	100	t	3047342	2236	2748	1	0	\N	\N	\N	f	\N	monet-La berge de La Seine.jpg	\N
750	Museo_Nacional_de_Arte_Decorativo.jpg	\N	\N	\N	image/jpeg	\N	\N	media	museo_nacional_de_arte_decorativo-196	\N	2025-10-28 14:45:44.299288-03	2025-11-11 22:16:07.697216-03	100	t	1238466	1774	2456	\N	0	\N	\N	\N	f	\N	Museo_Nacional_de_Arte_Decorativo.jpg	\N
560	malba1500x610fachada2023.jpg	malba1500x610fachada2023-jpg	\N	\N	image/jpeg	\N	\N	media	malba1500x610fachada2023-172	\N	2025-09-10 18:27:17.308694-03	2025-09-10 18:27:17.382329-03	100	t	329178	610	1500	1	0	\N	\N	\N	f	\N	malba1500x610fachada2023.jpg	\N
562	museo_coleccion_fortabat_1200_fachada (1).jpg	museo-coleccion-fortabat-1200-fachada-1-jpg	\N	\N	image/jpeg	\N	\N	media	museo_coleccion_fortabat_1200_fachada-(1)-173	\N	2025-09-12 14:11:30.154597-03	2025-10-02 13:42:24.645427-03	100	t	116441	489	1200	1	0	\N	\N	\N	f	\N	museo_coleccion_fortabat_1200_fachada (1).jpg	\N
554	fachada-mumoderno-1500x610.jpg	fachada-mumoderno-1500x610-jpg	\N	\N	image/jpeg	\N	\N	media	fachada-mumoderno-1500x610-167	\N	2025-09-10 18:13:51.004224-03	2025-10-02 13:42:24.646068-03	100	t	1011343	610	1500	1	0	\N	\N	\N	f	\N	fachada-mumoderno-1500x610.jpg	\N
583	Matta-The-disasters-of-Mysticism-122.jpg	matta-the-disasters-of-mysticism-122-jpg	\N	\N	image/jpeg	\N	\N	media	matta-the-disasters-of-mysticism-122-178	\N	2025-09-19 10:06:01.748986-03	2025-09-19 10:06:01.816049-03	100	t	115465	559	750	1	0	\N	\N	\N	f	\N	Matta-The-disasters-of-Mysticism-122.jpg	\N
586	barredas-circo.jpg	barredas-circo-jpg	\N	\N	image/jpeg	\N	\N	media	barredas-circo-179	\N	2025-09-19 10:15:23.967119-03	2025-09-19 10:15:24.025412-03	100	t	204273	714	750	1	0	\N	\N	\N	f	\N	barredas-circo.jpg	\N
778	churba-1.jpg	\N	\N	\N	image/jpeg	\N	\N	media	churba-1-204	\N	2025-10-29 13:31:47.169455-03	2025-10-29 13:31:47.247026-03	100	f	50681	1152	768	\N	0	\N	\N	\N	f	\N	churba-1.jpg	\N
30582	Cabildo_Abierto_-_Pedro_Subercaseaux.jpg	\N	\N	\N	image/jpeg	\N	\N	media	cabildo_abierto_-_pedro_subercaseaux-268	\N	2026-01-11 10:21:24.136375-03	2026-01-11 10:21:24.136378-03	100	f	234132	900	1200	3	0	\N	\N	\N	f		Cabildo_Abierto_-_Pedro_Subercaseaux.jpg	\N
30693	20260110_141221.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_141221-269	\N	2026-01-11 10:24:48.070752-03	2026-01-11 10:24:48.070755-03	100	f	703692	1568	1579	3	0	\N	\N	\N	f		20260110_141221.jpg	\N
34080	el-cabildo-de-buenos-aires-33825.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	el-cabildo-de-buenos-aires-33825-271	\N	2026-01-11 16:13:34.051609-03	2026-01-11 16:13:34.05161-03	100	f	2739003	0	0	3	0	\N	\N	\N	f	Error -> 609 secs | 4497.541871921182 bytes/sec	el-cabildo-de-buenos-aires-33825.mp3	\N
34081	el-cabildo-de-buenos-aires-33825-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	el-cabildo-de-buenos-aires-33825-music-272	\N	2026-01-11 16:15:24.021431-03	2026-01-11 16:15:24.021432-03	100	f	1904542	0	0	3	119000	\N	\N	\N	f		el-cabildo-de-buenos-aires-33825-music.mp3	\N
34165	the-buenos-aires-city-council-33827-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	the-buenos-aires-city-council-33827-music-275	\N	2026-01-11 16:22:44.836975-03	2026-01-11 16:22:44.836976-03	100	f	1986471	0	0	3	0	\N	\N	\N	f	\N	the-buenos-aires-city-council-33827-music.mp3	\N
34207	c-mara-municipal-de-buenos-aires-33828.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	c-mara-municipal-de-buenos-aires-33828-276	\N	2026-01-11 16:23:31.498577-03	2026-01-11 16:23:31.498578-03	100	f	2849001	0	0	3	0	\N	\N	\N	f	\N	c-mara-municipal-de-buenos-aires-33828.mp3	\N
815	museo-secreto-411.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	museo-secreto-411-220	\N	2025-11-17 15:03:29.278571-03	2025-11-17 15:03:29.486974-03	100	f	744773	0	0	\N	73000	\N	\N	\N	f		museo-secreto-411.mp3	\N
869	Lam-The-Jungle-thumb.jpg	\N	\N	\N	image/jpeg	\N	\N	media	lam-the-jungle-thumb-228	\N	2025-11-28 12:10:17.697511-03	2025-11-28 12:10:17.697518-03	100	f	725383	714	1188	\N	0	\N	\N	\N	f		Lam-The-Jungle-thumb.jpg	\N
874	odili.png	\N	\N	\N	image/png	\N	\N	media	odili-229	\N	2025-11-28 12:19:24.321955-03	2025-11-28 12:19:24.321958-03	100	t	2219201	948	1422	\N	0	\N	\N	\N	f		odili.png	\N
804	alberto-churba--dise-o-infinito-799.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	alberto-churba--dise-o-infinito-799-212	\N	2025-11-11 22:00:25.864127-03	2025-11-12 11:33:17.864102-03	100	f	321019	0	0	\N	9000	\N	\N	\N	f		alberto-churba--dise-o-infinito-799.mp3	\N
535	Bellasartes_logo.png	bellasartes-logo-png	\N	\N	image/png	\N	\N	media	bellasartes_logo-163	\N	2025-08-29 08:48:04.94776-03	2025-09-02 17:28:34.380823-03	100	t	8953	60	525	1	0	\N	\N	\N	f	\N	Bellasartes_logo.png	\N
317	manet-La Nymphe surprise.jpg	manet-la-nymphe-surprise-jpg	\N	\N	image/jpeg	\N	\N	media	manet-la-nymphe-surprise-112	\N	2025-05-19 14:05:00.073789-03	2025-09-02 17:28:34.537292-03	100	t	1052179	2594	1999	1	0	\N	\N	\N	f	\N	manet-La Nymphe surprise.jpg	\N
321	pissarro-femme-aux-camps.jpg	pissarro-femme-aux-camps-jpg	\N	\N	image/jpeg	\N	\N	media	pissarro-femme-aux-camps-116	\N	2025-05-19 14:05:06.531826-03	2025-09-02 17:28:34.547738-03	100	t	2154642	3544	1851	1	0	\N	\N	\N	f	\N	pissarro-femme-aux-camps.jpg	\N
319	van-gogh-Le Moulin de la Galette.jpg	van-gogh-le-moulin-de-la-galette-jpg	\N	\N	image/jpeg	\N	\N	media	van-gogh-le-moulin-de-la-galette-114	\N	2025-05-19 14:05:05.967549-03	2025-09-02 17:28:34.550979-03	100	t	2912483	2773	2272	1	0	\N	\N	\N	f	\N	van-gogh-Le Moulin de la Galette.jpg	\N
808	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-215	\N	2025-11-13 13:30:21.579222-03	2025-11-13 13:30:43.582412-03	100	f	462762	0	0	\N	105000	\N	\N	\N	f		sendas-perdidas-458.mp3	\N
879	moma-collection.png	\N	\N	\N	image/png	\N	\N	media	moma-collection-230	\N	2025-11-28 21:36:07.00466-03	2025-11-28 21:36:07.004665-03	100	t	3728681	659	1986	\N	0	\N	\N	\N	f		moma-collection.png	\N
889	Lam-The-Jungle-thumb.jpg	\N	\N	\N	image/jpeg	\N	\N	media	lam-the-jungle-thumb-231	\N	2025-11-29 07:40:33.867089-03	2025-11-29 07:40:33.867091-03	100	f	725383	714	1188	\N	0	\N	\N	\N	f		Lam-The-Jungle-thumb.jpg	\N
919	logo-mq.png	\N	\N	\N	image/png	\N	\N	media	logo-mq-242	\N	2025-12-01 10:37:12.891217-03	2025-12-01 10:37:12.89122-03	100	t	57480	511	1064	\N	0	\N	\N	\N	f		logo-mq.png	\N
1730	tolomei.jpg	\N	\N	\N	image/jpeg	\N	\N	media	tolomei-251	\N	2025-12-22 13:28:57.842626-03	2025-12-22 13:28:57.842628-03	100	f	23059	288	288	\N	0	\N	\N	\N	f		tolomei.jpg	\N
22521	the-jungle-901.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	the-jungle-901-259	\N	2026-01-06 15:11:45.499829-03	2026-01-06 15:11:45.499833-03	100	f	1672709	0	0	3	132000	\N	\N	\N	f		the-jungle-901.mp3	\N
34122	el-cabildo-de-buenos-aires-33825-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	el-cabildo-de-buenos-aires-33825-music-273	\N	2026-01-11 16:20:48.880477-03	2026-01-11 16:20:48.880478-03	100	f	1904551	0	0	3	0	\N	\N	\N	f	\N	el-cabildo-de-buenos-aires-33825-music.mp3	\N
34164	the-buenos-aires-city-council-33827.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	the-buenos-aires-city-council-33827-274	\N	2026-01-11 16:21:58.215567-03	2026-01-11 16:21:58.215567-03	100	f	2556218	0	0	3	0	\N	\N	\N	f	\N	the-buenos-aires-city-council-33827.mp3	\N
50377	museo-secreto-411-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	museo-secreto-411-music-291	\N	2026-02-10 22:29:01.578004-03	2026-02-10 22:29:01.57801-03	100	f	689254	0	0	3	43000	\N	\N	\N	f		museo-secreto-411-music.mp3	\N
50442	20260211_111909.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260211_111909-298	\N	2026-02-11 14:51:44.128126-03	2026-02-11 14:51:44.128128-03	100	t	1460524	0	0	3	0	\N	\N	\N	f	\N	20260211_111909.jpg	\N
50457	legacies-of-doing-50445.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	legacies-of-doing-50445-304	\N	2026-02-11 16:44:24.322781-03	2026-02-11 16:44:24.322785-03	100	f	1475438	0	0	3	0	\N	\N	\N	f	\N	legacies-of-doing-50445.mp3	\N
50471	test-campanoli-pt.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-campanoli-pt-313	\N	2026-02-11 21:18:11.070486-03	2026-02-11 21:18:11.070491-03	100	f	3601018	0	0	3	444000	\N	\N	\N	f		test-campanoli-pt.mp3	\N
50472	test-campanoli-pt.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-campanoli-pt-314	\N	2026-02-11 21:19:28.496722-03	2026-02-11 21:19:28.496732-03	100	f	3601018	0	0	3	444000	\N	\N	\N	f		test-campanoli-pt.mp3	\N
50495	04Beethoven_PianoSonata14InCSharpMinorOp.27_2_moonlight_-1.AdagioSostenuto.mp3	\N	\N	\N	audio/mp3	\N	\N	media	04beethoven_pianosonata14incsharpminorop.27_2_moonlight_-1.adagiosostenuto-328	\N	2026-02-12 10:26:52.845292-03	2026-02-12 10:26:52.845295-03	100	f	7403880	0	0	3	370000	\N	\N	\N	f		04Beethoven_PianoSonata14InCSharpMinorOp.27_2_moonlight_-1.AdagioSostenuto.mp3	\N
50496	06. Piano Sonata No. 8 in C Minor, Op. 13 -Pathétique-- 2. Adagio cantabile.mp3	\N	\N	\N	audio/mp3	\N	\N	media	06.-piano-sonata-no.-8-in-c-minor-op.-13--pathe-tique---2.-adagio-cantabile-329	\N	2026-02-12 10:31:55.399555-03	2026-02-12 10:31:55.399558-03	100	f	6654686	0	0	3	297000	\N	\N	\N	f		06. Piano Sonata No. 8 in C Minor, Op. 13 -Pathétique-- 2. Adagio cantabile.mp3	\N
50498	Respighi O., Danses antiques, 3e suite pour orchestre à cordes.mp3	\N	\N	\N	audio/mp3	\N	\N	media	respighi-o.-danses-antiques-3e-suite-pour-orchestre-a--cordes-330	\N	2026-02-12 10:59:27.913699-03	2026-02-12 10:59:27.913707-03	100	f	25513350	0	0	3	1220000	\N	\N	\N	f		Respighi O., Danses antiques, 3e suite pour orchestre à cordes.mp3	\N
50500	Boccherini L., Quintettino.mp3	\N	\N	\N	audio/mp3	\N	\N	media	boccherini-l.-quintettino-331	\N	2026-02-12 11:00:21.856212-03	2026-02-12 11:00:21.856216-03	100	f	15443886	0	0	3	753000	\N	\N	\N	f		Boccherini L., Quintettino.mp3	\N
50503	Pachebel - Canon et gigue en re mineur pour 3 violons-bc.mp3	\N	\N	\N	audio/mp3	\N	\N	media	pachebel---canon-et-gigue-en-re-mineur-pour-3-violons-bc-334	\N	2026-02-12 11:02:01.858636-03	2026-02-12 11:02:01.858641-03	100	f	5725450	0	0	3	251000	\N	\N	\N	f		Pachebel - Canon et gigue en re mineur pour 3 violons-bc.mp3	\N
50504	Albinoni T., Adagio en sol mineur.mp3	\N	\N	\N	audio/mp3	\N	\N	media	albinoni-t.-adagio-en-sol-mineur-335	\N	2026-02-12 11:02:55.244608-03	2026-02-12 11:02:55.24461-03	100	f	12578312	0	0	3	0	\N	\N	\N	f	\N	Albinoni T., Adagio en sol mineur.mp3	\N
50506	01 András Schiff, Sándor Végh, Camerata Academica des Mozarteums Salzburg — Mozart_ Piano Concerto No. 26 in D Major, K. 537 _Coronation_ - 1. Allegro.mp3	\N	\N	\N	audio/mp3	\N	\N	media	01-andra-s-schiff-sa-ndor-ve-gh-camerata-academica-des-mozarteums-salzburg---mozart_-piano-concerto--336	\N	2026-02-12 11:07:42.540851-03	2026-02-12 11:07:42.540858-03	100	f	35264305	0	0	3	882000	\N	\N	\N	f		01 András Schiff, Sándor Végh, Camerata Academica des Mozarteums Salzburg — Mozart_ Piano Concerto No. 26 in D Major, K. 537 _Coronation_ - 1. Allegro.mp3	\N
50508	02 András Schiff, Sándor Végh, Camerata Academica des Mozarteums Salzburg — Mozart_ Piano Concerto No. 26 in D Major, K. 537 _Coronation_ - 2. (Larghetto).mp3	\N	\N	\N	audio/mp3	\N	\N	media	02-andra-s-schiff-sa-ndor-ve-gh-camerata-academica-des-mozarteums-salzburg---mozart_-piano-concerto--337	\N	2026-02-12 11:09:37.086925-03	2026-02-12 11:09:37.086927-03	100	f	13254174	0	0	3	331000	\N	\N	\N	f		02 András Schiff, Sándor Végh, Camerata Academica des Mozarteums Salzburg — Mozart_ Piano Concerto No. 26 in D Major, K. 537 _Coronation_ - 2. (Larghetto).mp3	\N
50510	01- Concerto No. 1 - Moderato.mp3	\N	\N	\N	audio/mp3	\N	\N	media	01--concerto-no.-1---moderato-338	\N	2026-02-12 11:11:02.300544-03	2026-02-12 11:11:02.30055-03	100	f	27274246	0	0	3	0	\N	\N	\N	f	\N	01- Concerto No. 1 - Moderato.mp3	\N
50512	02- Concerto No. 1 - Adagio.mp3	\N	\N	\N	audio/mp3	\N	\N	media	02--concerto-no.-1---adagio-339	\N	2026-02-12 11:11:47.847398-03	2026-02-12 11:11:47.847404-03	100	f	20616156	0	0	3	0	\N	\N	\N	f	\N	02- Concerto No. 1 - Adagio.mp3	\N
816	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-221	\N	2025-11-19 16:17:31.111812-03	2025-11-19 16:17:31.617544-03	100	f	502375	0	0	\N	126000	\N	\N	\N	f		sendas-perdidas-458.mp3	\N
809	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-216	\N	2025-11-13 14:02:03.385651-03	2025-11-13 14:02:03.888352-03	100	f	447201	0	0	\N	49000	\N	\N	\N	f		sendas-perdidas-458.mp3	\N
906	wifredo-lam,-cuando-no-duermo,-sue-o-894.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	wifredo-lam,-cuando-no-duermo,-sue-o-894-232	\N	2025-11-30 14:57:24.829115-03	2025-11-30 14:57:24.829119-03	100	f	1124345	0	0	\N	0	\N	\N	\N	f	Error -> 240 secs | 4684.770833333333 bytes/sec	wifredo-lam,-cuando-no-duermo,-sue-o-894.mp3	\N
558	iglesia-del-pilar-recoleta-pano-1500x610.jpg	iglesia-del-pilar-recoleta-pano-1500x610-jpg	\N	\N	image/jpeg	\N	\N	media	iglesia-del-pilar-recoleta-pano-1500x610-170	\N	2025-09-10 18:22:34.367699-03	2025-09-10 18:22:34.437144-03	100	t	252479	610	1500	1	0	\N	\N	\N	f	\N	iglesia-del-pilar-recoleta-pano-1500x610.jpg	\N
559	museo-quinquela-martin1500x610-24.jpg	museo-quinquela-martin1500x610-24-jpg	\N	\N	image/jpeg	\N	\N	media	museo-quinquela-martin1500x610-24-171	\N	2025-09-10 18:24:50.233503-03	2025-09-10 18:24:50.303667-03	100	t	178941	610	1500	1	0	\N	\N	\N	f	\N	museo-quinquela-martin1500x610-24.jpg	\N
620	divan.jpg	divan-jpg	\N	\N	image/jpeg	\N	\N	media	divan-186	\N	2025-09-24 11:34:01.607671-03	2025-09-24 11:34:01.673732-03	100	t	131775	570	768	1	0	\N	\N	\N	f	\N	divan.jpg	\N
640	barredas-circo.jpg	barredas-circo-jpg	\N	\N	image/jpeg	\N	\N	media	barredas-circo-191	\N	2025-10-03 17:48:37.416316-03	2025-10-03 17:48:37.507906-03	100	t	204273	714	750	\N	0	\N	\N	\N	f	\N	barredas-circo.jpg	\N
779	01ALBERTO-CHURBA-EXPOSICION-1-1-768x512.jpg	\N	\N	\N	image/jpeg	\N	\N	media	01alberto-churba-exposicion-1-1-768x512-205	\N	2025-10-29 13:33:30.878597-03	2025-10-29 13:33:30.941812-03	100	f	67543	512	768	\N	0	\N	\N	\N	f	\N	01ALBERTO-CHURBA-EXPOSICION-1-1-768x512.jpg	\N
780	sillon-cinta.jpg	\N	\N	\N	image/jpeg	\N	\N	media	sillon-cinta-206	\N	2025-10-29 13:35:18.957927-03	2025-10-29 13:35:19.021391-03	100	f	39077	810	1440	\N	0	\N	\N	\N	f	\N	sillon-cinta.jpg	\N
531	MNBA_fachada.jpg	mnba-fachada-jpg	\N	\N	image/jpeg	\N	\N	media	mnba_fachada-159	\N	2025-08-28 13:27:26.719745-03	2025-09-02 17:28:34.369327-03	100	t	185426	489	1200	1	0	\N	\N	\N	f	\N	MNBA_fachada.jpg	\N
306	sin-pan-y-sin-trabajo.jpg	sin-pan-y-sin-trabajo-jpg	\N	\N	image/jpeg	\N	\N	media	sin-pan-y-sin-trabajo-106	\N	2025-05-19 12:53:47.43493-03	2025-09-02 17:28:34.458968-03	100	t	1604125	2251	4000	1	0	\N	\N	\N	f	\N	sin-pan-y-sin-trabajo.jpg	\N
491	procesion.jpg	procesion-jpg	\N	\N	image/jpeg	\N	\N	media	procesion-156	\N	2025-07-28 12:01:23.906137-03	2025-09-02 17:28:34.466325-03	100	t	2240912	1540	2504	1	0	\N	\N	\N	f	\N	procesion.jpg	\N
463	sendas-perdidas.jpg	sendas-perdidas-jpg	\N	\N	image/jpeg	\N	\N	media	sendas-perdidas-151	\N	2025-07-25 19:40:29.189818-03	2025-09-02 17:28:34.386105-03	100	t	1894439	1676	2048	1	0	\N	\N	\N	f	\N	sendas-perdidas.jpg	\N
301	la-vuelta-del-malon.jpg	la-vuelta-del-malon-jpg	\N	\N	image/jpeg	\N	\N	media	la-vuelta-del-malon-101	\N	2025-05-19 12:53:35.060014-03	2025-09-02 17:28:34.447773-03	100	t	2597596	3118	4979	1	0	\N	\N	\N	f	\N	la-vuelta-del-malon.jpg	\N
1104	museu-secreto-735.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	museu-secreto-735-243	\N	2025-12-08 12:11:34.464538-03	2025-12-08 12:11:34.464556-03	100	f	838670	0	0	\N	129000	\N	\N	\N	f		museu-secreto-735.mp3	\N
1107	secret-museum-734.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	secret-museum-734-245	\N	2025-12-08 12:19:24.502489-03	2025-12-08 12:19:24.502491-03	100	f	738764	0	0	\N	0	\N	\N	\N	f	\N	secret-museum-734.mp3	\N
563	logo-fortabat.svg	logo-fortabat-svg	\N	\N	image/svg	\N	\N	media	logo-fortabat-174	\N	2025-09-12 14:13:40.798216-03	2025-09-12 14:13:40.798223-03	100	f	2884	0	0	1	0	\N	\N	\N	f	\N	logo-fortabat.svg	\N
12516	Malembo.jpg	\N	\N	\N	image/jpeg	\N	\N	media	malembo-252	\N	2025-12-26 16:10:13.711287-03	2025-12-26 16:10:13.711291-03	100	f	1159349	2000	1660	\N	0	\N	\N	\N	f		Malembo.jpg	\N
23041	zambezia-zambezia-1950.jpg	\N	\N	\N	image/jpeg	\N	\N	media	zambezia-zambezia-1950-260	\N	2026-01-07 08:28:37.815067-03	2026-01-07 08:28:37.815068-03	100	f	25382	490	422	3	0	\N	\N	\N	f		zambezia-zambezia-1950.jpg	\N
34208	c-mara-municipal-de-buenos-aires-33828-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	c-mara-municipal-de-buenos-aires-33828-music-277	\N	2026-01-11 16:23:49.964405-03	2026-01-11 16:23:49.964406-03	100	f	2188345	0	0	3	137000	\N	\N	\N	f		c-mara-municipal-de-buenos-aires-33828-music.mp3	\N
50378	museo-secreto-411-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	museo-secreto-411-music-292	\N	2026-02-11 07:46:59.003524-03	2026-02-11 07:46:59.003526-03	100	f	545058	0	0	3	0	\N	\N	\N	f	\N	museo-secreto-411-music.mp3	\N
50451	legados-del-hacer-50443.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	legados-del-hacer-50443-299	\N	2026-02-11 15:19:58.573183-03	2026-02-11 15:19:58.573186-03	100	f	1856958	0	0	3	0	\N	\N	\N	f	Error -> 417 secs | 4453.136690647482 bytes/sec	legados-del-hacer-50443.mp3	\N
50458	legacies-of-doing-50445.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	legacies-of-doing-50445-305	\N	2026-02-11 16:51:57.056446-03	2026-02-11 16:51:57.05646-03	100	f	1574374	0	0	3	168000	\N	\N	\N	f		legacies-of-doing-50445.mp3	\N
50459	legacies-of-doing-50445.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	legacies-of-doing-50445-306	\N	2026-02-11 16:54:08.713746-03	2026-02-11 16:54:08.71375-03	100	f	1246028	0	0	3	151000	\N	\N	\N	f		legacies-of-doing-50445.mp3	\N
50473	test-rosas-pt.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-rosas-pt-315	\N	2026-02-11 21:28:01.801679-03	2026-02-11 21:28:01.801684-03	100	f	1749204	0	0	3	110000	\N	\N	\N	f		test-rosas-pt.mp3	\N
50474	test-anais-fr.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-anais-fr-316	\N	2026-02-11 21:28:22.649123-03	2026-02-11 21:28:22.64913-03	100	f	1314834	0	0	3	0	\N	\N	\N	f	Error -> 328 secs | 4008.640243902439 bytes/sec	test-anais-fr.mp3	\N
50475	test-clara-es.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-clara-es-317	\N	2026-02-11 21:28:51.256937-03	2026-02-11 21:28:51.256945-03	100	f	4076527	0	0	3	0	\N	\N	\N	f	\N	test-clara-es.mp3	\N
50476	test-tamsin-en.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-tamsin-en-318	\N	2026-02-11 21:29:24.641868-03	2026-02-11 21:29:24.641873-03	100	f	1246028	0	0	3	0	\N	\N	\N	f	\N	test-tamsin-en.mp3	\N
50477	test-mariana2-es.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-mariana2-es-319	\N	2026-02-11 21:31:26.640684-03	2026-02-11 21:31:26.64069-03	100	f	1994437	0	0	3	0	\N	\N	\N	f	\N	test-mariana2-es.mp3	\N
50481	test-antoine-fr.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-antoine-fr-323	\N	2026-02-11 21:35:17.748579-03	2026-02-11 21:35:17.748583-03	100	f	1298139	0	0	3	0	\N	\N	\N	f	\N	test-antoine-fr.mp3	\N
50482	test-jonathan-en.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-jonathan-en-324	\N	2026-02-11 21:36:08.053209-03	2026-02-11 21:36:08.053211-03	100	f	164720	0	0	3	0	\N	\N	\N	f	\N	test-jonathan-en.mp3	\N
50483	test-leon-ger.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-leon-ger-325	\N	2026-02-11 21:37:04.365943-03	2026-02-11 21:37:04.365945-03	100	f	197739	0	0	3	0	\N	\N	\N	f	\N	test-leon-ger.mp3	\N
50484	test-nigel-en.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-nigel-en-326	\N	2026-02-11 21:38:11.874619-03	2026-02-11 21:38:11.874621-03	100	f	92786	0	0	3	0	\N	\N	\N	f	\N	test-nigel-en.mp3	\N
50485	test-shaun-en.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-shaun-en-327	\N	2026-02-11 21:39:12.238648-03	2026-02-11 21:39:12.238649-03	100	f	200665	0	0	3	0	\N	\N	\N	f	\N	test-shaun-en.mp3	\N
50514	01 Rudolf Baumgartner Festival Strings Lucerne-Antonio Vivaldi — Concerto No. 4 in F Minor _LInverno__ I. Allegro non moltoNo. 4 in F Minor _LInverno__ I. Allegro non molto.mp3	\N	\N	\N	audio/mp3	\N	\N	media	01-rudolf-baumgartner-festival-strings-lucerne-antonio-vivaldi---concerto-no.-4-in-f-minor-_linverno-341	\N	2026-02-12 12:03:12.897919-03	2026-02-12 12:03:12.897921-03	100	f	9055129	0	0	3	0	\N	\N	\N	f	\N	01 Rudolf Baumgartner Festival Strings Lucerne-Antonio Vivaldi — Concerto No. 4 in F Minor _LInverno__ I. Allegro non moltoNo. 4 in F Minor _LInverno__ I. Allegro non molto.mp3	\N
817	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-222	\N	2025-11-19 16:27:42.964744-03	2025-11-19 16:27:43.100957-03	100	f	443915	0	0	\N	31000	\N	\N	\N	f		sendas-perdidas-458.mp3	\N
907	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	wifredo-lam,-cuando-no-duermo,-sue-o-894-music-233	\N	2025-11-30 14:59:01.297522-03	2025-11-30 14:59:01.297525-03	100	f	5249179	0	0	\N	0	\N	\N	\N	f	\N	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N
908	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	wifredo-lam,-cuando-no-duermo,-sue-o-894-music-234	\N	2025-11-30 15:01:11.604176-03	2025-11-30 15:01:11.604179-03	100	f	5249179	0	0	\N	0	\N	\N	\N	f	\N	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N
1106	utopia-del-sur-488.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	utopia-del-sur-488-244	\N	2025-12-08 12:17:11.800093-03	2025-12-08 12:17:11.800096-03	100	f	490952	0	0	\N	0	\N	\N	\N	f	\N	utopia-del-sur-488.mp3	\N
812	sendas-perdidas-458.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	sendas-perdidas-458-217	\N	2025-11-16 19:52:45.748554-03	2025-11-16 19:52:45.929498-03	100	f	485574	0	0	\N	45000	\N	\N	\N	f		sendas-perdidas-458.mp3	\N
790	viaggio-in-italia-spa.mp3	\N	\N	\N	audio/mp3	\N	\N	media	viaggio-in-italia-spa-208	\N	2025-10-30 14:56:07.85288-03	2025-11-12 11:33:17.855448-03	100	f	1723708	0	0	\N	108000	\N	\N	\N	f		viaggio-in-italia-spa.mp3	\N
576	femme-champs.jpg	femme-champs-jpg	\N	\N	image/jpeg	\N	\N	media	femme-champs-177	\N	2025-09-19 00:09:08.62643-03	2025-09-19 00:09:08.708713-03	100	t	2154642	3544	1851	1	0	\N	\N	\N	f	\N	femme-champs.jpg	\N
598	carrie-bencardino-1.jpg	carrie-bencardino-1-jpg	\N	\N	image/jpeg	\N	\N	media	carrie-bencardino-1-182	\N	2025-09-19 21:27:21.095501-03	2025-09-19 21:27:21.171658-03	100	t	126693	310	560	1	0	\N	\N	\N	f	\N	carrie-bencardino-1.jpg	\N
13112	museo-secreto-411-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	museo-secreto-411-music-253	\N	2025-12-27 09:05:37.591113-03	2025-12-27 09:05:37.591125-03	100	f	769084	0	0	\N	48000	\N	\N	\N	f		museo-secreto-411-music.mp3	\N
29025	invasiones-inglesas.jpeg	\N	\N	\N	image/jpeg	\N	\N	media	invasiones-inglesas-261	\N	2026-01-11 09:23:40.837624-03	2026-01-11 09:23:40.837627-03	100	f	1371047	2829	3454	3	0	\N	\N	\N	f		invasiones-inglesas.jpeg	\N
35197	20260110_134326.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_134326-278	\N	2026-01-12 10:41:14.67399-03	2026-01-12 10:41:14.673993-03	100	f	1542933	1800	4000	3	0	\N	\N	\N	f		20260110_134326.jpg	\N
42257	maca.webp	\N	\N	\N	image/webp	\N	\N	media	maca-284	\N	2026-01-21 04:50:37.365095-03	2026-01-21 04:50:37.365105-03	100	f	823790	0	0	3	0	\N	\N	\N	f	\N	maca.webp	\N
42508	maca.webp	\N	\N	\N	image/webp	\N	\N	media	maca-285	\N	2026-01-21 05:41:03.060067-03	2026-01-21 05:41:03.06007-03	100	f	823790	0	0	3	0	\N	\N	\N	f	\N	maca.webp	\N
50336	la-emperatriz-theodora-accesible-50282.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	la-emperatriz-theodora-accesible-50282-288	\N	2026-02-08 20:26:52.600368-03	2026-02-08 20:26:52.600371-03	100	f	5861552	0	0	3	723000	\N	\N	\N	f		la-emperatriz-theodora-accesible-50282.mp3	\N
50392	Victoria_Ocampo.jpg	\N	\N	\N	image/jpeg	\N	\N	media	victoria_ocampo-293	\N	2026-02-11 09:08:44.53222-03	2026-02-11 09:08:44.532225-03	100	t	395087	751	1106	3	0	\N	\N	\N	f		Victoria_Ocampo.jpg	\N
309	quiosco-de-canaletas.jpg	quiosco-de-canaletas-jpg	\N	\N	image/jpeg	\N	\N	media	quiosco-de-canaletas-107	\N	2025-05-19 12:53:48.924719-03	2025-09-02 17:28:34.454518-03	100	t	213641	577	750	1	0	\N	\N	\N	f	\N	quiosco-de-canaletas.jpg	\N
534	Escuela-y-museo-BQM-768x605.jpg	escuela-y-museo-bqm-768x605-jpg	\N	\N	image/jpeg	\N	\N	media	escuela-y-museo-bqm-768x605-162	\N	2025-08-28 21:38:59.934379-03	2025-09-02 17:28:34.454965-03	100	t	97970	605	768	1	0	\N	\N	\N	f	\N	Escuela-y-museo-BQM-768x605.jpg	\N
312	quiosco-de-canaletas.jpg	quiosco-de-canaletas-jpg	\N	\N	image/jpeg	\N	\N	media	quiosco-de-canaletas-108	\N	2025-05-19 13:08:46.307485-03	2025-09-02 17:28:34.520816-03	100	t	213641	577	750	1	0	\N	\N	\N	f	\N	quiosco-de-canaletas.jpg	\N
331	gaugin-Comentario sobre Vahine no te miti.jpg	gaugin-comentario-sobre-vahine-no-te-miti-jpg	\N	\N	image/jpeg	\N	\N	media	gaugin-comentario-sobre-vahine-no-te-miti-122	\N	2025-05-19 14:07:30.710928-03	2025-09-02 17:28:34.626734-03	100	t	2153679	3135	2495	1	0	\N	\N	\N	f	\N	gaugin-Comentario sobre Vahine no te miti.jpg	\N
357	malba.jpg	malba-jpg	\N	\N	image/jpeg	\N	\N	media	malba-129	\N	2025-06-08 23:32:47.085864-03	2025-09-02 17:28:34.626956-03	100	t	929219	1500	2000	1	0	\N	\N	\N	f	\N	malba.jpg	\N
358	mnba.jpg	mnba-jpg	\N	\N	image/jpeg	\N	\N	media	mnba-130	\N	2025-06-08 23:32:56.174359-03	2025-09-02 17:28:34.626865-03	100	t	533937	1365	2048	1	0	\N	\N	\N	f	\N	mnba.jpg	\N
359	quinquela.jpg	quinquela-jpg	\N	\N	image/jpeg	\N	\N	media	quinquela-131	\N	2025-06-08 23:35:16.284274-03	2025-09-02 17:28:34.627917-03	100	t	12635	199	253	1	0	\N	\N	\N	f	\N	quinquela.jpg	\N
363	arte-argentino.jpg	arte-argentino-jpg	\N	\N	image/jpeg	\N	\N	media	arte-argentino-132	\N	2025-06-09 19:30:29.515396-03	2025-09-02 17:28:34.628922-03	100	t	657966	1067	2000	1	0	\N	\N	\N	f	\N	arte-argentino.jpg	\N
367	museo-secreto.jpg	museo-secreto-jpg	\N	\N	image/jpeg	\N	\N	media	museo-secreto-136	\N	2025-06-10 10:48:25.570969-03	2025-09-02 17:28:34.699068-03	100	t	556211	1365	2047	1	0	\N	\N	\N	f	\N	museo-secreto.jpg	\N
416	sorolla.jpg	sorolla-jpg	\N	\N	image/jpeg	\N	\N	media	sorolla-139	\N	2025-06-10 15:39:32.077799-03	2025-09-02 17:28:34.699593-03	100	t	507011	1332	2048	1	0	\N	\N	\N	f	\N	sorolla.jpg	\N
365	museo-secreto.jpg	museo-secreto-jpg	\N	\N	image/jpeg	\N	\N	media	museo-secreto-134	\N	2025-06-09 19:30:42.542453-03	2025-09-02 17:28:34.700011-03	100	t	446108	2015	1100	1	0	\N	\N	\N	f	\N	museo-secreto.jpg	\N
415	rosas.jpg	rosas-jpg	\N	\N	image/jpeg	\N	\N	media	rosas-138	\N	2025-06-10 15:39:31.66896-03	2025-09-02 17:28:34.701326-03	100	t	597943	2048	1651	1	0	\N	\N	\N	f	\N	rosas.jpg	\N
50402	Gemini_Generated_Image_si5dc2si5dc2si5d.png	\N	\N	\N	image/png	\N	\N	media	gemini_generated_image_si5dc2si5dc2si5d-294	\N	2026-02-11 09:23:32.514958-03	2026-02-11 09:23:32.51496-03	100	t	6933889	1696	2496	3	0	\N	\N	\N	f		Gemini_Generated_Image_si5dc2si5dc2si5d.png	\N
417	theodora.jpg	theodora-jpg	\N	\N	image/jpeg	\N	\N	media	theodora-140	\N	2025-06-10 15:39:32.341741-03	2025-09-02 17:28:34.701368-03	100	t	446108	2015	1100	1	0	\N	\N	\N	f	\N	theodora.jpg	\N
464	recurso-metodo.jpg	recurso-metodo-jpg	\N	\N	image/jpeg	\N	\N	media	recurso-metodo-152	\N	2025-07-25 19:40:41.776111-03	2025-09-02 17:28:34.701497-03	100	t	690874	1533	2048	1	0	\N	\N	\N	f	\N	recurso-metodo.jpg	\N
50453	legacies-of-doing-50445.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	legacies-of-doing-50445-300	\N	2026-02-11 16:19:26.72215-03	2026-02-11 16:19:26.722154-03	100	f	1847383	0	0	3	191000	\N	\N	\N	f		legacies-of-doing-50445.mp3	\N
50454	legados-del-hacer-50443.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	legados-del-hacer-50443-301	\N	2026-02-11 16:27:00.360699-03	2026-02-11 16:27:00.360708-03	100	f	2049882	0	0	3	0	\N	\N	\N	f	Error -> 443 secs | 4627.273137697517 bytes/sec	legados-del-hacer-50443.mp3	\N
50455	legados-del-hacer-50443.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	legados-del-hacer-50443-302	\N	2026-02-11 16:33:06.171627-03	2026-02-11 16:33:06.171628-03	100	f	1772373	0	0	3	0	\N	\N	\N	f	Error -> 361 secs | 4909.620498614959 bytes/sec	legados-del-hacer-50443.mp3	\N
50463	h-ritages-de-laction-50448.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	h-ritages-de-laction-50448-309	\N	2026-02-11 17:41:11.469674-03	2026-02-11 17:41:11.469743-03	100	f	1314834	0	0	3	0	\N	\N	\N	f	Error -> 328 secs | 4008.640243902439 bytes/sec	h-ritages-de-laction-50448.mp3	\N
50466	retrato-de-juan-manuel-de-rosas-741.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	retrato-de-juan-manuel-de-rosas-741-311	\N	2026-02-11 17:48:49.043218-03	2026-02-11 17:48:49.043226-03	100	f	3601018	0	0	3	444000	\N	\N	\N	f		retrato-de-juan-manuel-de-rosas-741.mp3	\N
50478	test-ernesto-es.mp3	\N	\N	\N	audio/mp3	\N	\N	media	test-ernesto-es-320	\N	2026-02-11 21:32:00.670312-03	2026-02-11 21:32:00.670317-03	100	f	272269	0	0	3	0	\N	\N	\N	f	\N	test-ernesto-es.mp3	\N
601	tercer-ojo-mexico.jpg	tercer-ojo-mexico-jpg	\N	\N	image/jpeg	\N	\N	media	tercer-ojo-mexico-184	\N	2025-09-19 21:52:02.31562-03	2025-09-19 21:52:02.379299-03	100	t	118325	415	750	1	0	\N	\N	\N	f	\N	tercer-ojo-mexico.jpg	\N
320	monet-le-pont-dargenteuil.jpg	monet-le-pont-dargenteuil-jpg	\N	\N	image/jpeg	\N	\N	media	monet-le-pont-dargenteuil-115	\N	2025-05-19 14:05:06.226962-03	2025-09-02 17:28:34.535994-03	100	t	82102	504	846	1	0	\N	\N	\N	f	\N	monet-le-pont-dargenteuil.jpg	\N
772	sillon-cinta.jpg	\N	\N	\N	image/jpeg	\N	\N	media	sillon-cinta-203	\N	2025-10-29 09:31:53.123043-03	2025-10-29 09:31:53.205352-03	100	f	39077	810	1440	\N	0	\N	\N	\N	f	\N	sillon-cinta.jpg	\N
759	viaggio-italia.webp	\N	\N	\N	image/webp	\N	\N	media	viaggio-italia-201	\N	2025-10-28 15:05:10.618374-03	2025-10-28 15:05:10.618375-03	100	f	29436	0	0	\N	0	\N	\N	\N	f	\N	viaggio-italia.webp	\N
751	Museo_arte_decorativo_logo.png	\N	\N	\N	image/png	\N	\N	media	museo_arte_decorativo_logo-197	\N	2025-10-28 14:45:44.37819-03	2025-10-28 14:45:44.442327-03	100	t	17023	216	266	\N	0	\N	\N	\N	f	\N	Museo_arte_decorativo_logo.png	\N
756	museo_arte_decorativo_1200_2.jpg	\N	\N	\N	image/jpeg	\N	\N	media	museo_arte_decorativo_1200_2-199	\N	2025-10-28 14:54:22.616866-03	2025-10-28 14:54:22.676108-03	100	t	152388	489	1200	\N	0	\N	\N	\N	f	\N	museo_arte_decorativo_1200_2.jpg	\N
818	sendas-perdidas-458-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	sendas-perdidas-458-music-223	\N	2025-11-19 16:39:10.992124-03	2025-11-19 16:39:11.209041-03	100	f	5249179	0	0	\N	328000	\N	\N	\N	f		sendas-perdidas-458-music.mp3	\N
462	logo-mnba.png	logo-mnba-png	\N	\N	image/png	\N	\N	media	logo-mnba-150	\N	2025-07-25 19:27:31.965004-03	2025-09-02 17:28:34.370005-03	100	t	6708	94	802	1	0	\N	\N	\N	f	\N	logo-mnba.png	\N
568	Do-Amaral-Abaporu-067-1.jpg	do-amaral-abaporu-067-1-jpg	\N	\N	image/jpeg	\N	\N	media	do-amaral-abaporu-067-1-176	\N	2025-09-18 15:06:25.424901-03	2025-09-18 15:06:25.503139-03	100	t	158504	878	750	1	0	\N	\N	\N	f	\N	Do-Amaral-Abaporu-067-1.jpg	\N
538	Museo_malba_logo.png	museo-malba-logo-png	\N	\N	image/png	\N	\N	media	museo_malba_logo-165	\N	2025-08-29 09:30:02.271399-03	2025-09-02 17:28:34.374516-03	100	t	8728	78	424	1	0	\N	\N	\N	f	\N	Museo_malba_logo.png	\N
536	21-cabildo-800x800px.png	21-cabildo-800x800px-png	\N	\N	image/png	\N	\N	media	21-cabildo-800x800px-164	\N	2025-08-29 09:22:51.0849-03	2025-09-02 17:28:34.375691-03	100	t	14950	800	800	1	0	\N	\N	\N	f	\N	21-cabildo-800x800px.png	\N
638	cunsolo.jpg	cunsolo-jpg	\N	\N	image/jpeg	\N	\N	media	cunsolo-189	\N	2025-10-02 18:34:27.211566-03	2025-10-02 18:34:27.318351-03	100	t	3027507	3116	3604	\N	0	\N	\N	\N	f	\N	cunsolo.jpg	\N
300	abel.jpg	abel-jpg	\N	\N	image/jpeg	\N	\N	media	abel-100	\N	2025-05-19 12:53:18.529644-03	2025-09-02 17:28:11.622213-03	100	t	2454212	2848	4288	1	0	\N	\N	\N	f	\N	abel.jpg	\N
490	utopia-del-sur.jpg	utopia-del-sur-jpg	\N	\N	image/jpeg	\N	\N	media	utopia-del-sur-155	\N	2025-07-28 12:01:22.269836-03	2025-09-02 17:28:34.376015-03	100	t	1056849	2128	2000	1	0	\N	\N	\N	f	\N	utopia-del-sur.jpg	\N
329	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve.jpg	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve-jpg	\N	\N	image/jpeg	\N	\N	media	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve-121	\N	2025-05-19 14:07:30.38622-03	2025-09-02 17:28:34.377465-03	100	t	870974	2400	1960	1	0	\N	\N	\N	f	\N	toulouse-lautrec-en-observation-m-fabre-officier-de-reserve.jpg	\N
530	Cabildo_de_Buenos_Aires_2023.jpg	cabildo-de-buenos-aires-2023-jpg	\N	\N	image/jpeg	\N	\N	media	cabildo_de_buenos_aires_2023-158	\N	2025-08-28 10:02:25.822268-03	2025-09-02 17:28:34.504562-03	100	t	380905	1024	1280	1	0	\N	\N	\N	f	\N	Cabildo_de_Buenos_Aires_2023.jpg	\N
532	procesion.jpg	procesion-jpg	\N	\N	image/jpeg	\N	\N	media	procesion-160	\N	2025-08-28 19:01:15.963413-03	2025-09-02 17:28:34.512219-03	100	t	2240912	1540	2504	1	0	\N	\N	\N	f	\N	procesion.jpg	\N
594	Martins-O-impossivel-119.jpg	martins-o-impossivel-119-jpg	\N	\N	image/jpeg	\N	\N	media	martins-o-impossivel-119-181	\N	2025-09-19 20:09:56.199367-03	2025-09-19 20:09:56.262323-03	100	t	67671	1045	750	1	0	\N	\N	\N	f	\N	Martins-O-impossivel-119.jpg	\N
528	Museo de Arte Latinoaméricano de Buenos Aires	museo-de-arte-latinoam-ricano-de-buenos-aires	\N	\N	malba.jpg	\N	\N	media	malba-157	\N	2025-08-26 14:16:07.097636-03	2025-08-28 13:07:42.700421-03	100	t	329178	0	0	1	0	\N	\N	\N	f	\N	Museo de Arte Latinoaméricano de Buenos Aires	\N
604	el-mal.jpg	el-mal-jpg	\N	\N	image/jpeg	\N	\N	media	el-mal-185	\N	2025-09-21 19:59:51.154748-03	2025-09-21 19:59:51.220017-03	100	t	101814	768	721	1	0	\N	\N	\N	f	\N	el-mal.jpg	\N
752	Museo_Nacional_de_Arte_Decorativo-2.jpg	\N	\N	\N	image/jpeg	\N	\N	media	museo_nacional_de_arte_decorativo-2-198	\N	2025-10-28 14:49:35.309092-03	2025-11-11 22:16:42.686934-03	100	t	901625	1110	2452	\N	0	\N	\N	\N	f	\N	Museo_Nacional_de_Arte_Decorativo-2.jpg	\N
909	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	wifredo-lam-cuando-no-duermo-sue-o-894-music-235	\N	2025-11-30 17:43:29.919197-03	2025-11-30 17:43:29.919203-03	100	f	5249179	0	0	\N	328000	\N	\N	\N	f		wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N
16212	1949_LAM_Je-suis.jpg	\N	\N	\N	image/jpeg	\N	\N	media	1949_lam_je-suis-254	\N	2026-01-04 17:49:50.452533-03	2026-01-04 17:49:50.452535-03	100	f	490088	2000	1750	\N	0	\N	\N	\N	f		1949_LAM_Je-suis.jpg	\N
1490	ai_studio_code.svg	\N	\N	\N	image/svg+xml	\N	\N	media	ai_studio_code-246	\N	2025-12-15 11:16:19.714578-03	2025-12-15 11:16:19.714582-03	100	f	1183	0	0	\N	0	\N	\N	\N	f	\N	ai_studio_code.svg	\N
29336	20260110_135415.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_135415-262	\N	2026-01-11 09:42:13.687307-03	2026-01-11 09:42:13.687311-03	100	f	1703000	1800	4000	3	0	\N	\N	\N	f		20260110_135415.jpg	\N
35502	traje-de-calle-del-alf-rez-real-35357.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	traje-de-calle-del-alf-rez-real-35357-279	\N	2026-01-12 10:42:45.602524-03	2026-01-12 10:42:45.602527-03	100	f	1810524	0	0	3	0	\N	\N	\N	f	\N	traje-de-calle-del-alf-rez-real-35357.mp3	\N
305	reposo.jpg	reposo-jpg	\N	\N	image/jpeg	\N	\N	media	reposo-105	\N	2025-05-19 12:53:45.865734-03	2025-09-02 17:28:34.450806-03	100	t	657966	1067	2000	1	0	\N	\N	\N	f	\N	reposo.jpg	\N
315	forain-Danseuse et admirateur derrière la scène.jpg	forain-danseuse-et-admirateur-derri-re-la-sc-ne-jpg	\N	\N	image/jpeg	\N	\N	media	forain-danseuse-et-admirateur-derri-re-la-sc-ne-110	\N	2025-05-19 14:03:00.808154-03	2025-09-02 17:28:34.524228-03	100	t	1788278	2604	3160	1	0	\N	\N	\N	f	\N	forain-Danseuse et admirateur derrière la scène.jpg	\N
318	degas-La Toilette apres le bain.jpg	degas-la-toilette-apres-le-bain-jpg	\N	\N	image/jpeg	\N	\N	media	degas-la-toilette-apres-le-bain-113	\N	2025-05-19 14:05:05.606782-03	2025-09-02 17:28:34.529228-03	100	t	497166	1725	2000	1	0	\N	\N	\N	f	\N	degas-La Toilette apres le bain.jpg	\N
302	el-despertar-de-la-criada.jpg	el-despertar-de-la-criada-jpg	\N	\N	image/jpeg	\N	\N	media	el-despertar-de-la-criada-102	\N	2025-05-19 12:53:37.800791-03	2025-09-02 17:28:12.176293-03	100	t	1093118	6187	4138	1	0	\N	\N	\N	f	\N	el-despertar-de-la-criada.jpg	\N
314	forain-Danseuse et admirateur derrière la scène.jpg	forain-danseuse-et-admirateur-derri-re-la-sc-ne-jpg	\N	\N	image/jpeg	\N	\N	media	forain-danseuse-et-admirateur-derri-re-la-sc-ne-109	\N	2025-05-19 14:00:28.742066-03	2025-09-02 17:28:34.365234-03	100	t	1788278	2604	3160	1	0	\N	\N	\N	f	\N	forain-Danseuse et admirateur derrière la scène.jpg	\N
35503	traje-de-calle-del-alf-rez-real-35357-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	traje-de-calle-del-alf-rez-real-35357-music-280	\N	2026-01-12 10:43:24.101217-03	2026-01-12 10:43:24.101219-03	100	f	1352426	0	0	3	0	\N	\N	\N	f	\N	traje-de-calle-del-alf-rez-real-35357-music.mp3	\N
466	sendas-perdidas-muestra-2.jpg	sendas-perdidas-muestra-2-jpg	\N	\N	image/jpeg	\N	\N	media	sendas-perdidas-muestra-2-154	\N	2025-07-25 19:50:10.302434-03	2025-09-02 17:28:34.365581-03	100	t	525551	1365	2047	1	0	\N	\N	\N	f	\N	sendas-perdidas-muestra-2.jpg	\N
42893	maca.webp	\N	\N	\N	image/webp	\N	\N	media	maca-286	\N	2026-01-21 09:49:11.570207-03	2026-01-21 09:49:11.570208-03	100	f	823790	0	0	3	0	\N	\N	\N	f	\N	maca.webp	\N
50403	logo-casa-ocampo.jpg	\N	\N	\N	image/jpeg	\N	\N	media	logo-casa-ocampo-295	\N	2026-02-11 09:28:22.98749-03	2026-02-11 09:28:22.987491-03	100	t	18385	0	0	3	0	\N	\N	\N	f	\N	logo-casa-ocampo.jpg	\N
303	en-normandie.jpg	en-normandie-jpg	\N	\N	image/jpeg	\N	\N	media	en-normandie-103	\N	2025-05-19 12:53:42.510528-03	2025-09-02 17:28:34.447772-03	100	t	354419	944	1200	1	0	\N	\N	\N	f	\N	en-normandie.jpg	\N
304	la-vuelta-al-hogar.jpg	la-vuelta-al-hogar-jpg	\N	\N	image/jpeg	\N	\N	media	la-vuelta-al-hogar-104	\N	2025-05-19 12:53:44.209264-03	2025-09-02 17:28:34.44846-03	100	t	796475	1491	2000	1	0	\N	\N	\N	f	\N	la-vuelta-al-hogar.jpg	\N
639	banio-venus.jpg	banio-venus-jpg	\N	\N	image/jpeg	\N	\N	media	banio-venus-190	\N	2025-10-02 18:37:00.677647-03	2025-10-02 18:37:00.74375-03	100	t	2281261	2048	1555	\N	0	\N	\N	\N	f	\N	banio-venus.jpg	\N
548	pilar.JPG	pilar-jpg	\N	\N	image/jpeg	\N	\N	media	pilar-166	\N	2025-08-29 16:49:49.079774-03	2025-09-02 17:28:34.433374-03	100	t	773453	1445	1920	1	0	\N	\N	\N	f	\N	pilar.JPG	\N
533	quinquela.webp	quinquela-webp	\N	\N	image/webp	\N	\N	media	quinquela-161	\N	2025-08-28 21:37:23.026809-03	2025-08-28 21:37:23.026815-03	100	t	217278	0	0	1	0	\N	\N	\N	f	\N	quinquela.webp	\N
364	impresionismo.jpg	impresionismo-jpg	\N	\N	image/jpeg	\N	\N	media	impresionismo-133	\N	2025-06-09 19:30:42.127085-03	2025-09-02 17:28:34.706511-03	100	t	2912483	2773	2272	1	0	\N	\N	\N	f	\N	impresionismo.jpg	\N
366	obras-maestras.jpg	obras-maestras-jpg	\N	\N	image/jpeg	\N	\N	media	obras-maestras-135	\N	2025-06-09 19:30:42.927536-03	2025-09-02 17:28:34.706511-03	100	t	2279851	3757	2622	1	0	\N	\N	\N	f	\N	obras-maestras.jpg	\N
465	apocalipsis.jpg	apocalipsis-jpg	\N	\N	image/jpeg	\N	\N	media	apocalipsis-153	\N	2025-07-25 19:40:42.845304-03	2025-09-02 17:28:34.70748-03	100	t	2025274	2048	1536	1	0	\N	\N	\N	f	\N	apocalipsis.jpg	\N
843	museo-arte.jpg	\N	\N	\N	image/jpeg	\N	\N	media	museo-arte-224	\N	2025-11-24 10:45:05.627232-03	2025-11-24 10:45:05.721794-03	100	f	120077	412	1024	\N	0	\N	\N	\N	f		museo-arte.jpg	\N
910	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	wifredo-lam-cuando-no-duermo-sue-o-894-music-236	\N	2025-11-30 17:44:13.222877-03	2025-11-30 17:44:13.22288-03	100	f	5249179	0	0	\N	0	\N	\N	\N	f	\N	wifredo-lam,-cuando-no-duermo,-sue-o-894-music.mp3	\N
1491	ai_studio_code(2).svg	\N	\N	\N	image/svg+xml	\N	\N	media	ai_studio_code(2)-247	\N	2025-12-15 12:14:46.773054-03	2025-12-15 12:14:46.773057-03	100	f	1183	0	0	\N	0	\N	\N	\N	f	\N	ai_studio_code(2).svg	\N
556	museo-nacional-bellas-artes-1500x610-fachada.jpg	museo-nacional-bellas-artes-1500x610-fachada-jpg	\N	\N	image/jpeg	\N	\N	media	museo-nacional-bellas-artes-1500x610-fachada-168	\N	2025-09-10 18:18:19.182713-03	2025-09-10 18:18:19.264414-03	100	t	128416	610	1500	1	0	\N	\N	\N	f	\N	museo-nacional-bellas-artes-1500x610-fachada.jpg	\N
17171	vangogh1.jpg	\N	\N	\N	image/jpeg	\N	\N	media	vangogh1-255	\N	2026-01-04 18:07:59.869945-03	2026-01-04 18:07:59.869948-03	100	f	1615265	1602	2000	\N	0	\N	\N	\N	f		vangogh1.jpg	\N
557	cabildo-wide.jpg	cabildo-wide-jpg	\N	\N	image/jpeg	\N	\N	media	cabildo-wide-169	\N	2025-09-10 18:19:44.109945-03	2025-09-10 18:19:44.17457-03	100	t	140592	400	1600	1	0	\N	\N	\N	f	\N	cabildo-wide.jpg	\N
781	sillon-cinta.jpg	\N	\N	\N	image/jpeg	\N	\N	media	sillon-cinta-207	\N	2025-10-29 13:38:40.42156-03	2025-10-29 13:38:40.484282-03	100	f	89091	727	1440	\N	0	\N	\N	\N	f	\N	sillon-cinta.jpg	\N
643	flower-4989102_640.jpg	flower-4989102-640-jpg	\N	\N	image/jpeg	\N	\N	avatar	flower-4989102_640-195	\N	2025-10-06 11:27:53.11119-03	2025-11-11 22:18:20.046405-03	100	t	67817	480	640	\N	0	avatar	\N	\N	f	\N	flower-4989102_640.jpg	\N
791	viaggio-in-italia-eng.mp3	\N	\N	\N	audio/mp3	\N	\N	media	viaggio-in-italia-eng-209	\N	2025-10-30 14:57:35.274434-03	2025-11-12 11:33:17.85555-03	100	f	1648058	0	0	\N	103000	\N	\N	\N	f		viaggio-in-italia-eng.mp3	\N
425	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-148	\N	2025-06-13 14:30:02.052468-03	2025-11-12 11:33:17.875737-03	100	t	1724395	0	0	1	144000	\N	\N	\N	f		theodora.mp3	\N
338	reposo.mp3	reposo-mp3	\N	\N	audio/mp3	\N	\N	media	reposo-127	\N	2025-05-20 11:29:21.56577-03	2025-11-12 11:33:17.924614-03	100	t	6210291	0	0	1	304000	\N	\N	\N	f		reposo.mp3	\N
426	theodora.mp3	theodora-mp3	\N	\N	audio/mp3	\N	\N	media	theodora-149	\N	2025-06-13 14:33:41.173778-03	2025-11-12 11:33:17.946583-03	100	t	1724395	0	0	1	144000	\N	\N	\N	f		theodora.mp3	\N
337	reposo.mp3	reposo-mp3	\N	\N	audio/mp3	\N	\N	media	reposo-126	\N	2025-05-19 17:08:05.967738-03	2025-11-12 11:33:18.007135-03	100	t	6210291	0	0	1	304000	\N	\N	\N	f		reposo.mp3	\N
637	reposo.mp3	reposo-mp3	\N	\N	audio/mp3	\N	\N	media	reposo-188	\N	2025-10-02 14:51:25.450064-03	2025-11-12 11:33:18.079722-03	100	t	6210291	0	0	\N	304000	\N	\N	\N	f		reposo.mp3	\N
599	carrie-bencardino-2.jpg	carrie-bencardino-2-jpg	\N	\N	image/jpeg	\N	\N	media	carrie-bencardino-2-183	\N	2025-09-19 21:35:08.389486-03	2025-09-19 21:35:08.473541-03	100	t	80622	641	960	1	0	\N	\N	\N	f	\N	carrie-bencardino-2.jpg	\N
323	toulouse-lautrec-Portrait de Suzanne Valadon.jpg	toulouse-lautrec-portrait-de-suzanne-valadon-jpg	\N	\N	image/jpeg	\N	\N	media	toulouse-lautrec-portrait-de-suzanne-valadon-118	\N	2025-05-19 14:05:07.193106-03	2025-09-02 17:28:34.602897-03	100	t	2137276	2104	1808	1	0	\N	\N	\N	f	\N	toulouse-lautrec-Portrait de Suzanne Valadon.jpg	\N
334	benito-quinquela-martín-puente-de-la-boca.jpg	benito-quinquela-mart-n-puente-de-la-boca-jpg	\N	\N	image/jpeg	\N	\N	media	benito-quinquela-mart-n-puente-de-la-boca-123	\N	2025-05-19 14:07:33.795598-03	2025-09-02 17:28:34.60294-03	100	t	119371	470	569	1	0	\N	\N	\N	f	\N	benito-quinquela-martín-puente-de-la-boca.jpg	\N
322	pissarro-prairires.jpg	pissarro-prairires-jpg	\N	\N	image/jpeg	\N	\N	media	pissarro-prairires-117	\N	2025-05-19 14:05:06.787645-03	2025-09-02 17:28:34.602897-03	100	t	1142828	1187	2000	1	0	\N	\N	\N	f	\N	pissarro-prairires.jpg	\N
327	sisley-effet-de-niege.jpg	sisley-effet-de-niege-jpg	\N	\N	image/jpeg	\N	\N	media	sisley-effet-de-niege-120	\N	2025-05-19 14:07:27.773092-03	2025-09-02 17:28:34.626583-03	100	t	2545840	2094	2560	1	0	\N	\N	\N	f	\N	sisley-effet-de-niege.jpg	\N
325	manet-Portrait d-Ernest Hoschedé et sa fille Marthe.jpg	manet-portrait-d-ernest-hosched-et-sa-fille-marthe-jpg	\N	\N	image/jpeg	\N	\N	media	manet-portrait-d-ernest-hosched--et-sa-fille-marthe-119	\N	2025-05-19 14:07:27.272501-03	2025-09-02 17:28:34.626645-03	100	t	2196334	1890	2560	1	0	\N	\N	\N	f	\N	manet-Portrait d-Ernest Hoschedé et sa fille Marthe.jpg	\N
792	viaggio-in-italia-pt.mp3	\N	\N	\N	audio/mp3	\N	\N	media	viaggio-in-italia-pt-210	\N	2025-10-30 14:58:04.114809-03	2025-11-12 11:33:17.991493-03	100	f	1880443	0	0	\N	118000	\N	\N	\N	f		viaggio-in-italia-pt.mp3	\N
29477	20260110_135630.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_135630-263	\N	2026-01-11 09:43:16.672505-03	2026-01-11 09:43:16.672511-03	100	f	1613985	1816	4032	3	0	\N	\N	\N	f		20260110_135630.jpg	\N
29858	20260110_135049.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260110_135049-264	\N	2026-01-11 09:53:25.681492-03	2026-01-11 09:53:25.681509-03	100	f	1563385	3187	1814	3	0	\N	\N	\N	f		20260110_135049.jpg	\N
37553	ii.jpg	\N	\N	\N	image/jpeg	\N	\N	media	ii-281	\N	2026-01-12 16:07:19.241962-03	2026-01-12 16:07:19.241964-03	100	f	326985	1080	1920	3	0	\N	\N	\N	f		ii.jpg	\N
37908	patio.jpg	\N	\N	\N	image/jpeg	\N	\N	media	patio-282	\N	2026-01-13 04:44:53.588781-03	2026-01-13 04:44:53.588783-03	100	f	1884356	2047	1339	3	0	\N	\N	\N	f		patio.jpg	\N
43193	macap2.png	\N	\N	\N	image/png	\N	\N	media	macap2-287	\N	2026-01-21 10:03:05.659059-03	2026-01-21 10:03:05.659063-03	100	t	1876235	1138	884	3	0	\N	\N	\N	f		macap2.png	\N
50337	la-emperatriz-theodora-accesible-50282-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	la-emperatriz-theodora-accesible-50282-music-289	\N	2026-02-08 20:27:46.772727-03	2026-02-08 20:27:46.772728-03	100	f	3968970	0	0	3	0	\N	\N	\N	f	\N	la-emperatriz-theodora-accesible-50282-music.mp3	\N
50411	miremont.webp	\N	\N	\N	image/webp	\N	\N	media	miremont-296	\N	2026-02-11 09:38:10.945044-03	2026-02-11 09:38:10.945046-03	100	f	5618	0	0	3	0	\N	\N	\N	f	\N	miremont.webp	\N
50464	la-emperatriz-theodora-412.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	la-emperatriz-theodora-412-310	\N	2026-02-11 17:46:27.003026-03	2026-02-11 17:46:27.003031-03	100	f	4076527	0	0	3	0	\N	\N	\N	f	\N	la-emperatriz-theodora-412.mp3	\N
50528	Daniel Barenboim - Chopin - The Complete Nocturnes (CD 1 nos. 1-11) - 04 - fa mag  op. 15 No. 1 Andante cantabile.mp3	\N	\N	\N	audio/mp3	\N	\N	media	daniel-barenboim---chopin---the-complete-nocturnes-cd-1-nos-1-11---04---fa-mag-op-15-no-1-andante-cantabile-352	\N	2026-02-12 14:37:53.629568-03	2026-02-12 14:37:53.62957-03	100	f	5074846	0	0	3	271000	\N	\N	\N	f		Daniel Barenboim - Chopin - The Complete Nocturnes (CD 1 nos. 1-11) - 04 - fa mag  op. 15 No. 1 Andante cantabile.mp3	\N
50530	Daniel Barenboim - Chopin - The Complete Nocturnes (CD 1 nos. 1-11) - 01 - si bem minl op. 9 No. 1 Larghetto.mp3	\N	\N	\N	audio/mp3	\N	\N	media	daniel-barenboim---chopin---the-complete-nocturnes-cd-1-nos-1-11---01---si-bem-minl-op-9-no-1-larghetto-353	\N	2026-02-12 14:39:04.221947-03	2026-02-12 14:39:04.22195-03	100	f	6441534	0	0	3	0	\N	\N	\N	f	\N	Daniel Barenboim - Chopin - The Complete Nocturnes (CD 1 nos. 1-11) - 01 - si bem minl op. 9 No. 1 Larghetto.mp3	\N
50532	Daniel Barenboim - Chopin - The Complete Nocturnes (CD 1 nos. 1-11) - 02 - mi bem mag op. 9 No. 2 Andante.mp3	\N	\N	\N	audio/mp3	\N	\N	media	daniel-barenboim---chopin---the-complete-nocturnes-cd-1-nos-1-11---02---mi-bem-mag-op-9-no-2-andante-354	\N	2026-02-12 14:39:47.678568-03	2026-02-12 14:39:47.67857-03	100	f	4674097	0	0	3	0	\N	\N	\N	f	\N	Daniel Barenboim - Chopin - The Complete Nocturnes (CD 1 nos. 1-11) - 02 - mi bem mag op. 9 No. 2 Andante.mp3	\N
50534	Daniel Barenboim - Chopin - The Complete Nocturnes (CD 1 nos. 1-11) - 06 - sol min op. 15 No. 3 Lento.mp3	\N	\N	\N	audio/mp3	\N	\N	media	daniel-barenboim---chopin---the-complete-nocturnes-cd-1-nos-1-11---06---sol-min-op-15-no-3-lento-355	\N	2026-02-12 14:40:31.355609-03	2026-02-12 14:40:31.35561-03	100	f	4317888	0	0	3	0	\N	\N	\N	f	\N	Daniel Barenboim - Chopin - The Complete Nocturnes (CD 1 nos. 1-11) - 06 - sol min op. 15 No. 3 Lento.mp3	\N
50536	01 András Schiff, Sándor Végh, Camerata Academica des Mozarteums Salzburg — Mozart_ Piano Concerto No. 23 in A major, K.488 - 1. Allegro.mp3	\N	\N	\N	audio/mp3	\N	\N	media	01-andra-s-schiff-sa-ndor-ve-gh-camerata-academica-des-mozarteums-salzburg---mozart_-piano-concerto-no-23-in-a-major-k48-356	\N	2026-02-12 14:44:13.673485-03	2026-02-12 14:44:13.673493-03	100	f	26676759	0	0	3	666000	\N	\N	\N	f		01 András Schiff, Sándor Végh, Camerata Academica des Mozarteums Salzburg — Mozart_ Piano Concerto No. 23 in A major, K.488 - 1. Allegro.mp3	\N
50537	Daniel Barenboim - Chopin - The Complete Nocturnes (CD 1 nos. 1-11) - 06 - sol min op. 15 No. 3 Lento.mp3	\N	\N	\N	audio/mp3	\N	\N	media	daniel-barenboim---chopin---the-complete-nocturnes-cd-1-nos-1-11---06---sol-min-op-15-no-3-lento-357	\N	2026-02-12 14:44:40.876028-03	2026-02-12 14:44:40.876037-03	100	f	4317888	0	0	3	0	\N	\N	\N	f	\N	Daniel Barenboim - Chopin - The Complete Nocturnes (CD 1 nos. 1-11) - 06 - sol min op. 15 No. 3 Lento.mp3	\N
50539	02 András Schiff, Sándor Végh, Camerata Academica des Mozarteums Salzburg — Mozart_ Piano Concerto No. 23 in A major, K.488 - 2. Adagio.mp3	\N	\N	\N	audio/mp3	\N	\N	media	02-andra-s-schiff-sa-ndor-ve-gh-camerata-academica-des-mozarteums-salzburg---mozart_-piano-concerto-no-23-in-a-major-k48-358	\N	2026-02-12 14:45:54.840664-03	2026-02-12 14:45:54.840665-03	100	f	16051766	0	0	3	0	\N	\N	\N	f	\N	02 András Schiff, Sándor Végh, Camerata Academica des Mozarteums Salzburg — Mozart_ Piano Concerto No. 23 in A major, K.488 - 2. Adagio.mp3	\N
50541	03 András Schiff, Sándor Végh, Camerata Academica des Mozarteums Salzburg — Mozart_ Piano Concerto No. 23 in A major, K.488 - 3. Allegro assai.mp3	\N	\N	\N	audio/mp3	\N	\N	media	03-andra-s-schiff-sa-ndor-ve-gh-camerata-academica-des-mozarteums-salzburg---mozart_-piano-concerto-no-23-in-a-major-k48-359	\N	2026-02-12 14:46:46.592725-03	2026-02-12 14:46:46.592727-03	100	f	20573683	0	0	3	0	\N	\N	\N	f	\N	03 András Schiff, Sándor Végh, Camerata Academica des Mozarteums Salzburg — Mozart_ Piano Concerto No. 23 in A major, K.488 - 3. Allegro assai.mp3	\N
50549	03 - Sonata No. 1 - 3rd - Andante.mp3	\N	\N	\N	audio/mp3	\N	\N	media	03---sonata-no-1---3rd---andante-363	\N	2026-02-12 14:55:47.494846-03	2026-02-12 14:55:47.494849-03	100	f	9972872	0	0	3	0	\N	\N	\N	f	\N	03 - Sonata No. 1 - 3rd - Andante.mp3	\N
50551	10 - Sonata No. 6 - 2nd - Largo.mp3	\N	\N	\N	audio/mp3	\N	\N	media	10---sonata-no-6---2nd---largo-364	\N	2026-02-12 14:58:21.622164-03	2026-02-12 14:58:21.622166-03	100	f	5712825	0	0	3	0	\N	\N	\N	f	\N	10 - Sonata No. 6 - 2nd - Largo.mp3	\N
50553	01.02. Concerto In D Major, Op. 101_ Adagio.mp3	\N	\N	\N	audio/mp3	\N	\N	media	0102-concerto-in-d-major-op-101_-adagio-365	\N	2026-02-12 15:08:00.467973-03	2026-02-12 15:08:00.467979-03	100	f	10311634	0	0	3	0	\N	\N	\N	f	\N	01.02. Concerto In D Major, Op. 101_ Adagio.mp3	\N
50555	Concerto No 5 In E Flat  Op 73  Emperor III.mp3	\N	\N	\N	audio/mp3	\N	\N	media	concerto-no-5-in-e-flat-op-73-emperor-iii-369	\N	2026-02-12 15:23:14.765884-03	2026-02-12 15:23:14.765887-03	100	f	24017055	0	0	3	0	\N	\N	\N	f	\N	Concerto No 5 In E Flat  Op 73  Emperor III.mp3	\N
50563	ocampo-1.png	\N	\N	\N	image/png	\N	\N	media	ocampo-1-370	\N	2026-02-12 16:13:31.713518-03	2026-02-12 16:13:31.713538-03	100	t	109415	344	302	3	0	\N	\N	\N	f		ocampo-1.png	\N
50585	ocampo-2.png	\N	\N	\N	image/png	\N	\N	media	ocampo-2-371	\N	2026-02-12 18:12:36.741677-03	2026-02-12 18:12:36.741681-03	100	t	84140	468	370	3	0	\N	\N	\N	f		ocampo-2.png	\N
50586	legados-del-hacer-50443-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	legados-del-hacer-50443-music-372	\N	2026-02-13 00:44:15.338038-03	2026-02-13 00:44:15.338103-03	100	f	948413	0	0	3	59000	\N	\N	\N	f		legados-del-hacer-50443-music.mp3	\N
50588	08Vidala Del Silencio.mp3	\N	\N	\N	audio/mp3	\N	\N	media	08vidala-del-silencio-373	\N	2026-02-13 00:49:56.247912-03	2026-02-13 00:49:56.247916-03	100	f	3020978	0	0	3	193000	\N	\N	\N	f		08Vidala Del Silencio.mp3	\N
50591	07Piedra Y Camino.mp3	\N	\N	\N	audio/mp3	\N	\N	media	07piedra-y-camino-374	\N	2026-02-13 00:51:58.09761-03	2026-02-13 00:51:58.097615-03	100	f	3003535	0	0	3	0	\N	\N	\N	f	\N	07Piedra Y Camino.mp3	\N
50593	01Camino Del Indio.mp3	\N	\N	\N	audio/mp3	\N	\N	media	01camino-del-indio-375	\N	2026-02-13 00:52:52.638659-03	2026-02-13 00:52:52.638664-03	100	f	2293619	0	0	3	0	\N	\N	\N	f	\N	01Camino Del Indio.mp3	\N
50594	legados-del-hacer-50443-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	legados-del-hacer-50443-music-376	\N	2026-02-13 00:55:51.399762-03	2026-02-13 00:55:51.399765-03	100	f	964116	0	0	3	0	\N	\N	\N	f	\N	legados-del-hacer-50443-music.mp3	\N
50595	legados-del-hacer-50443.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice	legados-del-hacer-50443-377	\N	2026-02-13 00:57:11.650591-03	2026-02-13 00:57:11.650593-03	100	f	1202861	0	0	3	0	\N	\N	\N	f	\N	legados-del-hacer-50443.mp3	\N
50596	legados-del-hacer-50443-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	legados-del-hacer-50443-music-378	\N	2026-02-13 00:58:38.687446-03	2026-02-13 00:58:38.687448-03	100	f	955339	0	0	3	0	\N	\N	\N	f	\N	legados-del-hacer-50443-music.mp3	\N
50597	legados-del-hacer-50443-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	legados-del-hacer-50443-music-379	\N	2026-02-13 01:00:40.409572-03	2026-02-13 01:00:40.409574-03	100	f	1003404	0	0	3	0	\N	\N	\N	f	\N	legados-del-hacer-50443-music.mp3	\N
50598	el-cabildo-de-buenos-aires-33825-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	el-cabildo-de-buenos-aires-33825-music-380	\N	2026-02-13 01:06:08.579246-03	2026-02-13 01:06:08.579248-03	100	f	1904535	0	0	3	0	\N	\N	\N	f	\N	el-cabildo-de-buenos-aires-33825-music.mp3	\N
50605	Ludwig_van_Beethoven_-_sonata_no._14_in_c_sharp_minor_'moonlight',_op._27_no._2_-_i._adagio_sostenuto.ogg	\N	\N	\N	video/ogg	\N	\N	media	ludwig_van_beethoven_-_sonata_no_14_in_c_sharp_minor_moonlight_op_27_no_2_-_i_adagio_sostenuto-381	\N	2026-02-13 10:57:45.751381-03	2026-02-13 10:57:45.751386-03	100	f	10863043	0	0	3	0	\N	\N	\N	f	\N	Ludwig_van_Beethoven_-_sonata_no._14_in_c_sharp_minor_'moonlight',_op._27_no._2_-_i._adagio_sostenuto.ogg	\N
50607	Tomaso_Giovanni_Albinoni_-_Adagio_in_G_minor_-_Arr_for_alto_saxophone_and_piano_-_David_Hernando_Vitores.ogg	\N	\N	\N	video/ogg	\N	\N	media	tomaso_giovanni_albinoni_-_adagio_in_g_minor_-_arr_for_alto_saxophone_and_piano_-_david_hernando_vitores-382	\N	2026-02-13 11:01:01.822938-03	2026-02-13 11:01:01.822956-03	100	f	9502728	0	0	3	0	\N	\N	\N	f	\N	Tomaso_Giovanni_Albinoni_-_Adagio_in_G_minor_-_Arr_for_alto_saxophone_and_piano_-_David_Hernando_Vitores.ogg	\N
50615	carrie.jpg	\N	\N	\N	image/jpeg	\N	\N	media	carrie-383	\N	2026-02-14 17:35:47.485466-03	2026-02-14 17:35:47.485475-03	100	t	99846	1080	720	3	0	\N	\N	\N	f		carrie.jpg	\N
50627	Bach_-_Flute_Sonata_Emin_-_3._Andante.ogg	\N	\N	\N	video/ogg	\N	\N	media	bach_-_flute_sonata_emin_-_3_andante-384	\N	2026-02-15 09:47:24.943848-03	2026-02-15 09:47:24.943851-03	100	f	5365109	0	0	3	0	\N	\N	\N	f	\N	Bach_-_Flute_Sonata_Emin_-_3._Andante.ogg	\N
50629	JOHN_MICHEL_CELLO-BACH_AVE_MARIA.ogg	\N	\N	\N	video/ogg	\N	\N	media	john_michel_cello-bach_ave_maria-385	\N	2026-02-15 09:49:37.44916-03	2026-02-15 09:49:37.449167-03	100	f	6455192	0	0	3	0	\N	\N	\N	f	\N	JOHN_MICHEL_CELLO-BACH_AVE_MARIA.ogg	\N
50631	Bach,_Johann_Sebastian_-_Suite_No.2_in_B_Minor_-_X._Badinerie.ogg	\N	\N	\N	video/ogg	\N	\N	media	bach_johann_sebastian_-_suite_no2_in_b_minor_-_x_badinerie-386	\N	2026-02-15 09:56:06.460486-03	2026-02-15 09:56:06.460492-03	100	f	1300879	0	0	3	0	\N	\N	\N	f	\N	Bach,_Johann_Sebastian_-_Suite_No.2_in_B_Minor_-_X._Badinerie.ogg	\N
50633	Enrico_Caruso,_George_Frideric_Handel,_Ombra_mai_fu_(Serse).ogg	\N	\N	\N	video/ogg	\N	\N	media	enrico_caruso_george_frideric_handel_ombra_mai_fu_serse-387	\N	2026-02-15 09:59:29.568641-03	2026-02-15 09:59:29.568645-03	100	f	2708077	0	0	3	0	\N	\N	\N	f	\N	Enrico_Caruso,_George_Frideric_Handel,_Ombra_mai_fu_(Serse).ogg	\N
50635	Caruso-AveMaria.ogg	\N	\N	\N	video/ogg	\N	\N	media	caruso-avemaria-388	\N	2026-02-15 10:01:53.977409-03	2026-02-15 10:01:53.977415-03	100	f	2716080	0	0	3	0	\N	\N	\N	f	\N	Caruso-AveMaria.ogg	\N
50637	Enrico_Caruso,_L'elisir_d'amore,_Una_furtiva_lagrima.ogg	\N	\N	\N	video/ogg	\N	\N	media	enrico_caruso_lelisir_damore_una_furtiva_lagrima-389	\N	2026-02-15 10:04:46.399956-03	2026-02-15 10:04:46.399959-03	100	f	3150997	0	0	3	0	\N	\N	\N	f	\N	Enrico_Caruso,_L'elisir_d'amore,_Una_furtiva_lagrima.ogg	\N
50639	Beethoven_concerto4_1.ogg	\N	\N	\N	video/ogg	\N	\N	media	beethoven_concerto4_1-390	\N	2026-02-15 10:10:54.562342-03	2026-02-15 10:10:54.562352-03	100	f	20108560	0	0	3	0	\N	\N	\N	f	\N	Beethoven_concerto4_1.ogg	\N
50641	Chopin_-_Nocturne_No._1_in_B-flat_minor,_Op._9_No._1_(Olga_Gurevich).flac	\N	\N	\N		\N	\N	media	chopin_-_nocturne_no_1_in_b-flat_minor_op_9_no_1_olga_gurevich-391	\N	2026-02-15 10:15:14.793356-03	2026-02-15 10:15:14.79336-03	100	f	39995666	0	0	3	0	\N	\N	\N	f	\N	Chopin_-_Nocturne_No._1_in_B-flat_minor,_Op._9_No._1_(Olga_Gurevich).flac	\N
50643	Mozart_Concerto_No.1_in_F_major_II._Andante_(Mozart)_European_Archive.ogg	\N	\N	\N	video/ogg	\N	\N	media	mozart_concerto_no1_in_f_major_ii_andante_mozart_european_archive-392	\N	2026-02-15 13:55:09.892453-03	2026-02-15 13:55:09.892455-03	100	f	7691894	0	0	3	0	\N	\N	\N	f	\N	Mozart_Concerto_No.1_in_F_major_II._Andante_(Mozart)_European_Archive.ogg	\N
50645	Wolfgang_Amadeus_Mozart_-_Symphony_40_g-moll_-_1._Molto_allegro.ogg	\N	\N	\N	video/ogg	\N	\N	media	wolfgang_amadeus_mozart_-_symphony_40_g-moll_-_1_molto_allegro-393	\N	2026-02-15 13:58:16.263433-03	2026-02-15 13:58:16.263439-03	100	f	10896007	0	0	3	0	\N	\N	\N	f	\N	Wolfgang_Amadeus_Mozart_-_Symphony_40_g-moll_-_1._Molto_allegro.ogg	\N
50647	Mozart_Piano_Concerto_23_in_A_major,_K_488,_II_Adagio._MYAC_Symphony_Orchestra,_Amir_Siraj.ogg	\N	\N	\N	video/ogg	\N	\N	media	mozart_piano_concerto_23_in_a_major_k_488_ii_adagio_myac_symphony_orchestra_amir_siraj-394	\N	2026-02-15 14:06:17.331834-03	2026-02-15 14:06:17.331836-03	100	f	7803771	0	0	3	0	\N	\N	\N	f	\N	Mozart_Piano_Concerto_23_in_A_major,_K_488,_II_Adagio._MYAC_Symphony_Orchestra,_Amir_Siraj.ogg	\N
50649	Mozart;_Concerto_No._12_in_A-_II._Andante.ogg	\N	\N	\N	video/ogg	\N	\N	media	mozart_concerto_no_12_in_a-_ii_andante-395	\N	2026-02-15 14:29:20.820409-03	2026-02-15 14:29:20.820414-03	100	f	4784063	0	0	3	0	\N	\N	\N	f	\N	Mozart;_Concerto_No._12_in_A-_II._Andante.ogg	\N
50651	13. Concerto No. 20 in D Minor- I. Allegro.mp3	\N	\N	\N	audio/mp3	\N	\N	media	13-concerto-no-20-in-d-minor--i-allegro-396	\N	2026-02-15 14:38:02.138293-03	2026-02-15 14:38:02.1383-03	100	f	16510164	0	0	3	789000	\N	\N	\N	f		13. Concerto No. 20 in D Minor- I. Allegro.mp3	\N
50653	Mozart_Piano_Concerto_No._15_in_B_flat_major_K450_II._Andante_(Mozart)_European_Archive.ogg	\N	\N	\N	video/ogg	\N	\N	media	mozart_piano_concerto_no_15_in_b_flat_major_k450_ii_andante_mozart_european_archive-397	\N	2026-02-15 15:16:40.364565-03	2026-02-15 15:16:40.364567-03	100	f	9842828	0	0	3	0	\N	\N	\N	f	\N	Mozart_Piano_Concerto_No._15_in_B_flat_major_K450_II._Andante_(Mozart)_European_Archive.ogg	\N
50655	Violinist_CARRIE_REHKOPF-BEETHOVEN_VIOLIN_SONATA_2_3rd_mvt.ogg	\N	\N	\N	video/ogg	\N	\N	media	violinist_carrie_rehkopf-beethoven_violin_sonata_2_3rd_mvt-398	\N	2026-02-15 15:22:42.221629-03	2026-02-15 15:22:42.221631-03	100	f	5490351	0	0	3	0	\N	\N	\N	f	\N	Violinist_CARRIE_REHKOPF-BEETHOVEN_VIOLIN_SONATA_2_3rd_mvt.ogg	\N
50657	Violin_Sonata_No.5,_Op.24_(Beethoven)_2.ogg	\N	\N	\N	video/ogg	\N	\N	media	violin_sonata_no5_op24_beethoven_2-399	\N	2026-02-15 15:25:13.105996-03	2026-02-15 15:25:13.105999-03	100	f	4889118	0	0	3	0	\N	\N	\N	f	\N	Violin_Sonata_No.5,_Op.24_(Beethoven)_2.ogg	\N
50659	Ludwig_van_Beethoven_-_Sonata_Op._31_No._2_(The_Tempest)_-_Second_Movement.ogg	\N	\N	\N	video/ogg	\N	\N	media	ludwig_van_beethoven_-_sonata_op_31_no_2_the_tempest_-_second_movement-400	\N	2026-02-15 15:28:10.962672-03	2026-02-15 15:28:10.962684-03	100	f	5660105	0	0	3	0	\N	\N	\N	f	\N	Ludwig_van_Beethoven_-_Sonata_Op._31_No._2_(The_Tempest)_-_Second_Movement.ogg	\N
50661	Beethoven,_Sonata_No._8_in_C_Minor_Pathetique,_Op._13_-_III._Rondo_-_Allegro.ogg	\N	\N	\N	video/ogg	\N	\N	media	beethoven_sonata_no_8_in_c_minor_pathetique_op_13_-_iii_rondo_-_allegro-401	\N	2026-02-15 15:31:21.980508-03	2026-02-15 15:31:21.980509-03	100	f	6646486	0	0	3	0	\N	\N	\N	f	\N	Beethoven,_Sonata_No._8_in_C_Minor_Pathetique,_Op._13_-_III._Rondo_-_Allegro.ogg	\N
50663	Beethoven,_Sonata_No._8_in_C_Minor_Pathetique,_Op._13_-_I._Grave_-_Allegro_di_molto_e_con_brio.ogg	\N	\N	\N	video/ogg	\N	\N	media	beethoven_sonata_no_8_in_c_minor_pathetique_op_13_-_i_grave_-_allegro_di_molto_e_con_brio-402	\N	2026-02-15 15:34:38.017131-03	2026-02-15 15:34:38.017133-03	100	f	10222065	0	0	3	0	\N	\N	\N	f	\N	Beethoven,_Sonata_No._8_in_C_Minor_Pathetique,_Op._13_-_I._Grave_-_Allegro_di_molto_e_con_brio.ogg	\N
50665	Ludwig_van_Beethoven_-_sonata_'waldstein',_op._53_-_ii._introduzione-_adagio_molto.ogg	\N	\N	\N	video/ogg	\N	\N	media	ludwig_van_beethoven_-_sonata_waldstein_op_53_-_ii_introduzione-_adagio_molto-403	\N	2026-02-15 15:38:14.054607-03	2026-02-15 15:38:14.054608-03	100	f	4506521	0	0	3	0	\N	\N	\N	f	\N	Ludwig_van_Beethoven_-_sonata_'waldstein',_op._53_-_ii._introduzione-_adagio_molto.ogg	\N
50667	Canon_in_D_Major_(ISRC_USUAN1100301).mp3	\N	\N	\N	audio/mp3	\N	\N	media	canon_in_d_major_isrc_usuan1100301-404	\N	2026-02-15 15:42:04.807358-03	2026-02-15 15:42:04.80736-03	100	f	5226209	0	0	3	0	\N	\N	\N	f	\N	Canon_in_D_Major_(ISRC_USUAN1100301).mp3	\N
50668	museo-secreto-411-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	museo-secreto-411-music-405	\N	2026-02-15 16:33:01.131932-03	2026-02-15 16:33:01.131936-03	100	f	344492	0	0	3	43000	\N	\N	\N	f		museo-secreto-411-music.mp3	\N
50669	legados-del-hacer-50443-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	legados-del-hacer-50443-music-406	\N	2026-02-15 16:36:23.503602-03	2026-02-15 16:36:23.503604-03	100	f	485855	0	0	3	0	\N	\N	\N	f	\N	legados-del-hacer-50443-music.mp3	\N
50676	Bach,_J_S_-_Goldberg_Variations,_BWV988_-_02._Variation_1_(Shelley_Katz).flac	\N	\N	\N		\N	\N	media	bach_j_s_-_goldberg_variations_bwv988_-_02_variation_1_shelley_katz-407	\N	2026-02-15 22:15:00.114451-03	2026-02-15 22:15:00.114452-03	100	f	6273290	0	0	3	0	\N	\N	\N	f	\N	Bach,_J_S_-_Goldberg_Variations,_BWV988_-_02._Variation_1_(Shelley_Katz).flac	\N
50678	Bach,_J_S_-_Goldberg_Variations,_BWV988_-_24._Variation_23_(Shelley_Katz).flac	\N	\N	\N		\N	\N	media	bach_j_s_-_goldberg_variations_bwv988_-_24_variation_23_shelley_katz-408	\N	2026-02-15 22:16:52.348813-03	2026-02-15 22:16:52.348814-03	100	f	6970521	0	0	3	0	\N	\N	\N	f	\N	Bach,_J_S_-_Goldberg_Variations,_BWV988_-_24._Variation_23_(Shelley_Katz).flac	\N
50680	Johann_Sebastian_Bach_Fugue_in_A_minor_BWV_543_Robert_Köbler_Silbermann-Organ.mp3	\N	\N	\N	audio/mp3	\N	\N	media	johann_sebastian_bach_fugue_in_a_minor_bwv_543_robert_ko-bler_silbermann-organ-409	\N	2026-02-15 22:20:23.066657-03	2026-02-15 22:20:23.066658-03	100	f	11806181	0	0	3	0	\N	\N	\N	f	\N	Johann_Sebastian_Bach_Fugue_in_A_minor_BWV_543_Robert_Köbler_Silbermann-Organ.mp3	\N
50682	Bach_Cello_Suite_1_Prelude_(BWV_1007)_Played_by_Chris.ogg	\N	\N	\N	video/ogg	\N	\N	media	bach_cello_suite_1_prelude_bwv_1007_played_by_chris-410	\N	2026-02-15 22:23:34.118426-03	2026-02-15 22:23:34.118427-03	100	f	8025732	0	0	3	0	\N	\N	\N	f	\N	Bach_Cello_Suite_1_Prelude_(BWV_1007)_Played_by_Chris.ogg	\N
50684	Bach_-_Cello_Suite_no._1_in_G_major,_BWV_1007_-_II._Allemande.ogg	\N	\N	\N	video/ogg	\N	\N	media	bach_-_cello_suite_no_1_in_g_major_bwv_1007_-_ii_allemande-411	\N	2026-02-15 22:24:21.933569-03	2026-02-15 22:24:21.93357-03	100	f	9286146	0	0	3	0	\N	\N	\N	f	\N	Bach_-_Cello_Suite_no._1_in_G_major,_BWV_1007_-_II._Allemande.ogg	\N
50686	Bach_-_Cello_Suite_no._1_in_G_major,_BWV_1007_-_IV._Sarabande.ogg	\N	\N	\N	video/ogg	\N	\N	media	bach_-_cello_suite_no_1_in_g_major_bwv_1007_-_iv_sarabande-412	\N	2026-02-15 22:26:01.752449-03	2026-02-15 22:26:01.75245-03	100	f	6083909	0	0	3	0	\N	\N	\N	f	\N	Bach_-_Cello_Suite_no._1_in_G_major,_BWV_1007_-_IV._Sarabande.ogg	\N
50688	Bach_-_Cello_Suite_no._1_in_G_major,_BWV_1007_-_VI._Gigue.ogg	\N	\N	\N	video/ogg	\N	\N	media	bach_-_cello_suite_no_1_in_g_major_bwv_1007_-_vi_gigue-413	\N	2026-02-15 22:27:26.15104-03	2026-02-15 22:27:26.151041-03	100	f	4658650	0	0	3	0	\N	\N	\N	f	\N	Bach_-_Cello_Suite_no._1_in_G_major,_BWV_1007_-_VI._Gigue.ogg	\N
50690	Vivaldi_-_Four_Seasons_1_Spring_mvt_1_Allegro_-_John_Harrison_violin.oga	\N	\N	\N		\N	\N	media	vivaldi_-_four_seasons_1_spring_mvt_1_allegro_-_john_harrison_violin-414	\N	2026-02-16 12:38:45.954906-03	2026-02-16 12:38:45.954908-03	100	f	4299414	0	0	3	0	\N	\N	\N	f	\N	Vivaldi_-_Four_Seasons_1_Spring_mvt_1_Allegro_-_John_Harrison_violin.oga	\N
50692	Vivaldi_-_Four_Seasons_1_Spring_mvt_2_Largo_-_John_Harrison_violin.oga	\N	\N	\N		\N	\N	media	vivaldi_-_four_seasons_1_spring_mvt_2_largo_-_john_harrison_violin-415	\N	2026-02-16 12:41:44.416619-03	2026-02-16 12:41:44.416621-03	100	f	3288210	0	0	3	0	\N	\N	\N	f	\N	Vivaldi_-_Four_Seasons_1_Spring_mvt_2_Largo_-_John_Harrison_violin.oga	\N
50694	Vivaldi_-_Four_Seasons_3_Autumn_mvt_2_Adagio_molto_-_John_Harrison_violin.oga	\N	\N	\N		\N	\N	media	vivaldi_-_four_seasons_3_autumn_mvt_2_adagio_molto_-_john_harrison_violin-416	\N	2026-02-16 12:43:54.740618-03	2026-02-16 12:43:54.740619-03	100	f	2672181	0	0	3	0	\N	\N	\N	f	\N	Vivaldi_-_Four_Seasons_3_Autumn_mvt_2_Adagio_molto_-_John_Harrison_violin.oga	\N
50696	Vivaldi_-_Four_Seasons_3_Autumn_mvt_3_Allegro_-_John_Harrison_violin.oga	\N	\N	\N		\N	\N	media	vivaldi_-_four_seasons_3_autumn_mvt_3_allegro_-_john_harrison_violin-417	\N	2026-02-16 12:48:24.254113-03	2026-02-16 12:48:24.254115-03	100	f	3850772	0	0	3	0	\N	\N	\N	f	\N	Vivaldi_-_Four_Seasons_3_Autumn_mvt_3_Allegro_-_John_Harrison_violin.oga	\N
50698	Vivaldi_-_Four_Seasons_2_Summer_mvt_2_Adagio_-_John_Harrison_violin.oga	\N	\N	\N		\N	\N	media	vivaldi_-_four_seasons_2_summer_mvt_2_adagio_-_john_harrison_violin-418	\N	2026-02-16 12:51:25.433306-03	2026-02-16 12:51:25.433307-03	100	f	2037740	0	0	3	0	\N	\N	\N	f	\N	Vivaldi_-_Four_Seasons_2_Summer_mvt_2_Adagio_-_John_Harrison_violin.oga	\N
50700	Vivaldi_-_Four_Seasons_4_Winter_mvt_1_Allegro_non_molto_-_John_Harrison_violin.oga	\N	\N	\N		\N	\N	media	vivaldi_-_four_seasons_4_winter_mvt_1_allegro_non_molto_-_john_harrison_violin-419	\N	2026-02-16 12:54:00.658303-03	2026-02-16 12:54:00.658305-03	100	f	4008237	0	0	3	0	\N	\N	\N	f	\N	Vivaldi_-_Four_Seasons_4_Winter_mvt_1_Allegro_non_molto_-_John_Harrison_violin.oga	\N
50708	20260211_111754.jpg	\N	\N	\N	image/jpeg	\N	\N	media	20260211_111754-420	\N	2026-02-16 14:16:19.599378-03	2026-02-16 14:16:19.59938-03	100	t	1418455	1800	4000	3	0	\N	\N	\N	f		20260211_111754.jpg	\N
50710	Tchaikovsky_-_Swan_Lake_Op.20_-_Act_II_Pt.1.ogg	\N	\N	\N	video/ogg	\N	\N	media	tchaikovsky_-_swan_lake_op20_-_act_ii_pt1-421	\N	2026-02-16 18:08:07.252973-03	2026-02-16 18:08:07.252975-03	100	f	3468395	0	0	3	0	\N	\N	\N	f	\N	Tchaikovsky_-_Swan_Lake_Op.20_-_Act_II_Pt.1.ogg	\N
50712	Tchaikovsky_-_Swan_Lake_Op.20_-_Act_I_Intro.ogg	\N	\N	\N	video/ogg	\N	\N	media	tchaikovsky_-_swan_lake_op20_-_act_i_intro-422	\N	2026-02-16 18:11:36.61182-03	2026-02-16 18:11:36.611827-03	100	f	17219908	0	0	3	0	\N	\N	\N	f	\N	Tchaikovsky_-_Swan_Lake_Op.20_-_Act_I_Intro.ogg	\N
50714	Tchaikovsky_-_Swan_Lake_Op.20_-_Act_IV_Intro.ogg	\N	\N	\N	video/ogg	\N	\N	media	tchaikovsky_-_swan_lake_op20_-_act_iv_intro-423	\N	2026-02-16 18:17:29.104809-03	2026-02-16 18:17:29.104816-03	100	f	19508621	0	0	3	0	\N	\N	\N	f	\N	Tchaikovsky_-_Swan_Lake_Op.20_-_Act_IV_Intro.ogg	\N
50715	legacies-of-doing-50445-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	legacies-of-doing-50445-music-424	\N	2026-02-16 18:20:14.128726-03	2026-02-16 18:20:14.128728-03	100	f	538796	0	0	3	0	\N	\N	\N	f	\N	legacies-of-doing-50445-music.mp3	\N
50716	h-ritages-de-laction-50448-music.mp3	\N	\N	\N	audio/mp3	\N	\N	audio-voice-music	h-ritages-de-laction-50448-music-425	\N	2026-02-16 18:22:29.622759-03	2026-02-16 18:22:29.622761-03	100	f	547628	0	0	3	0	\N	\N	\N	f	\N	h-ritages-de-laction-50448-music.mp3	\N
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
936	admin	3	2025-12-05 09:49:32.504471-03	2025-12-05 09:49:32.504471-03	100	admin
937	audit	3	2025-12-05 09:50:11.22832-03	2025-12-05 09:50:11.22832-03	100	audit
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
42206	admin	42202	2026-01-21 04:29:06.391008-03	2026-01-21 04:29:06.391017-03	100	3	admin
50366	admin	50359	2026-02-10 18:51:24.779266-03	2026-02-10 18:51:24.779269-03	100	3	admin
50391	admin	50384	2026-02-11 09:06:17.640879-03	2026-02-11 09:06:17.640888-03	100	3	admin
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
42272	admin	42268	2026-01-21 04:56:29.659925-03	2026-01-21 04:56:29.65993-03	100	3	admin
42273	editor	42268	2026-01-21 04:56:29.661818-03	2026-01-21 04:56:29.661823-03	100	3	admin
50375	admin	50368	2026-02-10 19:01:46.771765-03	2026-02-10 19:01:46.771766-03	100	3	admin
50376	editor	50368	2026-02-10 19:01:46.772704-03	2026-02-10 19:01:46.772705-03	100	3	admin
50400	admin	50393	2026-02-11 09:10:13.971427-03	2026-02-11 09:10:13.971429-03	100	3	admin
50401	editor	50393	2026-02-11 09:10:13.972348-03	2026-02-11 09:10:13.97235-03	100	3	admin
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
552	Basílica Nuestra Señora del Pilar	bas-lica-nuestra-se-ora-del-pilar	Iglesia del Pilar	Basílica Nuestra Señora del Pilar	bas-lica-nuestra-se-ora-del-pilar	\N	547	https://basilicadelpilar.org/	https://maps.app.goo.gl/x1ceVM3UppVU4P4u9	\N	\N	\N	4806 2209	\N	Se inauguró en 1732. Conserva retablos, imaginería y ornamentos originales	\N	La Iglesia de Nuestra Señora del Pilar fue inaugurada en 1732 y declarada Monumento Histórico Nacional en 1942. Está ubicada en el barrio de Recoleta y fue proyectada por los arquitectos jesuitas Bianchi y Prímoli. Hoy en día, se conservan sus majestuosos retablos, imaginería y ornamentos originales. En su origen estaban en las afueras de la ciudad, teniendo en cuenta las reglas de la orden de recogimiento y separación. El que aportó el dinero para la construcción fue un vecino proveniente de Zaragoza y puso la condición de que venerara a la Virgen del Pilar, muy popular en su ciudad de origen.  \r\n\r\nLa iglesia consta de una sola nave con un crucero muy desarrollado, cubierto por bóveda vaída. Las capillas laterales son poco profundas. En su interior, se destaca el retablo mayor (barroco), con la imagen titular en el centro y a sus costados dos santos franciscanos. El altar mayor es una pieza muy singular, con ornamentación inca del Alto Perú, muy ricamente trabajado en plata.\r\n\r\nLos altares laterales de la única nave, también son barrocos. La talla de madera de San Pedro de Alcántara –copatrono de la iglesia– es del siglo XVIII y se le atribuye al escultor Alonso Cano, mientras que el Altar de las Reliquias, según la tradición, fue un regalo del rey Carlos III de España. El púlpito es también de factura barroca.	\N	Junín 1898 \r\nCiudad Autónoma de Buenos Aires	\N	1499	558	\N	\N	\N	2025-09-03 17:31:49.976266-03	2025-10-03 20:48:57.298804-03	100	\N	\N	\N	\N	\N	3	..	\N	es	0	t	\N	t	America/Buenos_Aires	\N	{"en": "en", "pt-BR": "pt-BR"}	Colección permanente	Exhibiciones temporarias	t
564	Colección de Arte Amalia Lacroze de Fortabat	colecci-n-de-arte-amalia-lacroze-de-fortabat	Museo Fortabat	Colección de Arte Amalia Lacroze de Fortabat	colecci-n-de-arte-amalia-lacroze-de-fortabat	\N	561	www.coleccionfortabat.org.ar	\N	\N	\N	\N	\N	\N	Posee más de 150 obras de reconocidos artistas internacionales.	\N	Esta importante colección privada de arte (inaugurada en 2008 y reabierta en 2012) posee más de 150 obras de reconocidos artistas internacionales como Rodin, Warhol, Turner, Dalí y Blanes, así como de artistas argentinos tales como Badii, Berni, Quinquela Martín, Noé, Pérez Celis, Fader, Soldi y Xul Solar, entre otros.	\N	OLGA  COSSETTINI  141	\N	563	562	\N	\N	\N	2025-09-12 14:14:03.166733-03	2025-10-03 20:50:41.244989-03	100	\N	\N	\N	\N	\N	3	es	\N	es	0	t	\N	t	America/Buenos_Aires	\N	{"en": "en", "pt-BR": "pt-BR"}	Colección permanente	Exhibiciones temporarias	t
753	Museo Nacional de Arte Decorativo	\N	Museo de Arte Decorativo	\N	\N	\N	749	https://museoartedecorativo.cultura.gob.ar/	https://maps.app.goo.gl/XHiRj9nd8zt8VTBv8	decorativocontacto@gmail.com	\N	\N	+54 (011) 4806-8306	\N	Arte decorativo europeo y oriental de los siglos XVI al XIX	\N	El Museo Nacional de Arte Decorativo, de la Ciudad de Buenos Aires, está ubicado en lo que fuera una residencia particular por lo que constituye una casa-museo. Su colección está compuesta por más de 6000 piezas, entre las que se destacan esculturas, pinturas, tapices, armas, libros, cerámicas, mobiliario y miniaturas, fundamentalmente europeas y orientales, de los siglos XVI al XX. \r\n\r\nEs un museo nacional en el que, a través de sus  exhibiciones permanentes y temporarias, programas públicos y diferentes actividades culturales, se propone un espacio para el diálogo y el intercambio de conocimientos. Todos los públicos son invitados a acercarse al patrimonio, la colección y su historia y a reflexionar sobre esta casa-museo.	\N	Av. del Libertador 1902, Ciudad de Buenos Aires.	\N	751	756	\N	\N	\N	2025-10-28 14:50:12.810972-03	2025-10-30 12:12:47.119058-03	100	Miércoles de 13:00 a 19:00hs\r\nJueves de 13:00 a 19:00hs\r\nViernes de 13:00 a 19:00hs\r\nSábado de 13:00 a 19:00hs\r\nDomingo de 13:00 a 19:00hs	\N	\N	\N	\N	3	es	\N	es	\N	f	\N	f	America/Buenos_Aires	\N	\N	Colección permanente	Exhibiciones temporarias	t
137	Museo Nacional de Bellas Artes	museo-nacional-de-bellas-artes	MNBA	Museo Nacional de Bellas Artes	\N	\N	135	https://www.bellasartes.gob.ar	https://maps.app.goo.gl/mCePQghfM2oUjmgy6	\N	\N	\N	54 911 64324235	\N	Posee la mayor colección de arte local y una de las más importantes de arte universal en toda Latinoamérica.	\N	El Museo Nacional de Bellas Artes es uno de los más importantes de Latinoamérica. \r\nSu colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad. \r\nEstá ubicado en el barrio de Recoleta y fue inaugurado en la Navidad de 1896.  \r\nDentro del arte internacional se destacan obras de El Greco, Goya, Rodin, Rembrandt, Rubens, Renoir, Degas, Cézanne, Chagall y Picasso. Entre los pintores argentinos más importantes, vas a sorprenderte con obras de Cándido López, Lino Enea Spilimbergo, Prilidiano Pueyrredón, Fernando Fader, Benito Quinquela Martín, Xul Solar, Antonio Berni, Marta Minujín, Emilio Pettoruti, Carlos Alonso, Antonio Seguí y León Ferrari. Además, posee un importante conjunto de arte latinoamericano, que reúne obras de Pedro Figari, Joaquín Torres García, Tarsila Do Amaral, Diego Rivera y Jesús Rafael Soto, entre otras.\r\nCuenta también con una sala de fotografía, otra de arte andino precolombino, dos terrazas de esculturas y una biblioteca con 150.000 ejemplares.	\N	MNBA	\N	462	531	\N	\N	\N	2025-05-19 12:29:38.885286-03	2026-02-04 22:04:16.352017-03	100	Lunes: cerrado \r\nMartes: de 11:00 a 19:30 \r\nMiércoles: de 11:00 a 19:30\r\nJueves: de 11:00 a 19:30\r\nViernes: de 11:00 a 19:30 \r\nSábado: de 10:00 a 19:30 \r\nDomingo: de 10:00 a 19:30	\N	Es una de las instituciones públicas de arte más importantes de Argentina.\\n Su colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad.	El Museo Nacional de Bellas Artes (MNBA), ubicado en la Ciudad de Buenos Aires, es una de las instituciones públicas de arte más importantes de Argentina. Alberga un patrimonio sumamente diverso, que incluye más de 12 000 piezas, entre pinturas, esculturas, dibujos, grabados, textiles y objetos. Su colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad.	\N	3	..	\N	es	0	t	\N	t	America/Argentina/Buenos_Aires	\N	{"en": "en", "pt-BR": "pt-BR"}	Colección permanente	Exhibiciones temporarias	t
860	Museo Latinoamericano de Arte Moderno	\N	MLM	\N	\N	\N	855	https://www.moma.org	\N	\N	\N	\N	\N	\N	Arte latinoamericano del siglo XX y XXI	\N	En el Museo de Arte Moderno  celebramos la creatividad, la apertura, la tolerancia y la generosidad. Nuestro objetivo es ser espacios inclusivos, tanto presenciales como virtuales, donde se aceptan diversas posturas culturales, artísticas, sociales y políticas. Nos comprometemos a compartir el arte moderno y contemporáneo más sugerente, y esperamos que nos acompañen a explorar el arte, las ideas y los problemas de nuestro tiempo.	\N	Moreau de Justo 2045. \r\nCiudad de Buenos Aires	\N	1493	859	\N	\N	\N	2025-11-28 10:43:20.500065-03	2025-11-28 10:43:20.500081-03	100	Lunes cerrado.\r\nMartes  a Viernes: 10 a 20 hs.\r\nSabados: 10 a 21 hs.\r\nDomingos: 10 a 21 hs.	\N	\N	\N	\N	3	..	\N	es	\N	t	\N	f	America/Buenos_Aires	\N	\N	Colección permanente	Exhibiciones temporarias	t
342	Museo de Arte Latinoaméricano de Buenos Aires	museo-de-arte-latinoam-ricano-de-buenos-aires	MALBA	Museo de Arte Latinoaméricano de Buenos Aires	\N	\N	341	https://www.malba.org.ar/	https://maps.app.goo.gl/VBMMA9osiJjECMoq6	\N	\N	\N	+54 11 4808 6500	\N	Alberga una de las colecciones de arte latinoamericano más importantes.	\N	El MALBA es una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región. Se specializa en el arte latinoamericano desde principios del siglo XX hasta la actualidad.	\N	Av. Figueroa Alcorta 3415\r\nC1425CLA Buenos Aires, Argentina	\N	565	560	\N	\N	\N	2025-05-30 00:37:14.101263-03	2025-09-19 09:41:50.633097-03	100	Jueves a lunes de 12:00 a 20:00.\r\nMiércoles de 11:00 a 20:00. \r\nMartes cerrado.	\N	Se specializa en el arte latinoamericano desde principios del siglo XX hasta la actualidad. \\nEs una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región.	El MALBA es una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región. Se specializa en el arte latinoamericano desde principios del siglo XX hasta la actualidad.	\N	3	es	\N	es	0	t	\N	t	America/Buenos_Aires	\N	\N	Colección permanente	Exhibiciones temporarias	t
555	Museo Moderno	museo-moderno	\N	Museo Moderno	museo-moderno	\N	553	https://museomoderno.org	https://maps.app.goo.gl/6AABaQxZXCyhcwzF7	info@museomoderno.org	\N	\N	011 4361 6919	\N	En el barrio de San Telmo, el museo alberga más de 7000 obras de arte argentino e internacional.	\N	En el corazón del Casco Histórico, en pleno barrio de San Telmo, el Museo Moderno pone a tu disposición una rica colección del arte argentino de los últimos años.\r\nLa propuesta del Moderno es acercarte a las artes visuales a través de sus 11.000 m2 y sus más de 7.000 obras.\r\nA su vez, el edificio es un fiel exponente de las construcciones inglesas de la era industrial del siglo XIX, con su estructura de hierro, grandes aberturas y su fachada de ladrillo a la vista. \r\nLa experiencia se complementa con el bar, que ofrece un excelente café de especialidad y un menú diseñado especialmente. No te olvides de visitar la tienda donde encontrarás todos los libros editados por el Moderno, una oportunidad para sumergirte en el universo de los artistas a través de estas publicaciones.	\N	Avenida San Juan 350	\N	\N	554	\N	\N	\N	2025-09-10 18:16:23.266138-03	2025-10-03 20:51:41.499541-03	100	\N	\N	\N	\N	\N	3	es	\N	es	0	t	\N	t	America/Buenos_Aires	\N	{"en": "en", "pt-BR": "pt-BR"}	Colección permanente	Exhibiciones temporarias	t
42268	Museo de Arte Contemporáneo Atchugarry	\N	MACA	\N	\N	\N	42202	https://macamuseo.org/museo	https://maps.app.goo.gl/zoqWJ8AyekyPzVs9A	\N	\N	\N	\N	\N	\N	\N	El Museo de Arte Contemporáneo Atchugarry (MACA) es un museo privado de acceso gratuito ubicado en Maldonado, Uruguay.\r\nFue impulsado por el escultor uruguayo Pablo Atchugarry y llevado adelante por la fundación que también lleva su nombre. \r\nEl edificio fue obra del arquitecto Carlos Ott. Para la construcción del mismo se utilizó la especie de eucaliptus red grandis, consta de una amplia superficie y cuenta con cuatro salas de exposiciones, una sala multifuncional y una sala de cine.	\N	Manantiales, Maldonado\r\nRuta 104 kilómetro 4	\N	\N	42257	\N	\N	\N	2026-01-21 04:56:29.626425-03	2026-01-21 04:56:29.626443-03	100	\N	\N	\N	\N	\N	3	es	\N	es	\N	t	\N	t	America/Montevideo	\N	{"en": "en", "pt-BR": "pt-BR"}	\N	\N	t
537	Cabildo de Buenos Aires	cabildo-de-buenos-aires	\N	Cabildo de Buenos Aires	cabildo-de-buenos-aires	\N	529	https://cabildonacional.cultura.gob.ar/	https://maps.app.goo.gl/eooviVBRmuy38Zcc7	cabildonacional@gmail.com	\N	\N	(011) 15 22642778\r\n(011) 4342-6729 / 4334-1782	\N	La sede de la administración colonial ocupa el mismo lugar desde 1580 y es un emblema de la historia viva. \r\nVisitá el corazón de la Revolución de Mayo.	\N	En este edificio funcionó el cabildo colonial fundado por Juan de Garay en 1580 durante la segunda fundación de la ciudad de Buenos Aires y que luego de la Revolución de Mayo de 1810, que derrocó al virrey español Baltasar Hidalgo de Cisneros y derivó en la guerra que llevó a la independencia de las Provincias Unidas del Río de la Plata, se transformó en una Junta de Gobierno que funcionó hasta su disolución en 1821 por el gobernador de Buenos Aires Martín Rodríguez.	\N	Bolívar 65\r\nCiudad Autónoma de Buenos Aires	\N	536	557	\N	\N	\N	2025-08-29 09:26:13.956557-03	2025-10-03 20:50:12.166903-03	100	Lunes. Cerrado\r\nMartes. Cerrado\r\nMiércoles. Abierto de 10:30 a 18:00hs\r\nJueves. Abierto de 10:30 a 18:00hs\r\nViernes. Abierto de 10:30 a 18:00hs\r\nSábado. Abierto de 10:30 a 18:00hs\r\nDomingo. Abierto de 10:30 a 18:00hs	\N	\N	En este edificio funcionó el cabildo colonial fundado por Juan de Garay en 1580 durante la segunda fundación de la ciudad de Buenos Aires y que luego de la Revolución de Mayo de 1810, que derrocó al virrey español Baltasar Hidalgo de Cisneros y derivó en la guerra que llevó a la independencia de las Provincias Unidas del Río de la Plata, se transformó en una Junta de Gobierno que funcionó hasta su disolución en 1821 por el gobernador de Buenos Aires Martín Rodríguez.	\N	3	es	\N	es	0	t	\N	t	America/Buenos_Aires	\N	{"en": "en", "pt-BR": "pt-BR"}	Colección permanente	Exhibiciones temporarias	t
50368	Museo del Holocausto	\N	\N	\N	\N	\N	50359	https://museodelholocausto.org.ar/	\N	\N	\N	\N	\N	\N	Montevideo 919, Ciudad Autónoma de Buenos Aires.	\N	Es un espacio que integra la historia del Holocausto-Shoá y sus repercusiones en la Argentina, con el objetivo de educar a las nuevas generaciones y preservar la memoria. A través de objetos, documentos y testimonios de los sobrevivientes se exhibe un recorrido que da cuenta del proceso de exterminio de seis millones de judíos a manos de la Alemania nazi.	\N	Montevideo 919.	\N	\N	50367	\N	\N	\N	2026-02-10 19:01:46.698529-03	2026-02-10 19:01:46.698537-03	100	Lunes a jueves: 11:00 a 19:00 h.\r\nDomingos: 14:00 a 18:00 h.\r\nViernes y sábados: Cerrado.	\N	\N	\N	\N	3	es	\N	es	\N	t	\N	t	America/Argentina/Buenos_Aires	\N	{"en": "en", "pt-BR": "pt-BR"}	\N	\N	f
50393	Casa de Victoria Ocampo	\N	\N	\N	\N	\N	50384	\N	https://maps.app.goo.gl/AnBEaKgXSMgaEdds5	\N	\N	\N	\N	\N	\N	\N	El proyecto de la casa es la culminación de un proceso de búsqueda de Victoria Ocampo respecto de la arquitectura y del diseño moderno, que se nutre de la vanguardia europea, y que surge después de haber construido una primera casa moderna en la Ciudad de Mar del Plata, Provincia de Buenos Aires y de encargar a Charles Édouard Jeanneret-Gris, más conocido como Le Corbusier, un diseño que nunca se construyó.\r\n\r\nEl proyecto definitivo lo realizó el arquitecto Alejandro Bustillo. \r\n\r\nSe trata de una vivienda de arquitectura moderna inspirada en múltiples referencias a la arquitectura de vanguardia europea, en especial francesa, con exteriores que revelan influencias del pintoresquismo e interiores organizados a través de múltiples ejes interconectados y de simetrías parciales derivadas del clasicismo francés del siglo XVIII.\r\n\r\nEn el año 1931, en sus inmediaciones, tuvo lugar la fundación de la mítica revista literaria “Sur”, una de las más importantes de su época.\r\nEn el año 1940, tras la muerte de su padre, Victoria resolvió vender la casa y mudarse a Villa Ocampo.	\N	Rufino de Elizalde 2831, CABA.	\N	50403	50392	\N	\N	\N	2026-02-11 09:10:13.949042-03	2026-02-11 09:31:33.774052-03	100	Lunes a viernes de 11:00 a 19:00 h.	\N	\N	\N	\N	3	es	\N	es	\N	t	\N	t	America/Argentina/Buenos_Aires	\N	{"en": "en", "fr": "fr", "pt-BR": "pt-BR"}	Colección permanente	Exhibiciones	f
177	Museo Benito Quinquela Martín	museo-benito-quinquela-mart-n	Museo Quinquela	Museo Benito Quinquela Martín	\N	\N	136	https://buenosaires.gob.ar/educacion/gestion-cultural/museo-benito-quinquela-martin	\N	\N	\N	\N	54 911 66892321	\N	Cuenta con la más amplia colección de óleos de este emblemático artista plástico y es todo un ícono del barrio de La Boca.	\N	El Museo "Benito Quinquela Martín" está ubicado en el barrio de La Boca en la Ciudad Autónoma de Buenos Aires, Argentina. \r\nFundado en el año 1938, está construido sobre terrenos originalmente donados por el pintor. Cuenta actualmente con la mayor colección reunida del artista plástico, más de 90 de sus obras. Además, el Museo ofrece un panorama del arte figurativo argentino desde fines del siglo XIX hasta mediados del siglo XX. Obras de grandes artistas, considerados iniciadores y precursores de las artes plásticas en el país, se encuentran exhibidas en este museo, donde el artista vivió y trabajó.	\N	Av. Don Pedro de Mendoza 1835, C1169 Cdad. Autónoma de Buenos Aires	\N	919	559	\N	\N	\N	2025-05-19 12:29:38.885286-03	2025-10-03 20:51:04.976098-03	100	\N	\N	El Museo "Benito Quinquela Martín" está ubicado en el barrio de La Boca en la Ciudad Autónoma de Buenos Aires, Argentina. Fundado en el año 1938, está construido sobre terrenos originalmente donados por el pintor. Cuenta actualmente con la mayor colección reunida del artista plástico, más de 90 de sus obras. Además, el Museo ofrece un panorama del arte figurativo argentino desde fines del siglo XIX hasta mediados del siglo XX. Obras de grandes artistas, considerados iniciadores y precursores de las artes plásticas en el país, se encuentran exhibidas en este museo, donde el artista vivió y trabajó.	El Museo "Benito Quinquela Martín" está ubicado en el barrio de La Boca en la Ciudad Autónoma de Buenos Aires, Argentina. Fundado en el año 1938, está construido sobre terrenos originalmente donados por el pintor. Cuenta actualmente con la mayor colección reunida del artista plástico, más de 90 de sus obras. Además, el Museo ofrece un panorama del arte figurativo argentino desde fines del siglo XIX hasta mediados del siglo XX. Obras de grandes artistas, considerados iniciadores y precursores de las artes plásticas en el país, se encuentran exhibidas en este museo, donde el artista vivió y trabajó.	\N	3	..	\N	es	0	t	\N	t	America/Buenos_Aires	\N	\N	Colección permanente	Exhibiciones temporarias	t
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
42269	42268	es	Museo de Arte Contemporáneo Atchugarry	\N	\N	\N	\N	\N	2026-01-21 04:56:29.653529-03	2026-01-21 04:56:29.653539-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
42270	42268	en	Museo de Arte Contemporáneo Atchugarry	\N	\N	\N	\N	\N	2026-01-21 04:56:29.656846-03	2026-01-21 04:56:29.656849-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
42271	42268	pt-BR	Museo de Arte Contemporáneo Atchugarry	\N	\N	\N	\N	\N	2026-01-21 04:56:29.657694-03	2026-01-21 04:56:29.657697-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50369	50368	es	Museo del Holocausto	\N	\N	\N	\N	\N	2026-02-10 19:01:46.767062-03	2026-02-10 19:01:46.767064-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50370	50368	en	Museo del Holocausto	\N	\N	\N	\N	\N	2026-02-10 19:01:46.768318-03	2026-02-10 19:01:46.768318-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50371	50368	pt-BR	Museo del Holocausto	\N	\N	\N	\N	\N	2026-02-10 19:01:46.769244-03	2026-02-10 19:01:46.769244-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50372	50368	it	Museo del Holocausto	\N	\N	\N	\N	\N	2026-02-10 19:01:46.769643-03	2026-02-10 19:01:46.769644-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50373	50368	fr	Museo del Holocausto	\N	\N	\N	\N	\N	2026-02-10 19:01:46.770007-03	2026-02-10 19:01:46.770007-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50374	50368	ger	Museo del Holocausto	\N	\N	\N	\N	\N	2026-02-10 19:01:46.770319-03	2026-02-10 19:01:46.77032-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50394	50393	es	new	\N	\N	\N	\N	\N	2026-02-11 09:10:13.965724-03	2026-02-11 09:10:13.965733-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50395	50393	en	new	\N	\N	\N	\N	\N	2026-02-11 09:10:13.967146-03	2026-02-11 09:10:13.967148-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50396	50393	pt-BR	new	\N	\N	\N	\N	\N	2026-02-11 09:10:13.967931-03	2026-02-11 09:10:13.967935-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50397	50393	it	new	\N	\N	\N	\N	\N	2026-02-11 09:10:13.968739-03	2026-02-11 09:10:13.968741-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50399	50393	ger	new	\N	\N	\N	\N	\N	2026-02-11 09:10:13.969796-03	2026-02-11 09:10:13.969798-03	100	\N	\N	1	\N	0	0	0	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
50398	50393	fr	La maison de Victoria Ocampo	\N	Le projet de cette maison est l'aboutissement de la recherche de Victoria Ocampo en matière d'architecture et de design modernes, puisant son inspiration dans l'avant-garde européenne. Cette recherche a débuté après la construction de sa première maison moderne à Mar del Plata, dans la province de Buenos Aires, et la commande d'un projet à Charles Édouard Jeanneret-Gris, plus connu sous le nom de Le Corbusier, projet qui ne verra jamais le jour.\n\nLe projet final a été réalisé par l'architecte Alejandro Bustillo.\n\nIl s'agit d'une maison moderne inspirée par de nombreuses références à l'architecture d'avant-garde européenne, notamment française, avec des extérieurs révélant des influences du style pittoresque et des intérieurs organisés autour de multiples axes interconnectés et de symétries partielles héritées du classicisme français du XVIIIe siècle.\n\nEn 1931, la revue littéraire légendaire « Sur », l'une des plus importantes de son époque, fut fondée à proximité. En 1940, après le décès de son père, Victoria décida de vendre la maison et de s'installer à la Villa Ocampo.	\N	\N	\N	2026-02-11 09:10:13.969397-03	2026-02-11 09:32:27.62682-03	100	\N	\N	1	\N	1187254133	0	551196188	0	t	\N	0	0	\N	\N	0	\N	0	\N	0	t	f	\N	\N
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
50670	101	936	\N	\N
50671	101	\N	\N	996
50672	101	\N	\N	987
50673	101	\N	1015	\N
50674	101	\N	1000	\N
50486	50412	936	\N	\N
50487	50412	\N	50400	\N
50488	50412	\N	50401	\N
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name, namekey, title, titlekey, created, lastmodified, lastmodifieduser, state, language, draft, audioautogenerate, locale, zoneid, password) FROM stdin;
1459	new	\N	\N	\N	2025-12-13 07:42:05.874287-03	2025-12-13 07:42:05.874306-03	100	3	es	\N	t	es	America/Buenos_Aires	$2a$10$BYMyEdPIANdXWuiRHYqbhOTh2ALWAefFYn3bBG1TlIpRA1WAvGVWe
50412	gmiremont	\N	\N	\N	2026-02-11 09:45:48.563751-03	2026-02-11 09:45:48.563769-03	100	3	es	\N	t	es	America/Buenos_Aires	$2a$10$ZoZYhMwO5N0Hhai9fw6Hk.cdtVA.AxH8I0e19tuyZAGcQC0U/EwHG
101	atolomei	\N	\N	\N	2025-05-19 12:29:38.885286-03	2026-02-13 08:01:43.897344-03	100	3	es	\N	t	es	America/Buenos_Aires	$2a$10$HjFAZ3OZnxf7lfS016iFlOcXyGv1K27dgyDyJuafPNqN65JTOaEJq
100	root	\N	\N	\N	2025-05-19 12:29:34.500268-03	2026-02-16 16:15:54.80029-03	100	3	es	\N	t	es	America/Buenos_Aires	$2a$10$vlESj0PiND5tAA.FB6mcJ.0rmy1PYv.96xgSP.NXTt9RSAX/N.nCy
\.


--
-- Data for Name: voice; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.voice (id, name, namekey, title, titlekey, url, state, voiceid, language, languageregion, comment, voicesettings, info, infokey, draft, audio, created, lastmodified, lastmodifieduser, sex) FROM stdin;
50338	amanda	\N	\N	\N	\N	3	oi8rgjIfLgJRsQ6rbZh3	pt	brazil	\N	\N	A sweet, feminine, and youthful Brazilian Portuguese voice with a neutral accent. Naturally warm and expressive, she brings a gentle charm to every word. Ideal for narrations, educational content, and conversational dialogue where clarity, softness, and an inviting tone are essential.	\N	\N	50473	2026-02-10 14:29:08.587752-03	2026-02-10 14:29:08.587752-03	100	female
50461	Campanholi	\N	\N	\N	\N	3	AaeZyyi87RCxtFnHPS3e	pt	brazil	\N	\N	Prof. Campanholi - Professor, 40 years old, clear and didactic voice	\N	\N	50472	2026-02-11 17:23:29.456125-03	2026-02-11 17:23:29.456125-03	100	male
50380	tamsin	\N	\N	\N	\N	3	dAlhI9qAHVIjXuVppzhW	en	british	\N	\N	Authentic British female voice with deep emotional resonance. Captures the nuances of a seasoned narrator—perfect for immersive audiobooks, dramatic documentarie.	\N	\N	50476	2026-02-11 08:42:47.065649-03	2026-02-11 08:42:47.065649-03	100	female
50339	mariana	\N	\N	\N	\N	3	9rvdnhrYoXoUt4igKpBw	es	latin america	\N	\N	Intimate and Assertive.	\N	\N	50477	2026-02-10 14:31:18.268079-03	2026-02-10 14:31:18.268079-03	100	female
50349	ernesto	\N	\N	\N	\N	3	TOFW0dONbX4o9MmkxwBB	es	spain	\N	\N	xplainer & Informative videos - Natural and kind voice. Middle-age man with Spanish peninsular accent	\N	\N	50478	2026-02-10 18:30:43.204246-03	2026-02-10 18:30:43.204246-03	100	male
50350	marcela	\N	\N	\N	\N	3	YM9MbhkdpZ8JR7EP49hA	es	latin america	\N	\N	Calm, Soft and Neutral.	\N	\N	50479	2026-02-10 18:30:43.205433-03	2026-02-10 18:30:43.205433-03	100	female
50381	antoine	\N	\N	\N	\N	3	1a3lMdKLUcfcMtvN772u	fr	france	\N	\N	A male adult voice, composed and formal, with a well-controlled neutral tone. Its timbre resembles that of an actor accustomed to delivering precise and articulate speech	\N	\N	50481	2026-02-11 08:50:29.839739-03	2026-02-11 08:50:29.839739-03	100	male
50379	jonathan	\N	\N	\N	\N	3	PIGsltMj3gFMR34aFDI3	en	american	\N	\N	A calm, trustworthy, confident voice to narrate your story, audiobooks, articles and other media.	\N	\N	50482	2026-02-11 08:36:43.249885-03	2026-02-11 08:36:43.249885-03	100	male
50355	leon	\N	\N	\N	\N	3	MJ0RnG71ty4LH3dvNfSd4	ger	germany	\N	\N		\N	\N	50483	2026-02-10 18:36:14.883186-03	2026-02-10 18:36:14.883186-03	100	male
50357	nigel	\N	\N	\N	\N	3	l9yjqAhh8GXv7ZJxsLZO	en	british	\N	\N	A distinct, seasoned English male voice that captivates audiences with warmth, clarity, and authenticity.	\N	\N	50484	2026-02-10 18:47:49.742126-03	2026-02-10 18:47:49.742126-03	100	male
50358	shaun	\N	\N	\N	\N	3	hfgNmTYYctMgJ7E2s6Vx	en	british	\N	\N		\N	\N	50485	2026-02-10 18:49:09.922671-03	2026-02-10 18:49:09.922671-03	100	male
50351	clara	\N	\N	\N	\N	3	LudcwvHIZaqQOcQfVZSY	es	spain	\N	\N		\N	\N	50475	2026-02-10 18:30:43.296535-03	2026-02-10 18:30:43.296535-03	100	female
50382	Anaïs	\N	\N	\N	\N	3	5OnMHwgTFgvPVwE8jP6B	fr	france	\N	\N	Middle aged French female voice. Warm and clear, ideal for podcasting, e-learning and news content.	\N	\N	50474	2026-02-11 08:52:10.605255-03	2026-02-11 08:52:10.605255-03	100	female
\.


--
-- Name: audio_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id', 40, true);


--
-- Name: audio_id_137_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_137_seq', 82, true);


--
-- Name: audio_id_177_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_177_seq', 1, true);


--
-- Name: audio_id_342_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_342_seq', 47, true);


--
-- Name: audio_id_42268_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_42268_seq', 4, true);


--
-- Name: audio_id_50368_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_50368_seq', 1, false);


--
-- Name: audio_id_50393_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_50393_seq', 5, true);


--
-- Name: audio_id_537_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_537_seq', 5, true);


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

SELECT pg_catalog.setval('public.audio_id_753_seq', 40, true);


--
-- Name: audio_id_860_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audio_id_860_seq', 8, true);


--
-- Name: audit_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audit_id', 2035, true);


--
-- Name: objectstorage_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.objectstorage_id', 425, true);


--
-- Name: readcode_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.readcode_id', 1035, true);


--
-- Name: sequence_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sequence_id', 50716, true);


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
-- Name: artist_sites artist_sites_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artist_sites
    ADD CONSTRAINT artist_sites_pkey PRIMARY KEY (artist_id, site_id);


--
-- Name: artistrecord artistrecord_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artistrecord
    ADD CONSTRAINT artistrecord_pkey PRIMARY KEY (id);


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
-- Name: voice elvoice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voice
    ADD CONSTRAINT elvoice_pkey PRIMARY KEY (id);


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
-- Name: music music_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.music
    ADD CONSTRAINT music_pkey PRIMARY KEY (id);


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
-- Name: artist_sites artist_sites_artist_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artist_sites
    ADD CONSTRAINT artist_sites_artist_id_fkey FOREIGN KEY (artist_id) REFERENCES public.artist(id) ON DELETE RESTRICT;


--
-- Name: artist_sites artist_sites_site_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artist_sites
    ADD CONSTRAINT artist_sites_site_id_fkey FOREIGN KEY (site_id) REFERENCES public.site(id) ON DELETE RESTRICT;


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
-- Name: artistrecord artistrecord_artist_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artistrecord
    ADD CONSTRAINT artistrecord_artist_id_fkey FOREIGN KEY (artist_id) REFERENCES public.artist(id) ON DELETE CASCADE;


--
-- Name: artistrecord artistrecord_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artistrecord
    ADD CONSTRAINT artistrecord_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artistrecord artistrecord_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artistrecord
    ADD CONSTRAINT artistrecord_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


--
-- Name: artistrecord artistrecord_photo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artistrecord
    ADD CONSTRAINT artistrecord_photo_fkey FOREIGN KEY (photo) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: artistrecord artistrecord_video_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.artistrecord
    ADD CONSTRAINT artistrecord_video_fkey FOREIGN KEY (video) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: audiostudio audiostudio_audiospeechaccesible_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_audiospeechaccesible_fkey FOREIGN KEY (audiospeechaccesible) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: audiostudio audiostudio_audiospeechmusicaccesible_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_audiospeechmusicaccesible_fkey FOREIGN KEY (audiospeechmusicaccesible) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: audiostudio audiostudio_music_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audiostudio
    ADD CONSTRAINT audiostudio_music_id_fkey FOREIGN KEY (music_id) REFERENCES public.music(id) ON DELETE RESTRICT;


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
-- Name: voice elvoice_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voice
    ADD CONSTRAINT elvoice_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: voice elvoice_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voice
    ADD CONSTRAINT elvoice_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


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
-- Name: guidecontent guidecontent_audioaccesible_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontent
    ADD CONSTRAINT guidecontent_audioaccesible_fkey FOREIGN KEY (audioaccessible) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: guidecontentrecord guidecontentrecord_audioaccesible_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.guidecontentrecord
    ADD CONSTRAINT guidecontentrecord_audioaccesible_fkey FOREIGN KEY (audioaccessible) REFERENCES public.resource(id) ON DELETE RESTRICT;


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
-- Name: music music_audio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.music
    ADD CONSTRAINT music_audio_fkey FOREIGN KEY (audio) REFERENCES public.resource(id) ON DELETE RESTRICT;


--
-- Name: music music_composer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.music
    ADD CONSTRAINT music_composer_fkey FOREIGN KEY (composer) REFERENCES public.person(id) ON DELETE RESTRICT;


--
-- Name: music music_lastmodifieduser_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.music
    ADD CONSTRAINT music_lastmodifieduser_fkey FOREIGN KEY (lastmodifieduser) REFERENCES public.users(id) ON DELETE RESTRICT;


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

