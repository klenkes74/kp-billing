/*
 *    Copyright 2018 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.kaiserpfalzedv.billing.api.common;

import java.util.Collection;
import java.util.HashSet;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.BillingRuntimeException;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class BuilderException extends BillingRuntimeException {
    private static final long serialVersionUID = -7415739270082130072L;

    private final Class<?> clasz;
    private final HashSet<String> failures = new HashSet<>();

    public BuilderException(@NotNull final Class<?> clasz, @NotNull final Collection<String> failures) {
        super("Can't build '" + clasz.getSimpleName() + "' due to: " + failures);

        this.clasz = clasz;
        this.failures.addAll(failures);
    }

    
    public Class<?> getClasz() {
        return clasz;
    }

    public HashSet<String> getFailures() {
        return failures;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("clasz", clasz)
                .append("failures", failures)
                .toString();
    }
}
