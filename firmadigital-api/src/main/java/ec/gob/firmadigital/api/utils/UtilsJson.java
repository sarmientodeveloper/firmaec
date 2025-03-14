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
package ec.gob.firmadigital.api.utils;

import com.google.gson.JsonObject;

/**
 *
 * @author mfernandez
 */
public class UtilsJson {

    public static String generarJsonResponse(int statusCode, String error, String response) {
        //creacion del JSON
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("statusCode", statusCode);
        jsonObject.addProperty("error", error);
        jsonObject.addProperty("response", response);
        return jsonObject.toString();
    }
}
