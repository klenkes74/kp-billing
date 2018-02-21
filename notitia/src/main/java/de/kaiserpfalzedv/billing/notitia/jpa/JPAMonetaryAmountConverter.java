/*
 * Copyright (c) 2015, 2016, Werner Keil and others by the @author tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.kaiserpfalzedv.billing.notitia.jpa;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converter to {@link MonetaryAmount} using the {@link Money} implementation using {@link Money#toString()}
 * and {@link Money#parse(CharSequence)}.
 *
 * This converter was taken from
 * <a href="https://github.com/JavaMoney/javamoney-midas">https://github.com/JavaMoney/javamoney-midas/blob/master/javaee7/src/main/java/org/javamoney/midas/javaee7/jpa/MoneyConverter.java</a>
 *
 * @author Otavio Santana
 * @author Werner Keil
 * @author Roland Lichti
 */
@Converter(autoApply = true)
public class JPAMonetaryAmountConverter implements AttributeConverter<MonetaryAmount, String> {
    private static final Logger LOG = LoggerFactory.getLogger(JPAMonetaryAmountConverter.class);

    @Override
    public String convertToDatabaseColumn(final MonetaryAmount attribute) {

        if (attribute == null) {
            return null;
        }
        String result = Money.from(attribute).toString();

        LOG.trace("Converted '{}' to db data: '{}'", attribute, result);
        return result;
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(final String dbData){
        if (dbData == null) {
            return null;
        }

        MonetaryAmount result = Money.parse(dbData);

        LOG.trace("Converted  db data '{}': '{}'", dbData, result);
        return result;
    }


}
