// Here is an example command that can be used to load users. Adjust paths and IPs as necessary based on your environment.
// cd ~/datastax/dse-graph-loader-5.0.0/; ./graphloader ~/workspace/twitter-exporter/loader/user.groovy -graph TWITTERVERSE -address localhost

config create_schema: true //This loading script will initialize the schema if it does not already exists

// !!!!!!!!!!!!!!!!!!!!
// NOTE: Adjust this path based on the location of your user.json file
def input = File.json("/data/bobbriody_twitter_export/user.json")

load(input).asVertices {
    label "user"
}
