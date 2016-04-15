package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.domain.ProductBaseDomain;
import com.yunsoo.api.rabbit.domain.ProductCommentsDomain;
import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.dto.ProductComments;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductCommentsObject;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by  : Haitao
 * Created on  : 2015/8/21
 * Descriptions:
 */

@RestController
@RequestMapping(value = "/productcommentsweb")
public class ProductCommentsForWebController {

    @Autowired
    private ProductCommentsDomain productCommentsDomain;

    @Autowired
    private UserDomain userDomain;

    @Autowired
    private ProductBaseDomain productBaseDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ProductComments getProductCommentsById(@PathVariable(value = "id") String id) {
        ProductCommentsObject object = productCommentsDomain.getById(id);
        if (object == null) {
            throw new NotFoundException("product comment not found by [id: " + id + "]");
        }
        return new ProductComments(object);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProductComments> getProductCommentsByFilter(
            @RequestParam(value = "product_base_id", required = true) String productBaseId,
            @RequestParam(value = "score_ge", required = false) Integer scoreGE,
            @RequestParam(value = "score_le", required = false) Integer scoreLE,
            @RequestParam(value = "last_comment_datetime_ge", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime lastCommentDatetimeGE,
            @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
            @ApiIgnore Pageable pageable,
            HttpServletResponse response) {
        if (productBaseId == null || productBaseId.isEmpty()) {
            throw new BadRequestException("product base id is not valid");
        }

        Page<ProductCommentsObject> productCommentsPage = productCommentsDomain.getProductCommentsByFilter(productBaseId, scoreGE, scoreLE, lastCommentDatetimeGE, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", productCommentsPage.toContentRange());
        }
        List<ProductComments> productCommentsList = productCommentsPage.map(ProductComments::new).getContent();
        productCommentsList.forEach(productComments -> {
            UserObject uo = userDomain.getUserById(productComments.getUserId());
            productComments.setUserName(uo == null ? null : uo.getName());
        });

        return productCommentsList;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProductComments createProductComments(@RequestBody ProductComments productComments) {
        if (productComments == null) {
            throw new BadRequestException("productComments can not be null");
        }
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productComments.getProductBaseId());
        if (productBaseObject == null) {
            throw new BadRequestException("product base not found");
        }

        ProductCommentsObject productCommentsObject = productComments.toProductCommentsObject();
        productCommentsObject.setUserId(Constants.Ids.ANONYMOUS_USER_ID);
        productCommentsObject.setCreatedDateTime(DateTime.now());

        ProductCommentsObject newPCObject = productCommentsDomain.createProductComments(productCommentsObject);
        return new ProductComments(newPCObject);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductCommentsById(@PathVariable(value = "id") String id) {
        productCommentsDomain.deleteProductComments(id);
    }


    @RequestMapping(value = "count", method = RequestMethod.GET)
    public Long getCommentsCountByProductBaseId(@RequestParam(value = "product_base_id", required = true) String productBaseId) {
        return productCommentsDomain.getProductCommentsCount(productBaseId);
    }


}
