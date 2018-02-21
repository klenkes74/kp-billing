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

package de.kaiserpfalzedv.billing.princeps.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class ProductInfoImpl implements ProductInfo {
    private static final long serialVersionUID = -4517938249820695164L;


    private final UUID id;
    private final String name;
    private final HashMap<String, String> tags = new HashMap<>();

    ProductInfoImpl(
            final UUID id,
            final String name,
            final Map<String,String> tags
    ) {
        this.id = id;
        this.name = name;

        this.tags.putAll(tags);
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
    public Map<String,String> getTags() {
        return tags;
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
