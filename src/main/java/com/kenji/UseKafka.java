package com.kenji;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import java.util.Properties;

import org.apache.kafka.clients.producer.*;

public class UseKafka extends AbstractVerticle {
	
	private Producer<String, String> producer;

	@Override
	public void start(Future<Void> fut) {
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("Linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		producer = new KafkaProducer<>(props);
		
		vertx
			.createHttpServer()
			.requestHandler(r -> {
				r.response().end("ok");
				r.handler(buffer -> {
					System.out.println("body message length is " + buffer.length());
					System.out.println("the mssage is [" + buffer.toString() + "]");
					producer.send(new ProducerRecord<String, String>("test", Integer.toString(1), buffer.toString()));
					producer.close();
				});
			})
			.listen(8080, result -> {
				if (result.succeeded()) {
					fut.complete();
				} else {
					fut.fail(result.cause());
				}
			});
	}
}
