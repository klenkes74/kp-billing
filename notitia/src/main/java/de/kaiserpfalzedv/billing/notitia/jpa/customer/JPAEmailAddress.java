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

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.common.EmailAddress;
import de.kaiserpfalzedv.billing.notitia.jpa.JPAIdentifiable;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
@Entity(name = "EmailAddress")
@Table(name = "EMAIL_ADDRESSES")
@Cacheable
public class JPAEmailAddress extends JPAIdentifiable implements EmailAddress {
    private static final long serialVersionUID = -7522171017678929170L;

    @Column(name = "NAME_", length = 100, nullable = false)
    private String name;

    @Column(name = "ADDRESS_", length = 100, nullable = false)
    private String address;

    @Column(name = "TYPE_", length = 20, nullable = false)
    private String type;


    public JPAEmailAddress() {}

    public JPAEmailAddress(@NotNull final EmailAddress orig) {
        setId(orig.getId());
        setName(orig.getName());
        setAddress(orig.getAddress());
        setType(orig.getType());
    }


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("name", name)
                .append("address", address)
                .append("type", type)
                .toString();
    }
}
