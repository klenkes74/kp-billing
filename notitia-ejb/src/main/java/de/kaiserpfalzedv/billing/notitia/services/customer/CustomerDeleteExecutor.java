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
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerDeleteCommand;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomer;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomerDeleteEvent;
import de.kaiserpfalzedv.billing.notitia.services.BaseExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-23
 */
@ApplicationScoped
public class CustomerDeleteExecutor extends BaseExecutor<CustomerDeleteCommand> {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerDeleteExecutor.class);

    @Override
    public void execute(@Observes final CustomerDeleteCommand command) throws CommandFailedException {
        try {
            JPACustomer customer = em.find(JPACustomer.class, command.getObjectId());

            if (customer != null) {
                em.remove(customer);

                JPACustomerDeleteEvent event = new JPACustomerDeleteEvent(command);
                em.persist(event);

                BUSINESS.info("Deleted customer (command: {}): {}", command.getId(), command.getObjectId());
                OPERATIONS.debug("Deleted customer (command: {}): {}", command.getId(), command.getObjectId());
            } else {
                BUSINESS.info("Deleted already absent customer (command: {}): {}", command.getId(), command.getObjectId());
                OPERATIONS.debug("Deleted non existing customer (command: {}): {}", command.getId(), command.getObjectId());
            }
        } catch (PersistenceException e) {
            if (em.isJoinedToTransaction() && em.getTransaction().isActive()) {
                em.getTransaction().setRollbackOnly();
            }

            BUSINESS.error("Can't delete customer (command: {}): {}", command.getId(), command.getObjectId());
            OPERATIONS.warn("Can't delete customer (command: {}): {}", command.getId(), command.getObjectId());

            throw new CommandFailedException(command.getId(), e.getMessage());
         }
    }
}
