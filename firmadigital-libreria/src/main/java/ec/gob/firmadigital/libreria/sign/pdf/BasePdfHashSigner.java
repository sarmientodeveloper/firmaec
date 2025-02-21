/*
 * Copyright (C) 2022 
 * Authors: Misael Fernández
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
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Logger;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.ExternalBlankSignatureContainer;
import com.itextpdf.signatures.IExternalSignatureContainer;
import com.itextpdf.signatures.ITSAClient;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import com.itextpdf.signatures.SignatureUtil;
import com.itextpdf.signatures.TSAClientBouncyCastle;

import ec.gob.firmadigital.libreria.certificate.CertEcUtils;
import ec.gob.firmadigital.libreria.certificate.to.DatosUsuario;
import ec.gob.firmadigital.libreria.exceptions.EntidadCertificadoraNoValidaException;
import ec.gob.firmadigital.libreria.exceptions.InvalidFormatException;
import ec.gob.firmadigital.libreria.model.Document;
import ec.gob.firmadigital.libreria.model.InMemoryDocument;
import ec.gob.firmadigital.libreria.sign.RubricaSigner;
import ec.gob.firmadigital.libreria.sign.SignInfo;
import ec.gob.firmadigital.libreria.sign.pdf.appearance.CustomAppearance;
import ec.gob.firmadigital.libreria.sign.pdf.appearance.Information1Appearance;
import ec.gob.firmadigital.libreria.sign.pdf.appearance.Information2Appearance;
import ec.gob.firmadigital.libreria.sign.pdf.appearance.QrAppereance;
import ec.gob.firmadigital.libreria.utils.PropertiesTsa;
import ec.gob.firmadigital.libreria.utils.Utils;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class BasePdfHashSigner implements PdfHashSigner {

    private static final Logger logger = Logger.getLogger(BasePdfHashSigner.class.getName());

    private static final String ALGORITHM = "SHA-256";
    private static final String SIGNING_REASON = "signingReason";
    private static final String SIGNING_LOCATION = "signingLocation";
    private static final String SIGN_TIME = "signTime";
    private static final String LAST_PAGE = "page";
    private static final String FONT_SIZE = "font";
    private static final String TYPE_SIG = "typeSignature";
    private static final String INFO_QR = "infoQR";

    private static String fieldName;
    private static ByteArrayOutputStream baos;
    private static ByteArrayOutputStream baosSign;

    // ETSI TS 102 778-1 V1.1.1 (2009-07)
    // PAdES Enhanced - PAdES-BES Profile
    protected abstract byte[] signInternal(ByteArrayOutputStream os, com.itextpdf.signatures.PdfSigner pdfSigner,
            RubricaSigner signer, Certificate[] certChain, Properties params) throws IOException;

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public ByteArrayOutputStream getDocumentoPorFirmar() {
        return baos;
    }

    @Override
    public ByteArrayOutputStream getDocumentoFirmado() {
        return baosSign;
    }

    @Override
    public void emptySignature(InputStream is, Certificate[] certChain, Properties params)
            throws IOException {
        PdfReader pdfReader = new PdfReader(is);
        baos = new ByteArrayOutputStream();

        X509Certificate x509Certificate = (X509Certificate) certChain[0];

        Properties extraParams = params != null ? params : new Properties();
        // Motivo de la firma
        String reason = extraParams.getProperty(SIGNING_REASON);
        // Lugar de realizacion de la firma
        String location = extraParams.getProperty(SIGNING_LOCATION);
        // Fecha y hora de la firma, en formato ISO-8601
        String signTime = extraParams.getProperty(SIGN_TIME);
        // Tamaño letra
        float fontSize = 3;

        try {
            if (extraParams.getProperty(FONT_SIZE) == null) {
                fontSize = 3;
            } else {
                fontSize = Float.parseFloat(extraParams.getProperty(FONT_SIZE).trim());
            }
        } catch (Exception e) {
            logger.warning("Se ha indicado un tamaño de letra invalida ('" + extraParams.getProperty(FONT_SIZE)
                    + "'), se usara el tamaño por defecto: " + fontSize + " " + e);
        }

        // Tipo de firma (Información, QR)
        String typeSig = extraParams.getProperty(TYPE_SIG);
        if (typeSig == null) {
            typeSig = "information1";
        }

        if (typeSig.equals("QR") && extraParams.getProperty(FONT_SIZE) == null) {
            fontSize = 4.5f;
        }

        String infoQR = "";
        if (extraParams.getProperty(INFO_QR) == null) {
            infoQR = "";
        } else {
//                infoQR = extraParams.getProperty(INFO_QR).trim();
            infoQR = extraParams.getProperty(INFO_QR, "").trim();
        }

        // Pagina donde situar la firma visible
        int page = 0;

        try {
            if (extraParams.getProperty(LAST_PAGE) == null) {
                page = 0;
            } else {
                page = Integer.parseInt(extraParams.getProperty(LAST_PAGE).trim());
            }
        } catch (Exception e) {
            logger.warning("Se ha indicado un numero de pagina invalido ('" + extraParams.getProperty(LAST_PAGE)
                    + "'), se usara la ultima pagina: " + e);
        }

        Rectangle signaturePositionOnPage = RectanguloUtil.getPositionOnPage(extraParams);

        StampingProperties stampingProperties = new StampingProperties();
        //TODO Edison Lomas Almeida: La línea siguiente genera error PdfException: Append mode requires a document without errors, even if recovery is possible.
        stampingProperties.useAppendMode();

        com.itextpdf.signatures.PdfSigner pdfSigner = new com.itextpdf.signatures.PdfSigner(pdfReader, baos, stampingProperties);

        if (page == 0 || page < 0 || page > pdfSigner.getDocument().getNumberOfPages()) {
            page = pdfSigner.getDocument().getNumberOfPages();
        }

        PdfSignatureAppearance signatureAppearance = pdfSigner.getSignatureAppearance();
        signatureAppearance.setPageRect(signaturePositionOnPage).setPageNumber(page);

        if (typeSig != null) {

            if (signaturePositionOnPage != null) {
                DatosUsuario datosUsuario = null;
                try {
                    datosUsuario = CertEcUtils.getDatosUsuarios(x509Certificate);
                } catch (EntidadCertificadoraNoValidaException ex) {
                    Logger.getLogger(BasePdfHashSigner.class.getName()).log(Level.SEVERE, null, ex);
                }
                String nombreFirmante = (datosUsuario.getNombre() + " " + datosUsuario.getApellido()).toUpperCase();
                String informacionCertificado = x509Certificate.getSubjectDN().getName();

                PdfDocument pdfDocument = pdfSigner.getDocument();

                CustomAppearance customAppearance;

                switch (typeSig) {
                    case "QR": {
                        customAppearance = new QrAppereance(nombreFirmante, reason, location, signTime, infoQR);
                        break;
                    }
                    case "information1": {
                        customAppearance = new Information1Appearance(nombreFirmante, informacionCertificado, reason,
                                location, signTime);
                        break;
                    }
                    case "information2": {
                        customAppearance = new Information2Appearance(nombreFirmante, reason, location, signTime);
                        break;
                    }
                    default: {
                        throw new RuntimeException("typeSign unknown");
                    }
                }

                customAppearance.createCustomAppearance(signatureAppearance, page, pdfDocument,
                        signaturePositionOnPage);
            }

            // Razon de firma
            if (reason != null) {
                signatureAppearance.setReason(reason);
            }

            // Localización en donde se produce la firma
            if (location != null) {
                signatureAppearance.setLocation(location);
            }

            signatureAppearance.setSignatureCreator("FirmaEC");
        }

        // Fecha y hora de la firma
        if (signTime != null) {
            Date date = Utils.getSignTime(signTime);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            pdfSigner.setSignDate(calendar);
        }

        try {
            /*
             * ExternalBlankSignatureContainer constructor will create the PdfDictionary for
             * the signature information and will insert the /Filter and /SubFilter values
             * into this dictionary. It will leave just a blank placeholder for the
             * signature that is to be inserted later.
             */
            IExternalSignatureContainer external = new ExternalBlankSignatureContainer(PdfName.Adobe_PPKLite,
                    PdfName.Adbe_pkcs7_detached);

            // Sign the document using an external container.
            // 8192 is the size of the empty signature placeholder.
            pdfSigner.signExternalContainer(external, 8192 * 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fieldName = pdfSigner.getFieldName();
    }

    @Override
    public void createSignature(ByteArrayOutputStream baos, String fieldName, PrivateKey pk, Certificate[] chain)
            throws IOException, GeneralSecurityException {
        Document document = new InMemoryDocument(baos.toByteArray());
        try (InputStream is = document.openStream()) {
            PdfReader reader = new PdfReader(is);
            baosSign = new ByteArrayOutputStream();
            com.itextpdf.signatures.PdfSigner signer = new com.itextpdf.signatures.PdfSigner(reader, baosSign, new StampingProperties());

            IExternalSignatureContainer external = new MyExternalSignatureContainer(pk, chain, ALGORITHM);

            // Signs a PDF where space was already reserved. The field must cover the whole
            // document.
            PdfSigner.signDeferred(signer.getDocument(), fieldName, baosSign, external);
            reader.close();
        }
    }

    public List<SignInfo> getSigners(byte[] sign) throws InvalidFormatException, IOException {
        PdfReader pdfReader;
        try {
            Document document = new InMemoryDocument(sign);
            try (InputStream is = document.openStream()) {
                pdfReader = new PdfReader(is);
            }
        } catch (Exception e) {
            logger.severe("No se ha podido leer el PDF: " + e);
            throw new InvalidFormatException("No se ha podido leer el PDF", e);
        }
        SignatureUtil signatureUtil;
        try {
            PdfDocument pdfDocument = new PdfDocument(pdfReader);
            signatureUtil = new com.itextpdf.signatures.SignatureUtil(pdfDocument);
        } catch (Exception e) {
            logger.severe(
                    "No se ha podido obtener la informacion de los firmantes del PDF, se devolvera un arbol vacio: "
                    + e);
            throw new InvalidFormatException("No se ha podido obtener la informacion de los firmantes del PDF", e);
        }

        @SuppressWarnings("unchecked")
        List<String> names = signatureUtil.getSignatureNames();
        List<SignInfo> signInfos = new ArrayList<>();
        for (String signatureName : names) {
            com.itextpdf.signatures.PdfPKCS7 pdfPKCS7;
            try {
                pdfPKCS7 = signatureUtil.readSignatureData(signatureName);
            } catch (Exception e) {
                e.printStackTrace();
                logger.severe("El PDF contiene una firma corrupta o con un formato desconocido (" + signatureName
                        + "), se continua con las siguientes si las hubiese: " + e);
                continue;
            }
            Certificate[] signCertificateChain = pdfPKCS7.getSignCertificateChain();
            X509Certificate[] certChain = new X509Certificate[signCertificateChain.length];
            for (int i = 0; i < certChain.length; i++) {
                certChain[i] = (X509Certificate) signCertificateChain[i];
            }
            SignInfo signInfo = new SignInfo(certChain, pdfPKCS7.getSignDate().getTime());
            signInfos.add(signInfo);
        }
        return signInfos;
    }

    // private static final String PDF_FILE_HEADER = "%PDF-";
    // private boolean isPdfFile(final byte[] data) {
    // byte[] buffer = new byte[PDF_FILE_HEADER.length()];
    // try {
    // new ByteArrayInputStream(data).read(buffer);
    // } catch (Exception e) {
    // buffer = null;
    // }
    // // Comprobamos que cuente con una cabecera PDF
    // if (buffer != null && !PDF_FILE_HEADER.equals(new String(buffer))) {
    // return false;
    // }
    // try {
    // // Si lanza una excepcion al crear la instancia, no es un fichero
    // // PDF
    // new PdfReader(data);
    // } catch (final Exception e) {
    // return false;
    // }
    // return true;
    // }
    class MyExternalSignatureContainer implements IExternalSignatureContainer {

        protected PrivateKey pk;
        protected Certificate[] chain;
        protected String algorithm;

        public MyExternalSignatureContainer(PrivateKey pk, Certificate[] chain, String algorithm) {
            this.pk = pk;
            this.chain = chain;
            this.algorithm = algorithm;
        }

        @Override
        public byte[] sign(InputStream is) throws GeneralSecurityException {
            try {
                PrivateKeySignature signature = new PrivateKeySignature(pk, algorithm, "BC");
//                PrivateKeySignature signature = new PrivateKeySignature(pk, algorithm,
//                        provider == null ? "BC" : provider.getName());
                String hashAlgorithm = signature.getHashAlgorithm();
                BouncyCastleDigest digest = new BouncyCastleDigest();

                PdfPKCS7 sgn = new PdfPKCS7(null, chain, hashAlgorithm, null, digest, false);
                byte hash[] = DigestAlgorithms.digest(is, digest.getMessageDigest(hashAlgorithm));
                byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, PdfSigner.CryptoStandard.CMS, null, null);
                byte[] extSignature = signature.sign(sh);
                sgn.setExternalDigest(extSignature, null, signature.getEncryptionAlgorithm());

                // Create TSAClient with optional authentication
                PropertiesTsa propertiesTsa = new PropertiesTsa();
//                ITSAClient tsaClient = new TSAClientBouncyCastle("https://freetsa.org/tsr", null, null);
                ITSAClient tsaClient = new TSAClientBouncyCastle(propertiesTsa.getTsaUrl(), propertiesTsa.getTsaUsername(), propertiesTsa.getTsaPassword());

                return sgn.getEncodedPKCS7(hash, PdfSigner.CryptoStandard.CMS, tsaClient, null, null);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }

        @Override
        public void modifySigningDictionary(PdfDictionary signDic) {
        }
    }
}
