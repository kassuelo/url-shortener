# url-shortener

A simple URL shortener built with Clojure, using Ring, Compojure, and Hiccup.
It allows you to submit a long URL and receive a short link such as http://localhost:3000/AbC123, which automatically redirects to the original URL.
All data is stored in memory (using an atom), making this project ideal for learning and experimentation.

## Usage

To use the application, start the server with:

lein run

Then open your browser and go to:

http://localhost:3000

Submit any long URL through the form, and the application will generate a short link in the format:

http://localhost:3000/<id>

Accessing this link will redirect you to the original URL stored in memory.


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
