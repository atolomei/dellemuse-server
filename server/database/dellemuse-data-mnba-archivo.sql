-- ------------------------------------------------------------------------------------------------------------------------------
--
-- Dellemuse / Allemuse
--
-- Museo secreto. De la reserva a la Sala.
--
--

-- psql dellemuse

-- dellemuse=# set client_encoding to 'utf8';
-- dellemuse=# \i dellemuse-datos.sql
-- dellemuse=# \i dellemuse-datos-impresionistas.sql



-- ------------------------------------------------------------------------------------------------------------------------------

BEGIN;
set client_encoding to 'utf8';
COMMIT;


BEGIN;







-- museo secreto

INSERT INTO artExhibition (id,	name, site_id, title, intro, info, opens, lastmodifieduser) VALUES
(nextval('sequence_id'), 
 'Museo secreto. De la reserva a la sala', 
  (select id from site where name like '%Bellas Artes%' limit 1),
  'Museo secreto. De la reserva a la sala', 'El Bellas Artes inicia su programación anual con una muestra antológica que reúne en el Pabellón de exposiciones temporarias cerca de 300 obras provenientes de las reservas.','El Museo Nacional de Bellas Artes inaugura el martes 21 de enero, a las 19, la muestra “Museo secreto. De la reserva a la sala”, que reúne en el Pabellón de exposiciones temporarias cerca de 300 obras de la colección institucional provenientes de las reservas, algunas de ellas exhibidas por primera vez. \n Esta exposición antológica con la que el Bellas Artes inicia su programación anual presenta verdaderos tesoros del acervo del Museo e incluye pinturas, dibujos, grabados, fotografías, esculturas e instalaciones de unos 250 artistas argentinos y extranjeros, desde el siglo XIV hasta la actualidad. \n "Como sucede en las grandes instituciones culturales del mundo, la cantidad de obras que el visitante puede apreciar en las salas del Museo Nacional de Bellas Artes es solo una parte de un conjunto mucho más vasto; es una propuesta, como toda selección, de una de las tantas lecturas posibles para un patrimonio –afirma el director del Bellas Artes, Andrés Duprat–. Lo que se exhibe es, entonces, un panorama de artistas, períodos, géneros y temas representativos de un corpus que, en gran parte, permanece en las reservas”.\n "Museo secreto, el título de esta exposición, hace referencia a un discurso pronunciado en 1926 por Eduardo Schiaffino, primer director del Bellas Artes, acerca de la necesidad de ampliar el espacio de exhibición y poder mostrar el patrimonio que, alojado en los depósitos, permanecía vedado a los ojos de los visitantes”, apunta Duprat.\n “En los 130 años que han transcurrido desde su creación, el Museo ha logrado reunir una colección integrada por más de 13.000 obras, un conjunto cuya magnitud hace que el desafío que acompaña a la institución desde sus primeros años no solo siga vigente, sino que adquiera cada vez mayor complejidad”, agrega el director.\nLa curaduría de esta muestra surge del intercambio de saberes y del trabajo colectivo entre todos los equipos del Bellas Artes. La disposición de las obras recrea el modo en que se presentaban las colecciones en el siglo XIX, como puede apreciarse en la sala Guerrico, a pocos metros del Pabellón. Este tipo de distribución permite disponer en el espacio un mayor número de piezas, y también emula la forma en que se las agrupa en las reservas.\n Concebida en un esquema no lineal, la selección conecta géneros, estilos y temas que han guiado la producción de artistas de todas las épocas, con obras distribuidas en grandes núcleos a modo de constelaciones que habilitan diálogos históricos y estéticos, y que, a la vez, ponen en tensión diversas concepciones del arte como herramienta de representación.','Del 22 de enero de 2025 al 31 de agosto de 2025 \nPabellón de exposiciones temporarias',
 (select id from users where username='root'));



-- UPDATE  person			  SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');
-- UPDATE  artExhibition	  SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');
-- UPDATE  artExhibitionItem SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g');
-- UPDATE  guideContent		SET namekey = regexp_replace(regexp_replace(lower(name),'[^a-z0-9]+', '-', 'g'),'(^-+|-+$)', '', 'g') where (artExhibitionGuide_id in ( select id from artExhibitionGuide where name like '%mpresionismo%'));

COMMIT;








