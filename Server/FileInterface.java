import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileInterface extends Remote {
   public byte[] downloadFile(String fileName, String userName, String folderPath) throws RemoteException;
   public void uploadFile(byte[] content, String fileName, String userName, String folderPath) throws RemoteException;
   public String login(String username, String password) throws RemoteException;
   public int signUp(String username, String password) throws RemoteException;
   public boolean successfulUpload() throws RemoteException;

}