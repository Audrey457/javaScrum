package databasemodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import domainmodel.Tag;
import sometools.SetTranslationTools;

/**
 * The representation of a mysql table, containing the tags
 * @author Audrey Loriette
 *
 */
public class TagsTable extends AbstractTable {
	
	private final Logger logger = Logger.getLogger(TagsTable.class);
	private final String KEY_WORDS_LIST = "keyWordsList";
	private final String TOPIC_ID = "topicId";
	public static final int ADD = 0;
	public static final int REPLACE = 1;

	/**
	 * Constructor
	 * @param forumDataBase, an instance of ForumDataBase
	 * @param tableName an instance of String
	 */
	public TagsTable(ForumDataBase forumDataBase, String tableName) {
		super(forumDataBase, tableName);
	}
	
	
	/**
	 * Calling this method will insert a new line in the tags table, or 
	 * if the table already contains a tag for the topic. 
	 * In that case, the key words list of this tag will be updated, 
	 * depending on your update choice: 
	 * - TagsTable.ADD = add the given key words list to the existing one
	 * - TagsTable.REPLACE = replace the existing key words list by the given one
	 * @param tag an instance of Tag
	 * @param updateChoice see below
	 * @see Tag
	 */
	public void insertTag(Tag tag, int updateChoice){
		Tag existingTag = this.contains(tag.getTopicId());
		if(existingTag == null){
			String insert = "INSERT INTO " + tableName 
				+" (" + this.KEY_WORDS_LIST + ", " + this.TOPIC_ID + ") "
				+ " VALUES (?, ?)";
			PreparedStatement stmt;
			try{
				stmt = forumDataBase.getConnection().prepareStatement(insert);
				stmt.setInt(2, tag.getTopicId());
				stmt.setString(1, SetTranslationTools.fromSetToStringList(tag.getKeyWordsList()));
				stmt.executeUpdate();
				stmt.close();
				logger.info("Tag for topic: " + tag.getTopicId() + " inserted");
			}catch(SQLException e){
				logger.error(e + "\nTag for topic: " + tag.getTopicId() + " not inserted");
			}
		}else{
			updateTag(existingTag, updateChoice, tag.getKeyWordsList());
		}
	}
	
	/**
	 * Update the key word list of the given tag, depending on your update choice: 
	 * - TagsTable.ADD = add the given key words list to the existing one
	 * - TagsTable.REPLACE = replace the existing key words list by the given one
	 * @param tag an instance of Tag
	 * @param updateChoice see below
	 * @param keyWordsList an instance of Set/<String/>
	 */
	public void updateTag(Tag tag, int updateChoice, Set<String> keyWordsList){
		if(updateChoice == TagsTable.ADD){
			updateTagByAdding(tag, keyWordsList);
			
		}else if(updateChoice == TagsTable.REPLACE){
			updateTagByReplacing(tag, keyWordsList);
		}else{
			logger.error("Unavailable update choice when trying to update "
					+ "tag of topic: " + tag.getTopicId());
		}
	}
	
	private void updateTagByAdding(Tag tag, Set<String> keyWordsList){
		PreparedStatement stmt;
		Tag temp = tag;
		String sql = "UPDATE " + this.tableName + 
				" SET " + this.KEY_WORDS_LIST + " = ? " + 
				" WHERE " + this.TOPIC_ID + " = " + 
				tag.getTopicId();
		temp.addAKeyWordList(keyWordsList);
		try{
			stmt = forumDataBase.getConnection().prepareStatement(sql);
			stmt.setString(1, SetTranslationTools.fromSetToStringList(temp.getKeyWordsList()));
			stmt.executeUpdate();
			stmt.close();
			logger.info("Tag for topic: " + tag.getTopicId() + " updated");
		}catch(SQLException e){
			logger.error(e + "\nTag for topic: " + tag.getTopicId() + " not updated");
		}
		
	}
	
	private void updateTagByReplacing(Tag tag, Set<String> keyWordsList){
		PreparedStatement stmt;
		String sql = "UPDATE " + this.tableName + 
				" SET " + this.KEY_WORDS_LIST + " = ? " + 
				" WHERE " + this.TOPIC_ID + " = " + 
				tag.getTopicId();
		try{
			stmt = forumDataBase.getConnection().prepareStatement(sql);
			stmt.setString(1, SetTranslationTools.fromSetToStringList(keyWordsList));
			stmt.executeUpdate();
			stmt.close();
			logger.info("Tag for topic: " + tag.getTopicId() + " updated");
		}catch(SQLException e){
			logger.error(e + "\nTag for topic: " + tag.getTopicId() + " not updated");
		}
		
		
	}
	
	public void insertAll(Set<Tag> tagsList, int updateChoice){
		for(Tag tag: tagsList){
			insertTag(tag, updateChoice);
		}
	}
	
	
	/**
	 * @param topicId an int
	 * @return an instance of Tag if the topic has a Tag, null otherwise
	 */
	public Tag contains(int topicId){
		String sql = "SELECT * FROM " + tableName + 
				" WHERE " + this.TOPIC_ID + " = " + topicId;
		Tag tag = null;
		try{
			Statement stmt = forumDataBase.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				HashSet<String> keyWordsList = (HashSet<String>)
						SetTranslationTools.fromStringListToSet(rs.getString(this.KEY_WORDS_LIST), "[|]");
				tag = new Tag(rs.getInt(this.TOPIC_ID), keyWordsList);
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			logger.error(e);
		}
		return tag;
	}
	
	

}
