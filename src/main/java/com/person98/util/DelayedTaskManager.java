package com.person98.util;

import net.minecraft.client.MinecraftClient;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class DelayedTaskManager {
    private static final Queue<DelayedTask> delayedTasks = new LinkedList<>();

    public static void scheduleDelayedTask(Consumer<MinecraftClient> action, int delayTicks) {
        synchronized (delayedTasks) {
            delayedTasks.add(new DelayedTask(action, delayTicks));
        }
    }

    public static void tick() {
        synchronized (delayedTasks) {
            while (!delayedTasks.isEmpty() && delayedTasks.peek().remainingTicks <= 0) {
                DelayedTask task = delayedTasks.poll();
                task.action.accept(MinecraftClient.getInstance());
            }
            delayedTasks.forEach(task -> task.remainingTicks--);
        }
    }

    private static class DelayedTask {
        final Consumer<MinecraftClient> action;
        int remainingTicks;

        DelayedTask(Consumer<MinecraftClient> action, int delayTicks) {
            this.action = action;
            this.remainingTicks = delayTicks;
        }
    }
}
