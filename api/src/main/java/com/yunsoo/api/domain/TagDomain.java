package com.yunsoo.api.domain;

import com.yunsoo.common.data.object.TagObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Admin on 6/8/2016.
 */
@Component
public class TagDomain {

    @Autowired
    private RestClient dataAPIClient;

    public TagObject getTag(String tagId) {
        return dataAPIClient.get("tag/" + tagId, new ParameterizedTypeReference<TagObject>() {
        });
    }

    public Page<TagObject> getTagList(String orgId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("tag" + query, new ParameterizedTypeReference<List<TagObject>>() {
        });
    }

    public void delete(String tagId) {
        dataAPIClient.delete("tag/" + tagId);
    }

    public TagObject create(TagObject tagObject) {
        return dataAPIClient.post("tag", tagObject, TagObject.class);
    }

    public void update(String tagId, TagObject tagObject) {
        dataAPIClient.patch("tag/" + tagId, tagObject, tagObject.getId());
    }
}
