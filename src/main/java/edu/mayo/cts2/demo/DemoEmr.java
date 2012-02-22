package edu.mayo.cts2.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

import edu.mayo.cts2.framework.core.client.Cts2RestClient;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDescriptionMsg;
import edu.mayo.cts2.framework.model.entity.EntityDirectory;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;

@SuppressWarnings("serial")
public class DemoEmr extends Application {
	
	private Cts2RestClient client = Cts2RestClient.instance();

    @Override
    public void init() {
        setTheme(Runo.themeName());

        VerticalLayout root = new VerticalLayout();
       
        root.setMargin(true);
        root.setSizeFull();
        Window main = new Window("CTS2 Demo", root);
        setMainWindow(main);

        Layout billingScreen = this.buildBillingScreen();
	   
        root.addComponent( billingScreen );
    }


    private Layout buildBillingScreen() {
        AbsoluteLayout root = new AbsoluteLayout();
        root.setSizeFull();
        root.setCaption("CTS2 Example Electronic Medical Record");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        root.addComponent(content);

        Panel top = new Panel("Treatment History (Click table row for more info)", new VerticalLayout());
        top.setSizeFull();
        top.setHeight("200px");
        top.addStyleName(Runo.PANEL_LIGHT);
        top.getContent().setSizeFull();
        content.addComponent(top);
      
        Table table = new Table();
        
        table.setSizeFull();
        table.setSelectable(true);
        table.setColumnReorderingAllowed(true);
        table.addStyleName(Runo.TABLE_BORDERLESS);
        top.addComponent(table);
        table.addContainerProperty("date", String.class, null);
        table.addContainerProperty("procedure", String.class, null);
        table.addContainerProperty("uri", String.class, null);
        table.addContainerProperty("notes", String.class, null);
       
        Item i = table.addItem(1);
     
        i.getItemProperty("date").setValue("01/12/2009");
        i.getItemProperty("procedure").setValue("Immunization");
        i.getItemProperty("notes").setValue("Tetanus Immunization");
        final EntityDescription immunization = 
        		this.client.getCts2Resource("http://informatics.mayo.edu/cts2/rest/codesystem/NCIt/version/11.06d/entity/Immunization", EntityDescriptionMsg.class).getEntityDescription();
        i.getItemProperty("uri").setValue(immunization.getNamedEntity().getAbout());
        
        i = table.addItem(2);
        i.getItemProperty("date").setValue("03/14/2010");
        i.getItemProperty("procedure").setValue("Vaccination");
        i.getItemProperty("notes").setValue("Annual influenze Vaccination");
        final EntityDescription vaccination = 
        		this.client.getCts2Resource("http://informatics.mayo.edu/cts2/rest/codesystem/NCIt/version/11.06d/entity/Vaccination", EntityDescriptionMsg.class).getEntityDescription();
        i.getItemProperty("uri").setValue(vaccination.getNamedEntity().getAbout());
        
        i = table.addItem(3);
        i.getItemProperty("date").setValue("03/14/2005");
        i.getItemProperty("procedure").setValue("Physical Therapy");
        i.getItemProperty("notes").setValue("Treatment of broken limb");
        final EntityDescription physicalTherapy = 
        		this.client.getCts2Resource("http://informatics.mayo.edu/cts2/rest/codesystem/SDO/version/1.0/entity/physical-therapy", EntityDescriptionMsg.class).getEntityDescription();
        i.getItemProperty("uri").setValue(physicalTherapy.getNamedEntity().getAbout());
        
        i = table.addItem(4);
        i.getItemProperty("date").setValue("03/14/2002");
        i.getItemProperty("procedure").setValue("Echocardiography"); 
        i.getItemProperty("notes").setValue("Stress Echocardiography test");
        final EntityDescription echocardiography = 
        		this.client.getCts2Resource("http://informatics.mayo.edu/cts2/rest/codesystem/CSP/version/2006/entity/1399-3428", EntityDescriptionMsg.class).getEntityDescription();
        i.getItemProperty("uri").setValue(echocardiography.getNamedEntity().getAbout());
        
        table.setColumnHeaders(new String[] { "Date", "Procedure", "Identifier", "Notes"});
        
        final List<EntityDescription> entities = Arrays.asList(immunization, vaccination, physicalTherapy, echocardiography);
        
        table.addListener(new ItemClickListener(){

			public void itemClick(ItemClickEvent event) {
				  Window w = new Window("Semantically Describing Data");
	                w.setPositionX(50);
	                w.setPositionY(70);
	                w.setWidth("500px");
	                w.setResizable(true);
	                
	                event.getComponent().getWindow().addWindow(w);

	                EntityDescription ed = entities.get( ((Integer)event.getItemId()) - 1);

	                VerticalLayout labels = new VerticalLayout();
	                Label preamble = new Label("CTS2 allows data to be semantically linked to standard definitions and meanings. " +
	                		" In this case, a Patient's history is annotated with information below to provide an unambigious description of" +
	                		" treatment and outcomes.", Label.CONTENT_XHTML);
	                labels.addComponent(preamble);
	                Label lineBreak = new Label("<br/>", Label.CONTENT_XHTML);
	                labels.addComponent(lineBreak);
	                Label sematics = new Label("Semantic Description", Label.CONTENT_XHTML);
	                sematics.setStyleName(Runo.LABEL_H2);
	                labels.addComponent(sematics);
	                Label code = new Label("<b>Code:</b> " + ed.getNamedEntity().getEntityID().getName(), Label.CONTENT_XHTML);
	                labels.addComponent(code);
	                Label uri = new Label("<b>URI:</b> " + ed.getNamedEntity().getAbout(), Label.CONTENT_XHTML);
	                labels.addComponent(uri);
	                Label definition = new Label("<b>Definition:</b> " + ed.getNamedEntity().getDefinition()[0].getValue().getContent(), Label.CONTENT_XHTML);
	                labels.addComponent(definition);
	                
	                w.addComponent(labels);
			}
        	
        });

        content.addComponent(new Label("<hr />", Label.CONTENT_XHTML));

        VerticalLayout bottom = new VerticalLayout();
        bottom.setCaption("Add a new Patient Record");
        bottom.setMargin(true);
        bottom.setSpacing(true);
        bottom.addStyleName(Runo.LAYOUT_DARKER);
        content.addComponent(bottom);

        HorizontalLayout line = new HorizontalLayout() {
            @Override
            public void addComponent(Component c) {
                super.addComponent(c);
                setComponentAlignment(c, Alignment.MIDDLE_LEFT);
                c.setSizeUndefined();
            }
        };
        line.setWidth("100%");
        line.setSpacing(true);
        
        
        
        line.addComponent(new Label("First Name:"));
        TextField tf = new TextField();
        tf.setInputPrompt("John");
        line.addComponent(tf);
        
        line.addComponent(new Label("Last Name:"));
        tf = new TextField();
        tf.setInputPrompt("Doe");
        line.addComponent(tf);
        
        line.setExpandRatio(tf, 1);
        bottom.addComponent(line);
        
        
        line = new HorizontalLayout() {
            @Override
            public void addComponent(Component c) {
                super.addComponent(c);
                setComponentAlignment(c, Alignment.MIDDLE_LEFT);
                c.setSizeUndefined();
            }
        };
        line.setWidth("100%");
        line.setSpacing(true);
        
        Label first = new Label("Maritial Status:");
        line.addComponent(first);

        NativeSelect select = new NativeSelect();
        select.addItem("Single");
        select.addItem("Married");
        select.addItem("Divorced");
        select.addItem("Widowed");
        select.select("Single");
        select.setNullSelectionAllowed(false);
        
        Button help = new Button();
        help.setIcon(new ThemeResource("icons/16/help.png"));
        help.setStyleName(Runo.BUTTON_SMALL);
  
        line.addComponent(help);
      
        line.addComponent(select);
        
        help.addListener(new ClickListener(){

			public void buttonClick(ClickEvent event) {
				  Window w = new Window("Maritial Status");
	                w.setPositionX(50);
	                w.setPositionY(70);
	                w.setWidth("500px");
	                w.setResizable(true);
	                
	                event.getComponent().getWindow().addWindow(w);

	 
	                VerticalLayout labels = new VerticalLayout();
	                Label sematics = new Label("Value Sets", Label.CONTENT_XHTML);
	                sematics.setStyleName(Runo.LABEL_H2);
	                labels.addComponent(sematics);
	                
	                Label preamble = new Label("A Value Set is a grouping of semantic content. Many times, content is grouped together" +
	                		" for a specific purpose, for example, to limit the allowable inputs on a form to ones that are acceptable. In this example," +
	                		" consider a Value Set of all possbile Maritial Status types. CTS2 allows the definition and querying of Value Sets, " +
	                		" allowing drop down lists like this one to be semantically meaningful.", Label.CONTENT_XHTML);
	                labels.addComponent(preamble);
	                
	                w.addComponent(labels);
			}
			
        });

        
        line.setExpandRatio(select, 1);
      
        bottom.addComponent(line);

        line = new HorizontalLayout() {
            @Override
            public void addComponent(Component c) {
                super.addComponent(c);
                setComponentAlignment(c, Alignment.MIDDLE_LEFT);
                c.setSizeUndefined();
            }
        };
        line.setWidth("100%");
        line.setSpacing(true);
       
        bottom.addComponent(line);

        line = new HorizontalLayout() {
            @Override
            public void addComponent(Component c) {
                super.addComponent(c);
                setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
            }
        };
        line.setWidth("100%");
        line.setSpacing(true);
        first = new Label("Exam Area:");
        line.addComponent(first);
        first.setWidth("88px");
        
        help = new Button();
        help.setIcon(new ThemeResource("icons/16/help.png"));
        help.setStyleName(Runo.BUTTON_SMALL);
 
        line.addComponent(help);
        
        help.addListener(new ClickListener(){

			public void buttonClick(ClickEvent event) {
				  Window w = new Window("Exam Area");
	                w.setPositionX(50);
	                w.setPositionY(70);
	                w.setWidth("500px");
	                w.setResizable(true);
	                
	                event.getComponent().getWindow().addWindow(w);

	                VerticalLayout labels = new VerticalLayout();
	                Label sematics = new Label("Text bases search", Label.CONTENT_XHTML);
	                sematics.setStyleName(Runo.LABEL_H2);
	                labels.addComponent(sematics);
	                
	                Label preamble = new Label("CTS2 allows text based matching based on semantic labels and designations. Start typing in the field to see matches displayed.", Label.CONTENT_XHTML);
	                labels.addComponent(preamble);
	                
	                w.addComponent(labels);
			}
			
        });
        
        line.addComponent(help);
        
        final ComboBox combo = new ComboBox();
        combo.setInputPrompt("Type here to search...");
        combo.setNewItemsAllowed(true);
        line.addComponent(combo);
        combo.setWidth("100%");
        line.setExpandRatio(combo, 1);
        combo.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);

