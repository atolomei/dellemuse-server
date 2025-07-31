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
-- dellemuse=# \i dellemuse-datos-impresionistas.sql


-- 1. Sisley - Effet de neige à Louveciennes
-- 2. Monet -Le Pont d`Argenteuil (El Puente de Argenteuil)
-- 3. Pissarro - Prairies du Valhermeil près Pontoise
-- 4. Pissarro - Femme aux champs (Campesina)
-- 5. Toulouse Lautrec - En observation - M.Fabre, Officier de reserve
-- 6. Gaugin- Vahine no te miti (Femme a la mer) (Mujer del mar).
-- 7. Toulouse Lautrec - Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)
-- 8. Monet - La berge de La Seine (Orillas del Sena)
-- 9. degas - La Toilette apres le bain (El arreglo después del baño)
-- 10. van gogh - Le Moulin de la Galette
-- 11. Manet, Édouard Portrait d'Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)
-- 12. Forain, Jean Louis . Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)
-- 13. Manet  La Nymphe surprise (La ninfa sorprendida)

-- ------------------------------------------------------------------------------------------------------------------------------

BEGIN;
set client_encoding to 'utf8';
COMMIT;


BEGIN;

-- grandes obras del impresionismo
--
-- 1. impresionismo Effet de neige à Louveciennes
--
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Effet de neige à Louveciennes', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Effet de neige à Louveciennes',
 '',
 'Oleo Sobre tela 50 x 61,5 cm.',
 1877,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Donación Mercedes Santamarina, 1960.',
 (select id from users where username='root'));
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Effet de neige à Louveciennes%' limit 1), 
 (select id from person where lastname like '%Sisley%' limit 1),
 (select id from users where username='root'));


--
-- 2. impresionismo  Le Pont d`Argenteuil (El Puente de Argenteuil)
--
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Le Pont d`Argenteuil (El Puente de Argenteuil)', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Le Pont d`Argenteuil (El Puente de Argenteuil)',
 'Donación Mercedes Santamarina, 1970.', 
  'Oleo Sobre tela 60 x 100 cm.',
 1875,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
  'Esta pintura plantea algunos interrogantes de orden iconográfico. Generalmente señalado con el título de Puente de Argenteuil, el cuadro es llamado de otra manera por Daniel Wildenstein, el autor del catálogo razonado de las obras de Claude Monet (1), que individualiza más bien en el paisaje representado al puente ferroviario de Chatou (2), visto desde la isla de Chiard. Esta propuesta sería más atendible. En efecto, la obra muestra un escorzo perspectivo, de abajo arriba, de un puente ferroviario de arcos amplios. Pero sucede que el puente de Argenteuil, de igual factura, es un puente vial (Le Pont d’Argenteuil, 1870, Musée d’Orsay, París); el puente ferroviario de la misma ciudad, en cambio, "no está formado por una serie de arcos redondeados sino por una sola arcada en forma de cavidad con compartimentos” que se apoya sobre “cuatro pares de cilindros alargados de cemento” (3), similares a columnas y sin arcos (Le pont du chemin de fer à Argenteuil, 1874, Musée d’Orsay, París). La isla de Chiard, conocida también como “isla de los impresionistas”, quienes la frecuentaron regularmente para pintar en plein air (4), se encuentra a orillas del Sena, en la periferia oeste de París. Toda la zona, constituida por numerosas localidades (Argenteuil, Asnières, Chatou) muy cercanas entre sí, era un destino muy frecuentado los domingos por los parisinos que iban allí a distenderse a la orilla del agua; esos lugares estaban, por lo demás, a solo media hora de la estación Saint-Lazare. Toda la zona, y en particular la isla de Chiard, albergaba, en la riqueza de su vegetación, numerosas viviendas de artistas. El propio Monet alquiló una casa en Argenteuil entre 1872 y 1877, justamente para estar lo más cerca posible de estos temas que tanto lo fascinaban. Verdaderas novedades sociológicas, estas localidades suscitaron el interés de toda la cultura francesa, desde la llamada “masa” hasta las corrientes más sofisticadas de la vanguardia artística y literaria. En sus representaciones de Argenteuil –aunque con ese nombre debe entenderse a menudo una zona mucho más amplia Monet denota cierta ambivalencia. En algunos cuadros ofrece una visión bucólica de la que están ausentes, o casi, la mayor parte de los símbolos de la modernidad, alineándose en ese sentido a la visión de la naturaleza cotidiana de Corot y Daubigny, alejada de lo pintoresco, casi intacta. En otras obras del mismo período, en cambio, los símbolos del progreso industrial alternan y se mezclan con los de la vida rural, como para subrayar una armonía posible entre naturaleza y modernidad. Las imágenes serenas de las regatas (Le bassin d’Argenteuil, 1872, Musée d’Orsay), a menudo representadas por el artista, atraído por los juegos de agua y de luz, alternan asimismo con las arquitecturas metálicas de los puentes modernos, que después de las destrucciones de la guerra franco-prusiana estaban redefiniendo nuevamente el paisaje. El tema de los ferrocarriles había entrado en el repertorio de Monet a partir de 1874, cuando el artista pintó numerosas vistas del Sena y de los puentes que lo atraviesan fuera de París. Entre 1850 y 1870, Francia conoció un gran desarrollo industrial respaldado por la construcción de importantes infraestructuras (ferrocarriles, estaciones, puentes, viaductos) que se insertaron repentinamente en el paisaje urbano y rural, sobre todo alrededor de la capital. En 1871, 16.000 km de red ferroviaria atravesaban ya todo el territorio francés y la pintura no fue indiferente a este hecho. Monet compartió su interés por los motivos ferroviarios con varios precursores de la pintura de tema moderno, como el alemán Adolf Menzel y el inglés William Turner, de quien había visto en Londres la pintura Rain, Steam and Speed durante su estadía entre 1870 y 1871. Los puentes de Monet, al igual que las sucesivas estaciones realizadas en 1877 (en particular la de Saint-Lazare), no constituyen una “serie” en sentido estricto, sino más bien una “secuencia”, según la definición de Grace Seiberling desarrollada en torno de la misma temática. Comparadas con las famosas series de los años 90 (La catedral de Rouen, por ejemplo), estas pinturas no presentan ni una concepción unitaria ni un trabajo de armonización general efectuado en el taller. Como el puente de Argenteuil, también el de Chatou había atraído la atención de numerosos artistas, que lo representaron desde distintos puntos de vista (Pierre-Auguste Renoir, Pont de chemin de fer à Chatou ou Les marronniers roses, 1881, Musée d’Orsay). El cuadro de Buenos Aires constituye un paisaje “puro”, vaciado de la presencia humana, enteramente concentrado en la reacción rápida del artista frente a la naturaleza, traducida aquí en un follaje denso y vibrante, completamente desarrollado en tonalidades frías (azul, verde, amarillo). El punto de vista de la obra –rebajado a un nivel coincidente con la rica vegetación– desde el cual se distingue el puente ferroviario atravesado por un tren en marcha, parece querer subrayar la idea de una modernidad aparecida al artista en forma imprevista y casi por casualidad. Esta obra proviene de la rica colección impresionista de Mercedes Santamarina. El MNBA posee otra obra de Monet, La berge de la Seine (1880), proveniente de la Exposición Internacional de Arte del Centenario, de 1910.
  Barbara Musetti
