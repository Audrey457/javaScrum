package domainmodel;

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
	private int authorId;

	public Author(String login) {
		super();
		this.login = login;
		this.authorId = login.hashCode();
	}

	public Author(String login, int authorId) {
		super();
		this.login = login;
		this.authorId = authorId;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public String toString() {
		return "Author id : " + this.authorId + "\t Login : " + this.login;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.authorId, this.login);
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
		if (authorId != other.authorId){
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
