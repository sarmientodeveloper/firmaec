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
package ec.gob.firmadigital.libreria.certificate;

import java.security.cert.X509Certificate;

import ec.gob.firmadigital.libreria.certificate.ec.CertificadoFuncionarioPublico;
import ec.gob.firmadigital.libreria.certificate.ec.CertificadoMiembroEmpresa;
import ec.gob.firmadigital.libreria.certificate.ec.CertificadoPersonaJuridica;
import ec.gob.firmadigital.libreria.certificate.ec.CertificadoPersonaNatural;
import ec.gob.firmadigital.libreria.certificate.ec.CertificadoRepresentanteLegal;
import ec.gob.firmadigital.libreria.certificate.ec.CertificadoSelladoTiempo;
import ec.gob.firmadigital.libreria.certificate.ec.alphatechnologies.*;
import ec.gob.firmadigital.libreria.certificate.ec.anfac.*;
import ec.gob.firmadigital.libreria.certificate.ec.argosdata.*;
import ec.gob.firmadigital.libreria.certificate.ec.bce.*;
import ec.gob.firmadigital.libreria.certificate.ec.cj.*;
import ec.gob.firmadigital.libreria.certificate.ec.corpnewbest.*;
import ec.gob.firmadigital.libreria.certificate.ec.datil.*;
import ec.gob.firmadigital.libreria.certificate.ec.digercic.*;
import ec.gob.firmadigital.libreria.certificate.ec.eclipsoft.*;
import ec.gob.firmadigital.libreria.certificate.ec.firmasegura.*;
import ec.gob.firmadigital.libreria.certificate.ec.lazzate.*;
import ec.gob.firmadigital.libreria.certificate.ec.securitydata.*;
import ec.gob.firmadigital.libreria.certificate.ec.uanataca.*;
import ec.gob.firmadigital.libreria.certificate.to.DatosUsuario;
import ec.gob.firmadigital.libreria.exceptions.EntidadCertificadoraNoValidaException;
import ec.gob.firmadigital.libreria.utils.Utils;

/**
 * Validar diferentes certificados digitales acreditados por ARCOTEL
 *
 * @author mfernandez
 */
public class CertEcUtils {

    public static final String BCE_NAME = "Banco Central del Ecuador";
    public static final String CJ_NAME = "Consejo de la Judicatura";
    public static final String SECURITYDATA_NAME = "Security Data";
    public static final String ANFAC_NAME = "Anf AC";
    public static final String DIGERCIC_NAME = "Dirección General de Registro Civil, Identificación y Cedulación";
    public static final String UANATACA_NAME = "UANATACA S.A.";
    public static final String ECLIPSOFT_NAME = "ECLIPSOFT S.A.";
    public static final String DATIL_NAME = "DATILMEDIA S.A.";
    public static final String AGOSDATA_NAME = "ARGOSDATA CA";
    public static final String LAZZATE_NAME = "LAZZATE CIA. LTDA";
    public static final String ALPHATECHNOLOGIES_NAME = "ALPHA TECHNOLOGIES";
    public static final String CORPNEWBEST_NAME = "CORPNEWBEST CIA. LTDA.";
    public static final String FIRMASEGURA_NAME = "FIRMASEGURA S.A.S.";

    public static X509Certificate getRootCertificate(X509Certificate certificado) throws EntidadCertificadoraNoValidaException {
        String entidadCertStr = getNombreCA(certificado);

        switch (entidadCertStr) {
            case BCE_NAME: {
                try {
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new BceSubCaCert20112021())) {
                        System.out.println("BceSubCaCert 2011-2021");
                        return new BceSubCaCert20112021();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new BceSubCaCert20192029())) {
                        System.out.println("BceSubCaCert 2019-2029");
                        return new BceSubCaCert20192029();
                    }
                    return null;
                } catch (java.security.InvalidKeyException ex) {
                    //TODO
                }
            }
            case SECURITYDATA_NAME: {
                try {
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new SecurityDataSubCaCert20112026())) {
                        System.out.println("SecurityDataSubCaCert");
                        return new SecurityDataSubCaCert20112026();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new SecurityDataSubCaCert20192031())) {
                        System.out.println("SecurityDataSubCaCert 2019-2031");
                        return new SecurityDataSubCaCert20192031();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new SecurityDataSubCaCert20202039())) {
                        System.out.println("SecurityDataSubCaCert 2020-2032");
                        return new SecurityDataSubCaCert20202039();
                    }
                    return null;
                } catch (java.security.InvalidKeyException ex) {
                    //TODO
                }
            }
            case CJ_NAME:
                return new ConsejoJudicaturaSubCaCert();
            case ANFAC_NAME:
                try {
                if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new AnfAc18332SubCaCert20162032())) {
                    System.out.println("Anf 2016-2032");
                    return new AnfAc18332SubCaCert20162032();
                }
                if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new AnfAc37442SubCaCert20192029())) {
                    System.out.println("Anf 2019-2029");
                    return new AnfAc37442SubCaCert20192029();
                }
                return null;
            } catch (java.security.InvalidKeyException ex) {
                //TODO
            }
            case DIGERCIC_NAME: {
                return new DigercicSubCaCert20212031();
            }
            case UANATACA_NAME: {
                try {
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new UanatacaSubCaCert0120162029())) {
                        System.out.println("Uanataca 2016-2029");
                        return new UanatacaSubCaCert0120162029();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new UanatacaSubCaCert0220162029())) {
                        System.out.println("Uanataca 2016-2029");
                        return new UanatacaSubCaCert0220162029();
                    }
                    return null;
                } catch (java.security.InvalidKeyException ex) {
                    //TODO
                }
            }
            case DATIL_NAME: {
                return new DatilSubCaCert20212031();
            }
            case AGOSDATA_NAME: {
                return new ArgosDataSubCaCert();
            }
            case LAZZATE_NAME: {
                try {
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new LazzateSubCaCert())) {
                        System.out.println("LazzateCA 2022-2037");
                        return new LazzateSubCaCert();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new LazzateSubCa1Cert())) {
                        System.out.println("LazzateCA1 2023-2053");
                        return new LazzateSubCa1Cert();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new LazzateSubCa2Cert())) {
                        System.out.println("LazzateCA2 2023-2053");
                        return new LazzateSubCa2Cert();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new LazzateSubCaWeGoCert())) {
                        System.out.println("LazzateCAWeGo 2023-2053");
                        return new LazzateSubCaWeGoCert();
                    }
                } catch (java.security.InvalidKeyException ex) {
                    //TODO
                }
