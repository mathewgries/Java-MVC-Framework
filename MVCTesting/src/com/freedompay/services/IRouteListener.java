package com.freedompay.services;

/**
 * <p>
 * Part of the Observer pattern Interfaces.
 * This is assigned to any class that listens for 
 * changes, i.e. the Observers.
 * </p>
 * @author MGries
 *
 */
public interface IRouteListener {
	public void update(Object obj);
}
