-- ------------------------------------------------------------------------------------------------------------------------------
--
-- Dellemuse / Allemuse
--
-- Un museo (del latín, musēum y este, a su vez, del griego, Μουσείον, 'santuario de las musas')
-- en italiano «luogo sacro alle Muse»

 
-- Crea Tablas 
-- Usuarios canonicos (deberia ser solo root)
-- y datos generales para las tablas maestras

-- Si se agregan las variables de entorno pguser y pgpassword no hace falta mas parametros:

--  dropdb dellemuse
--  createdb dellemuse
--  psql -f dellemuse-tablas.sql dellemuse

-- sequence sirve para generar ids para las filas de las tablas


BEGIN;
set client_encoding to 'utf8';
COMMIT;


BEGIN;



CREATE SEQUENCE if not exists objectstorage_id 	START 100;
CREATE SEQUENCE if not exists sequence_id 		START 100;
CREATE SEQUENCE if not exists sequence_user_id 	START 100;
CREATE SEQUENCE if not exists readcode_id 		START 1000;
CREATE SEQUENCE if not exists audit_id 			START 100;


-- ------------------------------------------------------------------------------------------------------------------------------
-- para usuarios de la aplicación
-- ------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE users (
						id					bigint primary key default nextval('sequence_user_id'),
						
						name				character varying(512),
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),
						
						username			character varying(512) not null unique,
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null						
					);
					
alter table users add column  lastmodifieduser bigint references users(id) on delete restrict;


-- ------------------------------------------------------------------------------------------------------------------------------
-- resource
-- ------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE resource (
						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512) not null,
						nameKey				character varying(512),
						title		 		character varying(1024),
						titleKey			character varying(512),

						media				character varying(64),
						size				bigint default 0,
						
						info		 		text,
						infoKey 			character varying(512),
						
						bucketName			character varying(512) not null,
						objectName			character varying(512) not null,
						
						binaryObject		bytea,
						
						usethumbnail 		boolean default true,
							
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);
					
-- ------------------------------------------------------------------------------------------------------------------------------
-- persona
-- ------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE person (
						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512),
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),

						lastname			character varying(512) not null,
						lastnameKey			character varying(512),
						
						sortlastfirstname character varying(1024),
	 					
						displayname			character varying(512),
						
						nickname			character varying(512),
						
						sex					character varying(2),
						isex				integer default 1,  -- male=1 female=0  
   					
						physicalid			character varying(64),
						address				character varying(4096),
						zipcode				character varying(64),
						phone				character varying(512),
						email				character varying(512),
						birthdate			timestamp with time zone,

						user_id				bigint references users(id) on delete restrict,
						

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						

						photo				bigint references resource(id) on delete restrict,
						video				bigint references resource(id) on delete restrict,
						audio				bigint references resource(id) on delete restrict,

						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);

-- root user
					
INSERT INTO users (id, username, lastmodifieduser) VALUES  
(nextval('sequence_user_id'), 'root', 	(select currval('sequence_user_id')));




-- ------------------------------------------------------------------------------------------------------------------------------						
-- Institution (institution > site > floor > room > artworkdisplayed)
-- ------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE institutionType (
						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						);


INSERT INTO institutionType (id, name, created, lastmodified, lastmodifieduser) 
VALUES  (nextval('sequence_id'), 'Museo', now(), now(), (select id from users where username='root')),
		(nextval('sequence_id'), 'Monumento', now(), now(), (select id from users where username='root')),
		(nextval('sequence_id'), 'Centro cultural', now(), now(), (select id from users where username='root')),		
		(nextval('sequence_id'), 'Parque', now(), now(), (select id from users where username='root')),
		(nextval('sequence_id'), 'Edificio religioso', now(), now(), (select id from users where username='root')),
		(nextval('sequence_id'), 'Teatro', now(), now(), (select id from users where username='root')),
		(nextval('sequence_id'), 'Plaza', now(), now(), (select id from users where username='root')),
		(nextval('sequence_id'), 'Edificio histórico', now(), now(), (select id from users where username='root')),
		(nextval('sequence_id'), 'Galería de Arte', now(), now(), (select id from users where username='root')),
		(nextval('sequence_id'), 'Zoo', now(), now(), (select id from users where username='root'));
		
		
