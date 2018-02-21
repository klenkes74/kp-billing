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

package de.kaiserpfalzedv.billing.api.common.impl;

import java.util.UUID;

import de.kaiserpfalzedv.billing.api.common.EmailAddress;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class NullEmailAddress extends EmailAddressImpl implements EmailAddress {
    public static final UUID ID = UUID.fromString("fe5da1fe-f65c-42cb-a281-634d89ab683a");
    public static final EmailAddress INSTANCE = new NullEmailAddress();
    private static final long serialVersionUID = 1358655594913618286L;
    private static final String NAME = "No Email";
    private static final String ADDRESS = "no-email@no-email.mail";
    private static final String TYPE = "NO-EMAIL";

    private NullEmailAddress() {
        super(ID, NAME, ADDRESS, TYPE);
    }

    @Override
    public void setAddress(String address) {
        // do nothing.
    }

    @Override
    public void setType(String type) {
        // do nothing.
    }
}
