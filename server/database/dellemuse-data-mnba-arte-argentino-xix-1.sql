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

INSERT INTO person (id, name, lastname,  physicalid, created, lastmodified, lastmodifieduser) VALUES  
(nextval('sequence_id'), 'Ángel' ,         'Della Valle',              '-', now(), now(), (select id from users where username='root')), 
(nextval('sequence_id'), 'María' ,         'Obligado',                 '-', now(), now(), (select id from users where username='root')), 
(nextval('sequence_id'), 'Ernesto' ,       'de la Cárcova',           '-', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Graciano' ,      'Mendihalarzu',      '-', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Eduardo' ,       'Sívori', 			     '-', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Eduardo' ,       'Schiaffino', 			     '-', now(), now(), (select id from users where username='root')),
(nextval('sequence_id'), 'Lucio' ,         'Correa Morales', 			     '-', now(), now(), (select id from users where username='root'));

-- 1. Della Valle - La vuelta del malón
--
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'La vuelta del malón', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'La vuelta del malón',
 'Adqusición Sociedad de Estímulo',
 'Oleo Sobre tela 186,5 x 292 cm.',
 1892,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'La vuelta del malón fue celebrada como la “primera obra de arte genuinamente nacional” desde el momento de su primera exhibición en la vidriera de un negocio de la calle Florida (la ferretería y pinturería de Nocetti y Repetto) en 1892. Pintado con el expreso propósito de enviarlo a la exposición universal con que se celebraría en Chicago el cuarto centenario de la llegada de Colón a América, el cuadro fue exhibido nuevamente en Buenos Aires ese mismo año en la exposición preliminar del envío a Chicago. En la Exposición Colombina obtuvo una medalla (de única clase) y al regreso se exhibió nuevamente en Buenos Aires, en el segundo Salón del Ateneo en 1894.
Fue la obra más celebrada de Della Valle. Presentaba por primera vez en las grandes dimensiones de una pintura de salón una escena que había sido un tópico central de la conquista y de la larga guerra de fronteras con las poblaciones indígenas de la pampa a lo largo del siglo XIX: el saqueo de los pueblos fronterizos, el robo de ganado, la violencia y el rapto de cautivas. En el manejo de la luz y la pincelada se advierte la huella de la formación de Della Valle en Florencia: no solo el aprendizaje con Antonio Ciseri sino también el conocimiento de los macchiaioli y los pintores del Risorgimento italiano. Algunos críticos lo vincularon con los grandes cuadros de historia del español Ulpiano Checa que se había hecho famoso por sus entradas de bárbaros en escenas de la historia de España y del imperio romano. Sin embargo, el cuadro de Della Valle entroncaba con una larga tradición no solo en las crónicas y relatos literarios inspirados en malones y cautivas, sino también en imágenes que, desde los primeros viajeros románticos europeos que recorrieron la región en la primera mitad del siglo XIX, representaron cautivas y malones. En la década de 1870 Juan Manuel Blanes había realizado también algunas escenas de malones que aparecen como antecedentes de esta obra. Casi ninguna, sin embargo, había sido expuesta al público ya que tuvieron una circulación bastante restringida. La vuelta del malón fue, entonces, la primera imagen que impactó al público de Buenos Aires referida a una cuestión de fuerte valor emotivo e inequívoco significado político e ideológico.
Según refiere Julio Botet, a partir de una entrevista al artista en agosto de 1892, el asunto del cuadro se inspiraba en un malón llevado por el cacique Cayutril y el capitanejo Caimán a una población no mencionada. Otro comentario (en el diario Sud-América) ubicaba el episodio en la población de 25 de Mayo. Pero más allá de la anécdota el cuadro aparece como una síntesis de los tópicos que circularon como justificación de la “campaña del desierto” de Julio A. Roca en 1879, produciendo una inversión simbólica de los términos de la conquista y el despojo. El cuadro aparece no solo como una glorificación de la figura de Roca sino que, en relación con la celebración de 1492, plantea implícitamente la campaña de exterminio como culminación de la conquista de América.
Todos los elementos de la composición responden a esta idea, desplegados con nitidez y precisión significativa. La escena se desarrolla en un amanecer en el que una tormenta comienza a despejarse. El malón aparece equiparado a las fuerzas de la naturaleza desencadenadas (otro tópico de la literatura de frontera). Los jinetes llevan cálices, incensarios y otros elementos de culto que indican que han saqueado una iglesia. Los indios aparecen, así, imbuidos de una connotación impía y demoníaca. El cielo ocupa más de la mitad de la composición, dividida por una línea de horizonte apenas interrumpida por las cabezas de los guerreros y sus lanzas. En la oscuridad de ese cielo se destaca luminosa la cruz que lleva uno de ellos y la larga lanza que empuña otro, como símbolos contrapuestos de civilización y barbarie. En la montura de dos de los jinetes se ven cabezas cortadas, en alusión a la crueldad del malón. En el extremo izquierdo se destaca del grupo un jinete que lleva una cautiva blanca semidesvanecida, apoyada sobre el hombro del raptor que se inclina sobre ella. Fue este el fragmento más comentado de la obra, a veces en tono de broma, aludiendo a su connotación erótica, o bien criticando cierta inadecuación del aspecto (demasiado “civilizado” y urbano) de la mujer y de su pose con el resto de la composición.
La vuelta del malón fue llevada a la Exposición Colombina de Chicago por el oftalmólogo Pedro Lagleyze, amigo del artista, en medio de la desorganización y dificultades que rodearon ese envío oficial. Fue exhibida en el pabellón de manufacturas, como parte del envío argentino, junto a bolsas de cereales, lanas, cueros, etc. Los pocos comentarios que recibió se refirieron a la escena representada como una imagen de las dificultades que la Argentina había logrado superar para convertirse en una exitosa nación agroexportadora.
Ángel Della Valle pintó una versión reducida de La vuelta del malón para obsequiar a Lagleyze al regreso. Conocida como “malón chico” ha sido con frecuencia tomada por un boceto. También pintó más tarde algunos fragmentos aislados de su gran tela: el grupo del guerrero y la cautiva y el indio que enarbola la cruz.
Della Valle había comenzado a pintar cuadros de tema pampeano durante su estadía en Florencia. En 1887 envió a Buenos Aires varias obras, entre las que pudo verse un indio a caballo (En la pampa) y La banda lisa, que aparecen como tempranas aproximaciones al tema de La vuelta del malón.
La pintura fue solicitada por el director del MNBA, Eduardo Schiaffino, a la familia del artista tras su muerte en 1903; esta optó por donarla a la Sociedad Estímulo de Bellas Artes con el cargo de su venta al MNBA a fin de instituir un premio anual de pintura denominado “Ángel Della Valle”',
 (select id from users where username='root'));
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%malón%' limit 1), 
 (select id from person where lastname like '%Valle%' limit 1),
 (select id from users where username='root'));


