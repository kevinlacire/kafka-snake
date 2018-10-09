package fr.ultimaratio.lab.keyboard.listener;

import java.io.IOException;
import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

/**
 *
 * @author klacire
 */
public class KeyboardCommandMapper {

  public static void main(String[] args) throws IOException {

    Properties props = new Properties();
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Integer().getClass());

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, Integer> source = builder.stream("keyboard-keystrokes");
    source.mapValues(v -> {
      String mapped = charToCommand((char) v.intValue());
      System.out.println(v + " -> " + mapped);
      return mapped;
    })
    .to("keyboard-input", Produced.with(Serdes.String(), Serdes.String()));

    final Topology topology = builder.build();

    final KafkaStreams streams = new KafkaStreams(topology, props);

    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
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
