package uk.ac.man.cs.eventlite.dao;

import java.util.List;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterService {
 
  public static Twitter getTwitterinstance() {
    
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey("Vu2O4pjLuWmDnLAmSAP75CBZu")
      .setOAuthConsumerSecret("e5gmzIUOBJxrppwjmFWQBCFSNGxyDp2Qg1EJhBBSBiu8SDWSkj")
      .setOAuthAccessToken("1333463636673695744-rO8MZW1fQvI5IyQZNpnEFSxZnbkOFK")
      .setOAuthAccessTokenSecret("PjokojqM6EwCsVyZSVCvghQ98V9FUhpnxyz0ebS69wtks");
    TwitterFactory tf = new TwitterFactory(cb.build());
    Twitter twitter = tf.getInstance();
  
    return twitter;
    
   }


  public static String createTweet(String tweet) throws TwitterException {
    Twitter twitter = getTwitterinstance();
    Status status = twitter.updateStatus(tweet);
           return status.getText();
  }

}