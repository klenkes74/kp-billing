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

package de.kaiserpfalzedv.billing.notitia.jpa.events;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.notitia.api.commands.BaseCommand;
import de.kaiserpfalzedv.billing.notitia.jpa.JPAIdentifiable;
import de.kaiserpfalzedv.billing.notitia.jpa.JPAOffsetDateTimeConverter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-22
 */
@MappedSuperclass
public abstract class BaseEvent<T extends Serializable> extends JPAIdentifiable implements BaseCommand {
    private static final long serialVersionUID = 8098424114572437615L;



    @Column(name = "CREATED_")
    @Convert(converter = JPAOffsetDateTimeConverter.class)
    private OffsetDateTime created;

    @Column(name = "EXECUTED_")
    @Convert(converter = JPAOffsetDateTimeConverter.class)
    private OffsetDateTime executed;

    @Column(name = "OBJECT_ID_", columnDefinition = "BINARY(16)", nullable = false)
    private UUID objectId;


    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public BaseEvent() {}

    public BaseEvent(@NotNull final UUID id, @NotNull final UUID objectId,
                     @NotNull final OffsetDateTime created, @NotNull final OffsetDateTime executed) {
        super(id);

        this.objectId = objectId;
        this.created = created;
        this.executed = executed;
    }


    /**
     * Updates the data set with the event data.
     * @param data The data set to be changed according to the event data.
     */
    public abstract T update(T data);


    @Override
    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(@NotNull final OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getExecuted() {
        return executed;
    }

    public void setExecuted(@NotNull final OffsetDateTime executed) {
        this.executed = executed;
    }

    @Override
    public UUID getObjectId() {
        return objectId;
    }

    public void setObjectId(@NotNull final UUID objectId) {
        this.objectId = objectId;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("created", created)
                .append("executed", executed)
                .append("objectId", objectId)
                .toString();
    }
}