-- 2. Obligado - En Normandie
--
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'En Normandie', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'En Normandie',
 'Colección particular. Cedida en comodato al Museo Nacional de Bellas Artes, 2021.',
 'Oleo Sobre tela 162 x 207 cm.',
 1902,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 '',
 (select id from users where username='root'));
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%En Normandie%' limit 1), 
 (select id from person where lastname like '%Obligado%' limit 1),
 (select id from users where username='root'));



-- 3. Ernesto de la Cárcova - 
--
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Sin pan y sin trabajo', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Sin pan y sin trabajo',
 '',
 'Oleo Sobre tela 125,5 x 216 cm.',
 1894,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Sin pan y sin trabajo es el primer cuadro de tema obrero con intención de crítica social en el arte argentino. Desde el momento de su exhibición ha sido una pieza emblemática del arte nacional: comentado, reproducido, citado y reapropiado por sucesivas generaciones de artistas, historiadores y críticos hasta la actualidad. Fue pintado por Ernesto de la Cárcova en Buenos Aires al regreso de su viaje de estudios en Turín y Roma, donde había comenzado su ejecución antes de partir. Allí dejó al menos un boceto en obsequio a Pío Collivadino, el artista argentino que a su llegada ocupó el taller que De la Cárcova dejaba en la Via del Corso 12.