1— Entre las biografías más recientes, véase: M. Alphant, Claude Monet. Une vie dans le paysage. Paris, Hazan, 1993; C. F. Stuckey, Monet. Un peintre, une vie, une oeuvre. Paris, Belfond, 1992. 2— D. Wildenstein, 1974, p. 1875. 3— H. P. Tucker, Monet at Argenteuil. New Haven/London, Yale University Press, 1982, p. 70. 4— Señalemos dos catálogos de exposición dedicados al paisaje impresionista: L’Impressionnisme et le paysage français, cat. exp. Paris, Réunion des musées nationaux, 1985; Landscapes of France. Impressionism and its Rivals, cat. exp. London/Boston, Hayward Gallery/ Museum of Fine Arts, 1995.
1964. NIEULESEU, R., “G. Di Bellio, l’ami des impressionnistes”, Revue Roumaine d’Histoire de l’Art, Bucarest, t. 1, nº 2, p. 222, 278. 1974. WILDENSTEIN, Daniel, Claude Monet. Biographie et catalogue raisonné. Lausanne/ Paris, Bibliothèque des Arts, t. 1, nº 367, reprod. p. 1875.',
  (select id from users where username='root'));
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Le Pont d`Argenteuil%' limit 1), 
 (select id from person where lastname like '%Monet%' limit 1),
 (select id from users where username='root'));


--
-- 3. impresionismo - Prairies du Valhermeil près Pontoise
--
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Prairies du Valhermeil près Pontoise', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Prairies du Valhermeil près Pontoise',
 '',
 'Oleo Sobre tela 55 x 92 cm.',
 1874,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Donación Mercedes Santamarina, 1970.',
 (select id from users where username='root'));
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Le Pont d`Argenteuil%' limit 1), 
 (select id from person where lastname like '%Pissarro%' limit 1),
 (select id from users where username='root'));


--
-- 4. impresionismo -Femme aux champs (Campesina)
--

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Femme aux champs (Campesina)', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Femme aux champs (Campesina)',
 '',
 'Oleo Sobre tela 88 x 48 cm.',
 1882,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Galerie Georges Petit.',
 (select id from users where username='root'));
 
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Femme aux champs%' limit 1), 
 (select id from person where lastname like '%Pissarro%' limit 1),
 (select id from users where username='root'));


--
-- 5. impresionismo - En observation - M.Fabre, Officier de reserve
--

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'En observation - M.Fabre, Officier de reserve', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'En observation - M.Fabre, Officier de reserve',
 '',
 'Oleo Sobre tela: 60,5 x 49,5 cm. - Marco: 76,2 x 66,2 cm.',
 1891,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 '',
 (select id from users where username='root'));
 
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%En observation%' limit 1), 
 (select id from person where lastname like '%Toulouse Lautrec%' limit 1),
 (select id from users where username='root'));

