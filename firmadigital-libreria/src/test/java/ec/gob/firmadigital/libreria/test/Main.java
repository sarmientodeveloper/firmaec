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
package ec.gob.firmadigital.libreria.test;

import static ec.gob.firmadigital.libreria.certificate.CertUtils.seleccionarAlias;
import static ec.gob.firmadigital.libreria.utils.Utils.dateToCalendar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Properties;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.TSAClientBouncyCastle;
import ec.gob.firmadigital.libreria.certificate.CertEcUtils;
import ec.gob.firmadigital.libreria.certificate.to.Certificado;
import ec.gob.firmadigital.libreria.certificate.to.DatosUsuario;
import ec.gob.firmadigital.libreria.certificate.to.Documento;
import ec.gob.firmadigital.libreria.core.Util;
import ec.gob.firmadigital.libreria.exceptions.HoraServidorException;
import ec.gob.firmadigital.libreria.exceptions.InvalidFormatException;
import ec.gob.firmadigital.libreria.exceptions.SignatureVerificationException;
import ec.gob.firmadigital.libreria.keystore.FileKeyStoreProvider;
import ec.gob.firmadigital.libreria.keystore.KeyStoreProvider;
import ec.gob.firmadigital.libreria.keystore.KeyStoreProviderFactory;
import ec.gob.firmadigital.libreria.model.Document;
import ec.gob.firmadigital.libreria.model.InMemoryDocument;
import ec.gob.firmadigital.libreria.sign.DigestAlgorithm;
import ec.gob.firmadigital.libreria.sign.PrivateKeySigner;
import ec.gob.firmadigital.libreria.sign.SignConstants;
import ec.gob.firmadigital.libreria.sign.pdf.PDFSignerItext;
import ec.gob.firmadigital.libreria.sign.pdf.PadesBasicSigner;
import ec.gob.firmadigital.libreria.sign.pdf.PadesHashSigner;
import ec.gob.firmadigital.libreria.sign.pdf.RectanguloUtil;
import ec.gob.firmadigital.libreria.sign.xades.XAdESSigner;
import ec.gob.firmadigital.libreria.utils.FileUtils;
import ec.gob.firmadigital.libreria.utils.Json;
import ec.gob.firmadigital.libreria.utils.PropertiesTsa;
import ec.gob.firmadigital.libreria.utils.PropertiesUtils;
import ec.gob.firmadigital.libreria.utils.TiempoUtils;
import ec.gob.firmadigital.libreria.utils.Utils;
import ec.gob.firmadigital.libreria.utils.UtilsCrlOcsp;
import ec.gob.firmadigital.libreria.utils.X509CertificateUtils;
import ec.gob.firmadigital.libreria.validaciones.DocumentoUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Metodo de pruebas funcionales
 *
 * @author mfernandez
 */
public class Main {

    // ARCHIVO
    private static final String PKCS12 = "/home/mfernandez/appFirmaEC/pruebaPre2024.p12";
    private static final String PASSWORD = "123456";
//    private static final String FILE = "/home/mfernandez/Descargas/Manual Usuario FirmaEC v3.pdf";
//    private static final String FILE = "/home/mfernandez/Test/Verify/09.pdf";
    private static final String FILE = "/home/mfernandez/appFirmaEC/documento_blanco.pdf";
    private static final String HASH_ALGORITHM = "SHA512";

    public static void main(String args[]) throws KeyStoreException, Exception {
//        firmarDocumentoHashTrifasica(FILE);
        firmarDocumentoTrifasica(FILE);
//        firmarDocumentoPDF(FILE);
//        firmarDocumentoXML(FILE);
//        validarCertificado();
//        verificarDocumento(FILE);
//        fechaHora(240);//espera en segundos
    }

