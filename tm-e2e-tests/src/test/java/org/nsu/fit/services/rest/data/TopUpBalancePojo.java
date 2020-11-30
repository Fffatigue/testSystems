package org.nsu.fit.services.rest.data;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopUpBalancePojo {
    @JsonProperty("customer_id")
    public UUID customerId;

    @JsonProperty("money")
    public int money;
}
