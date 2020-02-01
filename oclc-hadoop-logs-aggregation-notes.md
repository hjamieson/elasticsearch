# Hadoop Logs Aggregation
Setting up ELK for Hadoop log aggregation & analysis.
January 25, 2020 | Hugh Jamieson

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
   sed -i.bak -e 's/server.host: "localhost"/server.host: "0.0.0.0"/' /etc/kibana/kibana.yml
   ```
1. Start kibana:
   ```
   sudo systemctl start kibana.service
   # sudo systemctl stop kibana.service
   ```