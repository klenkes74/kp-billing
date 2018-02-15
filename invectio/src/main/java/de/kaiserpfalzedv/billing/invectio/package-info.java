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

/**
 * The importer module for all raw data. <p>This module will transfer the raw data to the
 * {@link de.kaiserpfalzedv.billing.api.imported.RawMeteredRecord} or
 * {@link de.kaiserpfalzedv.billing.api.imported.RawTimedRecord} needed as input for the next step.</p>
 * <p>
 * <p>We provide at least a CSV importer for a semantically enrichted CSV format.</p>
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-14
 */
package de.kaiserpfalzedv.billing.invectio;