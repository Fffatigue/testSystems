package org.nsu.fit.services.rest;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import com.github.javafaker.Faker;
import org.glassfish.jersey.client.ClientConfig;
import org.nsu.fit.services.log.Logger;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.nsu.fit.services.rest.data.CredentialsPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.services.rest.data.PlanPojo;
import org.nsu.fit.services.rest.data.SubscriptionPojo;
import org.nsu.fit.services.rest.data.TopUpBalancePojo;
import org.nsu.fit.shared.JsonMapper;

public class RestClient {
    private static final String REST_URI = "http://127.0.0.1:8080/tm-backend/rest";

    private static Client client = ClientBuilder.newClient(new ClientConfig().register(RestClientLogFilter.class));

    public AccountTokenPojo authenticate(String login, String pass) {
        CredentialsPojo credentialsPojo = new CredentialsPojo();

        credentialsPojo.login = login;
        credentialsPojo.pass = pass;

        return post("authenticate", JsonMapper.toJson(credentialsPojo, true), AccountTokenPojo.class, null);
    }

    public CustomerPojo createAutoGeneratedCustomer(AccountTokenPojo accountToken) {
        ContactPojo contactPojo = new ContactPojo();

        Faker faker = new Faker();
        contactPojo.firstName = faker.name().firstName();
        contactPojo.lastName = faker.name().lastName();
        contactPojo.login = faker.internet().emailAddress();
        String password = faker.internet().password();
        contactPojo.pass = password.length() > 12 ? password.substring(0, 12) : password;

        return post("customers", JsonMapper.toJson(contactPojo, true), CustomerPojo.class, accountToken);
    }

    public CustomerPojo getCustomer(AccountTokenPojo accountTokenPojo) {
        return get("/me", CustomerPojo.class, accountTokenPojo);
    }

    public List<CustomerPojo> getCustomers(AccountTokenPojo accountToken) {
        return getList("/customers", CustomerPojo.class, accountToken);
    }

    public TopUpBalancePojo topUpBalance(AccountTokenPojo accountToken, UUID customerId) {
        TopUpBalancePojo topUpBalancePojo = new TopUpBalancePojo();
        topUpBalancePojo.customerId = customerId;
        topUpBalancePojo.money = new Random().nextInt(5000);
        post("/customers/top_up_balance", JsonMapper.toJson(topUpBalancePojo, true), accountToken);
        return topUpBalancePojo;
    }

    public PlanPojo createAutoGeneratedPlan(AccountTokenPojo accountToken) {
        PlanPojo planPojo = new PlanPojo();

        Faker faker = new Faker();
        planPojo.details = faker.animal().name();
        planPojo.fee = faker.number().numberBetween(0, 5000);
        planPojo.name = faker.name().username();

        return post("plans", JsonMapper.toJson(planPojo, true), PlanPojo.class, accountToken);
    }

    public List<PlanPojo> getPlans(AccountTokenPojo accountToken) {
        return getList("plans", PlanPojo.class, accountToken);
    }

    public void deletePlan(AccountTokenPojo accountToken, UUID id) {
        delete("plans/" + id.toString(), accountToken);
    }

    public SubscriptionPojo createSubscription(AccountTokenPojo accountToken, UUID customerId, PlanPojo plan) {
        SubscriptionPojo subscriptionPojo = new SubscriptionPojo();

        subscriptionPojo.customerId = customerId;
        subscriptionPojo.planId = plan.id;
        subscriptionPojo.planDetails = plan.details;
        subscriptionPojo.planName = plan.name;
        subscriptionPojo.planFee = plan.fee;

        return post("subscriptions", JsonMapper.toJson(subscriptionPojo, true),
                SubscriptionPojo.class, accountToken);
    }

    public List<SubscriptionPojo> getSubscriptions(AccountTokenPojo accountToken) {
        return getList("subscriptions", SubscriptionPojo.class, accountToken);
    }

    private static <R> void delete(String path, AccountTokenPojo accountToken) {
        Invocation.Builder request = client
                .target(REST_URI)
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        if (accountToken != null) {
            request.header("Authorization", "Bearer " + accountToken.token);
        }

        try {
            String response = request.delete(String.class);
            Logger.debug(response);
        } catch (Exception e) {
            Logger.debug(e.toString());
            throw e;
        }
    }

    private static <R> R get(String path, Class<R> responseType, AccountTokenPojo accountToken) {
        Invocation.Builder request = client
                .target(REST_URI)
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        if (accountToken != null) {
            request.header("Authorization", "Bearer " + accountToken.token);
        }

        try {
            String response = request.get(String.class);
            Logger.debug(response);
            return JsonMapper.fromJson(response, responseType);
        } catch (Exception e) {
            Logger.debug(e.toString());
            throw e;
        }
    }


    private static <R> List<R> getList(String path, Class<R> responseType, AccountTokenPojo accountToken) {
        Invocation.Builder request = client
                .target(REST_URI)
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        if (accountToken != null) {
            request.header("Authorization", "Bearer " + accountToken.token);
        }

        try {
            String response = request.get(String.class);
            Logger.debug(response);
            return JsonMapper.fromJsonList(response, responseType);
        } catch (Exception e) {
            Logger.debug(e.toString());
            throw e;
        }
    }

    private static String post(String path, String body, AccountTokenPojo accountToken) {
        Invocation.Builder request = client
                .target(REST_URI)
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        if (accountToken != null) {
            request.header("Authorization", "Bearer " + accountToken.token);
        }

        try {
            String response = request.post(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
            Logger.debug(response);
            return response;
        } catch (Exception e) {
            Logger.debug(e.toString());
            throw e;
        }
    }

    private static <R> R post(String path, String body, Class<R> responseType, AccountTokenPojo accountToken) {
        return JsonMapper.fromJson(post(path, body, accountToken), responseType);
    }

    private static class RestClientLogFilter implements ClientRequestFilter {
        @Override
        public void filter(ClientRequestContext requestContext) {
            Logger.debug(requestContext.getMethod());
            Logger.debug(requestContext.getHeaders().toString());
            Object entity = requestContext.getEntity();
            if (entity != null) {
                Logger.debug(requestContext.getEntity().toString());
            }
        }
    }
}
