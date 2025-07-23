package org.service;

import java.util.List;
import java.util.stream.Collectors;

import org.marker.Searchable;

public class SearchService<T extends Searchable> {
    public List<T> searchById(List<T> dataSource, int id) {
        return dataSource.stream()
                .filter(item -> item.getId() == id)
                .collect(Collectors.toList());
    }

    public List<T> searchByTitle(List<T> dataSource, String title) {
        return dataSource.stream()
                .filter(item -> item.getTitle().equals(title))
                .collect(Collectors.toList());
    }
}
