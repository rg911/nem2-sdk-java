/*
 * NIS2 API Endpoints
 * This document defines all the nis2 api endpoints
 *
 * OpenAPI spec version: 0.9.1
 * Contact: guillem@nemeurope.eu
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.model.UInt64DTO;
import java.io.IOException;

/**
 * SecretLockInfo
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-03-30T14:24:32.577Z")
public class SecretLockInfo {
  @SerializedName("account")
  private String account = null;

  @SerializedName("accountAddress")
  private String accountAddress = null;

  @SerializedName("mosaicId")
  private UInt64DTO mosaicId = null;

  @SerializedName("amount")
  private UInt64DTO amount = null;

  @SerializedName("height")
  private UInt64DTO height = null;

  @SerializedName("status")
  private Integer status = null;

  @SerializedName("hashAlgorithm")
  private Integer hashAlgorithm = null;

  @SerializedName("secret")
  private String secret = null;

  @SerializedName("recipient")
  private String recipient = null;

  public SecretLockInfo account(String account) {
    this.account = account;
    return this;
  }

   /**
   * Get account
   * @return account
  **/
  @ApiModelProperty(required = true, value = "")
  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public SecretLockInfo accountAddress(String accountAddress) {
    this.accountAddress = accountAddress;
    return this;
  }

   /**
   * Get accountAddress
   * @return accountAddress
  **/
  @ApiModelProperty(required = true, value = "")
  public String getAccountAddress() {
    return accountAddress;
  }

  public void setAccountAddress(String accountAddress) {
    this.accountAddress = accountAddress;
  }

  public SecretLockInfo mosaicId(UInt64DTO mosaicId) {
    this.mosaicId = mosaicId;
    return this;
  }

   /**
   * Get mosaicId
   * @return mosaicId
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getMosaicId() {
    return mosaicId;
  }

  public void setMosaicId(UInt64DTO mosaicId) {
    this.mosaicId = mosaicId;
  }

  public SecretLockInfo amount(UInt64DTO amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Get amount
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getAmount() {
    return amount;
  }

  public void setAmount(UInt64DTO amount) {
    this.amount = amount;
  }

  public SecretLockInfo height(UInt64DTO height) {
    this.height = height;
    return this;
  }

   /**
   * Get height
   * @return height
  **/
  @ApiModelProperty(required = true, value = "")
  public UInt64DTO getHeight() {
    return height;
  }

  public void setHeight(UInt64DTO height) {
    this.height = height;
  }

  public SecretLockInfo status(Integer status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(required = true, value = "")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public SecretLockInfo hashAlgorithm(Integer hashAlgorithm) {
    this.hashAlgorithm = hashAlgorithm;
    return this;
  }

   /**
   * Get hashAlgorithm
   * @return hashAlgorithm
  **/
  @ApiModelProperty(required = true, value = "")
  public Integer getHashAlgorithm() {
    return hashAlgorithm;
  }

  public void setHashAlgorithm(Integer hashAlgorithm) {
    this.hashAlgorithm = hashAlgorithm;
  }

  public SecretLockInfo secret(String secret) {
    this.secret = secret;
    return this;
  }

   /**
   * Get secret
   * @return secret
  **/
  @ApiModelProperty(required = true, value = "")
  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public SecretLockInfo recipient(String recipient) {
    this.recipient = recipient;
    return this;
  }

   /**
   * Get recipient
   * @return recipient
  **/
  @ApiModelProperty(required = true, value = "")
  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SecretLockInfo secretLockInfo = (SecretLockInfo) o;
    return Objects.equals(this.account, secretLockInfo.account) &&
        Objects.equals(this.accountAddress, secretLockInfo.accountAddress) &&
        Objects.equals(this.mosaicId, secretLockInfo.mosaicId) &&
        Objects.equals(this.amount, secretLockInfo.amount) &&
        Objects.equals(this.height, secretLockInfo.height) &&
        Objects.equals(this.status, secretLockInfo.status) &&
        Objects.equals(this.hashAlgorithm, secretLockInfo.hashAlgorithm) &&
        Objects.equals(this.secret, secretLockInfo.secret) &&
        Objects.equals(this.recipient, secretLockInfo.recipient);
  }

  @Override
  public int hashCode() {
    return Objects.hash(account, accountAddress, mosaicId, amount, height, status, hashAlgorithm, secret, recipient);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SecretLockInfo {\n");
    
    sb.append("    account: ").append(toIndentedString(account)).append("\n");
    sb.append("    accountAddress: ").append(toIndentedString(accountAddress)).append("\n");
    sb.append("    mosaicId: ").append(toIndentedString(mosaicId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    hashAlgorithm: ").append(toIndentedString(hashAlgorithm)).append("\n");
    sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
    sb.append("    recipient: ").append(toIndentedString(recipient)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

