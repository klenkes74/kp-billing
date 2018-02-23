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

package de.kaiserpfalzedv.billing.notitia.jpa.customer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.cache.annotation.CacheResult;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.CustomerRepository;
import de.kaiserpfalzedv.billing.api.guided.NoCustomerFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class JPACustomerRepository implements CustomerRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JPACustomerRepository.class);

    @PersistenceContext(name = "notitia")
    private EntityManager em;

    @CacheResult
    @Override
    public Customer retrieve(final UUID id) throws NoCustomerFoundException {
        Customer result;
        try {
            result = em.find(JPACustomer.class, id);
        } catch (PersistenceException e) {
            LOG.warn("Could not load customer data with id '{}': {}", id, e.getMessage());
            throw new NoCustomerFoundException(id, e);
        }

        if (result == null) {
            throw new NoCustomerFoundException(id);
        }

        return result;
    }

    @CacheResult
    @Override
    public Customer retrieve(final Map<String, String> tags) throws NoCustomerFoundException {
        List<JPACustomer> customers = em
                .createQuery("select c from Customers c where c.tags=:tags", JPACustomer.class)
                .setParameter("tags", tags)
                .getResultList();

        if (customers.size() == 1) {
            return customers.get(0);
        }

        throw new NoCustomerFoundException(tags);
    }
}
