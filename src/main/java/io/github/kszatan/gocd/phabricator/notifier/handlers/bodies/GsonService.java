/*
 * Copyright (c) 2017 Krzysztof Szatan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.kszatan.gocd.phabricator.notifier.handlers.bodies;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Collection;

public class GsonService {
    private static String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static Collection<String> validate(String json, Collection<String> requiredFields) throws InvalidJson {
        if (json == null) {
            throw new InvalidJson("Null JSON object");
        }
        try {
            ArrayList<String> missing = new ArrayList<>();
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(json).getAsJsonObject();
            for (String field : requiredFields) {
                if (!root.has(field)) {
                    missing.add(field);
                }
            }
            return missing;
        } catch (JsonSyntaxException e) {
            throw new InvalidJson("Malformed JSON: " + json);
        }
    }

    public static String getField(String json, String fieldName) {
        JsonParser parser = new JsonParser();
        JsonObject root = parser.parse(json).getAsJsonObject();
        return root.get(fieldName).toString();
    }

    public static String toJson(Object object) {
        return (new GsonBuilder())
                .setDateFormat(DATE_TIME_FORMAT)
                .create()
                .toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return (new GsonBuilder())
                .setDateFormat(DATE_TIME_FORMAT)
                .create()
                .fromJson(json, type);
    }
}
