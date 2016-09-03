package com.bobbriody.twitterexport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

/**
 * @author Bob Briody (http://bobbriody.com)
 */
public class PopulateUser
{
    static TwitterClient t;

    public static void main(String[] args) throws Exception
    {
        File config = new File("config.properties");
        if (!config.exists() || config.isDirectory())
        {
            System.err.println("Error: Expected to find ./config.properties file. Perhaps you should copy config.properties.template and fill in your Twitter API info.");
            System.exit(1);
        }

        Properties props = new Properties();
        props.load(new FileInputStream(config));

        t = new TwitterClient(
                props.getProperty("consumerKey"),
                props.getProperty("consumerSecret"),
                props.getProperty("accessToken"),
                props.getProperty("accessSecret")
        );

        // read files from queue dir and write to users.json
        final File[] files = new File("data/profileQueue").listFiles();


        StringBuilder ids = new StringBuilder();
        for (int ii = 0; ii < files.length; ii++)
        {
            ids.append(files[ii].getName()).append(",");
            if (ii == files.length - 1 || (ii + 1) % 100 == 0)
            {
                System.out.println("Progress: " + ii + " / " + files.length);
                populateProfiles(ids.toString());
                ids = new StringBuilder();
            }
        }
    }

    private static void populateProfiles(String ids) throws Exception
    {
        try
        {
            JSONArray profiles = (JSONArray) t.request("users/lookup.json?include_entities=false&user_id=" + ids, TwitterClient.Type.array);
            for (int ii = 0; ii < profiles.length(); ii++)
            {
                JSONObject profileJson = profiles.getJSONObject(ii);
                // save id as twitter_id
                profileJson.put("twitter_id", profileJson.get("id").toString());
                // remove twitter's id fields
                profileJson.remove("id");
                profileJson.remove("id_str");
                // the follows props contains nested values that aren't handled by DSE Graph, so remove them.
                profileJson.remove("status");
                profileJson.remove("entities");
                profileJson.remove("profile_location");

                // Write profile to user.json
                Files.write(Paths.get("data/user.json"), (profileJson.toString() + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                // User profile is saved and written to disk. Delete from queue.
                new File("data/profileQueue/" + profileJson.get("twitter_id")).delete();
            }
        }
        catch (Exception e)
        {
            System.err.println("Failed on " + ids);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
