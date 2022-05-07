class PrivateChat {
    private User user;
    private String message;

    public PrivateChat(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public void sendMessage() {
        System.out.println(message);
    }
}
