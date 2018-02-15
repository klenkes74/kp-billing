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

package de.kaiserpfalzedv.billing.api.imported;

import de.kaiserpfalzedv.billing.api.base.BaseBillingRecord;
import de.kaiserpfalzedv.billing.api.guided.GuidedBaseRecord;
import de.kaiserpfalzedv.billing.api.guided.GuidingBusinessExeption;
import de.kaiserpfalzedv.billing.api.guided.GuidingExecutor;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public interface RawBaseRecord extends BaseBillingRecord {
    String getMeteredCustomer();

    String getMeteringProduct();

    GuidedBaseRecord execute(final GuidingExecutor executor) throws GuidingBusinessExeption;
}
