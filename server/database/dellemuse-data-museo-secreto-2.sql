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


INSERT INTO person (id, name, lastname,  physicalid, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'), 'Nicolás',  'García Uriburu',  '-', now(), now(), (select id from users where username='root'));

INSERT INTO person (id, name, lastname,  physicalid, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'),  'Mariano',  'Fortuny y Marsal',  '-', now(), now(), (select id from users where username='root'));


-- Utopia del Sur

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Utopia del Sur', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Utopia del Sur',
 '',
 'Oleo sobre tela 180 x 190 cm.',
 1993,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'García Uriburu le dedicó vida y obra: una proclama urgente por el cuidado del medio ambiente, “La globalización ha ligado salvajemente nuestras economías, creando una cruel dependencia que ha dividido aún más a la población mundial. Los países desarrollados contaminan el agua con fluidos tóxicos, derraman petróleo en nuestros mares y ríos, sin reparar el daño que ocasionan. Hace más de cuarenta años que intento dar una alarma contra la contaminación de ríos y mares, y es a través de mis acciones artísticas en distintos puntos del planeta que he transformado mi obra en una suerte de alerta contestataria globalizadora. Hoy y con más motivos que hace cuarenta años, sigo denunciando la contaminación del agua, y la salvaje destrucción que hacemos de las reservas del planeta. Un planeta que en nuestra ciega omnipotencia creemos inagotable e indestructible”.',
 (select id from users where username='root'));
 
INSERT INTO artworkArtist (id, artwork_id, person_id) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Utop%' limit 1), 
 (select id from person where lastname like '%Uribu%' limit 1)); 

 

-- fortuny
 
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Procesión sorprendida por la lluvia', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Procesión sorprendida por la lluvia',
 '',
 'Oleo sobre tela 63 x 102 cm. - Marco: 101,5 x 140 cm.',
 2017,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Esta obra constituye un ejemplo sobresaliente del costumbrismo practicado por Mariano Fortuny, de gran influencia en la pintura española del último tercio del siglo XIX, aunque en este caso se aleja del tratamiento preciosista con el que alcanzó éxito y fama internacionales, al punto de haber sido considerada un boceto por la falta de atención al detalle .',
 (select id from users where username='root'));

INSERT INTO artworkArtist (id, artwork_id, person_id) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%lluvia%' limit 1), 
 (select id from person where lastname like '%ortun%' limit 1));


 
 
 -- item
 INSERT INTO artExhibitionItem  (id,  name, title, artExhibition_id, artWork_id, artExhibitionOrder, readcode, site_id, floor_id,    lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select name from artwork where name like '%Utop%' limit 1), 
 (select title from artwork where name like '%Utop%' limit 1),
 (select id from artExhibition where name like 'Secre%' limit 1),
 (select id from artWork where name like '%Utop%' limit 1),
  1,
  nextval('readcode_id'),
  (select id from site where name like '%Nacional de Bellas Artes%' limit 1),
  (select id from floor where name like 'Primer%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from users where username='root'));
 

 INSERT INTO artExhibitionItem (id, name, title, artExhibition_id, artWork_id, artExhibitionOrder, readcode, site_id, floor_id,    lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select name from artwork where name like 'Proces%' limit 1), 
 (select title from artwork where name like 'Proces%' limit 1),
 (select id from artExhibition where name like 'Secre%' limit 1),
 (select id from artWork where name like 'Proces%' limit 1),
  2,
  nextval('readcode_id'),
  (select id from site where name like '%Nacional de Bellas Artes%' limit 1),
  (select id from floor where name like 'Primer%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
   (select id from users where username='root'));
 
  

-- 1. Utopia
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 1,
 (select name from artwork where name like 'Utop%' limit 1), 
 (select title from artwork where name like 'Utop%' limit 1), 
 (select id from artExhibitionGuide where lower(name) like '%secre%' limit 1),  
 (select id from artExhibitionItem where name like 'Utop%' limit 1),  
 (select id from users where username='root'));

-- 2 Procesion

INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 2,
 (select name from artwork where name like 'Proce%' limit 1), 
 (select title from artwork where name like 'Proce%' limit 1), 
 (select id from artExhibitionGuide where lower(name) like '%secre%' limit 1),  
 (select id from artExhibitionItem where name like '%Proce%' limit 1),  
 (select id from users where username='root'));


UPDATE artWork SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');
update artwork set site_owner_id = (select id from site where lower(name) like '%nacional%' limit 1) where site_owner_id is null;
update person set sortlastfirstname = concat( lower(lastname), ' ', lower(name));


COMMIT;

