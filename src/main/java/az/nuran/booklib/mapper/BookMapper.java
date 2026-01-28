package az.nuran.booklib.mapper;

import az.nuran.booklib.dto.request.CreateBookRequest;
import az.nuran.booklib.dto.request.UpdateBookRequest;
import az.nuran.booklib.dto.response.BookResponse;
import az.nuran.booklib.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookMapper {

    BookResponse toResponse(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    Book toEntity(CreateBookRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    void patch(UpdateBookRequest request, @MappingTarget Book book);
}
