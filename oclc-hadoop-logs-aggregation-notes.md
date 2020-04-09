# Hadoop Logs Aggregation
Setting up ELK for Hadoop log aggregation & analysis.
January 25, 2020 | Hugh Jamieson

## General log Notes
most Hadoop logs are using the appender format = `%d{ISO8601} %p %c{2}: %m%n`, which:
timestamp | level | class | message
---|---|---|---
2020-04-07 01:58:39,556 | INFO |  org.apache.hadoop.hdfs | log message


## Setup VPC
[aws doc](https://docs.aws.amazon.com/vpc/latest/userguide/VPC_Internet_Gateway.html#d0e22943)
1. run elastic-vpc.yaml cloudformation script to setup VPC, subnets, etc.
2. internet-accessible hosts should go on subnet01; protected on subnet02
3. run elastic-ec2.yaml to create the hosts
4. download elasticsearch: https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.5.2-linux-x86_64.tar.gz
5. install GPG key:
   ```
   sudo rpm --import https://artifacts.elastic.co/GPG-KEY-elasticsearch
   ```
6. create /etc/yum.repos.d/elasticsearch.repo
    ```
    [elasticsearch]
    name=Elasticsearch repository for 7.x packages
    baseurl=https://artifacts.elastic.co/packages/7.x/yum
    gpgcheck=1
    gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
    enabled=0
    autorefresh=1
    type=rpm-md
    ```
7. install 
   ```
   sudo yum --enablerepo=elasticsearch install elasticsearch
   ```
1. edit /etc/elasticsearch/elasticsearch.yaml to suit:
   ```
   node.name: es7n1
   network.host: 0.0.0.0
   discovery.seed_hosts: ["es7n1"]
   ```
2. reload systemctl:
   ```
   sudo systemctl daemon-reload
   sudo systemctl enable elasticsearch.service
   sudo systemctl start elasticsearch.service
   ```
3. test the install a bit:
   ```
   curl -XGET 127.0.0.1:9200/

   wget http://media.sundog-soft.com/es7/shakes-mapping.json

   curl -H "Content-Type: application/json" -XPUT localhost:9200/shakespeare --data-binary @shakes-mapping.json

    wget http://media.sundog-soft.com/es7/shakespeare_7.0.json

    curl -H "Content-Type: application/json" -XPOST localhost:9200/shakespeare/_bulk --data-binary @shakespeare_7.0.json
1. Install Kibana on the ES host:
   ```
   sudo yum --enablerepo=elasticsearch install kibana -y
   sudo /bin/systemctl daemon-reload
   sudo /bin/systemctl enable kibana.service
   ```
1. Don't forget to open port 5601 for kibana UI!
2. Kibana default only listens on localhost; need to update config to bind to all nics.
   ```
   sudo vi /etc/kibana/kibana.yml
   change server.host: "localhost"
   to server.host: "0.0.0.0"

   you should be able to use sed to do this:
   sed -i.orig 's/#server.host: "0.0.0.0"/server.host: "0.0.0.0"/' /etc/kibana/kibana.yml
   ```
1. Start kibana:
   ```
   sudo systemctl start kibana.service
   # sudo systemctl stop kibana.service
   ```

## Beats Installation
> tar install of filebeats is available here: 
> https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-7.6.1-linux-x86_64.tar.gz

For further steps visit the
[Getting started](https://www.elastic.co/guide/en/beats/filebeat/7.6/filebeat-getting-started.html) guide.

## Documentation

Visit [Elastic.co Docs](https://www.elastic.co/guide/en/beats/filebeat/7.6/index.html)
for the full Filebeat documentation.

### RPM Install

1. create a host
2. install public signing key
   ```
   sudo rpm --import https://packages.elastic.co/GPG-KEY-elasticsearch
   ```
3. create /etc/yum.repos.d/elastic.repo
   ```
   [elastic-7.x]
   name=Elastic repository for 7.x packages
   baseurl=https://artifacts.elastic.co/packages/7.x/yum
   gpgcheck=1
   gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
   enabled=1
   autorefresh=1
   type=rpm-md
   ```
4. install beats
   ```
   sudo yum install filebeat -y
   ```
1. configure beats:
   create /etc/filebeat/filebeat.yml
1. create /data/logs folders
3. when ready to run:
   ```
   sudo systemctl daemon-reload
   sudo systemctl enable filebeat
   sudo systemctl start filebeat
   ```
## Additional Fields Extraction
Using the script processor
hbas logs have time as _2019-09-29 10:01:42,403_
javascript to extract time:
```
hugh.match(/\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2}\,\d+/)
```
beats sends a timestamp as follows:
```
"fields": {
    "@timestamp": [
      "2020-02-02T17:11:26.622Z"
    ]
  },
 ```

## Todays Hosts
es: 3.133.141.39
beats: 52.14.173.132

## Installing adoptopenjdk
1. create repo file for adopt:
```
sudo su -
cat <<'EOF' > /etc/yum.repos.d/adoptopenjdk.repo
[AdoptOpenJDK]
name=AdoptOpenJDK
baseurl=http://adoptopenjdk.jfrog.io/adoptopenjdk/rpm/centos/7/x86_64
enabled=1
gpgcheck=1
gpgkey=https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public
EOF
exit

```
1. install the JDK:
```
yum install adoptopenjdk-8-hotspot -y
```

## Installing SBT/Scala
```
wget https://piccolo.link/sbt-1.3.8.tgz
sudo tar zxvf sbt-1.3.8.tgz -C /opt
export PATH=/opt/sbt/bin:$PATH
```

