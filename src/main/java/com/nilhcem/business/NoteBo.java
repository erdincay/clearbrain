package com.nilhcem.business;

import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nilhcem.core.hibernate.TransactionalReadOnly;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.dao.CategoryDao;
import com.nilhcem.dao.NoteDao;
import com.nilhcem.model.Note;
import com.nilhcem.model.User;

/**
 * Business class for accessing {@code Note} data.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Service
@TransactionalReadOnly
public class NoteBo {
	@Autowired
	private NoteDao dao;
	@Autowired
	private CategoryDao catDao;
	private final Logger logger = LoggerFactory.getLogger(NoteBo.class);

	/**
	 * Add a {@code Note} in database.
	 *
	 * @param user Owner of the note.
	 * @param noteName The name of the note we wanted to add.
	 * @param catId The category id of the note we wanted to add, 0 if null.
	 * @return The new added note.
	 */
	@TransactionalReadWrite
	public Note addNote(User user, String noteName, Long catId) {
		logger.debug("Add note {} in category {}", noteName, catId);

		Note note = new Note();
		note.setName(noteName);
		note.setCategory((catId.equals(Long.valueOf(0l))) ? null : catDao.getById(user, catId));
		note.setCreationDate(Calendar.getInstance().getTime());
		note.setUser(user);
		dao.save(note);

		return note;
	}

	/**
	 * Assign or remove a category to a note.
	 * @param user Owner of the notes / categories.
	 * @param categoryId The id of the category we want to assign, or 0 if we need to remove the category from the note.
	 * @param noteId The note's id.
	 */
	@TransactionalReadWrite
	public void assignCategoryToNote(User user, Long categoryId, Long noteId) {
		logger.debug("Assign category {} to note {}", categoryId, noteId);

		Note note = dao.getById(user, noteId);
		note.setCategory((categoryId.equals(Long.valueOf(0l))) ? null : catDao.getById(user, categoryId));
		dao.update(note);
	}

	/**
	 * Find all the notes owned by user.
	 *
	 * @param user Owner of the notes we are searching for.
	 * @return List of notes.
	 */
	public List<Note> getNotes(User user) {
		return dao.getNotes(user);
	}

	/**
	 * Delete a Note from its Id. Currently used only by NoteBoTest
	 * @param user Owner of the notes / categories.
	 * @param noteId Id of the note we need to remove.
	 */
	@TransactionalReadWrite
	public void deleteNoteById(User user, Long noteId) {
		Note note = dao.getById(user, noteId);
		dao.delete(note);
	}
}
