// Define case classes for the base tables
case class CatalogSales(  
  cs_sold_date_sk: Double,
  cs_sold_time_sk: Double,
  cs_ship_date_sk: Double,
  cs_bill_customer_sk: Double,//3
  cs_bill_cdemo_sk: Double,
  cs_bill_hdemo_sk: Double,
  cs_bill_addr_sk: Double,
  cs_ship_customer_sk: Double,
  cs_ship_cdemo_sk: Double,
  cs_ship_hdemo_sk: Double,
  cs_ship_addr_sk: Double,
  cs_call_center_sk: Double,
  cs_catalog_page_sk: Double,
  cs_ship_mode_sk: Double,
  cs_warehouse_sk: Double,
  cs_item_sk: Double,//15
  cs_promo_sk: Double,
  cs_order_number: Double,
  cs_order_sk: Double,
  cs_wholesale_cost: Double,
  cs_list_price: Double,
  cs_sales_price: Double,
  cs_ext_discount_amt: Double,
  cs_ext_sales_price: Double,
  cs_ext_wholesale_cost: Double,
  cs_ext_list_price: Double,
  cs_ext_tax: Double,
  cs_coupon_amt: Double,
  cs_ext_ship_cost: Double,
  cs_net_paid: Double,
  cs_net_paid_inc_tax: Double,
  cs_net_paid_inc_ship: Double,
  cs_net_paid_inc_ship_tax: Double,
  cs_net_profit: Double
)

case class WebSales(
  ws_sold_date_sk: Double,
  ws_sold_time_sk: Double,
  ws_ship_date_sk: Double,
  ws_item_sk: Double,
  ws_bill_customer_sk: Double, //5
  ws_bill_cdemo_sk: Double,
  ws_bill_hdemo_sk: Double,
  ws_bill_addr_sk: Double,
  ws_ship_customer_sk: Double,
  ws_ship_cdemo_sk: Double,
  ws_ship_hdemo_sk: Double,
  ws_ship_addr_sk: Double,
  ws_web_page_sk: Double,
  ws_web_site_sk: Double,
  ws_ship_mode_sk: Double,
  ws_warehouse_sk: Double,
  ws_promo_sk: Double,
  ws_order_number: Double,
  ws_quantity: Double,
  ws_wholesale_cost: Double,
  ws_list_price: Double,
  ws_sales_price: Double,
  ws_ext_discount_amt: Double,
  ws_ext_sales_price: Double,
  ws_ext_wholesale_cost: Double,
  ws_ext_list_price: Double,
  ws_ext_tax: Double,
  ws_coupon_amt: Double,
  ws_ext_ship_cost: Double,
  ws_net_paid: Double,
  ws_net_paid_inc_tax: Double,
  ws_net_paid_inc_ship: Double,
  ws_net_paid_inc_ship_tax: Double,
  ws_net_profit: Double
)

case class DateDim( 
  d_date_sk: Double,// 0
  d_date_id: String,
  d_date: String,
  d_month_seq: Double,
  d_week_seq: Double,
  d_quarter_seq: Double,
  d_year: Double,
  d_dow: Double,
  d_moy: Double,
  d_dom: Double,
  d_qoy: Double,
  d_fy_year: Double,
  d_fy_quarter_seq: Double, //12
  d_fy_week_seq: Double,
  d_day_name: String,
  d_quarter_name: String,
  d_holiday: String,
  d_weekend: String,
  d_following_holiday: String,
  d_first_dom: Double,
  d_last_dom: Double,
  d_same_day_ly: Double, //21
  d_same_day_lq: Double,
  d_current_day: String,
  d_current_week: String,
  d_current_month: String,
  d_current_quarter: String,
  d_current_year: String
)

case class StoreSales(
  ss_sold_date_sk: String,
  ss_sold_time_sk: String,
  ss_item_sk: String, //2
  ss_customer_sk: String,//3
  ss_cdemo_sk: String,
  ss_hdemo_sk: String,
  ss_addr_sk: String,
  ss_store_sk: String,
  ss_promo_sk: String,
  ss_ticket_number: String,//9
  ss_quantity: String,
  ss_wholesale_cost: Double,
  ss_list_price: Double,
  ss_sales_price: Double,
  ss_ext_discount_amt: Double,
  ss_ext_sales_price: Double,
  ss_ext_wholesale_cost: Double,
  ss_ext_list_price: Double,
  ss_ext_tax: Double,
  ss_coupon_amt: Double,
  ss_net_paid: Double,
  ss_net_paid_inc_tax: Double,
  ss_net_profit: Double
)

case class StoreReturns(
sr_returned_date_sk: String,
sr_return_time_sk: String,
sr_item_sk: String, //2
sr_customer_sk: String, //3
sr_cdemo_sk: String,
sr_hdemo_sk: String,
sr_addr_sk: String,
sr_store_sk: String,
sr_reason_sk: String,
sr_ticket_number: String, //9
sr_return_quantity: Double,
sr_return_amt: Double,
sr_return_tax: Double,
sr_return_amt_inc_tax: Double,
sr_fee: Double,
sr_return_ship_cost: Double,
sr_refunded_cash: Double,
sr_reversed_charge: Double,
sr_store_credit: Double,
sr_net_loss: Double
)
case class Store(
  s_store_sk: String, //0
  s_store_id: String,
  s_rec_start_date: String,
  s_rec_end_date: String,
  s_closed_date_sk: String,
  s_store_name: String,//5
  s_number_employees: Double,
  s_floor_space: Double,
  s_hours: String,
  s_manager: String, //9
  s_market_id: Double,
  s_geography_class: String,
  s_market_desc: String,
  s_market_manager: String, //13
  s_division_id: Double,
  s_division_name: String,
  s_company_id: String,
  s_company_name: String,//17
  s_street_number: String,//18
  s_street_name: String,
  s_street_type: String,
  s_suite_number: String,
  s_city: String,
  s_county: String,
  s_state: String, //24
  s_zip: String,//25
  s_country: String, // 26
  s_gmt_offset: Double,
  s_tax_precentage: Double
)