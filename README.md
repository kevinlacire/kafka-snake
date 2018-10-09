# Kafka "Snake" [WIP]

Just a square (let's pretend it's a tiny snake) controlled by a Java CLI app using Z,Q,S,D keystrokes (Yeah I did it the opposite way because why not).

## Prerequired

- Nodejs 10.11.0
- Npm 6.4.1
- Java 8
- Maven 3.5
- Docker 18.06.1 (with docker-compose)

## Installation

1. Deploy Docker composition

```sh
docker-compose up -d
```

2. Build Maven project

```sh
mvn clean package
```

3. Launch Java KeyboardListener class

```sh
java -cp target/keyboard-listener-0.0.1-SNAPSHOT.jar fr.ultimaratio.lab.keyboard.listener.KeyboardListener
```

4. Check that you can receive commands using the Debugging consumer

```sh
java -cp target/keyboard-listener-0.0.1-SNAPSHOT.jar fr.ultimaratio.lab.keyboard.listener.KeyboardInputConsumer
```

5. Install front dependencies

```sh
npm install
```

6. Launch webapp

```sh
npm start
```

7. Checkout out [localhost](http://localhost:8888)