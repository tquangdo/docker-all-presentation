# docker-all-presentation 🐳

![Stars](https://img.shields.io/github/stars/tquangdo/docker-all-presentation?color=f05340)
![Issues](https://img.shields.io/github/issues/tquangdo/docker-all-presentation?color=f05340)
![Forks](https://img.shields.io/github/forks/tquangdo/docker-all-presentation?color=f05340)
[![Report an issue](https://img.shields.io/badge/Support-Issues-green)](https://github.com/tquangdo/docker-all-presentation/issues/new)

## reference
[youtube](https://www.youtube.com/watch?v=o7s-eigrMAI&list=PL9nWRykSBSFihWbXBDX57EdpOmZxpUaVR&index=2)

## overall
1. ### run from other image in docker hub
    - pull & run
    ```shell
    docker run --privileged -d -p 8080:80 tinhocthatladongian/project01  /sbin/init 
    =>
    Unable to find image 'tinhocthatladongian/project01:latest' locally
    docker: Error response from daemon: manifest for tinhocthatladongian/project01:latest not found: manifest unknown: manifest unknown.
    ```
    - search `tinhocthatladongian/project01` on `hub.docker.com` -> latest tags=`v2`
    ```shell
    docker run --privileged -d -p 8080:80 tinhocthatladongian/project01:v2  /sbin/init 
    docker ps
    =>
    CONTAINER ID   IMAGE                              COMMAND        CREATED          STATUS          PORTS                  NAMES
    7104e869d2cf   tinhocthatladongian/project01:v2   "/sbin/init"   37 seconds ago   Up 36 seconds   0.0.0.0:8080->80/tcp   tender_jones
    ```
    - access `localhost:8080` on browser
    ![localhost](screenshots/localhost.png)
    - edit container
    ```shell
    docker exec -it 7104e869d2cf bash
    cat /var/www/html/index.html 
    # <html><body>Hello Tin Hoc That La Don Gian V3</body></html>
    ```
    - access `localhost:8080` on browser -> "Hello Tin Hoc That La Don Gian V3"

    > if ERR "Failed to get D-Bus connection"
    - `nano ~/Library/Group\ Containers/group.com.docker/settings.json`: change `deprecatedCgroupv1` = false->true
    ```shell
    cat ~/Library/Group\ Containers/group.com.docker/settings.json | grep deprecatedCgroupv1 # "deprecatedCgroupv1": true,
    ```
1. ### login
    - already login
    ```shell
    cat ~/.docker/config.json | grep docker.io # "https://index.docker.io/v1/": {},
    ```
    - login after logout
    ```shell
    docker login
    =>
    ...
    Username: jwmagazineeas
    Password: 
    Login Succeeded
    ```    
    > need to install "jq": `brew install jq`
    - check username
    ```shell
    docker-credential-$(
    jq -r .credsStore ~/.docker/config.json
    ) list | jq -r '
    . |
        to_entries[] |
        select(
        .key | 
        contains("docker.io")
        ) |
        last(.value)
    '
    => jwmagazineeas
    ```
    - check more credential infos
    ```shell
    docker-credential-desktop list | \
    jq -r 'to_entries[].key'   | \
    while read; do
        docker-credential-desktop get <<<"$REPLY";
    done
    ```
    ```json
    {"ServerURL":"https://388576098417.dkr.ecr.us-west-2.amazonaws.com","Username":"AWS","Secret":"..."}
    {"ServerURL":"https://462123133781.dkr.ecr.ap-northeast-1.amazonaws.com","Username":"AWS","Secret":"..."}
    {"ServerURL":"https://462123133781.dkr.ecr.us-east-1.amazonaws.com","Username":"AWS","Secret":"..."}
    {"ServerURL":"https://index.docker.io/v1/","Username":"jwmagazineeas","Secret":"..."}
    {"ServerURL":"https://registry.heroku.com","Username":"_","Secret":"..."}
    ```