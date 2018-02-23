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

import java.util.UUID;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.notitia.api.commands.AbstractBaseCommandImpl;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-22
 */
public class UpdateCustomerNameCommand extends AbstractBaseCommandImpl {
    private static final long serialVersionUID = 834839107392759842L;


    private String customerName;


    @SuppressWarnings("deprecation")
    @Deprecated
    public UpdateCustomerNameCommand() {}

    public UpdateCustomerNameCommand(@NotNull final UUID customerId, @NotNull final String customerName) {
        super(customerId);

        this.customerName = customerName;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(@NotNull final String customerName) {
        this.customerName = customerName;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("customerName", customerName)
                .toString();
    }
}
