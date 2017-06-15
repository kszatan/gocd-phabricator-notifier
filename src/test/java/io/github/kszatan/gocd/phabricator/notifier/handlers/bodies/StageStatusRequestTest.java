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

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class StageStatusRequestTest {
    private String correctJson = "{" +
            "   \"pipeline\":{" +
            "      \"name\":\"mypipeline\"," +
            "      \"counter\":\"16\"," +
            "      \"group\":\"PipeGroup\"," +
            "      \"build-cause\":[" +
            "         {" +
            "            \"material\":{" +
            "               \"plugin-id\":\"PhabricatorStagingMaterial\"," +
            "               \"scm-configuration\":{" +
            "                  \"url\":\"git@host:repo.git\"" +
            "               }," +
            "               \"type\":\"scm\"" +
            "            }," +
            "            \"changed\":false," +
            "            \"modifications\":[" +
            "               {" +
            "                  \"revision\":\"10\"," +
            "                  \"modified-time\":\"2017-06-12T22:12:29.000Z\"," +
            "                  \"data\":{" +
            "                  }" +
            "               }" +
            "            ]" +
            "         }" +
            "      ]," +
            "      \"stage\":{" +
            "         \"name\":\"defaultStage\"," +
            "         \"counter\":\"1\"," +
            "         \"approval-type\":\"success\"," +
            "         \"approved-by\":\"kszatan\"," +
            "         \"state\":\"Passed\"," +
            "         \"result\":\"Warning\"," +
            "         \"create-time\":\"2017-06-15T11:09:48.367Z\"," +
            "         \"last-transition-time\":\"2017-06-15T11:10:24.961Z\"," +
            "         \"jobs\":[" +
            "            {" +
            "               \"name\":\"defaultJob\"," +
            "               \"schedule-time\":\"2017-06-15T11:09:48.367Z\"," +
            "               \"complete-time\":\"2017-06-15T11:10:24.961Z\"," +
            "               \"state\":\"Completed\"," +
            "               \"result\":\"Passed\"," +
            "               \"agent-uuid\":\"0560f35f-62b3-4c25-bdae-d29a27d4d92c\"" +
            "            }" +
            "         ]" +
            "      }" +
            "   }" +
            "}";
            
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructorShouldParseCorrectJsonString() throws Exception {
        StageStatusRequest stageStatusRequest = new StageStatusRequest(correctJson);
        StageStatus stageStatus = stageStatusRequest.getStageStatus();
        Pipeline pipeline = stageStatus.pipeline;
        assertThat(pipeline.name, equalTo("mypipeline"));
        assertThat(pipeline.counter, equalTo("16"));
        assertThat(pipeline.group, equalTo("PipeGroup"));
        assertThat(pipeline.buildCause.size(), equalTo(1));

        BuildCause buildCause = pipeline.buildCause.iterator().next();
        Material material = buildCause.material;
        assertThat(material.pluginId, equalTo("PhabricatorStagingMaterial"));

        ScmConfiguration scmConfiguration = material.scmConfiguration;
        assertThat(scmConfiguration.url, equalTo("git@host:repo.git"));
        assertThat(scmConfiguration.username, equalTo(""));
        assertThat(scmConfiguration.password, equalTo(""));
        assertThat(material.type, equalTo("scm"));
        assertThat(buildCause.changed, equalTo(false));
        assertThat(buildCause.modifications.size(), equalTo(1));

        Modification modification = buildCause.modifications.iterator().next();
        assertThat(modification.revision, equalTo("10"));
        assertThat(modification.modifiedTime, equalTo(GsonService.fromJson("\"2017-06-12T22:12:29.000Z\"", Date.class)));
        assertThat(modification.data, equalTo(new ModificationData()));

        Stage stage = pipeline.stage;
        assertThat(stage.name, equalTo("defaultStage"));
        assertThat(stage.counter, equalTo("1"));
        assertThat(stage.approvalType, equalTo("success"));
        assertThat(stage.approvedBy, equalTo("kszatan"));
        assertThat(stage.state, equalTo("Passed"));
        assertThat(stage.result, equalTo("Warning"));
        assertThat(stage.crateTime, equalTo(GsonService.fromJson("\"2017-06-15T11:09:48.367Z\"", Date.class)));
        assertThat(stage.lastTransitionTime, equalTo(GsonService.fromJson("\"2017-06-15T11:10:24.961Z\"", Date.class)));
        assertThat(stage.jobs.size(), equalTo(1));

        Job job = stage.jobs.iterator().next();
        assertThat(job.name, equalTo("defaultJob"));
        assertThat(job.scheduledTime, equalTo(GsonService.fromJson("\"2017-06-15T11:09:48.367Z\"", Date.class)));
        assertThat(job.completeTime, equalTo(GsonService.fromJson("\"2017-06-15T11:10:24.961Z\"", Date.class)));
        assertThat(job.state, equalTo("Completed"));
        assertThat(job.result, equalTo("Passed"));
        assertThat(job.agentUuid, equalTo("0560f35f-62b3-4c25-bdae-d29a27d4d92c"));
    }

    @Test
    public void constructorShouldThrowWhenPipelineIsMissing() throws Exception {
        thrown.expect(IncompleteJson.class);
        thrown.expectMessage("Missing fields: ");
        String json = "{\"other\":{}}";
        new StageStatusRequest(json);
    }

    @Test
    public void constructorShouldThrowGivenInvalidJson() throws Exception {
        thrown.expect(InvalidJson.class);
        thrown.expectMessage("Malformed JSON:");
        String json = "INVALID JSON";
        new StageStatusRequest(json);
    }
}