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

package io.nem.sdk.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.nem.core.utils.ConvertUtils;
import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.mosaic.NetworkCurrencyMosaic;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.transaction.AggregateTransaction;
import io.nem.sdk.model.transaction.AggregateTransactionFactory;
import io.nem.sdk.model.transaction.NamespaceMetadataTransaction;
import io.nem.sdk.model.transaction.NamespaceMetadataTransactionFactory;
import io.nem.sdk.model.transaction.SignedTransaction;
import io.nem.sdk.model.transaction.TransactionAnnounceResponse;
import java.math.BigInteger;
import java.util.Collections;
import org.apache.commons.codec.binary.Base32;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Integration tests around account metadata.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NamespaceMetadataIntegrationTest extends BaseIntegrationTest {


    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    public void addMetadataToNamespace(RepositoryType type) {

        Account testAccount = getTestAccount();
        NamespaceId targetNamespaceId = NetworkCurrencyMosaic.NAMESPACEID;
        System.out.println(targetNamespaceId);

        String message = "This is the message in the Namespace!";
        NamespaceMetadataTransaction transaction =
            new NamespaceMetadataTransactionFactory(
                NetworkType.MIJIN_TEST, testAccount.getPublicAccount(), targetNamespaceId,
                BigInteger.TEN, message
            ).build();

        AggregateTransaction aggregateTransaction = AggregateTransactionFactory
            .createComplete(NetworkType.MIJIN_TEST,
                Collections.singletonList(transaction.toAggregate(getTestPublicAccount()))).build();

        SignedTransaction signedTransaction = testAccount
            .sign(aggregateTransaction, getGenerationHash());

        TransactionAnnounceResponse transactionAnnounceResponse =
            get(getRepositoryFactory(type).createTransactionRepository()
                .announce(signedTransaction));
        assertEquals(
            "packet 9 was pushed to the network via /transaction",
            transactionAnnounceResponse.getMessage());

        AggregateTransaction announceCorrectly = (AggregateTransaction) this
            .validateTransactionAnnounceCorrectly(
                testAccount.getAddress(), signedTransaction.getHash(), type);

        System.out.println(jsonHelper().print(announceCorrectly));

        Assertions.assertEquals(aggregateTransaction.getType(), announceCorrectly.getType());
        Assertions
            .assertEquals(testAccount.getPublicAccount(), announceCorrectly.getSigner().get());
        Assertions.assertEquals(1, announceCorrectly.getInnerTransactions().size());
        Assertions
            .assertEquals(transaction.getType(),
                announceCorrectly.getInnerTransactions().get(0).getType());
        NamespaceMetadataTransaction processedTransaction = (NamespaceMetadataTransaction) announceCorrectly
            .getInnerTransactions()
            .get(0);

        Assertions.assertEquals(transaction.getTargetNamespaceId(),
            processedTransaction.getTargetNamespaceId());
        Assertions.assertEquals(transaction.getValueSizeDelta(),
            processedTransaction.getValueSizeDelta());
        Assertions.assertEquals(transaction.getValueSize(), processedTransaction.getValueSize());

        byte[] actual = processedTransaction.getValue().getBytes();
        System.out.println(ConvertUtils.toHex(actual));
        System.out.println(new Base32().encodeToString(actual));
        System.out.println(new Base32().encodeAsString(actual));
        Assertions.assertEquals("", processedTransaction.getValue());
    }
}
