package com.rntgroup.repository.util;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchResult<T> {

    Page page;
    List<T> content = new ArrayList<>();

    public static <T> SearchResult<T> pack(List<T> originList, Page page) {
        int firstIndex = page.getSize() * (page.getNum() - 1);
        int lastIndex = Math.min(firstIndex + page.getSize(), originList.size());

        return new SearchResult<T>()
                .setPage(page)
                .setContent(originList.subList(firstIndex, lastIndex));
    }
}
