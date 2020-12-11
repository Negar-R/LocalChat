package localchat;

public class TextMessage implements Message {
    private String message = "";
    
    public TextMessage(String message) {
        this.message = message;
    }
    
    @Override
    public void execute() {
        System.out.println(this.message);
    }
}
