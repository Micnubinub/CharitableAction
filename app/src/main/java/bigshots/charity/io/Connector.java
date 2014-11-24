package bigshots.charity.io;

public class Connector {
    private final CharityManager charityManager;
    private final MessageManager messageManager;
    private final VoteManager voteManager;
    private final UserManager userManager;

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
