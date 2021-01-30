/* 
 * Copyright (C) 2020 - Angelo Di Iorio
 * 
 * Progetto Movida.
 * Corso di Algoritmi e Strutture Dati
 * Laurea in Informatica, UniBO, a.a. 2019/2020
 * 
*/
package movida.commons;

import movida.galavottigorini.MovidaParser.Parser;
/**
 * Classe usata per rappresentare una persona, attore o regista,
 * nell'applicazione Movida.
 * 
 * Una persona � identificata in modo univoco dal nome 
 * case-insensitive, senza spazi iniziali e finali, senza spazi doppi. 
 * 
 * Semplificazione: <code>name</code> � usato per memorizzare il nome completo (nome e cognome)
 * 
 * La classe pu˜ essere modicata o estesa ma deve implementare il metodo getName().
 * 
 */
public class Person {

	private String name;
	
	private String role;
	
	public Person(String name, String role)
	{
		this.name = name;
		this.role = role;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getRole() {
		return this.role;
	}
	
	public String toString() {
		return name + " " + role;
	}
	
	@Override
	public int hashCode() 
	{
		return name.toLowerCase().hashCode();	//To lower case siccome dev'essere case insensitive
	}
	
	@Override
	public boolean equals(Object o) 
	{
		if (o == this) return true;
		if (!(o instanceof Person) || o == null) return false;
		Person toCheck = (Person) o;
		
		return (name.toLowerCase()).equals(toCheck.getName().toLowerCase());	//To lower case siccome dev'essere case insensitive
	}
	
}
