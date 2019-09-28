/*
 * Copyright 2019 NEM
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.nem.sdk.model.metadata;

import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.transaction.UInt64Id;
import java.math.BigInteger;
import java.util.Optional;

/**
 * A mosaic describes an instance of a mosaic definition. Mosaics can be transferred by means of a
 * transfer transaction.
 */
public class MetadataEntry {

    /**
     * The composite hash
     */
    private final String compositeHash;
    /**
     * The metadata sender's public key
     */
    private final String senderPublicKey;

    /**
     * The metadata target public key
     */
    private final String targetPublicKey;

    /**
     * The key scoped to source, target and type
     */
    private final BigInteger scopedMetadataKey;

    /**
     * The metadata type
     */
    private final MetadataType metadataType;

    /**
     * The metadata value size
     */
    private final Integer valueSize;

    /**
     * The metadata value
     */
    private final String value;

    /**
     * The target {@link io.nem.sdk.model.mosaic.MosaicId} (when metadata type is MOSAIC)
     *
     * or
     *
     * {@link io.nem.sdk.model.namespace.NamespaceId} (when metadata type is MOSAIC)
     */
    private final Optional<UInt64Id> targetId;

    public MetadataEntry(String compositeHash, String senderPublicKey, String targetPublicKey,
        BigInteger scopedMetadataKey, MetadataType metadataType, Integer valueSize,
        String value, Optional<String> targetId) {
        this.compositeHash = compositeHash;
        this.senderPublicKey = senderPublicKey;
        this.targetPublicKey = targetPublicKey;
        this.scopedMetadataKey = scopedMetadataKey;
        this.metadataType = metadataType;
        this.valueSize = valueSize;
        this.value = value;
        this.targetId = resolveTargetId(targetId, metadataType);
    }

    private Optional<UInt64Id> resolveTargetId(Optional<String> targetId,
        MetadataType metadataType) {
        if (!targetId.isPresent() && metadataType == MetadataType.ACCOUNT) {
            return Optional.empty();
        }
        if (metadataType == MetadataType.NAMESPACE) {
            return targetId.map(s -> NamespaceId.createFromId(new BigInteger(s)));
        }
        if (metadataType == MetadataType.MOSAIC) {
            return targetId.map(s -> new MosaicId(new BigInteger(s)));
        }
        throw new IllegalArgumentException(
            "Cannot resolve target id for metadata type " + metadataType + " and value "
                + targetId);
    }

    public String getCompositeHash() {
        return compositeHash;
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public String getTargetPublicKey() {
        return targetPublicKey;
    }

    public BigInteger getScopedMetadataKey() {
        return scopedMetadataKey;
    }

    public MetadataType getMetadataType() {
        return metadataType;
    }

    public Integer getValueSize() {
        return valueSize;
    }

    public String getValue() {
        return value;
    }

    public Optional<UInt64Id> getTargetId() {
        return targetId;
    }
}
