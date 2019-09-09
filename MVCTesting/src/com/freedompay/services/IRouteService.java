package com.freedompay.services;

public interface IRouteService 
{
	public void addObserver(IRouteListener obj);
	public void removeObserver(IRouteListener obj);
	public void notifyObservers(Object obj);
}
