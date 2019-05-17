package com.digitechlabs.paymentgw.ssl;

import com.digitechlabs.paymentgw.coingate.CheckoutTask;
import com.digitechlabs.paymentgw.restobject.CallbackTask;
import com.digitechlabs.paymentgw.configs.ConfigLoader;
import com.digitechlabs.paymentgw.paypal.execute.response.ExeResponse;
import com.digitechlabs.paymentgw.paypal.token.GetTokenProcess;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.digitechlabs.paymentgw.restobject.OrderTask;
import com.digitechlabs.paymentgw.restobject.WithdrawUserConfirmedTask;
import com.digitechlabs.paymentgw.utils.GlobalVariables;
import com.digitechlabs.paymentgw.wallet.NEP5Transfer;
import com.digitechlabs.paymentgw.wallet.Tx;
import com.google.gson.Gson;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONException;

public class RestFulClient {

    private Logger logger = Logger.getLogger(RestFulClient.class);
    private String url;
    final ClientConfig config = new DefaultClientConfig();

    public RestFulClient() {
//        this.url = url;
        try {
            config.getProperties()
                    .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                            new HTTPSProperties(
                                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER,
                                    SSLUtil.getInsecureSSLContext()));
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            logger.error("Error init Https client --> please check:" + ex.getMessage(), ex);
        }
    }

    /**
     * create new order to coingate system
     *
     * @param task
     * @return
     */
    public String createOrder(OrderTask task) {
        long start = System.currentTimeMillis();

        String[] values = task.toArray();
//        for (int i = 0; i < values.length; i++) {
//            logger.info("VALUE:" + values[i]);
//        }
        logger.info("starting post ..." + this.url);
        Client client = null;
        String result = "";
        try {
            client = Client.create(config);
            client.setConnectTimeout(30000);

            WebResource resource = client.resource(this.url);
            JSONObject request = makeJsonFromArray(GlobalVariables.ORDER_ARRAY, values);
            ClientResponse response = resource.type("application/json")
                    .header(HttpHeaders.AUTHORIZATION, ConfigLoader.getInstance().getAppToken())
                    .header(HttpHeaders.CONTENT_TYPE, ConfigLoader.getInstance().getAppContentType())
                    .post(ClientResponse.class, request.toString());
            logger.info("Request to create order:" + request.toString());
            result = getStringFromInputStream(response.getEntityInputStream());
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
        logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");

        return result;
    }

    public String createOrder(CheckoutTask task) {
        long start = System.currentTimeMillis();

        String[] values = task.toArray();
        for (int i = 0; i < values.length; i++) {
            logger.info("VALUE:" + values[i]);
        }
        logger.info("starting post ..." + this.url);
        Client client = null;
        String result = "";
        try {
            client = Client.create(config);
            client.setConnectTimeout(30000);

            WebResource resource = client.resource(this.url);
            JSONObject request = makeJsonFromArray(GlobalVariables.ORDER_ARRAY, values);
            ClientResponse response = resource.type("application/json")
                    .header(HttpHeaders.AUTHORIZATION, ConfigLoader.getInstance().getAppToken())
                    .header(HttpHeaders.CONTENT_TYPE, ConfigLoader.getInstance().getAppContentType())
                    .post(ClientResponse.class, request.toString());
            logger.info("Request to create order:" + request.toString());
            result = getStringFromInputStream(response.getEntityInputStream());
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
        logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");

        return result;
    }

    public String checkOut(String pay_currency) {
        long start = System.currentTimeMillis();

        String[] values = new String[]{pay_currency};
        logger.info("starting post ..." + this.url);
        Client client = null;
        String result = "";
        try {
            client = Client.create(config);
            client.setConnectTimeout(30000);

            WebResource resource = client.resource(this.url);
            JSONObject request = makeJsonFromArray(GlobalVariables.CHECKOUT_ARRAY, values);
            ClientResponse response = resource.type("application/json")
                    .header(HttpHeaders.AUTHORIZATION, ConfigLoader.getInstance().getAppToken())
                    .header(HttpHeaders.CONTENT_TYPE, ConfigLoader.getInstance().getAppContentType())
                    .post(ClientResponse.class, request.toString());
            logger.info("Request to checkout:" + request.toString());
            result = getStringFromInputStream(response.getEntityInputStream());
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
        } finally {
            if (client != null) {
                client.destroy();
            }
        }

        logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");

        return result;
    }

    public String notifyService(CallbackTask task) {
        long start = System.currentTimeMillis();
        String[] values = new String[]{task.getOrder_id()};
        logger.info("starting post ..." + this.url);
        Client client = null;
        String result = "";
        try {
            client = Client.create(config);
            WebResource resource = client.resource(this.url);
            JSONObject request = makeJsonFromArray(GlobalVariables.NOTIFY_SERVICE_ARRAY, values);
            ClientResponse response = resource.type("application/json")
                    .header("api-key", ConfigLoader.getInstance().getServiceKey())
                    //                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .post(ClientResponse.class, request.toString());
            logger.info("notify to servicegw:" + request.toString());
            result = getStringFromInputStream(response.getEntityInputStream());
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
            result = "";
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
        logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");

        return result;
    }

    public void requestWithdraw(WithdrawUserConfirmedTask task) {
        long start = System.currentTimeMillis();
        String[] values = new String[]{task.getCurrency(), task.getAmount(), task.getTo_address()};
        logger.info("starting post ..." + this.url);
        Client client = null;
        String result = "";
        try {
            client = Client.create();
            WebResource resource = client.resource(this.url);
            JSONObject request = makeJsonFromArray(GlobalVariables.WITHDRAW_ARRAY, values);
            ClientResponse response = resource.type("application/json")
                    .header(HttpHeaders.AUTHORIZATION, ConfigLoader.getInstance().getWalletToken())
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .post(ClientResponse.class, request.toString());
            logger.info("sent request withdraw:" + request.toString() + " Success");
            result = getStringFromInputStream(response.getEntityInputStream());

        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
            result = ex.getMessage();
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
        logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");
    }

    public NEP5Transfer checkTXid(String transaction, String currency, String amount, String to_address) {
        long start = System.currentTimeMillis();
        String walletUrl = ConfigLoader.getInstance().getWalletURL() + "tx/" + transaction + "?currency=" + currency.toLowerCase();
        Client client = null;
        String result = "";

        try {
            client = Client.create(config);
            WebResource resource = client.resource(walletUrl);
            ClientResponse response = resource.type("application/json")
                    .header(HttpHeaders.AUTHORIZATION, ConfigLoader.getInstance().getWalletToken())
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .get(ClientResponse.class);
            logger.info("sent request :" + walletUrl + " Success");

            result = getStringFromInputStream(response.getEntityInputStream());
            logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");
            Gson gson = new Gson();
            Tx tx = gson.fromJson(result, Tx.class);

            for (NEP5Transfer neP5Transfer : tx.getNEP5Transfer()) {
                if (neP5Transfer.getAmount().equals(amount) && neP5Transfer.getTo_address().equals(to_address)) {
                    return neP5Transfer;
                }
            }

            return null;
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            logger.error("Exception " + ex.getMessage(), ex);
            return null;
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
    }

    public String get(String url) {
        long start = System.currentTimeMillis();
        Client client = null;
        String result;

        try {
            client = Client.create(config);
            WebResource resource = client.resource(url);
            ClientResponse response = resource.type("application/json").get(ClientResponse.class);
            logger.info("sent request :" + url + " Success");

            result = getStringFromInputStream(response.getEntityInputStream());
            logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");

            return result;
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            logger.error("Exception " + ex.getMessage(), ex);
            return null;
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
    }

    /**
     * create new payment transaction to Paypal system
     *
     * @param request
     * @return
     */
    public String createPayment(String request) {
        long start = System.currentTimeMillis();

        logger.info("starting post ..." + ConfigLoader.getInstance().getPaypalUrlCreatePayment());
        Client client = null;
        String result = "";
        try {
            client = Client.create(config);
            client.setConnectTimeout(30000);

            WebResource resource = client.resource(ConfigLoader.getInstance().getPaypalUrlCreatePayment());
            ClientResponse response = resource.type("application/json")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + GetTokenProcess.getInstance().getLiveToken())
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .post(ClientResponse.class, request);
            logger.info("Request to create payment:" + request);
            result = getStringFromInputStream(response.getEntityInputStream());
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            logger.error("Exception " + ex.getMessage(), ex);
            return null;
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
        logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");

        return result;
    }

    public ExeResponse exePayment(String request, String url) {
        long start = System.currentTimeMillis();
        ClientResponse response = null;
        ExeResponse resp = null;
        logger.info("starting post ..." + url);
        Client client = null;
        String result = "";
        try {
            client = Client.create(config);
            client.setConnectTimeout(30000);

            WebResource resource = client.resource(url);
            response = resource.type("application/json")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + GetTokenProcess.getInstance().getLiveToken())
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .post(ClientResponse.class, request);
            logger.info("Request to execute payment:" + request);
//            response.gets
            result = getStringFromInputStream(response.getEntityInputStream());

            resp = new ExeResponse(response.getStatus(), result);
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
        logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");

        return resp;
    }

    public String notifyBookFail(String request) {
        long start = System.currentTimeMillis();
        ClientResponse response = null;
        logger.info("starting post ..." + ConfigLoader.getInstance().getNotifyBookFailUrl());
        Client client = null;
        String result = "";
        try {
            client = Client.create(config);
            client.setConnectTimeout(30000);

            WebResource resource = client.resource(ConfigLoader.getInstance().getNotifyBookFailUrl());
            response = resource.type("application/json")
                    .header(ConfigLoader.getInstance().getNotifyBookFailHeader(), ConfigLoader.getInstance().getNotifyBookFailPrivatekey())
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .post(ClientResponse.class, request);
            logger.info("Request to back end notify booking fail:" + request);
//            response.gets
            result = getStringFromInputStream(response.getEntityInputStream());

        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
        logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");

        return result;
    }

    public String getPaypalToken() {
        long start = System.currentTimeMillis();

        logger.info("starting post ..." + ConfigLoader.getInstance().getPaypalUrlGetToken());
        Client client = null;
        String result = "";
        try {
            client = Client.create(config);
            client.setConnectTimeout(30000);
            HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(ConfigLoader.getInstance().getPaypalClientID(), ConfigLoader.getInstance().getPaypalSecret());
            client.addFilter(authFilter);

            WebResource resource = client.resource(ConfigLoader.getInstance().getPaypalUrlGetToken());

            MultivaluedMap form = new MultivaluedMapImpl();
            form.add("grant_type", "client_credentials");
            ClientResponse response = resource.type("application/x-www-form-urlencoded")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .header(HttpHeaders.ACCEPT_LANGUAGE, "en_US")
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .post(ClientResponse.class, form);
            logger.info("Request to get access token:");
            result = getStringFromInputStream(response.getEntityInputStream());
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
            return null;
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
        logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");

        return result;
    }

    public String reFund(String url) {
        long start = System.currentTimeMillis();

        logger.info("starting post ..." + url);
        Client client = null;
        String result = "";
        try {
            client = Client.create(config);
            client.setConnectTimeout(30000);

            WebResource resource = client.resource(url);
            ClientResponse response = resource.type("application/json")
                    //                    .header(HttpHeaders.AUTHORIZATION, ConfigLoader.getInstance().getAppToken())
                    .header(HttpHeaders.ACCEPT, "application/json")
                    //                    .header(HttpHeaders.ACCEPT_LANGUAGE, "en_US")
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + GetTokenProcess.getInstance().getLiveToken())
                    //post empty body request
                    .post(ClientResponse.class, "{}");
            logger.info("Request to refund payment:");
            result = getStringFromInputStream(response.getEntityInputStream());
        } catch (ClientHandlerException | UniformInterfaceException ex) {
            logger.error("Exception " + ex.getMessage(), ex);
            return null;
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
        logger.info("Response from API was: " + result + " (in " + (System.currentTimeMillis() - start) + " ms)");

        return result;
    }

    private JSONObject makeJsonFromArray(String[] names, String[] values) {
        if (names.length == values.length) {
            JSONObject order = new JSONObject();
            for (int i = 0; i < names.length; i++) {
                try {
                    order.put(names[i], values[i]);
                } catch (JSONException e) {
                    logger.error(e.getMessage(), e);
                    return null;
                }
            }
            return order;
        } else {
            return null;
        }
    }

    // convert InputStream to String
    public String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        final StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return sb.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String sendPost(WithdrawUserConfirmedTask task) {
        HttpClient client;
        HttpPost post;
        try {
            long start = System.currentTimeMillis();
            String[] values = new String[]{task.getCurrency(), task.getAmount(), task.getTo_address()};
            JSONObject request = makeJsonFromArray(GlobalVariables.WITHDRAW_ARRAY, values);
            logger.info("starting post ..." + this.url);
            client = new DefaultHttpClient();
            post = new HttpPost(this.url);

            // add header
//            post.setHeader("User-Agent", "Mozilla/5.0");
            post.setHeader(HttpHeaders.AUTHORIZATION, ConfigLoader.getInstance().getWalletToken());
            post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            post.setHeader(HttpHeaders.ACCEPT, "application/json");
            StringEntity entity = new StringEntity(request.toString());

            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            logger.info("Sending 'POST' request to URL : " + url);
            logger.info("Post parameters : " + post.getEntity());
            logger.info("Response Code : "
                    + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            logger.info("Response from API was: " + result.toString() + " (in " + (System.currentTimeMillis() - start) + " ms)");
            return result.toString();
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

}
