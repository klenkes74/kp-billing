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

package de.kaiserpfalzedv.billing.notitia.services;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.notitia.api.commands.BaseCommand;
import de.kaiserpfalzedv.billing.notitia.api.commands.CommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-23
 */
public abstract class BaseExecutor<T extends BaseCommand> implements CommandExecutor<T>, Serializable {
    private static final long serialVersionUID = -5082930512804224637L;

    protected static final Logger BUSINESS = LoggerFactory.getLogger("business");
    protected static final Logger OPERATIONS = LoggerFactory.getLogger("operations");

    @PersistenceContext(name = "notitia")
    protected EntityManager em;

    @SuppressWarnings("WeakerAccess")
    public void setEntityManager(@NotNull final EntityManager em) {
        this.em = em;
    }
}
