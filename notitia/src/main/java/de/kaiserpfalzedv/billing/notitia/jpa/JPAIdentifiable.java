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

package de.kaiserpfalzedv.billing.notitia.jpa;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.common.Identifiable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
@MappedSuperclass
public class JPAIdentifiable implements Identifiable, Serializable {
    private static final long serialVersionUID = 5478532100248757244L;

    /**
     * The business logger for business relevant events
     */
    protected static final Logger BUSINESS = LoggerFactory.getLogger("business");

    /**
     * The operations logger for operation relevant events
     */
    protected static final Logger OPERATIONS = LoggerFactory.getLogger("operations");


    @Id
    @Column(name = "ID_", columnDefinition = "BINARY(16)", nullable = false)
    private UUID id = UUID.randomUUID();

    @Version
    @Column(name = "VERSION_", nullable = false)
    private Long version = 0L;


    protected JPAIdentifiable() {}

    protected  JPAIdentifiable(@NotNull final UUID id) {
        this.id = id;
    }

    protected JPAIdentifiable(
            @NotNull final UUID id,
            @NotNull final Long version
    ) {
        this.id = id;
        this.version = version;
    }

    

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(@NotNull final UUID id) {
        this.id = id;
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(@NotNull final Long version) {
        this.version = version;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JPAIdentifiable)) return false;
        JPAIdentifiable that = (JPAIdentifiable) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("version", version)
                .toString();
    }
}
