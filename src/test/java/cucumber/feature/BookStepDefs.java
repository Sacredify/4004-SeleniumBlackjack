package cucumber.feature;

import cucumber.api.Format;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class Book {
    String title;
    String author;
    Date published;

    public Book(final String title, final String author, final Date published) {
        this.title = title;
        this.author = author;
        this.published = published;
    }

    public Date getPublished() {
        return this.published;
    }
}

class Library {
    private final List<Book> store = new ArrayList<>();

    public void addBook(final Book book) {
        this.store.add(book);
    }

    public List<Book> findBooks(final Date from, final Date to) {
        final Calendar end = Calendar.getInstance();
        end.setTime(to);
        end.roll(Calendar.YEAR, 1);

        return this.store.stream()
                .filter(book -> from.before(book.getPublished()) && end.getTime().after(book.getPublished()))
                .sorted(Comparator.comparing(Book::getPublished).reversed())
                .collect(Collectors.toList());
    }
}

public class BookStepDefs {

    final Library library = new Library();
    List<Book> results = new ArrayList<>();

    @Given(".+book with the title '(.+)', written by '(.+)', published in (.+)")
    public void addNewBook(final String title, final String author, @Format("dd MMMMM yyyy") final Date published) {
        this.library.addBook(new Book(title, author, published));
    }

    @When("^the customer searches for books published between (\\d+) and (\\d+)$")
    public void setSearchParameters(@Format("yyyy") final Date from, @Format("yyyy") final Date to) {
        this.results = this.library.findBooks(from, to);
    }

    @Then("(\\d+) books should have been found$")
    public void verifyAmountOfBooksFound(final int booksFound) {
        assertThat(this.results.size(), is(booksFound));
    }

    @Then("Book (\\d+) should have the title '(.+)'$")
    public void verifyBookAtPosition(final int position, final String title) {
        assertThat(this.results.get(position - 1).title, is(title));
    }
}