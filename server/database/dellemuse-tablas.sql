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

CREATE SEQUENCE if not exists sequence_id 		START 100;
CREATE SEQUENCE if not exists sequence_user_id 	START 100;
CREATE SEQUENCE if not exists readcode_id 		START 1000;

-- ------------------------------------------------------------------------------------------------------------------------------
-- para usuarios de la aplicación
-- ------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE users (
						id					bigint primary key default nextval('sequence_user_id'),
						username			character varying(512) not null,
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null						
					);
					
alter table users add column  lastmodifieduser bigint references users(id) on delete restrict;

-- ------------------------------------------------------------------------------------------------------------------------------
-- persona
-- ------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE person (
						id					bigint primary key,
						name				character varying(512) not null,
						lastname			character varying(512) not null,

						nickname			character varying(512),
						
						sex					character varying(2),
						physicalid			character varying(32) not null,
						address				character varying(4096),
						zipcode				character varying(64),
						phone				character varying(512),
						email				character varying(512),
						birthdate			timestamp with time zone,

						user_id				bigint references users(id) on delete restrict,
						
						title		 		character varying(1024),
						titleKey			character varying(512),

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						
						photo				bytea,
						photoKey			character varying(512),
						
						video				bytea,
						videoKey			character varying(512),
						
						audio				bytea,
						audioKey			character varying(512),

						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);

-- root user
					
INSERT INTO users (id, username, lastmodifieduser) VALUES  
(nextval('sequence_user_id'), 'root', 	(select currval('sequence_user_id')));

INSERT INTO person (id, name, lastname, physicalid, created, lastmodified, lastmodifieduser) 
VALUES  (nextval('sequence_id'), '-', 'root', '', now(), now(), (select id from users where username='root'));


-- ------------------------------------------------------------------------------------------------------------------------------						
-- Institution (institution > site > floor > room > artworkdisplayed)
-- ------------------------------------------------------------------------------------------------------------------------------

CREATE TABLE institutionType (
						id					bigint primary key,
						
						name				character varying(512) not null,
						nameKey				character varying(512),
						
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
						id					bigint primary key,
						
						name				character varying(512) not null,
						nameKey				character varying(512),
						
						institutionType_id bigint references institutionType(id) on delete restrict, 
						
						title		 		character varying(1024),
						titleKey			character varying(512),

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),

						address		 		text,
						addressKey 			character varying(512),

						moreinfo	 		text,
						moreinfoKey			character varying(512),

						photo				bytea,
						photoKey			character varying(512),
						
						video				bytea,
						videoKey			character varying(512),
						
						audio				bytea,
						audioKey			character varying(512),


						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						);


-- Site (Sucursales de las instituciones)

						
CREATE TABLE siteType (
						
						id					bigint primary key,
						name				character varying(512) not null,
						nameKey				character varying(512),

						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						);

INSERT INTO siteType (id, name, created, lastmodified, lastmodifieduser)
VALUES  (nextval('sequence_id'), 'Sede central', now(), now(), (select id from users where username='root'));

						
CREATE TABLE site (
						id					bigint primary key,
						
						name				character varying(512) not null,
						nameKey				character varying(512),

						siteType_id       	bigint references siteType(id) on delete restrict,
						institution_id      bigint references institution(id) on delete restrict not null,

						title		 		character varying(1024),
						titleKey			character varying(512),

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						
						address		 		text,
						addressKey 			character varying(512),
						
						photo				bytea,
						photoKey			character varying(512),
						
						video				bytea,
						videoKey			character varying(512),
						
						audio				bytea,
						audioKey			character varying(512),
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);

-- Site > floor

CREATE TABLE floorType (
						id					bigint primary key,
						
						name				character varying(512) not null,
						nameKey				character varying(512),
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);

