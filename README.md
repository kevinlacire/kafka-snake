# Kafka Snake [WIP]

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
java -cp target/kafka-snake-0.0.1-SNAPSHOT.jar fr.ultimaratio.lab.keyboard.listener.KeyboardListener
```
4. Check that you can receive commands using the Debugging consumer
```sh
java -cp target/kafka-snake-0.0.1-SNAPSHOT.jar fr.ultimaratio.lab.keyboard.listener.KeyboardInputConsumer
```