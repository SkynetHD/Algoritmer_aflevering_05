# Rapport: Aflevering 5 - Boids

## Delopgave 1: Refaktuering af koden
Jeg har flyttet koden der håndterer hvordan boids bevæger sig ud af Boid-klassen.

Konkret er metoderne calculateForces, calculateSeparation, calculateAlignment og calculateCohesion flyttede til FlockBehavior-klassen.

## Delopgave 2: Ny type boid

Jeg har lavet en ny type boid der bevæger sig tilfældigt i stedet for at følge flokken:

- Hvide boids følger flokken
- Gule boids bevæger sig tilfældigt og ignorerer andre boids
- I simulationen er 80% hvide og 20% gule

## Delopgave 3: test

Jeg har testet hvor hurtigt de fire forskellige nabosøgningsmetoder er:
- Antal boids: 50, 100, 250, 500, 1000, 2000
- Neighbor radius: 30, 50, 100 pixels
- 200 gennemløb for hver test

### Resultater med 2000 boids
**(30 pixels)**:
- QuadTree: 1.72 ms (hurtigst)
- KD-Tree: 2.23 ms
- Spatial Hashing: 2.24 ms
- Naive: 7.25 ms (langsomst)

**(50 pixels)**:
- QuadTree: 3.32 ms
- Spatial Hashing: 3.41 ms
- KD-Tree: 4.46 ms
- Naive: 8.16 ms

**(100 pixels)**:
- QuadTree: 6.52 ms
- Spatial Hashing: 8.83 ms
- KD-Tree: 9.31 ms
- Naive: 13.20 ms

### Konklusion

QuadTree er klart den hurtigste metode og bliver ved med at være hurtig selv når der er mange boids. Den simple Naive metode bliver meget langsommere når antallet af boids vokser - ved 2000 boids er den 2× langsommere end QuadTree.

Naive boids = O(n^2) - Naive følger perfekt O(n^2) - som forventet
Quadtree = O(n log n) - QuadTree overperformer - fordi boids naturligt klumper i flokke, så QuadTree's adaptive opdeling er perfekt
KD-tree = O(n log n) - KD-Tree underperformer - fordi den skal genopbygges hver frame, og det koster tid
Spatial hashing = O(n log n) - Spatial Hashing er stabil - O(n) holder fint når boids er spredt