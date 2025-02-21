/*
 * Copyright (C) 2024 
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
package ec.gob.firmadigital.libreria.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;

import ec.gob.firmadigital.libreria.certificate.to.Certificado;
import ec.gob.firmadigital.libreria.certificate.to.DatosUsuario;
import ec.gob.firmadigital.libreria.certificate.to.Documento;

/**
 *
 * @author mfernandez
 */
public class Json {

    private static final SimpleDateFormat simpleDateFormatISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public static String generarJsonVersion(String sistemaOperativo, String aplicacion, String versionApp, String sha) {
        if (sistemaOperativo != null && versionApp != null && sha != null) {
            JsonObject gsonObject = new JsonObject();
            gsonObject.addProperty("sistemaOperativo", sistemaOperativo);
            gsonObject.addProperty("aplicacion", aplicacion);
            gsonObject.addProperty("versionApp", versionApp);
            gsonObject.addProperty("sha", sha);
            return gsonObject.toString();
        } else {
            return null;
        }
    }

    public static String generarJsonDocumento(Documento documento) {
        return generarJsonDocumentoFirmado(null, documento);
    }

    public static String generarJsonDocumentoFirmado(byte[] byteDocumentoSigned, Documento documento) {
        //creacion del JSON
        JsonArray gsonArray = new JsonArray();
        JsonObject jsonObjectDocumento = null;
        jsonObjectDocumento = new JsonObject();
        jsonObjectDocumento.addProperty("signValidate", documento.getSignValidate());
        jsonObjectDocumento.addProperty("docValidate", documento.getDocValidate());
        if (byteDocumentoSigned != null) {
            jsonObjectDocumento.addProperty("docSigned", java.util.Base64.getEncoder().encodeToString(byteDocumentoSigned));
        }
        jsonObjectDocumento.addProperty("error", documento.getError());

        //Arreglo de Certificado(s)
        JsonArray jsonDocumentoArray = new JsonArray();
        JsonObject jsonObjectCertificado = null;
        for (Certificado certificado : documento.getCertificados()) {
            jsonObjectCertificado = new JsonObject();
            jsonObjectCertificado.addProperty("issuedTo", certificado.getIssuedTo());
            jsonObjectCertificado.addProperty("issuedBy", certificado.getIssuedBy());
            jsonObjectCertificado.addProperty("validFrom", simpleDateFormatISO8601.format(certificado.getValidFrom().getTime()));
            jsonObjectCertificado.addProperty("validTo", simpleDateFormatISO8601.format(certificado.getValidTo().getTime()));
            jsonObjectCertificado.addProperty("generated", simpleDateFormatISO8601.format(certificado.getGenerated().getTime()));
            if (certificado.getRevocated() != null) {
                jsonObjectCertificado.addProperty("revocated", simpleDateFormatISO8601.format(certificado.getRevocated().getTime()));
            }
            jsonObjectCertificado.addProperty("validated", certificado.getValidated());
            jsonObjectCertificado.addProperty("keyUsages", certificado.getKeyUsages());
            if (certificado.getDocTimeStamp() != null) {
                jsonObjectCertificado.addProperty("docTimeStamp", simpleDateFormatISO8601.format(certificado.getDocTimeStamp()));
            }
            jsonObjectCertificado.addProperty("signVerify", certificado.getSignVerify());
            jsonObjectCertificado.addProperty("docReason", certificado.getDocReason());
            jsonObjectCertificado.addProperty("docLocation", certificado.getDocLocation());

            String json = generarJsonDatosUsuario(certificado.getDatosUsuario());
            JsonObject jsonObjectDatosUsuario = new Gson().fromJson(json, JsonObject.class);
            jsonObjectCertificado.add("datosUsuario", jsonObjectDatosUsuario);

            jsonDocumentoArray.add(jsonObjectCertificado);
        }
        jsonObjectDocumento.add("certificado", new JsonParser()
                .parse(new Gson().toJson(jsonDocumentoArray)).getAsJsonArray());
        gsonArray.add(jsonObjectDocumento);
        return gsonArray.toString();
    }

    public static String generarJsonDocumentoFirmadoTransversal(byte[] byteDocumentoSigned, Documento documento) {
        //creacion del JSON
        JsonArray gsonArray = new JsonArray();
        JsonObject jsonObjectDocumento = null;
        jsonObjectDocumento = new JsonObject();
        jsonObjectDocumento.addProperty("validarFirma", documento.getSignValidate());
        jsonObjectDocumento.addProperty("validarDocumento", documento.getDocValidate());
        if (byteDocumentoSigned != null) {
            jsonObjectDocumento.addProperty("documentoFirmado", java.util.Base64.getEncoder().encodeToString(byteDocumentoSigned));
        }
        jsonObjectDocumento.addProperty("error", documento.getError());

        //Arreglo de Certificado(s)
        JsonArray jsonDocumentoArray = new JsonArray();
        JsonObject jsonObjectCertificado = null;
        for (Certificado certificado : documento.getCertificados()) {
            jsonObjectCertificado = new JsonObject();
            jsonObjectCertificado.addProperty("cedula", certificado.getDatosUsuario().getCedula());
            jsonObjectCertificado.addProperty("nombresApeilldos", certificado.getIssuedTo());
            jsonObjectCertificado.addProperty("emitidoPor", certificado.getIssuedBy());
            jsonObjectCertificado.addProperty("validoDesde", simpleDateFormatISO8601.format(certificado.getValidFrom().getTime()));
            jsonObjectCertificado.addProperty("validoHasta", simpleDateFormatISO8601.format(certificado.getValidTo().getTime()));
            jsonObjectCertificado.addProperty("fechaRevocado", certificado.getRevocated() != null ? simpleDateFormatISO8601.format(certificado.getRevocated().getTime()):null);
            jsonObjectCertificado.addProperty("certificadoDigitalValido", certificado.getDatosUsuario().isCertificadoDigitalValido());
            jsonObjectCertificado.addProperty("fechaDocumentoFirmado", simpleDateFormatISO8601.format(certificado.getGenerated().getTime()));
            jsonObjectCertificado.addProperty("razon", certificado.getDocReason());
            jsonObjectCertificado.addProperty("localizacion", certificado.getDocLocation());
            jsonObjectCertificado.addProperty("selladoTiempo", certificado.getDatosUsuario().getSelladoTiempo());

            jsonDocumentoArray.add(jsonObjectCertificado);
        }
        jsonObjectDocumento.add("certificado", new JsonParser()
                .parse(new Gson().toJson(jsonDocumentoArray)).getAsJsonArray());
        gsonArray.add(jsonObjectDocumento);
        return gsonArray.toString();
    }

