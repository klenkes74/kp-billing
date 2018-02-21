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

package de.kaiserpfalzedv.billing.api.cdr;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.base.TimeHolding;
import de.kaiserpfalzedv.billing.api.base.ValueHolding;
import de.kaiserpfalzedv.billing.api.common.Identifiable;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-13
 */
public interface CallDataRecord extends Identifiable, TimeHolding, ValueHolding, Serializable {
    /**
     * The description contains information for the customer to identify the reason. It could contain the telephone
     * number or the server name of the billed item.
     *
     * @return the textual description
     */
    String getDescription();


    /**
     * @return The printable name of the tarif
     */
    String getTarifName();

    /**
     * @return The rate of the tarif
     */
    MonetaryAmount getTarifRate();

    /**
     * @return The printable unit name of the tarif
     */
    String getTarifUnit();

    /**
     * The unit divisor will be calculated inside the price. If the divisor is "10", the rate is for a tenth of the
     * unit.
     * 
     * @return The divisor for a rate
     */
    BigDecimal getTarifUnitDivisor();

    /**
     * @return The price tag of this CDR
     */
    MonetaryAmount getAmount();
}
