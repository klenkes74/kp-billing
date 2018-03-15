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

import java.io.Serializable;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.kaiserpfalzedv.billing.api.guided.CustomerRepository;
import de.kaiserpfalzedv.billing.api.guided.NoCustomerFoundException;
import de.kaiserpfalzedv.billing.notitia.api.commands.CommandFailedException;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerCreateCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerDeleteCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerTO;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateBillingEmailCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateContactEmailCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateCostCenterCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateNameCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateTagsCommand;
import de.kaiserpfalzedv.billing.notitia.jpa.customer.JPACustomerRepository;
import de.kaiserpfalzedv.billing.notitia.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-22
 */
@Stateless
@Local(CustomerService.class)
public class CustomerServiceImpl implements Serializable, CustomerService {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    @Context
    private UriInfo uriInfo;


    private CustomerRepository repository;
    private BeanManager beanManager;

    @Deprecated
    public CustomerServiceImpl() {}

    @Inject
    public CustomerServiceImpl(
            final JPACustomerRepository repository,
            final BeanManager beanManager
    ) {
        this.repository = repository;
        this.beanManager = beanManager;
    }

    @Override
    public Response createCustomer(@NotNull final CustomerCreateCommand command) {
        LOG.info("Called: {}", uriInfo.getRequestUri().toASCIIString());

        try {
            beanManager.fireEvent(command);
        } catch (CommandFailedException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }

        return Response.ok().link("customer/" + command.getObjectId().toString(), "data").build();
    }


    @Override
    public CustomerTO retrieveCustomer(
            @NotNull final String customerId) {
        LOG.info("Called: {}", uriInfo.getRequestUri().toASCIIString());

        UUID id = UUID.fromString(customerId);
        LOG.info("Retrieving customer with id: {}", id);

        CustomerTO result;

        try {
            result = new CustomerTO(repository.retrieve(id));
        } catch (NoCustomerFoundException e) {
            LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);

            throw new IllegalStateException(e);
        }

        LOG.info("Returning customer data: {}", result);
        return result;
    }


    @Override
    public Response updateCustomer(
            @NotNull final String customerId,
            @NotNull final CustomerUpdateBillingEmailCommand command
    ) {
        LOG.info("Called: {}", uriInfo.getRequestUri().toASCIIString());

        try {
            beanManager.fireEvent(command);
        } catch (CommandFailedException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }

        return Response.ok().link("customer/" + customerId, "data").build();
    }

    @Override
    public Response updateCustomer(
            @NotNull final String customerId,
            @NotNull final CustomerUpdateContactEmailCommand command
    ) {
        LOG.info("Called: {}", uriInfo.getRequestUri().toASCIIString());

        try {
            beanManager.fireEvent(command);
        } catch (CommandFailedException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }

        return Response.ok().link("customer/" + customerId, "data").build();
    }

    @Override
    public Response updateCustomer(
            @NotNull final String customerId,
            @NotNull final CustomerUpdateCostCenterCommand command
    ) {
        LOG.info("Called: {}", uriInfo.getRequestUri().toASCIIString());

        try {
            beanManager.fireEvent(command);
        } catch (CommandFailedException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }

        return Response.ok().link("customer/" + customerId, "data").build();
    }

    @Override
    public Response updateCustomer(
            @NotNull final String customerId,
            @NotNull final CustomerUpdateNameCommand command
    ) {
        LOG.info("Called: {}", uriInfo.getRequestUri().toASCIIString());

        try {
            beanManager.fireEvent(command);
        } catch (CommandFailedException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }

        return Response.ok().link("customer/" + customerId, "data").build();
    }

    @Override
    public Response updateCustomer(
            @NotNull final String customerId,
            @NotNull final CustomerUpdateTagsCommand command
    ) {
        LOG.info("Called: {}", uriInfo.getRequestUri().toASCIIString());

        try {
            beanManager.fireEvent(command);
        } catch (CommandFailedException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }

        return Response.ok().link("customer/" + customerId, "data").build();
    }

    @Override
    public Response deleteCustomer(@NotNull final String customerId) {
        LOG.info("Called: {}", uriInfo.getRequestUri().toASCIIString());

        try {
            CustomerDeleteCommand command = new CustomerDeleteCommand(UUID.fromString(customerId));
            beanManager.fireEvent(command);
        } catch (IllegalArgumentException | CommandFailedException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }

        return Response.ok().link("customer/" + customerId, "data").build();
    }
}
