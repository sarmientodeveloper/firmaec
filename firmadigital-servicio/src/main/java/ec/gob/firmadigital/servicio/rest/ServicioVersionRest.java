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
package ec.gob.firmadigital.servicio.rest;

import ec.gob.firmadigital.servicio.ServicioVersion;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ec.gob.firmadigital.servicio.exception.ServicioVersionException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.stream.JsonParsingException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;

/**
 * Servicio REST para verificar Versión.
 *
 * @author Christian Espinosa <christian.espinosa@mintel.gob.ec>, Misael
 * Fernández
 */
@Stateless
@Path("/version")
public class ServicioVersionRest {

    @EJB
    private ServicioVersion servicioVersion;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String buscarVersion(@FormParam("base64") String base64) {
        if (base64 == null || base64.isEmpty()) {
            return "Se debe generar en Base64";
        }
//        logger.info("base64=" + base64);
        String jsonParameter;
        try {
            jsonParameter = new String(Base64.getDecoder().decode(base64));
        } catch (IllegalArgumentException e) {
            return getClass().getSimpleName() + "::Error al decodificar base64: \"" + e.getMessage();
        }

        if (jsonParameter == null || jsonParameter.isEmpty()) {
            return "Se debe incluir JSON con los parámetros: sistemaOperativo, aplicacion,versionApp y sha";
        }

        jakarta.json.JsonObject json;
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(URLDecoder.decode(jsonParameter, "UTF-8")));
            json = (jakarta.json.JsonObject) jsonReader.read();
        } catch (JsonParsingException | UnsupportedEncodingException e) {
            return getClass().getSimpleName() + "::Error al decodificar JSON: " + e.getMessage();
        }

        String sistemaOperativo;
        String aplicacion;
        String versionApp;
        String sha;




        try {
            sistemaOperativo = json.getString("sistemaOperativo");
			System.out.println("sistema operativo"+sistemaOperativo);
        } catch (NullPointerException e) {
            return getClass().getSimpleName() + "::Error al decodificar JSON: Se debe incluir \"sistemaOperativo\"";
        }
        try {
            aplicacion = json.getString("aplicacion");
			System.out.println("aplicacion"+aplicacion);
        } catch (NullPointerException e) {
            return getClass().getSimpleName() + "::Error al decodificar JSON: Se debe incluir \"aplicacion\"";
        }
        try {
            versionApp = json.getString("versionApp");
			System.out.println("version"+versionApp);
        } catch (NullPointerException e) {
            return getClass().getSimpleName() + "::Error al decodificar JSON: Se debe incluir \"versionApp\"";
        }
        try {
            sha = json.getString("sha");
			System.out.println("el sha es "+sha);
        } catch (NullPointerException e) {
            return getClass().getSimpleName() + "::Error al decodificar JSON: Se debe incluir \"sha\"";
        }

        try {
            return servicioVersion.validarVersion(sistemaOperativo, aplicacion, versionApp, sha);
        } catch (ServicioVersionException e) {
            return "versión no encontrada";
        }
    }
}
