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

package de.kaiserpfalzedv.billing.api.guided.impl;

import java.util.ArrayList;
import java.util.Collections;

import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import org.apache.commons.lang3.builder.Builder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class ProductRecordInfoBuilder implements Builder<ProductRecordInfo> {

    private final ArrayList<String> tags = new ArrayList<>();
    private ProductInfo productInfo;

    @Override
    public ProductRecordInfo build() {
        validate();

        try {
            return new ProductRecordInfoImpl(productInfo, tags.toArray(new String[0]));
        } finally {
            reset();
        }
    }

    private void validate() {
        if (productInfo == null) {
            throw new IllegalStateException("Can't create a ProductRecordInfo without ProductInfo");
        }
    }

    private void reset() {
        productInfo = null;
        tags.clear();
    }

    public ProductRecordInfoBuilder setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
        return this;
    }

    public ProductRecordInfoBuilder clearTags() {
        this.tags.clear();

        return this;
    }

    public ProductRecordInfoBuilder addTag(final String tag) {
        this.tags.add(tag);

        return this;
    }

    public ProductRecordInfoBuilder removeTag(final String tag) {
        this.tags.remove(tag);

        return this;
    }

    public ProductRecordInfoBuilder copy(final ProductRecordInfo orig) {
        this.productInfo = orig.getProductInfo();
        setTags(orig.getTags());

        return this;
    }

    public ProductRecordInfoBuilder setTags(String[] tags) {
        this.tags.clear();
        Collections.addAll(this.tags, tags);

        return this;
    }
}