--
-- 6. impresionismo - Vahine no te miti (Femme a la mer) (Mujer del mar).
--

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Vahine no te miti (Femme a la mer) (Mujer del mar).', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Vahine no te miti (Femme a la mer) (Mujer del mar).',
 '',
 'Oleo Sobre tela:92,5 x 74 cm.',
 1892,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'El MNBA tiene la suerte de poseer dos pinturas de bañistas de Gauguin, ambientada en este caso no en Francia, sino en los mares del Sur. Esta obra aparece mencionada como Estudio de espalda desnuda en una lista de pinturas realizadas durante la primera estadía en Tahití que Gauguin anotó aproximadamente en abril de 1892 en su Carnet de Tahiti (1). Fue pintada en Mataiea y se basa en un dibujo en color del mismo cuaderno de bocetos, probablemente realizado con modelo y con una cuadrícula apenas visible, útil al momento de repetir la composición en mayor escala.Cuando fue expuesta en París en 1893, Thadée Natanson describió concisamente su tema: “de esta, sentada en la arena, solamente se puede ver la espalda bronceada en medio de flores casi simétricas que la espuma borda sobre las olas” (2). La metáfora que vincula la cresta de las olas, las flores y el bordado destaca acertadamente el uso que hace Gauguin de formas polisémicas y la calidad ornamental de este motivo. Las “flores” de espuma también están relacionadas por su forma y su color con la conchilla que está en la playa y con las flores que forman parte del motivo del pareo sobre la rodilla derecha de la mujer. En contraste con otras pinturas de Gauguin que muestran una mujer tahitiana parcialmente vestida, este pareo no tiene ni espesor ni pliegues propios y está pintado sobre la pierna a la manera de los paños en una pintura manierista o de un tatuaje –realmente un “bordado” sobre la piel y la vestimenta original de muchos habitantes de los mares del Sur–. La cresta de las olas similar a una flor ya había sido utilizada por Gauguin en Bretaña (véase, por ejemplo, La playa en Le Pouldu, 1889, colección privada, Buenos Aires) y deriva del arte japonés a través de los grabados de Hiroshige.La descripción que hace Gauguin de la pintura como un Estudio de espalda desnuda confirma que el elemento esencial es el dorso de la figura. Varios autores han señalado que en el libro de 1876 La nouvelle peinture, Edmond Duranty había desafiado a los artistas a presentar figuras de espalda caracterizando su edad, su condición social y su estado psicológico (3). Degas respondió al reto, de manera muy notable sobre todo en sus pasteles de mujeres aseándose, algunas de las cuales están copiadas en una página del Album Briant de Gauguin, 1888-1889 (Museo del Louvre, París). La vista de atrás, con un énfasis en las nalgas, obviamente lo atraía y se encuentra también en la pintura Otahi, 1893 (colección privada). En Vahine no te miti, la posición incómoda de los brazos y las piernas confiere a la espalda un carácter fragmentario con reminiscencias de la escultura antigua, trasladando a esta la expresividad generalmente atribuida al rostro humano.En 1948, Raquel Edelman halló monumentalidad en esta pintura lograda a expensas de la individualidad y la sensualidad, y consideró que la misma probaba la intención de Gauguin de “dominar y sublimar su erotismo” (4). No obstante, la obra se remite a una mujer muy específica, cuya anatomía expresa un carácter –podría decirse incluso una fisonomía–. En lo que al erotismo de Gauguin respecta, es típicamente sugestivo e indirecto. Ronald Pickvance señaló que la cresta de las olas y el motivo de las flores en el pareo son “como amebas y están animados por una vitalidad orgánica” (5).Si agregamos la conchilla en el rincón, oculta en parte por la caprichosa forma de flores rojas, es evidente que el cuerpo autocontenido y semejante a un fruto de la bañista está rodeado por un ballet de criaturas animadas. Las flores rojas, como el fondo amarillo, reaparecen en Parahi te marae, 1892 (Philadelphia Museum of Art), donde las plantas imitan la sexualidad humana (6). En la pintura de Buenos Aires, la hoja grande que da lugar al título está dividida como una mano en extremidades similares a dedos y apunta hacia el trasero de la mujer (7). A Gauguin le encantaban las sugerencias anatómicas de las frutas y las flores que encontraba en Tahití, las que con frecuencia habían pasado al lenguaje y la mitología locales, y es probable que aquí haya aludido además a la semejanza entre las nalgas femeninas y la nuez llamada coco de mer, “coco de mar”, cuyo nombre científico había sido al principio Lodoicea callypige en razón, precisamente, de esta analogía (8).El título que le puso al cuadro se traduce de hecho como “mujer del mar” y está basado en la misma fórmula que empleó para Vahine no te tiare, “mujer de la flor”, 1891 (Ny Carlsberg Glyptotek, Copenhague) y Vahine no te vi, “mujer del mango”, 1892 (Baltimore Museum of Art). Hablando de esta última, Hiriata Millaud ha observado que vahine –contrariamente a hine– significa una mujer que ya tiene una vida social, y que el atributo introducido por no (“para” o “de”) puede definir el carácter de la mujer en cuestión antes que servir simplemente como una función descriptiva y mnemónica (9). La mujer en Vahine no te miti dirige su mirada y su oído hacia el océano, más específicamente hacia el mar abierto que aparece entre dos rocas o islas. Igual que las figuras de David Friedrich vistas de espalda, ella actúa por lo tanto como mediadora entre la naturaleza y el espectador, y parece realmente ser “del mar”, compenetrada con él, como una Venus tahitiana que es a la vez Anadiomene y Calipigia.
Dario Gamboni
1— Véase: Bernard Dorival, Carnet de Tahiti. Paris, Quatre Chemins-Editart, 1954; Carnet de Tahiti. Taravao, Avant et Après, 2001. 2— Thadée Natanson, “Oeuvres récentes de Paul Gauguin”, La revue blanche, diciembre de 1893, citado en: Marla Prather y Charles F. Stuckey (ed.), 1987, p. 225. 3— Véase la entrada de Charles F. Stuckey en: Richard Brettell et al., The Art of Paul Gauguin, cat. exp. Washington, National Gallery of Art, 1988. Versión francesa Gauguin, cat. exp. Paris, Réunion des musées nationaux, 1989, nº 144.4— Raquel Edelman, 1948, p. 73-79. 5— Ronald Pickvance, Gauguin, cat. exp. Martigny, Fondation Pierre Gianadda, 1998, nº 32. 6— Dario Gamboni, “Parahi te marae: où est le temple?”, 48/14: La revue du Musée d’Orsay, Paris, nº 20, 2005, p. 6-17. 7— Véase la planta más explícitamente antropomórfica que sostiene a una pareja copulando en el manuscrito Album ancien culte mahorie (1892, Musée d’Orsay, París, RF 10755, folio 46). 8— Gauguin posteriormente grabó toda la superficie de una de esas nueces (Coco de mer, ca. 1901-1903, Albright-Knox Art Gallery, Buffalo). 9— Hiriata Millaud, “Les titres tahitiens de Gauguin” en: Ia Orana Gauguin, cat. exp. Paris, Somogy, 2003, p. 81-90.
1893. NATANSON, Thadée, “Oeuvres récentes de Paul Gauguin”, La revue blanche, Paris, diciembre. 1936. Plástica, Buenos Aires, a. 2, nº 5, abril, reprod. byn p. 10. 1948. EDELMAN, Raquel, “Gauguin en Buenos Aires”, Ver y Estimar, Buenos Aires, vol. 2, nº 7-8, octubre-noviembre, p. 73-79, reprod. p. 75. 1964. WILDENSTEIN, Georges, Gauguin. Paris, Fondation Wildenstein, vol. 1, nº 465. 1977. FIELD, Richard S., Paul Gauguin: The Paintings of the First Trip to Tahiti. Tesis de doctorado, Harvard University [1963]. New York/London, Garland, nº 21.1987. PRATHER, Marla y Charles F. Stuckey, Gauguin: A retrospective. New York, Hugh Lauter Levin Associates, p. 224-226, reprod. nº 64. 1990. HADDAD, Michèle, La divine et l’impure: le nu au XIXe. Paris, Les Éditions du Jaguar, p. 49-50, reprod. color p. 51. 1993. 1893: L’Europe des peintres, cat. exp. Paris, Réunion des musées nationaux, p. 20.',
 (select id from users where username='root'));
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Vahine no%' limit 1), 
 (select id from person where lastname like '%Gauguin%' limit 1),
 (select id from users where username='root'));


