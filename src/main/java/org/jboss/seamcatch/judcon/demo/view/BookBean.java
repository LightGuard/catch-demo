package org.jboss.seamcatch.judcon.demo.view;

import org.jboss.forge.persistence.PersistenceUtil;
import org.jboss.seam.faces.context.conversation.End;
import org.jboss.seam.transaction.Transactional;
import org.jboss.seamcatch.judcon.demo.domain.Author;
import org.jboss.seamcatch.judcon.demo.domain.Book;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Transactional
@Named
@ConversationScoped
public class BookBean extends PersistenceUtil {
    private List<Book> list = null;
    private Book book = new Book();
    private long id = 0;

    @Inject
    private AuthorBean authorBean;

    @Inject
    private Conversation conv;

    @Inject
    public void init() {
        if (conv.isTransient())
            conv.begin();
    }

    public void load() {
        book = findById(Book.class, id);
    }

    public String create() {
        // FIXME
        // Have to hack this for now
        // I REALLY shouldn't have to do this, is Seam Persistence really that broken???
        // I should be inside of a conversation with the same entity manger, but I'm not,
        // it's a new conversation each time I do anything on the page!!!
        // Is this maybe because of metawidget??
        for (Author a : book.getAuthors()) {
            if (!entityManager.contains(a)) {
                book.getAuthors().remove(a);
                authorBean.setId(a.getId());
                authorBean.load();
                book.getAuthors().add(authorBean.getAuthor());
            }
        }

        create(book);
        return "view?faces-redirect=true&id=" + book.getId();
    }

    public String delete() {
        delete(book);
        return "list?faces-redirect=true";
    }

    @End
    public String save() {
        save(book);
        return "view?faces-redirect=true&id=" + book.getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        load();
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Book> getList() {
        if (list == null) {
            list = findAll(Book.class);
        }
        return list;
    }

    public void addAuthor(final Author author) {
        book.getAuthors().add(author);

        author.getBooks().add(book);

        authorBean.getList().remove(author);
    }

    public void setList(List<Book> list) {
        this.list = list;
    }
}
