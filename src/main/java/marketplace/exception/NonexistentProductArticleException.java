package marketplace.exception;

public class NonexistentProductArticleException extends RuntimeException {
    public NonexistentProductArticleException(Integer article) {
        super("The item with article " + article + " does not exist");
    }
}
