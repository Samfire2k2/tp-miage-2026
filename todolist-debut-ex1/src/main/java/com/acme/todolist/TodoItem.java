package com.acme.todolist;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Un item à faire, immuable
 * Entité JPA et DTO pour l'exercice 1
 * Contient la logique métier RG1 (marquage [LATE!])
 */
@Entity
public class TodoItem {
	
	private static final String LATE = "[LATE!]";
	
	@Id 
	private String id;
	
	private Instant time;
	
	private String content;
		

	public String getId() {
		return id;
	}

	public Instant getTime() {
		return time;
	}

	public String getContent() {
		return this.content;
	}
	
	/**
	 * Constructeur vide imposé par JPA, ne pas utiliser
	 */
	protected TodoItem() {	}
	
	public TodoItem(String id, Instant time, String content) {
		super();
		this.id = id;
		this.time = time;
		this.content = content;
	}

	/**
	 * RG 1 : si l'item a plus de 24h, ajouter "[LATE!]" au contenu
	 * 
	 * @return le contenu avec le préfixe [LATE!] si en retard
	 */
	public String finalContent() {
		return isLate() ? LATE + this.content : this.content;
	}

	/**
	 * Vérifie si l'item est en retard (plus de 24h)
	 * 
	 * @return true si l'item a plus de 24h
	 */
	private boolean isLate() {
		return Instant.now().isAfter(this.time.plus(1, ChronoUnit.DAYS));
	}

	@Override
	public String toString() {
		return "TodoItem [id=" + id + ", time=" + time + ", content=" + content + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TodoItem other = (TodoItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
