package vsst

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtils
import org.jfree.data.category.DefaultCategoryDataset
import scala.Tuple2
import vsst.report.ReportComposer
import java.io.File
import kotlin.system.exitProcess

object KotlinUsElectionAnalyzer {

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size != 2) {
            System.err.println("Usage: KotlinUsElectionAnalyzer <donald_trump_data> <joe_biden_data>")
            exitProcess(1)
        }

        val reportComposer = ReportComposer()
        val donaldTrumpReport = reportComposer.compose(args[0])
        val joeBidenReport = reportComposer.compose(args[1])

        fun saveCategoryChartAsPng(
            title: String,
            valueAxisLabel: String,
            donaldTrumpCategoryData: List<Tuple2<String, Int>>,
            joeBidenCategoryData: List<Tuple2<String, Int>>,
        ) {
            ChartUtils.saveChartAsPNG(
                File("report/$title.png"),
                ChartFactory.createBarChart(
                    title,
                    "Continent",
                    valueAxisLabel,
                    DefaultCategoryDataset().apply {
                        mapOf(
                            "Donald Trump" to donaldTrumpCategoryData,
                            "Joe Biden Data" to joeBidenCategoryData
                        ).forEach { (candidateName, categoryData) ->
                            categoryData.forEach {
                                addValue(it._2.toDouble(), candidateName, it._1)
                            }
                        }
                    }
                ),
                1280,
                720
            )
        }

        saveCategoryChartAsPng(
            "Tweets Count By Continent",
            "Tweets",
            donaldTrumpReport.tweetsCountByContinents,
            joeBidenReport.tweetsCountByContinents
        )

        saveCategoryChartAsPng(
            "Likes Count By Continent",
            "Likes",
            donaldTrumpReport.likesCountByContinents,
            joeBidenReport.likesCountByContinents
        )

        saveCategoryChartAsPng(
            "Retweets Count By Continent",
            "Retweets",
            donaldTrumpReport.retweetsCountByContinents,
            joeBidenReport.retweetsCountByContinents
        )

        saveCategoryChartAsPng(
            "Followers Count By Continent",
            "Followers",
            donaldTrumpReport.followersCountByContinents,
            joeBidenReport.followersCountByContinents
        )
    }
}