Había comenzado su formación europea en la Real Academia de Turín, donde fue admitido con una obra (Crisantemos) en la exposición de 1890. Luego había pasado a Roma, donde continuó su formación en los talleres de Antonio Mancini y Giacomo Grosso. Una obra suya (Cabeza de viejo) fue premiada con medalla de plata y adquirida en 1892 para la Galería Real de Turín; también obtuvo una medalla de oro en Milán en 1893 (1). Estos antecedentes hicieron que a su regreso, a los 28 años, fuera miembro del jurado del Ateneo, de modo que Sin pan y sin trabajo, celebrado como el gran acontecimiento artístico del Salón, quedó fuera de concurso.
El cuadro responde a un estilo naturalista y a una temática que tuvieron una importante presencia en los salones europeos de los años finales del siglo XIX: grandes pinturas resueltas en tonos sombríos que desplegaban escenas dramáticas de miseria y de los contemporáneos conflictos sociales urbanos. El espíritu crítico que sin duda alimentó aquellas composiciones naturalistas finiseculares se diluyó en los cuadros de salón, en el interés por figurar en grandes competencias con posiciones enfrentadas al arte académico más conservador. Sin embargo, Sin pan y sin trabajo no fue pintado para competir en un salón europeo: fue la obra con la que De la Cárcova se presentó al regreso en el segundo Salón del Ateneo en Buenos Aires, tras haberse afiliado al recién creado Centro Obrero Socialista (antecedente inmediato del Partido Socialista, fundado dos años después) (2). No había en Buenos Aires una tradición académica sino que el grupo de artistas del Ateneo procuraba dar sus primeros pasos. Por otra parte, a partir de la crisis de 1890, la inmensa afluencia de inmigrantes europeos que llegaban de Europa en busca de trabajo en Buenos Aires comenzaba a percibirse en forma conflictiva.
',
 (select id from users where username='root'));
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Sin pan y sin trabajo%' limit 1), 
 (select id from person where lastname like '%Cárcova%' limit 1),
 (select id from users where username='root'));


-- 4. Mendihalarzu - La vuelta al hogar
--
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'La vuelta al hogar', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'La vuelta al hogar',
 'Adqusición Sociedad de Estímulo',
 'Oleo Sobre tela 90,5 x 118,5 cm',
 1885,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'La vuelta al hogar  presenta una escena dramática donde el padre de familia, que el observador no ve,  muere rodeado de sus seres queridos. La atención del espectador se centra en el momento del abrazo de la hija con un hombre. La mujer, recién llegada de la ciudad, se distingue por su ropa urbana, el peinado y el sombrero. En el piso y en primer plano el artista coloca la pequeña cartera haciendo uso del recurso del repoussoir. Éste, muy usado en la  pintura de género, le permite al contemplador ingresar a la escena y dirigir la mirada  hacia el abrazo del hombre junto a la joven recién llegada  y, hacia atrás, a la silla de  juncos, que divide  la composición en dos. La madre da la espalda a los otros personajes,  todos ellos, inmersos en el drama de la situación, visten ropas sencillas propias del ámbito rural. El interior de la habitación está iluminado con una fuente de luz que proviene de atrás del lecho, y se dirige  especialmente hacia los protagonistas destacando sus gestos.
 
