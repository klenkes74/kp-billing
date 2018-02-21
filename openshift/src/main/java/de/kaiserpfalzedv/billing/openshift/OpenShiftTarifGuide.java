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

package de.kaiserpfalzedv.billing.openshift;

import java.io.Serializable;

import javax.inject.Inject;

import de.kaiserpfalzedv.billing.api.guided.GuidedBaseRecord;
import de.kaiserpfalzedv.billing.api.rated.NoTarifFoundException;
import de.kaiserpfalzedv.billing.api.rated.Tarif;
import de.kaiserpfalzedv.billing.api.rated.TarifingException;
import de.kaiserpfalzedv.billing.api.rated.TarifingGuide;
import de.kaiserpfalzedv.billing.api.rated.TarifingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-17
 */
public class OpenShiftTarifGuide implements TarifingGuide, Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(OpenShiftTarifGuide.class);

    /** The repository to retrieve the tarif from. */
    private TarifingRepository tarifingRepository;


    @Inject
    public OpenShiftTarifGuide(
            final TarifingRepository tarifingRepository
    ) {
        this.tarifingRepository = tarifingRepository;
    }

    
    @Override
    public Tarif getTarif(final GuidedBaseRecord record) throws TarifingException {
        try {
            return tarifingRepository.retrieveTarif(record.getCustomer(), record.getProductInfo());
        } catch (NoTarifFoundException e) {
            throw new TarifingException(record, e.getMessage(), e);
        }
    }
}
