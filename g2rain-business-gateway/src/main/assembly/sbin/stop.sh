#!/bin/bash
source base.sh
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`


PIDS=`ps -ef | grep java | grep "Dapp.name=$SERVICE_NAME" | awk '{print $2}'`
if [ -z "$PIDS" ]; then
    echo "ERROR: The $SERVICE_NAME does not started!"
    exit 0
fi

echo -e "Stopping the $SERVICE_NAME ...\c"
for PID in $PIDS ; do
    kill $PID > /dev/null 2>&1
done

COUNT=0
while [ $COUNT -lt 1 ]; do    
    echo -e ".\c"
    sleep 1
    COUNT=1
    for PID in $PIDS ; do
        PID_EXIST=`ps -f -p $PID | grep java`
        if [ -n "$PID_EXIST" ]; then
            COUNT=0
            break
        fi
    done
done

echo "OK!"
echo "PID: $PIDS"
