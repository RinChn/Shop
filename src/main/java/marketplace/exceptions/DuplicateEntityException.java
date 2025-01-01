package marketplace.exceptions;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(Integer articleEntityOriginal) {
        super("Such an entity already exists with the article " + articleEntityOriginal);
    }
}
