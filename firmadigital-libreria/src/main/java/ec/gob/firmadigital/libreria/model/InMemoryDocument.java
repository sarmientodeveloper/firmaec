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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Objects;

/**
 * Implementacion de Document en memoria.
 */
public class InMemoryDocument extends CommonDocument {

    /* Contenido del documento */
    private byte[] bytes;

    public InMemoryDocument() {
    }

    public InMemoryDocument(final byte[] bytes) {
        this(bytes, null);
    }

    public InMemoryDocument(final byte[] bytes, final String name) {
        Objects.requireNonNull(bytes, "Bytes cannot be null");
        this.bytes = bytes;
        this.name = name;
    }

    public InMemoryDocument(final InputStream inputStream) {
        this(toByteArray(inputStream), null);
    }

    public InMemoryDocument(final InputStream inputStream, final String name) {
        this(toByteArray(inputStream), name);
    }

    public InputStream openStream() {
        Objects.requireNonNull(bytes, "Bytes are null");
        return new ByteArrayInputStream(bytes);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getBase64Encoded() {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] toByteArray(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "The InputStream is null");
        try (InputStream is = inputStream; ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int nRead;
            byte[] data = new byte[2048];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                baos.write(data, 0, nRead);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RubricaRuntimeException("Unable to fully read the InputStream", e);
        }
    }
}
