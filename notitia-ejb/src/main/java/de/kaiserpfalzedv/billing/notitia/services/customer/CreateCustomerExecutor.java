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

package de.kaiserpfalzedv.billing.notitia.services.customer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.persistence.PersistenceException;

import de.kaiserpfalzedv.billing.notitia.api.commands.CommandFailedException;
import de.kaiserpfalzedv.billing.notitia.api.customer.CreateCustomerCommand;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomer;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.command.CreateCustomerEvent;
import de.kaiserpfalzedv.billing.notitia.services.BaseExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-23
 */
@ApplicationScoped
public class CreateCustomerExecutor extends BaseExecutor<CreateCustomerCommand> {
    private static final Logger LOG = LoggerFactory.getLogger(CreateCustomerExecutor.class);

    @Override
    public void execute(@Observes final CreateCustomerCommand command) throws CommandFailedException {
        JPACustomer jpa = new JPACustomer(command.getData());
        CreateCustomerEvent event = new CreateCustomerEvent(command);

        try {
            em.persist(jpa);
            em.persist(event);
        } catch (PersistenceException e) {
            OPERATIONS.warn("Could not create customer (command: {}): {}", command.getId(), command.getData());
            BUSINESS.info("Could not create customer (command: {}): {}", command.getId(), command.getData());

            if (em.isJoinedToTransaction() && em.getTransaction().isActive()) {
                em.getTransaction().setRollbackOnly();
            }
            
            throw new CommandFailedException(command.getId(), e.getMessage());
        }

        BUSINESS.info("Created customer (command: {}): {}", command.getId(), command.getData());
        OPERATIONS.debug("Created customer (command: {}): {}", command.getId(), command.getData());
    }
}
