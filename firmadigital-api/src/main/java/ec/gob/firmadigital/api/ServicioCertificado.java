/*
 * Firma Digital: API
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
package ec.gob.firmadigital.api;

import java.math.BigInteger;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

/**
 * Este servicio permite verificar si un certificado está revocado.
 *
 * @author Ricardo Arguello <ricardo.arguello@soportelibre.com>
 */
@Path("/certificado")
public class ServicioCertificado {

    // Servicio REST interno
//    private static final String REST_SERVICE_URL = "https://ws.firmadigital.gob.ec/servicio/certificado";
    private static final String REST_SERVICE_URL = "http://localhost:8080/servicio/certificado";

    @GET
    @Path("/revocado/{serial}")
    @Produces(MediaType.TEXT_PLAIN)
    public String validarCertificado(@PathParam("serial") BigInteger serial) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(REST_SERVICE_URL + "/revocado").path("{serial}").resolveTemplate("serial",
                serial);
        Invocation.Builder builder = target.request();
        Invocation invocation = builder.buildGet();
        return invocation.invoke(String.class);
    }

    @GET
    @Path("/fechaRevocado/{serial}")
    @Produces(MediaType.TEXT_PLAIN)
    public String validarFechaRevocado(@PathParam("serial") BigInteger serial) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(REST_SERVICE_URL + "/fechaRevocado").path("{serial}").resolveTemplate("serial",
                serial);
        Invocation.Builder builder = target.request();
        Invocation invocation = builder.buildGet();
        return invocation.invoke(String.class);
    }
}
