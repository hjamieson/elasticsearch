#cluster.name: my-application
s/#cluster.name: my-application/cluster.name: eshadoop/

#node.name: node-1
s/#node.name: node-1/node.name: es7n1/

#network.host: 192.168.0.1
s/#network.host: 192.168.0.1/network.host: 0.0.0.0/

#discovery.seed_hosts: ["host1", "host2"]
s/#discovery.seed_hosts: \["host1", "host2"\]/discovery.seed_hosts: ["es7n1"]/

#cluster.initial_master_nodes: ["node-1", "node-2"]
s/#cluster.initial_master_nodes: \["node-1", "node-2"\]/cluster.initial_master_nodes: ["es7n1"]/