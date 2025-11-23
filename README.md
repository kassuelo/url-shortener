# url-shortener

Um encurtador de URLs simples feito em Clojure, usando Ring, Compojure e Hiccup. Permite enviar uma URL longa e receber um link curto para redirecionamento. Armazena tudo em memória e é ideal para estudos.

## Usage

Para usar, execute o servidor com lein run e acesse http://localhost:3000. Envie uma URL pelo formulário e receba um endereço encurtado no formato /u/<codigo>. Acessar esse código redireciona para a URL original.


## License

Copyright © 2025 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
