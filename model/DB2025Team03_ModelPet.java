package model;

/*
 * ControllerPet 클래스에서 사용할 getter, setter
 */

public class DB2025Team03_ModelPet {
    private int petId;
    private int userId;
    private String name;
    private int age;
    private String species;
    
    /**
     * @param petId		: 반려동물 ID
     * @param userId	: 유저 ID
     * @param name		: 반려동물 이름
     * @param age		: 반려동물 나이
     * @param species	: 반려동물 종
     */

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
    // 2025.05.25 수정 - 출력 format 변경(기존 DB2025Team03_Pet{userID: ~~ 이런 식이었음)
    return String.format("%-7d %-8d %-10s %-4d %-10s", petId, userId, name, age, species);
    }
}
