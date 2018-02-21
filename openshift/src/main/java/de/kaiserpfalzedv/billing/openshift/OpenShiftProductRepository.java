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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.enterprise.inject.Default;

import de.kaiserpfalzedv.billing.api.guided.NoProductFoundException;
import de.kaiserpfalzedv.billing.api.guided.ProductInfo;
import de.kaiserpfalzedv.billing.api.guided.ProductRepository;
import de.kaiserpfalzedv.billing.princeps.api.ProductInfoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
@Default
public class OpenShiftProductRepository implements ProductRepository {
    private static final Logger LOG = LoggerFactory.getLogger(OpenShiftProductRepository.class);

    private static final HashMap<String, ProductInfo> products = new HashMap<>();
    static {
        products.put("POD",
                new ProductInfoBuilder()
                        .setId(UUID.fromString("11493085-2cb6-4c34-adf9-f80fb798ff90"))
                        .setName("POD")
                        .setTags(Arrays.asList("cluster","project", "pod"))
                        .build()
        );

        products.put("CPU",
                     new ProductInfoBuilder()
                             .setId(UUID.fromString("cf24d806-f303-4c69-85c7-67f5f93a8416"))
                             .setName("CPU Usage")
                             .setTags(Arrays.asList("cluster","project", "pod"))
                             .build()
        );

        products.put("Memory",
                     new ProductInfoBuilder()
                             .setId(UUID.fromString("79b8b0d3-45d2-465f-b147-3a9ed13e5970"))
                             .setName("RAM Usage")
                             .setTags(Arrays.asList("cluster","project", "pod"))
                             .build()
        );

        products.put("Network",
                     new ProductInfoBuilder()
                             .setId(UUID.fromString("4ebd7650-7424-4fa0-980b-0e930cbe7866"))
                             .setName("Network Usage")
                             .setTags(Arrays.asList("cluster","project", "pod"))
                             .build()
        );

        products.put("Storage",
                     new ProductInfoBuilder()
                             .setId(UUID.fromString("1016848d-cf44-4cb1-b214-1d78cec2d3db"))
                             .setName("Storage Usage")
                             .setTags(Arrays.asList("cluster","project", "pod"))
                             .build()
        );
    }

    @Override
    public ProductInfo retrieveProduct(Map<String, String> tags) throws NoProductFoundException {
        if (! products.containsKey(tags.get("product"))) {
            throw new NoProductFoundException(tags);
        }

        ProductInfo result = products.get(tags.get("product"));
        LOG.trace("Guided to product: {}", result);
        return result;
    }
}
