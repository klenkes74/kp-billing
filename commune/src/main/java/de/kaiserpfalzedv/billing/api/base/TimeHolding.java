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

import java.time.Duration;
import java.time.OffsetDateTime;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public interface TimeHolding {
    /**
     * The timestamp this record will be billed for. For a timed record it should be the start or end time (depending
     * if the tarif of a timely defined tarif is determined by the start or the end of the duration).
     *
     * @return The timestamp of the record
     */
    OffsetDateTime getMeteredTimestamp();

    /**
     * @return The duration of the billed event
     */
    Duration getMeteredDuration();
}