CREATE TABLE institution (
						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512) not null,
						nameKey				character varying(512),

						shortName			character varying(512),
						
						title		 		character varying(1024),
						titleKey			character varying(512),
						
						institutionType_id bigint references institutionType(id) on delete restrict, 
						

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),

						address		 		text,
						addressKey 			character varying(512),

						moreinfo	 		text,
						moreinfoKey			character varying(512),

						website		 		character varying(1024),
						mapurl		 		character varying(1024),
						email		 		character varying(1024),
						instagram	 		character varying(1024),
						whatsapp	 		character varying(1024),
						phone		 		character varying(1024),
						twitter		 		character varying(1024),
						

						logo				bigint references resource(id) on delete restrict,
						
						photo				bigint references resource(id) on delete restrict,
						video				bigint references resource(id) on delete restrict,
						audio				bigint references resource(id) on delete restrict,
						map					bigint references resource(id) on delete restrict,			

						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						);


-- Site (Sucursales de las instituciones)

						
CREATE TABLE siteType (
						
						id					bigint primary key default nextval('sequence_id'),
						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),

						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						);

INSERT INTO siteType (id, name, created, lastmodified, lastmodifieduser)
VALUES  (nextval('sequence_id'), 'Sede central', now(), now(), (select id from users where username='root'));

						
CREATE TABLE site (
						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512) not null,
						namekey				character varying(512),
						shortname			character varying(512),

						title		 		character varying(1024),
						titlekey			character varying(512),

						sitetype_id       	bigint references siteType(id) on delete restrict,
						institution_id      bigint references institution(id) on delete restrict not null,

						website		 		character varying(1024),
						mapurl 		 		character varying(1024),
						email		 		character varying(1024),
						instagram	 		character varying(1024),
						whatsapp	 		character varying(1024),
						phone		 		character varying(1024),
						twitter		 		character varying(1024),
 						
						opens 				character varying(2048),
						openskey			character varying(512),
												
						subtitle		 	character varying(1024),
						subtitlekey			character varying(512),

						abstract	 		text,
						info		 		text,
						
						
						infokey 			character varying(512),
						
						welcome 			text,
						welcomekey 			character varying(512),
						
						
						address		 		text,
						addresskey 			character varying(512),

						logo				bigint references resource(id) on delete restrict,

						photo				bigint references resource(id) on delete restrict,
						video				bigint references resource(id) on delete restrict,
						audio				bigint references resource(id) on delete restrict,
						map					bigint references resource(id) on delete restrict,			
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);

-- Site > floor

CREATE TABLE floorType (
						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),

						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);

INSERT INTO floorType (id, name, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'), 'Exhibición', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Pabellón temporario', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Administración', now(), now(), (select id from users where username='root'));

					
					
CREATE TABLE floor (
						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),

						
						floorType_id		bigint references floorType(id) on delete restrict,
						site_id			    bigint references site(id) on delete restrict not null,

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						
						floorNumber			character varying(256),
						floorNumberKey		character varying(512),
						
						map					bigint references resource(id) on delete restrict,
						photo				bigint references resource(id) on delete restrict,
						video				bigint references resource(id) on delete restrict,
						audio				bigint references resource(id) on delete restrict,


						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);



-- Site > floor > room

CREATE TABLE roomType (
						id					bigint primary key default nextval('sequence_id'),
						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),

						

						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);