El MNBA también conserva un estudio y un croquis de esta pintura, donados por Carlos Vega Belgrano, amigo personal del artista. En el primero -a lápiz- estudia el fuelle, el farol, la cama con detalles de las mantas, mostrando gran interés en cada componente de la escena. En el otro, una tinta sobre papel, plantea la composición final sobre la que hará posteriormente algunas variantes en el vestuario de la muchacha y sus accesorios.[1]
Mendilaharzu pasaba las tardes en el Museo del Louvre. En 1876 le escribe a su maestro diciéndole: “A la mañana voy al taller, y a la tarde hago esbozos en el Louvre”.[2]En este museo parisino el artista  puede haber visto pinturas con una iconografía afín a La vuelta al hogar, como por ejemplo el óleo El hijo castigado de 1778, de Jean-Baptiste Greuze, en el cual el hijo retorna a casa ante la enfermedad de su padre.
El pintor argentino, que había comenzado el camino artístico bajo la guía de Martín Boneo en Buenos Aires, ingresó a la Escuela Julien de la ciudad francesa de Bayona en 1873. Su protector allí fue Achiles Zó (Zoo) y su maestro el pintor Léon-Joseph Bonnat. Permaneció en Europa hasta 1887.
La vuelta al hogar fue pintada en Francia durante su primera estancia. Algunas de sus obras fueron seleccionadas para participar de los Salones de París entre 1879 a 1884 y en la edición de 1886.
Abordó también  el género del retrato, la pintura de historia y la naturaleza muerta. En este  último caso recibió los consejos del pintor Antoine Vollon que realizaba bodegones inspirado en el arte holandés.
El Ateneo inauguró una exposición-homenaje de Graciano Mendilaharzu el 26 de septiembre de 1894, organizada por Eduardo Schiaffino y Augusto Ballerini donde se presentaron 97 pinturas. El artista había integrado el grupo fundador del Ateneo y fue parte del jurado de la primera edición del salón en 1893.
Según Eduardo Schiaffino esta pintura es “la obra principal del artista” y  señala que en uno de los croquis la titula: Le retour au village.[3] El crítico e historiador la describió con estas palabras: “El drama desarrollado es de carácter doble; un labriego medianamente acomodado, acaba de morir en la modesta alcoba, rodeado de los suyos, la mujer, el hijo y el hermano, sumidos en un dolor sin gestos. En tal momento el autor introduce un elemento nuevo en la persona de la hija, destinado a complicar el drama; la recién venida aparece en traje de viaje, con un vestido casi elegante, que contrasta con la pobreza de la familia; sin duda baja recién del tren y llega tarde para encontrar al padre;  la madre la siente entrar, la sabe allí a sus espaldas, pero no corre a su encuentro, si siquiera se vuelve. Si es cierto que aquella desventurada tan bien puesta, ha sido alguna vez hija suya, hoy ya no la reconoce, nada hay de común entre ellas sino el pasado que llora… Empero el perdón despunta en el gesto del hermano cuyos brazos se abren para recibirla; la pobre muchacha cae sollozando en ellos, con inmenso abandono”.[4]
La vuelta al hogar  fue donada al MNBA por Valentina Seminario de Mendilaharzu, viuda del pintor, acompañada de  una carta fechada el 23 de enero de 1896, dirigida  a Eduardo Schiaffino donde  señala: “[…] queriendo contribuir en lo posible, a la realización del ideal de mi finado esposo; me impongo el deber de dirigir a Ud., como Director del Museo Nacional de Bellas Artes, una de sus producciones de más predilección ‘La vuelta’. Rogándole quiera aceptar la donación y tener la amabilidad de darle una humilde colocación”.[5] El primer director del museo le responde: “Recibo con profunda satisfacción el importante obsequio que generosamente ofrece Ud. al Museo Nacional de Bellas Artes; ello nos permitirá inaugurarlo -como todos sus amigos lo deseábamos- con una de las más bellas obras de nuestro malogrado artista Graciano Mendilaharzu, arrebatado tan pronto al cariño de los suyos y a las bien fundadas esperanzas de sus compatriotas. ‘La vuelta al hogar’ será tan bien colocada como es bienvenida”.',
 (select id from users where username='root'));
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%La vuelta al hogar%' limit 1), 
 (select id from person where lastname like '%Mendihalarzu%' limit 1),
 (select id from users where username='root'));



-- 5. Sívori - El despertar de la criada
--
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'El despertar de la criada', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'El despertar de la criada',
 '',
 '',
 1887,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Le lever de la bonne es un desnudo naturalista. Aun cuando el título y algunos elementos de la composición lo connotan, la pintura pertenece al género que a lo largo del siglo XIX fue campo de batalla de las audacias modernistas. No hay narratividad en la escena, se limita a presentar el cuerpo de una muchacha joven en el que se lee su pertenencia a la clase trabajadora. La sencillez del mobiliario, las ropas amontonadas sobre un banco de paja, al pie de la cama sin tender y, sobre todo, el título del cuadro, indican que se trata de una criada. Un foco de luz dirigida desde la izquierda ilumina ese cuerpo que se destaca con intensidad dramática sobre el fondo neutro de la pared de fondo. La piel de la muchacha es oscura, sobre todo en las zonas que el cuerpo de una mujer de trabajo se veía expuesto al sol: las manos, el rostro y las piernas. La criada aparece ensimismada en la tarea de dar vuelta una media para calzarla, de modo que el contraste entre los pechos y la mano castigada por la intemperie se hace más evidente. Cruzadas una sobre la otra, las piernas, gruesas y musculosas se destacan con un tratamiento naturalista que se detiene en la representación minuciosa de unos pies toscos y maltratados. El pubis, invisible tras la pierna cruzada, se ubica en el centro exacto de la composición. Ninguno de estos detalles pasó inadvertido a los críticos que, tanto en París como en Buenos Aires, comentaron el cuadro en 1887.
