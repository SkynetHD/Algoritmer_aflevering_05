run:
    javac -d build src/main/*.java src/main/*/*.java && java -cp build main.Boids

bench:
    javac -d build src/main/*.java src/main/*/*.java && java -cp build main.Microbench