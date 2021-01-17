package vsst.report

enum class Column(name: String) {
    CreatedAt("created_at"),
    TweetId("tweet_id"),
    Tweet("tweet"),
    Likes("likes"),
    RetweetCount("retweet_count"),
    Source("source"),
    UserId("user_id"),
    UserName("user_name"),
    UserScreenName("user_screen_name"),
    UserDescription("user_description"),
    UserJoinDate("user_join_date"),
    UserFollowersCount("user_followers_count"),
    UserLocation("user_location"),
    Lat("lat"),
    Long("long"),
    City("city"),
    Country("country"),
    Continent("continent"),
    State("state"),
    StateCode("state_code"),
    CollectedAt("collected_at")
}
