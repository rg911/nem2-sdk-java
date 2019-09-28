/*
 * Copyright 2018 NEM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.nem.sdk.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.nem.core.crypto.PublicKey;
import io.nem.sdk.api.AccountRepository;
import io.nem.sdk.api.QueryParams;
import io.nem.sdk.api.RepositoryCallException;
import io.nem.sdk.model.account.AccountInfo;
import io.nem.sdk.model.account.AccountNames;
import io.nem.sdk.model.account.AccountType;
import io.nem.sdk.model.account.Address;
import io.nem.sdk.model.account.MultisigAccountGraphInfo;
import io.nem.sdk.model.account.MultisigAccountInfo;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.transaction.AggregateTransaction;
import io.nem.sdk.model.transaction.Transaction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//TODO BROKEN!
class AccountRepositoryIntegrationTest extends BaseIntegrationTest {


    public AccountRepository getAccountRepository(RepositoryType type) {
        return getRepositoryFactory(type).createAccountRepository();
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void getAccountInfo(RepositoryType type) {
        AccountInfo accountInfo =
            get(this.getAccountRepository(type)
                .getAccountInfo(this.getTestAccount().getPublicAccount().getAddress()));

        assertEquals(this.getTestAccount().getPublicKey(), accountInfo.getPublicKey());
        assertEquals(AccountType.UNLINKED, accountInfo.getAccountType());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void getAccountsInfoFromAddresses(RepositoryType type) {
        List<AccountInfo> accountInfos =
            get(this.getAccountRepository(type)
                .getAccountsInfoFromAddresses(
                    Collections.singletonList(this.getTestAccountAddress())));

        assertEquals(1, accountInfos.size());
        assertEquals(this.getTestAccount().getAddress(), accountInfos.get(0).getAddress());
        assertEquals(AccountType.UNLINKED, accountInfos.get(0).getAccountType());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void getAccountsNamesFromAddresses(RepositoryType type) {
        List<AccountNames> accountNames = get(
            this.getAccountRepository(type).getAccountsNamesFromAddresses(
                Collections.singletonList(this.getTestAccountAddress())));

        assertEquals(1, accountNames.size());
        assertEquals(this.config().getTestAccountAddress(),
            accountNames.get(0).getAddress().plain());
        assertNotNull(accountNames.get(0).getNames());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void getAccountsInfoFromPublicKeys(RepositoryType type) {
        List<AccountInfo> accountInfos = get(this.getAccountRepository(type)
            .getAccountsInfoFromPublicKeys(Collections.singletonList(
                PublicKey.fromHexString(this.config().getTestAccount().getPublicKey()))));

        assertEquals(1, accountInfos.size());
        assertEquals(this.getTestAccount().getPublicKey(), accountInfos.get(0).getPublicKey());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void getAccountsNamesFromPublicKeys(RepositoryType type) {
        List<AccountNames> accountNames = get(this.getAccountRepository(type)
            .getAccountsNamesFromPublicKeys(Collections.singletonList(
                PublicKey.fromHexString(this.getTestAccount().getPublicKey())))
        );

        assertEquals(1, accountNames.size());
        assertEquals(this.config().getTestAccountAddress(),
            accountNames.get(0).getAddress().plain());
        assertEquals(0, accountNames.get(0).getNames().size());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void getAccountInfoNotExisting(RepositoryType type) {
        AccountRepository accountHttp = getRepositoryFactory(type).createAccountRepository();
        Address addressObject = Address
            .createFromPublicKey("67F69FA4BFCD158F6E1AF1ABC82F725F5C5C4710D6E29217B12BE66397435DFB",
                NetworkType.MIJIN_TEST);

        RepositoryCallException exception = Assertions
            .assertThrows(RepositoryCallException.class,
                () -> get(accountHttp.getAccountInfo(addressObject)));
        Assertions.assertEquals(
            "ApiException: Not Found - 404 - ResourceNotFound - no resource exists with id 'SCGEGBEHICF5PPOGIP2JSCQ5OYGZXOOJF7KUSUQJ'",
            exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void getMultisigAccountInfo(RepositoryType type) {
        MultisigAccountInfo multisigAccountInfo = get(this.getAccountRepository(type)
            .getMultisigAccountInfo(
                Address.createFromRawAddress("SBCPGZ3S2SCC3YHBBTYDCUZV4ZZEPHM2KGCP4QXX"))
        );
        assertEquals(
            "B694186EE4AB0558CA4AFCFDD43B42114AE71094F5A1FC4A913FE9971CACD21D",
            multisigAccountInfo.getAccount().getPublicKey().toString());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void getMultisigAccountGraphInfo(RepositoryType type) {
        MultisigAccountGraphInfo multisigAccountGraphInfos = get(this.getAccountRepository(type)
            .getMultisigAccountGraphInfo(
                Address.createFromRawAddress("SBCPGZ3S2SCC3YHBBTYDCUZV4ZZEPHM2KGCP4QXX"))
        );

        assertEquals(
            new HashSet<>(Arrays.asList(-2, -1, 0, 1)),
            multisigAccountGraphInfos.getLevelsNumber());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void transactions(RepositoryType type) {
        List<Transaction> transactions = get(
            this.getAccountRepository(type).transactions(this.getTestPublicAccount()));
        assertEquals(10, transactions.size());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void transactionsWithPagination(RepositoryType type) {
        List<Transaction> transactions = get(
            this.getAccountRepository(type).transactions(this.getTestPublicAccount()));

        assertEquals(10, transactions.size());

        List<Transaction> nextTransactions =
            get(this.getAccountRepository(type)
                .transactions(
                    this.getTestPublicAccount(),
                    new QueryParams(11,
                        transactions.get(0).getTransactionInfo().get().getId().get())));

        assertEquals(11, nextTransactions.size());
        assertEquals(
            transactions.get(1).getTransactionInfo().get().getHash(),
            nextTransactions.get(0).getTransactionInfo().get().getHash());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void transactionsWithPaginationManyTransactions(RepositoryType type) {
        //Testing that many transaction can be at at least parsed.
        List<Transaction> transactions =
            get(this.getAccountRepository(type)
                .transactions(this.getTestPublicAccount(), new QueryParams(100, null)));
        assertTrue(transactions.size() <= 100);

        transactions.forEach(transaction -> assertTransaction(transaction, false));
    }

    private void assertTransaction(Transaction transaction, boolean outgoingTransactions) {

        Assert.assertNotNull(transaction.getType());
        Assert.assertTrue(transaction.getTransactionInfo().isPresent());
        Assert.assertEquals(getNetworkType(), transaction.getNetworkType());
        if (outgoingTransactions) {
            Assert.assertEquals(getTestAccount().getAddress(),
                transaction.getSigner().get().getAddress());
        }

        Assert.assertTrue(transaction.getSignature().isPresent());
        Assert.assertTrue(transaction.getSignatureBytes().isPresent());
        Assert.assertNotNull(transaction.getMaxFee());
        Assert.assertNotNull(transaction.getVersion());
        Assert.assertNotNull(transaction.getDeadline());

    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void incomingTransactions(RepositoryType type) {
        List<Transaction> transactions = get(
            this.getAccountRepository(type).incomingTransactions(this.getTestPublicAccount()));

        transactions.forEach(transaction -> assertTransaction(transaction, false));
    }


    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void outgoingTransactions(RepositoryType type) {
        List<Transaction> transactions = get(
            this.getAccountRepository(type).outgoingTransactions(this.getTestPublicAccount()));
        System.out.println(transactions.size());
        transactions.forEach(transaction -> assertTransaction(transaction, true));
    }


    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void aggregateBondedTransactions(RepositoryType type) {
        List<AggregateTransaction> transactions = get(this.getAccountRepository(type)
            .aggregateBondedTransactions(this.getTestPublicAccount())
        );

        assertEquals(0, transactions.size());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void unconfirmedTransactions(RepositoryType type) {
        List<Transaction> transactions = get(this.getAccountRepository(type)
            .unconfirmedTransactions(this.getTestPublicAccount()));
        assertEquals(0, transactions.size());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void throwExceptionWhenBlockDoesNotExists(RepositoryType type) {
        RepositoryCallException exception = Assertions
            .assertThrows(RepositoryCallException.class, () -> get(this.getAccountRepository(type)
                .getAccountInfo(
                    Address.createFromRawAddress("SAAAAACB67D4HPGIMIHPNSRYRJRT7DOBGWZY"))));
        Assertions.assertEquals(
            "ApiException: Conflict - 409 - InvalidArgument - accountId has an invalid format",
            exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void throwExceptionWhenInvalidFormat(RepositoryType type) {
        RepositoryCallException exception = Assertions
            .assertThrows(RepositoryCallException.class, () -> get(this.getAccountRepository(type)
                .getAccountInfo(
                    Address.createFromRawAddress("SARDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY"))));
        Assertions.assertEquals(
            "ApiException: Not Found - 404 - ResourceNotFound - no resource exists with id 'SARDGFTDLLCB67D4HPGIMIHPNSRYRJRT7DOBGWZY'",
            exception.getMessage());
    }


}
