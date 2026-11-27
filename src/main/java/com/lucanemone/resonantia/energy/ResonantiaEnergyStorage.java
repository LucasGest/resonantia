package com.lucanemone.resonantia.energy;

import net.neoforged.neoforge.energy.EnergyStorage;

public class ResonantiaEnergyStorage extends EnergyStorage {

    public ResonantiaEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    /** Consomme de l'énergie en interne (traitement machine), sans passer par la limite maxExtract réservée aux câbles externes. */
    public boolean consumeEnergyInternal(int amount) {
        if (this.energy >= amount) {
            this.energy -= amount;
            return true;
        }
        return false;
    }

    /** Génère de l'énergie en interne (combustion), sans passer par la limite maxReceive réservée aux câbles externes. */
    public void generateEnergyInternal(int amount) {
        this.energy = Math.min(capacity, this.energy + amount);
    }

    public void setEnergy(int value) {
        this.energy = Math.max(0, Math.min(capacity, value));
    }
}