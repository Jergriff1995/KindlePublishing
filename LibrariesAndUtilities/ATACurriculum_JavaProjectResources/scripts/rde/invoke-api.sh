#!/usr/bin/env bash

merged_json_request=$(jq -s '.[0] + .[1]' /tmp/events/prepare_shipment.json /tmp/events/base_api_request.json)

# make sure we can r/w the log file
sudo chmod 666 ~/logs/api-gateway.log

# --event needs `-` as the value to read from stdin
# local invoke writes logs to stderr and output to stdout
# using -log-file writes both to the log file, we just want stderr in the logs
# and to keep stdout as-is, so we redirect stderr to the log file
# https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-local-invoke.html
response=$(echo ${merged_json_request} | aws-sam-local local invoke LambdaFunction --event - 2> ~/logs/api-gateway.log)

status_code=$(echo ${response} | jq '.statusCode')

printf "\n\n"
echo "Status Code: " ${status_code}

if [[ ${status_code} = 200 ]]; then
    echo "Body: "
    echo ${response} | jq -r '.body'| jq -r '.attributes' | jq '.'
else
    echo "Error Type: " $(echo ${response} | jq -r '.headers."x-amzn-ErrorType"')
    echo "Message: "  $(echo ${response} | jq -r '.body' | jq -r '.message')
fi

printf "\n\n"
