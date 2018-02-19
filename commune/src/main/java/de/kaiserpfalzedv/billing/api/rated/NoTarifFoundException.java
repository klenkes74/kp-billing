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

package de.kaiserpfalzedv.billing.api.rated;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.BillingBusinessException;
import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class NoTarifFoundException extends BillingBusinessException {
    private static final long serialVersionUID = -2051803304270093215L;

    private final Customer customer;
    private final ProductRecordInfo product;

    public NoTarifFoundException(@NotNull final Customer customer, @NotNull final ProductRecordInfo product) {
        super("Can't find a tarif for the product " + product.getProductName()
                      + " and customer " + customer.getName() + ".");

        this.customer = customer;
        this.product = product;
    }

    public NoTarifFoundException(@NotNull final Customer customer, @NotNull final ProductRecordInfo product,
                                 Throwable cause) {
            super("Can't find a tarif for the product " + product.getProductName()
                          + " and customer " + customer.getName() + ".", cause);

            this.customer = customer;
            this.product = product;
    }


    public Customer getCustomer() {
        return customer;
    }

    public ProductRecordInfo getProduct() {
        return product;
    }
}
