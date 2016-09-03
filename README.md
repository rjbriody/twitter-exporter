This project consists of a few small java classes that will export a twitter neighborhood for a set of twitter users to a set of files that can be easily loaded into DSE Graph using the DSE Graph Loader.

1) Set up your Twitter API account:
2) Add those creds to config.properties. A template is provided: config.properties.template. Copy that to config.properties and add your creds.
3) Run PopulateQueue.java
4) Run PopulateUsers.java
5) Run PopulateFollowers.java
6) Start DSE w/ Graph enabled.
7) Download the DSE Graph Loader
8) Load the user vertices.
9) Load the follows edges.
10) Party
