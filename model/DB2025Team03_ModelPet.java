package model;

public class DB2025Team03_ModelPet {
    private int petId;
    private int userId;
    private String name;
    private int age;
    private String species;

    public DB2025Team03_ModelPet(int petId, int userId, String name, int age, String species) {
        this.petId = petId;
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.species = species;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }


    @Override
    public String toString() {
        return "DB2025Team03_ModelPet{" +
                "petId=" + petId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", species='" + species + '\'' +
                '}';
        
    }
}
