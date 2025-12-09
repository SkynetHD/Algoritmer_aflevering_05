package main;

import main.simulation.FlockSimulation;
import main.model.BoidType;
import main.spatial.*;

public class Microbench {
    // Benchmark parametre
    private static final int WARMUP_ITERATIONS = 50;
    private static final int BENCHMARK_ITERATIONS = 200;
    private static final int SIMULATION_WIDTH = 1200;
    private static final int SIMULATION_HEIGHT = 800;

    // Test forskellige antal boids
    private static final int[] BOID_COUNTS = {50, 100, 250, 500, 1000, 2000};

    // Test forskellige neighbor radiuser
    private static final double[] NEIGHBOR_RADIUSES = {30.0, 50.0, 100.0};

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("    Boids Spatial Index Microbenchmark");
        System.out.println("=================================================");
        System.out.println();
        System.out.println("Parametre:");
        System.out.println("  Warmup iterations: " + WARMUP_ITERATIONS);
        System.out.println("  Benchmark iterations: " + BENCHMARK_ITERATIONS);
        System.out.println("  Simulation size: " + SIMULATION_WIDTH + "x" + SIMULATION_HEIGHT);
        System.out.println();

        // Opret alle spatial indices
        SpatialIndex[] indices = {
            new NaiveSpatialIndex(),
            new KDTreeSpatialIndex(),
            new SpatialHashIndex(SIMULATION_WIDTH, SIMULATION_HEIGHT, 50),
            new QuadTreeSpatialIndex(SIMULATION_WIDTH, SIMULATION_HEIGHT)
        };

        // Kør benchmarks for hver kombination af parametre
        for (double radius : NEIGHBOR_RADIUSES) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("NEIGHBOR RADIUS: " + radius + " pixels");
            System.out.println("=".repeat(80));

            for (int boidCount : BOID_COUNTS) {
                System.out.println("\n--- Antal boids: " + boidCount + " ---");

                for (SpatialIndex index : indices) {
                    double avgTime = runBenchmark(index, boidCount, radius);
                    System.out.printf("  %-20s: %.3f ms/iteration\n",
                        index.getName(), avgTime);
                }
            }
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("Benchmark færdig!");
        System.out.println("=".repeat(80));
    }

    private static double runBenchmark(SpatialIndex spatialIndex, int boidCount, double radius) {
        // Opret simulation
        FlockSimulation simulation = new FlockSimulation(SIMULATION_WIDTH, SIMULATION_HEIGHT);
        simulation.setSpatialIndex(spatialIndex);
        simulation.setNeighborRadius(radius);

        // Tilføj boids (kun STANDARD type for konsistens)
        for (int i = 0; i < boidCount; i++) {
            simulation.addBoid(BoidType.STANDARD);
        }

        // Warmup - få JVM til at optimere koden
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            simulation.update();
        }

        // Actual benchmark
        long totalTime = 0;
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            simulation.update();
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }

        // Beregn gennemsnitlig tid i millisekunder
        return (totalTime / (double) BENCHMARK_ITERATIONS) / 1_000_000.0;
    }
}