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
package ec.gob.firmadigital.libreria.certificate.ec.securitydata;

import java.security.cert.X509Certificate;

/**
 * Certificado de Prueba emitido por Security Data.
 *
 * @author Ricardo Arguello <ricardo.arguello@soportelibre.com>
 */
public class CertificadoPruebaSecurityData extends CertificadoSecurityData {

    public CertificadoPruebaSecurityData(X509Certificate certificado) {
        super(certificado);
    }

    public String getCedulaPasaporte() {
        return obtenerExtension(OID_CEDULA_PASAPORTE);
    }

    @Override
    public String getNombres() {
        return obtenerExtension(OID_NOMBRES);
    }

    @Override
    public String getPrimerApellido() {
        return obtenerExtension(OID_PRIMER_APELLIDO);
    }

    @Override
    public String getSegundoApellido() {
        return obtenerExtension(OID_SEGUNDO_APELLIDO);
    }

    public String getDireccion() {
        return obtenerExtension(OID_DIRECCION);
    }

    public String getTelefono() {
        return obtenerExtension(OID_TELEFONO);
    }

    public String getCiudad() {
        return obtenerExtension(OID_CIUDAD);
    }

    public String getPais() {
        return obtenerExtension(OID_PAIS);
    }

    public String getRuc() {
        return obtenerExtension(OID_RUC);
    }

    public String getRup() {
        return obtenerExtension(OID_RUP);
    }
}
