# ElasticSearch Ramblings
Ok; I have resorted to messing around with ElasticSearch (you know; for search) because its just too darn easy to use for indexing stuff.  I am dumping various artifacts in here to make it available where ever wireless hubs roam.

# docker
* docker image scripts

# Code
* logging-job - acutely simple job that logs using logback.  I really don't know why I need this.

# Setting up Environment
1. create a network to work on:
   ```
   docker network create skynet
   ```
1. start ES on skynet:
   ```
   docker container run -d --name es7 --net skynet -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.5.1
   ```
2. start kibana on skynet:
   ```
   docker container run -d --name kibana --net skynet --link es7:elasticsearch -p 5601:5601 kibana:7.5.1
   ```