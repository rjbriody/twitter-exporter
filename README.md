Welcome!
====

This project contains a few small java classes that will export a twitter neighborhood to JSON files that can be easily loaded into DSE Graph using the DSE Graph Loader.

Instructions
=====

Prerequisites
----
You're going to need to get DSE running with Graph enabled. You will also need to download the DSE Graph Loader. Please check out the wonderful DataStax instructions & documentation to get started:

   https://docs.datastax.com/en/latest-dse/datastax_enterprise/graph/graphTOC.html
   https://docs.datastax.com/en/latest-dse/datastax_enterprise/graph/dgl/dglOverview.html
   
Exporting a Twitter network and loading it into DSE Graph
----

* Set up your Twitter API account: https://apps.twitter.com/
* A template config file is provided: `config.properties.template`. Copy that to `config.properties`, fill in your creds, and enter a twitter account screen_name to export.
* Run PopulateQueue.java
* Run PopulateUser.java
* Run PopulateFollowers.java

   Note: The Twitter APIs are rate limited so this can take a while (one minute per user, to be exactly). However, the good news is that you can simply restart again if the app fails, you lose wifi, or you need to cancel for whatever reason.
   
* Make sure DSE Graph is running.
* **Edit the JSON file paths in user.groovy and follows.groovy based on your env.**
* Load the user vertices using the DSE Graph Loader. Here is an example command. Obviously you will need to tweak the paths, IP, and graph name to your environment.

   `cd ~/datastax/dse-graph-loader-5.0.0/; ./graphloader ~/workspace/twitter-exporter/dseGrapLoaderScripts/user.groovy -graph TWITTERVERSE -address localhost`
   
* Load the follows edges.

   `cd ~/datastax/dse-graph-loader-5.0.0/; ./graphloader ~/workspace/twitter-exporter/dseGrapLoaderScripts/follows.groovy -graph TWITTERVERSE -address localhost`
   
* Party!!!
