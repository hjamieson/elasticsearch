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


wget -P logstash/config  https://raw.githubusercontent.com/coralogix-resources/elk-course-samples/master/csv-read.conf

