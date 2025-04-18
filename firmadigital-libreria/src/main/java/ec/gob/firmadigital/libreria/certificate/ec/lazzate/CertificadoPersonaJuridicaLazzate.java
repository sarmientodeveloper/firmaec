/*
 * Copyright (C) 2024
 * Authors: Henry Carrera
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
package ec.gob.firmadigital.libreria.certificate.ec.lazzate;

import java.security.cert.X509Certificate;

import ec.gob.firmadigital.libreria.exceptions.DatoCertificadoNoIncluidoException;
import ec.gob.firmadigital.libreria.certificate.ec.CertificadoPersonaJuridica;

/**
 * Certificado de persona jurídica emitido por el Lazzate.
 *
 * @author Henry Carrera <henry@hyrserv.com>
 */
public class CertificadoPersonaJuridicaLazzate extends CertificadoLazzate
        implements CertificadoPersonaJuridica {

    public CertificadoPersonaJuridicaLazzate(X509Certificate certificado) {
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
        throw new DatoCertificadoNoIncluidoException(
                "Los certificados de Persona Juridica de Security Data no incluyen cedula o pasaporte");
    }

    @Override
    public String getNombres() {
        throw new DatoCertificadoNoIncluidoException(
                "Los certificados de Persona Juridica de Security Data no incluyen nombre(s)");
    }

    @Override
    public String getPrimerApellido() {
        throw new DatoCertificadoNoIncluidoException(
                "Los certificados de Persona Juridica de Security Data no incluyen primer apellido");
    }

    @Override
    public String getSegundoApellido() {
        throw new DatoCertificadoNoIncluidoException(
                "Los certificados de Persona Juridica de Security Data no incluyen segundo apellido");
    }

    @Override
    public String getCargo() {
        throw new DatoCertificadoNoIncluidoException(
                "Los certificados de Persona Juridica de Security Data no incluyen cargo");
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