INSERT INTO floorType (id, name, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'), 'Exhibición', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Pabellón temporario', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Administración', now(), now(), (select id from users where username='root'));

					
					
CREATE TABLE floor (
						id					bigint primary key,
						
						name				character varying(512) not null,
						nameKey			    character varying(512),
						
						floorType_id		bigint references floorType(id) on delete restrict,
						site_id			    bigint references site(id) on delete restrict not null,

						title		 		character varying(1024),
						titleKey			character varying(512),

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						
						floorNumber			character varying(256),
						floorNumberKey		character varying(512),
						
						photo				bytea,
						photoKey			character varying(512),
						
						video				bytea,
						videoKey			character varying(512),
						
						audio				bytea,
						audioKey			character varying(512),
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);



-- Site > floor > room

CREATE TABLE roomType (
						id					bigint primary key,
						name				character varying(512) not null,
						nameKey				character varying(512),
						
				--		title		 		character varying(1024),
				--		titleKey			character varying(512),

				--		subtitle		 	character varying(1024),
				--		subTitleKey			character varying(512),

				--		info		 		text,
				--		infoKey 			character varying(512),
						
				--		photo				bytea,
				--		photoKey			character varying(512),
						
				--		video				bytea,
				--		videoKey			character varying(512),
						
				--		audio				bytea,
				--		audioKey			character varying(512),

						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);

INSERT INTO roomType (id, name, created, lastmodified, lastmodifieduser) VALUES 
(nextval('sequence_id'), 'Sala de exhibición', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Pasillo', now(), now(), (select id from users where username='root'));

					
CREATE TABLE room (
						id					bigint primary key,

						name				character varying(512) not null,
						nameKey				character varying(512),
						
						roomType_id			bigint references roomType(id) on delete restrict,
						floor_id      		bigint references floor(id) on delete restrict not null,

						roomNumber			character varying(256),
						roomNumberKey		character varying(512),
	
						title		 		character varying(1024),
						titleKey			character varying(512),

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						
						photo				bytea,
						photoKey			character varying(512),
						
						video				bytea,
						videoKey			character varying(512),
						
						audio				bytea,
						audioKey			character varying(512),
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);

						
-- ------------------------------------------------------------------------------------------------------------------------------
-- Pintura, Escultura, Performance, Edificio
-- ------------------------------------------------------------------------------------------------------------------------------
								
CREATE TABLE artWorkType (
						id					bigint primary key,
						name				character varying(512) not null,
						nameKey				character varying(512),
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
						
						id					bigint primary key,
						name				character varying(512) not null,
						nameKey				character varying(512),
						
						artWorkType_id		bigint references artWorkType(id) on delete restrict,

						title		 		character varying(1024),
						titleKey			character varying(512),

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						spec		 		text,
						specKey				character varying(512),
						
						info		 		text,
						infoKey 			character varying(512),
						
						photo				bytea,
						photoKey			character varying(512),
						
						video				bytea,
						videoKey			character varying(512),
						
						audio				bytea,
						audioKey			character varying(512),
						
						year				integer default 0,
						
						person_owner_id			bigint references person(id) on delete restrict,
						institution_owner_id	bigint references institution(id) on delete restrict,
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						
						);

CREATE TABLE artworkArtist (
						id					bigint primary key,
						
						artwork_id 	 	    bigint references artwork(id) on delete restrict not null,
						person_id			bigint references person(id) on delete restrict not null,
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						);
					
						
CREATE TABLE institutionalContent (
			
						id					bigint primary key,
						
						name				character varying(512) not null,
						nameKey				character varying(512),

						institution_id 		bigint references institution(id) on delete restrict,
						site_id		 		bigint references institution(id) on delete restrict,

						title		 		character varying(1024),
						titleKey			character varying(512),

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						
						photo				bytea,
						photoKey			character varying(512),
						
						video				bytea,
						videoKey			character varying(512),
						
						audio				bytea,
						audioKey			character varying(512),
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
					);
					
					
					
-- Art exhibition status type

CREATE TABLE artExhibitionStatusType (
						id					bigint primary key,
						name				character varying(512) not null,
						nameKey				character varying(512),
						
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

						id					bigint primary key,
						
						name	 	   		character varying(512) not null,
						nameKey				character varying(512),

						artExhibitionStatusType_id	 	bigint references artExhibitionStatusType(id) on delete restrict,
						site_id 						bigint references site(id) on delete restrict,

						title		 		character varying(1024),
						titleKey			character varying(512),

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						
						photo				bytea,
						photoKey			character varying(512),
						
						video				bytea,
						videoKey			character varying(512),
						
						audio				bytea,
						audioKey			character varying(512),
						
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

						id					bigint primary key,

						name	 	   		character varying(512) not null,
						nameKey				character varying(512),

						title		 		character varying(1024),
						titleKey			character varying(512),
						
						artWork_id 				bigint references artWork(id) on delete restrict not null,

						site_id 				bigint references site(id) on delete restrict,
						floor_id 				bigint references floor(id) on delete restrict,
						room_id 				bigint references room(id) on delete restrict,
										
						ordinal					integer default 0,

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
			
						id					bigint primary key,
						
						name	 	   		character varying(512) not null,
						nameKey				character varying(512),

						publisher_id 		bigint references person(id) on delete restrict,
						artExhibition_id 	bigint references artExhibition(id) on delete restrict,
						
						title		 		character varying(1024),
						titleKey			character varying(512),

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						
						photo				bytea,
						photoKey			character varying(512),
						
						video				bytea,
						videoKey			character varying(512),
						
						audio				bytea,
						audioKey			character varying(512),
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						);
				
						
CREATE TABLE guideContent (
						
						id					bigint primary key,

						name	 	   		character varying(512) not null,
						nameKey				character varying(512),

						artExhibitionGuide_id	bigint references artExhibitionGuide(id) on delete restrict not null,
						artExhibitionItem_id   	bigint references artExhibitionItem (id) on delete restrict not null,

						title		 		character varying(1024),
						titleKey			character varying(512),

						subtitle		 	character varying(1024),
						subTitleKey			character varying(512),

						info		 		text,
						infoKey 			character varying(512),
						
						photo				bytea,
						photoKey			character varying(512),
						
						video				bytea,
						videoKey			character varying(512),
						
						audio				bytea,
						audioKey			character varying(512),
						
						created				timestamp with time zone DEFAULT now() not null,
						lastmodified		timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	bigint references users(id) on delete restrict not null
						
						);
COMMIT;
					
