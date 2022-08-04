import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DeviceUserManager {

  // read file from portal
  public ArrayList<User> readPortalUserList() {
    return readFile(Constants.PortalUserList);
  }

   // read file from device
  public ArrayList<User> readDeviceUserList() {
    return readFile(Constants.DeviceUserList);
  }

  //create file in device to store updated users
  public void updateUsers(ArrayList<User> deviceUsers) {
    try {  
      File file = new File(Constants.DeviceUserListUpdated);
      if(!file.exists())
        file.createNewFile();

      PrintWriter writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
      
      for(User user : deviceUsers){
        
        String type = "";
        String authorzation = "";
        String training = "";

        if(user.userStatus.getAuthorisation().equals("1")){
          authorzation = "Authorised ";
        } else {
          authorzation = "Disabled   ";
        }

        if(user.userStatus.getIsAdmin().equals("1")) {
          type = "Admin    ";
        } else {
          type = "Operator ";
        }

        if(user.userStatus.getTraining().equals("1")){
          training = "Trained   ";
        } else {
          training = "Untrained ";
        }
        writer.println(user.uuid+"\t"+user.deviceId+"\t"+authorzation+type+"\t"+training);
      }
      writer.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
 
  }

  public void printFileData(String path) {
    try {
      File myObj = new File(path);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        System.out.println(data);
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private ArrayList<User> readFile(String path) {
    ArrayList<User> userList = new ArrayList<>();
    try {
      File myObj = new File(path);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        if(!data.isEmpty()){
          User user = getUser(data);
          //System.out.println("UUID: "+user.uuid+", Device: "+user.deviceId+", Status: {Auth: "+user.userStatus.authorisation+", Training: "+user.userStatus.training+", IsAdmin: "+user.userStatus.isAdmin+"}");
          userList.add(user);
        }
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      //System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return userList;
  }

  private User getUser(String record) {

    String[] parts = record.split("\t");

    String uuid = parts[0];
    String deviceId = parts[1];
    UserStatus status = getUserStatus(parts[2]);
    User user = new User(uuid,deviceId,status);

    return user;
  }

  private UserStatus getUserStatus(String hexString){
    
    String binary = hexToBin(hexString.replace("0x", ""));
    
    String authorisation = ""+binary.charAt(0); //bit 7
    String training = ""+binary.charAt(1); //bit 6
    String isAdmin = ""+binary.charAt(2); //bit 5

    return new UserStatus(authorisation,training,isAdmin);
  }

  private static String hexToBin (String hex){
    int i = Integer.parseInt(hex, 16);
    String bin = Integer.toBinaryString(i);
    while (bin.length()<8){
        bin="0"+bin;
    }
    return bin;
}
 
}

