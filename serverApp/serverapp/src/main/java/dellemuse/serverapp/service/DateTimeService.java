package dellemuse.serverapp.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.BaseService;
 


@Service
public class DateTimeService extends BaseService {
    
	
    static final public DateTimeFormatter full_eng = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z uuuu", Locale.ENGLISH);
    static final public DateTimeFormatter full_spa = DateTimeFormatter.ofPattern("EEE dd MMM HH:mm:ss z uuuu", Locale.forLanguageTag("es"));

    static final int LOCAL_TSTAMP_LENGTH = ("yyyy-MM-dd HH:mm:ss").length();

    static final DateTimeFormatter local_tstamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static final public DateTimeFormatter postgres_df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    static final public DateTimeFormatter hibernate = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.XXX-z", Locale.ENGLISH);
    static final public DateTimeFormatter database_timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    static DateTimeFormatter solr_field = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    static final Locale LOCALE_ES = new Locale("es");

    static final private int DATE_LEN = "yyyy-mm-dd".length();

    static final public DateTimeFormatter legacy_offsetdatedatetime_iso_offset_date_time = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH);
    static final public SimpleDateFormat legacy_date_iso_offset_date_time_x = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX",
            Locale.getDefault());

    static private final SimpleDateFormat legacy_date_colloquial_displayformat_en = new SimpleDateFormat("MMM d yyyy");
    static private final SimpleDateFormat legacy_date_number_displayformat_en = new SimpleDateFormat("MM/dd/yyyy");

    static private final SimpleDateFormat legacy_date_colloquial_displayformat_es = new SimpleDateFormat("d MMM yyyy");
    static private final SimpleDateFormat legacy_date_number_displayformat_es = new SimpleDateFormat("d/MM/yyyy");

    static public int DATE_MONTH_COLLOQUIAL_FORMAT = 100;
    static public int DATE_FORMAT = 200;
    static public int DATE_FORMAT_GMT = 300;

    static long KB = 1024;
    static long MB = 1000 * KB;
    static long GB = 1000 * MB;

    public static final String ONLY_AGO_LABEL = "AGO";
    public static final String COLlOQUIAL_AGO_LABEL = "COLLOQUIAL_AGO";
    public static final String COLlOQUIAL_LABEL = "COLLOQUIAL";
    public static final String MONTH_DAY_YEAR_LABEL = "DATE";
    public static final String MONTH_DAY_YEAR_GMT_LABEL = "DATE_GMT";
    public static final String FULL_LABEL = "FULL";
    public static final String TIMESTAMP_LABEL = "TIMESTAMP";

    public static final int ONLY_AGO = 1;
    public static final int DATE_COLlOQUIAL = 2;
    public static final int DATE_COLlOQUIAL_AGO = 3;

    public final static int Full = 0;
    public final static int Month_Day_Year_hh_mm_ss_zzz = 1;
    public final static int Month_Day_Year_hh_mm = 2;
    public final static int Day_of_Year = 3;

    public final static int Month_Day_hr_min = 4;
    public final static int Dateformat_short_this_year = 5;
    public final static int Hour_of_today_format = 6;
    public final static int Hour_of_day_this_week_format = 7;
    public final static int Am_pm_format = 8;
    public final static int Day_Month_Year_hh_mm_ss_zzz = 9;
    public final static int Day_Month_Year_hh_mm_ss = 10;

    public final static int Dow_Month_Day_Year_hh_mm = 11;

    public final static int Year_Month_Day = 12;
    public final static int Dow_Month_Day_year_z = 13;

    public final static int Hibernate = 14;

    public final static int Full_GMT = 15;

    public final static int Dow_Month_Day_Year_hh_mm_z = 16;
    public final static int Month_Day_Year_gmt = 17;

    public final static int Dow_Month_Day_year = 18;

    
