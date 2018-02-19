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

import de.kaiserpfalzedv.billing.api.cdr.CallDataRecordGenerator;
import de.kaiserpfalzedv.billing.api.rated.RatedMeteredRecord;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-19
 */
public class OpenShiftCDRGenerator implements CallDataRecordGenerator<RatedMeteredRecord, OpenShiftCallDataRecordImpl>,
                                              Serializable {
    @Override
    public OpenShiftCallDataRecordImpl generate(RatedMeteredRecord record) {
        return new OpenShiftCallDataRecordImpl(
                record.getId(),
                String.format("%s: %s",
                              record.getTarifName(), record.getTags().get("pod"), record.getTags().get("project")),
                record.getTarifName(),
                record.getRate(),
                record.getUnit(),
                record.getUnitDivisor(),
                record.getMeteredTimestamp(),
                record.getMeteredDuration(),
                record.getMeteredValue(),
                record.getAmount(),
                record.getTags()
        );
    }
}
