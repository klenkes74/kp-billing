package de.kaiserpfalzedv.billing.api.guided;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-16
 */
public interface ProductHolding {
    ProductRecordInfo getProductInfo();

    String getProductName();
}
