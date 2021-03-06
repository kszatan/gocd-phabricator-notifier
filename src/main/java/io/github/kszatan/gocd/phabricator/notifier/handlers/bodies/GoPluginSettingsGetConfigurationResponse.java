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

import com.google.gson.annotations.SerializedName;

public class GoPluginSettingsGetConfigurationResponse {
    public class Field {
        @SerializedName("display-name")
        public String displayName;
        @SerializedName("display-order")
        public String displayOrder;
        @SerializedName("default-value")
        public String defaultValue;
        public Boolean secure;
        public Boolean required;
    }

    public Field url;
    public Field token;

    public GoPluginSettingsGetConfigurationResponse() {
        url = new Field();
        url.displayName = "Phabricator Base URL";
        url.displayOrder = "0";
        url.required = true;
        url.defaultValue = "https://phabricator.unicorn.com";

        token = new Field();
        token.displayName = "API Token";
        token.displayOrder = "1";
        token.secure = true;
        token.required = true;
    }
}
