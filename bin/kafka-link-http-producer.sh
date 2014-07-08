BROKER_URLS="192.168.1.232:9095,192.168.1.232:9096,192.168.1.232:9097"
#BROKER_URLS="192.168.1.232:9095,192.168.1.232:9096,192.168.1.232:9097"
base_dir=`pwd`
$base_dir/bin/kafka-http-endpoint.sh producer --broker-list $BROKER_URLS  --statsd-prefix kafka.http.producer
