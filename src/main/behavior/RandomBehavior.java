package main.behavior;

import main.model.Boid;
import main.simulation.Forces;
import main.simulation.Vector2D;

import java.util.List;
import java.util.Random;

/**
 * RandomBehavior: En adfærdsstrategi hvor boids ændrer retning tilfældigt
 * i stedet for at følge flok-regler. Boids drejer med en lille tilfældig vinkel
 * ved hvert trin i simulationen.
 */
public class RandomBehavior implements BehaviorStrategy {
    private static final double MAX_TURN_ANGLE = Math.PI / 32; // ~5.6 grader
    private static final double TURN_PROBABILITY = 0.3; // 30% chance for at dreje ved hvert step
    private final Random random;

    public RandomBehavior() {
        this.random = new Random();
    }

    public RandomBehavior(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public Forces calculateForces(Boid boid, List<Boid> neighbors) {
        // Random adfærd ignorerer naboer og beregner i stedet en tilfældig drejning

        // Kun drej nogle gange for at gøre bevægelsen mere naturlig
        if (random.nextDouble() > TURN_PROBABILITY) {
            return new Forces(); // Ingen ændring denne gang
        }

        // Beregn en tilfældig drejningsvinkel
        double turnAngle = (random.nextDouble() * 2 - 1) * MAX_TURN_ANGLE;

        // Få nuværende hastighed
        double vx = boid.getVx();
        double vy = boid.getVy();

        // Beregn nuværende retning
        double currentAngle = Math.atan2(vy, vx);

        // Tilføj den tilfældige drejning
        double newAngle = currentAngle + turnAngle;

        // Beregn ny retningsvektor (med samme hastighed)
        double speed = Math.sqrt(vx * vx + vy * vy);
        double newVx = Math.cos(newAngle) * speed;
        double newVy = Math.sin(newAngle) * speed;

        // Returner forskellen som en "alignment" kraft
        // Vi bruger alignment-feltet da det påvirker retning
        Vector2D alignmentForce = new Vector2D(newVx - vx, newVy - vy);

        // Begræns kraften for at undgå pludselige ændringer
        double forceMagnitude = Math.sqrt(
            alignmentForce.x() * alignmentForce.x() +
            alignmentForce.y() * alignmentForce.y()
        );

        if (forceMagnitude > 0.05) {
            double scale = 0.05 / forceMagnitude;
            alignmentForce = new Vector2D(
                alignmentForce.x() * scale,
                alignmentForce.y() * scale
            );
        }

        return new Forces(Vector2D.ZERO, alignmentForce, Vector2D.ZERO);
    }
}
