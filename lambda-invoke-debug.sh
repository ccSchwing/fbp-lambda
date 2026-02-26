#!/bin/bash

set -eu
if [ $# -ne 1 ]
then
	echo "Usage: $(basename $0) Lambda Function"
	exit 1
else
	LAMBDA=$1
fi

sam local invoke $LAMBDA --debug -t .aws-sam/build/template.yaml -e events/event.json
