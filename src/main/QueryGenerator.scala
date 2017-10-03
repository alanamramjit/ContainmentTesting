import scala.util.Random
object QueryGenerator{
  case class Query(
    str: String,
    rangeCols: Seq[(String, String, Double)],
    baseTable: String,
    cols: Seq[String]
  )
  case class Table(name: String, cols: Seq[String], rangeCols: Seq[String])

  lazy val tables = get_base_tables

  final val operators = Seq("<", ">", "=", ">=", "<=")

  def generate_query(catalog: Seq[Table], scale_factor: Int): Query = {
      val rand = new Random(System.currentTimeMillis())
      val table = catalog(rand.nextInt(catalog.size))
      val select = Random.shuffle(table.cols).take(Random.nextInt(table.cols.size))
      var filterCols = select.filter(col => table.rangeCols.contains(col))
      var rangeCols:List[(String, String, Double)] = Nil
      var filterString = ""
      if (filterCols.size > 0){
        filterCols =Random.shuffle(filterCols).take(Random.nextInt(filterCols.size))
        var filters:List[String] = Nil
        filterCols.foreach{ col => 
          var op =  Random.shuffle(operators).head
          var limit = (Random.nextDouble() * scale_factor)
          rangeCols = (col, op, limit) :: rangeCols
          filters = col + " " + op + " " + limit :: filters
        }
        filterString = " where " + filters.mkString(" and ")
      }
      var query_string = "select " + select.mkString(", ") + " from " + table.name + filterString
      return Query(query_string, rangeCols, table.name, select)
  }

  def get_base_tables: Seq[Table] = {
    var tables: List[Table] = Nil
    var ws = classOf[WebSales].getDeclaredFields  
    tables = Table("web_sales", ws.map(_.getName), ws.filter(_.getType == classOf[Double])
      .map(_.getName)) :: tables
    var cs = classOf[CatalogSales].getDeclaredFields
    tables = Table("catalog_sales", cs.map(_.getName), cs.filter(_.getType == classOf[Double])
      .map(_.getName)) :: tables
    var dd = classOf[DateDim].getDeclaredFields
    tables = Table("date_dim", dd.map(_.getName), dd.filter(_.getType == classOf[Double])
      .map(_.getName)) :: tables
    var ss = classOf[StoreSales].getDeclaredFields
    tables = Table("store_sales", ss.map(_.getName), ss.filter(_.getType == classOf[Double])
      .map(_.getName)) :: tables
    var sr = classOf[StoreReturns].getDeclaredFields
    tables = Table("store_returns", sr.map(_.getName), sr.filter(_.getType == classOf[Double])
      .map(_.getName)) :: tables
    var s = classOf[Store].getDeclaredFields
    tables = Table("store", s.map(_.getName), s.filter(_.getType == classOf[Double])
      .map(_.getName)) :: tables
    tables
  }
}