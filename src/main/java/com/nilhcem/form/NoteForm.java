package com.nilhcem.form;

import com.nilhcem.model.Note;

/**
 * Spring MVC SignUp form object model.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class NoteForm {
	private Note note;
	private Long categoryId;
	private String editDueDate; //no = no due date, yes = due date
	private String dueDate; //"MM/dd/yyyy" representation, to simplify date conversions
	private String dueDateStr; //string representation of the due date, to simplify user experience

	public Note getNote() {
		return this.note;
	}
	public void setNote(Note note) {
		this.note = note;
	}

	public Long getCategoryId() {
		return this.categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getEditDueDate() {
		return this.editDueDate;
	}
	public void setEditDueDate(String editDueDate) {
		this.editDueDate = editDueDate;
	}

	public String getDueDate() {
		return this.dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getDueDateStr() {
		return this.dueDateStr;
	}
	public void setDueDateStr(String dueDateStr) {
		this.dueDateStr = dueDateStr;
	}
}
