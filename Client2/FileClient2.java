import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class FileClient2 {

   public static void main(String[] argv) {
      
      // if (argv.length != 3) {
      //    System.out.println("Usage: java FileClient <fileName> <hostname> <port> ");
      //    System.exit(0);
      // }
      //System.out.println("FileClient: " + " File: " + " from " + argv[1] + " port:" + argv[2]);
      //String operation = argv[3].toLowerCase();

      BufferedOutputStream output = null;
      BufferedInputStream input = null;
      try {

         String registryUrl = "//" + "localhost" + ":" + "2099" + "/FileServer";
         FileInterface fi = (FileInterface) Naming.lookup(registryUrl);

         // new Thread(() -> {
         //    while (true) {
         //        try {
         //          Thread.sleep(100);
         //          String cMsg = "";
         //          if(cMsg != null && !cMsg.trim().isEmpty()) {
         //             //System.out.println(fi.getMsg());
         //             System.out.flush();
                  
         //          }
                    
                    
                    
                    
         //        } catch (InterruptedException ex) {
         //            ex.printStackTrace();
               
         //       } 
         //       // catch (RemoteException e) {
         //       //                   // TODO Auto-generated catch block
         //       //                   e.printStackTrace();
         //       //                }
         //    }
         // }).start();

         try (Scanner scan = new Scanner(System.in)) {
            System.out.println("Login or signup?");
            String operation = scan.nextLine();
            String userName;
            String password;
            
            // if (operation.equals("upload")) {
            //    String fileName = scan.nextLine();
            //    File file = new File(fileName);
            //    byte[] buffer = new byte[(int) file.length()];
            //    input = new BufferedInputStream(new FileInputStream(file));
            //    input.read(buffer, 0, buffer.length);
            //    fi.uploadFile(buffer, fileName);
            //    System.out.println("Successfully uploaded file " + fileName + " to server.");
               

            // }

            if (operation.equals("login")) {
               System.out.println("Username: ");
               userName = scan.nextLine();
               System.out.println("Password: ");
               password = scan.nextLine();
               String reponse = fi.login(userName, password);
               
               
               if(reponse == "Invalid Username or Password") {
                  System.out.println("Invalid Username or Password");
                  scan.close();
                  System.exit(0);
                  
                  
               }
               else {
                  System.out.println(reponse);
                  System.out.println("Upload or download?");
                  String operation2 = scan.nextLine();
                  if (operation2.equals("upload")) {
                     System.out.println("File Name:");
                     String fileName = scan.nextLine();
                     System.out.println("File Directory");
                     String folderPath = scan.nextLine();
                     
                     File file = new File(fileName);
                     byte[] buffer = new byte[(int) file.length()];
                     input = new BufferedInputStream(new FileInputStream(file));
                     input.read(buffer, 0, buffer.length);
                     fi.uploadFile(buffer, fileName, userName, folderPath);
                     if (fi.successfulUpload()==true) {
                        System.out.println("Successfully uploaded file " + fileName + " to server.");
                     }
                     else {
                        System.out.println("Not high enough level or invalid file/directory");
                     }
                  
                  }

                  if (operation2.equals("download")) {
                     System.out.println("File Name:");
                     String fileName = scan.nextLine();
                     System.out.println("File Directory");
                     String folderPath = scan.nextLine();
                     byte[] filedata = fi.downloadFile(fileName,userName,folderPath);
                     if (filedata==null) {
                        System.out.println("Not high enough level or invalid file/directory");
                        scan.close();
                        System.exit(0);

                     } else {
                        System.out.println("Successfully downloaded file " + fileName + " from " );
                        File file = new File(fileName);
                        output = new BufferedOutputStream(new FileOutputStream(file.getName()));
                        output.write(filedata, 0, filedata.length);
                        output.flush();
                        System.out.println("File " + fileName + " has been written successfully");
                     }
                     
                  
                  }

                  
               }
            }

            if (operation.equals("signup")) {
               System.out.println("Username: ");
               String newUserName = scan.nextLine();
               System.out.println("Password: ");
               String newPassword = scan.nextLine();
               int userLevel = fi.signUp(newUserName, newPassword);
               if (userLevel == 9) {
                  System.out.println("Your username has already been taken");
               }
               else {
                  System.out.println("Your system level is " + userLevel);
               }

               scan.close();
               System.exit(0);

            }
         }


         
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (output != null) {
               output.close();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }


   }
}