package model;

public class DB2025Team03_ModelUser {
    private int userId;
    private String name;
    private int birthYear;
    private String gender;
    private String email;

    /**
     * @param userId 사용자 ID
     * @param name 사용자 이름
     * @param birthYear 출생 연도
     * @param gender 성별
     * @param email 이메일
     */
    public DB2025Team03_ModelUser(int userId, String name, int birthYear, String gender, String email) {
        this.userId = userId;
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;
        this.email = email;
    }

    // Getter, Setter
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

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "DB2025Team03_ModelUser{" +
               "userId=" + userId +
               ", name='" + name + '\'' +
               ", birthYear=" + birthYear +
               ", gender='" + gender + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}
