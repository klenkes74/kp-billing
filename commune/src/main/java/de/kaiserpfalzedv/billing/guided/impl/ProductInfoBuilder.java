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

package de.kaiserpfalzedv.billing.guided.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import de.kaiserpfalzedv.billing.guided.ProductInfo;
import org.apache.commons.lang3.builder.Builder;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class ProductInfoBuilder implements Builder<ProductInfo> {
    private final ArrayList<String> tags = new ArrayList<>();
    private UUID id = UUID.randomUUID();
    private String name;

    @Override
    public ProductInfo build() {
        validate();

        try {
            return new ProductInfoImpl(id, name, tags.toArray(new String[0]));
        } finally {
            reset();
        }
    }

    private void validate() {
        if (isBlank(name)) {
            throw new IllegalStateException("Can't create a ProductInfo without name");
        }
    }

    private void reset() {
        id = UUID.randomUUID();
        name = null;
        tags.clear();
    }


    public ProductInfoBuilder setId(UUID id) {
        this.id = id;
        return this;
    }

    public ProductInfoBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductInfoBuilder setTags(String[] tags) {
        this.tags.clear();
        Collections.addAll(this.tags, tags);

        return this;
    }

    public ProductInfoBuilder clearTags() {
        this.tags.clear();

        return this;
    }

    public ProductInfoBuilder addTag(final String tag) {
        this.tags.add(tag);

        return this;
    }

    public ProductInfoBuilder removeTag(final String tag) {
        this.tags.remove(tag);

        return this;
    }

    public ProductInfoBuilder copy(final ProductInfo orig) {
        this.id = orig.getId();
        this.name = orig.getName();
        Collections.addAll(this.tags, orig.getTags());

        return this;
    }
}
