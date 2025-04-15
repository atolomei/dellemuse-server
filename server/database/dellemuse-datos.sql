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

-- MNBA
--
INSERT INTO institution (id, name, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'), 'Museo Nacional de Bellas Artes',  now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Museo Quinquela Martín', now(), now(), (select id from users where username='root'));

-- MNBA > recoleta
-- 
INSERT INTO site  (id,	name, institution_id,  created, lastmodified,	lastmodifieduser) VALUES
 (nextval('sequence_id'), 
  'Museo Nacional de Bellas Artes', 
  (select id from institution where name like '%Nacional de Bellas Artes' limit 1), 
  now(), 
  now(), 
  (select id from users where username='root'));

-- MNBA > recoleta > Planta Baja
-- 
INSERT INTO floor  (id,	name, site_id,  created, lastmodified,	lastmodifieduser) VALUES
(nextval('sequence_id'), 'Planta Baja', (select id from site where name like '%Nacional de Bellas Artes' limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Primer Piso', (select id from site where name like '%Nacional de Bellas Artes' limit 1), now(), now(), (select id from users where username='root'));


-- MNBA > recoleta > Planta Baja > Sala A 
--
INSERT INTO room  (id,	name, floor_id,  created, lastmodified,	lastmodifieduser) VALUES 
(nextval('sequence_id'), 'Hall Central'                            , (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Nacional de Bellas%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Rampa primer piso - etre salas 31 y 32'  , (select id from floor where name like '%Primer Piso%' and site_id in (select id from site where name like '%Nacional de Bellas%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Pabellón de exposiciones temporarias'    , (select id from floor where name like '%Primer Piso%' and site_id in (select id from site where name like '%Nacional de Bellas%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Hall y escaleras'                        , (select id from floor where name like '%Primer Piso%' and site_id in (select id from site where name like '%Nacional de Bellas%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Sala 2. Arte europeo siglo XII al XVI'   , (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Nacional de Bellas%' limit 1)  limit 1), now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Salón 20 ARTISTAS BRASILEÑOS'            , (select id from floor where name like '%Primer Piso%' and site_id in (select id from site where name like '%Nacional de Bellas%' limit 1)  limit 1), now(), now(), (select id from users where username='root'));

-- PERSON and USER
--
INSERT INTO person (id, name, lastname,  physicalid, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), '-', 'Alejandro', 'Tolomei', now(), now(), (select id from users where username='root'));
INSERT INTO users (id, username, lastmodifieduser) VALUES  (nextval('sequence_user_id'), 'atolomei', (select id from users where username='root'));

--

INSERT INTO person (id, name, lastname,  physicalid, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'), 'Doménikos',  'Theotokópoulos', '-', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Odilon',     'Redon',          '-', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Tarsila',    'do Amaral',      '-', now(), now(), (select id from users where username='root'));



-- Tarsila do Amaral

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Morro da favela II (Pueblito)', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Morro da favela II (Pueblito)',
 '',
 'Oleo Sobre tela 39,5 x 49,9 cm.',
 1945,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Época de temática popular, la década del 40 también sería para Tarsila, la gran pintora modernista de los años 20, tiempo de volver definitivamente de sus tonos pau Brasil, es decir, retornar a los colores llamados caipiras (azul y rosa) de la decoración popular de los modestos caseríos de las regiones del interior del Brasil. Este colorido, aunque más intenso, estuvo presente en sus telas de los años 20, su período de máxima creatividad y producción, etapa de una gran síntesis de sus elementos compositivos. Después de 1930, cuando Tarsila se orientó, por razones culturales y políticas, hacia la temática social, su paleta se alteró igualmente.\nPor esa época la artista regresó a San Pablo después de alternar varios años entre esa ciudad y Río de Janeiro, y pasó gran parte de su tiempo en su hacienda de Indaiatuba, en el interior paulista. \nConociendo la inexistencia de un mercado de arte local, realizó encargos de pinturas, ilustraciones y colaboró con crónicas regulares sobre la vida cultural y memorias en Diário de São Paulo.\nEn Pueblito, de 1945, encontramos una segunda versión de la famosa pintura Morro da favela (1924, colección João Estefano, San Pablo), tela tan admirada por el poeta Blaise Cendrars que le recomendó, en su momento, que preparase su primera exposición individual en París a partir de la serie que se iniciara con esta obra. La temática está inspirada en el viaje a Río con los modernistas de San Pablo acompañando a Cendrars en su primera visita al Brasil.\nAlgunas variantes, sin embargo, surgen en esta tela, ahora en la colección del MNBA: sus dimensiones son menores que las de Morro da favela, la vegetación tiene modificaciones, dejando de ser visualmente presentada en formas sintéticas, en esta tela de 1945 aparecen como matas de plantas más desmenuzadas, más diversificadas en su ubicación, especialmente en el primer plano. Por otra parte, las pinceladas son bien evidentes, casi “impresionistas” en su aplicación sobre la tela, y el ave extraña de 1924 es reemplazada por la confrontación de dos pavos. Finalmente, el color es más suave en comparación con la pintura de veinte años antes.\nLa obra del MNBA estuvo expuesta en Buenos Aires, La Plata y Montevideo en la exposición Veinte artistas brasileños presentada en 1945, con la organización del escritor Marques Rebelo y la intermediación efectiva de Emilio Pettoruti.\nEn esta muestra Tarsila estuvo representada por otra pintura, Pueblito II (1944), y por un dibujo, estudio de 16 x 22 cm de la pintura de la colección del MNBA, hoy aparentemente perdido. La exposición dio lugar a dos pequeños libros, actualmente antológicos, uno de autoría de Cipriano Vitureira, de Montevideo, y el segundo de Jorge Romero Brest, La pintura brasileña contemporánea, publicado el mismo año de la exposición por la editorial Poseidón.',
 (select id from users where username='root'));
 

 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Morro da favela%' limit 1), 
 (select id from person where name like '%Tarsila%' limit 1),
 (select id from users where username='root'));
 

 -- Odilon Redon
 
 INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec,  year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Le bain de Vénus (El baño de Venus)', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Le bain de Vénus (El baño de Venus)',
 '',
 'Oleo Sobre tabla 35 x 28 cm.',
 1908,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'El título original de esta pintura puede haber sido puesto por Redon, ya que al parecer la obra fue comprada al artista sin intermediarios. Los toques de azul y verde en la zona inferior derecha de la tela pueden considerarse una representación del agua, y el tocado de la figura puede evocar un personaje mitológico. Estos aspectos, sin embargo, son los únicos que justificarían el título de El baño de Venus. La figura no está desnuda, aparece (apenas) vestida y carece de otro atributo inherente a la diosa del amor. Su movimiento no corresponde a una escena de baño y el personaje demuestra una vacilación indigna de alguien nacido del mar. Con los dos brazos extendidos hacia delante y la cabeza hacia atrás, parece moverse de derecha a izquierda como si estuviese huyendo de algo –en una posición relativamente similar a la del dibujo Female Nude (Bridgestone Museum, Tokio)–. El origen de su miedo o resistencia parece ser la forma circular sobre la derecha (de la mitad del tamaño de la figura) que semeja más a un insecto o a uno de los monstruos submarinos pintados por Redon que a las flores y las hojas reconocibles sobre la izquierda. Esta interpretación se sustenta en el uso del negro para delinear y rellenar parte de esta forma, color asociado por Redon al mundo fantástico de sus primeras carbonillas y litografías.',
 (select id from users where username='root'));

 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Le bain de Vénus%' limit 1), 
 (select id from person where name like '%Odilon%' limit 1),
 (select id from users where username='root'));
 
  
 
 
