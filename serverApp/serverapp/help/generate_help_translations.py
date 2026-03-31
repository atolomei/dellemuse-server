#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Generate English and Portuguese help files from Spanish originals."""

import os
import re

BASE = "/Users/alejandrotolomei/eclipse-workspace/dellemuse-server/serverApp/serverapp/help"
SPA = os.path.join(BASE, "spa")
EN = os.path.join(BASE, "en")
PT = os.path.join(BASE, "pt")

# Ordered from longest to shortest to avoid partial replacements
# FORMAT: (spanish, english, portuguese)
translations = [
    # === Tutorials section (common footer) ===
    ("Conceptos fundamentales", "Fundamental concepts", "Conceitos fundamentais"),
    ("Crear Exhibiciones", "Create Exhibitions", "Criar Exposições"),
    ("Audio guías", "Audio guides", "Audioguias"),
    ("Audio studio", "Audio studio", "Estúdio de Áudio"),
    ("Portal público", "Public portal", "Portal público"),
    ("Tutoriales", "Tutorials", "Tutoriais"),
    ("(53 segs)", "(53 secs)", "(53 segs)"),
    ("(2:20 min)", "(2:20 min)", "(2:20 min)"),
    ("(2 min)", "(2 min)", "(2 min)"),

    # === Title ===
    (">Ayuda<", ">Help<", ">Ajuda<"),

    # === artexhibition-info ===
    ("Información de la Exhibición", "Exhibition Information", "Informação da Exposição"),
    ("Cada exhibición tiene obras y objetos que se agregan del archivo de la sede, y puede ser permanente o temporaria.",
     "Each exhibition has artworks and objects that are added from the site's collection, and can be permanent or temporary.",
     "Cada exposição tem obras e objetos que são adicionados do acervo do local, e pode ser permanente ou temporária."),
    ("El formulario de información principal es en el idioma principal de la sede; y además existen formularios de información\n\t\t\t\ten cada uno de los idiomas secundarios de la sede. El 'orden' se utiliza para ordenar las exhibiciones en la aplicación de consulta de los visitantes\n\t\t\t\t(en caso que la configuración de la sede sea por orden y no alfabético)",
     "The main information form is in the site's primary language; there are also information forms\n\t\t\t\tfor each of the site's secondary languages. The 'order' field is used to sort exhibitions in the visitors' app\n\t\t\t\t(if the site is configured to sort by order rather than alphabetically)",
     "O formulário de informação principal é no idioma principal do local; além disso, existem formulários de informação\n\t\t\t\tem cada um dos idiomas secundários do local. O campo 'ordem' é utilizado para ordenar as exposições no aplicativo dos visitantes\n\t\t\t\t(caso a configuração do local seja por ordem e não alfabética)"),
    ("La traducción automática a cada idioma secundario se realiza tomando como fuente el texto del idioma\n\t\t\t\tprincipal.",
     "Automatic translation to each secondary language is done using the primary language text\n\t\t\t\tas source.",
     "A tradução automática para cada idioma secundário é realizada utilizando como fonte o texto do idioma\n\t\t\t\tprincipal."),
    ("obras y objectos de la exhibición", "Exhibition artworks and objects", "Obras e objetos da exposição"),
    ("Conjunto de obras y objetos de la exhibición. Se agregan del archivo de obras y objetos de la sede.",
     "Collection of artworks and objects in the exhibition. They are added from the site's artwork and object archive.",
     "Conjunto de obras e objetos da exposição. São adicionados do acervo de obras e objetos do local."),
    ("Cada exhibición puede tener una o más audio guías.",
     "Each exhibition can have one or more audio guides.",
     "Cada exposição pode ter um ou mais audioguias."),

    # === artexhibitionguide-info ===
    ("Información de la Audio guía", "Audio Guide Information", "Informação do Audioguia"),
    ("Una Audio guía incluye un", "An Audio guide includes an", "Um Audioguia inclui um"),
    ("audio introductorio de la exhibición", "exhibition introductory audio", "áudio introdutório da exposição"),
    ("obras y objetos", "artworks and objects", "obras e objetos"),
    ("de la exhibición.\n\t\t\t\t  Los audios se pueden generar y publicar en todos los idiomas de la sede.",
     "in the exhibition.\n\t\t\t\t  Audio files can be generated and published in all the site's languages.",
     "da exposição.\n\t\t\t\t  Os áudios podem ser gerados e publicados em todos os idiomas do local."),
    ("La audio guía puede ser de dos tipos:", "The audio guide can be of two types:", "O audioguia pode ser de dois tipos:"),
    ("y los visitantes pueden elegir en el portal público el tipo de audio\n\t\t\t\tguías que consultan.",
     "and visitors can choose in the public portal the type of audio\n\t\t\t\tguides they access.",
     "e os visitantes podem escolher no portal público o tipo de\n\t\t\t\taudioguias que consultam."),
    ("Sólo las audio guías en estado publicada son accesibles por los visitantes en el portal público.",
     "Only audio guides in published state are accessible by visitors on the public portal.",
     "Somente os audioguias em estado publicado são acessíveis pelos visitantes no portal público."),
    ("Las que están en estado borrada serán eliminadas automáticamente del sistema. Los íconos son:",
     "Those in deleted state will be automatically removed from the system. The icons are:",
     "Os que estão em estado excluído serão eliminados automaticamente do sistema. Os ícones são:"),
    (">Edición<", ">Edition<", ">Edição<"),
    (">Borrada<", ">Deleted<", ">Excluído<"),
    ("El número de audio se utiliza en el buscador por número de audio, debe ser agregado en la obra u objeto físico\n\t\t\t\texhibido para que el visitante pueda usarlo cuando está frente al objeto y quiere escuchar el audio correspondiente\n\t\t\t\ten su teléfono.",
     "The audio number is used in the audio number search, it must be added to the physical artwork or object\n\t\t\t\ton display so that the visitor can use it when standing in front of the object and wants to listen to the corresponding audio\n\t\t\t\ton their phone.",
     "O número do áudio é utilizado no buscador por número de áudio, deve ser adicionado na obra ou objeto físico\n\t\t\t\texibido para que o visitante possa usá-lo quando estiver diante do objeto e quiser ouvir o áudio correspondente\n\t\t\t\tno seu telefone."),
    ("Audio. Introducción de la Exhibición", "Audio. Exhibition Introduction", "Áudio. Introdução da Exposição"),
    ("Audio del texto introductorio de la audio guía de la exhibición, puede ser cargado directamente en el formulario o\n\t\t\t\tgenerado automáticamente con Inteligencia Artificial desde el",
     "Audio of the audio guide's introductory text for the exhibition, it can be uploaded directly in the form or\n\t\t\t\tautomatically generated with Artificial Intelligence from the",
     "Áudio do texto introdutório do audioguia da exposição, pode ser carregado diretamente no formulário ou\n\t\t\t\tgerado automaticamente com Inteligência Artificial a partir do"),

    # === artist-info ===
    ("Información del Artista", "Artist Information", "Informação do Artista"),
    ("Cada sede tiene su propio archivo de artistas.", "Each site has its own artists archive.", "Cada local tem seu próprio arquivo de artistas."),

    # === artist-list ===
    ("Cada sede administra sus artistas en forma autónoma y no puede acceder a los artistas de otra sede.",
     "Each site manages its artists independently and cannot access artists from another site.",
     "Cada local administra seus artistas de forma autônoma e não pode acessar os artistas de outro local."),
    ("Esta sección contiene un listado de todos los artistas de todas las sedes y sirve solamente a los administradores del sistema para control y soporte.",
     "This section contains a list of all artists from all sites and is only for system administrators for control and support.",
     "Esta seção contém uma lista de todos os artistas de todos os locais e serve somente aos administradores do sistema para controle e suporte."),

    # === artwork-info ===
    ("Información de Obra u Objecto", "Artwork or Object Information", "Informação de Obra ou Objeto"),
    ("Cada sede tiene su propio archivo de obras y objetos, con formularios de información en los idiomas de la sede.",
     "Each site has its own artwork and object archive, with information forms in the site's languages.",
     "Cada local tem seu próprio acervo de obras e objetos, com formulários de informação nos idiomas do local."),
    ("Para las obras de arte se puede usar el campo artistas, mientras que para los objetos que no tienen un autor específico se puede usar el campo de texto 'fuente'.",
     "For artworks, the artists field can be used, while for objects without a specific author, the 'source' text field can be used.",
     "Para as obras de arte pode-se usar o campo artistas, enquanto para os objetos que não têm um autor específico pode-se usar o campo de texto 'fonte'."),
    ("Número de audio", "Audio number", "Número de áudio"),
    ("El número de audio es generado automáticamente por el sistema y sirve para el buscador de audio guías.\n\t\t\t\t  La institución deberá poner el número de audio y/o la imagen del QR en una tarjeta o cartel junto a la obra u objeto exhibido \n\t\t\t\t  para que el visitante pueda buscarlo en su teléfono.",
     "The audio number is automatically generated by the system and is used for the audio guide search.\n\t\t\t\t  The institution must place the audio number and/or the QR image on a card or sign next to the exhibited artwork or object \n\t\t\t\t  so that the visitor can look it up on their phone.",
     "O número de áudio é gerado automaticamente pelo sistema e serve para o buscador de audioguias.\n\t\t\t\t  A instituição deverá colocar o número de áudio e/ou a imagem do QR em um cartão ou placa junto à obra ou objeto exibido \n\t\t\t\t  para que o visitante possa buscá-lo no seu telefone."),

    # === audio-studio ===
    ("Consola para generar audios de obras e introductorios de audio guía.",
     "Console for generating artwork audio and audio guide introductions.",
     "Console para gerar áudios de obras e introduções de audioguia."),
    ("1. generar el audio de voz", "1. Generate voice audio", "1. Gerar o áudio de voz"),
    ("El texto del audio se toma del contenido y la voz se debe seleccionar de las voces disponibles en el idioma",
     "The audio text is taken from the content and the voice must be selected from the available voices in the language",
     "O texto do áudio é retirado do conteúdo e a voz deve ser selecionada das vozes disponíveis no idioma"),
    ("biblioteca de voces", "voice library", "biblioteca de vozes"),
    ("2. Agregar intro de música", "2. Add music intro", "2. Adicionar introdução de música"),
    ("Este paso es opcional. La música puede seleccionarse de los archivos disponibles en el sistema",
     "This step is optional. Music can be selected from the files available in the system",
     "Este passo é opcional. A música pode ser selecionada dos arquivos disponíveis no sistema"),
    ("biblioteca de música", "music library", "biblioteca de música"),
    ("o bien pegar\n\t\t\t\tuna url al archivo de música para el sistema lo descargue y lo utilice. En este caso el usuario es responsable de\n\t\t\t\tcontar con la licencia de uso de dicho archivo de música.",
     "or paste\n\t\t\t\ta URL to the music file so the system can download and use it. In this case the user is responsible for\n\t\t\t\thaving the usage license for that music file.",
     "ou colar\n\t\t\t\tuma URL do arquivo de música para que o sistema o baixe e utilize. Neste caso o usuário é responsável por\n\t\t\t\tter a licença de uso do referido arquivo de música."),
    ("3. Integrar", "3. Integrate", "3. Integrar"),
    ("En caso que el audio final se integre al contenido, reemplazará el audio existente del contenido -si había-,\n\t\t\t\tsino quedará guardado en el audio studio del contenido pero no será utilizado en la audio guía.",
     "If the final audio is integrated into the content, it will replace the existing content audio -if any-,\n\t\t\t\totherwise it will remain saved in the content's audio studio but will not be used in the audio guide.",
     "Caso o áudio final seja integrado ao conteúdo, substituirá o áudio existente do conteúdo -se houver-,\n\t\t\t\tcaso contrário ficará salvo no estúdio de áudio do conteúdo mas não será utilizado no audioguia."),

    # === candidate-info ===
    ("Candidatos a institución", "Institution Candidates", "Candidatos a instituição"),
    ("Registro generado en el proceso de signup de nueva institución.",
     "Record generated during the new institution sign-up process.",
     "Registro gerado no processo de cadastro de nova instituição."),

    # === guide-content-info ===
    (">Información<", ">Information<", ">Informação<"),
    ("Audio de la obra en exhibición. Se pueden generar audios en todos los idiomas de la sede.",
     "Audio of the artwork in the exhibition. Audio can be generated in all the site's languages.",
     "Áudio da obra na exposição. Os áudios podem ser gerados em todos os idiomas do local."),
    (">Estado<", ">Status<", ">Estado<"),
    ("Publicada, en edición o borrada. \n\t\t\t\tSolamente las que están en estado publicada son accesibles por los visitantes.\n\t\t\t\tLas borradas, están en la papelera de reciclaje, serán eliminadas automáticamente luego de varios meses.",
     "Published, in edition or deleted. \n\t\t\t\tOnly those in published state are accessible by visitors.\n\t\t\t\tDeleted items are in the recycle bin and will be automatically removed after several months.",
     "Publicado, em edição ou excluído. \n\t\t\t\tSomente os que estão em estado publicado são acessíveis pelos visitantes.\n\t\t\t\tOs excluídos estão na lixeira e serão eliminados automaticamente após vários meses."),

    # === institution-info ===
    (">Institución<", ">Institution<", ">Instituição<"),
    ("La institución es la organización 'madre' (como museo, edificio histórico, etc.) y tiene una o más sedes.  No\n\t\t\t\trequiere formularios en todos los idiomas, solamente en el idioma principal.",
     "The institution is the 'parent' organization (such as museum, historic building, etc.) and has one or more sites.  It does not\n\t\t\t\trequire forms in all languages, only in the primary language.",
     "A instituição é a organização 'mãe' (como museu, edifício histórico, etc.) e tem um ou mais locais.  Não\n\t\t\t\trequer formulários em todos os idiomas, somente no idioma principal."),
    (">Usuarios<", ">Users<", ">Usuários<"),
    ("Se listan los usuarios que tienen permisos sobre la institución (no se incluyen usuarios que sólo tienen permisos en alguna de las sedes).  \n\t\t\tAdicionalmente, también el usuario administrador de sistema ('root') y los usuarios con rol de 'Admin general' pueden acceder a la institución y sus sedes.",
     "Users with permissions on the institution are listed (users who only have permissions on one of the sites are not included).  \n\t\t\tAdditionally, the system administrator user ('root') and users with the 'General Admin' role can also access the institution and its sites.",
     "São listados os usuários que têm permissões sobre a instituição (não são incluídos usuários que apenas têm permissões em algum dos locais).  \n\t\t\tAdicionalmente, o usuário administrador do sistema ('root') e os usuários com função de 'Admin geral' também podem acessar a instituição e seus locais."),
    ("Publicada, en edición o borrada. Los contenidos borrados están en la papelera de reciclaje, serán eliminados automáticamente luego de varios meses.",
     "Published, in edition or deleted. Deleted content is in the recycle bin and will be automatically removed after several months.",
     "Publicado, em edição ou excluído. Os conteúdos excluídos estão na lixeira e serão eliminados automaticamente após vários meses."),

    # === institution-list ===
    ("La institución es la organización 'madre' (como museo, edificio histórico, etc.) y tiene una o más",
     "The institution is the 'parent' organization (such as museum, historic building, etc.) and has one or more",
     "A instituição é a organização 'mãe' (como museu, edifício histórico, etc.) e tem um ou mais"),
    (">sedes<", ">sites<", ">locais<"),
    ("Normalmente tiene una sola sede (-ej. Museo Nacional de Bellas Artes-, pero puede tener más de una -ej. Museo Latinoaméricano de Buenos Aires MALBA, sede Palermo y sede Escobar-.",
     "Usually it has a single site (-e.g. National Museum of Fine Arts-, but it can have more than one -e.g. Latin American Museum of Buenos Aires MALBA, Palermo site and Escobar site-.",
     "Normalmente tem um único local (-ex. Museu Nacional de Belas Artes-, mas pode ter mais de um -ex. Museu Latino-americano de Buenos Aires MALBA, local Palermo e local Escobar-."),
    ("A su vez, las", "In turn, the", "Por sua vez, os"),
    ("son las que gestionan sus obras, exhibiciones y audio guías.",
     "are the ones that manage their artworks, exhibitions and audio guides.",
     "são os que gerenciam suas obras, exposições e audioguias."),

    # === music ===
    (">Música<", ">Music<", ">Música<"),
    ("Contiene archivos de música disponibles para ser utilizados en las audio guías.",
     "Contains music files available to be used in audio guides.",
     "Contém arquivos de música disponíveis para serem utilizados nos audioguias."),
    ("Solamente pueden usarse\n\t\t\t\tarchivos cuya licencia de uso es libre ('royalty free') o que fueron específicamente licenciados para ser utilizados\n\t\t\t\tpor el sistema.",
     "Only files\n\t\t\t\twhose usage license is royalty free or that were specifically licensed for use\n\t\t\t\tby the system can be used.",
     "Somente podem ser usados\n\t\t\t\tarquivos cuja licença de uso é livre ('royalty free') ou que foram especificamente licenciados para serem utilizados\n\t\t\t\tpelo sistema."),
    ("La mayoría de los archivos provienen de", "Most files come from", "A maioria dos arquivos provém de"),
    ("Solamente los usuarios con rol\n\t\t\t\tgeneral", "Only users with the general role", "Somente os usuários com função\n\t\t\t\tgeral"),
    ("pueden administrar esta biblioteca.", "can manage this library.", "podem administrar esta biblioteca."),

    # === person-list ===
    ("Biblioteca de personas administrada por los usuarios con rol general",
     "Person library managed by users with the general role",
     "Biblioteca de pessoas administrada pelos usuários com função geral"),
    ("Cada", "Each", "Cada"),
    ("del sistema además de sus datos de usuario (contraseña, idioma, zona horaria etc.) \n\t\t \ttiene siempre una persona asociada donde figuran sus datos personales (email, teléfono, etc.).",
     "in the system, in addition to their user data (password, language, time zone, etc.) \n\t\t \talways has an associated person where their personal data is stored (email, phone, etc.).",
     "do sistema, além de seus dados de usuário (senha, idioma, fuso horário, etc.) \n\t\t \tsempre tem uma pessoa associada onde constam seus dados pessoais (email, telefone, etc.)."),
    ("Pueden existir personas sin usuario asociado, pero cada usuario tiene siempre una persona asociada.",
     "There can be persons without an associated user, but each user always has an associated person.",
     "Podem existir pessoas sem usuário associado, mas cada usuário sempre tem uma pessoa associada."),

    # === role-info ===
    ("\n\t\t\tRol\n\t\t\t", "\n\t\t\tRole\n\t\t\t", "\n\t\t\tFunção\n\t\t\t"),
    ("tiene un rol admin, y", "has an admin role, and", "tem uma função admin, e"),
    ("cada tiene dos roles, \n\t\t \tadmin y editor. Los roles no son editables. Se crean con la institución y sedes. \n\t\t \tSolamente se pueden agregar o quitar usuarios.",
     "each has two roles, \n\t\t \tadmin and editor. Roles are not editable. They are created with the institution and sites. \n\t\t \tOnly users can be added or removed.",
     "cada um tem duas funções, \n\t\t \tadmin e editor. As funções não são editáveis. São criadas com a instituição e os locais. \n\t\t \tSomente podem ser adicionados ou removidos usuários."),

    # === roles-list ===
    ("Los roles sirven para otorgar permisos a los", "Roles are used to grant permissions to", "As funções servem para conceder permissões aos"),
    ("Un usuario puede\n\t\t\t\ttener más de un rol asigando, y sus permisos son todos los que\n\t\t\t\tle otorgan los roles que tiene.",
     "A user can\n\t\t\t\thave more than one assigned role, and their permissions are all those\n\t\t\t\tgranted by their roles.",
     "Um usuário pode\n\t\t\t\tter mais de uma função atribuída, e suas permissões são todas as que\n\t\t\t\tlhe concedem as funções que possui."),
    ("Existen tres clases de roles,", "There are three types of roles,", "Existem três tipos de funções,"),
    ("Los roles de institución y de sede se crean automáticamente con ellas.",
     "Institution and site roles are created automatically with them.",
     "As funções de instituição e de local são criadas automaticamente com elas."),
    ("Roles generales", "General Roles", "Funções gerais"),
    ("Es el rol de administrador general, tiene todos los permisos disponibles, puede ver y hacer todo menos editar el usuario de sistema\n\t\t\t\t\t\t\t\t\t\t\t\t\t('root')",
     "This is the general administrator role, it has all available permissions, can see and do everything except edit the system user\n\t\t\t\t\t\t\t\t\t\t\t\t\t('root')",
     "Esta é a função de administrador geral, possui todas as permissões disponíveis, pode ver e fazer tudo exceto editar o usuário do sistema\n\t\t\t\t\t\t\t\t\t\t\t\t\t('root')"),
    ("Puede ver todos los contenidos del sistema pero no puede editar ni modificar nada",
     "Can view all system content but cannot edit or modify anything",
     "Pode ver todos os conteúdos do sistema mas não pode editar nem modificar nada"),
    ("Roles de Institución", "Institution Roles", "Funções de Instituição"),
    ("Puede administrar la institución sus sedes. Ejemplos:", "Can manage the institution and its sites. Examples:", "Pode administrar a instituição e seus locais. Exemplos:"),
    ("Admin Institución", "Institution Admin", "Admin Instituição"),
    ("Roles de Sede", "Site Roles", "Funções de Local"),
    ("Puede gestionar y publicar los contenidos y administrar los usuarios de la sede. Ejemplos:",
     "Can manage and publish content and administer site users. Examples:",
     "Pode gerenciar e publicar os conteúdos e administrar os usuários do local. Exemplos:"),
    ("Admin de Sede", "Site Admin", "Admin de Local"),
    ("Puede editar todos los contenidos de la sede pero no puede gestionar la seguridad -editar usuarios, cambiar permisos,  etc-, ejemplos:",
     "Can edit all site content but cannot manage security -edit users, change permissions, etc-, examples:",
     "Pode editar todos os conteúdos do local mas não pode gerenciar a segurança -editar usuários, alterar permissões, etc-, exemplos:"),
    ("Editor de Sede", "Site Editor", "Editor de Local"),

    # === site-list ===
    (">Sedes<", ">Sites<", ">Locais<"),
    ("Listado de todas las sedes accesibles por el usuario.",
     "List of all sites accessible by the user.",
     "Lista de todos os locais acessíveis pelo usuário."),

    # === site-artexhibition-list ===
    ("Exhibiciones de la sede", "Site Exhibitions", "Exposições do local"),
    ("Listado de todas las exhibiciones de la sede. Las", "List of all site exhibitions. The", "Lista de todas as exposições do local. As"),
    ("incluyen las que están en estado", "include those in", "incluem as que estão em estado"),
    ("El icono en el nombre corresponde a:", "The icon next to the name corresponds to:", "O ícone no nome corresponde a:"),
    ("Solamente las exhibiciones en estado", "Only exhibitions in", "Somente as exposições em estado"),
    ("se muestran en el portal público.", "are shown on the public portal.", "são mostradas no portal público."),

    # === site-artist-list ===
    ("Cada sede administra sus", "Each site manages its", "Cada local administra seus"),
    ("en forma autónoma.\n\t\t \t", "independently.\n\t\t \t", "de forma autônoma.\n\t\t \t"),
    ("Al borrar un artista el sistema lo envía a la papelera de reciclaje, donde permanecerá hasta ser eliminado del sistema\n\t\t \tautomáticamente.",
     "When deleting an artist, the system sends it to the recycle bin, where it will remain until it is automatically removed from the system.",
     "Ao excluir um artista, o sistema o envia para a lixeira, onde permanecerá até ser eliminado do sistema\n\t\t \tautomaticamente."),

    # === site-artwork-list ===
    ("Archivo de obras y objetos", "Artworks and objects archive", "Acervo de obras e objetos"),
    ("El archivo de obras y objetos de la sede es propio de la sede. Cada uno de ellos\n\tse puede agregar a las exhibiciones (como obra en exhibición). Al agregar una obra es posible que el artista no exista en el archivo de la sede, en cuyo caso se debe ir a\n\t la sección Artistas y agregarlo antes de completar la edición de la obra.",
     "The site's artwork and object archive belongs to the site. Each one\n\tcan be added to exhibitions (as an exhibition artwork). When adding an artwork, the artist may not exist in the site's archive, in which case you must go to\n\t the Artists section and add it before completing the artwork editing.",
     "O acervo de obras e objetos do local é próprio do local. Cada um deles\n\tpode ser adicionado às exposições (como obra na exposição). Ao adicionar uma obra, é possível que o artista não exista no acervo do local, nesse caso deve-se ir à\n\t seção Artistas e adicioná-lo antes de completar a edição da obra."),

    # === site-info ===
    ("La sección de información de la sede contiene los datos generales, incluyendo el",
     "The site information section contains general data, including the",
     "A seção de informação do local contém os dados gerais, incluindo o"),
    ("idioma principal", "primary language", "idioma principal"),
    ("idiomas secundarios", "secondary languages", "idiomas secundários"),
    ("Por cada idioma secundario existe un formulario donde se puede traducir automáticamente desde el idioma principal a ese idioma.",
     "For each secondary language there is a form where content can be automatically translated from the primary language to that language.",
     "Para cada idioma secundário existe um formulário onde o conteúdo pode ser traduzido automaticamente do idioma principal para esse idioma."),

    # === site-portal ===
    ("Portal público", "Public portal", "Portal público"),
    ("El portal público es donde los visitantes pueden acceder a las audio guías desde su teléfono. Puede ser accedido por cualquier persona sin necesidad de autenticación.",
     "The public portal is where visitors can access audio guides from their phone. It can be accessed by anyone without authentication.",
     "O portal público é onde os visitantes podem acessar os audioguias a partir do seu telefone. Pode ser acessado por qualquer pessoa sem necessidade de autenticação."),
    ("Para que esté disponible la sede debe estar en estado",
     "For it to be available, the site must be in",
     "Para que esteja disponível, o local deve estar em estado"),
    ("y el campo", "and the field", "e o campo"),
    ("Portal público habilitado", "Public portal enabled", "Portal público habilitado"),
    ("es la que la sede debe agregar en su página web para dirigir a los visitantes al portal público. \n\t\t\t\t También se puede imprimir en tarjetas o placas en el lugar físico de la exhibición.",
     "is what the site should add to its website to direct visitors to the public portal. \n\t\t\t\t It can also be printed on cards or plaques at the physical exhibition location.",
     "é a que o local deve adicionar em seu site web para direcionar os visitantes ao portal público. \n\t\t\t\t Também pode ser impressa em cartões ou placas no local físico da exposição."),

    # === site-roles-list ===
    ("Roles de la Sede", "Site Roles", "Funções do Local"),
    ("Cada sede tiene 2 roles de sede:", "Each site has 2 site roles:", "Cada local tem 2 funções de local:"),
    ("editor de sede", "site editor", "editor de local"),
    ("(puede editar y publicar contenidos)", "(can edit and publish content)", "(pode editar e publicar conteúdos)"),
    ("admin de sede", "site admin", "admin de local"),
    ("(tiene todos los permisos del editor más administración de la\n\t\t\t\tseguridad)",
     "(has all editor permissions plus security\n\t\t\t\tadministration)",
     "(tem todas as permissões do editor mais administração da\n\t\t\t\tsegurança)"),
    ("además existe un rol", "additionally there is a", "além disso existe uma função"),
    ("admin de la institución", "institution admin", "admin da instituição"),
    ("que permite administrar los\n\t\t\t\tusuarios de todas sus sedes.",
     "role that allows managing\n\t\t\t\tusers across all its sites.",
     "que permite administrar os\n\t\t\t\tusuários de todos os seus locais."),
    ("Al expandir cada fila se listan los usuarios que tienen el rol.",
     "When expanding each row, the users who have the role are listed.",
     "Ao expandir cada linha, são listados os usuários que possuem a função."),

    # === site-search ===
    ("Buscar por número de audio", "Search by audio number", "Pesquisar por número de áudio"),
    ("Si existe un único audio con el número de audio buscado (", "If there is a single audio with the searched audio number (", "Se existe um único áudio com o número de áudio buscado ("),
    ("audio intro de la exhibición", "exhibition intro audio", "áudio intro da exposição"),
    ("audio de obra", "artwork audio", "áudio de obra"),
    ("), \n\t\t\tel sistema directamente abre la página correspondiente.",
     "), \n\t\t\tthe system directly opens the corresponding page.",
     "), \n\t\t\to sistema abre diretamente a página correspondente."),
    ("Por qué puede haber más de un contenido con el mismo número de audio ?",
     "Why can there be more than one content with the same audio number?",
     "Por que pode haver mais de um conteúdo com o mesmo número de áudio?"),
    ("Los audios de obras y objetos en una audio guía se indexan con el número de audio de la obra u objeto, por lo que si está incluido en más de una audio guía \n\t\t\t(por ejemplo en la audio guía",
     "Artwork and object audio in an audio guide is indexed by the artwork or object audio number, so if it is included in more than one audio guide \n\t\t\t(for example in the",
     "Os áudios de obras e objetos em um audioguia são indexados pelo número de áudio da obra ou objeto, portanto se está incluído em mais de um audioguia \n\t\t\t(por exemplo no audioguia"),
    ("de la exhibición y en la audio guía", "of the exhibition and in the", "da exposição e no audioguia"),
    ("de la exhibición)\n\t\t\tes posible que exista más de un resultado para una búsqueda.",
     "of the exhibition)\n\t\t\tit is possible that there is more than one result for a search.",
     "da exposição)\n\t\t\té possível que exista mais de um resultado para uma pesquisa."),

    # === site-user-info ===
    ("Listado de los usuarios de la sede, es decir todos los usuarios que tienen permisos especificos sobre la sede (admin de sede, editor de sede).",
     "List of site users, i.e. all users who have specific permissions on the site (site admin, site editor).",
     "Lista dos usuários do local, ou seja, todos os usuários que têm permissões específicas sobre o local (admin de local, editor de local)."),
    ("No se incluyen otros que pueden tener acceso a la sede: usuarios con permiso de admin de la institución, admin general, administrador de sistema ('root').",
     "Others who may have access to the site are not included: users with institution admin permission, general admin, system administrator ('root').",
     "Não são incluídos outros que podem ter acesso ao local: usuários com permissão de admin da instituição, admin geral, administrador do sistema ('root')."),

    # === site-users ===
    ("Listado de los usuarios de la sede, es decir todos los usuarios que tienen permisos especificos sobre la sede (",
     "List of site users, i.e. all users who have specific permissions on the site (",
     "Lista dos usuários do local, ou seja, todos os usuários que têm permissões específicas sobre o local ("),
    ("de sede,", "site,", "de local,"),
    ("de sede).", "site).", "de local)."),
    ("No se incluyen usuarios con admin de la institución ni admin general del sistema.",
     "Users with institution admin or general system admin are not included.",
     "Não são incluídos usuários com admin da instituição nem admin geral do sistema."),
    ("El admin de Sede puede editar los usuarios de la sede, otorgarle y quitarle permisos relacionados con la sede.\n\t\t\tNo puede cambiar otros permisos que el usuario pueda tener, como por ejemplo el de admin de institución o admin general del sistema.",
     "The Site admin can edit site users, grant and revoke site-related permissions.\n\t\t\tThey cannot change other permissions the user may have, such as institution admin or general system admin.",
     "O admin de Local pode editar os usuários do local, conceder e revogar permissões relacionadas ao local.\n\t\t\tNão pode alterar outras permissões que o usuário possa ter, como admin de instituição ou admin geral do sistema."),

    # === user-info ===
    ("\n\t\t\tUsuario\n\t\t\t", "\n\t\t\tUser\n\t\t\t", "\n\t\t\tUsuário\n\t\t\t"),
    ("Cada usuario tiene una", "Each user has an associated", "Cada usuário tem uma"),
    ("persona", "person", "pessoa"),
    ("asociada donde figuran sus datos personales (teléfono, email, etc.), y",
     "where their personal data is stored (phone, email, etc.), and",
     "associada onde constam seus dados pessoais (telefone, email, etc.), e"),
    ("roles", "roles", "funções"),
    ("de seguridad, que determinan sus permisos.", "that determine their permissions.", "de segurança, que determinam suas permissões."),
    ("La validación de email y teléfono es un proceso automático que realiza el sistema con el usuario, solamente administradores pueden editar estos campos.",
     "Email and phone validation is an automatic process performed by the system with the user, only administrators can edit these fields.",
     "A validação de email e telefone é um processo automático realizado pelo sistema com o usuário, somente administradores podem editar estes campos."),

    # === user-list ===
    ("Esta sección muestra la lista de usuarios del sistema,  es accesible solamente por los administradores del sistema.",
     "This section shows the system user list and is only accessible by system administrators.",
     "Esta seção mostra a lista de usuários do sistema e é acessível somente pelos administradores do sistema."),
    ("tiene sus propios administradores, y pueden ver solamente los usuarios de la institución.",
     "has its own administrators, and they can only see the institution's users.",
     "tem seus próprios administradores, e podem ver somente os usuários da instituição."),

    # === voices ===
    (">Voces<", ">Voices<", ">Vozes<"),
    ("Archivo de voces disponibles para ser usadas desde el Audio Studio al generar audio guías de Exhibiciones y de Obras.",
     "Archive of voices available for use from the Audio Studio when generating Exhibition and Artwork audio guides.",
     "Arquivo de vozes disponíveis para serem usadas a partir do Estúdio de Áudio ao gerar audioguias de Exposições e de Obras."),
    ("Solamente los usuarios con rol", "Only users with the", "Somente os usuários com função"),
    ("pueden administrar esta biblioteca.", "role can manage this library.", "podem administrar esta biblioteca."),

    # === Common short terms ===
    (">Sede<", ">Site<", ">Local<"),
    ("usuario", "user", "usuário"),
    ("usuarios", "users", "usuários"),
    (">Publicada<", ">Published<", ">Publicado<"),
    (">General<", ">General<", ">Geral<"),
    (">Accesible<", ">Accessible<", ">Acessível<"),
    (">Activas<", ">Active<", ">Ativas<"),
    (">Edición<", ">Edition<", ">Edição<"),
]


def translate(content, lang_index):
    """Apply translations. lang_index: 1=English, 2=Portuguese"""
    result = content
    for spa, en, pt in translations:
        target = en if lang_index == 1 else pt
        result = result.replace(spa, target)
    return result


def main():
    os.makedirs(EN, exist_ok=True)
    os.makedirs(PT, exist_ok=True)

    created_en = 0
    created_pt = 0

    for filename in sorted(os.listdir(SPA)):
        if not filename.endswith('.html'):
            continue

        filepath = os.path.join(SPA, filename)
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()

        # English
        en_content = translate(content, 1)
        en_path = os.path.join(EN, filename)
        with open(en_path, 'w', encoding='utf-8') as f:
            f.write(en_content)
        created_en += 1
        print(f"EN: {filename}")

        # Portuguese
        pt_content = translate(content, 2)
        pt_path = os.path.join(PT, filename)
        with open(pt_path, 'w', encoding='utf-8') as f:
            f.write(pt_content)
        created_pt += 1
        print(f"PT: {filename}")

    print(f"\nDone: {created_en} English, {created_pt} Portuguese files created")


if __name__ == '__main__':
    main()