static DateTimeFormatter formatter_eng[] = { 
DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z uuuu", Locale.ENGLISH), // 0 for Time
DateTimeFormatter.ofPattern("MMM d yyyy HH:mm:ss zz", Locale.ENGLISH),
DateTimeFormatter.ofPattern("MMM d yyyy h:mm a", Locale.ENGLISH), // Default agrega am pm aparte.
DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH), // month day year 3
DateTimeFormatter.ofPattern("MMM d, h:mm a", Locale.ENGLISH),
DateTimeFormatter.ofPattern("MMM d, h:mm", Locale.ENGLISH),
DateTimeFormatter.ofPattern("h:mm", Locale.ENGLISH),
DateTimeFormatter.ofPattern("EEEE h:mm a", Locale.ENGLISH),
DateTimeFormatter.ofPattern(" a", Locale.ENGLISH),
DateTimeFormatter.ofPattern("d MMM yyyy HH:mm:ss  zzz", Locale.ENGLISH),
DateTimeFormatter.ofPattern("d MMM yyyy HH:mm:ss ", Locale.ENGLISH), // 10
DateTimeFormatter.ofPattern("EEE MMM d yyyy hh:mm a z", Locale.ENGLISH),
DateTimeFormatter.ofPattern("yyyy MM d HH:mm:ss", Locale.ENGLISH),
DateTimeFormatter.ofPattern("EEE MMM d yyyy z", Locale.ENGLISH),
DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.XXX-z", Locale.ENGLISH),
DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss z", Locale.ENGLISH),
DateTimeFormatter.ofPattern("EEE MMM d yyyy hh:mm a z", Locale.ENGLISH), // 16
DateTimeFormatter.ofPattern("MMM d yyyy x", Locale.ENGLISH), // 17 month day year gmt
DateTimeFormatter.ofPattern("EEE MMM d yyyy", Locale.ENGLISH)

};

