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

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

import org.junit.Test;

import ec.gob.firmadigital.libreria.sign.SignInfo;
import ec.gob.firmadigital.libreria.sign.TestHelper;
import ec.gob.firmadigital.libreria.utils.PropertiesUtils;

public class XAdESTest {

    @Test
    public void firmarXml() throws Exception {
        File tempFile = File.createTempFile("test", ".xml");
        System.out.println("Temporal para comprobacion manual: " + tempFile.getAbsolutePath());

        KeyPair kp = TestHelper.createKeyPair();
        Certificate[] chain = TestHelper.createCertificate(kp);
        byte[] xml = "<documento><parrafo>Hola mundo</parrafo></documento>".getBytes();

        try (FileOutputStream fos = new FileOutputStream(tempFile);) {
            XAdESSigner signer = new XAdESSigner();
            byte[] result = signer.sign(xml, "SHA1withRSA", kp.getPrivate(), chain, null, PropertiesUtils.versionBase64());

            assertNotNull(result);
            fos.write(result);
            fos.flush();

            List<SignInfo> firmantes = signer.getSigners(result);
            X509Certificate[] certs = firmantes.get(0).getCerts();
            assertTrue(((X509Certificate) chain[0]).getSerialNumber().equals(certs[0].getSerialNumber()));
        }
    }
}
