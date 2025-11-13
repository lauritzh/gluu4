package org.gluu.service.cache;

import jakarta.xml.bind.annotation.XmlEnum;

/**
 * @author yuriyz
 */
@XmlEnum(String.class)
public enum RedisProviderType {
    STANDALONE, CLUSTER, SHARDED, SENTINEL
}
