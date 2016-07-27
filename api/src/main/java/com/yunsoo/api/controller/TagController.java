package com.yunsoo.api.controller;

import com.yunsoo.api.domain.TagDomain;
import com.yunsoo.api.dto.Tag;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.object.TagObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagDomain tagDomain;

    @RequestMapping(value = "{tag_id}", method = RequestMethod.GET)
    public Tag getTag(@PathVariable(value = "tag_id") String tagId) {

        TagObject tag = tagDomain.getTag(tagId);
        return new Tag(tag);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Tag> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                 @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                 Pageable pageable,
                                 HttpServletResponse response) {

        orgId = AuthUtils.fixOrgId(orgId);
        Page<TagObject> tagPage = tagDomain.getTagList(orgId, pageable);

        return PageUtils.response(response, tagPage.map(Tag::new), pageable != null);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Tag create(@RequestBody Tag tag) {

        TagObject tagObject = new TagObject();
        String currentAccountId = AuthUtils.getCurrentAccount().getId();

        tagObject.setId(null);
        tagObject.setCreatedAccountId(currentAccountId);
        tagObject.setCreatedDateTime(DateTime.now());
        tagObject.setName(tag.getName());
        tagObject.setComments(tag.getComments());
        tagObject.setDeleted(false);

        if (StringUtils.hasText(tag.getOrgId()))
            tagObject.setOrgId(tag.getOrgId());
        else
            tagObject.setOrgId(AuthUtils.getCurrentAccount().getOrgId());

        TagObject newTagObject = tagDomain.create(tagObject);

        return new Tag(newTagObject);
    }

    @RequestMapping(value = "{tag_id}", method = RequestMethod.PATCH)
    public void update(@PathVariable(value = "tag_id") String tagId, @RequestBody Tag tag) {

        TagObject tagObject = tagDomain.getTag(tagId);
        if (tagObject == null) {
            throw new NotFoundException("tag not found by [id: " + tagId + "]");
        }

        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        tagObject.setModifiedAccountId(currentAccountId);
        tagObject.setModifiedDateTime(DateTime.now());
        tagObject.setName(tag.getName());
        tagObject.setComments(tag.getComments());

        tagDomain.update(tagId, tagObject);
    }

    @RequestMapping(value = "{tag_id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "tag_id") String tagId) {
        tagDomain.delete(tagId);
    }
}
