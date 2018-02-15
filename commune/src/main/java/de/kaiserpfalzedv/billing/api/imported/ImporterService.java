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

import java.io.InputStreamReader;
import java.util.List;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-14
 */
public interface ImporterService {
    /**
     * Imports a stream containing CSV data.
     *
     * @param reader The stream containing the CSV data.
     *
     * @return a list of the records contained in the stream.
     *
     * @throws IncompatibleImportDataException If at least one of the records did not match the specification.
     * @throws IncompleteImportDataException   If the stream is not complete. A stream is complete if it contains exactly
     *                                         the defined number of records.
     */
    List<? extends RawBaseRecord> execute(InputStreamReader reader)
            throws IncompatibleImportDataException, IncompleteImportDataException;
}