static DateTimeFormatter formatter_spa[] = { 
DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z uuuu", LOCALE_ES), // · HH:mm:ss     // z uuuu 0
DateTimeFormatter.ofPattern("d MMM yyyy HH:mm:ss zzz", LOCALE_ES), // 1
DateTimeFormatter.ofPattern("d MMM yyyy h:mm a", LOCALE_ES), //2
DateTimeFormatter.ofPattern("d MMM yyyy", LOCALE_ES),  // 3
DateTimeFormatter.ofPattern("d MMM h:mm a z", LOCALE_ES),  
DateTimeFormatter.ofPattern("d MMM, h:mm a z", LOCALE_ES),
DateTimeFormatter.ofPattern("h:mm a", LOCALE_ES), 
DateTimeFormatter.ofPattern("EEEE h:mm", LOCALE_ES),
DateTimeFormatter.ofPattern(" a", LOCALE_ES), // 8
DateTimeFormatter.ofPattern("d MMM yyyy HH:mm:ss  zzz", LOCALE_ES),
DateTimeFormatter.ofPattern("d MMM yyyy - HH:mm:ss ", LOCALE_ES), // 10 ·
DateTimeFormatter.ofPattern("EEE MMM d yyyy hh:mm a z", LOCALE_ES),
DateTimeFormatter.ofPattern("yyyy MM d HH:mm:ss", LOCALE_ES),
DateTimeFormatter.ofPattern("EEE d MMM yyyy z", LOCALE_ES),
DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.XXX-z", LOCALE_ES),
DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss z", LOCALE_ES),
DateTimeFormatter.ofPattern("EEE d MMM yyyy hh:mm a z", LOCALE_ES),
DateTimeFormatter.ofPattern("d MMM yyyy x", LOCALE_ES), // 17 month day year gmt
DateTimeFormatter.ofPattern("EEE d MMM yyyy", LOCALE_ES) };


    static public DateTimeFormatter getDefaultDateTime_Date_Formatter() {
        if (Locale.getDefault().getLanguage().equals("es"))
            return formatter_spa[Day_of_Year];
        else
            return formatter_eng[Day_of_Year];

    }

    static public DateTimeFormatter getDefaultDateTime_Time_Formatter() {
        if (Locale.getDefault().getLanguage().equals("es"))
            return formatter_spa[Month_Day_Year_hh_mm];
        else
            return formatter_eng[Month_Day_Year_hh_mm];
    }

    

    /**
     * Default Date : January 23, 2018 Default DateTime : January 23, 2018 10:12 am
     */
    static long dateTimeDifference(Temporal d1, Temporal d2, ChronoUnit unit) {
        return unit.between(d1, d2);
    }

    public String getSolrFieldValue(OffsetDateTime date) {
        return solr_field.format(date);
    }
 

    final static private long MINUTOS = 3600000L;
    final static private long HORAS = 86400000 * 1L;
    final static private long DIAS = 86400000 * 90L;
    final static private long SEGUNDOS = 120000L;

    static private final String second_eng = "second";
    static private final String seconds_eng = "seconds";

    static private final String minute_eng = "minute";
    static private final String minutes_eng = "minutes";

    static private final String hour_eng = "hour";
    static private final String hours_eng = "hours";

    static private final String day_eng = "day";
    static private final String days_eng = "days";

    static private final String second_spa = "segundo";
    static private final String seconds_spa = "segundos";

    static private final String minute_spa = "minuto";
    static private final String minutes_spa = "minutos";

    static private final String hour_spa = "hora";
    static private final String hours_spa = "horas";

    static private final String day_spa = "día";
    static private final String days_spa = "días";

    static private final String prefix_eng = "";
    static private final String prefix_spa = "hace ";

    static private final String suffix_eng = " ago";
    static private final String suffix_spa = "";

    static public final int ENG = 0;
    static public final int SPA = 1;

    private class DateFormatterLabels {
    	
    	public String second;
    	public String seconds;
    	public String minute;
    	public String minutes;
    	public String hour;
    	public String hours;
    	public String day;
    	public String days;
    	public String prefix;
    	public String suffix;
    	
    }
    
    private DateFormatterLabels LABELS[] = new DateFormatterLabels[2];
 
    private Map<String, String> ordered_zones = null;
    
    private Map<String, String> map_zones = null;   
    
    @Autowired
    private final UserDBService userDBService;

    public DateTimeService(ServerDBSettings settings, UserDBService userDBService) {
        super(settings);
        this.userDBService=userDBService;
    
        LABELS[ENG] = new DateFormatterLabels();
        LABELS[SPA] = new DateFormatterLabels();

        LABELS[ENG].second = second_eng;
        LABELS[ENG].seconds = seconds_eng;
        LABELS[ENG].minute = minute_eng;
        LABELS[ENG].minutes = minutes_eng;
        LABELS[ENG].hours = hours_eng;
        LABELS[ENG].hour = hour_eng;
        LABELS[ENG].day = day_eng;
        LABELS[ENG].days = days_eng;
        LABELS[ENG].prefix = prefix_eng;
        LABELS[ENG].suffix = suffix_eng;

        LABELS[SPA].second = second_spa;
        LABELS[SPA].seconds = seconds_spa;
        LABELS[SPA].minute = minute_spa;
        LABELS[SPA].minutes = minutes_spa;
        LABELS[SPA].hours = hours_spa;
        LABELS[SPA].hour = hour_spa;
        LABELS[SPA].prefix = prefix_spa;
        LABELS[SPA].day = day_spa;
        LABELS[SPA].days = days_spa;
        LABELS[SPA].suffix = suffix_spa;
    
    }
    
    public String format(OffsetDateTime date) {
        return format(date, null, null, DTFormatter.Month_Day_Year_hh_mm);
    }
    
    public String format(OffsetDateTime date, DTFormatter f) {
        return format(date, null, null, f);
    }
    
    
    /**
     * 
     * @param date
     * @param zid
     * @param locale
     * @param formatter
     * @return
     */
    public String format(OffsetDateTime date, String zoneId, Locale locale, DTFormatter o_formatter) {

        if (date == null)
            return "err";

        ZoneId zone = null;

        if (zoneId == null) {
            zone = getDefaultZoneId();
        } else
            zone = ZoneId.of(zoneId);

        if (locale == null) {
            locale = getDefaultLocale();
        }

        
        int formatter = o_formatter.getCode();
        
        ZonedDateTime zdate = ZonedDateTime.ofInstant(date.toInstant(), zone);
        
        if (locale == Locale.forLanguageTag("es")) {
        	if (formatter < 0 || formatter > (formatter_spa.length - 1))
                return full_spa.format(zdate);

        	   if (formatter == Day_of_Year)
        		    return formatter_spa[formatter].format(zdate);
        	   
        	else if (formatter == Month_Day_Year_hh_mm)
                return formatter_spa[formatter].format(zdate);// + formatter_spa[Am_pm_format].format(date).toLowerCase();

            else if (formatter == Dow_Month_Day_Year_hh_mm)
                return formatter_spa[formatter].format(zdate); // + formatter_spa[Am_pm_format].format(date).toLowerCase();

            else if (formatter == Dow_Month_Day_year_z)
                return formatter_spa[formatter].format(zdate);

            else
                return formatter_spa[formatter].format(zdate);
        }
        else  {
            if (formatter < 0 || formatter > (formatter_eng.length - 1))
                return full_eng.format(zdate);

            if (formatter == Day_of_Year)
    		    return formatter_eng[formatter].format(zdate);
            
            else if (formatter == Month_Day_Year_hh_mm)
                return formatter_eng[formatter].format(zdate);// + formatter_eng[Am_pm_format].format(date).toLowerCase();

            else if (formatter == Dow_Month_Day_Year_hh_mm)
                return formatter_eng[formatter].format(zdate);// + formatter_eng[Am_pm_format].format(date).toLowerCase();

            else if (formatter == Dow_Month_Day_year_z)
                return formatter_eng[formatter].format(zdate);

            else
                return formatter_eng[formatter].format(zdate);
        }
    }
    
    
    public User getRootUser() {
        return getUserDBService().findRoot();
    }

    public User getSessionUser() {
        return getUserDBService().findRoot();
    }

    
    protected UserDBService getUserDBService() {
        return userDBService;
    }


    
    private ZoneId getDefaultZoneId() {
        return getDefaultZoneId(null);
    }

    private ZoneId getDefaultZoneId(String zoneId) {

        if (zoneId != null)
            return ZoneId.of(zoneId);

        ZoneId zid = null;

        //User user = getSessionUser();

        //if (user != null)
        //    zid = user.getZoneId();
        
        if (zid == null)
            zid = ZoneId.systemDefault();
        return zid;
    }

    private Locale getDefaultLocale() {
        Locale locale = null;
        User user = getSessionUser();
        if (user != null)
            locale = user.getLocale();
        if (locale == null)
            locale = Locale.getDefault();
        return locale;
    }

    
    
    public LocalDate parseFlexibleDate(String input, Locale locale) {
        
    	if (locale==Locale.ENGLISH) {
   		
    		String trimmed = input.trim().replaceAll("\\s+", " ").toLowerCase().replace(" of ", " ");
	        
	       for (DateTimeFormatter formatter : FORMATTERS_ENG) {
	            try {
	                return LocalDate.parse(trimmed, formatter);
	            } catch (DateTimeParseException ignored) {
	                // try next format
	            }
	        }
	        throw new IllegalArgumentException("Unrecognized date format: " + input);
	    }
    		
    	
    	if (locale.getLanguage()==Locale.forLanguageTag("es").getLanguage()) {
       		
    		String trimmed = input.trim().replaceAll("\\s+", " ").toLowerCase().replace(" de ", " ");
	        
	       for (DateTimeFormatter formatter : FORMATTERS_SPA) {
	            try {
	                return LocalDate.parse(trimmed, formatter);
	            } catch (DateTimeParseException ignored) {
	                // try next format
	            }
	        }
	        throw new IllegalArgumentException("Unrecognized date format: " + input);
	    }
    	
    	
    		

    	if (locale.getLanguage()==Locale.forLanguageTag("pt-BR").getLanguage()) {
       		
    		String trimmed = input.trim().replaceAll("\\s+", " ").toLowerCase().replace(" de ", " ");
	        
	       for (DateTimeFormatter formatter : FORMATTERS_PT) {
	            try {
	                return LocalDate.parse(trimmed, formatter);
	            } catch (DateTimeParseException ignored) {
	                // try next format
	            }
	        }
	        throw new IllegalArgumentException("Unrecognized date format: " + input);
	    }
    	
    	
    		
    	String trimmed = input.trim().replaceAll("\\s+", " ").toLowerCase().replace(" of ", " ");
	        
	    for (DateTimeFormatter formatter : FORMATTERS_ENG) {
	            try {
	                return LocalDate.parse(trimmed, formatter);
	            } catch (DateTimeParseException ignored) {
	                // try next format
	            }
	        }
	     throw new IllegalArgumentException("Unrecognized date format: " + input);
		}
    	
    
    
    
    private static final List<DateTimeFormatter> FORMATTERS_SPA = List.of(

			 
		 	DateTimeFormatter.ofPattern("d/M/yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d/M/yyyy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d M yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d M yyyy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d-M-yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d/M/yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d/M/yyyy", Locale.forLanguageTag("es")),

		 	DateTimeFormatter.ofPattern("dd/M/yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd/M/yyyy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd M yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd M yyyy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd-M-yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd/M/yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd/M/yyyy", Locale.forLanguageTag("es")),
	        
		 	DateTimeFormatter.ofPattern("d/MM/yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d/MM/yyyy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d MM yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d MM yyyy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d-MM-yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d/MM/yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d/MM/yyyy", Locale.forLanguageTag("es")),

		 	DateTimeFormatter.ofPattern("dd/MM/yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd MM yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd MM yyyy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd-MM-yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd/MM/yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("es")),
	        
	        DateTimeFormatter.ofPattern("d MMM yy",  Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d MMM yyyy",  Locale.forLanguageTag("es")),

	        DateTimeFormatter.ofPattern("d MMMM yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("es")),

	        DateTimeFormatter.ofPattern("MMMM d yy", Locale.forLanguageTag("es")),
	        DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.forLanguageTag("es"))
	    );

    private static final List<DateTimeFormatter> FORMATTERS_ENG = List.of(

    		 
		 	DateTimeFormatter.ofPattern("M/d/yy", Locale.ENGLISH),
	        DateTimeFormatter.ofPattern("M/d/yyyy", Locale.ENGLISH),
	        DateTimeFormatter.ofPattern("M-d-yy", Locale.ENGLISH),
		    DateTimeFormatter.ofPattern("M d yy", Locale.ENGLISH),
	        DateTimeFormatter.ofPattern("M d yyyy", Locale.ENGLISH),
	        
	        DateTimeFormatter.ofPattern("M/d/yyyy"),
	        DateTimeFormatter.ofPattern("dd MMM yy", Locale.ENGLISH),
	        DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH),

	        DateTimeFormatter.ofPattern("d MMM yy", Locale.ENGLISH),
   			DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH),
	        DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH),
	        DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH)
    		
    		
    		
    		
    		);

    
    
    
    
    
    private static final List<DateTimeFormatter> FORMATTERS_PT= List.of(

			 
		 	DateTimeFormatter.ofPattern("d/M/yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d/M/yyyy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d M yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d M yyyy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d-M-yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d/M/yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d/M/yyyy", Locale.forLanguageTag("pt-BR")),
	        

		 	DateTimeFormatter.ofPattern("dd/M/yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd/M/yyyy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd M yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd M yyyy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd-M-yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd/M/yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd/M/yyyy", Locale.forLanguageTag("pt-BR")),
	        

		 	DateTimeFormatter.ofPattern("d/MM/yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d/MM/yyyy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d MM yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d MM yyyy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d-MM-yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d/MM/yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d/MM/yyyy", Locale.forLanguageTag("pt-BR")),

		 	DateTimeFormatter.ofPattern("dd/MM/yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd MM yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd MM yyyy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd-MM-yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd/MM/yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("pt-BR")),
	        
	        DateTimeFormatter.ofPattern("d MMM yy",  Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d MMM yyyy",  Locale.forLanguageTag("pt-BR")),

	        DateTimeFormatter.ofPattern("d MMMM yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("pt-BR")),

	        DateTimeFormatter.ofPattern("MMMM d yy", Locale.forLanguageTag("pt-BR")),
	        DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.forLanguageTag("pt-BR"))
	        
    		);  
    
    
    
    
    

}
