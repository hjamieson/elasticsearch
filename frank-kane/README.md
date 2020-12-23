# ElasticSearch Class by Frank Kane (sundog)

## Logstash
* download:
    `https://artifacts.elastic.co/downloads/logstash/logstash-7.10.0-darwin-x86_64.tar.gz`
### Pipeline COnfigurations
* Image: docker.elastic.co/logstash/logstash:7.10.0
* Logstash looks for pipeline configs in:
    `/usr/share/logstash/pipeline/`
* use ~/pipeline to provide configs:  
    `docker run --rm -it -v ~/pipeline/:/usr/share/logstash/pipeline/ docker.elastic.co/logstash/logstash:7.10.0`

### Settings
* file: logstash.yml
* location: /usr/share/logstash/config/
* using bind mounting:  
    `docker run --rm -it -v ~/settings/:/usr/share/logstash/config/ docker.elastic.co/logstash/logstash:7.10.0`

## HBase Event Timestamps
  We would like a field that defines the time of the event.  I have been using `@timestamp`, but I found this collides with Logstash.  Logstash implicitely adds a @timestamp field using a YYYY-MM-DDTHH:mm:ss.SSSz format.  If your index template expects a date in EPOCH_MILLIS format, they conflict and the CREATE of the _doc fails.

  I am solving this problem by definiting a new timestamp for the time of the event, called `eventTimeMs`.

### Defining an Index Template
  To get the date field correct, ES allows you to specify multiple formats that get tried in sequence.  The details are [here](https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-date-format.html).

```
"mappings": {
    "properties": {
        "eventTimeMs": {
            "type": "date",
            "format":"epoch_millis"
        }
    }
}
```
1. To configure an index template:
```json
PUT /_index_template/hbase-tamplate
{
   "index_patterns":"hbase*",
   "template": {
       "settings": {
           "number_of_shards": 1
       },
       "mappings": {
           "properties": {
               "eventTimeMs": {
                   "type": "date",
                   "format":"epoch_millis"
               }
           }
       }
    }
}
```
1. Send to Elasticsearch:
```
curl -s -XPUT -H "Content-Type: application/json" \
  http://elasticsearch:9200/_index_template/hbase-template \
  --data-binary "@/usr/app/setup/hbase-template.json"
```


wget -P logstash/config  https://raw.githubusercontent.com/coralogix-resources/elk-course-samples/master/csv-read.conf

## Aggregations
* just show me the number of matches; don't return data:
    ```
    GET /ratings/_search?size=0&pretty
    ```
* aggregate the ratings field by term (0.0, 1.0, 2.0)
    ```
    GET /ratings/_search?size=0&pretty
    {
    "aggs": {
        "ratings": {
        "terms": {
            "field": "rating"
        }
        }
    }
    }
    ```
* filter only ratings=5.0, then apply aggregation on rating
    ```
    GET /ratings/_search?size=0&pretty
    {
    "query":{
        "match":{
        "rating": 5.0
        }
    },
    "aggs": {
        "ratings": {
        "terms":{
            "field":"rating"
        }
        }
    }
    }
    ```
* calculate the average rating for _Star Wars Episode IV_ using the avg_rating agg:
    ```
    GET /ratings/_search?size=0&pretty
    {
        "query":{
            "match": {
                "movieId": 260
            }
        },
        "aggs":{
            "avg_rating":{
                "avg": {
                    "field": "rating"
                }
            }
        }
    }
    ```
* Histograms display a count of documents bucketed by some interval range.  Here is an example of creating a histogram aggregation of the field ratings using whole number intervals:
  ```
  GET /ratings/_search?size=0&pretty
  {
    "aggs": {
      "whole_ratings": {
        "histogram" : {
          "field": "rating",
          "interval": 1.0
        }
      }
    }
  }
  ```

  ```json
  {
    "took" : 26,
    "timed_out" : false,
    "_shards" : {
      "total" : 1,
      "successful" : 1,
      "skipped" : 0,
      "failed" : 0
    },
    "hits" : {
      "total" : {
        "value" : 10000,
        "relation" : "gte"
      },
      "max_score" : null,
      "hits" : [ ]
    },
    "aggregations" : {
      "whole_ratings" : {
        "buckets" : [
          {
            "key" : 0.0,
            "doc_count" : 1370
          },
          {
            "key" : 1.0,
            "doc_count" : 4602
          },
          {
            "key" : 2.0,
            "doc_count" : 13101
          },
          {
            "key" : 3.0,
            "doc_count" : 33183
          },
          {
            "key" : 4.0,
            "doc_count" : 35369
          },
          {
            "key" : 5.0,
            "doc_count" : 13211
          }
        ]
      }
    }
  }

  ```

