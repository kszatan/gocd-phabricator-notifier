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

package io.github.kszatan.gocd.phabricator.notifier.handlers;

import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import io.github.kszatan.gocd.phabricator.notifier.notification.PhabricatorNotifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class StageStatusRequestHandlerTest {
    private StageStatusRequestHandler handler;
    private PhabricatorNotifier notifier;
    private DefaultGoPluginApiRequest defaultRequest =
            new DefaultGoPluginApiRequest("notification", "1.0", "stage-status");
    
    @Before
    public void setUp() throws Exception {
        notifier = mock(PhabricatorNotifier.class);
        handler = new StageStatusRequestHandler(notifier);
    }

    @Test
    public void handleShouldReturnSuccessResponseCodeOnHappyPath() throws Exception {
        defaultRequest.setRequestBody("{\"pipeline\":{}}");
        GoPluginApiResponse response = handler.handle(defaultRequest);
        assertThat(DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE, equalTo(response.responseCode()));
    }

    @Test
    public void handleShouldReturnErrorResponseCodeGivenInvalidRequestBody() throws Exception {
        defaultRequest.setRequestBody("INVALID JSON");
        GoPluginApiResponse response = handler.handle(defaultRequest);
        assertThat(response.responseCode(), equalTo(DefaultGoPluginApiResponse.INTERNAL_ERROR));
    }

    @Test
    public void handleShouldReturnErrorResponseCodeWhenNotifierThrows() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(notifier).sendNotification();
        GoPluginApiResponse response = handler.handle(defaultRequest);
        assertThat(response.responseCode(), equalTo(DefaultGoPluginApiResponse.INTERNAL_ERROR));
    }

    @Test
    public void handleShouldReturnIncompleteResponseCodeGivenIncompleteRequestBody() throws Exception {
        defaultRequest.setRequestBody("{}");
        GoPluginApiResponse response = handler.handle(defaultRequest);
        assertThat(response.responseCode(), equalTo(DefaultGoPluginApiResponse.VALIDATION_FAILED));
    }
}