INSERT INTO roomType (id, name, created, lastmodified, lastmodifieduser) VALUES 
(nextval('sequence_id'), 'Sala de exhibición', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Pasillo', now(), now(), (select id from users where username='root'));

					
CREATE TABLE room (
						id					bigint primary key default nextval('sequence_id'),

						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),

						
						roomType_id			bigint references roomType(id) on delete restrict,
						floor_id      		bigint references floor(id) on delete restrict not null,

						roomNumber			character varying(256),
						roomNumberKey		character varying(512),
	
						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),

						map					bigint references resource(id) on delete restrict,
						photo				bigint references resource(id) on delete restrict,
						video				bigint references resource(id) on delete restrict,
						audio				bigint references resource(id) on delete restrict,


						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);

						
-- ------------------------------------------------------------------------------------------------------------------------------
-- Pintura, Escultura, Performance, Edificio
-- ------------------------------------------------------------------------------------------------------------------------------
								
CREATE TABLE artWorkType (
						id					bigint primary key default nextval('sequence_id'),
						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),

						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);

INSERT INTO artWorkType (id, name, namekey, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'), 'Vaso', 'vase', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Disco', 'disk', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Figura', 'figure', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Abanico', 'fan', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Miniatura', 'miniature', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Grabado', 'engraving', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Mueble', 'furniture', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Fotografía', 'photo', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Estructura cinética', 'cinetic structure', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Textil', 'textile', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Pintura', 'painting', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Jardín', 'garden', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Escultura', 'sculpture', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Edificio', 'building', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Instalación', 'art installation', now(), now(), (select id from users where username='root'));
   

CREATE TABLE artwork (
						
						id					bigint primary key default nextval('sequence_id'),

						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),

						shortName 			character varying(64),

						artWorkType_id		bigint references artWorkType(id) on delete restrict,

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						spec		 		text,
						specKey				character varying(512),
						
						info		 		text,
						infoKey 			character varying(512),

						intro		 		text,
						introKey 			character varying(512),
						
						photo				bigint references resource(id) on delete restrict,
						video				bigint references resource(id) on delete restrict,
						audio				bigint references resource(id) on delete restrict,
						
						year				integer default 0,
						
						person_owner_id			bigint references person(id) on delete restrict,
						institution_owner_id	bigint references institution(id) on delete restrict,
						site_owner_id			bigint references site(id) on delete restrict,
						
						usethumbnail 			boolean default true,
						url 					character varying(1024),
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						
						);


						
CREATE TABLE artworkArtist (
						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512),
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),


						artwork_id 	 	    bigint references artwork(id) on delete restrict not null,
						person_id			bigint references person(id) on delete restrict not null,
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						);
					
						
CREATE TABLE institutionalContent (
			
						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),


						institution_id 		bigint references institution(id) on delete restrict,
						site_id		 		bigint references institution(id) on delete restrict,

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						
						photo				bigint references resource(id) on delete restrict,
						video				bigint references resource(id) on delete restrict,
						audio				bigint references resource(id) on delete restrict,


						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);
					
					
					
-- Art exhibition status type

CREATE TABLE artExhibitionStatusType (
						id					bigint primary key default nextval('sequence_id'),
						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),

						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						);

INSERT INTO artExhibitionStatusType (id, name, namekey, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'), 'En preparación', 'coming', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'En curso', 'enabled', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Terminada', 'terminated', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Cancelada', 'cancelled', now(), now(), (select id from users where username='root'));


-- ------------------------------------------------------------------------------------------------------------------------------
-- art exhibition
-- ------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE artExhibition (

						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512) not null,
						nameKey				character varying(512),

						shortName			character varying(512),
						shortNameKey		character varying(512),
							
							
						title		 		character varying(1024),
						titleKey			character varying(512),

						artExhibitionStatusType_id	 	bigint references artExhibitionStatusType(id) on delete restrict,
						site_id 						bigint references site(id) on delete restrict,

						
						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						
						intro 				text,
						introKey 			character varying(512),
						
						
						opens 				character varying(2048),
						opensKey 			character varying(512),
						
						website		 		character varying(1024),
						mapurl		 		character varying(1024),
						email		 		character varying(1024),
						instagram	 		character varying(1024),
						whatsapp	 		character varying(1024),
						phone		 		character varying(1024),
						twitter		 		character varying(1024),

						location 			character varying (4096);
						
						
						photo				bigint references resource(id) on delete restrict,
						video				bigint references resource(id) on delete restrict,
						audio				bigint references resource(id) on delete restrict,

						
						permanent			boolean default true,
						
						fromDate			timestamp with time zone DEFAULT now() not null,
						toDate				timestamp with time zone DEFAULT now() not null,

						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						);

