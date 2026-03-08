package dellemuse.serverapp.serverdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * 
 * 
 * 
 * 
 * ObjectSource
 * ------------
 * 
 * HumanMadeObject
 *      ArtWorkObject
 *      HistoricObject
 *      
 * NoHumanMadeObject
 * 
 * 
 */
public enum CandidateStatus {
	
	SUBMITTED (0, "submitted"),
	EVALUATION(1, "evaluation"),
	REJECTED(2, "rejected"),
	APPROVED(3, "approved"),
	CANCELLED(4, "cancelled"),
	DRAFT(5, "draft");
	
	

	
	private final String label;
	private final int id;
	
	private static final List<CandidateStatus> values;
	
	static  {
		values = new ArrayList<CandidateStatus>();
		
		values.add(SUBMITTED);
		values.add(EVALUATION);
		values.add(REJECTED);
		values.add(APPROVED);
		values.add(CANCELLED);
		values.add(DRAFT);
	}
	
	public static List<CandidateStatus> getValues() {
		return values;
	}
	
	
	private CandidateStatus(int code, String label) {
		this.label = label;
		this.id = code; 
	}
	
	public int getId() {
		return id;
	}

	public String toString() {
		return getLabel();
	}
	
	public String getLabel() {
		return getLabel(Locale.getDefault());
	}

	public String getLabel(Locale locale) {
		ResourceBundle res = ResourceBundle.getBundle(CandidateStatus.this.getClass().getName(), locale);
		return res.getString(this.label);
	}
	
	 	
}
