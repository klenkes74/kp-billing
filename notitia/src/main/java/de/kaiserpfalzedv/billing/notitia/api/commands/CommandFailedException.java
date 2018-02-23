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

import java.util.UUID;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.BillingBusinessException;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-22
 */
public class CommandFailedException extends BillingBusinessException {
    private static final long serialVersionUID = -6984696603211276454L;

    private UUID commandId;


    public CommandFailedException(@NotNull final UUID commandId, @NotNull final String message) {
        super(message);

        this.commandId = commandId;
    }

    public CommandFailedException(@NotNull final UUID commandId, @NotNull final String message,
                                  @NotNull final Throwable cause) {
        super(message, cause);

        this.commandId = commandId;
    }

    public CommandFailedException(@NotNull final UUID commandId, @NotNull final String message,
                                  @NotNull final Throwable cause, @NotNull final boolean enableSuppression,
                                  @NotNull final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

        this.commandId = commandId;
    }

    public UUID getCommandId() {
        return commandId;
    }
}
