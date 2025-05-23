/*
 * Copyright (C) 2020
 * Authors: Ricardo Arguello, Misael Fernández
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.*
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ec.gob.firmadigital.libreria.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Representa un documento.
 */
public interface Document {

    /**
     * Obtener un InputStream del documento
     * @return 
     */
    InputStream openStream();

    /**
     * Escribir un documento a un OutputStream
     * @param stream
     * @throws java.io.IOException
     */
    void writeTo(OutputStream stream) throws IOException;

    /**
     * Obtener un nombre
     * @return 
     */
    String getName();

    /**
     * Establecer un nombre
     * @param name
     */
    void setName(String name);

    /**
     * Grabar el documento
     * @param filePath
     * @throws java.io.IOException
     */
    void save(final String filePath) throws IOException;
}
