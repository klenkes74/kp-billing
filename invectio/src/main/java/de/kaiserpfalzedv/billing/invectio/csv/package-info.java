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
 * The Default CSV importer module for all raw data.
 * <p>
 * <p>This is the CSV importer for a semantically enrichted CSV format:</p>
 * <p>
 * <code>
 * 2017-02-14T07:01:37.000000Z;5;8b920084-e403-44b9-8fe9-b5b8ac2d9fb7
 * value;start;end;cluster;project;pod;product
 * ;2017-02-14T01:00:00.000000Z;2017-02-14T01:59:59.999999Z;abbot1;billing;libellum-9xfd3;POD
 * 2.4235435235;2017-02-14T01:59:59.999999Z;;abbot1;billing;libellum-9xfd3;CPU
 * 4096;2017-02-14T01:59:59.999999Z;;abbot1;billing;libellum-9xfd3;Memory
 * 50.341;2017-02-14T01:59:59.999999Z;;abbot1;billing;libellum-9xfd3;Network
 * 5;2017-02-14T01:59:59.999999Z;;abbot1;billing;libellum-9xfd3;Storage
 * </code>
 * <p>
 * The first row contain the parts:
 * <ol>
 * <li>Timestamp of data generation.</li>
 * <li>Number of data records inside the dataset. Maximum number is 100.</li>
 * <li>
 * UUID of this data set. Needed for ommitting duplicates. The UUID may be dropped after
 * 6 Months, so sending the same dataset after more than 6 months may lead to double
 * billing.
 * </li>
 * </ol>
 * <p>
 * <p>The first line solves a common CSV problem: broken transfers. The default CSV file is not capable of being
 * checked that the file is complete. This enhancement allowes to only work with complete files. In addition the UUID
 * provides a practical way for checking for duplicate processing requests.</p>
 * <p>
 * <p>Then the default header line follows:</p>
 * <p>
 * <ol>
 * <li>The metered value (may be empty if there is the start <em>and</em> end time given</li>
 * <li>The start time of the record</li>
 * <li>The end time of the record (may be empty if the metered value is defined</li>
 * <li>
 * The tags as defined in the header line. Every tag increases the billing complexity, so please do
 * not overuse the tags.
 * </li>
 * </ol>
 * <p>
 * <p>Then the data follows.</p>
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-14
 */
package de.kaiserpfalzedv.billing.invectio.csv;