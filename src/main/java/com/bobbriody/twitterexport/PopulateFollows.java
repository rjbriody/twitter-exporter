package com.bobbriody.twitterexport;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author Bob Briody (http://bobbriody.com)
 */
public class PopulateFollows
{
    static TwitterClient t;
    static Set<String> seedSet = new HashSet<>();

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


        FileUtils.readLines(new File("data/seedList.txt")).forEach(idString -> seedSet.add(idString));

        // read files from queue dir and write to users.json
        final File[] files = new File("data/followsQueue").listFiles();

        for (int ii = 0; ii < files.length; ii++)
        {
            System.out.println(ii + "/" + files.length);
            populateFollows(files[ii].getName());
        }

    }

    private static void populateFollows(String id) throws Exception
    {
        try
        {
            final Object res = t.request("friends/ids.json?count=5000&stringify_ids=true&user_id=" + id, TwitterClient.Type.object);
            if (res == null)
            {
                // skip this one
                return;
            }

            JSONArray followsList = ((JSONObject) res).getJSONArray("ids");
            StringBuilder followsListJson = new StringBuilder();
            for (int ii = 0; ii < followsList.length(); ii++)
            {
                final String friendId = followsList.getString(ii);
                if (!seedSet.contains(friendId))
                {
                    // this friend is not in the neighborhood of the original seed set, so skip/ignore
                    continue;
                }
                followsListJson.append(new JSONObject().put("out", id).put("in", friendId).toString()).append("\n");
            }

            // Write profile to user.json
            Files.write(Paths.get("data/follows.json"), followsListJson.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            // User's follows are saved and written to disk. Delete from queue.
            new File("data/followsQueue/" + id).delete();
        }
        catch (Exception e)
        {
            System.err.println("Failed on " + id);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
