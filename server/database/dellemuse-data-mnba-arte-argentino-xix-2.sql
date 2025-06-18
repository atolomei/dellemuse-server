-- ------------------------------------------------------------------------------------------------------------------------------
--
-- Dellemuse / Allemuse
--
-- Un museo (del latín, musēum y este, a su vez, del griego, Μουσείον, 'santuario de las musas')
-- en italiano «luogo sacro alle Muse»
--

-- psql dellemuse

-- dellemuse=# set client_encoding to 'utf8';
-- dellemuse=# \i dellemuse-datos.sql
-- dellemuse=# \i dellemuse-datos-impresionistas.sql

-- 1. Della Valle - La vuelta del malón
-- 2. Obligado - En Normandie
-- 3. Ernesto de la Cárcova - Sin pan y sin trabajo
-- 4. Mendihalarzu - La vuelta al hogar
-- 5. Sívori - El despertar de la criada
-- 6. Schiaffino - Reposo
-- 7. Morales - Abel
--
-- ------------------------------------------------------------------------------------------------------------------------------

BEGIN;
set client_encoding to 'utf8';
COMMIT;

BEGIN;

-- Arte Argentino. Siglo XIX.
-- Hacia la Consolidación de un modelo nacional
--
-- 
INSERT INTO artExhibition (id, name, title, subtitle, info, site_id, website,  lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional', 
 'Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional', 
 '',
 'Con la Generación del 80, comenzó a tomar forma una estructura que daría lugar a la consolidación de las instituciones del arte.
 La actitud moderna de los artistas que fomentaron esta transformación se enmarcaba en un modelo de país ligado a las ideas de civilización y barbarie.
 ',
  (select id from site where name like '%Nacional de Bellas%' limit 1), 
 'https://www.bellasartes.gob.ar',
 (select id from users where username='root')); 

-- Items:

-- 1. Della Valle - 
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 1,
 'La vuelta del malón', 
 'La vuelta del malón', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%Arte argentino%' limit 1), 
 (select id from artwork where name like '%La vuelta del malón%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%11%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));

-- 2 
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 2,
 'En Normandie', 
 'En Normandie', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%Arte argentino%' limit 1), 
 (select id from artwork where name like '%En Normandie%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%11%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));
 

-- 3. Ernesto de la Cárcova - Sin pan y sin trabajo
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 3,
 'Sin pan y sin trabajo', 
 'Sin pan y sin trabajo', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%Arte argentino%' limit 1), 
 (select id from artwork where name like '%Sin pan y sin trabajo%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%11%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));


-- 4. Mendihalarzu - La vuelta al hogar
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 4,
 'La vuelta al hogar', 
 'La vuelta al hogar', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%Arte argentino%' limit 1), 
 (select id from artwork where name like '%La vuelta al hogar%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%11%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));


-- 5. Sívori - El despertar de la criada
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 5,
 'El despertar de la criada', 
 'El despertar de la criada', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%Arte argentino%' limit 1), 
 (select id from artwork where name like '%El despertar de la criada%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%11%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));


-- 6. Schiaffino - Reposo
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 6,
 'Reposo', 
 'Reposo', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%Arte argentino%' limit 1), 
 (select id from artwork where name like '%Reposo%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%11%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));


-- 7. Morales - Abel
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 7,
 'Abel', 
 'Abel', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%Arte argentino%' limit 1), 
 (select id from artwork where name like '%Abel%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%11%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));

-- 
-- Guide
--
-- Arte Argentino. Siglo XIX.
-- Hacia la Consolidación de un modelo nacional

INSERT INTO artExhibitionGuide (id, name, title, publisher_id, artExhibition_id, official,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 'Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional', 
 'Arte argentino siglo XIX. Hacia la Consolidación de un modelo nacional', 
 (select id from person where lastname like '%Tolomei%' limit 1), 
 (select id from artExhibition where name like '%Arte argentino%' limit 1), 
 true, 
 (select id from users where username='root'));


-- 1. Della Valle - La vuelta del malón
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 1,
 'La vuelta del malón', 
 'La vuelta del malón', 
 (select id from artExhibitionGuide where name like '%Arte argentino%' limit 1),  
 (select id from artExhibitionItem where name like '%La vuelta del malón%' limit 1),  
 (select id from users where username='root'));


-- 2. Obligado - En Normandie
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 2,
 'En Normandie', 
 'En Normandie', 
 (select id from artExhibitionGuide where name like '%Arte argentino%' limit 1),  
 (select id from artExhibitionItem where name like '%En Normandie%' limit 1),  
 (select id from users where username='root'));



-- 3. Ernesto de la Cárcova - Sin pan y sin trabajo
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 3,
 'Sin pan y sin trabajo', 
 'Sin pan y sin trabajo', 
 (select id from artExhibitionGuide where name like '%Arte argentino%' limit 1),  
 (select id from artExhibitionItem where name like '%Sin pan y sin trabajo%' limit 1),  
 (select id from users where username='root'));




-- 4. Mendihalarzu - La vuelta al hogar
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 4,
 'La vuelta al hogar', 
 'La vuelta al hogar', 
 (select id from artExhibitionGuide where name like '%Arte argentino%' limit 1),  
 (select id from artExhibitionItem where name like '%La vuelta al hogar%' limit 1),  
 (select id from users where username='root'));


-- 5. Sívori - El despertar de la criada
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 5,
 'El despertar de la criada', 
 'El despertar de la criada', 
 (select id from artExhibitionGuide where name like '%Arte argentino%' limit 1),  
 (select id from artExhibitionItem where name like '%El despertar de la criada%' limit 1),  
 (select id from users where username='root'));


-- 6. Schiaffino - Reposo
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 6,
 'Reposo', 
 'Reposo', 
 (select id from artExhibitionGuide where name like '%Arte argentino%' limit 1),  
 (select id from artExhibitionItem where name like '%Reposo%' limit 1),  
 (select id from users where username='root'));


-- 7. Morales - Abel
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 7,
 'Abel', 
 'Abel', 
 (select id from artExhibitionGuide where name like '%Arte argentino%' limit 1),  
 (select id from artExhibitionItem where name like '%Abel%' limit 1),  
 (select id from users where username='root'));


UPDATE  artWork			  	SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');

UPDATE  artExhibition	  	SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g') where name like 'Arte argentino%';
UPDATE  artExhibition	  	SET titlekey = regexp_replace(regexp_replace(lower(title),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g') where name like 'Arte argentino%';

UPDATE  artExhibitionItem 	SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g') where (artExhibition_id in (select id from artExhibition where name like 'Arte argentino%')); 
UPDATE  artExhibitionItem 	SET titlekey = regexp_replace(regexp_replace(lower(title),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g') where (artExhibition_id in (select id from artExhibition where name like 'Arte argentino%')); 

UPDATE  guideContent		SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g') where (artExhibitionGuide_id in ( select id from artExhibitionGuide where name like 'Arte argentino%'));
UPDATE  guideContent		SET titlekey = regexp_replace(regexp_replace(lower(titlekey),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g') where (artExhibitionGuide_id in ( select id from artExhibitionGuide where name like 'Arte argentino%'));

update artwork set title = name where title is null;

COMMIT;






