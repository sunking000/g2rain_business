#!/bin/bash

current_path=`pwd`
case "`uname`" in
    Linux)
		bin_abs_path=$(readlink -f $(dirname $0))
		;;
	*)
		bin_abs_path=`cd $(dirname $0); pwd`
		;;
esac
base=${bin_abs_path}/..
export LANG=en_US.UTF-8
export BASE=$base

## set java path
if [ -z "$JAVA" ] ; then
  JAVA=$(which java)
echo $JAVA
fi

JAVA_OPTS=" $JAVA_OPTS -server -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"
JAVA_OPTS=" $JAVA_OPTS -Xloggc:${LOG_PATH}/gc.log -XX:HeapDumpPath=LOGS_DIR=${LOG_PATH} "
JAVA_OPTS=" $JAVA_OPTS -Dapp.name=${SERVICE_NAME} -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8   -Duser.timezone=Asia/Shanghai"

## set classpath lib
for i in $base/lib/*;
	do CLASSPATH=$i:"$CLASSPATH";
done
CLASSPATH="$base:$base/conf:$CLASSPATH";

cd $bin_abs_path
echo CLASSPATH :$CLASSPATH
$JAVA $JAVA_OPTS $JAVA_DEBUG_OPT  -classpath .:$CLASSPATH ${MAIN_CLASS}
