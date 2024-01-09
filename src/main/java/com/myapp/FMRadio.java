package com.myapp;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Set;
import java.util.HashSet;


public class FMRadio {
    private double frequency = 108.0;
    private final AtomicBoolean isOn = new AtomicBoolean(false);
    private final AtomicBoolean isTerminated = new AtomicBoolean(false);
    private final Lock lock = new ReentrantLock();
    private final Condition turnedOn = lock.newCondition();
    private Set<Double> stations = new HashSet<>();

    public void powerOnOff() {
        lock.lock();
        try {
            if (!isTerminated.get()) {
                isOn.set(!isOn.get());
                if (isOn.get()) {
                    System.out.println("Радио включено.");
                    turnedOn.signalAll();
                } else {
                    System.out.println("Радио выключено.");
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void scan() {
        lock.lock();
        try {
            while (!isOn.get() && !isTerminated.get()) {
                turnedOn.await();
            }
            if (isTerminated.get()) return;

            frequency = Math.round((Math.max(frequency - 0.1, 88.0)) * 10) / 10.0;
            System.out.println("Текущая частота сканирования: " + frequency + " МГц");
            if (stations.contains(frequency)) {
                System.out.println("Станция найдена на частоте: " + frequency + " МГц");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void setStations(double[] stationFrequencies) {
        lock.lock();
        try {
            for (double freq : stationFrequencies) {
                stations.add(freq);
            }
        } finally {
            lock.unlock();
        }
    }

    public Double getCurrentStation() {
        lock.lock();
        try {
            if (!isOn.get() || isTerminated.get()) {
                return null;
            }
            return stations.contains(frequency) ? frequency : null;
        } finally {
            lock.unlock();
        }
    }

    public void reset() {
        lock.lock();
        try {
            if (isOn.get() && !isTerminated.get()) {
                frequency = 108.0;
                System.out.println("Частота была сброшена до " + frequency + " МГц");
            }
        } finally {
            lock.unlock();
        }
    }

    public void end() {
        lock.lock();
        try {
            isTerminated.set(true);
            isOn.set(false);
            turnedOn.signalAll();
            System.out.println("Радио завершает работу.");
        } finally {
            lock.unlock();
        }
    }

    public boolean isOn() {
        return isOn.get() && !isTerminated.get();
    }

    public double getFrequency() {
        return frequency;
    }
}
