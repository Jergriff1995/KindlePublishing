#!/usr/bin/env python
import json
import subprocess

base_path = '/tmp/events/base_api_request.json'
prepare_shipment_path = '/tmp/events/prepare_shipment.json'
logs_path = '/home/deceneu/logs/api-gateway.log'

# make sure we can r/w the log file
subprocess.check_call(['sudo', 'chmod', '666', logs_path])

with open(base_path, 'r') as base, open(prepare_shipment_path, 'r') as shipment, open(logs_path, 'a') as logs:
    base_dict = json.load(base)
    shipment_dict = json.load(shipment)
    # merge the requests, is there a way to do it not in place?
    base_dict.update(shipment_dict)
    merged_input = json.dumps(base_dict)

    # --event needs `-` as the value to read from stdin
    # local invoke writes logs to stderr and output to stdout
    # using -log-file writes both to the log file, we just want stderr in the logs
    # and to keep stdout as-is, so we redirect stderr to the log file
    # https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-local-invoke.html
    invoke_api = subprocess.Popen(['aws-sam-local', 'local', 'invoke', 'LambdaFunction', '--event', '-'],
                     stdin=subprocess.PIPE,
                     stdout=subprocess.PIPE,
                     stderr=logs)

    # pass our merged request as a JSON string in stdin, newline is to emulate echo
    output, err = invoke_api.communicate(merged_input.encode('utf-8')+'\n')
    response = json.loads(output)
    body = json.loads(response['body'])
    status_code = response['statusCode']
    print("")
    print("Status Code:  " + str(status_code))

    if status_code == 200:
        print("Body:")
        if body['attributes']:
            print(json.dumps(json.loads(body['attributes']), indent=2))
        else:
            print("null")
    else:
        print("Error Type:  "+ response['headers']['x-amzn-ErrorType'])
        print("Message:  " + body['message'])
    print("")
