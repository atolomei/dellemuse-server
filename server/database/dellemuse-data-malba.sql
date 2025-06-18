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


BEGIN;


-- MALBA 
INSERT INTO institution (id, shortname, name, website, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'), 'MALBA', 'Museo de Arte Latinoaméricano de Buenos Aires', 'https://www.malba.org.ar/', now(), now(), (select id from users where username='root'));

INSERT INTO site  (id,	name, shortname, website, institution_id, info,  created, lastmodified,	lastmodifieduser) VALUES
 (nextval('sequence_id'), 
  'Museo de Arte Latinoaméricano de Buenos Aires', 
  'MALBA',
  'https://www.malba.org.ar/',
  (select id from institution where name like 'Museo de Arte Latinoa%' limit 1), 
  'El Museo de Arte Latinoamericano de Buenos Aires (Malba) – Fundación Costantini, más conocido simplemente como MALBA es un museo argentino fundado en septiembre de 2001.Fue creado con el objetivo de coleccionar, preservar, estudiar y difundir el arte latinoamericano desde principios del siglo XX hasta la actualidad. Es una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región.\n  Lun y Mier-Dom 12:00 a 20:00 \n Av. Figueroa Alcorta 3415, CABA', 
  now(), 
  now(), 
  (select id from users where username='root'));

 
INSERT INTO floor  (id,	name, subtitle, site_id,  created, lastmodified,	lastmodifieduser) VALUES
(nextval('sequence_id'), 'Planta Baja', 'Arte europeo del siglo XII al XIX. Arte argentino del siglo XIX', 	(select id from site where name like 'Museo de Arte Latinoa%' limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Primer Piso', '', 																(select id from site where name like 'Museo de Arte Latinoa%' limit 1), now(), now(), (select id from users where username='root'));


-- MNBA > recoleta > Planta Baja > Sala A 
--
INSERT INTO room  (id,	name, roomNumber, floor_id,  created, lastmodified,	lastmodifieduser) VALUES 
(nextval('sequence_id'), 'Hall Central'                               ,'0', (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like 'Museo de Arte Latinoa%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Tienda del Museo'                           ,'' , (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like 'Museo de Arte Latinoa%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Sala 2. Arte europeo siglo XII al XVI'      ,'2' , (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like'Museo de Arte Latinoa%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Sala 3. Arte europeo siglo XV al XVIII'     , '3' , (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like 'Museo de Arte Latinoa%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Sala 4. Países Bajos siglo XVII'            , '4' , (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like 'Museo de Arte Latinoa%' limit 1)  limit 1), now(), now(), (select id from users where username='root'));


INSERT INTO room  (id,	name, roomNumber, floor_id,  created, lastmodified,	lastmodifieduser) VALUES 
(nextval('sequence_id'), 'Escuela de La Boca y realismo social'                             	  ,'' , (select id from floor where name like '%Primer%' and site_id in (select id from site where name like 'Museo de Arte Latinoa%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Arte latinoamericano y argentino 1910-1945'                             ,'' , (select id from floor where name like '%Primer%' and site_id in (select id from site where name like 'Museo de Arte Latinoa%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Exposición temporaria' 								 			      ,'' , (select id from floor where name like '%Primer%' and site_id in (select id from site where name like 'Museo de Arte Latinoa%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Informalismo y expresionismo abstracto'   							  , '',	(select id from floor where name like '%Primer%' and site_id in (select id from site where name like 'Museo de Arte Latinoa%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Arte concreto y abstracción 1945-1970'  			          			  ,'' , (select id from floor where name like '%Primer%' and site_id in (select id from site where name like 'Museo de Arte Latinoa%' limit 1)  limit 1), now(), now(), (select id from users where username='root'));


update site set abstract = 'Se specializa en el arte latinoamericano desde principios del siglo XX hasta la actualidad. \nEs una institución privada sin fines de lucro que conserva y exhibe un patrimonio de aproximadamente 400 obras de los principales artistas modernos y contemporáneos de la región.' where lower(name) like '%arte latinoam%';

update site set abstract = 'Es una de las instituciones públicas de arte más importantes de Argentina.\n Su colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad.' where lower(name) like '%bellas artes%';

update site set abstract = 'El Museo "Benito Quinquela Martín" está ubicado en el barrio de La Boca en la Ciudad Autónoma de Buenos Aires, Argentina. Fundado en el año 1938, está construido sobre terrenos originalmente donados por el pintor. Cuenta actualmente con la mayor colección reunida del artista plástico, más de 90 de sus obras. Además, el Museo ofrece un panorama del arte figurativo argentino desde fines del siglo XIX hasta mediados del siglo XX. Obras de grandes artistas, considerados iniciadores y precursores de las artes plásticas en el país, se encuentran exhibidas en este museo, donde el artista vivió y trabajó.' where lower(name) like '%quinquela%';



UPDATE  site  SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');
UPDATE institution  SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');


COMMIT;

