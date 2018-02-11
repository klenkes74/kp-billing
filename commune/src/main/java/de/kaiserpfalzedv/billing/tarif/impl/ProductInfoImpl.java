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

package de.kaiserpfalzedv.billing.tarif.impl;

import java.util.UUID;

import de.kaiserpfalzedv.billing.tarif.ProductInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class ProductInfoImpl implements ProductInfo {
    private static final long serialVersionUID = -5648933497591121916L;


    private final UUID id;
    private final String name;
    private final String[] tags;

    ProductInfoImpl(
            final UUID id,
            final String name,
            final String[] tags
    ) {
        this.id = id;
        this.name = name;
        this.tags = new String[tags.length];

        System.arraycopy(tags, 0, this.tags, 0, tags.length);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getTags() {
        return tags;
    }

    @Override
    public int hashCode() {
        return hashCodeImpl();
    }

    @Override
    public boolean equals(Object o) {
        return equalsImpl(o);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(System.identityHashCode(this))
                .append("id", id)
                .append("name", name)
                .append("tags", tags)
                .toString();
    }
}