INSERT INTO artExhibition (id,	name, site_id, title, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Obras Maestras', 
  (select id from site where name like '%Bellas Artes%' limit 1),
  'Obras Maestras',
  'La obra maestra era el nombre que recibía la pieza artesanal que debía realizar todo oficial que quisiera acceder a la categoría de maestro en el seno de los gremios.',
 (select id from users where username='root'));
 

 INSERT INTO artExhibitionItem  (id, name, title, artWork_id, ordinal, readcode, site_id, floor_id, room_id,  info,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 'Le bain de Vénus (El baño de Venus)', 
 'Le bain de Vénus (El baño de Venus)',
  (select id from artWork where name like 'Le bain%' limit 1),
  1,
  nextval('readcode_id'),
  (select id from site where name like '%Nacional de Bellas Artes%' limit 1),
  (select id from floor where name like 'Planta Baja%' limit 1),
  (select id from room where name like 'Hall Central%' limit 1),
  'El título original de esta pintura puede haber sido puesto por Redon, ya que al parecer la obra fue comprada al artista sin intermediarios. Los toques de azul y verde en la zona inferior derecha de la tela pueden considerarse una representación del agua, y el tocado de la figura puede evocar un personaje mitológico. Estos aspectos, sin embargo, son los únicos que justificarían el título de El baño de Venus. La figura no está desnuda, aparece (apenas) vestida y carece de otro atributo inherente a la diosa del amor. Su movimiento no corresponde a una escena de baño y el personaje demuestra una vacilación indigna de alguien nacido del mar. Con los dos brazos extendidos hacia delante y la cabeza hacia atrás, parece moverse de derecha a izquierda como si estuviese huyendo de algo –en una posición relativamente similar a la del dibujo Female Nude (Bridgestone Museum, Tokio)–. El origen de su miedo o resistencia parece ser la forma circular sobre la derecha (de la mitad del tamaño de la figura) que semeja más a un insecto o a uno de los monstruos submarinos pintados por Redon que a las flores y las hojas reconocibles sobre la izquierda. Esta interpretación se sustenta en el uso del negro para delinear y rellenar parte de esta forma, color asociado por Redon al mundo fantástico de sus primeras carbonillas y litografías.',
 (select id from users where username='root'));
 

 
 INSERT INTO artExhibitionItem  (id, name, title, artWork_id, ordinal, readcode, site_id, floor_id, room_id,  info, lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 'Morro da favela II (Pueblito)', 
 'Morro da favela II (Pueblito)',
  (select id from artWork where name like 'Morro da%' limit 1),
  2,
  nextval('readcode_id'),
  (select id from site where name like '%Nacional de Bellas Artes%' limit 1),
  (select id from floor where name like 'Planta Baja%' limit 1),
  (select id from room where name like 'Hall Central%' limit 1),
  'Época de temática popular, la década del 40 también sería para Tarsila, la gran pintora modernista de los años 20, tiempo de volver definitivamente de sus tonos pau Brasil, es decir, retornar a los colores llamados caipiras (azul y rosa) de la decoración popular de los modestos caseríos de las regiones del interior del Brasil. Este colorido, aunque más intenso, estuvo presente en sus telas de los años 20, su período de máxima creatividad y producción, etapa de una gran síntesis de sus elementos compositivos. Después de 1930, cuando Tarsila se orientó, por razones culturales y políticas, hacia la temática social, su paleta se alteró igualmente.\nPor esa época la artista regresó a San Pablo después de alternar varios años entre esa ciudad y Río de Janeiro, y pasó gran parte de su tiempo en su hacienda de Indaiatuba, en el interior paulista. \nConociendo la inexistencia de un mercado de arte local, realizó encargos de pinturas, ilustraciones y colaboró con crónicas regulares sobre la vida cultural y memorias en Diário de São Paulo.\nEn Pueblito, de 1945, encontramos una segunda versión de la famosa pintura Morro da favela (1924, colección João Estefano, San Pablo), tela tan admirada por el poeta Blaise Cendrars que le recomendó, en su momento, que preparase su primera exposición individual en París a partir de la serie que se iniciara con esta obra. La temática está inspirada en el viaje a Río con los modernistas de San Pablo acompañando a Cendrars en su primera visita al Brasil.\nAlgunas variantes, sin embargo, surgen en esta tela, ahora en la colección del MNBA: sus dimensiones son menores que las de Morro da favela, la vegetación tiene modificaciones, dejando de ser visualmente presentada en formas sintéticas, en esta tela de 1945 aparecen como matas de plantas más desmenuzadas, más diversificadas en su ubicación, especialmente en el primer plano. Por otra parte, las pinceladas son bien evidentes, casi “impresionistas” en su aplicación sobre la tela, y el ave extraña de 1924 es reemplazada por la confrontación de dos pavos. Finalmente, el color es más suave en comparación con la pintura de veinte años antes.\nLa obra del MNBA estuvo expuesta en Buenos Aires, La Plata y Montevideo en la exposición Veinte artistas brasileños presentada en 1945, con la organización del escritor Marques Rebelo y la intermediación efectiva de Emilio Pettoruti.\nEn esta muestra Tarsila estuvo representada por otra pintura, Pueblito II (1944), y por un dibujo, estudio de 16 x 22 cm de la pintura de la colección del MNBA, hoy aparentemente perdido. La exposición dio lugar a dos pequeños libros, actualmente antológicos, uno de autoría de Cipriano Vitureira, de Montevideo, y el segundo de Jorge Romero Brest, La pintura brasileña contemporánea, publicado el mismo año de la exposición por la editorial Poseidón.',
 (select id from users where username='root'));
 
 
 
 
 INSERT INTO artExhibitionGuide( id, name, title, publisher_id, artExhibition_id, lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 'Obras Maestras', 
 'Obras Maestras',
  (select id from person where name like 'Alejandro%' limit 1),
  (select id from artExhibition where name like 'Obras Maestras%' limit 1),
 (select id from users where username='root'));
 
 
 
 INSERT INTO  guideContent( id, name, title, artExhibitionGuide_id,  artExhibitionItem_id, lastmodifieduser) VALUES
 (nextval('sequence_id'), 
  (select name from artExhibitionItem where name like 'Morro da%' limit 1), 
  (select title from artExhibitionItem where name like 'Morro da%' limit 1),
  (select id from artExhibitionGuide where name like 'Obras%' limit 1),
  (select id from artExhibitionItem where name like 'Morro da%' limit 1),
  (select id from users where username='root'));
 

   INSERT INTO  guideContent( id, name, title, artExhibitionGuide_id,  artExhibitionItem_id, lastmodifieduser) VALUES
 (nextval('sequence_id'), 
  (select name from artExhibitionItem where name like 'Le bain%' limit 1), 
  (select title from artExhibitionItem where name like 'Le bain%' limit 1),
  (select id from artExhibitionGuide where name like 'Obras%' limit 1),
  (select id from artExhibitionItem where name like 'Le bain%' limit 1),
  (select id from users where username='root'));
 
 
COMMIT;

