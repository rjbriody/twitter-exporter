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
* Clone (or fork and clone) this project.
* A template config file is provided: `config.properties.template`. Copy that to `config.properties`, fill in your Twitter API creds, and enter a twitter account screen_name to export.

   Note: The Twitter APIs are rate limited so exporting the network can take a while (approximately 1 minute per user). Consider this when choosing what account to export. However, if you lose WIFI or the app stops then you can simply restart at any step to pick up where you left off.

* Run PopulateQueue.java. (I run from Intellij. Any method for executing the classes should work just fine.)
* Run PopulateUser.java
* Run PopulateFollowers.java. This step may take a while (approximately 1 minute per user).
* Make sure DSE Graph is running.
* **Edit the JSON file paths in user.groovy and follows.groovy based on your env.**
* Load the user vertices using the DSE Graph Loader. Here is an example command. Obviously you will need to tweak the paths, IP, and graph name to your environment.

   `cd ~/datastax/dse-graph-loader-5.0.1/; ./graphloader ~/datastax/twitter-exporter/dseGrapLoaderScripts/user.groovy -graph BOBS_TWITTERVERSE -address localhost`
   
* Load the follows edges.

   `cd ~/datastax/dse-graph-loader-5.0.1/; ./graphloader ~/datastax/twitter-exporter/dseGrapLoaderScripts/follows.groovy -graph BOBS_TWITTERVERSE -address localhost`
   
* Party!!!
