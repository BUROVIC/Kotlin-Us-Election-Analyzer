package vsst.report

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.DataTypes
import org.apache.spark.sql.types.Metadata
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

class ReportComposer {
    companion object {
        private val schema = StructType(
            arrayOf(
                StructField(Column.CreatedAt.name, DataTypes.TimestampType, true, Metadata.empty()),
                StructField(Column.TweetId.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.Tweet.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.Likes.name, DataTypes.DoubleType, true, Metadata.empty()),
                StructField(Column.RetweetCount.name, DataTypes.DoubleType, true, Metadata.empty()),
                StructField(Column.Source.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.UserId.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.UserName.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.UserScreenName.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.UserDescription.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.UserJoinDate.name, DataTypes.TimestampType, true, Metadata.empty()),
                StructField(Column.UserFollowersCount.name, DataTypes.DoubleType, true, Metadata.empty()),
                StructField(Column.UserLocation.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.Lat.name, DataTypes.DoubleType, true, Metadata.empty()),
                StructField(Column.Long.name, DataTypes.DoubleType, true, Metadata.empty()),
                StructField(Column.City.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.Country.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.Continent.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.State.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.StateCode.name, DataTypes.StringType, true, Metadata.empty()),
                StructField(Column.CollectedAt.name, DataTypes.TimestampType, true, Metadata.empty())
            )
        )
    }

    fun compose(candidateDataCsvFilePath: String): Report {
        val spark = SparkSession.builder().appName("KotlinUsElectionAnalyzer").orCreate
        val tweetsDataset = spark.read()
            .option("header", true)
            .option("multiline", true)
            .option("mode", "DROPMALFORMED")
            .option("timestampFormat", "yyyy-MM-dd HH:mm:ss")
            .schema(schema)
            .csv(candidateDataCsvFilePath)

        val tweetsByContinents = tweetsDataset
            .javaRDD()
            .groupBy { it.getString(Column.Continent.ordinal) }
            .filter { it._1 != null }
            .cache()

        val tweetsCountByContinents = tweetsByContinents.mapValues { tweets ->
            tweets.count()
        }

        val likesCountByContinents = tweetsByContinents.mapValues { tweets ->
            tweets.sumBy { it.getDouble(Column.Likes.ordinal).toInt() }
        }

        val retweetsCountByContinents = tweetsByContinents.mapValues { tweets ->
            tweets.sumBy { it.getDouble(Column.RetweetCount.ordinal).toInt() }
        }

        val followersCountByContinents = tweetsByContinents.mapValues { tweets ->
            tweets
                .distinctBy { it.get(Column.UserId.ordinal) }
                .sumBy { it.getDouble(Column.UserFollowersCount.ordinal).toInt() }
        }

        return Report(
            tweetsCountByContinents.collect(),
            likesCountByContinents.collect(),
            retweetsCountByContinents.collect(),
            followersCountByContinents.collect()
        ).apply { tweetsByContinents.unpersist() }
    }
}
