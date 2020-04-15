package messaging;

public interface EncryptionService {
    public boolean auth(String username);

    public String encrypt(String msg);

    public String decrypt(String msg);
}
