// Here is an example command that can be used to load the follows edges. Adjust paths and IPs as necessary based on your environment.
// cd ~/datastax/dse-graph-loader-5.0.1/; ./graphloader ~/datastax/twitter-exporter/dseGrapLoaderScripts/follows.groovy -graph BOBS_TWITTERVERSE -address localhost
config create_schema: true //This loading script will initialize the schema if it does not already exists

// !!!!!!!!!!!!!!!!!!!!
// NOTE: Adjust this path based on the location of your follows.json file
def input = File.json("/Users/bobbriody/datastax/twitter-exporter/data/follows.json");

load(input).asEdges {
    label "follows"
    outV "out", {
        label "user"
        key "twitter_id"
    }
    inV "in", {
        label "user"
        key "twitter_id"
    }
}
