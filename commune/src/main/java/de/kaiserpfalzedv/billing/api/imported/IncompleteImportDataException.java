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

import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The exception for incomplete data streams. If the data is missing records, this exception is being thrown.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-14
 */
public class IncompleteImportDataException extends ImportingException {
    private static final long serialVersionUID = 5528557870787949489L;

    private final UUID transaction;
    private final int expectedRecords;
    private final int receivedRecords;

    public IncompleteImportDataException(final UUID transaction, final int expectedRecords, final int receivedRecords) {
        super("Incomplete import for transaction " + transaction.toString()
                      + ". Only " + receivedRecords + " of " + expectedRecords + " expected records received!");

        this.transaction = transaction;
        this.expectedRecords = expectedRecords;
        this.receivedRecords = receivedRecords;
    }


    public UUID getTransaction() {
        return transaction;
    }

    public int getExpectedRecords() {
        return expectedRecords;
    }

    public int getReceivedRecords() {
        return receivedRecords;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("transaction", transaction)
                .append("expectedRecords", expectedRecords)
                .append("receivedRecords", receivedRecords)
                .toString();
    }
}
