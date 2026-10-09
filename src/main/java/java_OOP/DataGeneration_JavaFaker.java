package java_OOP;


import net.datafaker.Faker;

import java.util.Locale;

public class DataGeneration_JavaFaker {

    public static void main(String[] args) {
        Faker faker = new Faker(Locale.ENGLISH);
        System.out.println(faker.name().firstName());
        System.out.println(faker.name().lastName());
        System.out.println(faker.name().fullName());
        System.out.println(faker.animal().name());
        System.out.println(faker.internet().emailAddress());

    }

}