Fue pintado en París por Eduardo Sívori quien, tras haber logrado su aceptación en el Salón anual, lo envió a Buenos Aires ese mismo año sabiendo de antemano que su exhibición despertaría polémicas. Fue el primer gesto vanguardista en la historia del arte argentino.
Sívori ofreció su tela en donación a la Sociedad Estímulo de Bellas Artes, en cuya fundación él mismo había tenido un papel fundamental. La llegada del cuadro desde París, probablemente traído por Eduardo Schiaffino, fue precedida por una serie de artículos de prensa en los que la misma Sociedad Estímulo anunció que era un cuadro problemático, que sería de exhibición restringida, y que había recibido en París algunos comentarios (que fueron traducidos íntegramente) en los que se ponía en duda el buen gusto del artista al encarar un tema semejante.
En 1887 la pintura naturalista ocupaba un lugar destacado en el Salón de París, como una de las vías de renovación de la estética oficial de la Academia. Sin alejarse demasiado de las convenciones formales impuestas por la tradición (claroscuro, perspectiva, tratamiento de la superficie) los pintores naturalistas siguieron una línea de renovación iconográfica abierta a mediados de siglo por Gustave Courbet y Jean-François Millet, introduciendo temas derivados de la literatura de Émile Zola, o que planteaban una denuncia directa de los conflictos sociales contemporáneos, en un tono en general narrativo y melodramático. No fue el desnudo un género frecuente en la pintura naturalista. El cuadro de Sívori fue enseguida interpretado por la crítica francesa (Roger- Milès, Emery, E. Benjamin, Paul Gilbert, entre ellos) como obra derivada de Zola, un poco “excesivo” en la representación de un cuerpo que fue visto como feo, sucio y desagradable.
En Buenos Aires, donde no había habido hasta entonces más que pocas y discutidas exhibiciones de desnudos artísticos, el cuadro fue objeto no solo de una intensa polémica en la prensa (fue calificado de “indecente” y “pornográfico”) sino también de un importante alineamiento de intelectuales y artistas en su favor. En una reunión de su Comisión Directiva, el 22 de agosto de 1887, la Sociedad Estímulo de Bellas Artes decidió exhibir el cuadro en su local, cursar invitaciones especiales a los socios y a los periodistas de la capital, y abrir un álbum que recogiera las firmas de todos aquellos “que quieran manifestar al autor sus felicitaciones por los progresos realizados”. Más de 250 firmas de artistas, escritores, etc. se estamparon en ese álbum en cuyas páginas Sívori guardó además los recortes de las críticas recibidas y fotografías de ese y otros cuadros suyos que habían sido expuestos en el Salón de París hasta su regreso definitivo en 1891.
La fotografía de Le lever de la bonne conservada en ese álbum presenta algunas diferencias con el cuadro definitivo. No sabemos si las modificaciones fueron hechas antes o después de ser exhibido en el Salón de París. En la mesilla de noche puede verse una palangana y una jarra (elementos de higiene) en lugar del candelabro con una vela apagada de la versión final. Por otra parte, en la pared del fondo se vislumbra un estante con frascos y potes de tocador. Todos estos elementos pueden verse a simple vista cuando el cuadro se mira con una luz potente, como si el artista hubiera decidido dejar que aquellos arrepentimientos se adivinen en el fondo en penumbras. Pero lo más significativo es el cambio en la fisonomía de la criada. Su rostro y su peinado aparecen en la fotografía menos oscuros. La criada parece una faubourgienne en la versión de la fotografía. Tal vez más cercana a la apariencia de una prostituta (los elementos de higiene también contribuyen a ello), tema predilecto de la vanguardia y de la crítica social de la época. Aun modificada, la criada fue interpretada como prostituta y considerada pornográfica por varios de sus primeros comentadores. Su transformación es significativa. Tal vez el artista decidió alejarse del “tema” social de moda al presentarse al Salón. Tal vez decidió transformarla inequívocamente en una criada pobre para su exhibición en Buenos Aires.',
 (select id from users where username='root'));
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%El despertar de la criada%' limit 1), 
 (select id from person where lastname like '%Sívori%' limit 1),
 (select id from users where username='root'));


