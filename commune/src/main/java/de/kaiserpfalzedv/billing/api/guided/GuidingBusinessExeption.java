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

package de.kaiserpfalzedv.billing.api.guided;

import de.kaiserpfalzedv.billing.api.base.BillingRecordholdingBusinessException;
import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-15
 */
public class GuidingBusinessExeption extends BillingRecordholdingBusinessException {
    private static final long serialVersionUID = -8737903039233392683L;

    public GuidingBusinessExeption(final RawBaseRecord billingRecord, final String message) {
        super(billingRecord, message);
    }

    public GuidingBusinessExeption(final RawBaseRecord billingRecord, final String message, final Throwable cause) {
        super(billingRecord, message, cause);
    }

    public GuidingBusinessExeption(
            final RawBaseRecord billingRecord, final String message,
            final Throwable cause,
            final boolean enableSuppression, final boolean writableStackTrace
    ) {
        super(billingRecord, message, cause, enableSuppression, writableStackTrace);
    }
}
