# Docker Swarm Minipad

Visualise and manage Docker swarm containers using a Novation Launchpad Mini MK2.

## Building and Running

```bash
$ ./gradlew clean ass
$ ./gradlew run --args="-f src/test/resources/swarm-config.json"
```

## Configuration

We are using the default configuration of the Docker Java Client. Different ways of
configuration are found here: https://github.com/docker-java/docker-java

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the MIT open source license, read the `LICENSE`
file for details.
