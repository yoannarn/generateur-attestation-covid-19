package fr.yoannarn.covid19certificategenerator;

public class UserIdentity {

    private String name;
    private String birthday;
    private String birthPlace;
    private String address;
    private String currentCountry;

    public UserIdentity()
    {
    }
    public UserIdentity(String name, String birthday, String birthPlace, String address, String currentCountry) {
        this.name = name;
        this.birthday = birthday;
        this.birthPlace = birthPlace;
        this.address = address;
        this.currentCountry = currentCountry;

    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
