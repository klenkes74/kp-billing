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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-22
 */
public class UpdateCustomerCostCenterCommand extends AbstractUpdateCustomerCommand {
    private static final long serialVersionUID = 4590981250329964978L;


    private String costCenter;


    @SuppressWarnings("deprecation")
    @Deprecated
    public UpdateCustomerCostCenterCommand() {}

    public UpdateCustomerCostCenterCommand(@NotNull final UUID customerId, @NotNull final String costCenter) {
        super(customerId);

        this.costCenter = costCenter;
    }


    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(@NotNull final String costCenter) {
        this.costCenter = costCenter;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("costCenter", costCenter)
                .toString();
    }
}
