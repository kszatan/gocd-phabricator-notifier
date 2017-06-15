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

import java.util.Date;
import java.util.Objects;

public class Job {
    public String name = "";
    @SerializedName("schedule-time")
    public Date scheduledTime = new Date();
    @SerializedName("complete-time")
    public Date completeTime = new Date();
    public String state = "";
    public String result = "";
    @SerializedName("agent-uuid")
    public String agentUuid = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(name, job.name) &&
                Objects.equals(scheduledTime, job.scheduledTime) &&
                Objects.equals(completeTime, job.completeTime) &&
                Objects.equals(state, job.state) &&
                Objects.equals(result, job.result) &&
                Objects.equals(agentUuid, job.agentUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, scheduledTime, completeTime, state, result, agentUuid);
    }
}
