package com.bobbriody.twitterexport;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class TwitterClient
{
    private final OAuthConsumer consumer;


    enum Type
    {
        object,
        array
    }


    public TwitterClient(final String consumerKey, final String consumerSecret, final String accessToken, final String accessSecret)


    {
        // Initialize the twitter consumer
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, accessSecret);
    }

    /**
     * Helper method that executes a request against the Twitter API.
     *
     * @param url
     * @param t
     * @return
     * @throws Exception
     */
    public Object request(String url, Type t) throws Exception
    {
        HttpGet request = new HttpGet("https://api.twitter.com/1.1/" + url);
        consumer.sign(request);

        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(url + "\n\t" + statusCode + ":" + response.getStatusLine().getReasonPhrase());
        if (statusCode == 429)
        {
            System.out.println("Sleeping for 15 minutes...");
            Thread.sleep(1000 * 60 * 16); // wait 15 minutes + a little extra for api limit to reset and try again.
            return request(url, t);
        }
        else if (statusCode == 401)
        {
            System.err.println("WARNING: Authorization Required for " + url);
            return null;
        }
        else if (statusCode != 200)
        {
            throw new Exception("Failure on " + url + " . Status Code: " + statusCode);
        }

        if (t == Type.object)
        {
            return new JSONObject(IOUtils.toString(response.getEntity().getContent()));
        }
        else
        {
            return new JSONArray(IOUtils.toString(response.getEntity().getContent()));
        }
    }

}