-- 6. Schiaffino - Reposo
--
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Reposo', 
 (select id from artWorkType where name like '%Pintura%' limit 1),
 'Reposo',
 'Adquirido a Schiaffino, Eduardo por Ministerio de Justicia e Instrucción Pública',
 '109 x 200 cm.',
 1889,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 '​Reposo fue pintado por Eduardo Schiaffino en París y admitido en la Exposición Universal con la que se celebró el centenario de la Revolución Francesa, donde obtuvo una medalla de tercera clase (bronce). Su autor fue el fundador y primer director del Museo Nacional de Bellas Artes, además de activo promotor de las bellas artes en Buenos Aires y primer historiador del arte argentino. En su libro La pintura y la escultura en Argentina (1933) comentó su propia obra en tercera persona, destacando la importancia de aquella medalla (solo el escultor Manuel de Santa Coloma había sido premiado en París 25 años antes que él y otros 25 años habrían de pasar antes de que otro argentino, Antonio Alice, volviera a ser premiado allí).
El cuadro presenta un desnudo de espaldas, en una pose algo forzada, con las piernas extendidas y cruzadas y un brazo arqueado sobre la cabeza. El cuerpo aparece pálido y azulado en medio de un espacio enteramente azul, en el que el paño sobre el que se encuentra tendido y el fondo tienen apenas diferencias de valor. Esta ambigüedad espacial se ve interrumpida solamente por un pequeño fragmento de piso, sobre el que se advierte el borde del paño (con brillos de terciopelo), en el ángulo inferior izquierdo de la tela. Hay también cierta ambigüedad de género en ese cuerpo casi adolescente, con el cabello muy corto y la cara oculta, en una posición que aparece como una reelaboración del Hermafrodito durmiente, el mármol helenístico restaurado por Bernini, del Museo del Louvre. Es tal vez el primer cuadro de inspiración simbolista pintado por Schiaffino, quien hasta entonces había exhibido algunas “impresiones” de paisaje y escenas de toilette más cercanas al estilo y la iconografía impresionistas. Jaime de la María comentó el cuadro para La Nación (2 de julio de 1890) atribuyéndole un estilo análogo al del maestro de Schiaffino en París, Pierre Puvis de Chavannes: “la analogía de carácter podrá parecer casual, pero la de estilo se explica: Schiaffino es puvisiste”. La atmósfera simbolista de Reposo adquiere un carácter más marcado en varias obras posteriores del artista, como Vesper (inv. 5377, MNBA), Craintive o Margot, tres retratos exhibidos al año siguiente en la exposición de la Sociedad de Damas de Nuestra Señora del Carmen, o el Desnudo (sinfonía en rojo) (inv. 7463, MNBA) que expuso en el Salón del Ateneo de 1895. Margot (inv. 1656, MNBA) fue celebrada en varios artículos como la primera obra en la que se revelaba su calidad como artista. Pero la polémica que entabló con el crítico que firmaba A.Zul de Prusia a propósito de la supuesta autoría de las obras de los pensionados en Europa, terminó ese año con un duelo a pistola.
Schiaffino había viajado trayendo Reposo a Buenos Aires en medio de la crisis de 1890, con el objeto de exhibirlo y gestionar la renovación de su beca de estudios. Tanto la exposición de la pintura en la vidriera de la casa Bossi (donde el año anterior su primer envío desde París había sido comentado con franca hostilidad) como el otorgamiento de la beca, recibieron comentarios muy negativos en la prensa. Un comentarista anónimo del diario La Argentina (1 de julio de 1890) lo objetó por encontrarlo deforme e indecente, “con una sans-façon que huele a la legua al Paganismo y a sus más florecientes saturnales”. El artista encaró personalmente la defensa de su cuadro, que volvió a exponer en el primer Salón del Ateneo, en 1893, y nuevamente en el cuarto Salón del Ateneo en 1896, recogiendo en ambas ocasiones severas críticas, no solo hacia la pintura sino también al hecho de volver a incluirla en los salones cuando era una obra que no era nueva y que ya había sido expuesta y criticada con dureza. Es posible advertir en el gesto de Schiaffino una posición desafiante en consonancia con su tenaz actividad orientada –en sus propias palabras– a “educar el gusto” del público de Buenos Aires, introduciendo audacias del arte moderno en un género que a lo largo del siglo XIX se había tornado emblemático de aquellas. En Buenos Aires todavía resonaba el escándalo que había suscitado, en 1887, Le lever de la bonne de Eduardo Sívori.',
 (select id from users where username='root'));
 
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
  (nextval('sequence_id'), 
 (select id from artWork where name like '%Reposo%' limit 1), 
 (select id from person where lastname like '%Schiaffino%' limit 1),
 (select id from users where username='root'));

