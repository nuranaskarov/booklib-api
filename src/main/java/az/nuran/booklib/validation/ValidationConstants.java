package az.nuran.booklib.validation;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationConstants {
    public static final int BOOK_TITLE_MAX_LENGTH = 255;
    public static final int BOOK_AUTHOR_MAX_LENGTH = 255;
    public static final int BOOK_PUBLICATION_YEAR_MIN_VALUE = 1;
    public static final int BOOK_AVAILABLE_COPIES_MIN_VALUE = 0;
    public static final int REVIEW_RATING_MIN_VALUE = 1;
    public static final int REVIEW_RATING_MAX_VALUE = 5;
    public static final int REVIEW_COMMENT_MAX_LENGTH = 500;
}
