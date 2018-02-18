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

import java.util.Map;

import de.kaiserpfalzedv.billing.api.BillingBusinessException;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class NoProductFoundException extends BillingBusinessException {
    private static final long serialVersionUID = 1969846108984588890L;

    public NoProductFoundException(final Map<String, String> tags) {
        super("No customer found for this record: " + tags);
    }

    public NoProductFoundException(final Map<String, String> tags, final Throwable cause) {
        super("No customer found for this record: " + tags, cause);
    }
}
