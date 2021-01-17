package vsst.report

import scala.Tuple2

data class Report(
    val tweetsCountByContinents: List<Tuple2<String, Int>>,
    val likesCountByContinents: List<Tuple2<String, Int>>,
    val retweetsCountByContinents: List<Tuple2<String, Int>>,
    val followersCountByContinents: List<Tuple2<String, Int>>,
)
