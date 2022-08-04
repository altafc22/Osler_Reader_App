public class UserStatus {
  String authorisation;
  String training;
  String isAdmin;

  public UserStatus() {}

  public UserStatus(String authorisation, String training, String isAdmin) {
    this.authorisation = authorisation;
    this.training = training;
    this.isAdmin = isAdmin;
  }

  public String getAuthorisation() {
    return authorisation;
  }

  public void setAuthorisation(String authorisation) {
    this.authorisation = authorisation;
  }

  public String getTraining() {
    return training;
  }

  public void setTraining(String training) {
    this.training = training;
  }

  public String getIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(String isAdmin) {
    this.isAdmin = isAdmin;
  }
}
