package com.freedompay.services;

/**
 * <p>
 * Part of the observer pattern Interfaces. This is assigned
 * to any class that needs to notify an Observer of any changes
 * that may be made to the assigned class.
 * </p>
 * @author MGries
 *
 */
public interface IRouteService 
{
	public void addObserver(IRouteListener obj);
	public void removeObserver(IRouteListener obj);
	public void notifyObservers(Object obj);
}
