package dellemuse.serverapp.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
 
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.editor.ObjectRecordEditor;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import wktui.base.INamedTab;
import wktui.base.NamedTab;

/**
 * site 
 * foto 
 * Info - exhibitions
 */

public abstract class MultiLanguageObjectPage<T extends MultiLanguageObject, R extends TranslationRecord> extends ObjectPage<T> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(MultiLanguageObjectPage.class.getName());
		
	
	private Map<String, ObjectRecordEditor<T, R>> recordEditors = new HashMap<String, ObjectRecordEditor<T, R>>();

	
	protected abstract Optional<R> 	loadTranslationRecord(String lang);
	protected abstract R 			createTranslationRecord(String lang);
	
	
	public MultiLanguageObjectPage() {
		super();
	}		
	
	public MultiLanguageObjectPage(PageParameters parameters) {
		 super(parameters);
	}
	
	public MultiLanguageObjectPage(IModel<T> model) {
		super(model);
	}
	
	public MultiLanguageObjectPage(IModel<T> model, List<IModel<T>> list) {
		super(model, list);
	}
	
	
	@Override
	protected List<INamedTab> createInternalPanels() {
		
		List<INamedTab> tabs = super.createInternalPanels();

		
	List<Language> list = getLanguageService().getLanguages();
		
		for ( Language la:list ) {
			
			if (!la.getLanguageCode().equals( getModel().getObject().getMasterLanguage()) ) {
				
				NamedTab tab=new NamedTab(	Model.of(la.getLanguageCode()), 
											ServerAppConstant.object_translation_record_info+"-"+la.getLanguageCode(), 
											la.getLanguageCode()) {
					
					private static final long serialVersionUID = 1L;
					@Override
					public WebMarkupContainer getPanel(String panelId) {
						return getTranslateRecordEditor(panelId, getMoreInfo());
					}
				};
				tabs.add(tab);
			}
		}
		
		
		NamedTab audit=new NamedTab(Model.of("audit"), ServerAppConstant.object_audit) {
			 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getAuditPanel(panelId);
			}
		};
		tabs.add(audit);
		
		
		
		
		
		
		return tabs;
	}
	

	

	protected Map<String, ObjectRecordEditor<T, R>> getRecordEditors() {
		return this.recordEditors;
	}

	protected void onEditRecord(AjaxRequestTarget target, String lang) {
		getRecordEditors().get(lang).edit(target);		
}
	
	
	
	protected Class<?> getTranslationClass() {
		return InstitutionRecord.class;
	}
	
	
	
	protected Panel getTranslateRecordEditor(String id, String lang) {
		
		if (this.recordEditors.containsKey(lang))
			return this.recordEditors.get(lang);
		
		R ar = null;
		
		//Optional<R> a = getRecordDBService().findByInstitution(getModel().getObject(), lang);
		Optional<R> a=loadTranslationRecord(lang);
		
		
		if (a.isEmpty()) {
			ar = createTranslationRecord(lang);
			//ar =  getInstitutionRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
		}
		else
			ar=a.get();
				
		IModel<R> translationRecordModel =new ObjectModel<R>(ar);
		ObjectRecordEditor<T, R> e = new ObjectRecordEditor<T, R>(id, getModel(), translationRecordModel);
		
		e.setIntroVisible(isIntroVisible());
		e.setSpecVisible(isSpecVisible());
		e.setOpensVisible(isOpensVisible());
		e.setAudioVisible(isAudioVisible());
		e.setInfoVisible(isInfoVisible());
		
		this.recordEditors.put(lang, e);
		return e;
	}
	
	
	
	protected boolean isInfoVisible() {
		return true;
	}
	
	protected boolean isOpensVisible() {
		return false;
	}


	protected boolean isAudioVisible() {
		return false;
	}


	protected  boolean isIntroVisible() {
		return false;
	};

	protected  boolean isSpecVisible() {
		return false;
	};




	public void onDetach() {
		super.onDetach();
		 
	}
	

	@Override
	public void onInitialize() {
		super.onInitialize();


		
		
		//
		// object record correspondiente
		//
		// title
		// subtitle
		
		
		
		
	}
	
	
    
}


