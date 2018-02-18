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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import de.kaiserpfalzedv.billing.api.common.CurrencyProvider;
import de.kaiserpfalzedv.billing.api.guided.Customer;
import de.kaiserpfalzedv.billing.api.guided.ProductRecordInfo;
import de.kaiserpfalzedv.billing.api.rated.NoTarifFoundException;
import de.kaiserpfalzedv.billing.api.rated.Tarif;
import de.kaiserpfalzedv.billing.api.rated.TarifingRepository;
import de.kaiserpfalzedv.billing.ratio.api.TarifBuilder;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-18
 */
public class OpenShiftTarifingRepository implements TarifingRepository, Serializable {
    private static final long serialVersionUID = 8285756486849531829L;
    private static final Logger LOG = LoggerFactory.getLogger(OpenShiftTarifingRepository.class);

    private final CurrencyProvider currencyProvider;
    private final HashMap<String, Tarif> tarifs = new HashMap<>();

    static {
        if (! SLF4JBridgeHandler.isInstalled()) {
            SLF4JBridgeHandler.install();
        }
    }


    @Inject
    public OpenShiftTarifingRepository(
            final CurrencyProvider currencyProvider
    ) {
        this.currencyProvider = currencyProvider;
    }

    @PostConstruct
    public void init() {
        tarifs.put("POD",
                   new TarifBuilder()
                           .setId(UUID.fromString("451dd39a-a8bf-4063-acf5-e3c89ad98287"))
                           .setName("Base Rate Single POD")
                           .setUnit("Hourly Usage")
                           .setUnitDivisor(BigDecimal.valueOf(60L))
                           .setRate(Money.of(0.1, currencyProvider.getCurrency()))
                           .build()
        );

        tarifs.put("CPU",
                   new TarifBuilder()
                           .setId(UUID.fromString("e3fc0fc2-96f0-439e-9743-dc5cf750093b"))
                           .setName("CPU Usage")
                           .setUnit("mCore")
                           .setUnitDivisor(BigDecimal.valueOf(1000L))
                           .setRate(Money.of(0.5, currencyProvider.getCurrency()))
                           .build()
        );

        tarifs.put("Memory",
                   new TarifBuilder()
                           .setId(UUID.fromString("eb3f8469-89b2-4094-8014-a471f527699d"))
                           .setName("RAM Usage")
                           .setUnit("MB")
                           .setUnitDivisor(BigDecimal.ONE)
                           .setRate(Money.of(0.01, currencyProvider.getCurrency()))
                           .build()
        );

        tarifs.put("Network",
                   new TarifBuilder()
                           .setId(UUID.fromString("ddf90e50-e906-4260-99e6-9aab4f7c1fe7"))
                           .setName("Network Usage")
                           .setUnit("kbit I/O")
                           .setUnitDivisor(BigDecimal.valueOf(1048576L)) // Price for GBit, reported kBit
                           .setRate(Money.of(0.1, currencyProvider.getCurrency()))
                           .build()
        );

        tarifs.put("Storage",
                   new TarifBuilder()
                           .setId(UUID.fromString("86cf3529-9ee5-4f0f-897e-2c31ff7cc545"))
                           .setName("Storage Usage")
                           .setUnit("GB")
                           .setUnitDivisor(BigDecimal.valueOf(1440L)) // Price for days, reported minutes
                           .setRate(Money.of(0.0027777777, currencyProvider.getCurrency()))
                           .build()
        );
    }


    @Override
    public Tarif retrieveTarif(Customer customer, ProductRecordInfo product) throws NoTarifFoundException {
        return tarifs.get(product.getProductName());
    }
}
