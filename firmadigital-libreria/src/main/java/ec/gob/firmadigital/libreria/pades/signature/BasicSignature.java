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
package ec.gob.firmadigital.libreria.pades.signature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.Collections;

import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.CrlClientOnline;
import com.itextpdf.signatures.ICrlClient;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.IOcspClient;
import com.itextpdf.signatures.ITSAClient;
import com.itextpdf.signatures.OCSPVerifier;
import com.itextpdf.signatures.OcspClientBouncyCastle;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.TSAClientBouncyCastle;

import ec.gob.firmadigital.libreria.model.Document;
import ec.gob.firmadigital.libreria.model.InMemoryDocument;
import ec.gob.firmadigital.libreria.model.RubricaRuntimeException;
import ec.gob.firmadigital.libreria.pades.SignatureParameters;
import ec.gob.firmadigital.libreria.sign.RubricaSigner;
import ec.gob.firmadigital.libreria.sign.pdf.itext.ITextSignerAdapter;

public class BasicSignature {

    private RubricaSigner rubricaSigner;

    public BasicSignature(RubricaSigner rubricaSigner) {
        this.rubricaSigner = rubricaSigner;
    }

    public Document sign(Document document, SignatureParameters parameters) {
        try (PdfReader reader = new PdfReader(document.openStream()); ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            StampingProperties properties = new StampingProperties();
            properties.useAppendMode();

            PdfSigner signer = new PdfSigner(reader, os, properties);
            IExternalDigest digest = new BouncyCastleDigest();
            IExternalSignature signature = new ITextSignerAdapter(rubricaSigner);
            Certificate[] chain = parameters.getChain();

            ICrlClient crlClient = new CrlClientOnline();
            OCSPVerifier ocspVerifier = new OCSPVerifier(null, null);
            IOcspClient ocspClient = new OcspClientBouncyCastle(ocspVerifier);

            ITSAClient tsc = new TSAClientBouncyCastle(parameters.getTsaUrl(), parameters.getTsaUsername(),
                    parameters.getTsaPassword());

            Collection<ICrlClient> crlClients = Collections.singleton(crlClient);

            signer.signDetached(digest, signature, chain, crlClients, ocspClient, tsc, 0,
                    PdfSigner.CryptoStandard.CADES);

            return new InMemoryDocument(os.toByteArray());
        } catch (IOException e) {
            throw new RubricaRuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RubricaRuntimeException(e);
        }
    }

    private String getTsalUrl() {
        return null;
    }
}
