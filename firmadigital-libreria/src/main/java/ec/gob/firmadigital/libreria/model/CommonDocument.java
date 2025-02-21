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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * Implementación base de Document.
 */
public abstract class CommonDocument implements Document {

    protected String name;

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        byte[] buffer = new byte[1024];
        int count = -1;
        try (InputStream inStream = openStream()) {
            while ((count = inStream.read(buffer)) > 0) {
                stream.write(buffer, 0, count);
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void save(String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            writeTo(fos);
        }
    }

    @Override
    public String toString() {
        final StringWriter stringWriter = new StringWriter();
        stringWriter.append("Name: ").append(getName());
        return stringWriter.toString();
    }
}
