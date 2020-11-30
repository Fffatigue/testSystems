package org.nsu.fit.services.rest.data;

import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionPojo {
    @JsonProperty("id")
    public UUID id;

    @JsonProperty("customer_id")
    public UUID customerId;

    @JsonProperty("plan_id")
    public UUID planId;

    @JsonProperty("plan_name")
    public String planName;

    @JsonProperty("plan_details")
    public String planDetails;

    @JsonProperty("plan_fee")
    public Integer planFee;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionPojo)) {
            return false;
        }
        SubscriptionPojo that = (SubscriptionPojo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(planId, that.planId) &&
                Objects.equals(planName, that.planName) &&
                Objects.equals(planDetails, that.planDetails) &&
                Objects.equals(planFee, that.planFee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, planId, planName, planDetails, planFee);
    }
}
