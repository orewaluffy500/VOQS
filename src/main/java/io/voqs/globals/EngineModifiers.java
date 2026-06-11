package io.voqs.globals;

public class EngineModifiers {
    private static float pulseSpeed = 1;
    private static float buildSpeed = 1;
    private static float walkSpeed = 1;

    public static float getPulseSpeed() {
        return pulseSpeed;
    }

    public static void setPulseSpeed(float pulseSpeed) {
        EngineModifiers.pulseSpeed = Math.clamp(pulseSpeed, 0.25f, 8f);
    }

    public static float getBuildSpeed() {
        return buildSpeed;
    }

    public static void setBuildSpeed(float buildSpeed) {
        EngineModifiers.buildSpeed = Math.clamp(buildSpeed, 0.1f, 4f);
    }

    public static float getWalkSpeed() {
        return walkSpeed;
    }

    public static void setWalkSpeed(float walkSpeed) {
        EngineModifiers.walkSpeed = Math.clamp(walkSpeed, 0.1f, 8f);
    }
}
