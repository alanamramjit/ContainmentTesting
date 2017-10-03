import org.apache.spark.sql.SparkSession
import org.apache.log4j.{Logger, Level}
import org.apache.spark.storage.StorageLevel

object ConTest{
	def main(args: Array[String]){
		val spark = SparkSession.builder().getOrCreate()
		Logger.getLogger("org").setLevel(Level.ERROR)
		Logger.getLogger("akka").setLevel(Level.ERROR)
		loadTables("/Users/lramjit/Downloads/1G/", spark)
		println("Running simple tests without caching")
		run_simple_test(spark, false)
		println("Running simple tests with caching")
		run_simple_test(spark)
		spark.sqlContext.clearCache
		println("Running random tests without caching")
		run_random_test(spark, 15, false)
		spark.sqlContext.clearCache
		println("Running random tests with caching")	
		run_random_test(spark, 15, true)
	}

	def loadTables(base: String, spark: SparkSession): Unit = {
		import spark.implicits._
		val cat_path = base + "catalog_sales.dat"
		val web_path = base + "web_sales.dat"
		val date_dim_path = base + "date_dim.dat"
		val store_sales_path = base + "store_sales.dat"
		val store_returns_path = base + "store_returns.dat"
		val store_path = base + "store.dat"

		// Format them into SparkSQL tables
		val catalog_sales = spark.read.textFile(cat_path).map(line => line.split("\\|")).filter(_.size == 34)
		.filter(!_.contains("")).map(x => CatalogSales(x(0).toDouble, x(1).toDouble, x(2).toDouble,
		x(3).toDouble, x(4).toDouble, x(5).toDouble, x(6).toDouble, x(7).toDouble, x(8).toDouble, x(9).toDouble,
		x(10).toDouble, x(11).toDouble, x(12).toDouble, x(13).toDouble, x(14).toDouble, x(15).toDouble, x(16).toDouble,
		x(17).toDouble, x(18).toDouble, x(19).toDouble, x(20).toDouble, x(21).toDouble, x(22).toDouble, x(23).toDouble,
		x(24).toDouble, x(25).toDouble, x(26).toDouble, x(27).toDouble, x(28).toDouble, x(29).toDouble, x(30).toDouble,
		x(31).toDouble, x(32).toDouble, x(33).toDouble)).toDF()

		val web_sales = spark.read.textFile(web_path).map(line => line.split("\\|")).filter(_.size == 34)
		.filter(!_.contains("")).map(x => WebSales(x(0).toDouble, x(1).toDouble, x(2).toDouble, x(3).toDouble,
		x(4).toDouble, x(5).toDouble, x(6).toDouble, x(7).toDouble, x(8).toDouble, x(9).toDouble, x(10).toDouble,
		x(11).toDouble, x(12).toDouble, x(13).toDouble, x(14).toDouble, x(15).toDouble, x(16).toDouble,
		x(17).toDouble, x(18).toDouble, x(19).toDouble, x(20).toDouble, x(21).toDouble, x(22).toDouble,
		x(23).toDouble, x(24).toDouble, x(25).toDouble, x(26).toDouble, x(27).toDouble, x(28).toDouble,
		x(29).toDouble, x(30).toDouble, x(31).toDouble, x(32).toDouble, x(33).toDouble)).toDF()

		val date_dim = spark.read.textFile(date_dim_path).map(line => line.split("\\|")).filter(_.size == 28)
		.map(x => DateDim(x(0).toDouble, x(1), x(2), x(3).toDouble, x(4).toDouble, x(5).toDouble, x(6).toDouble,
		x(7).toDouble, x(8).toDouble, x(9).toDouble, x(10).toDouble, x(11).toDouble, x(12).toDouble,
		x(13).toDouble, x(14), x(15), x(16), x(17), x(18), x(19).toDouble, x(20).toDouble, x(21).toDouble, 
		x(22).toDouble, x(23), x(24), x(25), x(26), x(27))).toDF()

		val store_sales = spark.read.textFile(store_sales_path).map(line => line.split("\\|")).filter(_.size == 23)
		.filter(!_.contains("")).map(x => StoreSales(x(0), x(1), x(2), x(3), x(4), x(5), x(6), x(7), x(8), x(9), x(10),
		x(11).toDouble, x(12).toDouble, x(13).toDouble, x(14).toDouble, x(15).toDouble, x(16).toDouble,
		x(17).toDouble, x(18).toDouble, x(19).toDouble, x(20).toDouble, x(21).toDouble, x(22).toDouble)).toDF()

		val store_returns = spark.read.textFile(store_sales_path).map(line => line.split("\\|")).filter(_.size == 20)
		.filter(!_.contains("")).map(x => StoreReturns(x(0), x(1), x(2), x(3), x(4), x(5), x(6), x(7), x(8), x(9),
		x(10).toDouble, x(11).toDouble, x(12).toDouble, x(13).toDouble, x(14).toDouble, x(15).toDouble,
		x(16).toDouble, x(17).toDouble, x(18).toDouble, x(19).toDouble)).toDF()

		val store = spark.read.textFile(store_path).map(line => line.split("\\|")).filter(_.size == 29)
		.filter(!_.contains("")) .map(x => Store(x(0), x(1), x(2), x(3), x(4), x(5), x(6).toDouble, x(7).toDouble, x(8),
		x(9), x(10).toDouble, x(11), x(12), x(13), x(14).toDouble, x(15), x(16), x(17), x(18), x(19), x(20), x(21),
		x(22), x(23), x(24), x(25), x(26), x(27).toDouble, x(28).toDouble)).toDF()

		catalog_sales.createOrReplaceTempView("catalog_sales")
		web_sales.createOrReplaceTempView("web_sales")
		date_dim.createOrReplaceTempView("date_dim")
		store_sales.createOrReplaceTempView("store_sales")
		store_returns.createOrReplaceTempView("store_returns")
		store.createOrReplaceTempView("store")

	}

