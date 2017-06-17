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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class GoPluginSettingsValidateConfigurationRequestTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void constructorShouldParseCorrectJsonString() throws Exception {
        String json = "{" +
                "   \"plugin-settings\":{" +
                "      \"url\":{" +
                "         \"value\":\"https://phab.mycompany.com\"" +
                "      }," +
                "      \"token\":{" +
                "         \"value\":\"cli-56htnvberk65wer87kjkmmnaqyq3g\"" +
                "      }" +
                "   }" +
                "}" ;
        GoPluginSettingsValidateConfigurationRequest request =
                new GoPluginSettingsValidateConfigurationRequest(json);
        PluginSettings settings = request.getConfiguration();
        assertThat(settings.getUrl(), equalTo("https://phab.mycompany.com"));
        assertThat(settings.getToken(), equalTo("cli-56htnvberk65wer87kjkmmnaqyq3g"));
    }

    @Test
    public void constructorShouldThrowWhenPluginSettingsFieldIsMissing() throws Exception {
        thrown.expect(IncompleteJson.class);
        thrown.expectMessage("Missing fields: ");
        String json = "{}";
        new GoPluginSettingsValidateConfigurationRequest(json);
    }

    @Test
    public void constructorShouldThrowGivenInvalidJson() throws Exception {
        thrown.expect(InvalidJson.class);
        thrown.expectMessage("Malformed JSON: ");
        String json = "Invalid JSON";
        new GoPluginSettingsValidateConfigurationRequest(json);
    }
}