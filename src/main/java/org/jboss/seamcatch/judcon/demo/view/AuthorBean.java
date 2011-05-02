package org.jboss.seamcatch.judcon.demo.view;

import org.jboss.forge.persistence.PersistenceUtil;
import org.jboss.seam.transaction.Transactional;
import org.jboss.seamcatch.judcon.demo.domain.Author;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import java.util.List;

@Transactional
@Named
@ConversationScoped
public class AuthorBean extends PersistenceUtil {
    private List<Author> list = null;
    private Author author = new Author();
    private long id = 0;

    public void load() {
        author = findById(Author.class, id);
    }

    public String create() {
        create(author);
        return "view?faces-redirect=true&id=" + author.getId();
    }

    public String delete() {
        delete(author);
        return "list?faces-redirect=true";
    }

    public String save() {
        save(author);
        return "view?faces-redirect=true&id=" + author.getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        load();
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Author> getList() {
        if (list == null) {
            list = findAll(Author.class);
        }
        return list;
    }

    public void setList(List<Author> list) {
        this.list = list;
    }
}
