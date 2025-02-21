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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Implementación de Document usando un archivo.
 */
public class FileDocument extends CommonDocument {

    /**
     * The file
     */
    private final File file;

    /**
     * Create a FileDocument
     *
     * @param path the path to the file
     */
    public FileDocument(final String path) {
        this(new File(path));
    }

    /**
     * Create a FileDocument
     *
     * @param file {@code File}
     */
    public FileDocument(final File file) {
        Objects.requireNonNull(file, "File cannot be null");

        if (!file.exists()) {
            throw new RubricaRuntimeException("File Not Found: " + file.getAbsolutePath());
        }

        this.file = file;
        this.name = file.getName();
    }

    @Override
    public InputStream openStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RubricaRuntimeException("Unable to create a FileInputStream", e);
        }
    }

    /**
     * Checks if the file exists
     *
     * @return TRUE if the file exists in the file system, FALSE otherwise
     */
    public boolean exists() {
        return file.exists();
    }

    /**
     * Gets the {@code File}
     *
     * @return {@link File}
     */
    public File getFile() {
        return file;
    }

    /**
     * Returns the {@code String} representing the absolute path to the
     * encapsulated document.
     *
     * @return {@code String} representing the absolute path to the encapsulated
     * document.
     */
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }
}
