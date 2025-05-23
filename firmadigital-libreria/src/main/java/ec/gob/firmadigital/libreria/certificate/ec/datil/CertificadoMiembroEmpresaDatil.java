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

import java.security.cert.X509Certificate;

import ec.gob.firmadigital.libreria.certificate.ec.CertificadoPersonaJuridica;

/**
 * Certificado de Miembro de Empresa
 *
 * @author Eduardo Raad
 */
public class CertificadoMiembroEmpresaDatil extends CertificadoDatil implements CertificadoPersonaJuridica {

    public CertificadoMiembroEmpresaDatil(X509Certificate certificado) {
        super(certificado);
    }

    @Override
    public String getRazonSocial() {
        return obtenerExtension(OID_RAZON_SOCIAL);
    }

    @Override
    public String getRuc() {
        return obtenerExtension(OID_RUC);
    }

    @Override
    public String getCedulaPasaporte() {
        return obtenerExtension(OID_CEDULA_PASAPORTE);
    }

    @Override
    public String getNombres() {
        return obtenerExtension(OID_NOMBRES);
    }

    @Override
    public String getPrimerApellido() {
        return obtenerExtension(OID_APELLIDO_1);
    }

    @Override
    public String getSegundoApellido() {
        return obtenerExtension(OID_APELLIDO_2);
    }

    @Override
    public String getCargo() {
        return obtenerExtension(OID_CARGO);
    }

    @Override
    public String getDireccion() {
        return obtenerExtension(OID_DIRECCION);
    }

    @Override
    public String getTelefono() {
        return obtenerExtension(OID_TELEFONO);
    }

    @Override
    public String getCiudad() {
        return obtenerExtension(OID_CIUDAD);
    }

    @Override
    public String getPais() {
        return obtenerExtension(OID_PAIS);
    }
}
