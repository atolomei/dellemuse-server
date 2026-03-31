#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Fix remaining Spanish text in English and Portuguese help files."""

import os

BASE = "/Users/alejandrotolomei/eclipse-workspace/dellemuse-server/serverApp/serverapp/help"

# (file, old_text, en_text, pt_text)
fixes = [
    # artwork-info.html
    ("artwork-info.html",
     "Each sede tiene su propio archivo de artworks and objects, con formularios de información en los idiomas de la sede.",
     "Each site has its own artwork and object archive, with information forms in the site's languages.",
     "Cada local tem seu próprio acervo de obras e objetos, com formulários de informação nos idiomas do local."),

    # site-artwork-list.html
    ("site-artwork-list.html",
     "El archivo de artworks and objects de la sede es propio de la sede. Each uno de ellos\n\tse puede agregar a las exhibiciones (como obra en exhibición). Al agregar una obra es posible que el artista no exista en el archivo de la sede, en cuyo caso se debe ir a\n\t la sección Artistas y agregarlo antes de completar la edición de la obra.",
     "The site's artwork and object archive belongs to the site. Each one\n\tcan be added to exhibitions (as an exhibition artwork). When adding an artwork, the artist may not exist in the site's archive, in which case you must go to\n\t the Artists section and add it before completing the artwork editing.",
     "O acervo de obras e objetos do local é próprio do local. Cada um deles\n\tpode ser adicionado às exposições (como obra na exposição). Ao adicionar uma obra, é possível que o artista não exista no acervo do local, nesse caso deve-se ir à\n\t seção Artistas e adicioná-lo antes de completar a edição da obra."),

    # site-info.html
    ("site-info.html",
     """The site information section contains general data, including the <span class="highlight">primary language</span> y los <span class="highlight">secondary languages</span>. \n\t\t\t\tPor cada idioma secundario existe un formulario donde se puede traducir automáticamente desde el primary language a ese idioma.""",
     """The site information section contains general data, including the <span class="highlight">primary language</span> and the <span class="highlight">secondary languages</span>. \n\t\t\t\tFor each secondary language there is a form where content can be automatically translated from the primary language to that language.""",
     """A seção de informação do local contém os dados gerais, incluindo o <span class="highlight">idioma principal</span> e os <span class="highlight">idiomas secundários</span>. \n\t\t\t\tPara cada idioma secundário existe um formulário onde o conteúdo pode ser traduzido automaticamente do idioma principal para esse idioma."""),

    # site-user-info.html
    ("site-user-info.html",
     "No se incluyen otros que pueden tener acceso a la sede: users con permiso de institution admin, admin general, administrador de sistema ('root').",
     "Others who may have access to the site are not included: users with institution admin permission, general admin, system administrator ('root').",
     "Não são incluídos outros que podem ter acesso ao local: usuários com permissão de admin da instituição, admin geral, administrador do sistema ('root')."),

    # site-artist-list.html
    ("site-artist-list.html",
     """Each sede administra sus <span class="highlight">artistas</span>,  <span class="highlight">artworks and objects</span> independently.""",
     """Each site manages its <span class="highlight">artists</span>,  <span class="highlight">artworks and objects</span> independently.""",
     """Cada local administra seus <span class="highlight">artistas</span>,  <span class="highlight">obras e objetos</span> de forma autônoma."""),

    # site-roles-list.html
    ("site-roles-list.html",
     "Each sede tiene 2 roles de sede:",
     "Each site has 2 site roles:",
     "Cada local tem 2 funções de local:"),

    # site-roles-list.html - "y" connector
    ("site-roles-list.html",
     """(can edit and publish content)\n\t\t\t\ty <span class="highlight">site admin</span>""",
     """(can edit and publish content)\n\t\t\t\tand <span class="highlight">site admin</span>""",
     """(pode editar e publicar conteúdos)\n\t\t\t\te <span class="highlight">admin de local</span>"""),

    # user-info.html
    ("user-info.html",
     """Each user tiene una <a href="person/list">person</a> asociada donde figuran sus datos personles (teléfono, email, etc.), y  \n\t\t \t<a href="/security/roles">roles</a> that determine their permissions.""",
     """Each user has an associated <a href="person/list">person</a> where their personal data is stored (phone, email, etc.), and  \n\t\t \t<a href="/security/roles">roles</a> that determine their permissions.""",
     """Cada usuário tem uma <a href="person/list">pessoa</a> associada onde constam seus dados pessoais (telefone, email, etc.), e  \n\t\t \t<a href="/security/roles">funções</a> de segurança, que determinam suas permissões."""),

    # user-list.html
    ("user-list.html",
     """Each user tiene una <a href="person/list">person</a> asociada donde figuran sus datos personles (teléfono, email, etc.), y  \n\t\t \t<a href="/security/roles">roles</a> that determine their permissions.""",
     """Each user has an associated <a href="person/list">person</a> where their personal data is stored (phone, email, etc.), and  \n\t\t \t<a href="/security/roles">roles</a> that determine their permissions.""",
     """Cada usuário tem uma <a href="person/list">pessoa</a> associada onde constam seus dados pessoais (telefone, email, etc.), e  \n\t\t \t<a href="/security/roles">funções</a> de segurança, que determinam suas permissões."""),

    # site-users.html
    ("site-users.html",
     "No se incluyen users con institution admin ni admin general del sistema.",
     "Users with institution admin or general system admin are not included.",
     "Não são incluídos usuários com admin da instituição nem admin geral do sistema."),
]


def main():
    fixed_en = 0
    fixed_pt = 0

    for filename, old_text, en_text, pt_text in fixes:
        # Fix English
        en_path = os.path.join(BASE, "en", filename)
        if os.path.exists(en_path):
            with open(en_path, 'r', encoding='utf-8') as f:
                content = f.read()
            if old_text in content:
                content = content.replace(old_text, en_text)
                with open(en_path, 'w', encoding='utf-8') as f:
                    f.write(content)
                fixed_en += 1
                print(f"FIXED EN: {filename}")
            else:
                print(f"SKIP EN (not found): {filename}")
        
        # Fix Portuguese
        pt_path = os.path.join(BASE, "pt", filename)
        if os.path.exists(pt_path):
            with open(pt_path, 'r', encoding='utf-8') as f:
                content = f.read()
            # For PT files, the old_text might be the partially-translated version or the original Spanish
            # We need to check both the "old_text" (which is the EN partial) and figure out the PT partial
            # Actually, PT files were translated from Spanish with the same translation table,
            # so they may have similar partial issues. Let's check what's actually in the file.
            if old_text in content:
                content = content.replace(old_text, pt_text)
                with open(pt_path, 'w', encoding='utf-8') as f:
                    f.write(content)
                fixed_pt += 1
                print(f"FIXED PT: {filename}")
            else:
                print(f"SKIP PT (not found): {filename}")

    print(f"\nDone: {fixed_en} EN fixes, {fixed_pt} PT fixes")


if __name__ == '__main__':
    main()
