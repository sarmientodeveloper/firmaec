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
package ec.gob.firmadigital.libreria.sign.pdf.itext;

import java.security.GeneralSecurityException;

import com.itextpdf.signatures.IExternalSignature;

import ec.gob.firmadigital.libreria.sign.RubricaSigner;

public class ITextSignerAdapter implements IExternalSignature {

    private RubricaSigner signer;

    public ITextSignerAdapter(RubricaSigner signer) {
        this.signer = signer;
    }

    @Override
    public String getHashAlgorithm() {
        return signer.getDigestAlgorithm().getJavaName();
    }

    @Override
    public String getEncryptionAlgorithm() {
        return signer.getEncryptionAlgorithm().getName();
    }

    @Override
    public byte[] sign(byte[] message) throws GeneralSecurityException {
        return signer.sign(message);
    }
}
