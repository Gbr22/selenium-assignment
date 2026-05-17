# Selenium Assignment

To run the tests do the following:
1. Create a file called `.env` based on `example.env`. To find the admin login, visit https://qaplayground.com/bank and copy it from the Demo Credentials table, the admin user will be the one with "Full Access" role. The `docker-sandbox-ubuntu` container will obtain the environmental variables from this file. If you change the contents of `.env` the container will have to be recreated (not restarted!) for the change to apply.
2. Make sure that `./tests` and the contents therein are owned by 1000:1000, otherwise gradle will have permission issues.
    * To check the ownership: `ls -lan ./tests`
    * To change the ownership: `sudo chown -R 1000:1000 ./tests`.
3. Run `docker compose up -d` to start the containers.
4. Run `./test.sh` or execute `cd /home/selenium/tests; gradle cleanTest test` inside the `docker-sandbox-ubuntu` container.

In case of trouble starting the containers, you should verify that you have an up-to-date version of docker and compose, here are the versions I used:
* Docker version 28.5.2
* Docker Compose version 2.40.3
