package fr.ultimaratio.lab.keyboard.listener;

import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KeyboardListener extends KeyAdapter {

  public static void main(String[] args) throws IOException {

    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "keyboard-listener");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    KafkaProducer<String, String> kp = new KafkaProducer<>(props);
    String uuid = UUID.randomUUID().toString();
    System.out.println("Ton UUID est : " + uuid);

    Scanner s = new Scanner(System.in);

    while (true) {
      if (System.in.available() != 0) {
        String command = charToCommand(s.next().charAt(0));
        if (command == null) {
          continue;
        }

        kp.send(new ProducerRecord<>("keyboard-input", uuid, command), (metadata, exception) -> {
          if (exception != null) {
            exception.printStackTrace();
          }
        });
      }
    }
  }

  private static String charToCommand(char c) {
    if (c == 0x1B) {
      return null;
    }

    switch (c) {
      case 'z':
        return "up";
      case 'q':
        return "left";
      case 's':
        return "down";
      case 'd':
        return "right";
      default:
        System.out.println("Touche non reconnue. Utilises Z,Q,S ou D");
    }

    return null;
  }

}
