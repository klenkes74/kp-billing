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

package de.kaiserpfalzedv.billing.common.impl;

import java.util.Objects;
import java.util.UUID;

import de.kaiserpfalzedv.billing.common.Identifiable;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public abstract class IdentifiableImpl implements Identifiable {
    private final UUID id;


    protected IdentifiableImpl(final UUID id) {
        this.id = id;
    }

    public int hashCode() {
        return Objects.hash(getId());
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Identifiable)) return false;

        Identifiable that = (Identifiable) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(System.identityHashCode(this))
                .append("id", id)
                .toString();
    }

    @Override
    public UUID getId() {
        return id;
    }
}