    private static void firmarDocumentoHashTrifasica(String file) throws KeyStoreException, Exception {
        KeyStore keyStore = getKeyStore(PKCS12, PASSWORD, null);
//        KeyStore keyStore = getKeyStore(null, PASSWORD, "TOKEN");"TOKEN", "PCSC"

        ////// LEER PDF:
        byte[] docByteArry = DocumentoUtils.loadFile(file);

        byte[] signed = null;
        String alias = seleccionarAlias(keyStore, null);
        PrivateKey key = (PrivateKey) keyStore.getKey(alias, PASSWORD.toCharArray());
        Certificate[] certChain = keyStore.getCertificateChain(alias);
        //////////////////////////////////////////////////////
        Document document = new InMemoryDocument(docByteArry);
        try (InputStream is = document.openStream()) {
            // Crear un RubricaSigner para firmar el MessageDigest del documento
            PrivateKeySigner signer = new PrivateKeySigner(key, DigestAlgorithm.forName(HASH_ALGORITHM));
            // Crear un PdfSigner para firmar el documento
            PadesHashSigner padesHashSigner = new PadesHashSigner(signer);
            // Configurar el PdfSigner
            Properties properties = parametros();
            // Firmar el documento
            padesHashSigner.emptySignature(is, certChain, properties);
            String fieldName = padesHashSigner.getFieldName();
            ByteArrayOutputStream documentoPorFirmar = padesHashSigner.getDocumentoPorFirmar();
            padesHashSigner.createSignature(documentoPorFirmar, fieldName, key, certChain);
            ByteArrayOutputStream documentoFirmado = padesHashSigner.getDocumentoFirmado();
            signed = documentoFirmado.toByteArray();
        }
        //////////////////////////////////////////////////////
        System.out.println("final firma\n-------");
        ////// Permite guardar el archivo en el equipo y luego lo abre
        String nombreDocumento = FileUtils.crearNombreFirmado(new File(file), FileUtils.getExtension(signed));
        FileOutputStream fos = new java.io.FileOutputStream(nombreDocumento);
        //Abrir documento
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    FileUtils.abrirDocumento(nombreDocumento);
                    System.out.println(nombreDocumento);
                    // verificarDocumento(nombreDocumento);
                } catch (java.lang.Exception ex) {
                    ex.printStackTrace();
                } finally {
                    System.exit(0);
                }
            }
        }, 3000); //espera 3 segundos
        fos.write(signed);
        fos.close();
    }

    private static void firmarDocumentoTrifasica(String file) throws KeyStoreException, Exception {
        KeyStore keyStore = getKeyStore(PKCS12, PASSWORD, null);
//        KeyStore keyStore = getKeyStore(null, PASSWORD, "TOKEN");"TOKEN", "PCSC"

        ////// LEER PDF:
        byte[] docByteArry = DocumentoUtils.loadFile(file);

        byte[] signed = null;
        String alias = seleccionarAlias(keyStore, null);
        PrivateKey key = (PrivateKey) keyStore.getKey(alias, PASSWORD.toCharArray());
        Certificate[] certChain = keyStore.getCertificateChain(alias);
/////////////////////
//            // Calculate SHA256 hash of the PDF document
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hash = digest.digest(is.readAllBytes());
//            String pdfHash = Base64.getEncoder().encodeToString(hash);
//            System.out.println("pdfHash: " + pdfHash);
//
//            ICrlClient crlClient = new CrlClientOnline();
//            OCSPVerifier ocspVerifier = new OCSPVerifier(null, null);
//            IOcspClient ocspClient = new OcspClientBouncyCastle(ocspVerifier);
//            ITSAClient tsc = new TSAClientBouncyCastle(tsaUrl, tsaUsername, tsaPassword);
//            Collection<ICrlClient> crlClients = Collections.singleton(crlClient);
/////////////////////
//            // Create TSAClient with optional authentication
//            PropertiesTsa propertiesTsa = new PropertiesTsa();
//            TSAClientBouncyCastle tsaClient = new TSAClientBouncyCastle(propertiesTsa.getTsaUrl(), propertiesTsa.getTsaUsername(), propertiesTsa.getTsaPassword());
//            // Send timestamping request and get response
//            byte[] timestampToken;
//            try (ByteArrayInputStream bais = new ByteArrayInputStream(docByteArry)) {
//                timestampToken = tsaClient.getTimeStampToken(bais.readAllBytes());
//            } catch (Exception e) {
//                // Handle exception
//                e.printStackTrace();
//                return;
//            }
////            // Validate and process the timestamp token (use appropriate logic)
////            FileUtils.saveByteArrayToDisc(timestampToken, "/home/mfernandez/timestampToken");
////            System.out.println("Timestamp Token obtained!");
/////////////////////
        //////////////////////////////////////////////////////
        Document document = new InMemoryDocument(docByteArry);
        try (InputStream is = document.openStream()) {
            // Crear un RubricaSigner para firmar el MessageDigest del documento
            PrivateKeySigner signer = new PrivateKeySigner(key, DigestAlgorithm.forName(HASH_ALGORITHM));

            // Crear un PdfSigner para firmar el documento
            PadesBasicSigner pdfSigner = new PadesBasicSigner(signer);
//            PadesLtvSigner pdfSigner = new PadesLtvSigner(signer);
//            PadesEnhancedSigner pdfSigner = new PadesEnhancedSigner(signer);

            // Configurar el PdfSigner
            Properties properties = parametros();

            // Firmar el documento
            signed = pdfSigner.sign(is, signer, certChain, properties);
        }
        //////////////////////////////////////////////////////
        System.out.println("final firma\n-------");
        ////// Permite guardar el archivo en el equipo y luego lo abre
        String nombreDocumento = FileUtils.crearNombreFirmado(new File(file), FileUtils.getExtension(signed));
        FileOutputStream fos = new java.io.FileOutputStream(nombreDocumento);
        //Abrir documento
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    FileUtils.abrirDocumento(nombreDocumento);
                    System.out.println(nombreDocumento);
                    // verificarDocumento(nombreDocumento);
                } catch (java.lang.Exception ex) {
                    ex.printStackTrace();
                } finally {
                    System.exit(0);
                }
            }
        }, 3000); //espera 3 segundos
        fos.write(signed);
        fos.close();
    }

    private static void firmarDocumentoPDF(String file) throws KeyStoreException, Exception {
        KeyStore keyStore = getKeyStore(PKCS12, PASSWORD, null);
//        KeyStore keyStore = getKeyStore(null, PASSWORD, "TOKEN");"TOKEN", "PCSC"

        ////// LEER PDF:
        byte[] docByteArry = DocumentoUtils.loadFile(file);

        byte[] signed = null;
        String alias = seleccionarAlias(keyStore, null);
        PrivateKey key = (PrivateKey) keyStore.getKey(alias, PASSWORD.toCharArray());

        X509CertificateUtils x509CertificateUtils = new X509CertificateUtils();
        System.out.println("x509CertificateUtils: " + x509CertificateUtils);
        // if (x509CertificateUtils.validarX509Certificate((X509Certificate) keyStore.getCertificate(alias), null)) {//validación de firmaEC
        Certificate[] certChain = keyStore.getCertificateChain(alias);
        Properties properties = parametros();
        properties.setProperty(PDFSignerItext.PATH, file);
        PDFSignerItext pDFSignerItext = new PDFSignerItext();
        pDFSignerItext.setProvider(keyStore.getProvider());//QA
        signed = pDFSignerItext.sign(docByteArry, DigestAlgorithms.SHA512, key, certChain, properties, PropertiesUtils.versionBase64());
        System.out.println("final firma\n-------");
        ////// Permite guardar el archivo en el equipo y luego lo abre
        String nombreDocumento = FileUtils.crearNombreFirmado(new File(file), FileUtils.getExtension(signed));
        FileOutputStream fos = new java.io.FileOutputStream(nombreDocumento);
        System.out.println("fos: " + fos);
        //Abrir documento
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    FileUtils.abrirDocumento(nombreDocumento);
                    System.out.println(nombreDocumento);
                    // verificarDocumento(nombreDocumento);
                } catch (java.lang.Exception ex) {
                    ex.printStackTrace();
                } finally {
                    System.exit(0);
                }
            }
        }, 3000); //espera 3 segundos
        fos.write(signed);
        fos.close();
        //Abrir documento
        //  } else {
        //      System.out.println("Entidad Certificadora no reconocida");
        //   }
    }

    private static void firmarDocumentoXML(String file) throws KeyStoreException, Exception {
        KeyStore keyStore = getKeyStore(PKCS12, PASSWORD, null);
//        KeyStore keyStore = getKeyStore(null, PASSWORD, "TOKEN");"TOKEN", "PCSC"
        ////// LEER XML:
        byte[] docByteArry = DocumentoUtils.loadFile(file);

        byte[] signed = null;
        String alias = seleccionarAlias(keyStore, null);
        PrivateKey key = (PrivateKey) keyStore.getKey(alias, PASSWORD.toCharArray());

        X509CertificateUtils x509CertificateUtils = new X509CertificateUtils();
        try {
            if (x509CertificateUtils.validarX509Certificate((X509Certificate) keyStore.getCertificate(alias), null, PropertiesUtils.versionBase64())) {//validación de firmaEC
                Certificate[] certChain = keyStore.getCertificateChain(alias);
                XAdESSigner signer = new XAdESSigner();
                signed = signer.sign(docByteArry, SignConstants.SIGN_ALGORITHM_SHA512WITHRSA, key, certChain, null, PropertiesUtils.versionBase64());
                System.out.println("final firma\n-------");
                ////// Permite guardar el archivo en el equipo y luego lo abre
                String nombreDocumento = FileUtils.crearNombreFirmado(new File(file), FileUtils.getExtension(signed));
                FileOutputStream fos = new java.io.FileOutputStream(nombreDocumento);
                //Abrir documento
                new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            FileUtils.abrirDocumento(nombreDocumento);
                            System.out.println(nombreDocumento);
                            // verificarDocumento(nombreDocumento);
                        } catch (java.lang.Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            System.exit(0);
                        }
                    }
                }, 3000); //espera 3 segundos
                fos.write(signed);
                fos.close();
            } else {
                System.out.println("Entidad Certificadora no reconocida");
            }
        } catch (Exception e) {
            if (e.getClass() == IllegalArgumentException.class) {
                System.out.println("Problemas con la emisión del certificado digital");
            } else {
                e.printStackTrace();
            }
        }
    }

    private static void validarCertificado() throws IOException, KeyStoreException, Exception {
        KeyStore keyStore = getKeyStore(PKCS12, PASSWORD, null);//ARCHIVO
//        KeyStore keyStore = getKeyStore(null, PASSWORD, "TOKEN");//"TOKEN", "PCSC"
        String alias = seleccionarAlias(keyStore, null);
        X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
        System.out.println("UID: " + Utils.getUID(x509Certificate));
        System.out.println("CN: " + Utils.getCN(x509Certificate));
        System.out.println("emisión: " + CertEcUtils.getNombreCA(x509Certificate));
        System.out.println("fecha emisión: " + x509Certificate.getNotBefore());
        System.out.println("fecha expiración: " + x509Certificate.getNotAfter());
        System.out.println("ISSUER: " + x509Certificate.getIssuerX500Principal().getName());
        System.out.println("Subject: " + x509Certificate.getSubjectDN());
        System.out.println("Serial: " + x509Certificate.getSerialNumber());

        Date fechaHoraISO = fechaHoraISO();

        //Validad certificado revocado
        Date fechaRevocado = UtilsCrlOcsp.validarFechaRevocado(x509Certificate, null);
        //Desarrollo
//        Date fechaRevocado = UtilsCrlOcsp.validarOCSPDate(x509Certificate);
        if (fechaRevocado != null && fechaRevocado.compareTo(fechaHoraISO) <= 0) {
            System.out.println("Certificado revocado: " + fechaRevocado);
        }
        if (fechaHoraISO.compareTo(x509Certificate.getNotBefore()) <= 0 || fechaHoraISO.compareTo(x509Certificate.getNotAfter()) >= 0) {
            System.out.println("Certificado caducado");
        }
        System.out.println("Certificado emitido por entidad certificadora acreditada? " + Utils.verifySignature(x509Certificate));

        DatosUsuario datosUsuario = CertEcUtils.getDatosUsuarios(x509Certificate);
        Certificado certificado = new Certificado(
                Util.getCN(x509Certificate),
                CertEcUtils.getNombreCA(x509Certificate),
                dateToCalendar(x509Certificate.getNotBefore()),
                dateToCalendar(x509Certificate.getNotAfter()),
                null,
                dateToCalendar(UtilsCrlOcsp.validarFechaRevocado(x509Certificate, null)),
                null,
                datosUsuario);
        certificado.setKeyUsages(Utils.validacionKeyUsages(x509Certificate));

        System.out.println("Certificado: " + certificado);
        System.out.println(Json.generarJsonCertificado(certificado));
    }

