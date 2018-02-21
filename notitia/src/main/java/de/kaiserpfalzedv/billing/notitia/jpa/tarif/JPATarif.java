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

package de.kaiserpfalzedv.billing.notitia.jpa.tarif;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.rated.Tarif;
import de.kaiserpfalzedv.billing.notitia.jpa.JPAIdentifiable;
import de.kaiserpfalzedv.billing.notitia.jpa.JPAMonetaryAmountConverter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-21
 */
@Entity(name = "Tarifs")
@Table(name = "TARIFS")
@Cacheable
public class JPATarif extends JPAIdentifiable implements Tarif {
    private static final long serialVersionUID = 1871642500746516433L;

    private static final CurrencyUnit DEFAULT_CURRENCY = Monetary.getCurrency("EUR");

    @Column(name = "NAME_")
    private String name;

    @Column(name = "UNIT_")
    private String unit;

    @Digits(integer=10, fraction=5)
    @Column(name = "UNIT_DIVISOR_", precision = 10, scale = 2)
    private BigDecimal unitDivisor;

    @Column(name = "RATE_")
    @Convert(converter = JPAMonetaryAmountConverter.class)
    private MonetaryAmount rate;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @MapKeyColumn(name="KEY_")
    @Column(name="VALUE_")
    @CollectionTable(name="TARIF_TAGS", joinColumns=@JoinColumn(name="TARIF_"))
    private Map<String,String> tags = new HashMap<>(); // maps from attribute name to value


    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public BigDecimal getUnitDivisor() {
        return unitDivisor;
    }

    public void setUnitDivisor(BigDecimal unitDivisor) {
        this.unitDivisor = unitDivisor;
    }

    @Override
    public MonetaryAmount getRate() {
        return rate;
    }

    public void setRate(MonetaryAmount rate) {
        this.rate = rate;
    }

    @Override
    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    public void setTags(@NotNull final Map<String, String> tags) {
        if (this.tags != null) {
            this.tags.clear();
        } else {
            this.tags = new HashMap<>(tags.size());
        }
        
        this.tags.putAll(tags);
    }

    @Override
    public CurrencyUnit getCurrency() {
        if (rate == null) {
            return DEFAULT_CURRENCY;
        }

        return rate.getCurrency();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("name", name)
                .append("unit", unit)
                .append("unitDivisor", unitDivisor)
                .append("rate", rate)
                .toString();
    }
}
