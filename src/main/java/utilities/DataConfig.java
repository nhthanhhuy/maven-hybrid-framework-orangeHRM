package utilities;

import net.datafaker.Faker;

import java.util.Locale;

public class DataConfig {

    private Faker faker = new Faker(Locale.ENGLISH) ;

    public DataConfig() {}

    public static DataConfig getData() {
        return new DataConfig();
    }

    public String getFirstname() {
        return faker.name().firstName();
    }

    public String getLastname() {
        return faker.name().lastName();
    }

    public String getMiddlename() {
        return faker.name().nameWithMiddle();
    }
}
