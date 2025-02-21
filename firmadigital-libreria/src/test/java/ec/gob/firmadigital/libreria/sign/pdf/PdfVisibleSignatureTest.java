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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

public class PdfVisibleSignatureTest {

    // @Test
    public void testSignPdf() throws Exception {
        File tempFile = File.createTempFile("pdfSign", "." + "test1.pdf");
        System.out.println("Temporal para comprobacion manual: " + tempFile.getAbsolutePath());

        KeyPair kp = TestHelper.createKeyPair();
        Certificate[] chain = TestHelper.createCertificate(kp);

        Path path = Paths.get("/home/rarguello/Documents/diffie.pdf");
        byte[] pdf = Files.readAllBytes(path);

        Properties params = new Properties();
        params.setProperty(PDFSignerItext.SIGNING_REASON, "Razon de firma");
        params.setProperty(PDFSignerItext.SIGNING_LOCATION, "Quito, Ecuador");
        params.setProperty(PDFSignerItext.SIGNATURE_PAGE, "-2");

        params.setProperty(RectanguloUtil.POSITION_ON_PAGE_LOWER_LEFT_X, "0");
        params.setProperty(RectanguloUtil.POSITION_ON_PAGE_LOWER_LEFT_Y, "0");
        params.setProperty(RectanguloUtil.POSITION_ON_PAGE_UPPER_RIGHT_X, "200");
        params.setProperty(RectanguloUtil.POSITION_ON_PAGE_UPPER_RIGHT_Y, "100");

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
    }
}
