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
package ec.gob.firmadigital.libreria.sign.xades;

/**
 * Hoja de estilo local (rutal local no dereferenciable) a un XML
 */
public final class IsInnerlException extends StyleException {

    /**
     * Construye la excepci&oacute;n que indica que una referencia apunta al
     * interior del mismo XML.
     *
     * @param e Excepci&oacute;n anterior en la cadena
     */
    public IsInnerlException(final Throwable e) {
        super(e);
    }
}