		EntityDirectory dir = client
				.getCts2Resource(
						"http://informatics.mayo.edu/cts2/rest/codesystem/bodysystem/version/1.0/entities",
						EntityDirectory.class);

		combo.removeAllItems();

		for (EntityDirectoryEntry summary : dir.getEntry()) {
			if (summary.getKnownEntityDescriptionCount() > 0) {
				String designation = summary.getKnownEntityDescription(0)
						.getDesignation();
				String display = designation + " (" + summary.getAbout() + ")";
				combo.addItem(display);

			}
		}

        bottom.addComponent(line);

        line = new HorizontalLayout() {
            @Override
            public void addComponent(Component c) {
                super.addComponent(c);
                setComponentAlignment(c, Alignment.TOP_RIGHT);
            }
        };
        line.setWidth("100%");
        line.setSpacing(true);
        first = new Label("Clinical Notes:");
        line.addComponent(first);
        first.setWidth("87px");
        
        help = new Button();
        help.setIcon(new ThemeResource("icons/16/help.png"));
        help.setStyleName(Runo.BUTTON_SMALL);
 
        line.addComponent(help);
        
        help.addListener(new ClickListener(){

			public void buttonClick(ClickEvent event) {
				  Window w = new Window("Clinical Notes");
	                w.setPositionX(50);
	                w.setPositionY(70);
	                w.setWidth("500px");
	                w.setResizable(true);
	                
	                event.getComponent().getWindow().addWindow(w);

	                VerticalLayout labels = new VerticalLayout();
	                Label sematics = new Label("Annotating Free Text", Label.CONTENT_XHTML);
	                sematics.setStyleName(Runo.LABEL_H2);
	                labels.addComponent(sematics);
	                
	                Label preamble = new Label("Annotating unstructured free text adds semantic meaning to what could otherwise be" +
	                		" ambigious information. In this example, CTS2 will use a 'best match' text based algorithm to find the" +
	                		" most likely semantic meaing. Click the 'Annotate Clinical Notes' button below to semantically annotate" +
	                		" a randomly chosen word from the Clinical Notes. The chosen word will be highlighted.", Label.CONTENT_XHTML);
	                labels.addComponent(preamble);
	                
	                w.addComponent(labels);
			}
			
        });
       
