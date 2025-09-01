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

update artexhibition set info='Entre sus múltiples misiones, los museos preservan la memoria de una nación y, al mismo tiempo, como instituciones dinámicas, estimulan la construcción de nuevos sentidos para las piezas que albergan. Los modos en que las obras de una colección se dan a conocer al público moldean la relación que una comunidad entabla con la historia, vínculo que se potencia cuando una institución es permeable a las transformaciones estéticas y sociales que trae cada época. \n En el caso de los museos de arte que poseen grandes acervos, la cantidad de obras que el visitante puede apreciar en las salas es solo una parte de un conjunto mucho más vasto; es una propuesta, como toda selección, de una de las tantas lecturas posibles para un patrimonio. Lo que se exhibe es, entonces, un panorama de artistas, períodos, géneros y temas representativos de un corpus que, en gran parte, permanece en las reservas.\n Museo secreto, el título de esta exposición, hace referencia a un discurso pronunciado por Eduardo Schiaffino, primer director del Bellas Artes y quien impulsó la formación del acervo desde fines del siglo XIX. Publicadas en la prensa durante 1926, las palabras de Schiaffino apuntaban a la necesidad de mostrar el patrimonio que, alojado en los depósitos, permanecía vedado a los ojos de los visitantes. En los 130 años que han transcurrido desde su creación, el Museo Nacional de Bellas Artes ha logrado reunir más de 13.000 pinturas, esculturas, dibujos, grabados, objetos, instalaciones y fotografías, entre otros tipos de piezas, un conjunto cuya magnitud hace que el desafío que acompaña a la institución desde sus primeros años de existencia no solo siga vigente, sino que adquiera cada vez mayor complejidad.\n Con la idea de expandir el universo de lo que se presenta al público, esta muestra despliega cerca de 300 obras provenientes de las reservas del Museo Nacional de Bellas Artes, realizadas por más de 250 artistas argentinos y extranjeros, desde el siglo XIV hasta la actualidad. Algunas de ellas han estado en las salas como parte de distintos guiones permanentes o en exposiciones temporarias, mientras que otras han tenido menos visibilidad. \n La curaduría de esta muestra surge del intercambio de saberes y del trabajo colectivo entre todos los equipos del Museo. La disposición de las obras recrea el modo en que se presentaban las colecciones en el siglo XIX, como puede apreciarse a pocos metros de aquí, en la sala Guerrico. Este tipo de distribución permite disponer en el espacio un mayor número de piezas, y también emula la forma en que se las agrupa en las reservas. \n Concebida en un esquema no lineal, la selección conecta géneros, estilos y temas que han guiado la producción de artistas de todas las épocas, con obras distribuidas en grandes núcleos a modo de constelaciones que habilitan diálogos históricos y estéticos, y que, a la vez, ponen en tensión diversas concepciones del arte como herramienta de representación. A través de este panorama expandido de la colección de arte más importante de la Argentina, invitamos a pensar nuevos vínculos entre el pasado y el presente de la historia  visual.' where lower(name) like 'museo sec%';


