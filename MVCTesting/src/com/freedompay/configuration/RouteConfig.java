package com.freedompay.configuration;
import com.freedompay.application.BaseView;
import com.freedompay.controllers.*;
import com.freedompay.views.*;

public class RouteConfig {
	
	private static BaseView baseView;
	private static Controller controller;
	private static View view;
	
	public static void init(BaseView bv) {
		baseView = bv;
	}
	
	public static void setRoute(Controller c, View v) {
		Controller cc = RouteConfig.currentController();
		View cv = RouteConfig.currentView();
		if(cc != null && cv != null) {
			baseView.getContentPane().remove(cv);
		}
		RouteConfig.setViewAndController(c, v);
		RouteConfig.updateBaseView();
	}
	
	public static void setViewAndController(Controller c, View v) {
		RouteConfig.controller = c;
		RouteConfig.view = v;
		c.addView(v);
		v.addController(c);
		v.build();
	}
	
	public static void updateBaseView() {
		baseView.add(RouteConfig.currentView());
		baseView.getContentPane().invalidate();
		baseView.getContentPane().validate();
	}
	
	public static View currentView() {
		if(RouteConfig.view != null) {
			return RouteConfig.view;
		}else {
			return null;
		}
	}
	
	public static Controller currentController() {
		if(RouteConfig.controller != null) {
			return RouteConfig.controller;
		}else {
			return null;
		}
	}
}
