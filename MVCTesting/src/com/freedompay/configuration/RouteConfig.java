package com.freedompay.configuration;
import com.freedompay.controllers.*;
import com.freedompay.models.*;
import com.freedompay.views.*;
import com.freedompay.services.IRouteListener;
import com.freedompay.services.IRouteService;
import java.util.ArrayList;
import java.util.List;

public class RouteConfig implements IRouteListener, IRouteService{
	
	private Controller controller = null;
	private View view = null;
	private Model model = null;
	private List<IRouteListener> observers = new ArrayList<IRouteListener>();
	
	public void addObserver(IRouteListener o) {
		this.observers.add(o);
	}
	
	public void removeObserver(IRouteListener o) {
		this.observers.remove(o);
	}
	
	@Override
	public void notifyObservers(Object obj) {
		for(IRouteListener rs : observers) {
			rs.update(obj);
		}
	}
	
	@Override
	public void update(Object obj) {
		String txt = (String) obj;
		if(txt.equalsIgnoreCase("Home") || this.controller == null) {
			this.controller = new HomeController();
			this.view = new HomeView();
			this.model = null;
		}
		if(txt.equalsIgnoreCase("Files")) {
			this.controller = new FileController();
			this.view = new FileView();
			this.model = new FileModel();
		}
		this.view.addController(this.controller);
		this.controller.addView(this.view);
		this.controller.addModel(model);
		this.notifyObservers(this.view);
	}
}
