#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Fix remaining Spanish text in Portuguese help files."""

import os

BASE = "/Users/alejandrotolomei/eclipse-workspace/dellemuse-server/serverApp/serverapp/help/pt"

# (file, old_text, new_text)
fixes = [
    # artwork-info.html
    ("artwork-info.html",
     "Cada sede tiene su propio archivo de obras e objetos, con formularios de información en los idiomas de la sede.",
     "Cada local tem seu próprio acervo de obras e objetos, com formulários de informação nos idiomas do local."),

    # site-artwork-list.html
    ("site-artwork-list.html",
     """El archivo de obras e objetos de la sede es propio de la sede. Cada uno de ellos
	se puede agregar a las exhibiciones (como obra en exhibición). Al agregar una obra es posible que el artista no exista en el archivo de la sede, en cuyo caso se debe ir a
	 la sección Artistas y agregarlo antes de completar la edición de la obra.""",
     """O acervo de obras e objetos do local é próprio do local. Cada um deles
	pode ser adicionado às exposições (como obra na exposição). Ao adicionar uma obra, é possível que o artista não exista no acervo do local, nesse caso deve-se ir à
	 seção Artistas e adicioná-lo antes de completar a edição da obra."""),

    # site-info.html - "y los" connector
    ("site-info.html",
     """idioma principal</span> y los <span class="highlight">idiomas secundários</span>.""",
     """idioma principal</span> e os <span class="highlight">idiomas secundários</span>."""),

    # site-roles-list.html - "y" connector
    ("site-roles-list.html",
     """(pode editar e publicar conteúdos)\n\t\t\t\ty <span class="highlight">admin de local</span>""",
     """(pode editar e publicar conteúdos)\n\t\t\t\te <span class="highlight">admin de local</span>"""),

    # user-info.html
    ("user-info.html",
     """Cada usuário tem uma <a href="person/list">pessoa</a> asociada donde figuran sus datos pessoales (teléfono, email, etc.), y  \n\t\t \t<a href="/security/funções">funções</a>""",
     """Cada usuário tem uma <a href="person/list">pessoa</a> associada onde constam seus dados pessoais (telefone, email, etc.), e  \n\t\t \t<a href="/security/roles">funções</a>"""),

    # user-list.html
    ("user-list.html",
     """Cada usuário tem uma <a href="person/list">pessoa</a> asociada donde figuran sus datos pessoales (teléfono, email, etc.), y  \n\t\t \t<a href="/security/funções">funções</a>""",
     """Cada usuário tem uma <a href="person/list">pessoa</a> associada onde constam seus dados pessoais (telefone, email, etc.), e  \n\t\t \t<a href="/security/roles">funções</a>"""),

    # site-user-info.html
    ("site-user-info.html",
     "No se incluyen otros que pueden tener acceso a la sede: usuários con permiso de admin da instituição, admin general, administrador de sistema ('root').",
     "Não são incluídos outros que podem ter acesso ao local: usuários com permissão de admin da instituição, admin geral, administrador do sistema ('root')."),

    # site-users.html
    ("site-users.html",
     "No se incluyen usuários con admin da instituição ni admin general del sistema.",
     "Não são incluídos usuários com admin da instituição nem admin geral do sistema."),
]


def main():
    fixed = 0
    for filename, old_text, new_text in fixes:
        filepath = os.path.join(BASE, filename)
        if os.path.exists(filepath):
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
            if old_text in content:
                content = content.replace(old_text, new_text)
                with open(filepath, 'w', encoding='utf-8') as f:
                    f.write(content)
                fixed += 1
                print(f"FIXED: {filename}")
            else:
                print(f"NOT FOUND: {filename}")
                # Show context around potential match for debugging
                for line_num, line in enumerate(content.split('\n'), 1):
                    first_word = old_text.split()[0] if old_text.split() else ''
                    if first_word and first_word in line:
                        print(f"  Line {line_num}: {line.strip()[:120]}")
        else:
            print(f"FILE MISSING: {filename}")

    print(f"\nDone: {fixed} fixes applied")


if __name__ == '__main__':
    main()
