package dellemuse.serverapp.page.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.IModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.audiostudio.AudioStudioParentObject;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Identifiable;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionItemRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
import dellemuse.serverapp.serverdb.model.record.ArtWorkRecord;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import dellemuse.serverapp.serverdb.model.record.PersonRecord;
import dellemuse.serverapp.serverdb.model.record.SiteRecord;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;

public class DBModelPanel<T> extends ObjectModelPanel<T> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(DBModelPanel.class.getName());

	public DBModelPanel(String id, IModel<T> model) {
		super(id, model);
	}

	protected <S extends Identifiable> List<IModel<S>> iFilter(List<IModel<S>> initialList, String filter) {
		List<IModel<S>> list = new ArrayList<IModel<S>>();
		final String str = filter.trim().toLowerCase();
		initialList.forEach(s -> {
			if (s.getObject() instanceof MultiLanguageObject) {
				if (getObjectTitle((MultiLanguageObject) s.getObject()).getObject().toLowerCase().contains(str))
					list.add(s);
			} else if (s.getObject().getName().toLowerCase().contains(str)) {
				list.add(s);
			}
		});
		return list;
	}

	/** Save --------------------------------------------------------- */

	public void save(ArtExhibition ex) {
		getArtExhibitionDBService().save(ex);
	}

	public void save(ArtExhibitionGuide ex) {
		getArtExhibitionGuideDBService().save(ex);
	}

	public void save(GuideContentRecord ex) {
		getGuideContentRecordDBService().save(ex);
	}

	public void save(ArtExhibitionGuideRecord ex) {
		getArtExhibitionGuideRecordDBService().save(ex);
	}

	public void save(GuideContent ex) {
		getGuideContentDBService().save(ex);
	}

	public void save(User o) {
		getUserDBService().save(o);
	}

	public void save(AudioStudioParentObject po, User user, List<String> msg) {

		if (po instanceof GuideContent) {
			getGuideContentDBService().save((GuideContent) po, user, msg);
		}

		else if (po instanceof ArtExhibitionGuide) {
			getArtExhibitionGuideDBService().save((ArtExhibitionGuide) po, user, msg);
		}

		else if (po instanceof TranslationRecord) {

			if (po instanceof GuideContentRecord)
				getGuideContentRecordDBService().save((GuideContentRecord) po, user, msg);

			else if (po instanceof ArtExhibitionGuideRecord)
				getArtExhibitionGuideRecordDBService().save((ArtExhibitionGuideRecord) po, user, msg);

			else if (po instanceof ArtExhibitionItemRecord)
				getArtExhibitionItemRecordDBService().save((ArtExhibitionItemRecord) po, user, msg);

			else if (po instanceof ArtExhibitionRecord)
				getArtExhibitionRecordDBService().save((ArtExhibitionRecord) po, user, msg);

			else if (po instanceof ArtWorkRecord)
				getArtWorkRecordDBService().save((ArtWorkRecord) po, user, msg);

			else if (po instanceof PersonRecord)
				getPersonRecordDBService().save((PersonRecord) po, user, msg);

			else if (po instanceof InstitutionRecord)
				getInstitutionRecordDBService().save((InstitutionRecord) po, user, msg);

			else if (po instanceof SiteRecord)
				getSiteRecordDBService().save((SiteRecord) po, user, msg);

		}
	}

	/** ------------------------------------------------------------ */

	public void removeSection(ArtExhibition ex, ArtExhibitionSection item, User removedBy) {
		getArtExhibitionDBService().removeSection(ex, item, removedBy);
	}

	public void removeItem(ArtExhibition ex, ArtExhibitionItem item, User removedBy) {
		getArtExhibitionDBService().removeItem(ex, item, removedBy);
	}

	public void removeItem(ArtExhibition ex, ArtExhibitionSection item, User removedBy) {
		getArtExhibitionDBService().removeSection(ex, item, removedBy);
	}

	public void removeItem(ArtExhibitionGuide ex, GuideContent item, User removedBy) {
		getArtExhibitionGuideDBService().removeItem(ex, item, removedBy);
	}

	public void addItem(ArtExhibition ex, ArtWork item, User addedBy) {
		getArtExhibitionDBService().addItem(ex, item, addedBy);
	}

	public void addItem(ArtExhibitionGuide g, ArtExhibitionItem item, User addedBy) {
		getGuideContentDBService().create(g, item, addedBy);
	}

	// public LanguageService getLanguageService() {
	// return (LanguageService)
	// ServiceLocator.getInstance().getBean(LanguageService.class);
	// }

	/** Deps --------------------------------------------------------- */

	/**
	 * public Optional<ArtWork> findArtWorkWithDeps(Long id) { ArtWorkDBService
	 * service = (ArtWorkDBService)
	 * ServiceLocator.getInstance().getBean(ArtWorkDBService.class); return
	 * service.findWithDeps(id); }
	 * 
	 * public Optional<ArtExhibition> findArtExhibitionWithDeps(Long id) {
	 * ArtExhibitionDBService service = (ArtExhibitionDBService)
	 * ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class); return
	 * service.findWithDeps(id); }
	 * 
	 * public Optional<ArtExhibitionSection> findArtExhibitionSectionWithDeps(Long
	 * id) { ArtExhibitionSectionDBService service = (ArtExhibitionSectionDBService)
	 * ServiceLocator.getInstance().getBean(ArtExhibitionSectionDBService.class);
	 * return service.findWithDeps(id); }
	 * 
	 * public Optional<ArtExhibitionItem> findArtExhibitionItemWithDeps(Long id) {
	 * ArtExhibitionItemDBService service = (ArtExhibitionItemDBService)
	 * ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class);
	 * return service.findWithDeps(id); }
	 * 
	 * public Optional<ArtExhibitionGuide> findArtExhibitionGuideWithDeps(Long id) {
	 * ArtExhibitionGuideDBService service = (ArtExhibitionGuideDBService)
	 * ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
	 * return service.findWithDeps(id); }
	 * 
	 * public Optional<GuideContent> findGuideContentWithDeps(Long id) {
	 * GuideContentDBService service = (GuideContentDBService)
	 * ServiceLocator.getInstance().getBean(GuideContentDBService.class); return
	 * service.findWithDeps(id); }
	 * 
	 * public Optional<Resource> findResourceWithDeps(Long id) { ResourceDBService
	 * service = (ResourceDBService)
	 * ServiceLocator.getInstance().getBean(ResourceDBService.class); return
	 * service.findWithDeps(id); }
	 */

	/** Deps END */

	/**
	 * public Optional<Person> getPerson(Long id) { PersonDBService service =
	 * (PersonDBService)
	 * ServiceLocator.getInstance().getBean(PersonDBService.class); return
	 * service.findById(id); }
	 */

	/** Iterable */

	/**
	 * protected Iterable<Institution> getInstitutions() { InstitutionDBService s =
	 * (InstitutionDBService)
	 * ServiceLocator.getInstance().getBean(InstitutionDBService.class); return
	 * s.findAllSorted(); }
	 * 
	 * public Iterable<Person> getPersons() { PersonDBService service =
	 * (PersonDBService)
	 * ServiceLocator.getInstance().getBean(PersonDBService.class); return
	 * service.findAllSorted(); }
	 * 
	 * public Iterable<Site> getSites(Institution in) { InstitutionDBService service
	 * = (InstitutionDBService)
	 * ServiceLocator.getInstance().getBean(InstitutionDBService.class); return
	 * service.getSites(in.getId()); }
	 * 
	 * public Iterable<GuideContent> getGuideContents(ArtExhibitionGuide o) { return
	 * getArtExhibitionGuideDBService().getGuideContents(o); }
	 * 
	 * public Iterable<GuideContent> getSiteGuideContents(Site s) { return
	 * getGuideContentDBService().getBySite(s); }
	 * 
	 * public Iterable<ArtWork> getArtWorks(Person person) { return
	 * getSiteDBService().findDistinctArtWorkByPersonId(person.getId()); }
	 * 
	 * public Iterable<ArtWork> getSiteArtWorks(Site site) { return
	 * getSiteDBService().getSiteArtWorks(site, ObjectState.EDITION,
	 * ObjectState.APPROVED); }
	 * 
	 * public Iterable<GuideContent> getGuideContens(ArtExhibitionItem o) { return
	 * getArtExhibitionItemDBService().getGuideContents(o); }
	 */

	public Resource createAndUploadFile(InputStream inputStream, String bucketName, String objectName, String fileName, long size) {

		try (InputStream is = inputStream) {
			getResourceDBService().upload(bucketName, objectName, is, fileName);
			User user = getUserDBService().findRoot();
			Resource resource = getResourceDBService().create(bucketName, objectName, fileName, getResourceDBService().getMimeType(fileName), size, null, user, fileName);
			return resource;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