--
-- 7. impresionismo - Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)
--

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)',
 '',
 ' 55 x 46 cm. - Marco: 77,5 x 66,5 x 6,5 cm.',
 1885,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'En 1885, con apenas veintiún años, en busca de independencia y deseoso de escapar del severo control paterno, Henri de Toulouse-Lautrec (1) abandonó el sur de Francia para ir a París. Se estableció en el barrio de Montmartre (2) con su amigo René Grenier. La integración en la vida de la butte no fue simple; contribuyeron a su inserción sus amigos, entre ellos François Gauzi, compañero de estudios en el atelier de Ferdinand Cormon.En estrecho contacto con Van Gogh, Vallotton, Bonnard, Lautrec participó plenamente del clima artístico parisino, que en ese tiempo buscaba en varios sentidos la superación del impresionismo, y orientó sus indagaciones hacia una pintura adherida a la realidad que, a través de la estilización formal, fijara los tipos psicológicamente característicos. Participante desencantado del ambiente de Montmartre –cafés-concert, prostíbulos, salas de baile, teatros–, en sus cuadros y en los numerosos bocetos tomados de la realidad Lautrec trazó toda la intimidad y la tristeza de ese submundo humano. Fundamental en ese ambiente fue el encuentro con una mujer que, antes que una seguidilla de modelos y amantes, entró en su vida y en su obra. Se trata de Marie-Clémentine Valade (3), más famosa por el nombre de Suzanne Valadon, que prestó sus rasgos a algunos de los “tipos femeninos” más conocidos de Lautrec (4). Marie-Clémentine era una jovencita sin medios materiales ni culturales, nacida en 1865, de madre modista y padre desconocido. Se aventuró en la actividad circense como acróbata, pero una caída la obligó a desistir; probó otros trabajos humildes hasta que decidió ofrecerse como modelo artística. Comenzó a ser solicitada por los mejores pintores de la época, el primero de todos Puvis de Chavannes, que fue durante cierto período también su amante. Siguieron Renoir, Manet, Gauguin; a todos regaló su belleza, de todos tomó algo. Apasionada por el dibujo, durante las sesiones de pose la joven modelo observaba a los maestros trabajando; muy pronto todos, particularmente Lautrec (5), la alentaron a seguir su pasión. Fue él quien le sugirió el nombre artístico de Suzanne Valadon, porque como la Susana bíblica, Marie estaba rodeada de “viejos ávidos”. Con este retrato titulado Madame Valadon, artiste peintre, Lautrec ratifica el rol de pintora que había asumido Marie (6).Toulouse-Lautrec solía pasar del taller al aire libre. Sus pinturas a cielo abierto constituyeron una etapa que le permitió adaptar estilística y temáticamente ciertos valores adquiridos de la pintura en plein air, no tanto en la línea de los estudios lumínicos de los impresionistas sino en una búsqueda de mayor libertad. En esta pintura Suzanne está sentada, representada frontalmente, por delante de un paisaje otoñal. Se trata muy probablemente del jardín del viejo Forest, un terreno dedicado al tiro con arco, situado en la esquina del boulevard de Clichy y la rue Caulaincourt, a pocos pasos del atelier del artista, donde Lautrec realizó distintos retratos femeninos (Justine Dieuhl, 1891, Musée d’Orsay, París). Su cuerpo está delimitado por un contorno negro bien marcado –reminiscencia del ejemplo de Degas, por quien Lautrec sentía real veneración– dentro del cual, sin embargo, los volúmenes parecen llenados sumariamente por amplios trazos de color, cuya porosidad es exaltada por la textura de la tela sin preparación. El rostro de la modelo se recorta contra un conjunto de planos cromáticos que han perdido la rigidez y el rigor del contemporáneo Retrato de Jeanne Wenz (1886, The Art Institute of Chicago), testimoniando ya la gran maestría y la verdadera ductilidad de pincelada adquirida por el artista. El fondo, una armonía de amarillos, beiges y marrones, confundidos en toques diluidos, arroja una suerte de velo azulado sobre el cuerpo de la mujer, suavizando la expresión firme del personaje. El retrato femenino suele ser un compromiso entre la elegancia y el realismo a partir de la observación directa. Los artistas mundanos tienen como única preocupación realzar la belleza y la condición social de la modelo. Un artista en boga en la época, como por ejemplo Giovanni Boldini, daba a sus modelos un aspecto lo más semejante y halagador posible. Lautrec, en cambio, más lúcido, como Van Gogh, fue a lo esencial gracias a una manera descriptiva sobria y directa, escapando a la tentación de “embellecer”. El hecho de disponer de una renta familiar le permitía eludir las obligaciones de los retratos “alimentarios” –los realizados para vivir todo el mes– y seguir solamente su fantasía; son raros, de hecho, los retratos realizados por el artista en razón de algún encargo célebre (por ejemplo, Madame de Gortzikoff ) (7). La obra de Buenos Aires debe relacionarse con un retrato de asunto análogo, conservado en la Ny Carlsberg Glyptotek de Copenhague (1886-1887). La modelo posó además para la famosa tela La buveuse o La gueule de bois (1889, Harvard Fogg Art Museum, Cambridge; el dibujo, de 1887-1888, se conserva en el Musée Toulouse-Lautrec, Albi), escena social de depravación y miseria (8).
 Barbara Musetti
