package io.gingersnapproject.util;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.junit.platform.commons.util.ReflectionUtils;

public class Utils {

   private Utils() { }

   @SuppressWarnings("unchecked")
   public static <R, O> R extractField(Class<O> clazz, String field, O instance) {
      var value = ReflectionUtils.tryToReadFieldValue(clazz, field, instance);;
      return (R) value.getOrThrow(e ->
            new RuntimeException(String.format("Unable to read field '%s' of %s", field, clazz.getSimpleName()), e));
   }

   public static void eventually(Supplier<String> messageSupplier, BooleanSupplier condition, long timeout, TimeUnit timeUnit) {
      try {
         long timeoutNanos = timeUnit.toNanos(timeout);
         // We want the sleep time to increase in arithmetic progression
         // 30 loops with the default timeout of 30 seconds means the initial wait is ~ 65 millis
         int loops = 30;
         int progressionSum = loops * (loops + 1) / 2;
         long initialSleepNanos = timeoutNanos / progressionSum;
         long sleepNanos = initialSleepNanos;
         long expectedEndTime = System.nanoTime() + timeoutNanos;
         while (expectedEndTime - System.nanoTime() > 0) {
            if (condition.getAsBoolean())
               return;
            LockSupport.parkNanos(sleepNanos);
            sleepNanos += initialSleepNanos;
         }
         assert condition.getAsBoolean() : fail(messageSupplier.get());
      } catch (Exception e) {
         throw new RuntimeException("Unexpected!", e);
      }
   }
}
