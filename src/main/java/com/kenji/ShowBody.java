package com.kenji;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;


public class ShowBody extends AbstractVerticle {
	
	@Override
	public void start(Future<Void> fut) {
		vertx
			.createHttpServer()
			.requestHandler(r -> {
				r.response().end("ok");
				r.handler(buffer -> {
					System.out.println("body message length is " + buffer.length());
					System.out.println("the mssage is [" + buffer.toString() + "]");
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
