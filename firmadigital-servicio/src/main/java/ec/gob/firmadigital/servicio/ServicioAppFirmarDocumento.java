/*
 * Firma Digital: Servicio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ec.gob.firmadigital.servicio;

import com.itextpdf.kernel.crypto.BadPasswordException;
import com.itextpdf.kernel.pdf.PdfReader;
import ec.gob.firmadigital.libreria.certificate.CertEcUtils;
import ec.gob.firmadigital.libreria.certificate.to.DatosUsuario;
import ec.gob.firmadigital.servicio.util.Pkcs12;
import ec.gob.firmadigital.servicio.util.FirmaDigital;
import ec.gob.firmadigital.servicio.util.Propiedades;
import ec.gob.firmadigital.libreria.certificate.to.Documento;
import ec.gob.firmadigital.libreria.exceptions.CertificadoInvalidoException;
import ec.gob.firmadigital.libreria.exceptions.ConexionException;
import ec.gob.firmadigital.libreria.exceptions.DocumentoException;
import ec.gob.firmadigital.libreria.exceptions.EntidadCertificadoraNoValidaException;
import ec.gob.firmadigital.libreria.exceptions.HoraServidorException;
import ec.gob.firmadigital.libreria.exceptions.RubricaException;
import ec.gob.firmadigital.libreria.exceptions.SignatureVerificationException;
import ec.gob.firmadigital.libreria.sign.SignInfo;
import ec.gob.firmadigital.libreria.sign.Signer;
import ec.gob.firmadigital.libreria.sign.pdf.PDFSignerItext;
import ec.gob.firmadigital.libreria.utils.Json;
import ec.gob.firmadigital.libreria.utils.TiempoUtils;
import static ec.gob.firmadigital.libreria.utils.Utils.pdfToDocumento;
import jakarta.ejb.EJB;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Properties;
import jakarta.ejb.Stateless;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.DatatypeConverter;
import jakarta.xml.bind.SchemaOutputResolver;

import java.io.StringReader;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian Espinosa <christian.espinosa@mintel.gob.ec>, Misael
 * Fernández
 */
@Stateless
public class ServicioAppFirmarDocumento {

    @EJB
    private ServicioLog servicioLog;

    private static final Logger logger = Logger.getLogger(ec.gob.firmadigital.servicio.ServicioAppFirmarDocumento.class.getName());

