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
import java.util.UUID;

import javax.validation.constraints.NotNull;

/**
 * The base definition of a result set. We have an ID, the ID of the command being executed and of course an execution
 * time stamp.
 * 
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-21
 */
public class AbstractBaseResultImpl implements BaseResult {
    private static final long serialVersionUID = -1368271336004388096L;

    private UUID id = UUID.randomUUID();
    private OffsetDateTime executed = OffsetDateTime.now(ZoneOffset.UTC);
    private UUID commandId;
    private UUID objectId;


    @Deprecated
    public AbstractBaseResultImpl() {}

    public AbstractBaseResultImpl(@NotNull final BaseCommand command) {
        this.commandId = command.getId();
        this.objectId = command.getObjectId();
    }


    @Override
    public UUID getId() {
        return id;
    }

    public void setId(@NotNull final UUID id) {
        this.id = id;
    }

    @Override
    public UUID getCommandId() {
        return commandId;
    }

    public void setCommandId(@NotNull final UUID commandId) {
        this.commandId = commandId;
    }

    @Override
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
}