    public static String generarJsonCertificado(Certificado certificado) {
        //creacion del JSON
        JsonArray gsonArray = new JsonArray();
        JsonObject jsonObjectCertificado = null;
        jsonObjectCertificado = new JsonObject();
        jsonObjectCertificado.addProperty("issuedTo", certificado.getIssuedTo());
        jsonObjectCertificado.addProperty("issuedBy", certificado.getIssuedBy());
        jsonObjectCertificado.addProperty("validFrom", simpleDateFormatISO8601.format(certificado.getValidFrom().getTime()));
        jsonObjectCertificado.addProperty("validTo", simpleDateFormatISO8601.format(certificado.getValidTo().getTime()));
        if (certificado.getGenerated() != null) {
            jsonObjectCertificado.addProperty("generated", simpleDateFormatISO8601.format(certificado.getGenerated().getTime()));
        }
        if (certificado.getRevocated() != null) {
            jsonObjectCertificado.addProperty("revocated", simpleDateFormatISO8601.format(certificado.getRevocated().getTime()));
        }
        jsonObjectCertificado.addProperty("validated", certificado.getValidated());
        jsonObjectCertificado.addProperty("keyUsages", certificado.getKeyUsages());
        if (certificado.getDocTimeStamp() != null) {
            jsonObjectCertificado.addProperty("docTimeStamp", simpleDateFormatISO8601.format(certificado.getDocTimeStamp()));
        }
        jsonObjectCertificado.addProperty("signVerify", certificado.getSignVerify());
        jsonObjectCertificado.addProperty("docReason", certificado.getDocReason());
        jsonObjectCertificado.addProperty("docLocation", certificado.getDocLocation());

        String json = null;
        if (certificado.getDatosUsuario() != null) {
            json = generarJsonDatosUsuario(certificado.getDatosUsuario());
        }
        JsonObject jsonObjectDatosUsuario = new Gson().fromJson(json, JsonObject.class);
        jsonObjectCertificado.add("datosUsuario", jsonObjectDatosUsuario);

        gsonArray.add(jsonObjectCertificado);
        return gsonArray.toString();
    }

    public static String generarJsonCertificadoTransversal(Certificado certificado) {
        //creacion del JSON
        JsonArray gsonArray = new JsonArray();
        JsonObject jsonObjectCertificado = null;
        if (certificado.getDatosUsuario() != null) {
            jsonObjectCertificado = new JsonObject();
            jsonObjectCertificado.addProperty("cedula", certificado.getDatosUsuario().getCedula());
            jsonObjectCertificado.addProperty("nombresApeilldos", certificado.getIssuedTo());
            jsonObjectCertificado.addProperty("emitidoPor", certificado.getIssuedBy());
            jsonObjectCertificado.addProperty("validoDesde", simpleDateFormatISO8601.format(certificado.getValidFrom().getTime()));
            jsonObjectCertificado.addProperty("validoHasta", simpleDateFormatISO8601.format(certificado.getValidTo().getTime()));
            jsonObjectCertificado.addProperty("fechaRevocado", certificado.getRevocated() != null ? simpleDateFormatISO8601.format(certificado.getRevocated().getTime()):null);
        }
        gsonArray.add(jsonObjectCertificado);
        return gsonArray.toString();
    }

    public static String generarJsonDatosUsuario(DatosUsuario datosUsuario) {
        JsonObject jsonObjectDatosUsuario = null;
        jsonObjectDatosUsuario = new JsonObject();
        jsonObjectDatosUsuario.addProperty("cedula", datosUsuario.getCedula());
        jsonObjectDatosUsuario.addProperty("nombre", datosUsuario.getNombre());
        jsonObjectDatosUsuario.addProperty("apellido", datosUsuario.getApellido());
        jsonObjectDatosUsuario.addProperty("institucion", datosUsuario.getInstitucion());
        jsonObjectDatosUsuario.addProperty("cargo", datosUsuario.getCargo());
        jsonObjectDatosUsuario.addProperty("serial", datosUsuario.getSerial());
        if (datosUsuario.getFechaFirmaArchivo() != null) {
            jsonObjectDatosUsuario.addProperty("fechaFirmaArchivo", datosUsuario.getFechaFirmaArchivo());
        }
        jsonObjectDatosUsuario.addProperty("entidadCertificadora", datosUsuario.getEntidadCertificadora());
        jsonObjectDatosUsuario.addProperty("selladoTiempo", datosUsuario.getSelladoTiempo());
        jsonObjectDatosUsuario.addProperty("certificadoDigitalValido", datosUsuario.isCertificadoDigitalValido());
        return jsonObjectDatosUsuario.toString();
    }

}
