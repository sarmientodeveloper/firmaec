/*
 * Copyright (C) 2021 
 * Authors: Ricardo Arguello
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
package ec.gob.firmadigital.libreria.sign.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.util.Properties;

import ec.gob.firmadigital.libreria.sign.RubricaSigner;

public interface PdfSigner {

    byte[] sign(InputStream data, RubricaSigner signer, Certificate[] certChain, Properties params)
            throws IOException;
}
