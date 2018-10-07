package fr.ultimaratio.lab.keyboard.listener;

import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KeyboardInputConsumer extends KeyAdapter {

  public static void main(String[] args) throws IOException {

    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

    KafkaConsumer<Long, String> kc = new KafkaConsumer<>(props);
    kc.subscribe(Arrays.asList("keyboard-input"));

    while (true) {
      ConsumerRecords<Long, String> records = kc.poll(Duration.ofSeconds(1));
      records.forEach(record -> {
        System.out.println(record.value());
      });
    }
  }

}
