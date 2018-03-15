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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerTO;
import de.kaiserpfalzedv.billing.notitia.api.customer.CustomerUpdateTagsCommand;

import static java.time.ZoneOffset.UTC;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-21
 */
@Entity
@Table(name = "CUSTOMER_UPDATE_TAGS_EVENTS")
@DiscriminatorValue("UPDATE_TAGS")
public class JPACustomerUpdateTagsEvent extends JPACustomerEvent {
    private static final long serialVersionUID = 8834749613175007120L;

    @ElementCollection
    @MapKeyColumn(name="KEY_")
    @Column(name="VALUE_")
    @CollectionTable(name="CUSTOMER_UPDATE_TAGS", joinColumns=@JoinColumn(name="CUSTOMER_"))
    private Map<String, String> tags = new HashMap<>(); // maps from attribute name to value

    @SuppressWarnings("deprecation")
    @Deprecated
    public JPACustomerUpdateTagsEvent() {}

    public JPACustomerUpdateTagsEvent(@NotNull final CustomerUpdateTagsCommand command) {
        this(command, OffsetDateTime.now(UTC));
    }

    @SuppressWarnings("WeakerAccess")
    public JPACustomerUpdateTagsEvent(@NotNull final CustomerUpdateTagsCommand command, @NotNull final OffsetDateTime executed) {
        super(command.getId(), command.getObjectId(), command.getCreated(), executed);

        tags.putAll(command.getTags());
    }

    @Override
    public CustomerTO update(@NotNull CustomerTO data) {
        BUSINESS.trace("Command {} updated customer on {} and changed tags: {} -> {}",
                       getId(), getExecuted(), data.getTags(), getTags());

        data.setTags(getTags());

        return data;
    }


    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    public void setTags(@NotNull final Map<String, String> tags) {
        this.tags.clear();

        if (tags != null) {
            this.tags.putAll(tags);
        }
    }
}
