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

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import io.github.kszatan.gocd.phabricator.notifier.handlers.bodies.IncompleteJson;
import io.github.kszatan.gocd.phabricator.notifier.handlers.bodies.InvalidJson;
import io.github.kszatan.gocd.phabricator.notifier.handlers.bodies.StageStatusRequest;
import io.github.kszatan.gocd.phabricator.notifier.notification.DefaultPhabricatorNotifier;
import io.github.kszatan.gocd.phabricator.notifier.notification.PhabricatorNotifier;

public class StageStatusRequestHandler implements RequestHandler {
    private final PhabricatorNotifier notifier;

    public StageStatusRequestHandler() {
        this.notifier = new DefaultPhabricatorNotifier();
    }

    public StageStatusRequestHandler(PhabricatorNotifier notifier) {
        this.notifier = notifier;
    }
    
    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) {
        GoPluginApiResponse response;
        try {
            StageStatusRequest stageStatusRequest = new StageStatusRequest(request.requestBody());
            notifier.sendNotification(/*stageStatusRequest*/);
            response = DefaultGoPluginApiResponse.success("");
        } catch (RuntimeException | InvalidJson e) {
            response = DefaultGoPluginApiResponse.error(e.getMessage());
        } catch (IncompleteJson e) {
            response = DefaultGoPluginApiResponse.incompleteRequest(e.getMessage());
        }
        return response;
    }
}