-- ------------------------------------------------------------------------------------------------------------------------------
-- art exhibition work 
-- ------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE artExhibitionItem (

						id					bigint primary key default nextval('sequence_id'),

						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),
						
						artExhibition_id 		bigint references artExhibition(id) on delete restrict,
						artWork_id 				bigint references artWork(id) on delete restrict not null,

						site_id 				bigint references site(id) on delete restrict,
						floor_id 				bigint references floor(id) on delete restrict,
						room_id 				bigint references room(id) on delete restrict,
										
						artExhibitionOrder		integer default 0,

						mapurl		 			character varying(1024),
						website		 			character varying(1024),
						
						readcode	 			character varying(1024),
						qcode		 			character varying(1024),

						info		 			text,
						infoKey 				character varying(512),
						
						created					timestamp with time zone DEFAULT now() not null,
						lastmodified			timestamp with time zone DEFAULT now() not null,
						lastmodifieduser		bigint references users(id) on delete restrict not null
						);
						
-- ------------------------------------------------------------------------------------------------------------------------------
-- art exhibition guide
-- ------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE artExhibitionGuide (
			
						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),

						publisher_id 		bigint references person(id) on delete restrict,
						artExhibition_id 	bigint references artExhibition(id) on delete restrict,
						
						artExhibitionGuideOrder	integer default 0,
						
						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						official			boolean default false,
						info		 		text,
						infoKey 			character varying(512),
						
						photo				bigint references resource(id) on delete restrict,
						video				bigint references resource(id) on delete restrict,
						audio				bigint references resource(id) on delete restrict,

						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						);
				
						
CREATE TABLE guideContent (
						
						id					bigint primary key default nextval('sequence_id'),

						name				character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),


						artExhibitionGuide_id	bigint references artExhibitionGuide(id) on delete restrict not null,
						artExhibitionItem_id   	bigint references artExhibitionItem (id) on delete restrict not null,

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),

						guideOrder			integer default 0,	
						
						photo				bigint references resource(id) on delete restrict,
						video				bigint references resource(id) on delete restrict,
						audio				bigint references resource(id) on delete restrict,


						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						
						);
						
						
						
						
						
						
						
						
						
						
-- Favorites



CREATE TABLE Favorite (
						
						id					bigint primary key default nextval('sequence_id'),
						
						name				character varying(512) not null,
						nameKey				character varying(512) default regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g'),

						title		 		character varying(1024),
						titleKey			character varying(512),
						
						person_id			bigint references person(id) on delete restrict not null,
						site_id				bigint references site(id) on delete restrict not null,
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						
						);						


						
COMMIT;



	
 

CREATE TABLE elvoice (
						
						id					 bigint primary key default nextval('sequence_id'),

						name				 character varying(512) not null,
						nameKey				 character varying(512),

						title		 		 character varying(1024),
						titleKey			 character varying(512),

						url	 			     character varying(4096),
						state			     integer default 3,
						
						voiceid				 character varying(512),
						language		  	 character varying(64) default 'es',
						languageRegion       character varying(64),
						comment			 	 text,
						voiceSettings		 jsonb,	
						
						info		 		 text,
						infoKey 			 character varying(512),
						
						draft				 text,
						
						audio				 bigint references resource(id) on delete restrict,
						
						created				 timestamp with time zone DEFAULT now() not null,
						lastmodified		 timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	 bigint references users(id) on delete restrict not null
						
						);


					
