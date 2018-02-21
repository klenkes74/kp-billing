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

package de.kaiserpfalzedv.billing.notitia.jpa;

import java.time.Duration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-21
 */
@Converter(autoApply = true)
public class JPADurationConverter implements AttributeConverter<Duration, String> {
    private static final Logger LOG = LoggerFactory.getLogger(JPADurationConverter.class);

    @Override
    public String convertToDatabaseColumn(Duration attribute) {
        String result = attribute.toString();

        LOG.trace("Converted {} to db data: '{}'", attribute, result);
        return result;
    }

    @Override
    public Duration convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        Duration result = Duration.parse(dbData);

        LOG.trace("Converted from db data '{}': {}", dbData, result);
        return result;
    }
}
