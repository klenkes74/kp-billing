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

package de.kaiserpfalzedv.billing.notitia.jpa.product;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import de.kaiserpfalzedv.billing.notitia.jpa.JPAIdentifiable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-20
 */
@Entity(name = "Products")
@Table(name = "PRODUCTS")
@Cacheable
public class JPAProduct extends JPAIdentifiable implements ProductInfo {
    private static final long serialVersionUID = 6743414948587322702L;

    @Column(name = "NAME_", length=200, nullable = false)
    private String name;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @MapKeyColumn(name="KEY_")
    @Column(name="VALUE_")
    @CollectionTable(name="PRODUCT_TAGS", joinColumns=@JoinColumn(name="PRODUCT_"))
    private HashMap<String,String> tags = new HashMap<>(); // maps from attribute name to value


    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    public void setTags(@NotNull final Map<String, String> tags) {
        if (this.tags != null) {
            this.tags.clear();
        } else {
            this.tags = new HashMap<>(tags.size());
        }

        if (tags != null) {
            this.tags.putAll(tags);
        }
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("name", name)
                .append("tags", tags)
                .toString();
    }
}
