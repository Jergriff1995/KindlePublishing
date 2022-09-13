#!/bin/bash
export $(cat .env | xargs)
java -jar build/libs/KindlePublishingService.jar