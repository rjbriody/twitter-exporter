This project consists of a few small java classes that will export a twitter neighborhood to a set of files that can be easily loaded into DSE Graph using the DSE Graph Loader.

* Set up your Twitter API account: https://apps.twitter.com/
* A template config file is provided: `config.properties.template`. Copy that to `config.properties`, fill in your creds, and enter a twitter account screen_name to export.
* Run PopulateQueue.java
* Run PopulateUser.java
* Run PopulateFollowers.java

   Note: The Twitter APIs are rate limited so this can take a while (one minute per user, to be exactly). However, the good news is that you can simply restart again if the app fails, you lose wifi, or you need to cancel for whatever reason.
   
* Start DSE w/ Graph enabled. Instructions: https://docs.datastax.com/en/latest-dse/datastax_enterprise/graph/graphTOC.html
* Download the DSE Graph Loader: https://docs.datastax.com/en/latest-dse/datastax_enterprise/graph/dgl/dglOverview.html
* Load the user vertices. Here is an example command. Obviously you will need to tweak the paths, IP, and graph name to your environment.

   `cd ~/datastax/dse-graph-loader-5.0.0/; ./graphloader ~/workspace/twitter-export/loader/follows.groovy -graph TWITTERVERSE -address localhost`
   
* Load the follows edges.

   `cd ~/datastax/dse-graph-loader-5.0.0/; ./graphloader ~/workspace/twitter-export/loader/follows.groovy -graph TWITTERVERSE -address localhost`
   
* Party
