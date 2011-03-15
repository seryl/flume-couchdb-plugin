#!/bin/bash
curl -XPUT 'mydomain.com:9200/_river/apache/_meta' -d '{
    "type" : "couchdb",
    "couchdb" : {
        "host" : "localhost",
        "port" : 5984,
        "db" : "apache_errors",
        "filter" : null
    },
    "index" : {
        "index" : "apache",
        "type" : "errors",
        "bulk_size" : "1000",
        "bulk_timeout" : "10ms",
        "number_of_shards" : 3,
        "number_of_replicas" : 2
    }
}'