    public String firmarDocumento(@NotNull String pkcs12, @NotNull String password,
            @NotNull String documentoBase64, String versionFirmaEC, String formatoDocumento,
            String llx, String lly, String pagina, String tipoEstampado, String razon, String base64) {
        DatosUsuario datosUsuario;
        Documento documento = null;
        String retorno = null;
        byte[] byteDocumentoSigned = null;
        byte[] byteDocumento = java.util.Base64.getDecoder().decode(documentoBase64);
        try {
            // Obtener keyStore
            KeyStore keyStore = Pkcs12.getKeyStore(pkcs12, password);
            String alias = Pkcs12.getAlias(keyStore);
            datosUsuario = CertEcUtils.getDatosUsuarios((X509Certificate) keyStore.getCertificate(alias));

            String fechaHora = TiempoUtils.getFechaHoraServidor(null, base64);

            FirmaDigital firmador = new FirmaDigital();
            if ("xml".equalsIgnoreCase(formatoDocumento)) {
                byteDocumentoSigned = firmador.firmarXML(keyStore, alias, byteDocumento, password.toCharArray(), null, null, base64);
            }
            if ("pdf".equalsIgnoreCase(formatoDocumento)) {
                Properties properties = Propiedades.propiedades(versionFirmaEC, llx, lly, pagina, tipoEstampado, razon, null, fechaHora, base64);
                byteDocumentoSigned = firmador.firmarPDF(keyStore, alias, byteDocumento, password.toCharArray(), properties, null, base64);
           System.out.println("SARMEINTO EL TAMAÑO ES " + byteDocumentoSigned);

            }
        } catch (BadPasswordException bpe) {

            //2022-08-19 11:38:00,549 ERROR [org.jboss.as.ejb3.invocation] (default task-1) WFLYEJB0034: Jakarta Enterprise Beans Invocation failed on component ServicioAppFirmarDocumento for method public java.lang.String ec.gob.firmadigital.servicio.ServicioAppFirmarDocumento.firmarDocumento(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String): jakarta.ejb.EJBTransactionRolledbackException: PdfReader is not opened with owner password
//        Caused by: com.itextpdf.kernel.crypto.BadPasswordException: PdfReader is not opened with owner password
//        at deployment.servicio.war//com.itextpdf.kernel.pdf.PdfDocument.open(PdfDocument.java:1943)
//        at deployment.servicio.war//com.itextpdf.kernel.pdf.PdfDocument.<init>(PdfDocument.java:325)
//        at deployment.servicio.war//com.itextpdf.signatures.PdfSigner.initDocument(PdfSigner.java:306)
//        at deployment.servicio.war//com.itextpdf.signatures.PdfSigner.<init>(PdfSigner.java:288)
//        at deployment.servicio.war//com.itextpdf.signatures.PdfSigner.<init>(PdfSigner.java:271)
//        at deployment.servicio.war//ec.gob.firmadigital.sign.pdf.BasePdfSigner.sign(BasePdfSigner.java:86)
//        at deployment.servicio.war//ec.gob.firmadigital.servicio.util.FirmaDigital.firmarPDF(FirmaDigital.java:69)
//        at deployment.servicio.war//ec.gob.firmadigital.servicio.ServicioAppFirmarDocumento.firmarDocumento(ServicioAppFirmarDocumento.java:80)
            retorno = "Documento protegido con contraseña";
            throw bpe;
        } catch (ConexionException ce) {
            retorno = "Servidor FirmaEC: " + ce.getMessage();
            return retorno;
        } catch (InvalidKeyException ie) {
            retorno = "Problemas al abrir el documento";
            return retorno;
        } catch (EntidadCertificadoraNoValidaException ecnve) {
            retorno = "Certificado no válido";
            return retorno;
        } catch (HoraServidorException hse) {
            retorno = "Problemas en la red\nIntente nuevamente o verifique su conexión";
            return retorno;
        } catch (UnrecoverableKeyException uke) {
            retorno = "Certificado Corrupto";
            return retorno;
        } catch (KeyStoreException kse) {
            retorno = "La contraseña es inválida";
            return retorno;
        } catch (RubricaException re) {
            retorno = "No es posible procesar el documento";
            return retorno;
        } catch (CertificadoInvalidoException | SignatureVerificationException | DocumentoException e) {
            retorno = e.getMessage();
            return retorno;
        } catch (IOException | NoSuchAlgorithmException e) {
            retorno = "Excepción no conocida: " + e.getMessage();
            System.out.println("resultado: " + retorno);
            return retorno;
        }
        if (byteDocumentoSigned != null) {
            try {
                //Verificar Documento
                InputStream inputStreamDocumento = new ByteArrayInputStream(byteDocumentoSigned);
                PdfReader pdfReader = new PdfReader(inputStreamDocumento);
                Signer signer = new PDFSignerItext();
                java.util.List<SignInfo> signInfos;
                signInfos = signer.getSigners(byteDocumentoSigned);
                documento = pdfToDocumento(pdfReader, signInfos);
            } catch (java.lang.UnsupportedOperationException uoe) {
                retorno = "No es posible procesar el documento desde dispositivo móvil\nIntentar en FirmaEC de Escritorio";
            } catch (com.itextpdf.io.IOException ioe) {
                retorno = "El archivo no es PDF";
            } catch (SignatureVerificationException sve) {
                retorno = sve.toString();
            } catch (Exception ex) {
                retorno = ex.toString();
            }
        }
        if (documento == null) {
            documento = new Documento(false, false, new ArrayList<>(), retorno);
        }
        String json = Json.generarJsonDocumentoFirmado(byteDocumentoSigned, documento);
        if (documento.getError() == null) {
            String nombreSistema = "FirmaECMobile";
            logger.log(Level.INFO, "Documento enviado al sistema {0}, firmado por {1}, sistema operativo {2}, tamano documento (bytes) {3}", new Object[]{nombreSistema, hashMD5(datosUsuario.getCedula()), obtenerSO(base64), Integer.valueOf(byteDocumentoSigned.length)});
            this.servicioLog.info("ServicioAppFirmarDocumento::firmarDocumento", "Documento enviado al sistema " + nombreSistema + ", firmado por "
                    + hashMD5(datosUsuario.getCedula()) + ", sistema operativo "
                    + obtenerSO(base64) + ", tamano documento (bytes) " + byteDocumentoSigned.length);
        }
        return json;
    }

    private String obtenerSO(String base64) {
        JsonObject jsonObjectBase64;
        String toString = new String(Base64.getDecoder().decode(base64));
        JsonReader jsonReader = jakarta.json.Json.createReader(new StringReader(toString));
        try {
            jsonObjectBase64 = jsonReader.readObject();
            if (jsonReader != null) {
                jsonReader.close();
            }
        } catch (Throwable throwable) {
            if (jsonReader != null)
        try {
                jsonReader.close();
            } catch (Throwable throwable1) {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
        return jsonObjectBase64.getString("sistemaOperativo");
    }

    private String hashMD5(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(texto.getBytes("UTF-8"));
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