1— Sobre la vida de Lautrec, cf. U. Felbinger, Henri de Toulouse-Lautrec: sa vie et son oeuvre. Köln, Könemann, 2000; A. Simon, Toulouse-Lautrec: biographie. Tournai, La Renaissance du Livre, 1998. 2— Sobre la influencia de Montmartre en la obra de Lautrec, cf. R. Thomson; P. D. Cate y M. Weaver Chapin (dir.), Toulouse-Lautrec and Montmartre, cat. exp. Washington, National Gallery of Art, 2005; F. Maubert, Le Paris de Lautrec. Paris, Assouline, 2004; P. Vabanne, Henri de Toulouse-Lautrec: le peintre de la vie moderne. Paris, Terrail, 2003. 3— Cf. T. Diamand Rosinsk, Suzanne Valadon. Paris, Flammarion, 2005. 4— El retrato femenino figura entre los temas más recurrentes de la obra de Lautrec. Sobre este asunto, véase: Le donne di Toulouse-Lautrec, cat. exp. Milano, Mazzotta, 2001. 5— Degas, que fue mentor y amigo de Valadon y uno de los primeros en adquirir sus dibujos, los calificó de “malos y blandos” y definió a la artista como “la terrible Marie”. 6— Sobre la actividad artística de Valadon, madre del pintor Maurice Utrillo, véase: M. Restellini (dir.), Valadon-Utrillo: au tournant du siècle à Montmartre: de l’impressionnisme à l’École de Paris, cat. exp. Paris, Pinacothèque de Paris, 2009; Alexandra Charvier et al., Utrillo, Valadon, Utter: 12 rue Cortot: un atelier, trois artistes. Sannois, Musée Utrillo-Valadon, 2008. 7— Toulouse Lautrec, cat. exp. Paris, Réunion des musées nationaux, 1992, p. 133.8— Para un recorrido por toda la obra de Toulouse-Lautrec, cf. M. G. Dortu, Toulouse-Lautrec et son oeuvre. New York, Collectors Editions, 1971, vol. 1-4.',
 (select id from users where username='root'));
  
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Portrait de Suzanne Valadon%' limit 1), 
 (select id from person where lastname like '%Toulouse Lautrec%' limit 1),
 (select id from users where username='root'));


--
-- 8. impresionismo - La berge de La Seine (Orillas del Sena)
--

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'La berge de La Seine (Orillas del Sena)', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'La berge de La Seine (Orillas del Sena)',
 '',
 '60 x 73 cm. Con marco: 84 x 97 cm.',
 1891,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'En la sesión del 17 de noviembre de 1910 los miembros del Concejo Deliberante de la ciudad de Buenos Aires habían votado  un proyecto que destacaba el rol de la Municipalidad para promover la cultura artística,  una de las vías podía ser la donación de obras de arte. Esta resolución, benefició  al MNBA que contó con presupuesto estatal para incrementar su patrimonio  con adquisiciones de obras que concretó la Comisión Nacional de Bellas Artes en la “Exposición Internacional de Arte del Centenario Buenos Aires 1910”.',
 (select id from users where username='root'));

 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%La berge de La Seine%' limit 1), 
 (select id from person where lastname like '%Monet%' limit 1),
 (select id from users where username='root'));


--
-- 9. impresionismo - Morisot, Berthe Marie Pauline 
--    La Coiffure (El peinado)La berge de La Seine (Orillas del Sena)
--

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'La Coiffure (El peinado)La berge de La Seine (Orillas del Sena)', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'La Coiffure (El peinado)La berge de La Seine (Orillas del Sena)',
 '',
 '60 x 73 cm. Con marco: 84 x 97 cm.',
 1891,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 '',
 (select id from users where username='root'));

 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%La Coiffure%' limit 1), 
 (select id from person where lastname like '%Morisot%' limit 1),
 (select id from users where username='root'));



--
-- 10. impresionismo - La Toilette apres le bain (El arreglo después del baño)
--

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'La Toilette apres le bain (El arreglo después del baño)', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'La Toilette apres le bain (El arreglo después del baño)',
 '',
 '66,5 x 78,5 cm.',
 1888,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 '',
 (select id from users where username='root'));

 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%La berge de La Seine%' limit 1), 
 (select id from person where lastname like '%Degas%' limit 1),
 (select id from users where username='root'));


--
-- 11. impresionismo - Le Moulin de la Galette
--

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Le Moulin de la Galette', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Le Moulin de la Galette',
 '',
 '61 x 50 cm.',
 1886,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Fueron condiciones muy particulares, podría decirse de transición, las que llevaron a Vincent van Gogh a pintar este Moulin de la Galette, obra que se inscribe en una importante serie de vistas de París. El artista holandés llegó a la capital francesa en marzo de 1886. Allí se encontró –sin haberle siquiera avisado con anterioridad– con su hermano Theo, que llevaba siete años instalado en la ciudad y dirigía por cuenta de Boussod et Valadon una pequeña galería en el boulevard Montmartre.Cuando Van Gogh pinta su Moulin, está como deslumbrado por el contexto artístico circundante, de una infrecuente densidad: el manifiesto simbolista de Moréas, la última exposición impresionista (donde se presentó La Grande Jatte de Seurat), la publicación de las Illuminations de Rimbaud y de L’oeuvre de Zola se agolpan en el escenario cultural. Descubre además los encantos de la ciudad, las galerías y las discusiones animadas en los cafés. También las obras del Louvre, museo que visita con frecuencia. Para coronar esas experiencias, suma al frenesí un toque académico incorporándose, para afirmar sus cualidades técnicas, como alumno del taller del muy clásico Cormon. Allí alterna con Toulouse-Lautrec y Anquetin.Es sabido que Van Gogh llevó adelante su carrera con una determinación tan humilde como profunda, y sin duda esos meses parisinos movilizaron en él un poderoso deseo de crear. La Butte Montmartre formaba parte de su vida diaria, ya que se alojaba en la casa de Theo, que vivía en ese barrio. Los lazos que unían a los dos hermanos eran por cierto muy fuertes, pero en el otoño de 1886 la promiscuidad del nº 54 de la rue Lepic –desde donde el panorama de la ciudad era magnífico– comenzó a volverse pesada. Esto llevó a Vincent a reemplazar la naturaleza muerta, que podía realizar en el departamento, por el paisaje. Lo cual lo impulsó, en un primer momento, a representar los alrededores inmediatos, y por ende Montmartre (1).Por “Moulin de la Galette” se entiende el café-concert que se extendía en realidad entre dos molinos de Montmartre: el Blute-Fin y el Radet. En la muy célebre composición de Renoir titulada Le bal du Moulin de la Galette (1876, Musée d’Orsay, París), asistimos a las fiestas y los bailes que acompasaban la vida del lugar. Pero no fue el molino de viento como tal lo que había interesado entonces al pintor impresionista. Van Gogh, en cambio, adoptó una actitud totalmente distinta. Se concentró en este caso en uno de los dos edificios afectados por el café-concert: el Blute-Fin, antigua construcción en madera erigida en 1622 y que servía sobre todo para moler trigo.El punto de vista adoptado para el molino –la parte posterior del edificio– no tenía nada de original y lo elegían por la misma época montones de pintores (que saturaban el barrio de Montmartre). Se sabe sin embargo que Van Gogh ensayó en torno de su motivo varias otras vistas para circunscribirlo mejor. Es asombrosa aquí la claridad, la frescura, incluso, del cuadro, dominado por pinceladas vivas de azul que van virando al blanco, en tonalidades muy homogéneas. La perspectiva desde abajo adoptada por el pintor genera una línea del horizonte baja que deja estallar ese gran cielo luminoso. Van Gogh, que en abril de 1885 había trabajado con fervor en sus oscuros Mangeurs de pommes de terre, parece de pronto exultar al contacto con el ambiente parisino. Es verdad que ya había comenzado a aclarar su paleta en Anvers bajo la influencia de los cuadros de Rubens, pero Montmartre le inspira sobre todo, en el tratamiento de la atmósfera, una manera mucho más liviana. Se comprende, pues, lo que escribió a uno de sus amigos artistas (H. M. Levens) en 1887: “No hay otra cosa que París, y por difícil que la vida pueda ser aquí, aunque fuera peor y más dura, el aire francés limpia el cerebro y hace bien, muchísimo bien” (2). La bandera tricolor flameando al viento, representada en unas pocas pinceladas nerviosas, traduce perfectamente, por otro lado, esa plenitud triunfal en las tierras de Francia.Observemos, asimismo, que Van Gogh eligió una vista que no permite adivinar en nada las diversiones del Moulin de la Galette. Hay en él un interés pintoresco por el lugar, pero también una voluntad de mostrar un espacio de trabajo en el límite entre la ciudad y el campo, en lo que era todavía, en esa época, un barrio periférico de París, poblado de gente modesta. La pareja de personajes abajo a la derecha, además de indicar la escala, está vestida de manera humilde y casi campesina. Van Gogh desliza en su obra una dimensión social que lo conmueve particularmente.Podríamos afirmar, entonces, que este óleo es un excelente testimonio de la euforia del pintor holandés que recorre París y a la vez un ejemplo típico de tableau-laboratoire. Van Gogh experimenta en él serenamente sus conceptos plásticos, que encontrarían su realización absoluta unos meses más tarde, en el sur de Francia, en Provenza, región que le “limpia[ría] el cerebro” (como escribe él) con la intensidad combinada del genio y la locura.',
 (select id from users where username='root'));

 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Le Moulin de la Galette%' limit 1), 
 (select id from person where lastname like '%Gogh%' limit 1),
 (select id from users where username='root'));




