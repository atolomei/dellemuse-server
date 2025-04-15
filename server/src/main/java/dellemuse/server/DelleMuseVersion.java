package dellemuse.server;

public class DelleMuseVersion {

	public static final String VERSION = "0.1 beta";
	
	private static String[] brand_char = null;
	
	public static String[] getAppCharacterName() {
    
		if (brand_char != null)
            return brand_char;
		
        brand_char = new String[9];
        brand_char[0] = app[0] + "";
        brand_char[1] = app[1] + "";
        brand_char[2] = app[2] + "";
        brand_char[3] = app[3] + ""; 
        brand_char[4] = app[4] + "";
        brand_char[5] = app[5];
        brand_char[6] = "";
        brand_char[7] = "version: " + VERSION;
        brand_char[8] = "";
        return brand_char;
    }
    
    static final String app[] = {
            
	" ______    ____  __     "  +   "__      " + " ____  " + " ", 
	"|  __  \\  |  __| | |    " +   "| |     " + "|  __| " + " ",
	"| |  | |  | |    | |    "  +   "| |     " + "| |_   " + " ",
	"| |  | |  |  _|  | |    "  +   "| |     " + "|  _|  " + " ",
	"| |__| |  | |__  | |___ "  +   "| |___  " + "| |__  " + " ",
	"|______/  |____| |_____|"  +   "|_____| " + "|____| " + " " 
    
	
    };


    
}
