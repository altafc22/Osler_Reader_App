class User {
  String uuid;
  String deviceId;
  UserStatus userStatus;

  public User(String uuid, String deviceId, UserStatus userStatus) {
    this.uuid = uuid;
    this.deviceId = deviceId;
    this.userStatus = userStatus;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public UserStatus getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(UserStatus userStatus) {
    this.userStatus = userStatus;
  }
}