--
-- 12. impresionismo - Manet, Édouard Portrait d'Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)
--

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Portrait d Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Portrait d Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)',
 '',
 '66,5 x 78,5 cm.',
 1888,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 '​El retrato de Ernest Hoschedé y su hija es una pintura de plein air, resuelta en amplias pinceladas visibles de color, en apariencia inconclusa, que el artista habría comenzado en ocasión de una visita al Château de Rottembourg en 1876, la residencia de la familia Hoschedé en Montgeron, según una carta de Manet a su discípula Eva Gonzalès publicada por Moreau-Nélaton. En ese mismo año Hoschedé encargó a Claude Monet unos paneles decorativos para aquella residencia (1). En los primeros años de la década de 1870, a partir de su vínculo con el grupo de los jóvenes pintores impresionistas, y sobre todo con Claude Monet, Manet había comenzado a pintar y exponer grandes pinturas de plein air (en 1874 expone Chemin de fer en el Salón), en las que, como en el retrato de Hoschedé y su hija, las figuras ocupan buena parte de la composición. Duret afirma que para sostener su manière personal frente a sus amigos impresionistas, Manet casi nunca realizó paisajes “puros” sino que en sus obras de escenas al aire libre continuó pintando figuras humanas, alrededor de las cuales el paisaje se ubicaba como fondo de la escena. ',
 (select id from users where username='root'));

 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Ernest Hoschedé%' limit 1), 
 (select id from person where lastname like '%Manet%' limit 1),
 (select id from users where username='root'));


--
-- 13. impresionismo - Forain, Jean Louis . Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)
--

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)',
 '',
 '60,5 x 73,5 cm.',
 1887,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 '​ fines del siglo XIX París se perfilaba como la ciudad elegida por la burguesía para desplegar su hedonismo. Los sitios preferidos para hacerlo eran los cafés, la Ópera, los jardines suburbanos, el hipódromo o la ribera. Todos ellos quedaron registrados en las pinturas de los impresionistas o post-impresionistas, que examinaron en sus obras las distintas facetas de la vida moderna, tanto para criticarla, como para adularla. Jean-Louis Forain fue uno de estos artistas, de familia modesta, hijo de un “pintor de brocha gorda”, que inició sus estudios en el atelier del pintor Jean-Léon Gérôme, para luego estudiar con Jean-Baptiste Carpeaux quien lo introdujo en la representación de escenas bíblicas en un sentido moderno, temática importante en su desarrollo artístico posterior. También fue André Gill quien en 1870 le enseñó los secretos del arte de la ilustración, que utilizó para representar escenas de la vida cotidiana.No escapó al influjo de Manet, Renoir, Degas, quienes lo acercaron a las teorías del color y de la luz, tan experimentadas por el impresionismo. Sin embargo, con quien estableció mayor afinidad, tanto a nivel artístico como humano, fue con Degas. Tan es así que en varias ocasiones pintaron juntos trabajando sobre un mismo modelo. Bailarinas, carreras de caballos, desnudos, escenas de café, se repitieron en sus obras, pero abordadas desde una perspectiva diferente; mientras que Degas puso el acento en la captura de un instante, de un momento particular, cercano a un lenguaje fotográfico, Forain se concentró en lo gestual, en la fibra expresiva de los personajes, en los caracteres que transmiten su condición social (1).Uno de los temas que ambos comparten es el backstage de la Ópera. En Bailarina y admirador tras la escena, Forain pone de relieve la particular relación, ambigua y muchas veces dolorosa, que se gestaba entre las bailarinas y los abonados a la Ópera. Estos últimos representaban una elite política, financiera y cultural, que llegaba a reservar casi la mitad de las localidades del teatro. Varias miembros del cuerpo de ballet provenían de familias pobres y solían aceptar un soporte económico a cambio de favores sexuales; otras establecían una amistad con “el protector” y solo unas pocas se casaban con ellos. El foyer, la sala de ensayo o calentamiento, oficiaba durante los intervalos como lugar de encuentro con los abonados. En este sitio transcurre la escena en cuestión, “el admirador”, que despliega su condición social a partir de los detalles de su atuendo elegante, tiene la mirada perdida en un punto fijo en el piso, y se refriega sus manos con guantes blancos. La bailarina sentada a su lado apenas gira su cabeza para observarlo, dejando caer al piso, desganadamente, el arco de violín que sostiene en sus manos. Se puede sobreentender un vínculo en las condiciones antes relatadas, a pesar de que ellos casi no interactúan. En el extremo opuesto de la composición y frente al decorado del escenario, un grupo de bailarinas cuchichea, tal vez adivinando aquello que el admirador no se anima a decirle a la mujer deseada. La resolución pictórica de la escena tiene muchas filiaciones con el tipo de factura abocetada y rápida de las obras contemporáneas de Degas, en las que se desdibujan los detalles a medida que se alejan del primer plano.',
 (select id from users where username='root'));

 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Danseuse et admirateur%' limit 1), 
 (select id from person where lastname like '%Forain%' limit 1),
 (select id from users where username='root'));



