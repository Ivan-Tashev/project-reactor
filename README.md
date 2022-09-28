# project-reactor

REACTIVE PROGRAMMING - describe set of steps as pipeline or stream trough which data flows, reactive stream process data as it becomes available, data can be endless.
// example: national geographic	subscription

Imperative - set of tasks, each running one at a time, one after another. Data is processed in bulk, and can’t be handed to next task until previous has completed.
vs.
Reactive - set of tasks to process data, that can run in parallel. Each task can process subsets of data, handing it to next task, while continue work on another subset of data.
Advantage in scalability and performance, than imperative
// example: water ballon/garden hose

REACTIVE STREAM - late 2013 initiative, Netflix, Light-bend and Pivotal - standard for asynchronous (perform task in parallel) stream processing with nonblocking back-pressure (consumers of data establish limits on how much data can process).

Java streams - typically synchronous, work with finite set of data, like iterating over a collection with functions.
vs.
Reactive streams - support processing of any size datasets, incl infinite. Process data in realtime, as it becomes available, with back-pressure to avoid consumer blocking.


REACTIVE STREAM SPECIFICATION SUMMED BY FOUR DEFINITIONS

Publisher - produces data, send to Subscriber per Subscription, provide single method through which Subscriber can subscribe.
public interface Publisher<T> {
void subscribe(Subscriber<? extends T> subscriber); //start data flow
}

Subscriber - once subscribed, receives events from Publisher, via methods.
public interface Subscriber<T> {
void onSubscribe(Subscription sub); // 1st event, pass Subscription
void onNext(T item); // for every item published, to be delivered
void onError(Throwable ex); // if there are any errors
void onComplete(); // informs that publisher finish the data
}

Subscription - Subscriber manage with it the subscription
public interface Subscription {
void request(long n); // request data, n back-pressure limit of items
void cancel(); // cancel the subscription, stop the data flow
}

Processor - combination of Subscriber and Publisher, subscribe to data flow and then publish the results…
public interface Processor<T, R> extends Subscriber<T>, Publisher<R> { }

PROJECT REACTOR - implementation of Reactive Streams specification that provide functional API for composing Reactive Streams, foundation for Spring’s reactive programming.

    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-core</artifactId>

    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-test</artifactId>

Mono and Flux are core Reactor’s types, implementation of Reactive Streams Publisher.
Flux represents pipeline of zero, one or many(infinite) data items.
Mono optimised for datasets having zero or one items.
// example Reactor (Mono, Flux) vs RxJava/ReactiveX (Single, Observable)

String name = "Ivan"; // perform steps, one after another
String capitalName = name.toUpperCase();
String text = "Hello " + capitalName;
System.out.println(text); // 100% all on a single thread
vs.
Mono.just("Ivan") // publisher (Mono impl) that emits value
.map(n -> name.toUpperCase()) // get value and publish it, Mono 2
.map(cn -> "Hello " + cn) // get value and publish it, Mono 3
.subscribe( //finally subscribe to Mono (publisher), receive data
System.out::println); //maybe single thread or maybe not?


COMMON REACTIVE OPERATIONS - more than 500 operations

@Test
void create(){
Flux<String> flux = Flux.just("A", "B", "C");

    StepVerifier.create(flux) // subscribe to Reactive type
            .expectNext("A") // apply assertion to each data, as it flows
            .expectNext(“B”, "C")
            .verifyComplete(); // verify the stream completed
}

// Creation operations - usually get a Flux/Mono from a Service or Repo…
.just("A", "B", “C"); // create reactive type, from single data items
.fromArray(new String[]{"A", "B", “C"}); // create from Array
.fromIterable(List.of("A", "B", “C")); // create from Iterable
.fromStream(Stream.of("A", "B", “C")); // create from Stream
.range(1, 5) // create range 1,2,3,4,5
.interval(Duration.ofSeconds(1)); // create infinite

// Combining and Splitting operations
first.mergeWith(second); // merge to single data stream
.zip(names, ages); // combine to Tuple2 pairs
.zip(names, ages, (a, b) -> a + b); // combine to Objects

// Filtering and Transforming operations
.skip(2); // skip first n elements “C”
.take(2); // take first n elements “A”, “B”
.filter(el -> el.contains(“B")); // filter based on predicate
.distinct(); // only unique values will be emitted
.map(el -> new User(el)); // transform elements, synchronously!
.flatMap(el -> // transform elements, synchronously!
Mono.just(el).map(User::new)
.subscribeOn(Schedulers.parallel())
); // each subscription take place in parallel thread
.buffer(2); // buffer to collections List.of(“A”, “B”)
.collectList(); // collect emitted elements to Iterable
.collectMap(el -> el.charAt(0)); // collect to Map, key from function

// Logic operations
all(el -> el.length() == 1); // all data elements, meet criteria
.any(el -> el.equalsIgnoreCase(“c")); // at least one el, meet criteria


Schedulers concurrency models, for executing the subscription:
.immediate() // in the current thread
.single() // in a single, reusable thread for all callers
.newSingle() // in a per-call dedicated thread
.elastic() // in a worker from unbounded pool, create as needed, idle 60s
.parallel() //in worker from fix-size pool, to the number CPU cores
WEBFLUX - from v.5, Spring’s reactive web framework, asynchronous and nonblocking, based on Project Reactor.

Servlet web frameworks - (Spring MVC) are blocking and multithreaded, using single thread per connection, as request is handled, a worker thread is pulled from the pool to process the data, while request thread is blocked until notified by worker that is finished.
// this is how most web apps are developed, things change from occasionally to frequently consuming content, IoT exchanging data with web APIs

Asynchronous web frameworks - (WebFlux) achieve higher scalability with fewer threads (one per CPU core), by Event Looping can be handled many requests per thread.
// everything is handled as event, when costly operation (db, network) is needed, event loop register a callback for it to be performed in parallel, while it moves on to handle other events. When operation is complete, it’s treated as event and pass data as response.


Both framework share many common components (Annotations), Spring MVC is based on Servlet API, which requires Servlet Container (default Tomcat) to execute on, while  WebFlux builds on top Reactive HTTP API and embedded server is Netty, which is asynchronous, non-blocking and event-driven, making it natural fit for reactive web framework.

    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>

// WebFlux controller methods accept and return reactive types (Flux/Mono), instead of domain types, and also can work with ReactiveX (Single, Observable, Completable)

// Reactive Spring MVC? can also work with Mono/Flux, however it is servlet-based, relaying on multithreading to handle requests.
