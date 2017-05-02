package java_objects;

import java.util.LinkedHashSet;

public class HashSetAuthor extends LinkedHashSet<Author> {

	/**
	 * Generated automatically
	 */
	private static final long serialVersionUID = 4268663569106375394L;
	
	public Author getAuthor(Author author){
		for(Author a : this){
			if(a.getLogin().equals(author.getLogin()) && a.getAuthor_id() == author.getAuthor_id()){
				return a;
			}
		}
		return null;
	}

	

}
