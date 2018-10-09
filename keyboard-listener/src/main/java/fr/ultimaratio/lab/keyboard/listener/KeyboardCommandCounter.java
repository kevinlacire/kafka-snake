package fr.ultimaratio.lab.keyboard.listener;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;

/**
 *
 * @author klacire
 */
public class KeyboardCommandCounter {

  public static void main(String[] args) throws IOException {

    Properties props = new Properties();
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    StreamsBuilder builder = new StreamsBuilder();

    KStream<String, String> textLines = builder.stream("TextLinesTopic");
    KTable<String, Long> wordCounts = textLines
        .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
        .groupBy((key, word) -> word)
        .count(Materialized.as("Counts"));


    KTable<String, Long> keyboardInput = builder
      .table("keyboard-input", Consumed.with(Serdes.String(), Serdes.String()))
      .groupBy((key, value) -> value)
//      .groupBy((uuid, direction) -> direction)
      .count()
      .toStream();

    final Topology topology = builder.build();

    final KafkaStreams streams = new KafkaStreams(topology, props);
    
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

}
