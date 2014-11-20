package bigshots.charity.io;

public class Connector {
    private CharityManager charityManager;
    private MessageManager messageManager;
    private VoteManager voteManager;
    private UserManager userManager;

    public Connector() {
        charityManager = new CharityManager();
        messageManager = new MessageManager();
        voteManager = new VoteManager();
        userManager = new UserManager();
    }

    public CharityManager getCharityManager() {
        return charityManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public VoteManager getVoteManager() {
        return voteManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