	def time[R](block: => R): R = {
	    val t0 = System.nanoTime()
	    val result = block    // call-by-name
	    val t1 = System.nanoTime()
	    println("Elapsed time: " + (t1 - t0)/1e9+ "s")
	    result
	}

	def run_random_test(spark: SparkSession, scale: Int, cache: Boolean): Unit = {
		var catalog = QueryGenerator.tables
		if(cache){
			for (i <- 0 to scale){
				var query = QueryGenerator.generate_query(catalog, scale)
				var view = spark.sql(query.str)
				view.persist(StorageLevel.DISK_ONLY)
				view.count
			}
		}
		time{
			for (i <- 0 to scale){
				var query = QueryGenerator.generate_query(catalog, scale)
				spark.sql(query.str).count
			}
		}
	}

	def run_simple_test(spark: SparkSession, withCaching: Boolean = true): Unit = {
		import spark.implicits._
		// Q1.1
		var q1_1 = spark.sql("""select cs_item_sk, cs_list_price, cs_net_paid, cs_sales_price from catalog_sales where cs_net_paid > 65.50 
		and cs_sales_price > 62""")
		// Q1.2
		var q1_2 = spark.sql("""select cs_item_sk, cs_list_price, cs_net_paid, cs_sales_price from catalog_sales where cs_net_paid > 85.50 and cs_sales_price > 85""")
		var q1_2_cache = q1_1.filter($"cs_sales_price" > 85 && $"cs_net_paid" > 85.50)
		// Q1.3
		var q1_3 = spark.sql("""select cs_list_price, cs_net_paid, cs_sales_price from catalog_sales where cs_net_paid > 120.50 and cs_sales_price > 120""")
		var q1_3_cache = q1_1.filter($"cs_sales_price" > 120 && $"cs_net_paid" > 120.50)
		// Q2.1
		var q2_1 = spark.sql("""select ws_net_paid, ws_ext_discount_amt, ws_net_profit, ws_quantity from web_sales where
			ws_net_paid > 25 and ws_quantity > 1""")
		// Q2.2
		var q2_2 = spark.sql("""select  ws_net_paid, ws_ext_discount_amt, ws_net_profit, ws_quantity from web_sales where ws_net_paid > 55 and ws_quantity > 3""")
		var q2_2_cache = q2_1.filter($"ws_net_paid" > 55 && $"ws_quantity" > 3)
		// Q2.3
		var q2_3 = spark.sql("""select ws_net_profit, ws_net_paid from web_sales where
			ws_net_paid > 75 and ws_quantity > 3""")
		var q2_4 = spark.sql("""select ws_net_profit, ws_net_paid from web_sales where
			ws_net_paid > 175 and ws_quantity > 3""")
		var q2_3_cache = q2_1.filter($"ws_net_paid" > 75 && $"ws_quantity" > 3)
		// Q3.1
		var q3_1 = spark.sql("""select d_date, d_weekend, d_quarter_name , d_same_day_lq, d_month_seq from date_dim where
		 	d_same_day_lq > 2414939 and d_month_seq > 0""")
		// Q3.2
		var q3_2 = spark.sql("""select d_date, d_weekend, d_quarter_name from date_dim where
			d_same_day_lq < 2414920 and d_month_seq > 2""")
		// Q3.3
		var q3_3 = spark.sql("""select d_date, d_weekend, d_quarter_name from date_dim where
			d_same_day_lq = 2414939 and d_month_seq > 2""")

		if (withCaching) {
			q1_1.cache
			q2_1.cache
			q3_1.cache
		}
		print("Q1.1 ")
		time{q1_1.count}
		print("Q2.1 ")
		time{q2_1.count}
		print("Q3.1 ")
		time{q3_1.count}
		print("Q1.2 ")
		time{q1_2.count}
		print("Q2.2 ")
		time{q2_2.count}
		print("Q3.2 ")
		time{q3_2.count}
		print("Q1.3 ")
		time{q1_3.count}
		print("Q2.3 ")
		time{q2_3.count}
		print("Q3.3 ")
		time{q3_3.count}
	}
}