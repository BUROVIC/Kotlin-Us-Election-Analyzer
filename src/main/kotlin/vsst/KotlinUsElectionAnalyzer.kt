package vsst

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtils
import org.jfree.data.category.DefaultCategoryDataset
import scala.Tuple2
import vsst.report.Report
import vsst.report.ReportComposer
import java.io.File
import kotlin.concurrent.thread
import kotlin.system.exitProcess

object KotlinUsElectionAnalyzer {

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size != 2) {
            System.err.println("Usage: KotlinUsElectionAnalyzer <donald_trump_data> <joe_biden_data>")
            exitProcess(1)
        }

        val reportComposer = ReportComposer()
        lateinit var donaldTrumpReport: Report
        lateinit var joeBidenReport: Report

        listOf(
            thread {
                donaldTrumpReport = reportComposer.compose(args[0])
            },
            thread {
                joeBidenReport = reportComposer.compose(args[1])
            }
        ).forEach { it.join() }

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
