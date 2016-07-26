package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductCommentsDomain;
import com.yunsoo.api.dto.ProductComments;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.ProductCommentsObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by  : Haitao
 * Created on  : 2015/8/24
 * Descriptions:
 */

@RestController
@RequestMapping(value = "/productComments")
public class ProductCommentsController {

    @Autowired
    private ProductCommentsDomain productCommentsDomain;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductComments> getProductCommentsByFilter(@RequestParam(value = "product_base_id", required = false) String productBaseId,
                                                            @RequestParam(value = "score_ge", required = false) Integer scoreGE,
                                                            @RequestParam(value = "score_le", required = false) Integer scoreLE,
                                                            @RequestParam(value = "last_comment_datetime_ge", required = false)
                                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime lastCommentDatetimeGE,
                                                            @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                                            Pageable pageable,
                                                            HttpServletResponse response) {
//        if (productBaseId == null || productBaseId.isEmpty()) {
//            throw new BadRequestException("product base id is not valid");
//        }

        Page<ProductCommentsObject> productCommentsPage = productCommentsDomain.getProductCommentsByFilter(productBaseId, scoreGE, scoreLE, lastCommentDatetimeGE, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", productCommentsPage.toContentRange());
        }
        return productCommentsPage.map(ProductComments::new).getContent();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductComments getProductCommentsById(@PathVariable(value = "id") String id) {
        ProductCommentsObject object = productCommentsDomain.getById(id);
        if (object == null) {
            throw new NotFoundException("product comment not found by [id: " + id + "]");
        }
        return new ProductComments(object);
    }

    @RequestMapping(value = "/totalCount", method = RequestMethod.GET)
    public Long countByOrgId(@RequestParam(value = "org_id", required = false) String orgId) {
        orgId = AuthUtils.fixOrgId(orgId);
        if (orgId == null)
            throw new BadRequestException("org id can not be null");

        return productCommentsDomain.countProductCommentsByOrgId(orgId);
    }


    @RequestMapping(value = "/count/{id}", method = RequestMethod.GET)
    public Long getCommentsNumberByProductBaseId(@PathVariable(value = "id") String productBaseId) {
        return productCommentsDomain.getProductCommentsNumber(productBaseId);
    }

}
