///**
// *
// */
//package ec.gob.firmadigital.libreria.webServices;
//
//import java.io.StringReader;
//import java.util.Collection;
//import java.util.List;
//
//import jakarta.json.Json;
//import jakarta.json.JsonObject;
//import jakarta.json.JsonReader;
//import jakarta.json.stream.JsonParsingException;
//import jakarta.ws.rs.Consumes;
//import jakarta.ws.rs.GET;
//import jakarta.ws.rs.HeaderParam;
//import jakarta.ws.rs.POST;
//import jakarta.ws.rs.Path;
//import jakarta.ws.rs.Produces;
//import jakarta.ws.rs.core.GenericEntity;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//import jakarta.ws.rs.core.Response.Status;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
////http://localhost:8080/serviciosistema/rest/documentoFirmado/mensaje
//@Path("/documentoFirmado")
//public class ArchivoFirmadoRest {
//
//    private final Logger log = LoggerFactory.getLogger(ArchivoFirmadoRest.class);
//
//    private static final String API_KEY_HEADER = "X-API-KEY";
//
//    @POST
//    @Path("/grabar_archivos_firmados")
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response guardarDocumentosFirmados(@HeaderParam(API_KEY_HEADER) String apiKey, String jsonParameter) {
//
//        if (apiKey == null) {
//            return Response.status(Status.BAD_REQUEST).entity("Sin API Key").build();
//        }
//
//        log.info("TO firmado:" + jsonParameter + " apikey:" + apiKey);
//
//        if (jsonParameter == null || jsonParameter.isEmpty()) {
//            return Response.status(Status.BAD_REQUEST).entity("No hay texto tipo JSON").build();
//        }
//
//        JsonReader jsonReader = Json.createReader(new StringReader(jsonParameter));
//        JsonObject json;
//        try {
//            json = (JsonObject) jsonReader.read();
//        } catch (JsonParsingException e) {
//            return Response.status(Status.BAD_REQUEST).entity("Error al decodificar JSON: \"" + e.getMessage() + "\"")
//                    .build();
//        }
//
//        if (!apiKey.equals("prueba")) {
//            log.error("Error al validar API_KEY {0}", apiKey);
//            return Response.status(Status.FORBIDDEN).entity("Error al validar API_KEY").build();
//        }
//        String usuario;
//        try {
//            usuario = json.getString("usuario");
//        } catch (NullPointerException e) {
//            return Response.status(Status.BAD_REQUEST).entity("Error al decodificar JSON: Se debe incluir \"cedula\"")
//                    .build();
//        }
//        String archivo;
//        try {
//            archivo = json.getString("archivo");
//            if (archivo == null) {
//                return Response.status(Status.BAD_REQUEST)
//                        .entity("Error al decodificar JSON: Se debe incluir \"archivo\"").build();
//            }
//        } catch (NullPointerException e) {
//            return Response.status(Status.BAD_REQUEST).entity("Error al decodificar JSON: Se debe incluir \"archivo\"")
//                    .build();
//        }
//        log.info("Validando archivo {0}", archivo);
//        try {
//            //preparando para almacenar documento
//            log.info("Construyendo objeto para su almacenamiento {0}", "info documento");
//            //almacenar documento
//            log.info("Finalizado el proceso............");
//        } catch (Exception e) {
//            return Response.status(Status.FORBIDDEN).entity("Error al guardar el archivo").build();
//        }
//
//        return enviarRespuestaExitoHTTP("200");
//    }
//
//    @GET
//    @Path("/mensaje")
//    public Response imprimirMessage() {
//        String salida = "Hola desde un Restful Web Service ejemplo ";
//        return Response.status(200).entity(salida).build();
//    }
//
//    /**
//     * Envia respuesta de exito segun contenido
//     *
//     * @param contenido objeto pojo o lista
//     * @return respuesta HTTP (200 o 204)
//     */
//    public static Response enviarRespuestaExitoHTTP(Object contenido) {
//        if (esObjetoVacio(contenido)) {
//            return Response.noContent().build();
//        } else {
//            GenericEntity<Object> entidadRespuesta = new GenericEntity<Object>(contenido) {
//            };
//            return Response.ok(entidadRespuesta, MediaType.APPLICATION_JSON).build();
//        }
//    }
//
//    public static Boolean esObjetoVacio(Object contenido) {
//        if (null == contenido) {
//            return Boolean.TRUE;
//        } else if (contenido instanceof String) {
//            return contenido.toString().isEmpty();
//        } else if (contenido instanceof Collection<?>) {
//            List<Object> listaContenido = (List<Object>) contenido;
//            if (listaContenido.isEmpty()) {
//                return Boolean.TRUE;
//            }
//        }
//        return Boolean.FALSE;
//    }
//
//}