-- 7. Morales - Abel
--
INSERT INTO artwork (id, name, artWorkType_id, title, subtitle,	spec, year, institution_owner_id, info, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Abel', 
 (select id from artWorkType where name like '%Escultura%' limit 1),
 'Abel',
 '',
 'Bronce 47 x 198 x 98 cm.',
 1902,
 (select id from institution where name like '%Nacional de Bellas%' limit 1), 
 'Lucio Correa Morales, formado en Florencia con Urbano Lucchesi, realizó una vasta producción de retratos, esculturas funerarias, monumentos conmemorativos y obras decorativas, además de dedicarse a la enseñanza de la escultura en la Sociedad Estímulo de Bellas Artes, origen de la Academia en la Argentina.
En 1902 realizó un boceto de Abel en terracota y un segundo en bronce, conservados ambos en colecciones privadas, con la figura aislada del joven sin vida asesinado por su hermano Caín. En mayo de 1903 fue asociado por un crítico de La Nación con la escultura Abel del italiano Giovanni Duprè, un mármol de 1842, a pesar de las diferencias compositivas.
Las expresiones del mismo Correa Morales nos revelan las verdaderas intenciones de su trabajo, pues era consciente de que “Nadie comprendió ni tal vez se supuso que ese ‘Abel’ no era el que fue muerto por Caín, sino el arte argentino muerto por sus hermanos. En esa figura hasta mi nombre le había disfrazado; […] yo gustoso habría firmado Moralaine, Moralini ó Moralai” (1). Este comentario permite interpretar la frase como alusión a la falta de desarrollo y de interés por el arte nacional: “Muerto sin descendencia como miserable / Es fácil sacar la conclusión / Que nosotros somos toda raza de Caín”.
La versión en yeso de Abel obtuvo la medalla de plata de Escultura en la Exposición Internacional de Saint Louis en 1904. Schiaffino, responsable del envío argentino, había reclamado al jurado una medalla de oro para Lucio Correa Morales pero su propuesta fue rechazada, al ser criticada la proporción inadecuada de los brazos de la figura (2). El reconocimiento internacional estimuló la posibilidad de su fundición en bronce, junto con otras obras premiadas como Las pecadoras de Rogelio Yrurtia.',
 (select id from users where username='root'));
 
 INSERT INTO artworkArtist (id, artwork_id, person_id,  lastmodifieduser) VALUES
 (nextval('sequence_id'), 
 (select id from artWork where name like '%Abel%' limit 1), 
 (select id from person where lastname like '%Morales%' limit 1),
 (select id from users where username='root'));


update artExhibitionGuide set namekey='arte-argentino-xix' where name like 'Arte argentino siglo XIX%';

UPDATE  person			  SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');
UPDATE  artExhibition	  SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');
UPDATE  artExhibitionItem SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');

update artwork set title = name where title is null;

COMMIT;








