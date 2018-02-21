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

package de.kaiserpfalzedv.billing.ratio.api;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.rated.Tarif;
import org.apache.commons.lang3.builder.Builder;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class TarifBuilder implements Builder<Tarif> {
    private UUID id = UUID.randomUUID();
    private String tarifName;

    private String unit;
    private BigDecimal unitDivisor;
    private MonetaryAmount rate;

    private final HashMap<String, String> tags = new HashMap<>();


    @Override
    public Tarif build() {
        defaults();
        validate();

        try {
            return new TarifImpl(id, tarifName, unit, unitDivisor, rate, tags);
        } finally {
            reset();
        }
    }

    private void defaults() {
        if (unitDivisor == null) {
            unitDivisor = BigDecimal.ONE;
        }
    }

    private void validate() {
        if (isBlank(tarifName)) {
            throw new IllegalStateException("Can't create a Tarif without name");
        }

        if (isBlank(unit)) {
            throw new IllegalStateException("Can't create a Tarif without unit description");
        }

        if (rate == null) {
            throw new IllegalStateException("Can't create a Tarif without defining the rate");
        }
    }

    private void reset() {
        id = UUID.randomUUID();
        tarifName = null;
        unit = null;
        unitDivisor = null;
        rate = null;
        tags.clear();
    }


    public TarifBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public TarifBuilder withName(String tarifName) {
        this.tarifName = tarifName;
        return this;
    }

    public TarifBuilder withUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public TarifBuilder withUnitDivisor(BigDecimal unitDivisor) {
        this.unitDivisor = unitDivisor;
        return this;
    }

    public TarifBuilder withRate(MonetaryAmount rate) {
        this.rate = rate;
        return this;
    }

    public TarifBuilder withTags(@NotNull final Map<String, String> tags) {
        this.tags.clear();

        if (tags != null) {
            this.tags.putAll(tags);
        }

        return this;
    }

    public TarifBuilder copy(final Tarif orig) {
        this.id = orig.getId();
        this.tarifName = orig.getName();
        this.unit = orig.getUnit();
        this.unitDivisor = orig.getUnitDivisor();
        this.rate = orig.getRate();
        this.tags.putAll(orig.getTags());

        return this;
    }
}
