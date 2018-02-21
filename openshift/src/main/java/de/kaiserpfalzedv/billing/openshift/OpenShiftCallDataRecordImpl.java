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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.cdr.CallDataRecord;
import de.kaiserpfalzedv.billing.quod.CallDataRecordImpl;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-19
 */
public class OpenShiftCallDataRecordImpl extends CallDataRecordImpl implements CallDataRecord {
    private static final long serialVersionUID = 192375848696453311L;

    private final HashMap<String, String> tags = new HashMap<>();

    OpenShiftCallDataRecordImpl(
            final UUID id,
            final String description,
            final String tarifName,
            final MonetaryAmount tarifRate,
            final String tarifUnit,
            final BigDecimal tarifUnitDivisor,
            final OffsetDateTime meteredTimestamp,
            final Duration meteredDuration,
            final BigDecimal meteredValue,
            final MonetaryAmount amount,
            final Map<String, String> tags
            ) {
        super(id, description, tarifName, tarifRate, tarifUnit, tarifUnitDivisor, meteredTimestamp, meteredDuration, meteredValue, amount);

        if (tags != null) {
            this.tags.putAll(tags);
        }
    }

    public String getCluster() {
        return tags.get("cluster");
    }

    public String getProject() {
        return tags.get("project");
    }

    public String getPod() {
        return tags.get("pod");
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("cluster", getCluster())
                .append("project", getProject())
                .append("pod", getPod())
                .toString();
    }
}
