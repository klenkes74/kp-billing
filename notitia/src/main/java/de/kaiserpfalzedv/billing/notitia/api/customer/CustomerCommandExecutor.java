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

package de.kaiserpfalzedv.billing.notitia.api.customer;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;

import de.kaiserpfalzedv.billing.notitia.api.commands.CommandFailedException;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-22
 */
public interface CustomerCommandExecutor {
    void execute(@NotNull final CreateCustomerCommand command) throws CommandFailedException;
    void execute(@NotNull final UpdateCustomerNameCommand command) throws CommandFailedException;
    void execute(@NotNull final UpdateCustomerCostCenterCommand command) throws CommandFailedException;
    void execute(@NotNull final UpdateCustomerContactEmailCommand command) throws CommandFailedException;
    void execute(@NotNull final UpdateCustomerBillingEmailCommand command) throws CommandFailedException;
    void execute(@NotNull final DELETE command) throws CommandFailedException;
}
