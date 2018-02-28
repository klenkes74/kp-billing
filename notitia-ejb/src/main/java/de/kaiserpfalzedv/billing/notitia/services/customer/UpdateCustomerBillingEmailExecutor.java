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

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;

import de.kaiserpfalzedv.billing.notitia.api.commands.CommandFailedException;
import de.kaiserpfalzedv.billing.notitia.api.customer.UpdateCustomerBillingEmailCommand;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomer;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPAEmailAddress;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.command.UpdateCustomerBillingEmailEvent;
import de.kaiserpfalzedv.billing.notitia.services.BaseExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-23
 */
@RequestScoped
public class UpdateCustomerBillingEmailExecutor extends BaseExecutor<UpdateCustomerBillingEmailCommand> {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateCustomerBillingEmailExecutor.class);

    @Override
    public void execute(@Observes final UpdateCustomerBillingEmailCommand command) throws CommandFailedException {

        JPACustomer customer = em.find(JPACustomer.class, command.getObjectId());
        LOG.info("Loaded customer for change: {}", customer);

        if (customer != null) {
            customer.setBillingAddress(new JPAEmailAddress((command.getEmailAddress())));
            em.merge(customer);

            UpdateCustomerBillingEmailEvent event = new UpdateCustomerBillingEmailEvent(command);
            em.persist(event);

            BUSINESS.info("Update customer: {}", command);
            OPERATIONS.info("Updated customer: {}", command);
        } else {
            BUSINESS.warn("Tried to update customer, but customer not found for command: {}", command);
            OPERATIONS.info("Could not load customer for change: {}", command);
        }
    }
}
