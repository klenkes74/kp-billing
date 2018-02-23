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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.BillingBusinessException;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class NoCustomerFoundException extends BillingBusinessException {
    private static final long serialVersionUID = -6624501720871047544L;

    private final HashMap<String, String> tags = new HashMap<>();

    public NoCustomerFoundException(final UUID id) {
        super("No customer found with id: " + id.toString());

        this.tags.put("id", id.toString());
    }

    public NoCustomerFoundException(final UUID id, final Throwable cause) {
        super("No customer found with id: " + id.toString(), cause);

        this.tags.put("id", id.toString());
    }

    public NoCustomerFoundException(final Map<String, String> tags) {
        super("No customer found for this record: " + tags);

        this.tags.putAll(tags);
    }

    public NoCustomerFoundException(final Map<String, String> tags, final Throwable cause) {
        super("No customer found for this record: " + tags, cause);

        this.tags.putAll(tags);
    }


    public HashMap<String, String> getTags() {
        return tags;
    }
}
