package mi.practice.java.eight.effective;

import java.util.Optional;

public class OptionalDemo {
    /**
     * problematic method
     */
    public static String getCarInsuranceName(Person person) {
        // null and null and null
        return person.getCar().getInsurance().getName();
    }

    public static String getCarInsuranceNameWithNullCheck(Person person) {
        /*if(person ==null){
            Car car = person.getCar();
            if(car != null){
                Insurance insurance = car.getInsurance();
                if(insurance != null){
                    return insurance.getName();
                }
            }
        }
        return "Unknown";*/
        // or
        if (person == null
                || person.getCar() == null
                || person.getCar().getInsurance() == null) {
            return "Unknown";
        }
        return person.getCar().getInsurance().getName();
    }

    public static String getCarInsuranceName(Optional<PersonOptional> person) {
        return person.flatMap(PersonOptional::getCar)
                .flatMap(CarOptional::getInsurance)
                .map(InsuranceOptional::getName)
                .orElse("Unknown");
    }

    public static void main(String[] args) {
        Optional<Car> optCar = Optional.empty();
        Car car = new Car();
        optCar = Optional.of(car); // when car is null, throw NullPointerException
        optCar = Optional.ofNullable(car);

        Insurance insurance = new Insurance();
        Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
        Optional<String> name = optInsurance.map(Insurance::getName);

    }
}

class Person {
    private Car car;

    public Car getCar() {
        return car;
    }
}

class Car {
    private Insurance insurance;

    public Insurance getInsurance() {
        return insurance;
    }
}

class Insurance {
    private String name;

    public String getName() {
        return name;
    }
}

class PersonOptional {
    private Optional<CarOptional> car;

    public Optional<CarOptional> getCar() {
        return car;
    }
}

class CarOptional {
    private Optional<InsuranceOptional> insurance;

    public Optional<InsuranceOptional> getInsurance() {
        return insurance;
    }
}

class InsuranceOptional {
    private String name;

    public String getName() {
        return name;
    }
}