INSERT INTO person (id, name, lastname,  physicalid, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'), 'Joaquín' 				,     'Sorolla y Bastida'		,    '-', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Kazuya' 				,     'Sakai'					,    '-', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Raymond Auguste' 		,  	  'Quinsac Monvoisin'		,    '-', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Jean Joseph Benjamin' ,     'Constant'				,    '-', now(), now(), (select id from users where username='root'));

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'La Emperatriz Theodora', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'La Emperatriz Theodora',
 '',
 'Oleo sobre tela 224,5 x 125,5 cm. - Marco: 226,5 x 127,5 cm.',
 1887,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Benjamin-Constant estudió en la École des Beaux-Arts. En 1870 se enroló como soldado y al final de la guerra ya no volvió a retomar sus estudios artísticos sino que se dirigió a Granada donde conoció a Mariano Fortuny. Emprendió un viaje a Marruecos en 1872 y a partir de entonces el orientalismo fue central en su producción. Se especializó en dos temáticas: la historia musulmana y la vida contemporánea en Marruecos; ambas habilitaron el despliegue de los cuerpos desnudos y las alusiones eróticas.\n Dentro de este imaginario construido sobre el Oriente, la historia del emperador bizantino Justiniano y su esposa Theodora se volvió un tema repetido en sus pinturas realizadas hacia 1880. Esta presencia se corresponde con el interés que el bizantinismo suscitó en las letras y la historia francesas como parte de una “fiebre oriental” que se extendió en el último cuarto del siglo XIX por el mundo intelectual parisino. \n El pico más alto de esta moda fue el estreno, en 1884, de la obra teatral Théodora de Victorien Sardou que contaba con la popular Sarah Bernhardt en rol protagónico. Así la vida de esta cortesana y actriz de teatro que se volvió emperatriz en el momento de mayor esplendor del Imperio Bizantino fue un motivo más que apto para proyectar la imaginación respecto al poder y la sexualidad de la mujer oriental.\n En el Salón de 1886, Benjamin-Constant expuso un cuadro de Justiniano en el trono rodeado por su consejo. Al año siguiente envió dos obras: un desnudo masculino que representaba a Orfeo y el gran retrato frontal de Theodora. Sentada en su trono, la bella y seria joven mira con fijeza al espectador. Su rostro coincide con la descripción que aporta la principal fuente acerca de su vida (la Historia secreta de Procopios de Cesarea) que la señalaba como una mujer menuda y grácil con ojos vivaces y deslumbrantes.\n El trono en mármol blanco, similar al atribuido a Justiniano en otras obras del artista, alude a su posición emblemática como la primera emperatriz que compartió el protocolo imperial con su marido. Los arabescos que se observan en el fondo la ubican en el Sagrado Palacio Imperial de Constantinopla, sede del poder bizantino hasta el siglo XII. El edificio ya no estaba en pie en el momento en que Constant realizó estas pinturas y suponemos que la decoración de los muros de la catedral de Santa Sofía puede haber servido como inspiración al artista.\n Theodora está vestida ricamente; su atuendo está regado de piedras preciosas. En la parte frontal de la túnica se adivinan dos íconos que remiten a su papel de santa (consagrada por la Iglesia Ortodoxa) y que la vinculan también con el mosaico bizantino del siglo VI de la iglesia de San Vital en Ravena.',
 (select id from users where username='root'));
 
INSERT INTO artworkArtist (id, artwork_id, person_id) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Emperatriz%' limit 1), 
 (select id from person where name like '%Jean Joseph%' limit 1)); 

INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'En la costa de Valencia', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'En la costa de Valencia',
 '',
 'Oleo sobre tela 57 x 88,5 cm.',
 1898,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Esta escena costumbrista que representa el trabajo de los pescadores que vuelven de la faena y el juego de un niño se desarrolla posiblemente en la playa del Cabañal, frecuentada por Sorolla, que se caracterizaba por conjugar, en torno al 1900, tanto a pescadores y sus familias como a pintores en busca de temáticas que mostraran el ser regional. Esta combinación de los trabajos preindustriales de las clases populares, por una parte, y de pintura pleinairista de visos nacionalistas, por otra, brindaba una imagen optimista del Levante español distante del duro proceso de transformación del paisaje de la región por la industrialización. La mecanización y el gigantismo urbano eran la nueva fisonomía que modelaba la vida y las costumbres del Levante. Sin embargo, la actitud de huida nostálgica de la civilización hacia la rústica naturaleza de pintores como Sorolla hacía de estas costas un refugio bucólico frente a las consecuencias generadas por la Revolución Industrial.\nDetrás de este costumbrismo de ambiente marino se encuentra la verdadera raison d’être de las búsquedas de Sorolla: la luz mediterránea. Su sensibilidad lumínica pertenece a una de las dos tendencias de la pintura española que marcaron la vuelta del siglo: la “España blanca” contrapuesta a la “España negra” de Zuloaga, Solanas y Romero de Torres, entre otros. La luz, la gran protagonista de esta pintura, es captada con rapidez y al plein air del mediodía dando como resultado una obra definida por la instantaneidad, el abocetamiento y una atmósfera vibrante producto de los reflejos del sol enceguecedor en su cenit sobre el mar.\nSorolla ocupa un lugar especial dentro del movimiento luminista cuyos centros fueron Valencia y Sitges. A diferencia del resto de los luministas, reunió en sí influencias que supo dosificar, como la del impresionismo y la de los pintores escandinavos como Peder Severin Krøyer (1851-1909), Viggo Johansen (1851-1935) y Anders Zorn (1860-1920) o del alemán Adolph von Menzel (1815-1905), cuya obra conoció en París. En el debate de tradición-modernidad, su pintura no solo tenía la capacidad de resolver el dilema entre el conocimiento académico y la experimentación impresionista, equilibrando ambas posiciones, sino que también lograba mantener un juste milieu artístico (en un momento de vanguardias contestatarias y actitudes reaccionarias hacia ellas) que le brindó un éxito comercial internacional sin precedente en los otros luministas. Todo esto lleva a la historiografía artística a considerarlo como el pintor que clausura el movimiento luminista levantino.\nLa captación instantánea y lumínica de las formas, próxima al impresionismo parisino pero ajena a sus inquietudes, es una constante en los pintores nacidos en el Levante español y activos a finales del siglo XIX y principios del siguiente, de quienes el estilo sorollesco se nutre. Estos, a pesar de su formación conservadora, se atrevieron a incursionar en técnicas inusuales y de gran modernidad en relación con las normas académicas que imponían el gusto por lo bituminoso, provocando un cambio sustancial en la manera de pintar. Al focalizarse en la presencia dominante del mar en el que la vista se pierde al infinito, en su atmósfera costera y en la sugestión hipnótica de la luz y sus matices, propusieron al espectador involucrarse en una percepción de la realidad que es anterior a una primera estructuración gestáltica, es decir una percepción en la cual las sensaciones anteceden a las formas. El luminismo levantino se inserta en una tendencia al aclaramiento de la paleta que tiene lugar en la pintura europea del siglo XIX, evidencia de búsquedas en relación con el color y con la luz generadas a partir del desarrollo de la pintura de paisaje y de la captación de efectos naturalistas.\nSorolla creó una “maniera” posible de ser emulada a nivel compositivo y “digerible” temáticamente a pesar de su modernidad. Su técnica lumínica se oponía diametralmente a la paleta armada del frío academicismo que había entronizado a la pintura de historia con sus composiciones artificiosas. Sin embargo, la organización deliberada de Sorolla de masas de luz y color lo acercaban al pensamiento académico que gustaba de componer fuertemente el lienzo equilibrando las formas y los colores. La adhesión, en parte, a la técnica impresionista no debe confundirse con la adopción del descubrimiento de la impresión lumínica mediante la yuxtaposición de colores puros colocando en un plano secundario la estructura, ni tampoco con la disolución de la forma –divergencias con el impresionismo que lo unían al resto de los luministas– aunque su factura suele oscilar hacia una pincelada de coma impresionista. Estas características técnicas se observan en esta obra, como así también en La vuelta de la pesca (1898, inv. 2007, MNBA), ambas dotadas de una infraestructura muy compleja. Cierta impresión y velocidad de la poética de Sorolla nunca pudieron ser emuladas acabadamente por sus seguidores.\nEn cuanto a la temática, el artista impuso la descripción de una edad de oro del Mediterráneo, una Arcadia donde nos presenta hombres en simple y dulce armonía con la naturaleza. El gesto de los pescadores y el pequeño jugando representan lo que fluye y es efímero. Aun así, estos instantes evocan la eternidad de los tipos humanos de la región.',
 (select id from users where username='root'));

INSERT INTO artworkArtist (id, artwork_id, person_id) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Valencia%' limit 1), 
 (select id from person where name like '%Joaquín%' limit 1));


INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Retrato de Juan Manuel de Rosas', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Retrato de Juan Manuel de Rosas',
 '',
 'Oleo sobre tela 100 x 79,8 cm.',
 1842,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Según los descendientes de Monvoisin este retrato de Rosas era un estudio preparatorio para un retrato ecuestre de tamaño natural ejecutado en Chile, obra perdida supuestamente durante la guerra franco-prusiana. Es poco factible que este artista tan atento a los aspectos mercantiles hubiera resignado el pago correspondiente y realizado una obra de tal importancia en Santiago de Chile rodeado de exiliados unitarios, para los que realizó un retrato del derrotado Lavalle, jefe militar unitario, en 1843. Por otra parte, esta hipótesis se contrapone con la mención de la huida del artista ante la posible represión sobre su persona. Parte de la confusión con estas leyendas sobre Monvoisin se origina en la confiabilidad que le otorguemos a las propias memorias del artista y a los relatos de sus descendientes recogidos por Schiaffino. Lo que es innegable es que se trata de un retrato tomado del natural.\nEl retrato de Rosas se encuentra inconcluso y su atractivo reside en la fuerza de la cabeza, destacada por el cuerpo volumétrico del poncho negro, listado en bandas amarillas y rojizas, y el cuello blanco de la camisa que genera un tenue contraste que la enmarca. Bartolomé Mitre dejó un comentario interesante, anotado por Eduardo Schiaffino, de esta imagen de Rosas: “es el retrato más parecido que conozco de Rosas, hay mucho de emperador romano, pero atenuado y corregido por un marcado acento criollo”. Más allá de la ironía política de la frase, de eso se trata la implantación de los géneros pictóricos en las periferias: la nota local en una tradición visual de larga duración.\nDentro de la vasta iconografía de Juan Manuel de Rosas, el retrato realizado por Monvoisin es singular. La imagen de Rosas, gobernador de la provincia de Buenos Aires, fue una herramienta de propaganda para construir consenso y coerción, tanto en los grandes óleos retóricos exhibidos públicamente como en litografías de amplia circulación. Una característica general de la iconografía de Rosas es el uniforme militar de brigadier, al que se suman los atributos de su función ejecutiva y la condecoración de la Campaña del Desierto. Los retratos de Rosas con poncho, vestimenta de campaña, solo eran utilizados por la prensa ilustrada de los exiliados en Montevideo (Muera Rosas! y El grito arjentino) para afirmar la diatriba de gaucho bárbaro. \nMonvoisin había llegado en septiembre de 1842 a Buenos Aires, permaneciendo solo tres meses, en los que realizó obras notables como el orientalista Soldado de Rosas y Dama porteña en el templo, ambas en colecciones particulares (1). Desde ya, debía cumplir el compromiso asumido con ciudadanos chilenos en Europa y dirigirse a Santiago para formar una academia de pintura.\nMonvoisin había adquirido una sólida formación, alumno del neoclásico Pierre-Narcisse Guérin, pensionado en Roma entre 1821 y 1825, con presencia habitual en los salones. Solo el encuentro de un buen cliente como el cónsul de Cerdeña, Picolet d’Hermillon, y de encargos de retratos de miembros de la elite porteña como los Llavallol, fueron el motivo de su retraso en Buenos Aires. A pesar de la brevedad de su estadía en la ciudad incorporó un elemento definitorio para su obra posterior: el uso del cuero como soporte.\nEn el MNBA se conservan obras diversas de su autoría, entre ellas se destaca la acuarela con su autorretrato (inv. 8579), procedente de la colección Antonio Santamarina. Desde Santiago de Chile tuvo influencia en el arte argentino, principalmente en el inicio de la pintura de historia por Benjamín Franklin Rawson y en los textos sobre arte de Domingo F. Sarmiento, exiliado en la capital trasandina.',
 (select id from users where username='root'));

