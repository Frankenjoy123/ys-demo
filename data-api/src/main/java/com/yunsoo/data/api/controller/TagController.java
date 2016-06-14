package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.TagObject;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.TagEntity;
import com.yunsoo.data.service.repository.TagRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public TagObject getById(@PathVariable(value = "id") String id) {

        TagEntity entity = tagRepository.findByDeletedFalseAndId(id);
        return toTagObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TagObject> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                       Pageable pageable,
                                       HttpServletResponse response) {

        Page<TagEntity> entityPage = tagRepository.findByDeletedFalseAndOrgId(orgId, pageable);

        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }
        return entityPage.getContent().stream()
                .map(this::toTagObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public TagObject create(@RequestBody @Valid TagObject object) {
        TagEntity entity = toTagEntity(object);
        entity.setId(null);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity.setModifiedAccountId(null);
        entity.setModifiedDateTime(null);

        TagEntity newEntity = tagRepository.save(entity);
        return toTagObject(newEntity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable("id") String id, @RequestBody TagObject object) {
        TagEntity entity = tagRepository.findOne(id);
        if (object.getName() != null) {
            entity.setName(object.getName());
        }
        if (object.getComments() != null) {
            entity.setComments(object.getComments());
        }
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime() == null ? DateTime.now() : object.getModifiedDateTime());
        tagRepository.save(entity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        TagEntity entity = tagRepository.findOne(id);
        if (entity != null) {
            entity.setDeleted(true);
            tagRepository.save(entity);
        }
    }

    private TagObject toTagObject(TagEntity entity) {
        if (entity == null) {
            return null;
        }

        TagObject tagObject = new TagObject();
        tagObject.setId(entity.getId());
        tagObject.setOrgId(entity.getOrgId());
        tagObject.setName(entity.getName());
        tagObject.setComments(entity.getComments());
        tagObject.setDeleted(entity.getDeleted());
        tagObject.setCreatedAccountId(entity.getCreatedAccountId());
        tagObject.setCreatedDateTime(entity.getCreatedDateTime());
        tagObject.setModifiedAccountId(entity.getModifiedAccountId());
        tagObject.setModifiedDateTime(entity.getModifiedDateTime());

        return tagObject;
    }

    private TagEntity toTagEntity(TagObject object) {
        if (object == null) {
            return null;
        }

        TagEntity entity = new TagEntity();
        entity.setId(object.getId());
        entity.setOrgId(object.getOrgId());
        entity.setName(object.getName());
        entity.setComments(object.getComments());
        entity.setDeleted(object.getDeleted());
        entity.setCreatedAccountId(object.getCreatedAccountId());
        entity.setCreatedDateTime(object.getCreatedDateTime());
        entity.setModifiedAccountId(object.getModifiedAccountId());
        entity.setModifiedDateTime(object.getModifiedDateTime());

        return entity;
    }
}
