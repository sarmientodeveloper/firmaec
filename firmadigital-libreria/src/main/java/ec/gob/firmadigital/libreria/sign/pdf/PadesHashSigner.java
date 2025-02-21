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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.util.Properties;
import java.util.logging.Logger;

import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSigner;

import ec.gob.firmadigital.libreria.sign.RubricaSigner;
import ec.gob.firmadigital.libreria.sign.pdf.itext.ITextSignerAdapter;
import ec.gob.firmadigital.libreria.utils.PropertiesTsa;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * PaDES Basic Signer
 */
public class PadesHashSigner extends BasePdfHashSigner {

    private String tsaUrl;
    private String tsaUsername;
    private String tsaPassword;
    private IExternalSignature externalSignature;

    private static final Logger logger = Logger.getLogger(PadesHashSigner.class.getName());
    
    public PadesHashSigner(RubricaSigner signer) {
        PropertiesTsa propertiesTsa = new PropertiesTsa();
        this.tsaUrl = propertiesTsa.getTsaUrl();
        this.tsaUsername = propertiesTsa.getTsaUsername();
        this.tsaPassword = propertiesTsa.getTsaPassword();
        this.externalSignature = new ITextSignerAdapter(signer);
        Security.addProvider(new BouncyCastleProvider());//eliminar
    }

    @Override
    protected byte[] signInternal(ByteArrayOutputStream os, PdfSigner pdfSigner, RubricaSigner signer, Certificate[] certChain, Properties params) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
