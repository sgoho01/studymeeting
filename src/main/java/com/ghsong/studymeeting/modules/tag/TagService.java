package com.ghsong.studymeeting.modules.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author : song6
 * Date: 2020-06-14
 * Copyright(©) 2020
 */
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag findOrCreateNewTag(String tagTitle) {
        Tag tag = tagRepository.findByTitle(tagTitle).orElse(null);
        if (tag == null) {
            tag = tagRepository.save(Tag.builder().title(tagTitle).build());
        }
        return tag;
    }

}
