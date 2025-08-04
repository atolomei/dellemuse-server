-- ------------------------------------------------------------------------------------------------------------------------------
--
-- dellemuse
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

BEGIN;


-- artista German Gargano

INSERT INTO person (id, name, lastname,  physicalid, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'),      'Germán', 'Gárgano',    '-', now(), now(), (select id from users where username='root'));


-- Sendas perdias

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Sendas perdidas', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Sendas perdidas',
 '',
 'Carbonilla y temple sobre tela. 135 x 165 cm.',
 2015,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Como lo han hecho artistas de todas las épocas, Gárgano nos propone su propia versión del infierno, construido pacientemente sobre el papel a lo largo de muchos años. 
Sus trabajos no se alimentan directamente de la realidad: crean una realidad propia, que se nutre de otras imágenes. Cada obra está hecha de otras obras, y por eso mismo conforma un mundo.',
 (select id from users where username='root'));
 
INSERT INTO artworkArtist (id, artwork_id, person_id) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Sendas%' limit 1), 
 (select id from person where lastname like '%rgano%' limit 1)); 

 

-- Apocalisis
 
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Apocalipsis', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Apocalipsis',
 '',
 'Gouache sobre papel. 75 x 55 cm.',
 2017,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Según Dante Alighieri, el infierno es un cono invertido, dividido en nueve círculos que se estrechan a medida que descendemos. Para acceder a él hay que cruzar el río Aqueronte, guiados por Caronte, el barquero. Ningún viajero ha regresado para confirmar esa travesía. Por eso, Gárgano nos propone su propia versión del infierno, construido pacientemente sobre el papel a lo largo de muchos años. En la trama de líneas, el artista ofrece sus indicios, y nos corresponde a nosotros cruzar el río e interpretar aquello que vemos.',
 (select id from users where username='root'));

INSERT INTO artworkArtist (id, artwork_id, person_id) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%pocalip%' limit 1), 
 (select id from person where lastname like '%rgano%' limit 1));


 -- El recurso del método

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'El recurso del método', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'El recurso del método',
 '',
 'Tinta sobre papel 30 x 40 cm.',
 2012,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'El recurso del método es una novela del escritor cubano Alejo Carpentier. Publicada en 1974, se adscribe al subgénero de la literatura hispanoamericana conocido como novela del dictador.',
 (select id from users where username='root'));

INSERT INTO artworkArtist (id, artwork_id, person_id) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%recurso%' limit 1), 
 (select id from person where lastname like '%rgano%' limit 1));

 
 -- ArtExhibition -> sendas perdidas

 
INSERT INTO artExhibition (id,	 permanent, name, site_id, title, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 false,
 'Sendas perdidas', 
  (select id from site where name like '%Bellas Artes%' limit 1),
  'Sendas perdidas',
  'El Museo Nacional de Bellas Artes inaugura el martes 15 de julio, a las 18, la exposición temporaria “Germán Gárgano. Sendas perdidas”, que reúne en las salas del segundo piso más de 170 obras realizadas en los últimos años por el artista argentino.',
 (select id from users where username='root'));

  
 -- item
 INSERT INTO artExhibitionItem  (id,  name, title, artExhibition_id, artWork_id, artExhibitionOrder, readcode, site_id, floor_id,    lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select name from artwork where name like '%Sendas%' limit 1), 
 (select title from artwork where name like '%Sendas%' limit 1),
 (select id from artExhibition where name like 'Sendas%' limit 1),
 (select id from artWork where name like '%Sendas%' limit 1),
  1,
  nextval('readcode_id'),
  (select id from site where name like '%Nacional de Bellas Artes%' limit 1),
  (select id from floor where name like 'Primer%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from users where username='root'));
 

 INSERT INTO artExhibitionItem (id, name, title, artExhibition_id, artWork_id, artExhibitionOrder, readcode, site_id, floor_id,    lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select name from artwork where name like 'Apocalipsis%' limit 1), 
 (select title from artwork where name like 'Apocalipsis%' limit 1),
 (select id from artExhibition where name like 'Sendas%' limit 1),
 (select id from artWork where name like '%Apoca%' limit 1),
  2,
  nextval('readcode_id'),
  (select id from site where name like '%Nacional de Bellas Artes%' limit 1),
  (select id from floor where name like 'Primer%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
   (select id from users where username='root'));
 
 INSERT INTO artExhibitionItem  (id, name, title, artExhibition_id, artWork_id, artExhibitionOrder, readcode, site_id, floor_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select name from artwork where name like '%ecurso%' limit 1), 
 (select title from artwork where name like '%ecurso%' limit 1),
 (select id from artExhibition where name like 'Sendas%' limit 1),
 (select id from artWork where name like '%ecurso%' limit 1),
  2,
  nextval('readcode_id'),
  (select id from site where name like '%Nacional de Bellas Artes%' limit 1),
  (select id from floor where name like 'Primer%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
  (select id from users where username='root'));
 
 
 
delete from artExhibitionGuide where name like 'Sendas%';

insert into artExhibitionGuide (id, name, title, publisher_id, artexhibition_id, official, lastmodifiedUser) values (nextval('sequence_id'),  'Sendas perdidas',  'Sesndas perdidas', (select id from person where name like 'Alejandro%' limit 1), 
(select id from artExhibition where name like 'Sendas%' limit 1), true,  (select id from users where username='root'));  


-- 1. Sendas
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 1,
 (select name from artwork where name like 'Sendas%' limit 1), 
 (select title from artwork where name like 'Sendas%' limit 1), 
 (select id from artExhibitionGuide where lower(name) like 'sendas%' limit 1),  
 (select id from artExhibitionItem where name like 'Sendas%' limit 1),  
 (select id from users where username='root'));

