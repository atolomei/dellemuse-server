

BEGIN;
set client_encoding to 'utf8';
COMMIT;




BEGIN;



update site set info='El Museo Nacional de Bellas Artes (MNBA), ubicado en la Ciudad de Buenos Aires, es una de las instituciones públicas de arte más importantes de Argentina. Alberga un patrimonio sumamente diverso, que incluye más de 12 000 piezas, entre pinturas, esculturas, dibujos, grabados, textiles y objetos. Su colección está integrada por arte precolombino, colonial, argentino e internacional, en un rango temporal que va del siglo III a. C. a la actualidad.\n martes a viernes de 11 a 20 \n sábados y domingos de 10 a 20 \n(lunes: cerrado).', address='Avenida del Libertador 1473, Ciudad Autónoma de Buenos Aires', website='https://www.bellasartes.gob.ar', phone='54 911 64324235' where lower(name) like '%bellas artes%';

update site set info='El Museo de Artistas Argentinos "Benito Quinquela Martín" está ubicado en el barrio de La Boca, de la Comuna 4 en la Ciudad Autónoma de Buenos Aires, Argentina. Fundado en el año 1938, está construido sobre terrenos originalmente donados por el pintor. En esos terrenos, en 1936 se inauguró la Escuela Pedro de Mendoza y dos años más tarde abrió las puertas el museo. Denominado originalmente Museo de Artistas Argentinos. Cuenta actualmente con la mayor colección reunida del artista plástico, más de 90 de sus obras.\n  Martes a domingos y feriados de 11.15 a 18 (UTC-3).', address='	Pedro de Mendoza 1835, Ciudad Autónoma de Buenos Aires',website='https://buenosaires.gob.ar/educacion/gestion-cultural/museo-benito-quinquela-martin', phone='54 911 66892321' where lower(name) like '%uinquela%';

COMMIT