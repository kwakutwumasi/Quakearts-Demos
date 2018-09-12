package com.quakearts.webtools.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.quakearts.webapp.facelets.base.BaseBean;
import com.quakearts.webapp.facelets.bootstrap.common.BootHeaderRenderer;
import com.quakearts.webapp.facelets.bootstrap.common.BootHeaderRenderer.Theme;

@Named("greeting")
@ConversationScoped
public class GreetingBean extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2600740898655954929L;

	@Inject
	private Conversation conversation;
	
	private String value, name,//BootFormGroup.class BootInputText.class
		language="en-US", greetingType="standard", selectedLanguageTag; 
	private Theme theme;
	private int numberOfPresents; 
	private String[][] languages= {{"English","en-US"},{"Akan","ak"}};//BootSelectInputGroup.class
	private String[] greetingTypes = {"standard","formal","colloqial","pidgin"};//BootSelectOneInput.class
	private Theme[] availableThemes = Theme.values();//BootSelectOneListbox.class
	private String[][] availableLanguageTags = {{"French","fr_FR"},{"Yoruba","yo_NG"},{"Spanish","es"}};//BootSelectOneMenu.class
	private String[] actions= {"addGreetingBundle","changeTheme",
			"decrement","increment","reset",
			"searchActionsWithFilter","showAllActions",
			"updateGreeting","updateLanguage"};
	private String[] filterSelectedActions;//BootSelectManyMenu.class
	private String[] images = {"close-squirrel-1381764.jpg","daisy-s-1375598.jpg",
			"hinds-1638023.jpg","on-the-road-6-1384796.jpg","small-alpine-village-1639266.jpg",
			"small-alpine-village-italian-dolomites-1639269.jpg"};
	private String[] filterImages = images; //BootSelectManyListbox.class
	private String state; //BootBreadCrumb.class
	private boolean inAdvancedMode;//BootCheckbox.class
	private Date birthDate; //BootDateButton.class
	private byte[] messageBundleBytes;//BootFileInput.class
	private String messageBundleFileName;//BootFileInput.class
	private List<ActionLog> actionLogs;//BootPagination.class BootTable.class
	
	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getGreetingType() {
		return greetingType;
	}

	public void setGreetingType(String greetingType) {
		this.greetingType = greetingType;
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme Theme) {
		this.theme = Theme;
	}

	public String getSelectedLanguageTag() {
		return selectedLanguageTag;
	}

	public void setSelectedLanguageTag(String availableCulture) {
		this.selectedLanguageTag = availableCulture;
	}

	public int getNumberOfPresents() {
		return numberOfPresents;
	}

	public void setNumberOfPresents(int waitInterval) {
		this.numberOfPresents = waitInterval;
	}

	public String[] getFilterSelectedActions() {
		return filterSelectedActions;
	}

	public void setFilterSelectedActions(String[] filterSelectedActions) {
		this.filterSelectedActions = filterSelectedActions;
	}
	
	public String[] getFilterImages() {
		return filterImages;
	}

	public void setFilterImages(String[] filterImages) {
		this.filterImages = filterImages;
	}

	public String[] getImages() {
		return images;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isInAdvancedMode() {
		return inAdvancedMode;
	}

	public void setInAdvancedMode(boolean inAdvancedMode) {
		this.inAdvancedMode = inAdvancedMode;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public byte[] getMessageBundleBytes() {
		return messageBundleBytes;
	}

	public void setMessageBundleBytes(byte[] messageBundleBytes) {
		this.messageBundleBytes = messageBundleBytes;
	}

	public String getMessageBundleFileName() {
		return messageBundleFileName;
	}

	public void setMessageBundleFileName(String messageBundleFileName) {
		this.messageBundleFileName = messageBundleFileName;
	}

	public String[][] getLanguages() {
		return languages;
	}

	public String[] getGreetingTypes() {
		return greetingTypes;
	}

	public Theme[] getAvailableThemes() {
		return availableThemes;
	}

	public String[][] getAvailableLanguageTags() {
		return availableLanguageTags;
	}

	public String[] getActions() {
		return actions;
	}

	public List<ActionLog> getActionLogs() {
		return actionLogs;
	}

	@PostConstruct
	public void init() {
		if(conversation.isTransient()) {
			conversation.begin();
		}
	}
		
	MessageFormat messageFormat = new MessageFormat(ResourceBundle.getBundle("greetings").getString(greetingType));
	
	@Logged
	public void changeTheme(ActionEvent event) {
		BootHeaderRenderer.setTheme(FacesContext.getCurrentInstance(), theme);
	}
	
	//BootButtonGroup.class
	@Logged
	public void increment(AjaxBehaviorEvent event) {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
		}
		++numberOfPresents;
	}

	//BootButtonGroup.class
	@Logged
	public void decrement(AjaxBehaviorEvent event) {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
		}
		--numberOfPresents;
	}
	
	//BootAjaxLoaderComponent.class BootCommandButton.class BootGlyph.class BootForm.class
	@Logged
	public void updateGreeting(AjaxBehaviorEvent event) {
		value = messageFormat.format(new Object[] {name, numberOfPresents, numberOfPresents==1?"present":"presents", birthDate});
	}
	
	//BootAjaxLoaderComponent.class BootCommandButton.class BootFontawesome.class BootForm.class
	@Logged
	public void updateLanguage(AjaxBehaviorEvent event) {
		messageFormat = new MessageFormat(
				ResourceBundle.getBundle("greetings", Locale.forLanguageTag(language)).getString(greetingType));
	}
	
	//BootAjaxLoaderComponent.class BootCommandButton.class BootFlaticon.class BootForm.class BootInputText.class
	@Logged
	public void addGreetingBundle(ActionEvent event) {
		if(messageBundleBytes!=null) {
			File file = new File("etc", "greeting_"+selectedLanguageTag+".properties");
			try(FileOutputStream fos = new FileOutputStream(file)){
				fos.write(messageBundleBytes);
				fos.flush();
			} catch (IOException e) {
				addError("Unable to save", "There was an error saving the new bundle", FacesContext.getCurrentInstance());
				return;
			}
			addMessage("Uploaded", "The bundle has been updated", FacesContext.getCurrentInstance());
		} else {
			addWarning("Nothing worked", "The file was not uploaded", FacesContext.getCurrentInstance());
		}
	}
	
	//BootButtonToolbar.class
	@Logged
	public void searchActionsWithFilter(AjaxBehaviorEvent event) {
		if(filterSelectedActions == null) {
			addError("No Filter Selected", "Select actions to filter out", FacesContext.getCurrentInstance());
			return;
		}
		
		actionLogs = FacesLoggingInterceptor.ACTION_LOGS.stream().filter((actionLog)->{
			for(String filterAction:filterSelectedActions) {
				if(actionLog.action.equals(filterAction))
					return false;
			}
			return true;
		}).collect(Collectors.toList());
	}
	
	//BootButtonToolbar.class
	@Logged
	public void showAllActions(AjaxBehaviorEvent event) {
		actionLogs = FacesLoggingInterceptor.ACTION_LOGS;
	}
	
	//BootMenuAction
	@Logged
	public void reset(ActionEvent event) {
		conversation.end();
	}
}
