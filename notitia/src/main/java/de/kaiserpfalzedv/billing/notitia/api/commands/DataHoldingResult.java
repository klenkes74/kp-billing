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

package de.kaiserpfalzedv.billing.notitia.api.commands;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.common.Identifiable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-22
 */
public class DataHoldingResult<T extends Serializable> extends AbstractBaseCommandImpl {
    private static final long serialVersionUID = -1753833433387827444L;


    private T data;


    @Deprecated
    public DataHoldingResult() {}

    public DataHoldingResult(@NotNull final T data) {
        this.data = data;

        if (Identifiable.class.isAssignableFrom(data.getClass())) {
            setObjectId(((Identifiable) data).getId());
        }
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("data", data)
                .toString();
    }
}
