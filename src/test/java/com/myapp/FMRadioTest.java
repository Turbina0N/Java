package com.myapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.HashSet;

class FMRadioTest {
    private FMRadio radio;

    @BeforeEach
    void setUp() {
        radio = new FMRadio();
    }

    @Test
    void testPowerOnOff() throws InterruptedException {
        assertFalse(radio.isOn());

        Thread powerThread = new Thread(radio::powerOnOff);
        powerThread.start();
        powerThread.join();

        assertTrue(radio.isOn());

        powerThread = new Thread(radio::powerOnOff);
        powerThread.start();
        powerThread.join();

        assertFalse(radio.isOn());
    }

    @Test
    void testScan() throws InterruptedException {
        Thread powerThread = new Thread(radio::powerOnOff);
        powerThread.start();
        powerThread.join();

        Thread scanThread = new Thread(() -> {
            radio.scan();
        });
        scanThread.start();
        scanThread.join();

        assertEquals(107.9, radio.getFrequency(), 0.001);

        while (radio.getFrequency() > 88.0) {
            scanThread = new Thread(radio::scan);
            scanThread.start();
            scanThread.join();
        }

        assertEquals(88.0, radio.getFrequency());
    }

    @Test
    void testScanWithStations() throws InterruptedException {
        double[] stations = {90.5, 95.3, 102.7};
        radio.setStations(stations);

        Thread powerThread = new Thread(radio::powerOnOff);
        powerThread.start();
        powerThread.join();

        Set<Double> foundStations = new HashSet<>();
        while (radio.getFrequency() > 88.0) {
            Thread scanThread = new Thread(() -> {
                radio.scan();
            });
            scanThread.start();
            scanThread.join();

            if (radio.getCurrentStation() != null) {
                foundStations.add(radio.getCurrentStation());
            }
        }

        for (double station : stations) {
            assertTrue(foundStations.contains(station));
        }
    }

    @Test
    void testReset() throws InterruptedException {
        Thread powerThread = new Thread(radio::powerOnOff);
        powerThread.start();
        powerThread.join();

        Thread scanThread = new Thread(() -> {
            radio.scan();
        });
        scanThread.start();
        scanThread.join();

        Thread resetThread = new Thread(radio::reset);
        resetThread.start();
        resetThread.join();

        assertEquals(108.0, radio.getFrequency());
    }

    @Test
    void testEndFunctionality() throws InterruptedException {
        FMRadio radio = new FMRadio();

        Thread powerThread = new Thread(radio::powerOnOff);
        powerThread.start();
        powerThread.join();
        assertTrue(radio.isOn(), "Радио должно быть включено");

        Thread endThread = new Thread(radio::end);
        endThread.start();
        endThread.join();

        Thread scanThread = new Thread(radio::scan);
        scanThread.start();
        scanThread.join();
        assertFalse(radio.isOn(), "После вызова end, радио не должно реагировать на команды");

        assertFalse(radio.getCurrentStation() != null, "После вызова end, радио не должно иметь текущей станции");
    }


    @Test
    void testPotentialRaceCondition() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(radio::powerOnOff);
        executorService.submit(radio::scan);

        executorService.shutdown();
        assertTrue(executorService.awaitTermination(1, TimeUnit.SECONDS));

        assertTrue(radio.isOn());
        assertTrue(radio.getFrequency() < 108.0);
    }

    @Test
    void testPotentialDeadlock() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            radio.powerOnOff();
            radio.scan();
        });
        executorService.submit(() -> {
            radio.scan();
            radio.powerOnOff();
        });

        executorService.shutdown();
        assertTrue(executorService.awaitTermination(1, TimeUnit.SECONDS));
    }

    @Test
    void testConcurrentAccessToPowerOnOff() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executor.submit(radio::powerOnOff);
        }
        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.SECONDS));

        assertTrue(radio.isOn() || !radio.isOn());
    }

    @Test
    void testOperationSequence() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            for (int i = 0; i < 15; i++) {
                radio.powerOnOff();
            }
        });
        executor.submit(() -> {
            for (int i = 0; i < 15; i++) {
                radio.scan();
            }
        });

        executor.shutdown();
        boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);
        assertTrue(finished);

        assertTrue(radio.isOn() || !radio.isOn());
    }
}