INSERT INTO artworkArtist (id, artwork_id, person_id) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Rosas%' limit 1), 
 (select id from person where name like '%Raymond%' limit 1));

 
 INSERT INTO artExhibitionItem  (id, name, title, artExhibition_id, artWork_id, artExhibitionOrder, readcode, site_id, floor_id, room_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select name from artwork where name like '%Emperatriz Theodora%' limit 1), 
 (select title from artwork where name like '%Emperatriz Theodora%' limit 1),
 (select id from artExhibition where name like 'Museo sec%' limit 1),
 (select id from artWork where name like '%Theodora%' limit 1),
  1,
  nextval('readcode_id'),
  (select id from site where name like '%Nacional de Bellas Artes%' limit 1),
  (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
  (select id from room where name like 'Hall Central%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1),
 (select id from users where username='root'));
 

 INSERT INTO artExhibitionItem (id, name, title, artExhibition_id, artWork_id, artExhibitionOrder, readcode, site_id, floor_id, room_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select name from artwork where name like '%Rosas%' limit 1), 
 (select title from artwork where name like '%Rosas%' limit 1),
 (select id from artExhibition where name like 'Museo sec%' limit 1),
 (select id from artWork where name like '%Rosas%' limit 1),
  2,
  nextval('readcode_id'),
  (select id from site where name like '%Nacional de Bellas Artes%' limit 1),
  (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
  (select id from room where name like 'Hall Central%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1),
 (select id from users where username='root'));
 
 INSERT INTO artExhibitionItem  (id, name, title, artExhibition_id, artWork_id, artExhibitionOrder, readcode, site_id, floor_id, room_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select name from artwork where name like '%Valencia%' limit 1), 
 (select title from artwork where name like '%Valencia%' limit 1),
 (select id from artExhibition where name like 'Museo sec%' limit 1),
 (select id from artWork where name like '%Valencia%' limit 1),
  2,
  nextval('readcode_id'),
  (select id from site where name like '%Nacional de Bellas Artes%' limit 1),
  (select id from floor where name like 'Planta Baja%' and site_id  in (select id from site where name like '%Bellas Artes%') limit 1),
  (select id from room where name like 'Hall Central%' and floor_id in (select id from floor where name like '%Planta Baja%' and site_id in (select id from site where name like '%Bellas Artes%')  ) limit 1),
 (select id from users where username='root'));
 
 
 
delete from artExhibitionGuide where name like '%Museo Secreto%';

insert into artExhibitionGuide (id, name, title, publisher_id, artexhibition_id, official, lastmodifiedUser) values (nextval('sequence_id'),  'Museo Secreto',  'Museo Secreto. Del archivo a la sala', (select id from person where name like 'Alejandro%' limit 1), (select id from artExhibition where name like 'Museo secreto%' limit 1), true,  (select id from users where username='root'));  


-- 1. Theodora
INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 1,
 (select name from artwork where name like '%Theodora%' limit 1), 
 (select title from artwork where name like '%Theodora%' limit 1), 
 (select id from artExhibitionGuide where lower(name) like '%secreto%' limit 1),  
 (select id from artExhibitionItem where name like '%Theodora%' limit 1),  
 (select id from users where username='root'));


INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 2,
 (select name from artwork where name like '%Rosas%' limit 1), 
 (select title from artwork where name like '%Rosas%' limit 1), 
 (select id from artExhibitionGuide where lower(name) like '%secreto%' limit 1),  
 (select id from artExhibitionItem where name like '%Rosas%' limit 1),  
 (select id from users where username='root'));



INSERT INTO GuideContent (id, guideOrder, name, title, artExhibitionGuide_id, artExhibitionItem_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 3,
 (select name from artwork where name like '%Valencia%' limit 1), 
 (select title from artwork where name like '%Valencia%' limit 1), 
 (select id from artExhibitionGuide where lower(name) like '%secreto%' limit 1),  
 (select id from artExhibitionItem where name like '%Valencia%' limit 1),  
 (select id from users where username='root'));


UPDATE  artWork  SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');

COMMIT;

