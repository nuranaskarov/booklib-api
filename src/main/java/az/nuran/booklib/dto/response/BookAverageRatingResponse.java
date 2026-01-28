package az.nuran.booklib.dto.response;

public record BookAverageRatingResponse(
   String title,
   Double averageRating
) {}
