package fr.hunh0w.dscore.utils;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ParticleUtils {

    public static void spawnCloudAlongLine(Location start, Location end, int particleCount) {
        if(!start.getWorld().getName().equals(end.getWorld().getName()))
            return;

        Vector vector = end.toVector().subtract(start.toVector());
        Vector dividedVector = vector.divide(new Vector(particleCount, particleCount, particleCount));
        Location loc = start.clone();
        for (double i = 0; i <= particleCount; i++) {
            loc.getBlock().setType(Material.RED_WOOL);

            loc = loc.add(dividedVector);
        }
    }

    public static void spawnParticleAlongLine(Location start, Location end, Particle particle, int pointsPerLine, int particleCount, double offsetX, double offsetY, double offsetZ, double extra, @Nullable Object data, boolean forceDisplay,
                                       @Nullable Predicate<Location> operationPerPoint) {
        double d = start.distance(end) / pointsPerLine;
        for (int i = 0; i < pointsPerLine; i++) {
            Location l = start.clone();
            Vector direction = end.toVector().subtract(start.toVector()).normalize();
            Vector v = direction.multiply(i * d);
            l.add(v.getX(), v.getY(), v.getZ());
            if (operationPerPoint == null) {
                start.getWorld().spawnParticle(particle, l, particleCount, offsetX, offsetY, offsetZ, extra, data, forceDisplay);
                continue;
            }
            if (operationPerPoint.test(l)) {
                start.getWorld().spawnParticle(particle, l, particleCount, offsetX, offsetY, offsetZ, extra, data, forceDisplay);
            }
        }
    }

}
