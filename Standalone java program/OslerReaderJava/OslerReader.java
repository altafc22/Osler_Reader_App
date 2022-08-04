import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OslerReader {
    static ArrayList<User> deviceUsers = new ArrayList<User>();
    static ArrayList<User> portalUsers = new ArrayList<User>();
    static ArrayList<User> deviceUserListUpdated = new ArrayList<User>();
    public static void main(String[] args) {
        DeviceUserManager userManager = new DeviceUserManager();
        
        if(!deviceUserListUpdated.isEmpty())
            deviceUsers = deviceUserListUpdated;
        else
            deviceUsers = userManager.readDeviceUserList();

        portalUsers = userManager.readPortalUserList();

        deviceUserListUpdated.clear();
        for(User user : deviceUsers){
            User updatedUser = getUpdatedData(user);
            if(updatedUser!=null)
                deviceUserListUpdated.add(updatedUser);
        }
        userManager.updateUsers(deviceUserListUpdated);

        userManager.printFileData(Constants.DeviceUserListUpdated);
    }

    //get updated status for user by UUID and device id
    private static User getUpdatedData(User user) {
        //find portal users based on UUID
        List<User> usersFromPortal = portalUsers.stream().filter(item -> item.uuid.equals(user.uuid)).collect(Collectors.toList());
        
        User updatedUser = null;

        //check user status is available in portal list or not
        if(!usersFromPortal.isEmpty()){ 
            for(User portalUser : usersFromPortal){
                //verify device id to update correct user status
                if(portalUser.deviceId == user.deviceId){
                    updatedUser = portalUser;
                    break;
                }
            }
        } else { //get current status from device user list
            //User fromDevice = deviceUsers.stream().filter(deviceUser -> user.uuid.equals(user.uuid)).findAny().orElse(null);
            List<User> usersFromDevice = deviceUsers.stream().filter(item -> item.uuid.equals(user.uuid)).collect(Collectors.toList());
            if(usersFromDevice.size()>0){
                updatedUser = usersFromDevice.get(usersFromDevice.size()-1); //getting current status from device list
            }
        }
        return updatedUser;
    }
}
