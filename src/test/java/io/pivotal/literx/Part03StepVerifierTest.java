/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pivotal.literx;

import java.time.Duration;
import java.util.function.Supplier;

import io.pivotal.literx.domain.User;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Learn how to use StepVerifier to test Mono, Flux or any other kind of Reactive Streams Publisher.
 *
 * @author Sebastien Deleuze
 * @see <a href="https://projectreactor.io/docs/test/release/api/reactor/test/StepVerifier.html">StepVerifier Javadoc</a>
 */
public class Part03StepVerifierTest {

	Part03StepVerifier workshop = new Part03StepVerifier();

//========================================================================================

	/**
	 * Use StepVerifier to check that the flux parameter emits "foo" and "bar" elements then completes successfully.
	 */
	@Test
	public void expectElementsThenComplete() {
		final Flux<String> stringFlux = workshop.expectFooBarComplete();
		StepVerifier.create(stringFlux)
				.expectNext("foo")
				.expectNext("bar")
				.verifyComplete();
	}

//========================================================================================

	/**
	 * Use StepVerifier to check that the flux parameter emits "foo" and "bar" elements then a RuntimeException error.
	 */
	@Test
	public void expect2ElementsThenError() {
		final Flux<String> stringFlux = workshop.expectFooBarError();
		StepVerifier.create(stringFlux)
				.expectNext("foo")
				.expectNext("bar")
				.verifyError(RuntimeException.class);
	}

//========================================================================================

	/**
	 * Use StepVerifier to check that the flux parameter emits a User with "swhite" username
	 * and another one with "jpinkman" then completes successfully.
	 */
	@Test
	public void expectElementsWithThenComplete() {
		final Flux<User> userFlux = workshop.expectSkylerJesseComplete();
		StepVerifier.create(userFlux)
				.expectNextMatches(o -> "swhite".equals(o.getUsername()))
				.expectNextMatches(o -> "jpinkman".equals(o.getUsername()))
				.verifyComplete();
	}

//========================================================================================

	/**
	 * Expect 10 elements then complete and notice how long the test takes.
	 */
	@Test
	public void count() {
		final Flux<Long> longFlux = workshop.expect10Elements();
		StepVerifier.create(longFlux)
				.expectNextCount(10)
				.verifyComplete();
	}

//========================================================================================

	/**
	 * Expect 3600 elements at intervals of 1 second, and verify quicker than 3600s
	 * by manipulating virtual time thanks to StepVerifier#withVirtualTime, notice how long the test takes
	 */
	@Test
	public void countWithVirtualTime() {
		final Supplier<Flux<Long>> fluxSupplier = workshop.expect3600Elements();
		StepVerifier.withVirtualTime(fluxSupplier)
				.thenAwait(Duration.ofSeconds(3600))
				.expectNextCount(3600)
				.verifyComplete();
	}
}
