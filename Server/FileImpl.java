import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;

public class FileImpl extends UnicastRemoteObject implements FileInterface {

   private HashMap<String, String> userAndPass = new HashMap<>();

   private HashMap<String, Integer> userAndLevel = new HashMap<>();

   private boolean writeY; 

   // private List<String>= new List<String>;

   protected FileImpl() throws RemoteException {
      super();
   
   }
   
//    public String getUser(String username, String password) {
//       // System.out.println(userAndPass.containsValue("a"));
//       // userAndPass.containsValue("a");
//       return userAndPass.get(username).toString();
//   }

   public void addUser(String username, String password) {
      String hashedPassword = hashPassword(password);
      userAndPass.put(username, hashedPassword);
  }
  public void addLevel(String username, int level) {
   userAndLevel.put(username, level);
   }

  // Validate a username and password
  public boolean validateUser(String username, String password) {
      String hashedPassword = hashPassword(password);
      return userAndPass.containsKey(username) && userAndPass.get(username).equals(hashedPassword);
  }

  // Hashing the password (use a proper library in real use)
  private String hashPassword(String password) {
      return Integer.toString(password.hashCode()); // Simplified example
  }

   public byte[] downloadFile(String fileName, String userName, String folderPath){
      int validUserLevel = userAndLevel.get(userName);
      try {
         String clientIP;
         
         try {
            clientIP= RemoteServer.getClientHost();
         } catch (Exception e) {
            clientIP = "Not Found";
         }

         System.out.println("Client " + userName + " requests " + folderPath+fileName);
         
         String path = fileName;
         

         if (validUserLevel == 2 && folderPath.equals("S2")){
            path = "S2/"+fileName;

         }
         else if (validUserLevel != 2 && folderPath.equals("S2")) {
            
            throw new RuntimeException("Invalid Level");
         }
         else if (folderPath.equals("images")) {
                  path = "images/"+fileName;
               } 
         else if (folderPath.equals("text")) {
                  path = "text/"+fileName;
         }


         File file = new File(path);
         System.out.println(file.exists()+path);
         byte buffer[] = new byte[(int)file.length()];
         BufferedInputStream input = new BufferedInputStream(new FileInputStream(path));
         input.read(buffer,0,buffer.length);
         input.close();

         System.out.println("File " + fileName + " has been served to " + userName + " successfully.");
         return(buffer);
         
      } catch(Exception e){
         System.out.println("FileImpl: "+e.getMessage());
         e.printStackTrace();
         return(null);
      }
   }

   public void uploadFile(byte[] content, String fileName, String userName, String folderPath) throws RemoteException {
      int validUserLevel = userAndLevel.get(userName);
      // if (validUserLevel == 2 && folderPath=="S2") {
         try {
            String clientIP;
            writeY = true;
   
            try {
               clientIP= RemoteServer.getClientHost();
            } catch (Exception e) {
               clientIP = "Not Found";
            }
            
   
            System.out.println("Client from IP: " + clientIP + " uploading " + fileName);
            File file = new File(fileName);
            String path = fileName;
            writeY = true;
            
            if (validUserLevel == 2 && folderPath.equals("S2")){
               path = "S2/"+fileName;

            }
            else if (validUserLevel != 2 && folderPath.equals("S2")) {
               writeY = false;
               throw new RuntimeException("Invalid Level");
            }
            else if (folderPath.equals("images")) {
               path = "images/"+fileName;
                  } 
            else if (folderPath.equals("text")) {
               path = "text/"+fileName;
            } else {
               writeY = false;
               throw new RuntimeException("Invalid Level");
            }

            System.out.println(file.exists()+path);

            FileOutputStream fouts = new FileOutputStream(path);
            BufferedOutputStream output = new BufferedOutputStream(fouts);
            output.write(content, 0, content.length);
            output.flush();
            output.close();
            System.out.println("File " + fileName + " has been uploaded from " + userName + " successfully.");
   
            
         } catch(Exception e){
            System.out.println("FileImpl: "+e.getMessage());
            e.printStackTrace();
         }

   }

   public String login(String userName, String password) {
      // for (String name: userAndPass.keySet()) {
      //    String key = name.toString();
      //    String value = userAndPass.get(name).toString();
      //    System.out.println("there is a username and key " + key + " " + value);
      // }
      
      String reponse = "";
      addUser("a", "a");
      addLevel("a", 2);

      System.out.println("Validation: " + validateUser(userName, password));
      boolean validLogin = validateUser(userName, password);
      if (validLogin == false) {
         reponse = "Invalid Username or Password";
      }
      else {
         System.out.println("User " + userName + " has logged in");
         reponse = "Sucessfully logged in and your system level is " + userAndLevel.get(userName);
      }
      return reponse;
   }

   public int signUp(String newUserName, String newPassword) {

      if (userAndPass.containsKey(newUserName)) {
         return 9;
      }
      else {
         int randomInt = (int)(Math.random()*2 + 1);
         addUser(newUserName, newPassword);
         addLevel(newUserName, randomInt);

         if (newUserName.equals("a")) {
            addLevel(newUserName, 2);
         }
         if (newUserName.equals("b")) {
            addLevel(newUserName, 1);
         }
         System.out.println(newUserName + " was assigned level " + userAndLevel.get(newUserName));
      }

   //    for (String name: userAndPass.keySet()) {
   //       String key = name.toString();
   //       String value = userAndPass.get(name).toString();
   //       System.out.println(key + " " + value);
   //   }
      
      return userAndLevel.get(newUserName);

      
   }

   public boolean successfulUpload() {

      return writeY;

   }


   
}