--
-- 14. impresionismo  La Nymphe surprise (La ninfa sorprendida)
--
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'La Nymphe surprise (La ninfa sorprendida)', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'La Nymphe surprise (La ninfa sorprendida)',
 '',
 'Oleo Sobre tela 144,5 x 112,5 cm. Marco: 169,8 x 137,5 x 9,5 cm.',
 1861,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'La nymphe surprise inaugura un período clave de la trayectoria de Édouard Manet y de la historia del modernismo en la pintura francesa (1). Según Barskaya, fue terminada y enviada por el artista a la exposición de la Academia de Arte de San Petersburgo en 1861 con el título Ninfa y sátiro, dos años antes de la exposición en el Salón de Rechazados de Le déjeuner sur l’herbe y de Olympia (pintada ese mismo año aunque enviada y aceptada en el Salon des Artistes Français en 1865, ambas en el Musée d’Orsay, París). Una larga serie de estudios documentales y técnicos, así como discusiones respecto de sus posibles fuentes, revelan un proceso largo y complejo en la elaboración de esta tela, considerada el primer gran tableau-laboratoire de Manet reelaborando modelos de la gran pintura italiana y holandesa de los siglos XVI y XVII. La obra permaneció en poder del artista hasta su muerte, y existe evidencia de que Manet la consideró uno de sus cuadros más importantes.',
 (select id from users where username='root'));
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%La Nymphe surprise (La ninfa sorprendida)%' limit 1), 
 (select id from person where lastname like '%Manet%' limit 1),
 (select id from users where username='root'));



--
-- Art Exhibition  -> Grandes obras del impresionismo
-- La extensa colección de arte impresionista y postimpresionista del Museo incluye obras de artistas destacados, como Édouard Manet, Claude Monet, Camille Pissarro, Berthe Morisot, Edgar Degas, Vincent Van Gogh, Paul Gauguin y Henri Toulouse Lautrec, entre otros.
-- https://www.bellasartes.gob.ar/coleccion/recorridos/grandes-obras-del-impresionismo_1/

INSERT INTO artExhibition (id, name, title, subtitle, info, site_id, website,  lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Grandes obras del impresionismo', 
 'Grandes obras del impresionismo', 
 '',
 'La extensa colección de arte impresionista y postimpresionista del Museo incluye obras de artistas destacados, como Édouard Manet, Claude Monet, Camille Pissarro, Berthe Morisot, Edgar Degas, Vincent Van Gogh, Paul Gauguin y Henri Toulouse Lautrec, entre otros.',
  (select id from site where name like '%Nacional de Bellas%' limit 1), 
 'https://www.bellasartes.gob.ar/coleccion/recorridos/grandes-obras-del-impresionismo_1/.',
 (select id from users where username='root')); 



-- Items:

-- 1 Effet de neige à Louveciennes
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 1,
 'Effet de neige à Louveciennes', 
 'Effet de neige à Louveciennes', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%Effet%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%11%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));


-- 2 Le Pont d`Argenteuil (El Puente de Argenteuil)
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 2,
 'Le Pont d`Argenteuil (El Puente de Argenteuil)', 
 'Le Pont d`Argenteuil (El Puente de Argenteuil)', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%Le Pont%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%11%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));

-- 3 Prairies du Valhermeil près Pontoise
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 3,
 'Prairies du Valhermeil près Pontoise', 
 'Prairies du Valhermeil près Pontoise', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%Prairies%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%12' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));
 
-- 4. Femme aux champs (Campesina)
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 4,
 'Femme aux champs (Campesina)', 
 'Femme aux champs (Campesina)', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%Femme%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%12%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));


-- 5.  En observation - M.Fabre, Officier de reserve
INSERT INTO artExhibitionItem (id,  artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
  5,
 'En observation - M.Fabre, Officier de reserve', 
 'En observation - M.Fabre, Officier de reserve', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%En observation%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%12%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));


-- 6.  Vahine no te miti (Femme a la mer) (Mujer del mar).
INSERT INTO artExhibitionItem (id, artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 6,
 'Vahine no te miti (Femme a la mer) (Mujer del mar)', 
 'Vahine no te miti (Femme a la mer) (Mujer del mar)', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%Vahine no te miti%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%14%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));

-- 7.  Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)
INSERT INTO artExhibitionItem (id, artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 7,
 'Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)', 
 'Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%Portrait de Suzanne Valadon%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%14%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));

-- 8.  La berge de La Seine (Orillas del Sena)
INSERT INTO artExhibitionItem (id, artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 8,
 'La berge de La Seine (Orillas del Sena)', 
 'La berge de La Seine (Orillas del Sena)', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%La berge%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%14%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));


-- 9. La Coiffure (El peinado)
INSERT INTO artExhibitionItem (id, artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 9,
 'La Coiffure (El peinado)', 
 'La Coiffure (El peinado)', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%La Coiffure%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%14%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));

