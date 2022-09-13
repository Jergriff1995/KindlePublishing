#!/usr/bin/env bash
set -e

while [ $# -gt 0 ]; do
    case "$1" in
        -p | --package)       shift; java_package="$1";;
        -d | --debug-port)    shift; debug_port="$1";;
        --)                   shift; break;;
        -*)                   echo "Unsupported option: '$1'. It will be ignored" 1>&2; shift; break;;
        *)                    break;;
    esac
    shift
done

sed -i "s/package.placeholder/${java_package}/" ${HOME}/hydra/events/hydra.json

ARGS=('-e' "$HOME/hydra/events/hydra.json")

if [ ! -z "$debug_port" ]; then
    ARGS+=('-d' "$debug_port")
fi

sam local invoke LambdaFunction "${ARGS[@]}" 1> hydra/last_hydra_run.log 2> hydra/last_hydra_run_err.log
#Moving the log files to the logs folder but keeping the old ones not to break customers that mounted the folder
cp hydra/*.log logs/

#The exit code is taken from the last line that contains a single token
code="$(awk 'NF==1 {line=$0} END {print line}' hydra/last_hydra_run.log)"

# All of the test output (error or success) gets placed in the error log.
# We always want to print out the test output for participants (whether tests passed or failed).
cat hydra/last_hydra_run_err.log

# put the placeholder text back into the hydra.json file so this script can be reused within the lifetime of
# a container.
rep=".\\*"
escaped_package="${java_package/.\*/$rep}"
sed -i "s/${escaped_package}/package.placeholder/" ${HOME}/hydra/events/hydra.json

if [[ ! "$code" =~ ^[0-9]+$ ]]; then
    echo "Couldn't find exit code of Hydra tests run. Failing!"
    exit 1
else
    exit $code
fi
