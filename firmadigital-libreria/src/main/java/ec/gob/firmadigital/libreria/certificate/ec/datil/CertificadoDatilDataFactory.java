/*
 * Copyright (C) 2021 
 * Authors: Eduardo Raad
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
package ec.gob.firmadigital.libreria.certificate.ec.datil;

import static ec.gob.firmadigital.libreria.certificate.ec.datil.CertificadoDatil.OID_CERTIFICADO_MIEMBRO_EMPRESA;
import static ec.gob.firmadigital.libreria.certificate.ec.datil.CertificadoDatil.OID_CERTIFICADO_PERSONA_JURIDICA;
import static ec.gob.firmadigital.libreria.certificate.ec.datil.CertificadoDatil.OID_CERTIFICADO_PERSONA_NATURAL;
import static ec.gob.firmadigital.libreria.certificate.ec.datil.CertificadoDatil.OID_CERTIFICADO_REPRESENTANTE_EMPRESA;
import ec.gob.firmadigital.libreria.exceptions.EntidadCertificadoraNoValidaException;
import static ec.gob.firmadigital.libreria.utils.BouncyCastleUtils.certificateHasPolicy;

import java.security.cert.X509Certificate;

/**
 * Permite construir certificados tipo CertificadoDatil a partir de
 * certificados X509Certificate.
 *
 * @author Eduardo Raad
 */
public class CertificadoDatilDataFactory {

    public static boolean esCertificadoDatil(X509Certificate certificado) {
        return (certificateHasPolicy(certificado, OID_CERTIFICADO_PERSONA_NATURAL)
                || certificateHasPolicy(certificado, OID_CERTIFICADO_PERSONA_JURIDICA)
                || certificateHasPolicy(certificado, OID_CERTIFICADO_MIEMBRO_EMPRESA)
                || certificateHasPolicy(certificado, OID_CERTIFICADO_REPRESENTANTE_EMPRESA));
    }

    public static CertificadoDatil construir(X509Certificate certificado) throws EntidadCertificadoraNoValidaException {
        if (certificateHasPolicy(certificado, OID_CERTIFICADO_PERSONA_NATURAL)) {
            return new CertificadoPersonaNaturalDatil(certificado);
        } else if (certificateHasPolicy(certificado, OID_CERTIFICADO_PERSONA_JURIDICA)) {
            return new CertificadoPersonaJuridicaPrivadaDatil(certificado);
        } else if (certificateHasPolicy(certificado, OID_CERTIFICADO_MIEMBRO_EMPRESA)) {
            return new CertificadoMiembroEmpresaDatil(certificado);
        } else if (certificateHasPolicy(certificado, OID_CERTIFICADO_REPRESENTANTE_EMPRESA)) {
            return new CertificadoRepresentanteLegalDatil(certificado);
        } else {
            throw new EntidadCertificadoraNoValidaException("Certificado del Datilmedia S.A. de tipo desconocido!");
        }
    }

}
