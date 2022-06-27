package net.pingfang.network.tcp.parser.strateies;

import java.util.function.Function;
import java.util.function.Supplier;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import net.pingfang.iot.common.ValueObject;
import net.pingfang.network.tcp.parser.PayloadParser;
import net.pingfang.network.tcp.parser.PayloadParserBuilderStrategy;
import net.pingfang.network.tcp.parser.PayloadParserType;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public abstract class VertxPayloadParserBuilder implements PayloadParserBuilderStrategy {
	@Override
	public abstract PayloadParserType getType();

	protected abstract RecordParser createParser(ValueObject config);

	@Override
	public PayloadParser build(ValueObject config) {
		return new RecordPayloadParser(() -> createParser(config));
	}

	static class RecordPayloadParser implements PayloadParser {
		private final Supplier<RecordParser> recordParserSupplier;
		private final EmitterProcessor<Buffer> processor = EmitterProcessor.create(false);
		private final FluxSink<Buffer> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);

		private RecordParser recordParser;

		public RecordPayloadParser(Supplier<RecordParser> recordParserSupplier) {
			this.recordParserSupplier = recordParserSupplier;
			reset();
		}

		@Override
		public void handle(Buffer buffer) {
			recordParser.handle(buffer);
		}

		@Override
		public Flux<Buffer> handlePayload() {
			return processor.map(Function.identity());
		}

		@Override
		public void close() {
			processor.onComplete();
		}

		@Override
		public void reset() {
			this.recordParser = recordParserSupplier.get();
			this.recordParser.handler(sink::next);
		}
	}

}
