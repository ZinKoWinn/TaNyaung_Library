/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zinkowin.tanyaung.utils;

/**
 *
 * @author ZinKoWinn
 */
public  class ApplicationException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;

	public ApplicationException(String message) {
		super(message);
	}

}