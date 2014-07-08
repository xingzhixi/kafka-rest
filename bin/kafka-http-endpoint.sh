#base_dir=$(dirname $0)
base_dir=`pwd`
export KAFKA_OPTS="-Xmx2048M -server -Dcom.sun.management.jmxremote -Dlog4j.configuration=file:$base_dir/bin/kafka-endpoint-log4j.properties -Dorg.eclipse.jetty.io.ChanelEndPoint.LEVEL=ALL"
for file in $base_dir/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done
export CLASSPATH="$CLASSPATH"
$base_dir/bin/kafka-run-class.sh com.wcc.kafka.http.RestServer $@
