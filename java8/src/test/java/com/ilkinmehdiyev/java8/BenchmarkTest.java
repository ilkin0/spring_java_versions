package com.ilkinmehdiyev.java8;

import com.ilkinmehdiyev.java8.model.Product;
import com.ilkinmehdiyev.java8.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@Fork(value = 2, warmups = 1)
@Warmup(iterations = 1, time = 55, timeUnit = TimeUnit.MILLISECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkTest {

  public static final int noOfIterations = 1000;

  private static ConfigurableApplicationContext context;
  private static ProductRepository productRepository;

  @Setup(Level.Trial)
  public void init() {
    context = SpringApplication.run(Java8Application.class);
    productRepository = context.getBean(ProductRepository.class);
  }

  @TearDown(Level.Trial)
  public void tearDown() {
    context.close();
  }

  @Benchmark
  public void testSaveEntitiesBenchmark() {
    List<Product> entities =
        createEntities(noOfIterations); // Method to create 100 instances of Product
    productRepository.saveAll(entities);
  }

  @Benchmark
  public void testFetchEntitiesBenchmark() {
    Iterable<Product> fetchedEntities = productRepository.findAll();
  }

  @Benchmark
  public void testUpdateEntitiesBenchmark() {
    List<Product> entities =
        createEntities(noOfIterations); // Method to create 100 instances of Product
    entities.forEach(
        entity ->
            entity.setUuid(
                UUID.randomUUID().toString())); // Assuming 'setSomeField' is a method in Product
    productRepository.saveAll(entities);
  }

  private static List<Product> createEntities(int count) {
    List<Product> products = new ArrayList<>();
    IntStream.range(0, count).forEach(i -> products.add(getProduct(i)));

    return products;
  }

  private static Product getProduct(int i) {
    Product product = new Product();
    product.setDescription("Desc " + i);
    product.setQuantity(i);
    product.setCategory("category " + i);
    product.setName("product NO " + i);
    product.setPrice((BigDecimal.valueOf(i)));
    product.setUuid(UUID.randomUUID().toString());

    return product;
  }

  public static void main(String[] args) throws RunnerException {
    Options opts =
        new OptionsBuilder()
            .include("\\." + BenchmarkTest.class.getSimpleName() + "\\.")
            .threads(1)
            .shouldDoGC(true)
            .shouldFailOnError(true)
            .jvmArgs("-server")
            .resultFormat(ResultFormatType.CSV)
            .result("benchmark-result/java8/" + System.currentTimeMillis() + ".csv")
            .build();
    new Runner(opts).run();
  }
}
