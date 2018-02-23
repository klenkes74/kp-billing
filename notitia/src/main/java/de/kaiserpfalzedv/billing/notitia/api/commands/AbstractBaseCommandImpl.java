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

package de.kaiserpfalzedv.billing.notitia.api.commands;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.common.Identifiable;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The base definition of a command. We have an ID and a creation timestamp.
 * 
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-21
 */
public abstract class AbstractBaseCommandImpl implements BaseCommand {
    private static final long serialVersionUID = -1622701492712707563L;

    private UUID id = UUID.randomUUID();
    private OffsetDateTime created = OffsetDateTime.now(ZoneOffset.UTC);
    private UUID objectId;


    @Deprecated
    public AbstractBaseCommandImpl() {}

    public AbstractBaseCommandImpl(@NotNull final UUID objectId) {
        this.objectId = objectId;
    }

    public AbstractBaseCommandImpl(@NotNull final Identifiable object) {
        this(object.getId());
    }

    
    @Override
    public UUID getId() {
        return id;
    }

    public void setId(@NotNull final UUID id) {
        this.id = id;
    }

    @Override
    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(@NotNull final OffsetDateTime created) {
        this.created = created;
    }

    @Override
    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(@NotNull final UUID objectId) {
        this.objectId = objectId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractBaseCommandImpl)) return false;
        BaseCommand that = (BaseCommand) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("created", created)
                .append("objectId", objectId)
                .toString();
    }
}
