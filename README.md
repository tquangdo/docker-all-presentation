# docker-all-presentation 🐳

![Stars](https://img.shields.io/github/stars/tquangdo/docker-all-presentation?color=f05340)
![Issues](https://img.shields.io/github/issues/tquangdo/docker-all-presentation?color=f05340)
![Forks](https://img.shields.io/github/forks/tquangdo/docker-all-presentation?color=f05340)
[![Report an issue](https://img.shields.io/badge/Support-Issues-green)](https://github.com/tquangdo/docker-all-presentation/issues/new)

## reference
[tinhocthatladongian](https://www.youtube.com/watch?v=q3Vhi_MvUsQ&list=PLjCpH2Qpki-sTjdlYXE8AifSKQFa8ZL23&index=27)

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
1. ### pull & push in docker hub
    1. #### src code
        - `overall/Dockerfile` & `overall/index.html`
    1. #### local
        ```shell
        overall$ docker build -t img-nginx-demo .
        docker images # will see "img-nginx-demo"
        docker run -d --name cont-nginx-demo -p 8080:80 img-nginx-demo
        docker ps # will see "cont-nginx-demo"
        ```
        - access `localhost:8080` on browser -> "DTQ!!!"
    1. #### push docker hub
        1. ##### create new repo in docker hub
        - create repo `dockrepo-nginx-demo` in docker hub
        ```shell
        docker tag img-nginx-demo jwmagazineeas/dockrepo-nginx-demo:v1
        docker push jwmagazineeas/dockrepo-nginx-demo:v1
        ```
        ![pushv1](screenshots/pushv1.png)
        1. ##### NO need to create new repo in docker hub
        ```shell
        docker ps
        =>
        CONTAINER ID   IMAGE          COMMAND                  CREATED       STATUS       PORTS                  NAMES
        0509bea79b68   f5ed82203f09   "/docker-entrypoint.…"   3 hours ago   Up 3 hours   0.0.0.0:8080->80/tcp   cont-nginx-demo
        docker images
        =>
        REPOSITORY                          TAG       IMAGE ID       CREATED          SIZE
        jwmagazineeas/dockrepo-nginx-del    1.0       27fe34b66905   21 seconds ago   142MB
        docker commit -m "test docker commit CMD" -a "DoTQ" 0509bea79b68 jwmagazineeas/dockrepo-nginx-del:1.0
        docker push jwmagazineeas/dockrepo-nginx-del:1.0
        ```
        - will auto create repo `dockrepo-nginx-del` in docker hub
        ![pushdel](screenshots/pushdel.png)
        - delete containers & images
    1. #### pull docker hub
        ```shell
        docker pull jwmagazineeas/dockrepo-nginx-demo:v1
        docker images
        =>
        REPOSITORY                          TAG       IMAGE ID       CREATED          SIZE
        jwmagazineeas/dockrepo-nginx-demo   v1        262b11cf5916   14 minutes ago   142MB
        docker run -d --name cont-nginx-demo -p 8080:80 262b11cf5916
        docker ps # will see "cont-nginx-demo"
        ```
        - access `localhost:8080` on browser -> "DTQ!!!"
    1. #### push v2 docker hub
        - change `overall/index.html: DTQ V2!!!`
        ```shell
        docker build -t img-nginx-demo:v2 .
        docker images # will see "img-nginx-demo > v2"
        ```
        - test local before pushing
        ```shell
        docker run -d --name cont-nginx-demo -p 8080:80 img-nginx-demo:v2
        docker ps # will see "cont-nginx-demo"
        ```
        - access `localhost:8080` on browser -> "DTQ v2!!!"
        - push
        ```shell
        docker tag img-nginx-demo:v2 jwmagazineeas/dockrepo-nginx-demo:v2
        docker push jwmagazineeas/dockrepo-nginx-demo:v2
        ```
        ![pushv2](screenshots/pushv2.png)
        - delete containers & images
