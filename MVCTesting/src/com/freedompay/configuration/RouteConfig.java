package com.freedompay.configuration;
import com.freedompay.controllers.*;
import com.freedompay.data.FileData;
import com.freedompay.models.*;
import com.freedompay.views.*;
import com.freedompay.services.IRouteListener;
import com.freedompay.services.IRouteService;
import com.freedompay.util.Comparison;
import com.freedompay.util.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Routing for the application. The update method is
 * where the routing is controlled. This class is added
 * as a listener to any class that needs to update the 
 * RouteConfig of any routing changes.
 * </p>
 * @author MGries
 *
 */
public class RouteConfig implements IRouteListener, IRouteService{
	
	// Variables to keep track of the current state of the view
	private Controller controller = null;
	private View view = null;
	private Model model = null;
	private String currentRoute = null;
	private List<IRouteListener> observers = new ArrayList<IRouteListener>();
	
	
	// Observer methods - The BaseView observes the RouteConfig
	// for any changes so that it knows when to update the View class
	public void addObserver(IRouteListener o) {
		this.observers.add(o);
	}
	
	public void removeObserver(IRouteListener o) {
		this.observers.remove(o);
	}
	
	/**
	 * <p>
	 * Notify the BaseView when the route is updated so it
	 * knows when to update the view.
	 * </p>
	 */
	@Override
	public void notifyObservers(Object obj) {
		for(IRouteListener rs : observers) {
			rs.update(obj);
		}
	}
	
	/**
	 * <p>
	 * Updates the view when a route link is clicked.
	 * Notifies the main JFrame that a new View is selected
	 * 
	 * 
	 * 			** INSTRUCTIONS FOR ADDING ROUTES***
	 * When adding new Views, add a new section here for the routing.
	 * The MainMenu class has the button links that call the router
	 * to update the view. Buttons can be placed in Controller/Views
	 * to update routes as well. The RouteConfig is automatically added
	 * as a listener to the controller. Just follow the pattern of
	 * passing in the appropriate string to the update method when
	 * notifying the observer. The Home route below checks if this.controller
	 * is null. This is how the application knows to first open at the Home menu.
	 * You can remove the null check and add that to any route to make it
	 * the default route.
	 * </p>
	 */
	@Override
	public void update(Object obj) {
		String txt = (String) obj;
		if(!txt.equalsIgnoreCase(this.currentRoute)) {
			this.currentRoute = txt;
			
			if(txt.equalsIgnoreCase("Home") || this.controller == null) {
				this.controller = new HomeController();
				this.view = new HomeView();
			}
			
			if(txt.equalsIgnoreCase("Files")) {
				this.controller = new FileController();
				this.view = new FileView();
			}
			
			if(txt.equalsIgnoreCase("InvalidRows")) {
				this.controller = new InvalidRowsController();
				this.view = new InvalidRowsView();
				
				List<FileModel> models = FileData.getAllFileModels();
				if(models.size() > 1 && FileData.getIsPOSLoadedFlag()){
					for(FileModel model : models) {
						if(model.getFileContents().getSelectedHeaders().size() >= 3) {
							Validation.runValidation(model);
						}
					}
				}
			}
			
			if(txt.equalsIgnoreCase("MatchedRows")) {
				this.controller = new MatchedRowsController();
				this.view = new MatchedRowsView();
				if(FileData.getAllFileModels().size() > 1 && FileData.getIsPOSLoadedFlag()){
					Comparison.runComparison();
				}
			}
			
			this.view.addController(this.controller);
			this.controller.addView(this.view);
			this.controller.addModel(model);
			this.controller.addObserver(this);
			
			// Notify the BaseView
			this.notifyObservers(this.view);
		}
	}
}
