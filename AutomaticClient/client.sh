#!/bin/bash

#url2="http://ec2-18-221-159-205.us-east-2.compute.amazonaws.com"
url2="http://127.0.0.1:8080"
auth_port=8000;
api_port=8081;
access_token="xxxxxxx"
box="+ --------------------------------------- +"
FAILED=1
SUCCESS=0
SCRIPT_PATH="watch.sh"

# h - help, l - list of metrics, m - metric type, none - help
usage() {
	echo "Usage: $0 [-u <url>] [-m <metric_type>] [-l] [-h]"
	echo
	echo "-u url          Set URL"
	echo "-m metric_type  Set metric type to print top 10"
	echo "-l              Print list of metrics types"
	echo "-h              Print usage"
	exit $SUCCESS;
}

update_access_token() {
	access_token=`curl -s --data "username=test&password=test" "$url:$auth_port/v1/login" | sed -n 's/^.*access_token\"...//p' | sed -n 's/\".*//p'`;
}

while getopts ":u:m:lh" o
do
	case $o in
	u) url=$OPTARG;
	   echo $url2;
	   ;;
	m) metric=$OPTARG;
	   update_access_token;
	   ;;
	l) update_access_token;
	   curl -sH "access-token: $access_token" "$url2:$api_port/v1/metrics?meta=true" | sed -n 's/^.*types\"..\[//p'| sed -n 's/\]}}/\n/p';
	   ;;
	m) metric=$OPTARG;
	   ;;
	l) update_access_token;
	   curl -sH "access-token: $access_token" "$url/metrics?meta=true" | sed -n 's/^.*types":\s\[//p' | sed -n 's/\]\}\}/\n/p';
	   exit $SUCCESS;
	   ;;
	h|*) usage
	   ;;
	esac
done

if [ -z "$metric" ]; then
	usage
fi

#print top 10, refresh 1s
#watch -n1 -td "curl -sH 'access-token: $access_token' '$url2:$api_port/v1/hosts?metric_type=$metric&top=10' | sed 's/,/\n$box/g' | sed 's/.*host-id..../\t\t/' | sed 's/.monitor.*//' ";
source "$SCRIPT_PATH"  "curl -sH 'access-token: $access_token' '$url2:$api_port/v1/hosts?metric_type=$metric&top=10' | sed 's/,/\n$box/g' | sed 's/.*host-id..../\t\t/' | sed 's/.monitor.*//' ";
exit $SUCCESS;