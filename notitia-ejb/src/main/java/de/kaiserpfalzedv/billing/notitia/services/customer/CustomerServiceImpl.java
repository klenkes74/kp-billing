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
import de.kaiserpfalzedv.billing.notitia.api.customer.CreateCustomerCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerTO;
import de.kaiserpfalzedv.billing.notitia.api.customer.DeleteCustomerCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.UpdateCustomerBillingEmailCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.UpdateCustomerContactEmailCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.UpdateCustomerCostCenterCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.UpdateCustomerNameCommand;
import de.kaiserpfalzedv.billing.notitia.api.customer.UpdateCustomerTagsCommand;
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
    public Response createCustomer(@NotNull final CreateCustomerCommand command) {
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
            @NotNull final UpdateCustomerBillingEmailCommand command
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
            @NotNull final UpdateCustomerContactEmailCommand command
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
            @NotNull final UpdateCustomerCostCenterCommand command
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
            @NotNull final UpdateCustomerNameCommand command
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
            @NotNull final UpdateCustomerTagsCommand command
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
            DeleteCustomerCommand command = new DeleteCustomerCommand(UUID.fromString(customerId));
            beanManager.fireEvent(command);
        } catch (IllegalArgumentException | CommandFailedException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }

        return Response.ok().link("customer/" + customerId, "data").build();
    }
}
