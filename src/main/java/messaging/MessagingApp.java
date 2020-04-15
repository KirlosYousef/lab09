package messaging;

public class MessagingApp {

    private EncryptionService encryptionService;

    public MessagingApp(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public String sendMessage(String to, String msg){
        if (encryptionService.auth((to))){
            return encryptionService.encrypt(msg);
        } else {
            throw new IllegalArgumentException(to);
        }
    }

    public String receiveMessage(String from, String msg){
        if (encryptionService.auth(from)){
            return encryptionService.decrypt(msg);
        } else {
            throw new IllegalArgumentException(from);
        }
    }
}