//                return new LazzateSubCaCert();
            }
            case CORPNEWBEST_NAME: {
                try {
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new CorpNewBestSubCa1_20232033Cert())) {
                        System.out.println("CorpNewBestSubCa1Cert");
                        return new CorpNewBestSubCa1_20232033Cert();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new CorpNewBestSubCa2_20232033Cert())) {
                        System.out.println("CorpNewBestSubCa2Cert");
                        return new CorpNewBestSubCa2_20232033Cert();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new CorpNewBestSubCa3_20232033Cert())) {
                        System.out.println("CorpNewBestSubCa3Cert");
                        return new CorpNewBestSubCa3_20232033Cert();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new CorpNewBestSubCa1_2024011020330619Cert())) {
                        System.out.println("CorpNewBestSubCa1_2024011020330619Cert");
                        return new CorpNewBestSubCa1_2024011020330619Cert();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new CorpNewBestSubCa2_2024011020330619Cert())) {
                        System.out.println("CorpNewBestSubCa2_2024011020330619Cert");
                        return new CorpNewBestSubCa2_2024011020330619Cert();
                    }
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new CorpNewBestSubCa3_2024011020330619Cert())) {
                        System.out.println("CorpNewBestSubCa3_2024011020330619Cert");
                        return new CorpNewBestSubCa3_2024011020330619Cert();
                    }
                    return null;
                } catch (java.security.InvalidKeyException ex) {
                    //TODO
                }
            }
            case ALPHATECHNOLOGIES_NAME: {
                try {
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new AlphaTechnologiesSubCaCert20232026())) {
                        System.out.println("AlphaTechnologiesSubCaCert 2023-2026");
                        return new AlphaTechnologiesSubCaCert20232026();
                    }
                    return null;
                } catch (java.security.InvalidKeyException ex) {
                    //TODO
                }
            }
            case FIRMASEGURA_NAME: {
                try {
                    if (ec.gob.firmadigital.libreria.utils.Utils.verifySignature(certificado, new FirmaSeguraSubCaCert20232043())) {
                        System.out.println("FirmaSeguraSubCaCert2023-2043");
                        return new FirmaSeguraSubCaCert20232043();
                    }
                    return null;
                } catch (java.security.InvalidKeyException ex) {
                    //TODO
                }
            }

            default:
                throw new EntidadCertificadoraNoValidaException("Entidad Certificadora no reconocida");
        }
    }

    //TODO poner los nombres como constantes
    public static String getNombreCA(X509Certificate certificado) {
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains("BANCO CENTRAL DEL ECUADOR")) {
            return BCE_NAME;
        }
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains("SECURITY DATA")) {
            return SECURITYDATA_NAME;
        }
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains("CONSEJO DE LA JUDICATURA")) {
            return CJ_NAME;
        }
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains("ANF")) {
            return ANFAC_NAME;
        }
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains("DIRECCIÓN GENERAL DE REGISTRO CIVIL")) {
            return DIGERCIC_NAME;
        }
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains(UANATACA_NAME)) {
            return UANATACA_NAME;
        }
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains(DATIL_NAME)) {
            return DATIL_NAME;
        }
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains(AGOSDATA_NAME)) {
            return AGOSDATA_NAME;
        }
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains(LAZZATE_NAME)) {
            return LAZZATE_NAME;
        }
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains(ALPHATECHNOLOGIES_NAME)) {
            return ALPHATECHNOLOGIES_NAME;
        }
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains(CORPNEWBEST_NAME)) {
            return CORPNEWBEST_NAME;
        }
        if (certificado.getIssuerX500Principal().getName().toUpperCase().contains(FIRMASEGURA_NAME)) {
            return FIRMASEGURA_NAME;
        }

        return "Entidad no reconocida " + certificado.getIssuerX500Principal().getName();
    }

    //TODO poner los nombres como constantes
    public static DatosUsuario getDatosUsuarios(X509Certificate certificado) throws EntidadCertificadoraNoValidaException {
        DatosUsuario datosUsuario = new DatosUsuario();
        datosUsuario.setSelladoTiempo(false);
        if (CertificadoBancoCentralFactory.esCertificadoDelBancoCentral(certificado)) {
            CertificadoBancoCentral certificadoBancoCentral = CertificadoBancoCentralFactory.construir(certificado);
            if (certificadoBancoCentral instanceof CertificadoFuncionarioPublico) {
                CertificadoFuncionarioPublico certificadoFuncionarioPublico = (CertificadoFuncionarioPublico) certificadoBancoCentral;
                datosUsuario.setCedula(certificadoFuncionarioPublico.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoFuncionarioPublico.getNombres());
                datosUsuario.setApellido(certificadoFuncionarioPublico.getPrimerApellido() + " "
                        + certificadoFuncionarioPublico.getSegundoApellido());
                datosUsuario.setInstitucion(certificadoFuncionarioPublico.getInstitucion());
                datosUsuario.setCargo(certificadoFuncionarioPublico.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoBancoCentral instanceof CertificadoMiembroEmpresa) {
                CertificadoMiembroEmpresa certificadoMiembroEmpresa = (CertificadoMiembroEmpresa) certificadoBancoCentral;
                datosUsuario.setCedula(certificadoMiembroEmpresa.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoMiembroEmpresa.getNombres());
                datosUsuario.setApellido(certificadoMiembroEmpresa.getPrimerApellido() + " "
                        + certificadoMiembroEmpresa.getSegundoApellido());
                datosUsuario.setCargo(certificadoMiembroEmpresa.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoBancoCentral instanceof CertificadoPersonaJuridica) {
                CertificadoPersonaJuridica certificadoPersonaJuridica = (CertificadoPersonaJuridica) certificadoBancoCentral;
                datosUsuario.setCedula(certificadoPersonaJuridica.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaJuridica.getNombres());
                datosUsuario.setApellido(certificadoPersonaJuridica.getPrimerApellido() + " "
                        + certificadoPersonaJuridica.getSegundoApellido());
                datosUsuario.setCargo(certificadoPersonaJuridica.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoBancoCentral instanceof CertificadoPersonaNatural) {
                CertificadoPersonaNatural certificadoPersonaNatural = (CertificadoPersonaNatural) certificadoBancoCentral;
                datosUsuario.setCedula(certificadoPersonaNatural.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaNatural.getNombres());
                datosUsuario.setApellido(certificadoPersonaNatural.getPrimerApellido() + " "
                        + certificadoPersonaNatural.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoBancoCentral instanceof CertificadoRepresentanteLegal) {
                CertificadoRepresentanteLegal certificadoRepresentanteLegal = (CertificadoRepresentanteLegal) certificadoBancoCentral;
                datosUsuario.setCedula(certificadoRepresentanteLegal.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoRepresentanteLegal.getNombres());
                datosUsuario.setApellido(certificadoRepresentanteLegal.getPrimerApellido() + " "
                        + certificadoRepresentanteLegal.getSegundoApellido());
                datosUsuario.setCargo(certificadoRepresentanteLegal.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoBancoCentral instanceof CertificadoSelladoTiempo) {
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(BCE_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }

        if (CertificadoConsejoJudicaturaDataFactory.esCertificadoDelConsejoJudicatura(certificado)) {
            CertificadoConsejoJudicatura certificadoConsejoJudicatura = CertificadoConsejoJudicaturaDataFactory.construir(certificado);
            if (certificadoConsejoJudicatura instanceof CertificadoDepartamentoEmpresaConsejoJudicatura) {
                CertificadoDepartamentoEmpresaConsejoJudicatura certificadoDepartamentoEmpresaConsejoJudicatura;
                certificadoDepartamentoEmpresaConsejoJudicatura = (CertificadoDepartamentoEmpresaConsejoJudicatura) certificadoConsejoJudicatura;

                datosUsuario.setCedula(certificadoDepartamentoEmpresaConsejoJudicatura.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoDepartamentoEmpresaConsejoJudicatura.getNombres());
                datosUsuario.setApellido(certificadoDepartamentoEmpresaConsejoJudicatura.getPrimerApellido() + " "
                        + certificadoDepartamentoEmpresaConsejoJudicatura.getSegundoApellido());
                datosUsuario.setCargo(certificadoDepartamentoEmpresaConsejoJudicatura.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoConsejoJudicatura instanceof CertificadoEmpresaConsejoJudicatura) {
                CertificadoEmpresaConsejoJudicatura certificadoEmpresaConsejoJudicatura = (CertificadoEmpresaConsejoJudicatura) certificadoConsejoJudicatura;
                datosUsuario.setCedula(certificadoEmpresaConsejoJudicatura.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoEmpresaConsejoJudicatura.getNombres());
                datosUsuario.setApellido(certificadoEmpresaConsejoJudicatura.getPrimerApellido() + " "
                        + certificadoEmpresaConsejoJudicatura.getSegundoApellido());
                datosUsuario.setCargo(certificadoEmpresaConsejoJudicatura.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoConsejoJudicatura instanceof CertificadoMiembroEmpresaConsejoJudicatura) {
                CertificadoMiembroEmpresaConsejoJudicatura certificadoMiembroEmpresaConsejoJudicatura = (CertificadoMiembroEmpresaConsejoJudicatura) certificadoConsejoJudicatura;
                datosUsuario.setCedula(certificadoMiembroEmpresaConsejoJudicatura.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoMiembroEmpresaConsejoJudicatura.getNombres());
                datosUsuario.setApellido(certificadoMiembroEmpresaConsejoJudicatura.getPrimerApellido() + " "
                        + certificadoMiembroEmpresaConsejoJudicatura.getSegundoApellido());
                datosUsuario.setCargo(certificadoMiembroEmpresaConsejoJudicatura.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoConsejoJudicatura instanceof CertificadoPersonaJuridicaPrivadaConsejoJudicatura) {
                CertificadoPersonaJuridicaPrivadaConsejoJudicatura certificadoPersonaJuridicaPrivadaConsejoJudicatura = (CertificadoPersonaJuridicaPrivadaConsejoJudicatura) certificadoConsejoJudicatura;
                datosUsuario.setCedula(certificadoPersonaJuridicaPrivadaConsejoJudicatura.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaJuridicaPrivadaConsejoJudicatura.getNombres());
                datosUsuario.setApellido(certificadoPersonaJuridicaPrivadaConsejoJudicatura.getPrimerApellido() + " "
                        + certificadoPersonaJuridicaPrivadaConsejoJudicatura.getSegundoApellido());
                datosUsuario.setCargo(datosUsuario.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoConsejoJudicatura instanceof CertificadoPersonaJuridicaPublicaConsejoJudicatura) {
                CertificadoPersonaJuridicaPublicaConsejoJudicatura certificadoPersonaJuridicaPublicaConsejoJudicatura = (CertificadoPersonaJuridicaPublicaConsejoJudicatura) certificadoConsejoJudicatura;
                datosUsuario.setCedula(certificadoPersonaJuridicaPublicaConsejoJudicatura.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaJuridicaPublicaConsejoJudicatura.getNombres());
                datosUsuario.setApellido(certificadoPersonaJuridicaPublicaConsejoJudicatura.getPrimerApellido() + " "
                        + certificadoPersonaJuridicaPublicaConsejoJudicatura.getSegundoApellido());
                datosUsuario.setCargo(certificadoPersonaJuridicaPublicaConsejoJudicatura.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoConsejoJudicatura instanceof CertificadoPersonaNaturalConsejoJudicatura) {
                CertificadoPersonaNaturalConsejoJudicatura certificadoPersonaNaturalConsejoJudicatura = (CertificadoPersonaNaturalConsejoJudicatura) certificadoConsejoJudicatura;
                datosUsuario.setCedula(certificadoPersonaNaturalConsejoJudicatura.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaNaturalConsejoJudicatura.getNombres());
                datosUsuario.setApellido(certificadoPersonaNaturalConsejoJudicatura.getPrimerApellido() + " "
                        + certificadoPersonaNaturalConsejoJudicatura.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoConsejoJudicatura instanceof CertificadoSelladoTiempo) {
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(CJ_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }

        if (CertificadoSecurityDataFactory.esCertificadoDeSecurityData(certificado)) {
            CertificadoSecurityData certificadoSecurityData = CertificadoSecurityDataFactory.construir(certificado);
            if (certificadoSecurityData instanceof CertificadoFuncionarioPublico) {
                CertificadoFuncionarioPublico certificadoFuncionarioPublico = (CertificadoFuncionarioPublico) certificadoSecurityData;
                datosUsuario.setCedula(certificadoFuncionarioPublico.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoFuncionarioPublico.getNombres());
                datosUsuario.setApellido(certificadoFuncionarioPublico.getPrimerApellido() + " "
                        + certificadoFuncionarioPublico.getSegundoApellido());
                datosUsuario.setCargo(certificadoFuncionarioPublico.getCargo());
                datosUsuario.setInstitucion(certificadoFuncionarioPublico.getInstitucion());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoSecurityData instanceof CertificadoPersonaJuridica) {
                CertificadoPersonaJuridica certificadoPersonaJuridica = (CertificadoPersonaJuridica) certificadoSecurityData;
                datosUsuario.setCedula(certificadoPersonaJuridica.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaJuridica.getNombres());
                datosUsuario.setApellido(certificadoPersonaJuridica.getPrimerApellido() + " "
                        + certificadoPersonaJuridica.getSegundoApellido());
                datosUsuario.setCargo(certificadoPersonaJuridica.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }

            if (certificadoSecurityData instanceof CertificadoPersonaNatural) {
                CertificadoPersonaNatural certificadoPersonaNatural = (CertificadoPersonaNatural) certificadoSecurityData;
                datosUsuario.setCedula(certificadoPersonaNatural.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaNatural.getNombres());
                datosUsuario.setApellido(certificadoPersonaNatural.getPrimerApellido() + " "
                        + certificadoPersonaNatural.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoSecurityData instanceof CertificadoSelladoTiempo) {
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(SECURITYDATA_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }

        if (CertificadoAnfAc18332Factory.esCertificadoDeAnfAc18332(certificado)) {
            CertificadoAnfAc18332 certificadoAnfAc18332 = CertificadoAnfAc18332Factory.construir(certificado);
            if (certificadoAnfAc18332 instanceof CertificadoFuncionarioPublico) {
                CertificadoFuncionarioPublico certificadoFuncionarioPublico = (CertificadoFuncionarioPublico) certificadoAnfAc18332;

                datosUsuario.setCedula(certificadoFuncionarioPublico.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoFuncionarioPublico.getNombres());
                datosUsuario.setApellido(certificadoFuncionarioPublico.getPrimerApellido() + " "
                        + certificadoFuncionarioPublico.getSegundoApellido());
                datosUsuario.setCargo(certificadoFuncionarioPublico.getCargo());
                datosUsuario.setInstitucion(certificadoFuncionarioPublico.getInstitucion());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoAnfAc18332 instanceof CertificadoPersonaJuridica) {
                CertificadoPersonaJuridica certificadoPersonaJuridica = (CertificadoPersonaJuridica) certificadoAnfAc18332;
                datosUsuario.setCedula(certificadoPersonaJuridica.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaJuridica.getNombres());
                datosUsuario.setApellido(certificadoPersonaJuridica.getPrimerApellido() + " "
                        + certificadoPersonaJuridica.getSegundoApellido());
                datosUsuario.setCargo(certificadoPersonaJuridica.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }

            if (certificadoAnfAc18332 instanceof CertificadoPersonaNatural) {
                CertificadoPersonaNatural certificadoPersonaNatural = (CertificadoPersonaNatural) certificadoAnfAc18332;
                datosUsuario.setCedula(certificadoPersonaNatural.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaNatural.getNombres());
                datosUsuario.setApellido(certificadoPersonaNatural.getPrimerApellido() + " "
                        + certificadoPersonaNatural.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoAnfAc18332 instanceof CertificadoSelladoTiempo) {
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(ANFAC_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }

        if (CertificadoAnfAc37442Factory.esCertificadoDeAnfAc37442(certificado)) {
            CertificadoAnfAc37442 certificadoAnfAc37442 = CertificadoAnfAc37442Factory.construir(certificado);
            if (certificadoAnfAc37442 instanceof CertificadoFuncionarioPublico) {
                CertificadoFuncionarioPublico certificadoFuncionarioPublico = (CertificadoFuncionarioPublico) certificadoAnfAc37442;

                datosUsuario.setCedula(certificadoFuncionarioPublico.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoFuncionarioPublico.getNombres());
                datosUsuario.setApellido(certificadoFuncionarioPublico.getPrimerApellido() + " "
                        + certificadoFuncionarioPublico.getSegundoApellido());
                datosUsuario.setCargo(certificadoFuncionarioPublico.getCargo());
                datosUsuario.setInstitucion(certificadoFuncionarioPublico.getInstitucion());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoAnfAc37442 instanceof CertificadoPersonaJuridica) {
                CertificadoPersonaJuridica certificadoPersonaJuridica = (CertificadoPersonaJuridica) certificadoAnfAc37442;
                datosUsuario.setCedula(certificadoPersonaJuridica.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaJuridica.getNombres());
                datosUsuario.setApellido(certificadoPersonaJuridica.getPrimerApellido() + " "
                        + certificadoPersonaJuridica.getSegundoApellido());
                datosUsuario.setCargo(certificadoPersonaJuridica.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }

            if (certificadoAnfAc37442 instanceof CertificadoPersonaNatural) {
                CertificadoPersonaNatural certificadoPersonaNatural = (CertificadoPersonaNatural) certificadoAnfAc37442;
                datosUsuario.setCedula(certificadoPersonaNatural.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaNatural.getNombres());
                datosUsuario.setApellido(certificadoPersonaNatural.getPrimerApellido() + " "
                        + certificadoPersonaNatural.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoAnfAc37442 instanceof CertificadoSelladoTiempo) {
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(ANFAC_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }
        if (CertificadoDigercicFactory.esCertificadoDigercic(certificado)) {
            CertificadoDigercic certificadoDigercic = CertificadoDigercicFactory.construir(certificado);
            if (certificadoDigercic instanceof CertificadoPersonaNatural) {
                CertificadoPersonaNatural certificadoPersonaNatural = (CertificadoPersonaNatural) certificadoDigercic;

                datosUsuario.setCedula(certificadoPersonaNatural.getCedulaPasaporte());
                datosUsuario.setNombre(Utils.getCN(certificado));
                datosUsuario.setApellido("");
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(DIGERCIC_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }
        if (CertificadoUanatacaDataFactory.esCertificadoUanataca(certificado)) {
            CertificadoUanataca certificadoUanataca = CertificadoUanatacaDataFactory.construir(certificado);
            if (certificadoUanataca instanceof CertificadoMiembroEmpresaUanataca) {
                CertificadoMiembroEmpresaUanataca certificadoMiembroEmpresaUanataca = (CertificadoMiembroEmpresaUanataca) certificadoUanataca;
                datosUsuario.setCedula(certificadoMiembroEmpresaUanataca.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoMiembroEmpresaUanataca.getNombres());
                datosUsuario.setApellido(certificadoMiembroEmpresaUanataca.getPrimerApellido() + " "
                        + certificadoMiembroEmpresaUanataca.getSegundoApellido());
                datosUsuario.setCargo(certificadoMiembroEmpresaUanataca.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            } else if (certificadoUanataca instanceof CertificadoPersonaJuridicaPrivadaUanataca) {
                CertificadoPersonaJuridicaPrivadaUanataca certificadoPersonaJuridicaUanataca = (CertificadoPersonaJuridicaPrivadaUanataca) certificadoUanataca;
                datosUsuario.setCedula(certificadoPersonaJuridicaUanataca.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaJuridicaUanataca.getNombres());
                datosUsuario.setApellido(certificadoPersonaJuridicaUanataca.getPrimerApellido() + " "
                        + certificadoPersonaJuridicaUanataca.getSegundoApellido());
                datosUsuario.setCargo(datosUsuario.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            } else if (certificadoUanataca instanceof CertificadoPersonaNaturalUanataca) {
                CertificadoPersonaNaturalUanataca certificadoPersonaNaturalU = (CertificadoPersonaNaturalUanataca) certificadoUanataca;
                datosUsuario.setCedula(certificadoPersonaNaturalU.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaNaturalU.getNombres());
                datosUsuario.setApellido(certificadoPersonaNaturalU.getPrimerApellido() + " "
                        + certificadoPersonaNaturalU.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            } else if (certificadoUanataca instanceof CertificadoRepresentanteLegalUanataca) {
                CertificadoRepresentanteLegalUanataca certificadoRepresentanteLegalUanataca = (CertificadoRepresentanteLegalUanataca) certificadoUanataca;
                datosUsuario.setCedula(certificadoRepresentanteLegalUanataca.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoRepresentanteLegalUanataca.getNombres());
                datosUsuario.setApellido(certificadoRepresentanteLegalUanataca.getPrimerApellido() + " "
                        + certificadoRepresentanteLegalUanataca.getSegundoApellido());
                datosUsuario.setCargo(certificadoRepresentanteLegalUanataca.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            } else if (certificadoUanataca instanceof CertificadoSelladoTiempo) {
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
                datosUsuario.setSelladoTiempo(true);
            }
            datosUsuario.setEntidadCertificadora(UANATACA_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }
        if (CertificadoEclipsoftDataFactory.esCertificadoEclipsoft(certificado)) {
            CertificadoEclipsoft certificadoEclipsoft = CertificadoEclipsoftDataFactory.construir(certificado);
            if (certificadoEclipsoft instanceof CertificadoPersonalNaturalEclipsoft) {
                CertificadoPersonalNaturalEclipsoft certificadoPersonalNaturalEclipsoft = (CertificadoPersonalNaturalEclipsoft) certificadoEclipsoft;
                datosUsuario.setCedula(certificadoPersonalNaturalEclipsoft.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonalNaturalEclipsoft.getNombres());
                datosUsuario.setApellido(certificadoPersonalNaturalEclipsoft.getPrimerApellido() + " " + certificadoPersonalNaturalEclipsoft.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            } else if (certificadoEclipsoft instanceof CertificadoMiembroEmpresaEclipsoft) {
                CertificadoMiembroEmpresaEclipsoft certificadoMiembroEmpresaEclipsoft = (CertificadoMiembroEmpresaEclipsoft) certificadoEclipsoft;
                datosUsuario.setCedula(certificadoMiembroEmpresaEclipsoft.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoMiembroEmpresaEclipsoft.getNombres());
                datosUsuario.setApellido(certificadoMiembroEmpresaEclipsoft.getPrimerApellido() + " " + certificadoMiembroEmpresaEclipsoft.getSegundoApellido());
                datosUsuario.setCargo(certificadoMiembroEmpresaEclipsoft.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            } else if (certificadoEclipsoft instanceof CertificadoRepresentanteLegalEclipsoft) {
                CertificadoRepresentanteLegalEclipsoft certificadoRepresentanteLegalEclipsoft = (CertificadoRepresentanteLegalEclipsoft) certificadoEclipsoft;
                datosUsuario.setCedula(certificadoRepresentanteLegalEclipsoft.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoRepresentanteLegalEclipsoft.getNombres());
                datosUsuario.setApellido(certificadoRepresentanteLegalEclipsoft.getPrimerApellido() + " " + certificadoRepresentanteLegalEclipsoft.getSegundoApellido());
                datosUsuario.setCargo(certificadoRepresentanteLegalEclipsoft.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            } else if (certificadoEclipsoft instanceof CertificadoPersonaJuridicaPrivadaEclipsoft) {
                CertificadoPersonaJuridicaPrivadaEclipsoft certificadoPersonaJuridicaPrivadaEclipsoft = (CertificadoPersonaJuridicaPrivadaEclipsoft) certificadoEclipsoft;
                datosUsuario.setCedula(certificadoPersonaJuridicaPrivadaEclipsoft.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaJuridicaPrivadaEclipsoft.getNombres());
                datosUsuario.setApellido(certificadoPersonaJuridicaPrivadaEclipsoft.getPrimerApellido() + " " + certificadoPersonaJuridicaPrivadaEclipsoft.getSegundoApellido());
                datosUsuario.setCargo(datosUsuario.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            } else if (certificadoEclipsoft instanceof CertificadoSelladoTiempo) {
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(ECLIPSOFT_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }
        if (CertificadoDatilDataFactory.esCertificadoDatil(certificado)) {
            CertificadoDatil certificadoDatil = CertificadoDatilDataFactory.construir(certificado);
            if (certificadoDatil instanceof CertificadoMiembroEmpresaDatil) {
                CertificadoMiembroEmpresaDatil certificadoMiembroEmpresaDatil = (CertificadoMiembroEmpresaDatil) certificadoDatil;
                datosUsuario.setCedula(certificadoMiembroEmpresaDatil.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoMiembroEmpresaDatil.getNombres());
                datosUsuario.setApellido(certificadoMiembroEmpresaDatil.getPrimerApellido() + " " + certificadoMiembroEmpresaDatil.getSegundoApellido());
                datosUsuario.setCargo(certificadoMiembroEmpresaDatil.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoDatil instanceof CertificadoPersonaJuridicaPrivadaDatil) {
                CertificadoPersonaJuridicaPrivadaDatil certificadoPersonaJuridicaPrivadaDatil = (CertificadoPersonaJuridicaPrivadaDatil) certificadoDatil;
                datosUsuario.setCedula(certificadoPersonaJuridicaPrivadaDatil.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaJuridicaPrivadaDatil.getNombres());
                datosUsuario.setApellido(certificadoPersonaJuridicaPrivadaDatil.getPrimerApellido() + " "
                        + certificadoPersonaJuridicaPrivadaDatil.getSegundoApellido());
                datosUsuario.setCargo(certificadoPersonaJuridicaPrivadaDatil.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoDatil instanceof CertificadoRepresentanteLegalDatil) {
                CertificadoRepresentanteLegalDatil certificadoRepresentanteLegalDatil = (CertificadoRepresentanteLegalDatil) certificadoDatil;
                datosUsuario.setCedula(certificadoRepresentanteLegalDatil.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoRepresentanteLegalDatil.getNombres());
                datosUsuario.setApellido(certificadoRepresentanteLegalDatil.getPrimerApellido() + " "
                        + certificadoRepresentanteLegalDatil.getSegundoApellido());
                datosUsuario.setCargo(certificadoRepresentanteLegalDatil.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoDatil instanceof CertificadoPersonaNaturalDatil) {
                CertificadoPersonaNaturalDatil certificadoPersonaNaturalDatil = (CertificadoPersonaNaturalDatil) certificadoDatil;
                datosUsuario.setCedula(certificadoPersonaNaturalDatil.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaNaturalDatil.getNombres());
                datosUsuario.setApellido(certificadoPersonaNaturalDatil.getPrimerApellido() + " "
                        + certificadoPersonaNaturalDatil.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoDatil instanceof CertificadoSelladoTiempo) {
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(DATIL_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }
        if (CertificadoArgosDataFactory.esCertificadoArgosData(certificado)) {
            CertificadoArgosData certificadoArgosData = CertificadoArgosDataFactory.construir(certificado);
            if (certificadoArgosData instanceof CertificadoPersonaNaturalArgosData) {
                CertificadoPersonaNaturalArgosData certificadoPersonaNatural = (CertificadoPersonaNaturalArgosData) certificadoArgosData;
                datosUsuario.setCedula(certificadoPersonaNatural.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaNatural.getNombres());
                datosUsuario.setApellido(certificadoPersonaNatural.getPrimerApellido() + " "
                        + certificadoPersonaNatural.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            } else if (certificadoArgosData instanceof CertificadoRepresentanteLegalArgosData) {
                CertificadoRepresentanteLegalArgosData certificadoRepresentanteLegal = (CertificadoRepresentanteLegalArgosData) certificadoArgosData;
                datosUsuario.setCedula(certificadoRepresentanteLegal.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoRepresentanteLegal.getNombres());
                datosUsuario.setApellido(certificadoRepresentanteLegal.getPrimerApellido() + " "
                        + certificadoRepresentanteLegal.getSegundoApellido());
                datosUsuario.setCargo(certificadoRepresentanteLegal.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(AGOSDATA_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }
        if (CertificadoLazzateDataFactory.esCertificadoLazzate(certificado)) {
            CertificadoLazzate certificadoLazzate = CertificadoLazzateDataFactory.construir(certificado);
            if (certificadoLazzate instanceof CertificadoPersonaNatural) {
                CertificadoPersonaNatural certificadoPersonaNatural = (CertificadoPersonaNatural) certificadoLazzate;

                datosUsuario.setCedula(certificadoPersonaNatural.getCedulaPasaporte());
                if (certificadoPersonaNatural.getNombres().isEmpty()) {
                    datosUsuario.setNombre(Utils.getCN(certificado));
                    datosUsuario.setApellido("");
                } else {
                    datosUsuario.setNombre(certificadoPersonaNatural.getNombres());
                    datosUsuario.setApellido(certificadoPersonaNatural.getPrimerApellido() + " "
                            + certificadoPersonaNatural.getSegundoApellido());
                }
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoLazzate instanceof CertificadoPersonaJuridica) {
                CertificadoPersonaJuridica certificadoPersonaJuridica = (CertificadoPersonaJuridica) certificadoLazzate;

                datosUsuario.setCedula(certificadoPersonaJuridica.getCedulaPasaporte());
                datosUsuario.setInstitucion(certificadoPersonaJuridica.getRazonSocial());
                datosUsuario.setCargo(certificadoPersonaJuridica.getCargo());
                if (certificadoPersonaJuridica.getNombres().isEmpty()) {
                    datosUsuario.setNombre(Utils.getCN(certificado));
                    datosUsuario.setApellido("");
                } else {
                    datosUsuario.setNombre(certificadoPersonaJuridica.getNombres());
                    datosUsuario.setApellido(certificadoPersonaJuridica.getPrimerApellido() + " "
                            + certificadoPersonaJuridica.getSegundoApellido());
                }
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(LAZZATE_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }
        if (CertificadoAlphaTechnologiesFactory.esCertificadoDeAlphaTechnologies(certificado)) {
            CertificadoAlphaTechnologies certificadoAlphaTechnologies = CertificadoAlphaTechnologiesFactory.construir(certificado);
            if (certificadoAlphaTechnologies instanceof CertificadoMiembroEmpresa) {
                CertificadoMiembroEmpresa certificadoMiembroEmpresa = (CertificadoMiembroEmpresa) certificadoAlphaTechnologies;
                datosUsuario.setCedula(certificadoMiembroEmpresa.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoMiembroEmpresa.getNombres());
                datosUsuario.setApellido(certificadoMiembroEmpresa.getPrimerApellido() + " "
                        + certificadoMiembroEmpresa.getSegundoApellido());
                datosUsuario.setCargo(certificadoMiembroEmpresa.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoAlphaTechnologies instanceof CertificadoPersonaJuridica) {
                CertificadoPersonaJuridica certificadoPersonaJuridica = (CertificadoPersonaJuridica) certificadoAlphaTechnologies;
                datosUsuario.setCedula(certificadoPersonaJuridica.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaJuridica.getNombres());
                datosUsuario.setApellido(certificadoPersonaJuridica.getPrimerApellido() + " "
                        + certificadoPersonaJuridica.getSegundoApellido());
                datosUsuario.setCargo(certificadoPersonaJuridica.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoAlphaTechnologies instanceof CertificadoPersonaNatural) {
                CertificadoPersonaNatural certificadoPersonaNatural = (CertificadoPersonaNatural) certificadoAlphaTechnologies;
                datosUsuario.setCedula(certificadoPersonaNatural.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaNatural.getNombres());
                datosUsuario.setApellido(certificadoPersonaNatural.getPrimerApellido() + " "
                        + certificadoPersonaNatural.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(ALPHATECHNOLOGIES_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }
        if (CertificadoCorpNewBestDataFactory.esCertificadoCorpNewBest(certificado)) {
            CertificadoCorpNewBest certificadoCorpNewBest = CertificadoCorpNewBestDataFactory.construir(certificado);
            if (certificadoCorpNewBest instanceof CertificadoPersonaJuridica) {
                CertificadoPersonaJuridica certificadoPersonaJuridica = (CertificadoPersonaJuridica) certificadoCorpNewBest;
                datosUsuario.setCedula(certificadoPersonaJuridica.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaJuridica.getNombres());
                datosUsuario.setApellido(certificadoPersonaJuridica.getPrimerApellido() + " "
                        + certificadoPersonaJuridica.getSegundoApellido());
                datosUsuario.setCargo(certificadoPersonaJuridica.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoCorpNewBest instanceof CertificadoMiembroEmpresa) {
                CertificadoMiembroEmpresa certificadoMiembroEmpresa = (CertificadoMiembroEmpresa) certificadoCorpNewBest;
                datosUsuario.setCedula(certificadoMiembroEmpresa.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoMiembroEmpresa.getNombres());
                datosUsuario.setApellido(certificadoMiembroEmpresa.getPrimerApellido() + " "
                        + certificadoMiembroEmpresa.getSegundoApellido());
                datosUsuario.setCargo(certificadoMiembroEmpresa.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoCorpNewBest instanceof CertificadoPersonaNatural) {
                CertificadoPersonaNatural certificadoPersonaNatural = (CertificadoPersonaNatural) certificadoCorpNewBest;
                datosUsuario.setCedula(certificadoPersonaNatural.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaNatural.getNombres());
                datosUsuario.setApellido(certificadoPersonaNatural.getPrimerApellido() + " "
                        + certificadoPersonaNatural.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            if (certificadoCorpNewBest instanceof CertificadoSelladoTiempo) {
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(CORPNEWBEST_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }
        if (CertificadoFirmaSeguraFactory.esCertificadoDeFirmaSegura(certificado)) {
            CertificadoFirmaSegura certificadoFirmaSegura = CertificadoFirmaSeguraFactory.construir(certificado);
            if (certificadoFirmaSegura instanceof CertificadoPersonaNatural) {
                CertificadoPersonaNatural certificadoPersonaNatural = (CertificadoPersonaNatural) certificadoFirmaSegura;
                datosUsuario.setCedula(certificadoPersonaNatural.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoPersonaNatural.getNombres());
                datosUsuario.setApellido(certificadoPersonaNatural.getPrimerApellido() + " "
                        + certificadoPersonaNatural.getSegundoApellido());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            } else if (certificadoFirmaSegura instanceof CertificadoRepresentanteLegalFirmaSegura) {
                CertificadoRepresentanteLegalFirmaSegura certificadoRepresentanteLegal = (CertificadoRepresentanteLegalFirmaSegura) certificadoFirmaSegura;
                datosUsuario.setCedula(certificadoRepresentanteLegal.getCedulaPasaporte());
                datosUsuario.setNombre(certificadoRepresentanteLegal.getNombres());
                datosUsuario.setApellido(certificadoRepresentanteLegal.getPrimerApellido() + " "
                        + certificadoRepresentanteLegal.getSegundoApellido());
                datosUsuario.setCargo(certificadoRepresentanteLegal.getCargo());
                datosUsuario.setSerial(certificado.getSerialNumber().toString());
            }
            datosUsuario.setEntidadCertificadora(FIRMASEGURA_NAME);
            datosUsuario.setCertificadoDigitalValido(true);
            return datosUsuario;
        }
        return null;
    }
}
