package net.pingfang.network.tcp.parser;

import java.util.function.Function;

import io.vertx.core.buffer.Buffer;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

public class DirectRecordParser implements PayloadParser {

	EmitterProcessor<Buffer> processor = EmitterProcessor.create(false);

	@Override
	public void handle(Buffer buffer) {
		processor.onNext(buffer);
	}

	@Override
	public Flux<Buffer> handlePayload() {
		return processor.map(Function.identity());
	}

	@Override
	public void close() {
		processor.onComplete();
	}
}
