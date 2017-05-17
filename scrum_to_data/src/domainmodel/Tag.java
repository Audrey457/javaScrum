package domainmodel;

import java.io.Serializable;
import java.util.Set;

public class Tag implements Serializable{
	private int topicId;
	private Set<String> keyWordsList;
	
	/**
	 * Constructor
	 * @param topicId an int
	 * @param keyWordsList an instance of Set/<String/>
	 */
	public Tag(int topicId, Set<String> keyWordsList) {
		super();
		this.topicId = topicId;
		this.keyWordsList = keyWordsList;
	}
	
	/**
	 * @return the topicId
	 */
	public int getTopicId() {
		return topicId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + topicId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (topicId != other.topicId)
			return false;
		return true;
	}

	/**
	 * @return the keyWordsList
	 */
	public Set<String> getKeyWordsList() {
		return keyWordsList;
	}
	
	/**
	 * @param topicId, the ID of the topic that has one more key word
	 * @param keyWord, an instance of String
	 * @return true if the key word set did not already contain the specified key word
	 */
	public boolean addAKeyWord(String keyWord){
		return this.keyWordsList.add(keyWord);
	}
	
	/**
	 * @param keyWordsList, an instance of Set/<String/>
	 * @return true if the key words list changed as a result of the call
	 */
	public boolean addAKeyWordList(Set<String> keyWordsList){
		return this.keyWordsList.addAll(keyWordsList);
	}
	
	/**
	 * @param keyWord
	 * @return true if the key words list contained the specified key word
	 */
	public boolean removeAKeyWord(String keyWord){
		return this.keyWordsList.remove(keyWord);
	}
	
	/**
	 * @param keyWordsList
	 * @return true if the key words list changed as a result of the call
	 */
	public boolean removeAKeyWordList(Set<String> keyWordsList){
		return this.keyWordsList.removeAll(keyWordsList);
	}
	
	
	

}
