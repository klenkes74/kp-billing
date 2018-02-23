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

package de.kaiserpfalzedv.billing.api.base;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-22
 */
public class DuplicateDataException extends DataException {
    private static final long serialVersionUID = -3594246879918309937L;

    private Object data;


    public DuplicateDataException(final Object duplicate) {
        super(duplicate.getClass(), "Duplicate data for class '" + duplicate.getClass().getSimpleName() + "': "
                + duplicate);

        data = duplicate;
    }

    public DuplicateDataException(final Object duplicate, final Throwable cause) {
        super(duplicate.getClass(), "Duplicate data for class '" + duplicate.getClass().getSimpleName() + "': "
                + duplicate, cause);

        data = duplicate;
    }

    public Object getData() {
        return data;
    }
}
