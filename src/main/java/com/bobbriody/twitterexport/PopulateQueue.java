package com.bobbriody.twitterexport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class PopulateQueue
{
    private static final String PROFILE_QUEUE_DIR = "data/profileQueue";
    private static final String FOLLOWER_QUEUE_DIR = "data/followsQueue";

    private static void pushToQueue(String path) throws IOException
    {
        File f = new File(path);
        if (!f.exists())
        {
            f.createNewFile();
        }
    }

    private static void pushToQueues(String id) throws IOException
    {
        pushToQueue(PROFILE_QUEUE_DIR + File.separator + id.toString());
        pushToQueue(FOLLOWER_QUEUE_DIR + File.separator + id.toString());
        Files.write(Paths.get("data/seedList.txt"), (id.toString() + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private static void followsToQueues(JSONObject followingResp) throws IOException
    {
        JSONArray ids = followingResp.getJSONArray("ids");
        for (int ii = 0; ii < ids.length(); ii++)
        {
            pushToQueues(ids.getString(ii));
        }
    }

    public static void main(String[] args) throws Exception
    {
        File config = new File("config.properties");
        if (!config.exists() || config.isDirectory())
        {
            System.err.println("Error: Expected to find ./config.properties file. Perhaps you should copy config.properties.template and fill in your Twitter API info.");
            System.exit(1);
        }

        try
        {
            // create dirs if necessary
            new File("data").mkdir();
            new File(PROFILE_QUEUE_DIR).mkdir();
            new File(FOLLOWER_QUEUE_DIR).mkdir();

            Properties props = new Properties();
            props.load(new FileInputStream(config));

            TwitterClient t = new TwitterClient(
                    props.getProperty("consumerKey"),
                    props.getProperty("consumerSecret"),
                    props.getProperty("accessToken"),
                    props.getProperty("accessSecret")
            );

            String[] seeds = props.getProperty("screen_names").split(",");
            // Grab the list of followed users for each seed user.
            for (String seed : seeds)
            {
                // Grab info for the specified screen_name
                JSONObject seedJson = (JSONObject) t.request("users/show.json?screen_name=" + seed, TwitterClient.Type.object);
                pushToQueues(seedJson.getString("id_str"));

                // get friends (follows)
                JSONObject followingResp = (JSONObject) t.request("friends/ids.json?stringify_ids=true&screen_name=" + seed, TwitterClient.Type.object);
                followsToQueues(followingResp);

                // get followers
                JSONObject followersResp = (JSONObject) t.request("followers/ids.json?stringify_ids=true&screen_name=" + seed, TwitterClient.Type.object);
                followsToQueues(followersResp);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

}