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

package de.kaiserpfalzedv.billing.notitia.jpa.customer.command;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerTO;
import de.kaiserpfalzedv.billing.notitia.api.customer.UpdateCustomerCostCenterCommand;

import static java.time.ZoneOffset.UTC;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-21
 */
@Entity
@Table(name = "CUSTOMER_UPDATE_COST_CENTER_EVENTS")
@DiscriminatorValue("UPDATE_COST_CENTER")
public class UpdateCustomerCostCenterEvent extends BaseCustomerEvent {
    private static final long serialVersionUID = 6348483298408306647L;

    @Column(name = "COST_CENTER_")
    private String costCenter;

    @SuppressWarnings("deprecation")
    @Deprecated
    public UpdateCustomerCostCenterEvent() {}

    public UpdateCustomerCostCenterEvent(@NotNull final UpdateCustomerCostCenterCommand command) {
        this(command, OffsetDateTime.now(UTC));
    }

    public UpdateCustomerCostCenterEvent(@NotNull final UpdateCustomerCostCenterCommand command, @NotNull final OffsetDateTime executed) {
        super(command.getId(), command.getObjectId(), command.getCreated(), executed);

        costCenter = command.getCostCenter();
    }

    @Override
    public CustomerTO update(@NotNull CustomerTO data) {
        BUSINESS.trace("Command {} updated customer on {} and changed cost center: {} -> {}",
                       getId(), getExecuted(), data.getCostCenter(), getCostCenter());

        data.setCostCenter(getCostCenter());

        return data;
    }


    @SuppressWarnings("WeakerAccess")
    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(@NotNull final String costCenter) {
        this.costCenter = costCenter;
    }
}
