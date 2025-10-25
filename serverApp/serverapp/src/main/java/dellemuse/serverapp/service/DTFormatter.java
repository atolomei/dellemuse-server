package dellemuse.serverapp.service;

public enum DTFormatter {
	
	  Full (0),
	  Month_Day_Year_hh_mm_ss_zzz(1),
	  Month_Day_Year_hh_mm(2),
	  Month_Day_Year(3),
	  Month_Day_hr_min(4),
	  Dateformat_short_this_year(5),
	  Hour_of_today_format(6),
	  Hour_of_day_this_week_format(7),
	  Am_pm_format(8),
	  Day_Month_Year_hh_mm_ss_zzz(9),
	  Day_Month_Year_hh_mm_ss(10),
	  Dow_Month_Day_Year_hh_mm(11);
	
	  int code;
	
	  private DTFormatter( int code) {
		  this.code=code;
	  }
	  
	  public int getCode() {
		  return this.code;
	  }
	  
	  
/**
	    public final static int Year_Month_Day = 12;
	    public final static int Dow_Month_Day_year_z = 13;

	    public final static int Hibernate = 14;

	    public final static int Full_GMT = 15;

	    public final static int Dow_Month_Day_Year_hh_mm_z = 16;
	    public final static int Month_Day_Year_gmt = 17;

	    public final static int Dow_Month_Day_year = 18;
**/
	    

}
