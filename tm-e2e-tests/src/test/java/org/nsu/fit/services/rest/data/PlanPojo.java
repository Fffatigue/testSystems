package org.nsu.fit.services.rest.data;

import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanPojo {
    @JsonProperty("id")
    public UUID id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("details")
    public String details;

    @JsonProperty("fee")
    public int fee;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanPojo)) {
            return false;
        }
        PlanPojo planPojo = (PlanPojo) o;
        return fee == planPojo.fee &&
                Objects.equals(id, planPojo.id) &&
                Objects.equals(name, planPojo.name) &&
                Objects.equals(details, planPojo.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, details, fee);
    }
}
