package fr.ultimaratio.lab.keyboard.listener;

import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KeyboardListener extends KeyAdapter {

  public static void main(String[] args) throws IOException {

    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "keyboard-listener");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());

    KafkaProducer<String, Integer> kp = new KafkaProducer<>(props);
    String uuid = UUID.randomUUID().toString();
    System.out.println("Ton UUID est : " + uuid);

    Scanner s = new Scanner(System.in);

    while (true) {
      if (System.in.available() != 0) {
          kp.send(new ProducerRecord<>("keyboard-keystrokes", uuid, s.next().codePointAt(0)), (metadata, exception) -> {
          if (exception != null) {
            exception.printStackTrace();
          }
        });
      }
    }
  }

}
