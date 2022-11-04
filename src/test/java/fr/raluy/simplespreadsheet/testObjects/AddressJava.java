package fr.raluy.simplespreadsheet.testObjects;

public class AddressJava {
    private final String name;
    private final String surname;
    private final String address;
    private final String city;
    private final String state;
    private final String stateNb;

    public AddressJava(String name, String surname, String address, String city, String state, String stateNb) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.city = city;
        this.state = state;
        this.stateNb = stateNb;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getStateNb() {
        return stateNb;
    }
}
