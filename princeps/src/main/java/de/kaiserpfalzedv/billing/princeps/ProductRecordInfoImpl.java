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

package de.kaiserpfalzedv.billing.princeps;

import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class ProductRecordInfoImpl implements ProductRecordInfo {
    private static final long serialVersionUID = -3474606968988268416L;

    private final ProductInfo productInfo;
    private final String[] tags;


    ProductRecordInfoImpl(ProductInfo productInfo, String[] tags) {
        this.productInfo = productInfo;
        this.tags = tags;
    }

    @Override
    public ProductInfo getProductInfo() {
        return productInfo;
    }

    @Override
    public String getProductName() {
        return productInfo.getName();
    }

    @Override
    public String[] getTagTitles() {
        return productInfo.getTags();
    }


    @Override
    public String[] getTags() {
        return tags;
    }

    @Override
    public boolean matchTags(String[] info) {
        if (productInfo.getTags().length != info.length || productInfo.getTags().length != tags.length) {
            return false;
        }

        for (int i = 0; i < tags.length; i++) {
            if (!tags[i].equals(info[i])) {
                return false;
            }
        }

        return true;
    }
}
