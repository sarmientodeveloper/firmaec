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
package ec.gob.firmadigital.libreria.sign.pdf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Properties;

import ec.gob.firmadigital.libreria.sign.SignConstants;
import ec.gob.firmadigital.libreria.sign.SignInfo;
import ec.gob.firmadigital.libreria.sign.Signer;
import ec.gob.firmadigital.libreria.sign.TestHelper;
import ec.gob.firmadigital.libreria.utils.PropertiesUtils;

public class PdfSignerTest {

    // @Test
    public void testSignPdf() throws Exception {
        File tempFile = File.createTempFile("pdfSign", "." + "test1.pdf");
        System.out.println("Temporal para comprobacion manual: " + tempFile.getAbsolutePath());

        File tempFile2 = File.createTempFile("pdfSign", "." + "test2.pdf");
        System.out.println("Temporal2 para comprobacion manual: " + tempFile2.getAbsolutePath());

        KeyPair kp = TestHelper.createKeyPair();
        Certificate[] chain = TestHelper.createCertificate(kp);
        byte[] pdf = TestHelper.crearPdf();

        Properties params = new Properties();
        params.setProperty("format", SignConstants.SIGN_FORMAT_OOXML);
        params.setProperty("signatureReason", "Comentario : Razon de firma");

        byte[] result;

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            Signer signer = new PDFSignerItext();
            result = signer.sign(pdf, SignConstants.SIGN_ALGORITHM_SHA1WITHRSA, kp.getPrivate(), chain, params, PropertiesUtils.versionBase64());

            assertNotNull(result);
            fos.write(result);
            fos.flush();

            List<SignInfo> firmantes = signer.getSigners(result);
            X509Certificate[] certs = firmantes.get(0).getCerts();
            assertTrue(((X509Certificate) chain[0]).getSerialNumber().equals(certs[0].getSerialNumber()));
        }

        try (FileOutputStream fos = new FileOutputStream(tempFile2)) {
            Signer signer = new PDFSignerItext();
            byte[] result2 = signer.sign(result, SignConstants.SIGN_ALGORITHM_SHA1WITHRSA, kp.getPrivate(), chain,
                    params, PropertiesUtils.versionBase64());

            assertNotNull(result2);
            fos.write(result2);
            fos.flush();

            List<SignInfo> firmantes = signer.getSigners(result2);
            X509Certificate[] certs = firmantes.get(0).getCerts();
            assertTrue(((X509Certificate) chain[0]).getSerialNumber().equals(certs[0].getSerialNumber()));
        }
    }
}
