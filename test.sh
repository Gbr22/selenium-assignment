#!/bin/sh
docker exec -it docker-sandbox-ubuntu sh -c "cd /home/selenium/tests; gradle cleanTest test"