-- 2 Apocalipsis

INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 2,
 (select name from artwork where name like 'Apoca%' limit 1), 
 (select title from artwork where name like 'Apoca%' limit 1), 
 (select id from artExhibitionGuide where lower(name) like '%sendas%' limit 1),  
 (select id from artExhibitionItem where name like '%Apoca%' limit 1),  
 (select id from users where username='root'));


-- Recurso del 
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 3,
 (select name from artwork where name like '%ecurso%' limit 1), 
 (select title from artwork where name like '%ecurso%' limit 1), 
 (select id from artExhibitionGuide where lower(name) like '%sendas%' limit 1),  
 (select id from artExhibitionItem where name like '%ecurso%' limit 1),  
 (select id from users where username='root'));


UPDATE  artWork  SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');

update artexhibition set name = 'Sendas perdidas', title='Sendas perdidas' where lower(name) like 'sendas%';
update artexhibition set nameKey='sendas-perdidas' where name like 'Senda%';

update artwork set site_owner_id = (select id from site where lower(name) like '%nacional%' limit 1) where site_owner_id is null;

alter table artwork add column if not exists usethumbnail boolean default true;
alter table resource add column if not exists usethumbnail boolean default true;

update artwork set usethumbnail=false where name like 'Sendas%';

update resource set usethumbnail=false where name like '%endas%';
update resource set usethumbnail=true where name like '%muestra%';
update  artexhibitionguide set title='Sendas perdidas' where title like 'Sesn%';


update artexhibition set intro = 'Germán Gárgano fue preso político en Argentina desde 1975 hasta 1982, y  mantuvo una relación epistolar desde la cárcel con el pintor Carlos Gorriarena, con quien continuaría formándose al recuperar su libertad.  “Sendas perdidas” es su primera muestra en el Museo Nacional de Bellas Artes, con más de 170 dibujos, gouaches, acuarelas y tintas realizadas en los últimos años.' where lower(name) like 'sendas%';
update artexhibitionGuide set info = 'Germán Gárgano fue preso político en Argentina desde 1975 hasta 1982, y  mantuvo una relación epistolar desde la cárcel con el pintor Carlos Gorriarena, con quien continuaría formándose al recuperar su libertad.  “Sendas perdidas” es su primera muestra en el Museo Nacional de Bellas Artes, con más de 170  dibujos, gouaches, acuarelas y tintas realizadas en los últimos años.' where name like 'Sendas%';
update artexhibition set intro ='La consolidación de un modelo nacional en el arte argentino fue un proceso que se dió a fines del siglo XIX y principios del XX, marcado por la búsqueda de una identidad visual propia y la creación de instituciones que promoviesen el arte nacional. Se buscó reflejar la realidad argentina, las costumbres locales y la historia del país, diferenciándose de las influencias europeas.' where name like '%siglo%';
update artexhibition set intro ='La extensa colección de arte impresionista y postimpresionista del museo incluye obras de artistas destacados, como Édouard Manet, Claude Monet, Camille Pissarro, Berthe Morisot, Edgar Degas, Vincent Van Gogh, Paul Gauguin y Henri Toulouse Lautrec, entre otros.' where name like '%mpresioni%';
update artexhibition set website='https://www.bellasartes.gob.ar/exhibiciones/german-gargano/' where name like 'Sendas%';
update artexhibition set website='https://www.bellasartes.gob.ar/exhibiciones/museo-secreto/' where name like '%ecreto%';


update artexhibitionguide set info=null where name like 'Sendas%';
update artexhibition set info=null where name like 'Sendas%';
update artexhibitionguide set intro=null where name like 'Sendas%';

--
--
-- En la segunda mitad del siglo XIX, el arte argentino se caracterizó por el retrato, donde los artistas plasmaban a los personajes relevantes de la nueva nación, siguiendo los cánones neoclásicos de la época.
-- Luego a fines del siglo XIX y principios del XX, se buscó una identidad visual propia, con la creación de instituciones como El Ateneo, donde literatos y artistas debatiían sobre la existencia de un "arte nacional".
-- 'Del 16 de julio de 2025 al 17 de agosto de 2025' 
--
--

COMMIT;

