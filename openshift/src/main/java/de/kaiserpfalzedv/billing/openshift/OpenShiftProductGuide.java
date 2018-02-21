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

package de.kaiserpfalzedv.billing.openshift;

import java.io.Serializable;
import java.util.Map;

import javax.inject.Inject;

import de.kaiserpfalzedv.billing.api.guided.GuidingBusinessException;
import de.kaiserpfalzedv.billing.api.guided.NoProductFoundException;
import de.kaiserpfalzedv.billing.api.guided.ProductGuide;
import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.api.guided.ProductRepository;
import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;
import de.kaiserpfalzedv.billing.princeps.api.ProductRecordInfoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class OpenShiftProductGuide implements ProductGuide, Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(OpenShiftProductGuide.class);

    /** The repository to retrieve the product data from. */
    private ProductRepository productRepository;


    @Inject
    public OpenShiftProductGuide(
            final ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    
    @Override
    public ProductRecordInfo getProduct(final RawBaseRecord record) throws GuidingBusinessException {
        ProductInfo product;

        Map<String, String> tags = record.getTags();

        try {
            product = productRepository.retrieveProduct(tags);
        } catch (NoProductFoundException e) {
            throw new GuidingBusinessException(record, e.getMessage(), e);
        }

        return new ProductRecordInfoBuilder()
                .setProductInfo(product)
                .setTags(tags)
                .build();
    }
}
