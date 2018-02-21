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

package de.kaiserpfalzedv.billing.api.common.impl;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import de.kaiserpfalzedv.billing.api.common.CurrencyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-12
 */
public class DefaultCurrencyProvider implements CurrencyProvider {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCurrencyProvider.class);

    private static final CurrencyUnit DEFAULT_CURRENCY = Monetary.getCurrency("EUR");

    private CurrencyUnit defaultCurrency = DEFAULT_CURRENCY;


    public DefaultCurrencyProvider() {
        LOG.info("Default currency is: {}", defaultCurrency);
    }


    public CurrencyUnit getCurrency() {
        return defaultCurrency;
    }

    public void setCurrency(final CurrencyUnit currency) {
        LOG.info("Setting default currency to: {}", currency);

        this.defaultCurrency = currency;
    }
}
