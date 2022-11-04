package fr.raluy.simplespreadsheet.testObjects;

public class BioStatJava {
    private final String name;
    private final String sex;
    private final String age;
    private final String height;
    private final String weight;

    public BioStatJava(String name, String sex, String age, String height, String weight) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getAge() {
        return age;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }
}