-- 10.  La Toilette apres le bain (El arreglo después del baño)
INSERT INTO artExhibitionItem (id, artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 10,
 'La Toilette apres le bain (El arreglo después del baño)', 
 'La Toilette apres le bain (El arreglo después del baño)', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%La Toilette apres le bain (El arreglo después del baño)%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%14%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));


-- 11.   Le Moulin de la Galette
INSERT INTO artExhibitionItem (id, artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 11,
 'Le Moulin de la Galette', 
 'Le Moulin de la Galette', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
(select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%Le Moulin de la Galette%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%14%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));


-- 12.  Manet, Édouard Portrait d'Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)
INSERT INTO artExhibitionItem (id, artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 12,
 'Portrait de Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)', 
 'Portrait de Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
(select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%Le Moulin de la Galette%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%14%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));


-- 13.  La Nymphe surprise (La ninfa sorprendida)
INSERT INTO artExhibitionItem (id, artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 13,
 'Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)', 
 'Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
(select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%Le Moulin de la Galette%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%14%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));

-- 14.  La Nymphe surprise (La ninfa sorprendida)
INSERT INTO artExhibitionItem (id, artexhibitionorder, name, title, site_id, artExhibition_id, artWork_id, floor_id, room_id, readcode,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 14,
 'La Nymphe surprise (La ninfa sorprendida)', 
 'La Nymphe surprise (La ninfa sorprendida)', 
 (select id from site where name like '%Nacional de Bellas%' limit 1), 
 (select id from artExhibition where name like '%mpresion%' limit 1), 
 (select id from artwork where name like '%Le Moulin de la Galette%' limit 1), 
 (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
 (select id from room where name like '%14%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1), 
  nextval('readcode_id'),
 (select id from users where username='root'));

-- 
-- Guide
--
INSERT INTO artExhibitionGuide (id, name, title, publisher_id, artExhibition_id, official,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 'Grandes obras del Impresionismo', 
 'Grandes obras del Impresionismo', 
 (select id from person where lastname like '%Tolomei%' limit 1), 
 (select id from artExhibition where name like '%Impresionismo%' limit 1), 
 true, 
 (select id from users where username='root'));


-- 1.  Effet de neige à Louveciennes
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 1,
 'Effet de neige à Louveciennes', 
 'Effet de neige à Louveciennes', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%Effet%' limit 1),  
 (select id from users where username='root'));

-- 2.  Le Pont d`Argenteuil (El Puente de Argenteuil)
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 2,
 'Le Pont d`Argenteuil (El Puente de Argenteuil)', 
 'Le Pont d`Argenteuil (El Puente de Argenteuil)', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%Le Pont d`Argenteuil%' limit 1),  
 (select id from users where username='root'));

-- 3.  Prairies du Valhermeil près Pontoise
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 3,
 'Prairies du Valhermeil près Pontoise', 
 'Prairies du Valhermeil près Pontoise', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%Le Pont d`Argenteuil%' limit 1),  
 (select id from users where username='root'));

-- 4.  Femme aux champs (Campesina)
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 4,
 'Femme aux champs (Campesina)', 
 'Femme aux champs (Campesina)', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%Femme aux champs (Campesina)%' limit 1),  
 (select id from users where username='root'));

-- 5.  En observation - M.Fabre, Officier de reserve
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 5,
 'En observation - M.Fabre, Officier de reserve', 
 'En observation - M.Fabre, Officier de reserve', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%En observation - M.Fabre, Officier de reserve%' limit 1),  
 (select id from users where username='root'));
 
-- 6.  Vahine no te miti (Femme a la mer) (Mujer del mar).
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 6,
 'Vahine no te miti (Femme a la mer) (Mujer del mar)', 
 'Vahine no te miti (Femme a la mer) (Mujer del mar)', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%Vahine no te miti (Femme a la mer) (Mujer del mar)%' limit 1),  
 (select id from users where username='root'));

-- 7.  Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 7,
 'Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)', 
 'Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)%' limit 1),  
 (select id from users where username='root'));

-- 8.  La berge de La Seine (Orillas del Sena)
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 8,
 'La berge de La Seine (Orillas del Sena)', 
 'La berge de La Seine (Orillas del Sena)', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%La berge de La Seine (Orillas del Sena)%' limit 1),  
 (select id from users where username='root'));

-- 9.  La Coiffure (El peinado)
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 9,
 'La Coiffure (El peinado)', 
 'La Coiffure (El peinado)', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%La Coiffure (El peinado)%' limit 1),  
 (select id from users where username='root'));


-- 10. La Toilette apres le bain (El arreglo después del baño)
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 10,
 'La Toilette apres le bain (El arreglo después del baño)', 
 'La Toilette apres le bain (El arreglo después del baño)', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%La Toilette apres le bain (El arreglo después del baño)%' limit 1),  
 (select id from users where username='root'));

 
-- 11. Le Moulin de la Galette
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 11,
 'Le Moulin de la Galette', 
 'Le Moulin de la Galette', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%Le Moulin de la Galette%' limit 1),  
 (select id from users where username='root'));


-- 12. Manet, Édouard Portrait d'Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)

INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 12,
 'Portrait Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)', 
 'Portrait Ernest Hoschedé et sa fille Marthe (Retrato del Señor Hoschedé y su hija)', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%Hoschedé%' limit 1),  
 (select id from users where username='root'));


-- 13. Forain, Jean Louis . Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 13,
 'Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)', 
 'Danseuse et admirateur derrière la scène (Bailarina y admirador tras la escena)', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%Danseuse%' limit 1),  
 (select id from users where username='root'));


-- 14. La Nymphe surprise (La ninfa sorprendida)
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 14,
 'La Nymphe surprise (La ninfa sorprendida)', 
 'La Nymphe surprise (La ninfa sorprendida)', 
 (select id from artExhibitionGuide where name like '%Impresionismo%' limit 1),  
 (select id from artExhibitionItem where name like '%La Nymphe surprise (La ninfa sorprendida)%' limit 1),  
 (select id from users where username='root'));

UPDATE  person			  SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');
UPDATE  artExhibition	  SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');
UPDATE  artExhibitionItem SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');

UPDATE  guideContent		SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g') where (artExhibitionGuide_id in ( select id from artExhibitionGuide where name like '%mpresionismo%'));

update artwork set title = name where title is null;
update site set title = name where title is null;
update person set title = concat(lastname,' ',name) where title is null;
 
COMMIT;








