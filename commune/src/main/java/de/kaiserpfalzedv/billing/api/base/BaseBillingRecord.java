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

package de.kaiserpfalzedv.billing.api.base;

import java.io.Serializable;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Map;

import de.kaiserpfalzedv.billing.api.common.Identifiable;

/**
 * The base billing record. This is the base data the billing engine will rely on. There may be additional records
 * defined on this base record.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-09
 */
public interface BaseBillingRecord extends Identifiable, Serializable {
    String getMeteringId();

    OffsetDateTime getValueDate();

    OffsetDateTime getRecordedDate();

    OffsetDateTime getImportedDate();

    OffsetDateTime getMeteredTimestamp();

    Duration getMeteredDuration();

    Map<String, String> getTags();
}
