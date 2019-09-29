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
import io.nem.sdk.model.transaction.AccountMetadataTransaction;
import io.nem.sdk.model.transaction.AccountMetadataTransactionFactory;
import io.nem.sdk.model.transaction.AggregateTransaction;
import io.nem.sdk.model.transaction.AggregateTransactionFactory;
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
public class AccountMetadataIntegrationTest extends BaseIntegrationTest {

    private Account testAccount = config().getTestAccount();

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    public void addMetadataToAccount(RepositoryType type) {

        String message = "This is the message for this account! 汉字" + new Double(
            Math.floor(Math.random() * 10000))
            .intValue();
        System.out.println(
            "Storing message '" + message + "' in account metadata " + testAccount.getAddress());

        AccountMetadataTransaction transaction =
            new AccountMetadataTransactionFactory(
                getNetworkType(), testAccount.getPublicAccount(), BigInteger.valueOf(71),
                message
            ).build();

        AggregateTransaction aggregateTransaction = AggregateTransactionFactory
            .createComplete(getNetworkType(),
                Collections.singletonList(transaction.toAggregate(testAccount.getPublicAccount())))
            .build();

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

        Assertions.assertEquals(aggregateTransaction.getType(), announceCorrectly.getType());
        Assertions
            .assertEquals(testAccount.getPublicAccount(), announceCorrectly.getSigner().get());
        Assertions.assertEquals(1, announceCorrectly.getInnerTransactions().size());
        Assertions
            .assertEquals(transaction.getType(),
                announceCorrectly.getInnerTransactions().get(0).getType());
        AccountMetadataTransaction processedTransaction = (AccountMetadataTransaction) announceCorrectly
            .getInnerTransactions()
            .get(0);
        Assertions.assertEquals(transaction.getValueSizeDelta(),
            processedTransaction.getValueSizeDelta());
        Assertions.assertEquals(transaction.getValueSize(), processedTransaction.getValueSize());

        Assertions.assertEquals(message, processedTransaction.getValue());
    }
}