//    private static void verificarDocumento(String file) throws IOException, SignatureVerificationException, Exception {
//        byte[] bs = DocumentoUtils.loadFile(file);
//        FileNameMap MIMETYPES = URLConnection.getFileNameMap();
//        System.out.println("MIMETYPES: " + MIMETYPES.getContentTypeFor(file));
//        if (MIMETYPES.getContentTypeFor(file).equals("application/pdf")) {
//            Document document = new InMemoryDocument(bs);
//            InputStream is = document.openStream();
//            Documento documento = Utils.pdfToDocumento(is);
//            System.out.println("JSON:");
//            System.out.println(Json.GenerarJsonDocumento(documento));
//            System.out.println("Documento: " + documento);
//            if (documento.getCertificados() != null) {
//                documento.getCertificados().forEach((certificado) -> {
//                    System.out.println(certificado.toString());
//                });
//            }
//        }
//    }
    private static void verificarDocumento(String file) throws IOException, SignatureVerificationException, Exception {
        File document = new File(file);
        Documento documento = Utils.verificarDocumento(document, PropertiesUtils.versionBase64());
        System.out.println("JSON:");
        System.out.println(Json.generarJsonDocumento(documento));
        System.out.println("Documento: " + documento);
        if (documento.getCertificados() != null) {
            documento.getCertificados().forEach((certificado) -> {
                System.out.println(certificado.toString());
            });
            System.out.println(Json.generarJsonDocumento(documento));
        } else {
            throw new InvalidFormatException("Documento no soportado");
        }
    }

    private static Properties parametros() throws IOException, HoraServidorException {
        //PageSize.A4.getWidth();//595.0
        //PageSize.A4.getHeight();//842.0
        //QR
        //SUPERIOR IZQUIERDA
        String llx = "10";
        String lly = "830";
        //INFERIOR IZQUIERDA
        //String llx = "100";
        //String lly = "91";
        //INFERIOR DERECHA
        //String llx = "419";
        //String lly = "91";
        //INFERIOR CENTRADO
        //String llx = "260";
        //String lly = "91";
        //QR
        //SUPERIOR IZQUIERDA
        //String llx = "10";
        //String lly = "830";
        //String urx = String.valueOf(Integer.parseInt(llx) + 110);
        //String ury = String.valueOf(Integer.parseInt(lly) - 36);
        //INFERIOR CENTRADO
        //String llx = "190";
        //String lly = "85";
        //String urx = String.valueOf(Integer.parseInt(llx) + 260);
        //String ury = String.valueOf(Integer.parseInt(lly) - 36);
        //INFERIOR CENTRADO (ancho pie pagina)
        //String llx = "100";
        //String lly = "85";
        //String urx = String.valueOf(Integer.parseInt(llx) + 430);
        //String ury = String.valueOf(Integer.parseInt(lly) - 25);
        //INFERIOR DERECHA
        //String llx = "10";
        //String lly = "85";
        //String urx = String.valueOf(Integer.parseInt(llx) + 260);
        //String ury = String.valueOf(Integer.parseInt(lly) - 36);

        Properties params = new Properties();
        params.setProperty(PDFSignerItext.SIGNING_LOCATION, "Teletrabajo");
        params.setProperty(PDFSignerItext.SIGNING_REASON, "Firmado digitalmente con RUBRICA");
        params.setProperty(PDFSignerItext.SIGN_TIME, TiempoUtils.getFechaHoraServidor(null, PropertiesUtils.versionBase64()));
        params.setProperty(PDFSignerItext.LAST_PAGE, "6");
        params.setProperty(PDFSignerItext.TYPE_SIG, "QR");
        params.setProperty(PDFSignerItext.INFO_QR, "Firmado digitalmente con RUBRICA\nhttps://minka.gob.ec/rubrica/rubrica");
        //params.setProperty(PDFSignerItext.TYPE_SIG, "information2");
        //params.setProperty(PDFSigner.FONT_SIZE, "4.5");
        // Posicion firma
        params.setProperty(RectanguloUtil.POSITION_ON_PAGE_LOWER_LEFT_X, llx);
        params.setProperty(RectanguloUtil.POSITION_ON_PAGE_LOWER_LEFT_Y, lly);
        //params.setProperty(RectanguloUtil.POSITION_ON_PAGE_UPPER_RIGHT_X, urx);
        //params.setProperty(RectanguloUtil.POSITION_ON_PAGE_UPPER_RIGHT_Y, ury);
        return params;
    }

    private static KeyStore getKeyStore(String archivo, String password, String tipoKeyStoreProvider) throws KeyStoreException {
        if (archivo != null) { // ARCHIVO
            KeyStoreProvider ksp = new FileKeyStoreProvider(archivo);
            return ksp.getKeystore(password.toCharArray());
        } else { // TOKEN
            return KeyStoreProviderFactory.getKeyStore(password, tipoKeyStoreProvider);
        }
    }

    private static Date fechaHoraISO() throws IOException, HoraServidorException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        TemporalAccessor accessor = dateTimeFormatter.parse(TiempoUtils.getFechaHoraServidor(null, PropertiesUtils.versionBase64()));
        return Date.from(Instant.from(accessor));
    }

    //pruebas de fecha-hora
    private static void fechaHora(int segundos) throws KeyStoreException, Exception {
        tiempo(segundos);//espera en segundos
        do {
            try {
                System.out.println("getFechaHora() " + TiempoUtils.getFechaHora(null, PropertiesUtils.versionBase64()));
                System.out.println("getFechaHoraServidor() " + TiempoUtils.getFechaHoraServidor(null, PropertiesUtils.versionBase64()));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } while (tiempo);
        System.exit(0);
    }

    private static boolean tiempo = true;

    private static void tiempo(int segundos) {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                tiempo = false;
            }
        }, segundos * 1000); //espera 3 segundos
    }
    //pruebas de fecha-hora
}
