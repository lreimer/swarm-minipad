# Docker Swarm Minipad

Visualise and manage Docker swarm containers using a Novation Launchpad Mini MK2.

## Building and Running

```bash
$ ./gradlew clean ass

$ docker swarm init
$ docker stack deploy --orchestrator swarm -c src/test/resources/test-stack.yaml test

$ ./gradlew run --args="-f src/test/resources/swarm-config.json"

$ ./gradlew graalNativeImage
$ cd build
$ ./swarm-minipad -f ../src/test/resources/swarm-config.json
```

## Configuration

We are using the default configuration of the Docker Java Client. Different ways of
configuration are found here: https://github.com/docker-java/docker-java

:attention: The default configuration using Unix sockets only works for Linux, because the Unit Socket
Factory dependency only includes the native libraries for this platform.

For development under MacOS the following worked for me:
```bash
$ brew install socat
$ socat -4 TCP-LISTEN:2375,fork UNIX-CONNECT:/var/run/docker.sock
```

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the MIT open source license, read the `LICENSE`
file for details.