        final String note = "Patient feeling less short of breath, with decreased cough and sputum production. \n" +
        		"Assessment/Plan: \n" +
        		"     Pneumonia - RLL pneumonia. Responding to IV Ceftriaxone with decreasing O2 requirement and fever curve. Also feeling better. No evidence of complications.";
       
        final TextField clinicalNotes = new TextField(); 
        clinicalNotes.setValue(note);
        clinicalNotes.setReadOnly(true);

        line.addComponent(clinicalNotes);
        clinicalNotes.setWidth("100%");
        clinicalNotes.setHeight("4em");
        line.setExpandRatio(clinicalNotes, 1);
        bottom.addComponent(line);
        
        Button search = new Button("Annotate Clinical Notes");
        bottom.addComponent(search);

        search.addListener(new ClickListener(){

        	public void buttonClick(ClickEvent event) {
        		
        		Random rand = new Random();

        		List<String> possibilities = Arrays.asList("Ceftriaxone", "cough", "sputum", "pneumonia", "fever");
        		
        		int pick = rand.nextInt(possibilities.size());
        		
        		String selectedWord = possibilities.get(pick);
        		
        		int start = StringUtils.indexOf(note, selectedWord);
        		int end = StringUtils.indexOf(note, ' ', start);
        		
        		clinicalNotes.setSelectionRange(start, end - start);
 
        			Window w = new Window("Semantically Describing Data");
	                w.setPositionX(50);
	                w.setPositionY(70);
	                w.setWidth("500px");
	                w.setResizable(true);
	                
	                event.getComponent().getWindow().addWindow(w);

	                EntityDirectory dir = client
	        				.getCts2Resource(
	        						"http://informatics.mayo.edu/cts2/rest/entities?maxtoreturn=1&matchvalue=" + selectedWord,
	        						EntityDirectory.class);
	                
	                EntityDirectoryEntry summary = dir.getEntry(0);
	 
	                VerticalLayout labels = new VerticalLayout();
	                Label preamble = new Label("CTS2 allows data to be semantically linked to standard definitions and meanings. " +
	                		" In this case, Clinical Notes may annotated with information below to provide an unambigious description of" +
	                		" treatment and outcomes.", Label.CONTENT_XHTML);
	                labels.addComponent(preamble);
	                Label lineBreak = new Label("<br/>", Label.CONTENT_XHTML);
	                labels.addComponent(lineBreak);
	                Label sematics = new Label("Semantic Description", Label.CONTENT_XHTML);
	                sematics.setStyleName(Runo.LABEL_H2);
	                labels.addComponent(sematics);
	                Label code = new Label("<b>Code:</b> " + summary.getName().getName(), Label.CONTENT_XHTML);
	                labels.addComponent(code);
	                Label uri = new Label("<b>URI:</b> " + summary.getAbout(), Label.CONTENT_XHTML);
	                labels.addComponent(uri);
	                Label definition = new Label("<b>Designation:</b> " + summary.getKnownEntityDescription(0).getDesignation(), Label.CONTENT_XHTML);
	                labels.addComponent(definition);
	                
	                w.addComponent(labels);
	      
			}
        	
        });

        return root;
    }

}
