-- ------------------------------------------------------------------------------------------------------------------------------
--
-- Dellemuse / Allemuse
--
-- Un museo (del latín, musēum y este, a su vez, del griego, Μουσείον, 'santuario de las musas')
-- en italiano «luogo sacro alle Muse»
--
--

-- psql dellemuse

-- dellemuse=# set client_encoding to 'utf8';
-- dellemuse=# \i dellemuse-datos.sql


-- ------------------------------------------------------------------------------------------------------------------------------
BEGIN;
set client_encoding to 'utf8';
COMMIT;
END;


BEGIN;
  
CREATE TABLE floorrecord if not exists (
    id bigint primary key default nextval('sequence_id'),
    floor_id bigint references floor(id) ON DELETE CASCADE not null,
    language character varying(24) default 'es' NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    photo bigint references resource(id) ON DELETE SET NULL,
    video bigint references resource(id) ON DELETE SET NULL,
    audio bigint references resource(id) ON DELETE SET NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint references users(id) on delete restrict,
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
    opens_hash bigint  DEFAULT 0,    
    audioautogenerate  boolean DEFAULT true,
    audioauto boolean  DEFAULT false,    
    infoaccessible text, 
    infoaccesible_hash bigint default 0, 
    audioaccessible bigint references resource(id) ON DELETE SET NULL
);



CREATE TABLE roomrecord if not exists (
    id bigint primary key default nextval('sequence_id'),
    room_id bigint references room(id) ON DELETE CASCADE not null,
    language character varying(24) default 'es' NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    photo bigint references resource(id) ON DELETE SET NULL,
    video bigint references resource(id) ON DELETE SET NULL,
    audio bigint references resource(id) ON DELETE SET NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint references users(id) on delete restrict,
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
    opens_hash bigint  DEFAULT 0,    
    audioautogenerate  boolean DEFAULT true,
    audioauto boolean  DEFAULT false,    
    infoaccessible text, 
    infoaccesible_hash bigint default 0, 
    audioaccessible bigint references resource(id) ON DELETE SET NULL
);
 
	
END;











