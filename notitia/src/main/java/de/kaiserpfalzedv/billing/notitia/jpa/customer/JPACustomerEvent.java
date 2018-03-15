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

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerTO;
import de.kaiserpfalzedv.billing.notitia.jpa.JPABaseEvent;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-22
 */
@Entity(name = "CustomerEvents")
@Table(name = "CUSTOMER_EVENTS")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE_")
public class JPACustomerEvent extends JPABaseEvent<CustomerTO> {
    private static final long serialVersionUID = -1400298785350052002L;

    @SuppressWarnings({"deprecation", "DeprecatedIsStillUsed"})
    @Deprecated
    public JPACustomerEvent() {}

    @SuppressWarnings("WeakerAccess")
    public JPACustomerEvent(@NotNull final UUID id, @NotNull final UUID objectId,
                            @NotNull final OffsetDateTime created, @NotNull final OffsetDateTime executed) {
        super(id, objectId, created, executed);
    }

    @Override
    public CustomerTO update(@NotNull CustomerTO data) {
        throw new UnsupportedOperationException("Sorry, this operation is not allowed for base customer events!");
    }
}