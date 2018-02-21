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

package de.kaiserpfalzedv.billing.openshift;

import java.io.Serializable;

import javax.inject.Inject;

import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.CustomerGuide;
import de.kaiserpfalzedv.billing.api.guided.CustomerRepository;
import de.kaiserpfalzedv.billing.api.guided.GuidingBusinessException;
import de.kaiserpfalzedv.billing.api.guided.NoCustomerFoundException;
import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class OpenShiftCustomerGuide implements CustomerGuide, Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(OpenShiftCustomerGuide.class);

    /** The repository to retrieve customer data from. */
    private CustomerRepository customerRepository;


    @Inject
    public OpenShiftCustomerGuide(
            final CustomerRepository customerRepository
    ) {
        this.customerRepository = customerRepository;
    }

    
    @Override
    public Customer getCustomer(final RawBaseRecord record) throws GuidingBusinessException {
        try {
            return customerRepository.retrieve(record.getTags());
        } catch (NoCustomerFoundException e) {
            throw new GuidingBusinessException(record, e.getMessage(), e);
        }
    }
}
