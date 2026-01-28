package az.nuran.booklib.mapper;

import az.nuran.booklib.dto.request.CreateReviewRequest;
import az.nuran.booklib.entity.Book;
import az.nuran.booklib.entity.Review;
import az.nuran.booklib.dto.request.UpdateReviewRequest;
import az.nuran.booklib.dto.response.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReviewMapper {

    ReviewResponse toResponse(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", ignore = true)
    Review toEntity(CreateReviewRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", ignore = true)
    void patch(UpdateReviewRequest request, @MappingTarget Review review);
}
