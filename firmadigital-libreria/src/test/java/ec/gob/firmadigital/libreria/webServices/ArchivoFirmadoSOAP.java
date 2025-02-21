package ec.gob.firmadigital.libreria.webServices;

///**
// *
// */
//package ec.gob.firmadigital.libreria.webServices;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.util.Base64;
//import jakarta.jws.WebMethod;
//import jakarta.jws.WebParam;
//import jakarta.jws.WebResult;
//import jakarta.jws.WebService;
//import jakarta.jws.soap.SOAPBinding;
//import jakarta.jws.soap.SOAPBinding.Style;
//import jakarta.jws.soap.SOAPBinding.Use;
//
//@WebService(targetNamespace = "urn:soapapiorfeo", serviceName = "soapapiorfeo")
//@SOAPBinding(style = Style.RPC, use = Use.ENCODED)
//public class ArchivoFirmadoSOAP {
//
//    @WebMethod(operationName = "grabar_archivos_firmados")
//    @WebResult(name = "result")
//    public String grabar_archivos_firmados(
//            @WebParam(name = "set_var_usuario") String usuario, @WebParam(name = "set_var_documento") String nombre_doc, @WebParam(name = "set_var_archivo") String archivo, @WebParam(name = "set_var_datos_firmante") String datos_firmante, @WebParam(name = "set_var_fecha") String fecha, @WebParam(name = "set_var_institucion") String institucion, @WebParam(name = "set_var_cargo") String cargo) {
//
//        String path = "";
//        //Decodifica el archivo json y guarda
//        try {
//            byte[] archivoByte = Base64.getDecoder().decode(archivo);
//            OutputStream out = new FileOutputStream(path);
//            out.write(archivoByte);
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //retorna 1 si el documento existe
//        File archivo_ok = new File(path);
//        if (archivo_ok.exists()) {
//            return "1";
//        } else {
//            return "0";
//        }
//    }
//}
