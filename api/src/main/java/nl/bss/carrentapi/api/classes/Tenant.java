package nl.bss.carrentapi.api.classes;

import nl.bss.carrentapi.api.classes.Car;

public class Tenant {
    private String name;
    private int birthDate;
    private Car car;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(int birthDate) {
        this.birthDate = birthDate;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Tenant(String name, int birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public Tenant(String name, int birthDate, Car car) {
        this.name = name;
        this.birthDate = birthDate;
        this.car = car;
    }
}