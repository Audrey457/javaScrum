package java_objects;

import java.io.Serializable;
import java.util.Objects;

/**
 * An "Author" object, with its login and its id
 * @author Audrey Loriette
 *
 */
public class Author implements Serializable{
	/**
	 * Automatically generated
	 */
	private static final long serialVersionUID = 9191794490858888778L;
	private String login;
	private int author_id;

	public Author(String login) {
		super();
		this.login = login;
		this.author_id = login.hashCode();
	}

	public Author(String login, int author_id) {
		super();
		this.login = login;
		this.author_id = author_id;
	}

	public int getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public String toString() {
		return "Author id : " + this.author_id + "\t Login : " + this.login;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.author_id, this.login);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		Author other = (Author) obj;
		if (author_id != other.author_id){
			return false;
		}
		if (login == null) {
			if (other.login != null){
				return false;
			}
		} else if (!login.equals(other.login)){
			return false;
		}
		return true;
	}
	
	
	
	
	
}
