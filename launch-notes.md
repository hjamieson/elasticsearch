# Launch Notes
es: 3.15.14.188
client: 3.15.223.72/10.0.1.189

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


